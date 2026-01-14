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

package org.jscience.arts.hobbies;

import org.jscience.arts.ArtsConstants;

import org.jscience.util.Named;


/**
 * A class representing a hobby (a "for the fun" passion in life).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Hobby extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private int art;

/**
     * Creates a new Hobby object.
     *
     * @param name DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Hobby(String name) {
        if (name != null) {
            this.name = name;
            this.art = ArtsConstants.UNKNOWN;
        } else {
            throw new IllegalArgumentException(
                "The Hobby constructor can't have null arguments.");
        }
    }

/**
     * Creates a new Hobby object.
     *
     * @param name DOCUMENT ME!
     * @param art  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Hobby(String name, int art) {
        if (name != null) {
            this.name = name;
            this.art = art;
        } else {
            throw new IllegalArgumentException(
                "The Hobby constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getArt() {
        return art;
    }

    //just in case you are not sure about the category
    /**
     * DOCUMENT ME!
     *
     * @param art DOCUMENT ME!
     */
    public void setArt(int art) {
        this.art = art;
    }
}
