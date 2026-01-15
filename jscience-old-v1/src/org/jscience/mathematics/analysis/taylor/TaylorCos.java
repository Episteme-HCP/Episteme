package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of Taylor expandable cosine of a <b>TaylorDouble</b>.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorCos extends TaylorUnary {
    /**
     * DOCUMENT ME!
     */
    public TaylorSin sin;

    /**
     * Construct new cosine cos(<i>x</i>).
     */
    public TaylorCos(TaylorDouble x) {
        super(x);
        sin = new TaylorSin(x, this);
        independent = false;
    }

    /**
     * Construct new cosine cos(<i>x</i>), with corresponding sine <i>sin</i>.
     */
    public TaylorCos(TaylorDouble x, TaylorSin sin) {
        super(x);
        this.sin = sin;
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
            sin.setToLength(k + 10);
        }

        if (k == 0) {
            operand.calcOrder(0);
            coeffs[0] = Math.cos(operand.coeffs[0]);
            sin.coeffs[0] = Math.sin(operand.coeffs[0]);
            order = 0;
            sin.order = 0;

            return;
        }

        if (operand.constant) {
            coeffs[k] = 0;
            sin.coeffs[k] = 0;
            order = k;
            sin.order = 0;

            return;
        }

        if (operand.independent) {
            operand.calcOrder(1);
            coeffs[k] = (-operand.coeffs[1] * sin.coeffs[k - 1]) / k;
            sin.coeffs[k] = (operand.coeffs[1] * coeffs[k - 1]) / k;
            order = k;
            sin.order = k;
        }

        operand.calcOrder(k);

        coeffs[k] = 0;
        sin.coeffs[k] = 0;

        for (int j = 1; j <= k; j++) {
            coeffs[k] += (j * operand.coeffs[j] * sin.coeffs[k - j]);
            sin.coeffs[k] += (j * operand.coeffs[j] * coeffs[k - j]);
        }

        coeffs[k] /= -k;
        sin.coeffs[k] /= k;

        order = k;
        sin.order = k;
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

            return 40 + operand.flops(k);
        }

        order = k;
        sin.order = k;

        if (operand.constant) {
            return 0;
        }

        if (operand.independent) {
            return 5;
        }

        return (6 * k) + 2 + operand.flops(k);
    }
}
