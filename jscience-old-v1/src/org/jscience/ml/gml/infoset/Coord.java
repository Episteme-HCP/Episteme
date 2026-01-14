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

/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         4-Sep-2001    Made Coord a
 * PropertyOwner, hence X,Y,Z will be considered as such. amilanovic
 * 29-Mar-2002   Updated for the new package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines interface that every class representing the GML coord construct must
 * implement. Coord represents a coordinate 3-tuple.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Coord extends CoordinateTuple, GMLConstruct,
    UnknownConstructOwner, PropertyOwner {
    // Coord properties
    /** DOCUMENT ME! */
    public static final String X_PROP = "X";

    /** DOCUMENT ME! */
    public static final String Y_PROP = "Y";

    /** DOCUMENT ME! */
    public static final String Z_PROP = "Z";
}
