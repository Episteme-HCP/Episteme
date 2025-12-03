/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.distributed;

import org.jscience.mathematics.number.Real;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Distributed Monte Carlo simulations.
 * <p>
 * Provides distributed Monte Carlo simulation capabilities using parallel
 * streams.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class DistributedMonteCarlo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Distributed Monte Carlo π estimation.
     * 
     * @param totalSamples  Total number of random samples
     * @param numPartitions Number of parallel partitions
     * @return Estimated value of π
     */
    public static Real estimatePi(long totalSamples, int numPartitions) {
        long samplesPerPartition = totalSamples / numPartitions;

        // Use Java parallel streams for multi-core execution
        long totalInside = java.util.stream.IntStream.range(0, numPartitions)
                .parallel()
                .mapToLong(i -> countInsideCircle(samplesPerPartition))
                .sum();

        return Real.of(4.0 * totalInside / totalSamples);
    }

    /**
     * Count samples inside unit circle for one partition.
     */
    private static long countInsideCircle(long samples) {
        Random rand = new Random();
        long inside = 0;

        for (long i = 0; i < samples; i++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            if (x * x + y * y <= 1.0) {
                inside++;
            }
        }

        return inside;
    }

    /**
     * Generic distributed Monte Carlo simulation.
     * 
     * @param simulator     Function to run per partition
     * @param totalSamples  Total samples across all partitions
     * @param numPartitions Number of parallel partitions
     * @return Aggregated result
     */
    public static Real distributedSimulation(
            SamplerFunction simulator,
            long totalSamples,
            int numPartitions) {

        long samplesPerPartition = totalSamples / numPartitions;

        // Execute in parallel
        double sum = java.util.stream.IntStream.range(0, numPartitions)
                .parallel()
                .mapToDouble(i -> simulator.sample(samplesPerPartition))
                .sum();

        return Real.of(sum / numPartitions);
    }

    /**
     * Functional interface for Monte Carlo samplers.
     */
    @FunctionalInterface
    public interface SamplerFunction extends Serializable {
        double sample(long numSamples);
    }

    /**
     * Distributed variance reduction using control variates.
     */
    public static class ControlVariateSimulation {

        public static Real estimate(
                SamplerFunction target,
                SamplerFunction control,
                Real controlMean,
                long samples) {

            // Parallel computation of target and control samples
            double[] targetSamples = new double[(int) samples];
            double[] controlSamples = new double[(int) samples];

            java.util.stream.IntStream.range(0, (int) samples)
                    .parallel()
                    .forEach(i -> {
                        targetSamples[i] = target.sample(1);
                        controlSamples[i] = control.sample(1);
                    });

            // Compute correlation and adjusted estimate
            double targetMean = java.util.Arrays.stream(targetSamples).average().orElse(0);
            double controlSampleMean = java.util.Arrays.stream(controlSamples).average().orElse(0);

            // Control variate adjustment
            double adjustment = controlMean.doubleValue() - controlSampleMean;
            return Real.of(targetMean + adjustment);
        }
    }
}

