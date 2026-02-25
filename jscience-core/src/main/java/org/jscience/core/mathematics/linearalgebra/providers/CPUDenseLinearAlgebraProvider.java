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

package org.jscience.core.mathematics.linearalgebra.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jscience.core.mathematics.structures.rings.Field;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.*;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;

import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.vectors.GenericVector;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.QRDecomposition;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.SVDDecomposition;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.LUDecomposition;


/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({LinearAlgebraProvider.class, AlgorithmProvider.class})
public class CPUDenseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    protected final Field<E> field;
    private static final int PARALLEL_THRESHOLD = 1000;

    @SuppressWarnings("unchecked")
    public CPUDenseLinearAlgebraProvider(Field<E> field) {
        this.field = (field != null) ? field : (Field<E>) org.jscience.core.mathematics.sets.Reals.getInstance();
    }

    /**
     * Public no-arg constructor required by ServiceLoader.
     */
    public CPUDenseLinearAlgebraProvider() {
        this(null);
    }

    @Override
    public String getEnvironmentInfo() {
        return "CPU (Standard JVM)";
    }

    @Override
    public String getName() {
        return "JScience CPU (Dense)";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }


    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }

        if (a.dimension() < PARALLEL_THRESHOLD) {
            @SuppressWarnings("unchecked")
            E[] data = (E[]) new Object[a.dimension()];
            for (int i = 0; i < a.dimension(); i++) {
                data[i] = field.add(a.get(i), b.get(i));
            }
            return new GenericVector<>(
                    new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(data), this, field);
        } else {
            org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
            return IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> {
                        if (i % 512 == 0) ctx.checkCancelled();
                        return field.add(a.get(i), b.get(i));
                    })
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, field);
                            }));
        }
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }
        if (a.dimension() < PARALLEL_THRESHOLD) {
            List<E> result = new ArrayList<>(a.dimension());
            for (int i = 0; i < a.dimension(); i++) {
                E negB = field.negate(b.get(i));
                result.add(field.add(a.get(i), negB));
            }
            return new GenericVector<>(
                    new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(result), this,
                    field);
        } else {
            org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
            return IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> {
                        if (i % 512 == 0) ctx.checkCancelled();
                        E negB = field.negate(b.get(i));
                        return field.add(a.get(i), negB);
                    })
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, field);
                            }));
        }
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (vector.dimension() < PARALLEL_THRESHOLD) {
            List<E> result = new ArrayList<>(vector.dimension());
            for (int i = 0; i < vector.dimension(); i++) {
                result.add(field.multiply(vector.get(i), scalar));
            }
            // return new GenericVector(result.toArray(newFieldsElement[0]...), this,
            // field);
            // Handling array creation generically is hard without class token.
            // DenseVector dealt with List. GenericVector takes Array.
            // We can create Array from List if we have type?
            // E is generic.
            // We can convert List to Array if we cast.

            @SuppressWarnings("unchecked")
            E[] arr = (E[]) result.toArray(); // Safe if result contains E
            return new GenericVector<>(
                    new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(arr), this, field);
        } else {
            org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
            return IntStream.range(0, vector.dimension())
                    .parallel()
                    .mapToObj(i -> {
                        if (i % 512 == 0) ctx.checkCancelled();
                        return field.multiply(vector.get(i), scalar);
                    })
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, field);
                            }));
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "preview", "restricted"})
    public E dot(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }
        
        if (field instanceof org.jscience.core.mathematics.sets.Reals) {
            double sum = 0.0;
            int dim = a.dimension();
            for (int i = 0; i < dim; i++) {
                sum += ((Real) a.get(i)).doubleValue() * ((Real) b.get(i)).doubleValue();
            }
            return (E) (Object) Real.of(sum);
        }

        if (a.dimension() < PARALLEL_THRESHOLD) {
            E sum = field.zero();
            for (int i = 0; i < a.dimension(); i++) {
                E product = field.multiply(a.get(i), b.get(i));
                sum = field.add(sum, product);
            }
            return sum;
        } else {
            org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
            return IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> {
                        if (i % 512 == 0) ctx.checkCancelled();
                        return field.multiply(a.get(i), b.get(i));
                    })
                    .reduce(field.zero(), field::add);
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "preview", "restricted"})
    public E norm(Vector<E> a) {
        if (field instanceof org.jscience.core.mathematics.sets.Reals) {
            double sumSq = 0.0;
            int dim = a.dimension();
            for (int i = 0; i < dim; i++) {
                double val = ((Real) a.get(i)).doubleValue();
                sumSq += val * val;
            }
            return (E) (Object) Real.of(Math.sqrt(sumSq));
        }

        E dotProduct = dot(a, a);
        if (field instanceof org.jscience.core.mathematics.sets.Reals) {
            org.jscience.core.mathematics.numbers.real.Real r = (org.jscience.core.mathematics.numbers.real.Real) dotProduct;
            double val = r.doubleValue();
            return (E) (Object) org.jscience.core.mathematics.numbers.real.Real.of(Math.sqrt(val));
        }
        if (dotProduct instanceof org.jscience.core.mathematics.numbers.real.RealDouble) {
            org.jscience.core.mathematics.numbers.real.RealDouble rd = (org.jscience.core.mathematics.numbers.real.RealDouble) dotProduct;
            return (E) (Object) org.jscience.core.mathematics.numbers.real.RealDouble.of(Math.sqrt(rd.doubleValue()));
        }
        if (dotProduct instanceof org.jscience.core.mathematics.numbers.real.Real) {
            org.jscience.core.mathematics.numbers.real.Real r = (org.jscience.core.mathematics.numbers.real.Real) dotProduct;
            return (E) (Object) org.jscience.core.mathematics.numbers.real.Real.of(Math.sqrt(r.doubleValue()));
        }
        throw new UnsupportedOperationException("Norm not supported for field: " + field.getClass().getSimpleName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (a.getClass().getName().endsWith("SIMDRealDoubleMatrix") && b.getClass().getName().endsWith("SIMDRealDoubleMatrix")) {
            return a.add(b);
        }

        if (a.rows() != b.rows() || a.cols() != b.cols()) {
            throw new IllegalArgumentException("Matrix dimensions must match");
        }

        if (isReal(a) && isReal(b)) {
            int rows = a.rows();
            int cols = a.cols();
            double[] dataA = toDoubleArray(a);
            double[] dataB = toDoubleArray(b);
            double[] resData = new double[rows * cols];
            
            if (rows * cols < PARALLEL_THRESHOLD) {
                for (int i = 0; i < rows * cols; i++) {
                    resData[i] = dataA[i] + dataB[i];
                }
            } else {
                org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
                IntStream.range(0, rows).parallel().forEach(i -> {
                    ctx.checkCancelled();
                    int offset = i * cols;
                    for (int j = 0; j < cols; j++) {
                        resData[offset + j] = dataA[offset + j] + dataB[offset + j];
                    }
                });
            }
            return (Matrix<E>) (Matrix<?>) RealDoubleMatrix.of(resData, rows, cols);
        }

        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), a.cols(), field.zero());

        if (a.rows() * a.cols() < PARALLEL_THRESHOLD) {
            for (int i = 0; i < a.rows(); i++) {
                for (int j = 0; j < a.cols(); j++) {
                    storage.set(i, j, field.add(a.get(i, j), b.get(i, j)));
                }
            }
        } else {
            org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
            IntStream.range(0, a.rows()).parallel().forEach(i -> {
                ctx.checkCancelled();
                for (int j = 0; j < a.cols(); j++) {
                    storage.set(i, j, field.add(a.get(i, j), b.get(i, j)));
                }
            });
        }
        return new GenericMatrix<>(storage, this, field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (a.getClass().getName().endsWith("SIMDRealDoubleMatrix") && b.getClass().getName().endsWith("SIMDRealDoubleMatrix")) {
            return a.subtract(b);
        }

        if (a.rows() != b.rows() || a.cols() != b.cols()) {
            throw new IllegalArgumentException("Matrix dimensions must match");
        }

        if (isReal(a) && isReal(b)) {
            int rows = a.rows();
            int cols = a.cols();
            double[] dataA = toDoubleArray(a);
            double[] dataB = toDoubleArray(b);
            double[] resData = new double[rows * cols];
            
            if (rows * cols < PARALLEL_THRESHOLD) {
                for (int i = 0; i < rows * cols; i++) {
                    resData[i] = dataA[i] - dataB[i];
                }
            } else {
                org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
                IntStream.range(0, rows).parallel().forEach(i -> {
                    ctx.checkCancelled();
                    int offset = i * cols;
                    for (int j = 0; j < cols; j++) {
                        resData[offset + j] = dataA[offset + j] - dataB[offset + j];
                    }
                });
            }
            return (Matrix<E>) (Matrix<?>) RealDoubleMatrix.of(resData, rows, cols);
        }

        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), a.cols(), field.zero());

        if (a.rows() * a.cols() < PARALLEL_THRESHOLD) {
            for (int i = 0; i < a.rows(); i++) {
                for (int j = 0; j < a.cols(); j++) {
                    E negB = field.negate(b.get(i, j));
                    storage.set(i, j, field.add(a.get(i, j), negB));
                }
            }
        } else {
            org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
            IntStream.range(0, a.rows()).parallel().forEach(i -> {
                ctx.checkCancelled();
                for (int j = 0; j < a.cols(); j++) {
                    E negB = field.negate(b.get(i, j));
                    storage.set(i, j, field.add(a.get(i, j), negB));
                }
            });
        }
        return new GenericMatrix<>(storage, this, field);
    }

    @Override
    @SuppressWarnings({"unchecked", "preview", "restricted"})
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) {
            throw new IllegalArgumentException("Matrix inner dimensions must match");
        }

        // Generic Strassen for large power-of-two square matrices
        if (a.rows() >= 512 && a.cols() >= 512 && b.cols() >= 512
                && isSquarePowerOfTwo(a) && isSquarePowerOfTwo(b) && a.rows() == b.rows()) {
            return strassenRecursive(a, b);
        }

        return standardMultiply(a, b, field, this);
    }

    private boolean isSquarePowerOfTwo(Matrix<E> m) {
        return m.rows() == m.cols() && (m.rows() & (m.rows() - 1)) == 0;
    }

    private Matrix<E> strassenRecursive(Matrix<E> A, Matrix<E> B) {
        org.jscience.core.ComputeContext.checkCurrentCancelled();
        int n = A.rows();
        if (n <= 512) {
            return standardMultiply(A, B, field, this);
        }

        int newSize = n / 2;

        Matrix<E> A11 = A.getSubMatrix(0, newSize, 0, newSize);
        Matrix<E> A12 = A.getSubMatrix(0, newSize, newSize, n);
        Matrix<E> A21 = A.getSubMatrix(newSize, n, 0, newSize);
        Matrix<E> A22 = A.getSubMatrix(newSize, n, newSize, n);

        Matrix<E> B11 = B.getSubMatrix(0, newSize, 0, newSize);
        Matrix<E> B12 = B.getSubMatrix(0, newSize, newSize, n);
        Matrix<E> B21 = B.getSubMatrix(newSize, n, 0, newSize);
        Matrix<E> B22 = B.getSubMatrix(newSize, n, newSize, n);

        Matrix<E> M1 = strassenRecursive(add(A11, A22), add(B11, B22));
        Matrix<E> M2 = strassenRecursive(add(A21, A22), B11);
        Matrix<E> M3 = strassenRecursive(A11, subtract(B12, B22));
        Matrix<E> M4 = strassenRecursive(A22, subtract(B21, B11));
        Matrix<E> M5 = strassenRecursive(add(A11, A12), B22);
        Matrix<E> M6 = strassenRecursive(subtract(A21, A11), add(B11, B12));
        Matrix<E> M7 = strassenRecursive(subtract(A12, A22), add(B21, B22));

        Matrix<E> C11 = add(subtract(add(M1, M4), M5), M7);
        Matrix<E> C12 = add(M3, M5);
        Matrix<E> C21 = add(M2, M4);
        Matrix<E> C22 = add(subtract(add(M1, M3), M2), M6);

        return combineSubMatrices(C11, C12, C21, C22);
    }

    private Matrix<E> combineSubMatrices(Matrix<E> C11, Matrix<E> C12, Matrix<E> C21, Matrix<E> C22) {
        int n = C11.rows() * 2;
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(n, n, field.zero());

        copySubMatrixToStorage(storage, C11, 0, 0);
        copySubMatrixToStorage(storage, C12, 0, n / 2);
        copySubMatrixToStorage(storage, C21, n / 2, 0);
        copySubMatrixToStorage(storage, C22, n / 2, n / 2);

        return new GenericMatrix<>(storage, this, field);
    }

    private void copySubMatrixToStorage(DenseMatrixStorage<E> storage, Matrix<E> sub, int rowOffset, int colOffset) {
        for (int i = 0; i < sub.rows(); i++) {
            for (int j = 0; j < sub.cols(); j++) {
                storage.set(rowOffset + i, colOffset + j, sub.get(i, j));
            }
        }
    }

    @SuppressWarnings({"unchecked", "preview", "restricted"})
    public static <E> Matrix<E> standardMultiply(Matrix<E> a, Matrix<E> b, Field<E> field, LinearAlgebraProvider<E> provider) {
        int rowsA = a.rows();
        int colsA = a.cols();
        int colsB = b.cols();

        // Primitive Fast Path for Reals
        if (isReal(a) && isReal(b)) {
            double[] dataA = toDoubleArray(a);
            double[] dataB = toDoubleArray(b);
            double[] resData = new double[rowsA * colsB];

            long start = System.nanoTime();
            try {
                if (rowsA >= 64 && colsA >= 64 && colsB >= 64) {
                    // Tiled / Blocked multiplication for cache efficiency
                    staticTiledMultiply(dataA, dataB, resData, rowsA, colsA, colsB);
                } else {
                    // Optimized i-k-j loop for small-to-medium matrices
                    for (int i = 0; i < rowsA; i++) {
                        if (i % 64 == 0) org.jscience.core.ComputeContext.checkCurrentCancelled();
                        int rowOffsetA = i * colsA;
                        int rowOffsetC = i * colsB;
                        for (int k = 0; k < colsA; k++) {
                            double aik = dataA[rowOffsetA + k];
                            int rowOffsetB = k * colsB;
                            for (int j = 0; j < colsB; j++) {
                                resData[rowOffsetC + j] += aik * dataB[rowOffsetB + j];
                            }
                        }
                    }
                }
            } finally {
                org.jscience.core.util.PerformanceLogger.log("CPU:RealFastMultiply", 
                    a.rows() + "x" + b.cols(), System.nanoTime() - start);
            }
            return (Matrix<E>) (Matrix<?>) RealDoubleMatrix.of(resData, rowsA, colsB);
        }

        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), b.cols(), field.zero());
        long start = System.nanoTime();
        try {
            if (a.rows() < 10) {
                for (int i = 0; i < a.rows(); i++) {
                    for (int j = 0; j < b.cols(); j++) {
                        E sum = field.zero();
                        for (int k = 0; k < a.cols(); k++) {
                            E product = field.multiply(a.get(i, k), b.get(k, j));
                            sum = field.add(sum, product);
                        }
                        storage.set(i, j, sum);
                    }
                }
            } else {
                org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
                IntStream.range(0, a.rows()).parallel().forEach(i -> {
                    if (i % 64 == 0) ctx.checkCancelled();
                    for (int j = 0; j < b.cols(); j++) {
                        E sum = field.zero();
                        for (int k = 0; k < a.cols(); k++) {
                            E product = field.multiply(a.get(i, k), b.get(k, j));
                            sum = field.add(sum, product);
                        }
                        storage.set(i, j, sum);
                    }
                });
            }
        } finally {
            String context = "Unknown";
            if (a instanceof GenericMatrix<?>) {
                context = ((GenericMatrix<?>) a).getStorage().getClass().getSimpleName();
            } else {
                context = a.getClass().getSimpleName();
            }
            org.jscience.core.util.PerformanceLogger.log("CPU:GenericMultiply", context, System.nanoTime() - start);
        }
        return new GenericMatrix<>(storage, provider, field);
    }

    private static boolean isReal(Matrix<?> m) {
        return m.getScalarRing() instanceof org.jscience.core.mathematics.sets.Reals || m instanceof org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
    }

    private static double[] toDoubleArray(Matrix<?> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        if (m instanceof org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
            return ((org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) m).toDoubleArray();
        }
        if (m.getClass().getName().endsWith("SIMDRealDoubleMatrix")) {
            try {
                return (double[]) m.getClass().getMethod("getInternalData").invoke(m);
            } catch (Exception e) {
                // Fallback handled below
            }
        }
        if (m instanceof GenericMatrix) {
            org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<?> storage = 
                ((GenericMatrix<?>) m).getStorage();
            if (storage instanceof org.jscience.core.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage) {
                return ((org.jscience.core.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage) storage).getData();
            }
        }
        // Fallback: full copy
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object val = m.get(i, j);
                if (val instanceof Real) {
                    data[i * cols + j] = ((Real) val).doubleValue();
                } else if (val instanceof Number) {
                    data[i * cols + j] = ((Number) val).doubleValue();
                }
            }
        }
        return data;
    }

    private static void staticTiledMultiply(double[] A, double[] B, double[] C, int M, int K, int N) {
        final int BLOCK_SIZE = 64; 
        org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
        IntStream.range(0, (M + BLOCK_SIZE - 1) / BLOCK_SIZE).parallel().forEach(bi -> {
            ctx.checkCancelled();
            int i0 = bi * BLOCK_SIZE;
            int iMax = Math.min(i0 + BLOCK_SIZE, M);
            for (int k0 = 0; k0 < K; k0 += BLOCK_SIZE) {
                int kMax = Math.min(k0 + BLOCK_SIZE, K);
                for (int j0 = 0; j0 < N; j0 += BLOCK_SIZE) {
                    int jMax = Math.min(j0 + BLOCK_SIZE, N);
                    
                    for (int i = i0; i < iMax; i++) {
                        int rowA = i * K;
                        int rowC = i * N;
                        for (int k = k0; k < kMax; k++) {
                            double aik = A[rowA + k];
                            int rowB = k * N;
                            for (int j = j0; j < jMax; j++) {
                                C[rowC + j] += aik * B[rowB + j];
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    @SuppressWarnings({"unchecked", "preview", "restricted"})
    public Matrix<E> transpose(Matrix<E> a) {
        if (isReal(a)) {
            int rows = a.rows();
            int cols = a.cols();
            double[] data = toDoubleArray(a);
            double[] resData = new double[rows * cols];
            for (int i = 0; i < rows; i++) {
                int offsetI = i * cols;
                for (int j = 0; j < cols; j++) {
                    resData[j * rows + i] = data[offsetI + j];
                }
            }
            return (Matrix<E>) (Matrix<?>) RealDoubleMatrix.of(resData, cols, rows);
        }

        if (a.getClass().getName().endsWith("SIMDRealDoubleMatrix")) {
            try {
                return (Matrix<E>) a.getClass().getMethod("transpose").invoke(a);
            } catch (Exception e) {
                // Fallback handled below
            }
        }

        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.cols(), a.rows(), field.zero());
        org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
        IntStream.range(0, a.rows()).parallel().forEach(i -> {
            ctx.checkCancelled();
            for (int j = 0; j < a.cols(); j++) {
                storage.set(j, i, a.get(i, j));
            }
        });
        return new GenericMatrix<>(storage, this, field);
    }

    @Override
    @SuppressWarnings({"unchecked", "preview", "restricted"})
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (scalar instanceof Real && isReal(a)) {
            double s = ((Real) scalar).doubleValue();
            int rows = a.rows();
            int cols = a.cols();
            double[] data = toDoubleArray(a);
            double[] resData = new double[rows * cols];
            for (int i = 0; i < rows * cols; i++) {
                resData[i] = data[i] * s;
            }
            return (Matrix<E>) (Matrix<?>) RealDoubleMatrix.of(resData, rows, cols);
        }

        if (a.getClass().getName().endsWith("SIMDRealDoubleMatrix") && scalar instanceof Real) {
            try {
                return (Matrix<E>) a.getClass().getMethod("scale", double.class).invoke(a, ((Real) scalar).doubleValue());
            } catch (Exception e) {
                // Fallback handled below
            }
        }

        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), a.cols(), field.zero());
        org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
        IntStream.range(0, a.rows()).parallel().forEach(i -> {
            ctx.checkCancelled();
            for (int j = 0; j < a.cols(); j++) {
                storage.set(i, j, field.multiply(a.get(i, j), scalar));
            }
        });
        return new GenericMatrix<>(storage, this, field);
    }

    @Override
    @SuppressWarnings({"unchecked", "preview", "restricted"})
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (isReal(a) && b.dimension() == a.cols()) {
            int rows = a.rows();
            int cols = a.cols();
            double[] mat = toDoubleArray(a);
            double[] vec = new double[cols];
            for (int i = 0; i < cols; i++) vec[i] = ((Real) b.get(i)).doubleValue();
            
            double[] res = new double[rows];
            for (int i = 0; i < rows; i++) {
                double sum = 0;
                int offset = i * cols;
                for (int j = 0; j < cols; j++) {
                    sum += mat[offset + j] * vec[j];
                }
                res[i] = sum;
            }
            
            E[] resArray = (E[]) new Real[rows];
            for (int i = 0; i < rows; i++) resArray[i] = (E) (Object) Real.of(res[i]);
            return new GenericVector<>(new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(resArray), this, field);
        }

        if (a.getClass().getName().endsWith("SIMDRealDoubleMatrix")) {
            try {
                return (Vector<E>) a.getClass().getMethod("multiply", Vector.class).invoke(a, b);
            } catch (Exception e) {
                // Fallback handled below
            }
        }

        if (a.cols() != b.dimension()) {
             throw new IllegalArgumentException("Matrix columns must match vector dimension");
        }

        if (a.rows() < PARALLEL_THRESHOLD) {
            List<E> result = new ArrayList<>(a.rows());
            for (int i = 0; i < a.rows(); i++) {
                E sum = field.zero();
                for (int j = 0; j < a.cols(); j++) {
                    E product = field.multiply(a.get(i, j), b.get(j));
                    sum = field.add(sum, product);
                }
                result.add(sum);
            }
            return new GenericVector<>(
                    new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(result), this,
                    field);
        } else {
            org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
            return IntStream.range(0, a.rows())
                    .parallel()
                    .mapToObj(i -> {
                        if (i % 64 == 0) ctx.checkCancelled();
                        E sum = field.zero();
                        for (int j = 0; j < a.cols(); j++) {
                            E product = field.multiply(a.get(i, j), b.get(j));
                            sum = field.add(sum, product);
                        }
                        return sum;
                    })
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, field);
                            }));
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "preview", "restricted"})
    public Matrix<E> inverse(Matrix<E> a) {
        if (a.rows() != a.cols())
            throw new ArithmeticException("Must be square");
        int n = a.rows();

        if (isReal(a)) {
            double[] data = toDoubleArray(a);
            double[] aug = new double[n * 2 * n];
            // Initialize augmented matrix [A | I]
            for (int i = 0; i < n; i++) {
                System.arraycopy(data, i * n, aug, i * 2 * n, n);
                aug[i * 2 * n + n + i] = 1.0;
            }

            // Gauss-Jordan elimination with partial pivoting
            int rowSize = 2 * n;
            for (int k = 0; k < n; k++) {
                int pivot = k;
                double maxVal = Math.abs(aug[k * rowSize + k]);
                for (int i = k + 1; i < n; i++) {
                    double val = Math.abs(aug[i * rowSize + k]);
                    if (val > maxVal) {
                        maxVal = val;
                        pivot = i;
                    }
                }

                if (pivot != k) {
                    for (int j = k; j < rowSize; j++) {
                        double temp = aug[k * rowSize + j];
                        aug[k * rowSize + j] = aug[pivot * rowSize + j];
                        aug[pivot * rowSize + j] = temp;
                    }
                }

                double pivotVal = aug[k * rowSize + k];
                if (Math.abs(pivotVal) < 1e-18) throw new ArithmeticException("Matrix is singular");

                for (int j = k; j < rowSize; j++) aug[k * rowSize + j] /= pivotVal;

                for (int i = 0; i < n; i++) {
                    if (i != k) {
                        double factor = aug[i * rowSize + k];
                        int offsetI = i * rowSize;
                        int offsetK = k * rowSize;
                        for (int j = k; j < rowSize; j++) {
                            aug[offsetI + j] -= factor * aug[offsetK + j];
                        }
                    }
                }
            }

            double[] resData = new double[n * n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(aug, i * rowSize + n, resData, i * n, n);
            }
            return (Matrix<E>) (Matrix<?>) RealDoubleMatrix.of(resData, n, n);
        }

        List<List<E>> aug = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < n; j++)
                row.add(a.get(i, j));
            for (int j = 0; j < n; j++)
                row.add(i == j ? field.one() : field.zero());
            aug.add(row);
        }

        for (int col = 0; col < n; col++) {
            int pivotRow = col;
            if (field instanceof org.jscience.core.mathematics.sets.Reals) {
                double maxVal = Math
                        .abs(((org.jscience.core.mathematics.numbers.real.Real) aug.get(col).get(col)).doubleValue());
                for (int i = col + 1; i < n; i++) {
                    double val = Math
                            .abs(((org.jscience.core.mathematics.numbers.real.Real) aug.get(i).get(col)).doubleValue());
                    if (val > maxVal) {
                        maxVal = val;
                        pivotRow = i;
                    }
                }
            }
            if (pivotRow != col) {
                List<E> temp = aug.get(col);
                aug.set(col, aug.get(pivotRow));
                aug.set(pivotRow, temp);
            }

            E pivot = aug.get(col).get(col);
            if (pivot.equals(field.zero()))
                throw new ArithmeticException("Singular matrix");

            for (int j = 0; j < 2 * n; j++)
                aug.get(col).set(j, field.divide(aug.get(col).get(j), pivot));

            for (int i = 0; i < n; i++) {
                if (i != col) {
                    E factor = aug.get(i).get(col);
                    for (int j = 0; j < 2 * n; j++) {
                        E val = aug.get(i).get(j);
                        E sub = field.multiply(factor, aug.get(col).get(j));
                        aug.get(i).set(j, field.add(val, field.negate(sub)));
                    }
                }
            }
        }

        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(n, n, field.zero());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                storage.set(i, j, aug.get(i).get(n + j));
            }
        }
        return new GenericMatrix<>(storage, this, field);
    }

    @Override
    @SuppressWarnings({"unchecked", "preview", "restricted"})
    public E determinant(Matrix<E> a) {
        if (a.rows() != a.cols())
            throw new ArithmeticException("Must be square");
        int n = a.rows();
        if (n == 1)
            return a.get(0, 0);

        if (isReal(a)) {
            double[] mat = toDoubleArray(a);
            double det = 1.0;
            for (int col = 0; col < n; col++) {
                int pivotRow = col;
                double maxVal = Math.abs(mat[col * n + col]);
                for (int i = col + 1; i < n; i++) {
                    double val = Math.abs(mat[i * n + col]);
                    if (val > maxVal) {
                        maxVal = val;
                        pivotRow = i;
                    }
                }

                if (pivotRow != col) {
                    for (int j = col; j < n; j++) {
                        double temp = mat[col * n + j];
                        mat[col * n + j] = mat[pivotRow * n + j];
                        mat[pivotRow * n + j] = temp;
                    }
                    det = -det;
                }

                double pivot = mat[col * n + col];
                if (Math.abs(pivot) < 1e-18) return (E) (Object) Real.ZERO;

                det *= pivot;
                for (int i = col + 1; i < n; i++) {
                    double factor = mat[i * n + col] / pivot;
                    int offsetI = i * n;
                    int offsetCol = col * n;
                    for (int j = col + 1; j < n; j++) {
                        mat[offsetI + j] -= factor * mat[offsetCol + j];
                    }
                }
            }
            return (E) (Object) Real.of(det);
        }

        if (n == 2) {
            return field.add(field.multiply(a.get(0, 0), a.get(1, 1)),
                    field.negate(field.multiply(a.get(0, 1), a.get(1, 0))));
        }

        List<List<E>> mat = new ArrayList<>();
        // ... (remaining generic code)
        for (int i = 0; i < n; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < n; j++)
                row.add(a.get(i, j));
            mat.add(row);
        }

        E det = field.one();
        for (int col = 0; col < n; col++) {
            int pivotRow = col;
            for (int i = col + 1; i < n; i++) {
                if (!mat.get(i).get(col).equals(field.zero())) {
                    pivotRow = i;
                    break;
                }
            }
            if (pivotRow != col) {
                List<E> temp = mat.get(col);
                mat.set(col, mat.get(pivotRow));
                mat.set(pivotRow, temp);
                det = field.negate(det);
            }
            E pivot = mat.get(col).get(col);
            if (pivot.equals(field.zero()))
                return field.zero();
            det = field.multiply(det, pivot);
            for (int i = col + 1; i < n; i++) {
                E factor = field.divide(mat.get(i).get(col), pivot);
                for (int j = col; j < n; j++) {
                    E val = mat.get(i).get(j);
                    E sub = field.multiply(factor, mat.get(col).get(j));
                    mat.get(i).set(j, field.add(val, field.negate(sub)));
                }
            }
        }
        return det;
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (a.rows() != a.cols())
            throw new ArithmeticException("Must be square");
        int n = a.rows();

        if (isReal(a)) {
            double[] mat = toDoubleArray(a);
            double[] rhs = new double[n];
            for (int i = 0; i < n; i++) rhs[i] = ((Real) b.get(i)).doubleValue();

            // LU Decomposition (in-place) with Partial Pivoting
            int[] pivots = new int[n];
            for (int i = 0; i < n; i++) pivots[i] = i;

            for (int i = 0; i < n; i++) {
                int maxRow = i;
                double maxVal = Math.abs(mat[i * n + i]);
                for (int k = i + 1; k < n; k++) {
                    double val = Math.abs(mat[k * n + i]);
                    if (val > maxVal) {
                        maxVal = val;
                        maxRow = k;
                    }
                }

                // Swap rows
                if (maxRow != i) {
                    int tmpP = pivots[i]; pivots[i] = pivots[maxRow]; pivots[maxRow] = tmpP;
                    for (int k = 0; k < n; k++) {
                        double tmp = mat[i * n + k];
                        mat[i * n + k] = mat[maxRow * n + k];
                        mat[maxRow * n + k] = tmp;
                    }
                    double tmpR = rhs[i]; rhs[i] = rhs[maxRow]; rhs[maxRow] = tmpR;
                }

                double pivotVal = mat[i * n + i];
                if (Math.abs(pivotVal) < 1e-18) throw new ArithmeticException("Matrix is singular");

                for (int k = i + 1; k < n; k++) {
                    double factor = mat[k * n + i] / pivotVal;
                    mat[k * n + i] = factor;
                    for (int j = i + 1; j < n; j++) {
                        mat[k * n + j] -= factor * mat[i * n + j];
                    }
                    rhs[k] -= factor * rhs[i];
                }
            }

            // Back Substitution
            double[] res = new double[n];
            for (int i = n - 1; i >= 0; i--) {
                double sum = 0.0;
                for (int j = i + 1; j < n; j++) {
                    sum += mat[i * n + j] * res[j];
                }
                res[i] = (rhs[i] - sum) / mat[i * n + i];
            }

            @SuppressWarnings("unchecked")
            E[] resArray = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = 0; i < n; i++) resArray[i] = (E)(Object) Real.of(res[i]);
            return new GenericVector<>(new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(resArray), this, field);
        }

        List<List<E>> aug = new ArrayList<>();
        // ... (remaining generic code)
        for (int i = 0; i < n; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < n; j++)
                row.add(a.get(i, j));
            row.add(b.get(i));
            aug.add(row);
        }

        for (int col = 0; col < n; col++) {
            int pivotRow = col;
            if (field instanceof org.jscience.core.mathematics.sets.Reals) {
                // Partial Pivoting for Reals (Numerical Stability)
                double maxVal = Math
                        .abs(((org.jscience.core.mathematics.numbers.real.Real) aug.get(col).get(col)).doubleValue());
                for (int i = col + 1; i < n; i++) {
                    double val = Math
                            .abs(((org.jscience.core.mathematics.numbers.real.Real) aug.get(i).get(col)).doubleValue());
                    if (val > maxVal) {
                        maxVal = val;
                        pivotRow = i;
                    }
                }
            } else {
                // Fallback for generic fields: Swap if current is zero
                if (field.zero().equals(aug.get(col).get(col))) {
                    for (int i = col + 1; i < n; i++) {
                        if (!field.zero().equals(aug.get(i).get(col))) {
                            pivotRow = i;
                            break;
                        }
                    }
                }
            }
            if (pivotRow != col) {
                List<E> temp = aug.get(col);
                aug.set(col, aug.get(pivotRow));
                aug.set(pivotRow, temp);
            }
            E pivot = aug.get(col).get(col);
            if (pivot.equals(field.zero()))
                throw new ArithmeticException("Singular");
            E pivotInv = field.divide(field.one(), pivot);
            for (int j = col; j <= n; j++)
                aug.get(col).set(j, field.multiply(aug.get(col).get(j), pivotInv));
            for (int i = 0; i < n; i++) {
                if (i != col) {
                    E factor = aug.get(i).get(col);
                    for (int j = col; j <= n; j++) {
                        E val = aug.get(i).get(j);
                        E sub = field.multiply(factor, aug.get(col).get(j));
                        aug.get(i).set(j, field.add(val, field.negate(sub)));
                    }
                }
            }
        }

        List<E> res = new ArrayList<>();
        for (int i = 0; i < n; i++)
            res.add(aug.get(i).get(n));
        @SuppressWarnings("unchecked")
        E[] resArray = (E[]) res
                .toArray();
        return new GenericVector<>(
                new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(resArray), this, field);
    }

    @Override
    public int getPriority() {
        return 50; // Default priority
    }

    @Override
    @SuppressWarnings("unchecked")
    public QRResult<E> qr(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            QRDecomposition decomp = QRDecomposition.decompose((Matrix<Real>) a);
            return new QRResult<E>((Matrix<E>) decomp.getQ(), (Matrix<E>) decomp.getR());
        }
        return LinearAlgebraProvider.super.qr(a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SVDResult<E> svd(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            SVDDecomposition decomp = SVDDecomposition.decompose((Matrix<Real>) a);
            Real[] sigmas = decomp.getSingularValues();
            List<Real> sList = new ArrayList<>(sigmas.length);
            for (Real r : sigmas) sList.add(r);
            Vector<Real> S = org.jscience.core.mathematics.linearalgebra.vectors.DenseVector.of(sList, (Field<Real>) a.getScalarRing());
            return new SVDResult<E>((Matrix<E>) decomp.getU(), (Vector<E>) S, (Matrix<E>) decomp.getV());
        }
        return LinearAlgebraProvider.super.svd(a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EigenResult<E> eigen(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            EigenDecomposition decomp = EigenDecomposition.decompose((Matrix<Real>) a);
            Real[] values = decomp.getEigenvalues();
            List<Real> vList = new ArrayList<>(values.length);
            for (Real r : values) vList.add(r);
            Vector<Real> D = org.jscience.core.mathematics.linearalgebra.vectors.DenseVector.of(vList, (Field<Real>) a.getScalarRing());
            return new EigenResult<E>((Matrix<E>) decomp.getEigenvectors(), (Vector<E>) D);
        }
        return LinearAlgebraProvider.super.eigen(a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public LUResult<E> lu(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            LUDecomposition decomp = LUDecomposition.decompose((Matrix<Real>) a);
            int[] perm = decomp.getPermutation();
            List<Real> pList = new ArrayList<>(perm.length);
            for (int i : perm) pList.add(Real.of(i));
            Vector<Real> P = org.jscience.core.mathematics.linearalgebra.vectors.DenseVector.of(pList, (Field<Real>) a.getScalarRing());
            return new LUResult<E>((Matrix<E>) decomp.getL(), (Matrix<E>) decomp.getU(), (Vector<E>) P);
        }
        return LinearAlgebraProvider.super.lu(a);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            CholeskyDecomposition decomp = CholeskyDecomposition.decompose((Matrix<Real>) a);
            return new CholeskyResult<E>((Matrix<E>) decomp.getL());
        }
        return LinearAlgebraProvider.super.cholesky(a);
    }
}



