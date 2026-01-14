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

import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;

/**
 * An array-based implementation of an double vector.
 *
 * @author Mark Hale
 * @version 2.1
 */
public class DoubleVector extends AbstractDoubleVector implements Cloneable, Serializable {
    /**
     * Array containing the components of the vector.
     */
    protected double vector[];

    /**
     * Constructs an empty vector.
     *
     * @param dim the dimension of the vector.
     */
    public DoubleVector(final int dim) {
        super(dim);
        vector = new double[dim];
    }

    /**
     * Constructs a vector by wrapping an array.
     *
     * @param array an assigned value.
     */
    public DoubleVector(final double array[]) {
        super(array.length);
        vector = array;
    }

    /**
     * Constructs a vector by copying a vector.
     *
     * @param vec an assigned value
     */
    public DoubleVector(final AbstractDoubleVector vec) {
        super(vec.getDimension());
        vector = new double[vec.getDimension()];
        if (vec instanceof DoubleVector) {
            DoubleVector vect = (DoubleVector) vec;
            System.arraycopy(vect.vector, 0, vector, 0, vec.getDimension());
        } else {
            for (int i = 0; i < vec.getDimension(); i++) {
                vector[i] = vec.getPrimitiveElement(i);
            }
        }
    }

    /**
     * Compares two double vectors for equality.
     *
     * @param a a double vector.
     */
    public boolean equals(Object a, double tol) {
        if (a != null && (a instanceof AbstractDoubleVector) && getDimension() == ((AbstractDoubleVector) a).getDimension()) {
            final AbstractDoubleVector dv = (AbstractDoubleVector) a;
            double sumSqr = 0.0;
            for (int i = 0; i < getDimension(); i++) {
                double delta = vector[i] - dv.getPrimitiveElement(i);
                sumSqr += delta * delta;
            }
            return (sumSqr <= tol * tol);
        } else
            return false;
    }

    /**
     * Returns a comma delimited string representing the value of this vector.
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(8 * getDimension());
        int i;
        for (i = 0; i < getDimension() - 1; i++) {
            buf.append(vector[i]);
            buf.append(',');
        }
        buf.append(vector[i]);
        return buf.toString();
    }

    /**
     * Converts this vector to an integer vector.
     *
     * @return an integer vector.
     */
    public AbstractIntegerVector toIntegerVector() {
        final int array[] = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++)
            array[i] = Math.round((float) vector[i]);
        return new IntegerVector(array);
    }

    /**
     * Converts this vector to a complex vector.
     *
     * @return a complex vector.
     */
    public AbstractComplexVector toComplexVector() {
        return new ComplexVector(vector, new double[getDimension()]);
    }

    /**
     * Returns an element of this vector.
     *
     * @param n index of the vector element.
     * @throws IllegalDimensionException If attempting to access an invalid element .
     */
    public double getPrimitiveElement(final int n) {
        if (n >= 0 && n < getDimension())
            return vector[n];
        else
            throw new IllegalDimensionException(getInvalidElementMsg(n));
    }

    /**
     * Sets the value of a element of this vector.
     *
     * @param n index of the vector element .
     * @param x a number.
     * @throws IllegalDimensionException If attempting to access an invalid element .
     */
    public void setElement(final int n, final double x) {
        if (n >= 0 && n < getDimension())
            vector[n] = x;
        else
            throw new IllegalDimensionException(getInvalidElementMsg(n));
    }

    /**
     * Sets the value of all elements of the vector.
     *
     * @param r a ring element
     */
    public void setAllElements(final double r) {
        for (int i = 0; i < getDimension(); i++) {
            vector[i] = r;
        }
    }

    /**
     * Returns the l<sup>n</sup>-norm.
     *
     * @planetmath VectorPnorm
     */
    public double norm(final int n) {
        if (getDimension() > 0) {
            double answer = Math.pow(Math.abs(vector[0]), n);
            for (int i = 1; i < getDimension(); i++)
                answer += Math.pow(Math.abs(vector[i]), n);
            return Math.pow(answer, 1.0 / n);
        } else
            throw new ArithmeticException("The norm of a zero dimension vector is undefined.");
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude).
     *
     * @planetmath VectorPnorm
     */
    public double norm() {
        if (getDimension() > 0) {
            double answer = vector[0];
            for (int i = 1; i < getDimension(); i++)
                answer = MathUtils.hypot(answer, vector[i]);
            return answer;
        } else
            throw new ArithmeticException("The norm of a zero dimension vector is undefined.");
    }

    /**
     * Makes the norm of this vector equal to 1.
     */
    public void normalize() {
        if (getDimension() > 0) {
            final double norm = norm();
            for (int i = 0; i < getDimension(); i++)
                vector[i] /= norm;
        }
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
     *
     * @author Taber Smith
     * @planetmath VectorPnorm
     */
    public double infNorm() {
        if (getDimension() > 0) {
            double infNorm = Math.abs(vector[0]);
            for (int i = 1; i < getDimension(); i++) {
                final double abs = Math.abs(vector[i]);
                if (abs > infNorm)
                    infNorm = abs;
            }
            return infNorm;
        } else
            throw new ArithmeticException("The inf norm of a zero dimension vector is undefined.");
    }

    /**
     * Returns the sum of the squares of the components.
     *
     * @return DOCUMENT ME!
     */
    public double sumSquares() {
        if (getDimension() > 0) {
            double norm = 0;

            for (int k = 0; k < vector.length; k++)
                norm += (vector[k] * vector[k]);

            return norm;
        } else
            throw new ArithmeticException("The sum of squares of a zero dimension vector is undefined.");
    }

    /**
     * Returns the mass.
     *
     * @return DOCUMENT ME!
     */
    public double mass() {
        if (getDimension() > 0) {
            double mass = 0;

            for (int k = 0; k < vector.length; k++)
                mass += vector[k];

            return mass;
        } else
            throw new ArithmeticException("The mass of a zero dimension vector is undefined.");
    }

//============
// OPERATIONS
//============

    /**
     * Returns the negative of this vector.
     */
    public AbelianGroup.Member negate() {
        final double array[] = new double[getDimension()];
        array[0] = -vector[0];
        for (int i = 1; i < getDimension(); i++)
            array[i] = -vector[i];
        return new DoubleVector(array);
    }

// ADDITION

    /**
     * Returns the addition of this vector and another.
     */
    public AbelianGroup.Member add(final AbelianGroup.Member v) {
        if (v instanceof AbstractDoubleVector)
            return add((AbstractDoubleVector) v);
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param v a double vector.
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractDoubleVector add(AbstractDoubleVector v) {
        if (v instanceof DoubleVector)
            return add((DoubleVector) v);
        else {
            if (getDimension() == v.getDimension()) {
                final double array[] = new double[getDimension()];
                array[0] = vector[0] + v.getPrimitiveElement(0);
                for (int i = 1; i < getDimension(); i++)
                    array[i] = vector[i] + v.getPrimitiveElement(i);
                return new DoubleVector(array);
            } else
                throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    public DoubleVector add(final DoubleVector v) {
        if (getDimension() == v.getDimension()) {
            final double array[] = new double[getDimension()];
            array[0] = vector[0] + v.vector[0];
            for (int i = 1; i < getDimension(); i++)
                array[i] = vector[i] + v.vector[i];
            return new DoubleVector(array);
        } else
            throw new IllegalDimensionException("Vectors are different sizes.");
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this vector by another.
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member v) {
        if (v instanceof AbstractDoubleVector)
            return subtract((AbstractDoubleVector) v);
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param v a double vector.
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public AbstractDoubleVector subtract(final AbstractDoubleVector v) {
        if (v instanceof DoubleVector)
            return subtract((DoubleVector) v);
        else {
            if (getDimension() == v.getDimension()) {
                final double array[] = new double[getDimension()];
                array[0] = vector[0] - v.getPrimitiveElement(0);
                for (int i = 1; i < getDimension(); i++)
                    array[i] = vector[i] - v.getPrimitiveElement(i);
                return new DoubleVector(array);
            } else
                throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    public DoubleVector subtract(final DoubleVector v) {
        if (getDimension() == v.getDimension()) {
            final double array[] = new double[getDimension()];
            array[0] = vector[0] - v.vector[0];
            for (int i = 1; i < getDimension(); i++)
                array[i] = vector[i] - v.vector[i];
            return new DoubleVector(array);
        } else
            throw new IllegalDimensionException("Vectors are different sizes.");
    }

// SCALAR MULTIPLICATION

    /**
     * Returns the multiplication of this vector by a scalar.
     */
    public Module.Member scalarMultiply(final Ring.Member x) {
        if (x instanceof Double)
            return scalarMultiply(((Double) x).value());
        else if (x instanceof Integer)
            return scalarMultiply(((Integer) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param x a double.
     */
    public AbstractDoubleVector scalarMultiply(final double x) {
        final double array[] = new double[getDimension()];
        array[0] = x * vector[0];
        for (int i = 1; i < getDimension(); i++)
            array[i] = x * vector[i];
        return new DoubleVector(array);
    }

// SCALAR DIVISION

    /**
     * Returns the division of this vector by a scalar.
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        if (x instanceof Double)
            return scalarDivide(((Double) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the division of this vector by a scalar.
     *
     * @param x a double.
     * @throws ArithmeticException If divide by zero.
     */
    public AbstractDoubleVector scalarDivide(final double x) {
        final double array[] = new double[getDimension()];
        array[0] = vector[0] / x;
        for (int i = 1; i < getDimension(); i++)
            array[i] = vector[i] / x;
        return new DoubleVector(array);
    }

// SCALAR PRODUCT

    /**
     * Returns the scalar product of this vector and another.
     *
     * @param v a double vector.
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public double scalarProduct(final AbstractDoubleVector v) {
        if (v instanceof DoubleVector)
            return scalarProduct((DoubleVector) v);
        else {
            if (getDimension() == v.getDimension()) {
                double answer = vector[0] * v.getPrimitiveElement(0);
                for (int i = 1; i < getDimension(); i++)
                    answer += vector[i] * v.getPrimitiveElement(i);
                return answer;
            } else
                throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    public double scalarProduct(final DoubleVector v) {
        if (getDimension() == v.getDimension()) {
            double answer = vector[0] * v.vector[0];
            for (int i = 1; i < getDimension(); i++)
                answer += vector[i] * v.vector[i];
            return answer;
        } else
            throw new IllegalDimensionException("Vectors are different sizes.");
    }

// MAP COMPONENTS

    /**
     * Applies a function on all the vector components.
     *
     * @param f a user-defined function.
     * @return a double vector.
     */
    public AbstractDoubleVector mapElements(final PrimitiveMapping f) {
        final double array[] = new double[getDimension()];
        array[0] = f.map(vector[0]);
        for (int i = 1; i < getDimension(); i++)
            array[i] = f.map(vector[i]);
        return new DoubleVector(array);
    }

    /**
     * Projects the vector to an array.
     *
     * @return an double array.
     */
    public double[] toPrimitiveArray() {
        final double[] result = new double[getDimension()];
        System.arraycopy(vector, 0, result, 0, getDimension());
        return result;
    }

    /**
     * Clone vector into a new vector.
     *
     * @return the cloned vector.
     */
    public Object clone() {
        return new DoubleVector(this);
    }

}

