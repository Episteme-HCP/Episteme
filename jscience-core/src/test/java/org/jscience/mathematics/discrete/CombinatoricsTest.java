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
package org.jscience.mathematics.discrete;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

/**
 * Unit tests for enhanced Combinatorics class.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CombinatoricsTest {

    @Test
    public void testBinomialBasic() {
        // C(5, 2) = 10
        assertEquals(BigInteger.valueOf(10), Combinatorics.binomial(5, 2));

        // C(10, 3) = 120
        assertEquals(BigInteger.valueOf(120), Combinatorics.binomial(10, 3));

        // C(n, 0) = 1
        assertEquals(BigInteger.ONE, Combinatorics.binomial(5, 0));

        // C(n, n) = 1
        assertEquals(BigInteger.ONE, Combinatorics.binomial(7, 7));
    }

    @Test
    public void testPascalTriangle() {
        BigInteger[][] triangle = Combinatorics.pascalTriangle(4);

        // Row 0: [1]
        assertArrayEquals(new BigInteger[] { BigInteger.ONE }, triangle[0]);

        // Row 4: [1, 4, 6, 4, 1]
        assertArrayEquals(
                new BigInteger[] {
                        BigInteger.ONE,
                        BigInteger.valueOf(4),
                        BigInteger.valueOf(6),
                        BigInteger.valueOf(4),
                        BigInteger.ONE
                },
                triangle[4]);
    }

    @Test
    public void testMultinomial() {
        // Multinomial(6; 2,2,2) = 6! / (2! * 2! * 2!) = 90
        assertEquals(BigInteger.valueOf(90), Combinatorics.multinomial(6, 2, 2, 2));

        // Multinomial(10; 3,3,4) = 10! / (3! * 3! * 4!) = 4200
        assertEquals(BigInteger.valueOf(4200), Combinatorics.multinomial(10, 3, 3, 4));
    }

    @Test
    public void testBinomialMod() {
        // C(10, 3) mod 7 = 120 mod 7 = 1
        assertEquals(1, Combinatorics.binomialMod(10, 3, 7));

        // C(100, 50) mod 13
        long result = Combinatorics.binomialMod(100, 50, 13);
        assertTrue(result >= 0 && result < 13);
    }

    @Test
    public void testCentralBinomial() {
        // C(0, 0) = 1
        assertEquals(BigInteger.ONE, Combinatorics.centralBinomial(0));

        // C(2, 1) = 2
        assertEquals(BigInteger.valueOf(2), Combinatorics.centralBinomial(1));

        // C(4, 2) = 6
        assertEquals(BigInteger.valueOf(6), Combinatorics.centralBinomial(2));

        // C(6, 3) = 20
        assertEquals(BigInteger.valueOf(20), Combinatorics.centralBinomial(3));
    }

    @Test
    public void testSymmetryIdentity() {
        // C(10, 3) = C(10, 7)
        assertTrue(Combinatorics.verifySymmetry(10, 3));

        // C(20, 8) = C(20, 12)
        assertTrue(Combinatorics.verifySymmetry(20, 8));
    }

    @Test
    public void testPascalIdentity() {
        // C(5, 2) = C(4, 1) + C(4, 2)
        assertTrue(Combinatorics.verifyPascal(5, 2));

        // C(10, 5) = C(9, 4) + C(9, 5)
        assertTrue(Combinatorics.verifyPascal(10, 5));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, Combinatorics.factorial(0));
        assertEquals(BigInteger.valueOf(120), Combinatorics.factorial(5));
        assertEquals(BigInteger.valueOf(3628800), Combinatorics.factorial(10));
    }

    @Test
    public void testPermutations() {
        // P(5, 3) = 60
        assertEquals(BigInteger.valueOf(60), Combinatorics.permutations(5, 3));

        // P(10, 2) = 90
        assertEquals(BigInteger.valueOf(90), Combinatorics.permutations(10, 2));
    }

    @Test
    public void testCatalan() {
        // Catalan numbers: 1, 1, 2, 5, 14, 42, ...
        assertEquals(BigInteger.ONE, Combinatorics.catalan(0));
        assertEquals(BigInteger.ONE, Combinatorics.catalan(1));
        assertEquals(BigInteger.valueOf(2), Combinatorics.catalan(2));
        assertEquals(BigInteger.valueOf(5), Combinatorics.catalan(3));
        assertEquals(BigInteger.valueOf(14), Combinatorics.catalan(4));
    }
}