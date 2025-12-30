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
import org.jscience.ui.i18n.I18n;

/**
 * Main engine that discovers and executes benchmarks.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BenchmarkRunner {

    private final List<RunnableBenchmark> benchmarks = new ArrayList<>();
    private final List<BenchmarkResult> results = new ArrayList<>();

    public void discover() {
        ServiceLoader<RunnableBenchmark> loader = ServiceLoader.load(RunnableBenchmark.class);
        loader.forEach(benchmarks::add);
        System.out.println(I18n.getInstance().get("benchmark.discovered", benchmarks.size()));
    }

    public void runAll() {
        System.out.println(I18n.getInstance().get("benchmark.suite.starting"));
        System.out.printf("%-30s | %-15s | %-13s | %-13s | %-9s%n",
                I18n.getInstance().get("benchmark.header.name"),
                I18n.getInstance().get("benchmark.header.domain"),
                I18n.getInstance().get("benchmark.header.time"),
                I18n.getInstance().get("benchmark.header.ops"),
                I18n.getInstance().get("benchmark.header.mem"));
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
                System.err.println(I18n.getInstance().get("benchmark.failed", b.getName(), e.getMessage()));
            }
        }
    }

    public void exportCharts() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (BenchmarkResult r : results) {
            dataset.addValue(r.operationsPerSecond(), "Ops/Sec", r.benchmarkName());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                I18n.getInstance().get("benchmark.chart.title"),
                I18n.getInstance().get("benchmark.header.name"),
                I18n.getInstance().get("benchmark.chart.yaxis"),
                dataset);

        try {
            File chartFile = new File("benchmark_results.png");
            ChartUtils.saveChartAsPNG(chartFile, barChart, 800, 600);
            System.out.println("\n" + I18n.getInstance().get("benchmark.chart.saved", chartFile.getAbsolutePath()));
        } catch (Exception e) {
            System.err.println(I18n.getInstance().get("benchmark.chart.error", e.getMessage()));
        }
    }

    public static void main(String[] args) {
        BenchmarkRunner runner = new BenchmarkRunner();
        runner.discover();
        runner.runAll();
        runner.exportCharts();
    }
}
