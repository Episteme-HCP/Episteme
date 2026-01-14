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
 * Implementation of a function that is equal to the independent variable <i>t</i>.
 * <p/>
 * The expansion coefficients of <i>y</i>(<i>t</i>) = <i>t</i> are
 * <i>y<sub>0</sub> = t<sub>0</sub>, y<sub>1</sub> = 1</i> and
 * <i>y<sub>k</sub> = 0</i> otherwise.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorIndependant extends TaylorDouble {
    /**
     * Construct new independent variable with expansion point <i>t<sub>0</sub></i>.
     */
    public TaylorIndependant(double t0) {
        coeffs = new double[10];
        coeffs[0] = t0;
        coeffs[1] = 1;
        order = 1;
        constant = false;
        independent = true;
    }

    /**
     * Set the <i>k</i>th coefficient - all except the two first are zero.
     */
    public void calcOrder(int k) {
        if (order >= k) {
            return;
        }

        if (k >= coeffs.length) {
            setToLength(k + 10);
        }

        coeffs[k] = 0;
        order = k;
    }

    /**
     * Evaluation is simply <i>t<sub>0</sub>+h</i>
     */
    public double evaluate(double h) {
        return coeffs[0] + h;
    }

    /**
     * This method is empty; use <i>reset</i>(<i>t0</i>) instead.
     */
    public void reset() {
    }

    /**
     * Shift expansion point. Remember to <i>reset</i> all dependent variables and
     * functions.
     */
    public void reset(double t0) {
        coeffs[0] = t0;
    }
}
