package org.jscience.mathematics.analysis.polynomials;

import org.jscience.JScience;

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.analysis.ComplexFunction;

import java.io.Serializable;


/**
 * A Polynomial over the complex field. For a description of the methods
 *
 * @author b.dietrich
 *
 * @see org.jscience.mathematics.analysis.polynomials.DoublePolynomial
 */
public class ComplexPolynomial extends ComplexFunction implements Polynomial,
    Serializable, Cloneable {
    /**
     * DOCUMENT ME!
     */
    private Complex[] _c;

/**
     * Creates a new instance of ComplexPolynomial, polynom is always simplified
     * discarding every trailing zeros and array copied (but not contents of elements).
     */
    public ComplexPolynomial(Complex[] coeff) {
        if (coeff == null) {
            throw new NullPointerException("Coefficients cannot be null");
        }

        _c = normalize(coeff);
    }

/**
     * Creates a new instance of ComplexPolynomial, polynom is always
     * simplified discarding every trailing zeros.
     *
     * @param polynomial DOCUMENT ME!
     */
    public ComplexPolynomial(ComplexPolynomial polynomial) {
        this(polynomial.getCoefficientsAsComplexes());
    }

/**
     * Creates a new ComplexPolynomial object, polynom is always simplified
     * discarding every trailing zeros and array copied (but not contents of elements).
     *
     * @param f
     */
    public ComplexPolynomial(Field.Member[] f) {
        _c = normalize(ComplexPolynomialRing.toComplex(f));
    }

    /**
     * Normalises the coefficient array. Trims off any leading (high
     * degree) zero terms.
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Complex[] normalize(Complex[] c) {
        int i = c.length - 1;

        while ((i >= 0) &&
                (c[i].norm() <= (2.0 * java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")).doubleValue())))
            i--;

        if (i < 0) {
            return new Complex[] { Complex.ZERO };
        } else if (i < (c.length - 1)) {
            Complex[] arr = new Complex[i + 1];
            System.arraycopy(c, 0, arr, 0, arr.length);

            return arr;
        } else {
            return c;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     *
     * @return DOCUMENT ME!
     */
    public Field.Member getCoefficient(int n) {
        return getCoefficientAsComplex(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     *
     * @return DOCUMENT ME!
     */
    public Complex getCoefficientAsComplex(int n) {
        //we could test negative values of n
        if (n >= _c.length) {
            return Complex.ZERO;
        } else {
            return _c[n];
        }
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public Field.Member[] getCoefficients() {
        return getCoefficientsAsComplexes();
    }

    /**
     * Return the coefficients as an array of ExactComplex numbers.
     *
     * @return DOCUMENT ME!
     */
    public Complex[] getCoefficientsAsComplexes() {
        return _c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     * @param c DOCUMENT ME!
     */
    public void setCoefficient(int n, Field.Member c) {
        if (c instanceof Complex) {
            setCoefficientAsComplex(n, (Complex) c);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     * @param c DOCUMENT ME!
     */
    public void setCoefficientAsComplex(int n, Complex c) {
        //we could test negative values of n
        if (n >= _c.length) {
            if (c.norm() > (2.0 * java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue())) {
                Complex[] d = new Complex[n + 1];
                System.arraycopy(_c, 0, d, 0, _c.length);
                _c = d;
                _c[n] = c;
            }
        } else {
            if ((n == (_c.length - 1)) &&
                    (c.norm() <= (2.0 * java.lang.Double.valueOf(
                        JScience.getProperty("tolerance")).doubleValue()))) {
                _c[n] = Complex.ZERO;
                _c = normalize(_c);
            } else {
                _c[n] = c;
            }
        }
    }

    /**
     * Evaluates this polynomial.
     *
     * @param z DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex map(Complex z) {
        return PolynomialMathUtils.evalPolynomial(this, z);
    }

    /**
     * Evaluates this polynomial.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex map(double x, double y) {
        return map(new Complex(x, y));
    }

    /**
     * DOCUMENT ME!
     *
     * @return the degree
     */
    public int degree() {
        return _c.length - 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSet() {
        return ComplexPolynomialRing.getInstance();
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return true if this is equal to zero.
     */
    public boolean isZero() {
        for (int k = 0; k < _c.length; k++) {
            if (_c[k].norm() > (java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue() * 2.0)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return true if this is equal to one.
     */
    public boolean isOne() {
        if (_c[0].subtract(Complex.ONE).norm() > (java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")).doubleValue() * 2.0)) {
            return false;
        }

        for (int k = 1; k < _c.length; k++) {
            if (_c[k].norm() > (2.0 * java.lang.Double.valueOf(
                        JScience.getProperty("tolerance")).doubleValue())) {
                return false;
            }
        }

        return true;
    }

    /**
     * The group composition law.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member add(AbelianGroup.Member g) {
        if (g instanceof ComplexPolynomial) {
            ComplexPolynomial p = (ComplexPolynomial) g;
            int maxgrade = PolynomialMathUtils.maxDegree(this, p);
            Complex[] c = new Complex[maxgrade + 1];

            for (int k = 0; k < c.length; k++) {
                c[k] = getCoefficientAsComplex(k)
                           .add(p.getCoefficientAsComplex(k));
            }

            return new ComplexPolynomial(c);
        } else {
            return super.add(g);
        }
    }

    /**
     * Differentiate the complex polynomial. Only useful iff the
     * polynomial is built over a banach space and an appropriate
     * multiplication law is provided.
     *
     * @return a new polynomial with degree = max(this.degree-1 , 0)
     */
    public ComplexFunction differentiate() {
        if (degree() == 0) {
            return (ComplexPolynomial) ComplexPolynomialRing.getInstance().zero();
        } else {
            Complex[] dn = new Complex[_c.length - 1];

            for (int k = 0; k < _c.length; k++) {
                dn[k] = getCoefficientAsComplex(k + 1).multiply(k + 1);
            }

            return new ComplexPolynomial(dn);
        }
    }

    /**
     * Returns the division of this polynomial by a scalar.
     *
     * @param f
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Polynomial scalarDivide(Field.Member f) {
        if (f instanceof Complex) {
            return scalarDivide((Complex) f);
        } else if (f instanceof Double) {
            return scalarDivide(((Double) f).value());
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the division of this polynomial by a scalar.
     *
     * @param a
     *
     * @return DOCUMENT ME!
     */
    public ComplexPolynomial scalarDivide(Complex a) {
        Complex[] c = new Complex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].divide(a);
        }

        return new ComplexPolynomial(c);
    }

    /**
     * Returns the division of this polynomial by a scalar.
     *
     * @param a
     *
     * @return DOCUMENT ME!
     */
    public ComplexPolynomial scalarDivide(double a) {
        Complex[] c = new Complex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].divide(a);
        }

        return new ComplexPolynomial(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param o
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        boolean result = false;

        if (o == this) {
            result = true;
        } else if (o instanceof ComplexPolynomial) {
            ComplexPolynomial p = (ComplexPolynomial) o;
            result = true;

            for (int k = 0; result && (k < degree()); k++) {
                if (p.getCoefficientAsComplex(k)
                         .subtract(getCoefficientAsComplex(k)).norm() > (2 * java.lang.Double.valueOf(
                            JScience.getProperty("tolerance")).doubleValue())) {
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int res = 0;

        for (int k = 0; k < degree(); k++) {
            res += (int) (getCoefficientAsComplex(k).norm() * 10.0);
        }

        return res;
    }

    /**
     * "inverse" operation for differentiate, zero degree constant set
     * to zero
     *
     * @return the integrated polynomial
     */
    public ComplexPolynomial integrate() {
        Complex[] dn = new Complex[_c.length + 1];

        for (int k = 0; k < dn.length; k++) {
            dn[k + 1] = getCoefficientAsComplex(k).divide(k + 1);
        }

        return new ComplexPolynomial(dn);
    }

    /**
     * Returns the multiplication of this polynomial by a scalar.
     *
     * @param f
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Polynomial scalarMultiply(Field.Member f) {
        if (f instanceof Double) {
            double a = ((Double) f).value();

            return scalarMultiply(a);
        } else if (f instanceof Complex) {
            return scalarMultiply((Complex) f);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this polynomial by a scalar.
     *
     * @param a
     *
     * @return DOCUMENT ME!
     */
    public ComplexPolynomial scalarMultiply(double a) {
        Complex[] c = new Complex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].multiply(a);
        }

        return new ComplexPolynomial(c);
    }

    /**
     * Returns the multiplication of this polynomial by a scalar.
     *
     * @param a
     *
     * @return DOCUMENT ME!
     */
    public ComplexPolynomial scalarMultiply(Complex a) {
        Complex[] c = new Complex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].multiply(a);
        }

        return new ComplexPolynomial(c);
    }

    /**
     * The multiplication law.
     *
     * @param r a ring member
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member multiply(Ring.Member r) {
        if (r instanceof ComplexPolynomial) {
            ComplexPolynomial p = (ComplexPolynomial) r;
            Complex[] n = new Complex[(_c.length + p._c.length) - 1];

            for (int k = 0; k < n.length; k++) {
                n[k] = Complex.ZERO;
            }

            for (int k = 0; k < _c.length; k++) {
                Complex tis = getCoefficientAsComplex(k);

                for (int j = 0; j < p._c.length; j++) {
                    Complex tat = p.getCoefficientAsComplex(j);
                    n[k + j] = n[k + j].add(tis.multiply(tat));
                }
            }

            return new ComplexPolynomial(n);
        } else {
            return super.multiply(r);
        }
    }

    /**
     * Returns the inverse member.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member negate() {
        Complex[] c = new Complex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = (Complex) _c[k].negate();
        }

        return new ComplexPolynomial(c);
    }

    /**
     * The group composition law with inverse.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(AbelianGroup.Member g) {
        if (g instanceof ComplexPolynomial) {
            ComplexPolynomial p = (ComplexPolynomial) g;
            Complex[] c = new Complex[PolynomialMathUtils.maxDegree(this, p) +
                1];

            for (int k = 0; k < c.length; k++) {
                c[k] = getCoefficientAsComplex(k)
                           .subtract(p.getCoefficientAsComplex(k));
            }

            return new ComplexPolynomial(c);
        } else {
            return super.subtract(g);
        }
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("P(z) = ");

        for (int k = degree(); k > 0; k--) {
            sb.append(_c[k].toString()).append("z^").append(k).append(" + ");
        }

        sb.append(_c[0].toString());

        return sb.toString();
    }

    /**
     * String representation <I>P(x) = a_k x^k +...</I>
     *
     * @param unknown The name of the unkwown
     *
     * @return String
     */
    public String toString(String unknown) {
        StringBuffer sb = new StringBuffer("P(" + unknown + ") = ");

        for (int k = degree(); k > 0; k--) {
            sb.append(_c[k].toString()).append(unknown).append("^").append(k)
              .append(" + ");
        }

        sb.append(_c[0].toString());

        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ExactComplexPolynomial toExactComplexPolynomial() {
        return new ExactComplexPolynomial(ArrayMathUtils.toExactComplex(
                getCoefficientsAsComplexes()));
    }

    //also clones components (deep copy)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new ComplexPolynomial(ArrayMathUtils.copy(
                getCoefficientsAsComplexes()));
    }

    // ROOTS OF POLYNOMIALS
    /**
     * DOCUMENT ME!
     *
     * @param roots DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComplexPolynomial rootsToPolynomial(Complex[] roots) {
        int pdeg = roots.length;

        Complex[] rootCoeff = new Complex[2];
        rootCoeff[0] = roots[0].multiply(Complex.MINUS_ONE);
        rootCoeff[1] = Complex.ONE;

        ComplexPolynomial rPoly = new ComplexPolynomial(rootCoeff);

        for (int i = 1; i < pdeg; i++) {
            rootCoeff[0] = roots[i].multiply(Complex.MINUS_ONE);

            ComplexPolynomial cRoot = new ComplexPolynomial(rootCoeff);
            rPoly = (ComplexPolynomial) rPoly.multiply(cRoot);
        }

        return rPoly;
    }

    // Calculate the roots (real or complex) of a polynomial (real or complex)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex[] roots() {
        if (this.degree() == 0) {
            System.out.println(
                "degree of the polynomial is zero in the method ComplexPolynomial.roots");
            System.out.println("null returned");

            return null;
        }

        // check for zero roots
        boolean testzero = true;
        int ii = 0;
        int nzeros = 0;

        while (testzero) {
            if (_c[ii].equals(Complex.ZERO)) {
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

        for (int i = 0; i <= degwz; i++)
            coeffwz[i] = new Complex(_c[i + nzeros]);

        // calculate non-zero roots
        Complex[] roots = new Complex[degree()];
        Complex[] root = new Complex[degwz];

        switch (degwz) {
        case 1:
            root[0] = (Complex) coeffwz[0].divide(coeffwz[1]).negate();

            break;

        case 2:
            root = quadratic(coeffwz[0], coeffwz[1], coeffwz[2]);

            break;

        case 3:
            root = cubic(coeffwz[0], coeffwz[1], coeffwz[2], coeffwz[3]);

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
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Complex[] quadratic(Complex c, Complex b, Complex a) {
        double qsign = 1.0;
        Complex qsqrt = Complex.ZERO;
        Complex qtest = Complex.ZERO;
        Complex bconj = Complex.ZERO;
        Complex[] root = new Complex[2];

        bconj = b.conjugate();
        qsqrt = b.sqr().subtract(a.multiply(c).multiply(4)).sqrt();

        qtest = bconj.multiply(qsqrt);

        if (qtest.real() < 0.0) {
            qsign = -1.0;
        }

        qsqrt = ((qsqrt.divide(qsign)).add(b)).divide(-2.0);
        root[0] = qsqrt.divide(a);
        root[1] = c.divide(qsqrt);

        return root;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Complex[] quadratic(double c, double b, double a) {
        Complex aa = new Complex(a, 0.0);
        Complex bb = new Complex(b, 0.0);
        Complex cc = new Complex(c, 0.0);

        return quadratic(cc, bb, aa);
    }

    // ROOTS OF A CUBIC EQUATION
    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Complex[] cubic(Complex d, Complex c, Complex b, Complex a) {
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

        astore = a;

        Complex aa = b.divide(astore);
        Complex bb = c.divide(astore);
        Complex cc = d.divide(astore);

        q = aa.sqr().subtract(new Complex(3.0).multiply(bb)).divide(9.0);
        r = aa.pow(3).multiply(2.0).subtract(aa.multiply(bb).multiply(9.0))
              .add(new Complex(27.0).multiply(cc)).divide(54.0);

        if (q.isReal() && r.isReal() && (r.pow(2).abs() < q.pow(3).abs())) {
            theta = Complex.acos(r.divide(q.pow(3).sqrt()));
            qsqrt = q.sqrt();
            aover3 = aa.divide(3.0);
            root[0] = (Complex) new Complex(2.0).multiply(qsqrt)
                                                .multiply(Complex.cos(
                        theta.divide(3.0))).add(aover3).negate();
            root[1] = (Complex) new Complex(2.0).multiply(qsqrt)
                                                .multiply(Complex.cos(
                        theta.add(new Complex(2.0).multiply(Complex.PI))
                             .divide(3.0))).add(aover3).negate();
            root[2] = (Complex) new Complex(2.0).multiply(qsqrt)
                                                .multiply(Complex.cos(
                        theta.subtract(new Complex(2.0).multiply(Complex.PI))
                             .divide(3.0))).add(aover3).negate();
        } else {
            rconj = r.conjugate();
            qsqrt = r.sqr().subtract(q.pow(3)).sqrt();
            qtest = rconj.multiply(qsqrt);

            if (qtest.real() < 0.0) {
                qsign = -1.0;
            }

            biga = (Complex) r.add(new Complex(qsign).multiply(qsqrt))
                              .pow(1.0 / 3.0).negate();

            if (biga.abs() == 0.0) {
                bigb = Complex.ZERO;
            } else {
                bigb = q.divide(biga);
            }

            aplusb = biga.add(bigb);
            aminusb = biga.subtract(bigb);
            first = (Complex) aplusb.divide(2.0).add(aover3).negate();
            root[0] = aplusb.subtract(aover3);
            root[1] = first.add(Complex.I.multiply(Math.sqrt(0.75))
                                         .multiply(aminusb));
            root[2] = first.add(Complex.MINUS_I.multiply(Math.sqrt(0.75))
                                               .multiply(aminusb));
        }

        return root;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Complex[] cubic(double d, double c, double b, double a) {
        Complex aa = new Complex(a, 0.0);
        Complex bb = new Complex(b, 0.0);
        Complex cc = new Complex(c, 0.0);
        Complex dd = new Complex(d, 0.0);

        return cubic(dd, cc, bb, aa);
    }

    // LAGUERRE'S METHOD FOR COMPLEX ROOTS OF A COMPLEX POLYNOMIAL
    /**
     * DOCUMENT ME!
     *
     * @param estx DOCUMENT ME!
     * @param pcoeff DOCUMENT ME!
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Complex laguerre(Complex estx, Complex[] pcoeff, int m) {
        double eps = 1e-7; // estimated fractional round-off error
        int mr = 8; // number of fractional values in Adam's method of breaking a limit cycle
        int mt = 1000; // number of steps in breaking a limit cycle
        int maxit = mr * mt; // maximum number of iterations allowed
        int niter = 0; // number of iterations taken

        // fractions used to break a limit cycle
        double[] frac = { 0.5, 0.25, 0.75, 0.13, 0.38, 0.62, 0.88, 1.0 };

        Complex root = Complex.ZERO; // root
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

        double abp = 0.0D;
        double abm = 0.0D;
        double err = 0.0D;
        double abx = 0.0D;

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
                err = b.abs() + (abx * err);
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
            sq = new Complex((double) (m - 1)).multiply(new Complex((double) m).multiply(
                        h).multiply(g2)).sqrt();
            gp = g.add(sq);
            gm = g.subtract(sq);
            abp = gp.abs();
            abm = gm.abs();

            if (abp < abm) {
                gp = gm;
            }

            temp1 = new Complex((double) m, temp1.imag());
            temp2 = new Complex(Math.cos((double) i), Math.sin((double) i));
            dx = (((Math.max(abp, abm) > 0.0) ? temp1.divide(gp)
                                              : new Complex(Math.exp(1.0 + abx)).multiply(temp2)));
            x1 = estx.subtract(dx);

            if (estx.equals(x1)) {
                root = new Complex(estx);
                niter = i;

                return root; // converged
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
    /**
     * DOCUMENT ME!
     *
     * @param degwz DOCUMENT ME!
     * @param coeffwz DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Complex[] laguerreAll(int degwz, Complex[] coeffwz) {
        // polish boolean variable
        // if true roots polished also by Laguerre
        // if false roots returned to be polished by another method elsewhere.
        // estx estimate of root - Preferred default value is zero to favour convergence
        //   to smallest remaining root
        int m = degwz;
        double eps = 2.0e-6; // tolerance in determining round off in imaginary part

        Complex x = Complex.ZERO;
        Complex b = Complex.ZERO;
        Complex c = Complex.ZERO;
        Complex[] ad = new Complex[m + 1];
        Complex[] roots = new Complex[m + 1];

        // Copy polynomial for successive deflation
        for (int j = 0; j <= m; j++)
            ad[j] = new Complex(coeffwz[j]);

        // Loop over each root found
        for (int j = m; j >= 1; j--) {
            x = Complex.ZERO; // Preferred default value is zero to favour convergence to smallest remaining root
                              // and find the root

            x = laguerre(x, ad, j);

            if (Math.abs(x.imag()) <= (2.0 * eps * Math.abs(x.real()))) {
                x = new Complex(x.real(), 0.0);
            }

            roots[j] = new Complex(x);
            b = new Complex(ad[j]);

            for (int jj = j - 1; jj >= 0; jj--) {
                c = new Complex(ad[jj]);
                ad[jj] = new Complex(b);
                b = (x.multiply(b)).add(c);
            }
        }

        if (true) { //polish set to true
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
                if (roots[i].real() <= x.real()) {
                    break;
                }

                roots[i + 1] = new Complex(roots[i]);
            }

            roots[i + 1] = new Complex(x);
        }

        // shift roots to zero initial index
        for (int i = 0; i < m; i++)
            roots[i] = new Complex(roots[i + 1]);

        return roots;
    }
}
