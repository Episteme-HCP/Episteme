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

package org.jscience.psychology;

import java.util.Arrays;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Provides mathematical models for analyzing human reaction times and motor control.
 * Includes implementations of Hick's Law, Fitts's Law, and basic decision probability models.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class ReactionTimeAnalysis {

    private ReactionTimeAnalysis() {}

    /**
     * Models decision time based on the number of choices using Hick's Law.
     * RT = a + b * log2(n + 1)
     * 
     * @param a Base reaction time (simple RT intercept)
     * @param b Processing speed coefficient (slope, time per bit)
     * @param n Number of equally probable choices
     * @return Estimated reaction time
     */
    public static Real hicksLaw(double a, double b, int n) {
        if (n < 0) return Real.of(a);
        return Real.of(a + b * (Math.log(n + 1) / Math.log(2)));
    }

    /**
     * Models movement time to a target using Fitts's Law.
     * MT = a + b * log2(2D / W)
     * 
     * @param a Intercept (start/stop time)
     * @param b Slope (1/bandwidth)
     * @param d Distance to target center
     * @param w Width of target (tolerance)
     * @return Estimated movement time
     */
    public static Real fittsLaw(double a, double b, double d, double w) {
        if (w <= 0 || d < 0) return Real.ZERO;
        return Real.of(a + b * (Math.log(2 * d / w) / Math.log(2)));
    }

    /**
     * Provides a rough estimate of Simple Reaction Time (SRT) based on age.
     * Base approximation: ~200ms at age 20, increasing by ~2ms per year thereafter due to cognitive slowing.
     * 
     * @param age Age in years
     * @return Estimated SRT in seconds
     */
    public static Real typicalSimpleRT(int age) {
        double base = 0.200;
        if (age > 20) {
            base += (age - 20) * 0.002;
        }
        return Real.of(base);
    }

    /**
     * Calculates the Index of Difficulty (ID) for a pointing task.
     * ID = log2(2D / W)
     * Measured in bits.
     * 
     * @param d Distance to target
     * @param w Width of target
     * @return Index of Difficulty
     */
    public static double indexOfDifficulty(double d, double w) {
        if (w <= 0 || d < 0) return 0.0;
        return Math.log(2 * d / w) / Math.log(2);
    }

    /**
     * Calculates choice probabilities using Luce's Choice Axiom.
     * Probability of choosing item i = v_i / sum(v_j)
     * Where v represents the utility or value of an option.
     * 
     * @param utilities Array of positive utility values for each option
     * @return Array of probabilities summing to 1.0
     * @throws IllegalArgumentException if utilities contains non-positive values
     */
    public static double[] lucisChoiceProbability(double[] utilities) {
        double sum = Arrays.stream(utilities).sum();
        if (sum <= 0) {
            throw new IllegalArgumentException("Sum of utilities must be positive.");
        }
        
        double[] probs = new double[utilities.length];
        for (int i = 0; i < utilities.length; i++) {
            if (utilities[i] < 0) throw new IllegalArgumentException("Utilities must be non-negative.");
            probs[i] = utilities[i] / sum;
        }
        return probs;
    }
}
