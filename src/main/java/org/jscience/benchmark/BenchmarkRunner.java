package org.jscience.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.results.RunResult;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BenchmarkRunner {

    public static void main(String[] args) throws RunnerException, java.io.IOException {
        System.out.println("Starting JScience Benchmarks...");

        Options opt = new OptionsBuilder()
                .include(MatrixBenchmark.class.getSimpleName())
                .include(NBodyBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        Collection<RunResult> results = new Runner(opt).run();

        Map<String, Double> matrixResults = new HashMap<>();
        Map<String, Double> nbodyResults = new HashMap<>();

        for (RunResult result : results) {
            String benchmarkName = result.getPrimaryResult().getLabel();
            // Expected format: "multiplyJScience", "multiplyCommonsMath", etc.
            double score = result.getPrimaryResult().getScore(); // Average Latency in ms for Matrix, us for NBody?

            System.out.println("Result: " + benchmarkName + " = " + score);

            if (benchmarkName.contains("MatrixBenchmark")) {
                String param = result.getParams().getParam("size");
                String method = result.getParams().getBenchmark().replace("org.jscience.benchmark.MatrixBenchmark.",
                        "");
                matrixResults.put(method + " (Size=" + param + ")", score);
            } else if (benchmarkName.contains("NBodyBenchmark")) {
                String param = result.getParams().getParam("bodiesCount");
                nbodyResults.put("N=" + param, score);
            }
        }

        // Generate Graphs
        File matrixChart = new File("benchmark_matrix.png");
        BenchmarkGraphGenerator.generateBarChart(
                "Matrix Multiplication Performance", "Matrix Size", "Time (ms)",
                matrixResults, matrixChart);
        System.out.println("Generated " + matrixChart.getAbsolutePath());

        File nbodyChart = new File("benchmark_nbody.png");
        BenchmarkGraphGenerator.generateBarChart(
                "N-Body Simulation Step", "Bodies Count", "Time (us)",
                nbodyResults, nbodyChart);
        System.out.println("Generated " + nbodyChart.getAbsolutePath());
    }
}
