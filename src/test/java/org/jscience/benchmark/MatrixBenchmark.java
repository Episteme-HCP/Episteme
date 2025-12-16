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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.benchmark;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Benchmarks for Matrix operations (CPU vs GPU).
 * <p>
 * Measures performance of addition, multiplication, and other matrix operations
 * across different implementations (DenseMatrix, SparseMatrix, CudaMatrix).
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class MatrixBenchmark {

    private static final int SIZE_SMALL = 100;
    @SuppressWarnings("unused")
    private static final int SIZE_MEDIUM = 500; // Reserved for benchmark expansion
    // Large might be too slow for a quick test, let's stick to medium for now or
    // keep it smallish.

    public static void run() {
        System.out.println("\n--- Matrix Multiplication Benchmarks ---");

        benchmarkRawDouble(SIZE_SMALL);
        benchmarkDenseMatrix(SIZE_SMALL);

        // benchmarkRawDouble(SIZE_MEDIUM);
        // benchmarkDenseMatrix(SIZE_MEDIUM);
    }

    private static void benchmarkRawDouble(int size) {
        double[][] A = createRandomArray(size);
        double[][] B = createRandomArray(size);

        BenchmarkRunner.run("Raw double[][] Multiplication (" + size + "x" + size + ")", () -> {
            multiplyRaw(A, B);
        });
    }

    private static void benchmarkDenseMatrix(int size) {
        Matrix<Real> A = createRandomMatrix(size);
        Matrix<Real> B = createRandomMatrix(size);

        BenchmarkRunner.run("JScience DenseMatrix Multiplication (" + size + "x" + size + ")", () -> {
            A.multiply(B);
        });
    }

    private static double[][] multiplyRaw(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                double a = A[i][k];
                for (int j = 0; j < n; j++) {
                    C[i][j] += a * B[k][j];
                }
            }
        }
        return C;
    }

    private static double[][] createRandomArray(int size) {
        Random r = new Random(42);
        double[][] m = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                m[i][j] = r.nextDouble();
            }
        }
        return m;
    }

    private static Matrix<Real> createRandomMatrix(int size) {
        Random r = new Random(42);
        List<List<Real>> rows = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            List<Real> row = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                row.add(Real.of(r.nextDouble()));
            }
            rows.add(row);
        }
        return new DenseMatrix<>(rows, org.jscience.mathematics.sets.Reals.getInstance());
    }
}
