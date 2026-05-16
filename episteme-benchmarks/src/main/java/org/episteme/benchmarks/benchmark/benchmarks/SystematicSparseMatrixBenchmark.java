/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.benchmarks.benchmark.benchmarks;

import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
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
    private static final int POOL_SIZE = 10;
    private SparseMatrix<Real>[] matricesA;
    private SparseMatrix<Real>[] matricesB;
    private SparseLinearAlgebraProvider<Real> currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "sparse-matrix-systematic"; }
    @Override public String getNameBase() { return "Sparse Matrix Multiplication"; }
    @Override 
    public Class<SparseLinearAlgebraProvider<Real>> getProviderClass() { 
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Class<SparseLinearAlgebraProvider<Real>> clazz = (Class) SparseLinearAlgebraProvider.class; 
        return clazz;
    }

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
        int actualSize = SIZE;
        @SuppressWarnings("unchecked")
        SparseMatrix<Real>[] storageA = (SparseMatrix<Real>[]) new SparseMatrix[POOL_SIZE];
        matricesA = storageA;
        @SuppressWarnings("unchecked")
        SparseMatrix<Real>[] storageB = (SparseMatrix<Real>[]) new SparseMatrix[POOL_SIZE];
        matricesB = storageB;
        Random r = new Random(42);
        Reals ring = Reals.getInstance();
        
        for (int p = 0; p < POOL_SIZE; p++) {
            SparseMatrix<Real> a = SparseMatrix.zeros(actualSize, actualSize, ring);
            SparseMatrix<Real> b = SparseMatrix.zeros(actualSize, actualSize, ring);
            
            for (int i = 0; i < actualSize; i++) {
                for (int k = 0; k < actualSize * SPARSITY; k++) {
                    a.set(i, r.nextInt(actualSize), Real.of(r.nextDouble()));
                }
            }
            for (int i = 0; i < actualSize; i++) {
                for (int k = 0; k < actualSize * SPARSITY; k++) {
                    b.set(i, r.nextInt(actualSize), Real.of(r.nextDouble()));
                }
            }
            matricesA[p] = a;
            matricesB[p] = b;
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
            for (int i = 0; i < POOL_SIZE; i++) {
                currentProvider.multiply(matricesA[i], matricesB[i]);
            }
        }
    }

    @Override
    public void teardown() {
        matricesA = null;
        matricesB = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 5; // Each iteration now does 10 multiplications
    }
}
