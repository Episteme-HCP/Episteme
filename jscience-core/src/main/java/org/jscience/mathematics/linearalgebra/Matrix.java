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

package org.jscience.mathematics.linearalgebra;

import org.jscience.mathematics.structures.spaces.Module;
import org.jscience.mathematics.structures.rings.Ring;

import org.jscience.mathematics.linearalgebra.matrices.storage.MatrixStorage;

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

    /**
     * Returns the identity matrix (for square matrices).
     */
    Matrix<E> one();

    /**
     * Returns the underlying storage of this matrix.
     */
    MatrixStorage<E> getStorage();

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
    default org.jscience.mathematics.linearalgebra.tensors.Tensor<E> toTensor() {
        // Basic default implementation
        // This is inefficient but functional for general case.
        // Specific implementations (DenseMatrix, etc.) should override this.
        if (rows() == 0 || cols() == 0) {
            throw new UnsupportedOperationException(
                    "Cannot convert empty matrix to tensor (type inference limitation)");
        }

        // We need class of E. Hack: get(0,0)
        E sample = get(0, 0);
        @SuppressWarnings("unchecked")
        Class<E> type = (Class<E>) sample.getClass();

        org.jscience.mathematics.linearalgebra.tensors.Tensor<E> t = org.jscience.mathematics.linearalgebra.tensors.TensorFactory
                .zeros(type, rows(), cols());

        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < cols(); j++) {
                t.set(get(i, j), i, j);
            }
        }
        return t;
    }
}

