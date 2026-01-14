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

package org.jscience.util.mapper;

/**
 * Wrapper class around a scalar in order to have it implement the {@link
 * ArraySliceMappable} interface.
 *
 * @author L. Maisonobe
 * @version $Id: MappableScalar.java,v 1.3 2007-10-23 18:24:37 virtualcall Exp $
 */
public class MappableScalar implements ArraySliceMappable {
    /** Internal scalar. */
    double value;

/**
     * Simple constructor. Build a mappable scalar
     */
    public MappableScalar() {
        value = 0;
    }

/**
     * Simple constructor. Build a mappable scalar from its initial value
     *
     * @param value initial value of the scalar
     */
    public MappableScalar(double value) {
        this.value = value;
    }

    /**
     * Get the value stored in the instance.
     *
     * @return value stored in the instance
     */
    public double getValue() {
        return value;
    }

    /**
     * Set the value stored in the instance.
     *
     * @param value value to store in the instance
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Get the dimension of the internal array.
     *
     * @return dimension of the array (always 1 for this class)
     */
    public int getStateDimension() {
        return 1;
    }

    /**
     * Reinitialize internal state from the specified array slice data.
     *
     * @param start start index in the array
     * @param array array holding the data to extract
     */
    public void mapStateFromArray(int start, double[] array) {
        value = array[start];
    }

    /**
     * Store internal state data into the specified array slice.
     *
     * @param start start index in the array
     * @param array array where data should be stored
     */
    public void mapStateToArray(int start, double[] array) {
        array[start] = value;
    }
}
