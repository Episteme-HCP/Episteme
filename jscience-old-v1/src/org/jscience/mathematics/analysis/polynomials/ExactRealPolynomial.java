/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.analysis.polynomials;

import org.jscience.JScience;
import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.ExactReal;
import org.jscience.mathematics.analysis.ExactRealFunction;

import java.io.Serializable;


/**
 * A Polynomial as a <code>Ring.Member</code> over a
 * <i>real</i><code>Field</code>
 *
 * @author b.dietrich, Silvere Martin-Michiellot
 */

//we could provide rootsToPolynomial, roots, quadratic, cubic methods from ComplexPolynomial
public class ExactRealPolynomial extends ExactRealFunction implements Polynomial, Serializable, Cloneable {
    /**
     * The real polynomial representing the additive identity.
     */
    public static final ExactRealPolynomial ZERO = ExactRealPolynomialRing.ZERO;

    /**
     * The real polynomial representing the multiplicative identity.
     */
    public static final ExactRealPolynomial ONE = ExactRealPolynomialRing.ONE;

    /**
     * Coefficients in ascending degree order
     */
    private ExactReal[] _c;

    /**
     * Creates a new instance of ExactRealPolynomial, polynom is always simplified
     * discarding every trailing zeros and array copied (but not contents of elements).
     *
     * @param coeff DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public ExactRealPolynomial(ExactReal[] coeff) {
        if (coeff == null) {
            throw new NullPointerException();
        }
        _c = normalize(coeff);
    }

    /**
     * Normalises the coefficient array.
     * Trims off any leading (high degree) zero terms.
     */
    private static ExactReal[] normalize(ExactReal[] c) {
        int i = c.length - 1;
        while (i >= 0 && c[i].abs().compareTo(new ExactReal(java.lang.Double.valueOf(JScience.getProperty(
                "tolerance")))) <= 0)
            i--;
        if (i < 0) {
            return new ExactReal[]{ExactReal.ZERO};
        } else if (i < c.length - 1) {
            ExactReal[] arr = new ExactReal[i + 1];
            System.arraycopy(c, 0, arr, 0, arr.length);
            return arr;
        } else {
            return c;
        }
    }

    /**
     * Creates a new instance of ExactRealPolynomial, polynom is always
     * simplified discarding every trailing zeros.
     *
     * @param polynomial DOCUMENT ME!
     */
    public ExactRealPolynomial(ExactRealPolynomial polynomial) {
        this(polynomial.getCoefficientsAsExactReals());
    }

    /**
     * Creates a new ExactRealPolynomial object, polynom is always simplified
     * discarding every trailing zeros and array copied (but not contents of elements).
     *
     * @param f
     * @throws NullPointerException     DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ExactRealPolynomial(Field.Member[] f) {
        if (f == null) {
            throw new NullPointerException();
        }

        _c = normalize(ExactRealPolynomialRing.toExactReal(f));
    }

    /**
     * Simple constructor. Build a one term polynomial from one coefficient and
     * the corresponding degree.
     *
     * @param d      coefficient
     * @param degree degree associated with the coefficient
     */
    public ExactRealPolynomial(ExactReal d, int degree) {
        if (!(d.equals(ExactReal.ZERO))) {
            _c = new ExactReal[]{ExactReal.ZERO};
        } else {
            _c = new ExactReal[degree + 1];

            for (int i = 0; i < degree; ++i) {
                _c[i] = ExactReal.ZERO;
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
        return new ExactReal(getCoefficientAsExactReal(n));
    }

    /**
     * Get the coefficient of degree k, i.e. <I>a_k</I> if <I>P(x)</I> :=
     * sum_{k=0}^n <I>a_k x^k</I> as a real number
     *
     * @param n degree
     * @return coefficient as described above
     */
    public ExactReal getCoefficientAsExactReal(int n) {
        //we could test negative values of n
        if (n >= _c.length) {
            return ExactReal.ZERO;
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
        return ExactRealPolynomialRing.toExactReal(getCoefficientsAsExactReals());
    }

    /**
     * Get the coefficients as an array of ExactReals
     *
     * @return the coefficients as an array
     */
    public ExactReal[] getCoefficientsAsExactReals() {
        return _c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     */
    public void setCoefficient(int n, Field.Member c) {
        if (c instanceof ExactReal) {
            setCoefficientAsExactReal(n, (ExactReal) c);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     */
    public void setCoefficientAsExactReal(int n, ExactReal c) {
        //we could test negative values of n
        if (n >= _c.length) {
            if (c.abs().compareTo(new ExactReal(java.lang.Double.valueOf(JScience.getProperty(
                    "tolerance")))) == 1) {
                ExactReal[] d = new ExactReal[n + 1];
                System.arraycopy(_c, 0, d, 0, _c.length);
                _c = d;
                _c[n] = c;
            }
        } else {
            if (n == (_c.length - 1) && (c.abs().compareTo(new ExactReal(java.lang.Double.valueOf(JScience.getProperty(
                    "tolerance")))) <= 0)) {
                _c[n] = ExactReal.ZERO;
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
    public ExactReal map(ExactReal x) {
        return PolynomialMathUtils.evalPolynomial(this, x);
    }

    /**
     * The degree understood as the number of coeffiecients, and therefore the
     * highest degree is degree()-1
     *
     * @return the degree
     */
    public int degree() {
        return _c.length - 1;
    }

    public Object getSet() {
        return ExactRealPolynomialRing.getInstance();
    }

    /**
     * Returns true if this polynomial is equal to zero.
     * All coefficients are tested for |a_k| < GlobalSettings.ZERO_TOL.
     *
     * @return true if all coefficients <  GlobalSettings.ZERO_TOL
     */
    public boolean isZero() {
        for (int k = 0; k < _c.length; k++) {
            if (_c[k].abs().compareTo(new ExactReal(java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")))) == 1) {
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
        if (_c[0].subtract(ExactReal.ONE).abs().compareTo(new ExactReal(java.lang.Double.valueOf(
                JScience.getProperty("tolerance")))) == 1)
            return false;

        for (int k = 1; k < _c.length; k++) {
            if (_c[k].abs().compareTo(new ExactReal(java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")))) == 1) {
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
    public ExactRealFunction add(ExactRealFunction g) {
        if (g instanceof ExactRealPolynomial) {
            ExactRealPolynomial p = (ExactRealPolynomial) g;
            int maxgrade = PolynomialMathUtils.maxDegree(this, p);
            ExactReal[] c = new ExactReal[maxgrade + 1];

            for (int k = 0; k < c.length; k++) {
                c[k] = getCoefficientAsExactReal(k).add(p.getCoefficientAsExactReal(
                        k));
            }

            return new ExactRealPolynomial(c);
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
    public ExactRealFunction differentiate() {
        if (degree() == 0) {
            return (ExactRealPolynomial) ExactRealPolynomialRing.getInstance()
                    .zero();
        } else {
            ExactReal[] dn = new ExactReal[_c.length - 1];

            for (int k = 0; k < _c.length; k++) {
                dn[k] = (ExactReal) getCoefficientAsExactReal(k + 1).multiply(new ExactReal(
                        k + 1));
            }

            return new ExactRealPolynomial(dn);
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
        if (f instanceof ExactReal) {
            ExactReal a = (ExactReal) f;

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
    public ExactRealPolynomial scalarDivide(ExactReal a) {
        ExactReal[] c = new ExactReal[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].divide(a);
        }

        return new ExactRealPolynomial(c);
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
        } else if (o instanceof ExactRealPolynomial) {
            ExactRealPolynomial p = (ExactRealPolynomial) o;

            return ((ExactRealPolynomial) this.subtract(p)).isZero();
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
            res += (int) (getCoefficientAsExactReal(k)
                    .multiply(new ExactReal(10.0))).intValue();
        }

        return res;
    }

    /**
     * "inverse" operation for differentiate
     *
     * @return the integrated polynomial
     */
    public ExactRealPolynomial integrate() {
        ExactReal[] dn = new ExactReal[_c.length + 1];

        for (int k = 0; k < dn.length; k++) {
            dn[k + 1] = getCoefficientAsExactReal(k).divide(new ExactReal(k +
                    1));
        }

        return new ExactRealPolynomial(dn);
    }

    /**
     * Returns the multiplication of this polynomial by a scalar
     *
     * @param f
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Polynomial scalarMultiply(Field.Member f) {
        if (f instanceof ExactReal) {
            ExactReal a = (ExactReal) f;

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
    public ExactRealPolynomial scalarMultiply(ExactReal a) {
        ExactReal[] c = new ExactReal[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = _c[k].multiply(a);
        }

        return new ExactRealPolynomial(c);
    }

    /**
     * The multiplication law. Multiplies this Polynomial with another
     *
     * @param r a RealFunction
     * @return a new Polynomial with grade = max( this.grade, r.grade) + min(
     *         this.grade, r.grade) -1
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ExactRealFunction multiply(ExactRealFunction r) {
        if (r instanceof ExactRealPolynomial) {
            ExactRealPolynomial p = (ExactRealPolynomial) r;
            ExactReal[] n = new ExactReal[_c.length + p._c.length - 1];

            for (int k = 0; k < _c.length; k++) {
                ExactReal tis = getCoefficientAsExactReal(k);

                for (int j = 0; j < p._c.length; j++) {
                    ExactReal tat = p.getCoefficientAsExactReal(j);
                    n[k + j] = n[k + j].add((tis.multiply(tat)));
                }
            }

            return new ExactRealPolynomial(n);
        } else {
            throw new IllegalArgumentException(
                    "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the inverse member. (That is mult(-1))
     *
     * @return inverse
     */
    public AbelianGroup.Member negate() {
        ExactReal[] c = new ExactReal[_c.length];

        for (int k = 0; k < c.length; k++) {
            c[k] = (ExactReal) _c[k].negate();
        }

        return new ExactRealPolynomial(c);
    }

    /**
     * The group composition law with inverse.
     *
     * @param g a group member
     * @return DOCUMENT ME!
     */
    public ExactRealFunction subtract(ExactRealFunction g) {
        if (g instanceof ExactRealPolynomial) {
            ExactRealPolynomial p = (ExactRealPolynomial) g;
            int maxgrade = PolynomialMathUtils.maxDegree(this, p);
            ExactReal[] c = new ExactReal[maxgrade + 1];

            for (int k = 0; k < c.length; k++) {
                c[k] = getCoefficientAsExactReal(k).subtract(p.getCoefficientAsExactReal(
                        k));
            }

            return new ExactRealPolynomial(c);
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

        if (_c[degree()].compareTo(ExactReal.ZERO) == -1) {
            sb.append("-");
        } else {
            sb.append(" ");
        }

        for (int k = degree(); k > 0; k--) {
            sb.append(_c[k].abs().toString()).append("x^").append(k).append((_c[k -
                    1].compareTo(ExactReal.ZERO) > -1) ? " + " : " - ");
        }

        sb.append(_c[0].abs().toString());

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

        if (_c[degree()].compareTo(ExactReal.ZERO) == -1) {
            sb.append("-");
        } else {
            sb.append(" ");
        }

        for (int k = degree(); k > 0; k--) {
            sb.append(_c[k].abs().toString()).append(unknown).append("^").append(k
            ).append((_c[k - 1].compareTo(ExactReal.ZERO) > -1) ? " + "
                    : " - ");
        }

        sb.append(_c[0].abs().toString());

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
    public ExactRealPolynomial[] euclidianDivision(ExactRealPolynomial divisor) {
        ExactRealPolynomial quotient = ExactRealPolynomial.ZERO;
        ExactRealPolynomial remainder = new ExactRealPolynomial(this);

        int divisorDegree = divisor.degree();
        int remainderDegree = remainder.degree();

        while ((!remainder.equals(ZERO)) && (remainderDegree >= divisorDegree)) {
            ExactReal c = remainder.getCoefficientAsExactReal(remainderDegree
            ).divide(divisor.getCoefficientAsExactReal(divisorDegree));
            ExactRealPolynomial monomial = new ExactRealPolynomial(c,
                    remainderDegree - divisorDegree);

            remainder = (ExactRealPolynomial) remainder.subtract((ExactRealPolynomial) monomial.multiply(
                    divisor));
            quotient = (ExactRealPolynomial) quotient.add(monomial);

            remainderDegree = remainder.degree();
        }

        return new ExactRealPolynomial[]{quotient, remainder};

    }

    public ExactComplexPolynomial toExactComplexPolynomial() {

        return new ExactComplexPolynomial(ArrayMathUtils.toExactComplex(getCoefficientsAsExactReals()));

    }

    //also clones components (deep copy)
    public Object clone() {
        return new ExactRealPolynomial(copy(getCoefficientsAsExactReals()));
    }

    /**
     * Returns a copy of the array.
     */
    //deep copy
    private static ExactReal[] copy(ExactReal[] array) {
        ExactReal[] result;
        result = new ExactReal[array.length];
        for (int i = 0; i < array.length; i++) {
//result[i] = (ExactReal)array[i].clone();
            result[i] = new ExactReal(array[i]);
        }
        return result;
    }

}
