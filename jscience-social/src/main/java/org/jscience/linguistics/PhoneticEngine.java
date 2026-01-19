package org.jscience.linguistics;

import java.util.*;

/**
 * Engine for converting text to phonetic representations (IPA).
 */
public final class PhoneticEngine {

    private PhoneticEngine() {}

    private static final Map<String, String> ENGLISH_MAP = Map.ofEntries(
        Map.entry("th", "θ"),
        Map.entry("ch", "tʃ"),
        Map.entry("sh", "ʃ"),
        Map.entry("ee", "i:"),
        Map.entry("oo", "u:"),
        Map.entry("a", "æ"),
        Map.entry("e", "ɛ"),
        Map.entry("i", "ɪ"),
        Map.entry("o", "ɒ"),
        Map.entry("u", "ʌ"),
        Map.entry("f", "f"),
        Map.entry("v", "v"),
        Map.entry("s", "s"),
        Map.entry("z", "z")
    );

    /**
     * Simplified rule-based phonetic transcriber for English.
     */
    public static String transcribeEnglish(String text) {
        StringBuilder sb = new StringBuilder("/");
        String low = text.toLowerCase();
        
        int i = 0;
        while (i < low.length()) {
            boolean matched = false;
            // Check double chars
            if (i + 1 < low.length()) {
                String pair = low.substring(i, i + 2);
                if (ENGLISH_MAP.containsKey(pair)) {
                    sb.append(ENGLISH_MAP.get(pair));
                    i += 2;
                    matched = true;
                }
            }
            
            if (!matched) {
                String sing = low.substring(i, i + 1);
                sb.append(ENGLISH_MAP.getOrDefault(sing, sing));
                i++;
            }
        }
        sb.append("/");
        return sb.toString();
    }

    /**
     * Calculates Soundex code for string comparison.
     */
    public static String soundex(String s) {
        if (s == null || s.isEmpty()) return "";
        char[] code = {'0', '0', '0', '0'};
        code[0] = Character.toUpperCase(s.charAt(0));
        
        int count = 1;
        for (int i = 1; i < s.length() && count < 4; i++) {
            char c = getSoundexMapping(s.charAt(i));
            if (c != '0' && c != code[count - 1]) {
                code[count++] = c;
            }
        }
        return new String(code);
    }

    private static char getSoundexMapping(char c) {
        c = Character.toUpperCase(c);
        if ("BFPV".indexOf(c) >= 0) return '1';
        if ("CGJKQSXZ".indexOf(c) >= 0) return '2';
        if ("DT".indexOf(c) >= 0) return '3';
        if ("L".indexOf(c) >= 0) return '4';
        if ("MN".indexOf(c) >= 0) return '5';
        if ("R".indexOf(c) >= 0) return '6';
        return '0';
    }
}
