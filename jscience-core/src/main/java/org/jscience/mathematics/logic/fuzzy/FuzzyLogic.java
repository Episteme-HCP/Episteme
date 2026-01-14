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

package org.jscience.mathematics.logic.fuzzy;

import org.jscience.mathematics.logic.Logic;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Fuzzy Logic system where truth values are Real numbers in [0, 1].
 * <p>
 * Uses Zadeh operators:
 * <ul>
 * <li>AND(a, b) = min(a, b)</li>
 * <li>OR(a, b) = max(a, b)</li>
 * <li>NOT(a) = 1 - a</li>
 * </ul>
 * </p>
 *
 * <h2>References</h2>
 * <ul>
 * <li>Lotfi A. Zadeh, "Fuzzy Sets", Information and Control,
 * Vol. 8, No. 3, 1965, pp. 338-353 (foundational paper)</li>
 * <li>George J. Klir and Bo Yuan, "Fuzzy Sets and Fuzzy Logic: Theory and
 * Applications",
 * Prentice Hall, 1995</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FuzzyLogic implements Logic<Real> {

    private static final FuzzyLogic INSTANCE = new FuzzyLogic();

    public static FuzzyLogic getInstance() {
        return INSTANCE;
    }

    private FuzzyLogic() {
    }

    @Override
    public Real trueValue() {
        return Real.ONE;
    }

    @Override
    public Real falseValue() {
        return Real.ZERO;
    }

    @Override
    public Real and(Real a, Real b) {
        return (a.compareTo(b) <= 0) ? a : b; // min(a, b)
    }

    @Override
    public Real or(Real a, Real b) {
        return (a.compareTo(b) >= 0) ? a : b; // max(a, b)
    }

    @Override
    public Real not(Real a) {
        return Real.ONE.subtract(a);
    }
}


