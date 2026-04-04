/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.technical.algorithm.AlgorithmService;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.TestingAlgorithmService;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.benchmarks.benchmark.BenchmarkResult;
import org.episteme.benchmarks.reporting.BenchmarkReporter;


import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Performance benchmark for High-Precision Linear Algebra operations.
 * Measures execution time (ms) for 68+ operations across RealBig and Complex domains.
 */
public class HighPrecisionPerformanceTest {

    private static org.springframework.context.ConfigurableApplicationContext serverContext;

    @org.junit.jupiter.api.BeforeAll
    public static void startServer() {
        org.episteme.core.technical.algorithm.AlgorithmManager.setService(new org.episteme.core.technical.algorithm.StandardAlgorithmService());
        try {
            serverContext = GrpcTestApplication.start();
            System.out.println("Episteme gRPC Test Server started successfully for performance benchmarks.");
        } catch (Exception e) {
            System.err.println("Failed to start Episteme gRPC Test Server: " + e.getMessage());
        }
    }

    @org.junit.jupiter.api.AfterAll
    public static void stopServer() {
        if (serverContext != null) {
            serverContext.close();
        }
    }

    private static final int MATRIX_SIZE = 5; // Balanced for high-precision
    private static final Set<String> EXCLUDED_PROVIDERS = Set.of(
        "EJML", "Colt", "Commons Math", "JBlas", "ND4J",
        "CUDA", "OpenCL", "SIMD", "Unified", "FFMBLAS", "Native BLAS Provider FFM",
        "Native CPU-BLAS"
    );

    

    @Test
    public void runPerformanceBenchmark() throws IOException {
        List<LinearAlgebraProvider<?>> providers = discoverHPProviders();
        BenchmarkReporter reporter = new BenchmarkReporter("High-Precision");
        reporter.setComments(
            "High-precision performance audit evaluating 68+ operations across diverse numeric domains (RealBig, Complex).\n" +
            "Metrics represent both Throughput (Operations/second) and Latency (mean time in ms) for " + MATRIX_SIZE + "x" + MATRIX_SIZE + " matrices."
        );
        
        reporter.addSection("Methodology", "Measuring execution time (ms) for 68+ operations on " + MATRIX_SIZE + "x" + MATRIX_SIZE + " matrices.");

        for (LinearAlgebraProvider<?> provider : providers) {
            if (!provider.isAvailable()) continue;
            
            System.out.println("Benchmarking performance for: " + provider.getName());
            
            AlgorithmService oldService = AlgorithmManager.getService();
            AlgorithmManager.setService(new TestingAlgorithmService(provider));
            
            try {
                Map<String, Object> metrics = new LinkedHashMap<>();
                
                // RealBig Domain
                RealBig rbVal = RealBig.create(BigDecimal.ONE);
                @SuppressWarnings("unchecked")
                Ring<RealBig> rbRing = (Ring<RealBig>) (Object) rbVal.getScalarRing();
                if (provider.isCompatible(rbRing)) {
                    @SuppressWarnings("unchecked")
                    LinearAlgebraProvider<RealBig> rbProvider = (LinearAlgebraProvider<RealBig>) (Object) provider;
                    benchmarkRealBig(metrics, rbProvider);
                }

                // Complex Domain
                Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
                if (provider.isCompatible(complexRing)) {
                    @SuppressWarnings("unchecked")
                    LinearAlgebraProvider<Complex> cProvider = (LinearAlgebraProvider<Complex>) (Object) provider;
                    benchmarkComplex(metrics, cProvider);
                }

                BenchmarkResult res = new BenchmarkResult(
                    "hp-perf-" + provider.getName().toLowerCase().replace(" ", "-"),
                    provider.getName(),
                    provider.getClass().getSimpleName(),
                    "Linear Algebra (High-Precision Performance)",
                    "SUCCESS",
                    System.currentTimeMillis(),
                    0, 0, 0, 0, 0,
                    new java.util.HashMap<>(),
                    metrics
                );
                reporter.addResult(res);
            } catch (Throwable t) {
                System.err.println("Benchmark failed for " + provider.getName() + ": " + t.getMessage());
            } finally {
                AlgorithmManager.setService(oldService);
            }
        }
        reporter.generateReport();
    }

    private void benchmarkRealBig(Map<String, Object> metrics, LinearAlgebraProvider<RealBig> p) {
        HighPrecisionAuditOperations.runRealBigAudit(p, MATRIX_SIZE, (op, test) -> {
            try {
                // Warmup
                for (int i = 0; i < 5; i++) test.get();
                long start = System.nanoTime();
                int iters = 10;
                for (int i = 0; i < iters; i++) test.get();
                long end = System.nanoTime();
                double durationMs = (end - start) / (1_000_000.0 * iters);
                double throughput = 1000.0 / Math.max(durationMs, 1e-9);
                metrics.put(op + ":latency", durationMs);
                metrics.put(op + ":throughput", throughput);
            } catch (Throwable t) {
                metrics.put(op + ":latency", -1.0);
                metrics.put(op + ":throughput", 0.0);
            }
        });
    }

    private void benchmarkComplex(Map<String, Object> metrics, LinearAlgebraProvider<Complex> p) {
        HighPrecisionAuditOperations.runComplexAudit(p, MATRIX_SIZE, (op, test) -> {
            try {
                // Warmup
                for (int i = 0; i < 5; i++) test.get();
                long start = System.nanoTime();
                int iters = 10;
                for (int i = 0; i < iters; i++) test.get();
                long end = System.nanoTime();
                double durationMs = (end - start) / (1_000_000.0 * iters);
                double throughput = 1000.0 / Math.max(durationMs, 1e-9);
                metrics.put(op + ":latency", durationMs);
                metrics.put(op + ":throughput", throughput);
            } catch (Throwable t) {
                metrics.put(op + ":latency", -1.0);
                metrics.put(op + ":throughput", 0.0);
            }
        });
    }

    private List<LinearAlgebraProvider<?>> discoverHPProviders() {
        List<LinearAlgebraProvider<?>> list = new ArrayList<>();
        @SuppressWarnings("rawtypes")
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> p : loader) {
            String name = p.getName();
            boolean excluded = false;
            for (String ex : EXCLUDED_PROVIDERS) if (name.contains(ex)) excluded = true;
            if (!excluded) list.add(p);
        }
        return list;
    }

}
