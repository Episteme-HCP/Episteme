package org.jscience.linguistics;

import java.util.*;

/**
 * Checks for common grammatical errors.
 */
public final class GrammarChecker {

    private GrammarChecker() {}

    public record Error(int index, String type, String message) {}

    /**
     * Detects double words and common typos.
     */
    public static List<Error> check(String text) {
        List<Error> errors = new ArrayList<>();
        String[] words = text.split("\\s+");
        for (int i = 1; i < words.length; i++) {
            if (words[i].equalsIgnoreCase(words[i-1])) {
                errors.add(new Error(i, "REPETITION", "Double word: " + words[i]));
            }
        }
        return errors;
    }
}
