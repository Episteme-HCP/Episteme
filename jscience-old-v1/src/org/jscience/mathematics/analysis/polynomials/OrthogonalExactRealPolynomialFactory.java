package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.numbers.ExactReal;


/**
 * This class is the base class to generate orthogonal polynomials.
 *
 * @author L. Maisonobe, Silvere Martin-Michiellot
 */
public abstract class OrthogonalExactRealPolynomialFactory extends Object
    implements OrthogonalPolynomialFactory {
    /** List holding the coefficients of the polynomials computed so far. */
    private ExactReal[] computedCoefficients;

    /** int that contains the max computed degree */
    private int computedDegree;

/**
     * Simple constructor.
     */
    public OrthogonalExactRealPolynomialFactory() {
        computedDegree = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param degree DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract DoublePolynomial getOrthogonalPolynomial(int degree);

    //the idea here is to precompute coefficients of lower degree polynomials to shorten computing time when multiple polynomials are accessed
    /**
     * DOCUMENT ME!
     *
     * @param degree DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ExactReal[] getOrthogonalPolynomialCoefficients(int degree) {
        if (computedDegree == 0) {
            Polynomial[] polynomials;
            polynomials = getFirstTermsPolynomials();
            computedCoefficients = new ExactReal[3];
            computedCoefficients[0] = (ExactReal) polynomials[0].getCoefficient(0);
            computedCoefficients[1] = (ExactReal) polynomials[1].getCoefficient(0);
            computedCoefficients[2] = (ExactReal) polynomials[1].getCoefficient(1);
            computedDegree = 1;
        }

        // coefficient  for polynomial 0 is  l [0]
        // coefficient  for polynomial 1 are l [1] ... l [2] (degrees 0 ... 1)
        // coefficients for polynomial 2 are l [3] ... l [5] (degrees 0 ... 2)
        // coefficients for polynomial 3 are l [6] ... l [9] (degrees 0 ... 3)
        // coefficients for polynomial 4 are l[10] ... l[14] (degrees 0 ... 4)
        // coefficients for polynomial 5 are l[15] ... l[20] (degrees 0 ... 5)
        // coefficients for polynomial 6 are l[21] ... l[27] (degrees 0 ... 6)
        // ...
        if (degree > computedDegree) {
            ExactReal[] resultComputedCoefficients = new ExactReal[((degree +
                2) * (degree + 1)) / 2];

            System.arraycopy(computedCoefficients, 0,
                resultComputedCoefficients, 0, computedCoefficients.length);

            ExactReal b2k = ExactReal.ZERO;
            ExactReal b3k = ExactReal.ZERO;
            ExactReal b4k = ExactReal.ZERO;

            int startK = ((computedDegree - 1) * computedDegree) / 2;

            for (int k = computedDegree; k < degree; ++k) {
                // start indices of two previous polynomials Ok(X) and Ok-1(X)
                int startKm1 = startK;
                startK += k;

                int index = ((k + 2) * (k + 1)) / 2;

                // a1k Ok+1(X) = (a2k + a3k X) Ok(X) - a4k Ok-1(X)
                // we use bik = aik/a1k
                ExactReal[] values;
                values = (ExactReal[]) getRecurrenceCoefficients(k);
                b2k = values[0];
                b3k = values[1];
                b4k = values[2];

                ExactReal ckPrev;
                ExactReal ck = resultComputedCoefficients[startK];
                ExactReal ckm1 = resultComputedCoefficients[startKm1];

                // degree 0 coefficient
                ExactReal coeff = ck.multiply(b2k);
                coeff = coeff.subtract(ckm1.multiply(b4k));
                resultComputedCoefficients[index] = coeff;

                // degree 1 to degree k-1 coefficients
                for (int i = 1; i < k; ++i) {
                    ckPrev = ck;
                    ck = resultComputedCoefficients[startK + i];
                    ckm1 = resultComputedCoefficients[startKm1 + i];
                    coeff = ck.multiply(b2k);
                    coeff = coeff.add(ckPrev.multiply(b3k));
                    coeff = coeff.subtract(ckm1.multiply(b4k));
                    resultComputedCoefficients[index + i] = coeff;
                }

                // degree k coefficient
                ckPrev = ck;
                ck = resultComputedCoefficients[startK + k];
                coeff = ck.multiply(b2k);
                coeff = coeff.add(ckPrev.multiply(b3k));
                resultComputedCoefficients[index + k] = coeff;

                // degree k+1 coefficient
                resultComputedCoefficients[index + k + 1] = ck.multiply(b3k);
            }

            computedDegree = degree;
            computedCoefficients = resultComputedCoefficients;
        }

        int start = (degree * (degree + 1)) / 2;
        ExactReal[] coefficients;

        coefficients = new ExactReal[degree + 1];

        for (int i = 0; i <= degree; ++i) {
            coefficients[i] = computedCoefficients[start + i];
        }

        return coefficients;
    }

    /**
     * Initialize the recurrence coefficients for degree 0 and 1.
     *
     * @return an array which contains the coefficients for degree 0 and 1
     */
    public abstract Polynomial[] getFirstTermsPolynomials();

    /**
     * Initialize the recurrence coefficients. The recurrence relation
     * is<pre>a1k Ok+1(X) = (a2k + a3k X) Ok(X) - a4k Ok-1(X)</pre>
     *
     * @param k index of the current step
     *
     * @return a double array of 3 elements: b2k = double[0] coefficient to
     *         initialize (b2k = a2k / a1k) b3k = double[1] coefficient to
     *         initialize (b3k = a3k / a1k) b4k = double[2] coefficient to
     *         initialize (b4k = a4k / a1k)
     */
    public abstract Field.Member[] getRecurrenceCoefficients(int k);
}
