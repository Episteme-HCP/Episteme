package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.numbers.Double;

/**
 * This class implements Hermite polynomials.
 * <p/>
 * <p>Hermite polynomials can be defined by the following recurrence
 * relations:
 * <pre>
 *  H0(X)   = 1
 *  H1(X)   = 2X
 *  Hk+1(X) = 2X Hk(X) - 2k Hk-1(X)
 * </pre></p>
 *
 * @author L. Maisonobe
 * @version $Id: HermiteDoublePolynomialFactory.java,v 1.2 2007-10-21 17:45:48 virtualcall Exp $
 */

public final class HermiteDoublePolynomialFactory extends OrthogonalDoublePolynomialFactory {

    public HermiteDoublePolynomialFactory() {
    }

    /**
     * Simple constructor.
     * Build a degree d Chebyshev polynomial
     *
     * @param degree degree of the polynomial
     */
    public DoublePolynomial getOrthogonalPolynomial(int degree) {
        return new DoublePolynomial(getOrthogonalPolynomialCoefficients(degree));
    }

    /**
     * Initialize the recurrence coefficients for degree 0 and 1.
     *
     * @return an array which contains the coefficients for degree 0 and 1
     */
    public Polynomial[] getFirstTermsPolynomials() {

        Polynomial[] result;
        org.jscience.mathematics.algebraic.numbers.Double[] coefficients;

        result = new Polynomial[2];
        // H0(X)  = 1
        coefficients = new org.jscience.mathematics.algebraic.numbers.Double[1];
        coefficients[0] = new org.jscience.mathematics.algebraic.numbers.Double(1);
        result[0] = new DoublePolynomial(coefficients);

        // H1(X)  = 2X
        coefficients = new Double[2];
        coefficients[0] = new Double(0);
        coefficients[1] = new Double(2);
        result[1] = new DoublePolynomial(coefficients);

        return result;

    }

    /**
     * Initialize the recurrence coefficients.
     * The recurrence relation is
     * <pre>Hk+1(X) = 2X Hk(X) - 2k Hk-1(X)</pre>
     *
     * @param k index of the current step
     */
    public Field.Member[] getRecurrenceCoefficients(int k) {

        Field.Member[] result;

        result = new Double[3];
        result[0] = new Double(0);
        result[1] = new Double(2);
        result[2] = new Double(2 * k);

        return result;

    }

}
