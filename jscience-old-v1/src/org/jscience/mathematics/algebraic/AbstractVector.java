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

package org.jscience.mathematics.algebraic;

import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;

/**
 * The Vector superclass provides an abstract encapsulation for vectors. Vectors are basically a matrix with 1 column. Concrete implementations of this class should implement additional interfaces. See subclasses.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @planetmath Vector
 */
public abstract class AbstractVector extends AbstractMatrix implements Vector, Module.Member, Serializable {

    /**
     * Constructs a mathematical vector.
     *
     * @param n the dimension of the vector.
     */
    public AbstractVector(int n) {

        super(n, 1);

    }

    /**
     * Returns the vector's dimension.
     */
    public final int getDimension() {
        return numRows();
    }

    /**
     * Returns the element at position i,j.
     */
    public abstract Number getElement(int i) throws IllegalDimensionException;

    /**
     * Returns the element at position i.
     */
    public final Number getElement(int i, int j) throws IllegalDimensionException {

        if (j == 1) {
            return getElement(i);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Returns the ith row.
     */
    /**public AbstractVector getRow(int i) {

     if ((i>=0) && (i<numRows())) {
     Number[] array = new Number[1];
     array[0] = getElement(i);
     return new AbstractVector(array);
     } else
     throw new IllegalDimensionException("Requested element out of bounds.");

     }
     */

    /**
     * Returns the ith column.
     */
    /**
     * public AbstractVector getColumn(int j) {
     * <p/>
     * if (j==1) {
     * return this;
     * } else
     * throw new IllegalDimensionException("Requested element out of bounds.");
     * <p/>
     * }
     */

    //Object is a Number[]
    public Object toArray() {

        return toArray(this);

    }

    /**
     * Converts a vector to an array.
     */
    //Java doesn't allow static here, too bad
    public Number[] toArray(Vector v) {

        Number[] array = new Number[v.getDimension()];

        for (int i = 0; i < array.length; i++) {
            array[i] = v.getElement(i);
        }

        return array;

    }

    /**
     * Returns an "invalid element" error message.
     *
     * @param i row index of the element
     */
    protected static String getInvalidElementMsg(int i) {
        return "(" + i + ") is an invalid element for this vector.";
    }

}
