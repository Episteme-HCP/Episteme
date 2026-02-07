/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.montecarlo;

import org.jscience.core.technical.algorithm.MonteCarloProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Multicore Monte Carlo integration provider using parallel streams.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreMonteCarloProvider implements MonteCarloProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public double integrate(ToDoubleFunction<double[]> function, double[] lowerBounds, 
                           double[] upperBounds, int samples) {
        int dimensions = lowerBounds.length;
        
        double sum = IntStream.range(0, samples)
            .parallel()
            .mapToDouble(i -> {
                double[] point = new double[dimensions];
                ThreadLocalRandom rng = ThreadLocalRandom.current();
                
                for (int d = 0; d < dimensions; d++) {
                    point[d] = lowerBounds[d] + rng.nextDouble() * (upperBounds[d] - lowerBounds[d]);
                }
                
                return function.applyAsDouble(point);
            })
            .sum();
        
        double volume = 1.0;
        for (int d = 0; d < dimensions; d++) {
            volume *= (upperBounds[d] - lowerBounds[d]);
        }
        
        return (sum / samples) * volume;
    }

    @Override
    public Real integrate(Function<Real[], Real> function, Real[] lowerBounds, 
                         Real[] upperBounds, int samples) {
        int dimensions = lowerBounds.length;
        
        Real sum = IntStream.range(0, samples)
            .parallel()
            .mapToObj(i -> {
                Real[] point = new Real[dimensions];
                ThreadLocalRandom rng = ThreadLocalRandom.current();
                
                for (int d = 0; d < dimensions; d++) {
                    double range = upperBounds[d].doubleValue() - lowerBounds[d].doubleValue();
                    point[d] = lowerBounds[d].add(Real.of(rng.nextDouble() * range));
                }
                
                return function.apply(point);
            })
            .reduce(Real.ZERO, Real::add);
            
        Real volume = Real.ONE;
        for (int d = 0; d < dimensions; d++) {
            volume = volume.multiply(upperBounds[d].subtract(lowerBounds[d]));
        }
        
        return sum.divide(Real.of(samples)).multiply(volume);
    }

    @Override
    public double estimatePi(int samples) {
        long insideCircle = IntStream.range(0, samples)
            .parallel()
            .filter(i -> {
                ThreadLocalRandom rng = ThreadLocalRandom.current();
                double x = rng.nextDouble();
                double y = rng.nextDouble();
                return (x * x + y * y) <= 1.0;
            })
            .count();
        
        return 4.0 * insideCircle / samples;
    }

    @Override
    public Real estimatePiReal(int samples) {
        return Real.of(estimatePi(samples));
    }

    @Override
    public String getName() {
        return "Multicore Monte Carlo Provider (Parallel Streams)";
    }
}
