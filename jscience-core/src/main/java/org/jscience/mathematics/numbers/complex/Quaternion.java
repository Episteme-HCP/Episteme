package org.jscience.mathematics.numbers.complex;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a Quaternion number (H).
 * <p>
 * q = a + bi + cj + dk
 * Quaternions form a non-commutative division ring (skew field).
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Quaternion implements Field<Quaternion> {

    private final Real a, b, c, d;

    public static final Quaternion ZERO = new Quaternion(Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO);
    public static final Quaternion ONE = new Quaternion(Real.ONE, Real.ZERO, Real.ZERO, Real.ZERO);
    public static final Quaternion I = new Quaternion(Real.ZERO, Real.ONE, Real.ZERO, Real.ZERO);
    public static final Quaternion J = new Quaternion(Real.ZERO, Real.ZERO, Real.ONE, Real.ZERO);
    public static final Quaternion K = new Quaternion(Real.ZERO, Real.ZERO, Real.ZERO, Real.ONE);

    public Quaternion(Real a, Real b, Real c, Real d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Quaternion(double a, double b, double c, double d) {
        this(Real.of(a), Real.of(b), Real.of(c), Real.of(d));
    }

    public static Quaternion of(Real a, Real b, Real c, Real d) {
        return new Quaternion(a, b, c, d);
    }

    public static Quaternion of(double a, double b, double c, double d) {
        return new Quaternion(a, b, c, d);
    }

    public static Quaternion of(Real real) {
        return new Quaternion(real, Real.ZERO, Real.ZERO, Real.ZERO);
    }

    public static Quaternion of(double real) {
        return new Quaternion(real, 0, 0, 0);
    }

    public Real getReal() {
        return a;
    }

    public Real getI() {
        return b;
    }

    public Real getJ() {
        return c;
    }

    public Real getK() {
        return d;
    }

    public Quaternion add(Quaternion other) {
        return new Quaternion(a.add(other.a), b.add(other.b), c.add(other.c), d.add(other.d));
    }

    public Quaternion subtract(Quaternion other) {
        return new Quaternion(a.subtract(other.a), b.subtract(other.b), c.subtract(other.c), d.subtract(other.d));
    }

    public Quaternion multiply(Quaternion other) {
        // Hamilton product
        Real newA = a.multiply(other.a).subtract(b.multiply(other.b)).subtract(c.multiply(other.c))
                .subtract(d.multiply(other.d));
        Real newB = a.multiply(other.b).add(b.multiply(other.a)).add(c.multiply(other.d)).subtract(d.multiply(other.c));
        Real newC = a.multiply(other.c).subtract(b.multiply(other.d)).add(c.multiply(other.a)).add(d.multiply(other.b));
        Real newD = a.multiply(other.d).add(b.multiply(other.c)).subtract(c.multiply(other.b)).add(d.multiply(other.a));
        return new Quaternion(newA, newB, newC, newD);
    }

    public Quaternion multiply(Real scalar) {
        return new Quaternion(a.multiply(scalar), b.multiply(scalar), c.multiply(scalar), d.multiply(scalar));
    }

    public Quaternion multiply(double scalar) {
        return multiply(Real.of(scalar));
    }

    public Quaternion conjugate() {
        return new Quaternion(a, b.negate(), c.negate(), d.negate());
    }

    public Real normSquared() {
        return a.multiply(a).add(b.multiply(b)).add(c.multiply(c)).add(d.multiply(d));
    }

    public Real norm() {
        return normSquared().sqrt();
    }

    public Quaternion inverse() {
        Real n2 = normSquared();
        if (n2.isZero())
            throw new ArithmeticException("Cannot invert zero quaternion");
        Quaternion conj = conjugate();
        return new Quaternion(conj.a.divide(n2), conj.b.divide(n2), conj.c.divide(n2), conj.d.divide(n2));
    }

    public Quaternion negate() {
        return new Quaternion(a.negate(), b.negate(), c.negate(), d.negate());
    }

    @Override
    public String toString() {
        return String.format("%s + %si + %sj + %sk", a, b, c, d);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Quaternion))
            return false;
        Quaternion q = (Quaternion) obj;
        return a.equals(q.a) && b.equals(q.b) && c.equals(q.c) && d.equals(q.d);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(a, b, c, d);
    }
    // --- Field Implementation ---

    @Override
    public Quaternion operate(Quaternion left, Quaternion right) {
        return left.add(right);
    }

    @Override
    public Quaternion zero() {
        return ZERO;
    }

    @Override
    public Quaternion one() {
        return ONE;
    }

    @Override
    public Quaternion negate(Quaternion element) {
        return element.negate();
    }

    @Override
    public Quaternion inverse(Quaternion element) {
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
        return "Quaternions (H)";
    }

    @Override
    public boolean isEmpty() {
        return false; // Quaternions are never empty as a field
    }

    @Override
    public boolean contains(Quaternion element) {
        return element != null;
    }

    @Override
    public Quaternion add(Quaternion a, Quaternion b) {
        return a.add(b);
    }

    @Override
    public Quaternion multiply(Quaternion a, Quaternion b) {
        return a.multiply(b);
    }
}
