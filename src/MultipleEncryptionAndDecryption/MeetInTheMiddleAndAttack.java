package MultipleEncryptionAndDecryption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import SAES.SAES_Decrypt;
import SAES.SAES_encrypt;
import java.util.ArrayList;
import java.util.List;
import static SAES.SAES_Decrypt.decryptBinary;
import static SAES.SAES_encrypt.encryptText;

import java.util.*;
public class MeetInTheMiddleAndAttack {
    public static List<String> attack(String originalText, String encodedText) {
        String[] Mid1 = new String[65536];
        String[] Mid2 = new String[65536];
        List<String> keys = new ArrayList<>();

        for (int i = 0; i < 65536; i++) {
            String hexKey1 = Integer.toHexString(i);
            while (hexKey1.length() < 4) {
                hexKey1 = "0" + hexKey1;
            }
            Mid1[i] = encryptText(originalText, hexKey1);
        }

        for (int j = 0; j < 65536; j++) {
            String hexKey2 = Integer.toHexString(j);
            while (hexKey2.length() < 4) {
                hexKey2 = "0" + hexKey2;
            }
            Mid2[j] = decryptBinary(encodedText, hexKey2);
        }

        for (int i = 0; i < Mid1.length; i++) {
            for (int j = 0; j < Mid2.length; j++) {
                if (Mid1[i] != null && Mid1[i].equals(Mid2[j])) {
                    String key1 = Integer.toHexString(i).toUpperCase();
                    String key2 = Integer.toHexString(j).toUpperCase();
                    while (key1.length() < 4) {
                        key1 = "0" + key1;
                    }
                    while (key2.length() < 4) {
                        key2 = "0" + key2;
                    }
                    keys.add(key1 +key2);
                }
            }
        }

        return keys;
    }

    public static void main(String[] args) {
        String originalText = "0000000000000000";
        String encodedText = "0011011011010010";
        List<String> keys = attack(originalText, encodedText);
        for (String key : keys) {
            System.out.println("Found key: " + key);
        }
    }
}