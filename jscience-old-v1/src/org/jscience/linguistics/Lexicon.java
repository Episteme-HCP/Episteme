/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
