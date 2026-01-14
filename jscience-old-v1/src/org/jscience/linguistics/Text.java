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

import java.util.Iterator;
import java.util.Vector;


/**
 * The Text class provides a placeholder for texts to be analyzed by
 * linguistics. Not to be used to store or manipulate Strings.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a text is a vector of sentences.
public class Text extends Object {
    /** DOCUMENT ME! */
    private Vector sentences;

/**
     * Creates a new Text object.
     */
    public Text() {
        sentences = new Vector();
    }

/**
     * Creates a new Text object.
     *
     * @param sentences DOCUMENT ME!
     */
    public Text(Vector sentences) {
        Iterator iterator;
        boolean valid;

        if (sentences != null) {
            iterator = sentences.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Sentence;
            }

            if (valid) {
                this.sentences = sentences;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of Sentences must consist only of Sentences.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Text constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getSentences() {
        return sentences;
    }
}
