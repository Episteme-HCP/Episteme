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
 * Implementation of a function that is constant with respect to the
 * independent variable <i>t</i>, but that will need to be varied during
 * calculations.
 *
 * @author Laurits H&oslash;jgaard Olesen
 */
public class TaylorParameter extends TaylorDouble {
/**
     * Construct new parameter of value <i>a</i>.
     *
     * @param a DOCUMENT ME!
     */
    public TaylorParameter(double a) {
        coeffs = new double[10];
        coeffs[0] = a;
        order = 0;
        constant = true;
        independent = false;
    }

    /**
     * Set <i>k</i>th Taylor coefficient - all except the first are
     * zero.
     *
     * @param k DOCUMENT ME!
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
     * Evaluation is simply <i>a<sub>0</sub></i>
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double evaluate(double t) {
        return coeffs[0];
    }

    /**
     * Set the value of this parameter to <i>a</i>.
     *
     * @param a DOCUMENT ME!
     */
    public void setValue(double a) {
        coeffs[0] = a;
    }

    /**
     * The <i>reset</i> method is empty for <b>TaylorParameter</b>,
     * since Taylor coefficients are unaffected by change of expansion point.
     */
    public void reset() {
    }
}
