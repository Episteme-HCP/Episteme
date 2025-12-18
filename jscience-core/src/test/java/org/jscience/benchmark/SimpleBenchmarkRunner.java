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
package org.jscience.benchmark;

/**
 * Simple utility for running benchmarks and reporting results.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class SimpleBenchmarkRunner {

    public static void run(String name, Runnable benchmark) {
        run(name, 10, 100, benchmark);
    }

    public static void run(String name, int warmupIterations, int measurementIterations, Runnable benchmark) {
        System.out.println("Running benchmark: " + name);

        // Warmup
        System.out.print("  Warmup... ");
        for (int i = 0; i < warmupIterations; i++) {
            benchmark.run();
        }
        System.out.println("Done.");

        // Measurement
        System.out.print("  Measuring... ");
        long start = System.nanoTime();
        for (int i = 0; i < measurementIterations; i++) {
            benchmark.run();
        }
        long end = System.nanoTime();
        System.out.println("Done.");

        double totalTimeMs = (end - start) / 1_000_000.0;
        double avgTimeMs = totalTimeMs / measurementIterations;

        System.out.printf("  Results: Total: %.2f ms, Avg: %.4f ms/op%n", totalTimeMs, avgTimeMs);
        System.out.println("--------------------------------------------------");
    }

    public static void main(String[] args) {
        System.out.println("=== JScience Comparative Benchmarks ===");

        // Matrix Benchmarks
        SimpleMatrixBenchmark.run();

        // FFT Benchmarks
        FFTBenchmark.run();

    }
}
