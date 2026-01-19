package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models the value function from Prospect Theory (Kahneman & Tversky).
 */
public final class BehavioralEconomics {

    private BehavioralEconomics() {}

    /**
     * Tversky & Kahneman Value Function.
     * v(x) = x^alpha if x >= 0
     * v(x) = -lambda * (-x)^beta if x < 0
     * 
     * Typically: alpha=0.88, beta=0.88, lambda=2.25 (Loss Aversion)
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
     */
    public static Real hyperbolicDiscount(double amount, double k, double delay) {
        return Real.of(amount / (1 + k * delay));
    }

    /**
     * Calculates the "Decision Weight" for a prospect.
     */
    public static Real decisionValue(double amount, double probability, double gamma) {
        double v = amount >= 0 ? Math.pow(amount, 0.88) : -2.25 * Math.pow(-amount, 0.88);
        double w = probabilityWeight(probability, gamma).doubleValue();
        return Real.of(v * w);
    }
}
