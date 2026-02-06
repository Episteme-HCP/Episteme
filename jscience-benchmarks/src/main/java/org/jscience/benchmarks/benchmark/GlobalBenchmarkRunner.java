package org.jscience.benchmarks.benchmark;

import org.jscience.core.ui.i18n.I18N;

/**
 * Global runner for all JScience benchmarks.
 * Orchestrates FFT, Monte Carlo, and Matrix benchmarks.
 */
public class GlobalBenchmarkRunner {

    public static void main(String[] args) {
        // Register benchmark I18N bundle
        I18N.getInstance().addBundle("org.jscience.benchmarks.i18n.messages_benchmarks");
        
        System.out.println("==========================================================");
        System.out.println("       JSCIENCE GLOBAL BENCHMARK SUITE");
        System.out.println("==========================================================");
        System.out.println();

        // 1. FFT Benchmarks
        try {
            System.out.println("[FFT Benchmarks]");
            FFTBenchmark b = new FFTBenchmark();
            b.setup();
            SimpleBenchmarkRunner.run("FFT (Size 4096)", b::run);
        } catch (Exception e) {
            System.err.println("FFT Benchmark failed: " + e.getMessage());
        }
        System.out.println();

        // 2. Monte Carlo Benchmarks (Manual call)
        try {
            System.out.println("[Monte Carlo Benchmarks]");
            MonteCarloBenchmark mc = new MonteCarloBenchmark();
            mc.iterations = 100000;
            mc.doSetup();
            SimpleBenchmarkRunner.run("Monte Carlo Pi (100k)", () -> mc.benchmarkMulticorePi());
        } catch (Exception e) {
            System.err.println("Monte Carlo Benchmark failed: " + e.getMessage());
        }
        System.out.println();

        // 3. Matrix Benchmarks (Manual)
        try {
            ManualMatrixBenchmark.main(args);
        } catch (Exception e) {
            System.err.println("Matrix Benchmark failed: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("==========================================================");
        System.out.println("       SUITE COMPLETE");
        System.out.println("==========================================================");
    }
}
