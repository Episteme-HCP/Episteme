package org.jscience.benchmarks.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.core.technical.backend.algorithms.GeneticAlgorithmProvider;
import org.jscience.core.technical.backend.algorithms.MulticoreGeneticAlgorithmProvider;
import org.jscience.nativ.mathematics.optimization.backends.NativeGeneticAlgorithmProvider;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class GeneticAlgorithmBenchmark {

    @Param({"10", "50"})
    public int dimensions;

    @Param({"100", "500"})
    public int populationSize;

    @Param({"50"})
    public int generations;

    private GeneticAlgorithmProvider multicoreProvider;
    private GeneticAlgorithmProvider nativeProvider;
    private Function<double[], Double> sphereFunction;

    @Setup(Level.Trial)
    public void doSetup() {
        multicoreProvider = new MulticoreGeneticAlgorithmProvider();
        nativeProvider = new NativeGeneticAlgorithmProvider();

        // Sphere function: sum(x_i^2)
        sphereFunction = (args) -> {
            double sum = 0;
            for (double val : args) {
                sum += val * val;
            }
            return sum;
        };
    }

    @Benchmark
    public double[] benchmarkMulticore() {
        return multicoreProvider.solve(sphereFunction, dimensions, populationSize, generations, 0.01);
    }

    @Benchmark
    public double[] benchmarkNative() {
        return nativeProvider.solve(sphereFunction, dimensions, populationSize, generations, 0.01);
    }
}
