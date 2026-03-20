/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.nativ.mathematics.linearalgebra.backends.NativeCPULinearAlgebraBackend;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.AlgorithmService;
import org.episteme.core.technical.algorithm.TestingAlgorithmService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Isolated compliance test for NativeCPULinearAlgebraBackend.
 * Forces the use of ONLY this backend to ensure autonomy.
 */
public class NativeCPUComplianceTest {

    private static AlgorithmService originalService;
    private static NativeCPULinearAlgebraBackend backend;

    @BeforeAll
    public static void setup() {
        originalService = AlgorithmManager.getService();
        backend = new NativeCPULinearAlgebraBackend();
        
        // Force AlgorithmManager to only see this backend
        AlgorithmManager.setService(new TestingAlgorithmService(backend));
        System.out.println("[NativeCPUComplianceTest] Testing NativeCPU Backend in strict isolation.");
    }

    @AfterAll
    public static void tearDown() {
        AlgorithmManager.setService(originalService);
    }

    @Test
    public void runComplianceSuite() {
        LinearAlgebraComplianceTest suite = new LinearAlgebraComplianceTest();
        
        System.setProperty("org.episteme.include.provider", "Native CPU-BLAS");
        suite.generateComplianceReport();
    }
}
