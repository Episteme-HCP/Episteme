package org.jscience.mathematics.analysis;

/**
 * The constant function.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//could also be defined as a RealPolynomial
public class ConstantDoubleFunction extends DoubleFunction implements C2Function {
    /** DOCUMENT ME! */
    private final double c;

/**
     * Constructs a constant function.
     *
     * @param c DOCUMENT ME!
     */
    public ConstantDoubleFunction(double c) {
        this.c = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(double x) {
        return c;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction differentiate() {
        return new ConstantDoubleFunction(0);
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
