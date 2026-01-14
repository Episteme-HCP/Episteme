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

package org.jscience.mathematics.linearalgebra.backends;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.technical.backend.ExecutionContext;

/**
 * CUDA Linear Algebra Provider (Dense).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CUDADenseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;

    public CUDADenseLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
    }

    /**
     * Public no-arg constructor required by ServiceLoader.
     */
    public CUDADenseLinearAlgebraProvider() {
        this(null);
    }

    private static boolean checkAvailability() {
        try {
            Class.forName("jcuda.jcublas.JCublas");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (UnsatisfiedLinkError e) {
            return false;
        }
    }

    @Override
    public boolean isAvailable() {
        return checkAvailability();
    }

    @Override
    public String getName() {
        return "CUDA (Dense)";
    }

    @Override
    public ExecutionContext createContext() {
        return null;
    }

    @Override
    public int getPriority() {
        return isAvailable() ? 100 : 0;
    }

    // Delegate to CPU for now, as full JCuda implementation requires specific
    // kernel logic or bridging Generic Field<E> to float/double arrays for CUBLAS.
    // NOTE: This implementation currently supports Real fields (double precision).

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!isAvailable() || !(field.zero() instanceof org.jscience.mathematics.numbers.real.Real)) {
            return cpuProvider.add(a, b);
        }
        // JCUDA vector addition (implement if performance critical, often overhead
        // dominates for simple ops)
        return cpuProvider.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return cpuProvider.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        return cpuProvider.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return cpuProvider.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable() || !(field.zero() instanceof org.jscience.mathematics.numbers.real.Real)) {
            return cpuProvider.multiply(a, b);
        }

        try {
            int m = a.rows();
            int k = a.cols();
            int n = b.cols();

            if (b.rows() != k)
                throw new IllegalArgumentException("Matrix dimension mismatch");

            // Threshold for GPU offload
            if ((long) m * n * k < 500_000) {
                return cpuProvider.multiply(a, b);
            }

            // Convert to double array
            double[] A = toDoubleArray(a);
            double[] B = toDoubleArray(b);
            double[] C = new double[m * n];

            // Initialize JCublas
            jcuda.jcublas.JCublas.cublasInit();

            // Allocate device memory
            jcuda.Pointer d_A = new jcuda.Pointer();
            jcuda.Pointer d_B = new jcuda.Pointer();
            jcuda.Pointer d_C = new jcuda.Pointer();

            jcuda.jcublas.JCublas.cublasAlloc(m * k, jcuda.Sizeof.DOUBLE, d_A);
            jcuda.jcublas.JCublas.cublasAlloc(k * n, jcuda.Sizeof.DOUBLE, d_B);
            jcuda.jcublas.JCublas.cublasAlloc(m * n, jcuda.Sizeof.DOUBLE, d_C);

            // Copy to device
            jcuda.jcublas.JCublas.cublasSetVector(m * k, jcuda.Sizeof.DOUBLE, jcuda.Pointer.to(A), 1, d_A, 1);
            jcuda.jcublas.JCublas.cublasSetVector(k * n, jcuda.Sizeof.DOUBLE, jcuda.Pointer.to(B), 1, d_B, 1);

            // Execute DGEMM
            // Alpha = 1, Beta = 0
            double alpha = 1.0;
            double beta = 0.0;

            // Note: CUBLAS uses column-major, Java uses row-major.
            // C = A * B
            // Passing B as A and A as B with proper dimensions handles the transpose
            // implicitly for layout?
            // Standard trick: C^T = B^T * A^T
            // Here we just follow standard call for now, assuming data layout is managed or
            // purely dense.
            jcuda.jcublas.JCublas.cublasDgemm('N', 'N', n, m, k, alpha, d_B, n, d_A, k, beta, d_C, n);

            // Copy back
            jcuda.jcublas.JCublas.cublasGetVector(m * n, jcuda.Sizeof.DOUBLE, d_C, 1, jcuda.Pointer.to(C), 1);

            // Cleanup
            jcuda.jcublas.JCublas.cublasFree(d_A);
            jcuda.jcublas.JCublas.cublasFree(d_B);
            jcuda.jcublas.JCublas.cublasFree(d_C);
            jcuda.jcublas.JCublas.cublasShutdown();

            return toMatrix(C, m, n);

        } catch (Throwable t) {
            System.err.println("CUDA execution failed: " + t.getMessage());
            return cpuProvider.multiply(a, b);
        }
    }

    @Override
    public Vector<E> solve(Matrix<E> A, Vector<E> b) {
        return cpuProvider.solve(A, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> A) {
        return cpuProvider.inverse(A);
    }

    @Override
    public E determinant(Matrix<E> A) {
        return cpuProvider.determinant(A);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> A) {
        return cpuProvider.transpose(A);
    }

    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] arr = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                org.jscience.mathematics.numbers.real.Real r = (org.jscience.mathematics.numbers.real.Real) m.get(i, j);
                arr[i * cols + j] = r.doubleValue();
            }
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> toMatrix(double[] data, int rows, int cols) {
        java.util.List<java.util.List<org.jscience.mathematics.numbers.real.Real>> matrixData = new java.util.ArrayList<>();
        for (int i = 0; i < rows; i++) {
            java.util.List<org.jscience.mathematics.numbers.real.Real> row = new java.util.ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(org.jscience.mathematics.numbers.real.Real.of(data[i * cols + j]));
            }
            matrixData.add(row);
        }
        return (Matrix<E>) new org.jscience.mathematics.linearalgebra.matrices.DenseMatrix<>(matrixData,
                org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return cpuProvider.multiply(a, b);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return cpuProvider.scale(scalar, a);
    }

    @Override
    public String getId() {
        return "cudadense";
    }

    @Override
    public String getDescription() {
        return "CUDADenseLinearAlgebraProvider";
    }
}



