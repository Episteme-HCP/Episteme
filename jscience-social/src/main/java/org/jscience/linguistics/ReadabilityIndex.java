package org.jscience.linguistics;


/**
 * Calculates text readability indices.
 */
public final class ReadabilityIndex {

    private ReadabilityIndex() {}

    public record ReadabilityResult(
        double score,
        String indexName,
        String gradeLevel,
        String interpretation
    ) {}

    /**
     * Flesch Reading Ease score.
     * 206.835 - 1.015 × (words/sentences) - 84.6 × (syllables/words)
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
     * Flesch-Kincaid Grade Level.
     * 0.39 × (words/sentences) + 11.8 × (syllables/words) - 15.59
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
     * Gunning Fog Index.
     * 0.4 × ((words/sentences) + 100 × (complex words/words))
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
     * SMOG (Simple Measure of Gobbledygook) Index.
     * 1.043 × sqrt(30 × polysyllables/sentences) + 3.1291
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
     * Coleman-Liau Index.
     * 0.0588 × L - 0.296 × S - 15.8
     * where L = avg letters per 100 words, S = avg sentences per 100 words
     */
    public static ReadabilityResult colemanLiauIndex(String text) {
        TextStats stats = analyzeText(text);
        
        double L = (double) stats.letters / stats.words * 100;
        double S = (double) stats.sentences / stats.words * 100;
        
        double cli = (0.0588 * L) - (0.296 * S) - 15.8;
        
        String gradeLevel = String.format("Grade %.1f", cli);
        
        return new ReadabilityResult(cli, "Coleman-Liau Index", gradeLevel, "US grade level");
    }

    /**
     * Automated Readability Index (ARI).
     * 4.71 × (characters/words) + 0.5 × (words/sentences) - 21.43
     */
    public static ReadabilityResult automatedReadabilityIndex(String text) {
        TextStats stats = analyzeText(text);
        
        double charsPerWord = (double) stats.letters / Math.max(1, stats.words);
        double wordsPerSentence = (double) stats.words / Math.max(1, stats.sentences);
        
        double ari = (4.71 * charsPerWord) + (0.5 * wordsPerSentence) - 21.43;
        
        String gradeLevel = String.format("Grade %.1f", ari);
        
        return new ReadabilityResult(ari, "Automated Readability Index", gradeLevel, "US grade level");
    }

    private record TextStats(
        int words, int sentences, int syllables, 
        int letters, int complexWords, int polysyllables
    ) {}

    private static TextStats analyzeText(String text) {
        String[] sentences = text.split("[.!?]+");
        String[] words = text.toLowerCase().split("\\s+");
        
        int totalSyllables = 0;
        int complexWords = 0;
        int polysyllables = 0;
        int letters = 0;
        
        for (String word : words) {
            String cleaned = word.replaceAll("[^a-z]", "");
            letters += cleaned.length();
            
            int syllables = countSyllables(cleaned);
            totalSyllables += syllables;
            
            if (syllables >= 3) {
                complexWords++;
                polysyllables++;
            }
        }
        
        return new TextStats(words.length, sentences.length, totalSyllables, 
                           letters, complexWords, polysyllables);
    }

    private static int countSyllables(String word) {
        if (word.isEmpty()) return 0;
        
        // Simple English syllable counting heuristic
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
        
        // Adjust for silent e
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
