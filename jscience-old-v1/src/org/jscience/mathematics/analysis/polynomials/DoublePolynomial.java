package org.jscience.mathematics.analysis.polynomials;

import org.jscience.JScience;
import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.analysis.DoubleFunction;

import java.io.Serializable;


/**
 * A Polynomial as a <code>Ring.Member</code> over a
 * <i>real</i><code>Field</code>
 *
 * @author b.dietrich, Silvere Martin-Michiellot
 */

//we could provide rootsToPolynomial, roots, quadratic, cubic methods from ComplexPolynomial
public class DoublePolynomial extends DoubleFunction implements Polynomial, Serializable, Cloneable {
    /**
     * The real polynomial representing the additive identity.
     */
    public static final DoublePolynomial ZERO = DoublePolynomialRing.ZERO;

    /**
     * The real polynomial representing the multiplicative identity.
     */
    public static final DoublePolynomial ONE = DoublePolynomialRing.ONE;

    /**
     * Coefficients in ascending degree order
     */
    private double[] _c;

    /**
     * Creates a new instance of DoublePolynomial, polynom is always simplified
     * discarding every trailing zeros and array copied.
     *
     * @param coeff DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public DoublePolynomial(double[] coeff) {
        if (coeff == null) {
            throw new NullPointerException("Coefficients cannot be null");
        }
        _c = normalize(coeff);
    }

    /**
     * Normalises the coefficient array.
     * Trims off any leading (high degree) zero terms.
     */
    private static double[] normalize(double[] c) {
        int i = c.length - 1;
        while (i >= 0 && Math.abs(c[i]) <= java.lang.Double.valueOf(JScience.getProperty(
                "tolerance")).doubleValue())
            i--;
        if (i < 0) {
            return new double[]{0.0};
        } else if (i < c.length - 1) {
            double[] arr = new double[i + 1];
            System.arraycopy(c, 0, arr, 0, arr.length);
            return arr;
        } else {
            return c;
        }
    }

    /**
     * Creates a new instance of DoublePolynomial, polynom is always simplified
     * discarding every trailing zeros.
     *
     * @param polynomial DOCUMENT ME!
     */
    public DoublePolynomial(DoublePolynomial polynomial) {
        this(polynomial.getCoefficientsAsDoubles());
    }

    /**
     * Creates a new RealPolynomial object, polynom is always simplified
     * discarding every trailing zeros and array copied.
     *
     * @param f
     */
    public DoublePolynomial(Field.Member[] f) {
        if (f == null) {
            throw new NullPointerException("Coefficients cannot be null");
        }
        _c = normalize(DoublePolynomialRing.toDouble(f));
    }

    /**
     * Simple constructor. Build a one term polynomial from one coefficient and
     * the corresponding degree.
     *
     * @param d      coefficient
     * @param degree degree associated with the coefficient
     */
    public DoublePolynomial(double d, int degree) {
        if (Math.abs(d) <= java.lang.Double.valueOf(JScience.getProperty(
                "tolerance")).doubleValue()) {
            _c = new double[]{0.0};
        } else {
            _c = new double[degree + 1];

            for (int i = 0; i < degree; ++i) {
                _c[i] = 0;
            }

            _c[degree] = d;
        }
    }

    /**
     * Get the coefficient of degree k, i.e. <I>a_k</I> if <I>P(x)</I> :=
     * sum_{k=0}^n <I>a_k x^k</I>
     *
     * @param n degree
     * @return coefficient as described above
     */
    public Field.Member getCoefficient(int n) {
        return new Double(getCoefficientAsDouble(n));
    }

    /**
     * Get the coefficient of degree k, i.e. <I>a_k</I> if <I>P(x)</I> :=
     * sum_{k=0}^n <I>a_k x^k</I> as a real number
     *
     * @param n degree
     * @return coefficient as described above
     */
    public double getCoefficientAsDouble(int n) {
        //we could test negative values of n
        if (n >= _c.length) {
            return 0.;
        } else {
            return _c[n];
        }
    }

    /**
     * Get the coefficients as an array
     *
     * @return the coefficients as an array
     */
    public Field.Member[] getCoefficients() {
        return DoublePolynomialRing.toDouble(getCoefficientsAsDoubles());
    }

    /**
     * Get the coefficients as an array of doubles
     *
     * @return the coefficients as an array
     */
    public double[] getCoefficientsAsDoubles() {
        return _c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     */
    public void setCoefficient(int n, Field.Member c) {
        if (c instanceof Double) {
            setCoefficientAsDouble(n, ((Double) c).doubleValue());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     */
    public void setCoefficientAsDouble(int n, double c) {
        //we could test negative values of n
        if (n >= _c.length) {
            if (Math.abs(c) > java.lang.Double.valueOf(JScience.getProperty(
                    "tolerance")).doubleValue()) {
                double[] d = new double[n + 1];
                System.arraycopy(_c, 0, d, 0, _c.length);
                _c = d;
                _c[n] = c;
            }
        } else {
            if (n == (_c.length - 1) && (Math.abs(c) <= java.lang.Double.valueOf(JScience.getProperty(
                    "tolerance")).doubleValue())) {
                _c[n] = 0;
                _c = normalize(_c);
            } else {
                _c[n] = c;
            }
        }
    }

    /**
     * Evaluates this polynomial.
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double map(double x) {
        return PolynomialMathUtils.evalPolynomial(this, x);
    }

    /**
     * Evaluates this polynomial.
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double map(float x) {
        return map((double) x);
    }

    /**
     * Evaluates this polynomial.
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double map(long x) {
        return map((double) x);
    }

    /**
     * Evaluates this polynomial.
     *
     * @param x DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double map(int x) {
        return map((double) x);
    }

    /**
     * The degree understood as the
     * highest degree
     *
     * @return the degree
     */
    public int degree() {
        return _c.length - 1;
    }

    public Object getSet() {
        return DoublePolynomialRing.getInstance();
    }

    /**
     * Returns true if this polynomial is equal to zero.
     * All coefficients are tested for |a_k| < GlobalSettings.ZERO_TOL.
     *
     * @return true if all coefficients <  GlobalSettings.ZERO_TOL
     */
    public boolean isZero() {
        for (int k = 0; k < _c.length; k++) {
            if (Math.abs(_c[k]) > java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")).doubleValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if this polynomial is equal to one.
     * It is tested, whether |a_0 - 1| <= GlobalSettings.ZERO_TOL and the remaining
     * coefficients are |a_k| < GlobalSettings.ZERO_TOL.
     *
     * @return true if this is equal to one.
     */
    public boolean isOne() {
        if (Math.abs(_c[0] - 1.0) > java.lang.Double.valueOf(
                JScience.getProperty("tolerance")).doubleValue())
            return false;

        for (int k = 1; k < _c.length; k++) {
            if (Math.abs(_c[k]) > java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")).doubleValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * The group composition law. Returns a new polynom with grade = max(
     * this.degree, g.degree)
     *
     * @param g a group member
     * @return DOCUMENT ME!
     */
    public DoubleFunction add(DoubleFunction g) {
        if (g instanceof DoublePolynomial) {
            DoublePolynomial p = (DoublePolynomial) g;
            int maxgrade = PolynomialMathUtils.maxDegree(this, p);
            double[] c = new double[maxgrade + 1];

            for (int k = 0; k < c.length; k++) {
                c[k] = getCoefficientAsDouble(k) + p.getCoefficientAsDouble(k);
            }

            return new DoublePolynomial(c);
        } else {
            return super.add(g);
        }
    }

    /**
     * Differentiate the real polynomial. Only useful iff the polynomial is
     * built over a banach space and an appropriate multiplication law is
     * provided.
     *
     * @return a new polynomial with degree = max(this.degree-1 , 0)
     */
    public DoubleFunction differentiate() {
        if (degree() == 0) {
            return (DoublePolynomial) DoublePolynomialRing.getInstance().zero();
        } else {
            double[] dn = new double[_c.length - 1];

            for (int k = 0; k < _c.length; k++) {
                dn[k] = getCoefficientAsDouble(k + 1) * (k + 1);
            }

            return new DoublePolynomial(dn);
        }
    }

    /**
     * return a new real Polynomial with coefficients divided by <I>f</I>
     *
     * @param f divisor
     * @return new Polynomial with coefficients /= <I>f</I>
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Polynomial scalarDivide(Field.Member f) {
        if (f instanceof Double) {
            double a = ((Double) f).value();

            return scalarDivide(a);
        } else {
            throw new IllegalArgumentException(
                    "Member class not recognised by this method.");
        }
    }

    /**
     * return a new real Polynomial with coefficients divided by <I>a</I>
     *
     * @param a divisor
     * @return new Polynomial with coefficients /= <I>a</I>
     */
    public DoublePolynomial scalarDivide(double a) {
        double[] c = new double[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k] / a;
        }

        return new DoublePolynomial(c);
    }

    /**
     * Is this-o == Null ?
     *
     * @param o the other polynomial
     * @return true if so
     */
    //may be we should use tolerance on this
    public boolean equals(Object o) {
        boolean result = false;

        if (o == this) {
            result = true;
        } else if (o instanceof DoublePolynomial) {
            DoublePolynomial p = (DoublePolynomial) o;

            return ((DoublePolynomial) this.subtract(p)).isZero();
        }

        return result;
    }

    /**
     * Some kind of hashcode... (Since I have an equals)
     *
     * @return a hashcode
     */
    public int hashCode() {
        int res = 0;

        for (int k = 0; k < _c.length; k++) {
            res += (int) (getCoefficientAsDouble(k) * 10.0);
        }

        return res;
    }

    /**
     * "inverse" operation for differentiate, zero degree constant set to zero
     *
     * @return the integrated polynomial
     */
    public DoublePolynomial integrate() {
        double[] dn = new double[_c.length + 1];

        for (int k = 0; k < dn.length; k++) {
            dn[k + 1] = getCoefficientAsDouble(k) / (k + 1);
        }

        return new DoublePolynomial(dn);
    }

    /**
     * Returns the multiplication of this polynomial by a scalar
     *
     * @param f
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Polynomial scalarMultiply(Field.Member f) {
        if (f instanceof Double) {
            double a = ((Double) f).value();

            return scalarMultiply(a);
        } else {
            throw new IllegalArgumentException(
                    "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this polynomial by a scalar
     *
     * @param a factor
     * @return new Polynomial with coefficients = <I>a</I>
     */
    public DoublePolynomial scalarMultiply(double a) {
        double[] c = new double[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k] * a;
        }

        return new DoublePolynomial(c);
    }

    /**
     * The multiplication law. Multiplies this Polynomial with another
     *
     * @param r a RealFunction
     * @return a new Polynomial with grade = max( this.grade, r.grade) + min(
     *         this.grade, r.grade) -1
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public DoubleFunction multiply(DoubleFunction r) {
        if (r instanceof DoublePolynomial) {
            DoublePolynomial p = (DoublePolynomial) r;
            double[] n = new double[_c.length + p._c.length - 1];

            for (int k = 0; k < _c.length; k++) {
                double tis = getCoefficientAsDouble(k);

                for (int j = 0; j < p._c.length; j++) {
                    double tat = p.getCoefficientAsDouble(j);
                    n[k + j] += (tis * tat);
                }
            }

            return new DoublePolynomial(n);
        } else {
            return super.multiply(r);
        }
    }

    /**
     * Returns the inverse member. (That is mult(-1))
     *
     * @return inverse
     */
    public AbelianGroup.Member negate() {
        double[] c = new double[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = -_c[k];
        }

        return new DoublePolynomial(c);
    }

    /**
     * The group composition law with inverse.
     *
     * @param g a group member
     * @return DOCUMENT ME!
     */
    public DoubleFunction subtract(DoubleFunction g) {
        if (g instanceof DoublePolynomial) {
            DoublePolynomial p = (DoublePolynomial) g;
            int maxgrade = PolynomialMathUtils.maxDegree(this, p);
            double[] c = new double[maxgrade + 1];

            for (int k = 0; k < c.length; k++) {
                c[k] = getCoefficientAsDouble(k) - p.getCoefficientAsDouble(k);
            }

            return new DoublePolynomial(c);
        } else {
            return super.subtract(g);
        }
    }

    /**
     * String representation <I>P(x) = a_k x^k +...</I>
     *
     * @return String
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("P(x) = ");

        if (_c[degree()] < 0.0) {
            sb.append("-");
        } else {
            sb.append(" ");
        }

        for (int k = degree(); k > 0; k--) {
            sb.append(Math.abs(_c[k])).append("x^").append(k).append((_c[k -
                    1] >= 0.0) ? " + " : " - ");
        }

        sb.append(Math.abs(_c[0]));

        return sb.toString();
    }

    /**
     * String representation <I>P(x) = a_k x^k +...</I>
     *
     * @param unknown The name of the unkwown
     * @return String
     */
    public String toString(String unknown) {
        StringBuffer sb = new StringBuffer("P(" + unknown + ") = ");

        if (_c[degree()] < 0.0) {
            sb.append("-");
        } else {
            sb.append(" ");
        }

        for (int k = degree(); k > 0; k--) {
            sb.append(Math.abs(_c[k])).append(unknown).append("^")
                    .append(k).append((_c[k - 1] >= 0.0) ? " + " : " - ");
        }

        sb.append(Math.abs(_c[0]));

        return sb.toString();
    }

    /**
     * Perform the euclidian division of two polynomials.
     *
     * @param divisor denominator polynomial
     * @return an array of two elements containing the quotient and the
     *         remainder of the division
     */

    //I am not sure if this works for real coefficients and not only for rational ones
    //I don't have time to test, sorry
    public DoublePolynomial[] euclidianDivision(DoublePolynomial divisor) {
        DoublePolynomial quotient = DoublePolynomial.ZERO;
        DoublePolynomial remainder = new DoublePolynomial(this);

        int divisorDegree = divisor.degree();
        int remainderDegree = remainder.degree();

        while ((!remainder.equals(ZERO)) && (remainderDegree >= divisorDegree)) {
            double c = remainder.getCoefficientAsDouble(remainderDegree) / divisor.getCoefficientAsDouble(divisorDegree);
            DoublePolynomial monomial = new DoublePolynomial(c,
                    remainderDegree - divisorDegree);

            remainder = (DoublePolynomial) remainder.subtract((DoublePolynomial) monomial.multiply(
                    divisor));
            quotient = (DoublePolynomial) quotient.add(monomial);

            remainderDegree = remainder.degree();
        }

        return new DoublePolynomial[]{quotient, remainder};
    }

    public ComplexPolynomial toComplexPolynomial() {

        return new ComplexPolynomial(ArrayMathUtils.toComplex(getCoefficientsAsDoubles()));

    }

    public ExactRealPolynomial toExactRealPolynomial() {

        return new ExactRealPolynomial(ArrayMathUtils.toExactReal(getCoefficientsAsDoubles()));

    }

    public ExactComplexPolynomial toExactComplexPolynomial() {

        return new ExactComplexPolynomial(ArrayMathUtils.toExactComplex(getCoefficientsAsDoubles()));

    }

    //also clones components (deep copy)
    public Object clone() {
        return new DoublePolynomial(ArrayMathUtils.copy(getCoefficientsAsDoubles()));
    }

    // ROOTS OF POLYNOMIALS
    // For general details of root searching and a discussion of the rounding errors
    // see Numerical Recipes, The Art of Scientific Computing
    // by W H Press, S A Teukolsky, W T Vetterling & B P Flannery
    // Cambridge University Press,   http://www.nr.com/

    // Returns a DoublePolynomial given the polynomial's roots

    public static DoublePolynomial rootsToPolynomial(double[] roots) {
        int pdeg = roots.length;

        double[] rootCoeff = new double[2];
        rootCoeff[0] = -roots[0];
        rootCoeff[1] = 1;
        DoublePolynomial rPoly = new DoublePolynomial(rootCoeff);
        for (int i = 1; i < pdeg; i++) {
            rootCoeff[0] = -roots[i];
            DoublePolynomial cRoot = new DoublePolynomial(rootCoeff);
            rPoly = (DoublePolynomial) rPoly.multiply(cRoot);
        }
        return rPoly;
    }

    // Calculate the roots (real or complex) of a polynomial (real)
    // polish = true ([for deg>3 see laguerreAll(...)]
    // initial root estimates are all zero [for deg>3 see laguerreAll(...)]
    public Complex[] roots() {
        if (this.degree() == 0) {
            System.out.println("degree of the polynomial is zero in the method DoublePolynomial.roots");
            System.out.println("null returned");
            return null;
        }

        // check for zero roots
        boolean testzero = true;
        int ii = 0, nzeros = 0;
        while (testzero) {
            if (_c[ii] == 0) {
                nzeros++;
                ii++;
            } else {
                testzero = false;
            }
        }
        int degwz;
        Complex[] coeffwz;

        degwz = degree() - nzeros;
        coeffwz = new Complex[degwz + 1];
        for (int i = 0; i <= degwz; i++) coeffwz[i] = new Complex(_c[i + nzeros]);

        // calculate non-zero roots
        Complex[] roots = new Complex[degree()];
        Complex[] root = new Complex[degwz];

        switch (degwz) {
            case 1:
                root[0] = (Complex) coeffwz[0].divide(coeffwz[1]).negate();
                break;
            case 2:
                root = ComplexPolynomial.quadratic(coeffwz[0], coeffwz[1], coeffwz[2]);
                break;
            case 3:
                root = ComplexPolynomial.cubic(coeffwz[0], coeffwz[1], coeffwz[2], coeffwz[3]);
                break;
            default:
                root = laguerreAll(degwz, coeffwz);
        }

        for (int i = 0; i < degwz; i++) {
            roots[i] = root[i];
        }
        if (nzeros > 0) {
            for (int i = degwz; i < degree(); i++) {
                roots[i] = Complex.ZERO;
            }
        }

        return roots;
    }

    // ROOTS OF A QUADRATIC EQUATION
    // ax^2 + bx + c = 0
    // roots returned in root[]
    // 4ac << b*b accomodated by these methods
    public static Complex[] quadratic(double c, double b, double a) {
        Complex aa = new Complex(a, 0.0);
        Complex bb = new Complex(b, 0.0);
        Complex cc = new Complex(c, 0.0);

        double qsign = 1.0;
        Complex qsqrt = Complex.ZERO;
        Complex qtest = Complex.ZERO;
        Complex bconj = Complex.ZERO;
        Complex[] root = new Complex[2];

        bconj = bb.conjugate();
        qsqrt = bb.sqr().subtract(aa.multiply(cc).multiply(4)).sqrt();

        qtest = bconj.multiply(qsqrt);

        if (qtest.real() < 0.0) qsign = -1.0;

        qsqrt = ((qsqrt.divide(qsign)).add(bb)).divide(-2.0);
        root[0] = qsqrt.divide(aa);
        root[1] = cc.divide(qsqrt);

        return root;
    }

    // ROOTS OF A CUBIC EQUATION
    // ax^3 + bx^2 + cx + d = 0
    // roots returned in root[]
    public static Complex[] cubic(double d, double c, double b, double a) {

        double qsign = 1.0;
        Complex[] root = new Complex[3];
        Complex qsqrt = Complex.ZERO;
        Complex qtest = Complex.ZERO;
        Complex rconj = Complex.ZERO;
        Complex astore = Complex.ZERO;
        Complex first = Complex.ZERO;
        Complex aplusb = Complex.ZERO;
        Complex aminusb = Complex.ZERO;
        Complex aover3 = Complex.ZERO;
        Complex biga = Complex.ZERO;
        Complex bigb = Complex.ZERO;
        Complex theta = Complex.ZERO;
        Complex q = Complex.ZERO;
        Complex r = Complex.ZERO;

        astore = new Complex(a, 0.0);
        Complex aa = new Complex(b, 0.0).divide(astore);
        Complex bb = new Complex(c, 0.0).divide(astore);
        Complex cc = new Complex(d, 0.0).divide(astore);

        q = aa.sqr().subtract(new Complex(3.0).multiply(bb)).divide(9.0);
        r = aa.pow(3).multiply(2.0).subtract(aa.multiply(bb).multiply(9.0)).add(new Complex(27.0).multiply(cc)).divide(54.0);

        if (q.isReal() && r.isReal() && (r.pow(2).abs() < q.pow(3).abs())) {
            theta = Complex.acos(r.divide(q.pow(3).sqrt()));
            qsqrt = q.sqrt();
            aover3 = aa.divide(3.0);
            root[0] = (Complex) new Complex(2.0).multiply(qsqrt).multiply(Complex.cos(theta.divide(3.0))).add(aover3).negate();
            root[1] = (Complex) new Complex(2.0).multiply(qsqrt).multiply(Complex.cos(theta.add(new Complex(2.0).multiply(Complex.PI)).divide(3.0))).add(aover3).negate();
            root[2] = (Complex) new Complex(2.0).multiply(qsqrt).multiply(Complex.cos(theta.subtract(new Complex(2.0).multiply(Complex.PI)).divide(3.0))).add(aover3).negate();
        } else {
            rconj = r.conjugate();
            qsqrt = r.sqr().subtract(q.pow(3)).sqrt();
            qtest = rconj.multiply(qsqrt);
            if (qtest.real() < 0.0) qsign = -1.0;
            biga = (Complex) r.add(new Complex(qsign).multiply(qsqrt)).pow(1.0 / 3.0).negate();

            if (biga.abs() == 0.0) {
                bigb = Complex.ZERO;
            } else {
                bigb = q.divide(biga);
            }
            aplusb = biga.add(bigb);
            aminusb = biga.subtract(bigb);
            first = (Complex) aplusb.divide(2.0).add(aover3).negate();
            root[0] = aplusb.subtract(aover3);
            root[1] = first.add(Complex.I.multiply(Math.sqrt(0.75)).multiply(aminusb));
            root[2] = first.add(Complex.MINUS_I.multiply(Math.sqrt(0.75)).multiply(aminusb));
        }
        return root;
    }

    // LAGUERRE'S METHOD FOR COMPLEX ROOTS OF A COMPLEX POLYNOMIAL

    // Laguerre method for one of the roots
    // Following the procedure in Numerical Recipes for C [Reference above]
    // estx     estimate of the root
    // coeff[]  coefficients of the polynomial
    // m        degree of the polynomial

    private static Complex laguerre(Complex estx, Complex[] pcoeff, int m) {
        double eps = 1e-7;     // estimated fractional round-off error
        int mr = 8;         // number of fractional values in Adam's method of breaking a limit cycle
        int mt = 1000;      // number of steps in breaking a limit cycle
        int maxit = mr * mt;  // maximum number of iterations allowed
        int niter = 0;      // number of iterations taken

        // fractions used to break a limit cycle
        double frac[] = {0.5, 0.25, 0.75, 0.13, 0.38, 0.62, 0.88, 1.0};

        Complex root = Complex.ZERO;    // root
        Complex b = Complex.ZERO;
        Complex d = Complex.ZERO;
        Complex f = Complex.ZERO;
        Complex g = Complex.ZERO;
        Complex g2 = Complex.ZERO;
        Complex h = Complex.ZERO;
        Complex sq = Complex.ZERO;
        Complex gp = Complex.ZERO;
        Complex gm = Complex.ZERO;
        Complex dx = Complex.ZERO;
        Complex x1 = Complex.ZERO;
        Complex temp1 = Complex.ZERO;
        Complex temp2 = Complex.ZERO;

        double abp = 0.0D, abm = 0.0D;
        double err = 0.0D, abx = 0.0D;

        for (int i = 1; i <= maxit; i++) {
            niter = i;
            b = new Complex(pcoeff[m]);
            err = b.abs();
            d = f = Complex.ZERO;
            abx = estx.abs();
            for (int j = m - 1; j >= 0; j--) {
                // Efficient computation of the polynomial and its first two derivatives
                f = estx.multiply(f).add(d);
                d = estx.multiply(d).add(b);
                b = estx.multiply(b).add(pcoeff[j]);
                err = b.abs() + abx * err;
            }
            err *= eps;

            // Estimate of round-off error in evaluating polynomial
            if (b.abs() <= err) {
                root = new Complex(estx);
                niter = i;
                return root;
            }
            // Laguerre formula
            g = d.divide(b);
            g2 = g.sqr();
            h = g2.subtract(new Complex(2.0).multiply(f.divide(b)));
            sq = new Complex((double) (m - 1)).multiply(new Complex((double) m).multiply(h).multiply(g2)).sqrt();
            gp = g.add(sq);
            gm = g.subtract(sq);
            abp = gp.abs();
            abm = gm.abs();
            if (abp < abm) gp = gm;
            temp1 = new Complex((double) m, temp1.imag());
            temp2 = new Complex(Math.cos((double) i), Math.sin((double) i));
            dx = ((Math.max(abp, abm) > 0.0 ? temp1.divide(gp) : new Complex(Math.exp(1.0 + abx)).multiply(temp2)));
            x1 = estx.subtract(dx);
            if (estx.equals(x1)) {
                root = new Complex(estx);
                niter = i;
                return root;     // converged
            }
            if ((i % mt) == 0) {
                estx = new Complex(x1);
            } else {
                // Every so often we take a fractional step to break any limit cycle
                // (rare occurence)
                estx = estx.subtract(new Complex(frac[i / mt]).multiply(dx));
            }
            niter = i;
        }
        // exceeded maximum allowed iterations
        root = new Complex(estx);
        System.out.println("Maximum number of iterations exceeded in laguerre");
        System.out.println("root returned at this point");
        return root;
    }

    // Finds all roots of a complex polynomial by successive calls to laguerre
    // Following the procedure in Numerical Recipes for C [Reference above]
    // Initial estimates are all zero, polish=true
    private Complex[] laguerreAll(int degwz, Complex[] coeffwz) {
        // polish boolean variable
        // if true roots polished also by Laguerre
        // if false roots returned to be polished by another method elsewhere.
        // estx estimate of root - Preferred default value is zero to favour convergence
        //   to smallest remaining root

        int m = degwz;
        double eps = 2.0e-6;  // tolerance in determining round off in imaginary part

        Complex x = Complex.ZERO;
        Complex b = Complex.ZERO;
        Complex c = Complex.ZERO;
        Complex[] ad = new Complex[m + 1];
        Complex[] roots = new Complex[m + 1];

        // Copy polynomial for successive deflation
        for (int j = 0; j <= m; j++) ad[j] = new Complex(coeffwz[j]);

        // Loop over each root found
        for (int j = m; j >= 1; j--) {
            x = Complex.ZERO;   // Preferred default value is zero to favour convergence to smallest remaining root
            // and find the root
            x = laguerre(x, ad, j);
            if (Math.abs(x.imag()) <= 2.0 * eps * Math.abs(x.real())) x = new Complex(x.real(), 0.0);
            roots[j] = new Complex(x);
            b = new Complex(ad[j]);
            for (int jj = j - 1; jj >= 0; jj--) {
                c = new Complex(ad[jj]);
                ad[jj] = new Complex(b);
                b = (x.multiply(b)).add(c);
            }
        }

        if (true) {//polish set to true
            // polish roots using the undeflated coefficients
            for (int j = 1; j <= m; j++) {
                roots[j] = laguerre(roots[j], coeffwz, m);
            }
        }

        // Sort roots by their real parts by straight insertion
        for (int j = 2; j <= m; j++) {
            x = new Complex(roots[j]);
            int i = 0;
            for (i = j - 1; i >= 1; i--) {
                if (roots[i].real() <= x.real()) break;
                roots[i + 1] = new Complex(roots[i]);
            }
            roots[i + 1] = new Complex(x);
        }
        // shift roots to zero initial index
        for (int i = 0; i < m; i++) roots[i] = new Complex(roots[i + 1]);
        return roots;
    }

}
