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

import org.jscience.mathematics.algebraic.algebras.Algebra;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.awt.*;

import java.util.Hashtable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public final class DoubleMatrixAlgebra implements Algebra, Ring {
    /**
     * DOCUMENT ME!
     */
    private static final Hashtable algebras = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    private final int rows;

    /**
     * DOCUMENT ME!
     */
    private final int cols;

    /**
     * DOCUMENT ME!
     */
    private AbstractDoubleMatrix zero;

    /**
     * DOCUMENT ME!
     */
    private AbstractDoubleSquareMatrix one;

    /**
     * Creates a new DoubleMatrixAlgebra object.
     *
     * @param rows DOCUMENT ME!
     * @param cols DOCUMENT ME!
     */
    private DoubleMatrixAlgebra(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rows DOCUMENT ME!
     * @param cols DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static DoubleMatrixAlgebra get(int rows, int cols) {
        Dimension dim = new Dimension(rows, cols);
        DoubleMatrixAlgebra algebra = (DoubleMatrixAlgebra) algebras.get(dim);

        if (algebra == null) {
            algebra = new DoubleMatrixAlgebra(rows, cols);
            algebras.put(dim, algebra);
        }

        return algebra;
    }

    /**
     * Returns the (right) identity.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        if (one == null) {
            one = DoubleDiagonalMatrix.identity(cols);
        }

        return one;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        return one().equals(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        if (zero == null) {
            zero = new DoubleMatrix(rows, cols);
        }

        return zero;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member r) {
        return zero().equals(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        return zero().equals(a.add(b));
    }
}
