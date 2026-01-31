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

package org.jscience.social.linguistics;

import java.util.*;

/**
 * Phonetic transcription to International Phonetic Alphabet (IPA).
 */
public final class PhoneticTranscriber {

    private PhoneticTranscriber() {}

    public enum Language {
        ENGLISH, FRENCH, GERMAN, SPANISH, ITALIAN
    }

    // English grapheme-to-phoneme rules (simplified)
    private static final Map<String, String> ENGLISH_IPA = Map.ofEntries(
        // Consonants
        Map.entry("th", "Î¸"), Map.entry("ch", "tÊƒ"), Map.entry("sh", "Êƒ"),
        Map.entry("ng", "Å‹"), Map.entry("ph", "f"), Map.entry("wh", "w"),
        Map.entry("ck", "k"), Map.entry("dg", "dÊ’"),
        // Single consonants
        Map.entry("b", "b"), Map.entry("c", "k"), Map.entry("d", "d"),
        Map.entry("f", "f"), Map.entry("g", "g"), Map.entry("h", "h"),
        Map.entry("j", "dÊ’"), Map.entry("k", "k"), Map.entry("l", "l"),
        Map.entry("m", "m"), Map.entry("n", "n"), Map.entry("p", "p"),
        Map.entry("q", "kw"), Map.entry("r", "É¹"), Map.entry("s", "s"),
        Map.entry("t", "t"), Map.entry("v", "v"), Map.entry("w", "w"),
        Map.entry("x", "ks"), Map.entry("y", "j"), Map.entry("z", "z"),
        // Vowels (simplified)
        Map.entry("ee", "iË"), Map.entry("ea", "iË"), Map.entry("oo", "uË"),
        Map.entry("ou", "aÊŠ"), Map.entry("ow", "aÊŠ"), Map.entry("ai", "eÉª"),
        Map.entry("ay", "eÉª"), Map.entry("oi", "É”Éª"), Map.entry("oy", "É”Éª"),
        Map.entry("a", "Ã¦"), Map.entry("e", "É›"), Map.entry("i", "Éª"),
        Map.entry("o", "É’"), Map.entry("u", "ÊŒ")
    );

    // French IPA rules
    private static final Map<String, String> FRENCH_IPA = Map.ofEntries(
        Map.entry("ou", "u"), Map.entry("au", "o"), Map.entry("eau", "o"),
        Map.entry("ai", "É›"), Map.entry("ei", "É›"), Map.entry("eu", "Ã¸"),
        Map.entry("oi", "wa"), Map.entry("on", "É”Ìƒ"), Map.entry("an", "É‘Ìƒ"),
        Map.entry("in", "É›Ìƒ"), Map.entry("un", "Å“Ìƒ"), Map.entry("ch", "Êƒ"),
        Map.entry("gn", "É²"), Map.entry("j", "Ê’"), Map.entry("r", "Ê"),
        Map.entry("c", "s"), Map.entry("Ã§", "s"),
        Map.entry("a", "a"), Map.entry("e", "É™"), Map.entry("i", "i"),
        Map.entry("o", "o"), Map.entry("u", "y")
    );

    /**
     * Transcribes text to IPA.
     */
    public static String transcribe(String text, Language language) {
        Map<String, String> rules = switch (language) {
            case ENGLISH -> ENGLISH_IPA;
            case FRENCH -> FRENCH_IPA;
            default -> ENGLISH_IPA;
        };
        
        StringBuilder result = new StringBuilder();
        String lower = text.toLowerCase();
        int i = 0;
        
        while (i < lower.length()) {
            boolean found = false;
            
            // Try longest matches first (3, 2, 1 characters)
            for (int len = 3; len >= 1 && !found; len--) {
                if (i + len <= lower.length()) {
                    String sub = lower.substring(i, i + len);
                    if (rules.containsKey(sub)) {
                        result.append(rules.get(sub));
                        i += len;
                        found = true;
                    }
                }
            }
            
            if (!found) {
                char c = lower.charAt(i);
                if (c == ' ') {
                    result.append(" ");
                } else if (Character.isLetter(c)) {
                    result.append(c); // Keep unknown letters
                }
                i++;
            }
        }
        
        return "/" + result.toString().trim() + "/";
    }

    /**
     * Gets IPA symbol description.
     */
    public static String describeSymbol(String ipaSymbol) {
        return switch (ipaSymbol) {
            case "Î¸" -> "voiceless dental fricative (English 'th' in 'think')";
            case "Ã°" -> "voiced dental fricative (English 'th' in 'this')";
            case "Êƒ" -> "voiceless postalveolar fricative (English 'sh')";
            case "Ê’" -> "voiced postalveolar fricative (French 'j')";
            case "tÊƒ" -> "voiceless postalveolar affricate (English 'ch')";
            case "dÊ’" -> "voiced postalveolar affricate (English 'j')";
            case "Å‹" -> "velar nasal (English 'ng')";
            case "É¹" -> "alveolar approximant (English 'r')";
            case "Ê" -> "voiced uvular fricative (French 'r')";
            case "É²" -> "palatal nasal (French 'gn')";
            case "iË" -> "close front unrounded vowel, long";
            case "Éª" -> "near-close near-front unrounded vowel";
            case "uË" -> "close back rounded vowel, long";
            case "ÊŠ" -> "near-close near-back rounded vowel";
            case "É™" -> "schwa (unstressed neutral vowel)";
            case "Ã¦" -> "near-open front unrounded vowel";
            case "É›" -> "open-mid front unrounded vowel";
            case "É”" -> "open-mid back rounded vowel";
            case "É‘" -> "open back unrounded vowel";
            default -> "IPA symbol: " + ipaSymbol;
        };
    }

    /**
     * Counts syllables in transcribed IPA.
     */
    public static int countSyllables(String ipaTranscription) {
        // Count vowel nuclei
        String vowels = "aeiouÉªÊŠÉ›É”Ã¦ÊŒÉ™É‘Ã¸yÅ“";
        String cleaned = ipaTranscription.replace("/", "").replace("Ë", "");
        
        int count = 0;
        boolean inVowel = false;
        
        for (char c : cleaned.toCharArray()) {
            boolean isVowel = vowels.indexOf(c) >= 0;
            if (isVowel && !inVowel) {
                count++;
            }
            inVowel = isVowel;
        }
        
        return Math.max(1, count);
    }
}

