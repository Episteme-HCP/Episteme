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
 * The Lexeme defines common rooted words.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Lexeme extends Object {
    /** DOCUMENT ME! */
    private Word[] words;

/**
     * Creates a new Lexeme object.
     *
     * @param words DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Lexeme(Word[] words) {
        boolean valid;
        int i;

        if ((words != null) && (words.length > 0)) {
            i = 1;
            valid = true;

            while ((valid) && (i < words.length)) {
                valid = (words[i].getLanguage() == words[0].getLanguage());
            }

            if (valid) {
                this.words = words;
            } else {
                throw new IllegalArgumentException(
                    "The words must all be of the same Language.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Lexeme constructor arguments can't be null and words can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return words[0].getLanguage();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Word[] getWords() {
        return words;
    }
}
