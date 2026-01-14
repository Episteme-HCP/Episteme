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
 * The abstract superclass for functions of one <b>TaylorDouble</b> variable.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public abstract class TaylorUnary extends TaylorDouble {
    /**
     * The operand of this unary operator.
     */
    public TaylorDouble operand;

    /**
     * Default constructor for functions of one variable.
     */
    public TaylorUnary(TaylorDouble op) {
        coeffs = new double[10];
        order = -1;
        operand = op;

        if (op.constant) {
            constant = true;
        } else {
            constant = false;
        }
    }

    /**
     * Signal shift of expansion point.
     * <p/>
     * All current coefficients of <i>this</i> and the operand are expected to be
     * invalid, so <i>order</i> is set to -1.
     * <p/>
     * Notice: if <i>order</i> is -1, corresponding to a newly <i>reset</i> of this,
     * nothing is done, and the operand is thus not guaranteed to be <i>reset</i>!
     */
    public void reset() {
        if (order < 0) {
            return;
        }

        order = -1;
        operand.reset();
    }
}
