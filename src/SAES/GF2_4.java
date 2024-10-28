package SAES;

public class GF2_4 {

    private static final int ModulusPolynomial = 0b10011;  // x^4 + x + 1


    public static int mult(int a, int b) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            if ((b & 1) == 1) {
                result ^= a;
            }
            boolean carry = (a & 0b1000) == 0b1000;
            a <<= 1;
            if (carry) {
                a ^= ModulusPolynomial;
            }
            b >>= 1;
        }
        return result;
    }
}

