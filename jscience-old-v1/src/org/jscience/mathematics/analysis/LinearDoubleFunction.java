package org.jscience.mathematics.analysis;

/**
 * The linear function class.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//could also be defined as a DoublePolynomial
public class LinearDoubleFunction extends DoubleFunction implements C2Function {
    /** DOCUMENT ME! */
    private final double m;

    /** DOCUMENT ME! */
    private final double p;

/**
     * Constructs a linear function, y=mx+p
     *
     * @param m DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public LinearDoubleFunction(double m, double p) {
        this.m = m;
        this.p = p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(double x) {
        return (m * x) + p;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction differentiate() {
        return new ConstantDoubleFunction(m);
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
