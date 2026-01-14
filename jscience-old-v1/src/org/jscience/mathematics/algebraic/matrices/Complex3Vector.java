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
 * An optimised implementation of a 3D complex vector.
 *
 * @author Mark Hale
 * @version 2.2
 */
public class Complex3Vector extends AbstractComplexVector implements Cloneable,
    Serializable {
    //quick access convenient public modifier
    /** DOCUMENT ME! */
    public double xre;

    //quick access convenient public modifier
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    public double xim;

    /** DOCUMENT ME! */
    public double yre;

    /** DOCUMENT ME! */
    public double yim;

    /** DOCUMENT ME! */
    public double zre;

    /** DOCUMENT ME! */
    public double zim;

/**
     * Constructs an empty 3-vector.
     */
    public Complex3Vector() {
        super(3);
    }

/**
     * Constructs a 3-vector.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
    public Complex3Vector(final Complex x, final Complex y, final Complex z) {
        this();
        xre = x.real();
        xim = x.imag();
        yre = y.real();
        yim = y.imag();
        zre = z.real();
        zim = z.imag();
    }

/**
     * Creates a new Complex3Vector object.
     *
     * @param xRe DOCUMENT ME!
     * @param xIm DOCUMENT ME!
     * @param yRe DOCUMENT ME!
     * @param yIm DOCUMENT ME!
     * @param zRe DOCUMENT ME!
     * @param zIm DOCUMENT ME!
     */
    public Complex3Vector(final double xRe, final double xIm, final double yRe,
        final double yIm, final double zRe, final double zIm) {
        this();
        xre = xRe;
        xim = xIm;
        yre = yRe;
        yim = yIm;
        zre = zRe;
        zim = zIm;
    }

/**
     * Constructs a 3-vector.
     *
     * @param array DOCUMENT ME!
     */
    public Complex3Vector(final Complex[] array) {
        this();

        if (array.length == 3) {
            xre = array[0].real();
            xim = array[0].imag();
            yre = array[1].real();
            yim = array[1].imag();
            zre = array[2].real();
            zim = array[2].imag();
        } else {
            throw new IllegalArgumentException("Invalid array size.");
        }
    }

/**
     * Constructs a 3-vector.
     *
     * @param vec DOCUMENT ME!
     */
    public Complex3Vector(final Complex3Vector vec) {
        this();
        xre = vec.xre;
        xim = vec.xim;
        yre = vec.yre;
        yim = vec.yim;
        zre = vec.zre;
        zim = vec.zim;
    }

    /**
     * Compares two complex vectors for equality.
     *
     * @param obj a complex 3-vector
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Complex3Vector) {
                final Complex3Vector vec = (Complex3Vector) obj;
                double dxRe = xre - vec.xre;
                double dxIm = xim - vec.xim;
                double dyRe = yre - vec.yre;
                double dyIm = yim - vec.yim;
                double dzRe = zre - vec.zre;
                double dzIm = zim - vec.zim;

                return (((dxRe * dxRe) + (dxIm * dxIm) + (dyRe * dyRe) +
                (dyIm * dyIm) + (dzRe * dzRe) + (dzIm * dzIm)) <= (java.lang.Double.valueOf(JScience.getProperty(
                        "tolerance")).doubleValue() * java.lang.Double.valueOf(JScience.getProperty(
                        "tolerance")).doubleValue()));
            } else {
                if ((obj instanceof AbstractComplexVector)) {
                    AbstractComplexVector vect = (AbstractComplexVector) obj;
                    double dxRe = xre - vect.getPrimitiveElement(0).real();
                    double dxIm = xim - vect.getPrimitiveElement(0).imag();
                    double dyRe = yre - vect.getPrimitiveElement(1).real();
                    double dyIm = yim - vect.getPrimitiveElement(1).imag();
                    double dzRe = zre - vect.getPrimitiveElement(2).real();
                    double dzIm = zim - vect.getPrimitiveElement(2).imag();

                    return (((dxRe * dxRe) + (dxIm * dxIm) + (dyRe * dyRe) +
                    (dyIm * dyIm) + (dzRe * dzRe) + (dzIm * dzIm)) <= (java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue() * java.lang.Double.valueOf(JScience.getProperty(
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
        buf.append(Complex.toString(xre, xim)).append(',')
           .append(Complex.toString(yre, yim)).append(',')
           .append(Complex.toString(zre, zim));

        return buf.toString();
    }

    /**
     * Returns the real part of this complex 3-vector.
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector real() {
        return new Double3Vector(xre, yre, zre);
    }

    /**
     * Returns the imaginary part of this complex 3-vector.
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector imag() {
        return new Double3Vector(xim, yim, zim);
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
        switch (n) {
        case 0:
            return new Complex(xre, xim);

        case 1:
            return new Complex(yre, yim);

        case 2:
            return new Complex(zre, zim);

        default:
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Sets the value of a component of this vector. Should only be
     * used to initialise this vector.
     *
     * @param n index of the vector component
     * @param z a complex number
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public void setElement(final int n, final Complex z) {
        switch (n) {
        case 0:
            xre = z.real();
            xim = z.imag();

            break;

        case 1:
            yre = z.real();
            yim = z.imag();

            break;

        case 2:
            zre = z.real();
            zim = z.imag();

            break;

        default:
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Sets the value of a component of this vector. Should only be
     * used to initialise this vector.
     *
     * @param n index of the vector component
     * @param x the real part of a complex number
     * @param y the imaginary part of a complex number
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         component.
     */
    public void setElement(final int n, final double x, final double y) {
        switch (n) {
        case 0:
            xre = x;
            xim = y;

            break;

        case 1:
            yre = x;
            yim = y;

            break;

        case 2:
            zre = x;
            zim = y;

            break;

        default:
            throw new IllegalDimensionException("Invalid component.");
        }
    }

    /**
     * Sets the value of all elements of the vector.
     *
     * @param c a Complex element
     */
    public void setAllElements(final Complex c) {
        xre = c.real();
        xim = c.imag();
        yre = c.real();
        yim = c.imag();
        zre = c.real();
        zim = c.imag();
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude).
     *
     * @return DOCUMENT ME!
     */
    public double norm() {
        return Math.sqrt((xre * xre) + (xim * xim) + (yre * yre) + (yim * yim) +
            (zre * zre) + (zim * zim));
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity"
     * src="doc-files/infinity.gif"></sup>-norm.
     *
     * @return DOCUMENT ME!
     */
    public double infNorm() {
        double infNormSq = 0;
        double modSq;
        modSq = (xre * xre) + (xim * xim);

        if (modSq > infNormSq) {
            infNormSq = modSq;
        }

        modSq = (yre * yre) + (yim * yim);

        if (modSq > infNormSq) {
            infNormSq = modSq;
        }

        modSq = (zre * zre) + (zim * zim);

        if (modSq > infNormSq) {
            infNormSq = modSq;
        }

        return Math.sqrt(infNormSq);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex getC1() {
        return new Complex(xre, xim);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex getC2() {
        return new Complex(yre, yim);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex getC3() {
        return new Complex(zre, zim);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void setC1(final Complex c) {
        xre = c.real();
        xim = c.imag();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void setC2(final Complex c) {
        yre = c.real();
        yim = c.imag();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void setC3(final Complex c) {
        zre = c.real();
        zim = c.imag();
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
        return new Complex3Vector(-xre, -xim, -yre, -yim, -zre, -zim);
    }

    // COMPLEX CONJUGATE
    /**
     * Returns the complex conjugate of this vector.
     *
     * @return a complex 3-vector
     */
    public AbstractComplexVector conjugate() {
        return new Complex3Vector(xre, -xim, yre, -yim, zre, -zim);
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
        if (vec instanceof AbstractComplexVector) {
            return add((AbstractComplexVector) vec);
        } else if (vec instanceof AbstractDoubleVector) {
            return add((AbstractDoubleVector) vec);
        } else if (vec instanceof AbstractIntegerVector) {
            return add((AbstractIntegerVector) vec);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param vec a complex 3-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractComplexVector add(final AbstractComplexVector vec) {
        if (vec.getDimension() == 3) {
            return new Complex3Vector(xre + vec.getPrimitiveElement(0).real(),
                xim + vec.getPrimitiveElement(0).imag(),
                yre + vec.getPrimitiveElement(1).real(),
                yim + vec.getPrimitiveElement(1).imag(),
                zre + vec.getPrimitiveElement(2).real(),
                zim + vec.getPrimitiveElement(2).imag());
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param vec a double 3-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractComplexVector add(final AbstractDoubleVector vec) {
        if (vec.getDimension() == 3) {
            return new Complex3Vector(xre + vec.getPrimitiveElement(0), xim,
                yre + vec.getPrimitiveElement(1), yim,
                zre + vec.getPrimitiveElement(2), zim);
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * Returns the addition of this vector and another.
     *
     * @param vec an integer 3-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractComplexVector add(final AbstractIntegerVector vec) {
        if (vec.getDimension() == 3) {
            return new Complex3Vector(xre + vec.getPrimitiveElement(0), xim,
                yre + vec.getPrimitiveElement(1), yim,
                zre + vec.getPrimitiveElement(2), zim);
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
        if (vec instanceof AbstractComplexVector) {
            return subtract((AbstractComplexVector) vec);
        } else if (vec instanceof AbstractDoubleVector) {
            return subtract((AbstractDoubleVector) vec);
        } else if (vec instanceof AbstractIntegerVector) {
            return subtract((AbstractIntegerVector) vec);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param vec a complex 3-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractComplexVector subtract(final AbstractComplexVector vec) {
        if (vec.getDimension() == 3) {
            return new Complex3Vector(xre - vec.getPrimitiveElement(0).real(),
                xim - vec.getPrimitiveElement(0).imag(),
                yre - vec.getPrimitiveElement(1).real(),
                yim - vec.getPrimitiveElement(1).imag(),
                zre - vec.getPrimitiveElement(2).real(),
                zim - vec.getPrimitiveElement(2).imag());
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param vec a double 3-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractComplexVector subtract(final AbstractDoubleVector vec) {
        if (vec.getDimension() == 3) {
            return new Complex3Vector(xre - vec.getPrimitiveElement(0), xim,
                yre - vec.getPrimitiveElement(1), yim,
                zre - vec.getPrimitiveElement(2), zim);
        } else {
            throw new IllegalDimensionException("Vectors are different sizes.");
        }
    }

    /**
     * Returns the subtraction of this vector by another.
     *
     * @param vec an integer 3-vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractComplexVector subtract(final AbstractIntegerVector vec) {
        if (vec.getDimension() == 3) {
            return new Complex3Vector(xre - vec.getPrimitiveElement(0), xim,
                yre - vec.getPrimitiveElement(1), yim,
                zre - vec.getPrimitiveElement(2), zim);
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
    public Module.Member scalarMultiply(Ring.Member x) {
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
     * @param z a complex number
     *
     * @return a complex 3-vector
     */
    public AbstractComplexVector scalarMultiply(final Complex z) {
        final double real = z.real();
        final double imag = z.imag();

        return new Complex3Vector((xre * real) - (xim * imag),
            (xre * imag) + (xim * real), (yre * real) - (yim * imag),
            (yre * imag) + (yim * real), (zre * real) - (zim * imag),
            (zre * imag) + (zim * real));
    }

    /**
     * Returns the multiplication of this vector by a scalar.
     *
     * @param k a double
     *
     * @return a complex 3-vector
     */
    public AbstractComplexVector scalarMultiply(final double k) {
        return new Complex3Vector(k * xre, k * xim, k * yre, k * yim, k * zre,
            k * zim);
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
    public VectorSpace.Member scalarDivide(Field.Member x) {
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
     * @param z a complex number
     *
     * @return a complex 3-vector
     */
    public AbstractComplexVector scalarDivide(final Complex z) {
        final double real = z.real();
        final double imag = z.imag();
        final double a;
        final double denom;

        if (Math.abs(real) < Math.abs(imag)) {
            a = real / imag;
            denom = (real * a) + imag;

            return new Complex3Vector(((xre * a) + xim) / denom,
                ((xim * a) - xre) / denom, ((yre * a) + yim) / denom,
                ((yim * a) - yre) / denom, ((zre * a) + zim) / denom,
                ((zim * a) - zre) / denom);
        } else {
            a = imag / real;
            denom = real + (imag * a);

            return new Complex3Vector((xre + (xim * a)) / denom,
                (xim - (xre * a)) / denom, (yre + (yim * a)) / denom,
                (yim - (yre * a)) / denom, (zre + (zim * a)) / denom,
                (zim - (zre * a)) / denom);
        }
    }

    /**
     * Returns the division of this vector by a scalar.
     *
     * @param k a double
     *
     * @return a complex 3-vector
     */
    public AbstractComplexVector scalarDivide(final double k) {
        return new Complex3Vector(xre / k, xim / k, yre / k, yim / k, zre / k,
            zim / k);
    }

    // SCALAR PRODUCT
    /**
     * Returns the scalar product of this vector and another.
     *
     * @param vec DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Complex scalarProduct(HilbertSpace.Member vec) {
        if (vec instanceof AbstractComplexVector) {
            return scalarProduct((AbstractComplexVector) vec);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the scalar product of this vector and another.
     *
     * @param vec a complex vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the vectors are different sizes.
     */
    public Complex scalarProduct(final AbstractComplexVector vec) {
        if (vec instanceof Complex3Vector) {
            return scalarProduct((Complex3Vector) vec);
        } else {
            if (vec.getDimension() == 3) {
                return new Complex((xre * vec.getPrimitiveElement(0).real()) +
                    (xim * vec.getPrimitiveElement(0).imag()) +
                    (yre * vec.getPrimitiveElement(1).real()) +
                    (yim * vec.getPrimitiveElement(1).imag()) +
                    (zre * vec.getPrimitiveElement(2).real()) +
                    (zim * vec.getPrimitiveElement(2).imag()),
                    (((xim * vec.getPrimitiveElement(0).real()) -
                    (xre * vec.getPrimitiveElement(0).imag()) +
                    (yim * vec.getPrimitiveElement(1).real())) -
                    (yre * vec.getPrimitiveElement(1).imag()) +
                    (zim * vec.getPrimitiveElement(2).real())) -
                    (zre * vec.getPrimitiveElement(2).imag()));
            } else {
                throw new IllegalDimensionException(
                    "Vectors are different sizes.");
            }
        }
    }

    /**
     * Returns the scalar product of this vector and another.
     *
     * @param vec a complex 3-vector
     *
     * @return DOCUMENT ME!
     */
    public Complex scalarProduct(final Complex3Vector vec) {
        return new Complex((xre * vec.xre) + (xim * vec.xim) + (yre * vec.yre) +
            (yim * vec.yim) + (zre * vec.zre) + (zim * vec.zim),
            (((xim * vec.xre) - (xre * vec.xim) + (yim * vec.yre)) -
            (yre * vec.yim) + (zim * vec.zre)) - (zre * vec.zim));
    }

    // VECTOR PRODUCT
    /**
     * Returns the vector product of this vector and another.
     *
     * @param vec a complex 3-vector
     *
     * @return DOCUMENT ME!
     */
    public Complex3Vector vectorProduct(final Complex3Vector vec) {
        return new Complex3Vector((yre * vec.zre) - (yim * vec.zim) -
            (zre * vec.yre) + (zim * vec.yim),
            ((yre * vec.zim) + (yim * vec.zre)) - (zre * vec.yim) -
            (zim * vec.yre),
            (zre * vec.xre) - (zim * vec.xim) - (xre * vec.zre) +
            (xim * vec.zim),
            ((zre * vec.xim) + (zim * vec.xre)) - (xre * vec.zim) -
            (xim * vec.zre),
            (xre * vec.yre) - (xim * vec.yim) - (yre * vec.xre) +
            (yim * vec.xim),
            ((xre * vec.yim) + (xim * vec.yre)) - (yre * vec.xim) -
            (yim * vec.xre));
    }

    // MAP COMPONENTS
    /**
     * Applies a function on all the vector components.
     *
     * @param mapping a user-defined function
     *
     * @return a complex 3-vector
     */
    public AbstractComplexVector mapElements(final ComplexMapping mapping) {
        return new Complex3Vector(mapping.map(new Complex(xre, xim)),
            mapping.map(new Complex(yre, yim)),
            mapping.map(new Complex(zre, zim)));
    }

    /**
     * Projects the vector to an array.
     *
     * @return an double array.
     */
    public Complex[] toPrimitiveArray() {
        final Complex[] result = new Complex[3];
        result[0] = new Complex(xre, xim);
        result[1] = new Complex(yre, yim);
        result[2] = new Complex(zre, zim);

        return result;
    }

    /**
     * Clone vector into a new vector.
     *
     * @return the cloned vector.
     */
    public Object clone() {
        return new Complex3Vector(this);
    }
}
