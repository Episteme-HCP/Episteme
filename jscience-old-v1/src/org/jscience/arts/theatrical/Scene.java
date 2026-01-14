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

package org.jscience.arts.theatrical;

import org.jscience.arts.musical.Composition;


/**
 * A class representing a scene that takes place in a show, with a text, a
 * music and body movements. In movies, "scenes" are further described in
 * terms of shots (a view from a camera). Shots are not provided here. Treat
 * them as scenes and scenes as acts (it is hard to tell when watching a movie
 * or even a play where an act really begins unless you are told).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Scene extends Object {
    /** DOCUMENT ME! */
    private String text;

    /** DOCUMENT ME! */
    private Composition composition;

    /** DOCUMENT ME! */
    private Choregraphy choregraphy; //or actors movements

/**
     * Creates a new Scene object.
     *
     * @param text DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Scene(String text) {
        if (text != null) {
            this.text = text;
            this.composition = null;
            this.choregraphy = null;
        } else {
            throw new IllegalArgumentException(
                "The Scene constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setText(String text) {
        if (text != null) {
            this.text = text;
        } else {
            throw new IllegalArgumentException("The text can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Composition getComposition() {
        return composition;
    }

    //null allowed
    /**
     * DOCUMENT ME!
     *
     * @param composition DOCUMENT ME!
     */
    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Choregraphy getChoregraphy() {
        return choregraphy;
    }

    //it is a good idea to match the number of poses with the corresponding note events
    /**
     * DOCUMENT ME!
     *
     * @param choregraphy DOCUMENT ME!
     */
    public void setChoregraphy(Choregraphy choregraphy) {
        this.choregraphy = choregraphy;
    }
}
