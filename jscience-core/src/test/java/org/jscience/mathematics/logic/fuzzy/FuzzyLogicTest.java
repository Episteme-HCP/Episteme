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

package org.jscience.mathematics.logic.fuzzy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FuzzyLogicTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testMinimumTNorm() {
        assertEquals(0.3, TNorm.MINIMUM.apply(0.3, 0.7), EPSILON);
        assertEquals(0.5, TNorm.MINIMUM.apply(0.5, 0.8), EPSILON);
    }

    @Test
    public void testProductTNorm() {
        assertEquals(0.21, TNorm.PRODUCT.apply(0.3, 0.7), EPSILON);
        assertEquals(0.4, TNorm.PRODUCT.apply(0.5, 0.8), EPSILON);
    }

    @Test
    public void testLukasiewiczTNorm() {
        assertEquals(0.0, TNorm.LUKASIEWICZ.apply(0.3, 0.7), EPSILON);
        assertEquals(0.3, TNorm.LUKASIEWICZ.apply(0.5, 0.8), EPSILON);
    }

    @Test
    public void testDrasticTNorm() {
        assertEquals(0.3, TNorm.DRASTIC.apply(0.3, 1.0), EPSILON);
        assertEquals(0.0, TNorm.DRASTIC.apply(0.3, 0.7), EPSILON);
    }

    @Test
    public void testTConorm() {
        // S(a,b) = 1 - T(1-a, 1-b)
        double result = TNorm.MINIMUM.applyConorm(0.3, 0.7);
        assertEquals(0.7, result, EPSILON);
    }

    @Test
    public void testTNormProperties() {
        // Commutativity
        assertEquals(TNorm.MINIMUM.apply(0.3, 0.7), TNorm.MINIMUM.apply(0.7, 0.3), EPSILON);

        // Identity
        assertEquals(0.5, TNorm.MINIMUM.apply(0.5, 1.0), EPSILON);
    }
}