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
 * Vector3D.java
 *
 * Created on April 19, 2004, 10:50 AM
 */
package org.jscience.chemistry.quantum.math.vector;

import org.jscience.chemistry.quantum.math.matrix.Matrix;


/**
 * Defines an R^n space vector.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class VectorND implements Cloneable {
    /** Holds value of property size. */
    protected int size;

    /** Holds value of property vector. */
    protected double[] vector;

/**
     * Creates a new instance of Vector
     *
     * @param size DOCUMENT ME!
     */
    public VectorND(int size) {
        vector = new double[size];
        this.size = size;
    }

/**
     * Creates a new VectorND object.
     *
     * @param array DOCUMENT ME!
     */
    public VectorND(double[] array) {
        this.vector = array;
        this.size = this.vector.length;
    }

/**
     * Creates a new instance of Vector from a square Matrix
     *
     * @param a the square matrix from which this vector will be made
     */
    public VectorND(Matrix a) {
        double[][] matrix = a.getMatrix();

        this.vector = new double[matrix.length * matrix.length];
        this.size = this.vector.length;

        int ii = 0;
        int i;
        int j;

        for (i = 0; i < matrix.length; i++) {
            for (j = 0; j < matrix.length; j++) {
                this.vector[ii] = matrix[i][j];
                ii++;
            } // end for
        } // end for
    }

    /**
     * Make this vector a Null vector
     */
    public void makeZero() {
        for (int i = 0; i < size; i++)
            vector[i] = 0.0;
    }

    /**
     * Getter for property size.
     *
     * @return Value of property size.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Setter for property size.
     *
     * @param size New value of property size.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Indexed getter for property vector.
     *
     * @param index Index of the property.
     *
     * @return Value of the property at <CODE>index</CODE>.
     */
    public double getVector(int index) {
        return this.vector[index];
    }

    /**
     * Getter for property vector.
     *
     * @return Value of property vector.
     */
    public double[] getVector() {
        return this.vector;
    }

    /**
     * Indexed setter for property vector.
     *
     * @param index Index of the property.
     * @param vector New value of the property at <CODE>index</CODE>.
     */
    public void setVector(int index, double vector) {
        this.vector[index] = vector;
    }

    /**
     * Setter for property vector.
     *
     * @param vector New value of property vector.
     */
    public void setVector(double[] vector) {
        this.vector = vector;
    }

    /**
     * addition of two vectors
     *
     * @param b : to be added to the current vector
     *
     * @return the addition of two vector or null if that is not possible
     */
    public VectorND add(VectorND b) {
        if (this.size != b.size) {
            return null;
        } // end if

        VectorND result = new VectorND(size);

        for (int i = 0; i < size; i++) {
            result.vector[i] = this.vector[i] + b.vector[i];
        } // end for

        return result;
    }

    /**
     * substraction of two vectors (this - b)
     *
     * @param b : to be substracted from the current vector
     *
     * @return the addition of two vector or null if that is not possible
     */
    public VectorND sub(VectorND b) {
        if (this.size != b.size) {
            return null;
        } // end if

        VectorND result = new VectorND(size);

        for (int i = 0; i < size; i++) {
            result.vector[i] = this.vector[i] - b.vector[i];
        } // end for

        return result;
    }

    /**
     * multiplication of this vector with a scalar k
     *
     * @param k k : the scalar to be multiplied to this vector
     *
     * @return the result !
     */
    public VectorND mul(double k) {
        VectorND result = new VectorND(size);

        for (int i = 0; i < size; i++) {
            result.vector[i] = this.vector[i] * k;
        } // end for

        return result;
    }

    /**
     * The dot product of two vectors (a.b)
     *
     * @param b vector with which the dot product is to be evaluated
     *
     * @return a double value which is the result of the dot product
     */
    public double dot(VectorND b) {
        if (this.size != b.size) {
            return Double.NaN;
        } // end if

        double result = 0.0;

        for (int i = 0; i < size; i++) {
            result += (this.vector[i] * b.vector[i]);
        } // end for

        return result;
    }

    /**
     * The magnitude of this vector
     *
     * @return the magnitude (length) of this vector.
     */
    public double magnitude() {
        double length;

        length = 0.0;

        for (int i = 0; i < size; i++) {
            length += (vector[i] * vector[i]);
        } // end for

        return Math.sqrt(length);
    }

    /**
     * get the normalized form of this vector
     *
     * @return the normalized form of this vector
     */
    public VectorND normalize() {
        double magnitude = magnitude();

        VectorND n = new VectorND(size);

        for (int i = 0; i < size; i++) {
            n.vector[i] = vector[i] / magnitude;
        } // end for

        return n;
    }

    /**
     * negate this vector
     *
     * @return the reverse vector
     */
    public VectorND negate() {
        double magnitude = magnitude();

        VectorND n = new VectorND(size);

        for (int i = 0; i < size; i++) {
            n.vector[i] = -vector[i];
        } // end for

        return n;
    }

    /**
     * clone this vector ;) Cloning is getting interesting! :)
     *
     * @return the clone
     */
    public Object clone() {
        VectorND theCopy = new VectorND(size);

        for (int i = 0; i < size; i++) {
            theCopy.vector[i] = vector[i];
        } // end for

        theCopy.size = size;

        return theCopy;
    }

    /**
     * the overridden toString() method
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < size; i++) {
            sb.append(vector[i]);
            sb.append("\n");
        } // end for

        return sb.toString();
    }
} // end of class Vector
