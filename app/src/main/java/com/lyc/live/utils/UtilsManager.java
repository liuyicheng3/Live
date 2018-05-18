package com.lyc.live.utils;

/**
 * Created by lyc on 18/5/18.
 */

public class UtilsManager {
    public static String getMD5(byte[] bytes) {
        return bytesToHexString(bytes);
    }

    /**
     * Converts a byte array into a String hexidecimal characters
     *
     * null returns null
     */
    private static String bytesToHexString(byte[] bytes) {
        if (bytes == null)
            return null;
        String table = "0123456789abcdef";
        StringBuilder ret = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            int b;
            b = 0x0f & (bytes[i] >> 4);
            ret.append(table.charAt(b));
            b = 0x0f & bytes[i];
            ret.append(table.charAt(b));
        }
        return ret.toString();
    }
}
