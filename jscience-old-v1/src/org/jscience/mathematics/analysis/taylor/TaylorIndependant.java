package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of a function that is equal to the independent variable <i>t</i>.
 * <p/>
 * The expansion coefficients of <i>y</i>(<i>t</i>) = <i>t</i> are
 * <i>y<sub>0</sub> = t<sub>0</sub>, y<sub>1</sub> = 1</i> and
 * <i>y<sub>k</sub> = 0</i> otherwise.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorIndependant extends TaylorDouble {
    /**
     * Construct new independent variable with expansion point <i>t<sub>0</sub></i>.
     */
    public TaylorIndependant(double t0) {
        coeffs = new double[10];
        coeffs[0] = t0;
        coeffs[1] = 1;
        order = 1;
        constant = false;
        independent = true;
    }

    /**
     * Set the <i>k</i>th coefficient - all except the two first are zero.
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
     * Evaluation is simply <i>t<sub>0</sub>+h</i>
     */
    public double evaluate(double h) {
        return coeffs[0] + h;
    }

    /**
     * This method is empty; use <i>reset</i>(<i>t0</i>) instead.
     */
    public void reset() {
    }

    /**
     * Shift expansion point. Remember to <i>reset</i> all dependent variables and
     * functions.
     */
    public void reset(double t0) {
        coeffs[0] = t0;
    }
}
