/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.analysis.series;

import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.numbers.integers.Natural;

/**
 * Factorial sequence: n! = n × (n-1) × ... × 2 × 1, with 0! = 1.
 * <p>
 * OEIS A000142: Factorial numbers.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FactorialSequence implements IntegerSequence {

    @Override
    public Integer get(Natural n) {
        int nInt = n.intValue();

        if (nInt == 0 || nInt == 1)
            return Integer.ONE;

        Integer result = Integer.ONE;
        for (int i = 2; i <= nInt; i++) {
            result = result.multiply(Integer.of(i));
        }

        return result;
    }

    @Override
    public String getOEISId() {
        return "A000142";
    }

    @Override
    public String getName() {
        return "Factorial numbers";
    }

    @Override
    public String getFormula() {
        return "n! = n × (n-1) × ... × 2 × 1, with 0! = 1";
    }

    @Override
    public String getCodomain() {
        return "ℤ⁺";
    }
}