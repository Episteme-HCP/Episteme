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
package org.jscience.mathematics.algebra.algebras;

import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.algebra.Field;

/**
 * A concrete Lie Algebra where elements are square matrices and the bracket
 * is the commutator [A, B] = AB - BA.
 * 
 * @param <E> the type of scalars
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MatrixLieAlgebra<E> implements LieAlgebra<Matrix<E>, E> {

    private final Field<E> field;
    private final int n; // Dimension of the square matrices

    public MatrixLieAlgebra(Field<E> field, int n) {
        this.field = field;
        this.n = n;
    }

    @Override
    public int dimension() {
        return n * n; // Dimension of n x n matrices is n^2
    }

    @Override
    public Field<E> getScalarField() {
        return field;
    }

    @Override
    public Field<E> getScalarRing() {
        return field;
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        // [A, B] = A * B - B * A
        Matrix<E> ab = a.multiply(b);
        Matrix<E> ba = b.multiply(a);
        return ab.subtract(ba);
    }

    @Override
    public Matrix<E> operate(Matrix<E> left, Matrix<E> right) {
        return add(left, right);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return a.add(b);
    }

    @Override
    public Matrix<E> zero() {
        // Return zero matrix of size n x n
        java.util.List<java.util.List<E>> rows = new java.util.ArrayList<>();
        E zero = field.zero();
        for (int i = 0; i < n; i++) {
            java.util.List<E> row = new java.util.ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(zero);
            }
            rows.add(row);
        }
        return org.jscience.mathematics.vector.DenseMatrix.of(rows, field);
    }

    @Override
    public Matrix<E> negate(Matrix<E> element) {
        return element.negate();
    }

    @Override
    public Matrix<E> inverse(Matrix<E> element) {
        return negate(element);
    }

    @Override
    public String description() {
        return "Matrix Lie Algebra (Commutator)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Matrix<E> element) {
        return element != null; // Simplified
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> element) {
        // Scalar multiplication: s * M
        java.util.List<java.util.List<E>> rows = new java.util.ArrayList<>();
        for (int i = 0; i < element.rows(); i++) {
            java.util.List<E> row = new java.util.ArrayList<>();
            for (int j = 0; j < element.cols(); j++) {
                row.add(field.multiply(scalar, element.get(i, j)));
            }
            rows.add(row);
        }
        return org.jscience.mathematics.vector.DenseMatrix.of(rows, field);
    }
}

