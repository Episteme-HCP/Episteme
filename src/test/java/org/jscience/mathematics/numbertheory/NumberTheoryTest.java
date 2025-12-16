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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.numbertheory;

import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for NumberTheory and PrimeSieve.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class NumberTheoryTest {

    @Test
    public void testBailliePSW() {
        // Known primes
        assertTrue(NumberTheory.isBailliePSW(BigInteger.valueOf(2)));
        assertTrue(NumberTheory.isBailliePSW(BigInteger.valueOf(3)));
        assertTrue(NumberTheory.isBailliePSW(BigInteger.valueOf(17)));
        assertTrue(NumberTheory.isBailliePSW(new BigInteger("104729"))); // 10,000th prime
        assertTrue(NumberTheory.isBailliePSW(new BigInteger("179424673"))); // Large prime

        // Known composites
        assertFalse(NumberTheory.isBailliePSW(BigInteger.valueOf(4)));
        assertFalse(NumberTheory.isBailliePSW(BigInteger.valueOf(15)));
        assertFalse(NumberTheory.isBailliePSW(BigInteger.valueOf(100)));
        assertFalse(NumberTheory.isBailliePSW(new BigInteger("104728")));

        // Carmichael numbers (pass Fermat, fail Miller-Rabin/Baillie-PSW)
        assertFalse(NumberTheory.isBailliePSW(BigInteger.valueOf(561)));
        assertFalse(NumberTheory.isBailliePSW(BigInteger.valueOf(1105)));
        assertFalse(NumberTheory.isBailliePSW(BigInteger.valueOf(1729)));
    }

    @Test
    public void testAKS() {
        // AKS is slow, so we test with smaller numbers
        assertTrue(NumberTheory.isAKS(BigInteger.valueOf(17)));
        assertTrue(NumberTheory.isAKS(BigInteger.valueOf(101)));

        assertFalse(NumberTheory.isAKS(BigInteger.valueOf(15)));
        assertFalse(NumberTheory.isAKS(BigInteger.valueOf(100)));
    }

    @Test
    public void testSegmentedSieve() {
        List<Long> primes = PrimeSieve.segmentedSieve(100);
        assertEquals(25, primes.size()); // 25 primes under 100
        assertEquals(2, primes.get(0));
        assertEquals(97, primes.get(primes.size() - 1));

        // Check a larger range
        List<Long> primes1000 = PrimeSieve.segmentedSieve(1000);
        assertEquals(168, primes1000.size()); // 168 primes under 1000
    }
}
