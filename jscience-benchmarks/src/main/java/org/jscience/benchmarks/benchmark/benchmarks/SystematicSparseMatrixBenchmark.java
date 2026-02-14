/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import java.util.Random;

/**
 * A benchmark that systematically tests all available SparseLinearAlgebraProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicSparseMatrixBenchmark implements SystematicBenchmark<SparseLinearAlgebraProvider<Real>> {

    private static final int SIZE = 1000;
    private static final double SPARSITY = 0.05; // 5% non-zero elements
    private SparseMatrix<Real> A;
    private SparseMatrix<Real> B;
    private SparseLinearAlgebraProvider<Real> currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "sparse-matrix-systematic"; }
    @Override public String getNameBase() { return "Sparse Matrix Multiplication"; }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override public Class<SparseLinearAlgebraProvider<Real>> getProviderClass() { return (Class) SparseLinearAlgebraProvider.class; }

    @Override
    public String getDescription() {
        return "Sparse Matrix Multiplication (1000x1000, 5% sparsity)";
    }

    @Override
    public String getDomain() {
        return "Linear Algebra (Sparse)";
    }

    @Override
    public void setup() {
        // Create sparse matrices
        // We use the default/current provider for creation, but execution will use the injected provider
        // Assuming creation isn't the bottleneck or is handled by the provider implicitly if we used factories
        
        Reals ring = Reals.getInstance();
        A = SparseMatrix.zeros(SIZE, SIZE, ring);
        B = SparseMatrix.zeros(SIZE, SIZE, ring);
        
        Random r = new Random(42);
        for (int i = 0; i < SIZE; i++) {
            for (int k = 0; k < SIZE * SPARSITY; k++) {
                int j = r.nextInt(SIZE);
                A.set(i, j, Real.of(r.nextDouble()));
            }
        }
        for (int i = 0; i < SIZE; i++) {
            for (int k = 0; k < SIZE * SPARSITY; k++) {
                int j = r.nextInt(SIZE);
                B.set(i, j, Real.of(r.nextDouble()));
            }
        }
    }

    @Override
    public void setProvider(SparseLinearAlgebraProvider<Real> provider) {
        this.currentProvider = provider;
        // In a real scenario, we might need to migrate data to provider-specific storage here
        // But for now, we assume the provider can handle the generic SparseMatrix input
    }


    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.multiply(A, B);
        }
    }

    @Override
    public void teardown() {
        A = null;
        B = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 50;
    }
}
