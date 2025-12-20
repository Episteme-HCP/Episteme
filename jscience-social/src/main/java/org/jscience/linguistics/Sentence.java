package org.jscience.linguistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a sentence consisting of words or phrases.
 */
public class Sentence {

    public enum Type {
        DECLARATIVE, INTERROGATIVE, IMPERATIVE, EXCLAMATORY, UNKNOWN
    }

    private final String content;
    private final Type type;

    // In V1 legacy, it was a Vector of Phrases. Here we just store basic meta-data
    // for now.

    public Sentence(String content) {
        this(content, determineType(content));
    }

    public Sentence(String content, Type type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public Type getType() {
        return type;
    }

    private static Type determineType(String text) {
        if (text == null || text.isEmpty())
            return Type.UNKNOWN;
        String trimmed = text.trim();
        if (trimmed.endsWith("?"))
            return Type.INTERROGATIVE;
        if (trimmed.endsWith("!"))
            return Type.EXCLAMATORY;
        if (trimmed.endsWith("."))
            return Type.DECLARATIVE;
        return Type.UNKNOWN;
    }
}
