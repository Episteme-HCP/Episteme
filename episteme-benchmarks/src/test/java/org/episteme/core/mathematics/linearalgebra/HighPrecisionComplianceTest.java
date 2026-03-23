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
import org.episteme.core.mathematics.sets.Reals;
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

    // Providers that only support double — exclude from HP tests
    private static final Set<String> DOUBLE_ONLY_PROVIDERS = Set.of(
        "EJML", "Colt", "Commons Math", "JBlas", "ND4J"
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

        List<LinearAlgebraProvider<Real>> providers = discoverHPProviders();
        List<ComplianceResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<Real> provider : providers) {
            System.out.println("Starting HP compliance tests for: " + provider.getName());
            ComplianceResult res = new ComplianceResult();
            res.providerName = provider.getName();
            
            if (!provider.isAvailable()) {
                res.environment = "DISABLED";
                results.add(res);
                continue;
            }
            res.environment = provider.getEnvironmentInfo();

            // === REALBIG DOMAIN ===
            MathContext.exact().compute(() -> {
                RealBig val = RealBig.create(new BigDecimal("0.12345678901234567890123456789012345678901234567890"));
                RealBig val2 = RealBig.create(new BigDecimal("2.71828182845904523536028747135266249775724709369995"));

                testOp(res, "RB:Add", provider, () -> {
                    Matrix<Real> a = createRealBigMatrix(val, 3);
                    Matrix<Real> b = createRealBigMatrix(val2, 3);
                    Matrix<Real> c = provider.add(a, b);
                    assertNoFallback(provider, c);
                });

                testOp(res, "RB:Sub", provider, () -> {
                    Matrix<Real> a = createRealBigMatrix(val, 3);
                    Matrix<Real> b = createRealBigMatrix(val2, 3);
                    Matrix<Real> c = provider.subtract(a, b);
                    assertNoFallback(provider, c);
                });

                testOp(res, "RB:Scale", provider, () -> {
                    Matrix<Real> a = createRealBigMatrix(val, 3);
                    Matrix<Real> c = provider.scale((Real) val2, a);
                    assertNoFallback(provider, c);
                });

                testOp(res, "RB:Mul", provider, () -> {
                    Matrix<Real> a = createRealBigMatrix(val, 3);
                    Matrix<Real> b = createRealBigMatrix(val2, 3);
                    Matrix<Real> c = provider.multiply(a, b);
                    assertNoFallback(provider, c);
                });

                testOp(res, "RB:MatVec", provider, () -> {
                    Matrix<Real> a = createRealBigMatrix(val, 3);
                    Vector<Real> v = createRealBigVector(val2, 3);
                    Vector<Real> r = provider.multiply(a, v);
                    assertNoFallback(provider, r);
                });

                testOp(res, "RB:Trans", provider, () -> {
                    Matrix<Real> a = createRealBigMatrix(val, 3);
                    Matrix<Real> t = provider.transpose(a);
                    assertNoFallback(provider, t);
                });

                testOp(res, "RB:Inv", provider, () -> {
                    Matrix<Real> a = createInvertibleRealBigMatrix(3);
                    Matrix<Real> inv = provider.inverse(a);
                    assertNoFallback(provider, inv);
                });

                testOp(res, "RB:Det", provider, () -> {
                    Matrix<Real> a = createInvertibleRealBigMatrix(3);
                    Real det = provider.determinant(a);
                    assertNotNull(det);
                });

                testOp(res, "RB:Solve", provider, () -> {
                    Matrix<Real> a = createInvertibleRealBigMatrix(3);
                    Vector<Real> b = createRealBigVector(val, 3);
                    Vector<Real> x = provider.solve(a, b);
                    assertNoFallback(provider, x);
                });

                testOp(res, "RB:Dot", provider, () -> {
                    Vector<Real> a = createRealBigVector(val, 4);
                    Vector<Real> b = createRealBigVector(val2, 4);
                    Real d = provider.dot(a, b);
                    assertNotNull(d);
                });

                testOp(res, "RB:Norm", provider, () -> {
                    Vector<Real> a = createRealBigVector(val, 4);
                    Real n = provider.norm(a);
                    assertNotNull(n);
                });

                testOp(res, "RB:LU", provider, () -> {
                    Matrix<Real> a = createInvertibleRealBigMatrix(3);
                    LUResult<Real> lu = provider.lu(a);
                    assertNotNull(lu);
                });

                testOp(res, "RB:QR", provider, () -> {
                    Matrix<Real> a = createRealBigMatrix(val, 3);
                    QRResult<Real> qr = provider.qr(a);
                    assertNotNull(qr);
                });

                testOp(res, "RB:SVD", provider, () -> {
                    Matrix<Real> a = createRealBigMatrix(val, 3);
                    SVDResult<Real> svd = provider.svd(a);
                    assertNotNull(svd);
                });

                testOp(res, "RB:Chol", provider, () -> {
                    Matrix<Real> a = createSPDRealBigMatrix(3);
                    CholeskyResult<Real> chol = provider.cholesky(a);
                    assertNotNull(chol);
                });

                testOp(res, "RB:Eigen", provider, () -> {
                    Matrix<Real> a = createInvertibleRealBigMatrix(3);
                    EigenResult<Real> eig = provider.eigen(a);
                    assertNotNull(eig);
                });

                testOp(res, "RB:BiCGSTAB", provider, () -> {
                    if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
                    SparseLinearAlgebraProvider<Real> sp = (SparseLinearAlgebraProvider<Real>) provider;
                    Matrix<Real> a = createInvertibleRealBigMatrix(4);
                    Vector<Real> b = createRealBigVector(val, 4);
                    Vector<Real> x0 = createRealBigVector(RealBig.create(BigDecimal.ZERO), 4);
                    Vector<Real> x = sp.bicgstab(a, b, x0, Real.of("1e-8"), 1000);
                    assertNotNull(x);
                });

                testOp(res, "RB:ConjGrad", provider, () -> {
                    if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
                    SparseLinearAlgebraProvider<Real> sp = (SparseLinearAlgebraProvider<Real>) provider;
                    Matrix<Real> a = createSPDRealBigMatrix(4);
                    Vector<Real> b = createRealBigVector(val, 4);
                    Vector<Real> x0 = createRealBigVector(RealBig.create(BigDecimal.ZERO), 4);
                    Vector<Real> x = sp.conjugateGradient(a, b, x0, Real.of("1e-8"), 1000);
                    assertNotNull(x);
                });

                testOp(res, "RB:GMRES", provider, () -> {
                    if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
                    SparseLinearAlgebraProvider<Real> sp = (SparseLinearAlgebraProvider<Real>) provider;
                    Matrix<Real> a = createInvertibleRealBigMatrix(4);
                    Vector<Real> b = createRealBigVector(val, 4);
                    Vector<Real> x0 = createRealBigVector(RealBig.create(BigDecimal.ZERO), 4);
                    Vector<Real> x = sp.gmres(a, b, x0, Real.of("1e-8"), 1000, 10);
                    assertNotNull(x);
                });

                // Transcendentals (element-level, not provider ops)
                testOp(res, "RB:Exp", provider, () -> {
                    Real v1 = Real.of("1.0");
                    if (v1 instanceof RealBig rb) {
                        Real exp = rb.exp();
                        assertNotNull(exp);
                    } else {
                        throw new UnsupportedOperationException("Not a high-precision type");
                    }
                });

                testOp(res, "RB:Sin", provider, () -> {
                    Real v1 = Real.of("0.5");
                    if (v1 instanceof RealBig rb) {
                        Real sin = rb.sin();
                        assertNotNull(sin);
                    } else {
                        throw new UnsupportedOperationException("Not a high-precision type");
                    }
                });

                return null;
            });

            // === COMPLEX DOMAIN ===
            @SuppressWarnings("unchecked")
            Ring<Real> complexRing = (Ring<Real>) (Object) Complex.of(1.0, 0.0).getScalarRing();
            boolean complexCompatible = provider.isCompatible(complexRing);

            testOp(res, "C:Add", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createComplexMatrix(Complex.of(1.5, 2.3), 3);
                Matrix<Real> b = createComplexMatrix(Complex.of(0.7, -1.1), 3);
                Matrix<Real> c = provider.add(a, b);
                assertNoFallback(provider, c);
            });

            testOp(res, "C:Sub", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createComplexMatrix(Complex.of(1.5, 2.3), 3);
                Matrix<Real> b = createComplexMatrix(Complex.of(0.7, -1.1), 3);
                Matrix<Real> c = provider.subtract(a, b);
                assertNoFallback(provider, c);
            });

            testOp(res, "C:Scale", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createComplexMatrix(Complex.of(1.5, 2.3), 3);
                Matrix<Real> c = provider.scale((Real)(Object)Complex.of(2.0, -1.0), a);
                assertNoFallback(provider, c);
            });

            testOp(res, "C:Mul", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createComplexMatrix(Complex.of(1.0, 1.0), 3);
                Matrix<Real> b = createComplexMatrix(Complex.of(0.5, -0.5), 3);
                Matrix<Real> c = provider.multiply(a, b);
                assertNoFallback(provider, c);
            });

            testOp(res, "C:MatVec", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createComplexMatrix(Complex.of(1.0, 1.0), 3);
                Vector<Real> v = createComplexVector(Complex.of(2.0, -1.0), 3);
                Vector<Real> r = provider.multiply(a, v);
                assertNoFallback(provider, r);
            });

            testOp(res, "C:Trans", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createComplexMatrix(Complex.of(1.5, 2.3), 3);
                Matrix<Real> t = provider.transpose(a);
                assertNoFallback(provider, t);
            });

            testOp(res, "C:Inv", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createInvertibleComplexMatrix(3);
                Matrix<Real> inv = provider.inverse(a);
                assertNoFallback(provider, inv);
            });

            testOp(res, "C:Det", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createInvertibleComplexMatrix(3);
                Real det = provider.determinant(a);
                assertNotNull(det);
            });

            testOp(res, "C:Solve", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createInvertibleComplexMatrix(3);
                Vector<Real> b = createComplexVector(Complex.of(1.0, 2.0), 3);
                Vector<Real> x = provider.solve(a, b);
                assertNoFallback(provider, x);
            });

            testOp(res, "C:Dot", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Vector<Real> a = createComplexVector(Complex.of(1.0, 1.0), 4);
                Vector<Real> b = createComplexVector(Complex.of(2.0, -1.0), 4);
                Real d = provider.dot(a, b);
                assertNotNull(d);
            });

            testOp(res, "C:Norm", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Vector<Real> a = createComplexVector(Complex.of(1.0, 1.0), 4);
                Real n = provider.norm(a);
                assertNotNull(n);
            });

            testOp(res, "C:LU", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createInvertibleComplexMatrix(3);
                LUResult<Real> lu = provider.lu(a);
                assertNotNull(lu);
            });

            testOp(res, "C:QR", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createComplexMatrix(Complex.of(1.0, 1.0), 3);
                QRResult<Real> qr = provider.qr(a);
                assertNotNull(qr);
            });

            testOp(res, "C:SVD", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createComplexMatrix(Complex.of(1.0, 1.0), 3);
                SVDResult<Real> svd = provider.svd(a);
                assertNotNull(svd);
            });

            testOp(res, "C:Chol", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createSPDComplexMatrix(3);
                CholeskyResult<Real> chol = provider.cholesky(a);
                assertNotNull(chol);
            });

            testOp(res, "C:Eigen", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                Matrix<Real> a = createInvertibleComplexMatrix(3);
                EigenResult<Real> eig = provider.eigen(a);
                assertNotNull(eig);
            });

            testOp(res, "C:BiCGSTAB", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
                SparseLinearAlgebraProvider<Real> sp = (SparseLinearAlgebraProvider<Real>) provider;
                Matrix<Real> a = createInvertibleComplexMatrix(4);
                Vector<Real> b = createComplexVector(Complex.of(1.0, 2.0), 4);
                Vector<Real> x0 = createComplexVector(Complex.of(0.0, 0.0), 4);
                Vector<Real> x = sp.bicgstab(a, b, x0, (Real)(Object)Complex.of(1e-8, 0), 1000);
                assertNotNull(x);
            });

            testOp(res, "C:ConjGrad", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
                SparseLinearAlgebraProvider<Real> sp = (SparseLinearAlgebraProvider<Real>) provider;
                Matrix<Real> a = createSPDComplexMatrix(4);
                Vector<Real> b = createComplexVector(Complex.of(1.0, 2.0), 4);
                Vector<Real> x0 = createComplexVector(Complex.of(0.0, 0.0), 4);
                Vector<Real> x = sp.conjugateGradient(a, b, x0, (Real)(Object)Complex.of(1e-8, 0), 1000);
                assertNotNull(x);
            });

            testOp(res, "C:GMRES", provider, () -> {
                if (!complexCompatible) throw new UnsupportedOperationException("No Complex support");
                if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException("Not sparse");
                SparseLinearAlgebraProvider<Real> sp = (SparseLinearAlgebraProvider<Real>) provider;
                Matrix<Real> a = createInvertibleComplexMatrix(4);
                Vector<Real> b = createComplexVector(Complex.of(1.0, 2.0), 4);
                Vector<Real> x0 = createComplexVector(Complex.of(0.0, 0.0), 4);
                Vector<Real> x = sp.gmres(a, b, x0, (Real)(Object)Complex.of(1e-8, 0), 1000, 10);
                assertNotNull(x);
            });

            results.add(res);
        }

        printMarkdownReport(results);
    }

    // --- Provider Discovery ---
    private List<LinearAlgebraProvider<Real>> discoverHPProviders() {
        Map<String, LinearAlgebraProvider<Real>> providers = new LinkedHashMap<>();
        
        @SuppressWarnings({"unchecked"})
        ServiceLoader<LinearAlgebraProvider<Real>> loader = ServiceLoader.load((Class<LinearAlgebraProvider<Real>>)(Class<?>)LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<Real> p : loader) {
            if (isDoubleOnlyProvider(p.getName())) continue;
            if (p.isCompatible(Reals.getInstance())) {
                providers.put(p.getName(), p);
            }
        }

        try {
            for (Backend b : BackendDiscovery.getInstance().getProviders()) {
                try {
                    for (var ap : b.getAlgorithmProviders()) {
                        if (ap instanceof LinearAlgebraProvider<?> p) {
                            if (isDoubleOnlyProvider(p.getName())) continue;
                            if (p.isCompatible(Reals.getInstance())) {
                                @SuppressWarnings("unchecked")
                                LinearAlgebraProvider<Real> typed = (LinearAlgebraProvider<Real>) p;
                                providers.putIfAbsent(typed.getName(), typed);
                            }
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

    private boolean isDoubleOnlyProvider(String name) {
        for (String dop : DOUBLE_ONLY_PROVIDERS) {
            if (name.contains(dop)) return true;
        }
        return false;
    }

    // --- Test Infrastructure ---
    private void assertNoFallback(LinearAlgebraProvider<Real> expectedProvider, Object result) {
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

    private void testOp(ComplianceResult res, String opName, LinearAlgebraProvider<Real> provider, Runnable test) {
        try {
            test.run();
            res.status.put(opName, "✅ PASS");
        } catch (UnsupportedOperationException e) {
            res.status.put(opName, "❌ N/A");
        } catch (Throwable e) {
            String className = e.getClass().getSimpleName();
            String msg = e.getMessage();
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
    private Matrix<Real> createRealBigMatrix(Real val, int n) {
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = Real.of(new BigDecimal(String.valueOf((i + 1) * 0.1 + (j + 1) * 0.01)).add(val.bigDecimalValue()));
            }
        }
        return Matrix.of(data, Reals.getInstance());
    }

    private Matrix<Real> createInvertibleRealBigMatrix(int n) {
        // Diagonally dominant for invertibility
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    data[i][j] = Real.of(new BigDecimal(String.valueOf(n * 2.0 + i)));
                } else {
                    data[i][j] = Real.of(new BigDecimal(String.valueOf(0.1 * (i + j + 1))));
                }
            }
        }
        return Matrix.of(data, Reals.getInstance());
    }

    private Matrix<Real> createSPDRealBigMatrix(int n) {
        // A^T * A + nI for symmetric positive definite
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += (0.1 * (k + i + 1)) * (0.1 * (k + j + 1));
                }
                if (i == j) sum += n;
                data[i][j] = Real.of(new BigDecimal(String.valueOf(sum)));
            }
        }
        return Matrix.of(data, Reals.getInstance());
    }

    private Vector<Real> createRealBigVector(Real val, int n) {
        Real[] data = new Real[n];
        for (int i = 0; i < n; i++) {
            data[i] = Real.of(new BigDecimal(String.valueOf((i + 1) * 0.5)).add(val.bigDecimalValue()));
        }
        return Vector.of(data, Reals.getInstance());
    }

    @SuppressWarnings("unchecked")
    private Matrix<Real> createComplexMatrix(Complex z, int n) {
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = (Real)(Object) Complex.of(z.getReal().doubleValue() + i * 0.1, z.getImaginary().doubleValue() + j * 0.1);
            }
        }
        Ring<Real> ring = (Ring<Real>)(Object) z.getScalarRing();
        return Matrix.of(data, ring);
    }

    @SuppressWarnings("unchecked")
    private Matrix<Real> createInvertibleComplexMatrix(int n) {
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    data[i][j] = (Real)(Object) Complex.of(n * 2.0 + i, 0.1);
                } else {
                    data[i][j] = (Real)(Object) Complex.of(0.1 * (i + j + 1), 0.05);
                }
            }
        }
        Ring<Real> ring = (Ring<Real>)(Object) Complex.of(0, 0).getScalarRing();
        return Matrix.of(data, ring);
    }

    @SuppressWarnings("unchecked")
    private Matrix<Real> createSPDComplexMatrix(int n) {
        // Hermitian positive definite
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    data[i][j] = (Real)(Object) Complex.of(n * 3.0 + i, 0.0);
                } else {
                    data[i][j] = (Real)(Object) Complex.of(0.1 * (i + j + 1), i > j ? 0.05 : -0.05);
                }
            }
        }
        Ring<Real> ring = (Ring<Real>)(Object) Complex.of(0, 0).getScalarRing();
        return Matrix.of(data, ring);
    }

    @SuppressWarnings("unchecked")
    private Vector<Real> createComplexVector(Complex z, int n) {
        Real[] data = new Real[n];
        for (int i = 0; i < n; i++) {
            data[i] = (Real)(Object) Complex.of(z.getReal().doubleValue() + i * 0.5, z.getImaginary().doubleValue() - i * 0.3);
        }
        Ring<Real> ring = (Ring<Real>)(Object) z.getScalarRing();
        return Vector.of(data, ring);
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
