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

import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.analysis.PrimitiveMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * An optimised implementation of a 2D integer vector.
 *
 * @author Mark Hale
 * @version 2.0
 */
public class Integer2Vector extends AbstractIntegerVector implements Cloneable,
    Serializable {
    //quick access convenient public modifier
    /** DOCUMENT ME! */
    public int x;

    /** DOCUMENT ME! */
    public int y;

/**
     * Constructs an empty 2-vector.
     */
    public Integer2Vector() {
        super(2);
    }

/**
     * Constructs a 2-vector.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     */
    public Integer2Vector(final int x, final int y) {
        this();
        this.x = x;
        this.y = y;
    }

/**
     * Constructs a 2-vector.
     *
     * @param array DOCUMENT ME!
     */
    public Integer2Vector(int[] array) {
        this();

        if (array.length == 2) {
            x = array[0];
            y = array[1];
        } else {
            throw new IllegalArgumentException("Invalid array size.");
        }
    }

/**
     * Constructs a 2-vector.
     *
     * @param vec DOCUMENT ME!
     */
    public Integer2Vector(final Integer2Vector vec) {
        this();
        x = vec.x;
        y = vec.y;
    }

    /**
     * Compares two integer vectors for equality.
     *
     * @param obj a integer 2-vector
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Integer2Vector) {
                final Integer2Vector vec = (Integer2Vector) obj;

                return ((Math.abs(x - vec.x) <= java.lang.Double.valueOf(JScience.getProperty(
                        "tolerance")).doubleValue()) &&
                (Math.abs(y - vec.y) <= java.lang.Double.valueOf(JScience.getProperty(
                        "tolerance")).doubleValue()));
            } else {
                if ((obj instanceof AbstractIntegerVector)) {
                    AbstractIntegerVector vect = (AbstractIntegerVector) obj;

                    return ((vect.getDimension() == 2) &&
                    (Math.abs(x - vect.getPrimitiveElement(0)) <= java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue()) &&
                    (Math.abs(y - vect.getPrimitiveElement(1)) <= java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue()));
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * Returns a comma delimited string representing the value of this
     * vector.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(15);
        buf.append(x).append(',').append(y);

        return buf.toString();
    }

    /**
     * Converts this 2-vector to a double 2-vector.
     *
     * @return a double 2-vector
     */
    public AbstractDoubleVector toDoubleVector() {
        return new Double2Vector(x, y);
    }

    /**
     * Converts this 2-vector to a complex 2-vector.
     *
     * @return a complex 2-vector
     */
    public AbstractComplexVector toComplexVector() {
        return new Complex2Vector(x, 0.0, y, 0.0);
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
        switch (n) {
        case 0:
            return x;

        case 1:
            return y;

        default:
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Sets the value of a component of this vector. Should only be
     * used to initialise this vector.
     *
     * @param n index of the vector component
     * @param value a number
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public void setElement(final int n, final int value) {
        switch (n) {
        case 0:
            x = value;

            break;

        case 1:
            y = value;

            break;

        default:
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Sets the value of all elements of the vector.
     *
     * @param r a int element
     */
    public void setAllElements(final int r) {
        x = r;
        y = r;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getX() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getY() {
        return y;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setX(final int d) {
        x = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void setY(final int d) {
        y = d;
    }

    /**
     * Returns the l<sup>n</sup>-norm.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double norm(final int n) {
        final double answer = Math.pow(Math.abs(x), n) +
            Math.pow(Math.abs(y), n);

        return Math.pow(answer, 1.0 / n);
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude).
     *
     * @return DOCUMENT ME!
     */
    public double norm() {
        return Math.sqrt((x * x) + (y * y));
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity"
     * src="doc-files/infinity.gif"></sup>-norm.
     *
     * @return DOCUMENT ME!
     */
    public int infNorm() {
        int infNorm = 0;
        int abs;
        abs = Math.abs(x);

        if (abs > infNorm) {
            infNorm = abs;
        }

        abs = Math.abs(y);

        if (abs > infNorm) {
            infNorm = abs;
        }

        return infNorm;
    }

    /**
     * Returns the sum of the squares of the components.
     *
     * @return DOCUMENT ME!
     */
    public int sumSquares() {
        return (x * x) + (y * y);
    }

    /**
     * Returns the mass.
     *
     * @return DOCUMENT ME!
     */
    public int mass() {
        return x + y;
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
        return new Integer2Vector(-x, -y);
    }

    // ADDITION
    /**
     * Returns the addition of this vector and another.
     *
     * @param vec DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(final AbelianGroup.Member vec) {
        if (vec instanceof AbstractIntegerVector) {
            return add((AbstractIntegerVector) vec);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param vec a integer 2-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractIntegerVector add(final AbstractIntegerVector vec) {
        if (vec.getDimension() == 2) {
            return new Integer2Vector(x + vec.getPrimitiveElement(0),
                y + vec.getPrimitiveElement(1));
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this vector by another.
     *
     * @param vec DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member vec) {
        if (vec instanceof AbstractIntegerVector) {
            return subtract((AbstractIntegerVector) vec);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param vec a integer 2-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractIntegerVector subtract(final AbstractIntegerVector vec) {
        if (vec.getDimension() == 2) {
            return new Integer2Vector(x - vec.getPrimitiveElement(0),
                y - vec.getPrimitiveElement(1));
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
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
     * @param k a integer
     *
     * @return a integer 2-vector
     */
    public AbstractIntegerVector scalarMultiply(final int k) {
        return new Integer2Vector(k * x, k * y);
    }

    // SCALAR PRODUCT
    /**
     * Returns the scalar product of this vector and another.
     *
     * @param vec a integer 2-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public int scalarProduct(final AbstractIntegerVector vec) {
        if (vec.getDimension() == 2) {
            return (x * vec.getPrimitiveElement(0)) +
            (y * vec.getPrimitiveElement(1));
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    // MAP COMPONENTS
    /**
     * Applies a function on all the vector components.
     *
     * @param f a user-defined function.
     *
     * @return a double vector.
     */
    public AbstractDoubleVector mapElements(final PrimitiveMapping f) {
        return new Double2Vector(f.map(x), f.map(y));
    }

    /**
     * Projects the vector to an array.
     *
     * @return an integer array.
     */
    public int[] toPrimitiveArray() {
        final int[] result = new int[3];
        result[0] = x;
        result[1] = y;

        return result;
    }

    /**
     * Clone vector into a new vector.
     *
     * @return the cloned vector.
     */
    public Object clone() {
        return new Integer2Vector(this);
    }
}
