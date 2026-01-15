package org.jscience.mathematics.analysis;

/**
 * The exponential function.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.0
 */
public class ExponentialDoubleFunction extends DoubleFunction
    implements C2Function {
    /** DOCUMENT ME! */
    private final double A;

    /** DOCUMENT ME! */
    private final double w;

    /** DOCUMENT ME! */
    private final double k;

/**
     * Constructs an exponential function of the form <code>A exp(wx+k)</code>.
     *
     * @param A DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public ExponentialDoubleFunction(double A, double w, double k) {
        this.A = A;
        this.w = w;
        this.k = k;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(double x) {
        return A * Math.exp((w * x) + k);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction differentiate() {
        return new ExponentialDoubleFunction(A * w, w, k);
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
