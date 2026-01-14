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

package org.jscience.physics.relativity;

import org.jscience.JScience;

import org.jscience.util.IllegalDimensionException;


/**
 * The Rank3Tensor class encapsulates 3rd rank tensors.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class Rank3Tensor extends Tensor {
    /** DOCUMENT ME! */
    protected double[][][] rank3;

/**
     * Constructs a 3rd rank tensor.
     */
    public Rank3Tensor() {
        rank3 = new double[4][4][4];
    }

    /**
     * Compares two tensors for equality.
     *
     * @param a a 3rd rank tensor
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        if (a instanceof Rank3Tensor) {
            Rank3Tensor v = (Rank3Tensor) a;

            for (int j, i = 0; i < 4; i++) {
                for (j = 0; j < 4; j++) {
                    if ((Math.abs(rank3[i][j][0] - v.rank3[i][j][0]) > Double.valueOf(
                                JScience.getProperty("tolerance")).doubleValue()) ||
                            (Math.abs(rank3[i][j][1] - v.rank3[i][j][1]) > Double.valueOf(
                                JScience.getProperty("tolerance")).doubleValue()) ||
                            (Math.abs(rank3[i][j][2] - v.rank3[i][j][2]) > Double.valueOf(
                                JScience.getProperty("tolerance")).doubleValue()) ||
                            (Math.abs(rank3[i][j][3] - v.rank3[i][j][3]) > Double.valueOf(
                                JScience.getProperty("tolerance")).doubleValue())) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Returns a component of this tensor.
     *
     * @param i 1st index
     * @param j 2nd index
     * @param k 3rd index
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public double getComponent(int i, int j, int k) {
        if ((i >= 0) && (i < 4) && (j >= 0) && (j < 4) && (k >= 0) && (k < 4)) {
            return rank3[i][j][k];
        } else {
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Sets the value of a component of this tensor.
     *
     * @param i 1st index
     * @param j 2nd index
     * @param k 3rd index
     * @param x value
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public void setComponent(int i, int j, int k, double x) {
        if ((i >= 0) && (i < 4) && (j >= 0) && (j < 4) && (k >= 0) && (k < 4)) {
            rank3[i][j][k] = x;
        } else {
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    //============
    // OPERATIONS
    //============
    // ADDITION
    /**
     * Returns the addition of this tensor and another.
     *
     * @param t a 3rd rank tensor
     *
     * @return DOCUMENT ME!
     */
    public Rank3Tensor add(Rank3Tensor t) {
        Rank3Tensor ans = new Rank3Tensor();

        for (int j, i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                ans.setComponent(i, j, 0, rank3[i][j][0] + t.rank3[i][j][0]);
                ans.setComponent(i, j, 1, rank3[i][j][1] + t.rank3[i][j][1]);
                ans.setComponent(i, j, 2, rank3[i][j][2] + t.rank3[i][j][2]);
                ans.setComponent(i, j, 3, rank3[i][j][3] + t.rank3[i][j][3]);
            }
        }

        return ans;
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this tensor by another.
     *
     * @param t a 3rd rank tensor
     *
     * @return DOCUMENT ME!
     */
    public Rank3Tensor subtract(Rank3Tensor t) {
        Rank3Tensor ans = new Rank3Tensor();

        for (int j, i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                ans.setComponent(i, j, 0, rank3[i][j][0] - t.rank3[i][j][0]);
                ans.setComponent(i, j, 1, rank3[i][j][1] - t.rank3[i][j][1]);
                ans.setComponent(i, j, 2, rank3[i][j][2] - t.rank3[i][j][2]);
                ans.setComponent(i, j, 3, rank3[i][j][3] - t.rank3[i][j][3]);
            }
        }

        return ans;
    }

    // TENSOR PRODUCT
    /**
     * Returns the tensor product of this tensor and another.
     *
     * @param t a 1st rank tensor
     *
     * @return DOCUMENT ME!
     */
    public Rank4Tensor tensorProduct(Rank1Tensor t) {
        Rank4Tensor ans = new Rank4Tensor();

        for (int k, j, i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                for (k = 0; k < 4; k++) {
                    ans.setComponent(i, j, k, 0,
                        rank3[i][j][k] * t.getComponent(0));
                    ans.setComponent(i, j, k, 1,
                        rank3[i][j][k] * t.getComponent(1));
                    ans.setComponent(i, j, k, 2,
                        rank3[i][j][k] * t.getComponent(2));
                    ans.setComponent(i, j, k, 3,
                        rank3[i][j][k] * t.getComponent(3));
                }
            }
        }

        return ans;
    }
}
