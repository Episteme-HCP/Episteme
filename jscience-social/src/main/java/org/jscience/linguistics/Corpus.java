package org.jscience.linguistics;

import java.util.*;

/**
 * Represents a collection of texts for linguistic analysis.
 */
public class Corpus {

    private final List<String> documents = new ArrayList<>();

    public void addDocument(String text) {
        documents.add(text);
    }

    /**
     * Calculates word frequency map.
     * Simple tokenization by splitting on whitespace.
     */
    public Map<String, Integer> getWordFrequency() {
        Map<String, Integer> frequency = new HashMap<>();
        for (String doc : documents) {
            String[] words = doc.toLowerCase().replaceAll("[^a-zA-Z ]", "").split("\\s+");
            for (String word : words) {
                if (!word.isEmpty()) {
                    frequency.put(word, frequency.getOrDefault(word, 0) + 1);
                }
            }
        }
        return frequency;
    }

    public int getDocumentCount() {
        return documents.size();
    }
}
