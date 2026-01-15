package org.jscience.mathematics.analysis;

/**
 * The interface C2Function defines the methods that must be implemented for a
 * class to represent a twice differentiable function. It should be used for a
 * function of 1 to 1 parameters on R.
 *
 * @author Silvere Martin-Michiellot
 */
public interface C2Function extends C1Function {
    /**
     * Returns the second differential of this function, or
     * f.differenciate().differenciate()
     *
     * @return DOCUMENT ME!
     */
    public AbstractMapping secondDerivative();
}
