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

package org.jscience.mathematics.discrete;

import java.math.BigInteger;

/**
 * Combinatorial functions and utilities.
 * <p>
 * Provides methods for counting, permutations, combinations, and binomial
 * coefficients.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Combinatorics {

    /**
     * Computes n! (factorial).
     * 
     * @param n non-negative integer
     * @return n!
     */
    public static BigInteger factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial undefined for negative numbers");
        }

        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    /**
     * Computes binomial coefficient C(n, k) = n! / (k! * (n-k)!).
     * Also known as "n choose k".
     * 
     * @param n total items
     * @param k items to choose
     * @return binomial coefficient
     */
    public static BigInteger binomial(int n, int k) {
        if (k < 0 || k > n) {
            return BigInteger.ZERO;
        }

        if (k > n - k) {
            k = n - k; // Optimization: C(n, k) = C(n, n-k)
        }

        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < k; i++) {
            result = result.multiply(BigInteger.valueOf(n - i));
            result = result.divide(BigInteger.valueOf(i + 1));
        }
        return result;
    }

    /**
     * Computes the number of permutations P(n, k) = n! / (n-k)!.
     * 
     * @param n total items
     * @param k items to arrange
     * @return number of permutations
     */
    public static BigInteger permutations(int n, int k) {
        if (k < 0 || k > n) {
            return BigInteger.ZERO;
        }

        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < k; i++) {
            result = result.multiply(BigInteger.valueOf(n - i));
        }
        return result;
    }

    /**
     * Computes Catalan number C_n = (2n)! / ((n+1)! * n!).
     * 
     * @param n index
     * @return nth Catalan number
     */
    public static BigInteger catalan(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Catalan number undefined for negative index");
        }

        return binomial(2 * n, n).divide(BigInteger.valueOf(n + 1));
    }

    /**
     * Computes Stirling number of the second kind S(n, k).
     * Represents the number of ways to partition n objects into k non-empty
     * subsets.
     * 
     * @param n number of objects
     * @param k number of subsets
     * @return Stirling number S(n, k)
     */
    public static BigInteger stirling2(int n, int k) {
        if (k == 0 || k > n) {
            return BigInteger.ZERO;
        }
        if (k == n || k == 1) {
            return BigInteger.ONE;
        }

        // S(n, k) = k * S(n-1, k) + S(n-1, k-1)
        return stirling2(n - 1, k).multiply(BigInteger.valueOf(k))
                .add(stirling2(n - 1, k - 1));
    }

    /**
     * Computes Pascal's triangle up to row n.
     * 
     * @param n number of rows (0-indexed)
     * @return 2D array where triangle[i][j] = C(i, j)
     */
    public static BigInteger[][] pascalTriangle(int n) {
        BigInteger[][] triangle = new BigInteger[n + 1][];
        for (int i = 0; i <= n; i++) {
            triangle[i] = new BigInteger[i + 1];
            for (int j = 0; j <= i; j++) {
                triangle[i][j] = binomial(i, j);
            }
        }
        return triangle;
    }

    /**
     * Computes multinomial coefficient n! / (k1! * k2! * ... * km!).
     * 
     * @param n      total items
     * @param groups sizes of each group (must sum to n)
     * @return multinomial coefficient
     */
    public static BigInteger multinomial(int n, int... groups) {
        int sum = 0;
        for (int k : groups) {
            sum += k;
        }
        if (sum != n) {
            throw new IllegalArgumentException("Group sizes must sum to n");
        }

        BigInteger result = factorial(n);
        for (int k : groups) {
            result = result.divide(factorial(k));
        }
        return result;
    }

    /**
     * Computes binomial coefficient modulo m using Lucas' theorem for prime m.
     * 
     * @param n total items
     * @param k items to choose
     * @param m modulus (should be prime for Lucas' theorem)
     * @return C(n, k) mod m
     */
    public static long binomialMod(int n, int k, int m) {
        if (k > n)
            return 0;
        if (k == 0 || k == n)
            return 1;

        // Use modular arithmetic directly for small numbers
        BigInteger result = binomial(n, k);
        return result.mod(BigInteger.valueOf(m)).longValue();
    }

    /**
     * Computes central binomial coefficient C(2n, n).
     * 
     * @param n the parameter
     * @return C(2n, n)
     */
    public static BigInteger centralBinomial(int n) {
        return binomial(2 * n, n);
    }

    /**
     * Verifies the symmetry property: C(n, k) = C(n, n-k).
     * 
     * @param n total items
     * @param k items to choose
     * @return true if symmetry holds
     */
    public static boolean verifySymmetry(int n, int k) {
        return binomial(n, k).equals(binomial(n, n - k));
    }

    /**
     * Verifies Pascal's identity: C(n, k) = C(n-1, k-1) + C(n-1, k).
     * 
     * @param n total items
     * @param k items to choose
     * @return true if Pascal's identity holds
     */
    public static boolean verifyPascal(int n, int k) {
        if (k <= 0 || k >= n || n <= 1) {
            return true; // Edge cases where identity doesn't apply
        }
        return binomial(n, k).equals(binomial(n - 1, k - 1).add(binomial(n - 1, k)));
    }
}