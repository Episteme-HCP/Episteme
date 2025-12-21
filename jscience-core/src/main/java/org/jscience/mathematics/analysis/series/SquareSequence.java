/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * Square numbers sequence: a(n) = n².
 * <p>
 * OEIS A000290: The squares.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SquareSequence implements IntegerSequence {

    @Override
    public Integer get(Natural n) {
        Integer nInt = Integer.of(n.longValue());
        return nInt.multiply(nInt);
    }

    @Override
    public String getOEISId() {
        return "A000290";
    }

    @Override
    public String getName() {
        return "Square numbers";
    }

    @Override
    public String getFormula() {
        return "a(n) = n²";
    }

    @Override
    public String getCodomain() {
        return "ℤ⁺ ∪ {0}";
    }
}