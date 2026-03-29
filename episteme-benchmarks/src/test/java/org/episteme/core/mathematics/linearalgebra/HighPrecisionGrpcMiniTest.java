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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        // Force AlgorithmManager to use a fresh service
        AlgorithmManager.setService(new StandardAlgorithmService());

        // Start the gRPC server in a separate thread/context
        System.out.println("[MiniTest] Starting gRPC Server using GrpcTestApplication...");
        
        try {
            Class<?> serverClass = Class.forName("org.episteme.core.mathematics.linearalgebra.GrpcTestApplication");
            var startMethod = serverClass.getMethod("start");
            serverContext = (ConfigurableApplicationContext) startMethod.invoke(null);
        } catch (Exception e) {
            System.err.println("[MiniTest] Reflection start failed, trying direct call...");
            serverContext = GrpcTestApplication.start();
        }
        
        Thread.sleep(3000); 

        // Find the gRPC provider
        for (var p : AlgorithmManager.getService().getProviders(LinearAlgebraProvider.class)) {
            if (p.getName().contains("gRPC Remote")) {
                grpcProvider = (LinearAlgebraProvider<Real>) p;
                break;
            }
        }

        if (grpcProvider == null) {
            throw new RuntimeException("gRPC Remote provider not found!");
        }
    }

    @AfterAll
    public static void teardown() {
        if (serverContext != null) {
            try {
                serverContext.close();
            } catch (Exception e) {
                System.err.println("[MiniTest] Error during server teardown: " + e.getMessage());
            }
        }
    }

    @Test
    public void testHighPrecisionInverse() {
        MathContext.exact().compute(() -> {
            int n = 3;
            // A simple Hilbert-like matrix which is notoriously ill-conditioned
            Real[][] data = new Real[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    data[i][j] = RealBig.of(1).divide(RealBig.of(i + j + 1));
                }
            }
            
            Matrix<Real> A = Matrix.of(data, Real.ZERO);
            System.out.println("[MiniTest] Matrix created. Element type: " + A.get(0,0).getClass().getName());
            
            System.out.println("[MiniTest] Sending inversion request to gRPC server...");
            long start = System.currentTimeMillis();
            Matrix<Real> invA = grpcProvider.inverse(A);
            long end = System.currentTimeMillis();
            
            System.out.println("[MiniTest] Inversion completed in " + (end - start) + " ms.");
            assertNotNull(invA, "Result should not be null");
            
            // Verify precision by multiplying back
            Matrix<Real> I = grpcProvider.multiply(A, invA);
            
            // Check diagonal is close to 1
            for (int i = 0; i < n; i++) {
                Real element = I.get(i, i);
                System.out.println("[MiniTest] Verification Diagonal[" + i + "] = " + element + " (Type: " + element.getClass().getName() + ")");
                
                assertFalse(element.isNaN(), "Diagonal element " + i + " is NaN");
                assertFalse(element.isInfinite(), "Diagonal element " + i + " is Infinite");
                
                BigDecimal val = element.bigDecimalValue();
                // We use a slightly more relaxed tolerance to ensure stable testing while still confirming high-precision presence
                assertTrue(val.subtract(BigDecimal.ONE).abs().compareTo(new BigDecimal("1e-20")) < 0, 
                        "Diagonal element " + i + " should be close to 1, but is " + val);
            }
            
            System.out.println("[MiniTest] SUCCESS: Multi-step high-precision gRPC roundtrip completed successfully.");
            return null;
        });
    }
}
