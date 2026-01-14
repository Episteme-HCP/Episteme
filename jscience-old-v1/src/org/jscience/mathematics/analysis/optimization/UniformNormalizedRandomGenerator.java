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

package org.jscience.mathematics.analysis.optimization;

import org.jscience.measure.random.MersenneTwisterGenerator;
import org.jscience.measure.random.RandomGenerator;

/**
 * This class implements a normalized uniform random generator.
 * <p/>
 * <p>Since this is a normalized random generator, it has a null mean
 * and a unit standard deviation. Beeing also a uniform
 * generator, it produces numbers in the range [-sqrt(3) ;
 * sqrt(3)]. It uses the {@link MersenneTwister MersenneTwister}
 * generator as the underlying generator.</p>
 *
 * @author L. Maisonobe
 * @version $Id: UniformNormalizedRandomGenerator.java,v 1.2 2007-10-21 17:45:47 virtualcall Exp $
 * @see MersenneTwister
 */

public class UniformNormalizedRandomGenerator
        implements RandomGenerator {

    private static final double SQRT3 = Math.sqrt(3.0);

    private static final double TWOSQRT3 = 2.0 * Math.sqrt(3.0);

    /**
     * Underlying generator.
     */
    MersenneTwisterGenerator generator;

    /**
     * Create a new generator.
     * The seed of the generator is related to the current time.
     */
    public UniformNormalizedRandomGenerator() {
        generator = new MersenneTwisterGenerator();
    }

    /** Creates a new random number generator using a single int seed.
     * @param seed the initial seed (32 bits integer)
     */
    //public UniformNormalizedRandomGenerator(int seed) {
    //  generator = new MersenneTwisterGenerator(seed);
    //}

    /**
     * Creates a new random number generator using an int array seed.
     *
     * @param seed the initial seed (32 bits integers array), if null
     *             the seed of the generator will be related to the current time
     */
    public UniformNormalizedRandomGenerator(int[] seed) {
        generator = new MersenneTwisterGenerator(seed);
    }

    /**
     * Create a new generator initialized with a single long seed.
     *
     * @param seed seed for the generator (64 bits integer)
     */
    public UniformNormalizedRandomGenerator(long seed) {
        generator = new MersenneTwisterGenerator(seed);
    }

    /**
     * Generate a random scalar with null mean and unit standard deviation.
     * <p>The number generated is uniformly distributed between -sqrt(3)
     * and sqrt(3).</p>
     *
     * @return a random scalar with null mean and unit standard deviation
     */
    public double nextDouble() {
        return TWOSQRT3 * generator.nextDouble() - SQRT3;
    }

}
