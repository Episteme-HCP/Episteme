package org.jscience.mathematics.analysis.series;

/**
 * The Integer Sum of Square mathematical series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class IntegerSumOfSquaresSeries implements PrimitiveSeries {
/**
     * Creates a new IntegerSumOfSquaresSeries object.
     */
    public IntegerSumOfSquaresSeries() {
    }

    //n>0
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        if (n > 0) {
            return getValueAtRank(n - 1) + (n * n);
        } else {
            if (n == 0) {
                return 0;
            } else {
                throw new IllegalArgumentException(
                    "n must be greater than or equal 0.");
            }
        }
    }

    //is DOES diverge
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConvergent() {
        return false;
    }

    //tries to compute the value at infinitum
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return Double.POSITIVE_INFINITY;
    }
}
