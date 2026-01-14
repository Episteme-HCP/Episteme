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

/*
 * JacobiDiagonalizer.java
 *
 * Created on August 4, 2004, 8:49 PM
 */
package org.jscience.chemistry.quantum.math.la;

import org.jscience.chemistry.quantum.math.matrix.Matrix;


/**
 * Jacobi diagonalization attempts to diagonalize a matrix such that after
 * O(N<SUP>3</SUP>) operations, the off-diagonal elements are made zero. So,
 * the diagonal elements now represent the eigen values. <br>
 * Jacobi diagonalization becomes inefficient for higher order matrices
 * because of use of matrix multiplications for diagonalizing the matrix.
 * Also, the diagonal elements made zero may become non-zero in successive
 * sweeps. <br>
 * Taken from <i>Numerical Recipes, section <b>11.1</b></i>.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class JacobiDiagonalizer extends Diagonalizer {
    // reference to actual matrix and eigen vector matrix

    /**
     * DOCUMENT ME!
     */
    private double[][] a;

    // reference to actual matrix and eigen vector matrix

    /**
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[][] v;

    // intermediate arrays for multiplications and other variables

    /**
     * DOCUMENT ME!
     */
    private double[] xp;

    // intermediate arrays for multiplications and other variables

    /**
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[] xq;

    // intermediate arrays for multiplications and other variables

    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[] yp;

    // intermediate arrays for multiplications and other variables

    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     * <p/>
     * DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     */
    private double[] yq;

    /**
     * DOCUMENT ME!
     */
    double[] b;

    /**
     * DOCUMENT ME!
     */
    double[] z;

    /**
     * DOCUMENT ME!
     */
    private double theta;

    /**
     * DOCUMENT ME!
     */
    private double tau;

    /**
     * DOCUMENT ME!
     */
    private double t;

    /**
     * DOCUMENT ME!
     */
    private double sum;

    /**
     * DOCUMENT ME!
     */
    private double sin;

    /**
     * DOCUMENT ME!
     */
    private double cos;

    /**
     * DOCUMENT ME!
     */
    private double h;

    /**
     * DOCUMENT ME!
     */
    private double g;

    /**
     * Creates a new instance of JacobiDiagonalizer
     */
    public JacobiDiagonalizer() {
        super();
    }

    /**
     * the diagonalization method, for the matrix A
     *
     * @param matrix - the matrix that is to be diagonalized
     */
    public void diagonalize(Matrix matrix) {
        int i;
        int j;
        int ip;
        int iq;

        // initilize 'v' as identity matrix
        eigenVectors = new Matrix(matrix.getRowCount());

        eigenVectors.makeIdentity();

        v = eigenVectors.getMatrix();
        a = ((Matrix) matrix.clone()).getMatrix();

        int n = a.length;

        eigenValues = new double[n];

        b = new double[n];
        z = new double[n];

        // init other arrays
        // initilize b and eigenValues to diagonal of a
        // and z to zero
        for (ip = 0; ip < n; ip++) {
            eigenValues[ip] = b[ip] = a[ip][ip];
            z[ip] = 0.0;
        } // end for

        // start of the diagonalizer
        for (int sweeps = 0; sweeps < maximumIteration; sweeps++) {
            sum = matrix.sumOffDiagonal();

            if (sum == 0.0) {
                break; // off diagonals are zero! thats what we wnt
            }

            // for the first three cycles keep zero tolerance a bit high
            // then make it zero!
            if (sweeps < 3) {
                zeroTolerance = (0.2 * sum) / (n * n);
            } else {
                zeroTolerance = 0.0;
            }

            // start zeroing off
            for (ip = 0; ip < (n - 1); ip++) {
                for (iq = ip + 1; iq < n; iq++) {
                    g = 100.0 * Math.abs(a[ip][iq]);

                    // after 4 sweeps, skip rotation if off diagonal element
                    // is small
                    if ((sweeps > 4) &&
                            ((float) (Math.abs(eigenValues[ip]) + g) == (float) Math.abs(
                                    eigenValues[ip])) &&
                            ((float) (Math.abs(eigenValues[iq]) + g) == (float) Math.abs(
                                    eigenValues[iq]))) {
                        a[ip][iq] = 0.0;
                    } else if (Math.abs(a[ip][iq]) > zeroTolerance) {
                        h = eigenValues[iq] - eigenValues[ip];

                        if (((float) (Math.abs(h) + g)) == (float) Math.abs(h)) {
                            t = a[ip][iq] / h;
                        } else {
                            theta = (0.5 * h) / a[ip][iq];
                            t = 1.0 / (Math.abs(theta) +
                                    Math.sqrt(1.0 + (theta * theta)));

                            if (theta < 0.0) {
                                t = -t;
                            }
                        } // end if

                        cos = 1.0 / Math.sqrt(1.0 + (t * t));
                        sin = t * cos;
                        tau = sin / (1.0 + cos);
                        h = t * a[ip][iq];

                        z[ip] -= h;
                        z[iq] += h;
                        eigenValues[ip] -= h;
                        eigenValues[iq] += h;

                        a[ip][iq] = 0.0;

                        // do appropriate rotations
                        for (j = 0; j < ip; j++) {
                            doRotate(a, j, ip, j, iq);
                        }

                        for (j = ip + 1; j < iq; j++) {
                            doRotate(a, ip, j, j, iq);
                        }

                        for (j = iq + 1; j < n; j++) {
                            doRotate(a, ip, j, iq, j);
                        }

                        for (j = 0; j < n; j++) {
                            doRotate(v, j, ip, j, iq);
                        }
                    } // end if
                } // end for
            } // end for

            // update eigenValues and reinit z
            for (ip = 0; ip < n; ip++) {
                b[ip] += z[ip];
                eigenValues[ip] = b[ip];
                z[ip] = 0.0;
            } // end for
        } // end for

        // sort the eigen value in ascending order, if requested
        if (eigenSort) {
            sortEigenValues();
        }

        // V' so that rows are now eigen vectors
        eigenVectors = eigenVectors.transpose();
    }

    /**
     * Do the rotation
     *
     * @param a DOCUMENT ME!
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     * @param l DOCUMENT ME!
     */
    private void doRotate(double[][] a, int i, int j, int k, int l) {
        g = a[i][j];
        h = a[k][l];

        a[i][j] = g - (sin * (h + (g * tau)));
        a[k][l] = h + (sin * (g - (h * tau)));
    }

    /**
     * Sort the eigenvalues in ascending order as well as eigenvactors using
     * simple insertion sort.
     */
    private void sortEigenValues() {
        int i;
        int j;
        int k;
        double p;
        int n = v.length;

        for (i = 0; i < n; i++) {
            p = eigenValues[k = i];

            for (j = i + 1; j < n; j++) {
                if (eigenValues[j] <= p) {
                    p = eigenValues[k = j];
                }
            } // end for

            if (k != j) { // swap
                eigenValues[k] = eigenValues[i];
                eigenValues[i] = p;

                for (j = 0; j < n; j++) { // swap eigenVectors
                    p = v[j][i];
                    v[j][i] = v[j][k];
                    v[j][k] = p;
                } // end for
            } // end if
        } // end for
    }
} // end of class JacobiDiagonalizer
