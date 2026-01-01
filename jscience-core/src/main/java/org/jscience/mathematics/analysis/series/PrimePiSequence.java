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
import org.jscience.mathematics.numbertheory.NumberTheory;
import java.util.ArrayList;
import java.util.List;

/**
 * Prime counting function Ãâ‚¬(n) (OEIS A000720).
 * <p>
 * Ãâ‚¬(n) gives the number of primes less than or equal to n.
 * For example:
 * <ul>
 * <li>Ãâ‚¬(1) = 0: no primes Ã¢â€°Â¤ 1</li>
 * <li>Ãâ‚¬(2) = 1: {2}</li>
 * <li>Ãâ‚¬(10) = 4: {2, 3, 5, 7}</li>
 * <li>Ãâ‚¬(100) = 25</li>
 * <li>Ãâ‚¬(1000) = 168</li>
 * </ul>
 * </p>
 *
 * <h2>Asymptotic Behavior</h2>
 * <p>
 * The Prime Number Theorem states that:
 *
 * <pre>
 * Ãâ‚¬(n) ~ n / ln(n)
 * </pre>
 *
 * More precisely, Legendre's approximation:
 *
 * <pre>
 * Ãâ‚¬(n) Ã¢â€°Ë† n / (ln(n) - 1.08366)
 * </pre>
 * </p>
 *
 * <h2>Implementation</h2>
 * <p>
 * This implementation uses:
 * <ul>
 * <li>Sieve of Eratosthenes for small n (< 10^6)</li>
 * <li>Miller-Rabin primality test for large n</li>
 * <li>Caching for performance</li>
 * </ul>
 * </p>
 *
 * <h2>OEIS Reference</h2>
 * <a href="https://oeis.org/A000720">A000720</a>
 *
 * <h2>References</h2>
 * <ul>
 * <li>Gauss, C. F. (1849). Letter to Encke</li>
 * <li>Hadamard, J. & de la VallÃƒÂ©e Poussin, C. (1896). "Prime Number
 * Theorem"</li>
 * <li>Riemann, B. (1859). "On the Number of Primes Less Than a Given
 * Magnitude"</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PrimePiSequence implements IntegerSequence {

    private static final int SIEVE_THRESHOLD = 1_000_000;

    // Cache for computed values
    private final java.util.Map<Long, java.lang.Integer> cache = new java.util.concurrent.ConcurrentHashMap<>();

    public PrimePiSequence() {
        // Initialize small values
        cache.put(0L, 0);
        cache.put(1L, 0);
        cache.put(2L, 1);
    }

    @Override
    public Integer get(Natural n) {
        long value = n.longValue();
        if (value < 0) {
            throw new IllegalArgumentException("Ãâ‚¬(n) is only defined for non-negative n");
        }

        return Integer.of(countPrimes(value));
    }

    /**
     * Counts the number of primes Ã¢â€°Â¤ n.
     * 
     * @param n the upper bound
     * @return Ãâ‚¬(n)
     */
    public int countPrimes(long n) {
        if (cache.containsKey(n)) {
            return cache.get(n);
        }

        int count;
        if (n < SIEVE_THRESHOLD) {
            count = countPrimesWithSieve((int) n);
        } else {
            count = countPrimesWithTest(n);
        }

        cache.put(n, count);
        return count;
    }

    /**
     * Uses Sieve of Eratosthenes for small n.
     * Efficient for n < 10^6.
     */
    private int countPrimesWithSieve(int n) {
        if (n < 2)
            return 0;

        boolean[] isPrime = new boolean[n + 1];
        java.util.Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;

        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime[i])
                count++;
        }
        return count;
    }

    /**
     * Uses primality testing for large n.
     * Less efficient but works for arbitrarily large n.
     */
    private int countPrimesWithTest(long n) {
        if (n < 2)
            return 0;

        int count = 1; // Count 2

        // Only check odd numbers
        for (long i = 3; i <= n; i += 2) {
            if (isPrime(i)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Primality test using Miller-Rabin (via NumberTheory if available).
     */
    private boolean isPrime(long n) {
        if (n < 2)
            return false;
        if (n == 2)
            return true;
        if (n % 2 == 0)
            return false;
        if (n < 9)
            return true;
        if (n % 3 == 0)
            return false;

        // Try small primes first
        int[] smallPrimes = { 5, 7, 11, 13, 17, 19, 23, 29, 31 };
        for (int p : smallPrimes) {
            if (n == p)
                return true;
            if (n % p == 0)
                return false;
        }

        // Miller-Rabin for larger numbers
        try {
            return NumberTheory.isProbablePrime(Natural.of(n), 10);
        } catch (Exception e) {
            // Fallback to basic trial division
            return trialDivision(n);
        }
    }

    /**
     * Basic trial division primality test.
     */
    private boolean trialDivision(long n) {
        if (n < 2)
            return false;
        if (n == 2 || n == 3)
            return true;
        if (n % 2 == 0 || n % 3 == 0)
            return false;

        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Approximates Ãâ‚¬(n) using the Prime Number Theorem.
     * 
     * @param n the value
     * @return approximation of Ãâ‚¬(n)
     */
    public double approximatePi(long n) {
        if (n < 2)
            return 0.0;

        double logN = Math.log(n);

        // Legendre's improvement
        return n / (logN - 1.08366);
    }

    /**
     * Returns all primes up to n.
     * 
     * @param n the upper bound
     * @return list of primes Ã¢â€°Â¤ n
     */
    public List<Long> getPrimesUpTo(long n) {
        List<Long> primes = new ArrayList<>();

        if (n < 2)
            return primes;

        if (n < SIEVE_THRESHOLD) {
            boolean[] isPrime = new boolean[(int) n + 1];
            java.util.Arrays.fill(isPrime, true);
            isPrime[0] = isPrime[1] = false;

            for (int i = 2; i * i <= n; i++) {
                if (isPrime[i]) {
                    for (int j = i * i; j <= n; j += i) {
                        isPrime[j] = false;
                    }
                }
            }

            for (int i = 2; i <= n; i++) {
                if (isPrime[i])
                    primes.add((long) i);
            }
        } else {
            // For large n, use primality testing
            primes.add(2L);
            for (long i = 3; i <= n; i += 2) {
                if (isPrime(i)) {
                    primes.add(i);
                }
            }
        }

        return primes;
    }

    @Override
    public String getOEISId() {
        return "A000720";
    }

    /**
     * Returns Ãâ‚¬(n) for the first n values.
     * 
     * @param count number of terms
     * @return array [Ãâ‚¬(0), Ãâ‚¬(1), ..., Ãâ‚¬(count-1)]
     */
    public int[] getFirstN(int count) {
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = countPrimes(i);
        }
        return result;
    }

    @Override
    public String toString() {
        return "PrimePiSequence(A000720): 0, 0, 1, 2, 2, 3, 3, 4, 4, 4, 4, 5, ...";
    }
}

