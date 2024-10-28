package SAES;

/**
 * KeyExpansion 类用于扩展初始密钥，生成一系列的子密钥。
 * 此类实现了一个简单的密钥扩展算法，其中包括S盒查找和轮常数的异或操作。
 */
public class KeyExpansion {
    // S盒数组，用于密钥扩展过程中的非线性变换
    private static final String[][] S_BOX = {
            {"9", "4", "A", "B"},
            {"D", "1", "8", "5"},
            {"6", "2", "0", "3"},
            {"C", "E", "F", "7"}
    };

    // 轮常数数组，用于密钥扩展过程中的轮密钥加
    private static final String[] RoundConstants = {"10000000", "00110000"};

    /**
     * 主方法，用于测试密钥扩展功能。
     * @param args 命令行参数（在此方法中未使用）。
     */
    public static void main(String[] args) {
        String baseKey = "3777";  // 初始4位十六进制密钥
        String[] derivedKeys = deriveKeys(baseKey);

        // 打印扩展后的密钥（二进制形式）
        for (int i = 0; i < derivedKeys.length; i++) {
            System.out.println("w" + i + ": " + derivedKeys[i]);
        }
    }

    /**
     * 扩展初始密钥，生成一系列的子密钥。
     * @param baseKey 初始密钥（4位十六进制字符串）。
     * @return 扩展后的子密钥数组（二进制字符串形式）。
     */
    public static String[] deriveKeys(String baseKey) {
        String w0 = baseKey.substring(0, 2);
        String w1 = baseKey.substring(2, 4);

        // 将十六进制转换为二进制
        w0 = hexadecimalToBinary(w0);
        w1 = hexadecimalToBinary(w1);
        // 根据描述进行密钥扩展逻辑
        String w2 = xor(xor(w0, RoundConstants[0]), g(w1));
        String w3 = xor(w2, w1);
        String w4 = xor(xor(w2, RoundConstants[1]), g(w3));
        String w5 = xor(w4, w3);

        // 返回扩展后的密钥数组（二进制字符串形式）
        return new String[]{w0, w1, w2, w3, w4, w5};
    }

    /**
     * 执行g函数变换，用于密钥扩展过程中的非线性变换。
     * @param w 二进制字符串形式的4位字。
     * @return 经过g函数变换后的二进制字符串。
     */
    public static String g(String w) {
        String N0 = w.substring(0, 4);
        String N1 = w.substring(4, 8);

        // 交换N0和N1
        String temp = N0;
        N0 = N1;
        N1 = temp;
        // 在S盒中查找
        N1 = sBoxQuery(N1);
        N0 = sBoxQuery(N0);

        // 通过连接变换后的N1和N0
        return N0 + N1;
    }

    /**
     * 在S盒中查找对应的值。
     * @param nibble 4位二进制字符串形式的半字节。
     * @return 查找后的S盒值（4位二进制字符串）。
     */
    public static String sBoxQuery(String nibble) {
        // 从半字节中解析行和列索引
        int row = Integer.parseInt(nibble.substring(0, 2), 2);
        int col = Integer.parseInt(nibble.substring(2, 4), 2);

        // 将S盒值从十六进制转换为二进制，并确保为4位二进制字符串
        String sBoxValue = S_BOX[row][col];
        return String.format("%4s", Integer.toBinaryString(Integer.parseInt(sBoxValue, 16)))
                .replace(' ', '0');
    }

    /**
     * 将十六进制字符串转换为二进制字符串。
     * @param hex 十六进制字符串。
     * @return 对应的二进制字符串。
     */
    public static String hexadecimalToBinary(String hex) {
        return String.format("%8s", Integer.toBinaryString(Integer.parseInt(hex, 16)))
                .replace(' ', '0');
    }

    /**
     * 执行异或操作。
     * @param a 一个二进制字符串。
     * @param b 另一个二进制字符串。
     * @return 异或操作的结果。
     */
    public static String xor(String a, String b) {
        StringBuilder xorResult = new StringBuilder();

        for (int i = 0; i < a.length(); i++) {
            xorResult.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }

        return xorResult.toString();
    }
}