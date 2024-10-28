package SAES;

import java.io.IOException;

/**
 * SAES_Decrypt 类实现了简化的高级加密标准（S-AES）算法的解密过程。
 * 类似于 SAES_encrypt，它使用逆S盒替换、逆行位移、逆列混淆和轮密钥加操作来解密数据。
 */
public class SAES_Decrypt {
    // 逆S盒数组，用于逆半字节替换操作
    private static final String[][] InverseSBox = {
            {"A", "5", "9", "B"},
            {"1", "7", "8", "F"},
            {"6", "0", "2", "3"},
            {"C", "4", "D", "E"}
    };

    // 逆混淆矩阵，用于逆列混淆操作
    private static final int[][] InverseMixMatrix = {
            {9, 2},
            {2, 9}
    };

    // GF2_4类实例，用于执行特定的算术运算
    private static final GF2_4 gf = new GF2_4();

    /**
     * 主方法，用于测试SAES_Decrypt类的解密功能。
     * @param args 命令行参数
     */
    public static void main(String[] args) throws IOException {
        String baseKey = "3777";  // 初始16位密钥

        String ciphertext = "1011100011100001";
        String decryptedText = decryptBinary(ciphertext, baseKey);
        System.out.println("解密结果如下：");
        System.out.println(decryptedText); // 输出解密结果

        String encodedTextASCII = "\u008Có\u008Có\u0005\u008DTäTä0B´¶\u0087¡"; // ASCII编码的加密文本
        String recoveredTextASCII = decryptAsciiText(encodedTextASCII, baseKey);
        System.out.println("解密结果如下：");
        System.out.println(recoveredTextASCII); // 输出解密结果
    }

    /**
     * 使用简化的 AES 算法解密ASCII字符串。
     * @param encodedTextASCII 要解密的ASCII编码的加密文本
     * @param baseKey 解密的初始密钥
     * @return ASCII格式的解密文本
     */
    public static String decryptAsciiText(String encodedTextASCII, String baseKey) {
        StringBuilder decryptedTextBuilder = new StringBuilder();
        for (int i = 0; i < encodedTextASCII.length(); i += 2) {
            // 获取下一个16位的块（2个字符）
            String block = encodedTextASCII.substring(i, Math.min(i + 2, encodedTextASCII.length()));
            // 将字符转换为二进制
            String binaryBlock = asciiToBinary(block);
            // 解密二进制块
            String decryptedBinaryBlock = decryptBinary(binaryBlock, baseKey);
            // 将解密的二进制块转换回ASCII
            String decryptedBlock = binaryToAscii(decryptedBinaryBlock);
            decryptedTextBuilder.append(decryptedBlock);
        }
        return decryptedTextBuilder.toString();
    }

    /**
     * 使用简化的 AES 算法解密16位二进制文本。
     * @param encodedText 待解密二进制文本
     * @param baseKey 解密的初始密钥
     * @return 二进制格式的解密文本
     */
    public static String decryptBinary(String encodedText, String baseKey) {
        String[] derivedKeys = KeyExpansion.deriveKeys(baseKey);  // 密钥扩展
        String roundOneKey = derivedKeys[4] + derivedKeys[5]; // 第一轮解密密钥
        String roundTwoKey = derivedKeys[2] + derivedKeys[3]; // 第二轮解密密钥
        // 第一步：轮密钥加
        String[][] preRoundOneMatrix = addRoundKey(encodedText, roundOneKey);
        // 第二步：第一轮解密
        String[][] afterRoundOneMatrix = firstDecryptRound(preRoundOneMatrix, roundTwoKey);
        // 第三步：第二轮解密
        String[][] decryptedMatrix = secondDecryptRound(afterRoundOneMatrix, hexadecimalToBinary(baseKey, 16));
        return printMatrixContent(decryptedMatrix);  // 返回字符串格式的解密文本
    }

    /**
     * 轮密钥加操作，将密文与密钥进行按位异或。
     * @param encodedText 密文
     * @param key 十六进制密钥
     * @return 按位异或后的二维矩阵
     */
    public static String[][] addRoundKey(String encodedText, String key) {
        String[][] stateMatrix = convertToMatrix(encodedText);
        String[][] keyMatrix = convertToMatrix(key);
        return xorMatricesOperation(stateMatrix, keyMatrix);
    }

    /**
     * 第一轮解密轮函数，包括逆行位移、逆半字节替换、轮密钥加和逆列混淆。
     * @param inputMatrix 输入的二维矩阵
     * @param key 十六进制密钥
     * @return 第一轮解密后的二维矩阵
     */
    public static String[][] firstDecryptRound(String[][] inputMatrix, String key) {
        // 1. 逆行位移操作
        String[][] invShiftRowsMatrix = inverseRowShift(inputMatrix);
        // 2. 逆半字节代替操作
        String[][] invSubNibblesMatrix = inverseByteSubstitution(invShiftRowsMatrix);
        // 3. 轮密钥加操作
        String[][] keyMatrix = convertToMatrix(key);
        String[][] xorMatricesOperationMatrix = xorMatricesOperation(invSubNibblesMatrix, keyMatrix);
        // 4. 逆列混淆操作
        String[][] invMixColumnsMatrix = inverseColumnMix(xorMatricesOperationMatrix);
        return invMixColumnsMatrix;
    }

    /**
     * 第二轮解密轮函数，包括逆行位移、逆半字节替换和轮密钥加。
     * @param inputMatrix 输入的二维矩阵
     * @param key 十六进制密钥
     * @return 第二轮解密后的二维矩阵
     */
    public static String[][] secondDecryptRound(String[][] inputMatrix, String key) {
        // 1. 逆行位移操作
        String[][] invShiftRowsMatrix = inverseRowShift(inputMatrix);
        // 2. 逆半字节代替操作
        String[][] invSubNibblesMatrix = inverseByteSubstitution(invShiftRowsMatrix);
        // 3. 轮密钥加操作
        String[][] keyMatrix = convertToMatrix(key);
        String[][] xorMatricesOperationMatrix = xorMatricesOperation(invSubNibblesMatrix, keyMatrix);
        return xorMatricesOperationMatrix;
    }

    /**
     * 逆行位移操作，将第一行保持不变，第二行循环左移一位。
     * @param matrix 输入的二维矩阵
     * @return 逆行位移后的二维矩阵
     */
    public static String[][] inverseRowShift(String[][] matrix) {
        String temp = matrix[1][0];
        matrix[1][0] = matrix[1][1];
        matrix[1][1] = temp;
        return matrix;
    }

    /**
     * 逆半字节代替操作，使用逆S盒替换矩阵中的每个半字节。
     * @param matrix 输入的二维矩阵
     * @return 逆半字节替换后的二维矩阵
     */
    public static String[][] inverseByteSubstitution(String[][] matrix) {
        String[][] result = new String[2][2];
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                String nibble = matrix[row][col];
                int rowIndex = Integer.parseInt(nibble.substring(0, 2), 2);
                int colIndex = Integer.parseInt(nibble.substring(2, 4), 2);
                // 直接从逆S盒中获取值
                result[row][col] = hexadecimalToBinary(InverseSBox[rowIndex][colIndex], 4);
            }
        }
        return result;
    }

    /**
     * 逆列混淆操作，使用逆混淆矩阵对列进行混淆。
     * @param matrix 输入的二维矩阵
     * @return 逆列混淆后的二维矩阵
     */
    public static String[][] inverseColumnMix(String[][] matrix) {
        String[][] result = new String[2][2];
        for (int i = 0; i < 2; i++) {  // 对于每列
            // 确保每个半字节都是4位长
            String upperNibble = String.format("%4s", matrix[0][i]).replace(' ', '0');
            String lowerNibble = String.format("%4s", matrix[1][i]).replace(' ', '0');
            // 将二进制字符串转换为整数
            int upperValue = Integer.parseInt(upperNibble, 2);
            int lowerValue = Integer.parseInt(lowerNibble, 2);
            // 执行逆列混淆运算
            int resultUpperValue = gf.mult(InverseMixMatrix[0][0], upperValue)
                    ^ gf.mult(InverseMixMatrix[0][1], lowerValue);
            int             resultLowerValue = gf.mult(InverseMixMatrix[1][0], upperValue)
                    ^ gf.mult(InverseMixMatrix[1][1], lowerValue);
            // 将结果转换回二进制字符串并存储在结果矩阵中
            result[0][i] = String.format("%4s", Integer.toBinaryString(resultUpperValue)).replace(' ', '0');
            result[1][i] = String.format("%4s", Integer.toBinaryString(resultLowerValue)).replace(' ', '0');
        }
        return result;
    }

    /**
     * 将十六进制字符串转换为二进制字符串。
     * @param hex 十六进制字符串
     * @param bitLength 目标二进制字符串的长度
     * @return 转换后的二进制字符串
     */
    public static String hexadecimalToBinary(String hex, int bitLength) {
        return String.format("%" + bitLength + "s", Integer.toBinaryString(Integer.parseInt(hex, 16)))
                .replace(' ', '0');
    }

    /**
     * 将输入的二进制字符串转换为2x2的二维矩阵。
     * @param input 二进制字符串
     * @return 转换后的二维矩阵
     */
    public static String[][] convertToMatrix(String input) {
        return new String[][]{
                {input.substring(0, 4), input.substring(8, 12)},
                {input.substring(4, 8), input.substring(12, 16)}
        };
    }

    /**
     * 执行按位异或操作。
     * @param matrix1 第一个二维矩阵
     * @param matrix2 第二个二维矩阵
     * @return 异或操作后的二维矩阵
     */
    public static String[][] xorMatricesOperation(String[][] matrix1, String[][] matrix2) {
        String[][] result = new String[2][2];
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                result[row][col] = xor(matrix1[row][col], matrix2[row][col]);
            }
        }
        return result;
    }

    /**
     * 执行异或操作。
     * @param a 第一个二进制字符串
     * @param b 第二个二进制字符串
     * @return 异或操作后的二进制字符串
     */
    public static String xor(String a, String b) {
        StringBuilder xorResult = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            xorResult.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return xorResult.toString();
    }

    /**
     * 打印2x2矩阵的内容。
     * @param matrix 二维矩阵
     * @return 矩阵内容的字符串表示
     */
    public static String printMatrixContent(String[][] matrix) {
        StringBuilder result = new StringBuilder();
        for (int col = 0; col < 2; col++) {  // 先遍历列
            for (int row = 0; row < 2; row++) {  // 再遍历行
                result.append(matrix[row][col]);
            }
        }
        return result.toString();
    }

    /**
     * 将ASCII字符串转换为二进制形式。
     * @param ascii ASCII字符串
     * @return ASCII字符串的二进制表示
     */
    public static String asciiToBinary(String ascii) {
        StringBuilder binary = new StringBuilder();
        for (char ch : ascii.toCharArray()) {
            binary.append(String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0'));
        }
        return binary.toString();
    }

    /**
     * 将二进制字符串转换为ASCII形式。
     * @param binary 二进制字符串
     * @return 二进制字符串的ASCII表示
     */
    public static String binaryToAscii(String binary) {
        StringBuilder ascii = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String byteStr = binary.substring(i, i + 8);
            int charCode = Integer.parseInt(byteStr, 2);
            ascii.append((char) charCode);
        }
        return ascii.toString();
    }
}