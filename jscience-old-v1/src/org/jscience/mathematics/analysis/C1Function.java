package org.jscience.mathematics.analysis;

/**
 * The interface C1Function defines the methods that must be implemented for a
 * class to represent a differentiable function.  It should be used for a
 * function of 1 to 1 parameters on R.
 *
 * @author Silvere Martin-Michiellot
 */
public interface C1Function extends AbstractMapping {
    /**
     * Returns the differential of this function.
     *
     * @return DOCUMENT ME!
     */
    public AbstractMapping differentiate();
}
