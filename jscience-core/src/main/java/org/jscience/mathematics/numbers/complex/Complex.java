package org.jscience.mathematics.numbers.complex;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.structures.rings.FieldElement;

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
public final class Complex implements Field<Complex>, FieldElement<Complex> {

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

    public static Complex of(Real real) {
        return new Complex(real, Real.ZERO);
    }

    public static Complex of(double real, double imaginary) {
        return new Complex(Real.of(real), Real.of(imaginary));
    }

    public static Complex of(float real, float imaginary) {
        return new Complex(Real.of(real), Real.of(imaginary));
    }

    public static Complex of(double real) {
        return new Complex(Real.of(real), Real.ZERO);
    }

    public static Complex of(float real) {
        return new Complex(Real.of(real), Real.ZERO);
    }

    public static Complex ofPolar(double magnitude, double phase) {
        return new Complex(Real.of(magnitude * Math.cos(phase)),
                Real.of(magnitude * Math.sin(phase)));
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
        return imaginary.atan2(real);
    }

    // --- Transcendental Functions ---

    /**
     * Returns e raised to this complex number.
     * exp(a + bi) = e^a * (cos(b) + i*sin(b))
     * 
     * @return e^this
     */
    public Complex exp() {
        Real expReal = real.exp();
        return new Complex(expReal.multiply(imaginary.cos()), expReal.multiply(imaginary.sin()));
    }

    /**
     * Returns the natural logarithm of this complex number.
     * log(z) = ln(|z|) + i*arg(z)
     * 
     * @return ln(this)
     */
    public Complex log() {
        return new Complex(abs().log(), arg());
    }

    /**
     * Returns this complex number raised to a complex power.
     * z^w = exp(w * log(z))
     * 
     * @param exponent the exponent
     * @return this^exponent
     */
    public Complex pow(Complex exponent) {
        return log().multiply(exponent).exp();
    }

    /**
     * Returns this complex number raised to a double power.
     * 
     * @param exponent the exponent
     * @return this^exponent
     */
    public Complex pow(double exponent) {
        return log().multiply(Complex.of(Real.of(exponent))).exp();
    }

    /**
     * Returns the sine of this complex number.
     * sin(a + bi) = sin(a)cosh(b) + i*cos(a)sinh(b)
     * 
     * @return sin(this)
     */
    public Complex sin() {
        return new Complex(real.sin().multiply(imaginary.cosh()),
                real.cos().multiply(imaginary.sinh()));
    }

    /**
     * Returns the cosine of this complex number.
     * cos(a + bi) = cos(a)cosh(b) - i*sin(a)sinh(b)
     * 
     * @return cos(this)
     */
    public Complex cos() {
        return new Complex(real.cos().multiply(imaginary.cosh()),
                real.sin().multiply(imaginary.sinh()).negate());
    }

    /**
     * Returns the tangent of this complex number.
     * tan(z) = sin(z) / cos(z)
     * 
     * @return tan(this)
     */
    public Complex tan() {
        return sin().divide(cos());
    }

    /**
     * Returns the arcsine of this complex number.
     * asin(z) = -i * log(iz + sqrt(1 - z^2))
     * 
     * @return asin(this)
     */
    public Complex asin() {
        Complex iz = I.multiply(this);
        Complex root = ONE.subtract(this.multiply(this)).sqrt();
        return iz.add(root).log().multiply(I.negate());
    }

    /**
     * Returns the arccosine of this complex number.
     * acos(z) = -i * log(z + i*sqrt(1 - z^2))
     * 
     * @return acos(this)
     */
    public Complex acos() {
        Complex root = ONE.subtract(this.multiply(this)).sqrt();
        Complex iRoot = I.multiply(root);
        return this.add(iRoot).log().multiply(I.negate());
    }

    /**
     * Returns the arctangent of this complex number.
     * atan(z) = (i/2) * log((1-iz)/(1+iz))
     * 
     * @return atan(this)
     */
    public Complex atan() {
        Complex iz = I.multiply(this);
        Complex num = ONE.subtract(iz);
        Complex den = ONE.add(iz);
        Complex logVal = num.divide(den).log();
        return I.multiply(Complex.of(0.5)).multiply(logVal);
    }

    /**
     * Returns the hyperbolic sine of this complex number.
     * sinh(z) = (exp(z) - exp(-z)) / 2
     * 
     * @return sinh(this)
     */
    public Complex sinh() {
        Complex exp = exp();
        Complex negExp = negate().exp();
        return exp.subtract(negExp).divide(Complex.of(2.0));
    }

    /**
     * Returns the hyperbolic cosine of this complex number.
     * cosh(z) = (exp(z) + exp(-z)) / 2
     * 
     * @return cosh(this)
     */
    public Complex cosh() {
        Complex exp = exp();
        Complex negExp = negate().exp();
        return exp.add(negExp).divide(Complex.of(2.0));
    }

    /**
     * Returns the hyperbolic tangent of this complex number.
     * tanh(z) = sinh(z) / cosh(z)
     * 
     * @return tanh(this)
     */
    public Complex tanh() {
        return sinh().divide(cosh());
    }

    /**
     * Returns the inverse hyperbolic sine of this complex number.
     * asinh(z) = log(z + sqrt(z^2 + 1))
     * 
     * @return asinh(this)
     */
    public Complex asinh() {
        Complex z2p1 = this.multiply(this).add(ONE);
        return this.add(z2p1.sqrt()).log();
    }

    /**
     * Returns the inverse hyperbolic cosine of this complex number.
     * acosh(z) = log(z + sqrt(z^2 - 1))
     * 
     * @return acosh(this)
     */
    public Complex acosh() {
        Complex z2m1 = this.multiply(this).subtract(ONE);
        return this.add(z2m1.sqrt()).log();
    }

    /**
     * Returns the inverse hyperbolic tangent of this complex number.
     * atanh(z) = 0.5 * log((1 + z) / (1 - z))
     * 
     * @return atanh(this)
     */
    public Complex atanh() {
        Complex onePlusZ = ONE.add(this);
        Complex oneMinusZ = ONE.subtract(this);
        return onePlusZ.divide(oneMinusZ).log().multiply(Complex.of(0.5));
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

    // --- Field Interface Implementation ---
    @Override
    public int characteristic() {
        return 0; // Complex numbers have characteristic 0
    }

    @Override
    public boolean contains(Complex element) {
        return element != null;
    }

    @Override
    public String description() {
        return "Complex Numbers (\u2102)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
