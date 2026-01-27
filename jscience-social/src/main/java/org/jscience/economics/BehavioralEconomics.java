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
    public static Real prospectValue(Real gainLoss, Real alpha, Real beta, Real lambda) {
        if (gainLoss.isZero() || gainLoss.isPositive()) {
            return gainLoss.pow(alpha);
        } else {
            return lambda.negate().multiply(gainLoss.negate().pow(beta));
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
    public static Real probabilityWeight(Real p, Real gamma) {
        if (p.compareTo(Real.ZERO) <= 0) return Real.ZERO;
        if (p.compareTo(Real.ONE) >= 0) return Real.ONE;
        
        // w(p) = exp(-(-ln p)^gamma)
        // Calculating (-ln p)
        double lnP = Math.log(p.doubleValue());
        Real negLnP = Real.of(-lnP);
        
        // (-ln p)^gamma
        Real powGamma = negLnP.pow(gamma);
        
        // -(-ln p)^gamma
        Real exponent = powGamma.negate();
        
        // exp(...)
        return exponent.exp();
    }
    
    // ... (rest of the file until decisionValue)

    /**
     * Calculates the "Decision Weight" for a prospect.
     * Combines value function and probability weighting.
     *
     * @param amount the potential outcome amount
     * @param probability the probability of the outcome
     * @param gamma the weighting parameter
     * @return the decision weight
     */
    public static Real decisionValue(Real amount, Real probability, Real gamma) {
        Real alpha = Real.of(0.88);
        Real lambda = Real.of(2.25);
        
        Real v;
        if (amount.compareTo(Real.ZERO) >= 0) {
            v = amount.pow(alpha);
        } else {
            v = lambda.negate().multiply(amount.negate().pow(alpha));
        }
        
        Real w = probabilityWeight(probability, gamma);
        return v.multiply(w);
    }
}
