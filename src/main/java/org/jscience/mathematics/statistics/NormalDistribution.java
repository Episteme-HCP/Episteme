package org.jscience.mathematics.statistics;

import org.jscience.mathematics.number.Real;

/**
 * Normal (Gaussian) probability distribution.
 * <p>
 * Characterized by mean (μ) and standard deviation (σ).
 * PDF: f(x) = (1/(σ√(2π))) * e^(-(x-μ)²/(2σ²))
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class NormalDistribution {

    private final Real mean;
    private final Real stdDev;

    public NormalDistribution(Real mean, Real stdDev) {
        if (stdDev.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Standard deviation must be positive");
        }
        this.mean = mean;
        this.stdDev = stdDev;
    }

    /**
     * Standard normal distribution (μ=0, σ=1).
     */
    public static NormalDistribution standard() {
        return new NormalDistribution(Real.ZERO, Real.ONE);
    }

    /**
     * Probability density function.
     */
    public Real pdf(Real x) {
        Real diff = x.subtract(mean);
        Real exponent = diff.multiply(diff).divide(Real.of(2).multiply(stdDev).multiply(stdDev)).negate();
        Real coefficient = Real.ONE.divide(stdDev.multiply(Real.of(Math.sqrt(2 * Math.PI))));
        return coefficient.multiply(Real.of(Math.exp(exponent.doubleValue())));
    }

    /**
     * Cumulative distribution function (approximation).
     */
    public Real cdf(Real x) {
        Real z = x.subtract(mean).divide(stdDev);
        // Abramowitz & Stegun approximation
        double t = 1.0 / (1.0 + 0.2316419 * Math.abs(z.doubleValue()));
        double d = 0.3989423 * Math.exp(-z.doubleValue() * z.doubleValue() / 2);
        double prob = d * t * (0.3193815 + t * (-0.3565638 + t * (1.781478 + t * (-1.821256 + t * 1.330274))));

        if (z.doubleValue() > 0) {
            return Real.of(1 - prob);
        } else {
            return Real.of(prob);
        }
    }

    public Real getMean() {
        return mean;
    }

    public Real getStdDev() {
        return stdDev;
    }

    public Real getVariance() {
        return stdDev.multiply(stdDev);
    }

    @Override
    public String toString() {
        return "N(" + mean + ", " + stdDev + "²)";
    }
}
