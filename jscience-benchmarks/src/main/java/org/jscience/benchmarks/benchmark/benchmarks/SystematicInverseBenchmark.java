/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.Random;

/**
 * A benchmark that tests Matrix Inversion performance.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicInverseBenchmark implements SystematicBenchmark<LinearAlgebraProvider<Real>> {

    private static final int SIZE = 500; // Inversion is O(N^3) and expensive
    private static final int POOL_SIZE = 10;
    private RealDoubleMatrix[] matricesA;
    private LinearAlgebraProvider<Real> currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "matrix-inverse"; }
    @Override public String getNameBase() { return "Matrix Inversion"; }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override public Class<LinearAlgebraProvider<Real>> getProviderClass() { return (Class) LinearAlgebraProvider.class; }

    @Override
    public String getDescription() {
        return "Matrix Inversion (500x500), Double Precision";
    }

    @Override
    public String getDomain() {
        return "Matrix Inversion";
    }

    @Override
    public void setup() {
        matricesA = new RealDoubleMatrix[POOL_SIZE];
        Random r = new Random(42);
        
        for (int p = 0; p < POOL_SIZE; p++) {
            double[][] data = new double[SIZE][SIZE];
            for (int i = 0; i < SIZE; i++) {
                double sum = 0;
                for (int j = 0; j < SIZE; j++) {
                    if (i != j) {
                        data[i][j] = r.nextDouble();
                        sum += Math.abs(data[i][j]);
                    }
                }
                data[i][i] = sum + 1.0 + r.nextDouble(); // Ensure dominant diagonal
            }
            matricesA[p] = RealDoubleMatrix.of(data);
        }
    }

    @Override
    public void setProvider(LinearAlgebraProvider<Real> provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            for (int i = 0; i < POOL_SIZE; i++) {
                currentProvider.inverse(matricesA[i]);
            }
        }
    }

    @Override
    public void teardown() {
        matricesA = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 2; // Each iteration now does 10 inversions
    }
}
