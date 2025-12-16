package org.jscience.mathematics.linearalgebra.matrices;

import org.jscience.ComputeContext;
import org.jscience.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.jscience.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage;
import org.jscience.mathematics.structures.rings.Field;

import java.util.List;

/**
 * A sparse matrix implementation.
 * Wrapper around GenericMatrix that enforces SparseMatrixStorage.
 * 
 * @param <E> the type of scalar elements
 */
public class SparseMatrix<E> extends GenericMatrix<E> {

    /**
     * Creates a SparseMatrix with automatic storage optimization.
     */
    public SparseMatrix(E[][] data, Field<E> field) {
        this(MatrixFactory.createSparseStorage(data, field), field);
    }

    public SparseMatrix(List<List<E>> rows, Field<E> field) {
        this(MatrixFactory.createSparseStorage(rows, field), field);
    }

    public SparseMatrix(int rows, int cols, Field<E> field) {
        this(new SparseMatrixStorage<>(rows, cols, field.zero()), field);
    }

    // Internal constructor using storage - Made public for Providers
    public SparseMatrix(MatrixStorage<E> storage, Field<E> field) {
        super(storage, ComputeContext.current().getSparseLinearAlgebraProvider(field), field);
        // Explicit validation
        if (storage instanceof org.jscience.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage
                || storage instanceof org.jscience.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage) { // RealDouble
                                                                                                                         // is
                                                                                                                         // Dense
            throw new IllegalArgumentException("Cannot create SparseMatrix with Dense storage");
        }
    }

    public static <E> SparseMatrix<E> fromDense(List<List<E>> rows, Field<E> field) {
        int r = rows.size();
        int c = r > 0 ? rows.get(0).size() : 0;
        E zero = field.zero();
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
        return new SparseMatrix<>(storage, field);
    }

    public static <E> SparseMatrix<E> zeros(int rows, int cols, Field<E> field) {
        E zero = field.zero();
        return new SparseMatrix<>(new SparseMatrixStorage<>(rows, cols, zero), field);
    }

    public static <E> SparseMatrix<E> identity(int size, Field<E> field) {
        E zero = field.zero();
        SparseMatrixStorage<E> storage = new SparseMatrixStorage<>(size, size, zero);
        E one = field.one();
        for (int i = 0; i < size; i++) {
            storage.set(i, i, one);
        }
        return new SparseMatrix<>(storage, field);
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
