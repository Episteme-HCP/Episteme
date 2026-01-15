package org.jscience.mathematics.analysis;

/**
 * The interface DivergenceFunction defines the methods that must be
 * implemented for a class to represent a function with a divergence (the
 * counterpart of the derivative for multidimensional functions).  It should
 * be used for a function (vector field) of 2 (or 3) to 2 (or 3) parameters on
 * R.
 *
 * @author Silvere Martin-Michiellot
 */
public interface DivergenceFunction extends AbstractMapping {
    /**
     * Returns the Divergence of this function, itself a mapping of 2
     * (or 3) to 1 parameters on R.
     *
     * @return DOCUMENT ME!
     */
    public AbstractMapping getDivergence();
}
