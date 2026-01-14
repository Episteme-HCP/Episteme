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
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

import java.math.BigDecimal;


/**
 * A convenience class that implements the Coordinate interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class CoordinateImpl implements Coordinate {
    // the decimal value of this coordinate
    /** DOCUMENT ME! */
    private BigDecimal value_;

/**
     * Constructor.
     *
     * @param value The value used for the construction of this coordinate in
     *              string representation.
     */
    public CoordinateImpl(String value) {
        value_ = new BigDecimal(value);
    }

    // Coordinate interface implementation
    /**
     * Provides access to the value of this coordinate. The value is
     * BigDecimal because it doesn't lose precision. One may obtain other
     * representations from BigDecimal.
     *
     * @return Returns the value as BigDecimal. Cannot be null.
     */
    public BigDecimal getValue() {
        return value_;
    }

    // redefined methods
    /**
     * Returns the string representation of the coordinate decimal
     * value.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return value_.toString();
    }
}
