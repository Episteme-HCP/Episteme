package org.jscience.mathematics.algebraic.numbers;

import org.jscience.JScience;
import org.jscience.mathematics.algebraic.algebras.CStarAlgebra;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.matrices.Double3Vector;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;

import java.io.Serializable;

/**
 * The Quaternion class encapsulates quaternions.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 *         <p/>
 *         Quaternions are a noncommutative C<sup>*</sup>-algebra over the reals.
 * @version 1.1
 * @planetmath Quaternions
 */
public final class Quaternion extends Number implements Cloneable, Serializable,
        Field.Member, CStarAlgebra.Member {
    private static final long serialVersionUID = 1605315490425547301L;

    private double re;
    private double imi, imj, imk;

    public static final Quaternion ZERO = new Quaternion(0.0, 0.0, 0.0, 0.0);
    public static final Quaternion ONE = new Quaternion(1.0, 0.0, 0.0, 0.0);
    public static final Quaternion I = new Quaternion(0.0, 1.0, 0.0, 0.0);
    public static final Quaternion J = new Quaternion(0.0, 0.0, 1.0, 0.0);
    public static final Quaternion K = new Quaternion(0.0, 0.0, 0.0, 1.0);

    //A constant holding the largest positive finite value for the real part of type quaternion , (2-2-52)�21023.
    public static final double MAX_REAL_VALUE = java.lang.Double.MAX_VALUE;
    //A constant holding the smallest positive nonzero for the real part value of type quaternion , 2-1074.
    public static final double MIN_REAL_VALUE = java.lang.Double.MIN_VALUE;
    //A constant holding the largest positive finite value for the imaginary part of type quaternion , (2-2-52)�21023.
    public static final double MAX_IMAGINARY_VALUE = java.lang.Double.MAX_VALUE;
    //A constant holding the smallest positive nonzero for the imaginary part value of type quaternion , 2-1074.
    public static final double MIN_IMAGINARY_VALUE = java.lang.Double.MIN_VALUE;
    //A constant holding a Not-a-Number (NaN) value of type quaternion .
    public static final double NaN = java.lang.Double.NaN;
    //A constant holding the negative infinity for the real partof type quaternion .
    public static final double NEGATIVE_REAL_INFINITY = java.lang.Double.NEGATIVE_INFINITY;
    //A constant holding the positive infinity for the real part of type quaternion .
    public static final double POSITIVE_REAL_INFINITY = java.lang.Double.POSITIVE_INFINITY;
    //A constant holding the negative infinity for the imaginary partof type quaternion .
    public static final double NEGATIVE_IMAGINARY_INFINITY = java.lang.Double.NEGATIVE_INFINITY;
    //A constant holding the positive infinity for the imaginary part of type quaternion .
    public static final double POSITIVE_IMAGINARY_INFINITY = java.lang.Double.POSITIVE_INFINITY;

    //The Class instance representing the primitive type Quaternion.
    public static final Class TYPE = ZERO.getClass();

    /**
     * Constructs a quaternion.
     */
    public Quaternion(final double real, final Double3Vector imag) {
        re = real;
        imi = imag.x;
        imj = imag.y;
        imk = imag.z;
    }

    /**
     * Constructs the quaternion q<sub>0</sub>+iq<sub>1</sub>+jq<sub>2</sub>+kq<sub>3</sub>.
     */
    public Quaternion(final double q0, final double q1, final double q2, final double q3) {
        re = q0;
        imi = q1;
        imj = q2;
        imk = q3;
    }

    /**
     * Constructs a quaternion.
     */
    public Quaternion(final Quaternion quat) {
        re = quat.real();
        imi = quat.imag().x;
        imj = quat.imag().y;
        imk = quat.imag().z;
    }

    /**
     * Compares two quaternions for equality.
     *
     * @param obj a quaternion
     */
    public boolean equals(Object obj) {
        if (obj instanceof Quaternion) {
            final Quaternion q = (Quaternion) obj;
            return Math.abs(re - q.re) <= java.lang.Double.valueOf(JScience.getProperty("tolerance")).doubleValue() &&
                    Math.abs(imi - q.imi) <= java.lang.Double.valueOf(JScience.getProperty("tolerance")).doubleValue() &&
                    Math.abs(imj - q.imj) <= java.lang.Double.valueOf(JScience.getProperty("tolerance")).doubleValue() &&
                    Math.abs(imk - q.imk) <= java.lang.Double.valueOf(JScience.getProperty("tolerance")).doubleValue();
        } else
            return false;
    }

    /**
     * Returns a string representing the value of this quaternion.
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            final StringBuffer buf = new StringBuffer(40);
            buf.append(re);
            if (imi >= 0.0)
                buf.append("+");
            buf.append(imi);
            buf.append("i");
            if (imj >= 0.0)
                buf.append("+");
            buf.append(imj);
            buf.append("j");
            if (imk >= 0.0)
                buf.append("+");
            buf.append(imk);
            buf.append("k");
            return buf.toString();
        }
    }

    /**
     * Returns a hashcode for this quaternion.
     */
    public int hashCode() {
        return (int) (Math.exp(norm()));
    }

    /**
     * Returns true if either the real or imaginary part is NaN.
     */
    public boolean isNaN() {
        return (re == NaN) || (imi == NaN) ||
                (imj == NaN) || (imk == NaN);
    }

    /**
     * Returns true if either the real or imaginary part is infinite.
     */
    public boolean isInfinite() {
        return (re == POSITIVE_REAL_INFINITY) || (re == NEGATIVE_REAL_INFINITY)
                || (imi == POSITIVE_IMAGINARY_INFINITY) || (imi == NEGATIVE_IMAGINARY_INFINITY)
                || (imj == POSITIVE_IMAGINARY_INFINITY) || (imj == NEGATIVE_IMAGINARY_INFINITY)
                || (imk == POSITIVE_IMAGINARY_INFINITY) || (imk == NEGATIVE_IMAGINARY_INFINITY);
    }

    /**
     * Returns the real part of this quaternion.
     */
    public double real() {
        return re;
    }

    /**
     * Returns the imaginary part of this quaternion.
     */
    public Double3Vector imag() {
        return new Double3Vector(imi, imj, imk);
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude),
     * which is also the C<sup>*</sup> norm.
     */
    public double norm() {
        return Math.sqrt(sumSquares());
    }

    /**
     * Returns the sum of the squares of the components.
     */
    public double sumSquares() {
        return re * re + imi * imi + imj * imj + imk * imk;
    }

    //return the norm
    public int intValue() {
        return (int) norm();
        //return new Double(norm()).intValue();
    }

    //return the norm
    public long longValue() {
        return (long) norm();
        //return new Double(norm()).longValue();
    }

    //return the norm
    public float floatValue() {
        return (float) norm();
        //return new Double(norm()).floatValue();
    }

    //return the norm
    public double doubleValue() {
        return norm();
    }

//============
// OPERATIONS
//============

    /**
     * Returns the negative of this quaternion.
     */
    public AbelianGroup.Member negate() {
        return new Quaternion(-re, -imi, -imj, -imk);
    }

    /**
     * Returns the inverse of this quaternion.
     */
    public Field.Member inverse() {
        final double sumSqr = sumSquares();
        return new Quaternion(re / sumSqr, -imi / sumSqr, -imj / sumSqr, -imk / sumSqr);
    }

    /**
     * Returns the involution of this quaternion.
     */
    public CStarAlgebra.Member involution() {
        return conjugate();
    }

    /**
     * Returns the conjugate of this quaternion.
     */
    public Quaternion conjugate() {
        return new Quaternion(re, -imi, -imj, -imk);
    }

// ADDITION

    /**
     * Returns the addition of this number and another.
     */
    public AbelianGroup.Member add(final AbelianGroup.Member x) {
        if (x instanceof Quaternion)
            return add((Quaternion) x);
        else if (x instanceof Integer)
            return addReal(((Integer) x).value());
        else if (x instanceof Long)
            return addReal(((Long) x).value());
        else if (x instanceof Rational)
            return addReal(((Rational) x).value());
        else if (x instanceof Float)
            return addReal(((Float) x).value());
        else if (x instanceof Double)
            return addReal(((Double) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the addition of this quaternion and another.
     *
     * @param q a quaternion
     */
    public Quaternion add(final Quaternion q) {
        return new Quaternion(re + q.re, imi + q.imi, imj + q.imj, imk + q.imk);
    }

    /**
     * Returns the addition of this quaternion with a real part.
     *
     * @param real a real part
     */
    public Quaternion addReal(final double real) {
        return new Quaternion(re + real, imi, imj, imk);
    }

    /**
     * Returns the addition of this quaternion with an imaginary part.
     *
     * @param imag an imaginary part
     */
    public Quaternion addImag(final Double3Vector imag) {
        return new Quaternion(re, imi + imag.x, imj + imag.y, imk + imag.z);
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this number and another.
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member x) {
        if (x instanceof Quaternion)
            return subtract((Quaternion) x);
        else if (x instanceof Integer)
            return subtractReal(((Integer) x).value());
        else if (x instanceof Long)
            return subtractReal(((Long) x).value());
        else if (x instanceof Rational)
            return subtractReal(((Rational) x).value());
        else if (x instanceof Float)
            return subtractReal(((Float) x).value());
        else if (x instanceof Double)
            return subtractReal(((Double) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the subtraction of this quaternion by another.
     *
     * @param q a quaternion
     */
    public Quaternion subtract(final Quaternion q) {
        return new Quaternion(re - q.re, imi - q.imi, imj - q.imj, imk - q.imk);
    }

    /**
     * Returns the subtraction of this quaternion by a real part.
     *
     * @param real a real part
     */
    public Quaternion subtractReal(final double real) {
        return new Quaternion(re - real, imi, imj, imk);
    }

    /**
     * Returns the subtraction of this quaternion by an imaginary part.
     *
     * @param imag an imaginary part
     */
    public Quaternion subtractImag(final Double3Vector imag) {
        return new Quaternion(re, imi - imag.x, imj - imag.y, imk - imag.z);
    }

// MULTIPLICATION

    /**
     * Returns the multiplication of this number by a real scalar.
     */
    public Module.Member scalarMultiply(final Ring.Member x) {
        if (x instanceof Integer)
            return multiply(((Integer) x).value());
        else if (x instanceof Long)
            return multiply(((Long) x).value());
        else if (x instanceof Rational)
            return multiply(((Rational) x).value());
        else if (x instanceof Float)
            return multiply(((Float) x).value());
        else if (x instanceof Double)
            return multiply(((Double) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this number and another.
     */
    public Ring.Member multiply(final Ring.Member x) {
        if (x instanceof Quaternion)
            return multiply((Quaternion) x);
        else if (x instanceof Integer)
            return multiply(((Integer) x).value());
        else if (x instanceof Long)
            return multiply(((Long) x).value());
        else if (x instanceof Rational)
            return multiply(((Rational) x).value());
        else if (x instanceof Float)
            return multiply(((Float) x).value());
        else if (x instanceof Double)
            return multiply(((Double) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this quaternion and another.
     *
     * @param q a quaternion
     */
    public Quaternion multiply(final Quaternion q) {
        return new Quaternion(re * q.re - imi * q.imi - imj * q.imj - imk * q.imk,
                re * q.imi + q.re * imi + (imj * q.imk - q.imj * imk),
                re * q.imj + q.re * imj + (imk * q.imi - q.imk * imi),
                re * q.imk + q.re * imk + (imi * q.imj - q.imi * imj));
    }

    /**
     * Returns the multiplication of this quaternion by a scalar.
     *
     * @param x a real number
     */
    public Quaternion multiply(final double x) {
        return new Quaternion(x * re, x * imi, x * imj, x * imk);
    }

// DIVISION

    /**
     * Returns the division of this number by a real scalar.
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        if (x instanceof Double)
            return divide(((Double) x).value());
        else if (x instanceof Float)
            return divide(new Double(((Float) x)).value());
            //Wouldn't it be nice if we could divide by Ring.Member instead ?
            //else if (n instanceof Integer)
            //    return divide(new Double(((Integer) n)).value());
            //else if (n instanceof Long)
            //    return divide(new Double(((Long) n)).value());
        else if (x instanceof Rational)
            return divide(((Rational) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the division of this number and another.
     */
    public Field.Member divide(final Field.Member x) {
        if (x instanceof Quaternion)
            return divide((Quaternion) x);
        else if (x instanceof Double)
            return divide(((Double) x).value());
        else if (x instanceof Float)
            return divide(new Double(((Float) x)).value());
            //Wouldn't it be nice if we could divide by Ring.Member instead ?
            //else if (n instanceof Integer)
            //    return divide(new Double(((Integer) n)).value());
            //else if (n instanceof Long)
            //    return divide(new Double(((Long) n)).value());
        else if (x instanceof Rational)
            return divide(((Rational) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the division of this quaternion by another.
     *
     * @param q a quaternion
     * @throws ArithmeticException If divide by zero.
     */
    public Quaternion divide(final Quaternion q) {
        final double qSumSqr = q.sumSquares();
        return new Quaternion((re * q.re + imi * q.imi + imj * q.imj + imk * q.imk) / qSumSqr,
                (q.re * imi - re * q.imi - (imj * q.imk - q.imj * imk)) / qSumSqr,
                (q.re * imj - re * q.imj - (imk * q.imi - q.imk * imi)) / qSumSqr,
                (q.re * imk - re * q.imk - (imi * q.imj - q.imi * imj)) / qSumSqr);
    }

    /**
     * Returns the division of this quaternion by a scalar.
     *
     * @param x a real number
     * @throws ArithmeticException If divide by zero.
     */
    public Quaternion divide(final double x) {
        return new Quaternion(re / x, imi / x, imj / x, imk / x);
    }

    public Object clone() {
        return new Quaternion(this);
    }

}
