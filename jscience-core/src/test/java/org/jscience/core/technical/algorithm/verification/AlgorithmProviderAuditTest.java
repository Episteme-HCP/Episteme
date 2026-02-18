/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.verification;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.technical.algorithm.AlgorithmManager;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Automated audit of all registered LinearAlgebraProvider implementations.
 */
public class AlgorithmProviderAuditTest {

    @TestFactory
    public Stream<DynamicTest> auditLinearAlgebraProviders() {
        @SuppressWarnings("rawtypes")
        List<LinearAlgebraProvider> providers = AlgorithmManager.getProviders(LinearAlgebraProvider.class);
        
        List<DynamicTest> tests = new ArrayList<>();
        
        for (@SuppressWarnings("rawtypes") LinearAlgebraProvider provider : providers) {
            if (!provider.isAvailable()) continue;
            
            String pName = provider.getName();
            
            // Multiply Test
            tests.add(DynamicTest.dynamicTest(pName + " - Multiplication Correctness", () -> {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Real> realProvider = (LinearAlgebraProvider<Real>) provider;
                ProviderVerificationSuite.AuditResult result = ProviderVerificationSuite.verifyMultiply(realProvider, 10, 1e-9);
                assertTrue(result.passed, "Multiplication failed for " + pName + ". Max error: " + result.maxError);
            }));
            
            // Solve Test
            tests.add(DynamicTest.dynamicTest(pName + " - Solve Correctness", () -> {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Real> realProvider = (LinearAlgebraProvider<Real>) provider;
                ProviderVerificationSuite.AuditResult result = ProviderVerificationSuite.verifySolve(realProvider, 10, 1e-7);
                assertTrue(result.passed, "Solve failed for " + pName + ". Max error: " + result.maxError);
            }));
        }
        
        return tests.stream();
    }
}
