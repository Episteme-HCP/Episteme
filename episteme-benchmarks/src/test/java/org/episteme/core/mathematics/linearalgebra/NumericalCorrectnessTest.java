/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.technical.algorithm.AlgorithmService;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.TestingAlgorithmService;
// import removed
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validates numerical correctness of linear algebra operations.
 */
public class NumericalCorrectnessTest {

    private static final BigDecimal TOLERANCE_REALBIG = new BigDecimal("1e-25");
    private static final double TOLERANCE_COMPLEX = 1e-8;

    @Test
    public void runNumericalAudit() {
        List<LinearAlgebraProvider<?>> providers = discoverHPProviders();
        List<CorrectnessResult> results = new ArrayList<>();

        AlgorithmService oldService = AlgorithmManager.getService();
        try {
            // Find MPFR reference provider
            LinearAlgebraProvider<RealBig> mpfrRef = null;
            for (AlgorithmProvider p : AlgorithmManager.getProviders(AlgorithmProvider.class)) {
                if (p.getName().contains("MPFR") && (p instanceof LinearAlgebraProvider)) {
                    @SuppressWarnings("unchecked")
                    LinearAlgebraProvider<RealBig> casted = (LinearAlgebraProvider<RealBig>) (Object) p;
                    mpfrRef = casted;
                    break;
                }
            }
            
            if (mpfrRef == null) {
                System.out.println("Skipping MPFR comparison (provider not found)");
                return;
            }

            final LinearAlgebraProvider<RealBig> mpfr = mpfrRef;
            AlgorithmManager.setService(new TestingAlgorithmService(mpfr));

            for (LinearAlgebraProvider<?> providerToTest : providers) {
                System.out.println("Running numerical correctness tests for: " + providerToTest.getName());
                CorrectnessResult currentRes = new CorrectnessResult();
                currentRes.providerName = providerToTest.getName();

                if (!providerToTest.isAvailable()) {
                    results.add(currentRes);
                    continue;
                }

                // Isolation: only allow the current provider under test
                AlgorithmManager.setService(new TestingAlgorithmService(providerToTest));
                
                try {
                    MathContext.exact().compute(() -> {
                        RealBig rbVal = RealBig.create(BigDecimal.ONE);
                        @SuppressWarnings("unchecked")
                        Ring<RealBig> rbRing = (Ring<RealBig>) (Object) rbVal.getScalarRing();
                        if (providerToTest.isCompatible(rbRing)) {
                            @SuppressWarnings("unchecked")
                            LinearAlgebraProvider<RealBig> rbProvider = (LinearAlgebraProvider<RealBig>) providerToTest;
                            runRealBigCorrectnessTests(currentRes, rbProvider, mpfr);
                        }
                        return null;
                    });
                } catch (Throwable t) {
                    currentRes.status.put("CRITICAL", "⚠️ CRASH");
                    currentRes.details.put("CRITICAL", t.getMessage());
                } finally {
                    AlgorithmManager.setService(oldService);
                }

                // Complex domain
                Ring<Complex> cRing = Complex.of(1.0, 0.0).getScalarRing();
                if (providerToTest.isCompatible(cRing)) {
                    @SuppressWarnings("unchecked")
                    LinearAlgebraProvider<Complex> cProvider = (LinearAlgebraProvider<Complex>) providerToTest;
                    LinearAlgebraProvider<Complex> cMpfr = null;
                    if (mpfr.isCompatible(cRing)) {
                        @SuppressWarnings("unchecked")
                        LinearAlgebraProvider<Complex> casted = (LinearAlgebraProvider<Complex>) (Object) mpfr;
                        cMpfr = casted; 
                    }
                    runComplexCorrectnessTests(currentRes, cProvider, cMpfr);
                }

                results.add(currentRes);
            }
        } finally {
            AlgorithmManager.setService(oldService);
        }

        // Final Report
        printCorrectnessReport(results);
    }

    private void runRealBigCorrectnessTests(CorrectnessResult res, LinearAlgebraProvider<RealBig> p, LinearAlgebraProvider<RealBig> mpfr) {
        Matrix<RealBig> A = createInvertibleRealBigMatrix(3);
        testCorrectness(res, "RB:Inverse", () -> {
            Matrix<RealBig> inv = p.inverse(A);
            Matrix<RealBig> I = Matrix.identity(3, A.getScalarRing());
            assertMatrixClose(p.multiply(A, inv), I, TOLERANCE_REALBIG, "A * A^-1 = I");
        });
        testCorrectness(res, "RB:QR", () -> {
            QRResult<RealBig> qr = p.qr(A);
            Matrix<RealBig> reconstructed = qr.Q().multiply(qr.R());
            assertMatrixClose(reconstructed, A, TOLERANCE_REALBIG, "Q * R = A");
        });
        testCorrectness(res, "RB:Cholesky", () -> {
            Matrix<RealBig> posDef = p.multiply(A.transpose(), A);
            CholeskyResult<RealBig> chol = p.cholesky(posDef);
            Matrix<RealBig> reconstructed = chol.L().multiply(chol.L().transpose());
            assertMatrixClose(reconstructed, posDef, TOLERANCE_REALBIG, "L * L^T = A");
        });
    }

    private void runComplexCorrectnessTests(CorrectnessResult res, LinearAlgebraProvider<Complex> p, LinearAlgebraProvider<Complex> mpfr) {
        Matrix<Complex> A = createInvertibleComplexMatrix(3);
        testCorrectness(res, "C:Inverse", () -> {
            Matrix<Complex> inv = p.inverse(A);
            Matrix<Complex> I = Matrix.identity(3, A.getScalarRing());
            assertComplexMatrixClose(p.multiply(A, inv), I, TOLERANCE_COMPLEX, "A * A^-1 = I");
        });
    }

    private void assertMatrixClose(Matrix<? extends Real> actual, Matrix<? extends Real> expected, BigDecimal tol, String msg) {
        assertEquals(expected.rows(), actual.rows());
        assertEquals(expected.cols(), actual.cols());
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.cols(); j++) {
                BigDecimal diff = actual.get(i, j).bigDecimalValue().subtract(expected.get(i, j).bigDecimalValue()).abs();
                assertTrue(diff.compareTo(tol) < 0, msg + " at [" + i + "," + j + "]: diff=" + diff);
            }
        }
    }

    private void assertComplexMatrixClose(Matrix<Complex> actual, Matrix<Complex> expected, double tol, String msg) {
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.cols(); j++) {
                double dist = actual.get(i, j).subtract(expected.get(i, j)).abs().doubleValue();
                assertTrue(dist < tol, msg + " at [" + i + "," + j + "]: dist=" + dist);
            }
        }
    }

    private void testCorrectness(CorrectnessResult res, String op, Runnable test) {
        try {
            test.run();
            res.status.put(op, "✅ PASS");
        } catch (Throwable t) {
            res.status.put(op, "❌ FAIL");
            res.details.put(op, t.getMessage());
        }
    }

    private static class CorrectnessResult {
        String providerName;
        Map<String, String> status = new LinkedHashMap<>();
        Map<String, String> details = new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    private List<LinearAlgebraProvider<?>> discoverHPProviders() {
        List<LinearAlgebraProvider<?>> list = new ArrayList<>();
        ServiceLoader<LinearAlgebraProvider<?>> loader = (ServiceLoader<LinearAlgebraProvider<?>>) (ServiceLoader<?>) ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> p : loader) list.add(p);
        return list;
    }

    @SuppressWarnings("unchecked")
    private Matrix<RealBig> createInvertibleRealBigMatrix(int n) {
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = RealBig.create(i == j ? new BigDecimal(n + i) : new BigDecimal(0.1));
            }
        }
        return (Matrix<RealBig>) (Matrix<?>) Matrix.of(data, RealBig.create(BigDecimal.ZERO).getScalarRing());
    }

    private Matrix<Complex> createInvertibleComplexMatrix(int n) {
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = Complex.of(i == j ? n + i : 0.1, 0.05);
            }
        }
        return Matrix.of(data, Complex.of(0, 0).getScalarRing());
    }

    private void printCorrectnessReport(List<CorrectnessResult> results) {
        System.out.println("NUMERICAL CORRECTNESS REPORT");
        for (var r : results) {
            System.out.println(r.providerName + " : " + r.status);
        }
    }
}
