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

package org.jscience.mathematics.statistics;

import org.jscience.mathematics.algebraic.matrices.DoubleSquareMatrix;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;
import org.jscience.mathematics.algebraic.matrices.LinearMathUtils;

import org.jscience.util.MaximumIterationsExceededException;


/**
 * This class implements the Karhunen-Loeve expansion.
 *
 * @author Daniel Lemire
 */
public final class KarhunenLoeve {
    /** DOCUMENT ME! */
    double[][] data;

/**
     * Creates a new KarhunenLoeve object.
     *
     * @param v DOCUMENT ME!
     */
    public KarhunenLoeve(double[][] v) {
        setData(v);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[][] getProductMatrix() {
        return (getProductMatrix(data));
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[][] vectorToSquare(double[] v) {
        double[][] ans = new double[v.length][v.length];

        for (int k = 0; k < v.length; k++) {
            for (int l = 0; l < v.length; l++) {
                ans[l][k] = v[k] * v[l];
            }
        }

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    private static void add(double[][] a, double c, double[][] b) {
        for (int k = 0; k < a.length; k++) {
            for (int l = 0; l < a[k].length; l++) {
                a[k][l] += (b[k][l] * c);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double[][] getProductMatrix(double[][] v) {
        double[][] ans = new double[v[0].length][v[0].length];

        for (int k = 0; k < v.length; k++) {
            add(ans, 1.0 / v.length, vectorToSquare(v[k]));
        }

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double[][] getProductMatrix(double[] v) {
        return (vectorToSquare(v));
    }

    /**
     * Careful: doesn't generate a copy.
     *
     * @return DOCUMENT ME!
     */
    public double[][] getData() {
        return (data);
    }

    /**
     * Careful: doesn't generate a copy.
     *
     * @param v DOCUMENT ME!
     */
    public void setData(double[][] v) {
        data = v;
    }

    /**
     * Returns the eigenvectors ordered by the norm of the eigenvalues
     * (from max to min).
     *
     * @return DOCUMENT ME!
     *
     * @throws MaximumIterationsExceededException if it can't compute the
     *         eigenvectors within the limited number of iterations allowed.
     */
    public double[][] getEigenvectors()
        throws MaximumIterationsExceededException {
        double[][] test = getProductMatrix(data);
        DoubleSquareMatrix alpha = new DoubleSquareMatrix(test);
        DoubleVector[] beta = new DoubleVector[data[0].length];
        double[] eigen = LinearMathUtils.eigenSolveSymmetric(alpha, beta);
        tri(eigen, beta);

        double[][] ans = new double[beta.length][beta[0].getDimension()];

        for (int k = 0; k < beta.length; k++) {
            for (int l = 0; l < beta[k].getDimension(); l++) {
                ans[k][l] = beta[k].getPrimitiveElement(l);
            }
        }

        return (ans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     * @param mat DOCUMENT ME!
     */
    private static void tri(double[] v, DoubleVector[] mat) {
        double temp;
        DoubleVector arraytemp;
        boolean doitTrier = true;

        while (doitTrier) {
            doitTrier = false;

            for (int k = 0; k < (v.length - 1); k++) {
                if (v[k] < v[k + 1]) {
                    temp = v[k + 1];
                    v[k + 1] = v[k];
                    v[k] = temp;
                    doitTrier = true;
                    arraytemp = mat[k + 1];
                    mat[k + 1] = mat[k];
                    mat[k] = arraytemp;
                }
            }
        }
    }
}
