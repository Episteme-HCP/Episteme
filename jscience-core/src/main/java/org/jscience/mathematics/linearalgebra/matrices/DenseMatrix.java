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

import org.jscience.ComputeContext;
import org.jscience.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.jscience.mathematics.structures.rings.Field;

import java.util.List;

/**
 * A dense matrix implementation.
 * Wrapper around GenericMatrix that enforces DenseMatrixStorage.
 * 
 * @param <E> the type of scalar elements * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class DenseMatrix<E> extends GenericMatrix<E> {

    /**
     * Creates a DenseMatrix with automatic storage optimization.
     */
    public DenseMatrix(E[][] data, Field<E> field) {
        this(MatrixFactory.createDenseStorage(data, field), field);
    }

    /**
     * Creates a DenseMatrix with automatic storage optimization.
     */
    public DenseMatrix(List<List<E>> rows, Field<E> field) {
        this(MatrixFactory.createDenseStorage(rows, field), field);
    }

    public DenseMatrix(E[] flatData, int rows, int cols, Field<E> field) {
        this(new DenseMatrixStorage<>(rows, cols, flatData), field);
    }

    // Internal constructor
    protected DenseMatrix(MatrixStorage<E> storage, Field<E> field) {
        super(storage, ComputeContext.current().getDenseLinearAlgebraProvider(field), field);
        // Explicit validation: Ensure storage is intended for Dense usage
        // We accept DenseMatrixStorage OR RealDoubleMatrixStorage (which is Dense
        // optimized)
        // We reject SparseMatrixStorage
        if (storage instanceof org.jscience.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage) {
            throw new IllegalArgumentException("Cannot create DenseMatrix with Sparse storage");
        }
    }

    public Object[] getRawData() {
        // Construct raw data from storage if needed or if storage has it
        // DenseMatrixStorage uses 1D array internally, assuming it exposes access or we
        // copy
        // For now, implementing copy
        int rows = rows();
        int cols = cols();
        Object[] data = new Object[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = get(i, j);
            }
        }
        return data;
    }

    // Legacy support for toDoubleArray (used for GPU/JNI usually)
    public double[] toDoubleArray() {
        int rows = rows();
        int cols = cols();
        double[] result = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = get(i, j);
                int idx = i * cols + j;
                if (val instanceof org.jscience.mathematics.numbers.real.Real) {
                    result[idx] = ((org.jscience.mathematics.numbers.real.Real) val).doubleValue();
                } else if (val instanceof Number) {
                    result[idx] = ((Number) val).doubleValue();
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DenseMatrix[").append(rows()).append("x").append(cols()).append("]");
        return sb.toString();
    }
}
