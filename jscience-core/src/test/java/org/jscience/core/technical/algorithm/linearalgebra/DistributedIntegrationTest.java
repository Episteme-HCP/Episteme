/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.technical.algorithm.linearalgebra;

import org.jscience.core.distributed.DistributedCompute;
import org.jscience.core.technical.backend.distributed.DistributedContext;
import org.jscience.core.distributed.LocalDistributedContext;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.DistributedLinearAlgebraProvider;
import org.jscience.core.technical.algorithm.AlgorithmManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Validates the integration of Distributed Linear Algebra.
 */
public class DistributedIntegrationTest {

    @Test
    public void testProviderSelection() {
        System.out.println("Starting Distributed Integration Validation...");

        // 1. Setup Distributed Context
        DistributedContext distCtx = new SimulationDistributedContext(4); // Simulate 4 nodes
        DistributedCompute.setContext(distCtx);
        System.out.println("Context set to: " + distCtx.getClass().getSimpleName() + " (Parallelism: " + distCtx.getParallelism() + ")");

        // 2. Query AlgorithmManager
        LinearAlgebraProvider<?> provider = AlgorithmManager.getProvider(LinearAlgebraProvider.class);
        System.out.println("Selected Provider: " + provider.getClass().getSimpleName());
        
        // 3. Verify
        boolean isDistributed = provider instanceof DistributedLinearAlgebraProvider;
        if (!isDistributed) {
             // Debug info
             System.out.println("Available Providers:");
             AlgorithmManager.getProviders(LinearAlgebraProvider.class)
                .forEach(p -> System.out.println(" - " + p.getClass().getSimpleName() + " Priority: " + p.getPriority()));
        }
        
        assertTrue(isDistributed, "DistributedLinearAlgebraProvider should be selected when parallelism > 1");
    }
    
    // Mock Context to force "Distributed" behavior
    static class SimulationDistributedContext extends LocalDistributedContext {
        private final int parallelism;
        
        public SimulationDistributedContext(int p) {
            this.parallelism = p;
        }
        
        @Override
        public int getParallelism() {
            return parallelism;
        }
    }
}
