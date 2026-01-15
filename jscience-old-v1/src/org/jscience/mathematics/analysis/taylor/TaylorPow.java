package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of Taylor expandable real power of a <b>TaylorDouble</b>.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorPow extends TaylorUnary {
    private double a;

    /**
     * Construct new <i>a</i>th power of <i>x</i> <i>x<sup>a</sup></i>.
     * <p/>
     * Notice that if <i>a</i> is an integer, e.g. 2, <b>TaylorPow</b> is as
     * much overkill as <i>Math.pow(x,2)</i> is to <i>x*x</i>.
     * <p/>
     * The power function is only analytical for <i>x &gt; 0</i>, and
     * <b>TaylorPow</b> only works properly in this regime.
     */
    public TaylorPow(TaylorDouble x, double a) {
        super(x);
        this.a = a;

        independent = false;
    }

    /**
     * Creates a new TaylorPow object.
     *
     * @param x DOCUMENT ME!
     * @param a DOCUMENT ME!
     */
    public TaylorPow(TaylorDouble x, TaylorConstant a) {
        this(x, a.evaluate(0.0));
    }

    /**
     * Increase the order of this Taylor expansion from <i>k-1</i> to <i>k</i>.
     */
    public void calcOrder(int k) {
        if (order >= k) {
            return;
        }

        if (k >= coeffs.length) {
            setToLength(k + 10);
        }

        if (k == 0) {
            operand.calcOrder(0);
            coeffs[0] = Math.pow(operand.coeffs[0], a);
            order = 0;

            return;
        }

        if (operand.constant) {
            coeffs[k] = 0;
            order = k;

            return;
        }

        if (operand.independent) {
            operand.calcOrder(1);
            coeffs[k] = (operand.coeffs[1] * coeffs[k - 1] * (((a + 1) / k) -
                    1)) / operand.coeffs[0];
            order = k;

            return;
        }

        operand.calcOrder(k);
        coeffs[k] = 0;

        for (int j = 1; j <= k; j++)
            coeffs[k] += ((((j * (a + 1)) / k) - 1) * operand.coeffs[j] * coeffs[k -
                    j]);

        coeffs[k] /= operand.coeffs[0];

        order = k;
    }

    /**
     * Returns the number of flops required to calculate the <i>k</i>th coefficient
     * of this <b>TaylorDouble</b>.
     * <p/>
     * Notice that <i>flops</i> changes the current order, so <i>calcOrder</i> and
     * <i>flops</i> should be separated by a call to <i>reset</i>
     */
    public int flops(int k) {
        if (order >= k) {
            return 0;
        }

        if (k == 0) {
            order = 0;

            return 20 + operand.flops(k);
        }

        order = k;

        if (operand.constant) {
            return 0;
        }

        if (operand.independent) {
            return 6;
        }

        return (7 * k) + 1 + operand.flops(k);
    }
}
