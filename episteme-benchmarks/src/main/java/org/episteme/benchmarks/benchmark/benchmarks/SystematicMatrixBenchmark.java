/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.benchmarks.benchmark.benchmarks;

import org.episteme.benchmarks.benchmark.RunnableBenchmark;

import com.google.auto.service.AutoService;

import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.numbers.real.Real;
import java.util.Random;

/**
 * A benchmark that systematically tests all available LinearAlgebraProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicMatrixBenchmark implements SystematicBenchmark<LinearAlgebraProvider<Real>> {

    private static final int SIZE = 1024;
    private static final int DRY_RUN_SIZE = 16;
    private static final int POOL_SIZE = 1;
    private RealDoubleMatrix[] matricesA;
    private RealDoubleMatrix[] matricesB;
    private LinearAlgebraProvider<Real> currentProvider;
    private boolean dryRun = false;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "matrix-systematic"; }
    @Override public String getNameBase() { return "Matrix Multiplication"; }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override public Class<LinearAlgebraProvider<Real>> getProviderClass() { return (Class) LinearAlgebraProvider.class; }

    @Override
    public String getDescription() {
        return "Dense Matrix Multiplication (1024x1024), Double Precision";
    }

    @Override
    public String getDomain() {
        return "Linear Algebra (Dense)";
    }

    @Override
    public void setup() {
        int actualSize = dryRun ? DRY_RUN_SIZE : SIZE;
        matricesA = new RealDoubleMatrix[POOL_SIZE];
        matricesB = new RealDoubleMatrix[POOL_SIZE];
        Random r = new Random(42);
        for (int i = 0; i < POOL_SIZE; i++) {
            matricesA[i] = RealDoubleMatrix.of(generateData(actualSize, r));
            matricesB[i] = RealDoubleMatrix.of(generateData(actualSize, r));
        }
    }

    @Override public void setDryRun(boolean dryRun) { this.dryRun = dryRun; }
    @Override public boolean isDryRun() { return dryRun; }

    @Override
    public void setProvider(LinearAlgebraProvider<Real> provider) {
        this.currentProvider = provider;
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

    private double[][] generateData(int n, Random r) {
        double[][] d = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                d[i][j] = r.nextDouble();
        return d;
    }
}
