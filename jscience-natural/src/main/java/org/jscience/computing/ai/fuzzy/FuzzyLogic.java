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

package org.jscience.computing.ai.fuzzy;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Basic fuzzy logic engine.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FuzzyLogic {

    private FuzzyLogic() {
    }

    /**
     * Triangular membership function.
     */
    public static Real triangular(Real x, Real a, Real b, Real c) {
        if (x.compareTo(a) <= 0 || x.compareTo(c) >= 0)
            return Real.ZERO;
        if (x.equals(b))
            return Real.ONE;
        if (x.compareTo(b) < 0)
            return x.subtract(a).divide(b.subtract(a));
        return c.subtract(x).divide(c.subtract(b));
    }

    /**
     * Trapezoidal membership function.
     */
    public static Real trapezoidal(Real x, Real a, Real b, Real c, Real d) {
        if (x.compareTo(a) <= 0 || x.compareTo(d) >= 0)
            return Real.ZERO;
        if (x.compareTo(b) >= 0 && x.compareTo(c) <= 0)
            return Real.ONE;
        if (x.compareTo(b) < 0)
            return x.subtract(a).divide(b.subtract(a));
        return d.subtract(x).divide(d.subtract(c));
    }

    /**
     * Gaussian membership function.
     * ÃŽÂ¼(x) = exp(-(x-c)Ã‚Â²/(2ÃÆ’Ã‚Â²))
     */
    public static Real gaussian(Real x, Real center, Real sigma) {
        Real diff = x.subtract(center);
        return diff.pow(2).negate().divide(Real.TWO.multiply(sigma.pow(2))).exp();
    }

    /** Fuzzy AND (T-norm: minimum). */
    public static Real and(Real a, Real b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    /** Fuzzy OR (S-norm: maximum). */
    public static Real or(Real a, Real b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    /** Fuzzy NOT (complement). */
    public static Real not(Real a) {
        return Real.ONE.subtract(a);
    }

    /** Product T-norm. */
    public static Real productAnd(Real a, Real b) {
        return a.multiply(b);
    }

    /** Probabilistic OR (S-norm). */
    public static Real probabilisticOr(Real a, Real b) {
        return a.add(b).subtract(a.multiply(b));
    }

    /**
     * Centroid defuzzification (for discrete output).
     */
    public static Real centroidDefuzzification(Real[] values, Real[] memberships) {
        Real numerator = Real.ZERO;
        Real denominator = Real.ZERO;
        for (int i = 0; i < values.length; i++) {
            numerator = numerator.add(values[i].multiply(memberships[i]));
            denominator = denominator.add(memberships[i]);
        }
        return denominator.isZero() ? Real.ZERO : numerator.divide(denominator);
    }
}


