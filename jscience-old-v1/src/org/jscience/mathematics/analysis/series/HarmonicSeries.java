package org.jscience.mathematics.analysis.series;

/**
 * The Harmonic mathematical series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class HarmonicSeries implements PrimitiveSeries {
/**
     * Creates a new HarmonicSeries object.
     */
    public HarmonicSeries() {
    }

    //n>0 strictly otherwise an error is thrown
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        if (n > 1) {
            return getValueAtRank(n - 1) + (1 / n);
        } else {
            if (n == 1) {
                return 1;
            } else {
                throw new IllegalArgumentException("n must be greater than 0.");
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
