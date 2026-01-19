package org.jscience.history;

import java.util.*;

/**
 * Analyzes document authenticity through linguistic anachronism detection.
 */
public final class DocumentAuthenticity {

    private DocumentAuthenticity() {}

    /**
     * Checks if a text contains words that didn't exist in a given year.
     */
    public static List<String> findAnachronisms(String text, int targetYear, Map<String, Integer> etymologyData) {
        List<String> found = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");
        for (String w : words) {
            String clean = w.replaceAll("[^a-z]", "");
            if (etymologyData.containsKey(clean) && etymologyData.get(clean) > targetYear) {
                found.add(clean);
            }
        }
        return found;
    }
}
