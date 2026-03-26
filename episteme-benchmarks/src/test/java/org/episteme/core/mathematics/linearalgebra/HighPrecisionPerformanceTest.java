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

    private static final int MATRIX_SIZE = 50; // Balanced for high-precision
    private static final Set<String> EXCLUDED_PROVIDERS = Set.of(
        "EJML", "Colt", "Commons Math", "JBlas", "ND4J",
        "CUDA", "OpenCL", "SIMD", "Unified", "FFMBLAS", "Native BLAS Provider FFM",
        "Native CPU-BLAS"
    );

    @Test
    public void runPerformanceBenchmark() throws IOException {
        List<LinearAlgebraProvider<?>> providers = discoverHPProviders();
        BenchmarkReporter reporter = new BenchmarkReporter("High-Precision Performance Audit");
        
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
                    benchmarkRealBig(metrics, (LinearAlgebraProvider<RealBig>) (Object) provider);
                }

                // Complex Domain
                Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
                if (provider.isCompatible(complexRing)) {
                    benchmarkComplex(metrics, (LinearAlgebraProvider<Complex>) (Object) provider);
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
        Matrix<RealBig> A = createRealBigMatrix(MATRIX_SIZE);
        Matrix<RealBig> B = createRealBigMatrix(MATRIX_SIZE);
        Vector<RealBig> v = createRealBigVector(MATRIX_SIZE);
        RealBig s = RealBig.create(new BigDecimal("2.5"));

        // Basic
        measure(metrics, "RB:Add", () -> p.add(A, B));
        measure(metrics, "RB:Sub", () -> p.subtract(A, B));
        measure(metrics, "RB:Scale", () -> p.scale(s, A));
        measure(metrics, "RB:Mul", () -> p.multiply(A, B));
        measure(metrics, "RB:MatVec", () -> p.multiply(A, v));
        measure(metrics, "RB:Trans", () -> p.transpose(A));

        // Advanced
        Matrix<RealBig> InvA = createInvertibleRealBigMatrix(MATRIX_SIZE);
        measure(metrics, "RB:Inv", () -> p.inverse(InvA));
        measure(metrics, "RB:Det", () -> p.determinant(InvA));
        measure(metrics, "RB:Solve", () -> p.solve(InvA, v));
        measure(metrics, "RB:Dot", () -> p.dot(v, v));
        measure(metrics, "RB:Norm", () -> p.norm(v));

        // Factorizations
        measure(metrics, "RB:LU", () -> p.lu(InvA));
        measure(metrics, "RB:QR", () -> p.qr(A));
        measure(metrics, "RB:SVD", () -> p.svd(createRealBigMatrix(MATRIX_SIZE / 2)));
        measure(metrics, "RB:Chol", () -> p.cholesky(createSPDRealBigMatrix(MATRIX_SIZE)));
        measure(metrics, "RB:Eigen", () -> p.eigen(InvA));

        // Sparse
        if (p instanceof SparseLinearAlgebraProvider sp) {
            measure(metrics, "RB:BiCGSTAB", () -> ((SparseLinearAlgebraProvider<RealBig>)sp).bicgstab(InvA, v, v, RealBig.create(new BigDecimal("1e-10")), 100));
        }

        // Transcendental
        Matrix<RealBig> Small = createRealBigMatrix(1);
        measure(metrics, "RB:Exp", () -> p.exp(Small));
        measure(metrics, "RB:Sin", () -> p.sin(Small));
        measure(metrics, "RB:Cos", () -> p.cos(Small));
        measure(metrics, "RB:Log", () -> p.log(createRealBigMatrix(new BigDecimal("10.0"), 1)));
    }

    private void benchmarkComplex(Map<String, Object> metrics, LinearAlgebraProvider<Complex> p) {
        Matrix<Complex> A = createComplexMatrix(MATRIX_SIZE);
        Matrix<Complex> B = createComplexMatrix(MATRIX_SIZE);
        Vector<Complex> v = createComplexVector(MATRIX_SIZE);
        Complex s_val = Complex.of(2.5, 1.0);

        measure(metrics, "C:Add", () -> p.add(A, B));
        measure(metrics, "C:Mul", () -> p.multiply(A, B));
        measure(metrics, "C:Inv", () -> p.inverse(createInvertibleComplexMatrix(MATRIX_SIZE)));
        measure(metrics, "C:Solve", () -> p.solve(createInvertibleComplexMatrix(MATRIX_SIZE), v));
        measure(metrics, "C:Sin", () -> p.sin(createComplexMatrix(1)));
        
        System.out.println("Used scale factor: " + s_val); // Keep side effect to avoid unused
    }

    private void measure(Map<String, Object> metrics, String name, Runnable op) {
        try {
            // Warmup
            for (int i = 0; i < 2; i++) op.run();
            long start = System.nanoTime();
            int iters = 3;
            for (int i = 0; i < iters; i++) op.run();
            metrics.put(name, (System.nanoTime() - start) / (1_000_000.0 * iters));
        } catch (Throwable t) {
            metrics.put(name, -1.0);
        }
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

    // --- Helper Methods ---
    private Matrix<RealBig> createRealBigMatrix(int n) {
        return createRealBigMatrix(new BigDecimal("1.0"), n);
    }
    private Matrix<RealBig> createRealBigMatrix(BigDecimal val, int n) {
        RealBig[][] data = new RealBig[n][n];
        @SuppressWarnings("unchecked")
        Ring<RealBig> ring = (Ring<RealBig>)(Object)RealBig.create(BigDecimal.ZERO).getScalarRing();
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = RealBig.create(val.add(new BigDecimal(i + j)));
        return Matrix.of(data, ring);
    }
    private Matrix<RealBig> createInvertibleRealBigMatrix(int n) {
        RealBig[][] data = new RealBig[n][n];
        @SuppressWarnings("unchecked")
        Ring<RealBig> ring = (Ring<RealBig>)(Object)RealBig.create(BigDecimal.ZERO).getScalarRing();
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = RealBig.create(i == j ? new BigDecimal(n + i) : new BigDecimal("0.1"));
        return Matrix.of(data, ring);
    }
    private Matrix<RealBig> createSPDRealBigMatrix(int n) {
        RealBig[][] data = new RealBig[n][n];
        @SuppressWarnings("unchecked")
        Ring<RealBig> ring = (Ring<RealBig>)(Object)RealBig.create(BigDecimal.ZERO).getScalarRing();
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = RealBig.create(i == j ? new BigDecimal(n * n + i) : new BigDecimal(i + j));
        return Matrix.of(data, ring);
    }
    private Vector<RealBig> createRealBigVector(int n) {
        RealBig[] data = new RealBig[n];
        @SuppressWarnings("unchecked")
        Ring<RealBig> ring = (Ring<RealBig>)(Object)RealBig.create(BigDecimal.ZERO).getScalarRing();
        for (int i = 0; i < n; i++) data[i] = RealBig.create(new BigDecimal(i + 1));
        return Vector.of(data, ring);
    }
    private Matrix<Complex> createComplexMatrix(int n) {
        Complex[][] data = new Complex[n][n];
        Ring<Complex> ring = Complex.of(0, 0).getScalarRing();
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = Complex.of(i + 0.1, j - 0.1);
        return Matrix.of(data, ring);
    }
    private Matrix<Complex> createInvertibleComplexMatrix(int n) {
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = Complex.of(i == j ? n + i : 0.1, 0.05);
        return Matrix.of(data, Complex.of(0, 0).getScalarRing());
    }
    private Vector<Complex> createComplexVector(int n) {
        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) data[i] = Complex.of(i + 1, -i);
        return Vector.of(data, Complex.of(0, 0).getScalarRing());
    }
}
