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

package org.jscience.social.sports;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Provides mathematical models for sports performance indicators across 
 * endurance and strength disciplines.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class PerformanceModels {

    private PerformanceModels() {}

    /** Aerobic capacity (VO2 Max) estimation algorithms. */
    public static final class VO2Max {
        
        /** Cooper Test: VO2Max = (distanceMeters - 504.9) / 44.73 */
        public static Real fromCooperTest(double distanceMeters) {
            return Real.of((distanceMeters - 504.9) / 44.73);
        }

        /** Uth-SÃ¸rensen-Overgaard-Pedersen: VO2Max = 15.3 * (HRmax / HRrest) */
        public static Real fromHeartRates(int hrMax, int hrRest) {
            if (hrRest <= 0) return Real.ZERO;
            return Real.of(15.3 * ((double) hrMax / hrRest));
        }

        /** Rockport Walk Test: Estimating VO2Max for walking. */
        public static Real rockportWalk(double weightLb, int age, boolean male, 
                double timeMinutes, int finalHR) {
            double gender = male ? 1.0 : 0.0;
            double vo2 = 132.853 - (0.0769 * weightLb) - (0.3877 * age) + 
                         (6.315 * gender) - (3.2649 * timeMinutes) - (0.1565 * finalHR);
            return Real.of(vo2);
        }
    }

    /** Critical Power (CP) and Anaerobic Work Capacity (W') modeling. */
    public static final class CriticalPower {
        
        /** Calculates CP and W' from two distinct exhaustive efforts. */
        public static Real[] solveFromTwoPoints(double[] effort1, double[] effort2) {
            double t1 = effort1[0], p1 = effort1[1];
            double t2 = effort2[0], p2 = effort2[1];
            double wPrime = (p1 - p2) / (1.0/t1 - 1.0/t2);
            double cp = p1 - wPrime / t1;
            return new Real[]{Real.of(cp), Real.of(wPrime)};
        }

        /** Estimates time to exhaustion at a specific power output above CP. */
        public static Real timeToExhaustion(double power, double cp, double wPrime) {
            if (power <= cp) return Real.of(Double.POSITIVE_INFINITY);
            return Real.of(wPrime / (power - cp));
        }
    }

    /** Resistance training and strength metrics. */
    public static final class Strength {
        
        /** Epley Formula for 1-Rep Max estimation. */
        public static Real epley1RM(double weight, int reps) {
            if (reps <= 1) return Real.of(weight);
            return Real.of(weight * (1.0 + reps / 30.0));
        }

        /** Brzycki Formula for 1-Rep Max estimation. */
        public static Real brzycki1RM(double weight, int reps) {
            if (reps <= 1) return Real.of(weight);
            return Real.of(weight / (1.0278 - 0.0278 * reps));
        }
    }
}

