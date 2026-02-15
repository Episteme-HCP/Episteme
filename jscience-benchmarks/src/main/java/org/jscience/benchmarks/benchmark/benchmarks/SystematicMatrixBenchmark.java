/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;

import com.google.auto.service.AutoService;

import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.numbers.real.Real;
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
    private RealDoubleMatrix A;
    private RealDoubleMatrix B;
    private LinearAlgebraProvider<Real> currentProvider;

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
        double[][] dataA = generateData(SIZE);
        double[][] dataB = generateData(SIZE);
        A = RealDoubleMatrix.of(dataA);
        B = RealDoubleMatrix.of(dataB);
    }

    @Override
    public void setProvider(LinearAlgebraProvider<Real> provider) {
        this.currentProvider = provider;
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
        return 100;
    }

    private double[][] generateData(int n) {
        Random r = new Random(42);
        double[][] d = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                d[i][j] = r.nextDouble();
        return d;
    }
}
