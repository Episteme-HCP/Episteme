package org.jscience.mathematics.analysis;

/**
 * The interface CurlFunction defines the methods that must be implemented for
 * a class to represent a function with a curl (the counterpart of the
 * derivative for multidimensional functions).  It should be used for a
 * function (vector field) of 2 (or 3) to 2 (or 3) parameters on R.
 *
 * @author Silvere Martin-Michiellot
 */
public interface CurlFunction extends AbstractMapping {
    /**
     * Returns the Curl of this function, itself a mapping (vector
     * field) of 2 (or 3) to 2 (or 3) parameters on R.
     *
     * @return DOCUMENT ME!
     */
    public AbstractMapping getCurl();
}
