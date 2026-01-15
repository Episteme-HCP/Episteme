package org.jscience.mathematics.analysis.series;

/**
 * The Fibonacci mathematical series, 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55,
 * 89, 144, . . .
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class FibonacciSeries implements PrimitiveSeries {
/**
     * Creates a new FibonacciSeries object.
     */
    public FibonacciSeries() {
    }

    //always returns 0 for 0 or less
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        if (n > 1) {
            return getValueAtRank(n - 1) + getValueAtRank(n - 2);
        } else {
            if (n == 1) {
                return 1;
            } else {
                return 0;
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return Double.POSITIVE_INFINITY;
    }
}
