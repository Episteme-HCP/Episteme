package org.jscience.mathematics.numbers.complex;

import java.util.Objects;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.numbers.real.Real;

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
    public static final Octonion ZERO = new Octonion(Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO,
            Real.ZERO, Real.ZERO);

    /** The unit (1,0,0,0,0,0,0,0). */
    public static final Octonion ONE = new Octonion(Real.ONE, Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO,
            Real.ZERO, Real.ZERO);

    private final Real e0, e1, e2, e3, e4, e5, e6, e7;

    /**
     * Creates an octonion from 8 real components.
     * o = e0 + e1*i + e2*j + e3*k + e4*l + e5*il + e6*jl + e7*kl
     */
    public Octonion(Real e0, Real e1, Real e2, Real e3,
            Real e4, Real e5, Real e6, Real e7) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        this.e4 = e4;
        this.e5 = e5;
        this.e6 = e6;
        this.e7 = e7;
    }

    public Octonion(double e0, double e1, double e2, double e3,
            double e4, double e5, double e6, double e7) {
        this(Real.of(e0), Real.of(e1), Real.of(e2), Real.of(e3),
                Real.of(e4), Real.of(e5), Real.of(e6), Real.of(e7));
    }

    /**
     * Creates an octonion from a quaternion (last 4 components = 0).
     */
    public static Octonion fromQuaternion(Quaternion q) {
        return new Octonion(q.getReal(), q.getI(), q.getJ(), q.getK(), Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO);
    }

    /**
     * Creates an octonion from a complex number (last 6 components = 0).
     */
    public static Octonion fromComplex(Complex c) {
        return new Octonion(Real.of(c.real()), Real.of(c.imaginary()), Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO,
                Real.ZERO, Real.ZERO);
    }

    // Getters
    public Real getE0() {
        return e0;
    }

    public Real getE1() {
        return e1;
    }

    public Real getE2() {
        return e2;
    }

    public Real getE3() {
        return e3;
    }

    public Real getE4() {
        return e4;
    }

    public Real getE5() {
        return e5;
    }

    public Real getE6() {
        return e6;
    }

    public Real getE7() {
        return e7;
    }

    /**
     * Returns the real part (e0).
     */
    public Real real() {
        return e0;
    }

    /**
     * Returns the norm (magnitude) of this octonion.
     */
    public Real norm() {
        return normSquared().sqrt();
    }

    /**
     * Returns the squared norm.
     */
    public Real normSquared() {
        return e0.multiply(e0).add(e1.multiply(e1)).add(e2.multiply(e2)).add(e3.multiply(e3))
                .add(e4.multiply(e4)).add(e5.multiply(e5)).add(e6.multiply(e6)).add(e7.multiply(e7));
    }

    /**
     * Returns the conjugate of this octonion.
     */
    public Octonion conjugate() {
        return new Octonion(e0, e1.negate(), e2.negate(), e3.negate(),
                e4.negate(), e5.negate(), e6.negate(), e7.negate());
    }

    /**
     * Returns the negation of this octonion.
     */
    public Octonion negate() {
        return new Octonion(e0.negate(), e1.negate(), e2.negate(), e3.negate(),
                e4.negate(), e5.negate(), e6.negate(), e7.negate());
    }

    /**
     * Adds another octonion to this one.
     */
    public Octonion plus(Octonion o) {
        return new Octonion(
                e0.add(o.e0), e1.add(o.e1), e2.add(o.e2), e3.add(o.e3),
                e4.add(o.e4), e5.add(o.e5), e6.add(o.e6), e7.add(o.e7));
    }

    /**
     * Subtracts another octonion from this one.
     */
    public Octonion minus(Octonion o) {
        return new Octonion(
                e0.subtract(o.e0), e1.subtract(o.e1), e2.subtract(o.e2), e3.subtract(o.e3),
                e4.subtract(o.e4), e5.subtract(o.e5), e6.subtract(o.e6), e7.subtract(o.e7));
    }

    /**
     * Multiplies this octonion by a scalar.
     */
    public Octonion times(double s) {
        Real sr = Real.of(s);
        return new Octonion(e0.multiply(sr), e1.multiply(sr), e2.multiply(sr), e3.multiply(sr),
                e4.multiply(sr), e5.multiply(sr), e6.multiply(sr), e7.multiply(sr));
    }

    /**
     * Divides this octonion by a scalar.
     */
    public Octonion divides(double s) {
        Real sr = Real.of(s);
        return new Octonion(e0.divide(sr), e1.divide(sr), e2.divide(sr), e3.divide(sr),
                e4.divide(sr), e5.divide(sr), e6.divide(sr), e7.divide(sr));
    }

    public Octonion divides(Real sr) {
        return new Octonion(e0.divide(sr), e1.divide(sr), e2.divide(sr), e3.divide(sr),
                e4.divide(sr), e5.divide(sr), e6.divide(sr), e7.divide(sr));
    }

    /**
     * Multiplies this octonion by another using the Cayley-Dickson construction.
     * Note: Octonion multiplication is NOT associative!
     */
    public Octonion times(Octonion o) {
        // Using Cayley-Dickson construction: (a,b)*(c,d) = (ac - d*b*, a*d + cb)
        // where a,b,c,d are quaternions and * denotes conjugation

        Real a0 = e0, a1 = e1, a2 = e2, a3 = e3;
        Real b0 = e4, b1 = e5, b2 = e6, b3 = e7;
        Real c0 = o.e0, c1 = o.e1, c2 = o.e2, c3 = o.e3;
        Real d0 = o.e4, d1 = o.e5, d2 = o.e6, d3 = o.e7;

        // Quaternion multiplication logic applied to Real components
        // Helper lambda or method would be cleaner, but inline for now.
        // ac
        Real ac0 = a0.multiply(c0).subtract(a1.multiply(c1)).subtract(a2.multiply(c2)).subtract(a3.multiply(c3));
        Real ac1 = a0.multiply(c1).add(a1.multiply(c0)).add(a2.multiply(c3)).subtract(a3.multiply(c2));
        Real ac2 = a0.multiply(c2).subtract(a1.multiply(c3)).add(a2.multiply(c0)).add(a3.multiply(c1));
        Real ac3 = a0.multiply(c3).add(a1.multiply(c2)).subtract(a2.multiply(c1)).add(a3.multiply(c0));

        // db* (d * conjugate(b))

        Real nb1 = b1.negate();
        Real nb2 = b2.negate();
        Real nb3 = b3.negate();

        Real db0 = d0.multiply(b0).subtract(d1.multiply(nb1)).subtract(d2.multiply(nb2)).subtract(d3.multiply(nb3));
        Real db1 = d0.multiply(nb1).add(d1.multiply(b0)).add(d2.multiply(nb3)).subtract(d3.multiply(nb2));
        Real db2 = d0.multiply(nb2).subtract(d1.multiply(nb3)).add(d2.multiply(b0)).add(d3.multiply(nb1));
        Real db3 = d0.multiply(nb3).add(d1.multiply(nb2)).subtract(d2.multiply(nb1)).add(d3.multiply(b0));

        // First quaternion result: ac - d*b*
        Real r0 = ac0.subtract(db0);
        Real r1 = ac1.subtract(db1);
        Real r2 = ac2.subtract(db2);
        Real r3 = ac3.subtract(db3);

        // a*d (conjugate(a) * d)
        Real na1 = a1.negate();
        Real na2 = a2.negate();
        Real na3 = a3.negate();

        Real ad0 = a0.multiply(d0).subtract(na1.multiply(d1)).subtract(na2.multiply(d2)).subtract(na3.multiply(d3));
        Real ad1 = a0.multiply(d1).add(na1.multiply(d0)).add(na2.multiply(d3)).subtract(na3.multiply(d2));
        Real ad2 = a0.multiply(d2).subtract(na1.multiply(d3)).add(na2.multiply(d0)).add(na3.multiply(d1));
        Real ad3 = a0.multiply(d3).add(na1.multiply(d2)).subtract(na2.multiply(d1)).add(na3.multiply(d0));

        // cb
        Real cb0 = c0.multiply(b0).subtract(c1.multiply(b1)).subtract(c2.multiply(b2)).subtract(c3.multiply(b3));
        Real cb1 = c0.multiply(b1).add(c1.multiply(b0)).add(c2.multiply(b3)).subtract(c3.multiply(b2));
        Real cb2 = c0.multiply(b2).subtract(c1.multiply(b3)).add(c2.multiply(b0)).add(c3.multiply(b1));
        Real cb3 = c0.multiply(b3).add(c1.multiply(b2)).subtract(c2.multiply(b1)).add(c3.multiply(b0));

        // Second quaternion result: a*d + cb
        Real r4 = ad0.add(cb0);
        Real r5 = ad1.add(cb1);
        Real r6 = ad2.add(cb2);
        Real r7 = ad3.add(cb3);

        return new Octonion(r0, r1, r2, r3, r4, r5, r6, r7);
    }

    /**
     * Returns the multiplicative inverse of this octonion.
     */
    public Octonion inverse() {
        Real n2 = normSquared();
        if (n2.isZero()) {
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
        return e0.equals(o.e0) && e1.equals(o.e1) && e2.equals(o.e2) && e3.equals(o.e3) &&
                e4.equals(o.e4) && e5.equals(o.e5) && e6.equals(o.e6) && e7.equals(o.e7);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e0, e1, e2, e3, e4, e5, e6, e7);
    }

    @Override
    public String toString() {
        return String.format("(%s + %si + %sj + %sk + %sl + %sil + %sjl + %skl)",
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
