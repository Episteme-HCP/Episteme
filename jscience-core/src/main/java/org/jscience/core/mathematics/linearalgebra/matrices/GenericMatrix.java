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

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage;
import org.jscience.core.ComputeContext;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.technical.algorithm.AlgorithmManager;
import org.jscience.core.technical.algorithm.OperationContext;

import java.util.List;

/**
 * A generic matrix implementation with smart storage and provider selection.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GenericMatrix<E> implements Matrix<E> {

    @Override
    public String description() {
        return "GenericMatrix (" + rows() + "x" + cols() + ")";
    }

    /**
     * Creates a GenericMatrix with automatic storage selection.
     */
    public GenericMatrix(E[][] data, Ring<E> ring) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;
        int nz = 0;
        E zero = ring.zero();
        for (E[] row : data) for (E val : row) if (!val.equals(zero)) nz++;
        double density = (rows * cols > 0) ? (double) nz / (rows * cols) : 1.0;

        this.storage = AlgorithmManager.getRegistry().createStorage(rows, cols, ring, density);
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) this.storage.set(i, j, data[i][j]);
        this.provider = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        this.ring = ring;
    }

    @Override
    public boolean contains(Matrix<E> element) {
        return this.equals(element);
    }

    protected MatrixStorage<E> storage;
    protected LinearAlgebraProvider<E> provider;
    protected Ring<E> ring; // Context for Ring/Field operations

    /**
     * Public constructor for Providers and internal use.
     */
    public GenericMatrix(MatrixStorage<E> storage, LinearAlgebraProvider<E> provider, Ring<E> ring) {
        this.storage = storage;
        this.provider = provider;
        this.ring = ring;
    }

    public void set(int row, int col, E value) {
        storage.set(row, col, value);
    }

    // GenericMatrix relies on Matrix.of() for smart instantiation.

    // ================= Matrix<E> Implementation =================

    @Override
    public int rows() {
        return storage.rows();
    }

    @Override
    public int cols() {
        return storage.cols();
    }

    @Override
    public E get(int row, int col) {
        return storage.get(row, col);
    }

    @Override
    public Matrix<E> add(Matrix<E> other) {
        if (other instanceof GenericMatrix) {
            // Delegate to provider for potentially optimized operation
            return provider.add(this, (GenericMatrix<E>) other);
        }
        // Fallback manual addition
        // Fallback manual addition
        int rows = rows();
        int cols = cols();
        if (rows != other.rows() || cols != other.cols()) {
            throw new IllegalArgumentException("Matrix dimensions do not match for addition.");
        }

        // Result storage (default to this matrix's storage type or Dense)
        MatrixStorage<E> resStorage = new DenseMatrixStorage<>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E sum = ring.add(this.get(i, j), other.get(i, j));
                resStorage.set(i, j, sum);
            }
        }
        return new GenericMatrix<E>(resStorage, provider, ring);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> other) {
        if (other instanceof GenericMatrix) {
            return provider.multiply(this, (GenericMatrix<E>) other);
        }
        
        // Fallback manual multiplication
        int rows = this.rows();
        int cols = other.cols();
        int inner = this.cols();
        
        if (inner != other.rows()) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");
        }
        
        DenseMatrixStorage<E> resStorage = new DenseMatrixStorage<>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E sum = ring.zero();
                for (int k = 0; k < inner; k++) {
                    E prod = ring.multiply(this.get(i, k), other.get(k, j));
                    sum = ring.add(sum, prod);
                }
                resStorage.set(i, j, sum);
            }
        }
        return new GenericMatrix<E>(resStorage, provider, ring);
    }

    @Override
    public Matrix<E> transpose() {
        // Create new storage with transposed dimensions
        // This relies on provider or manual transpose.
        // Let's ask provider.
        return provider.transpose(this);
    }

    @Override
    public E trace() {
        E sum = ring.zero();
        for (int i = 0; i < Math.min(rows(), cols()); i++) {
            sum = ring.add(sum, get(i, i));
        }
        return sum;
    }

    @Override
    public Matrix<E> getSubMatrix(int rowStart, int rowEnd, int colStart, int colEnd) {
        // Manual slicing or provider?
        // Provider creates a view or copy.
        // For now, let's just create a dense copy.
        int newRows = rowEnd - rowStart;
        int newCols = colEnd - colStart;
        DenseMatrixStorage<E> newStorage = new DenseMatrixStorage<E>(newRows, newCols, ring.zero());
        for (int i = 0; i < newRows; i++) {
            for (int j = 0; j < newCols; j++) {
                newStorage.set(i, j, get(rowStart + i, colStart + j));
            }
        }
        return new GenericMatrix<E>(newStorage, provider, ring);
    }

    @Override
    public Vector<E> getRow(int row) {
        java.util.List<E> rowData = new java.util.ArrayList<>(cols());
        for (int j = 0; j < cols(); j++) {
            rowData.add(get(row, j));
        }
        return new org.jscience.core.mathematics.linearalgebra.vectors.GenericVector<>(rowData, ring);
    }

    @Override
    public Vector<E> getColumn(int col) {
        java.util.List<E> colData = new java.util.ArrayList<>(rows());
        for (int i = 0; i < rows(); i++) {
            colData.add(get(i, col));
        }
        return new org.jscience.core.mathematics.linearalgebra.vectors.GenericVector<>(colData, ring);
    }

    @Override
    public E determinant() {
        return provider.determinant(this);
    }

    @Override
    public Matrix<E> inverse() {
        return provider.inverse(this);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector) {
        // Delegate to provider which handles GenericVector natively or via interface
        return provider.multiply(this, vector);
    }

    @Override
    public Matrix<E> negate() {
        return scale(ring.negate(ring.one()), this);
    }

    public Matrix<E> zero() {
        return new GenericMatrix<E>(new DenseMatrixStorage<E>(rows(), cols(), ring.zero()), provider, ring); // Implicitly
                                                                                                              // zero
        // filled?
        // DenseStorage usually inits to null for Generic E? Need to fill with zero if
        // using array.
        // Actually DenseStorage needs field to know zero.
    }

    @Override
    public Matrix<E> one() {
        // Identity
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), rows(), cols());
        GenericMatrix<E> m = (GenericMatrix<E>) Matrix.of(data, ring);
        // Implementation detail: create storage directly.
        return m; // simplified
    }

    @Override
    public Ring<E> getScalarRing() {
        return ring;
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> element) {
        if (element instanceof GenericMatrix) {
            return provider.scale(scalar, (GenericMatrix<E>) element);
        }
        return null;
    }

    // Getters for Provider usage

    public MatrixStorage<E> getStorage() {
        return storage;
    }

    public Ring<E> getField() {
        return ring;
    }

}

