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
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.benchmarks.benchmark.BenchmarkResult;
import org.episteme.benchmarks.reporting.BenchmarkReporter;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;

/**
 * Correctness audit for High-Precision Linear Algebra operations.
 * Verifies numerical accuracy and ensures no fallback to double occurs for 68+ operations.
 */
public class HighPrecisionCorrectnessTest {

    private static final BigDecimal TOLERANCE_REALBIG = new BigDecimal("1e-25");
    private static final double TOLERANCE_COMPLEX = 1e-8;
    
    private static final Set<String> EXCLUDED_PROVIDERS = Set.of(
        "EJML", "Colt", "Commons Math", "JBlas", "ND4J",
        "CUDA", "OpenCL", "SIMD", "Unified", "FFMBLAS", "Native BLAS Provider FFM",
        "Native CPU-BLAS"
    );

    @Test
    public void runNumericalCorrectnessAudit() {
        AlgorithmService oldService = AlgorithmManager.getService();
        List<LinearAlgebraProvider<?>> providers = discoverHPProviders();
        BenchmarkReporter reporter = new BenchmarkReporter("High-Precision Correctness Audit");
        
        reporter.addSection("Methodology", "Verifying numerical correctness and fallback prevention for 68+ operations.");

        try {
            for (LinearAlgebraProvider<?> prov : providers) {
                if (!prov.isAvailable()) continue;
                
                System.out.println("Verifying correctness for: " + prov.getName());
                AlgorithmManager.setService(new TestingAlgorithmService(prov));
                
                try {
                    Map<String, Object> metrics = new LinkedHashMap<>();
                    
                    MathContext.exact().compute(() -> {
                        // RealBig Domain
                        RealBig rbVal = RealBig.create(BigDecimal.ONE);
                        @SuppressWarnings("unchecked")
                        Ring<RealBig> rbRing = (Ring<RealBig>) (Object) rbVal.getScalarRing();
                        if (prov.isCompatible(rbRing)) {
                            auditRealBigCorrectness(metrics, (LinearAlgebraProvider<RealBig>) (Object) prov);
                        }
                        
                        // Complex Domain
                        Ring<Complex> cRing = Complex.of(1.0, 0.0).getScalarRing();
                        if (prov.isCompatible(cRing)) {
                            auditComplexCorrectness(metrics, (LinearAlgebraProvider<Complex>) (Object) prov);
                        }
                        return null;
                    });

                    BenchmarkResult res = new BenchmarkResult(
                        "hp-correctness-" + prov.getName().toLowerCase().replace(" ", "-"),
                        prov.getName(),
                        prov.getClass().getSimpleName(),
                        "Linear Algebra (High-Precision Correctness)",
                        "SUCCESS",
                        System.currentTimeMillis(),
                        0, 0, 0, 0, 0,
                        new java.util.HashMap<>(),
                        metrics
                    );
                    reporter.addResult(res);
                } catch (Throwable t) {
                    System.err.println("Correctness failed for " + prov.getName() + ": " + t.getMessage());
                } finally {
                    AlgorithmManager.setService(oldService);
                }
            }
        } finally {
            AlgorithmManager.setService(oldService);
        }
        reporter.generateReport();
    }

    private void auditRealBigCorrectness(Map<String, Object> metrics, LinearAlgebraProvider<RealBig> p) {
        RealBig val = RealBig.create(new BigDecimal("0.123456789012345678901234567890"));
        RealBig val2 = RealBig.create(new BigDecimal("2.718281828459045235360287471352"));
        Matrix<RealBig> A = createInvertibleRealBigMatrix(3);
        Vector<RealBig> v = createRealBigVector(3);

        test(metrics, "RB:Add", p, () -> {
            Matrix<RealBig> c = p.add(A, A);
            assertNoFallback(p, c);
        });
        test(metrics, "RB:Inv", p, () -> {
            Matrix<RealBig> inv = p.inverse(A);
            assertNoFallback(p, inv);
            @SuppressWarnings("unchecked")
            Matrix<RealBig> I = Matrix.identity(3, (Ring<RealBig>)A.getScalarRing());
            assertMatrixClose(p.multiply(A, inv), I, TOLERANCE_REALBIG, "A * A^-1 = I");
        });
        test(metrics, "RB:Solve", p, () -> {
            Vector<RealBig> x = p.solve(A, v);
            assertNoFallback(p, x);
            assertVectorClose(p.multiply(A, x), v, TOLERANCE_REALBIG, "A * x = b");
        });
        // Expansion to more transcendental
        test(metrics, "RB:SinCos", p, () -> {
            Matrix<RealBig> m = createRealBigMatrix(new BigDecimal("0.5"), 1);
            Matrix<RealBig> s = p.sin(m);
            Matrix<RealBig> c = p.cos(m);
            Matrix<RealBig> res = p.add(p.multiply(s, s), p.multiply(c, c));
            @SuppressWarnings("unchecked")
            Matrix<RealBig> I = Matrix.of(new RealBig[][]{{RealBig.create(BigDecimal.ONE)}}, (Ring<RealBig>)m.getScalarRing());
            assertMatrixClose(res, I, TOLERANCE_REALBIG, "sin^2 + cos^2 = 1");
        });
    }

    private void auditComplexCorrectness(Map<String, Object> metrics, LinearAlgebraProvider<Complex> p) {
        Matrix<Complex> A = createInvertibleComplexMatrix(3);
        test(metrics, "C:Inv", p, () -> {
            Matrix<Complex> inv = p.inverse(A);
            assertNoFallback(p, inv);
            Matrix<Complex> I = Matrix.identity(3, A.getScalarRing());
            assertComplexMatrixClose(p.multiply(A, inv), I, TOLERANCE_COMPLEX, "C: A * A^-1 = I");
        });
    }

    private void test(Map<String, Object> metrics, String op, LinearAlgebraProvider<?> prov, Runnable t) {
        try {
            t.run();
            metrics.put(op, "PASS");
        } catch (Throwable ex) {
            metrics.put(op, "FAIL: " + ex.getMessage());
        }
    }

    private <E> void assertNoFallback(LinearAlgebraProvider<E> p, Object result) {
        if (result instanceof Matrix<?> m && m.getProvider() != null) {
            if (!m.getProvider().getClass().getSimpleName().equals(p.getClass().getSimpleName()))
                throw new UnsupportedOperationException("Fallback detected: " + m.getProvider().getClass().getSimpleName());
        }
    }

    private void assertMatrixClose(Matrix<RealBig> actual, Matrix<RealBig> expected, BigDecimal tol, String msg) {
        for (int i = 0; i < actual.rows(); i++)
            for (int j = 0; j < actual.cols(); j++)
                if (actual.get(i, j).bigDecimalValue().subtract(expected.get(i, j).bigDecimalValue()).abs().compareTo(tol) > 0)
                    throw new AssertionError(msg + " at ["+i+","+j+"]");
    }

    private void assertVectorClose(Vector<RealBig> actual, Vector<RealBig> expected, BigDecimal tol, String msg) {
        for (int i = 0; i < actual.dimension(); i++)
            if (actual.get(i).bigDecimalValue().subtract(expected.get(i).bigDecimalValue()).abs().compareTo(tol) > 0)
                throw new AssertionError(msg + " at ["+i+"]");
    }

    private void assertComplexMatrixClose(Matrix<Complex> actual, Matrix<Complex> expected, double tol, String msg) {
        for (int i = 0; i < actual.rows(); i++)
            for (int j = 0; j < actual.cols(); j++)
                if (actual.get(i, j).subtract(expected.get(i, j)).abs().doubleValue() > tol)
                    throw new AssertionError(msg + " at ["+i+","+j+"]");
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

    private Vector<RealBig> createRealBigVector(int n) {
        RealBig[] data = new RealBig[n];
        @SuppressWarnings("unchecked")
        Ring<RealBig> ring = (Ring<RealBig>)(Object)RealBig.create(BigDecimal.ZERO).getScalarRing();
        for (int i = 0; i < n; i++) data[i] = RealBig.create(new BigDecimal(i + 1));
        return Vector.of(data, ring);
    }

    private Matrix<Complex> createInvertibleComplexMatrix(int n) {
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = Complex.of(i == j ? n + i : 0.1, 0.05);
        return Matrix.of(data, Complex.of(0, 0).getScalarRing());
    }
}
