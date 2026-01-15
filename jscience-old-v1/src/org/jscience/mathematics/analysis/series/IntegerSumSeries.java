package org.jscience.mathematics.analysis.series;

/**
 * The Integer Sum mathematical series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class IntegerSumSeries implements PrimitiveSeries {
/**
     * Creates a new IntegerSumSeries object.
     */
    public IntegerSumSeries() {
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
            return (n * (n - 1)) / 2;
        } else {
            throw new IllegalArgumentException("n must be greater than 0.");
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
