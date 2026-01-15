package org.jscience.mathematics.wavelet;

/**
 * This exception is used to indicate that the current chosen
 * multiresolution is not appropriate because the number of scaling functions
 * is incompatible (with the multiresolution).
 *
 * @author Daniel Lemire
 */
public class IllegalScalingException extends IllegalArgumentException {
/**
     * Creates a new IllegalScalingException object.
     */
    public IllegalScalingException() {
    }

/**
     * Creates a new IllegalScalingException object.
     *
     * @param s DOCUMENT ME!
     */
    public IllegalScalingException(String s) {
        super(s);
    }

/**
     * Creates a new IllegalScalingException object.
     *
     * @param n0  DOCUMENT ME!
     * @param min DOCUMENT ME!
     */
    public IllegalScalingException(int n0, int min) {
        super("The length parameter " + n0 + " must be at least " + min +
            ". Please change the wavelet or the number of iterations.");
    }
}
