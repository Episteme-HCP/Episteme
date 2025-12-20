package org.jscience.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.io.File;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Main engine that discovers and executes benchmarks.
 */
public class BenchmarkRunner {

    private final List<RunnableBenchmark> benchmarks = new ArrayList<>();
    private final List<BenchmarkResult> results = new ArrayList<>();

    public void discover() {
        ServiceLoader<RunnableBenchmark> loader = ServiceLoader.load(RunnableBenchmark.class);
        loader.forEach(benchmarks::add);
        System.out.println("Discovered " + benchmarks.size() + " benchmarks.");
    }

    public void runAll() {
        System.out.println("Starting Benchmark Suite...");
        System.out.printf("%-30s | %-15s | %-13s | %-13s | %-9s%n", "Benchmark", "Domain", "Time (ms/op)", "Ops/Sec",
                "Mem (MB)");
        System.out.println("-".repeat(90));

        for (RunnableBenchmark b : benchmarks) {
            try {
                b.setup();

                // Warmup
                for (int i = 0; i < 3; i++)
                    b.run();

                // Measurement
                long start = System.nanoTime();
                long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                int iterations = b.getSuggestedIterations();
                for (int i = 0; i < iterations; i++) {
                    b.run();
                }

                long end = System.nanoTime();
                long endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                long durationNs = end - start;
                double avgMs = (durationNs / 1_000_000.0) / iterations;
                double opsSec = (iterations * 1_000_000_000.0) / durationNs;
                long memUsed = Math.max(0, endMem - startMem);

                BenchmarkResult res = new BenchmarkResult(
                        b.getName(), b.getDomain(), durationNs / 1_000_000, iterations, avgMs, opsSec, memUsed,
                        Map.of());

                results.add(res);
                System.out.println(res.toSummaryString());

                b.teardown();

            } catch (Exception e) {
                System.err.println("Benchmark " + b.getName() + " failed: " + e.getMessage());
            }
        }
    }

    public void exportCharts() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (BenchmarkResult r : results) {
            dataset.addValue(r.operationsPerSecond(), "Ops/Sec", r.benchmarkName());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "JScience Benchmark Performance",
                "Benchmark",
                "Operations per Second",
                dataset);

        try {
            File chartFile = new File("benchmark_results.png");
            ChartUtils.saveChartAsPNG(chartFile, barChart, 800, 600);
            System.out.println("\nChart saved to: " + chartFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error saving chart: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        BenchmarkRunner runner = new BenchmarkRunner();
        runner.discover();
        runner.runAll();
        runner.exportCharts();
    }
}
