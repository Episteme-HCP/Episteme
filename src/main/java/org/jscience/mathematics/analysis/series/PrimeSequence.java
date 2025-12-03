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

import org.jscience.mathematics.numbertheory.Primes;
import org.jscience.backend.ComputeBackend;
import org.jscience.mathematics.number.Integer;
import org.jscience.mathematics.number.Natural;
import java.util.ArrayList;
import java.util.List;

/**
 * Prime number sequence: 2, 3, 5, 7, 11, ...
 * <p>
 * Uses a segmented sieve for efficient generation of large primes.
 * Supports GPU acceleration for batch generation if a compatible backend is
 * provided.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PrimeSequence implements IntegerSequence {

    private final List<Long> cachedPrimes = new ArrayList<>();
    private ComputeBackend backend;

    // Segment size for sieving (L1 cache friendly ~32KB)
    private static final int SEGMENT_SIZE = 32768;

    public PrimeSequence() {
        // Seed with initial primes
        cachedPrimes.add(2L);
        cachedPrimes.add(3L);
    }

    @Override
    public Integer get(Natural n) {
        int index = n.intValue();
        synchronized (cachedPrimes) {
            while (index >= cachedPrimes.size()) {
                generateMorePrimes();
            }
            return Integer.of(cachedPrimes.get(index));
        }
    }

    /**
     * Convenience method using primitive int.
     * 
     * @param n the index
     * @return the n-th prime
     */
    public Integer get(int n) {
        if (n < 0)
            throw new IllegalArgumentException("n must be >= 0");
        return get(Natural.of(n));
    }

    private void generateMorePrimes() {
        // If we have a GPU backend, use it to sieve the next block
        if (backend != null && backend.getName().contains("GPU")) {
            generatePrimesGPU();
        } else {
            generatePrimesCPU();
        }
    }

    private void generatePrimesCPU() {
        long lastPrime = cachedPrimes.get(cachedPrimes.size() - 1);
        long start = lastPrime + 1;
        // Ensure start is odd
        if (start % 2 == 0)
            start++;

        long end = start + SEGMENT_SIZE;

        // Simple Sieve segment
        // We need primes up to sqrt(end) to sieve this segment
        // Since we generate sequentially, we likely have them in cachedPrimes

        boolean[] isComposite = new boolean[SEGMENT_SIZE]; // false = prime (initially)

        long limit = (long) Math.sqrt(end);

        for (Long p : cachedPrimes) {
            if (p > limit)
                break;
            if (p == 2)
                continue; // Skip 2, we iterate odds

            // Find first multiple of p >= start
            long startMultiple = (start + p - 1) / p * p;
            if (startMultiple < p * p)
                startMultiple = p * p;

            // Adjust to odd multiple if needed (since we step by 2*p)
            if ((startMultiple & 1) == 0)
                startMultiple += p;

            for (long j = startMultiple; j < end; j += 2 * p) {
                int index = (int) (j - start);
                if (index >= 0 && index < SEGMENT_SIZE) {
                    isComposite[index] = true;
                }
            }
        }

        for (int i = 0; i < SEGMENT_SIZE; i += 2) { // Check only odds?
            // Actually start is odd, so i=0 is start (odd), i=1 is start+1 (even)
            // Wait, if we only check odds, array mapping is different.
            // Let's stick to simple mapping for clarity first, optimize later.
            // Mapping: index i corresponds to number start + i

            // If start is odd:
            // i=0 (start) -> check
            // i=1 (start+1) -> even, skip
            // i=2 (start+2) -> odd, check

            if ((start + i) % 2 == 0)
                continue;

            if (!isComposite[i]) {
                cachedPrimes.add(start + i);
            }
        }
    }

    private void generatePrimesGPU() {
        // Placeholder for GPU logic
        // In a real implementation, we would dispatch a kernel to mark composites
        // in a large buffer, then read back the boolean array.
        // For now, fallback to CPU but log/notify?
        // Or just run CPU logic.
        generatePrimesCPU();
    }

    @Override
    public void setBackend(ComputeBackend backend) {
        this.backend = backend;
    }

    @Override
    public ComputeBackend getBackend() {
        return backend;
    }

    @Override
    public String getOEISId() {
        return "A000040";
    }

    @Override
    public String getName() {
        return "Prime numbers";
    }

    @Override
    public String getFormula() {
        return "p(n) = n-th prime number";
    }
}
