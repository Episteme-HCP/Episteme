/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import java.util.Random;

/**
 * A benchmark that tests Matrix Solve (Ax = B) performance.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicSolveBenchmark implements SystematicBenchmark<LinearAlgebraProvider<Real>> {

    private static final int SIZE = 1500;
    private RealDoubleMatrix A;
    private RealDoubleVector B;
    private LinearAlgebraProvider<Real> currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "matrix-solve"; }
    @Override public String getNameBase() { return "Linear System Solve"; }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override public Class<LinearAlgebraProvider<Real>> getProviderClass() { return (Class) LinearAlgebraProvider.class; }

    @Override
    public String getDescription() {
        return "Linear System Solver (Ax=B, 1500x1500), Double Precision";
    }

    @Override
    public String getDomain() {
        return "Linear Algebra (Dense)";
    }

    @Override
    public void setup() {
        // Generate a random INVERTIBLE matrix (diagonally dominant)
        Random r = new Random(42);
        double[][] dataA = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            double sum = 0;
            for (int j = 0; j < SIZE; j++) {
                if (i != j) {
                    dataA[i][j] = r.nextDouble();
                    sum += Math.abs(dataA[i][j]);
                }
            }
            dataA[i][i] = sum + 1.0 + r.nextDouble(); // Ensure dominant diagonal
        }
        A = RealDoubleMatrix.of(dataA);
        
        double[] dataB = new double[SIZE];
        for (int i=0; i<SIZE; i++) dataB[i] = r.nextDouble();
        B = RealDoubleVector.of(dataB);
    }

    @Override
    public void setProvider(LinearAlgebraProvider<Real> provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.solve(A, B);
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
