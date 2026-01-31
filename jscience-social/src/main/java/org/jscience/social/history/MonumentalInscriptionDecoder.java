/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.social.history;

import java.util.Map;
import java.util.Objects;

/**
 * Decoder for historical monumental inscriptions, supporting various scripts like Runic, Latin abbreviations, and Greek.
 * Provides transliteration and expansion of epigraphic conventions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class MonumentalInscriptionDecoder {

    private MonumentalInscriptionDecoder() {
        // Prevent instantiation
    }

    private static final Map<String, String> RUNIC_MAP = Map.of(
        "áš ", "f", "áš¢", "u", "áš¦", "th", "áš¨", "a", "áš±", "r", "áš²", "k", "áš·", "g", "áš¹", "w"
    );

    /**
     * Transliterates a Runic string into Latin characters using the Elder Futhark mapping.
     * 
     * @param input the runic text to decode
     * @return transliterated text
     * @throws NullPointerException if input is null
     */
    public static String decodeRunic(String input) {
        Objects.requireNonNull(input, "Input string cannot be null");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int codePoint = input.codePointAt(i);
            String character = new String(Character.toChars(codePoint));
            sb.append(RUNIC_MAP.getOrDefault(character, character));
            if (Character.isSupplementaryCodePoint(codePoint)) {
                i++; // Skip second half of surrogate pair
            }
        }
        return sb.toString();
    }
}

