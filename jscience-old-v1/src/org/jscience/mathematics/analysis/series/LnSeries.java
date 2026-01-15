package org.jscience.mathematics.analysis.series;

/**
 * The Ln mathematical series, also named MercatorSeries
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this is really a raw implementation, we can do better using high precision numbers
public final class LnSeries implements PrimitiveSeries {
    /** DOCUMENT ME! */
    private double x;

    //computes ln (x+1)
    /**
     * Creates a new LnSeries object.
     *
     * @param x DOCUMENT ME!
     */
    public LnSeries(double x) {
        if (x > 0) {
            this.x = x;
        } else {
            throw new IllegalArgumentException("x must be greater or equal 0.");
        }
    }

    //computes ln (x+1)
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        if (n > 1) {
            //we won't get a high precision using Math.pow(), but who knows
            return getValueAtRank(n - 1) +
            ((Math.pow(-1, n + 1) * Math.pow(x, n)) / n);
        } else {
            if (n == 1) {
                return x;
            } else {
                throw new IllegalArgumentException("n must be greater than 0.");
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
        return true;
    }

    //computes ln (x+1)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return Math.log(1 + x);
    }
}
