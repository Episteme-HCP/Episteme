/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.social.linguistics;

import java.util.*;

/**
 * Engine for converting text to phonetic representations (IPA).
 */
public final class PhoneticEngine {

    private PhoneticEngine() {}

    private static final Map<String, String> ENGLISH_MAP = Map.ofEntries(
        Map.entry("th", "Î¸"),
        Map.entry("ch", "tÊƒ"),
        Map.entry("sh", "Êƒ"),
        Map.entry("ee", "i:"),
        Map.entry("oo", "u:"),
        Map.entry("a", "Ã¦"),
        Map.entry("e", "É›"),
        Map.entry("i", "Éª"),
        Map.entry("o", "É’"),
        Map.entry("u", "ÊŒ"),
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

