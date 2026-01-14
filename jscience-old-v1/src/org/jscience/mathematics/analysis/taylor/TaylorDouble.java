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
 * The abstract superclass of all <b>TaylorDouble</b> objects, representing a species
 * of expressions that are automatically Taylor expandable.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public abstract class TaylorDouble implements java.io.Serializable {
    /**
     * The Taylor coefficients of this <b>TaylorDouble</b>.
     * <p/>
     * Only the first <i>order + 1</i> elements are valid, including
     * <i>coeffs[order]</i>.
     * The zeroth coefficient <i>f<sub>0</sub> = f</i> (<i>t<sub>0</sub></i>) is
     * indexed <i>coeffs[0]</i>.
     * <p/>
     * The array is typically initialized with 10 elements, but the size is
     * dynamically resized if in need for extra space.
     */
    protected double[] coeffs;

    /**
     * The current order of the expansion of this <b>TaylorDouble</b>.
     */
    protected int order;

    /**
     * True for <b>TaylorDouble</b>s that are constant in time, such as constants,
     * parameters or a multiplication of two parameters.
     * <p/>
     * When a method realizes that an operand of some kind is either <i>constant</i>
     * or <i>independent</i>, it should attempt to optimize code such as not to
     * address the numerous zero coefficients of such <b>TaylorDouble</b>.
     */
    protected boolean constant;

    /**
     * True for the independent variable <i>t</i> or another <b>TaylorDouble</b> with only
     * the two first Taylor coefficients nonzero, such as <i>w&middot;t + c</i>.
     */
    protected boolean independent;

    /**
     * Evaluate at time <i>t = t<sub>0</sub> + h</i>, where <i>t<sub>0</sub></i> is
     * the expansion point; that is evaluate the Taylor expansion
     * <center>
     * <i>f</i> (<i>x,t</i>) = <i>f</i> (<i>t<sub>0</sub></i>) + <i>f '</i>
     * (<i>t<sub>0</sub></i>)&middot;<i>h + f ''</i> (<i>t<sub>0</sub></i>)
     * &middot;<i>h<sup>2</sup> + ... </i>
     * </center>
     */
    public double evaluate(double h) {
        double p = coeffs[order];

        for (int i = order - 1; i >= 0; i--) {
            p *= h;
            p += coeffs[i];
        }

        return p;
    }

    /**
     * Expand this <b>TaylorDouble</b> variable to order <i>k</i>. If dependent on any
     * <b>TaylorDependant</b>s, those must be available to order <i>k</i>, or they must be
     * governed by some ODE.
     */
    public void expandToOrder(int k) {
        for (int i = order + 1; i <= k; i++)
            calcOrder(i);
    }

    /**
     * Any <b>TaylorDouble</b> must implement this method for calculation of the
     * <i>k</i>th Taylor coefficient.
     * <p/>
     * The <b>TaylorDouble</b> should assume, that it's <i>k-1</i> first coefficients are
     * valid, so that only the <i>k</i>th is to be calculated.
     */
    protected abstract void calcOrder(int k);

    /**
     * Return the number of flops required to calculate the <i>k</i>th coefficient of
     * this <b>TaylorDouble</b>.
     * <p/>
     * Notice that <i>flops</i> changes <i>order</i>, so <i>calcOrder</i> and
     * <i>flops</i> should be separated by a call to <i>reset</i>
     */
    protected int flops(int k) {
        return 0;
    }

    /**
     * Any <b>TaylorDouble</b> must implement a method to reset, that is to drop it's
     * order to -1 and reset any <b>TaylorDouble</b>s it is dependent on.
     * <p/>
     * This strategy has been chosen, since it is difficult/unefficient to
     * communicate the shifting of the expansion point from the independent variable
     * upwards in the dependence tree to the top function value. Instead the top
     * level <b>TaylorDouble</b> function value(s) is <i>reset</i>, which causes the
     * information to propagate downwards to all the <b>TaylorDouble</b>s it is dependent
     * on.
     * <p/>
     * The <i>reset</i> signal is also intended to communicate such thing as the
     * change of a parameter value, which also require all coefficients to be
     * recalculated.
     */
    public abstract void reset();

    /**
     * Get the <i>k</i>th Taylor coefficient.
     * <p/>
     * Throws an <i>ArithmeticException</i> if the <i>k</i>th coefficient is not
     * available (yet).
     */
    public double getCoeff(int k) {
        if (k > order) {
            throw new ArithmeticException("The " + k +
                    "'th Taylor coefficient is not available");
        }

        return coeffs[k];
    }

    /**
     * Get the order of the Taylor expansion. The coefficients are indexed from zero,
     * and -1 is returned if no coefficients are currently available.
     */
    public int getOrder() {
        return order;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "(x_0,...x_n) = (" + coeffs[0];

        for (int i = 1; i <= order; i++)
            s += ("," + coeffs[i]);

        s += ")";

        return s;
    }

    /**
     * Set the size of the coefficient array <i>coeffs[]</i> to <i>k</i>.
     * <p/>
     * If the current <i>order</i> is larger than <i>k-1</i>, it is decreased.
     */
    protected void setToLength(int k) {
        double[] temp = coeffs;
        coeffs = new double[k];

        if (k <= order) {
            order = k - 1;
        }

        for (int i = 0; i <= order; i++)
            coeffs[i] = temp[i];
    }
}
