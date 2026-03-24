/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Systematic compliance test for High-Precision LinearAlgebraProvider implementations.
 * Tests both RealBig (arbitrary-precision) and Complex domains across all standard operations.
 * 
 * <p>Providers that only support {@code double} (EJML, Colt, Commons Math, JBlas, ND4J)
 * are excluded since they cannot operate on BigReal or Complex matrix types.</p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class HighPrecisionComplianceTest {

    private static final String PROJECT_NAME = System.getProperty("org.episteme.project.name", "Episteme");
    private static final String REPORT_PATH = System.getProperty("org.episteme.report.path", "../docs/HIGH_PRECISION_COMPLIANCE_REPORT.md");

    // Providers to exclude from HP tests (double-only, broken, or unused)
    private static final Set<String> EXCLUDED_PROVIDERS = Set.of(
        "EJML", "Colt", "Commons Math", "JBlas", "ND4J",
        "CUDA", "OpenCL", "SIMD", "Unified", "FFMBLAS", "Native BLAS Provider FFM"
    );

    private static class ComplianceResult {
        String providerName;
        String environment;
        Map<String, String> status = new LinkedHashMap<>();
    }
    @Test
    public void generateHighPrecisionReport() {
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(REPORT_PATH));
        } catch (IOException ignored) {}

        List<LinearAlgebraProvider<?>> providers = discoverHPProviders();
        List<ComplianceResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<?> rawProvider : providers) {
            System.out.println("Starting HP compliance tests for: " + rawProvider.getName());
            ComplianceResult res = new ComplianceResult();
            res.providerName = rawProvider.getName();
            
            if (!rawProvider.isAvailable()) {
                res.environment = "DISABLED";
                results.add(res);
                continue;
            }
            res.environment = rawProvider.getEnvironmentInfo();

            MathContext.exact().compute(() -> {
                RealBig rbVal = RealBig.create(BigDecimal.ONE);
                @SuppressWarnings("unchecked")
                Ring<RealBig> ring = (Ring<RealBig>) (Object) rbVal.getScalarRing();
                if (rawProvider.isCompatible(ring)) {
                    @SuppressWarnings("unchecked")
                    LinearAlgebraProvider<RealBig> provider = (LinearAlgebraProvider<RealBig>) rawProvider;
                    runRealBigTests(res, provider);
                }
                return null;
            });

            // === COMPLEX DOMAIN ===
            Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
            if (rawProvider.isCompatible(complexRing)) {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Complex> provider = (LinearAlgebraProvider<Complex>) rawProvider;
                runComplexTests(res, provider);
            }

            results.add(res);
        }

        printMarkdownReport(results);
    }

    private void runRealBigTests(ComplianceResult res, LinearAlgebraProvider<RealBig> provider) {
        RealBig val = RealBig.create(new BigDecimal("0.12345678901234567890123456789012345678901234567890"));
        RealBig val2 = RealBig.create(new BigDecimal("2.71828182845904523536028747135266249775724709369995"));

        testOp(res, "RB:Add", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 3);
            Matrix<RealBig> b = createRealBigMatrix(val2, 3);
            Matrix<RealBig> c = provider.add(a, b);
            assertNoFallback(provider, c);
        });

        testOp(res, "RB:Sub", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 3);
            Matrix<RealBig> b = createRealBigMatrix(val2, 3);
            Matrix<RealBig> c = provider.subtract(a, b);
            assertNoFallback(provider, c);
        });

        testOp(res, "RB:Scale", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 3);
            Matrix<RealBig> c = provider.scale(val2, a);
            assertNoFallback(provider, c);
        });

        testOp(res, "RB:Mul", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 3);
            Matrix<RealBig> b = createRealBigMatrix(val2, 3);
            Matrix<RealBig> c = provider.multiply(a, b);
            assertNoFallback(provider, c);
        });

        testOp(res, "RB:MatVec", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 3);
            Vector<RealBig> v = createRealBigVector(val2, 3);
            Vector<RealBig> r = provider.multiply(a, v);
            assertNoFallback(provider, r);
        });

        testOp(res, "RB:Trans", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 3);
            Matrix<RealBig> t = provider.transpose(a);
            assertNoFallback(provider, t);
        });

        testOp(res, "RB:Inv", provider, () -> {
            Matrix<RealBig> a = createInvertibleRealBigMatrix(3);
            Matrix<RealBig> inv = provider.inverse(a);
            assertNoFallback(provider, inv);
        });

        testOp(res, "RB:Det", provider, () -> {
            Matrix<RealBig> a = createInvertibleRealBigMatrix(3);
            RealBig det = provider.determinant(a);
            assertNotNull(det);
        });

        testOp(res, "RB:Solve", provider, () -> {
            Matrix<RealBig> a = createInvertibleRealBigMatrix(3);
            Vector<RealBig> b = createRealBigVector(val, 3);
            Vector<RealBig> x = provider.solve(a, b);
            assertNoFallback(provider, x);
        });

        testOp(res, "RB:Dot", provider, () -> {
            Vector<RealBig> a = createRealBigVector(val, 4);
            Vector<RealBig> b = createRealBigVector(val2, 4);
            RealBig d = provider.dot(a, b);
            assertNotNull(d);
        });

        testOp(res, "RB:Norm", provider, () -> {
            Vector<RealBig> a = createRealBigVector(val, 4);
            RealBig n = provider.norm(a);
            assertNotNull(n);
        });

        testOp(res, "RB:LU", provider, () -> {
            Matrix<RealBig> a = createInvertibleRealBigMatrix(3);
            LUResult<RealBig> lu = provider.lu(a);
            assertNotNull(lu);
        });

        testOp(res, "RB:QR", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 3);
            QRResult<RealBig> qr = provider.qr(a);
            assertNotNull(qr);
        });

        testOp(res, "RB:SVD", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 3);
            SVDResult<RealBig> svd = provider.svd(a);
            assertNotNull(svd);
        });

        testOp(res, "RB:Chol", provider, () -> {
            Matrix<RealBig> a = createSPDRealBigMatrix(3);
            CholeskyResult<RealBig> chol = provider.cholesky(a);
            assertNotNull(chol);
        });

        testOp(res, "RB:Eigen", provider, () -> {
            Matrix<RealBig> a = createInvertibleRealBigMatrix(3);
            EigenResult<RealBig> eig = provider.eigen(a);
            assertNotNull(eig);
        });

        testOp(res, "RB:BiCGSTAB", provider, () -> {
            if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
            SparseLinearAlgebraProvider<RealBig> sp = (SparseLinearAlgebraProvider<RealBig>) provider;
            Matrix<RealBig> a = createInvertibleRealBigMatrix(4);
            Vector<RealBig> b = createRealBigVector(val, 4);
            Vector<RealBig> x0 = createRealBigVector(RealBig.create(BigDecimal.ZERO), 4);
            Vector<RealBig> x = sp.bicgstab(a, b, x0, RealBig.create(new BigDecimal("1e-8")), 1000);
            assertNotNull(x);
        });

        testOp(res, "RB:ConjGrad", provider, () -> {
            if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
            SparseLinearAlgebraProvider<RealBig> sp = (SparseLinearAlgebraProvider<RealBig>) provider;
            Matrix<RealBig> a = createSPDRealBigMatrix(4);
            Vector<RealBig> b = createRealBigVector(val, 4);
            Vector<RealBig> x0 = createRealBigVector(RealBig.create(BigDecimal.ZERO), 4);
            Vector<RealBig> x = sp.conjugateGradient(a, b, x0, RealBig.create(new BigDecimal("1e-8")), 1000);
            assertNotNull(x);
        });

        testOp(res, "RB:GMRES", provider, () -> {
            if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
            SparseLinearAlgebraProvider<RealBig> sp = (SparseLinearAlgebraProvider<RealBig>) provider;
            Matrix<RealBig> a = createInvertibleRealBigMatrix(4);
            Vector<RealBig> b = createRealBigVector(val, 4);
            Vector<RealBig> x0 = createRealBigVector(RealBig.create(BigDecimal.ZERO), 4);
            Vector<RealBig> x = sp.gmres(a, b, x0, RealBig.create(new BigDecimal("1e-8")), 1000, 10);
            assertNotNull(x);
        });

        testOp(res, "RB:Exp", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 1);
            Matrix<RealBig> c = provider.exp(a);
            assertNoFallback(provider, c);
        });

        testOp(res, "RB:Sin", provider, () -> {
            Matrix<RealBig> a = createRealBigMatrix(val, 1);
            Matrix<RealBig> c = provider.sin(a);
            assertNoFallback(provider, c);
        });
    }

    private void runComplexTests(ComplianceResult res, LinearAlgebraProvider<Complex> provider) {
        testOp(res, "C:Add", provider, () -> {
            Matrix<Complex> a = createComplexMatrix(Complex.of(1.5, 2.3), 3);
            Matrix<Complex> b = createComplexMatrix(Complex.of(0.7, -1.1), 3);
            Matrix<Complex> c = provider.add(a, b);
            assertNoFallback(provider, c);
        });

        testOp(res, "C:Sub", provider, () -> {
            Matrix<Complex> a = createComplexMatrix(Complex.of(1.5, 2.3), 3);
            Matrix<Complex> b = createComplexMatrix(Complex.of(0.7, -1.1), 3);
            Matrix<Complex> c = provider.subtract(a, b);
            assertNoFallback(provider, c);
        });

        testOp(res, "C:Scale", provider, () -> {
            Matrix<Complex> a = createComplexMatrix(Complex.of(1.5, 2.3), 3);
            Matrix<Complex> c = provider.scale(Complex.of(2.0, -1.0), a);
            assertNoFallback(provider, c);
        });

        testOp(res, "C:Mul", provider, () -> {
            Matrix<Complex> a = createComplexMatrix(Complex.of(1.0, 1.0), 3);
            Matrix<Complex> b = createComplexMatrix(Complex.of(0.5, -0.5), 3);
            Matrix<Complex> c = provider.multiply(a, b);
            assertNoFallback(provider, c);
        });

        testOp(res, "C:MatVec", provider, () -> {
            Matrix<Complex> a = createComplexMatrix(Complex.of(1.0, 1.0), 3);
            Vector<Complex> v = createComplexVector(Complex.of(2.0, -1.0), 3);
            Vector<Complex> r = provider.multiply(a, v);
            assertNoFallback(provider, r);
        });

        testOp(res, "C:Trans", provider, () -> {
            Matrix<Complex> a = createComplexMatrix(Complex.of(1.5, 2.3), 3);
            Matrix<Complex> t = provider.transpose(a);
            assertNoFallback(provider, t);
        });

        testOp(res, "C:Inv", provider, () -> {
            Matrix<Complex> a = createInvertibleComplexMatrix(3);
            Matrix<Complex> inv = provider.inverse(a);
            assertNoFallback(provider, inv);
        });

        testOp(res, "C:Det", provider, () -> {
            Matrix<Complex> a = createInvertibleComplexMatrix(3);
            Complex det = provider.determinant(a);
            assertNotNull(det);
        });

        testOp(res, "C:Solve", provider, () -> {
            Matrix<Complex> a = createInvertibleComplexMatrix(3);
            Vector<Complex> b = createComplexVector(Complex.of(1.0, 2.0), 3);
            Vector<Complex> x = provider.solve(a, b);
            assertNoFallback(provider, x);
        });

        testOp(res, "C:Dot", provider, () -> {
            Vector<Complex> a = createComplexVector(Complex.of(1.0, 1.0), 4);
            Vector<Complex> b = createComplexVector(Complex.of(2.0, -1.0), 4);
            Complex d = provider.dot(a, b);
            assertNotNull(d);
        });

        testOp(res, "C:Norm", provider, () -> {
            Vector<Complex> a = createComplexVector(Complex.of(1.0, 1.0), 4);
            Complex n = provider.norm(a);
            assertNotNull(n);
        });

        testOp(res, "C:LU", provider, () -> {
            Matrix<Complex> a = createInvertibleComplexMatrix(3);
            LUResult<Complex> lu = provider.lu(a);
            assertNotNull(lu);
        });

        testOp(res, "C:QR", provider, () -> {
            Matrix<Complex> a = createComplexMatrix(Complex.of(1.0, 1.0), 3);
            QRResult<Complex> qr = provider.qr(a);
            assertNotNull(qr);
        });

        testOp(res, "C:SVD", provider, () -> {
            Matrix<Complex> a = createComplexMatrix(Complex.of(1.0, 1.0), 3);
            SVDResult<Complex> svd = provider.svd(a);
            assertNotNull(svd);
        });

        testOp(res, "C:Chol", provider, () -> {
            Matrix<Complex> a = createSPDComplexMatrix(3);
            CholeskyResult<Complex> chol = provider.cholesky(a);
            assertNotNull(chol);
        });

        testOp(res, "C:Eigen", provider, () -> {
            Matrix<Complex> a = createInvertibleComplexMatrix(3);
            EigenResult<Complex> eig = provider.eigen(a);
            assertNotNull(eig);
        });

        testOp(res, "C:BiCGSTAB", provider, () -> {
            if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
            SparseLinearAlgebraProvider<Complex> sp = (SparseLinearAlgebraProvider<Complex>) provider;
            Matrix<Complex> a = createInvertibleComplexMatrix(4);
            Vector<Complex> b = createComplexVector(Complex.of(1.0, 2.0), 4);
            Vector<Complex> x0 = createComplexVector(Complex.of(0.0, 0.0), 4);
            Vector<Complex> x = sp.bicgstab(a, b, x0, Complex.of(1e-8, 0), 1000);
            assertNotNull(x);
        });

        testOp(res, "C:ConjGrad", provider, () -> {
            if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
            SparseLinearAlgebraProvider<Complex> sp = (SparseLinearAlgebraProvider<Complex>) provider;
            Matrix<Complex> a = createSPDComplexMatrix(4);
            Vector<Complex> b = createComplexVector(Complex.of(1.0, 2.0), 4);
            Vector<Complex> x0 = createComplexVector(Complex.of(0.0, 0.0), 4);
            Vector<Complex> x = sp.conjugateGradient(a, b, x0, Complex.of(1e-8, 0), 1000);
            assertNotNull(x);
        });

        testOp(res, "C:GMRES", provider, () -> {
            if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
            SparseLinearAlgebraProvider<Complex> sp = (SparseLinearAlgebraProvider<Complex>) provider;
            Matrix<Complex> a = createInvertibleComplexMatrix(4);
            Vector<Complex> b = createComplexVector(Complex.of(1.0, 2.0), 4);
            Vector<Complex> x0 = createComplexVector(Complex.of(0.0, 0.0), 4);
            Vector<Complex> x = sp.gmres(a, b, x0, Complex.of(1e-8, 0), 1000, 10);
            assertNotNull(x);
        });
    }

    // --- Provider Discovery ---
    private List<LinearAlgebraProvider<?>> discoverHPProviders() {
        Map<String, LinearAlgebraProvider<?>> providers = new LinkedHashMap<>();
        
        @SuppressWarnings("rawtypes")
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> prov : loader) {
            if (isExcludedProvider(prov.getName())) continue;
            providers.put(prov.getName(), prov);
        }

        try {
            for (Backend b : BackendDiscovery.getInstance().getProviders()) {
                try {
                    for (var ap : b.getAlgorithmProviders()) {
                        if (ap instanceof LinearAlgebraProvider<?> p) {
                            if (isExcludedProvider(p.getName())) continue;
                            providers.putIfAbsent(p.getName(), p);
                        }
                    }
                } catch (Throwable t) {
                    System.err.println("Warning: Could not load providers for backend " + b.getName() + ": " + t.getMessage());
                }
            }
        } catch (Throwable t) {
            System.err.println("Warning: Backend discovery failed: " + t.getMessage());
        }
        return new ArrayList<>(providers.values());
    }

    private boolean isExcludedProvider(String name) {
        for (String dop : EXCLUDED_PROVIDERS) {
            if (name.contains(dop)) return true;
        }
        return false;
    }

    // --- Test Infrastructure ---
    private <E> void assertNoFallback(LinearAlgebraProvider<E> expectedProvider, Object result) {
        if (result instanceof Matrix<?> m && m.getProvider() != null) {
            String expected = expectedProvider.getClass().getSimpleName();
            String actual = m.getProvider().getClass().getSimpleName();
            if (!actual.equals(expected)) {
                throw new UnsupportedOperationException("Fallback: expected " + expected + " got " + actual);
            }
        } else if (result instanceof Vector<?> v && v.getProvider() != null) {
            String expected = expectedProvider.getClass().getSimpleName();
            String actual = v.getProvider().getClass().getSimpleName();
            if (!actual.equals(expected)) {
                throw new UnsupportedOperationException("Fallback: expected " + expected + " got " + actual);
            }
        }
    }

    private <E> void testOp(ComplianceResult res, String opName, LinearAlgebraProvider<E> provider, Runnable test) {
        try {
            test.run();
            res.status.put(opName, "✅ PASS");
        } catch (UnsupportedOperationException e) {
            res.status.put(opName, "❌ N/A");
        } catch (Throwable e) {
            // Find root cause
            Throwable root = e;
            while (root.getCause() != null && root.getCause() != root) root = root.getCause();
            
            String className = root.getClass().getSimpleName();
            String msg = root.getMessage();
            String label = className;
            if (msg != null && !msg.isBlank()) {
                String cleanMsg = msg.replace("\n", " ").replace("|", "/");
                if (cleanMsg.length() > 40) cleanMsg = cleanMsg.substring(0, 37) + "...";
                label += ": " + cleanMsg;
            }
            res.status.put(opName, "⚠️ FAIL (" + label + ")");
        }
    }

    // --- Matrix/Vector Creation ---
    private Matrix<RealBig> createRealBigMatrix(RealBig val, int n) {
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = RealBig.create(new BigDecimal(String.valueOf((i + 1) * 0.1 + (j + 1) * 0.01)).add(val.bigDecimalValue()));
            }
        }
        Ring<RealBig> ring = (Ring<RealBig>) (Object) val.getScalarRing();
        return Matrix.of(data, ring);
    }

    private Matrix<RealBig> createInvertibleRealBigMatrix(int n) {
        // Diagonally dominant for invertibility
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    data[i][j] = RealBig.create(new BigDecimal(String.valueOf(n * 2.0 + i)));
                } else {
                    data[i][j] = RealBig.create(new BigDecimal(String.valueOf(0.1 * (i + j + 1))));
                }
            }
        }
        Ring<RealBig> ring = (Ring<RealBig>) (Object) data[0][0].getScalarRing();
        return Matrix.of(data, ring);
    }

    private Matrix<RealBig> createSPDRealBigMatrix(int n) {
        // A^T * A + nI for symmetric positive definite
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += (0.1 * (k + i + 1)) * (0.1 * (k + j + 1));
                }
                if (i == j) sum += n;
                data[i][j] = RealBig.create(new BigDecimal(String.valueOf(sum)));
            }
        }
        Ring<RealBig> ring = (Ring<RealBig>) (Object) data[0][0].getScalarRing();
        return Matrix.of(data, ring);
    }

    private Vector<RealBig> createRealBigVector(RealBig val, int n) {
        RealBig[] data = new RealBig[n];
        for (int i = 0; i < n; i++) {
            data[i] = RealBig.create(new BigDecimal(String.valueOf((i + 1) * 0.5)).add(val.bigDecimalValue()));
        }
        Ring<RealBig> ring = (Ring<RealBig>) (Object) val.getScalarRing();
        return Vector.of(data, ring);
    }

    private Matrix<Complex> createComplexMatrix(Complex z, int n) {
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = Complex.of(z.getReal().doubleValue() + i * 0.1, z.getImaginary().doubleValue() + j * 0.1);
            }
        }
        return Matrix.of(data, z.getScalarRing());
    }

    private Matrix<Complex> createInvertibleComplexMatrix(int n) {
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    data[i][j] = Complex.of(n * 2.0 + i, 0.1);
                } else {
                    data[i][j] = Complex.of(0.1 * (i + j + 1), 0.05);
                }
            }
        }
        return Matrix.of(data, Complex.of(0, 0).getScalarRing());
    }

    private Matrix<Complex> createSPDComplexMatrix(int n) {
        // Hermitian positive definite
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    data[i][j] = Complex.of(n * 3.0 + i, 0.0);
                } else {
                    data[i][j] = Complex.of(0.1 * (i + j + 1), i > j ? 0.05 : -0.05);
                }
            }
        }
        return Matrix.of(data, Complex.of(0, 0).getScalarRing());
    }

    private Vector<Complex> createComplexVector(Complex z, int n) {
        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) {
            data[i] = Complex.of(z.getReal().doubleValue() + i * 0.5, z.getImaginary().doubleValue() - i * 0.3);
        }
        return Vector.of(data, z.getScalarRing());
    }

    // --- Report Generation ---
    private void printMarkdownReport(List<ComplianceResult> results) {
        if (results.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(PROJECT_NAME).append(" High-Precision Compliance Report\n\n");

        Set<String> ops = new LinkedHashSet<>();
        for (var r : results) ops.addAll(r.status.keySet());

        sb.append("| Provider | Environment |");
        for (String op : ops) sb.append(" ").append(op).append(" |");
        sb.append("\n| --- | --- |").append(" --- |".repeat(ops.size())).append("\n");

        for (var r : results) {
            sb.append("| ").append(r.providerName).append(" | ").append(r.environment).append(" |");
            for (String op : ops) {
                sb.append(" ").append(r.status.getOrDefault(op, "N/A")).append(" |");
            }
            sb.append("\n");
        }
        sb.append("\n*Generated by HighPrecisionComplianceTest on ").append(new Date()).append("*\n");

        String report = sb.toString();
        System.out.println(report);

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(REPORT_PATH);
            if (path.getParent() != null) {
                java.nio.file.Files.createDirectories(path.getParent());
            }
            java.nio.file.Files.writeString(path, report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
