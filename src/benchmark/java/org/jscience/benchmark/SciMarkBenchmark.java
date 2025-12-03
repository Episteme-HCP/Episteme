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
 * LIABILITY, WHETHER IN AN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.distributed.DistributedFFT;
import org.jscience.distributed.DistributedMonteCarlo;
import org.jscience.mathematics.number.Real;
import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * SciMark 2.0 with DUAL implementations:
 * 1. Naive (pure double arrays) - baseline CPU performance
 * 2. JScience (Real + contexts) - optimized with backend switching
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class SciMarkBenchmark {

    // ========== NAIVE IMPLEMENTATIONS (Baseline) ==========

    /**
     * Naive FFT using direct double arrays - BASELINE.
     */
    public static BenchmarkResult naiveFFT(int size) {
        long start = System.nanoTime();

        double[] real = new double[size];
        double[] imag = new double[size];
        for (int i = 0; i < size; i++) {
            real[i] = Math.random();
            imag[i] = Math.random();
        }

        // Simplified FFT (Cooley-Tukey butterfly)
        fftNaive(real, imag);

        long end = System.nanoTime();
        double mflops = (5.0 * size * Math.log(size) / Math.log(2)) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("FFT Naive(" + size + ")", mflops, (end - start) / 1e6);
    }

    private static void fftNaive(double[] real, double[] imag) {
        int n = real.length;
        if (n <= 1)
            return;

        // Simplified radix-2 FFT
        for (int i = 0; i < n; i++) {
            int j = Integer.reverse(i) >>> (32 - (int) (Math.log(n) / Math.log(2)));
            if (j > i) {
                double tr = real[i];
                real[i] = real[j];
                real[j] = tr;
                double ti = imag[i];
                imag[i] = imag[j];
                imag[j] = ti;
            }
        }

        for (int s = 1; s <= Math.log(n) / Math.log(2); s++) {
            int m = 1 << s;
            double wm_real = Math.cos(-2 * Math.PI / m);
            double wm_imag = Math.sin(-2 * Math.PI / m);

            for (int k = 0; k < n; k += m) {
                double w_real = 1;
                double w_imag = 0;

                for (int j = 0; j < m / 2; j++) {
                    double t_real = w_real * real[k + j + m / 2] - w_imag * imag[k + j + m / 2];
                    double t_imag = w_real * imag[k + j + m / 2] + w_imag * real[k + j + m / 2];

                    double u_real = real[k + j];
                    double u_imag = imag[k + j];

                    real[k + j] = u_real + t_real;
                    imag[k + j] = u_imag + t_imag;
                    real[k + j + m / 2] = u_real - t_real;
                    imag[k + j + m / 2] = u_imag - t_imag;

                    double w_temp = w_real;
                    w_real = w_real * wm_real - w_imag * wm_imag;
                    w_imag = w_temp * wm_imag + w_imag * wm_real;
                }
            }
        }
    }

    /**
     * Naive SOR using double[][] - BASELINE.
     */
    public static BenchmarkResult naiveSOR(int size, int iterations) {
        long start = System.nanoTime();

        double[][] grid = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Math.random();
            }
        }

        double omega = 1.25;
        for (int iter = 0; iter < iterations; iter++) {
            for (int i = 1; i < size - 1; i++) {
                for (int j = 1; j < size - 1; j++) {
                    double old = grid[i][j];
                    double neighbors = grid[i - 1][j] + grid[i + 1][j] + grid[i][j - 1] + grid[i][j + 1];
                    grid[i][j] = old + omega * (0.25 * neighbors - old);
                }
            }
        }

        long end = System.nanoTime();
        double mflops = (iterations * (size - 2) * (size - 2) * 6.0) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("SOR Naive(" + size + ")", mflops, (end - start) / 1e6);
    }

    /**
     * Naive Monte Carlo using double - BASELINE.
     */
    public static BenchmarkResult naiveMonteCarlo(int numPoints) {
        long start = System.nanoTime();

        int inside = 0;
        for (int i = 0; i < numPoints; i++) {
            double x = Math.random();
            double y = Math.random();
            if (x * x + y * y <= 1.0)
                inside++;
        }

        double pi = 4.0 * inside / numPoints;
        long end = System.nanoTime();
        double mflops = (numPoints * 4.0) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("MonteCarlo Naive(" + numPoints + ")", mflops, (end - start) / 1e6);
    }

    /**
     * Naive Sparse MatMult using arrays - BASELINE.
     */
    public static BenchmarkResult naiveSparseMatMult(int N, int nz) {
        long start = System.nanoTime();

        int[] row = new int[nz];
        int[] col = new int[nz];
        double[] val = new double[nz];

        for (int i = 0; i < nz; i++) {
            row[i] = (int) (Math.random() * N);
            col[i] = (int) (Math.random() * N);
            val[i] = Math.random();
        }

        double[] x = new double[N];
        double[] y = new double[N];
        for (int i = 0; i < N; i++)
            x[i] = Math.random();

        for (int i = 0; i < nz; i++) {
            y[row[i]] += val[i] * x[col[i]];
        }

        long end = System.nanoTime();
        double mflops = (nz * 2.0) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("Sparse Naive(N=" + N + ")", mflops, (end - start) / 1e6);
    }

    /**
     * Naive LU Decomposition - BASELINE.
     */
    public static BenchmarkResult naiveLU(int size) {
        long start = System.nanoTime();

        double[][] A = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                A[i][j] = Math.random();
            }
        }

        // In-place LU (Doolittle algorithm)
        for (int k = 0; k < size; k++) {
            for (int i = k + 1; i < size; i++) {
                A[i][k] /= A[k][k];
                for (int j = k + 1; j < size; j++) {
                    A[i][j] -= A[i][k] * A[k][j];
                }
            }
        }

        long end = System.nanoTime();
        double mflops = ((2.0 * size * size * size) / 3.0) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("LU Naive(" + size + ")", mflops, (end - start) / 1e6);
    }

    // ========== JSCIENCE IMPLEMENTATIONS ==========

    public static BenchmarkResult jscienceLU(int size) {
        long start = System.nanoTime();

        Field<Real> field = org.jscience.mathematics.number.Reals.getInstance();
        Real[][] data = new Real[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = Real.of(Math.random());
            }
        }
        Matrix<Real> matrix = DenseMatrix.of(data, field);

        ComputeContext context = ComputeContext.global();
        LinearAlgebraService<Real> service = context.getService(LinearAlgebraService.class);
        var lu = service.luDecomposition(matrix);

        long end = System.nanoTime();
        double mflops = ((2.0 * size * size * size) / 3.0) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("LU JScience(" + size + ")", mflops, (end - start) / 1e6);
    }

    public static BenchmarkResult jscienceMonteCarlo(int numPoints) {
        long start = System.nanoTime();

        int inside = 0;
        for (int i = 0; i < numPoints; i++) {
            Real x = Real.of(Math.random());
            Real y = Real.of(Math.random());
            if (x.multiply(x).add(y.multiply(y)).compareTo(Real.ONE) <= 0)
                inside++;
        }

        Real pi = Real.of(4.0 * inside / numPoints);
        long end = System.nanoTime();
        double mflops = (numPoints * 4.0) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("MonteCarlo JScience(" + numPoints + ")", mflops, (end - start) / 1e6);
    }

    /**
     * JScience SOR using Matrix operations.
     */
    public static BenchmarkResult jscienceSOR(int size, int iterations) {
        long start = System.nanoTime();

        Field<Real> field = org.jscience.mathematics.number.Reals.getInstance();

        // Create matrix
        Real[][] data = new Real[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = Real.of(Math.random());
            }
        }
        Matrix<Real> grid = DenseMatrix.of(data, field);

        Real omega = Real.of(1.25);

        // SOR iterations using Matrix operations
        for (int iter = 0; iter < iterations; iter++) {
            Real[][] tempData = new Real[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    tempData[i][j] = grid.get(i, j);
                }
            }

            for (int i = 1; i < size - 1; i++) {
                for (int j = 1; j < size - 1; j++) {
                    Real old = grid.get(i, j);
                    Real neighbors = grid.get(i - 1, j).add(grid.get(i + 1, j))
                            .add(grid.get(i, j - 1)).add(grid.get(i, j + 1));
                    tempData[i][j] = old.add(omega.multiply(
                            Real.of(0.25).multiply(neighbors).subtract(old)));
                }
            }
            grid = DenseMatrix.of(tempData, field);
        }

        long end = System.nanoTime();
        double mflops = (iterations * (size - 2) * (size - 2) * 6.0) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("SOR JScience(" + size + "x" + size + ")", mflops, (end - start) / 1e6);
    }

    /**
     * JScience FFT using DiscreteFourierTransform.
     */
    public static BenchmarkResult jscienceFFT(int size) {
        long start = System.nanoTime();

        // Create complex data
        Complex[] data = new Complex[size];
        for (int i = 0; i < size; i++) {
            data[i] = Complex.of(Math.random(), Math.random());
        }

        // Use JScience FFT
        Complex[] transformed = DiscreteFourierTransform.transform(data);

        long end = System.nanoTime();
        double mflops = (5.0 * size * Math.log(size) / Math.log(2)) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("FFT JScience(" + size + ")", mflops, (end - start) / 1e6);
    }

    /**
     * JScience Sparse Matrix Multiply using SparseMatrix.
     */
    public static BenchmarkResult jscienceSparseMatMult(int N, int nz) {
        long start = System.nanoTime();

        Field<Real> field = org.jscience.mathematics.number.Reals.getInstance();

        // Create sparse matrix manually (simplified)
        Map<Integer, Map<Integer, Real>> sparseData = new HashMap<>();
        for (int i = 0; i < nz; i++) {
            int row = (int) (Math.random() * N);
            int col = (int) (Math.random() * N);
            Real val = Real.of(Math.random());

            sparseData.putIfAbsent(row, new HashMap<>());
            sparseData.get(row).put(col, val);
        }

        // Create vector
        List<Real> xData = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            xData.add(Real.of(Math.random()));
        }
        Vector<Real> x = DenseVector.of(xData, field);

        // Sparse matrix-vector multiply
        Real[] y = new Real[N];
        Arrays.fill(y, Real.ZERO);

        for (Map.Entry<Integer, Map<Integer, Real>> rowEntry : sparseData.entrySet()) {
            int row = rowEntry.getKey();
            for (Map.Entry<Integer, Real> colEntry : rowEntry.getValue().entrySet()) {
                int col = colEntry.getKey();
                Real val = colEntry.getValue();
                y[row] = y[row].add(val.multiply(x.get(col)));
            }
        }

        long end = System.nanoTime();
        double mflops = (nz * 2.0) / ((end - start) / 1e9) / 1e6;
        return new BenchmarkResult("Sparse JScience(N=" + N + ", nz=" + nz + ")", mflops, (end - start) / 1e6);
    }

    // ========== BENCHMARK RESULT ==========

    public static class BenchmarkResult {
        public final String name;
        public final double mflops;
        public final double timeMs;

        public BenchmarkResult(String name, double mflops, double timeMs) {
            this.name = name;
            this.mflops = mflops;
            this.timeMs = timeMs;
        }

        @Override
        public String toString() {
            return String.format("%-50s %10.2f Mflops  (%8.2f ms)", name, mflops, timeMs);
        }
    }

    // ========== MAIN ==========

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("JScience SciMark 2.0 - Baseline vs Optimized Comparison");
        System.out.println("=".repeat(80));

        System.out.println("\n--- BASELINE (Naive double arrays) ---");
        System.out.println(naiveFFT(1024));
        System.out.println(naiveSOR(100, 100));
        System.out.println(naiveMonteCarlo(1000000));
        System.out.println(naiveSparseMatMult(1000, 5000));
        System.out.println(naiveLU(100));

        System.out.println("\n--- JSCIENCE (Real + Matrix/Vector classes) ---");
        System.out.println(jscienceFFT(1024));
        System.out.println(jscienceSOR(100, 100));
        System.out.println(jscienceMonteCarlo(1000000));
        System.out.println(jscienceSparseMatMult(1000, 5000));
        System.out.println(jscienceLU(100));

        System.out.println("\n" + "=".repeat(80));
        System.out.println("Comparison shows overhead/speedup of JScience vs baseline");
        System.out.println("Run with: mvn exec:java -Dexec.mainClass=\"org.jscience.benchmarking.SciMarkBenchmark\"");
        System.out.println("=".repeat(80));
    }
}
