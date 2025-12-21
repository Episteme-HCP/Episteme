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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.biology.biochemistry;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Michaelis-Menten enzyme kinetics.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EnzymeKinetics {

    private EnzymeKinetics() {
    }

    /** Michaelis-Menten: V = Vmax * [S] / (Km + [S]) */
    public static Real velocity(Real vMax, Real km, Real substrateConc) {
        return vMax.multiply(substrateConc).divide(km.add(substrateConc));
    }

    /** Lineweaver-Burk parameters: {slope, intercept} */
    public static Real[] lineweaverBurkParams(Real vMax, Real km) {
        Real slope = km.divide(vMax);
        Real intercept = Real.ONE.divide(vMax);
        return new Real[] { slope, intercept };
    }

    /** Find Km from velocity data (half-maximal) */
    public static Real findKm(Real[] substrateConcs, Real[] velocities, Real vMax) {
        Real halfVmax = vMax.divide(Real.TWO);
        int bestIdx = 0;
        Real bestDiff = Real.of(Double.MAX_VALUE);
        for (int i = 0; i < velocities.length; i++) {
            Real diff = velocities[i].subtract(halfVmax).abs();
            if (diff.compareTo(bestDiff) < 0) {
                bestDiff = diff;
                bestIdx = i;
            }
        }
        return substrateConcs[bestIdx];
    }

    /** Competitive inhibition: V = Vmax * [S] / (Km * (1 + [I]/Ki) + [S]) */
    public static Real velocityWithCompetitiveInhibitor(Real vMax, Real km,
            Real substrateConc, Real inhibitorConc, Real ki) {
        Real apparentKm = km.multiply(Real.ONE.add(inhibitorConc.divide(ki)));
        return vMax.multiply(substrateConc).divide(apparentKm.add(substrateConc));
    }
}
