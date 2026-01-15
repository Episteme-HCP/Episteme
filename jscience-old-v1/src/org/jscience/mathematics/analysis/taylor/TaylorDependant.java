package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of an <i>unknown</i> function <i>x</i>(<i>t</i>) of the independent
 * variable <i>t</i>.
 * <p/>
 * When performing calculations, either the Taylor coefficients must be known and set
 * with <i>setCoeff</i>, <br>
 * or the <b>TaylorDependant</b> must be governed by an <i>ODE</i>
 * <center>
 * <i>x'</i>(<i>t</i>) = <i>f</i> (<b>x</b>,<i>t</i>)
 * </center>
 * The right-hand-side function should be dependent on <i>x</i>, that is first <i>x</i>
 * is initialized, then <i>f</i> is evaluated using <i>x</i>, an finally the
 * <i>ODE</i> dependence is set using the <b>TaylorDependant</b> <i>setOde</i> method. Now the
 * <i>k</i>th Taylor coefficient can be obtained from the relation
 * <center>
 * (<i>x</i>)<i><sub>k</sub></i> = ( <i>f</i> )<i><sub>k-1</sub> / k </i>
 * </center>
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorDependant extends TaylorDouble {
    /**
     * <b>TaylorDouble</b> expression for the <i>ODE</i> governing this variable.
     */
    protected TaylorDouble rhs;

    /**
     * Construct new dependent variable.
     */
    public TaylorDependant() {
        coeffs = new double[10];
        order = -1;
        constant = false;
        independent = false;
        rhs = null;
    }

    /**
     * Construct new dependent variable of which the first <i>n</i> Taylor
     * coefficients are <i>xn[]</i>.
     */
    public TaylorDependant(double[] xn) {
        order = xn.length - 1;

        if (order >= 10) {
            coeffs = new double[order + 10];
        } else {
            coeffs = new double[10];
        }

        for (int i = 0; i <= order; i++)
            coeffs[i] = xn[i];
    }

    /**
     * If an <i>ODE</i> governing this <b>TaylorDependant</b> is provided, the <i>k</i>th Taylor
     * coefficient can be determined if right-hand-side function is known to order
     * <i>k-1</i>.
     * <p/>
     * If no <i>ODE</i> is available, the Taylor coefficients should be set using
     * <i>setCoeff</i>.
     */
    public void calcOrder(int k) {
        if (order >= k) {
            return;
        }

        if (k >= coeffs.length) {
            setToLength(k + 10);
        }

        rhs.calcOrder(k - 1);
        coeffs[k] = rhs.coeffs[k - 1] / k;
        order = k;
    }

    /**
     * Set the <i>k</i>th Taylor coefficient.
     * <p/>
     * It is assumed that (only) the first <i>k</i> coefficients are valid; the
     * <i>k+1</i>th is set to <i>d</i> and the order is set to <i>k</i>.
     */
    public void setCoeff(int k, double d) {
        if (k >= coeffs.length) {
            setToLength(k + 10);
        }

        coeffs[k] = d;
        order = k;
    }

    /**
     * Set <i>ODE</i> governing this <b>TaylorDependant</b>.
     * <p/>
     * If this is <i>x<sub>i</sub></i>, set
     * <i>x<sub>i</sub></i>'(<i>t</i>) = <i>f</i>(<b>x</b>,<i>t</i>).
     */
    public void setOde(TaylorDouble f) {
        rhs = f;
    }

    /**
     * This method is empty, use <i>reset</i>(<i>double x0</i>) instead.
     */
    public void reset() {
    }

    /**
     * Signal shift of expansion point. Set the expansion point for this to
     * <i>x0</i> and reset right-hand-side function.
     */
    public void reset(double x0) {
        rhs.reset();

        coeffs[0] = x0;
        order = 0;
    }

    /**
     * Return the number of flops required to calculate the <i>k</i>th coefficient
     * of this <b>TaylorDouble</b>.
     * <p/>
     * Notice that <i>flops</i> changes the current order, so <i>calcOrder</i> and
     * <i>flops</i> should be separated by a call to <i>reset</i>.
     * <p/>
     * If no <i>ODE</i> is set, zero is returned.
     */
    public int flops(int k) {
        if ((order >= k) || (rhs == null)) {
            return 0;
        }

        order = k;

        return 1 + rhs.flops(k);
    }
}
