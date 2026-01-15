package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.numbers.Double;

/**
 * This class implements Legendre polynomials.
 * <p/>
 * <p>Legendre polynomials can be defined by the following recurrence
 * relations:
 * <pre>
 *        P0(X)   = 1
 *        P1(X)   = X
 *  (k+1) Pk+1(X) = (2k+1) X Pk(X) - k Pk-1(X)
 * </pre></p>
 *
 * @author L. Maisonobe
 * @version $Id: LegendreDoublePolynomialFactory.java,v 1.2 2007-10-21 17:45:48 virtualcall Exp $
 */

public final class LegendreDoublePolynomialFactory extends OrthogonalDoublePolynomialFactory {

    public LegendreDoublePolynomialFactory() {
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
        // P0(X)  = 1
        coefficients = new Double[1];
        coefficients[0] = new Double(1);
        result[0] = new DoublePolynomial(coefficients);

        // P1(X)  = X
        coefficients = new Double[2];
        coefficients[0] = new Double(0);
        coefficients[1] = new Double(1);
        result[1] = new DoublePolynomial(coefficients);

        return result;

    }

    /**
     * Initialize the recurrence coefficients.
     * The recurrence relation is
     * (k+1) Pk+1(X) = (2k+1) X Pk(X) - k Pk-1(X)
     *
     * @param k index of the current step
     */
    public Field.Member[] getRecurrenceCoefficients(int k) {

        Field.Member[] result;

        result = new Double[3];
        result[0] = new Double(0);
        result[1] = new Double((2 * k + 1) / (k + 1));
        result[2] = new Double(k / (k + 1));

        return result;

    }

}
