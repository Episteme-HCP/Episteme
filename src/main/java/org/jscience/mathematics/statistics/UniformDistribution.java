package org.jscience.mathematics.statistics;

import org.jscience.mathematics.number.Real;
import java.util.Random;

/**
 * Uniform probability distribution over [a, b].
 * <p>
 * All values in the interval are equally likely.
 * PDF: f(x) = 1/(b-a) for x ∈ [a,b], 0 otherwise
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class UniformDistribution {

    private final Real min;
    private final Real max;
    private final Random random;

    public UniformDistribution(Real min, Real max) {
        if (min.compareTo(max) >= 0) {
            throw new IllegalArgumentException("min must be < max");
        }
        this.min = min;
        this.max = max;
        this.random = new Random();
    }

    /**
     * Standard uniform distribution [0, 1].
     */
    public static UniformDistribution standard() {
        return new UniformDistribution(Real.ZERO, Real.ONE);
    }

    /**
     * Probability density function.
     */
    public Real pdf(Real x) {
        if (x.compareTo(min) < 0 || x.compareTo(max) > 0) {
            return Real.ZERO;
        }
        return Real.ONE.divide(max.subtract(min));
    }

    /**
     * Cumulative distribution function.
     */
    public Real cdf(Real x) {
        if (x.compareTo(min) <= 0) {
            return Real.ZERO;
        }
        if (x.compareTo(max) >= 0) {
            return Real.ONE;
        }
        return x.subtract(min).divide(max.subtract(min));
    }

    /**
     * Generates a random sample from this distribution.
     */
    public Real sample() {
        double range = max.subtract(min).doubleValue();
        double value = min.doubleValue() + random.nextDouble() * range;
        return Real.of(value);
    }

    public Real getMean() {
        return min.add(max).divide(Real.of(2));
    }

    public Real getVariance() {
        Real range = max.subtract(min);
        return range.multiply(range).divide(Real.of(12));
    }

    @Override
    public String toString() {
        return "U[" + min + ", " + max + "]";
    }
}
