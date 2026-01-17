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

package org.jscience.mathematics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.mathematics.numbers.integers.Natural;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Naturals;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.structures.rings.Semiring;

/**
 * Test demonstrating Exact (Natural) vs Approximate (Real) number operations.
 * Adapted from ExactVsApproximateDemo.
 */
public class ExactVsApproximateTest {

    /**
     * Generic Fibonacci algorithm.
     */
    private <E> E fibonacci(int n, Semiring<E> structure) {
        E a = structure.zero();
        E b = structure.one();

        for (int i = 0; i < n; i++) {
            E temp = structure.add(a, b);
            a = b;
            b = temp;
        }
        return b;
    }

    /**
     * Generic power function.
     */
    private <E> E power(E base, int exponent, Semiring<E> structure) {
        if (exponent == 0) {
            return structure.one();
        }
        E result = structure.one();
        for (int i = 0; i < exponent; i++) {
            result = structure.multiply(result, base);
        }
        return result;
    }

    @Test
    public void testFibonacciVerification() {
        // Exact
        Natural fibExact = fibonacci(100, Naturals.getInstance());
        assertNotNull(fibExact);
        String exactStr = fibExact.toString();
        // Fib(100) is 354224848179261915075
        // We check it starts with 354 and has correct length (21 digits)
        assertTrue(exactStr.startsWith("354"), "Exact fib(100) should start with 354");
        assertEquals(21, exactStr.length(), "Exact fib(100) should have 21 digits");

        // Approximate
        Real fibApprox = fibonacci(100, Reals.getInstance());
        assertNotNull(fibApprox);
        // Check reasonable proximity
        // Real might be scientific notation.
        assertTrue(fibApprox.doubleValue() > 1e20);
        assertTrue(fibApprox.doubleValue() < 1e22);
    }

    @Test
    public void testPowerVerification() {
        // 2^100
        Natural baseExact = Natural.of(2);
        Natural powerExact = power(baseExact, 100, Naturals.getInstance());
        assertNotNull(powerExact);
        assertTrue(powerExact.toString().length() > 30); // 2^100 is approx 1.26e30 (31 digits)
        
        Real baseApprox = Real.of(2.0);
        Real powerApprox = power(baseApprox, 100, Reals.getInstance());
        assertNotNull(powerApprox);
        // 2^100 = 1.2676506e30
        assertEquals(1.2676506002282294E30, powerApprox.doubleValue(), 1e15); // Large tolerance for high magnitude
    }

    @Test
    public void testErgonomicAPI() {
        // Exact
        Natural a = Natural.of(1000000);
        Natural b = Natural.of(500000);
        Natural sumExact = a.add(b).multiply(Natural.of(2));
        assertEquals("3000000", sumExact.toString(), "Exact calculation incorrect");

        // Approximate
        Real x = Real.of(1e6);
        Real y = Real.of(5e5);
        Real sumApprox = x.add(y).multiply(Real.of(2.0));
        assertEquals(3000000.0, sumApprox.doubleValue(), 1e-5, "Approximate calculation incorrect");
    }

    @Test
    public void testLargeNumbers() {
        // Exact square of Long.MAX_VALUE
        Natural huge = Natural.of(Long.MAX_VALUE);
        Natural hugeSquared = huge.multiply(huge);
        assertNotNull(hugeSquared);
        assertTrue(hugeSquared.toString().length() > 38);

        // Approx square
        Real approxHuge = Real.of(Long.MAX_VALUE);
        Real approxSquared = approxHuge.multiply(approxHuge);
        assertNotNull(approxSquared);
        assertTrue(Double.isFinite(approxSquared.doubleValue()));
    }
}
