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

import org.jscience.JScience;

import org.jscience.mathematics.algebraic.algebras.HilbertSpace;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.analysis.ComplexMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * The ComplexSparseVector class encapsulates sparse vectors. Uses
 * Morse-coding.
 *
 * @author Silvere Martin-Michiellot
 */
public class ComplexSparseVector extends AbstractComplexVector
    implements Cloneable, Serializable {
    /** DOCUMENT ME! */
    private Complex[] vector;

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
    public ComplexSparseVector(final int dim) {
        super(dim);
        vector = new Complex[0];
        pos = new int[0];
    }

/**
     * Constructs a vector from an array.
     *
     * @param array DOCUMENT ME!
     */
    public ComplexSparseVector(final Complex[] array) {
        super(array.length);

        int n = 0;

        for (int i = 0; i < getDimension(); i++) {
            if (!array[i].equals(Complex.ZERO)) {
                n++;
            }
        }

        vector = new Complex[n];
        pos = new int[n];
        n = 0;

        for (int i = 0; i < getDimension(); i++) {
            if (!array[i].equals(Complex.ZERO)) {
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
    public ComplexSparseVector(final AbstractComplexVector vec) {
        super(vec.getDimension());

        if (vec instanceof ComplexSparseVector) {
            ComplexSparseVector vect = (ComplexSparseVector) vec;
            vector = new Complex[vect.vector.length];
            pos = new int[vect.pos.length];
            System.arraycopy(vect.vector, 0, vector, 0, vec.getDimension());
            System.arraycopy(vect.pos, 0, pos, 0, vec.getDimension());
        } else {
            int n = 0;

            for (int i = 0; i < getDimension(); i++) {
                if (!vec.getPrimitiveElement(i).equals(Complex.ZERO)) {
                    n++;
                }
            }

            vector = new Complex[n];
            pos = new int[n];
            n = 0;

            for (int i = 0; i < getDimension(); i++) {
                if (!vec.getPrimitiveElement(i).equals(Complex.ZERO)) {
                    vector[n] = vec.getPrimitiveElement(i);
                    pos[n] = i;
                    n++;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector real() {
        final DoubleVector result = new DoubleVector(getDimension());

        for (int i = 0; i < pos.length; i++) {
            result.setElement(pos[i], vector[pos[i]].real());
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector imag() {
        final DoubleVector result = new DoubleVector(getDimension());

        for (int i = 0; i < pos.length; i++) {
            result.setElement(pos[i], vector[pos[i]].imag());
        }

        return result;
    }

    /**
     * Compares two vectors for equality.
     *
     * @param obj a complex sparse vector
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj, double tol) {
        if (obj != null) {
            if (obj instanceof ComplexSparseVector) {
                if (getDimension() == ((ComplexSparseVector) obj).getDimension()) {
                    ComplexSparseVector v = (ComplexSparseVector) obj;

                    if (pos.length != v.pos.length) {
                        return false;
                    }

                    double sumSqr = 0.0;

                    for (int i = 0; i < pos.length; i++) {
                        if (pos[i] != v.pos[i]) {
                            return false;
                        }

                        double deltaRe = vector[i].real() - v.vector[i].real();
                        double deltaIm = vector[i].imag() - v.vector[i].imag();
                        sumSqr += ((deltaRe * deltaRe) + (deltaIm * deltaIm));
                    }

                    return (sumSqr <= (tol * tol));
                } else {
                    return false;
                }
            } else {
                if (obj instanceof AbstractComplexVector) {
                    return ((AbstractComplexVector) obj).equals(this);
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
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
    public Complex getPrimitiveElement(final int n) {
        if ((n < 0) || (n >= getDimension())) {
            throw new IllegalDimensionException(getInvalidElementMsg(n));
        }

        for (int k = 0; k < pos.length; k++) {
            if (pos[k] == n) {
                return vector[k];
            }
        }

        return Complex.ZERO;
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
    public void setElement(final int n, final Complex x) {
        if ((n < 0) || (n >= getDimension())) {
            throw new IllegalDimensionException(getInvalidElementMsg(n));
        }

        if ((Math.abs(x.real()) <= java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")).doubleValue()) &&
                (Math.abs(x.imag()) <= java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")).doubleValue())) {
            return;
        }

        for (int k = 0; k < pos.length; k++) {
            if (n == pos[k]) {
                vector[k] = x;

                return;
            }
        }

        int[] newPos = new int[pos.length + 1];
        Complex[] newVector = new Complex[vector.length + 1];
        System.arraycopy(pos, 0, newPos, 0, pos.length);
        System.arraycopy(vector, 0, newVector, 0, pos.length);
        newPos[pos.length] = n;
        newVector[vector.length] = x;
        pos = newPos;
        vector = newVector;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void setElement(final int n, final double x, final double y) {
        setElement(n, new Complex(x, y));
    }

    /**
     * Sets the value of all elements of the vector. You should think
     * about using a DoubleVector.
     *
     * @param r a ring element
     */
    public void setAllElements(final Complex r) {
        if (r.equals(Complex.ZERO)) {
            vector = new Complex[0];
            pos = new int[0];
        } else {
            pos = new int[getDimension()];
            vector = new Complex[getDimension()];

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
            if (pos.length > 0) {
                double answer = (vector[0].real() * vector[0].real()) +
                    (vector[0].imag() * vector[0].imag());

                for (int i = 1; i < pos.length; i++)
                    answer += ((vector[i].real() * vector[i].real()) +
                    (vector[i].imag() * vector[i].imag()));

                return Math.sqrt(answer);
            } else {
                return 0.0;
            }
        } else {
            throw new ArithmeticException(
                "The norm of a zero dimension vector is undefined.");
        }
    }

    /**
     * Makes the norm of this vector equal to 1.
     */
    public void normalize() {
        if (getDimension() > 0) {
            final double norm = norm();

            for (int i = 0; i < pos.length; i++)
                vector[i] = vector[i].divide(norm);
        }
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity"
     * src="doc-files/infinity.gif"></sup>-norm.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public double infNorm() {
        if (getDimension() > 0) {
            if (pos.length > 0) {
                double infNorm = (vector[0].real() * vector[0].real()) +
                    (vector[0].imag() * vector[0].imag());

                for (int i = 1; i < pos.length; i++) {
                    double mod = (vector[i].real() * vector[i].real()) +
                        (vector[i].imag() * vector[i].imag());

                    if (mod > infNorm) {
                        infNorm = mod;
                    }
                }

                return Math.sqrt(infNorm);
            } else {
                return 0.0;
            }
        } else {
            throw new ArithmeticException(
                "The inf norm of a zero dimension vector is undefined.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector conjugate() {
        return null; //To change body of implemented methods use File | Settings | File Templates.
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
        final ComplexSparseVector ans = new ComplexSparseVector(getDimension());
        ans.vector = new Complex[vector.length];
        ans.pos = new int[pos.length];
        System.arraycopy(pos, 0, ans.pos, 0, pos.length);

        for (int i = 0; i < pos.length; i++)
            ans.vector[i] = (Complex) vector[i].negate();

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
        if (v instanceof AbstractComplexVector) {
            return add((AbstractComplexVector) v);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v a complex vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractComplexVector add(final AbstractComplexVector v) {
        if (v instanceof ComplexSparseVector) {
            return add((ComplexSparseVector) v);
        } else if (v instanceof ComplexVector) {
            return add((ComplexVector) v);
        } else {
            if (getDimension() != v.getDimension()) {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }

            Complex[] array = new Complex[getDimension()];
            array[0] = v.getPrimitiveElement(0);

            for (int i = 1; i < getDimension(); i++)
                array[i] = v.getPrimitiveElement(i);

            for (int i = 0; i < pos.length; i++)
                array[pos[i]] = array[pos[i]].add(vector[i]);

            return new ComplexVector(array);
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
    public ComplexVector add(final ComplexVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        double[] arrayRe = new double[getDimension()];
        double[] arrayIm = new double[getDimension()];
        System.arraycopy(v.vectorRe, 0, arrayRe, 0, getDimension());
        System.arraycopy(v.vectorIm, 0, arrayIm, 0, getDimension());

        for (int i = 0; i < pos.length; i++) {
            arrayRe[pos[i]] += vector[i].real();
            arrayIm[pos[i]] += vector[i].imag();
        }

        return new ComplexVector(arrayRe, arrayIm);
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v a complex sparse vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public ComplexSparseVector add(final ComplexSparseVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        Complex[] array = new Complex[getDimension()];

        for (int i = 0; i < pos.length; i++)
            array[pos[i]] = vector[i].add(v.getPrimitiveElement(pos[i]));

        for (int m, i = 0; i < v.pos.length; i++) {
            m = v.pos[i];
            array[m] = getPrimitiveElement(m).add(v.vector[i]);
        }

        return new ComplexSparseVector(array);
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
        if (v instanceof AbstractComplexVector) {
            return subtract((AbstractComplexVector) v);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v a complex vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractComplexVector subtract(final AbstractComplexVector v) {
        if (v instanceof ComplexSparseVector) {
            return subtract((ComplexSparseVector) v);
        } else if (v instanceof ComplexVector) {
            return subtract((ComplexVector) v);
        } else {
            if (getDimension() != v.getDimension()) {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }

            Complex[] array = new Complex[getDimension()];
            array[0] = (Complex) v.getPrimitiveElement(0).negate();

            for (int i = 1; i < getDimension(); i++)
                array[i] = (Complex) v.getPrimitiveElement(i).negate();

            for (int i = 0; i < pos.length; i++)
                array[pos[i]] = array[pos[i]].add(vector[i]);

            return new ComplexVector(array);
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
    public ComplexVector subtract(final ComplexVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        double[] arrayRe = new double[getDimension()];
        double[] arrayIm = new double[getDimension()];
        arrayRe[0] = -v.vectorRe[0];
        arrayIm[0] = -v.vectorIm[0];

        for (int i = 1; i < getDimension(); i++) {
            arrayRe[i] = -v.vectorRe[i];
            arrayIm[i] = -v.vectorRe[i];
        }

        for (int i = 0; i < pos.length; i++) {
            arrayRe[pos[i]] += vector[i].real();
            arrayIm[pos[i]] += vector[i].imag();
        }

        return new ComplexVector(arrayRe, arrayIm);
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v a complex sparse vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public ComplexSparseVector subtract(final ComplexSparseVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        Complex[] array = new Complex[getDimension()];

        for (int i = 0; i < pos.length; i++)
            array[pos[i]] = vector[i].subtract(v.getPrimitiveElement(pos[i]));

        for (int m, i = 0; i < v.pos.length; i++) {
            m = v.pos[i];
            array[m] = getPrimitiveElement(m).subtract(v.vector[i]);
        }

        return new ComplexSparseVector(array);
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
        if (x instanceof Complex) {
            return scalarMultiply((Complex) x);
        } else if (x instanceof Double) {
            return scalarMultiply(((Double) x).value());
        } else if (x instanceof Integer) {
            return scalarMultiply(((Integer) x).value());
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param x a complex
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector scalarMultiply(final Complex x) {
        final ComplexSparseVector ans = new ComplexSparseVector(getDimension());
        ans.vector = new Complex[vector.length];
        ans.pos = new int[pos.length];
        System.arraycopy(pos, 0, ans.pos, 0, pos.length);

        for (int i = 0; i < pos.length; i++)
            ans.vector[i] = x.multiply(vector[i]);

        return ans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector scalarMultiply(final double x) {
        final ComplexSparseVector ans = new ComplexSparseVector(getDimension());
        ans.vector = new Complex[vector.length];
        ans.pos = new int[pos.length];
        System.arraycopy(pos, 0, ans.pos, 0, pos.length);

        for (int i = 0; i < pos.length; i++)
            ans.vector[i] = vector[i].multiply(x);

        return ans;
    }

    // SCALAR DIVISION
    /**
     * Returns the division of this vector by a scalar.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        if (x instanceof Complex) {
            return scalarDivide((Complex) x);
        } else if (x instanceof Double) {
            return scalarDivide(((Double) x).value());
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the division of this vector by a scalar.
     *
     * @param x a complex
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector scalarDivide(final Complex x) {
        final ComplexSparseVector ans = new ComplexSparseVector(getDimension());
        ans.vector = new Complex[vector.length];
        ans.pos = new int[pos.length];
        System.arraycopy(pos, 0, ans.pos, 0, pos.length);

        for (int i = 0; i < pos.length; i++)
            ans.vector[i] = vector[i].divide(x);

        return ans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexVector scalarDivide(final double x) {
        final ComplexSparseVector ans = new ComplexSparseVector(getDimension());
        ans.vector = new Complex[vector.length];
        ans.pos = new int[pos.length];
        System.arraycopy(pos, 0, ans.pos, 0, pos.length);

        for (int i = 0; i < pos.length; i++)
            ans.vector[i] = vector[i].divide(x);

        return ans;
    }

    // SCALAR PRODUCT
    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Complex scalarProduct(HilbertSpace.Member v) {
        if (v instanceof ComplexVector) {
            return scalarProduct((ComplexVector) v);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the scalar product of this vector and another.
     *
     * @param v a complex vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public Complex scalarProduct(final AbstractComplexVector v) {
        if (v instanceof ComplexSparseVector) {
            return scalarProduct((ComplexSparseVector) v);
        } else if (v instanceof ComplexVector) {
            return scalarProduct((ComplexVector) v);
        } else {
            if (getDimension() != v.getDimension()) {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }

            Complex ps = Complex.ZERO;

            for (int i = 0; i < pos.length; i++)
                ps = ps.add(vector[i].multiply(v.getPrimitiveElement(pos[i])));

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
    public Complex scalarProduct(final ComplexVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        Complex ps = Complex.ZERO;

        for (int i = 0; i < pos.length; i++)
            ps = ps.add(vector[i].multiply(
                        new Complex(v.vectorRe[pos[i]], v.vectorIm[pos[i]])));

        return ps;
    }

    /**
     * Returns the scalar product of this vector and another.
     *
     * @param v a complex sparse vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public Complex scalarProduct(final ComplexSparseVector v) {
        if (getDimension() != v.getDimension()) {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }

        Complex ps = Complex.ZERO;

        if (pos.length <= v.pos.length) {
            for (int i = 0; i < pos.length; i++)
                ps = ps.add(vector[i].multiply(v.getPrimitiveElement(pos[i])));
        } else {
            for (int i = 0; i < v.pos.length; i++)
                ps = ps.add(getPrimitiveElement(v.pos[i]).multiply(v.vector[i]));
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
    public ComplexSparseMatrix tensorProduct(final ComplexSparseVector v) {
        ComplexSparseMatrix ans = new ComplexSparseMatrix(getDimension(),
                v.getDimension());

        for (int j, i = 0; i < pos.length; i++) {
            for (j = 0; j < v.pos.length; j++)
                ans.setElement(pos[i], v.pos[j], vector[i].multiply(v.vector[j]));
        }

        return ans;
    }

    // MAP COMPONENTS
    /**
     * Applies a function on all the vector components.
     *
     * @param f a user-defined function
     *
     * @return a complex sparse vector
     */
    public AbstractComplexVector mapElements(final ComplexMapping f) {
        final Complex[] ans = new Complex[getDimension()];
        Complex val = f.map(Complex.ZERO);

        for (int i = 0; i < getDimension(); i++) {
            ans[i] = val;
        }

        for (int i = 0; i < pos.length; i++)
            ans[pos[i]] = f.map(vector[pos[i]]);

        return new ComplexVector(ans);
    }

    /**
     * Projects the vector to an array.
     *
     * @return a Complex array.
     */
    public Complex[] toPrimitiveArray() {
        final Complex[] result = new Complex[getDimension()];

        for (int i = 0; i < pos.length; i++)
            result[pos[i]] = vector[i];

        return result;
    }

    /**
     * Clone vector into a new vector.
     *
     * @return the cloned vector.
     */
    public Object clone() {
        return new ComplexSparseVector(this);
    }
}
