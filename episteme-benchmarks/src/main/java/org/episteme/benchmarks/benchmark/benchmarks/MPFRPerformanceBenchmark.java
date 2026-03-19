/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.benchmarks.benchmark.benchmarks;

import com.google.auto.service.AutoService;
import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;

import java.util.Arrays;
import java.util.Random;

/**
 * Performance benchmark comparing Native MPFR Sparse Solver vs generic Java Sparse Solver.
 */
@AutoService(RunnableBenchmark.class)
public class MPFRPerformanceBenchmark implements SystematicBenchmark<SparseLinearAlgebraProvider<Real>> {

    private static final int SIZE = 200;
    private static final double SPARSITY = 0.01; // 1% non-zero elements
    private static final int PRECISION = 100;
    
    private SparseMatrix<Real> A;
    private Vector<Real> b;
    private SparseLinearAlgebraProvider<Real> currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "sparse-solve-mpfr-performance"; }
    @Override public String getNameBase() { return "Sparse Solve: Native MPFR vs Generic Java"; }
    @Override public String getDescription() { 
        return "Sparse System Solve (Ax=b, 200x200, 1% sparsity) at " + PRECISION + " digits precision."; 
    }
    @Override public String getDomain() { return "Linear Algebra (High-Precision Sparse)"; }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override public Class<SparseLinearAlgebraProvider<Real>> getProviderClass() { return (Class) SparseLinearAlgebraProvider.class; }

    @Override
    public void setup() {
        Random r = new Random(42);
        Reals ring = Reals.getInstance();
        
        // Ensure the benchmark uses the requested precision
        MathContext.withPrecision(PRECISION + 5).compute(() -> {
            SparseMatrixStorage<Real> storage = new SparseMatrixStorage<>(SIZE, SIZE, Real.ZERO);
            for (int i = 0; i < SIZE; i++) {
                // Dominant diagonal to ensure convergence
                storage.set(i, i, Real.of(SIZE + r.nextDouble()));
                for (int k = 0; k < SIZE * SPARSITY; k++) {
                    int col = r.nextInt(SIZE);
                    if (col != i) {
                        storage.set(i, col, Real.of(r.nextDouble()));
                    }
                }
            }
            A = new SparseMatrix<>(storage, Real.ZERO);
            
            Real[] bData = new Real[SIZE];
            for (int i = 0; i < SIZE; i++) bData[i] = Real.of(r.nextDouble());
            b = DenseVector.of(Arrays.asList(bData), ring);
            
            return null;
        });
    }

    @Override
    public void setProvider(SparseLinearAlgebraProvider<Real> provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            MathContext.withPrecision(PRECISION + 5).compute(() -> {
                currentProvider.solve(A, b);
                return null;
            });
        }
    }

    @Override
    public void teardown() {
        A = null;
        b = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 3;
    }
}
