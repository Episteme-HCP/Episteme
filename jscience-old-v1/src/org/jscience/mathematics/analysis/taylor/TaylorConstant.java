package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of a function that is constant with respect to the
 * independent variable <i>t</i>.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorConstant extends TaylorDouble {
/**
     * Construct new constant function of value <i>c</i>.
     *
     * @param c DOCUMENT ME!
     */
    public TaylorConstant(double c) {
        coeffs = new double[10];
        coeffs[0] = c;
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
     * Evaluation is simply <i>c<sub>0</sub></i>
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double evaluate(double t) {
        return coeffs[0];
    }

    /**
     * The <i>reset</i> method is empty for <b>TaylorConstant</b>,
     * since Taylor coefficients are unaffected by change of expansion point.
     */
    public void reset() {
    }
}
