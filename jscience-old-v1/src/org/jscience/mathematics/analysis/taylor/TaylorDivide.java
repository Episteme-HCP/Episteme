package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of Taylor expandable division of <b>two TaylorDouble</b>s.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorDivide extends TaylorBinary {
    /**
     * Constructs new division <i>a / b</i>.
     */
    public TaylorDivide(TaylorDouble a, TaylorDouble b) {
        super(a, b);

        // if the left operand is independent and if the right is constant
        // then this is independent
        if (leftOperand.independent && rightOperand.constant) {
            independent = true;
        } else {
            independent = false;
        }
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

        leftOperand.calcOrder(k);

        if (rightOperand.constant && (k > 0)) { // for k=0 the operands need to be recalculated

            // don't struggle to save the last flop, when both a and b are constant
            coeffs[k] = leftOperand.coeffs[k] / rightOperand.coeffs[0];
            order = k;

            return;
        }

        rightOperand.calcOrder(k);

        coeffs[k] = leftOperand.coeffs[k];

        for (int j = 1; j <= k; j++)
            coeffs[k] -= (rightOperand.coeffs[j] * coeffs[k - j]);

        coeffs[k] /= rightOperand.coeffs[0];

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

        order = k;

        if (rightOperand.constant) { // rightOperand may be cosine of a constant which is expensive

            return 1 + leftOperand.flops(k) + rightOperand.flops(k);
        }

        return (3 * k) + 1 + leftOperand.flops(k) + rightOperand.flops(k);
    }
}
