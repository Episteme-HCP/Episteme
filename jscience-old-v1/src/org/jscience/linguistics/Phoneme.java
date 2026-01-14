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

import javax.sound.sampled.Clip;


/**
 * The Phoneme class defines the basic audible units of a language. They
 * are for sound what graphemes are for text. We recommend to use the Unicode
 * IPA codes values.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Phoneme extends Object {
    /** DOCUMENT ME! */
    private Language language;

    /** DOCUMENT ME! */
    private Clip clip;

    /** DOCUMENT ME! */
    private char character;

/**
     * Creates a new Phoneme object.
     *
     * @param language DOCUMENT ME!
     * @param clip     DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //please avoid this constructor
    public Phoneme(Language language, Clip clip) {
        if ((language != null) && (clip != null)) {
            this.language = language;
            this.clip = clip;
            language.addPhoneme(this);
        } else {
            throw new IllegalArgumentException(
                "The Phoneme constructor arguments can't be null.");
        }
    }

/**
     * Creates a new Phoneme object.
     *
     * @param language  DOCUMENT ME!
     * @param clip      DOCUMENT ME!
     * @param character DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Phoneme(Language language, Clip clip, char character) {
        if ((language != null) && (clip != null)) {
            this.language = language;
            this.clip = clip;
            this.character = character;
            language.addPhoneme(this);
        } else {
            throw new IllegalArgumentException(
                "The Phoneme constructor arguments can't be null.");
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
    public Clip getSound() {
        return clip;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //CAUTION: the character may be undefined
    public char getCharacter() {
        return character;
    }
}
