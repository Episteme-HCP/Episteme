package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.analysis.series.PochhammerSeries;

/**
 * This class implements Jacobi polynomials.
 * <p/>
 * <p>Jacobi polynomials can be defined by the following recurrence
 * relations:
 * <pre>
 *  T0(X)   = 1
 *  T1(X)   = [2(a+1)+(a+b+2)(x-1)]/2
 *  2(n+1)(n+a+b+1)(2n+a+b)Tn+1(X) = [(2n+a+b+1)(a2-b2)+P(2n+a+b,3)X] Tn(X) - 2(n+a)(n+b)(2n+a+b+2)Tn-1(X)
 * </pre></p>
 * P(x,y) stands for Pochhammer series, see org.jscience.mathematics.series.PochhammerSeries.
 *
 * @author Silvere Martin-Michiellot
 */

public class JacobiDoublePolynomialFactory extends OrthogonalDoublePolynomialFactory {

    private double a;

    private double b;

    public JacobiDoublePolynomialFactory(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
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
        // T0(X)  = 1
        coefficients = new org.jscience.mathematics.algebraic.numbers.Double[1];
        coefficients[0] = new Double(1);
        result[0] = new DoublePolynomial(coefficients);

        // T1(X)  = [2(a+1)+(a+b+2)(x-1)]/2   = a+1-(a+b+2)/2+((a+b+2)/2)x
        coefficients = new Double[2];
        coefficients[0] = new Double(a + 1 - (a + b + 2) / 2);
        coefficients[1] = new Double((a + b + 2) / 2);
        result[1] = new DoublePolynomial(coefficients);

        return result;

    }

    /**
     * Initialize the recurrence coefficients.
     * The recurrence relation is
     * 2(n+1)(n+a+b+1)(2n+a+b)Tn+1(X) = [(2n+a+b+1)(a2-b2)+P(2n+a+b,3)X] Tn(X) - 2(n+a)(n+b)(2n+a+b+2)Tn-1(X)
     *
     * @param k index of the current step
     */
    public Field.Member[] getRecurrenceCoefficients(int k) {

        Field.Member[] result;

        double denominator = 2 * (k + 1) * (k + a + b + 1) * (2 * k + a + b);
        PochhammerSeries pochhammerSeries = new PochhammerSeries(2 * k + a + b);
        result = new Double[3];
        result[0] = new Double((2 * k + a + b + 1) * (a * a - b * b) / denominator);
        result[1] = new Double(pochhammerSeries.getValueAtRank(3) / denominator);
        result[2] = new Double(2 * (k + a) * (k + b) * (2 * k + a + b + 2) / denominator);

        return result;

    }

}
