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

import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.analysis.PrimitiveMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * The IntegerSparseVector class encapsulates sparse vectors. Uses
 * Morse-coding.
 *
 * @author Silvere Martin-Michiellot
 */
public class IntegerSparseVector extends AbstractIntegerVector
    implements Cloneable, Serializable {
    /** DOCUMENT ME! */
    private int[] vector;

    /**
     * Sparse indexing data. Contains the component positions of each
     * element, e.g. <code>pos[n]</code> is the component position of the
     * <code>n</code>th element (the <code>pos[n]</code>th component is stored
     * at index <code>n</code>).
     */
    private int[] pos;

/**
     * Constructs an empty vector.
     *
     * @param dim the dimension of the vector.
     */
    public IntegerSparseVector(final int dim) {
        super(dim);
        vector = new int[0];
        pos = new int[0];
    }

/**
     * Constructs a vector from an array.
     *
     * @param array DOCUMENT ME!
     */
    public IntegerSparseVector(final int[] array) {
        super(array.length);

        int n = 0;

        for (int i = 0; i < getDimension(); i++) {
            if (array[i] != 0) {
                n++;
            }
        }

        vector = new int[n];
        pos = new int[n];
        n = 0;

        for (int i = 0; i < getDimension(); i++) {
            if (array[i] != 0) {
                vector[n] = array[i];
                pos[n] = i;
                n++;
            }
        }
    }

/**
     * Constructs a vector by copying a vector.
     *
     * @param vec an assigned value
     */
    public IntegerSparseVector(final AbstractIntegerVector vec) {
        super(vec.getDimension());

        if (vec instanceof IntegerSparseVector) {
            IntegerSparseVector vect = (IntegerSparseVector) vec;
            vector = new int[vect.vector.length];
            pos = new int[vect.pos.length];
            System.arraycopy(vect.vector, 0, vector, 0, vec.getDimension());
            System.arraycopy(vect.pos, 0, pos, 0, vec.getDimension());
        } else {
            int n = 0;

            for (int i = 0; i < getDimension(); i++) {
                if (vec.getPrimitiveElement(i) != 0) {
                    n++;
                }
            }

            vector = new int[n];
            pos = new int[n];
            n = 0;

            for (int i = 0; i < getDimension(); i++) {
                if (vec.getPrimitiveElement(i) != 0) {
                    vector[n] = vec.getPrimitiveElement(i);
                    pos[n] = i;
                    n++;
                }
            }
        }
    }

    /**
     * Compares two vectors for equality.
     *
     * @param obj a Integer sparse vector
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj, double tol) {
        if (obj != null) {
            if (obj instanceof IntegerSparseVector) {
                if (getDimension() == ((IntegerSparseVector) obj).getDimension()) {
                    IntegerSparseVector v = (IntegerSparseVector) obj;

                    if (pos.length != v.pos.length) {
                        return false;
                    }

                    double sumSqr = 0.0;

                    for (int i = 0; i < pos.length; i++) {
                        if (pos[i] != v.pos[i]) {
                            return false;
                        }

                        double delta = vector[i] - v.vector[i];
                        sumSqr += (delta * delta);
                    }

                    return (sumSqr <= (tol * tol));
                } else {
                    return false;
                }
            } else {
                if (obj instanceof AbstractIntegerVector) {
                    return ((AbstractIntegerVector) obj).equals(this);
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * Converts this vector to a double vector.
     *
     * @return a double vector
     */
    public AbstractDoubleVector toDoubleVector() {
        final double[] array = new double[getDimension()];

        for (int i = 0; i < pos.length; i++)
            array[pos[i]] = vector[i];

        return new DoubleVector(array);
    }

    /**
     * Converts this vector to a complex vector.
     *
     * @return a complex vector
     */
    public AbstractComplexVector toComplexVector() {
        final double[] arrayRe = new double[getDimension()];

        for (int i = 0; i < pos.length; i++)
            arrayRe[pos[i]] = vector[i];

        return new ComplexVector(arrayRe, new double[getDimension()]);
    }

    /**
     * Returns a component of this vector.
     *
     * @param n index of the vector component
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public int getPrimitiveElement(final int n) {
        if ((n < 0) || (n >= getDimension())) {
            throw new IllegalDimensionException(getInvalidElementMsg(n));
        }

        for (int k = 0; k < pos.length; k++) {
            if (pos[k] == n) {
                return vector[k];
            }
        }

        return 0;
    }

    /**
     * Sets the value of a component of this vector.
     *
     * @param n index of the vector component
     * @param x a number
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public void setElement(final int n, final int x) {
        if ((n < 0) || (n >= getDimension())) {
            throw new IllegalDimensionException(getInvalidElementMsg(n));
        }

        if (x == 0) {
            return;
        }

        for (int k = 0; k < pos.length; k++) {
            if (n == pos[k]) {
                vector[k] = x;

                return;
            }
        }

        int[] newPos = new int[pos.length + 1];
        int[] newVector = new int[vector.length + 1];
        System.arraycopy(pos, 0, newPos, 0, pos.length);
        System.arraycopy(vector, 0, newVector, 0, pos.length);
        newPos[pos.length] = n;
        newVector[vector.length] = x;
        pos = newPos;
        vector = newVector;
    }

    /**
     * Sets the value of all elements of the vector. You should think
     * about using a IntegerVector.
     *
     * @param r a int element
     */
    public void setElements(final int r) {
        if (r == 0) {
            vector = new int[0];
            pos = new int[0];
        } else {
            pos = new int[getDimension()];
            vector = new int[getDimension()];

            for (int i = 0; i < getDimension(); i++) {
                vector[i] = r;
                pos[i] = i;
            }
        }
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude).
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public double norm() {
        if (getDimension() > 0) {
            return Math.sqrt(sumSquares());
        } else {
            throw new ArithmeticException(
                "The norm of a zero dimension vector is undefined.");
        }
    }

    /**
     * Returns the sum of the squares of the components.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public int sumSquares() {
        if (getDimension() > 0) {
            int norm = 0;

            for (int k = 0; k < pos.length; k++)
                norm += (vector[k] * vector[k]);

            return norm;
        } else {
            throw new ArithmeticException(
                "The sum of squares of a zero dimension vector is undefined.");
        }
    }

    /**
     * Returns the mass.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public int mass() {
        if (getDimension() > 0) {
            int mass = 0;

            for (int k = 0; k < pos.length; k++)
                mass += vector[k];

            return mass;
        } else {
            throw new ArithmeticException(
                "The mass of a zero dimension vector is undefined.");
        }
    }

    //============
    // OPERATIONS
    //============
    /**
     * Returns the negative of this vector.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member negate() {
        final IntegerSparseVector ans = new IntegerSparseVector(getDimension());
        ans.vector = new int[vector.length];
        ans.pos = new int[pos.length];
        System.arraycopy(pos, 0, ans.pos, 0, pos.length);

        for (int i = 0; i < pos.length; i++)
            ans.vector[i] = -vector[i];

        return ans;
    }

    // ADDITION
    /**
     * Returns the addition of this vector and another.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(final AbelianGroup.Member v) {
        if (v instanceof AbstractIntegerVector) {
            return add((AbstractIntegerVector) v);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v an integer vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractIntegerVector add(final AbstractIntegerVector v) {
        if (v instanceof IntegerSparseVector) {
            return add((IntegerSparseVector) v);
        } else if (v instanceof IntegerVector) {
            return add((IntegerVector) v);
        } else {
            if (getDimension() != v.getDimension()) {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }

            int[] array = new int[getDimension()];
            array[0] = v.getPrimitiveElement(0);

            for (int i = 1; i < getDimension(); i++)
                array[i] = v.getPrimitiveElement(i);

            for (int i = 0; i < pos.length; i++)
                array[pos[i]] += vector[i];

            return new IntegerVector(array);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public IntegerVector add(final IntegerVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        int[] array = new int[getDimension()];
        System.arraycopy(v.vector, 0, array, 0, getDimension());

        for (int i = 0; i < pos.length; i++)
            array[pos[i]] += vector[i];

        return new IntegerVector(array);
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v an integer sparse vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public IntegerSparseVector add(final IntegerSparseVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        int[] array = new int[getDimension()];

        for (int i = 0; i < pos.length; i++)
            array[pos[i]] = vector[i] + v.getPrimitiveElement(pos[i]);

        for (int m, i = 0; i < v.pos.length; i++) {
            m = v.pos[i];
            array[m] = getPrimitiveElement(m) + v.vector[i];
        }

        return new IntegerSparseVector(array);
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member v) {
        if (v instanceof AbstractIntegerVector) {
            return subtract((AbstractIntegerVector) v);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v an integer vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractIntegerVector subtract(final AbstractIntegerVector v) {
        if (v instanceof IntegerSparseVector) {
            return subtract((IntegerSparseVector) v);
        } else if (v instanceof IntegerVector) {
            return subtract((IntegerVector) v);
        } else {
            if (getDimension() != v.getDimension()) {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }

            int[] array = new int[getDimension()];
            array[0] = -v.getPrimitiveElement(0);

            for (int i = 1; i < getDimension(); i++)
                array[i] = -v.getPrimitiveElement(i);

            for (int i = 0; i < pos.length; i++)
                array[pos[i]] += vector[i];

            return new IntegerVector(array);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public IntegerVector subtract(final IntegerVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        int[] array = new int[getDimension()];
        array[0] = -v.vector[0];

        for (int i = 1; i < getDimension(); i++)
            array[i] = -v.vector[i];

        for (int i = 0; i < pos.length; i++)
            array[pos[i]] += vector[i];

        return new IntegerVector(array);
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v an integer sparse vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public IntegerSparseVector subtract(final IntegerSparseVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        int[] array = new int[getDimension()];

        for (int i = 0; i < pos.length; i++)
            array[pos[i]] = vector[i] - v.getPrimitiveElement(pos[i]);

        for (int m, i = 0; i < v.pos.length; i++) {
            m = v.pos[i];
            array[m] = getPrimitiveElement(m) - v.vector[i];
        }

        return new IntegerSparseVector(array);
    }

    // SCALAR MULTIPLICATION
    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Module.Member scalarMultiply(final Ring.Member x) {
        if (x instanceof Integer) {
            return scalarMultiply(((Integer) x).value());
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param x an integer
     *
     * @return DOCUMENT ME!
     */
    public AbstractIntegerVector scalarMultiply(final int x) {
        final IntegerSparseVector ans = new IntegerSparseVector(getDimension());
        ans.vector = new int[vector.length];
        ans.pos = new int[pos.length];
        System.arraycopy(pos, 0, ans.pos, 0, pos.length);

        for (int i = 0; i < pos.length; i++)
            ans.vector[i] = x * vector[i];

        return ans;
    }

    // SCALAR PRODUCT
    /**
     * Returns the scalar product of this vector and another.
     *
     * @param v an integer vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public int scalarProduct(final AbstractIntegerVector v) {
        if (v instanceof IntegerSparseVector) {
            return scalarProduct((IntegerSparseVector) v);
        } else if (v instanceof IntegerVector) {
            return scalarProduct((IntegerVector) v);
        } else {
            if (getDimension() != v.getDimension()) {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }

            int ps = 0;

            for (int i = 0; i < pos.length; i++)
                ps += (vector[i] * v.getPrimitiveElement(pos[i]));

            return ps;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public int scalarProduct(final IntegerVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        int ps = 0;

        for (int i = 0; i < pos.length; i++)
            ps += (vector[i] * v.vector[pos[i]]);

        return ps;
    }

    /**
     * Returns the scalar product of this vector and another.
     *
     * @param v an integer sparse vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public int scalarProduct(final IntegerSparseVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        int ps = 0;

        if (pos.length <= v.pos.length) {
            for (int i = 0; i < pos.length; i++)
                ps += (vector[i] * v.getPrimitiveElement(pos[i]));
        } else {
            for (int i = 0; i < v.pos.length; i++)
                ps += (getPrimitiveElement(v.pos[i]) * v.vector[i]);
        }

        return ps;
    }

    // TENSOR PRODUCT
    /**
     * Returns the tensor product of this vector and another.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IntegerSparseMatrix tensorProduct(final IntegerSparseVector v) {
        IntegerSparseMatrix ans = new IntegerSparseMatrix(getDimension(),
                v.getDimension());

        for (int j, i = 0; i < pos.length; i++) {
            for (j = 0; j < v.pos.length; j++)
                ans.setElement(pos[i], v.pos[j], vector[i] * v.vector[j]);
        }

        return ans;
    }

    // MAP COMPONENTS
    /**
     * Applies a function on all the vector components.
     *
     * @param f a user-defined function
     *
     * @return an integer sparse vector
     */
    public AbstractDoubleVector mapElements(final PrimitiveMapping f) {
        final double[] ans = new double[getDimension()];
        double val = f.map(0);

        for (int i = 0; i < getDimension(); i++) {
            ans[i] = val;
        }

        for (int i = 0; i < pos.length; i++)
            ans[pos[i]] = f.map(vector[pos[i]]);

        return new DoubleVector(ans);
    }

    /**
     * Projects the vector to an array.
     *
     * @return an integer array.
     */
    public int[] toPrimitiveArray() {
        final int[] result = new int[getDimension()];

        for (int i = 0; i < pos.length; i++)
            result[pos[i]] = vector[pos[i]];

        return result;
    }

    /**
     * Clone vector into a new vector.
     *
     * @return the cloned vector.
     */
    public Object clone() {
        return new IntegerSparseVector(this);
    }
}
