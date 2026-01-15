package org.jscience.mathematics.analysis.series;

/**
 * The Pochhammer Symbol mathematical series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//see http://mathworld.wolfram.com/PochhammerSymbol.html
public final class PochhammerSeries implements PrimitiveSeries {
    /** DOCUMENT ME! */
    private double x;

/**
     * Creates a new PochhammerSeries object.
     *
     * @param x DOCUMENT ME!
     */
    public PochhammerSeries(double x) {
        this.x = x;
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
        if (n > 0) {
            return getValueAtRank(n - 1) * ((x + n) - 1);
        } else {
            if (n == 0) {
                return 1;
            } else {
                throw new IllegalArgumentException(
                    "n must be greater or equal to 0.");
            }
        }
    }

    //we should provide a method getPolynomial()
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConvergent() {
        //actually, this result is pure speculation from my side
        return (x != 0);
    }

    //tries to compute the value at infinitum
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        if (x == 0) {
            return 0;
        } else {
            //actually, this result is pure speculation from my side
            return Double.POSITIVE_INFINITY;
        }
    }
}
