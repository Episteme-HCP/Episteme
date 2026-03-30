/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.StandardAlgorithmService;
import org.episteme.core.mathematics.context.MathContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Minimal gRPC performance and correctness test for high-precision operations.
 * Isolates the "gRPC Remote" provider to diagnose reported blocking/slowness.
 */
public class HighPrecisionGrpcMiniTest {

    private static ConfigurableApplicationContext serverContext;
    private static LinearAlgebraProvider<Real> grpcProvider;

    @BeforeAll
    @SuppressWarnings("unchecked")
    public static void setup() throws Exception {
        AlgorithmManager.setService(new StandardAlgorithmService());

        System.out.println("[MiniTest] Starting gRPC Server...");
        serverContext = GrpcTestApplication.start();
        Thread.sleep(3000);

        for (var p : AlgorithmManager.getService().getProviders(LinearAlgebraProvider.class)) {
            System.out.println("[MiniTest] Available provider: " + p.getName() + " (" + p.getClass().getName() + ")");
            if (p.getName().contains("gRPC Remote")) {
                grpcProvider = (LinearAlgebraProvider<Real>) p;
            }
        }

        if (grpcProvider == null) {
            throw new RuntimeException("gRPC Remote provider not found!");
        }
        System.out.println("[MiniTest] Using provider: " + grpcProvider.getName());
    }

    @AfterAll
    public static void teardown() {
        if (serverContext != null) {
            try { serverContext.close(); } catch (Exception e) { /* ignore */ }
        }
    }

    @Test
    public void testHighPrecisionInverse() {
        MathContext.exact().compute(() -> {
            System.out.println("[MiniTest] MathContext.isHighPrecision() = " + MathContext.getCurrent().isHighPrecision());
            
            int n = 3;
            // Build Hilbert matrix with RealBig elements
            Real[][] data = new Real[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    data[i][j] = RealBig.of(1).divide(RealBig.of(i + j + 1));
                    System.out.println("[MiniTest] data[" + i + "][" + j + "] = " + data[i][j] 
                        + " (type: " + data[i][j].getClass().getSimpleName() + ")");
                }
            }

            // Use RealBig.ZERO as ring to ensure correct HP detection on the wire
            Matrix<Real> A = Matrix.of(data, (org.episteme.core.mathematics.structures.rings.Ring<Real>)(Object) RealBig.ZERO);
            
            System.out.println("[MiniTest] Matrix A ring zero type: " + A.getScalarRing().zero().getClass().getName());
            System.out.println("[MiniTest] Matrix A[0,0] type: " + A.get(0, 0).getClass().getName());
            System.out.println("[MiniTest] Matrix A[0,0] value: " + A.get(0, 0));

            // Test the inverse
            System.out.println("[MiniTest] Sending inverse request...");
            long start = System.currentTimeMillis();
            Matrix<Real> invA = grpcProvider.inverse(A);
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("[MiniTest] Inverse completed in " + elapsed + " ms");

            assertNotNull(invA, "Inverse result should not be null");

            // Inspect every element of the result
            System.out.println("[MiniTest] Inspecting inverse result:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Real val = invA.get(i, j);
                    System.out.println("[MiniTest] invA[" + i + "][" + j + "] = " + val 
                        + " (type: " + val.getClass().getName() 
                        + ", isNaN: " + val.isNaN() 
                        + ", isInf: " + val.isInfinite() + ")");
                }
            }

            // Verify: the result should contain RealBig elements, NOT RealDouble
            Real firstElement = invA.get(0, 0);
            String firstType = firstElement.getClass().getName();
            System.out.println("[MiniTest] First result element type: " + firstType);

            // If the server returned RealDouble with NaN, the HP path failed
            if (firstElement.isNaN()) {
                fail("The server returned NaN for invA[0,0]. This means the HP path is broken - "
                   + "the server is computing with double precision on an ill-conditioned Hilbert matrix. "
                   + "Element type: " + firstType);
            }

            // Multiply A * invA to verify identity
            System.out.println("[MiniTest] Sending multiply request for verification...");
            Matrix<Real> product = grpcProvider.multiply(A, invA);

            for (int i = 0; i < n; i++) {
                Real diag = product.get(i, i);
                System.out.println("[MiniTest] product[" + i + "][" + i + "] = " + diag 
                    + " (type: " + diag.getClass().getName() + ")");
                
                assertFalse(diag.isNaN(), "Product diagonal[" + i + "] is NaN");
                assertFalse(diag.isInfinite(), "Product diagonal[" + i + "] is Infinite");

                BigDecimal val = diag.bigDecimalValue();
                assertTrue(val.subtract(BigDecimal.ONE).abs().compareTo(new BigDecimal("1e-20")) < 0,
                        "Diagonal element " + i + " should be close to 1, but is " + val);
            }

            System.out.println("[MiniTest] SUCCESS!");
            return null;
        });
    }
}
