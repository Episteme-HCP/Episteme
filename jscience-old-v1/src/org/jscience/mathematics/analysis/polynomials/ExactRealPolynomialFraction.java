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

import org.jscience.mathematics.algebraic.numbers.ExactReal;
import org.jscience.mathematics.analysis.ExactRealFunction;


/**
 * This class implements fractions of polynomials with one unknown and real
 * coefficients.
 *
 * @author L. Maisonobe, Silvere Martin-Michiellot
 * @version $Id: ExactRealPolynomialFraction.java,v 1.3 2007-10-23 18:19:21 virtualcall Exp $
 */
public class ExactRealPolynomialFraction extends ExactRealFunction {
    /** Numerator. */
    private ExactRealPolynomial p;

    /** Denominator. */
    private ExactRealPolynomial q;

/**
     * Simple constructor. Build a fraction from a numerator and a denominator.
     *
     * @param numerator   numerator of the fraction
     * @param denominator denominator of the fraction
     * @throws ArithmeticException if the denominator is null
     */
    public ExactRealPolynomialFraction(ExactRealPolynomial numerator,
        ExactRealPolynomial denominator) {
        if (denominator.isZero()) {
            throw new ArithmeticException("null denominator");
        }

        p = new ExactRealPolynomial(numerator);
        q = new ExactRealPolynomial(denominator);

        ExactReal[] a = q.getCoefficientsAsExactReals();

        if (a[a.length - 1].compareTo(ExactReal.ZERO) == -1) {
            p.negate();
            q.negate();
        }

        simplify();
    }

/**
     * Simple constructor. Build a fraction from a single Polynom
     *
     * @param p value of the fraction
     */
    public ExactRealPolynomialFraction(ExactRealPolynomial p) {
        this(p, ExactRealPolynomial.ONE);
    }

/**
     * Copy-constructor.
     *
     * @param f fraction to copy
     */
    public ExactRealPolynomialFraction(ExactRealPolynomialFraction f) {
        p = new ExactRealPolynomial(f.p);
        q = new ExactRealPolynomial(f.q);
    }

    /**
     * Evaluates this polynomial fraction.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ExactReal map(ExactReal x) {
        return p.map(x).divide(q.map(x));
    }

    /**
     * Returns the differential of this function.
     *
     * @return DOCUMENT ME!
     */
    public ExactRealPolynomialFraction differentiate() {
        return new ExactRealPolynomialFraction((ExactRealPolynomial) p.differentiate()
                                                                      .multiply(q)
                                                                      .subtract(q.differentiate()
                                                                                 .multiply(p)),
            (ExactRealPolynomial) q.multiply(q));
    }

    /**
     * Negate a fraction.
     *
     * @return a new fraction which is the opposite of this
     */
    public ExactRealPolynomialFraction negate() {
        return new ExactRealPolynomialFraction((ExactRealPolynomial) p.negate(),
            q);
    }

    /**
     * Add a fraction to the instance.
     *
     * @param f fraction to add.
     *
     * @return DOCUMENT ME!
     */
    public ExactRealPolynomialFraction add(ExactRealPolynomialFraction f) {
        ExactRealPolynomial num = (ExactRealPolynomial) p.multiply(f.q)
                                                         .add(f.p.multiply(
                    this.q));
        ExactRealPolynomial den = (ExactRealPolynomial) q.multiply(f.q);

        return new ExactRealPolynomialFraction(num, den);
    }

    /**
     * Subtract a fraction to the instance.
     *
     * @param f fraction to subtract.
     *
     * @return DOCUMENT ME!
     */
    public ExactRealPolynomialFraction subtract(ExactRealPolynomialFraction f) {
        ExactRealPolynomial num = (ExactRealPolynomial) p.multiply(f.q)
                                                         .subtract(f.p.multiply(
                    this.q));
        ExactRealPolynomial den = (ExactRealPolynomial) q.multiply(f.q);

        return new ExactRealPolynomialFraction(num, den);
    }

    /**
     * Multiply the instance by a fraction.
     *
     * @param f fraction to multiply by
     *
     * @return DOCUMENT ME!
     */
    public ExactRealPolynomialFraction multiply(ExactRealPolynomialFraction f) {
        ExactRealPolynomial num = (ExactRealPolynomial) p.multiply(f.p);
        ExactRealPolynomial den = (ExactRealPolynomial) q.multiply(f.q);

        return new ExactRealPolynomialFraction(num, den);
    }

    /**
     * Divide the instance by a fraction.
     *
     * @param f fraction to divide by
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException if f is null
     */
    public ExactRealPolynomialFraction divide(ExactRealPolynomialFraction f) {
        if (f.p.isZero()) {
            throw new ArithmeticException("Divide by zero.");
        } else {
            ExactRealPolynomial num = (ExactRealPolynomial) p.multiply(f.q);
            ExactRealPolynomial den = (ExactRealPolynomial) q.multiply(f.p);

            return new ExactRealPolynomialFraction(num, den);
        }
    }

    /**
     * Invert the instance. Replace the instance by its inverse.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException if the instance is null
     */
    public ExactRealPolynomialFraction invert() {
        if (p.isZero()) {
            throw new ArithmeticException("Divide by zero.");
        } else {
            return new ExactRealPolynomialFraction(q, p);
        }
    }

    /**
     * Simplify a fraction. If the denominator polynom is a constant
     * polynom, then simplification involves merging this constant in the
     * rational coefficients of the numerator in order to replace the
     * denominator by the constant 1. If the degree of the denominator is non
     * null, then simplification involves both removing common polynomial
     * factors (by euclidian division) and replacing rational coefficients by
     * integer coefficients (multiplying both numerator and denominator by the
     * proper value). The signs of both the numerator and the denominator are
     * adjusted in order to have a positive leeding degree term in the
     * denominator.
     */
    private void simplify() {
        ExactRealPolynomial a = new ExactRealPolynomial(p);
        ExactRealPolynomial b = new ExactRealPolynomial(q);

        if (a.degree() < b.degree()) {
            ExactRealPolynomial tmp = a;
            a = b;
            b = tmp;
        }

        ExactRealPolynomial[] res = a.euclidianDivision(b);

        while (res[1].degree() != 0) {
            a = b;
            b = res[1];
            res = a.euclidianDivision(b);
        }

        if (res[1].isZero()) {
            // there is a common factor we can remove
            p = p.euclidianDivision(b)[0];
            q = q.euclidianDivision(b)[0];
        }

        if (q.degree() == 0) {
            if (!q.isOne()) {
                ExactReal f = q.getCoefficientsAsExactReals()[0];
                p.divide(f);
                q = ExactRealPolynomial.ONE;
            }
        }

        if (q.getCoefficientsAsExactReals()[q.degree()].compareTo(
                    ExactReal.ZERO) == -1) {
            p = (ExactRealPolynomial) p.negate();
            q = (ExactRealPolynomial) q.negate();
        }
    }

    /**
     * Get the numerator.
     *
     * @return the numerator
     */
    public ExactRealPolynomial getNumerator() {
        return p;
    }

    /**
     * Get the denominator.
     *
     * @return the denominator (leeding coefficient is always positive)
     */
    public ExactRealPolynomial getDenominator() {
        return q;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (p.isZero()) {
            return "0";
        } else if (q.isOne()) {
            return p.toString();
        } else {
            StringBuffer s = new StringBuffer();

            String pString = p.toString();

            if (pString.indexOf(' ') > 0) {
                s.append('(');
                s.append(pString);
                s.append(')');
            } else {
                s.append(pString);
            }

            s.append('/');

            String qString = q.toString();

            if (qString.indexOf(' ') > 0) {
                s.append('(');
                s.append(qString);
                s.append(')');
            } else {
                s.append(qString);
            }

            return s.toString();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param unknown DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString(String unknown) {
        if (p.isZero()) {
            return "0";
        } else if (q.isOne()) {
            return p.toString();
        } else {
            StringBuffer s = new StringBuffer();

            String pString = p.toString(unknown);

            if (pString.indexOf(' ') > 0) {
                s.append('(');
                s.append(pString);
                s.append(')');
            } else {
                s.append(pString);
            }

            s.append('/');

            String qString = q.toString(unknown);

            if (qString.indexOf(' ') > 0) {
                s.append('(');
                s.append(qString);
                s.append(')');
            } else {
                s.append(qString);
            }

            return s.toString();
        }
    }
}
