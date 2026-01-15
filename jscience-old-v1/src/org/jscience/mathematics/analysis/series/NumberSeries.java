package org.jscience.mathematics.analysis.series;

/**
 * A mathematical series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//some series that could be implemented http://mathworld.wolfram.com/MaclaurinSeries.html
//may be we should implement Mapping althougth be be better define a FunctionOfSeries
//You may want to have a look at http://www.research.att.com/~njas/sequences/Seis.html, the On-Line Encyclopedia of Integer Sequences
public interface NumberSeries extends AbstractSeries {
    //n should always be greater or equal to zero, though no error is thrown otherwise
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number getValueAtRank(int n);

    //there is probably some things to implement here using http://mathworld.wolfram.com/ConvergenceTests.html
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number getValue();

    //we could also getMean() getVariance(), etc.
}
