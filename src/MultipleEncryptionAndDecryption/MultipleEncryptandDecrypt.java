package MultipleEncryptionAndDecryption;

import SAES.SAES_Decrypt;
import SAES.SAES_encrypt;

public class MultipleEncryptandDecrypt {

    /**
     * 使用两个密钥对原始文本进行双重加密。
     *
     * @param originalText 需要加密的原始文本。
     * @param encryptionKey 用于加密的密钥，前4位作为第一个密钥，后12位作为第二个密钥。
     * @return 加密后的文本。
     */
    public static String doubleEncrypt(String originalText, String encryptionKey) {
        // 取前4位作为第一个密钥
        String key1 = encryptionKey.substring(0, 4);
        // 取后12位作为第二个密钥
        String key2 = encryptionKey.substring(4);
        // 使用第一个密钥进行第一轮加密
        String firstEncryptionResult = SAES_encrypt.encryptText(originalText, key1);
        // 使用第二个密钥对第一轮加密的结果进行第二轮加密
        String doubleEncryptionResult = SAES_encrypt.encryptText(firstEncryptionResult, key2);
        return doubleEncryptionResult;
    }

    /**
     * 使用两个密钥对加密文本进行双重解密。
     *
     * @param encodedText 需要解密的加密文本。
     * @param decryptionKey 用于解密的密钥，前4位作为第一个密钥，后12位作为第二个密钥。
     * @return 解密后的原始文本。
     */
    public static String doubleDecrypt(String encodedText, String decryptionKey) {
        // 取前4位作为第一个密钥
        String key1 = decryptionKey.substring(0, 4);
        // 取后12位作为第二个密钥
        String key2 = decryptionKey.substring(4);
        // 使用第二个密钥进行第一轮解密
        String firstDecryptionResult = SAES_Decrypt.decryptBinary(encodedText, key2);
        // 使用第一个密钥对第一轮解密的结果进行第二轮解密
        String doubleDecryptionResult = SAES_Decrypt.decryptBinary(firstDecryptionResult, key1);
        return doubleDecryptionResult;
    }

    /**
     * 使用三个密钥对原始文本进行三重加密。
     *
     * @param originalText 需要加密的原始文本。
     * @param encryptionKey 用于加密的密钥，每4位分别作为三个不同的密钥。
     * @return 加密后的文本。
     */
    public static String tripleEncrypt(String originalText, String encryptionKey) {
        String key1 = encryptionKey.substring(0, 4);
        String key2 = encryptionKey.substring(4, 8);
        String key3 = encryptionKey.substring(8, 12);
        // 使用第一个密钥进行第一轮加密
        String firstEncryptionResult = SAES_encrypt.encryptText(originalText, key1);
        // 使用第二个密钥对第一轮加密的结果进行第二轮加密
        String secondEncryptionResult = SAES_encrypt.encryptText(firstEncryptionResult, key2);
        // 使用第三个密钥对第二轮加密的结果进行第三轮加密
        String tripleEncryptionResult = SAES_encrypt.encryptText(secondEncryptionResult, key3);
        return tripleEncryptionResult;
    }

    /**
     * 使用三个密钥对加密文本进行三重解密。
     *
     * @param encodedText 需要解密的加密文本。
     * @param decryptionKey 用于解密的密钥，每4位分别作为三个不同的密钥。
     * @return 解密后的原始文本。
     */
    public static String tripleDecrypt(String encodedText, String decryptionKey) {
        String key1 = decryptionKey.substring(0, 4);
        String key2 = decryptionKey.substring(4, 8);
        String key3 = decryptionKey.substring(8, 12);
        // 使用第三个密钥进行第一轮解密
        String firstRoundPlain = SAES_Decrypt.decryptBinary(encodedText, key3);
        // 使用第二个密钥对第一轮解密的结果进行第二轮解密
        String secondRoundPlain = SAES_Decrypt.decryptBinary(firstRoundPlain, key2);
        // 使用第一个密钥对第二轮解密的结果进行第三轮解密
        String triplePlain = SAES_Decrypt.decryptBinary(secondRoundPlain, key1);
        return triplePlain;
    }

    /**
     * 主方法，用于测试加密和解密功能。
     *
     * @param args 命令行参数。
     */
    public static void main(String[] args) {
        // 定义两个密钥和明文
        String key = "37773777";
        String originalText = "0000000000000011";
        String encodedText = "0011110011100000";

        // 调用doubleEncrypt方法测试双重加密
        String doubleencodedText = doubleEncrypt(originalText, key);
        System.out.println("双重加密结果如下：");
        System.out.println(doubleencodedText);  // 输出双重加密的密文
        // 调用doubleDecrypt方法测试双重解密
        String doubleDecryptedText = doubleDecrypt(encodedText, key);
        System.out.println("双重解密结果如下：");
        System.out.println(doubleDecryptedText);  // 输出双重解密的明文

        String keyt = "2D55A1F3B276";
        String tripleCiphertext = "1010101001111000";
        // 调用tripleEncrypt方法测试三重加密
        String tripleEncryptedText = tripleEncrypt(originalText, keyt);
        System.out.println("三重加密结果如下：");
        System.out.println(tripleEncryptedText);  // 输出三重加密的密文
        // 调用tripleDecrypt方法测试三重解密
        String tripleRecoveredText = tripleDecrypt(tripleCiphertext, keyt);
        System.out.println("三重解密结果如下：");
        System.out.println(tripleRecoveredText);  // 输出三重解密的明文
    }
}