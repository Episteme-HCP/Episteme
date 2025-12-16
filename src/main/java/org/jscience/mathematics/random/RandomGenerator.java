package org.jscience.mathematics.random;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.numbers.integers.Natural;
import java.util.Random;

/**
 * High-quality random number generator utilities.
 * <p>
 * Provides various distributions and seed management.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class RandomGenerator {

    private final Random random;

    public RandomGenerator() {
        this.random = new Random();
    }

    public RandomGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Returns random Real in [0, 1).
     */
    public Real nextReal() {
        return Real.of(random.nextDouble());
    }

    /**
     * Returns random Real in [min, max).
     */
    public Real nextReal(Real min, Real max) {
        double range = max.subtract(min).doubleValue();
        return Real.of(min.doubleValue() + random.nextDouble() * range);
    }

    /**
     * Returns random Integer in [min, max].
     */
    public Integer nextInteger(int min, int max) {
        return Integer.of(random.nextInt(max - min + 1) + min);
    }

    /**
     * Returns random Natural in [0, bound).
     */
    public Natural nextNatural(int bound) {
        return Natural.of(random.nextInt(bound));
    }

    /**
     * Returns random boolean.
     */
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    /**
     * Returns random Real from standard normal distribution N(0,1).
     */
    public Real nextGaussian() {
        return Real.of(random.nextGaussian());
    }

    /**
     * Returns random Real from normal distribution N(mean, stdDev).
     */
    public Real nextGaussian(Real mean, Real stdDev) {
        return Real.of(mean.doubleValue() + random.nextGaussian() * stdDev.doubleValue());
    }

    /**
     * Returns random Real from exponential distribution with rate lambda.
     */
    public Real nextExponential(Real lambda) {
        return Real.of(-Math.log(1.0 - random.nextDouble()) / lambda.doubleValue());
    }

    /**
     * Shuffles an array in-place (Fisher-Yates algorithm).
     */
    public <T> void shuffle(T[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    /**
     * Returns random element from array.
     */
    public <T> T choice(T[] array) {
        return array[random.nextInt(array.length)];
    }

    /**
     * Sets the seed for reproducibility.
     */
    public void setSeed(long seed) {
        random.setSeed(seed);
    }
}
