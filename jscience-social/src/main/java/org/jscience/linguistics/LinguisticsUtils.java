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

package org.jscience.linguistics;

/**
 * Utility class providing mathematical models used in linguistics,
 * such as Zipf's law for word frequency distribution.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class LinguisticsUtils {

    private LinguisticsUtils() {}

    /**
     * Calculates Zipf's law probability.
     * P(k) = 1 / (k^s * H(n, s)) where H is the generalized harmonic number.
     *
     * @param k rank of the word
     * @param s value of the exponent characterizing the distribution
     * @param n total number of elements
     * @return the probability of the k-th element
     * @see <a href="http://en.wikipedia.org/wiki/Zipf%27s_law">Zipf's Law</a>
     */
    public static double calculateZipf(double k, double s, double n) {
        return 1.0 / (Math.pow(k, s) * harmonicNumber(n, s));
    }

    /**
     * Calculates the generalized harmonic number.
     * H(n, s) = sum_{i=1}^n (1 / i^s)
     */
    private static double harmonicNumber(double n, double s) {
        double result = 0;
        for (int i = 1; i <= n; i++) {
            result += 1.0 / Math.pow(i, s);
        }
        return result;
    }
}
