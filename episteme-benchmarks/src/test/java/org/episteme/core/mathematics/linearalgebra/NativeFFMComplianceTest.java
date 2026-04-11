/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.nativ.mathematics.linearalgebra.backends.NativeFFMBLASBackend;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.AlgorithmService;
import org.episteme.core.technical.algorithm.TestingAlgorithmService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Isolated compliance test for NativeFFMBLASBackend.
 * Forces the use of ONLY this backend to ensure autonomy.
 */
public class NativeFFMComplianceTest {

    private static AlgorithmService originalService;
    private static NativeFFMBLASBackend<?> backend;

    @BeforeAll
    public static void setup() {
        originalService = AlgorithmManager.getService();
        backend = new NativeFFMBLASBackend<>();
        
        // Force AlgorithmManager to only see this backend
        AlgorithmManager.setService(new TestingAlgorithmService(backend));
        System.out.println("[NativeFFMComplianceTest] Testing NativeFFM Backend in strict isolation.");
    }

    @AfterAll
    public static void tearDown() {
        AlgorithmManager.setService(originalService);
    }

    @Test
    public void runComplianceSuite() {
        LinearAlgebraComplianceTest suite = new LinearAlgebraComplianceTest();
        
        System.setProperty("org.episteme.include.provider", "Native BLAS Provider FFM");
        suite.generateComplianceReport();
    }
}
