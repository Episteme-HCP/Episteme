package org.jscience.mathematics.analysis.fitting;

import org.jscience.mathematics.analysis.estimation.EstimatedParameter;

/**
 * This class represents a polynomial coefficient.
 * <p/>
 * <p>Each coefficient is uniquely defined by its degree.</p>
 *
 * @author L. Maisonobe
 * @version $Id: PolynomialCoefficient.java,v 1.2 2007-10-21 17:38:16 virtualcall Exp $
 * @see PolynomialFitter
 */
public class PolynomialCoefficient
        extends EstimatedParameter {

    public PolynomialCoefficient(int degree) {
        super("a" + degree, 0.0);
        this.degree = degree;
    }

    public final int degree;

}
