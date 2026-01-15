package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of Taylor expandable unary minus of a <b>TaylorDouble</b>.
 *
 * @author Carsten Knudsen
 */
public class TaylorNegate extends TaylorUnary {
    /**
     * Construct new unary minus of <i>x</i>.
     */
    public TaylorNegate(TaylorDouble x) {
        super(x);
        independent = false;
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
            coeffs[0] = -operand.coeffs[0];
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
            coeffs[k] = -operand.coeffs[k];
            order = k;

            return;
        }

        operand.calcOrder(k);
        coeffs[k] = -operand.coeffs[k];

        order = k;
    }

    /**
     * Return the number of flops required to calculate the <i>k</i>th coefficient
     * of this <b>TaylorDouble</b>.
     * <p/>
     * Note that <i>flops</i> changes the current order, so <i>calcOrder</i> and
     * <i>flops</i> should be separated by a call to <i>reset</i>
     */
    public int flops(int k) {
        if (order >= k) {
            return 0;
        }

        order = k;

        return 1 + operand.flops(k);
    }
}
