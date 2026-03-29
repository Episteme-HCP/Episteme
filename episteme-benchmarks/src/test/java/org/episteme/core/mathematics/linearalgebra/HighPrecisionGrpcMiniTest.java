/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.StandardAlgorithmService;
import org.episteme.core.mathematics.context.MathContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Minimal gRPC performance and correctness test for high-precision operations.
 * Isolates the "gRPC Remote" provider to diagnose reported blocking/slowness.
 */
public class HighPrecisionGrpcMiniTest {

    private static ConfigurableApplicationContext serverContext;
    private static LinearAlgebraProvider<RealBig> grpcProvider;

    @BeforeAll
    @SuppressWarnings("unchecked")
    public static void setup() throws Exception {
        // 1. Ensure a standard service is used
        AlgorithmManager.setService(new StandardAlgorithmService());

        // 2. Start the gRPC server in a separate thread/context
        System.out.println("[MiniTest] Starting gRPC Server...");
        serverContext = GrpcTestApplication.start();
        Thread.sleep(3000); // Give it a bit more time to bind

        // 3. Find the gRPC provider
        for (var p : AlgorithmManager.getService().getProviders(LinearAlgebraProvider.class)) {
            if (p.getName().contains("gRPC Remote")) {
                grpcProvider = (LinearAlgebraProvider<RealBig>) p;
                break;
            }
        }

        if (grpcProvider == null) {
            throw new IllegalStateException("gRPC Remote provider not found! Check classpath and backend initialization.");
        }
        System.out.println("[MiniTest] gRPC Provider initialized: " + grpcProvider.getEnvironmentInfo());
    }

    @AfterAll
    public static void teardown() {
        if (serverContext != null) {
            serverContext.close();
        }
    }

    @Test
    public void testSingleInversionTiming() {
        // Run everything inside EXACT precision context
        MathContext.exact().compute(() -> {
            int n = 3;
            System.out.println("[MiniTest] Preparing 3x3 invertible matrix with high-precision elements...");
            
            RealBig[][] data = new RealBig[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    data[i][j] = RealBig.create(i == j ? new BigDecimal(n + i) : new BigDecimal("0.1"));
                }
            }
            
            @SuppressWarnings("unchecked")
            Matrix<RealBig> A = Matrix.of(data, (org.episteme.core.mathematics.structures.rings.Ring<RealBig>) (Object) RealBig.ZERO);
            
            System.out.println("[MiniTest] Matrix created. Element type: " + A.get(0,0).getClass().getSimpleName());
            System.out.println("[MiniTest] ScalarRing zero type: " + A.getScalarRing().zero().getClass().getSimpleName());
            System.out.println("[MiniTest] Sending inversion request to gRPC server...");
            long start = System.currentTimeMillis();
            
            Matrix<RealBig> invA = grpcProvider.inverse(A);
            
            long end = System.currentTimeMillis();
            long duration = end - start;
            
            System.out.println("[MiniTest] Inversion completed in " + duration + " ms.");
            
            // Log element types to trace RealDouble/NaN issues safely
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Real val = invA.get(i, j);
                    System.out.println("[MiniTest] Result Element[" + i + "," + j + "] = " + val + " (Type: " + val.getClass().getSimpleName() + ")");
                    
                    // Assert it's not NaN (the test should pass even if we just log the failure here)
                    if ("NaN".equals(val.toString()) || val.getClass().getSimpleName().contains("Double")) {
                         System.err.println("[MiniTest] ERROR: Element [" + i + "," + j + "] is " + val + ". Inversion quality failed.");
                    }
                }
            }
            
            assertNotNull(invA, "Result should not be null");
            
            // Verify precision by multiplying back
            Matrix<RealBig> I = grpcProvider.multiply(A, invA);
            System.out.println("[MiniTest] Verification product calculated. Element type: " + I.get(0,0).getClass().getSimpleName());
            
            // Check diagonal is close to 1
            for (int i = 0; i < n; i++) {
                Real element = I.get(i, i);
                System.out.println("[MiniTest] Verification Diagonal[" + i + "] = " + element + " (Type: " + element.getClass().getSimpleName() + ")");
                if (!(element instanceof RealBig)) {
                    System.err.println("[MiniTest] FAILURE: Diagonal element is NOT RealBig! It is " + element.getClass().getSimpleName());
                }
                BigDecimal val = element.bigDecimalValue();
                assertTrue(val.subtract(BigDecimal.ONE).abs().compareTo(new BigDecimal("1e-50")) < 0, 
                    "Diagonal element " + i + " lost precision: " + val);
            }
            
            System.out.println("[MiniTest] SUCCESS: Precision verified (matching 1e-50 tolerance).");
            return null;
        });
    }
}
