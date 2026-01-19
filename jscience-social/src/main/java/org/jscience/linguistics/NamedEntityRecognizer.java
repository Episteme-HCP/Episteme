package org.jscience.linguistics;

import java.util.*;

/**
 * Named Entity Recognizer (NER) for text samples.
 */
public final class NamedEntityRecognizer {

    private NamedEntityRecognizer() {}

    public record Entity(String text, String type) {}

    /**
     * Extracts entities based on capitalization and keyword lists.
     */
    public static List<Entity> extractEntities(String text) {
        List<Entity> entities = new ArrayList<>();
        String[] words = text.split("\\s+");
        for (String w : words) {
            if (Character.isUpperCase(w.charAt(0)) && w.length() > 2) {
                entities.add(new Entity(w, "PROPER_NAME"));
            }
        }
        return entities;
    }
}
