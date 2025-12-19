package org.jscience.mathematics.linearalgebra.matrices;

import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;
import org.jscience.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.jscience.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage;
import org.jscience.ComputeContext;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.rings.Ring;

import java.util.List;

/**
 * A generic matrix implementation with smart storage and provider selection.
 * 
 * @param <E> the element type
 */
public class GenericMatrix<E> implements Matrix<E> {

    @Override
    public String description() {
        return "GenericMatrix (" + rows() + "x" + cols() + ")";
    }

    /**
     * Creates a GenericMatrix with automatic storage selection.
     */
    public GenericMatrix(E[][] data, Field<E> field) {
        this(MatrixFactory.create(data, field).getStorage(),
                MatrixFactory.getStandardProvider(field),
                field);
    }

    @Override
    public boolean contains(Matrix<E> element) {
        return this.equals(element);
    }

    protected MatrixStorage<E> storage;
    protected LinearAlgebraProvider<E> provider;
    protected Field<E> field; // Context for Ring/Field operations

    /**
     * Public constructor for Providers and internal use.
     */
    public GenericMatrix(MatrixStorage<E> storage, LinearAlgebraProvider<E> provider, Field<E> field) {
        this.storage = storage;
        this.provider = provider;
        this.field = field;
    }

    // ================= Factory Methods (Replacing MatrixFactory) =================

    /**
     * Creates a matrix from a 2D array, automatically selecting storage and
     * provider.
     */
    public static <E> GenericMatrix<E> of(E[][] data, Field<E> field) {
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;

        // Smart Selection Logic
        MatrixStorage<E> storage;
        if (isSparse(data, field)) {
            storage = new SparseMatrixStorage<>(rows, cols, field.zero());
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (!data[i][j].equals(field.zero())) {
                        storage.set(i, j, data[i][j]);
                    }
                }
            }
        } else {
            // Default to Dense
            storage = new DenseMatrixStorage<E>(rows, cols, field.zero());
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    storage.set(i, j, data[i][j]);
                }
            }
        }

        // Provider Selection (could be enhanced with system properties)
        LinearAlgebraProvider<E> provider = ComputeContext.current().getDenseLinearAlgebraProvider(field);

        return new GenericMatrix<>(storage, provider, field);
    }

    /**
     * Creates a matrix from a List of Lists, automatically selecting storage and
     * provider.
     */
    public static <T> GenericMatrix<T> of(List<List<T>> data, Field<T> field) {
        Matrix<T> m = MatrixFactory.create(data, field);
        if (m instanceof GenericMatrix) {
            return (GenericMatrix<T>) m;
        }
        // If MatrixFactory returns something else, wrap it (or extract storage if
        // possible)
        return new GenericMatrix<>(m.getStorage(), MatrixFactory.getStandardProvider(field), field);
    }

    private static <T> boolean isSparse(T[][] data, Field<T> field) {
        // Simple heuristic: > 70% zeros
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;
        if (rows * cols == 0)
            return false;

        int zeros = 0;
        T zero = field.zero();
        // int sampleSize = Math.min(rows * cols, 100);

        for (T[] row : data) {
            for (T val : row) {
                if (val.equals(zero))
                    zeros++;
            }
        }
        return (double) zeros / (rows * cols) > 0.7;
    }

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
        MatrixStorage<E> resStorage = new DenseMatrixStorage<>(rows, cols, field.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E sum = field.add(this.get(i, j), other.get(i, j));
                resStorage.set(i, j, sum);
            }
        }
        return new GenericMatrix<>(resStorage, provider, field);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> other) {
        if (other instanceof GenericMatrix) {
            return provider.multiply(this, (GenericMatrix<E>) other);
        }
        throw new UnsupportedOperationException("Multiplication with non-GenericMatrix not supported");
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
        E sum = field.zero();
        for (int i = 0; i < Math.min(rows(), cols()); i++) {
            sum = field.add(sum, get(i, i));
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
        DenseMatrixStorage<E> newStorage = new DenseMatrixStorage<E>(newRows, newCols, field.zero());
        for (int i = 0; i < newRows; i++) {
            for (int j = 0; j < newCols; j++) {
                newStorage.set(i, j, get(rowStart + i, colStart + j));
            }
        }
        return new GenericMatrix<>(newStorage, provider, field);
    }

    @Override
    public Vector<E> getRow(int row) {
        // Return GenericVector
        // Implementation needed
        return null; // Placeholder
    }

    @Override
    public Vector<E> getColumn(int col) {
        return null; // Placeholder
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
        // provider.scale(this, -1);
        return scale(field.negate(field.one()), this);
    }

    public Matrix<E> zero() {
        return new GenericMatrix<>(new DenseMatrixStorage<E>(rows(), cols(), field.zero()), provider, field); // Implicitly
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
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), rows(), cols());
        GenericMatrix<E> m = of(data, field);
        // Implementation detail: create storage directly.
        return m; // simplified
    }

    @Override
    public Ring<E> getScalarRing() {
        return field;
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

    public Field<E> getField() {
        return field;
    }

}
