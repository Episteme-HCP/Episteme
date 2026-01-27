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

package org.jscience.biology.ecology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models ecosystem food webs and energy flow.
 */
public final class EcosystemFoodWeb {

    private EcosystemFoodWeb() {}

    /**
     * Lindeman's Efficiency (10% rule).
     * Energy at Nth level = Initial * 0.1^(N-1)
     */
    public static Real energyAtTrophicLevel(double initialEnergy, int level) {
        return Real.of(initialEnergy * Math.pow(0.1, level - 1));
    }

    /**
     * Biodiversity Index (Shannon-Wiener).
     * H = -Sum(pi * ln(pi))
     */
    public static Real shannonIndex(double[] speciesCounts) {
        double total = Arrays.stream(speciesCounts).sum();
        double h = 0;
        for (double c : speciesCounts) {
            double p = c / total;
            if (p > 0) h -= p * Math.log(p);
        }
        return Real.of(h);
    }
}
