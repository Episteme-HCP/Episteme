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

/**
 * The Word defines sequences of Morphemes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Word extends Object {
    /** DOCUMENT ME! */
    private Morpheme[] morphemes;

/**
     * Creates a new Word object.
     *
     * @param language DOCUMENT ME!
     * @param string   DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Word(Language language, String string) {
        if ((language != null) && (string != null) && (string.length() > 0)) {
            morphemes = new Morpheme[string.length()];

            for (int i = 0; i < string.length(); i++) {
                morphemes[i] = new Morpheme(language, string.substring(i, i +
                            1));
            }
        } else {
            throw new IllegalArgumentException(
                "The Lexeme constructor arguments can't be null and string can't be empty.");
        }
    }

/**
     * Creates a new Word object.
     *
     * @param morphemes DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Word(Morpheme[] morphemes) {
        boolean valid;
        int i;

        if ((morphemes != null) && (morphemes.length > 0)) {
            i = 1;
            valid = true;

            while ((valid) && (i < morphemes.length)) {
                valid = (morphemes[i].getLanguage() == morphemes[0].getLanguage());
            }

            if (valid) {
                this.morphemes = morphemes;
            } else {
                throw new IllegalArgumentException(
                    "The morphemes must all be of the same Language.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Word constructor arguments can't be null and morphemes can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return morphemes[0].getLanguage();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Morpheme[] getMorphemes() {
        return morphemes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getString() {
        int i;
        StringBuffer result;

        result = new StringBuffer();

        for (i = 0; i < morphemes.length; i++) {
            result.append(morphemes[i].getString());
        }

        return result.toString();
    }
}
