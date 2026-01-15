package org.jscience.mathematics.algebraic.numbers;

import org.jscience.mathematics.algebraic.algebras.CStarAlgebra;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.matrices.Double3Vector;
import org.jscience.mathematics.algebraic.matrices.RingVector;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;

import java.io.Serializable;

/**
 * The ExactQuaternion class encapsulates quaternions.
 *
 * @author Silvere Martin-Michiellot
 *         <p/>
 *         Quaternions are a noncommutative C<sup>*</sup>-algebra over the reals.
 * @version 1.1
 * @planetmath Quaternions
 */
public final class ExactQuaternion extends Number implements Cloneable, Serializable,
        Field.Member, CStarAlgebra.Member {
    private static final long serialVersionUID = 160531549L;

    private ExactReal re;
    private ExactReal imi, imj, imk;

    public static final ExactQuaternion ZERO = new ExactQuaternion(0.0, 0.0, 0.0, 0.0);
    public static final ExactQuaternion ONE = new ExactQuaternion(1.0, 0.0, 0.0, 0.0);
    public static final ExactQuaternion I = new ExactQuaternion(0.0, 1.0, 0.0, 0.0);
    public static final ExactQuaternion J = new ExactQuaternion(0.0, 0.0, 1.0, 0.0);
    public static final ExactQuaternion K = new ExactQuaternion(0.0, 0.0, 0.0, 1.0);

    //The Class instance representing the type.
    public static final Class TYPE = ZERO.getClass();

    /**
     * Constructs a quaternion.
     */
    public ExactQuaternion(final double real, final Double3Vector imag) {
        re = new ExactReal(real);
        imi = new ExactReal(imag.x);
        imj = new ExactReal(imag.y);
        imk = new ExactReal(imag.z);
    }

    /**
     * Constructs the quaternion q<sub>0</sub>+iq<sub>1</sub>+jq<sub>2</sub>+kq<sub>3</sub>.
     */
    public ExactQuaternion(final double q0, final double q1, final double q2, final double q3) {
        re = new ExactReal(q0);
        imi = new ExactReal(q1);
        imj = new ExactReal(q2);
        imk = new ExactReal(q3);
    }

    /**
     * Constructs the quaternion q<sub>0</sub>+iq<sub>1</sub>+jq<sub>2</sub>+kq<sub>3</sub>.
     */
    public ExactQuaternion(final ExactReal q0, final ExactReal q1, final ExactReal q2, final ExactReal q3) {
        re = q0;
        imi = q1;
        imj = q2;
        imk = q3;
    }

    /**
     * Constructs the quaternion q<sub>0</sub>+iq<sub>1</sub>+jq<sub>2</sub>+kq<sub>3</sub>.
     */
    public ExactQuaternion(final Quaternion quaternion) {
        re = new ExactReal(quaternion.real());
        imi = new ExactReal(quaternion.imag().x);
        imj = new ExactReal(quaternion.imag().y);
        imk = new ExactReal(quaternion.imag().z);
    }

    /**
     * Constructs the quaternion q<sub>0</sub>+iq<sub>1</sub>+jq<sub>2</sub>+kq<sub>3</sub>.
     */
    public ExactQuaternion(final ExactQuaternion quaternion) {
        re = quaternion.real();
        imi = (ExactReal) quaternion.imag().getElement(0);
        imj = (ExactReal) quaternion.imag().getElement(1);
        imk = (ExactReal) quaternion.imag().getElement(2);
    }

    /**
     * Compares two quaternions for equality.
     *
     * @param obj a quaternion
     */
    public boolean equals(Object obj) {
        if (obj instanceof ExactQuaternion) {
            final ExactQuaternion q = (ExactQuaternion) obj;
            return re.equals(q.re) && imi.equals(q.imi) && imj.equals(q.imj) && imk.equals(q.imk);
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
            buf.append(re.toString());
            if (imi.compareTo(new ExactReal(0.0)) >= 0)
                buf.append("+");
            buf.append(imi.toString());
            buf.append("i");
            if (imj.compareTo(new ExactReal(0.0)) >= 0)
                buf.append("+");
            buf.append(imj.toString());
            buf.append("j");
            if (imk.compareTo(new ExactReal(0.0)) >= 0)
                buf.append("+");
            buf.append(imk.toString());
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
        return (re == ExactReal.NaN) || (imi == ExactReal.NaN) ||
                (imj == ExactReal.NaN) || (imk == ExactReal.NaN);
    }

    /**
     * Returns true if either the real or imaginary part is infinite.
     */
    public boolean isInfinite() {
        return (re == ExactReal.POSITIVE_INFINITY) || (re == ExactReal.NEGATIVE_INFINITY)
                || (imi == ExactReal.POSITIVE_INFINITY) || (imi == ExactReal.NEGATIVE_INFINITY)
                || (imj == ExactReal.POSITIVE_INFINITY) || (imj == ExactReal.NEGATIVE_INFINITY)
                || (imk == ExactReal.POSITIVE_INFINITY) || (imk == ExactReal.NEGATIVE_INFINITY);
    }

    /**
     * Returns the real part of this quaternion.
     */
    public ExactReal real() {
        return re;
    }

    /**
     * Returns the imaginary part of this quaternion as a Vector of length 3.
     */
    public RingVector imag() {
        RingVector result;
        Ring.Member[] values = new Ring.Member[3];
        values[0] = imi;
        values[1] = imj;
        values[2] = imk;
        result = new RingVector(values);
        return result;
    }

    /**
     * Returns the l<sup>2</sup>-norm (magnitude),
     * which is also the C<sup>*</sup> norm.
     */
    public double norm() {
        return Math.sqrt(sumSquares().doubleValue());
    }

    /**
     * Returns the sum of the squares of the components.
     */
    public ExactReal sumSquares() {
        return re.multiply(re).add(imi.multiply(imi)).add(imj.multiply(imj)).add(imk.multiply(imk));
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
        return new ExactQuaternion((ExactReal) re.negate(), (ExactReal) imi.negate(), (ExactReal) imj.negate(), (ExactReal) imk.negate());
    }

    /**
     * Returns the inverse of this quaternion.
     */
    public Field.Member inverse() {
        final ExactReal sumSqr = sumSquares();
        return new ExactQuaternion(re.divide(sumSqr), ((ExactReal) imi.negate()).divide(sumSqr), ((ExactReal) imj.negate()).divide(sumSqr), ((ExactReal) imk.negate()).divide(sumSqr));
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
    public ExactQuaternion conjugate() {
        return new ExactQuaternion(re, (ExactReal) imi.negate(), (ExactReal) imj.negate(), (ExactReal) imk.negate());
    }

// ADDITION

    /**
     * Returns the addition of this number and another.
     */
    public AbelianGroup.Member add(final AbelianGroup.Member x) {
        if (x instanceof ExactQuaternion)
            return add((ExactQuaternion) x);
        else if (x instanceof Quaternion)
            return add((Quaternion) x);
        else if (x instanceof ExactReal)
            return addReal((ExactReal) x);
        else if (x instanceof Double)
            return addReal(((Double) x).value());
        else if (x instanceof Float)
            return addReal(((Float) x).value());
        else if (x instanceof Long)
            return addReal(((Long) x).value());
        else if (x instanceof Integer)
            return addReal(((Integer) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the addition of this quaternion and another.
     *
     * @param q a quaternion
     */
    public ExactQuaternion add(final ExactQuaternion q) {

        RingVector result;
        result = q.imag();

        return new ExactQuaternion(re.add(q.real()),
                (ExactReal) imi.add((Ring.Member) result.getElement(0)),
                (ExactReal) imj.add((Ring.Member) result.getElement(1)),
                (ExactReal) imk.add((Ring.Member) result.getElement(2)));
    }

    /**
     * Returns the addition of this quaternion and another.
     *
     * @param q a quaternion
     */
    public ExactQuaternion add(final Quaternion q) {
        return add(new ExactQuaternion(q));
    }

    /**
     * Returns the addition of this quaternion with a real part.
     *
     * @param real a real part
     */
    public ExactQuaternion addReal(final double real) {
        return new ExactQuaternion(re.add(new ExactReal(real)), imi, imj, imk);
    }

    /**
     * Returns the addition of this quaternion with a real part.
     *
     * @param real a real part
     */
    public ExactQuaternion addReal(final ExactReal real) {
        return new ExactQuaternion(re.add(real), imi, imj, imk);
    }

    /**
     * Returns the addition of this quaternion with an imaginary part.
     *
     * @param imag an imaginary part
     */
    public ExactQuaternion addImag(final RingVector imag) {
        if (imag.getDimension() == 3) {
            return new ExactQuaternion(re,
                    (ExactReal) imi.add((Ring.Member) imag.getElement(0)),
                    (ExactReal) imj.add((Ring.Member) imag.getElement(1)),
                    (ExactReal) imk.add((Ring.Member) imag.getElement(2)));
        } else
            throw new IllegalArgumentException("RingVector argument must be of length 3.");
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this number and another.
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member x) {
        if (x instanceof ExactQuaternion)
            return subtract((ExactQuaternion) x);
        else if (x instanceof Quaternion)
            return subtract((Quaternion) x);
        else if (x instanceof ExactReal)
            return subtractReal((ExactReal) x);
        else if (x instanceof Double)
            return subtractReal(((Double) x).value());
        else if (x instanceof Float)
            return subtractReal(((Float) x).value());
        else if (x instanceof Long)
            return subtractReal(((Long) x).value());
        else if (x instanceof Integer)
            return subtractReal(((Integer) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the subtraction of this quaternion and another.
     *
     * @param q a quaternion
     */
    public ExactQuaternion subtract(final ExactQuaternion q) {

        RingVector result;
        result = q.imag();

        return new ExactQuaternion(re.subtract(q.real()),
                (ExactReal) imi.subtract((Ring.Member) result.getElement(0)),
                (ExactReal) imj.subtract((Ring.Member) result.getElement(1)),
                (ExactReal) imk.subtract((Ring.Member) result.getElement(2)));
    }

    /**
     * Returns the subtraction of this quaternion and another.
     *
     * @param q a quaternion
     */
    public ExactQuaternion subtract(final Quaternion q) {
        return subtract(new ExactQuaternion(q));
    }

    /**
     * Returns the subtraction of this quaternion with a real part.
     *
     * @param real a real part
     */
    public ExactQuaternion subtractReal(final double real) {
        return new ExactQuaternion(re.subtract(new ExactReal(real)), imi, imj, imk);
    }

    /**
     * Returns the subtraction of this quaternion with a real part.
     *
     * @param real a real part
     */
    public ExactQuaternion subtractReal(final ExactReal real) {
        return new ExactQuaternion(re.subtract(real), imi, imj, imk);
    }

    /**
     * Returns the subtraction of this quaternion with an imaginary part.
     *
     * @param imag an imaginary part
     */
    public ExactQuaternion subtractImag(final RingVector imag) {
        if (imag.getDimension() == 3) {
            return new ExactQuaternion(re,
                    (ExactReal) imi.subtract((Ring.Member) imag.getElement(0)),
                    (ExactReal) imj.subtract((Ring.Member) imag.getElement(1)),
                    (ExactReal) imk.subtract((Ring.Member) imag.getElement(2)));
        } else
            throw new IllegalArgumentException("RingVector argument must be of length 3.");
    }

// MULTIPLICATION

    /**
     * Returns the multiplication of this number by a real scalar.
     */
    public Module.Member scalarMultiply(final Ring.Member x) {
        if (x instanceof ExactReal)
            return multiply((ExactReal) x);
        else if (x instanceof Double)
            return multiply(((Double) x).value());
        else if (x instanceof Float)
            return multiply(((Float) x).value());
        else if (x instanceof Rational)
            return multiply(((Rational) x).value());
        else if (x instanceof Long)
            return multiply(((Long) x).value());
        else if (x instanceof Integer)
            return multiply(((Integer) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this number and another.
     */
    public Ring.Member multiply(final Ring.Member x) {
        if (x instanceof ExactQuaternion)
            return multiply((ExactQuaternion) x);
        else if (x instanceof Quaternion)
            return multiply((Quaternion) x);
        else if (x instanceof ExactReal)
            return multiply((ExactReal) x);
        else if (x instanceof Double)
            return multiply(((Double) x).value());
        else if (x instanceof Float)
            return multiply(((Float) x).value());
        else if (x instanceof Rational)
            return multiply(((Rational) x).value());
        else if (x instanceof Long)
            return multiply(((Long) x).value());
        else if (x instanceof Integer)
            return multiply(((Integer) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this quaternion and another.
     *
     * @param q a quaternion
     */
    public ExactQuaternion multiply(final ExactQuaternion q) {
        RingVector result;
        result = q.imag();
        return new ExactQuaternion((ExactReal) re.multiply(q.real()).subtract(imi.multiply((Ring.Member) result.getElement(0))).subtract(imj.multiply((Ring.Member) result.getElement(1))).subtract(imk.multiply((Ring.Member) result.getElement(2))),
                (ExactReal) re.multiply((Ring.Member) result.getElement(0)).add(q.real().multiply(imi)).add(imj.multiply((Ring.Member) result.getElement(2)).subtract(((Ring.Member) result.getElement(1)).multiply(imk))),
                (ExactReal) re.multiply((Ring.Member) result.getElement(1)).add(q.real().multiply(imj)).add(imk.multiply((Ring.Member) result.getElement(0)).subtract(((Ring.Member) result.getElement(2)).multiply(imi))),
                (ExactReal) re.multiply((Ring.Member) result.getElement(2)).add(q.real().multiply(imk)).add(imi.multiply((Ring.Member) result.getElement(1)).subtract(((Ring.Member) result.getElement(0)).multiply(imj))));
    }

    /**
     * Returns the multiplication of this quaternion and another.
     *
     * @param q a real number
     */
    public ExactQuaternion multiply(final Quaternion q) {
        return multiply(new ExactQuaternion(q));
    }

    /**
     * Returns the multiplication of this quaternion by a scalar.
     *
     * @param x a real number
     */
    public ExactQuaternion multiply(final double x) {
        return new ExactQuaternion(re.multiply(new ExactReal(x)), imi.multiply(new ExactReal(x)), imj.multiply(new ExactReal(x)), imk.multiply(new ExactReal(x)));
    }

    /**
     * Returns the multiplication of this quaternion by a scalar.
     *
     * @param x a real number
     */
    public ExactQuaternion multiply(final ExactReal x) {
        return new ExactQuaternion(re.multiply(x), imi.multiply(x), imj.multiply(x), imk.multiply(x));
    }

// DIVISION

    /**
     * Returns the division of this number by a real scalar.
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        if (x instanceof ExactReal)
            return divide((ExactReal) x);
        else if (x instanceof Double)
            return divide(((Double) x).value());
        else if (x instanceof Float)
            return divide(((Float) x).value());
            //Wouldn't it be nice if we could divide by Ring.Member instead ?
            //else if (x instanceof Long)
            //   return divide(((Long) x).value());
            //else if (x instanceof Integer)
            //    return divide(((Integer) x).value());
        else if (x instanceof Rational)
            return divide(((Rational) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the division of this number and another.
     */
    public Field.Member divide(final Field.Member x) {
        if (x instanceof ExactQuaternion)
            return divide((ExactQuaternion) x);
        else if (x instanceof Quaternion)
            return divide((Quaternion) x);
        else if (x instanceof ExactReal)
            return divide((ExactReal) x);
        else if (x instanceof Double)
            return divide(((Double) x).value());
        else if (x instanceof Float)
            return divide(((Float) x).value());
            //Wouldn't it be nice if we could divide by Ring.Member instead ?
            //else if (x instanceof Long)
            //     return divide(((Long) x).value());
            //else if (x instanceof Integer)
            //     return divide(((Integer) x).value());
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
    public ExactQuaternion divide(final ExactQuaternion q) {
        RingVector result;
        result = q.imag();
        final ExactReal qSumSqr = q.sumSquares();
        return new ExactQuaternion(new ExactReal((ExactReal) re.multiply(q.real()).add(imi.multiply((Ring.Member) result.getElement(0))).add(imj.multiply((Ring.Member) result.getElement(1))).add(imk.multiply((Ring.Member) result.getElement(2)))).divide(qSumSqr),
                new ExactReal((ExactReal) q.real().multiply(imi).subtract(re.multiply((Ring.Member) result.getElement(0))).subtract(imj.multiply((Ring.Member) result.getElement(2))).subtract(((Ring.Member) result.getElement(1)).multiply(imk))).divide(qSumSqr),
                new ExactReal((ExactReal) q.real().multiply(imj).subtract(re.multiply((Ring.Member) result.getElement(1))).subtract(imk.multiply((Ring.Member) result.getElement(0))).subtract(((Ring.Member) result.getElement(2)).multiply(imi))).divide(qSumSqr),
                new ExactReal((ExactReal) q.real().multiply(imk).subtract(re.multiply((Ring.Member) result.getElement(2))).subtract(imi.multiply((Ring.Member) result.getElement(1))).subtract(((Ring.Member) result.getElement(0)).multiply(imj))).divide(qSumSqr));
    }

    /**
     * Returns the division of this quaternion by a scalar.
     *
     * @param x a real number
     * @throws ArithmeticException If divide by zero.
     */
    public ExactQuaternion divide(final double x) {
        return new ExactQuaternion(re.divide(new ExactReal(x)), imi.divide(new ExactReal(x)), imj.divide(new ExactReal(x)), imk.divide(new ExactReal(x)));
    }

    /**
     * Returns the division of this quaternion by a scalar.
     *
     * @param x a real number
     * @throws ArithmeticException If divide by zero.
     */
    public ExactQuaternion divide(final ExactReal x) {
        return new ExactQuaternion(re.divide(x), imi.divide(x), imj.divide(x), imk.divide(x));
    }

    public Object clone() {
        return new ExactQuaternion(this);
    }

}
