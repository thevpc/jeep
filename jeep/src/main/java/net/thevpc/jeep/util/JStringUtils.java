package net.thevpc.jeep.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JStringUtils {
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean isBlank(CharSequence s) {
        return s == null || s.toString().trim().isEmpty();
    }

    public static String capitalize(String s) {
        boolean toCapital = true;
        StringBuilder mnamesb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == '_' || c == '-') {
                toCapital = true;
            } else if (Character.isUpperCase(c)) {
                mnamesb.append(c);
                toCapital = false;
            } else {
                if (toCapital) {
                    char c2 = Character.toUpperCase(c);
                    mnamesb.append(c2);
                    toCapital = false;
                } else {
                    mnamesb.append(Character.toLowerCase(c));
                }
            }
        }
        return mnamesb.toString();
    }

    public static String toSHA1(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        byte[] bytes = md.digest(str.getBytes());
        return toHexString(bytes);
    }

    public static String toHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
