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

package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.algebraic.numbers.Complex;

import java.io.Serializable;


// TODO as it extends DoubleSquareMatrix -- maybe able to simplify some of the operations.
/**
 * The ComplexSymmetricMatrix class provides an object for encapsulating
 * square matrices containing doubles.
 *
 * @author Mark Hale
 * @version 2.3
 */
public class ComplexSymmetricMatrix extends ComplexSquareMatrix
    implements Cloneable, Serializable {
/**
     * Constructs an empty matrix.
     *
     * @param size the number of rows/columns.
     */
    public ComplexSymmetricMatrix(final int size) {
        super(size);
    }

/**
     * Constructs a matrix by wrapping an array.
     *
     * @param array an assigned value.
     */
    public ComplexSymmetricMatrix(final Complex[][] array) {
        super(array);
    }

/**
     * Constructs a matrix from an array of vectors (columns).
     *
     * @param array an assigned value.
     */
    public ComplexSymmetricMatrix(final ComplexVector[] array) {
        super(array);
    }

/**
     * Copy constructor.
     *
     * @param mat an assigned value.
     */
    public ComplexSymmetricMatrix(final ComplexSymmetricMatrix mat) {
        super(mat);
    }

    /**
     * Also sets the symmetric element.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param x DOCUMENT ME!
     */
    public void setElement(final int i, final int j, final Complex x) {
        super.setElement(i, j, x);

        if (i != j) {
            super.setElement(j, i, x);
        }
    }

    //============
    // OPERATIONS
    //============
    // ADDITION
    // SUBTRACTION
    // INVERSE
    // LU DECOMPOSITION
    // CHOLESKY DECOMPOSITION
    // QR DECOMPOSITION
    // SINGULAR VALUE DECOMPOSITION
    // POLAR DECOMPOSITION
    // MAP ELEMENTS
    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new ComplexSymmetricMatrix(this);
    }
}
