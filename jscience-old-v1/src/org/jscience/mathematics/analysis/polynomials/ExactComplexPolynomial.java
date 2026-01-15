package org.jscience.mathematics.analysis.polynomials;

import org.jscience.JScience;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.ExactComplex;
import org.jscience.mathematics.algebraic.numbers.ExactReal;
import org.jscience.mathematics.analysis.ExactComplexFunction;

import java.io.Serializable;


/**
 * A Polynomial over the ExactComplex field. For a description of the
 * methods
 *
 * @author b.dietrich
 *
 * @see org.jscience.mathematics.analysis.polynomials.ExactRealPolynomial
 */

//we could provide rootsToPolynomial, roots, quadratic, cubic methods from ComplexPolynomial
public class ExactComplexPolynomial extends ExactComplexFunction
    implements Polynomial, Serializable, Cloneable {
    /** DOCUMENT ME! */
    private ExactComplex[] _c;

/**
     * Creates a new instance of ExactComplexPolynomial, polynom is always simplified
     * discarding every trailing zeros and array copied (but not contents of elements).
     *
     * @param coeff DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public ExactComplexPolynomial(ExactComplex[] coeff) {
        if (coeff == null) {
            throw new NullPointerException();
        }

        if (coeff.length == 0) {
            _c = new ExactComplex[] { ExactComplex.ZERO };
        } else {
            _c = normalize(coeff);
        }
    }

/**
     * Creates a new instance of ExactComplexPolynomial, polynom is always
     * simplified discarding every trailing zeros.
     *
     * @param polynomial DOCUMENT ME!
     */
    public ExactComplexPolynomial(ExactComplexPolynomial polynomial) {
        this(polynomial.getCoefficientsAsExactComplexes());
    }

/**
     * Creates a new ExactComplexPolynomial object.
     *
     * @param f
     */
    public ExactComplexPolynomial(Field.Member[] f) {
        _c = ExactComplexPolynomialRing.toExactComplex(f);
    }

    /**
     * Normalises the coefficient array. Trims off any leading (high
     * degree) zero terms.
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static ExactComplex[] normalize(ExactComplex[] c) {
        int i = c.length - 1;

        while ((i >= 0) &&
                (c[i].norm() <= (2.0 * java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")).doubleValue())))
            i--;

        if (i < 0) {
            return new ExactComplex[] { ExactComplex.ZERO };
        } else if (i < (c.length - 1)) {
            ExactComplex[] arr = new ExactComplex[i + 1];
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
        return getCoefficientAsExactComplex(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     *
     * @return DOCUMENT ME!
     */
    public ExactComplex getCoefficientAsExactComplex(int n) {
        //we could test negative values of n
        if (n >= _c.length) {
            return ExactComplex.ZERO;
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
        return getCoefficientsAsExactComplexes();
    }

    /**
     * Return the coefficients as an array of ExactComplex numbers.
     *
     * @return DOCUMENT ME!
     */
    public ExactComplex[] getCoefficientsAsExactComplexes() {
        return _c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     * @param c DOCUMENT ME!
     */
    public void setCoefficient(int n, Field.Member c) {
        if (c instanceof ExactComplex) {
            setCoefficientAsExactComplex(n, (ExactComplex) c);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     * @param c DOCUMENT ME!
     */
    public void setCoefficientAsExactComplex(int n, ExactComplex c) {
        //we could test negative values of n
        if (n >= _c.length) {
            if (c.norm() > (2.0 * java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue())) {
                ExactComplex[] d = new ExactComplex[n + 1];
                System.arraycopy(_c, 0, d, 0, _c.length);
                _c = d;
                _c[n] = c;
            }
        } else {
            if ((n == (_c.length - 1)) &&
                    (c.norm() <= (2.0 * java.lang.Double.valueOf(
                        JScience.getProperty("tolerance")).doubleValue()))) {
                _c[n] = ExactComplex.ZERO;
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
    public ExactComplex map(ExactComplex z) {
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
    public ExactComplex map(double x, double y) {
        return map(new ExactComplex(x, y));
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
        return ExactComplexPolynomialRing.getInstance();
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
        if (_c[0].subtract(ExactComplex.ONE).norm() > (java.lang.Double.valueOf(
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
        if (g instanceof ExactComplexPolynomial) {
            ExactComplexPolynomial p = (ExactComplexPolynomial) g;
            int maxgrade = PolynomialMathUtils.maxDegree(this, p);
            ExactComplex[] c = new ExactComplex[maxgrade + 1];

            for (int k = 0; k < c.length; k++) {
                c[k] = getCoefficientAsExactComplex(k)
                           .add(p.getCoefficientAsExactComplex(k));
            }

            return new ExactComplexPolynomial(c);
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
    public ExactComplexFunction differentiate() {
        if (degree() == 0) {
            return (ExactComplexPolynomial) ExactComplexPolynomialRing.getInstance()
                                                                      .zero();
        } else {
            ExactComplex[] dn = new ExactComplex[_c.length - 1];

            for (int k = 0; k < _c.length; k++) {
                dn[k] = getCoefficientAsExactComplex(k + 1).multiply(k + 1);
            }

            return new ExactComplexPolynomial(dn);
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
        if (f instanceof ExactComplex) {
            return scalarDivide((ExactComplex) f);
        } else if (f instanceof ExactReal) {
            return scalarDivide(((ExactReal) f));
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
    public ExactComplexPolynomial scalarDivide(ExactComplex a) {
        ExactComplex[] c = new ExactComplex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].divide(a);
        }

        return new ExactComplexPolynomial(c);
    }

    /**
     * Returns the division of this polynomial by a scalar.
     *
     * @param a
     *
     * @return DOCUMENT ME!
     */
    public ExactComplexPolynomial scalarDivide(ExactReal a) {
        ExactComplex[] c = new ExactComplex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].divide(a);
        }

        return new ExactComplexPolynomial(c);
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
        } else if (o instanceof ExactComplexPolynomial) {
            ExactComplexPolynomial p = (ExactComplexPolynomial) o;
            result = true;

            for (int k = 0; result && (k < degree()); k++) {
                if (p.getCoefficientAsExactComplex(k)
                         .subtract(getCoefficientAsExactComplex(k)).norm() > (2 * java.lang.Double.valueOf(
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
            res += (int) (getCoefficientAsExactComplex(k).norm() * 10.0);
        }

        return res;
    }

    /**
     * "inverse" operation for differentiate, zero degree constant set
     * to zero
     *
     * @return the integrated polynomial
     */
    public ExactComplexPolynomial integrate() {
        ExactComplex[] dn = new ExactComplex[_c.length + 1];

        for (int k = 0; k < dn.length; k++) {
            dn[k + 1] = getCoefficientAsExactComplex(k).divide(k + 1);
        }

        return new ExactComplexPolynomial(dn);
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
        if (f instanceof ExactReal) {
            return scalarMultiply((ExactReal) f);
        } else if (f instanceof ExactComplex) {
            return scalarMultiply((ExactComplex) f);
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
    public ExactComplexPolynomial scalarMultiply(ExactReal a) {
        ExactComplex[] c = new ExactComplex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].multiply(a);
        }

        return new ExactComplexPolynomial(c);
    }

    /**
     * Returns the multiplication of this polynomial by a scalar.
     *
     * @param a
     *
     * @return DOCUMENT ME!
     */
    public ExactComplexPolynomial scalarMultiply(ExactComplex a) {
        ExactComplex[] c = new ExactComplex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].multiply(a);
        }

        return new ExactComplexPolynomial(c);
    }

    /**
     * The multiplication law.
     *
     * @param r a ring member
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member multiply(Ring.Member r) {
        if (r instanceof ExactComplexPolynomial) {
            ExactComplexPolynomial p = (ExactComplexPolynomial) r;
            ExactComplex[] n = new ExactComplex[(_c.length + p._c.length) - 1];

            for (int k = 0; k < n.length; k++) {
                n[k] = ExactComplex.ZERO;
            }

            for (int k = 0; k < _c.length; k++) {
                ExactComplex tis = getCoefficientAsExactComplex(k);

                for (int j = 0; j < p._c.length; j++) {
                    ExactComplex tat = p.getCoefficientAsExactComplex(j);
                    n[k + j] = n[k + j].add(tis.multiply(tat));
                }
            }

            return new ExactComplexPolynomial(n);
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
        ExactComplex[] c = new ExactComplex[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = (ExactComplex) _c[k].negate();
        }

        return new ExactComplexPolynomial(c);
    }

    /**
     * The group composition law with inverse.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(AbelianGroup.Member g) {
        if (g instanceof ExactComplexPolynomial) {
            ExactComplexPolynomial p = (ExactComplexPolynomial) g;
            ExactComplex[] c = new ExactComplex[PolynomialMathUtils.maxDegree(this,
                    p) + 1];

            for (int k = 0; k < c.length; k++) {
                c[k] = getCoefficientAsExactComplex(k)
                           .subtract(p.getCoefficientAsExactComplex(k));
            }

            return new ExactComplexPolynomial(c);
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

    //also clones components (deep copy)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new ExactComplexPolynomial(copy(getCoefficientsAsExactComplexes()));
    }

    /**
     * Returns a copy of the array.
     *
     * @param array DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //deep copy
    private static ExactComplex[] copy(ExactComplex[] array) {
        ExactComplex[] result;
        result = new ExactComplex[array.length];

        for (int i = 0; i < array.length; i++) {
            //result[i] = (ExactComplex)array[i].clone();
            result[i] = new ExactComplex(array[i]);
        }

        return result;
    }
}
