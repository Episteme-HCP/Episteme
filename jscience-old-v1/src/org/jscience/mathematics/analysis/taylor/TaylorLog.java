package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of Taylor expandable logarithm of a <b>TaylorDouble</b>.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorLog extends TaylorUnary {
    /**
     * Construct new natural logarithm ln(<i>x</i>).
     */
    public TaylorLog(TaylorDouble x) {
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
            coeffs[0] = Math.log(operand.coeffs[0]);
            order = 0;

            return;
        }

        if (operand.constant) {
            coeffs[k] = 0;
            order = k;

            return;
        }

        if (operand.independent) {
            if (k == 1) {
                operand.calcOrder(1);
                coeffs[1] = operand.coeffs[1] / operand.coeffs[0];
                order = 1;

                return;
            }

            coeffs[k] = ((1 - k) * coeffs[1] * coeffs[k - 1]) / k;
            order = k;

            return;
        }

        operand.calcOrder(k);

        coeffs[k] = 0;

        for (int j = 1; j < k; j++)
            coeffs[k] -= ((k - j) * operand.coeffs[j] * this.coeffs[k - j]);

        coeffs[k] /= k;
        coeffs[k] += operand.coeffs[k];
        coeffs[k] /= operand.coeffs[0];

        order = k;
    }

    /**
     * Returns the number of flops required to calculate the <i>k</i>th coefficient
     * of this <b>TaylorDouble</b>.
     * <p/>
     * Note that <i>flops</i> changes the current order, so <i>calcOrder</i> and
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
            return 3;
        }

        return (3 * (k - 1)) + 3 + operand.flops(k);
    }
}
