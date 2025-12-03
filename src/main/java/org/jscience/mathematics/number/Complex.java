package org.jscience.mathematics.number;

/**
 * Represents a complex number (ℂ), defined as a + bi where a, b are Real
 * numbers.
 * <p>
 * Complex numbers form a Field under addition and multiplication.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Complex {

    private final Real real;
    private final Real imaginary;

    public static final Complex ZERO = new Complex(Real.ZERO, Real.ZERO);
    public static final Complex ONE = new Complex(Real.ONE, Real.ZERO);
    public static final Complex I = new Complex(Real.ZERO, Real.ONE);

    private Complex(Real real, Real imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static Complex of(Real real, Real imaginary) {
        return new Complex(real, imaginary);
    }

    public static Complex of(double real, double imaginary) {
        return new Complex(Real.of(real), Real.of(imaginary));
    }

    public static Complex of(double real) {
        return new Complex(Real.of(real), Real.ZERO);
    }

    public Real getReal() {
        return real;
    }

    public Real getImaginary() {
        return imaginary;
    }

    /**
     * Returns the real part as a double.
     * 
     * @return the real part
     */
    public double real() {
        return real.doubleValue();
    }

    /**
     * Returns the imaginary part as a double.
     * 
     * @return the imaginary part
     */
    public double imaginary() {
        return imaginary.doubleValue();
    }

    // --- Operations ---

    public Complex operate(Complex a, Complex b) {
        return a.add(b);
    }

    public Complex add(Complex a, Complex b) {
        return a.add(b);
    }

    public Complex add(Complex other) {
        return new Complex(this.real.add(other.real), this.imaginary.add(other.imaginary));
    }

    public Complex zero() {
        return ZERO;
    }

    public Complex negate(Complex a) {
        return a.negate();
    }

    public Complex negate() {
        return new Complex(real.negate(), imaginary.negate());
    }

    public Complex subtract(Complex a, Complex b) {
        return a.subtract(b);
    }

    public Complex subtract(Complex other) {
        return new Complex(this.real.subtract(other.real), this.imaginary.subtract(other.imaginary));
    }

    public Complex multiply(Complex a, Complex b) {
        return a.multiply(b);
    }

    public Complex multiply(Complex other) {
        // (a + bi)(c + di) = (ac - bd) + (ad + bc)i
        Real ac = this.real.multiply(other.real);
        Real bd = this.imaginary.multiply(other.imaginary);
        Real ad = this.real.multiply(other.imaginary);
        Real bc = this.imaginary.multiply(other.real);
        return new Complex(ac.subtract(bd), ad.add(bc));
    }

    public Complex one() {
        return ONE;
    }

    public boolean isMultiplicationCommutative() {
        return true;
    }

    public Complex inverse(Complex a) {
        return a.inverse();
    }

    public Complex inverse() {
        // 1 / (a + bi) = (a - bi) / (a^2 + b^2)
        Real denominator = real.multiply(real).add(imaginary.multiply(imaginary));
        return new Complex(real.divide(denominator), imaginary.negate().divide(denominator));
    }

    public Complex divide(Complex a, Complex b) {
        return a.divide(b);
    }

    public Complex divide(Complex other) {
        return multiply(other.inverse());
    }

    public Complex conjugate() {
        return new Complex(real, imaginary.negate());
    }

    public Real abs() {
        // |z| = sqrt(a^2 + b^2)
        return real.multiply(real).add(imaginary.multiply(imaginary)).sqrt();
    }

    public Complex sqrt() {
        // sqrt(a + bi) = +/- (sqrt((|z| + a) / 2) + i * sgn(b) * sqrt((|z| - a) / 2))
        Real r = abs();
        Real two = Real.of(2.0);

        Real u = r.add(real).divide(two).sqrt();
        Real v = r.subtract(real).divide(two).sqrt();

        if (imaginary.sign() < 0) {
            v = v.negate();
        }

        return new Complex(u, v);
    }

    public Real arg() {
        // arg(z) = atan2(b, a)
        // Note: Real doesn't have atan2 yet, using Math.atan2 via doubleValue for now
        return Real.of(Math.atan2(imaginary.doubleValue(), real.doubleValue()));
    }

    @Override
    public String toString() {
        if (imaginary.sign() < 0) {
            return real + " - " + imaginary.abs() + "i";
        }
        return real + " + " + imaginary + "i";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Complex))
            return false;
        Complex other = (Complex) obj;
        return real.equals(other.real) && imaginary.equals(other.imaginary);
    }

    @Override
    public int hashCode() {
        return 31 * real.hashCode() + imaginary.hashCode();
    }
}
