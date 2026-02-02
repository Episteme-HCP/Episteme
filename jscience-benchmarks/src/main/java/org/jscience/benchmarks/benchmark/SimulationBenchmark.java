package org.jscience.benchmarks.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.core.technical.algorithm.SimulationProvider;
import org.jscience.core.technical.algorithm.simulation.MulticoreSimulationProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SimulationBenchmark {

    @Param({"1000", "5000"})
    public int numTasks;

    private SimulationProvider parallelProvider;
    private SimulationProvider nativeProvider;
    private List<Runnable> tasks;
    private int threads = 4;

    @Setup(Level.Trial)
    public void doSetup() {
        parallelProvider = new MulticoreSimulationProvider();
        nativeProvider = new MulticoreSimulationProvider(); // Native provider not available

        tasks = new ArrayList<>(numTasks);
        for (int i = 0; i < numTasks; i++) {
            tasks.add(() -> {
                // Simulate some work: simple math
                double result = Math.random() * 1000; // Simulate work
                if (result > 999) System.out.println("Rare event"); // Use result
                for (int j = 0; j < 1000; j++) {
                    result += Math.sin(j) * Math.cos(j);
                }
            });
        }
    }

    @Benchmark
    public void benchmarkParallelSimulation() {
        parallelProvider.parallelExecute(tasks, threads);
    }

    @Benchmark
    public void benchmarkNativeSimulation() {
        nativeProvider.parallelExecute(tasks, threads);
    }
}
