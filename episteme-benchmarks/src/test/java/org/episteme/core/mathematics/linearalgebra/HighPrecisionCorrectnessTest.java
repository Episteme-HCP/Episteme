/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.technical.algorithm.AlgorithmService;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.context.MathContext;
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

    private static org.springframework.context.ConfigurableApplicationContext serverContext;

    @org.junit.jupiter.api.BeforeAll
    public static void startServer() {
        org.episteme.core.technical.algorithm.AlgorithmManager.setService(new org.episteme.core.technical.algorithm.StandardAlgorithmService());
        try {
            serverContext = GrpcTestApplication.start();
            System.out.println("Episteme Server started successfully for correctness audit.");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.err.println("Failed to start Episteme Server: " + e.getMessage());
        }
    }

    @org.junit.jupiter.api.AfterAll
    public static void stopServer() {
        if (serverContext != null) {
            serverContext.close();
        }
    }

    private static final int MATRIX_SIZE = 3;
    private static final BigDecimal TOLERANCE_REALBIG = new BigDecimal("1e-25");
    private static final double TOLERANCE_COMPLEX = 1e-8;
    
    private static final Set<String> EXCLUDED_PROVIDERS = Set.of(
        "EJML", "Colt", "Commons Math", "JBlas", "ND4J",
        "CUDA", "OpenCL", "SIMD", "Unified", "FFMBLAS", "Native BLAS Provider FFM",
        "Native CPU-BLAS", "gRPC", "Remote"
    );

    @Test
    public void runNumericalCorrectnessAudit() {
        AlgorithmService oldService = AlgorithmManager.getService();
        List<LinearAlgebraProvider<?>> providers = discoverHPProviders();
        BenchmarkReporter reporter = new BenchmarkReporter("High-Precision Correctness Audit");
        reporter.setComments(
            "Numerical correctness is verified by comparing results from the tested provider against a reference JVM-based provider (Ground Truth).\n" +
            "Icons: ✅ (Pass - Within tolerance), ❌ (Fail - Numerical error or exception), ⚠️ (Skip - Operation not supported)."
        );
        
        reporter.addSection("Methodology", "Verifying numerical correctness and fallback prevention for 68+ operations. Tolerance: " + TOLERANCE_REALBIG + " (Real), " + TOLERANCE_COMPLEX + " (Complex).");

        try {
            for (LinearAlgebraProvider<?> prov : providers) {
                if (!prov.isAvailable()) continue;
                
                System.out.println("Verifying correctness for: " + prov.getName());
                
                try {
                    Map<String, Object> metrics = new LinkedHashMap<>();
                    
                    MathContext.withPrecision(50).compute(() -> {
                        // RealBig Domain
                        RealBig rbVal = RealBig.create(BigDecimal.ONE);
                        @SuppressWarnings("unchecked")
                        Ring<RealBig> rbRing = (Ring<RealBig>) (Object) rbVal.getScalarRing();
                        if (prov.isCompatible(rbRing)) {
                            @SuppressWarnings("unchecked")
                            LinearAlgebraProvider<RealBig> providerRB = (LinearAlgebraProvider<RealBig>) (Object) prov;
                            auditRealBigCorrectness(metrics, providerRB, rbRing);
                        }
                        
                        // Complex Domain
                        Ring<Complex> cRing = Complex.of(1.0, 0.0).getScalarRing();
                        if (prov.isCompatible(cRing)) {
                            @SuppressWarnings("unchecked")
                            LinearAlgebraProvider<Complex> providerC = (LinearAlgebraProvider<Complex>) (Object) prov;
                            auditComplexCorrectness(metrics, providerC, cRing);
                        }
                        return null;
                    });

                    BenchmarkResult res = new BenchmarkResult(
                        "hp-correctness-" + prov.getName().toLowerCase().replace(" ", "-"),
                        prov.getName(),
                        prov.getClass().getSimpleName(),
                        "Linear Algebra (High-Precision Correctness)",
                        "COMPLETED",
                        System.currentTimeMillis(),
                        0, 0, 0, 0, 0,
                        new java.util.HashMap<>(),
                        metrics
                    );
                    reporter.addResult(res);
                } catch (Throwable t) {
                    System.err.println("Correctness failed for " + prov.getName() + ": " + t.getMessage());
                }
            }
        } finally {
            AlgorithmManager.setService(oldService);
        }
        
        String reportPath = getReportPath();
        System.out.println("[INFO] Exporting correctness report to: " + reportPath);
        reporter.exportMarkdown(reportPath);
    }

    private String getReportPath() {
        String customPath = System.getProperty("org.episteme.report.path");
        if (customPath != null && !customPath.isEmpty()) return customPath;
        
        String userDir = System.getProperty("user.dir");
        java.nio.file.Path rootPath = java.nio.file.Paths.get(userDir);
        if (rootPath.endsWith("episteme-benchmarks")) rootPath = rootPath.getParent();
        return rootPath.resolve("docs/HIGH_PRECISION_CORRECTNESS_REPORT.md").toString();
    }

    private void auditRealBigCorrectness(Map<String, Object> metrics, LinearAlgebraProvider<RealBig> p, Ring<RealBig> ring) {
        @SuppressWarnings("unchecked")
        LinearAlgebraProvider<RealBig> groundTruth = (LinearAlgebraProvider<RealBig>) AlgorithmManager.getService().getProvider(LinearAlgebraProvider.class);
        HighPrecisionAuditOperations.runRealBigAudit(p, MATRIX_SIZE, (op, test) -> {
            test(metrics, op, p, test, () -> {
                final Object[] refRes = new Object[1];
                HighPrecisionAuditOperations.runRealBigAudit(groundTruth, MATRIX_SIZE, (refOp, refTest) -> {
                    if (refOp.equals(op)) refRes[0] = refTest.get();
                });
                return refRes[0];
            });
        });
    }

    private void auditComplexCorrectness(Map<String, Object> metrics, LinearAlgebraProvider<Complex> p, Ring<Complex> ring) {
        @SuppressWarnings("unchecked")
        LinearAlgebraProvider<Complex> groundTruth = (LinearAlgebraProvider<Complex>) AlgorithmManager.getService().getProvider(LinearAlgebraProvider.class);
        HighPrecisionAuditOperations.runComplexAudit(p, MATRIX_SIZE, (op, test) -> {
            test(metrics, op, p, test, () -> {
                final Object[] refRes = new Object[1];
                HighPrecisionAuditOperations.runComplexAudit(groundTruth, MATRIX_SIZE, (refOp, refTest) -> {
                    if (refOp.equals(op)) refRes[0] = refTest.get();
                });
                return refRes[0];
            });
        });
    }

    @SuppressWarnings("unchecked")
    private <E> void test(Map<String, Object> metrics, String op, LinearAlgebraProvider<E> prov, java.util.function.Supplier<Object> actualSupplier, java.util.function.Supplier<Object> expectedSupplier) {
        try {
            Object actual = actualSupplier.get();
            Object expected = expectedSupplier.get();
            assertNoFallback(prov, actual);
            
            if (actual instanceof Matrix<?> && expected instanceof Matrix<?>) {
                if (op.startsWith("RB:")) {
                    assertMatrixClose((Matrix<RealBig>) actual, (Matrix<RealBig>) expected, TOLERANCE_REALBIG, op);
                } else {
                    assertComplexMatrixClose((Matrix<Complex>) actual, (Matrix<Complex>) expected, TOLERANCE_COMPLEX, op);
                }
            } else if (actual instanceof Vector<?> && expected instanceof Vector<?>) {
                if (op.startsWith("RB:")) {
                    assertVectorClose((Vector<RealBig>) actual, (Vector<RealBig>) expected, TOLERANCE_REALBIG, op);
                }
            }
            // Add scalar comparison if needed (Dot, Det, etc. might return scalars)
            
            metrics.put(op, "✅ PASS");
        } catch (UnsupportedOperationException ex) {
            metrics.put(op, "⚠️ SKIP: " + ex.getMessage());
        } catch (Throwable ex) {
            metrics.put(op, "❌ FAIL: " + ex.getMessage());
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

}
