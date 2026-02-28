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

package org.episteme.social.linguistics.braille;

import java.util.HashMap;
import java.util.Map;

/**
 * Modern Braille translation system using Unicode Braille Patterns (U+2800).
 * Supports Grade 1 (literal) translation for basic alphanumeric text.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class BrailleSystem {

    private static final Map<Character, Character> GRADE1_MAP = new HashMap<>();

    static {
        // Base letters a-z (Grade 1) mapped to Unicode Braille Pattern hex codes
        GRADE1_MAP.put('a', '\u2801'); // 1
        GRADE1_MAP.put('b', '\u2803'); // 1-2
        GRADE1_MAP.put('c', '\u2809'); // 1-4
        GRADE1_MAP.put('d', '\u2819'); // 1-4-5
        GRADE1_MAP.put('e', '\u2811'); // 1-5
        GRADE1_MAP.put('f', '\u280B'); // 1-2-4
        GRADE1_MAP.put('g', '\u281B'); // 1-2-4-5
        GRADE1_MAP.put('h', '\u2813'); // 1-2-5
        GRADE1_MAP.put('i', '\u280A'); // 2-4
        GRADE1_MAP.put('j', '\u281A'); // 2-4-5
        GRADE1_MAP.put('k', '\u2805'); // 1-3
        GRADE1_MAP.put('l', '\u2807'); // 1-2-3
        GRADE1_MAP.put('m', '\u280D'); // 1-3-4
        GRADE1_MAP.put('n', '\u281D'); // 1-3-4-5
        GRADE1_MAP.put('o', '\u2815'); // 1-3-5
        GRADE1_MAP.put('p', '\u280F'); // 1-2-3-4
        GRADE1_MAP.put('q', '\u281F'); // 1-2-3-4-5
        GRADE1_MAP.put('r', '\u2817'); // 1-2-3-5
        GRADE1_MAP.put('s', '\u280E'); // 2-3-4
        GRADE1_MAP.put('t', '\u281E'); // 2-3-4-5
        GRADE1_MAP.put('u', '\u2825'); // 1-3-6
        GRADE1_MAP.put('v', '\u2827'); // 1-2-3-6
        GRADE1_MAP.put('w', '\u283A'); // 2-4-5-6
        GRADE1_MAP.put('x', '\u282D'); // 1-3-4-6
        GRADE1_MAP.put('y', '\u283D'); // 1-3-4-5-6
        GRADE1_MAP.put('z', '\u2835'); // 1-3-5-6
        GRADE1_MAP.put(' ', '\u2800'); // empty
    }

    private BrailleSystem() {}

    /**
     * Translates a string to Grade 1 Unicode Braille.
     */
    public static String translate(String text) {
        if (text == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : text.toLowerCase().toCharArray()) {
            sb.append(GRADE1_MAP.getOrDefault(c, c));
        }
        return sb.toString();
    }

    /**
     * Checks if a character is a valid Unicode Braille pattern.
     */
    public static boolean isBraillePattern(char c) {
        return c >= '\u2800' && c <= '\u28FF';
    }
}

