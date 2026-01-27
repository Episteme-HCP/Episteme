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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Analytical tool for sentiment analysis and emotional polarity detection in text. 
 * It identifies positive, negative, and neutral tones using a lexicon-based 
 * approach with support for negations and intensifiers.
 */
public final class SentimentAnalyzer {

    private SentimentAnalyzer() {}

    /**
     * Qualitative classification of text sentiment.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Sentiment {
        VERY_POSITIVE, POSITIVE, NEUTRAL, NEGATIVE, VERY_NEGATIVE
    }

    /**
     * Result of a sentiment analysis operation.
     */
    public record SentimentResult(
        Sentiment sentiment,
        double score,       // Polarity score from -1.0 to 1.0
        double confidence,  // Confidence level from 0.0 to 1.0
        Map<String, Double> wordScores
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    // Lexicon of sentiment-bearing words
    private static final Map<String, Double> LEXICON = new HashMap<>();
    static {
        // Positive
        LEXICON.put("excellent", 0.9); LEXICON.put("amazing", 0.85);
        LEXICON.put("wonderful", 0.8); LEXICON.put("fantastic", 0.85);
        LEXICON.put("great", 0.7); LEXICON.put("good", 0.5);
        LEXICON.put("nice", 0.4); LEXICON.put("love", 0.8);
        LEXICON.put("happy", 0.7); LEXICON.put("beautiful", 0.6);
        LEXICON.put("perfect", 0.9); LEXICON.put("best", 0.8);
        LEXICON.put("brilliant", 0.8); LEXICON.put("awesome", 0.75);
        LEXICON.put("pleased", 0.6); LEXICON.put("delighted", 0.75);
        
        // Negative
        LEXICON.put("terrible", -0.9); LEXICON.put("awful", -0.85);
        LEXICON.put("horrible", -0.85); LEXICON.put("bad", -0.5);
        LEXICON.put("poor", -0.4); LEXICON.put("hate", -0.8);
        LEXICON.put("sad", -0.5); LEXICON.put("ugly", -0.6);
        LEXICON.put("worst", -0.9); LEXICON.put("disappointing", -0.6);
        LEXICON.put("frustrated", -0.6); LEXICON.put("angry", -0.7);
        LEXICON.put("annoyed", -0.5); LEXICON.put("boring", -0.4);
        LEXICON.put("dreadful", -0.8); LEXICON.put("disgusting", -0.85);
    }

    private static final Set<String> NEGATORS = Set.of(
        "not", "no", "never", "neither", "nobody", "nothing", 
        "nowhere", "hardly", "scarcely", "barely", "don't", "doesn't",
        "didn't", "won't", "wouldn't", "couldn't", "shouldn't", "isn't", "aren't"
    );

    private static final Set<String> INTENSIFIERS = Set.of(
        "very", "really", "extremely", "absolutely", "incredibly", 
        "exceptionally", "remarkably", "particularly", "especially"
    );

    /**
     * Performs sentiment analysis on a raw text string.
     * 
     * @param text the input text to analyze
     * @return a SentimentResult containing the polarity score and classification
     */
    public static SentimentResult analyze(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new SentimentResult(Sentiment.NEUTRAL, 0, 0, Collections.emptyMap());
        }
        
        String[] words = text.toLowerCase().split("\\s+");
        Map<String, Double> wordScores = new HashMap<>();
        
        double totalScore = 0;
        int scoredWords = 0;
        boolean negate = false;
        double intensifier = 1.0;
        
        for (String word : words) {
            String cleaned = word.replaceAll("[^a-z']", "");
            if (cleaned.isEmpty()) continue;
            
            if (NEGATORS.contains(cleaned)) {
                negate = true;
                continue;
            }
            if (INTENSIFIERS.contains(cleaned)) {
                intensifier = 1.5;
                continue;
            }
            
            if (LEXICON.containsKey(cleaned)) {
                double score = LEXICON.get(cleaned) * intensifier;
                if (negate) score = -score * 0.5;
                
                wordScores.put(cleaned, score);
                totalScore += score;
                scoredWords++;
            }
            
            negate = false;
            intensifier = 1.0;
        }
        
        double avgScore = scoredWords > 0 ? totalScore / scoredWords : 0;
        double normalizedScore = Math.max(-1, Math.min(1, avgScore));
        double confidence = Math.min(1.0, (double) scoredWords / Math.max(1, words.length) * 5);
        
        return new SentimentResult(classifySentiment(normalizedScore), normalizedScore, confidence, wordScores);
    }

    /**
     * Performs aspect-based sentiment analysis by targeting specific keywords.
     * 
     * @param text full text input
     * @param aspects list of keywords (aspects) to analyze separately
     * @return map of aspects to their corresponding SentimentResult
     */
    public static Map<String, SentimentResult> analyzeWithAspects(String text, List<String> aspects) {
        Map<String, SentimentResult> results = new HashMap<>();
        if (text == null || aspects == null) return results;
        
        String[] sentences = text.split("[.!?]+");
        for (String aspect : aspects) {
            StringBuilder relevantText = new StringBuilder();
            for (String sentence : sentences) {
                if (sentence.toLowerCase().contains(aspect.toLowerCase())) {
                    relevantText.append(sentence).append(" ");
                }
            }
            if (relevantText.length() > 0) {
                results.put(aspect, analyze(relevantText.toString()));
            } else {
                results.put(aspect, new SentimentResult(Sentiment.NEUTRAL, 0, 0, Collections.emptyMap()));
            }
        }
        return results;
    }

    /**
     * Calculates the numerical sentiment difference between two texts.
     * 
     * @param text1 first text
     * @param text2 second text
     * @return the difference in sentiment score (text1 - text2)
     */
    public static Real sentimentDifference(String text1, String text2) {
        double score1 = analyze(text1).score();
        double score2 = analyze(text2).score();
        return Real.of(score1 - score2);
    }

    private static Sentiment classifySentiment(double score) {
        if (score >= 0.5) return Sentiment.VERY_POSITIVE;
        if (score >= 0.2) return Sentiment.POSITIVE;
        if (score > -0.2) return Sentiment.NEUTRAL;
        if (score > -0.5) return Sentiment.NEGATIVE;
        return Sentiment.VERY_NEGATIVE;
    }
}
