package org.jscience.history;

import java.util.*;

/**
 * Decoder for monumental inscriptions (Latin, Greek, Runic).
 */
public final class MonumentalInscriptionDecoder {

    private MonumentalInscriptionDecoder() {}

    private static final Map<String, String> RUNIC_MAP = Map.of(
        "ᚠ", "f", "ᚢ", "u", "ᚦ", "th", "ᚨ", "a", "ᚱ", "r"
    );

    /**
     * Decodes runic characters.
     */
    public static String decodeRunic(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(RUNIC_MAP.getOrDefault(String.valueOf(c), "?"));
        }
        return sb.toString();
    }
}
