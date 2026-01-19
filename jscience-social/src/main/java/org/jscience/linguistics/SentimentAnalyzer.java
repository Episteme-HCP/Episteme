package org.jscience.linguistics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Sentiment analysis for text polarity detection.
 */
public final class SentimentAnalyzer {

    private SentimentAnalyzer() {}

    public enum Sentiment {
        VERY_POSITIVE, POSITIVE, NEUTRAL, NEGATIVE, VERY_NEGATIVE
    }

    public record SentimentResult(
        Sentiment sentiment,
        double score,       // -1 to 1
        double confidence,  // 0 to 1
        Map<String, Double> wordScores
    ) {}

    // Simplified sentiment lexicon
    private static final Map<String, Double> LEXICON = new HashMap<>();
    static {
        // Positive words
        LEXICON.put("excellent", 0.9); LEXICON.put("amazing", 0.85);
        LEXICON.put("wonderful", 0.8); LEXICON.put("fantastic", 0.85);
        LEXICON.put("great", 0.7); LEXICON.put("good", 0.5);
        LEXICON.put("nice", 0.4); LEXICON.put("love", 0.8);
        LEXICON.put("happy", 0.7); LEXICON.put("beautiful", 0.6);
        LEXICON.put("perfect", 0.9); LEXICON.put("best", 0.8);
        LEXICON.put("brilliant", 0.8); LEXICON.put("awesome", 0.75);
        LEXICON.put("pleased", 0.6); LEXICON.put("delighted", 0.75);
        
        // Negative words
        LEXICON.put("terrible", -0.9); LEXICON.put("awful", -0.85);
        LEXICON.put("horrible", -0.85); LEXICON.put("bad", -0.5);
        LEXICON.put("poor", -0.4); LEXICON.put("hate", -0.8);
        LEXICON.put("sad", -0.5); LEXICON.put("ugly", -0.6);
        LEXICON.put("worst", -0.9); LEXICON.put("disappointing", -0.6);
        LEXICON.put("frustrated", -0.6); LEXICON.put("angry", -0.7);
        LEXICON.put("annoyed", -0.5); LEXICON.put("boring", -0.4);
        LEXICON.put("dreadful", -0.8); LEXICON.put("disgusting", -0.85);
        
        // Intensifiers
        LEXICON.put("very", 0.0); LEXICON.put("really", 0.0);
        LEXICON.put("extremely", 0.0); LEXICON.put("absolutely", 0.0);
        
        // Negators (handled separately)
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
     * Analyzes sentiment of text.
     */
    public static SentimentResult analyze(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        Map<String, Double> wordScores = new HashMap<>();
        
        double totalScore = 0;
        int scoredWords = 0;
        boolean negate = false;
        double intensifier = 1.0;
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i].replaceAll("[^a-z']", "");
            
            // Check for negation
            if (NEGATORS.contains(word)) {
                negate = true;
                continue;
            }
            
            // Check for intensifier
            if (INTENSIFIERS.contains(word)) {
                intensifier = 1.5;
                continue;
            }
            
            // Score the word
            if (LEXICON.containsKey(word)) {
                double score = LEXICON.get(word) * intensifier;
                if (negate) {
                    score = -score * 0.5; // Negation partially inverses
                }
                
                wordScores.put(word, score);
                totalScore += score;
                scoredWords++;
            }
            
            // Reset modifiers after scored word
            negate = false;
            intensifier = 1.0;
        }
        
        // Calculate final score
        double avgScore = scoredWords > 0 ? totalScore / scoredWords : 0;
        double normalizedScore = Math.max(-1, Math.min(1, avgScore));
        
        // Calculate confidence based on coverage
        double coverage = (double) scoredWords / words.length;
        double confidence = Math.min(1.0, coverage * 2);
        
        Sentiment sentiment = classifySentiment(normalizedScore);
        
        return new SentimentResult(sentiment, normalizedScore, confidence, wordScores);
    }

    /**
     * Analyzes sentiment with aspect extraction.
     */
    public static Map<String, SentimentResult> analyzeWithAspects(String text, List<String> aspects) {
        Map<String, SentimentResult> results = new HashMap<>();
        
        // Split into sentences
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
                results.put(aspect, new SentimentResult(
                    Sentiment.NEUTRAL, 0, 0, Collections.emptyMap()
                ));
            }
        }
        
        return results;
    }

    /**
     * Compares sentiment between two texts.
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
