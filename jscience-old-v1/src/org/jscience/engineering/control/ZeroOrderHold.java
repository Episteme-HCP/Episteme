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

package org.jscience.engineering.control;

import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ZeroOrderHold extends BlackBox {
    // Constructor
    /**
     * Creates a new ZeroOrderHold object.
     *
     * @param deltaT DOCUMENT ME!
     * @param orderPade DOCUMENT ME!
     */
    public ZeroOrderHold(double deltaT, int orderPade) {
        super.name = "ZeroOrderHold";
        super.fixedName = "ZeroOrderHold";
        super.deltaT = deltaT;
        super.orderPade = orderPade;
        this.setNumDen(deltaT);
    }

    // Constructor
    /**
     * Creates a new ZeroOrderHold object.
     *
     * @param deltaT DOCUMENT ME!
     */
    public ZeroOrderHold(double deltaT) {
        super.name = "ZeroOrderHold";
        super.fixedName = "ZeroOrderHold";
        super.deltaT = deltaT;
        this.setNumDen(deltaT);
    }

    // set the numerators and denominators
    /**
     * DOCUMENT ME!
     *
     * @param deltaT DOCUMENT ME!
     */
    public void setNumDen(double deltaT) {
        // set denominator, s
        super.sDenom = new ComplexPolynomial(new Complex[] {
                    new Complex(0.0D), new Complex(1.0D)
                });
        super.sPoles[0] = new Complex(0.0D, 0.0D);
        super.sPoles = new Complex[] { Complex.ZERO };

        // set exp(-sT) part of pade numerator
        super.sNumer = new ComplexPolynomial(new Complex[] { new Complex(1.0D) });
        super.deadTime = deltaT;
        super.pade();
        super.deadTime = 0.0D;

        // add 1 to exp(-sT)[=padeNumer/padeDenom]/s
        super.sNumerPade = (ComplexPolynomial) super.sNumerPade.add(super.sDenomPade);
        super.sZerosPade = sNumerPade.roots();
    }
}
