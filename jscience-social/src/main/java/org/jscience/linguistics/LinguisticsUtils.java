package org.jscience.linguistics;

/**
 * A class representing some useful methods for linguistics
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class LinguisticsUtils extends Object {
    /**
     * The zipf's law, see http://en.wikipedia.org/wiki/Zipf%27s_law
     *
     * @param k DOCUMENT ME!
     * @param s DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getZipf(double k, double s, double n) {
        return 1 / (Math.pow(k, s) * harmonicSum(n, s));
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double harmonicSum(double n, double s) {
        int i;
        double result;

        result = 0;

        for (i = 1; i <= n; i++) {
            result = result + (1 / Math.pow(i, s));
        }

        return result;
    }
}
