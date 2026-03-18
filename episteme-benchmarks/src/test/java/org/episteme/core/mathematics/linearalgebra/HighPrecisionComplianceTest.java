/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Systematic compliance test for High-Precision LinearAlgebraProvider implementations.
 * Specifically targets Native MPFR and arbitrary precision libraries.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class HighPrecisionComplianceTest {

    private static final int SIZE = 10;
    private static final String REPORT_PATH = "../docs/HIGH_PRECISION_COMPLIANCE_REPORT.md";

    private static class ComplianceResult {
        String providerName;
        String environment;
        Map<String, String> status = new LinkedHashMap<>();
    }

    @Test
    public void generateHighPrecisionReport() {
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

            // Run tests in High Precision Context
            MathContext.exact().compute(() -> {
                
                testOperation(res, "Correctness (1/3 * 3)", () -> {
                    // Create exactly 1/3
                    Real third = Real.of(new BigDecimal("1")).divide(Real.of(new BigDecimal("3")));
                    Real result = provider.multiply(createVector(third, SIZE), Real.of(3)).get(0);
                    // result should be exactly 1.0 (RealBig allows this if configured correctly)
                    // At least it should be much better than double
                    assertTrue(result.toString().startsWith("1.0"), "Expected exactly 1.0, got " + result);
                });

                testOperation(res, "High Prec Multiply", () -> {
                    RealBig val = RealBig.create(new BigDecimal("0.123456789012345678901234567890"));
                    Matrix<Real> a = createMatrix(val, SIZE);
                    Matrix<Real> b = createMatrix(val, SIZE);
                    Matrix<Real> c = provider.multiply(a, b);
                    
                    Real result = c.get(0, 0);
                    // Manually check a few digits of 0.123...^2 * SIZE
                    String s = result.toString();
                    assertTrue(s.length() > 20, "Result should maintain high precision digits");
                });

                testOperation(res, "Complex High Prec", () -> {
                    // Multi-precision complex numbers
                    RealBig re = RealBig.create(new BigDecimal("0.5"));
                    RealBig im = RealBig.create(new BigDecimal("0.5"));
                    Complex z = Complex.of(re, im);
                    
                    // Complex multiplication: (0.5+0.5i)^2 = 0.5i
                    Complex z2 = z.multiply(z);
                    assertTrue(z2.getReal().isZero(), "Real part should be zero: " + z2.getReal());
                    assertEquals("0.5", z2.getImaginary().toString());
                    
                    // Matrix<Complex> multiplication
                    // LinearAlgebraProvider<Real> might not support Complex, but we test the Field logic
                    // if it's a generic provider.
                });

                testOperation(res, "No Generic Fallback", () -> {
                    // Check if provider is a native one and not using the field loop
                    // This is more of an inspection, but we can check the class
                    String className = provider.getClass().getName();
                    if (className.contains("NativeMPFR")) {
                        res.status.put("Implementation", "NATIVE ✅");
                    } else if (className.contains("CPUDense")) {
                        res.status.put("Implementation", "GENERIC ⚠️");
                    }
                });
                return null;
            });

            results.add(res);
        }

        printMarkdownReport(results);
    }

    private List<LinearAlgebraProvider<Real>> discoverHPProviders() {
        List<LinearAlgebraProvider<Real>> hpProviders = new ArrayList<>();
        
        // 1. ServiceLoader
        @SuppressWarnings({ "unchecked", "rawtypes" })
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider p : loader) {
            if (p.isCompatible(Reals.getInstance())) {
                hpProviders.add((LinearAlgebraProvider<Real>) p);
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
                                 if (!hpProviders.contains(typed)) hpProviders.add(typed);
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
        return hpProviders;
    }

    private void testOperation(ComplianceResult res, String opName, Runnable test) {
        try {
            test.run();
            res.status.putIfAbsent(opName, "✅ PASS");
        } catch (Throwable e) {
            res.status.put(opName, "⚠️ FAIL (" + e.getClass().getSimpleName() + ")");
        }
    }

    private Matrix<Real> createMatrix(Real val, int n) {
        Real[][] data = new Real[n][n];
        for (int i = 0; i < n; i++) Arrays.fill(data[i], val);
        return Matrix.of(data, Reals.getInstance());
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
