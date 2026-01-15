package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.numbers.Double;

/**
 * This class implements Chebyshev polynomials.
 * <p/>
 * <p>Chebyshev polynomials can be defined by the following recurrence
 * relations:
 * <pre>
 *  T0(X)   = 1
 *  T1(X)   = X
 *  Tk+1(X) = 2X Tk(X) - Tk-1(X)
 * </pre></p>
 *
 * @author L. Maisonobe
 * @version $Id: ChebyshevDoublePolynomialFactory.java,v 1.2 2007-10-21 17:45:48 virtualcall Exp $
 */

//also see ChebychevBasis
public final class ChebyshevDoublePolynomialFactory extends OrthogonalDoublePolynomialFactory {

    public ChebyshevDoublePolynomialFactory() {
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
        Double[] coefficients;

        result = new Polynomial[2];
        // T0(X)  = 1
        coefficients = new Double[1];
        coefficients[0] = new Double(1);
        result[0] = new DoublePolynomial(coefficients);

        // T1(X)  = X
        coefficients = new Double[2];
        coefficients[0] = new Double(0);
        coefficients[1] = new Double(1);
        result[1] = new DoublePolynomial(coefficients);

        return result;

    }

    /**
     * Initialize the recurrence coefficients.
     * The recurrence relation is
     * <pre>Tk+1(X) = 2X Tk(X) - Tk-1(X)</pre>
     *
     * @param k index of the current step
     */
    public Field.Member[] getRecurrenceCoefficients(int k) {

        Field.Member[] result;

        result = new Double[3];
        result[0] = new Double(0);
        result[1] = new Double(2);
        result[2] = new Double(1);

        return result;

    }

}
