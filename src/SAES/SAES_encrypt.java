package SAES;

import java.io.IOException;

/**
 * SAES_encrypt 类实现了简化的高级加密标准（S-AES）算法，用于加密16位二进制数据。
 * S-AES是一种块加密算法，它使用简化的AES操作，包括轮密钥加、半字节替换、行位移和列混淆。
 */
public class SAES_encrypt {
    // S盒数组，用于半字节替换操作
    private static final String[][] S_BOX = {{"9", "4", "A", "B"}, {"D", "1", "8", "5"}, {"6", "2", "0", "3"}, {"C", "E", "F", "7"}};

    // 混淆矩阵，用于列混淆操作
    private static final int[][] MixMatrix = {
            {1, 4},
            {4, 1}
    };

    // GF2_4类实例，用于执行特定的算术运算
    private static final GF2_4 gf = new GF2_4();

    /**
     * 轮密钥加操作，将原始文本与密钥进行按位异或。
     * @param originalText 原始二进制文本
     * @param key 十六进制密钥
     * @return 按位异或后的二维矩阵
     */
    public static String[][] addRoundKeyOperation(String originalText, String key) {
        String binaryKey = hexadecimalToBinary(key, 16);  // 将16进制密钥转换为二进制
        String[][] stateMatrix = convertToMatrix(originalText);
        String[][] keyMatrix = convertToMatrix(binaryKey);
        return xorMatricesOperation(stateMatrix, keyMatrix);
    }

    /**
     * 第一轮加密操作，包括半字节替换、行位移、列混淆和轮密钥加。
     * @param inputMatrix 输入的二维矩阵
     * @param key 十六进制密钥
     * @return 加密后的二维矩阵
     */
    public static String[][] firstEncryptRound(String[][] inputMatrix, String key) {
        // 一：半字节替换
        String[][] substituteNibblesMatrix = substituteNibbles(inputMatrix);
        // 二：行位移
        String[][] shiftRowsOperationMatrix = shiftRowsOperation(substituteNibblesMatrix);
        // 三：列混淆
        String[][] mixColumnsOperationMatrix = mixColumnsOperation(shiftRowsOperationMatrix);
        // 四：轮密钥加
        String[][] keyMatrix = convertToMatrix(key);
        String[][] xorMatricesOperationMatrix = xorMatricesOperation(mixColumnsOperationMatrix, keyMatrix);
        return xorMatricesOperationMatrix;
    }

    /**
     * 第二轮加密操作，包括半字节替换、行位移和轮密钥加。
     * @param inputMatrix 输入的二维矩阵
     * @param key 十六进制密钥
     * @return 加密后的二维矩阵
     */
    public static String[][] secondEncryptRound(String[][] inputMatrix, String key) {
        // 半字节替换
        String[][] substituteNibblesMatrix = substituteNibbles(inputMatrix);
        // 行位移
        String[][] shiftRowsOperationMatrix = shiftRowsOperation(substituteNibblesMatrix);
        // 轮密钥加
        String[][] keyMatrix = convertToMatrix(key);
        String[][] xorMatricesOperationMatrix = xorMatricesOperation(shiftRowsOperationMatrix, keyMatrix);
        return xorMatricesOperationMatrix;
    }

    /**
     * 半字节替换操作，使用S盒替换矩阵中的每个半字节。
     * @param matrix 输入的二维矩阵
     * @return 替换后的二维矩阵
     */
    public static String[][] substituteNibbles(String[][] matrix) {
        String[][] result = new String[2][2];
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                String nibble = matrix[row][col];
                int rowIndex = Integer.parseInt(nibble.substring(0, 2), 2);
                int colIndex = Integer.parseInt(nibble.substring(2, 4), 2);
                // 直接从S_BOX中获取值
                result[row][col] = hexadecimalToBinary(S_BOX[rowIndex][colIndex], 4);
            }
        }
        return result;
    }

    /**
     * 行位移操作，将第一行保持不变，第二行循环右移一位。
     * @param matrix 输入的二维矩阵
     * @return 行位移后的二维矩阵
     */
    public static String[][] shiftRowsOperation(String[][] matrix) {
        String temp = matrix[1][0];
        matrix[1][0] = matrix[1][1];
        matrix[1][1] = temp;
        return matrix;
    }

    /**
     * 列混淆操作，使用混淆矩阵对列进行混淆。
     * @param matrix 输入的二维矩阵
     * @return 混淆后的二维矩阵
     */
    public static String[][] mixColumnsOperation(String[][] matrix) {
        String[][] result = new String[2][2];
        for (int i = 0; i < 2; i++) {  // 对于每列
            // 确保每个半字节都是4位长
            String upperNibble = String.format("%4s", matrix[0][i]).replace(' ', '0');
            String lowerNibble = String.format("%4s", matrix[1][i]).replace(' ', '0');
            // 将二进制字符串转换为整数
            int upperValue = Integer.parseInt(upperNibble, 2);
            int lowerValue = Integer.parseInt(lowerNibble, 2);
            // 执行列混淆运算
            int resultUpperValue = GF2_4.mult(MixMatrix[0][0], upperValue)
                    ^ GF2_4.mult(MixMatrix[0][1], lowerValue);
            int resultLowerValue = GF2_4.mult(MixMatrix[1][0], upperValue)
                    ^ GF2_4.mult(MixMatrix[1][1], lowerValue);
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

    /**
     * 使用S-AES算法加密ASCII字符串。
     * @param originalTextASCII 要加密的ASCII文本
     * @param baseKey 加密的初始密钥
     * @return ASCII格式的加密文本
     */
    public static String encryptASCII(String originalTextASCII, String baseKey) {
        StringBuilder encodedTextBuilder = new StringBuilder();
        for (int i = 0; i < originalTextASCII.length(); i += 2) {
            // 获取下一个16位的块（2个字符）
            String block = originalTextASCII.substring(i, Math.min(i + 2, originalTextASCII.length()));
            // 将字符转换为二进制
            String binaryBlock = asciiToBinary(block);
            // 加密二进制块
            String encryptedBinaryBlock = encryptText(binaryBlock, baseKey);
            // 将加密的二进制块转换回ASCII
            String encryptedBlock = binaryToAscii(encryptedBinaryBlock);
            encodedTextBuilder.append(encryptedBlock);
        }
        return encodedTextBuilder.toString();
    }

    /**
     * 使用S-AES算法加密16位二进制文本。
     * @param originalText 待加密的二进制文本
     * @param baseKey 加密的初始密钥
     * @return 二进制格式的加密文本
     */
    public static String encryptText(String originalText, String baseKey) {
        // 密钥扩展
        String[] derivedKeys = KeyExpansion.deriveKeys(baseKey);
        String Key1 = derivedKeys[2] + derivedKeys[3];
        String Key2 = derivedKeys[4] + derivedKeys[5];
        // 轮密钥加
        String[][] initialMatrix = addRoundKeyOperation(originalText, baseKey);
        // 第一轮
        String[][] firstEncryptRoundResult = firstEncryptRound(initialMatrix, Key1);
        // 第二轮
        String[][] encodedText = secondEncryptRound(firstEncryptRoundResult, Key2);
        return printMatrixContent(encodedText);  // 返回字符串格式的密文
    }

    /**
     * 主方法，用于测试SAES_encrypt类的加密功能。
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        String baseKey = "3777";  // 初始16位密钥

        String originalText = "0110101110100011";
        String encodedText = encryptText(originalText, baseKey);
        System.out.println("加密结果如下：");
        System.out.println(encodedText); // 输出密文结果

        String originalTextASCII = "wpk in software engineer and this is a test txt file"; // 输入String类型的ASCII
        String encodedTextASCII = encryptASCII(originalTextASCII, baseKey);
        System.out.println("加密结果如下：");
        System.out.println(encodedTextASCII); // 输出ASCII格式的加密文本
    }
}