package org.jscience.linguistics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Validates Zipf's Law in a text corpus.
 */
public final class ZipfLawAnalyzer {

    private ZipfLawAnalyzer() {}

    /**
     * Analyzes word frequency distribution.
     * Returns a map of Rank -> Frequency.
     */
    public static Map<Integer, Integer> analyzeFrequencies(String text) {
        String[] words = text.toLowerCase().replaceAll("[^a-z\\s]", "").split("\\s+");
        Map<String, Integer> counts = new HashMap<>();
        for (String w : words) counts.put(w, counts.getOrDefault(w, 0) + 1);
        
        List<Integer> freqs = new ArrayList<>(counts.values());
        Collections.sort(freqs, Collections.reverseOrder());
        
        Map<Integer, Integer> rankToFreq = new LinkedHashMap<>();
        for (int i = 0; i < freqs.size(); i++) {
            rankToFreq.put(i + 1, freqs.get(i));
        }
        return rankToFreq;
    }

    /**
     * Checks the Zipf constant (k = rank * frequency).
     * Ideally, this should be constant across the corpus.
     */
    public static Real calculateZipfConstant(int rank, int frequency) {
        return Real.of((long) rank * frequency);
    }
}
