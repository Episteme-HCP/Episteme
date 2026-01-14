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
 * Diagonalizer.java
 *
 * Created on August 4, 2004, 6:42 AM
 */
package org.jscience.chemistry.quantum.math.la;

import org.jscience.chemistry.quantum.math.matrix.Matrix;


/**
 * Abstract class defining how a matrix diagonalizers public interfaces
 * should be. <br>
 * Caution! Diagonalizers only work with real square matrices ;)
 *
 * @author V.Ganesh
 * @version 1.0
 */
public abstract class Diagonalizer {
    // the default required precision value
    /** DOCUMENT ME! */
    private final static double DEFAULT_RMS_TOLERANCE = 1e-7;

    /** DOCUMENT ME! */
    private final static double DEFAULT_ZERO_TOLERANCE = 1e-12;

    // the default maximum sweeps done by the diagonalizing algorithm
    /** DOCUMENT ME! */
    private final static int DEFAULT_MAX_ITERATIONS = 50;

    /** Eigen vectors one per row */
    protected Matrix eigenVectors;

    /** the eigen values */
    protected double[] eigenValues;

    /** Holds value of property maximumIteration. */
    protected int maximumIteration;

    /** Holds value of property zeroTolerance. */
    protected double zeroTolerance;

    /** Holds value of property rmsTolerance. */
    protected double rmsTolerance;

    /** Holds value of property eigenSort. */
    protected boolean eigenSort;

/**
     * Creates a new instance of Diagonalizer
     */
    public Diagonalizer() {
        zeroTolerance = DEFAULT_ZERO_TOLERANCE;
        rmsTolerance = DEFAULT_RMS_TOLERANCE;

        maximumIteration = DEFAULT_MAX_ITERATIONS;

        eigenSort = true;
    }

    /**
     * the diagonalization method, for the matrix A
     *
     * @param matrix - the matrix that is to be diagonalized
     */
    public abstract void diagonalize(Matrix matrix);

    /**
     * get the eigen vectors
     *
     * @return the eigen vectors in a Matrix object
     */
    public Matrix getEigenVectors() {
        return eigenVectors;
    }

    /**
     * get the eigen values
     *
     * @return the eigen values as an array of doubles
     */
    public double[] getEigenValues() {
        return eigenValues;
    }

    /**
     * Getter for property maximumIteration.
     *
     * @return Value of property maximumIteration.
     */
    public int getMaximumIteration() {
        return this.maximumIteration;
    }

    /**
     * Setter for property maximumIteration.
     *
     * @param maximumIteration New value of property maximumIteration.
     */
    public void setMaximumIteration(int maximumIteration) {
        this.maximumIteration = maximumIteration;
    }

    /**
     * Getter for property zeroTolerance.
     *
     * @return Value of property zeroTolerance.
     */
    public double getZeroTolerance() {
        return this.zeroTolerance;
    }

    /**
     * Setter for property zeroTolerance.
     *
     * @param zeroTolerance New value of property zeroTolerance.
     */
    public void setZeroTolerance(double zeroTolerance) {
        this.zeroTolerance = zeroTolerance;
    }

    /**
     * Getter for property rmsTolerance.
     *
     * @return Value of property rmsTolerance.
     */
    public double getRmsTolerance() {
        return this.rmsTolerance;
    }

    /**
     * Setter for property rmsTolerance.
     *
     * @param rmsTolerance New value of property rmsTolerance.
     */
    public void setRmsTolerance(double rmsTolerance) {
        this.rmsTolerance = rmsTolerance;
    }

    /**
     * Getter for property eigenSort.
     *
     * @return Value of property eigenSort.
     */
    public boolean isEigenSort() {
        return this.eigenSort;
    }

    /**
     * Setter for property eigenSort.
     *
     * @param eigenSort New value of property eigenSort.
     */
    public void setEigenSort(boolean eigenSort) {
        this.eigenSort = eigenSort;
    }
} // end of interface Diagonalizer
