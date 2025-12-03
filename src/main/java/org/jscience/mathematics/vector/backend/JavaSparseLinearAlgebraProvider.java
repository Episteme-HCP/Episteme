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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.vector.backend;

import org.jscience.mathematics.algebra.Field;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.SparseMatrix;
import java.util.HashMap;
import java.util.Map;

/**
 * Java-based Linear Algebra Provider optimized for Sparse Matrices.
 * <p>
 * Implements efficient sparse matrix operations using compressed storage
 * and algorithms that only process non-zero elements.
 * </p>
 * 
 * @param <E> the type of scalar elements
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JavaSparseLinearAlgebraProvider<E> extends JavaLinearAlgebraProvider<E> {

    public JavaSparseLinearAlgebraProvider(Field<E> field) {
        super(field);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        // Optimize for sparse matrices
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {
            return addSparse((SparseMatrix<E>) a, (SparseMatrix<E>) b);
        }
        return super.add(a, b);
    }

    /**
     * Efficient sparse matrix addition.
     * Only processes non-zero elements.
     */
    private SparseMatrix<E> addSparse(SparseMatrix<E> a, SparseMatrix<E> b) {
        if (a.rows() != b.rows() || a.columns() != b.columns()) {
            throw new IllegalArgumentException(
                    "Matrix dimensions must match: " + a.rows() + "x" + a.columns() +
                            " vs " + b.rows() + "x" + b.columns());
        }

        SparseMatrix<E> result = new SparseMatrix<>(a.rows(), a.columns(), field);

        // Add all non-zero elements from matrix a
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.columns(); j++) {
                E valueA = a.get(i, j);
                if (!isZero(valueA)) {
                    result.set(i, j, valueA);
                }
            }
        }

        // Add non-zero elements from matrix b
        for (int i = 0; i < b.rows(); i++) {
            for (int j = 0; j < b.columns(); j++) {
                E valueB = b.get(i, j);
                if (!isZero(valueB)) {
                    E current = result.get(i, j);
                    E sum = field.add(current, valueB);
                    if (!isZero(sum)) {
                        result.set(i, j, sum);
                    } else {
                        // Remove entry if it becomes zero
                        result.set(i, j, field.zero());
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        // Optimize for sparse matrices
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {
            return multiplySparse((SparseMatrix<E>) a, (SparseMatrix<E>) b);
        }
        return super.multiply(a, b);
    }

    /**
     * Efficient sparse matrix multiplication.
     * Uses row-column dot product, skipping zero elements.
     */
    private SparseMatrix<E> multiplySparse(SparseMatrix<E> a, SparseMatrix<E> b) {
        if (a.columns() != b.rows()) {
            throw new IllegalArgumentException(
                    "Matrix dimensions incompatible for multiplication: " +
                            a.rows() + "x" + a.columns() + " * " + b.rows() + "x" + b.columns());
        }

        SparseMatrix<E> result = new SparseMatrix<>(a.rows(), b.columns(), field);

        // For each row of a
        for (int i = 0; i < a.rows(); i++) {
            // For each column of b
            for (int j = 0; j < b.columns(); j++) {
                E sum = field.zero();

                // Compute dot product of row i of a with column j of b
                for (int k = 0; k < a.columns(); k++) {
                    E aValue = a.get(i, k);
                    E bValue = b.get(k, j);

                    // Skip if either element is zero
                    if (!isZero(aValue) && !isZero(bValue)) {
                        E product = field.multiply(aValue, bValue);
                        sum = field.add(sum, product);
                    }
                }

                // Only store non-zero results
                if (!isZero(sum)) {
                    result.set(i, j, sum);
                }
            }
        }

        return result;
    }

    /**
     * Helper method to check if a value is zero.
     */
    private boolean isZero(E value) {
        return value.equals(field.zero());
    }
}
