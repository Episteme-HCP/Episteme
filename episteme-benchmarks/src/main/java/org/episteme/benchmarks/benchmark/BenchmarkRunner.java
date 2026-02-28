/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.benchmarks.benchmark;

import java.util.ArrayList;
import java.util.List;
import org.episteme.core.ui.i18n.I18N;


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
        benchmarks.addAll(BenchmarkRegistry.discover());
        System.out.println(I18N.getInstance().get("benchmark.discovered", benchmarks.size()));
    }

    public void runAll(String filter) {
        System.out.println(I18N.getInstance().get("benchmark.suite.starting"));
        System.out.printf("%-40s | %-20s | %-13s | %-13s | %-9s%n",
                I18N.getInstance().get("benchmark.header.name"),
                I18N.getInstance().get("benchmark.header.provider", "Provider"),
                I18N.getInstance().get("benchmark.header.time"),
                I18N.getInstance().get("benchmark.header.ops"),
                I18N.getInstance().get("benchmark.header.mem"));
        System.out.println("-".repeat(110));

        org.episteme.core.technical.monitoring.DistributedMonitor monitor = 
                org.episteme.core.technical.monitoring.DistributedMonitor.getInstance();

        for (RunnableBenchmark b : benchmarks) {
            // Apply filter if present
            if (filter != null && !filter.isEmpty()) {
                boolean matchId = b.getId().toLowerCase().contains(filter.toLowerCase());
                boolean matchName = b.getName().toLowerCase().contains(filter.toLowerCase());
                if (!matchId && !matchName) continue;
            }

            try {
                b.setup();

                // Warmup
                for (int i = 0; i < 3; i++)
                    b.run();

                // Measurement (Robust 5 trials x ITERS_PER_TRIAL)
                final int TRIALS = 5;
                final int ITERS_PER_TRIAL = Math.max(1, b.getSuggestedIterations() / TRIALS);
                final int totalIterations = TRIALS * ITERS_PER_TRIAL;
                
                long totalNs = 0;
                long totalMem = 0;

                for (int t = 0; t < TRIALS; t++) {
                    long trialStart = System.nanoTime();
                    long trialStartMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    
                    for (int i = 0; i < ITERS_PER_TRIAL; i++) {
                        b.run();
                    }
                    
                    long trialEnd = System.nanoTime();
                    long trialEndMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    
                    totalNs += (trialEnd - trialStart);
                    totalMem += Math.max(0, trialEndMem - trialStartMem);
                }

                long durationNs = totalNs;
                int iterations = totalIterations;
                
                // Record to Monitor
                monitor.recordExecution(b.getId(), b.getDomain(), durationNs / iterations);
                double avgMs = (durationNs / 1_000_000.0) / iterations;
                double opsSec = (iterations * 1_000_000_000.0) / durationNs;
                long memUsed = totalMem / TRIALS;

                BenchmarkResult res = new BenchmarkResult(
                        b.getId(), b.getName(), b.getAlgorithmProvider(), b.getDomain(), durationNs / 1_000_000, iterations, avgMs, opsSec, memUsed,
                        java.util.Collections.emptyMap());

                results.add(res);
                System.out.println(res.toSummaryString());

                b.teardown();

            } catch (Exception e) {
                System.err.println(I18N.getInstance().get("benchmark.failed", b.getName(), e.getMessage()));
            }
        }
    }

    public void exportCharts() {
        // JFreeChart removed. Charts are now handled natively in the Episteme Studio GUI.
        System.out.println("\nCharts are available in the Episteme Studio GUI (--studio).");
        System.out.println("Real-time metrics are available at http://localhost:7070/metrics");
    }

    public static void main(String[] args) {
        // Register benchmark I18N bundle
        I18N.getInstance().addBundle("org.episteme.benchmarks.i18n.messages_benchmarks");

        boolean monitorEnabled = true;
        boolean forceGui = false;
        boolean forceCli = false;
        String filter = null;

        for (String arg : args) {
            if (arg.equals("--monitor")) monitorEnabled = true;
            if (arg.equals("--studio") || arg.equals("--gui")) forceGui = true;
            if (arg.equals("--cli") || arg.equals("--console")) forceCli = true;
            if (arg.startsWith("--filter=")) filter = arg.substring(9);
        }

        if (monitorEnabled) {
            org.episteme.core.technical.monitoring.DistributedMonitor.getInstance().startServer();
        }

        // GUI is default unless --cli/--console is specified
        if (forceGui || !forceCli) {
            System.out.println("Launching Episteme Benchmarking Suite (GUI)...");
            org.episteme.benchmarks.ui.EpistemeBenchmarkingApp.main(args);
            return;
        }

        System.out.println("Starting Episteme Benchmarks (CLI mode)...");
        BenchmarkRunner runner = new BenchmarkRunner();
        runner.discover();
        runner.runAll(filter);
        runner.exportCharts();
    }
}



