package org.jscience.mathematics.analysis.series;

/**
 * The SumOfSeriesSeries is the series defined as the sum of two series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class SumOfSeriesSeries implements PrimitiveSeries {
    /** DOCUMENT ME! */
    private PrimitiveSeries series1;

    /** DOCUMENT ME! */
    private PrimitiveSeries series2;

    //be sure to have not null arguments
    /**
     * Creates a new SumOfSeriesSeries object.
     *
     * @param series1 DOCUMENT ME!
     * @param series2 DOCUMENT ME!
     */
    public SumOfSeriesSeries(PrimitiveSeries series1, PrimitiveSeries series2) {
        this.series1 = series1;
        this.series2 = series2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        return series1.getValueAtRank(n) + series2.getValueAtRank(n);
    }

    //this method will return the following
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConvergent() {
        return series1.isConvergent() && series2.isConvergent();
    }

    //tries to compute the value at infinitum
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return Double.NaN;
    }
}
