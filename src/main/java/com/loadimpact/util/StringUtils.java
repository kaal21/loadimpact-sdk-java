package com.loadimpact.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility methods for String manipulation.
 *
 * @author jens
 */
public class StringUtils {

    /**
     * Returns true if the given string is null, empty or filled with spaces.
     * @param s     string to investigate
     * @return  true if blank
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * Makes the initial letter upper-case.
     * @param s the string
     * @return first letter is now in upper-case
     */
    public static String toInitialCase(String s) {
        if (isBlank(s)) return s;
        if (s.length() == 1) return s.toUpperCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * Returns true if the target string has a value and starts with the given prefix (null-safe).
     * @param target    string to investigate
     * @param prefix    substring to check for
     * @return true if prefix starts in target
     */
    public static boolean startsWith(Object target, String prefix) {
        if (target==null || prefix==null) return false;
        String targetTxt = target.toString();
        return targetTxt.length() > 0 && targetTxt.startsWith(prefix);
    }

    /**
     * Returns null if the given string is {@link #isBlank(String)}.
     * @param s the string
     * @return null is no content
     */
    public static String fixEmpty(String s) {
        if (isBlank(s)) return null;
        return s.trim();
    }

    /**
     * Replicates a string.
     * @param s         the string to replicate
     * @param times     number of times
     * @return combined string
     */
    public static String replicate(String s, int times) {
        if (s==null) return null;
        if (times <= 0 || s.length() == 0) return "";

        StringBuilder b = new StringBuilder(s.length() * times);
        for (int k = 1; k <= times; ++k) b.append(s);
        return b.toString();
    }

    /**
     * Computes a MD5 fingerprint of a text-string and returns as a HEX encoded string.
     * @param s     string to process
     * @return HEX encoded MD5 fingerprint
     */
    public static String md5(String s) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(s.getBytes(Charset.forName("UTF-8")));
            StringBuilder buf = new StringBuilder(2 * digest.length);
            for (byte oneByte : digest) {
                buf.append(Integer.toHexString((oneByte & 0xFF) | 0x100).substring(1, 3));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException ignore) { }
        return s;
    }

    /**
     * Creates a percentage ASCII bar.
     * @param percentage    value in [0, 100]
     * @return "[###......] nn%"
     */
    public static String percentageBar(double percentage) {
        final char  dot   = '.';
        final char  mark  = '#';
        final int   slots = 40;

        StringBuilder bar = new StringBuilder(replicate(String.valueOf(dot), slots));
        int numSlots = (int) (slots * percentage / 100.0);
        for (int k = 0; k < numSlots; ++k) bar.setCharAt(k, mark);

        return String.format("[%s] %3.0f%%", bar, percentage);
    }

    /**
     * Consumes the given input-stream and return the content as a string.
     * @param is    input-stream to read from
     * @return content as string
     */
    public static String toString(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder buf = new StringBuilder(4096);
            final String EOL = System.getProperty("line.separator", "\n");
            for (String line; (line = reader.readLine()) != null; ) {
                buf.append(line).append(EOL); 
            }
            return buf.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }
}
