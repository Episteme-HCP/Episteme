package org.jscience.linguistics;

import java.util.*;

/**
 * Language detection from text samples.
 */
public final class LanguageDetector {

    private LanguageDetector() {}

    public record DetectionResult(
        String detectedLanguage,
        double confidence,
        Map<String, Double> allScores
    ) {}

    // Common trigram patterns for various languages
    private static final Map<String, Set<String>> LANGUAGE_TRIGRAMS = Map.of(
        "English", Set.of("the", "and", "ing", "tion", "her", "hat", "his", "tha", 
            "ere", "for", "ent", "ion", "ter", "was", "you", "ith", "ver", "all"),
        "French", Set.of("les", "ent", "ait", "que", "ion", "tio", "ons", "eur", 
            "des", "est", "ous", "ant", "men", "eme", "par", "our", "com", "ais"),
        "German", Set.of("der", "die", "und", "den", "ein", "sch", "ich", "cht", 
            "che", "gen", "ber", "ung", "eit", "ver", "auf", "ste", "ent", "ach"),
        "Spanish", Set.of("que", "ent", "ade", "cion", "los", "del", "las", "con",
            "ion", "par", "ero", "nte", "est", "tra", "mente", "aba", "ado", "ida"),
        "Italian", Set.of("che", "lla", "ell", "ion", "ent", "ato", "per", "tta",
            "zio", "nte", "con", "gli", "del", "ono", "ere", "ale", "ess", "ita"),
        "Portuguese", Set.of("que", "ent", "ade", "cao", "oes", "par", "com", "era",
            "ava", "nte", "men", "con", "est", "ida", "ais", "dos", "ado", "mos")
    );

    // Common words for each language
    private static final Map<String, Set<String>> LANGUAGE_WORDS = Map.of(
        "English", Set.of("the", "a", "is", "it", "of", "and", "to", "in", "that", "you"),
        "French", Set.of("le", "la", "les", "de", "et", "est", "un", "une", "que", "en"),
        "German", Set.of("der", "die", "das", "und", "ist", "in", "zu", "den", "mit", "von"),
        "Spanish", Set.of("el", "la", "de", "que", "y", "en", "un", "es", "se", "no"),
        "Italian", Set.of("il", "la", "di", "che", "e", "un", "a", "in", "non", "per"),
        "Portuguese", Set.of("o", "a", "de", "que", "e", "em", "um", "para", "com", "nao")
    );

    /**
     * Detects the language of a text sample.
     */
    public static DetectionResult detect(String text) {
        String cleaned = text.toLowerCase().replaceAll("[^a-záàâäãéèêëíìîïóòôöõúùûüñç ]", " ");
        String[] words = cleaned.split("\\s+");
        
        Map<String, Double> scores = new HashMap<>();
        
        // Score based on common words
        for (String language : LANGUAGE_WORDS.keySet()) {
            double wordScore = 0;
            for (String word : words) {
                if (LANGUAGE_WORDS.get(language).contains(word)) {
                    wordScore++;
                }
            }
            scores.put(language, wordScore / words.length);
        }
        
        // Score based on trigrams
        List<String> trigrams = extractTrigrams(cleaned);
        for (String language : LANGUAGE_TRIGRAMS.keySet()) {
            double trigramScore = 0;
            for (String trigram : trigrams) {
                if (LANGUAGE_TRIGRAMS.get(language).contains(trigram)) {
                    trigramScore++;
                }
            }
            double normalized = trigrams.isEmpty() ? 0 : trigramScore / trigrams.size();
            scores.merge(language, normalized, Double::sum);
        }
        
        // Normalize scores
        double total = scores.values().stream().mapToDouble(Double::doubleValue).sum();
        if (total > 0) {
            scores.replaceAll((k, v) -> v / total);
        }
        
        // Find best match
        String best = scores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Unknown");
        
        double confidence = scores.getOrDefault(best, 0.0);
        
        return new DetectionResult(best, confidence, scores);
    }

    /**
     * Detects if text is multilingual.
     */
    public static boolean isMultilingual(String text) {
        // Split into sentences
        String[] sentences = text.split("[.!?]+");
        
        Set<String> detectedLanguages = new HashSet<>();
        for (String sentence : sentences) {
            if (sentence.trim().length() > 20) {
                DetectionResult result = detect(sentence);
                if (result.confidence() > 0.3) {
                    detectedLanguages.add(result.detectedLanguage());
                }
            }
        }
        
        return detectedLanguages.size() > 1;
    }

    /**
     * Gets language family.
     */
    public static String getLanguageFamily(String language) {
        return switch (language.toLowerCase()) {
            case "english", "german", "dutch" -> "Germanic";
            case "french", "spanish", "italian", "portuguese", "romanian" -> "Romance";
            case "russian", "polish", "czech", "ukrainian" -> "Slavic";
            case "hindi", "punjabi", "bengali" -> "Indo-Aryan";
            case "chinese", "mandarin", "cantonese" -> "Sinitic";
            case "arabic", "hebrew" -> "Semitic";
            case "japanese" -> "Japonic";
            case "korean" -> "Koreanic";
            default -> "Unknown";
        };
    }

    /**
     * Checks for specific language features.
     */
    public static Map<String, Boolean> checkLanguageFeatures(String text) {
        Map<String, Boolean> features = new HashMap<>();
        
        features.put("Has diacritics", text.matches(".*[áàâäãéèêëíìîïóòôöõúùûüñç].*"));
        features.put("Has Cyrillic", text.matches(".*[а-яА-ЯёЁ].*"));
        features.put("Has CJK", text.matches(".*[\\u4e00-\\u9fff].*"));
        features.put("Has Arabic", text.matches(".*[\\u0600-\\u06FF].*"));
        features.put("Has Hebrew", text.matches(".*[\\u0590-\\u05FF].*"));
        features.put("Has Devanagari", text.matches(".*[\\u0900-\\u097F].*"));
        features.put("Latin script only", text.matches("[a-zA-Z\\s.,!?'\"()-]+"));
        
        return features;
    }

    private static List<String> extractTrigrams(String text) {
        List<String> trigrams = new ArrayList<>();
        String cleaned = text.replaceAll("\\s+", " ").trim();
        
        for (int i = 0; i < cleaned.length() - 2; i++) {
            String trigram = cleaned.substring(i, i + 3);
            if (!trigram.contains(" ")) {
                trigrams.add(trigram);
            }
        }
        
        return trigrams;
    }
}
