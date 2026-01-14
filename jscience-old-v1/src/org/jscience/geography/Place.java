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

package org.jscience.geography;

import org.jscience.util.Named;
import org.jscience.util.Positioned;


/**
 * A class representing a geographical spot, namely a feature. It is meant
 * to be used primarily to define places like human settlements, that is
 * places with a name.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Place extends Object implements Named, Positioned {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Boundary boundary;

/**
     * Creates a new Place object.
     *
     * @param name     DOCUMENT ME!
     * @param boundary DOCUMENT ME!
     */
    public Place(String name, Boundary boundary) {
        if ((name != null) && (name.length() > 0) && (boundary != null)) {
            this.name = name;
            this.boundary = boundary;
        } else {
            throw new IllegalArgumentException(
                "The Place constructor can't have null or empty arguments.");
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
    public Boundary getBoundary() {
        return boundary;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getPosition() {
        return boundary.getPosition();
    }
}
