package org.jscience.linguistics;

import java.util.HashMap;
import java.util.Map;


/**
 * The Lexicon class provides a dictionnary of lexemes and corresponding
 * definition.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Lexicon extends HashMap {
    //null shouldn't be allowed
    //map should consists only of Lexeme/String mappings
    //Lexemes should all be of the same language
    /** DOCUMENT ME! */
    private Language language;

/**
     * Creates a new Lexicon object.
     */
    public Lexicon() {
        super();
        language = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Object put(Object key, Object value) {
        if ((key != null) && (value != null) && (key instanceof Lexeme) &&
                (value instanceof String)) {
            return put((Lexeme) key, (String) value);
        } else {
            throw new IllegalArgumentException(
                "You can only put a couple (Lexeme, String).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param lexeme DOCUMENT ME!
     * @param definition DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Object put(Lexeme lexeme, String definition) {
        Object value;

        if ((lexeme != null) && (definition != null) &&
                (definition.length() > 0)) {
            if (language != null) {
                if (lexeme.getLanguage() == language) {
                    value = super.put(lexeme, definition);
                } else {
                    throw new IllegalArgumentException(
                        "Lexeme language must be the same as other lexemes of this lexicon.");
                }
            } else {
                value = super.put(lexeme, definition);
                language = lexeme.getLanguage();
            }

            return value;
        } else {
            throw new IllegalArgumentException(
                "Lexeme or definition can't be null and definition can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param map DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void putAll(Map map) {
        throw new IllegalArgumentException("This operation is not supported.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Object remove(Object key) {
        if ((key != null) && (key instanceof Lexeme)) {
            return remove((Lexeme) key);
        } else {
            throw new IllegalArgumentException(
                "Lexeme or definition can't be null and definition can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param lexeme DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object remove(Lexeme lexeme) {
        Object value;

        if (super.containsKey(lexeme)) {
            value = super.remove(lexeme);

            if (size() == 0) {
                language = null;
            }

            return value;
        } else {
            return null;
        }
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return language;
    }
}
