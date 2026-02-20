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

import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.technical.algorithm.AlgorithmManager;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage;

import java.util.List;

/**
 * A sparse matrix implementation.
 * Wrapper around GenericMatrix that enforces SparseMatrixStorage.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SparseMatrix<E> extends GenericMatrix<E> {

    /**
     * Creates a SparseMatrix with automatic storage optimization.
     */
    /**
     * Creates a SparseMatrix with automatic storage optimization.
     */
    public SparseMatrix(E[][] data, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        this(createPopulatedStorage(data, ring), ring);
    }

    public SparseMatrix(List<List<E>> rows, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        this(createPopulatedStorage(rows, ring), ring);
    }

    private static <E> MatrixStorage<E> createPopulatedStorage(E[][] data, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        int r = data.length;
        int c = r > 0 ? data[0].length : 0;
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(r, c, ring, 0.0);
        for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) storage.set(i, j, data[i][j]);
        return storage;
    }

    private static <E> MatrixStorage<E> createPopulatedStorage(List<List<E>> rows, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        int r = rows.size();
        int c = r > 0 ? rows.get(0).size() : 0;
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(r, c, ring, 0.0);
        for (int i = 0; i < r; i++) {
            List<E> row = rows.get(i);
            for (int j = 0; j < c; j++) storage.set(i, j, row.get(j));
        }
        return storage;
    }

    public SparseMatrix(int rows, int cols, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        this(new SparseMatrixStorage<>(rows, cols, ring.zero()), ring);
    }

    // Internal constructor using storage - Made public for Providers
    public SparseMatrix(MatrixStorage<E> storage, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        super(storage, AlgorithmManager.getRegistry().selectLinearAlgebraProvider(org.jscience.core.technical.algorithm.OperationContext.DEFAULT, ring), ring);
        // Explicit validation
        if (storage instanceof org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage
                || storage instanceof org.jscience.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage) { // RealDouble is Dense
            throw new IllegalArgumentException("Cannot create SparseMatrix with Dense storage");
        }
    }

    public static <E> SparseMatrix<E> fromDense(List<List<E>> rows, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        int r = rows.size();
        int c = r > 0 ? rows.get(0).size() : 0;
        E zero = ring.zero();
        SparseMatrixStorage<E> storage = new SparseMatrixStorage<>(r, c, zero);
        for (int i = 0; i < r; i++) {
            List<E> row = rows.get(i);
            for (int j = 0; j < c; j++) {
                E val = row.get(j);
                if (!val.equals(zero)) {
                    storage.set(i, j, val);
                }
            }
        }
        return new SparseMatrix<>(storage, ring);
    }

    public static <E> SparseMatrix<E> of(int rows, int cols, Ring<E> ring) {
        return new SparseMatrix<>(AlgorithmManager.getRegistry().createStorage(rows, cols, ring, 0.0), ring);
    }

    public static <E> SparseMatrix<E> of(int rows, int cols, E zero, Ring<E> ring) {
        return new SparseMatrix<>(AlgorithmManager.getRegistry().createStorage(rows, cols, ring, 0.0), ring);
    }

    public static <E> SparseMatrix<E> zeros(int rows, int cols, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        E zero = ring.zero();
        return new SparseMatrix<>(new SparseMatrixStorage<>(rows, cols, zero), ring);
    }

    public static <E> SparseMatrix<E> identity(int size, org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        E zero = ring.zero();
        SparseMatrixStorage<E> storage = new SparseMatrixStorage<>(size, size, zero);
        E one = ring.one();
        for (int i = 0; i < size; i++) {
            storage.set(i, i, one);
        }
        return new SparseMatrix<>(storage, ring);
    }

    public int getNnz() {
        return getSparseStorage().getNnz();
    }

    public SparseMatrixStorage<E> getSparseStorage() {
        return (SparseMatrixStorage<E>) storage;
    }

    public void set(int row, int col, E value) {
        storage.set(row, col, value);
    }

    // Legacy CSR accessors - emulating them from Storage map if needed or
    // deprecating
    // Since Storage is HashMap based, CSR access is expensive/synthetic.
    // If user needs CSR for GPU, we might want to implement a toCSR() method.

    public Object[] getValues() {
        // Reconstruct CSR values
        return getSparseStorage().getValuesCSR();
    }

    public int[] getColIndices() {
        return getSparseStorage().getColIndicesCSR();
    }

    public int[] getRowPointers() {
        return getSparseStorage().getRowPointersCSR();
    }

    @Override
    public String toString() {
        return "SparseMatrix[" + rows() + "x" + cols() + "]";
    }
}



