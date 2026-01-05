/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.technical.backend.algorithms;

import org.jscience.mathematics.numbers.real.Real;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Parallel Monte Carlo integration provider using thread pools.
 * 
 * <p>
 * Monte Carlo integration estimates integrals by random sampling. This provider
 * parallelizes the sampling across multiple CPU cores. While not
 * GPU-accelerated
 * (random number generation is challenging on GPUs), it provides near-linear
 * speedup with core count.
 * </p>
 * 
 * <p>
 * Use cases:
 * <ul>
 * <li>High-dimensional integration (curse of dimensionality)</li>
 * <li>Volume estimation</li>
 * <li>Option pricing in finance</li>
 * <li>Physics simulations (particle transport)</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreMonteCarloProvider implements MonteCarloProvider { // Renamed from ParallelMonteCarloProvider

    private static final Logger LOGGER = Logger.getLogger(MulticoreMonteCarloProvider.class.getName());

    private final int numThreads;
    private final ExecutorService executor;

    /**
     * Creates a new parallel Monte Carlo provider using all available processors.
     */
    /**
     * Creates a new parallel Monte Carlo provider using all available processors.
     */
    public MulticoreMonteCarloProvider() {
        this(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Creates a new parallel Monte Carlo provider with specified thread count.
     *
     * @param numThreads number of parallel threads
     */
    public MulticoreMonteCarloProvider(int numThreads) {
        this.numThreads = numThreads;
        this.executor = Executors.newFixedThreadPool(numThreads);
        LOGGER.info("Parallel Monte Carlo provider initialized with " + numThreads + " threads");
    }

    /**
     * Estimates the integral of a function over a hypercube [0,1]^d.
     *
     * @param f          the function to integrate, takes double[] and returns
     *                   double
     * @param dimensions number of dimensions
     * @param samples    total number of samples
     * @return estimated integral value
     */
    @Override
    public double integrate(Function<double[], Double> f, int dimensions, long samples) {
        long samplesPerThread = samples / numThreads;
        @SuppressWarnings("unchecked")
        Future<Double>[] futures = new Future[numThreads];

        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            futures[t] = executor.submit(() -> {
                Random rng = new Random(threadId * 12345L + System.nanoTime());
                double sum = 0;
                double[] point = new double[dimensions];

                for (long i = 0; i < samplesPerThread; i++) {
                    for (int d = 0; d < dimensions; d++) {
                        point[d] = rng.nextDouble();
                    }
                    sum += f.apply(point);
                }
                return sum;
            });
        }

        double totalSum = 0;
        try {
            for (Future<Double> future : futures) {
                totalSum += future.get();
            }
        } catch (Exception e) {
            LOGGER.warning("Monte Carlo integration failed: " + e.getMessage());
            return Double.NaN;
        }

        return totalSum / samples;
    }

    /**
     * Estimates the integral using Real API for arbitrary precision.
     *
     * @param f          the function to integrate
     * @param dimensions number of dimensions
     * @param samples    total number of samples
     * @return estimated integral value as Real
     */
    @Override
    public Real integrateReal(Function<Real[], Real> f, int dimensions, long samples) {
        // Wrapper that converts to/from Real
        Function<double[], Double> wrapper = point -> {
            Real[] realPoint = new Real[point.length];
            for (int i = 0; i < point.length; i++) {
                realPoint[i] = Real.of(point[i]);
            }
            return f.apply(realPoint).doubleValue();
        };

        return Real.of(integrate(wrapper, dimensions, samples));
    }

    /**
     * Estimates pi using the classic Monte Carlo circle method.
     * <p>
     * This is a demonstration/benchmark method.
     * </p>
     *
     * @param samples number of random points
     * @return estimate of pi
     */
    public double estimatePi(long samples) {
        long samplesPerThread = samples / numThreads;

        @SuppressWarnings("unchecked")
        Future<Long>[] futures = new Future[numThreads];

        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            futures[t] = executor.submit(() -> {
                Random rng = new Random(threadId * 12345L + System.nanoTime());
                long inside = 0;

                for (long i = 0; i < samplesPerThread; i++) {
                    double x = rng.nextDouble();
                    double y = rng.nextDouble();
                    if (x * x + y * y <= 1.0) {
                        inside++;
                    }
                }
                return inside;
            });
        }

        long totalInside = 0;
        try {
            for (Future<Long> future : futures) {
                totalInside += future.get();
            }
        } catch (Exception e) {
            LOGGER.warning("Pi estimation failed: " + e.getMessage());
            return Double.NaN;
        }

        return 4.0 * totalInside / (samplesPerThread * numThreads);
    }

    /**
     * Shuts down the thread pool.
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Returns the name of this provider.
     *
     * @return provider name
     */
    @Override
    public String getName() {
        return "Monte Carlo (Parallel " + numThreads + " threads)";
    }
}
