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

import org.jscience.mathematics.algebraic.algebras.Algebra;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;

import java.lang.reflect.Array;


/**
 * The Hypermatrix superclass provides an abstract encapsulation for
 * extended matrices. Hypermatrices are also known as MultiArrays. You should
 * be aware when using this class that it is very computationally intensive.
 * First it uses Java data structures that are not really optimized for this
 * sort of task. Then there is additional error checking to ensure you use it
 * the right way. Furthermore there is of course a real temptation to use many
 * dimensions. Please recall that 10 elements in 10 dimensions is 10 power 10
 * total elements, therefore more than your system is probably able to handle.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class AbstractHypermatrix extends Object implements Hypermatrix,
    Algebra.Member, Serializable {
    /** The number of elements for each of the dimensions. */
    private final int[] dimensions;

/**
     * Constructs an hypermatrix. The length of dimensions should be less than
     * 256 although this is dependent upon the JVM implementation.
     *
     * @param dimensions DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractHypermatrix(int[] dimensions) {
        int i;
        boolean result;

        if (dimensions.length > 0) {
            i = 0;
            result = true;

            while (result && (i < dimensions.length)) {
                result = (dimensions[i] > 0);
                i++;
            }

            if (result) {
                this.dimensions = dimensions;
            } else {
                throw new IllegalDimensionException(
                    "All dimensions of an Hypermatrix must be strictly positive.");
            }
        } else {
            throw new IllegalDimensionException(
                "Hypermatrix must have more than 0 dimension.");
        }
    }

    /**
     * Returns the number of dimensions.
     *
     * @return DOCUMENT ME!
     */
    public final int numDimensions() {
        return dimensions.length;
    }

    /**
     * Returns the array of dimensions.
     *
     * @return DOCUMENT ME!
     */
    public final int[] getDimensions() {
        return dimensions;
    }

    /**
     * Returns the number of elements for the given dimension.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final int numElements(int i) {
        return dimensions[i];
    }

    /**
     * Returns the total number of elements.
     *
     * @return DOCUMENT ME!
     */
    public final int numElements() {
        int result;

        result = 0;

        for (int i = 0; i < dimensions.length; i++) {
            result = result * dimensions[i];
        }

        return result;
    }

    //equals, toString, clone, hashcode
    /**
     * Returns the element at position given by the array of int.
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public abstract Number getElement(int[] position)
        throws IllegalDimensionException;

    /**
     * Converts an hypermatrix to an array of n dimensions
     *
     * @return DOCUMENT ME!
     */
    public Object toArray() {
        Object array = Array.newInstance(Number.class, this.getDimensions());

        return getElements(this, array, this.getDimensions(), new int[0]);
    }

    /**
     * Converts an hypermatrix to an array of n dimensions
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //Java doesn't allow static here, too bad
    public Object toArray(Hypermatrix m) {
        Object array = Array.newInstance(Number.class, m.getDimensions());

        return getElements(m, array, m.getDimensions(), new int[0]);
    }

    //dimensions.length>0
    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     * @param array DOCUMENT ME!
     * @param dimensions DOCUMENT ME!
     * @param valuatedDimensions DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Object getElements(Hypermatrix m, Object array,
        int[] dimensions, int[] valuatedDimensions) {
        if (dimensions.length > 1) {
            int[] resultDimensions = new int[dimensions.length - 1];
            int[] resultValuatedDimensions = new int[valuatedDimensions.length +
                1];

            for (int j = 0; j < (dimensions.length - 1); j++) {
                resultDimensions[j] = dimensions[j];
            }

            for (int j = 0; j < valuatedDimensions.length; j++) {
                resultValuatedDimensions[j + 1] = valuatedDimensions[j];
            }

            for (int i = 0; i < dimensions[dimensions.length - 1]; i++) {
                resultValuatedDimensions[0] = i;
                getElements(m, Array.get(array, i), resultDimensions,
                    resultValuatedDimensions);
            }

            return array;
        } else {
            int[] resultValuatedPosition = new int[valuatedDimensions.length +
                1];

            for (int j = 1; j < resultValuatedPosition.length; j++) {
                resultValuatedPosition[j] = valuatedDimensions[j - 1];
            }

            for (int i = 0; i < dimensions[0]; i++) {
                resultValuatedPosition[0] = i;
                Array.set(array, i, m.getElement(resultValuatedPosition));
            }

            return array;
        }
    }

    //we could also provide a method to extract/project an hypermatrix onto one of the dimensions
    //thus resulting in an hypermatrix of n-1 dimensions
}
