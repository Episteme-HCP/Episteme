package org.jscience.benchmarks.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.core.technical.backend.algorithms.MonteCarloProvider;
import org.jscience.core.technical.backend.algorithms.MulticoreMonteCarloProvider;
import java.util.concurrent.TimeUnit;


@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MonteCarloBenchmark {

    @Param({"10000", "100000"})
    public int iterations;


    private MonteCarloProvider multicoreProvider;

    @Setup(Level.Trial)
    public void doSetup() {
        multicoreProvider = new MulticoreMonteCarloProvider();
    }

    @Benchmark
    public double benchmarkMulticorePi() {
        // Pi estimation: x^2 + y^2 <= 1
        return multicoreProvider.integrate((args) -> {
            double x = args[0];
            double y = args[1];
            return (x*x + y*y <= 1.0) ? 1.0 : 0.0;
        }, 2, iterations); // 2 dimensions
    }
}
