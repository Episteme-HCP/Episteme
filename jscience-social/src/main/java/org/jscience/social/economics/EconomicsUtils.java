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

package org.jscience.social.economics;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Utility functions for common economic modeling curves (Logistic, Logit, Richards).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class EconomicsUtils {

    private EconomicsUtils() {}

    /** Generalised logistic curve (Richards' curve). */
    public static Real getRichards(Real t, Real lower, Real upper,
            Real maxTime, Real growthRate, Real nearMax) {
        // lower + (upper / (1 + nearMax * exp(-growthRate * (t - maxTime)))^(1/nearMax))
        // Real doesn't typically have static Math methods, it has instance methods.
        // Assuming Real has exp(), pow().
        Real exponent = growthRate.negate().multiply(t.subtract(maxTime));
        Real denominatorInner = Real.ONE.add(nearMax.multiply(exponent.exp()));
        
        // Power: (1/nearMax)
        Real power = Real.ONE.divide(nearMax);
        
        return lower.add(upper.divide(denominatorInner.pow(power)));
    }

    /** Standard logistic curve. */
    public static Real getLogistic(Real t, Real cap, Real m, Real n, Real tau) {
        Real expTerm = t.negate().divide(tau).exp();
        // (cap * (1 + (m * expTerm))) / (1 + (n * expTerm))
        Real numerator = cap.multiply(Real.ONE.add(m.multiply(expTerm)));
        Real denominator = Real.ONE.add(n.multiply(expTerm));
        return numerator.divide(denominator);
    }

    /** Standard logit function (log-odds). */
    public static Real getLogit(Real p) {
        if (p.compareTo(Real.ZERO) <= 0 || p.compareTo(Real.ONE) >= 0) return Real.NaN;
        // ln(p / (1 - p))
        return p.divide(Real.ONE.subtract(p)).log();
    }
}

