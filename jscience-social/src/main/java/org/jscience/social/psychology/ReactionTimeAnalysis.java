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

package org.jscience.social.psychology;

import java.util.ArrayList;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.measure.quantity.Time;

/**
 * Provides mathematical models for analyzing human reaction times and motor control.
 * Includes implementations of Hick's Law, Fitts's Law, and basic decision probability models.
 * Modernized to use Quantity and Real for high-precision physical and cognitive modeling.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class ReactionTimeAnalysis {

    private ReactionTimeAnalysis() {}

    /**
     * Models decision time based on the number of choices using Hick's Law.
     * RT = a + b * log2(n + 1)
     * 
     * @param a Base reaction time
     * @param b Processing speed coefficient
     * @param n Number of equally probable choices
     * @return Estimated reaction time
     */
    public static Quantity<Time> hicksLaw(Quantity<Time> a, Quantity<Time> b, int n) {
        if (n < 0) return a;
        // log2(x) = log(x) / log(2)
        Real logPart = Real.of(Math.log(n + 1.0) / Math.log(2.0));
        Real rtValue = a.getValue().add(b.getValue().multiply(logPart));
        return Quantities.create(rtValue, Units.SECOND);
    }

    /**
     * Models movement time to a target using Fitts's Law.
     * MT = a + b * log2(2D / W)
     * 
     * @param a Intercept (Time)
     * @param b Slope (Time per bit)
     * @param d Distance to target center
     * @param w Width of target (tolerance)
     * @return Estimated movement time
     */
    public static Quantity<Time> fittsLaw(
            Quantity<Time> a, 
            Quantity<Time> b, 
            Quantity<Length> d, 
            Quantity<Length> w) {
        
        Real dVal = d.getValue();
        Real wVal = w.getValue();
        if (wVal.isZero() || dVal.isNegative()) return Quantities.create(Real.ZERO, Units.SECOND);
        
        Real logPart = Real.of(Math.log(2.0 * dVal.doubleValue() / wVal.doubleValue()) / Math.log(2.0));
        Real mtValue = a.getValue().add(b.getValue().multiply(logPart));
        return Quantities.create(mtValue, Units.SECOND);
    }

    /**
     * Provides a rough estimate of Simple Reaction Time (SRT) based on age.
     * 
     * @param age Age in years (Quantity<Time> or Real) - using int as it's a discrete demographic count here
     * @return Estimated SRT
     */
    public static Quantity<Time> typicalSimpleRT(int age) {
        Real base = Real.of(0.200);
        if (age > 20) {
            base = base.add(Real.of(age - 20).multiply(Real.of(0.002)));
        }
        return Quantities.create(base, Units.SECOND);
    }

    /**
     * Calculates the Index of Difficulty (ID) for a pointing task.
     * ID = log2(2D / W)
     * 
     * @param d Distance to target
     * @param w Width of target
     * @return Index of Difficulty (dimensionless Real)
     */
    public static Real indexOfDifficulty(Quantity<Length> d, Quantity<Length> w) {
        Real dVal = d.getValue();
        Real wVal = w.getValue();
        if (wVal.isZero() || dVal.isNegative()) return Real.ZERO;
        return Real.of(Math.log(2.0 * dVal.doubleValue() / wVal.doubleValue()) / Math.log(2.0));
    }

    /**
     * Calculates choice probabilities using Luce's Choice Axiom.
     * 
     * @param utilities List of utility values
     * @return List of probabilities
     */
    public static List<Real> lucisChoiceProbability(List<Real> utilities) {
        Real sum = Real.ZERO;
        for (Real u : utilities) {
            if (u.isNegative()) throw new IllegalArgumentException("Utilities must be non-negative.");
            sum = sum.add(u);
        }
        
        if (sum.isZero()) {
            throw new IllegalArgumentException("Sum of utilities must be positive.");
        }
        
        List<Real> probs = new ArrayList<>();
        for (Real u : utilities) {
            probs.add(u.divide(sum));
        }
        return probs;
    }
}

