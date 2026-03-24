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

package org.episteme.core.mathematics.linearalgebra.providers;

import org.episteme.core.Episteme;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import com.google.auto.service.AutoService;


/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({LinearAlgebraProvider.class, Backend.class, CPUBackend.class})
public class CPUDenseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E>, CPUBackend {

    protected final Field<E> field;
    private static final int PARALLEL_THRESHOLD = 1000;

    @SuppressWarnings("unchecked")
    public CPUDenseLinearAlgebraProvider(Field<E> field) {
        this.field = (field != null) ? field : (Field<E>) org.episteme.core.mathematics.sets.Reals.getInstance();
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
        return "Episteme CPU (Dense)";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }


    @Override
    public void shutdown() {
        // No-op
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null;
    }

    @Override
    public String getId() {
        return "cpu-dense";
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public String getDescription() {
        return "Core CPU Dense Linear Algebra Provider";
    }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        return (this.field != null) ? this.field.equals(ring) : (ring instanceof org.episteme.core.mathematics.sets.Reals);
    }

    @Override
    public Object createBackend() {
        return this;
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
                    new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(data), this, field);
        } else {
            return Episteme.computeParallel(() -> java.util.stream.IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> field.add(a.get(i), b.get(i)))
                    .collect(java.util.stream.Collectors.collectingAndThen(
                            java.util.stream.Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, field);
                            })));
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
                    new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(result), this,
                    field);
        } else {
            
            return IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> {
                        if (i % 512 == 0) org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
                        E negB = field.negate(b.get(i));
                        return field.add(a.get(i), negB);
                    })
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
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
                    new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(arr), this, field);
        } else {
            
            return IntStream.range(0, vector.dimension())
                    .parallel()
                    .mapToObj(i -> {
                        if (i % 512 == 0) org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
                        return field.multiply(vector.get(i), scalar);
                    })
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, field);
                            }));
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "restricted"})
    public E dot(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }
        
        if (field instanceof org.episteme.core.mathematics.sets.Reals && 
            !org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
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
            
            return IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> {
                        if (i % 512 == 0) org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
                        return field.multiply(a.get(i), b.get(i));
                    })
                    .reduce(field.zero(), field::add);
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "restricted"})
    public E norm(Vector<E> a) {
        if (field instanceof org.episteme.core.mathematics.sets.Reals && 
            !org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            double sumSq = 0.0;
            int dim = a.dimension();
            for (int i = 0; i < dim; i++) {
                double val = ((Real) a.get(i)).doubleValue();
                sumSq += val * val;
            }
            return (E) (Object) Real.of(Math.sqrt(sumSq));
        }

        E dotProduct = dot(a, a);
        return sqrt(dotProduct, field);
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
                
                IntStream.range(0, rows).parallel().forEach(i -> {
                    org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
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
            
            IntStream.range(0, a.rows()).parallel().forEach(i -> {
                org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
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
                
                IntStream.range(0, rows).parallel().forEach(i -> {
                    org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
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
            
            IntStream.range(0, a.rows()).parallel().forEach(i -> {
                org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
                for (int j = 0; j < a.cols(); j++) {
                    E negB = field.negate(b.get(i, j));
                    storage.set(i, j, field.add(a.get(i, j), negB));
                }
            });
        }
        return new GenericMatrix<>(storage, this, field);
    }

    @Override
    @SuppressWarnings("restricted")
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
        org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
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

    @SuppressWarnings({"unchecked", "restricted"})
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
                        if (i % 64 == 0) org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
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
                org.episteme.core.util.PerformanceLogger.log("CPU:RealFastMultiply", 
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
                
                IntStream.range(0, a.rows()).parallel().forEach(i -> {
                    if (i % 64 == 0) org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
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
            org.episteme.core.util.PerformanceLogger.log("CPU:GenericMultiply", context, System.nanoTime() - start);
        }
        return new GenericMatrix<>(storage, provider, field);
    }

    private static boolean isReal(Matrix<?> m) {
        if (m instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
            // RealDoubleMatrix is ALWAYS double precision. 
            // If we are in EXACT mode, we MUST treat it as a generic matrix to avoid downcasting.
            return !org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision();
        }
        if (m.getScalarRing() instanceof org.episteme.core.mathematics.sets.Reals) {
            return !org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision();
        }
        return false;
    }

    private static double[] toDoubleArray(Matrix<?> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        if (m instanceof org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
            return ((org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix) m).toDoubleArray();
        }
        if (m.getClass().getName().endsWith("SIMDRealDoubleMatrix")) {
            try {
                return (double[]) m.getClass().getMethod("getInternalData").invoke(m);
            } catch (Exception e) {
                // Fallback handled below
            }
        }
        if (m instanceof GenericMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<?> storage = 
                ((GenericMatrix<?>) m).getStorage();
            if (storage instanceof org.episteme.core.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage) {
                return ((org.episteme.core.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage) storage).getData();
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
        
        IntStream.range(0, (M + BLOCK_SIZE - 1) / BLOCK_SIZE).parallel().forEach(bi -> {
            org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
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
    @SuppressWarnings({"unchecked", "restricted"})
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
        
        IntStream.range(0, a.rows()).parallel().forEach(i -> {
            org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
            for (int j = 0; j < a.cols(); j++) {
                storage.set(j, i, a.get(i, j));
            }
        });
        return new GenericMatrix<>(storage, this, field);
    }

    @Override
    @SuppressWarnings({"unchecked", "restricted"})
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
        
        IntStream.range(0, a.rows()).parallel().forEach(i -> {
            org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();
            for (int j = 0; j < a.cols(); j++) {
                storage.set(i, j, field.multiply(a.get(i, j), scalar));
            }
        });
        return new GenericMatrix<>(storage, this, field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (a.cols() != b.dimension()) {
            throw new IllegalArgumentException("Matrix columns must match vector dimension");
        }
        int rows = a.rows();
        int cols = a.cols();
        
        if (isReal(a)) {
            double[] mat = toDoubleArray(a);
            double[] vec = new double[cols];
            for (int i = 0; i < cols; i++) vec[i] = ((Real)b.get(i)).doubleValue();
            
            double[] res = new double[rows];
            for (int i = 0; i < rows; i++) {
                double sum = 0;
                int rowOffset = i * cols;
                for (int j = 0; j < cols; j++) {
                    sum += mat[rowOffset + j] * vec[j];
                }
                res[i] = sum;
            }
            return (Vector<E>) (Vector<?>) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(res);
        }
        
        java.util.List<E> result = new java.util.ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            E sum = field.zero();
            for (int j = 0; j < cols; j++) {
                sum = field.add(sum, field.multiply(a.get(i, j), b.get(j)));
            }
            result.add(sum);
        }
        return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(result, field);
    }

    @Override
    @SuppressWarnings({"unchecked", "restricted"})
    public Matrix<E> inverse(Matrix<E> a) {
        if (a.rows() != a.cols()) {
            return pseudoInverse(a);
        }
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
            if (field instanceof org.episteme.core.mathematics.sets.Reals) {
                double maxVal = Math
                        .abs(((org.episteme.core.mathematics.numbers.real.Real) aug.get(col).get(col)).doubleValue());
                for (int i = col + 1; i < n; i++) {
                    double val = Math
                            .abs(((org.episteme.core.mathematics.numbers.real.Real) aug.get(i).get(col)).doubleValue());
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
    @SuppressWarnings({"unchecked", "restricted"})
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
                if (Math.abs(pivot) < 1e-60) return (E) (Object) Real.ZERO;

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
    @SuppressWarnings("unchecked")
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (a.rows() != a.cols()) {
            // Rectangular: Least Squares via pseudo-inverse x = A+ * b
            return multiply(pseudoInverse(a), b);
        }
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

            E[] resArray = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = 0; i < n; i++) resArray[i] = (E)(Object) Real.of(res[i]);
            return new GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(resArray), this, field);
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
            if (field instanceof org.episteme.core.mathematics.sets.Reals) {
                // Partial Pivoting for Reals (Numerical Stability)
                double maxVal = Math
                        .abs(((org.episteme.core.mathematics.numbers.real.Real) aug.get(col).get(col)).doubleValue());
                for (int i = col + 1; i < n; i++) {
                    double val = Math
                            .abs(((org.episteme.core.mathematics.numbers.real.Real) aug.get(i).get(col)).doubleValue());
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
        E[] resArray = (E[]) res
                .toArray();
        return new GenericVector<>(
                new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(resArray), this, field);
    }

    @Override
    public int getPriority() {
        return 50; // Default priority
    }

    @Override
    @SuppressWarnings("unchecked")
    public QRResult<E> qr(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            return (QRResult<E>) JavaQR.decompose((Matrix<Real>) a);
        }
        return GenericQR.decompose(a, field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SVDResult<E> svd(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            return (SVDResult<E>) JavaSVD.decompose((Matrix<Real>) a);
        }
        return GenericSVD.decompose(a, field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EigenResult<E> eigen(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            return (EigenResult<E>) JavaEigen.decompose((Matrix<Real>) a);
        }
        return GenericEigen.decompose(a, field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public LUResult<E> lu(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            return (LUResult<E>) JavaLU.decompose((Matrix<Real>) a);
        }
        return GenericLU.decompose(a, field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        if (a.getScalarRing() instanceof Reals) {
            return (CholeskyResult<E>) JavaCholesky.decompose((Matrix<Real>) a);
        }
        return GenericCholesky.decompose(a, field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        if (field instanceof Reals) {
            return (Vector<E>) JavaLU.solve((LUResult<Real>) lu, (Vector<Real>) b);
        }
        return GenericLU.solve(lu, b, field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        if (field instanceof Reals) {
            return (Vector<E>) JavaQR.solve((QRResult<Real>) qr, (Vector<Real>) b);
        }
        return GenericQR.solve(qr, b, field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        if (field instanceof Reals) {
            return (Vector<E>) JavaCholesky.solve((CholeskyResult<Real>) cholesky, (Vector<Real>) b);
        }
        return GenericCholesky.solve(cholesky, b, field);
    }

    private Matrix<E> pseudoInverse(Matrix<E> a) {
        SVDResult<E> svd = svd(a);
        // A+ = V * S+ * UT (using economy dimensions)
        int m = a.rows();
        int n = a.cols();
        int k = svd.S().dimension();
        
        // Create S+ (pseudo-inverse of diagonal S) as k x k
        DenseMatrixStorage<E> sPlusStorage = new DenseMatrixStorage<>(k, k, field.zero());
        for (int i = 0; i < k; i++) {
            E sVal = svd.S().get(i);
            if (absValue(sVal) > 1e-12) {
                sPlusStorage.set(i, i, field.divide(field.one(), sVal));
            }
        }
        Matrix<E> sPlus = new GenericMatrix<>(sPlusStorage, this, field);
        
        // V is n x n, we need first k columns to match sPlus (k x k)
        Matrix<E> vEco = svd.V().getSubMatrix(0, n, 0, k);
        
        // U is m x m, we need first k columns (m x k) so UT is (k x m)
        Matrix<E> uEco = svd.U().getSubMatrix(0, m, 0, k);
        
        return multiply(multiply(vEco, sPlus), transpose(uEco));
    }

    // --- Generic Field Decompositions ---

    private static class GenericLU {
        public static <E> LUResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            int n = matrix.rows();
            if (n != matrix.cols()) throw new IllegalArgumentException("Matrix must be square");

            @SuppressWarnings("unchecked")
            E[][] data = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = matrix.get(i, j);

            int[] perm = new int[n];
            for (int i = 0; i < n; i++) perm[i] = i;

            for (int k = 0; k < n; k++) {
                int maxRow = k;
                double maxVal = absValue(data[k][k]);
                for (int i = k + 1; i < n; i++) {
                    double val = absValue(data[i][k]);
                    if (val > maxVal) {
                        maxVal = val;
                        maxRow = i;
                    }
                }

                if (maxRow != k) {
                    E[] temp = data[k];
                    data[k] = data[maxRow];
                    data[maxRow] = temp;
                    int tempPerm = perm[k];
                    perm[k] = perm[maxRow];
                    perm[maxRow] = tempPerm;
                }

                E diagonal = data[k][k];
                if (diagonal.equals(field.zero())) continue;

                for (int i = k + 1; i < n; i++) {
                    E factor = field.divide(data[i][k], diagonal);
                    data[i][k] = factor;
                    for (int j = k + 1; j < n; j++) {
                        data[i][j] = field.add(data[i][j], field.negate(field.multiply(factor, data[k][j])));
                    }
                }
            }

            @SuppressWarnings("unchecked")
            E[][] lData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            @SuppressWarnings("unchecked")
            E[][] uData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i > j) { lData[i][j] = data[i][j]; uData[i][j] = field.zero(); }
                    else if (i == j) { lData[i][j] = field.one(); uData[i][j] = data[i][j]; }
                    else { lData[i][j] = field.zero(); uData[i][j] = data[i][j]; }
                }
            }

            double[] pDouble = new double[n];
            for (int i = 0; i < n; i++) pDouble[i] = perm[i];

            @SuppressWarnings("unchecked")
            Vector<E> pVec = (Vector<E>) (Vector<?>) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(pDouble);
            return new LUResult<E>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<E>(lData, field),
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<E>(uData, field),
                pVec
            );
        }

        public static <E> Vector<E> solve(LUResult<E> lu, Vector<E> b, Field<E> field) {
            int n = lu.L().rows();
            @SuppressWarnings("unchecked")
            E[] x = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            @SuppressWarnings("unchecked")
            E[] pb = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);

            for (int i = 0; i < n; i++) {
                Object pVal = lu.P().get(i);
                int pIdx;
                if (pVal instanceof org.episteme.core.mathematics.numbers.real.Real) pIdx = (int) ((org.episteme.core.mathematics.numbers.real.Real) pVal).doubleValue();
                else if (pVal instanceof Number) pIdx = ((Number) pVal).intValue();
                else pIdx = i;
                pb[i] = b.get(pIdx);
            }

            @SuppressWarnings("unchecked")
            E[] y = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = 0; i < n; i++) {
                E sum = field.zero();
                for (int j = 0; j < i; j++) {
                    sum = field.add(sum, field.multiply(lu.L().get(i, j), y[j]));
                }
                y[i] = field.add(pb[i], field.negate(sum));
            }

            for (int i = n - 1; i >= 0; i--) {
                E sum = field.zero();
                for (int j = i + 1; j < n; j++) {
                    sum = field.add(sum, field.multiply(lu.U().get(i, j), x[j]));
                }
                y[i] = field.add(y[i], field.negate(sum));
                x[i] = field.divide(y[i], lu.U().get(i, i));
            }

            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), field);
        }
    }

    private static class GenericQR {
        public static <E> QRResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            int m = matrix.rows();
            int n = matrix.cols();

            @SuppressWarnings("unchecked")
            E[][] Q = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), m, n);
            @SuppressWarnings("unchecked")
            E[][] R = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            
            for (int j = 0; j < n; j++) {
                @SuppressWarnings("unchecked")
                E[] v = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), m);
                for (int i = 0; i < m; i++) v[i] = matrix.get(i, j);
                
                for (int i = 0; i < j; i++) {
                    E rij = dot(Q, v, i, m, field);
                    R[i][j] = rij;
                    for (int k = 0; k < m; k++) {
                        v[k] = field.add(v[k], field.negate(field.multiply(rij, Q[k][i])));
                    }
                }
                
                E rjj = norm(v, field);
                R[j][j] = rjj;
                if (!rjj.equals(field.zero())) {
                    for (int k = 0; k < m; k++) Q[k][j] = field.divide(v[k], rjj);
                } else {
                    for (int k = 0; k < m; k++) Q[k][j] = field.zero();
                }
            }

            return new QRResult<>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(Q, field),
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(R, field)
            );
        }

        private static <E> E dot(E[][] Q, E[] v, int col, int m, Field<E> field) {
            E sum = field.zero();
            for (int i = 0; i < m; i++) {
                sum = field.add(sum, field.multiply(conjugate(Q[i][col], field), v[i]));
            }
            return sum;
        }

        private static <E> E norm(E[] v, Field<E> field) {
            E sum = field.zero();
            for (E val : v) {
                sum = field.add(sum, field.multiply(conjugate(val, field), val));
            }
            return sqrt(sum, field);
        }

        public static <E> Vector<E> solve(QRResult<E> qr, Vector<E> b, Field<E> field) {
            int m = qr.Q().rows();
            int n = qr.R().cols();
            
            @SuppressWarnings("unchecked")
            E[] qtb = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = 0; i < n; i++) {
                E sum = field.zero();
                for (int j = 0; j < m; j++) {
                    sum = field.add(sum, field.multiply(conjugate(qr.Q().get(j, i), field), b.get(j)));
                }
                qtb[i] = sum;
            }
            
            @SuppressWarnings("unchecked")
            E[] x = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = n - 1; i >= 0; i--) {
                E sum = qtb[i];
                for (int j = i + 1; j < n; j++) {
                    sum = field.add(sum, field.negate(field.multiply(qr.R().get(i, j), x[j])));
                }
                x[i] = field.divide(sum, qr.R().get(i, i));
            }
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), field);
        }
    }

    private static class GenericCholesky {
        public static <E> CholeskyResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            int n = matrix.rows();
            @SuppressWarnings("unchecked")
            E[][] L = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) L[i][j] = field.zero();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= i; j++) {
                    E sum = field.zero();
                    if (j == i) {
                        for (int k = 0; k < j; k++) {
                            sum = field.add(sum, field.multiply(L[j][k], conjugate(L[j][k], field)));
                        }
                        L[j][j] = sqrt(field.add(matrix.get(j, j), field.negate(sum)), field);
                    } else {
                        for (int k = 0; k < j; k++) {
                            sum = field.add(sum, field.multiply(L[i][k], conjugate(L[j][k], field)));
                        }
                        L[i][j] = field.divide(field.add(matrix.get(i, j), field.negate(sum)), L[j][j]);
                    }
                }
            }
            return new CholeskyResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(L, field));
        }

        public static <E> Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b, Field<E> field) {
            int n = cholesky.L().rows();
            @SuppressWarnings("unchecked")
            E[] y = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            @SuppressWarnings("unchecked")
            E[] x = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);

            for (int i = 0; i < n; i++) {
                E sum = field.zero();
                for (int j = 0; j < i; j++) sum = field.add(sum, field.multiply(cholesky.L().get(i, j), y[j]));
                y[i] = field.divide(field.add(b.get(i), field.negate(sum)), cholesky.L().get(i, i));
            }
            for (int i = n - 1; i >= 0; i--) {
                E sum = field.zero();
                for (int j = i + 1; j < n; j++) sum = field.add(sum, field.multiply(conjugate(cholesky.L().get(j, i), field), x[j]));
                x[i] = field.divide(field.add(y[i], field.negate(sum)), conjugate(cholesky.L().get(i, i), field));
            }
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), field);
        }
    }

    // --- Helper math methods for generic fields ---

    private static double absValue(Object element) {
        if (element instanceof Real) return ((Real) element).doubleValue();
        if (element instanceof org.episteme.core.mathematics.numbers.complex.Complex) 
            return ((org.episteme.core.mathematics.numbers.complex.Complex) element).abs().doubleValue();
        if (element instanceof Number) return ((Number) element).doubleValue();
        return 0.0;
    }

    @SuppressWarnings("unchecked")
    private static <E> E conjugate(E element, Field<E> field) {
        if (element instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
            return (E) ((org.episteme.core.mathematics.numbers.complex.Complex) element).conjugate();
        }
        return element;
    }

    @SuppressWarnings("unchecked")
    private static <E> E sqrt(E element, Field<E> field) {
        if (element instanceof org.episteme.core.mathematics.numbers.real.Real) {
            return (E) ((org.episteme.core.mathematics.numbers.real.Real) element).sqrt();
        }
        if (element instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
            return (E) ((org.episteme.core.mathematics.numbers.complex.Complex) element).sqrt();
        }
        try {
            java.lang.reflect.Method m = element.getClass().getMethod("sqrt");
            return (E) m.invoke(element);
        } catch (Exception e) {}
        throw new UnsupportedOperationException("sqrt not supported for type: " + element.getClass().getName());
    }

    private static class GenericEigen {
        public static <E> EigenResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            int n = matrix.rows();
            if (n != matrix.cols()) throw new IllegalArgumentException("Matrix must be square");

            @SuppressWarnings("unchecked")
            E[][] A = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) A[i][j] = matrix.get(i, j);

            @SuppressWarnings("unchecked")
            E[][] V = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) V[i][j] = (i == j) ? field.one() : field.zero();

            int maxSweeps = 50;
            double eps = 1e-15;

            for (int sweep = 0; sweep < maxSweeps; sweep++) {
                double offDiag = 0;
                int p = 0, q = 0;
                double maxOff = -1.0;

                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        double val = absValue(A[i][j]);
                        offDiag += val;
                        if (val > maxOff) {
                            maxOff = val;
                            p = i;
                            q = j;
                        }
                    }
                }

                if (offDiag < eps) break;

                // Jacobi rotation for A[p][p], A[p][q], A[q][q]
                // For generic fields, we'll use a simplified version for now
                // approximating rotation if possible, or using a basic numeric approach
                // since exact trig for generic FieldElement isn't always available.
                // However, Reals and Complex have sqrt/atan2.
                
                E app = A[p][p];
                E aqq = A[q][q];
                E apq = A[p][q];

                // Simplified Jacobi for generic: tan(2theta) = 2*apq / (aqq - app)
                // For Reals it's standard. For others, we might need more abstractions.
                // But we can use the bridging methods.
                
                // theta calculation
                E diff = field.add(aqq, field.negate(app));
                E twoApq = field.add(apq, apq);
                
                // This part is tricky generically. I'll rely on the Number bridge if available
                // or use a simplified iterative step.
                
                double tau = absValue(diff) < 1e-18 ? 0.0 : absValue(twoApq) / absValue(diff);
                double t = tau / (1.0 + Math.sqrt(1.0 + tau * tau));
                double c = 1.0 / Math.sqrt(1.0 + t * t);
                double s = t * c;

                // For Complex/Real, we can use these c, s values directly if we convert back
                @SuppressWarnings("unchecked")
                E cE = (E) Real.of(c);
                @SuppressWarnings("unchecked")
                E sE = (E) Real.of(s);
                
                // Apply rotation to A
                for (int i = 0; i < n; i++) {
                    E ap = A[p][i];
                    E aq = A[q][i];
                    A[p][i] = field.add(field.multiply(cE, ap), field.negate(field.multiply(sE, aq)));
                    A[q][i] = field.add(field.multiply(sE, ap), field.multiply(cE, aq));
                }
                for (int i = 0; i < n; i++) {
                    E ap = A[i][p];
                    E aq = A[i][q];
                    A[i][p] = field.add(field.multiply(cE, ap), field.negate(field.multiply(sE, aq)));
                    A[i][q] = field.add(field.multiply(sE, ap), field.multiply(cE, aq));
                }
                // Accumulate V
                for (int i = 0; i < n; i++) {
                    E vp = V[i][p];
                    E vq = V[i][q];
                    V[i][p] = field.add(field.multiply(cE, vp), field.negate(field.multiply(sE, vq)));
                    V[i][q] = field.add(field.multiply(sE, vp), field.multiply(cE, vq));
                }
            }

            @SuppressWarnings("unchecked")
            E[] eigenvalues = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = 0; i < n; i++) eigenvalues[i] = A[i][i];

            return new EigenResult<>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(V, field),
                org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(eigenvalues), field)
            );
        }
    }

    private static class GenericSVD {
        public static <E> SVDResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            // SVD via AT*A for generic field (simplified)
            int m = matrix.rows();
            int n = matrix.cols();
            
            Matrix<E> selfAdj = matrix.multiply(matrix.transpose()); // m x m
            EigenResult<E> eigen = GenericEigen.decompose(selfAdj, field);
            
            @SuppressWarnings("unchecked")
            E[] sValues = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), Math.min(m, n));
            for (int i = 0; i < sValues.length; i++) {
                sValues[i] = (E) eigen.D().get(i); // Eigen S is already E (or castable)
            }
            
            // U = eigenvectors of A*A*
            Matrix<E> U = eigen.V();
            
            // V = eigenvectors of A**A (if needed) or derive from U, S, A
            // For simplicity in generic, we'll do A**A for V too
            Matrix<E> selfAdjV = matrix.transpose().multiply(matrix); // n x n
            EigenResult<E> eigenV = GenericEigen.decompose(selfAdjV, field);
            Matrix<E> V = eigenV.V();

            return new SVDResult<E>(U, org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(sValues), field), V);
        }
    }

    private static class JavaLU {
        public static LUResult<Real> decompose(Matrix<Real> matrix) {
            int n = matrix.rows();
            if (n != matrix.cols()) throw new IllegalArgumentException("Matrix must be square");

            Real[][] data = new Real[n][n];
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = matrix.get(i, j);

            int[] perm = new int[n];
            for (int i = 0; i < n; i++) perm[i] = i;

            for (int k = 0; k < n; k++) {
                int maxRow = k;
                Real maxVal = data[k][k].abs();
                for (int i = k + 1; i < n; i++) {
                    Real val = data[i][k].abs();
                    if (val.compareTo(maxVal) > 0) {
                        maxVal = val;
                        maxRow = i;
                    }
                }

                if (maxRow != k) {
                    Real[] temp = data[k];
                    data[k] = data[maxRow];
                    data[maxRow] = temp;
                    int tempPerm = perm[k];
                    perm[k] = perm[maxRow];
                    perm[maxRow] = tempPerm;
                }

                for (int i = k + 1; i < n; i++) {
                    if (!data[k][k].isZero()) {
                        Real factor = data[i][k].divide(data[k][k]);
                        data[i][k] = factor;
                        for (int j = k + 1; j < n; j++) {
                            data[i][j] = data[i][j].subtract(factor.multiply(data[k][j]));
                        }
                    }
                }
            }

            Real[][] lData = new Real[n][n];
            Real[][] uData = new Real[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i > j) { lData[i][j] = data[i][j]; uData[i][j] = Real.ZERO; }
                    else if (i == j) { lData[i][j] = Real.ONE; uData[i][j] = data[i][j]; }
                    else { lData[i][j] = Real.ZERO; uData[i][j] = data[i][j]; }
                }
            }

            double[] pDouble = new double[n];
            for(int i=0; i<n; i++) pDouble[i] = perm[i];

            return new LUResult<>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(lData, Reals.getInstance()),
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(uData, Reals.getInstance()),
                org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(pDouble)
            );
        }

        public static Vector<Real> solve(LUResult<Real> lu, Vector<Real> b) {
            int n = lu.L().rows();
            Real[] x = new Real[n];
            Real[] pb = new Real[n];

            for (int i = 0; i < n; i++) {
                Object pVal = lu.P().get(i);
                int pIdx;
                if (pVal instanceof org.episteme.core.mathematics.numbers.real.Real) pIdx = (int) ((org.episteme.core.mathematics.numbers.real.Real) pVal).doubleValue();
                else if (pVal instanceof Number) pIdx = ((Number) pVal).intValue();
                else pIdx = i;
                pb[i] = b.get(pIdx);
            }

            Real[] y = new Real[n];
            for (int i = 0; i < n; i++) {
                Real sum = Real.ZERO;
                for (int j = 0; j < i; j++) {
                    sum = sum.add(lu.L().get(i, j).multiply(y[j]));
                }
                y[i] = pb[i].subtract(sum);
            }

            for (int i = n - 1; i >= 0; i--) {
                Real sum = Real.ZERO;
                for (int j = i + 1; j < n; j++) {
                    sum = sum.add(lu.U().get(i, j).multiply(x[j]));
                }
                x[i] = y[i].subtract(sum).divide(lu.U().get(i, i));
            }

            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), Reals.getInstance());
        }
    }

    private static class JavaQR {
        public static QRResult<Real> decompose(Matrix<Real> matrix) {
            int m = matrix.rows();
            int n = matrix.cols();

            Real[][] A = new Real[m][n];
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) A[i][j] = matrix.get(i, j);

            Real[][] Q = new Real[m][m];
            for (int i = 0; i < m; i++) for (int j = 0; j < m; j++) Q[i][j] = (i == j) ? Real.ONE : Real.ZERO;

            for (int k = 0; k < Math.min(m, n); k++) {
                Real norm = Real.ZERO;
                for (int i = k; i < m; i++) norm = norm.add(A[i][k].multiply(A[i][k]));
                norm = norm.sqrt();
                if (norm.isZero()) {
                    continue;
                }

                Real a1 = A[k][k];
                Real alpha = (a1.compareTo(Real.ZERO) >= 0) ? norm.negate() : norm;
                
                Real[] v = new Real[m - k];
                v[0] = A[k][k].subtract(alpha);
                for (int i = 1; i < v.length; i++) v[i] = A[k + i][k];

                Real vNorm = Real.ZERO;
                for (Real val : v) vNorm = vNorm.add(val.multiply(val));
                vNorm = vNorm.sqrt();
                if (vNorm.isZero()) continue;
                for (int i = 0; i < v.length; i++) v[i] = v[i].divide(vNorm);

                for (int j = k; j < n; j++) {
                    Real vDotA = Real.ZERO;
                    for (int i = 0; i < v.length; i++) vDotA = vDotA.add(v[i].multiply(A[k + i][j]));
                    Real twoVDotA = vDotA.add(vDotA);
                    for (int i = 0; i < v.length; i++) A[k + i][j] = A[k + i][j].subtract(twoVDotA.multiply(v[i]));
                }

                for (int i = 0; i < m; i++) {
                    Real qDotV = Real.ZERO;
                    for (int j = 0; j < v.length; j++) qDotV = qDotV.add(Q[i][k + j].multiply(v[j]));
                    Real twoQDotV = qDotV.add(qDotV);
                    for (int j = 0; j < v.length; j++) Q[i][k + j] = Q[i][k + j].subtract(twoQDotV.multiply(v[j]));
                }
            }

            for (int i = 0; i < m; i++) for (int j = 0; j < Math.min(i, n); j++) A[i][j] = Real.ZERO;
            return new QRResult<>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(Q, Reals.getInstance()), 
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(A, Reals.getInstance())
            );
        }

        public static Vector<Real> solve(QRResult<Real> qr, Vector<Real> b) {
            int m = qr.Q().rows();
            int n = qr.R().cols();
            Real[] qtb = new Real[m];
            for (int i = 0; i < m; i++) {
                Real sum = Real.ZERO;
                for (int j = 0; j < m; j++) sum = sum.add(qr.Q().get(j, i).multiply(b.get(j)));
                qtb[i] = sum;
            }
            Real[] x = new Real[n];
            for (int i = n - 1; i >= 0; i--) {
                Real sum = qtb[i];
                for (int j = i + 1; j < n; j++) sum = sum.subtract(qr.R().get(i, j).multiply(x[j]));
                Real rii = qr.R().get(i, i);
                if (rii.abs().compareTo(Real.of(1e-15)) < 0) x[i] = Real.ZERO;
                else x[i] = sum.divide(rii);
            }
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), Reals.getInstance());
        }
    }

    private static class JavaCholesky {
        public static CholeskyResult<Real> decompose(Matrix<Real> matrix) {
            int n = matrix.rows();
            Real[][] L = new Real[n][n];
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) L[i][j] = Real.ZERO;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= i; j++) {
                    Real sum = Real.ZERO;
                    if (j == i) {
                        for (int k = 0; k < j; k++) sum = sum.add(L[j][k].multiply(L[j][k]));
                        Real diag = matrix.get(j, j).subtract(sum);
                        if (diag.compareTo(Real.ZERO) <= 0) throw new IllegalArgumentException("Not positive definite");
                        L[j][j] = diag.sqrt();
                    } else {
                        for (int k = 0; k < j; k++) sum = sum.add(L[i][k].multiply(L[j][k]));
                        L[i][j] = matrix.get(i, j).subtract(sum).divide(L[j][j]);
                    }
                }
            }
            return new CholeskyResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(L, Reals.getInstance()));
        }

        public static Vector<Real> solve(CholeskyResult<Real> cholesky, Vector<Real> b) {
            int n = cholesky.L().rows();
            Real[] y = new Real[n];
            Real[] x = new Real[n];
            for (int i = 0; i < n; i++) {
                Real sum = Real.ZERO;
                for (int j = 0; j < i; j++) sum = sum.add(cholesky.L().get(i, j).multiply(y[j]));
                y[i] = b.get(i).subtract(sum).divide(cholesky.L().get(i, i));
            }
            for (int i = n - 1; i >= 0; i--) {
                Real sum = Real.ZERO;
                for (int j = i + 1; j < n; j++) sum = sum.add(cholesky.L().get(j, i).multiply(x[j]));
                x[i] = y[i].subtract(sum).divide(cholesky.L().get(i, i));
            }
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), Reals.getInstance());
        }
    }

    private static class JavaSVD {
        public static SVDResult<Real> decompose(Matrix<Real> matrix) {
            int m = matrix.rows();
            int n = matrix.cols();
            double[][] A = new double[m][n];
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) A[i][j] = matrix.get(i, j).doubleValue();

            int nu = Math.min(m, n);
            double[] s = new double[Math.min(m + 1, n)];
            double[][] U = new double[m][nu];
            double[][] V = new double[n][n];
            double[] e = new double[n];
            double[] work = new double[m];

            int nct = Math.min(m - 1, n);
            int nrt = Math.max(0, Math.min(n - 2, m));
            for (int k = 0; k < Math.max(nct, nrt); k++) {
                if (k < nct) {
                    s[k] = 0;
                    for (int i = k; i < m; i++) s[k] = Math.hypot(s[k], A[i][k]);
                    if (s[k] != 0.0) {
                        if (A[k][k] < 0.0) s[k] = -s[k];
                        for (int i = k; i < m; i++) A[i][k] /= s[k];
                        A[k][k] += 1.0;
                    }
                    s[k] = -s[k];
                }
                for (int j = k + 1; j < n; j++) {
                    if ((k < nct) && (s[k] != 0.0)) {
                        double t = 0;
                        for (int i = k; i < m; i++) t += A[i][k] * A[i][j];
                        t = -t / A[k][k];
                        for (int i = k; i < m; i++) A[i][j] += t * A[i][k];
                    }
                    e[j] = A[k][j];
                }
                if (k < nct) {
                    for (int i = k; i < m; i++) U[i][k] = A[i][k];
                }
                if (k < nrt) {
                    e[k] = 0;
                    for (int i = k + 1; i < n; i++) e[k] = Math.hypot(e[k], e[i]);
                    if (e[k] != 0.0) {
                        if (e[k + 1] < 0.0) e[k] = -e[k];
                        for (int i = k + 1; i < n; i++) e[i] /= e[k];
                        e[k + 1] += 1.0;
                    }
                    e[k] = -e[k];
                    if ((k + 1 < m) && (e[k] != 0.0)) {
                        for (int i = k + 1; i < m; i++) work[i] = 0.0;
                        for (int j = k + 1; j < n; j++) {
                            for (int i = k + 1; i < m; i++) work[i] += e[j] * A[i][j];
                        }
                        for (int j = k + 1; j < n; j++) {
                            double t = -e[j] / e[k + 1];
                            for (int i = k + 1; i < m; i++) A[i][j] += t * work[i];
                        }
                    }
                    for (int i = k + 1; i < n; i++) V[i][k] = e[i];
                }
            }

            int p = Math.min(n, m + 1);
            if (nct < n) s[nct] = A[nct][nct];
            if (m < p) s[p - 1] = 0.0;
            if (nrt + 1 < p) e[nrt] = A[nrt][p - 1];
            e[p - 1] = 0.0;

            for (int j = nct; j < nu; j++) {
                for (int i = 0; i < m; i++) U[i][j] = 0.0;
                U[j][j] = 1.0;
            }
            for (int k = nct - 1; k >= 0; k--) {
                if (s[k] != 0.0) {
                    for (int j = k + 1; j < nu; j++) {
                        double t = 0;
                        for (int i = k; i < m; i++) t += U[i][k] * U[i][j];
                        t = -t / U[k][k];
                        for (int i = k; i < m; i++) U[i][j] += t * U[i][k];
                    }
                    for (int i = k; i < m; i++) U[i][k] = -U[i][k];
                    U[k][k] = 1.0 + U[k][k];
                    for (int i = 0; i < k - 1; i++) U[i][k] = 0.0;
                } else {
                    for (int i = 0; i < m; i++) U[i][k] = 0.0;
                    U[k][k] = 1.0;
                }
            }

            for (int k = n - 1; k >= 0; k--) {
                if ((k < nrt) && (e[k] != 0.0)) {
                    for (int j = k + 1; j < nu; j++) {
                        double t = 0;
                        for (int i = k + 1; i < n; i++) t += V[i][k] * V[i][j];
                        t = -t / V[k + 1][k];
                        for (int i = k + 1; i < n; i++) V[i][j] += t * V[i][k];
                    }
                }
                for (int i = 0; i < n; i++) V[i][k] = 0.0;
                V[k][k] = 1.0;
            }

            int pp = p - 1;
            int iter = 0;
            double eps = Math.pow(2.0, -52.0);
            double tiny = Math.pow(2.0, -966.0);
            while (p > 0) {
                int k, kase;
                if (iter >= 500) break;
                for (k = p - 2; k >= -1; k--) {
                    if (k == -1) break;
                    if (Math.abs(e[k]) <= tiny + eps * (Math.abs(s[k]) + Math.abs(s[k + 1]))) {
                        e[k] = 0.0;
                        break;
                    }
                }
                if (k == p - 2) {
                    kase = 4;
                } else {
                    int ks;
                    for (ks = p - 1; ks >= k; ks--) {
                        if (ks == k) break;
                        double t = (ks != p ? Math.abs(e[ks]) : 0.) + (ks != k + 1 ? Math.abs(e[ks - 1]) : 0.);
                        if (Math.abs(s[ks]) <= tiny + eps * t) {
                            s[ks] = 0.0;
                            break;
                        }
                    }
                    if (ks == k) kase = 3;
                    else if (ks == p - 1) kase = 1;
                    else { kase = 2; k = ks; }
                }
                k++;

                switch (kase) {
                    case 1: {
                        double f = e[p - 2];
                        e[p - 2] = 0.0;
                        for (int j = p - 2; j >= k; j--) {
                            double t = Math.hypot(s[j], f);
                            double cs = s[j] / t;
                            double sn = f / t;
                            s[j] = t;
                            if (j != k) { f = -sn * e[j - 1]; e[j - 1] = cs * e[j - 1]; }
                            for (int i = 0; i < n; i++) {
                                t = cs * V[i][j] + sn * V[i][p - 1];
                                V[i][p - 1] = -sn * V[i][j] + cs * V[i][p - 1];
                                V[i][j] = t;
                            }
                        }
                    }
                    break;
                    case 2: {
                        double f = e[k - 1];
                        e[k - 1] = 0.0;
                        for (int j = k; j < p; j++) {
                            double t = Math.hypot(s[j], f);
                            double cs = s[j] / t;
                            double sn = f / t;
                            s[j] = t;
                            f = -sn * e[j];
                            e[j] = cs * e[j];
                            for (int i = 0; i < m; i++) {
                                t = cs * U[i][j] + sn * U[i][k - 1];
                                U[i][k - 1] = -sn * U[i][j] + cs * U[i][k - 1];
                                U[i][j] = t;
                            }
                        }
                    }
                    break;
                    case 3: {
                        double scale = Math.max(Math.max(Math.max(Math.max(Math.abs(s[p - 1]), Math.abs(s[p - 2])), Math.abs(e[p - 2])), Math.abs(s[k])), Math.abs(e[k]));
                        double sp = s[p - 1] / scale;
                        double spm1 = s[p - 2] / scale;
                        double epm1 = e[p - 2] / scale;
                        double sk = s[k] / scale;
                        double ek = e[k] / scale;
                        double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
                        double c = (sp * epm1) * (sp * epm1);
                        double shift = 0.0;
                        if ((b != 0.0) || (c != 0.0)) {
                            shift = Math.sqrt(b * b + c);
                            if (b < 0.0) shift = -shift;
                            shift = c / (b + shift);
                        }
                        double f = (sk + sp) * (sk - sp) + shift;
                        double g = sk * ek;
                        for (int j = k; j < p - 1; j++) {
                            double t = Math.hypot(f, g);
                            double cs = f / t;
                            double sn = g / t;
                            if (j != k) e[j - 1] = t;
                            f = cs * s[j] + sn * e[j];
                            e[j] = cs * e[j] - sn * s[j];
                            g = sn * s[j + 1];
                            s[j + 1] = cs * s[j + 1];
                            for (int i = 0; i < n; i++) {
                                t = cs * V[i][j] + sn * V[i][j + 1];
                                V[i][j + 1] = -sn * V[i][j] + cs * V[i][j + 1];
                                V[i][j] = t;
                            }
                            t = Math.hypot(f, g);
                            cs = f / t;
                            sn = g / t;
                            s[j] = t;
                            f = cs * e[j] + sn * s[j + 1];
                            s[j + 1] = -sn * e[j] + cs * s[j + 1];
                            g = sn * e[j + 1];
                            e[j + 1] = cs * e[j + 1];
                            for (int i = 0; i < m; i++) {
                                t = cs * U[i][j] + sn * U[i][j + 1];
                                U[i][j + 1] = -sn * U[i][j] + cs * U[i][j + 1];
                                U[i][j] = t;
                            }
                        }
                        e[p - 2] = f;
                        iter = iter + 1;
                    }
                    break;
                    case 4: {
                        if (s[k] <= 0.0) {
                            s[k] = (s[k] < 0.0 ? -s[k] : 0.0);
                            for (int i = 0; i <= pp; i++) V[i][k] = -V[i][k];
                        }
                        while (k < pp) {
                            if (s[k] >= s[k + 1]) break;
                            double t = s[k]; s[k] = s[k + 1]; s[k + 1] = t;
                            for (int i = 0; i < n; i++) { t = V[i][k + 1]; V[i][k + 1] = V[i][k]; V[i][k] = t; }
                            for (int i = 0; i < m; i++) { t = U[i][k + 1]; U[i][k + 1] = U[i][k]; U[i][k] = t; }
                            k++;
                        }
                        iter = 0;
                        p--;
                    }
                    break;
                }
            }

            Real[] sigmaReal = new Real[nu];
            for (int i = 0; i < nu; i++) sigmaReal[i] = Real.of(s[i]);
            Real[][] UReal = new Real[m][nu];
            for (int i = 0; i < m; i++) for (int j = 0; j < nu; j++) UReal[i][j] = Real.of(U[i][j]);
            Real[][] VReal = new Real[n][nu]; // Economy V
            for (int i = 0; i < n; i++) for (int j = 0; j < nu; j++) VReal[i][j] = Real.of(V[i][j]);

            return new SVDResult<>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(UReal, Reals.getInstance()),
                org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(s),
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(VReal, Reals.getInstance())
            );
        }
    }

    private static class JavaEigen {
        public static EigenResult<Real> decompose(Matrix<Real> matrix) {
            int n = matrix.rows();
            if (n != matrix.cols()) throw new IllegalArgumentException("Matrix must be square");

            // Check for symmetry - Jacobi is very robust for symmetric matrices
            boolean isSymmetric = true;
            for (int i = 0; i < n && isSymmetric; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (Math.abs(matrix.get(i, j).doubleValue() - matrix.get(j, i).doubleValue()) > 1e-10) {
                        isSymmetric = false;
                        break;
                    }
                }
            }

            if (isSymmetric) {
                return jacobi(matrix);
            }

            // Fallback to QR for non-symmetric matrices
            Real[][] A = new Real[n][n];
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) A[i][j] = matrix.get(i, j);

            Real[][] Q_acc = new Real[n][n];
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) Q_acc[i][j] = (i == j) ? Real.ONE : Real.ZERO;

            int maxIter = 200;
            for (int iter = 0; iter < maxIter; iter++) {
                QRResult<Real> qr = JavaQR.decompose(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(A, Reals.getInstance()));
                Matrix<Real> nextA = qr.R().multiply(qr.Q());
                
                Matrix<Real> nextQ = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(Q_acc, Reals.getInstance()).multiply(qr.Q());
                for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) Q_acc[i][j] = nextQ.get(i, j);

                double offDiagNorm = 0.0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        A[i][j] = nextA.get(i, j);
                        if (i != j) offDiagNorm += Math.abs(A[i][j].doubleValue());
                    }
                }
                if (offDiagNorm < 1e-12) break;
            }

            Real[] values = new Real[n];
            for (int i = 0; i < n; i++) values[i] = A[i][i];

            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(Q_acc, Reals.getInstance()), 
                org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(java.util.Arrays.stream(values).mapToDouble(Real::doubleValue).toArray()));
        }

        private static EigenResult<Real> jacobi(Matrix<Real> a) {
            int n = a.rows();
            double[] data = new double[n * n];
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i * n + j] = a.get(i, j).doubleValue();
            
            double[] vData = new double[n * n];
            for (int i = 0; i < n; i++) vData[i * n + i] = 1.0;
            
            int maxSweeps = 50;
            double eps = 1e-15;
            
            for (int sweep = 0; sweep < maxSweeps; sweep++) {
                double offDiag = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) offDiag += Math.abs(data[i * n + j]);
                }
                if (offDiag < eps) break;
                
                for (int p = 0; p < n - 1; p++) {
                    for (int q = p + 1; q < n; q++) {
                        double apq = data[p * n + q];
                        if (Math.abs(apq) < eps) continue;
                        
                        double app = data[p * n + p];
                        double aqq = data[q * n + q];
                        
                        double tau = (aqq - app) / (2.0 * apq);
                        double t = (tau >= 0) ? 1.0 / (tau + Math.sqrt(1.0 + tau * tau)) 
                                             : 1.0 / (tau - Math.sqrt(1.0 + tau * tau));
                        double c = 1.0 / Math.sqrt(1.0 + t * t);
                        double s = t * c;

                        // Update A
                        for (int i = 0; i < n; i++) {
                            double tp = data[p * n + i];
                            double tq = data[q * n + i];
                            data[p * n + i] = c * tp - s * tq;
                            data[q * n + i] = s * tp + c * tq;
                        }
                        for (int j = 0; j < n; j++) {
                            data[j * n + p] = data[p * n + j];
                            data[j * n + q] = data[q * n + j];
                        }
                        
                        data[p * n + p] = app - t * apq;
                        data[q * n + q] = aqq + t * apq;
                        data[p * n + q] = 0.0;
                        data[q * n + p] = 0.0;
                        
                        // Update V
                        for (int i = 0; i < n; i++) {
                            double vp = vData[i * n + p];
                            double vq = vData[i * n + q];
                            vData[i * n + p] = c * vp - s * vq;
                            vData[i * n + q] = s * vp + c * vq;
                        }
                    }
                }
            }
            
            double[] eigenvalues = new double[n];
            for (int i = 0; i < n; i++) eigenvalues[i] = data[i * n + i];
            
            Real[][] V = new Real[n][n];
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) V[i][j] = Real.of(vData[i * n + j]);

            return new EigenResult<>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(V, Reals.getInstance()),
                org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(eigenvalues)
            );
        }
    }
}



