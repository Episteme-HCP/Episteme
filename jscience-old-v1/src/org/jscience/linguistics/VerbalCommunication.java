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
 * The Communication class provides support for communication, which is
 * what the language ultimatly stands for.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//exchange of information between locutors
//all the receivers are believed to be part of the ChatSituation
//this class is mainly targetted at verbal situation where people emit short phrases rather than complete texts (theater like)
public class VerbalCommunication extends Object {
    /** DOCUMENT ME! */
    private Locutor locutor; //emiter

    /** DOCUMENT ME! */
    private Phrase message;

/**
     * Creates a new VerbalCommunication object.
     *
     * @param locutor DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public VerbalCommunication(Locutor locutor, Phrase message) {
        if ((locutor != null) && (message != null)) {
            this.locutor = locutor;
            this.message = message;
        } else {
            throw new IllegalArgumentException(
                "The Communication constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Locutor getLocutor() {
        return locutor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Phrase getMessage() {
        return message;
    }

    //public Language getLanguage() {
    //we would have to test all the contents of the phrase are in the same language
    //}
}
