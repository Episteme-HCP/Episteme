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
