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
public interface AbstractSeries {
    //you should return false if the result is unknown
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConvergent();

    //we could also getMean() getVariance(), etc.
}
