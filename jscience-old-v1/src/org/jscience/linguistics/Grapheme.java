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
 * The Grapheme class is the minimal text unit. English letters are common
 * graphemes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://66.102.9.104/search?q=cache:jglOb1Mdnp8J:www.languefrancaise.net/dossiers/dossiers.php%3Fid_dossier%3D111++graphemes+francais&hl=fr
//http://moodle.ed.uiuc.edu/wiked/index.php/Phonemic_awareness
//space, as a delimiter, is not a grapheme
public class Grapheme extends Object {
    /** DOCUMENT ME! */
    private Language language;

    /** DOCUMENT ME! */
    private char character;

/**
     * Creates a new Grapheme object.
     *
     * @param language  DOCUMENT ME!
     * @param character DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Grapheme(Language language, char character) {
        if (language != null) {
            this.language = language;
            this.character = character;
            language.addGrapheme(this);
        } else {
            throw new IllegalArgumentException(
                "The Grapheme constructor arguments can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public char getCharacter() {
        return character;
    }
}
