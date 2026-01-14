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
 * Implementation of Taylor expandable multiplication of <b>two TaylorDouble</b>s.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorMultiply extends TaylorBinary {
    /**
     * Constructs new multiplication <i>a&middot;b</i>.
     */
    public TaylorMultiply(TaylorDouble a, TaylorDouble b) {
        super(a, b);

        // if any of the operands are constant, force the left one to be
        if (b.constant) {
            leftOperand = b;
            rightOperand = a;
        }

        // if the right operand is independent and if the other is constant
        // then this is independent
        if (rightOperand.independent && leftOperand.constant) {
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

        rightOperand.calcOrder(k);

        if (leftOperand.constant && (k > 0)) { // if k=0, the operands need to be recalculated

            // don't struggle to save the last flop when both a and b are constant
            coeffs[k] = rightOperand.coeffs[k] * leftOperand.coeffs[0];
            order = k;

            return;
        }

        leftOperand.calcOrder(k);

        coeffs[k] = 0;

        for (int j = 0; j <= k; j++)
            coeffs[k] += (leftOperand.coeffs[j] * rightOperand.coeffs[k - j]);

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

        if (leftOperand.constant) { // leftOperand may be cosine of a constant which is expensive

            return 1 + leftOperand.flops(k) + rightOperand.flops(k);
        }

        return (3 * k) + leftOperand.flops(k) + rightOperand.flops(k);
    }
}
