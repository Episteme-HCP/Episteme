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

/**
 * Linear Algebra Provider optimized for Sparse Matrices.
 * <p>
 * Currently extends JavaLinearAlgebraProvider as a baseline, but intended
 * to override operations with sparse-efficient algorithms.
 * </p>
 * 
 * @param <E> the type of scalar elements
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class SparseLinearAlgebraProvider<E> extends JavaLinearAlgebraProvider<E> {

    public SparseLinearAlgebraProvider(Field<E> field) {
        super(field);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        // TODO: Implement sparse addition
        // If both are SparseMatrix, we can iterate over non-zero elements only
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {
            // Optimization placeholder
            return super.add(a, b);
        }
        return super.add(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        // TODO: Implement sparse multiplication
        return super.multiply(a, b);
    }
}

