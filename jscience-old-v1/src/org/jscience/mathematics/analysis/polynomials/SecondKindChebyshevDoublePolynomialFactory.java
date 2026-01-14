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

package org.jscience.mathematics.analysis.polynomials;

/**
 * This class implements Second Kind Chebyshev polynomials.
 * <p/>
 * <p>Second Kind Chebyshev polynomials can be defined by the following recurrence
 * relations:
 * <pre>
 *  T0(X)   = 1
 *  T1(X)   = 2X
 *  2(n+1)(n+2)(2n+1)Tn+1(X) = P(2n+1,3)X Tn(X) - 2(n+1/2)(n+1/2)(2n+3)Tn-1(X)
 * </pre></p>
 * P(x,y) stands for Pochhammer series, see org.jscience.mathematics.series.PochhammerSeries.
 *
 * @author Silvere Martin-Michiellot
 */

//IMPORTANT:
//there is something wrong here as this class is a special case of Jacobi polynomial with a=1/2 and b=1/2
//however http://mathworld.wolfram.com/OrthogonalPolynomials.html states that T1(X) = 2X
//whereas by applying the Jacobi formula one founds T1(X) = 3X/2

//we could also do the same for many other OrthogonalPolynomial subclasses that are special cases of JacobiPolynomials
//although performance might be a key issue here
public final class SecondKindChebyshevDoublePolynomialFactory extends JacobiDoublePolynomialFactory {

    public SecondKindChebyshevDoublePolynomialFactory() {
        super(1 / 2, 1 / 2);
    }

}
