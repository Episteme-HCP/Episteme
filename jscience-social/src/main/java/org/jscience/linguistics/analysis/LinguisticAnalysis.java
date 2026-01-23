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

package org.jscience.linguistics.analysis;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides modern NLP (Natural Language Processing) analysis capabilities.
 * Includes tokenization, n-gram extraction, and readability metrics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class LinguisticAnalysis {

    private LinguisticAnalysis() {}

    /**
     * Splits text into individual word tokens.
     */
    public static List<String> tokenize(String text) {
        if (text == null) return Collections.emptyList();
        return Arrays.stream(text.toLowerCase().split("\\W+"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Extracts N-grams from a list of tokens.
     */
    public static List<String> extractNGrams(List<String> tokens, int n) {
        List<String> nGrams = new ArrayList<>();
        for (int i = 0; i <= tokens.size() - n; i++) {
            nGrams.add(String.join(" ", tokens.subList(i, i + n)));
        }
        return nGrams;
    }

    /**
     * Calculates the Flesch Reading Ease score.
     * Score = 206.835 - 1.015 * (total_words / total_sentences) - 84.6 * (total_syllables / total_words)
     */
    public static double fleschReadingEase(String text) {
        List<String> words = tokenize(text);
        if (words.isEmpty()) return 0.0;
        
        long sentenceCount = Math.max(1, text.split("[.!?]+").length);
        long syllableCount = words.stream().mapToLong(LinguisticAnalysis::countSyllables).sum();

        double asl = (double) words.size() / sentenceCount;
        double asw = (double) syllableCount / words.size();

        return 206.835 - (1.015 * asl) - (84.6 * asw);
    }

    /**
     * Simple heuristic for counting syllables in English.
     */
    public static int countSyllables(String word) {
        word = word.toLowerCase();
        if (word.length() <= 3) return 1;
        word = word.replaceAll("e$", "");
        String[] vowels = {"a", "e", "i", "o", "u", "y"};
        int count = 0;
        boolean lastWasVowel = false;
        for (char c : word.toCharArray()) {
            boolean isVowel = false;
            for (String v : vowels) {
                if (String.valueOf(c).equals(v)) {
                    isVowel = true;
                    break;
                }
            }
            if (isVowel && !lastWasVowel) count++;
            lastWasVowel = isVowel;
        }
        return Math.max(1, count);
    }
}
