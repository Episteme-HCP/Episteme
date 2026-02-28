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

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Optional;

/**
 * Utility class for detecting the language of text samples using statistical methods.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class LanguageDetector {

    private LanguageDetector() {}

    public record DetectionResult(
        Language detectedLanguage,
        double confidence,
        Map<Language, Double> allScores
    ) {}

    private static final Map<String, Set<String>> LANGUAGE_TRIGRAMS = Map.of(
        "en", Set.of("the", "and", "ing", "tion", "her", "hat", "his", "tha"),
        "fr", Set.of("les", "ent", "ait", "que", "ion", "tio", "ons", "eur", "des", "est"),
        "de", Set.of("der", "die", "und", "den", "ein", "sch", "ich", "cht"),
        "es", Set.of("que", "ent", "ade", "cion", "los", "del", "las", "con")
    );

    private static final Map<String, Set<String>> LANGUAGE_WORDS = Map.of(
        "en", Set.of("the", "a", "is", "it", "of", "and", "to", "in"),
        "fr", Set.of("le", "la", "les", "de", "et", "est", "un", "une"),
        "de", Set.of("der", "die", "das", "und", "ist", "in", "zu", "den"),
        "es", Set.of("el", "la", "de", "que", "y", "en", "un", "es")
    );

    public static DetectionResult detect(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new DetectionResult(null, 0.0, Map.of());
        }

        String cleaned = text.toLowerCase().replaceAll("[^a-zÃ Ã¢Ã¤Ã©Ã¨ÃªÃ«Ã¯Ã®Ã´Ã¹Ã»Ã¼Ã¿Å“Ã¦Ã§Ã±Ã¡Ã­Ã³Ãº ]", " ");
        String[] words = cleaned.split("\\s+");
        
        Map<Language, Double> scores = new HashMap<>();
        
        for (String iso : LANGUAGE_WORDS.keySet()) {
            Optional<Language> langOpt = Languages.get(iso);
            if (langOpt.isEmpty()) continue;
            Language lang = langOpt.get();

            double wordScore = 0;
            for (String word : words) {
                if (LANGUAGE_WORDS.get(iso).contains(word)) wordScore++;
            }
            scores.put(lang, wordScore / words.length);
        }
        
        List<String> trigrams = extractTrigrams(cleaned);
        for (String iso : LANGUAGE_TRIGRAMS.keySet()) {
            Optional<Language> langOpt = Languages.get(iso);
            if (langOpt.isEmpty()) continue;
            Language lang = langOpt.get();

            double trigramScore = 0;
            for (String trigram : trigrams) {
                if (LANGUAGE_TRIGRAMS.get(iso).contains(trigram)) trigramScore++;
            }
            double normalized = trigrams.isEmpty() ? 0 : trigramScore / trigrams.size();
            scores.merge(lang, normalized, (a, b) -> a + b);
        }
        
        double total = scores.values().stream().mapToDouble(Double::doubleValue).sum();
        if (total > 0) scores.replaceAll((k, v) -> v / total);
        
        Language best = scores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
        
        return new DetectionResult(best, best == null ? 0.0 : scores.get(best), scores);
    }

    private static List<String> extractTrigrams(String text) {
        List<String> trigrams = new ArrayList<>();
        String cleaned = text.replaceAll("\\s+", " ").trim();
        for (int i = 0; i < cleaned.length() - 2; i++) {
            String trigram = cleaned.substring(i, i + 3);
            if (!trigram.contains(" ")) trigrams.add(trigram);
        }
        return trigrams;
    }
}

