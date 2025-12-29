/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.sets;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.rings.Ring;

/**
 * The structure of n×n square matrices over a field, forming a ring.
 * <p>
 * For a given field F and dimension n, the set of n×n matrices forms a ring
 * under matrix addition and multiplication. Unlike scalar rings, matrix
 * multiplication is generally <strong>not commutative</strong>.
 * </p>
 * 
 * <h2>Mathematical Properties</h2>
 * <ul>
 * <li>Addition: closed, associative, commutative, with zero matrix</li>
 * <li>Multiplication: closed, associative, with identity matrix</li>
 * <li>Distributive: A(B + C) = AB + AC</li>
 * <li>Non-commutative: AB ≠ BA in general</li>
 * </ul>
 * 
 * @param <E> the type of scalar elements in the field
 * 
 * @see Matrix
 * @see DenseMatrix * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class SquareMatrices<E> implements Ring<Matrix<E>> {

    private final Field<E> scalarField;
    private final int dimension;

    private SquareMatrices(Field<E> scalarField, int dimension) {
        if (dimension <= 0) {
            throw new IllegalArgumentException("Matrix dimension must be positive");
        }
        this.scalarField = scalarField;
        this.dimension = dimension;
    }

    /**
     * Returns the SquareMatrices structure for n×n matrices over a field.
     */
    public static <E> SquareMatrices<E> of(Field<E> scalarField, int dimension) {
        return new SquareMatrices<>(scalarField, dimension);
    }

    public int getDimension() {
        return dimension;
    }

    public Field<E> getScalarField() {
        return scalarField;
    }

    // --- Ring Implementation ---

    @Override
    public Matrix<E> operate(Matrix<E> a, Matrix<E> b) {
        return add(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        validateDimensions(a);
        validateDimensions(b);
        return a.add(b);
    }

    @Override
    public Matrix<E> zero() {
        return createZeroMatrix();
    }

    @Override
    public Matrix<E> negate(Matrix<E> element) {
        validateDimensions(element);
        return element.negate();
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        validateDimensions(a);
        validateDimensions(b);
        return a.multiply(b);
    }

    @Override
    public Matrix<E> one() {
        return createIdentityMatrix();
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return dimension == 1;
    }

    @Override
    public Matrix<E> inverse(Matrix<E> element) {
        validateDimensions(element);
        return element.inverse();
    }

    // --- Set Implementation ---

    @Override
    public boolean contains(Matrix<E> element) {
        return element != null && element.rows() == dimension && element.cols() == dimension;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "M_" + dimension + "(" + scalarField.description() + ")";
    }

    // --- Helpers ---

    private void validateDimensions(Matrix<E> m) {
        if (m.rows() != dimension || m.cols() != dimension) {
            throw new IllegalArgumentException("Matrix must be " + dimension + "×" + dimension);
        }
    }

    private Matrix<E> createZeroMatrix() {
        @SuppressWarnings("unchecked")
        E[] data = (E[]) new Object[dimension * dimension];
        E zero = scalarField.zero();
        for (int i = 0; i < data.length; i++) {
            data[i] = zero;
        }
        return new DenseMatrix<E>(data, dimension, dimension, scalarField);
    }

    private Matrix<E> createIdentityMatrix() {
        @SuppressWarnings("unchecked")
        E[] data = (E[]) new Object[dimension * dimension];
        E zero = scalarField.zero();
        E one = scalarField.one();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i * dimension + j] = (i == j) ? one : zero;
            }
        }
        return new DenseMatrix<E>(data, dimension, dimension, scalarField);
    }

    @Override
    public String toString() {
        return "SquareMatrices(" + dimension + "×" + dimension + " over " + scalarField + ")";
    }
}