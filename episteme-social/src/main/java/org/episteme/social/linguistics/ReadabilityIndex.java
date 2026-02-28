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

import java.io.Serializable;

/**
 * Utility tool for calculating various text readability indices. 
 * Supports standard linguistic formulas including Flesch, Gunning Fog, SMOG, 
 * Coleman-Liau, and the Automated Readability Index (ARI).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Readability">Readability (Wikipedia)</a>
 */
public final class ReadabilityIndex {

    private ReadabilityIndex() {}

    /**
     * Encapsulates the evaluation of a readability calculation.
     */
    public record ReadabilityResult(
        double score,
        String indexName,
        String gradeLevel,
        String interpretation
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Calculates the Flesch Reading Ease score.
     * Formula: 206.835 - 1.015 * (words/sentences) - 84.6 * (syllables/words)
     * 
     * @param text the input text to analyze
     * @return ReadabilityResult (higher score = easier to read)
     */
    public static ReadabilityResult fleschReadingEase(String text) {
        TextStats stats = analyzeText(text);
        
        double wordsPerSentence = (double) stats.words / Math.max(1, stats.sentences);
        double syllablesPerWord = (double) stats.syllables / Math.max(1, stats.words);
        
        double score = 206.835 - (1.015 * wordsPerSentence) - (84.6 * syllablesPerWord);
        score = Math.max(0, Math.min(100, score));
        
        String grade = getFleschGradeLevel(score);
        String interpretation = getFleschInterpretation(score);
        
        return new ReadabilityResult(score, "Flesch Reading Ease", grade, interpretation);
    }

    /**
     * Calculates the Flesch-Kincaid Grade Level.
     * Formula: 0.39 * (words/sentences) + 11.8 * (syllables/words) - 15.59
     * 
     * @param text input text
     * @return ReadabilityResult representing the school grade level
     */
    public static ReadabilityResult fleschKincaidGradeLevel(String text) {
        TextStats stats = analyzeText(text);
        
        double wordsPerSentence = (double) stats.words / Math.max(1, stats.sentences);
        double syllablesPerWord = (double) stats.syllables / Math.max(1, stats.words);
        
        double grade = (0.39 * wordsPerSentence) + (11.8 * syllablesPerWord) - 15.59;
        grade = Math.max(0, grade);
        
        String gradeLevel = String.format("Grade %.1f", grade);
        String interpretation = grade < 6 ? "Elementary" : 
            grade < 9 ? "Middle School" : 
            grade < 13 ? "High School" : "College";
        
        return new ReadabilityResult(grade, "Flesch-Kincaid Grade Level", gradeLevel, interpretation);
    }

    /**
     * Calculates the Gunning Fog Index.
     * Formula: 0.4 * ((words/sentences) + 100 * (complex words/words))
     * 
     * @param text input text
     * @return ReadabilityResult
     */
    public static ReadabilityResult gunningFogIndex(String text) {
        TextStats stats = analyzeText(text);
        
        double wordsPerSentence = (double) stats.words / Math.max(1, stats.sentences);
        double complexRatio = (double) stats.complexWords / Math.max(1, stats.words);
        
        double fog = 0.4 * (wordsPerSentence + 100 * complexRatio);
        
        String gradeLevel = String.format("Grade %.1f", fog);
        String interpretation = fog < 8 ? "Easy reading" :
            fog < 12 ? "Acceptable for most" :
            fog < 16 ? "Difficult" : "Very difficult";
        
        return new ReadabilityResult(fog, "Gunning Fog Index", gradeLevel, interpretation);
    }

    /**
     * Calculates the SMOG (Simple Measure of Gobbledygook) Index.
     * Formula: 1.043 * sqrt(30 * polysyllables/sentences) + 3.1291
     * 
     * @param text input text
     * @return ReadabilityResult
     */
    public static ReadabilityResult smogIndex(String text) {
        TextStats stats = analyzeText(text);
        
        double polysyllablesPerSentence = (double) stats.polysyllables / Math.max(1, stats.sentences);
        double smog = 1.043 * Math.sqrt(30 * polysyllablesPerSentence) + 3.1291;
        
        String gradeLevel = String.format("Grade %.1f", smog);
        String interpretation = "Years of education needed to understand";
        
        return new ReadabilityResult(smog, "SMOG Index", gradeLevel, interpretation);
    }

    /**
     * Calculates the Coleman-Liau Index based on character and sentence counts.
     * Formula: 0.0588 * L - 0.296 * S - 15.8
     * 
     * @param text input text
     * @return ReadabilityResult
     */
    public static ReadabilityResult colemanLiauIndex(String text) {
        TextStats stats = analyzeText(text);
        if (stats.words == 0) return new ReadabilityResult(0, "Coleman-Liau", "0", "N/A");
        
        double L = (double) stats.letters / stats.words * 100;
        double S = (double) stats.sentences / stats.words * 100;
        
        double cli = (0.0588 * L) - (0.296 * S) - 15.8;
        return new ReadabilityResult(cli, "Coleman-Liau Index", String.format("Grade %.1f", cli), "US grade level");
    }

    /**
     * Calculates the Automated Readability Index (ARI).
     * Formula: 4.71 * (characters/words) + 0.5 * (words/sentences) - 21.43
     * 
     * @param text input text
     * @return ReadabilityResult
     */
    public static ReadabilityResult automatedReadabilityIndex(String text) {
        TextStats stats = analyzeText(text);
        
        double charsPerWord = (double) stats.letters / Math.max(1, stats.words);
        double wordsPerSentence = (double) stats.words / Math.max(1, stats.sentences);
        
        double ari = (4.71 * charsPerWord) + (0.5 * wordsPerSentence) - 21.43;
        return new ReadabilityResult(ari, "Automated Readability Index", String.format("Grade %.1f", ari), "US grade level");
    }

    private record TextStats(
        int words, int sentences, int syllables, 
        int letters, int complexWords, int polysyllables
    ) {}

    private static TextStats analyzeText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new TextStats(0, 0, 0, 0, 0, 0);
        }
        
        String[] sentences = text.split("[.!?]+");
        String[] words = text.toLowerCase().split("\\s+");
        
        int totalSyllables = 0;
        int complexWords = 0;
        int polysyllables = 0;
        int letters = 0;
        int wordCount = 0;
        
        for (String word : words) {
            String cleaned = word.replaceAll("[^a-z]", "");
            if (cleaned.isEmpty()) continue;
            
            wordCount++;
            letters += cleaned.length();
            
            int syllables = countSyllables(cleaned);
            totalSyllables += syllables;
            
            if (syllables >= 3) {
                complexWords++;
                polysyllables++;
            }
        }
        
        return new TextStats(wordCount, sentences.length, totalSyllables, 
                           letters, complexWords, polysyllables);
    }

    private static int countSyllables(String word) {
        if (word == null || word.isEmpty()) return 0;
        
        word = word.toLowerCase().replaceAll("[^a-z]", "");
        if (word.isEmpty()) return 0;
        
        int count = 0;
        boolean lastWasVowel = false;
        String vowels = "aeiouy";
        
        for (int i = 0; i < word.length(); i++) {
            boolean isVowel = vowels.indexOf(word.charAt(i)) >= 0;
            if (isVowel && !lastWasVowel) {
                count++;
            }
            lastWasVowel = isVowel;
        }
        
        if (word.endsWith("e") && count > 1) {
            count--;
        }
        
        return Math.max(1, count);
    }

    private static String getFleschGradeLevel(double score) {
        if (score >= 90) return "5th grade";
        if (score >= 80) return "6th grade";
        if (score >= 70) return "7th grade";
        if (score >= 60) return "8th-9th grade";
        if (score >= 50) return "10th-12th grade";
        if (score >= 30) return "College";
        return "College graduate";
    }

    private static String getFleschInterpretation(double score) {
        if (score >= 90) return "Very easy to read";
        if (score >= 80) return "Easy to read";
        if (score >= 70) return "Fairly easy to read";
        if (score >= 60) return "Plain English";
        if (score >= 50) return "Fairly difficult to read";
        if (score >= 30) return "Difficult to read";
        return "Very difficult to read";
    }
}

