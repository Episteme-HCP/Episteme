/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.linearalgebra;

import org.jscience.core.technical.algorithm.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.vectors.GenericVector;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.mathematics.structures.rings.Field;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Standard Java CPU implementation for Dense Linear Algebra.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class CPUDenseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    protected final Ring<E> ring;
    private static final int PARALLEL_THRESHOLD = 1000;

    public CPUDenseLinearAlgebraProvider(Ring<E> ring) {
        this.ring = ring;
    }

    public CPUDenseLinearAlgebraProvider() {
        this(null);
    }

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Vector dimensions must match");
        
        if (a.dimension() < PARALLEL_THRESHOLD) {
            @SuppressWarnings("unchecked")
            E[] data = (E[]) new Object[a.dimension()];
            for (int i = 0; i < a.dimension(); i++) {
                data[i] = ring.add(a.get(i), b.get(i));
            }
            return new GenericVector<>(new DenseVectorStorage<>(data), this, ring);
        } else {
            List<E> list = IntStream.range(0, a.dimension())
                    .parallel()
                    .mapToObj(i -> ring.add(a.get(i), b.get(i)))
                    .collect(Collectors.toList());
            @SuppressWarnings("unchecked")
            E[] arr = (E[]) list.toArray();
            return new GenericVector<>(new DenseVectorStorage<>(arr), this, ring);
        }
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Vector dimensions must match");
        return add(a, multiply(b, ring.negate(ring.one())));
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        List<E> list = IntStream.range(0, vector.dimension())
                .parallel()
                .mapToObj(i -> ring.multiply(vector.get(i), scalar))
                .collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) list.toArray();
        return new GenericVector<>(new DenseVectorStorage<>(arr), this, ring);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return IntStream.range(0, a.dimension())
                .parallel()
                .mapToObj(i -> ring.multiply(a.get(i), b.get(i)))
                .reduce(ring.zero(), ring::add);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        E dotProduct = dot(a, a);
        if (dotProduct instanceof org.jscience.core.mathematics.numbers.real.Real) {
            return (E) org.jscience.core.mathematics.numbers.real.Real.of(Math.sqrt(((org.jscience.core.mathematics.numbers.real.Real) dotProduct).doubleValue()));
        }
        throw new UnsupportedOperationException("Norm requires Real field");
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), a.cols(), ring.zero());
        IntStream.range(0, a.rows()).parallel().forEach(i -> {
            for (int j = 0; j < a.cols(); j++) storage.set(i, j, ring.add(a.get(i, j), b.get(i, j)));
        });
        return new GenericMatrix<>(storage, this, ring);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return add(a, scale(ring.negate(ring.one()), b));
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), b.cols(), ring.zero());
        IntStream.range(0, a.rows()).parallel().forEach(i -> {
            for (int j = 0; j < b.cols(); j++) {
                E sum = ring.zero();
                for (int k = 0; k < a.cols(); k++) {
                    sum = ring.add(sum, ring.multiply(a.get(i, k), b.get(k, j)));
                }
                storage.set(i, j, sum);
            }
        });
        return new GenericMatrix<>(storage, this, ring);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        List<E> list = IntStream.range(0, a.rows())
                .parallel()
                .mapToObj(i -> {
                    E sum = ring.zero();
                    for (int j = 0; j < a.cols(); j++) {
                        sum = ring.add(sum, ring.multiply(a.get(i, j), b.get(j)));
                    }
                    return sum;
                })
                .collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) list.toArray();
        return new GenericVector<>(new DenseVectorStorage<>(arr), this, ring);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!(ring instanceof Field)) throw new UnsupportedOperationException("Inversion requires Field");
        Field<E> f = (Field<E>) ring;
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");

        // Identity matrix
        DenseMatrixStorage<E> resultStorage = new DenseMatrixStorage<>(n, n, f.zero());
        for (int i = 0; i < n; i++) resultStorage.set(i, i, f.one());

        // Working copy
        DenseMatrixStorage<E> workingStorage = new DenseMatrixStorage<>(n, n, f.zero());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) workingStorage.set(i, j, a.get(i, j));
        }

        // Gauss-Jordan
        for (int i = 0; i < n; i++) {
            // Find pivot
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (!workingStorage.get(j, i).equals(f.zero())) {
                    pivot = j;
                    break;
                }
            }
            
            // Swap rows
            if (pivot != i) {
                for (int j = 0; j < n; j++) {
                    E tempW = workingStorage.get(i, j);
                    workingStorage.set(i, j, workingStorage.get(pivot, j));
                    workingStorage.set(pivot, j, tempW);
                    
                    E tempR = resultStorage.get(i, j);
                    resultStorage.set(i, j, resultStorage.get(pivot, j));
                    resultStorage.set(pivot, j, tempR);
                }
            }

            E diag = workingStorage.get(i, i);
            if (diag.equals(f.zero())) throw new ArithmeticException("Matrix is singular");

            // Normalize row i
            for (int j = 0; j < n; j++) {
                workingStorage.set(i, j, f.divide(workingStorage.get(i, j), diag));
                resultStorage.set(i, j, f.divide(resultStorage.get(i, j), diag));
            }

            // Eliminate column i in other rows
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    E factor = workingStorage.get(k, i);
                    for (int j = 0; j < n; j++) {
                        workingStorage.set(k, j, f.subtract(workingStorage.get(k, j), f.multiply(factor, workingStorage.get(i, j))));
                        resultStorage.set(k, j, f.subtract(resultStorage.get(k, j), f.multiply(factor, resultStorage.get(i, j))));
                    }
                }
            }
        }

        return new GenericMatrix<>(resultStorage, this, ring);
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (!(ring instanceof Field)) throw new UnsupportedOperationException("Determinant requires Field");
        Field<E> f = (Field<E>) ring;
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");

        // Working copy
        DenseMatrixStorage<E> workingStorage = new DenseMatrixStorage<>(n, n, f.zero());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) workingStorage.set(i, j, a.get(i, j));
        }

        E det = f.one();
        int swaps = 0;

        for (int i = 0; i < n; i++) {
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (!workingStorage.get(j, i).equals(f.zero())) {
                    pivot = j;
                    break;
                }
            }
            
            if (pivot != i) {
                for (int j = 0; j < n; j++) {
                    E tempW = workingStorage.get(i, j);
                    workingStorage.set(i, j, workingStorage.get(pivot, j));
                    workingStorage.set(pivot, j, tempW);
                }
                swaps++;
            }

            E diag = workingStorage.get(i, i);
            if (diag.equals(f.zero())) return f.zero();

            det = f.multiply(det, diag);

            for (int k = i + 1; k < n; k++) {
                E factor = f.divide(workingStorage.get(k, i), diag);
                for (int j = i; j < n; j++) {
                    workingStorage.set(k, j, f.subtract(workingStorage.get(k, j), f.multiply(factor, workingStorage.get(i, j))));
                }
            }
        }

        if (swaps % 2 != 0) det = f.negate(det);
        return det;
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        // Simple solve: Ax = b -> x = A^-1 * b (not most efficient but robust)
        return multiply(inverse(a), b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.cols(), a.rows(), ring.zero());
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.cols(); j++) storage.set(j, i, a.get(i, j));
        }
        return new GenericMatrix<>(storage, this, ring);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<>(a.rows(), a.cols(), ring.zero());
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.cols(); j++) storage.set(i, j, ring.multiply(a.get(i, j), scalar));
        }
        return new GenericMatrix<>(storage, this, ring);
    }

    @Override
    public String getName() {
        return "Java CPU (Dense)";
    }
}
