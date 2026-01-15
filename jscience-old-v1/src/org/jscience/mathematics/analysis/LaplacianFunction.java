package org.jscience.mathematics.analysis;

/**
 * The interface LaplacianFunction defines the methods that must be implemented
 * for a class to represent a function with a laplacian (the counterpart of
 * the second derivative for multidimensional functions).  It should be used
 * for a function of 2 (or 3) to 1 parameters on R.
 *
 * @author Silvere Martin-Michiellot
 */
public interface LaplacianFunction extends GradientFunction {
    /**
     * Returns the Laplacian of this function, itself a mapping (vector
     * field) of 2 (or 3) to 1 parameters on R.
     *
     * @return DOCUMENT ME!
     */
    public AbstractMapping getScalarLaplacian();
}
