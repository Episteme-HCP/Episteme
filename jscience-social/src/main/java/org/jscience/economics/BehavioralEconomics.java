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
package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models the value function from Prospect Theory (Kahneman & Tversky).
 * Provides models for loss aversion, probability weighting, and hyperbolic discounting.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
public final class BehavioralEconomics {

    private BehavioralEconomics() {}

    /**
     * Tversky & Kahneman Value Function.
     * v(x) = x^alpha if x >= 0
     * v(x) = -lambda * (-x)^beta if x < 0
     * 
     * <p>Typically: alpha=0.88, beta=0.88, lambda=2.25 (Loss Aversion)</p>
     *
     * @param gainLoss the magnitude of the gain (positive) or loss (negative)
     * @param alpha the exponent for gains
     * @param beta the exponent for losses
     * @param lambda the loss aversion coefficient
     * @return the subjective value
     */
    public static Real prospectValue(double gainLoss, double alpha, double beta, double lambda) {
        if (gainLoss >= 0) {
            return Real.of(Math.pow(gainLoss, alpha));
        } else {
            return Real.of(-lambda * Math.pow(-gainLoss, beta));
        }
    }

    /**
     * Probability Weighting Function (Prelec).
     * w(p) = exp(-(-ln p)^gamma)
     *
     * @param p the objective probability (0 to 1)
     * @param gamma the curvature parameter
     * @return the weighted probability
     */
    public static Real probabilityWeight(double p, double gamma) {
        if (p <= 0) return Real.ZERO;
        if (p >= 1) return Real.ONE;
        return Real.of(Math.exp(-Math.pow(-Math.log(p), gamma)));
    }

    /**
     * Hyperbolic Discounting: V = A / (1 + kD)
     * Models preference for immediate vs delayed rewards.
     * 
     * @param amount Future Amount
     * @param k Impulsivity parameter
     * @param delay Delay periods
     * @return the present value
     */
    public static Real hyperbolicDiscount(double amount, double k, double delay) {
        return Real.of(amount / (1 + k * delay));
    }

    /**
     * Calculates the "Decision Weight" for a prospect.
     * Combines value function and probability weighting.
     *
     * @param amount the potential outcome amount
     * @param probability the probability of the outcome
     * @param gamma the weighting parameter
     * @return the decision weight
     */
    public static Real decisionValue(double amount, double probability, double gamma) {
        double v = amount >= 0 ? Math.pow(amount, 0.88) : -2.25 * Math.pow(-amount, 0.88);
        double w = probabilityWeight(probability, gamma).doubleValue();
        return Real.of(v * w);
    }
}
