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
 * @author Gemini AI (Google DeepMind)
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
