/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.examples.linearalgebra;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.technical.algorithm.AlgorithmManager;
import org.jscience.core.technical.algorithm.OperationContext;

/**
 * Demonstrates high-performance linear algebra using JScience's Smart Dispatch.
 */
public class LinearAlgebraExample {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   JScience Linear Algebra Smart Dispatch");
        System.out.println("==========================================");

        // 1. Setup Matrix Dimension
        int n = 1000;
        System.out.println("Initializing " + n + "x" + n + " matrices...");
        
        org.jscience.core.mathematics.numbers.real.Real[][] dataA = new org.jscience.core.mathematics.numbers.real.Real[n][n];
        org.jscience.core.mathematics.numbers.real.Real[][] dataB = new org.jscience.core.mathematics.numbers.real.Real[n][n];
        
        // Simple initialization
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dataA[i][j] = org.jscience.core.mathematics.numbers.real.Real.of(i);
                dataB[i][j] = org.jscience.core.mathematics.numbers.real.Real.of(j);
            }
        }
        
        Matrix<org.jscience.core.mathematics.numbers.real.Real> A = org.jscience.core.mathematics.linearalgebra.Matrix.of(dataA, org.jscience.core.mathematics.sets.Reals.getInstance());
        Matrix<org.jscience.core.mathematics.numbers.real.Real> B = org.jscience.core.mathematics.linearalgebra.Matrix.of(dataB, org.jscience.core.mathematics.sets.Reals.getInstance());

        // 2. Define Contexts to show selection logic
        System.out.println("\n--- Scenario 1: Default Context ---");
        runBenchmark(A, B, new OperationContext.Builder()
                .dataSize(n * n)
                .addHint(OperationContext.Hint.DENSE)
                .build());

        System.out.println("\n--- Scenario 2: GPU Resident Hint (Simulation) ---");
        runBenchmark(A, B, new OperationContext.Builder()
                .dataSize(n * n)
                .addHint(OperationContext.Hint.DENSE)
                .addHint(OperationContext.Hint.GPU_RESIDENT)
                .build());
    }

    private static void runBenchmark(Matrix<org.jscience.core.mathematics.numbers.real.Real> A, Matrix<org.jscience.core.mathematics.numbers.real.Real> B, OperationContext ctx) {
        // Select best provider
        org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider<org.jscience.core.mathematics.numbers.real.Real> provider = 
                AlgorithmManager.getRegistry().selectLinearAlgebraProvider(ctx, org.jscience.core.mathematics.sets.Reals.getInstance());
        
        System.out.println("Context Hints: " + ctx.getHints());
        System.out.println("Selected Provider: " + provider.getName());
        System.out.println("Provider Class: " + provider.getClass().getSimpleName());
        
        // Warmup (optional, skipping for brevity)
        
        // Execute
        long start = System.nanoTime();
        Matrix<org.jscience.core.mathematics.numbers.real.Real> C = provider.multiply(A, B);
        long end = System.nanoTime();
        
        double durationMs = (end - start) / 1_000_000.0;
        System.out.println("Execution Time: " + String.format("%.2f", durationMs) + " ms");
        System.out.println("Result (0,0): " + C.get(0, 0));
    }
}
