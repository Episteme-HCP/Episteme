package org.jscience.mathematics.analysis;

/**
 * The interface GradientFunction defines the methods that must be implemented
 * for a class to represent a function with a gradient (the counterpart of the
 * derivative for multidimensional functions).  It should be used for a
 * function of 2 (or 3) to 1 parameters on R.
 *
 * @author Silvere Martin-Michiellot
 */
public interface GradientFunction extends AbstractMapping {
    /**
     * Returns the Gradient of this function, itself a mapping (vector
     * field) of 2 (or 3) to 2 (or 3) parameters on R.
     *
     * @return DOCUMENT ME!
     */
    public AbstractMapping getGradient();
}
