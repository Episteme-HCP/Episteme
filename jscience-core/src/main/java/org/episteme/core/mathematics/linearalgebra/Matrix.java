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

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.structures.spaces.Module;
import org.episteme.core.mathematics.structures.rings.Ring;

import java.util.List;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.TriangularMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.TridiagonalMatrixStorage;
// DiagonalMatrixStorage removed
// TensorFactory removed

/**
 * Represents a matrix of scalar elements.
 * <p>
 * A matrix is a rectangular array of numbers, symbols, or expressions, arranged
 * in rows and columns. Square matrices form a Ring.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Matrix<E> extends Ring<Matrix<E>>, Module<Matrix<E>, E> {

    /**
     * Creates a matrix from a 2D array.
     */
    @SuppressWarnings({"unchecked", "preview", "restricted"})
    static <E> Matrix<E> of(E[][] data, Ring<E> ring) {
        int rows = data.length;
        if (rows == 0) return new GenericMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(0, 0, ring.zero()), null, ring);
        int cols = data[0].length;

        // Density heuristic
        int nz = 0;
        E zero = ring.zero();
        for (E[] row : data) for (E val : row) if (val != null && !val.equals(zero)) nz++;
        double density = (rows * cols > 0) ? (double) nz / (rows * cols) : 1.0;

        org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(rows, cols, ring, density);
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) storage.set(i, j, data[i][j] == null ? zero : data[i][j]);

        LinearAlgebraProvider<E> provider = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        if (ring instanceof Reals && storage instanceof org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage) {
            return (Matrix<E>) (Matrix<?>) RealDoubleMatrix.of((org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage) storage);
        }
        return new GenericMatrix<>(storage, provider, ring);
    }

    /**
     * Creates a matrix from a 2D list.
     */
    static <E> Matrix<E> of(List<List<E>> data, Ring<E> ring) {
        int rows = data.size();
        int cols = rows > 0 ? data.get(0).size() : 0;
        
        int nz = 0;
        E zero = ring.zero();
        for (List<E> row : data) for (E val : row) if (val != null && !val.equals(zero)) nz++;
        double density = (rows * cols > 0) ? (double) nz / (rows * cols) : 1.0;
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(rows, cols, ring, density);
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) storage.set(i, j, data.get(i).get(j) == null ? zero : data.get(i).get(j));
        
        LinearAlgebraProvider<E> provider = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        return new GenericMatrix<>(storage, provider, ring);
    }

    /**
     * Creates a triangular matrix from a 2D list.
     */
    static <E> Matrix<E> triangular(List<List<E>> data, Ring<E> ring) {
        int rows = data.size();
        TriangularMatrixStorage<E> storage = new TriangularMatrixStorage<E>(rows, false, ring.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= i && j < rows; j++) {
                E val = data.get(i).get(j);
                storage.set(i, j, val == null ? ring.zero() : val);
            }
        }
        LinearAlgebraProvider<E> provider = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        return new GenericMatrix<>(storage, provider, ring);
    }

    /**
     * Creates a tridiagonal matrix from a 2D list.
     */
    static <E> Matrix<E> tridiagonal(List<List<E>> data, Ring<E> ring) {
        int rows = data.size();
        TridiagonalMatrixStorage<E> storage = new TridiagonalMatrixStorage<E>(rows, ring.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = Math.max(0, i - 1); j <= Math.min(rows - 1, i + 1); j++) {
                E val = data.get(i).get(j);
                storage.set(i, j, val == null ? ring.zero() : val);
            }
        }
        LinearAlgebraProvider<E> provider = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        return new GenericMatrix<>(storage, provider, ring);
    }

    /**
     * Creates an identity matrix.
     */
    static <E> Matrix<E> identity(int size, Ring<E> ring) {
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(size, size, ring, 0.0);
        E one = ring.one();
        for (int i = 0; i < size; i++) storage.set(i, i, one);
        
        LinearAlgebraProvider<E> provider = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        return new GenericMatrix<>(storage, provider, ring);
    }

    /**
     * Creates a matrix of zeros.
     */
    static <E> Matrix<E> zeros(int rows, int cols, Ring<E> ring) {
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(rows, cols, ring, 1.0);
        LinearAlgebraProvider<E> provider = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        return new GenericMatrix<>(storage, provider, ring);
    }

    /**
     * Returns the number of rows in this matrix.
     */
    int rows();

    /**
     * Returns the number of columns in this matrix.
     */
    int cols();

    /**
     * Returns the element at the specified row and column.
     */
    E get(int row, int col);

    /**
     * Returns the underlying storage of this matrix.
     * 
     * @return the matrix storage
     */
    org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<E> getStorage();

    /**
     * Returns the sum of this matrix and another.
     */
    Matrix<E> add(Matrix<E> other);

    /**
     * Returns the difference of this matrix and another.
     */
    default Matrix<E> subtract(Matrix<E> other) {
        return this.add(other.negate());
    }

    /**
     * Returns the product of this matrix and another.
     */
    Matrix<E> multiply(Matrix<E> other);

    /**
     * Returns the transpose of this matrix.
     */
    Matrix<E> transpose();

    /**
     * Returns the trace of this matrix (sum of diagonal elements).
     */
    E trace();

    /**
     * Returns a submatrix of this matrix.
     */
    Matrix<E> getSubMatrix(int rowStart, int rowEnd, int colStart, int colEnd);

    /**
     * Returns the row at the specified index as a vector.
     */
    Vector<E> getRow(int row);

    /**
     * Returns the column at the specified index as a vector.
     */
    Vector<E> getColumn(int col);

    /**
     * Returns the determinant of this matrix.
     */
    E determinant();

    /**
     * Returns the multiplicative inverse of this matrix.
     */
    Matrix<E> inverse();

    /**
     * Multiplies this matrix by a vector.
     */
    Vector<E> multiply(Vector<E> vector);

    /**
     * Returns the negation of this matrix (-this).
     */
    Matrix<E> negate();

    /**
     * Returns the zero matrix of the same dimensions.
     */
    Matrix<E> zero();


    // --- Default implementations for Ring/Module/Group interfaces ---

    @Override
    default boolean isMultiplicationCommutative() {
        return false;
    }

    @Override
    default boolean isCommutative() {
        return true;
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default Matrix<E> inverse(Matrix<E> element) {
        return element.negate();
    }

    @Override
    default Matrix<E> identity() {
        return zero();
    }

    @Override
    default Matrix<E> operate(Matrix<E> left, Matrix<E> right) {
        return left.multiply(right);
    }

    @Override
    default Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return a.add(b);
    }

    @Override
    default Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        return a.multiply(b);
    }

    // Module methods - must be implemented by concrete classes
    @Override
    Ring<E> getScalarRing();

    @Override
    Matrix<E> scale(E scalar, Matrix<E> element);

    /**
     * Converts this matrix to a rank-2 tensor.
     * 
     * @return the corresponding tensor
     */
    default Tensor<E> toTensor() {
        if (rows() == 0 || cols() == 0) {
            @SuppressWarnings("unchecked")
            Class<E> type = (Class<E>) getScalarRing().zero().getClass();
            return Tensor.zeros(type, rows(), cols());
        }

        E sample = get(0, 0);
        @SuppressWarnings("unchecked")
        Class<E> type = (Class<E>) sample.getClass();

        Tensor<E> t = Tensor.zeros(type, rows(), cols());

        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < cols(); j++) {
                t.set(get(i, j), i, j);
            }
        }
        return t;
    }
}


