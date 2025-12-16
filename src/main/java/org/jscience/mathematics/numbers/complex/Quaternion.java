package org.jscience.mathematics.numbers.complex;

import org.jscience.mathematics.structures.rings.Field;

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

    private final double a, b, c, d;

    public static final Quaternion ZERO = new Quaternion(0, 0, 0, 0);
    public static final Quaternion ONE = new Quaternion(1, 0, 0, 0);
    public static final Quaternion I = new Quaternion(0, 1, 0, 0);
    public static final Quaternion J = new Quaternion(0, 0, 1, 0);
    public static final Quaternion K = new Quaternion(0, 0, 0, 1);

    public Quaternion(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public static Quaternion of(double a, double b, double c, double d) {
        return new Quaternion(a, b, c, d);
    }

    public static Quaternion of(double real) {
        return new Quaternion(real, 0, 0, 0);
    }

    public double getReal() {
        return a;
    }

    public double getI() {
        return b;
    }

    public double getJ() {
        return c;
    }

    public double getK() {
        return d;
    }

    public Quaternion add(Quaternion other) {
        return new Quaternion(a + other.a, b + other.b, c + other.c, d + other.d);
    }

    public Quaternion subtract(Quaternion other) {
        return new Quaternion(a - other.a, b - other.b, c - other.c, d - other.d);
    }

    public Quaternion multiply(Quaternion other) {
        // Hamilton product
        double newA = a * other.a - b * other.b - c * other.c - d * other.d;
        double newB = a * other.b + b * other.a + c * other.d - d * other.c;
        double newC = a * other.c - b * other.d + c * other.a + d * other.b;
        double newD = a * other.d + b * other.c - c * other.b + d * other.a;
        return new Quaternion(newA, newB, newC, newD);
    }

    public Quaternion multiply(double scalar) {
        return new Quaternion(a * scalar, b * scalar, c * scalar, d * scalar);
    }

    public Quaternion conjugate() {
        return new Quaternion(a, -b, -c, -d);
    }

    public double normSquared() {
        return a * a + b * b + c * c + d * d;
    }

    public double norm() {
        return Math.sqrt(normSquared());
    }

    public Quaternion inverse() {
        double n2 = normSquared();
        if (n2 == 0.0)
            throw new ArithmeticException("Cannot invert zero quaternion");
        Quaternion conj = conjugate();
        return new Quaternion(conj.a / n2, conj.b / n2, conj.c / n2, conj.d / n2);
    }

    public Quaternion negate() {
        return new Quaternion(-a, -b, -c, -d);
    }

    @Override
    public String toString() {
        return String.format("%.2f + %.2fi + %.2fj + %.2fk", a, b, c, d);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Quaternion))
            return false;
        Quaternion q = (Quaternion) obj;
        return Double.compare(a, q.a) == 0 && Double.compare(b, q.b) == 0 &&
                Double.compare(c, q.c) == 0 && Double.compare(d, q.d) == 0;
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
