package CBCMode;
import SAES.SAES_encrypt;
import SAES.SAES_Decrypt;
/**
 * CBC 类实现了密码分组链（Cipher Block Chaining, CBC）模式的加密和解密。
 * 在CBC模式中，每个明文块在加密前会与前一个密文块进行按位异或操作，初始块与初始向量异或。
 * 解密时，每个密文块在解密后会与前一个密文块进行按位异或操作，以还原明文。
 */
public class CBC {

    /**
     * 使用密码分组链模式加密明文。
     * @param plaintext 待加密的明文
     * @param initialVector 初始向量
     * @param key 加密密钥
     * @return 加密后的密文
     */
    public static String CBCModeEncrypt(String plaintext, String initialVector, String key) {
        StringBuilder encryptedTextBuilder = new StringBuilder();
        String previousCiphertextBlock = initialVector;  // 初始向量作为第一块的"前一块密文"

        for (int i = 0; i < plaintext.length(); i += 16) {
            // 获取下一个16位的块
            String block = plaintext.substring(i, i + 16);
            // 将当前块与前一块的密文（或初始向量）进行按位异或操作
            String xorResult = xor(block, previousCiphertextBlock);
            // 使用密钥加密异或结果
            String encryptedBlock = SAES_encrypt.encryptText(xorResult, key);
            // 将加密块添加到加密文本构建器中
            encryptedTextBuilder.append(encryptedBlock);
            // 更新"前一块密文"以供下一块使用
            previousCiphertextBlock = encryptedBlock;
        }

        return encryptedTextBuilder.toString();
    }

    /**
     * 使用密码分组链模式解密密文。
     * @param ciphertext 待解密的密文
     * @param initialVector 初始向量
     * @param key 解密密钥
     * @return 解密后的明文
     */
    public static String CBCModeDecrypt(String ciphertext, String initialVector, String key) {
        StringBuilder decryptedTextBuilder = new StringBuilder();
        String previousCiphertextBlock = initialVector;  // 初始向量作为第一块的"前一块密文"

        for (int i = 0; i < ciphertext.length(); i += 16) {
            // 获取下一个16位的块
            String block = ciphertext.substring(i, i + 16);
            // 使用密钥解密当前块
            String decryptedBlock = SAES_Decrypt.decryptBinary(block, key);
            // 将解密的块与前一块的密文（或初始向量）进行按位异或操作
            String xorResult = xor(decryptedBlock, previousCiphertextBlock);
            // 将解密的块添加到解密文本构建器中
            decryptedTextBuilder.append(xorResult);
            // 更新"前一块密文"以供下一块使用
            previousCiphertextBlock = block;
        }

        return decryptedTextBuilder.toString();
    }

    /**
     * 执行按位异或操作。
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
     * 主方法，用于测试CBC类的加密和解密功能。
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        String plaintext = "0000000011111111111111110000000011111111111111110000000000001010";
        String initialVector = "1010101010101010";  // 协商好的16位初始向量
        String key = "3777";  // 16位密钥

        String encryptedTextforDecrypt = "0100000110100001001100000010000110001011111100100110101001010111";
        String decryptedText = CBCModeDecrypt(encryptedTextforDecrypt, initialVector, key);
        System.out.println("解密结果如下：");
        System.out.println(decryptedText);  // 输出解密结果

        String encryptedText = CBCModeEncrypt(plaintext, initialVector, key);
        System.out.println("加密结果如下：");
        System.out.println(encryptedText);  // 输出加密结果
    }
}