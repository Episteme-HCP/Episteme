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

package org.jscience.mathematics.analysis.series;

import java.util.Vector;


/**
 * The prime numbers mathematical series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://www.utm.edu/research/primes/
public final class PrimeSeries implements PrimitiveSeries {
    /** DOCUMENT ME! */
    private static final long MAX = 500000;

    /** DOCUMENT ME! */
    private static Vector trialDivisors = null;

    static {
        int sqrtn = (int) Math.ceil(Math.sqrt(MAX));
        trialDivisors = getPrimes(sqrtn);
        trialDivisors.addElement(new Long(sqrtn));
    }

/**
     * Creates a new PrimeSeries object.
     */
    public PrimeSeries() {
        //as found http://www.merriampark.com/factor.htm
        //see http://www.rsok.com/~jrm/printprimes.html for something better
        //and http://alumnus.caltech.edu/~chamness/prime.html
        // The trial divisors include all primes <= sqrt (n).
        // The trial divisors must also contain at least one
        // value such that d(k) >= sqrt (n).
    }

    //returns the nth prime number, n>0
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        if (n > 0) {
            return ((Long) trialDivisors.elementAt(n)).doubleValue();
        } else {
            throw new IllegalArgumentException("n must be greater than 0.");
        }
    }

    //this is not a probable prime: we actually run through all numbers to check if it is a prime or not and this may take quite some time
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isPrime(int n) {
        return trialDivisors.contains(new Long(n));
    }

    //------------------
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getPrimeFactors(long n) {
        long nDivisions = 0;

        if (n > 0) {
            if (n > MAX) {
                String s = n + " > " + MAX;
                throw new IllegalArgumentException(s);
            }

            Vector factors = new Vector();
            nDivisions = 0;

            // Step 1
            int k = 0;
            long divisor;
            Long divisorObj;
            long quotient;
            long remainder;

            // Step 2
            while (n > 1) {
                // Step 3
                divisorObj = (Long) trialDivisors.elementAt(k);
                divisor = divisorObj.longValue();
                quotient = (long) Math.floor(n / divisor);
                remainder = n % divisor;
                nDivisions++;

                // Step 4
                if (remainder == 0) {
                    // Step 5
                    factors.addElement(new Long(divisor));
                    n = quotient;
                } else {
                    // Step 6
                    if (quotient > divisor) {
                        k++;
                    } else {
                        // Step 7
                        factors.addElement(new Long(n));

                        break;
                    }
                }
            }

            return factors;
        } else {
            throw new IllegalArgumentException("n must be greater than 0.");
        }
    }

    //n>0
    /**
     * DOCUMENT ME!
     *
     * @param upTo DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Vector getPrimes(int upTo) {
        if (upTo > 0) {
            int size = upTo + 1;
            boolean[] flags = new boolean[size];
            Vector primes = new Vector();
            double limit = Math.sqrt(size);

            // Set flags
            for (int i = 2; i < size; i++) {
                flags[i] = true;
            }

            // Cross out multiples of 2
            int j = 2;

            for (int i = j + j; i < size; i = i + j) {
                flags[i] = false;
            }

            // Cross out multiples of odd numbers
            for (j = 3; j <= limit; j = j + 2) {
                if (flags[j]) {
                    for (int i = j + j; i < size; i = i + j) {
                        flags[i] = false;
                    }
                }
            }

            // Build list of primes from what is left
            for (int i = 2; i < size; i++) {
                if (flags[i]) {
                    primes.addElement(new Long(i));
                }
            }

            return primes;
        } else {
            throw new IllegalArgumentException("n must be greater than 0.");
        }
    }

    //is DOES diverge
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConvergent() {
        return false;
    }

    //tries to compute the value at infinitum
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return Double.POSITIVE_INFINITY;
    }
}
