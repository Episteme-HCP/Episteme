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
package org.jscience.mathematics.linearalgebra.matrices;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.storage.*;
import org.jscience.mathematics.structures.rings.Field;
import java.util.List;
import java.util.ArrayList;

/**
 * Factory for creating matrices with specific storage layouts.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class MatrixFactory {

    private MatrixFactory() {
        // Utility class
    }

    /**
     * Storage layout types.
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
    public static <E> Matrix<E> identity(int size, Field<E> field) {
        @SuppressWarnings("unchecked")
        E[] diag = (E[]) new Object[size];
        E one = field.one();
        for (int i = 0; i < size; i++) {
            diag[i] = one;
        }
        DiagonalMatrixStorage<E> storage = new DiagonalMatrixStorage<>(diag, field);
        return new GenericMatrix<>(storage, getProvider(field), field);
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
        if (elementClass == org.jscience.mathematics.numbers.real.Real.class) {
            org.jscience.mathematics.numbers.real.Real[][] data = new org.jscience.mathematics.numbers.real.Real[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    data[i][j] = org.jscience.mathematics.numbers.real.Real.ZERO;
                }
            }
            return (DenseMatrix<E>) DenseMatrix.of(data, org.jscience.mathematics.numbers.real.Real.ZERO);
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
    public static <E> Matrix<E> create(E[][] data, Field<E> field) {
        return create(data, field, Storage.AUTO);
    }

    /**
     * Creates a matrix with the specified storage layout.
     * 
     * @param data        the data in row-major order (2D array)
     * @param field       the field of elements
     * @param storageType the desired storage layout
     * @return a new Matrix instance
     */
    public static <E> Matrix<E> create(E[][] data, Field<E> field, Storage storageType) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;

        switch (storageType) {
            case AUTO:
                // Simple heuristic: check density
                int nonZero = 0;
                int total = rows * cols;
                E zero = field.zero();
                for (E[] row : data) {
                    for (E val : row) {
                        if (!val.equals(zero))
                            nonZero++;
                    }
                }
                // If density < 0.2, use Sparse
                if ((double) nonZero / total < 0.2) {
                    return create(data, field, Storage.SPARSE);
                } else {
                    return create(data, field, Storage.DENSE);
                }

            case DENSE:
                // Check for RealDouble via first element or Field type
                // Ideally we check if Field is Reals
                // if (field instanceof org.jscience.mathematics.sets.Reals) -> this is cleaner
                // but type erasure issues?
                // Let's stick to element check for safety or explicit check

                boolean isReal = false;
                // Check first element if available
                if (rows > 0 && cols > 0 && data[0][0] instanceof org.jscience.mathematics.numbers.real.Real) {
                    isReal = true;
                }

                if (isReal) {
                    // Convert to double[][] for RealDoubleMatrix
                    double[][] dData = new double[rows][cols];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            dData[i][j] = ((org.jscience.mathematics.numbers.real.Real) data[i][j]).doubleValue();
                        }
                    }
                    double[] flatData = new double[rows * cols];
                    for (int i = 0; i < rows; i++) {
                        System.arraycopy(dData[i], 0, flatData, i * cols, cols);
                    }
                    @SuppressWarnings("unchecked")
                    Matrix<E> m = (Matrix<E>) RealDoubleMatrix.of(flatData, rows, cols);
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
                return new DenseMatrix<>(listData, field);

            case SPARSE:
                // Use SparseMatrix
                SparseMatrix<E> sm = new SparseMatrix<E>(rows, cols, field);
                E z = field.zero();
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
                TriangularMatrixStorage<E> triStorage = new TriangularMatrixStorage<>(rows, true, field.zero());
                // Try to fill. If data in lower part is non-zero, this will fail if we enforce
                // it.
                // Let's iterate and set.
                for (int i = 0; i < rows; i++) {
                    for (int j = i; j < cols; j++) { // Only upper
                        triStorage.set(i, j, data[i][j]);
                    }
                }
                // Use Registry
                return new GenericMatrix<>(triStorage, getProvider(field),
                        field);

            case TRIDIAGONAL:
                if (rows != cols)
                    throw new IllegalArgumentException("Tridiagonal matrices must be square");
                TridiagonalMatrixStorage<E> tridStorage = new TridiagonalMatrixStorage<>(rows, field.zero());
                for (int i = 0; i < rows; i++) {
                    if (i < cols)
                        tridStorage.set(i, i, data[i][i]);
                    if (i + 1 < cols)
                        tridStorage.set(i, i + 1, data[i][i + 1]);
                    if (i - 1 >= 0)
                        tridStorage.set(i, i - 1, data[i][i - 1]);
                }
                return new GenericMatrix<>(tridStorage, getProvider(field),
                        field);

            case DIAGONAL:
                if (rows != cols)
                    throw new IllegalArgumentException("Diagonal matrices must be square");
                @SuppressWarnings("unchecked")
                E[] diag = (E[]) new Object[rows];
                for (int i = 0; i < rows; i++)
                    diag[i] = data[i][i];
                DiagonalMatrixStorage<E> diagStorage = new DiagonalMatrixStorage<>(diag, field);
                return new GenericMatrix<>(diagStorage, getProvider(field),
                        field);

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
                SymmetricMatrixStorage<E> symStorage = new SymmetricMatrixStorage<>(symData, field);
                return new GenericMatrix<>(symStorage, getProvider(field),
                        field);

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
    private static <E> org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider<E> getProvider(
            Field<E> field) {
        // Simple implementation picking the default/first available provider
        // Ideally we would query Registry for a provider compatible with 'field'
        // But for now we just use the default provider from Registry or create one
        // Note: CPUDenseLinearAlgebraProvider needs field
        return new org.jscience.mathematics.linearalgebra.backends.CPUDenseLinearAlgebraProvider<>(field);
    }

    /**
     * Creates a matrix with automatic storage selection (AUTO) from a List of
     * Lists.
     * 
     * @param data  the data (List of Lists)
     * @param field the field of elements
     * @return a new Matrix instance
     */
    public static <E> Matrix<E> create(List<List<E>> data, Field<E> field) {
        return create(data, field, Storage.AUTO);
    }

    /**
     * Creates a matrix from a List of Lists with the specified storage layout.
     * 
     * @param data        the data (List of Lists)
     * @param field       the field of elements
     * @param storageType the desired storage layout
     * @return a new Matrix instance
     */
    public static <E> Matrix<E> create(List<List<E>> data, Field<E> field, Storage storageType) {
        int rows = data.size();
        int cols = rows > 0 ? data.get(0).size() : 0;

        switch (storageType) {
            case AUTO:
                // Simple heuristic: check density
                int nonZero = 0;
                int total = rows * cols;
                E zero = field.zero();
                for (List<E> row : data) {
                    for (E val : row) {
                        if (!val.equals(zero))
                            nonZero++;
                    }
                }
                // If density < 0.2, use Sparse
                if (total > 0 && (double) nonZero / total < 0.2) {
                    return create(data, field, Storage.SPARSE);
                } else {
                    return create(data, field, Storage.DENSE);
                }

            case DENSE:
                // Check for RealDouble via first element
                boolean isRealList = false;
                if (rows > 0 && cols > 0 && data.get(0).get(0) instanceof org.jscience.mathematics.numbers.real.Real) {
                    isRealList = true;
                }

                if (isRealList) {
                    double[][] dData = new double[rows][cols];
                    for (int i = 0; i < rows; i++) {
                        List<E> row = data.get(i);
                        for (int j = 0; j < cols; j++) {
                            dData[i][j] = ((org.jscience.mathematics.numbers.real.Real) row.get(j)).doubleValue();
                        }
                    }
                    double[] flatData = new double[rows * cols];
                    for (int i = 0; i < rows; i++) {
                        System.arraycopy(dData[i], 0, flatData, i * cols, cols);
                    }
                    @SuppressWarnings("unchecked")
                    Matrix<E> m = (Matrix<E>) RealDoubleMatrix.of(flatData, rows, cols);
                    return m;
                }
                return new DenseMatrix<>(data, field);

            case SPARSE:
                SparseMatrix<E> sm = new SparseMatrix<E>(rows, cols, field);
                E zeroVal = field.zero();
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
                SymmetricMatrixStorage<E> symStorage = new SymmetricMatrixStorage<>(data, field);
                return new GenericMatrix<>(symStorage, getProvider(field),
                        field);

            case TRIANGULAR:
                if (rows != cols)
                    throw new IllegalArgumentException("Triangular matrices must be square");
                TriangularMatrixStorage<E> triStorage = new TriangularMatrixStorage<>(rows, true, field.zero());
                for (int i = 0; i < rows; i++) {
                    List<E> row = data.get(i);
                    for (int j = i; j < cols; j++) {
                        triStorage.set(i, j, row.get(j));
                    }
                }
                return new GenericMatrix<>(triStorage, getProvider(field),
                        field);

            case TRIDIAGONAL:
                if (rows != cols)
                    throw new IllegalArgumentException("Tridiagonal matrices must be square");
                TridiagonalMatrixStorage<E> tridStorage = new TridiagonalMatrixStorage<>(rows, field.zero());
                for (int i = 0; i < rows; i++) {
                    List<E> row = data.get(i);
                    if (i < cols)
                        tridStorage.set(i, i, row.get(i));
                    if (i + 1 < cols)
                        tridStorage.set(i, i + 1, row.get(i + 1));
                    if (i - 1 >= 0)
                        tridStorage.set(i, i - 1, row.get(i - 1));
                }
                return new GenericMatrix<>(tridStorage, getProvider(field),
                        field);

            case DIAGONAL:
                if (rows != cols)
                    throw new IllegalArgumentException("Diagonal matrices must be square");

                @SuppressWarnings("unchecked")
                E[] diag = (E[]) new Object[rows]; // Unchecked cast
                for (int i = 0; i < rows; i++)
                    diag[i] = data.get(i).get(i);
                DiagonalMatrixStorage<E> diagStorage = new DiagonalMatrixStorage<>(diag, field);
                return new GenericMatrix<>(diagStorage, getProvider(field),
                        field);

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
    public static <E> MatrixStorage<E> createDenseStorage(E[][] data, Field<E> field) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;

        boolean isReal = (rows > 0 && cols > 0 && data[0][0] instanceof org.jscience.mathematics.numbers.real.Real);

        if (isReal) {
            double[][] dData = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    dData[i][j] = ((org.jscience.mathematics.numbers.real.Real) data[i][j]).doubleValue();
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
        DenseMatrixStorage<E> storage = new DenseMatrixStorage<E>(rows, cols, field.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                storage.set(i, j, data[i][j]);
            }
        }
        return storage;
    }

    public static <E> MatrixStorage<E> createDenseStorage(List<List<E>> data, Field<E> field) {
        int rows = data.size();
        int cols = rows > 0 ? data.get(0).size() : 0;

        boolean isReal = (rows > 0 && cols > 0
                && data.get(0).get(0) instanceof org.jscience.mathematics.numbers.real.Real);

        if (isReal) {
            double[][] dData = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                List<E> row = data.get(i);
                for (int j = 0; j < cols; j++) {
                    dData[i][j] = ((org.jscience.mathematics.numbers.real.Real) row.get(j)).doubleValue();
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

        DenseMatrixStorage<E> storage = new DenseMatrixStorage<E>(rows, cols, field.zero());
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
    public static <E> MatrixStorage<E> createSparseStorage(E[][] data, Field<E> field) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;
        SparseMatrixStorage<E> storage = new SparseMatrixStorage<>(rows, cols, field.zero());
        E zero = field.zero();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!data[i][j].equals(zero)) {
                    storage.set(i, j, data[i][j]);
                }
            }
        }
        return storage;
    }

    public static <E> MatrixStorage<E> createSparseStorage(List<List<E>> data, Field<E> field) {
        int rows = data.size();
        int cols = rows > 0 ? data.get(0).size() : 0;
        SparseMatrixStorage<E> storage = new SparseMatrixStorage<E>(rows, cols, field.zero());
        E zero = field.zero();
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

    public static <E> org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider<E> getStandardProvider(
            Field<E> field) {
        return getProvider(field);
    }

    /**
     * Determines and creates the optimal storage (Dense or Sparse) based on data
     * density.
     */
    public static <E> MatrixStorage<E> createAutomaticStorage(E[][] data, Field<E> field) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;
        int nonZero = 0;
        int total = rows * cols;
        E zero = field.zero();
        for (E[] row : data) {
            for (E val : row) {
                if (!val.equals(zero))
                    nonZero++;
            }
        }
        if (total > 0 && (double) nonZero / total < 0.2) {
            return createSparseStorage(data, field);
        } else {
            return createDenseStorage(data, field);
        }
    }

    public static <E> MatrixStorage<E> createAutomaticStorage(List<List<E>> data, Field<E> field) {
        int rows = data.size();
        int cols = rows > 0 ? data.get(0).size() : 0;
        int nonZero = 0;
        int total = rows * cols;
        E zero = field.zero();
        for (List<E> row : data) {
            for (E val : row) {
                if (!val.equals(zero))
                    nonZero++;
            }
        }
        if (total > 0 && (double) nonZero / total < 0.2) {
            return createSparseStorage(data, field);
        } else {
            return createDenseStorage(data, field);
        }
    }
}
