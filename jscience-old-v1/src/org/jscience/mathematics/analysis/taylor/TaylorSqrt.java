/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.analysis.taylor;

/**
 * Implementation of Taylor expandable square root of a <b>TaylorDouble</b>.
 *
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorSqrt extends TaylorUnary {
    /**
     * Construct new square root of <i>x</i> sqrt(<i>x</i>).
     * <p/>
     * The square root function is only analytical for <i>x &gt; 0</i>,
     * and <b>TaylorSqrt</b> only works properly in this domain.
     */
    public TaylorSqrt(TaylorDouble x) {
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
            coeffs[0] = Math.sqrt(operand.coeffs[0]);
            order = 0;

            return;
        }

        if (operand.constant) {
            coeffs[k] = 0;
            order = k;

            return;
        }

        operand.calcOrder(k);

        coeffs[k] = 0;

        for (int j = 1; j < k; j++)
            coeffs[k] -= (j * coeffs[j] * coeffs[k - j - 1]);

        coeffs[k] /= k;
        coeffs[k] += (operand.coeffs[k] / 2);
        coeffs[k] /= coeffs[0];

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

        return (3 * k) + 1 + operand.flops(k);
    }
}
