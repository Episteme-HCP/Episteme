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

import org.jscience.mathematics.algebraic.matrices.DoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The Rank1Tensor class encapsulates 1st rank tensors.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class Rank1Tensor extends Tensor {
    /** DOCUMENT ME! */
    protected double[] rank1;

/**
     * Constructs a 1st rank tensor.
     */
    public Rank1Tensor() {
        rank1 = new double[4];
    }

/**
     * Constructs a 1st rank tensor.
     *
     * @param s a scalar
     * @param v a 3-vector
     */
    public Rank1Tensor(double s, DoubleVector v) {
        this();
        rank1[0] = s;
        rank1[1] = v.getPrimitiveElement(0);
        rank1[2] = v.getPrimitiveElement(1);
        rank1[3] = v.getPrimitiveElement(2);
    }

/**
     * Constructs a 1st rank tensor.
     *
     * @param s  a scalar
     * @param v1 1st 3-vector component
     * @param v2 2nd 3-vector component
     * @param v3 3rd 3-vector component
     */
    public Rank1Tensor(double s, double v1, double v2, double v3) {
        this();
        rank1[0] = s;
        rank1[1] = v1;
        rank1[2] = v2;
        rank1[3] = v3;
    }

    /**
     * Compares two tensors for equality.
     *
     * @param a a 1st rank tensor
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        if (a instanceof Rank1Tensor) {
            Rank1Tensor v = (Rank1Tensor) a;

            return (Math.abs(rank1[0] - v.rank1[0]) <= Double.valueOf(JScience.getProperty(
                    "tolerance")).doubleValue()) &&
            (Math.abs(rank1[1] - v.rank1[1]) <= Double.valueOf(JScience.getProperty(
                    "tolerance")).doubleValue()) &&
            (Math.abs(rank1[2] - v.rank1[2]) <= Double.valueOf(JScience.getProperty(
                    "tolerance")).doubleValue()) &&
            (Math.abs(rank1[3] - v.rank1[3]) <= Double.valueOf(JScience.getProperty(
                    "tolerance")).doubleValue());
        }

        return false;
    }

    /**
     * Returns a comma delimited string representing the value of this
     * tensor.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new String(rank1[0] + "," + rank1[1] + "," + rank1[2] + "," +
            rank1[3]);
    }

    /**
     * Returns a hashcode for this tensor.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return (int) Math.exp(norm());
    }

    /**
     * Returns a component of this tensor.
     *
     * @param i 1st index
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public double getComponent(int i) {
        if ((i >= 0) && (i < 4)) {
            return rank1[i];
        } else {
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Sets the value of a component of this tensor.
     *
     * @param i 1st index
     * @param x value
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public void setComponent(int i, double x) {
        if ((i >= 0) && (i < 4)) {
            rank1[i] = x;
        } else {
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Returns the norm (invariant).
     *
     * @return DOCUMENT ME!
     */
    public double norm() {
        return Math.sqrt((rank1[0] * rank1[0]) - (rank1[1] * rank1[1]) -
            (rank1[2] * rank1[2]) - (rank1[3] * rank1[3]));
    }

    //============
    // OPERATIONS
    //============
    // ADDITION
    /**
     * Returns the addition of this tensor and another.
     *
     * @param t a 1st rank tensor
     *
     * @return DOCUMENT ME!
     */
    public Rank1Tensor add(Rank1Tensor t) {
        Rank1Tensor ans = new Rank1Tensor();
        ans.rank1[0] = rank1[0] + t.rank1[0];
        ans.rank1[1] = rank1[1] + t.rank1[1];
        ans.rank1[2] = rank1[2] + t.rank1[2];
        ans.rank1[3] = rank1[3] + t.rank1[3];

        return ans;
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this tensor by another.
     *
     * @param t a 1st rank tensor
     *
     * @return DOCUMENT ME!
     */
    public Rank1Tensor subtract(Rank1Tensor t) {
        Rank1Tensor ans = new Rank1Tensor();
        ans.rank1[0] = rank1[0] - t.rank1[0];
        ans.rank1[1] = rank1[1] - t.rank1[1];
        ans.rank1[2] = rank1[2] - t.rank1[2];
        ans.rank1[3] = rank1[3] - t.rank1[3];

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
    public Rank2Tensor tensorProduct(Rank1Tensor t) {
        Rank2Tensor ans = new Rank2Tensor();

        for (int i = 0; i < 4; i++) {
            ans.setComponent(i, 0, rank1[i] * t.rank1[0]);
            ans.setComponent(i, 1, rank1[i] * t.rank1[1]);
            ans.setComponent(i, 2, rank1[i] * t.rank1[2]);
            ans.setComponent(i, 3, rank1[i] * t.rank1[3]);
        }

        return ans;
    }

    /**
     * Returns the tensor product of this tensor and another.
     *
     * @param t a 2nd rank tensor
     *
     * @return DOCUMENT ME!
     */
    public Rank3Tensor tensorProduct(Rank2Tensor t) {
        Rank3Tensor ans = new Rank3Tensor();

        for (int j, i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                ans.setComponent(i, j, 0, rank1[i] * t.getComponent(j, 0));
                ans.setComponent(i, j, 1, rank1[i] * t.getComponent(j, 1));
                ans.setComponent(i, j, 2, rank1[i] * t.getComponent(j, 2));
                ans.setComponent(i, j, 3, rank1[i] * t.getComponent(j, 3));
            }
        }

        return ans;
    }

    /**
     * Returns the tensor product of this tensor and another.
     *
     * @param t a 3rd rank tensor
     *
     * @return DOCUMENT ME!
     */
    public Rank4Tensor tensorProduct(Rank3Tensor t) {
        Rank4Tensor ans = new Rank4Tensor();

        for (int k, j, i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                for (k = 0; k < 4; k++) {
                    ans.setComponent(i, j, k, 0,
                        rank1[i] * t.getComponent(j, k, 0));
                    ans.setComponent(i, j, k, 1,
                        rank1[i] * t.getComponent(j, k, 1));
                    ans.setComponent(i, j, k, 2,
                        rank1[i] * t.getComponent(j, k, 2));
                    ans.setComponent(i, j, k, 3,
                        rank1[i] * t.getComponent(j, k, 3));
                }
            }
        }

        return ans;
    }
}
