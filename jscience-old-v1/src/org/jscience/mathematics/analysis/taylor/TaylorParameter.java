package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of a function that is constant with respect to the
 * independent variable <i>t</i>, but that will need to be varied during
 * calculations.
 *
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorParameter extends TaylorDouble {
/**
     * Construct new parameter of value <i>a</i>.
     *
     * @param a DOCUMENT ME!
     */
    public TaylorParameter(double a) {
        coeffs = new double[10];
        coeffs[0] = a;
        order = 0;
        constant = true;
        independent = false;
    }

    /**
     * Set <i>k</i>th Taylor coefficient - all except the first are
     * zero.
     *
     * @param k DOCUMENT ME!
     */
    public void calcOrder(int k) {
        if (order >= k) {
            return;
        }

        if (k >= coeffs.length) {
            setToLength(k + 10);
        }

        coeffs[k] = 0;
        order = k;
    }

    /**
     * Evaluation is simply <i>a<sub>0</sub></i>
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double evaluate(double t) {
        return coeffs[0];
    }

    /**
     * Set the value of this parameter to <i>a</i>.
     *
     * @param a DOCUMENT ME!
     */
    public void setValue(double a) {
        coeffs[0] = a;
    }

    /**
     * The <i>reset</i> method is empty for <b>TaylorParameter</b>,
     * since Taylor coefficients are unaffected by change of expansion point.
     */
    public void reset() {
    }
}
