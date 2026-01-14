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

package org.jscience.philosophy.storytelling;

/**
 * A class representing the object of an Event (what happens).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Object extends java.lang.Object {
    /** DOCUMENT ME! */
    private String object; //the description

/**
     * Creates a new Object object.
     *
     * @param object DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Object(String object) {
        if ((object != null) && (object.length() > 0)) {
            this.object = object;
        } else {
            throw new IllegalArgumentException(
                "The Object constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getObject() {
        return object;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(java.lang.Object o) {
        if ((o != null) &&
                (o instanceof org.jscience.philosophy.storytelling.Object)) {
            return this.getObject().equals(((Object) o).getObject());
        } else {
            return false;
        }
    }

    //public boolean isActIncluded(org.jscience.philosophy.storytelling.Object object);
    //for example driving contains the act of using the car key
}
