package org.jscience.linguistics;

import java.util.*;

/**
 * Etymology graph for tracking word derivations across languages.
 */
public final class EtymologyGraph {

    private EtymologyGraph() {}

    public record Etymology(
        String word,
        String language,
        String meaning,
        int approximateYear
    ) {}

    public record Derivation(
        Etymology source,
        Etymology target,
        String derivationType,  // "borrowing", "inheritance", "calque", "compound"
        String notes
    ) {}

    private static final Map<String, List<Derivation>> DERIVATION_MAP = new HashMap<>();

    /**
     * Adds a derivation to the graph.
     */
    public static void addDerivation(Derivation derivation) {
        DERIVATION_MAP
            .computeIfAbsent(derivation.target().word().toLowerCase(), k -> new ArrayList<>())
            .add(derivation);
    }

    /**
     * Traces the etymology of a word back to its roots.
     */
    public static List<Etymology> traceEtymology(String word, String language) {
        List<Etymology> path = new ArrayList<>();
        traceRecursive(word.toLowerCase(), language, path, new HashSet<>());
        return path;
    }

    private static void traceRecursive(String word, String language, 
            List<Etymology> path, Set<String> visited) {
        
        String key = word + "_" + language;
        if (visited.contains(key)) return;
        visited.add(key);
        
        List<Derivation> derivations = DERIVATION_MAP.get(word);
        if (derivations == null) return;
        
        for (Derivation d : derivations) {
            if (d.target().language().equalsIgnoreCase(language) ||
                d.target().word().equalsIgnoreCase(word)) {
                path.add(d.target());
                path.add(d.source());
                traceRecursive(d.source().word(), d.source().language(), path, visited);
            }
        }
    }

    /**
     * Finds cognates (words with common ancestry) across languages.
     */
    public static List<Etymology> findCognates(String word, String sourceLanguage) {
        List<Etymology> cognates = new ArrayList<>();
        
        // Find the root
        List<Etymology> path = traceEtymology(word, sourceLanguage);
        if (path.isEmpty()) return cognates;
        
        Etymology root = path.get(path.size() - 1);
        
        // Find all words derived from the same root
        for (List<Derivation> derivations : DERIVATION_MAP.values()) {
            for (Derivation d : derivations) {
                if (d.source().word().equals(root.word()) &&
                    d.source().language().equals(root.language())) {
                    cognates.add(d.target());
                }
            }
        }
        
        return cognates;
    }

    /**
     * Pre-built Indo-European roots database (sample).
     */
    public static void loadSampleData() {
        // Example: "father" derivatives
        Etymology piePhter = new Etymology("*ph₂tḗr", "Proto-Indo-European", "father", -4000);
        Etymology latinPater = new Etymology("pater", "Latin", "father", -500);
        Etymology greekPater = new Etymology("πατήρ", "Ancient Greek", "father", -800);
        Etymology englishFather = new Etymology("father", "English", "father", 1000);
        Etymology frenchPere = new Etymology("père", "French", "father", 1200);
        Etymology germanVater = new Etymology("Vater", "German", "father", 800);
        Etymology spanishPadre = new Etymology("padre", "Spanish", "father", 1300);
        
        addDerivation(new Derivation(piePhter, latinPater, "inheritance", "Regular sound change"));
        addDerivation(new Derivation(piePhter, greekPater, "inheritance", ""));
        addDerivation(new Derivation(piePhter, englishFather, "inheritance", "Via Proto-Germanic"));
        addDerivation(new Derivation(latinPater, frenchPere, "inheritance", "Vulgar Latin"));
        addDerivation(new Derivation(piePhter, germanVater, "inheritance", "Grimm's Law"));
        addDerivation(new Derivation(latinPater, spanishPadre, "inheritance", ""));
        
        // Example: "water" derivatives
        Etymology pieWodr = new Etymology("*wódr̥", "Proto-Indo-European", "water", -4000);
        Etymology latinAqua = new Etymology("aqua", "Latin", "water", -500);
        Etymology englishWater = new Etymology("water", "English", "water", 1000);
        Etymology germanWasser = new Etymology("Wasser", "German", "water", 800);
        Etymology russianVoda = new Etymology("вода", "Russian", "water", 1000);
        
        addDerivation(new Derivation(pieWodr, englishWater, "inheritance", ""));
        addDerivation(new Derivation(pieWodr, germanWasser, "inheritance", ""));
        addDerivation(new Derivation(pieWodr, russianVoda, "inheritance", ""));
        
        // Example: borrowed words
        Etymology arabicAlgorithm = new Etymology("الخوارزمي", "Arabic", "al-Khwarizmi", 800);
        Etymology latinAlgorismus = new Etymology("algorismus", "Medieval Latin", "algorithm", 1200);
        Etymology englishAlgorithm = new Etymology("algorithm", "English", "algorithm", 1600);
        
        addDerivation(new Derivation(arabicAlgorithm, latinAlgorismus, "borrowing", "From mathematician's name"));
        addDerivation(new Derivation(latinAlgorismus, englishAlgorithm, "borrowing", "Via Medieval Latin"));
    }

    /**
     * Gets statistics about the etymology database.
     */
    public static Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        int totalWords = DERIVATION_MAP.size();
        int totalDerivations = DERIVATION_MAP.values().stream()
            .mapToInt(List::size).sum();
        
        Map<String, Integer> languageCount = new HashMap<>();
        for (List<Derivation> derivations : DERIVATION_MAP.values()) {
            for (Derivation d : derivations) {
                languageCount.merge(d.source().language(), 1, Integer::sum);
                languageCount.merge(d.target().language(), 1, Integer::sum);
            }
        }
        
        stats.put("Total Words", totalWords);
        stats.put("Total Derivations", totalDerivations);
        stats.put("Languages Covered", languageCount.size());
        
        return stats;
    }
}
