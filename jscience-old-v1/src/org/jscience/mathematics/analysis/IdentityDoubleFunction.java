package org.jscience.mathematics.analysis;

/**
 * The identity function.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//could also be defined as a RealPolynomial
public class IdentityDoubleFunction extends DoubleFunction implements C2Function {
/**
     * Constructs an identity function.
     */
    public IdentityDoubleFunction() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(double x) {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction differentiate() {
        return new ConstantDoubleFunction(1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction secondDerivative() {
        return differentiate().differentiate();
    }
}
