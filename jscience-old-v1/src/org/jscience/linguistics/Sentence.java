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
 * The Sentence class corresponds to the common sense sentence. In English
 * a sentence starts with an uppercase and ends with a period.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a sentence is a vector of phrases.
public class Sentence extends Object {
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    //In the English language, linguists classify sentences into one of four types based on their structure:
    //mutually exclusive
    /** DOCUMENT ME! */
    public final static int SIMPLE_SENTENCE = 1;

    /** DOCUMENT ME! */
    public final static int COMPOUND_SENTENCE = 2;

    /** DOCUMENT ME! */
    public final static int COMPLEX_SENTENCE = 3;

    /** DOCUMENT ME! */
    public final static int COMPOUND_COMPLEX_SENTENCE = 4;

    //Sentences can also be classified based on their purpose:
    /** DOCUMENT ME! */
    public final static int DECLARATIVE_SENTENCE = 1; //(Declarations)

    //Used to state a fact or argument that does not require a response from the listener. For example:
    //I am going home.
    /** DOCUMENT ME! */
    public final static int INTERROGATIVE_SENTENCE = 2;

    //Used to ask a question that expects an answer from the listener. For example:
    //When are you going to work?
    /** DOCUMENT ME! */
    public final static int RHETORICAL_QUESTIONS = 3;

    //Appears in the form of a question that is not expected to be answered by the listener and generally used emphasize a statement or argument. For example:
    //Who am I to question his authority?
    /** DOCUMENT ME! */
    public final static int EXCLAMATORY_SENTENCE = 4; //(Exclamations)

    //Used to make a forceful or emphatic statement or argument. Can also be an interjection. For example:
    //This is such a wonderful day!
    //Wow!
    /** DOCUMENT ME! */
    public final static int IMPERATIVE_SENTENCE = 5; //(Imperatives)

    /** DOCUMENT ME! */
    private Vector phrases;

/**
     * Creates a new Sentence object.
     */
    public Sentence() {
        phrases = new Vector();
    }

/**
     * Creates a new Sentence object.
     *
     * @param phrases DOCUMENT ME!
     */
    public Sentence(Vector phrases) {
        Iterator iterator;
        boolean valid;

        if (phrases != null) {
            iterator = phrases.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Phrase;
            }

            if (valid) {
                this.phrases = phrases;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of Phrases must consist only of Phrases.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Sentence constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getPhrases() {
        return phrases;
    }
}
