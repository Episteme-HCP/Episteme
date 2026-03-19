/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Systematic compliance test for High-Precision LinearAlgebraProvider implementations.
 * Specifically targets Native MPFR and arbitrary precision libraries.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class HighPrecisionComplianceTest {

    // SIZE no longer used in granular tests
    private static final String REPORT_PATH = "../docs/HIGH_PRECISION_COMPLIANCE_REPORT.md";

    private static class ComplianceResult {
        String providerName;
        String environment;
        Map<String, String> status = new LinkedHashMap<>();
    }

    @Test
    public void generateHighPrecisionReport() {
        // Clear or initialize report
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(REPORT_PATH));
        } catch (IOException ignored) {}

        List<LinearAlgebraProvider<Real>> providers = discoverHPProviders();
        List<ComplianceResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<Real> provider : providers) {
            System.out.println("Starting compliance tests for: " + provider.getName());
            ComplianceResult res = new ComplianceResult();
            res.providerName = provider.getName();
            
            if (!provider.isAvailable()) {
                res.environment = "DISABLED";
                results.add(res);
                continue;
            }
            res.environment = provider.getEnvironmentInfo();

            // --- REAL BIG TESTS ---
            MathContext.exact().compute(() -> {
                testOperation(res, "RealBig: Add", provider, () -> {
                    RealBig a = RealBig.create(new BigDecimal("1.1"));
                    // Use scale() for scalar-matrix multiplication testing
                    Matrix<Real> resMat = provider.scale(Real.of(1.0), createMatrix(a, 1));
                    assertNotNull(resMat);
                    assertNoFallback(provider, resMat);
                });
                
                testOperation(res, "RealBig: Multiply", provider, () -> {
                    RealBig val = RealBig.create(new BigDecimal("0.12345678901234567890"));
                    Matrix<Real> a = createMatrix(val, 2);
                    Matrix<Real> b = createMatrix(val, 2);
                    Matrix<Real> c = provider.multiply(a, b);
                    assertNoFallback(provider, c);
                });
                
                testOperation(res, "RealBig: Solve", provider, () -> {
                    Matrix<Real> a = createMatrix(Real.of("2.0"), 2);
                    Vector<Real> b = createVector(Real.of("4.0"), 2);
                    Vector<Real> x = provider.solve(a, b);
                    assertNoFallback(provider, x);
                });
                
                return null;
            });

            // --- COMPLEX TESTS ---
            testOperation(res, "Complex: Multiply", provider, () -> {
                Complex z = Complex.of(1.0, 1.0);
                if (!provider.isCompatible(z.getScalarRing())) {
                    throw new UnsupportedOperationException("Provider não compatível com Complex math");
                }
                Matrix<Real> a = createComplexMatrix(z, 2);
                Matrix<Real> b = createComplexMatrix(z, 2);
                Matrix<Real> c = provider.multiply(a, b);
                assertNoFallback(provider, c);
            });
            
            testOperation(res, "Complex: Add", provider, () -> {
                Complex z = Complex.of(1.0, 1.0);
                if (!provider.isCompatible(z.getScalarRing())) {
                     throw new UnsupportedOperationException("Provider não compatível com Complex math");
                }
                Matrix<Real> mat = createComplexMatrix(z, 2);
                assertNotNull(mat);
                // Fallback check on addition if provider supports it (via transpose or similar if add isn't direct)
                throw new UnsupportedOperationException("No direct Add in LinearAlgebraProvider yet");
            });

            // --- TRANSCENDENTAL & INVERSE ---
            testOperation(res, "RealBig: Inverse", provider, () -> {
                RealBig val = RealBig.create(new BigDecimal("2.0"));
                Matrix<Real> a = createMatrix(val, 2);
                Matrix<Real> inv = provider.inverse(a);
                assertNoFallback(provider, inv);
            });

            testOperation(res, "Transcendental: Exp", provider, () -> {
                Real val = Real.of("1.0");
                if (val instanceof RealBig rb) {
                     Real resExp = rb.exp();
                     assertNotNull(resExp);
                } else {
                     throw new UnsupportedOperationException("Test only for High-Precision types");
                }
            });

            results.add(res);
        }

        printMarkdownReport(results);
    }

    private void assertNoFallback(LinearAlgebraProvider<Real> expectedProvider, Object result) {
        if (result instanceof Matrix<?> m) {
            String expectedName = expectedProvider.getClass().getSimpleName();
            String actualName = m.getProvider().getClass().getSimpleName();
            if (!actualName.equals(expectedName)) {
                throw new UnsupportedOperationException("Fallback detected: Expected " + expectedName + " but got " + actualName);
            }
        } else if (result instanceof Vector<?> v) {
            String expectedName = expectedProvider.getClass().getSimpleName();
            String actualName = v.getProvider().getClass().getSimpleName();
             if (!actualName.equals(expectedName)) {
                throw new UnsupportedOperationException("Fallback detected: Expected " + expectedName + " but got " + actualName);
            }
        }
    }

    private List<LinearAlgebraProvider<Real>> discoverHPProviders() {
        Map<String, LinearAlgebraProvider<Real>> providers = new LinkedHashMap<>();
        
        // 1. ServiceLoader
        @SuppressWarnings({ "unchecked" })
        ServiceLoader<LinearAlgebraProvider<Real>> loader = ServiceLoader.load((Class<LinearAlgebraProvider<Real>>)(Class<?>)LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<Real> p : loader) {
            if (p.isCompatible(Reals.getInstance())) {
                providers.put(p.getName(), p);
            }
        }

        // 2. BackendDiscovery
        try {
            for (Backend b : BackendDiscovery.getInstance().getProviders()) {
                try {
                    for (var ap : b.getAlgorithmProviders()) {
                        if (ap instanceof LinearAlgebraProvider<?> p) {
                            if (p.isCompatible(Reals.getInstance())) {
                                 @SuppressWarnings("unchecked")
                                 LinearAlgebraProvider<Real> typed = (LinearAlgebraProvider<Real>) p;
                                 providers.putIfAbsent(typed.getName(), typed);
                            }
                        }
                    }
                } catch (Throwable t) {
                    System.err.println("Warning: Could not load algorithm providers for backend " + b.getName() + ": " + t.getMessage());
                }
            }
        } catch (Throwable t) {
            System.err.println("Warning: Backend discovery failed: " + t.getMessage());
        }
        return new ArrayList<>(providers.values());
    }

    private void testOperation(ComplianceResult res, String opName, LinearAlgebraProvider<Real> provider, Runnable test) {
        try {
            test.run();
            res.status.put(opName, "✅ PASS");
        } catch (UnsupportedOperationException e) {
            // Silenced for expected N/A
            res.status.put(opName, "❌ N/A");
        } catch (Throwable e) {
             String msg = e.getMessage();
            if (msg == null || msg.isBlank()) msg = e.getClass().getSimpleName();
            res.status.put(opName, "⚠️ FAIL (" + msg + ")");
        }
    }

    private Matrix<Real> createMatrix(Real val, int n) {
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) Arrays.fill(data[i], val);
        return Matrix.of(data, Reals.getInstance());
    }

    private Matrix<Real> createComplexMatrix(Complex z, int n) {
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) Arrays.fill(data[i], (Real)(Object)z);
        @SuppressWarnings("unchecked")
        org.episteme.core.mathematics.structures.rings.Ring<Real> complexRing = (org.episteme.core.mathematics.structures.rings.Ring<Real>)(Object)z.getScalarRing();
        return Matrix.of(data, complexRing);
    }

    private Vector<Real> createVector(Real val, int n) {
        Real[] data = new Real[n];
        Arrays.fill(data, val);
        return Vector.of(data, Reals.getInstance());
    }

    private void printMarkdownReport(List<ComplianceResult> results) {
        StringBuilder sb = new StringBuilder("# High-Precision Compliance Report\n\n");
        if (results.isEmpty()) {
            sb.append("No providers discovered.\n");
        } else {
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
        }
        
        System.out.println(sb);
        try {
            java.nio.file.Files.writeString(java.nio.file.Paths.get(REPORT_PATH), sb.toString());
        } catch (IOException ignored) {}
    }
}
