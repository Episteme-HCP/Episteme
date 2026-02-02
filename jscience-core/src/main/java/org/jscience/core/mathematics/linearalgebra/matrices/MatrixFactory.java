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

package org.jscience.core.mathematics.linearalgebra.matrices;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.*;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.technical.algorithm.LinearAlgebraProvider;
import org.jscience.core.technical.algorithm.linearalgebra.CPUDenseLinearAlgebraProvider;
import java.util.List;
import java.util.ArrayList;

/**
 * Factory for creating matrices with specific storage layouts.
 * * */
public final class MatrixFactory {

    private MatrixFactory() {
        // Utility class
    }

    /**
 * Storage layout types.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Storage {
        AUTO,
        DENSE,
        SPARSE,
        BANDED,
        DIAGONAL,
        SYMMETRIC,
        TRIANGULAR,
        TRIDIAGONAL
    }

    /**
     * Creates an identity matrix of the specified size.
     * 
     * @param size  the dimension of the matrix
     * @param field the field of elements
     * @return a new Identity Matrix instance
     */
    public static <E> Matrix<E> identity(int size, Ring<E> ring) {
        @SuppressWarnings("unchecked")
        E[] diag = (E[]) new Object[size];
        E one = ring.one();
        for (int i = 0; i < size; i++) {
            diag[i] = one;
        }
        DiagonalMatrixStorage<E> storage = new DiagonalMatrixStorage<E>(diag, ring);
        return new GenericMatrix<E>(storage, getProvider(ring), ring);
    }

    public static <E> GenericMatrix<E> create(DiagonalMatrixStorage<E> storage, LinearAlgebraProvider<E> provider, Ring<E> ring) {
        return new GenericMatrix<E>(storage, provider, ring);
    }

    /**
     * Creates a zero-initialized dense matrix of the specified dimensions.
     * Convenience method for Real matrices.
     * 
     * @param elementClass the class of elements (e.g., Real.class)
     * @param rows         number of rows
     * @param cols         number of columns
     * @return a new mutable DenseMatrix instance
     */
    @SuppressWarnings("unchecked")
    public static <E> DenseMatrix<E> dense(Class<E> elementClass, int rows, int cols) {
        if (elementClass == org.jscience.core.mathematics.numbers.real.Real.class) {
            org.jscience.core.mathematics.numbers.real.Real[][] data = new org.jscience.core.mathematics.numbers.real.Real[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    data[i][j] = org.jscience.core.mathematics.numbers.real.Real.ZERO;
                }
            }
            return (DenseMatrix<E>) DenseMatrix.of(data, org.jscience.core.mathematics.numbers.real.Real.ZERO);
        }
        throw new UnsupportedOperationException("dense() only supports Real.class currently");
    }

    /**
     * Creates a matrix with automatic storage selection (AUTO).
     * 
     * @param data  the data in row-major order (2D array)
     * @param field the field of elements
     * @return a new Matrix instance
     */
    public static <E> Matrix<E> create(E[][] data, Ring<E> ring) {
        return create(data, ring, Storage.AUTO);
    }

    /**
     * Creates a matrix with the specified storage layout.
     * 
     * @param data        the data in row-major order (2D array)
     * @param field       the field of elements
     * @param storageType the desired storage layout
     * @return a new Matrix instance
     */
    public static <E> Matrix<E> create(E[][] data, Ring<E> ring, Storage storageType) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;

        switch (storageType) {
            case AUTO:
                // Simple heuristic: check density
                int nonZero = 0;
                int total = rows * cols;
                E zero = ring.zero();
                for (E[] row : data) {
                    for (E val : row) {
                        if (!val.equals(zero))
                            nonZero++;
                    }
                }
                // If density < 0.2, use Sparse
                if ((double) nonZero / total < 0.2) {
                    return create(data, ring, Storage.SPARSE);
                } else {
                    return create(data, ring, Storage.DENSE);
                }

            case DENSE:
                // Check for RealDouble via first element or Field type
                // Ideally we check if Field is Reals
                // if (field instanceof org.jscience.core.mathematics.sets.Reals) -> this is cleaner
                // but type erasure issues?
                // Let's stick to element check for safety or explicit check

                boolean isReal = false;
                // Check first element if available
                if (rows > 0 && cols > 0 && data[0][0] instanceof org.jscience.core.mathematics.numbers.real.Real) {
                    isReal = true;
                }

                if (isReal) {
                    // Convert to double[][] for SIMD matrix (or Generic optimization)
                    double[] flatData = new double[rows * cols];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            flatData[i * cols + j] = ((org.jscience.core.mathematics.numbers.real.Real) data[i][j]).doubleValue();
                        }
                    }
                    // Use SIMDDoubleMatrix directly if applicable
                    @SuppressWarnings("unchecked")
                    Matrix<E> m = (Matrix<E>) new SIMDDoubleMatrix(rows, cols, flatData);
                    return m;
                }
                // Flatten for DenseMatrix constructor
                List<List<E>> listData = new ArrayList<>();
                for (E[] row : data) {
                    List<E> lRow = new ArrayList<>();
                    for (E val : row)
                        lRow.add(val);
                    listData.add(lRow);
                }
                return new DenseMatrix<E>(listData, ring);

            case SPARSE:
                // Use SparseMatrix
                SparseMatrix<E> sm = new SparseMatrix<E>(rows, cols, ring);
                E z = ring.zero();
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if (!data[i][j].equals(z)) {
                            sm.set(i, j, data[i][j]);
                        }
                    }
                }
                return sm;

            case TRIANGULAR:
                // Determine if Upper or Lower from data or assume one?
                // Usually factory creates what you give it. If data is full, it might truncate?
                // Let's assume Upper for now or check?
                // TriangularMatrixStorage requires n (square)
                if (rows != cols)
                    throw new IllegalArgumentException("Triangular matrices must be square");
                // boolean upper = true;
                // For simplicity, let's just create a generic matrix with Triangular Storage
                // But we need to populate it.
                TriangularMatrixStorage<E> triStorage = new TriangularMatrixStorage<>(rows, true, ring.zero());
                // Try to fill. If data in lower part is non-zero, this will fail if we enforce
                // it.
                // Let's iterate and set.
                for (int i = 0; i < rows; i++) {
                    for (int j = i; j < cols; j++) { // Only upper
                        triStorage.set(i, j, data[i][j]);
                    }
                }
                // Use Registry
                return new GenericMatrix<E>(triStorage, getProvider(ring),
                        ring);

            case TRIDIAGONAL:
                if (rows != cols)
                    throw new IllegalArgumentException("Tridiagonal matrices must be square");
                TridiagonalMatrixStorage<E> tridStorage = new TridiagonalMatrixStorage<>(rows, ring.zero());
                for (int i = 0; i < rows; i++) {
                    if (i < cols)
                        tridStorage.set(i, i, data[i][i]);
                    if (i + 1 < cols)
                        tridStorage.set(i, i + 1, data[i][i + 1]);
                    if (i - 1 >= 0)
                        tridStorage.set(i, i - 1, data[i][i - 1]);
                }
                return new GenericMatrix<E>(tridStorage, getProvider(ring),
                        ring);

            case DIAGONAL:
                if (rows != cols)
                    throw new IllegalArgumentException("Diagonal matrices must be square");
                @SuppressWarnings("unchecked")
                E[] diag = (E[]) new Object[rows];
                for (int i = 0; i < rows; i++)
                    diag[i] = data[i][i];
                DiagonalMatrixStorage<E> diagStorage = new DiagonalMatrixStorage<E>(diag, ring);
                return new GenericMatrix<E>(diagStorage, getProvider(ring),
                        ring);

            case SYMMETRIC:
                if (rows != cols)
                    throw new IllegalArgumentException("Symmetric matrices must be square");
                // Flatten to List<List> for constructor
                List<List<E>> symData = new ArrayList<>();
                for (E[] row : data) {
                    List<E> lRow = new ArrayList<>();
                    for (E val : row)
                        lRow.add(val);
                    symData.add(lRow);
                }
                SymmetricMatrixStorage<E> symStorage = new SymmetricMatrixStorage<E>(symData, ring);
                return new GenericMatrix<E>(symStorage, getProvider(ring),
                        ring);

            case BANDED:
                // Need generic way to deduce bandwidth?
                // For now, fail or default to Dense?
                throw new UnsupportedOperationException(
                        "Automatic creation of Banded Matrix from 2D array not yet implemented");

            default:
                throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
    }

    /**
     * Creates a matrix from a List of Lists with the specified storage layout.
     * 
     * @param data        the data (List of Lists)
     * @param field       the field of elements
     * @param storageType the desired storage layout
     * @return a new Matrix instance
     */
    private static <E> LinearAlgebraProvider<E> getProvider(Ring<E> ring) {
        return new CPUDenseLinearAlgebraProvider<>(ring);
    }

    /**
     * Creates a matrix with automatic storage selection (AUTO) from a List of
     * Lists.
     * 
     * @param data  the data (List of Lists)
     * @param field the field of elements
     * @return a new Matrix instance
     */
    public static <E> Matrix<E> create(List<List<E>> data, Ring<E> ring) {
        return create(data, ring, Storage.AUTO);
    }

    /**
     * Creates a matrix from a List of Lists with the specified storage layout.
     * 
     * @param data        the data (List of Lists)
     * @param field       the field of elements
     * @param storageType the desired storage layout
     * @return a new Matrix instance
     */
    public static <E> Matrix<E> create(List<List<E>> data, Ring<E> ring, Storage storageType) {
        int rows = data.size();
        int cols = rows > 0 ? data.get(0).size() : 0;

        switch (storageType) {
            case AUTO:
                // Simple heuristic: check density
                int nonZero = 0;
                int total = rows * cols;
                E zero = ring.zero();
                for (List<E> row : data) {
                    for (E val : row) {
                        if (!val.equals(zero))
                            nonZero++;
                    }
                }
                // If density < 0.2, use Sparse
                if (total > 0 && (double) nonZero / total < 0.2) {
                    return create(data, ring, Storage.SPARSE);
                } else {
                    return create(data, ring, Storage.DENSE);
                }

            case DENSE:
                // Check for RealDouble via first element
                boolean isRealList = false;
                if (rows > 0 && cols > 0 && data.get(0).get(0) instanceof org.jscience.core.mathematics.numbers.real.Real) {
                    isRealList = true;
                }

                if (isRealList) {
                    double[] flatData = new double[rows * cols];
                    for (int i = 0; i < rows; i++) {
                        List<E> row = data.get(i);
                        for (int j = 0; j < cols; j++) {
                            flatData[i * cols + j] = ((org.jscience.core.mathematics.numbers.real.Real) row.get(j)).doubleValue();
                        }
                    }
                    @SuppressWarnings("unchecked")
                    Matrix<E> m = (Matrix<E>) new SIMDDoubleMatrix(rows, cols, flatData);
                    return m;
                }
                return new DenseMatrix<E>(data, ring);

            case SPARSE:
                SparseMatrix<E> sm = new SparseMatrix<E>(rows, cols, ring);
                E zeroVal = ring.zero();
                for (int i = 0; i < rows; i++) {
                    List<E> row = data.get(i);
                    for (int j = 0; j < cols; j++) {
                        E val = row.get(j);
                        if (!val.equals(zeroVal)) {
                            sm.set(i, j, val);
                        }
                    }
                }
                return sm;

            case SYMMETRIC:
                SymmetricMatrixStorage<E> symStorage = new SymmetricMatrixStorage<E>(data, ring);
                return new GenericMatrix<E>(symStorage, getProvider(ring),
                        ring);

            case TRIANGULAR:
                if (rows != cols)
                    throw new IllegalArgumentException("Triangular matrices must be square");
                TriangularMatrixStorage<E> triStorage = new TriangularMatrixStorage<>(rows, true, ring.zero());
                for (int i = 0; i < rows; i++) {
                    List<E> row = data.get(i);
                    for (int j = i; j < cols; j++) {
                        triStorage.set(i, j, row.get(j));
                    }
                }
                return new GenericMatrix<E>(triStorage, getProvider(ring),
                        ring);

            case TRIDIAGONAL:
                if (rows != cols)
                    throw new IllegalArgumentException("Tridiagonal matrices must be square");
                TridiagonalMatrixStorage<E> tridStorage = new TridiagonalMatrixStorage<>(rows, ring.zero());
                for (int i = 0; i < rows; i++) {
                    List<E> row = data.get(i);
                    if (i < cols)
                        tridStorage.set(i, i, row.get(i));
                    if (i + 1 < cols)
                        tridStorage.set(i, i + 1, row.get(i + 1));
                    if (i - 1 >= 0)
                        tridStorage.set(i, i - 1, row.get(i - 1));
                }
                return new GenericMatrix<E>(tridStorage, getProvider(ring),
                        ring);

            case DIAGONAL:
                if (rows != cols)
                    throw new IllegalArgumentException("Diagonal matrices must be square");

                @SuppressWarnings("unchecked")
                E[] diag = (E[]) new Object[rows]; // Unchecked cast
                for (int i = 0; i < rows; i++)
                    diag[i] = data.get(i).get(i);
                DiagonalMatrixStorage<E> diagStorage = new DiagonalMatrixStorage<E>(diag, ring);
                return new GenericMatrix<E>(diagStorage, getProvider(ring),
                        ring);

            case BANDED:
                throw new UnsupportedOperationException(
                        "Automatic creation of Banded Matrix from List not yet implemented");

            default:
                throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
    }
    // --- Helper methods for Constructors ---

    /**
     * Determines and creates the optimal Dense storage for the given data.
     */
    public static <E> MatrixStorage<E> createDenseStorage(E[][] data, Ring<E> ring) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;

        boolean isReal = (rows > 0 && cols > 0 && data[0][0] instanceof org.jscience.core.mathematics.numbers.real.Real);

        if (isReal) {
            double[][] dData = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    dData[i][j] = ((org.jscience.core.mathematics.numbers.real.Real) data[i][j]).doubleValue();
                }
            }
            // Use Heap storage by default for constructors unless configured otherwise
            double[] flat = new double[rows * cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(dData[i], 0, flat, i * cols, cols);
            }
            @SuppressWarnings("unchecked")
            MatrixStorage<E> res = (MatrixStorage<E>) new HeapRealDoubleMatrixStorage(flat, rows, cols);
            return res;
        }

        // Generic Dense
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<E>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                storage.set(i, j, data[i][j]);
            }
        }
        return storage;
    }

    public static <E> MatrixStorage<E> createDenseStorage(List<List<E>> data, Ring<E> ring) {
        int rows = data.size();
        int cols = rows > 0 ? data.get(0).size() : 0;

        boolean isReal = (rows > 0 && cols > 0
                && data.get(0).get(0) instanceof org.jscience.core.mathematics.numbers.real.Real);

        if (isReal) {
            double[][] dData = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                List<E> row = data.get(i);
                for (int j = 0; j < cols; j++) {
                    dData[i][j] = ((org.jscience.core.mathematics.numbers.real.Real) row.get(j)).doubleValue();
                }
            }
            double[] flat = new double[rows * cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(dData[i], 0, flat, i * cols, cols);
            }
            @SuppressWarnings("unchecked")
            MatrixStorage<E> res = (MatrixStorage<E>) new HeapRealDoubleMatrixStorage(flat, rows, cols);
            return res;
        }

        DenseMatrixStorage<E> storage = new DenseMatrixStorage<E>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) {
            List<E> row = data.get(i);
            for (int j = 0; j < cols; j++) {
                storage.set(i, j, row.get(j));
            }
        }
        return storage;
    }

    /**
     * Determines and creates the optimal Sparse storage for the given data.
     */
    public static <E> MatrixStorage<E> createSparseStorage(E[][] data, Ring<E> ring) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;
        SparseMatrixStorage<E> storage = new SparseMatrixStorage<>(rows, cols, ring.zero());
        E zero = ring.zero();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!data[i][j].equals(zero)) {
                    storage.set(i, j, data[i][j]);
                }
            }
        }
        return storage;
    }

    public static <E> MatrixStorage<E> createSparseStorage(List<List<E>> data, Ring<E> ring) {
        int rows = data.size();
        int cols = rows > 0 ? data.get(0).size() : 0;
        SparseMatrixStorage<E> storage = new SparseMatrixStorage<E>(rows, cols, ring.zero());
        E zero = ring.zero();
        for (int i = 0; i < rows; i++) {
            List<E> row = data.get(i);
            for (int j = 0; j < cols; j++) {
                E val = row.get(j);
                if (!val.equals(zero)) {
                    storage.set(i, j, val);
                }
            }
        }
        return storage;
    }

    public static <E> LinearAlgebraProvider<E> getStandardProvider(Ring<E> ring) {
        return getProvider(ring);
    }

    /**
     * Determines and creates the optimal storage (Dense or Sparse) based on data
     * density.
     */
    public static <E> MatrixStorage<E> createAutomaticStorage(E[][] data, Ring<E> ring) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;
        int nonZero = 0;
        int total = rows * cols;
        E zero = ring.zero();
        for (E[] row : data) {
            for (E val : row) {
                if (!val.equals(zero))
                    nonZero++;
            }
        }
        if (total > 0 && (double) nonZero / total < 0.2) {
            return createSparseStorage(data, ring);
        } else {
            return createDenseStorage(data, ring);
        }
    }

    public static <E> MatrixStorage<E> createAutomaticStorage(List<List<E>> data, Ring<E> ring) {
        int rows = data.size();
        int cols = rows > 0 ? data.get(0).size() : 0;
        int nonZero = 0;
        int total = rows * cols;
        E zero = ring.zero();
        for (List<E> row : data) {
            for (E val : row) {
                if (!val.equals(zero))
                    nonZero++;
            }
        }
        if (total > 0 && (double) nonZero / total < 0.2) {
            return createSparseStorage(data, ring);
        } else {
            return createDenseStorage(data, ring);
        }
    }
}



