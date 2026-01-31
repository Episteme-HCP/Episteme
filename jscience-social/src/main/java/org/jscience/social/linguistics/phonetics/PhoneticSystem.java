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

package org.jscience.social.linguistics.phonetics;

import java.util.Map;
import java.util.HashMap;

/**
 * Modern structural representation of phonetic systems.
 * Focuses on IPA (International Phonetic Alphabet) and articulatory features.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class PhoneticSystem {

    /**
     * Represents a single phoneme with articulatory features.
     */
    public record IPAChar(
        String symbol,
        String name,
        Category category,
        Map<String, String> features
    ) {
        public enum Category { VOWEL, CONSONANT, DIACRITIC, SUPRASEGMENTAL }
    }

    private static final Map<String, IPAChar> SYMBOLS = new HashMap<>();

    static {
        // Consonants (Pulmonic) - subset for demonstration
        register(new IPAChar("p", "Voiceless bilabial plosive", IPAChar.Category.CONSONANT, Map.of("voicing", "voiceless", "place", "bilabial", "manner", "plosive")));
        register(new IPAChar("b", "Voiced bilabial plosive", IPAChar.Category.CONSONANT, Map.of("voicing", "voiced", "place", "bilabial", "manner", "plosive")));
        register(new IPAChar("t", "Voiceless alveolar plosive", IPAChar.Category.CONSONANT, Map.of("voicing", "voiceless", "place", "alveolar", "manner", "plosive")));
        register(new IPAChar("d", "Voiced alveolar plosive", IPAChar.Category.CONSONANT, Map.of("voicing", "voiced", "place", "alveolar", "manner", "plosive")));
        register(new IPAChar("k", "Voiceless velar plosive", IPAChar.Category.CONSONANT, Map.of("voicing", "voiceless", "place", "velar", "manner", "plosive")));
        register(new IPAChar("É¡", "Voiced velar plosive", IPAChar.Category.CONSONANT, Map.of("voicing", "voiced", "place", "velar", "manner", "plosive")));
        
        // Vowels - subset
        register(new IPAChar("a", "Open front unrounded vowel", IPAChar.Category.VOWEL, Map.of("height", "open", "backness", "front", "roundedness", "unrounded")));
        register(new IPAChar("e", "Close-mid front unrounded vowel", IPAChar.Category.VOWEL, Map.of("height", "close-mid", "backness", "front", "roundedness", "unrounded")));
        register(new IPAChar("i", "Close front unrounded vowel", IPAChar.Category.VOWEL, Map.of("height", "close", "backness", "front", "roundedness", "unrounded")));
        register(new IPAChar("o", "Close-mid back rounded vowel", IPAChar.Category.VOWEL, Map.of("height", "close-mid", "backness", "back", "roundedness", "rounded")));
        register(new IPAChar("u", "Close back rounded vowel", IPAChar.Category.VOWEL, Map.of("height", "close", "backness", "back", "roundedness", "rounded")));
    }

    private static void register(IPAChar c) {
        SYMBOLS.put(c.symbol(), c);
    }

    public static IPAChar getIPA(String symbol) {
        return SYMBOLS.get(symbol);
    }

    /**
     * Calculates phonetic similarity based on feature overlap.
     */
    public static double calculateSimilarity(IPAChar c1, IPAChar c2) {
        if (c1 == null || c2 == null) return 0.0;
        if (c1.category() != c2.category()) return 0.0;
        
        long matches = c1.features().entrySet().stream()
            .filter(e -> e.getValue().equals(c2.features().get(e.getKey())))
            .count();
            
        return (double) matches / c1.features().size();
    }
}

