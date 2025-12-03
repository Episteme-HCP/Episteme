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
package org.jscience.mathematics.analysis.series;

import org.jscience.mathematics.number.Integer;
import org.jscience.mathematics.number.Natural;
import java.math.BigInteger;

/**
 * Bell numbers sequence (OEIS A000110).
 * <p>
 * The Bell number B(n) counts the number of partitions of a set with n
 * elements.
 * For example:
 * <ul>
 * <li>B(0) = 1: {{}} (empty set has 1 partition)</li>
 * <li>B(1) = 1: {{1}}</li>
 * <li>B(2) = 2: {{1,2}}, {{1},{2}}</li>
 * <li>B(3) = 5: {{1,2,3}}, {{1,2},{3}}, {{1,3},{2}}, {{2,3},{1}},
 * {{1},{2},{3}}</li>
 * </ul>
 * </p>
 * 
 * <h2>Recurrence Relations</h2>
 * <p>
 * Bell numbers satisfy several recurrence relations:
 * 
 * <pre>
 * B(n+1) = Σ(k=0 to n) C(n,k) * B(k)
 * </pre>
 * 
 * where C(n,k) is the binomial coefficient "n choose k".
 * </p>
 * 
 * <p>
 * They can also be computed using the Bell triangle (Aitken's array):
 * 
 * <pre>
 * 1
 * 1  2
 * 2  3  5
 * 5  7  10  15
 * 15 20 27  37  52
 * </pre>
 * 
 * Each row starts with the last element of the previous row, and each element
 * is the sum of the element to its left and the element above-left.
 * </p>
 * 
 * <h2>Properties</h2>
 * <ul>
 * <li>Exponential generating function: exp(e^x - 1)</li>
 * <li>Asymptotic behavior: B(n) ~ (n/log n)^n / e^(n/log n - n)</li>
 * <li>Growth rate: faster than exponential</li>
 * </ul>
 * 
 * <h2>OEIS Reference</h2>
 * <a href="https://oeis.org/A000110">A000110</a>
 * 
 * <h2>References</h2>
 * <ul>
 * <li>Bell, E. T. (1934). "Exponential polynomials"</li>
 * <li>Comtet, L. (1974). "Advanced Combinatorics"</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BellSequence implements IntegerSequence {

    // Cache for computed Bell numbers
    private final java.util.Map<java.lang.Integer, BigInteger> cache = new java.util.concurrent.ConcurrentHashMap<>();

    public BellSequence() {
        // Initialize first few values
        cache.put(0, BigInteger.ONE);
        cache.put(1, BigInteger.ONE);
        cache.put(2, BigInteger.valueOf(2));
    }

    @Override
    public Integer get(Natural n) {
        int index = n.intValue();
        if (index < 0) {
            throw new IllegalArgumentException("Bell numbers are only defined for non-negative integers");
        }

        return Integer.of(computeBell(index));
    }

    /**
     * Computes the nth Bell number using the Bell triangle method.
     * This is more efficient than the recurrence relation for large n.
     * 
     * @param n the index
     * @return B(n)
     */
    private BigInteger computeBell(int n) {
        if (cache.containsKey(n)) {
            return cache.get(n);
        }

        // Build Bell triangle up to row n
        BigInteger[][] triangle = new BigInteger[n + 1][];
        triangle[0] = new BigInteger[] { BigInteger.ONE };

        for (int i = 1; i <= n; i++) {
            triangle[i] = new BigInteger[i + 1];
            // First element is last element of previous row
            triangle[i][0] = triangle[i - 1][i - 1];

            // Each subsequent element is sum of left and above-left
            for (int j = 1; j <= i; j++) {
                triangle[i][j] = triangle[i][j - 1].add(triangle[i - 1][j - 1]);
            }

            // Cache the Bell number (first element of next row)
            cache.put(i, triangle[i][0]);
        }

        return triangle[n][0];
    }

    /**
     * Computes Bell number using the explicit summation formula.
     * Less efficient but useful for verification.
     * 
     * @param n the index
     * @return B(n)
     */
    public BigInteger computeBellExplicit(int n) {
        if (n == 0)
            return BigInteger.ONE;

        BigInteger sum = BigInteger.ZERO;
        for (int k = 0; k <= n; k++) {
            BigInteger binomial = binomialCoefficient(n, k);
            BigInteger bellK = computeBell(k);
            sum = sum.add(binomial.multiply(bellK));
        }
        return sum;
    }

    /**
     * Computes binomial coefficient C(n, k).
     */
    private BigInteger binomialCoefficient(int n, int k) {
        if (k > n || k < 0)
            return BigInteger.ZERO;
        if (k == 0 || k == n)
            return BigInteger.ONE;

        // Use C(n,k) = C(n,n-k) to minimize computation
        if (k > n - k) {
            k = n - k;
        }

        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < k; i++) {
            result = result.multiply(BigInteger.valueOf(n - i));
            result = result.divide(BigInteger.valueOf(i + 1));
        }
        return result;
    }

    @Override
    public String getOEISId() {
        return "A000110";
    }

    /**
     * Returns the first n Bell numbers.
     * 
     * @param count number of terms
     * @return array of Bell numbers [B(0), B(1), ..., B(n-1)]
     */
    public BigInteger[] getFirstN(int count) {
        BigInteger[] result = new BigInteger[count];
        for (int i = 0; i < count; i++) {
            result[i] = computeBell(i);
        }
        return result;
    }

    @Override
    public String toString() {
        return "BellSequence(A000110): 1, 1, 2, 5, 15, 52, 203, 877, 4140, ...";
    }
}

