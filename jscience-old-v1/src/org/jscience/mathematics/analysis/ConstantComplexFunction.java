package org.jscience.mathematics.analysis;

import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The constant function.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//could also be defined as a RealPolynomial
public class ConstantComplexFunction extends ComplexFunction
    implements C2Function {
    /** DOCUMENT ME! */
    private final Complex c;

/**
     * Constructs a constant function.
     *
     * @param c DOCUMENT ME!
     */
    public ConstantComplexFunction(Complex c) {
        this.c = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex map(Complex x) {
        return c;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComplexFunction differentiate() {
        return new ConstantComplexFunction(Complex.ZERO);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComplexFunction secondDerivative() {
        return differentiate().differentiate();
    }
}
