package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of Taylor expandable subtraction of <b>two TaylorDouble</b>s.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorSubtract extends TaylorBinary {
    /**
     * Construct new subtraction <i>a - b</i>.
     */
    public TaylorSubtract(TaylorDouble a, TaylorDouble b) {
        super(a, b);

        if ((a.independent && (b.constant || b.independent)) ||
                (a.constant && b.independent)) {
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

        // don't struggle to save the one flop
        leftOperand.calcOrder(k);
        rightOperand.calcOrder(k);

        coeffs[k] = leftOperand.coeffs[k] - rightOperand.coeffs[k];
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

        order = k;

        return 1 + leftOperand.flops(k) + rightOperand.flops(k);
    }
}
