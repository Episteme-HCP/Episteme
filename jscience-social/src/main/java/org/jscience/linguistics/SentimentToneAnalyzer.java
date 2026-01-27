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

package org.jscience.linguistics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Analyzes emotional tone and sentiment of text using keyword valences.
 */
public final class SentimentToneAnalyzer {

    private SentimentToneAnalyzer() {}

    public record SentimentScore(
        double positivity,
        double negativity,
        double subjectivity,
        String dominantTone
    ) {}

    private static final Map<String, Double> LEXICON = Map.of(
        "excellent", 1.0, "good", 0.5, "happy", 0.6,
        "bad", -0.5, "terrible", -1.0, "sad", -0.6,
        "think", 0.1, "believe", 0.2, "feel", 0.3
    );

    /**
     * Analyzes sentiment based on a simple weighted lexicon.
     */
    public static SentimentScore analyze(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        double pos = 0, neg = 0, subj = 0;
        int matches = 0;

        for (String w : words) {
            String clean = w.replaceAll("[^a-z]", "");
            if (LEXICON.containsKey(clean)) {
                double val = LEXICON.get(clean);
                if (val > 0) pos += val;
                else neg += Math.abs(val);
                
                // Subjectivity keywords
                if (clean.matches("think|believe|feel|opinion")) subj += 0.5;
                matches++;
            }
        }

        double total = Math.max(1, matches);
        String tone = (pos > neg) ? "POSITIVE" : (neg > pos) ? "NEGATIVE" : "NEUTRAL";
        
        return new SentimentScore(pos/total, neg/total, subj/total, tone);
    }

    /**
     * Measures VAD (Valence, Arousal, Dominance) simplified.
     */
    public static Real valenceScore(String text) {
        SentimentScore score = analyze(text);
        return Real.of(score.positivity() - score.negativity());
    }
}
