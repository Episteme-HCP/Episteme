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

package org.jscience.biology.genetics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Population genetics models.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PopulationGenetics {

    /** Hardy-Weinberg genotype frequencies: [p², 2pq, q²] */
    public static Real[] hardyWeinbergFrequencies(Real p) {
        if (p.compareTo(Real.ZERO) < 0 || p.compareTo(Real.ONE) > 0)
            throw new IllegalArgumentException("Allele frequency p must be between 0 and 1");
        Real q = Real.ONE.subtract(p);
        return new Real[] { p.pow(2), Real.TWO.multiply(p).multiply(q), q.pow(2) };
    }

    /** Estimate allele frequency: p = (2*AA + Aa) / (2*N) */
    public static Real estimateAlleleFrequency(int AA, int Aa, int aa) {
        int N = AA + Aa + aa;
        if (N == 0)
            return Real.ZERO;
        return Real.of(2.0 * AA + Aa).divide(Real.of(2.0 * N));
    }

    /** Fixation index: F_ST = (H_T - H_S) / H_T */
    public static Real fixationIndex(Real heterozygosityTotal, Real heterozygositySub) {
        if (heterozygosityTotal.isZero())
            return Real.ZERO;
        return heterozygosityTotal.subtract(heterozygositySub).divide(heterozygosityTotal);
    }

    /** Wright-Fisher step (genetic drift simulation) */
    public static int wrightFisherStep(int initialCountAlleleA, int populationSize) {
        int totalGenes = 2 * populationSize;
        double p = (double) initialCountAlleleA / totalGenes;
        int nextGenCount = 0;
        for (int i = 0; i < totalGenes; i++) {
            if (Math.random() < p)
                nextGenCount++;
        }
        return nextGenCount;
    }
}
