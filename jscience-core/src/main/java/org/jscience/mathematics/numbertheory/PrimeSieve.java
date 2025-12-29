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

package org.jscience.mathematics.numbertheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * High-performance prime number generation using a segmented sieve.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PrimeSieve {

    private static final int SEGMENT_SIZE = 32768; // 32KB cache friendly

    /**
     * Generates all prime numbers up to the given limit using a segmented sieve.
     * 
     * @param limit the upper bound (inclusive)
     * @return list of primes
     */
    public static List<Long> segmentedSieve(long limit) {
        List<Long> primes = new ArrayList<>();
        if (limit < 2)
            return primes;

        int sqrt = (int) Math.sqrt(limit);
        List<Integer> basePrimes = simpleSieve(sqrt);

        // Add base primes to result
        for (Integer p : basePrimes) {
            primes.add(p.longValue());
        }

        // Segmented sieve
        long low = sqrt + 1;
        long high = Math.min(limit, low + SEGMENT_SIZE);
        boolean[] isPrime = new boolean[SEGMENT_SIZE + 1];

        while (low <= limit) {
            Arrays.fill(isPrime, true);

            for (Integer p : basePrimes) {
                long start = (low + p - 1) / p * p;
                if (start < p * p)
                    start = (long) p * p; // Optimization: start at p^2

                // Adjust start index relative to 'low'
                int startIndex = (int) (start - low);

                for (int j = startIndex; j < high - low + 1; j += p) {
                    if (j >= 0 && j < isPrime.length) {
                        isPrime[j] = false;
                    }
                }
            }

            for (int i = 0; i < high - low + 1; i++) {
                if (isPrime[i]) {
                    primes.add(low + i);
                }
            }

            low += SEGMENT_SIZE;
            high += SEGMENT_SIZE;
            if (high > limit)
                high = limit;
        }

        return primes;
    }

    private static List<Integer> simpleSieve(int limit) {
        List<Integer> primes = new ArrayList<>();
        boolean[] isPrime = new boolean[limit + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;

        for (int p = 2; p * p <= limit; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i <= limit; i += p) {
                    isPrime[i] = false;
                }
            }
        }

        for (int p = 2; p <= limit; p++) {
            if (isPrime[p]) {
                primes.add(p);
            }
        }
        return primes;
    }
}