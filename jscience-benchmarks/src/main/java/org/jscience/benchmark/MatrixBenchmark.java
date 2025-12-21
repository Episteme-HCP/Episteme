/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.numbers.real.Real;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.ejml.simple.SimpleMatrix;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import org.jblas.DoubleMatrix;

/**
 * Matrix multiplication benchmark comparing JScience with other libraries.
 * <p>
 * Compares: JScience, Apache Commons Math, EJML, Colt, JBlas
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class MatrixBenchmark {

    @Param({ "128", "256", "512" })
    public int size;

    // JScience
    private Matrix<Real> A;
    private Matrix<Real> B;

    // Commons Math
    private RealMatrix cmA;
    private RealMatrix cmB;

    // EJML
    private SimpleMatrix ejmlA;
    private SimpleMatrix ejmlB;

    // Colt
    private DoubleMatrix2D coltA;
    private DoubleMatrix2D coltB;
    private static final Algebra coltAlgebra = new Algebra();

    // JBlas
    private DoubleMatrix jblasA;
    private DoubleMatrix jblasB;

    @Setup(Level.Trial)
    public void setup() {
        double[][] dataA = generateRandomData(size);
        double[][] dataB = generateRandomData(size);

        // No changes needed for imports if they match file structure.
        // But I will trigger a safe edit to force re-evaluation or just verify.
        // I will skip editing MatrixBenchmark if it looks correct, and focus on
        // rebuilding.
        // Actually, earlier compilation failed.
        // Maybe `DenseMatrix` constructor args are wrong?
        // Line 64: new ...DenseMatrix<>(toReal(dataA), Reals.getInstance());
        // DenseMatrix.java constructor might need checking.

        B = new org.jscience.mathematics.linearalgebra.matrices.DenseMatrix<>(toReal(dataB), Reals.getInstance());

        // Commons Math Setup
        cmA = new Array2DRowRealMatrix(dataA);
        cmB = new Array2DRowRealMatrix(dataB);

        // EJML Setup
        ejmlA = new SimpleMatrix(dataA);
        ejmlB = new SimpleMatrix(dataB);

        // Colt Setup
        coltA = new DenseDoubleMatrix2D(dataA);
        coltB = new DenseDoubleMatrix2D(dataB);

        // JBlas Setup
        jblasA = new DoubleMatrix(dataA);
        jblasB = new DoubleMatrix(dataB);
    }

    private Real[][] toReal(double[][] data) {
        int n = data.length;
        Real[][] reals = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                reals[i][j] = Real.of(data[i][j]);
            }
        }
        return reals;
    }

    @Benchmark
    public Matrix<Real> multiplyJScience() {
        return A.multiply(B);
    }

    @Benchmark
    public RealMatrix multiplyCommonsMath() {
        return cmA.multiply(cmB);
    }

    @Benchmark
    public SimpleMatrix multiplyEJML() {
        return ejmlA.mult(ejmlB);
    }

    @Benchmark
    public DoubleMatrix2D multiplyColt() {
        return coltAlgebra.mult(coltA, coltB);
    }

    @Benchmark
    public DoubleMatrix multiplyJBlas() {
        return jblasA.mmul(jblasB);
    }

    private double[][] generateRandomData(int n) {
        Random r = new Random(42);
        double[][] data = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = r.nextDouble();
            }
        }
        return data;
    }
}
