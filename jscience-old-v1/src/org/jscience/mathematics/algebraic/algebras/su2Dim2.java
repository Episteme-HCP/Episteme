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

package org.jscience.mathematics.algebraic.algebras;

import org.jscience.mathematics.algebraic.fields.ComplexField;
import org.jscience.mathematics.algebraic.matrices.*;
import org.jscience.mathematics.algebraic.numbers.Complex;

import org.jscience.util.IllegalDimensionException;


/**
 * The su2Dim2 class encapsulates su(2) algebras using the 2 dimensional
 * (fundamental) representation. Elements are represented by 3-vectors with a
 * matrix basis.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class su2Dim2 extends LieAlgebra {
    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t1 = {
            { Complex.ZERO, ComplexField.HALF },
            { ComplexField.HALF, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t2 = {
            { Complex.ZERO, ComplexField.HALF_I },
            { ComplexField.MINUS_HALF_I, Complex.ZERO }
        };

    /**
     * DOCUMENT ME!
     */
    private final static Complex[][] t3 = {
            { ComplexField.HALF, Complex.ZERO },
            { Complex.ZERO, ComplexField.MINUS_HALF }
        };

    /** Basis. */
    private final static AbstractComplexSquareMatrix[] basisMatrices = {
            new ComplexSquareMatrix(t1), new ComplexSquareMatrix(t2),
            new ComplexSquareMatrix(t3)
        };

    /**
     * DOCUMENT ME!
     */
    private final static su2Dim2 _instance = new su2Dim2();

/**
     * Constructs an su(2) algebra.
     */
    private su2Dim2() {
        super("su(2) [2]");
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final su2Dim2 getInstance() {
        return _instance;
    }

    /**
     * Returns an element as a matrix (vectorbasis).
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexSquareMatrix getElement(final AbstractDoubleVector v) {
        AbstractComplexMatrix m = basisMatrices[0].scalarMultiply(v.getPrimitiveElement(
                    0));
        m = m.add(basisMatrices[1].scalarMultiply(v.getPrimitiveElement(1)));
        m = m.add(basisMatrices[2].scalarMultiply(v.getPrimitiveElement(2)));

        return (AbstractComplexSquareMatrix) m.scalarMultiply(Complex.I);
    }

    /**
     * Returns the Lie bracket (commutator) of two elements. Same as
     * the vector cross product.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractDoubleVector multiply(final AbstractDoubleVector a,
        final AbstractDoubleVector b) {
        if (!(a instanceof Double3Vector) || !(b instanceof Double3Vector)) {
            throw new IllegalDimensionException("Vectors must be 3-vectors.");
        }

        return ((Double3Vector) a).multiply((Double3Vector) b);
    }

    /**
     * Returns the basis used to represent the Lie algebra.
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexSquareMatrix[] basis() {
        return basisMatrices;
    }
}
