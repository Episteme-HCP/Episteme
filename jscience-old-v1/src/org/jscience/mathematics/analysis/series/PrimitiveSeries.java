package org.jscience.mathematics.analysis.series;

/**
 * A mathematical series that uses primitive types.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface PrimitiveSeries extends AbstractSeries {
    //n should always be greater or equal to zero, though no error is thrown otherwise
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n);

    //there is probably some things to implement here using http://mathworld.wolfram.com/ConvergenceTests.html
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue();

    //we could also getMean() getVariance(), etc.
}
