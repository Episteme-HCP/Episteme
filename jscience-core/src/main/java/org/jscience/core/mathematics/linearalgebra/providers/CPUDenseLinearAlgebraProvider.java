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

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;


import java.util.ArrayList;

import java.util.List;

import java.util.stream.Collectors;

import java.util.stream.IntStream;


import org.jscience.core.mathematics.linearalgebra.matrices.GenericMatrix;

import org.jscience.core.mathematics.linearalgebra.Matrix;


import org.jscience.core.mathematics.linearalgebra.Vector;

import org.jscience.core.technical.backend.ExecutionContext;

import org.jscience.core.technical.backend.cpu.CPUExecutionContext;

import org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;

import org.jscience.core.mathematics.linearalgebra.vectors.GenericVector;

/**
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CPUDenseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    protected final org.jscience.core.mathematics.structures.rings.Ring<E> ring;
    private static final int PARALLEL_THRESHOLD = 1000;

    public CPUDenseLinearAlgebraProvider(org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        this.ring = ring;
    }

    /**
     * Public no-arg constructor required by ServiceLoader.
     */
    public CPUDenseLinearAlgebraProvider() {
        this(null);
    }

    @Override
    public String getName() {
        return "Java CPU (Dense)";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
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
                data[i] = ring.add(a.get(i), b.get(i));
            }
            return new GenericVector<>(
                    new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(data), this, ring);
        } else {
            return IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> ring.add(a.get(i), b.get(i)))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, ring);
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
                E negB = ring.negate(b.get(i));
                result.add(ring.add(a.get(i), negB));
            }
            return new GenericVector<>(
                    new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(result), this,
                    ring);
        } else {
            return IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> {
                        E negB = ring.negate(b.get(i));
                        return ring.add(a.get(i), negB);
                    })
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, ring);
                            }));
        }
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (vector.dimension() < PARALLEL_THRESHOLD) {
            List<E> result = new ArrayList<>(vector.dimension());
            for (int i = 0; i < vector.dimension(); i++) {
                result.add(ring.multiply(vector.get(i), scalar));
            }
            // return new GenericVector(result.toArray(newFieldsElement[0]...), this,
            // field);
            // Handling array creation generically is hard without class token.
            // DenseVector dealt with List. GenericVector takes Array.
            // We can create Array from List if we cast.

            @SuppressWarnings("unchecked")
            E[] arr = (E[]) result.toArray(); // Safe if result contains E
            return new GenericVector<>(
                    new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(arr), this, ring);
        } else {
            return IntStream.range(0, vector.dimension())
                    .parallel()
                    .mapToObj(i -> ring.multiply(vector.get(i), scalar))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, ring);
                            }));
        }
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }
        if (a.dimension() < PARALLEL_THRESHOLD) {
            E sum = ring.zero();
            for (int i = 0; i < a.dimension(); i++) {
                E product = ring.multiply(a.get(i), b.get(i));
                sum = ring.add(sum, product);
            }
            return sum;
        } else {
            return IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> ring.multiply(a.get(i), b.get(i)))
                    .reduce(ring.zero(), ring::add);
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        E dotProduct = dot(a, a);
        if (ring instanceof org.jscience.core.mathematics.sets.Reals) {
            org.jscience.core.mathematics.numbers.real.Real r = (org.jscience.core.mathematics.numbers.real.Real) dotProduct;
            double val = r.doubleValue();
            return (E) org.jscience.core.mathematics.numbers.real.Real.of(Math.sqrt(val));
        }
        if (dotProduct instanceof org.jscience.core.mathematics.numbers.real.RealDouble) {
            org.jscience.core.mathematics.numbers.real.RealDouble rd = (org.jscience.core.mathematics.numbers.real.RealDouble) dotProduct;
            return (E) org.jscience.core.mathematics.numbers.real.RealDouble.of(Math.sqrt(rd.doubleValue()));
        }
        if (dotProduct instanceof org.jscience.core.mathematics.numbers.real.Real) {
            org.jscience.core.mathematics.numbers.real.Real r = (org.jscience.core.mathematics.numbers.real.Real) dotProduct;
            return (E) org.jscience.core.mathematics.numbers.real.Real.of(Math.sqrt(r.doubleValue()));
        }
        throw new UnsupportedOperationException("Norm not supported for ring: " + ring.getClass().getSimpleName());
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) {
            throw new IllegalArgumentException("Matrix dimensions must match");
        }
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), a.cols(), ring.zero());

        if (a.rows() * a.cols() < PARALLEL_THRESHOLD) {
            for (int i = 0; i < a.rows(); i++) {
                for (int j = 0; j < a.cols(); j++) {
                    storage.set(i, j, ring.add(a.get(i, j), b.get(i, j)));
                }
            }
        } else {
            IntStream.range(0, a.rows()).parallel().forEach(i -> {
                for (int j = 0; j < a.cols(); j++) {
                    storage.set(i, j, ring.add(a.get(i, j), b.get(i, j)));
                }
            });
        }
        return new GenericMatrix<E>(storage, this, ring);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) {
            throw new IllegalArgumentException("Matrix dimensions must match");
        }
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), a.cols(), ring.zero());

        if (a.rows() * a.cols() < PARALLEL_THRESHOLD) {
            for (int i = 0; i < a.rows(); i++) {
                for (int j = 0; j < a.cols(); j++) {
                    E negB = ring.negate(b.get(i, j));
                    storage.set(i, j, ring.add(a.get(i, j), negB));
                }
            }
        } else {
            IntStream.range(0, a.rows()).parallel().forEach(i -> {
                for (int j = 0; j < a.cols(); j++) {
                    E negB = ring.negate(b.get(i, j));
                    storage.set(i, j, ring.add(a.get(i, j), negB));
                }
            });
        }
        return new GenericMatrix<E>(storage, this, ring);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) {
            throw new IllegalArgumentException("Matrix inner dimensions must match");
        }

        // Use Strassen if beneficial
        if (a.rows() >= 64 && a.cols() >= 64 && b.cols() >= 64
                && isSquarePowerOfTwo(a) && isSquarePowerOfTwo(b) && a.rows() == b.rows()) {
            return strassenRecursive(a, b);
        }

        return standardMultiply(a, b);
    }

    private boolean isSquarePowerOfTwo(Matrix<E> m) {
        return m.rows() == m.cols() && (m.rows() & (m.rows() - 1)) == 0;
    }

    private Matrix<E> strassenRecursive(Matrix<E> A, Matrix<E> B) {
        int n = A.rows();
        if (n <= 64) {
            return standardMultiply(A, B);
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
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(n, n, ring.zero());

        copySubMatrixToStorage(storage, C11, 0, 0);
        copySubMatrixToStorage(storage, C12, 0, n / 2);
        copySubMatrixToStorage(storage, C21, n / 2, 0);
        copySubMatrixToStorage(storage, C22, n / 2, n / 2);

        return new GenericMatrix<E>(storage, this, ring);
    }

    private void copySubMatrixToStorage(DenseMatrixStorage<E> storage, Matrix<E> sub, int rowOffset, int colOffset) {
        for (int i = 0; i < sub.rows(); i++) {
            for (int j = 0; j < sub.cols(); j++) {
                storage.set(rowOffset + i, colOffset + j, sub.get(i, j));
            }
        }
    }

    private Matrix<E> standardMultiply(Matrix<E> a, Matrix<E> b) {
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), b.cols(), ring.zero());
        long start = System.nanoTime();
        try {
            if (a.rows() < 10) {
                // ... serial loop ...
                for (int i = 0; i < a.rows(); i++) {
                    for (int j = 0; j < b.cols(); j++) {
                        E sum = ring.zero();
                        for (int k = 0; k < a.cols(); k++) {
                            E product = ring.multiply(a.get(i, k), b.get(k, j));
                            sum = ring.add(sum, product);
                        }
                        storage.set(i, j, sum);
                    }
                }
            } else {
                IntStream.range(0, a.rows()).parallel().forEach(i -> {
                    for (int j = 0; j < b.cols(); j++) {
                        E sum = ring.zero();
                        for (int k = 0; k < a.cols(); k++) {
                            E product = ring.multiply(a.get(i, k), b.get(k, j));
                            sum = ring.add(sum, product);
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
        return new GenericMatrix<E>(storage, this, ring);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.cols(), a.rows(), ring.zero());
        IntStream.range(0, a.rows()).parallel().forEach(i -> {
            for (int j = 0; j < a.cols(); j++) {
                storage.set(j, i, a.get(i, j));
            }
        });
        return new GenericMatrix<E>(storage, this, ring);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), a.cols(), ring.zero());
        IntStream.range(0, a.rows()).parallel().forEach(i -> {
            for (int j = 0; j < a.cols(); j++) {
                storage.set(i, j, ring.multiply(a.get(i, j), scalar));
            }
        });
        return new GenericMatrix<E>(storage, this, ring);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (a.cols() != b.dimension()) {
            throw new IllegalArgumentException("Matrix columns must match vector dimension");
        }

        if (a.rows() < PARALLEL_THRESHOLD) {
            List<E> result = new ArrayList<>(a.rows());
            for (int i = 0; i < a.rows(); i++) {
                E sum = ring.zero();
                for (int j = 0; j < a.cols(); j++) {
                    E product = ring.multiply(a.get(i, j), b.get(j));
                    sum = ring.add(sum, product);
                }
                result.add(sum);
            }
            return new GenericVector<>(
                    new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(result), this,
                    ring);
        } else {
            return IntStream.range(0, a.rows())
                    .parallel()
                    .mapToObj(i -> {
                        E sum = ring.zero();
                        for (int j = 0; j < a.cols(); j++) {
                            E product = ring.multiply(a.get(i, j), b.get(j));
                            sum = ring.add(sum, product);
                        }
                        return sum;
                    })
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                @SuppressWarnings("unchecked")
                                E[] arr = (E[]) list.toArray();
                                return new GenericVector<>(
                                        new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(
                                                arr),
                                        this, ring);
                            }));
        }
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!(ring instanceof org.jscience.core.mathematics.structures.rings.Field)) {
            throw new UnsupportedOperationException("Matrix inversion requires a Field, found: " + ring.getClass().getSimpleName());
        }
        org.jscience.core.mathematics.structures.rings.Field<E> field = (org.jscience.core.mathematics.structures.rings.Field<E>) ring;

        if (a.rows() != a.cols()) {
            throw new ArithmeticException("Matrix must be square to compute inverse");
        }

        int n = a.rows();
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
            for (int i = col + 1; i < n; i++) {
                if (!aug.get(i).get(col).equals(field.zero())) {
                    pivotRow = i;
                    break;
                }
            }
            if (pivotRow != col) {
                List<E> temp = aug.get(col);
                aug.set(col, aug.get(pivotRow));
                aug.set(pivotRow, temp);
            }
            E pivot = aug.get(col).get(col);
            if (pivot.equals(field.zero()))
                throw new ArithmeticException("Matrix is singular");
            E pivotInv = field.divide(field.one(), pivot);
            for (int j = 0; j < 2 * n; j++)
                aug.get(col).set(j, field.multiply(aug.get(col).get(j), pivotInv));

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
        return new GenericMatrix<E>(storage, this, ring);
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (a.rows() != a.cols())
            throw new ArithmeticException("Must be square");
        int n = a.rows();
        if (n == 1)
            return a.get(0, 0);
        if (n == 2) {
            return ring.add(ring.multiply(a.get(0, 0), a.get(1, 1)),
                    ring.negate(ring.multiply(a.get(0, 1), a.get(1, 0))));
        }

        // Division-free determinant if not a field?
        // Using Gauss for now, but requires Field.
        if (!(ring instanceof org.jscience.core.mathematics.structures.rings.Field)) {
            // Simplified: only allow 1x1, 2x2 determinant for general rings
            // in LU-based approach. We could implement Bareiss algorithm here.
            throw new UnsupportedOperationException("Determinant for N > 2 requires a Field or Bareiss implementation.");
        }
        org.jscience.core.mathematics.structures.rings.Field<E> field = (org.jscience.core.mathematics.structures.rings.Field<E>) ring;

        List<List<E>> mat = new ArrayList<>();
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
        if (!(ring instanceof org.jscience.core.mathematics.structures.rings.Field)) {
            throw new UnsupportedOperationException("Linear system solving requires a Field, found: " + ring.getClass().getSimpleName());
        }
        org.jscience.core.mathematics.structures.rings.Field<E> field = (org.jscience.core.mathematics.structures.rings.Field<E>) ring;

        if (a.rows() != a.cols())
            throw new ArithmeticException("Must be square");
        int n = a.rows();
        List<List<E>> aug = new ArrayList<>();
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
                new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(resArray), this, ring);
    }

    @Override
    public String getId() {
        return "cpudense";
    }

    @Override
    public String getDescription() {
        return "CPUDenseLinearAlgebraProvider";
    }
}



