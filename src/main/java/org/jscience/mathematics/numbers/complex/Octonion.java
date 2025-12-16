package org.jscience.mathematics.numbers.complex;

import java.util.Objects;
import org.jscience.mathematics.structures.rings.Field;

/**
 * Represents an octonion number (8-dimensional hypercomplex number).
 * <p>
 * Octonions extend quaternions but are non-associative.
 * They form the largest normed division algebra over the real numbers.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class Octonion implements Field<Octonion> {

    /** The zero octonion. */
    public static final Octonion ZERO = new Octonion(0, 0, 0, 0, 0, 0, 0, 0);

    /** The unit (1,0,0,0,0,0,0,0). */
    public static final Octonion ONE = new Octonion(1, 0, 0, 0, 0, 0, 0, 0);

    private final double e0, e1, e2, e3, e4, e5, e6, e7;

    /**
     * Creates an octonion from 8 real components.
     * o = e0 + e1*i + e2*j + e3*k + e4*l + e5*il + e6*jl + e7*kl
     */
    public Octonion(double e0, double e1, double e2, double e3,
            double e4, double e5, double e6, double e7) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        this.e4 = e4;
        this.e5 = e5;
        this.e6 = e6;
        this.e7 = e7;
    }

    /**
     * Creates an octonion from a quaternion (last 4 components = 0).
     */
    public static Octonion fromQuaternion(Quaternion q) {
        return new Octonion(q.getReal(), q.getI(), q.getJ(), q.getK(), 0, 0, 0, 0);
    }

    /**
     * Creates an octonion from a complex number (last 6 components = 0).
     */
    public static Octonion fromComplex(Complex c) {
        return new Octonion(c.real(), c.imaginary(), 0, 0, 0, 0, 0, 0);
    }

    // Getters
    public double getE0() {
        return e0;
    }

    public double getE1() {
        return e1;
    }

    public double getE2() {
        return e2;
    }

    public double getE3() {
        return e3;
    }

    public double getE4() {
        return e4;
    }

    public double getE5() {
        return e5;
    }

    public double getE6() {
        return e6;
    }

    public double getE7() {
        return e7;
    }

    /**
     * Returns the real part (e0).
     */
    public double real() {
        return e0;
    }

    /**
     * Returns the norm (magnitude) of this octonion.
     */
    public double norm() {
        return Math.sqrt(normSquared());
    }

    /**
     * Returns the squared norm.
     */
    public double normSquared() {
        return e0 * e0 + e1 * e1 + e2 * e2 + e3 * e3 + e4 * e4 + e5 * e5 + e6 * e6 + e7 * e7;
    }

    /**
     * Returns the conjugate of this octonion.
     */
    public Octonion conjugate() {
        return new Octonion(e0, -e1, -e2, -e3, -e4, -e5, -e6, -e7);
    }

    /**
     * Returns the negation of this octonion.
     */
    public Octonion negate() {
        return new Octonion(-e0, -e1, -e2, -e3, -e4, -e5, -e6, -e7);
    }

    /**
     * Adds another octonion to this one.
     */
    public Octonion plus(Octonion o) {
        return new Octonion(
                e0 + o.e0, e1 + o.e1, e2 + o.e2, e3 + o.e3,
                e4 + o.e4, e5 + o.e5, e6 + o.e6, e7 + o.e7);
    }

    /**
     * Subtracts another octonion from this one.
     */
    public Octonion minus(Octonion o) {
        return new Octonion(
                e0 - o.e0, e1 - o.e1, e2 - o.e2, e3 - o.e3,
                e4 - o.e4, e5 - o.e5, e6 - o.e6, e7 - o.e7);
    }

    /**
     * Multiplies this octonion by a scalar.
     */
    public Octonion times(double s) {
        return new Octonion(e0 * s, e1 * s, e2 * s, e3 * s, e4 * s, e5 * s, e6 * s, e7 * s);
    }

    /**
     * Divides this octonion by a scalar.
     */
    public Octonion divides(double s) {
        return new Octonion(e0 / s, e1 / s, e2 / s, e3 / s, e4 / s, e5 / s, e6 / s, e7 / s);
    }

    /**
     * Multiplies this octonion by another using the Cayley-Dickson construction.
     * Note: Octonion multiplication is NOT associative!
     */
    public Octonion times(Octonion o) {
        // Using Cayley-Dickson construction: (a,b)*(c,d) = (ac - d*b*, a*d + cb)
        // where a,b,c,d are quaternions and * denotes conjugation

        double a0 = e0, a1 = e1, a2 = e2, a3 = e3;
        double b0 = e4, b1 = e5, b2 = e6, b3 = e7;
        double c0 = o.e0, c1 = o.e1, c2 = o.e2, c3 = o.e3;
        double d0 = o.e4, d1 = o.e5, d2 = o.e6, d3 = o.e7;

        // Quaternion multiplication for ac
        double ac0 = a0 * c0 - a1 * c1 - a2 * c2 - a3 * c3;
        double ac1 = a0 * c1 + a1 * c0 + a2 * c3 - a3 * c2;
        double ac2 = a0 * c2 - a1 * c3 + a2 * c0 + a3 * c1;
        double ac3 = a0 * c3 + a1 * c2 - a2 * c1 + a3 * c0;

        // d* = conjugate of d as quaternion: (d0, -d1, -d2, -d3)
        // d* * b* where b* = (b0, -b1, -b2, -b3)
        double db0 = d0 * b0 - (-d1) * (-b1) - (-d2) * (-b2) - (-d3) * (-b3);
        double db1 = d0 * (-b1) + (-d1) * b0 + (-d2) * (-b3) - (-d3) * (-b2);
        double db2 = d0 * (-b2) - (-d1) * (-b3) + (-d2) * b0 + (-d3) * (-b1);
        double db3 = d0 * (-b3) + (-d1) * (-b2) - (-d2) * (-b1) + (-d3) * b0;

        // First quaternion result: ac - d*b*
        double r0 = ac0 - db0;
        double r1 = ac1 - db1;
        double r2 = ac2 - db2;
        double r3 = ac3 - db3;

        // a* = conjugate of a: (a0, -a1, -a2, -a3)
        // a* * d
        double ad0 = a0 * d0 - (-a1) * d1 - (-a2) * d2 - (-a3) * d3;
        double ad1 = a0 * d1 + (-a1) * d0 + (-a2) * d3 - (-a3) * d2;
        double ad2 = a0 * d2 - (-a1) * d3 + (-a2) * d0 + (-a3) * d1;
        double ad3 = a0 * d3 + (-a1) * d2 - (-a2) * d1 + (-a3) * d0;

        // c * b
        double cb0 = c0 * b0 - c1 * b1 - c2 * b2 - c3 * b3;
        double cb1 = c0 * b1 + c1 * b0 + c2 * b3 - c3 * b2;
        double cb2 = c0 * b2 - c1 * b3 + c2 * b0 + c3 * b1;
        double cb3 = c0 * b3 + c1 * b2 - c2 * b1 + c3 * b0;

        // Second quaternion result: a*d + cb
        double r4 = ad0 + cb0;
        double r5 = ad1 + cb1;
        double r6 = ad2 + cb2;
        double r7 = ad3 + cb3;

        return new Octonion(r0, r1, r2, r3, r4, r5, r6, r7);
    }

    /**
     * Returns the multiplicative inverse of this octonion.
     */
    public Octonion inverse() {
        double n2 = normSquared();
        if (n2 < 1e-30) {
            throw new ArithmeticException("Cannot invert zero or near-zero octonion");
        }
        return conjugate().divides(n2);
    }

    /**
     * Divides this octonion by another.
     * Note: Since octonions are non-associative, left and right division differ.
     * This implements right division: a / b = a * b^(-1)
     */
    public Octonion divides(Octonion o) {
        return this.times(o.inverse());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Octonion))
            return false;
        Octonion o = (Octonion) obj;
        double tol = 1e-10;
        return Math.abs(e0 - o.e0) < tol && Math.abs(e1 - o.e1) < tol &&
                Math.abs(e2 - o.e2) < tol && Math.abs(e3 - o.e3) < tol &&
                Math.abs(e4 - o.e4) < tol && Math.abs(e5 - o.e5) < tol &&
                Math.abs(e6 - o.e6) < tol && Math.abs(e7 - o.e7) < tol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(e0, e1, e2, e3, e4, e5, e6, e7);
    }

    @Override
    public String toString() {
        return String.format("(%.4f + %.4fi + %.4fj + %.4fk + %.4fl + %.4fil + %.4fjl + %.4fkl)",
                e0, e1, e2, e3, e4, e5, e6, e7);
    }

    // --- Field Implementation ---

    @Override
    public Octonion operate(Octonion left, Octonion right) {
        return left.plus(right);
    }

    @Override
    public Octonion add(Octonion left, Octonion right) {
        return left.plus(right);
    }

    @Override
    public Octonion subtract(Octonion left, Octonion right) {
        return left.minus(right);
    }

    @Override
    public Octonion multiply(Octonion left, Octonion right) {
        return left.times(right);
    }

    @Override
    public Octonion zero() {
        return ZERO;
    }

    @Override
    public Octonion one() {
        return ONE;
    }

    @Override
    public Octonion negate(Octonion element) {
        return element.negate();
    }

    @Override
    public Octonion inverse(Octonion element) {
        return element.inverse();
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return false;
    }

    @Override
    public int characteristic() {
        return 0;
    }

    @Override
    public String description() {
        return "Octonions (O)";
    }

    @Override
    public boolean isEmpty() {
        return false; // Octonions are never empty as a field
    }

    @Override
    public boolean contains(Octonion element) {
        return element != null;
    }
}
