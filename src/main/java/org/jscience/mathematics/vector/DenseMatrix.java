/*
 * JScience Reimagined - Unified Scientific Computing Framework
 * Copyright (c) 2025 Silvere Martin-Michiellot
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.vector;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.algebra.Field;
import org.jscience.mathematics.context.ComputeMode;
import org.jscience.mathematics.context.MathContext;
import org.jscience.mathematics.vector.backend.JavaLinearAlgebraProvider;
import org.jscience.mathematics.vector.backend.LinearAlgebraProvider;

/**
 * A dense matrix implementation backed by a list of rows.
 * 
 * @param <E> the type of scalar elements
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DenseMatrix<E> implements Matrix<E> {

    private final List<List<E>> rows;
    private final int rowsCount;
    private final int colsCount;
    private final Field<E> field;

    /**
     * Creates a new DenseMatrix.
     * 
     * @param rows  the rows of the matrix
     * @param field the field structure for the elements
     */
    public DenseMatrix(List<List<E>> rows, Field<E> field) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Matrix cannot be empty");
        }
        this.rows = new ArrayList<>();
        for (List<E> row : rows) {
            this.rows.add(new ArrayList<>(row));
        }
        this.rowsCount = rows.size();
        this.colsCount = rows.get(0).size();
        this.field = field;
    }

    public static <E> DenseMatrix<E> of(List<List<E>> rows, Field<E> field) {
        return new DenseMatrix<>(rows, field);
    }

    private LinearAlgebraProvider<E> getProvider() {
        ComputeMode mode = MathContext.getCurrent().getComputeMode();

        if (mode == ComputeMode.CPU) {
            return new JavaLinearAlgebraProvider<>(field);
        }

        // Check if we can use GPU (must be Real numbers for now)
        boolean canUseGpu = (field.zero() instanceof org.jscience.mathematics.number.Real);

        if (mode == ComputeMode.GPU) {
            if (!canUseGpu) {
                throw new UnsupportedOperationException("GPU mode currently only supports Real numbers");
            }
            return new org.jscience.mathematics.vector.backend.CudaLinearAlgebraProvider<>(field);
        }

        // AUTO mode
        if (canUseGpu) {
            try {
                return new org.jscience.mathematics.vector.backend.CudaLinearAlgebraProvider<>(field);
            } catch (UnsupportedOperationException e) {
                // Fallback to CPU if CUDA not available
                return new JavaLinearAlgebraProvider<>(field);
            }
        }

        return new JavaLinearAlgebraProvider<>(field);
    }

    @Override
    public Matrix<E> getSubMatrix(int rowStart, int rowEnd, int colStart, int colEnd) {
        if (rowStart < 0 || rowEnd > numberOfRows() || rowStart >= rowEnd ||
                colStart < 0 || colEnd > numberOfColumns() || colStart >= colEnd) {
            throw new IndexOutOfBoundsException("Invalid submatrix indices");
        }

        int rows = rowEnd - rowStart;
        int cols = colEnd - colStart;
        List<List<E>> newRows = new ArrayList<>(rows);

        for (int i = rowStart; i < rowEnd; i++) {
            List<E> sourceRow = this.rows.get(i);
            List<E> newRowData = new ArrayList<>(cols);
            for (int j = colStart; j < colEnd; j++) {
                newRowData.add(sourceRow.get(j));
            }
            newRows.add(newRowData);
        }

        return new DenseMatrix<>(newRows, field);
    }

    @Override
    public Vector<E> getRow(int row) {
        if (row < 0 || row >= numberOfRows()) {
            throw new IndexOutOfBoundsException("Row index out of bounds: " + row);
        }
        return new DenseVector<>(rows.get(row), field);
    }

    @Override
    public Vector<E> getColumn(int col) {
        if (col < 0 || col >= numberOfColumns()) {
            throw new IndexOutOfBoundsException("Column index out of bounds: " + col);
        }
        List<E> columnData = new ArrayList<>(numberOfRows());
        for (List<E> row : rows) {
            columnData.add(row.get(col));
        }
        return new DenseVector<>(columnData, field);
    }

    private int numberOfRows() {
        return rowsCount;
    }

    private int numberOfColumns() {
        return colsCount;
    }

    @Override
    public int rows() {
        return rowsCount;
    }

    @Override
    public int cols() {
        return colsCount;
    }

    @Override
    public E get(int row, int col) {
        return rows.get(row).get(col);
    }

    @Override
    public Matrix<E> add(Matrix<E> other) {
        return getProvider().add(this, other);
    }

    public Matrix<E> subtract(Matrix<E> other) {
        if (this.rows() != other.rows() || this.cols() != other.cols()) {
            throw new IllegalArgumentException("Matrix dimensions must match");
        }
        List<List<E>> resultRows = new ArrayList<>();
        for (int i = 0; i < rowsCount; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < colsCount; j++) {
                E negB = field.negate(other.get(i, j));
                row.add(field.add(this.get(i, j), negB));
            }
            resultRows.add(row);
        }
        return new DenseMatrix<>(resultRows, field);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> other) {
        return getProvider().multiply(this, other);
    }

    @Override
    public Matrix<E> operate(Matrix<E> left, Matrix<E> right) {
        return left.add(right);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> left, Matrix<E> right) {
        return left.multiply(right);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector) {
        return getProvider().multiply(this, vector);
    }

    @Override
    public Matrix<E> transpose() {
        List<List<E>> resultRows = new ArrayList<>();
        for (int j = 0; j < colsCount; j++) {
            List<E> row = new ArrayList<>();
            for (int i = 0; i < rowsCount; i++) {
                row.add(this.get(i, j));
            }
            resultRows.add(row);
        }
        return new DenseMatrix<>(resultRows, field);
    }

    @Override
    public E trace() {
        if (rowsCount != colsCount) {
            throw new ArithmeticException("Trace only defined for square matrices");
        }
        E sum = field.zero();
        for (int i = 0; i < rowsCount; i++) {
            sum = field.add(sum, this.get(i, i));
        }
        return sum;
    }

    @Override
    public E determinant() {
        return getProvider().determinant(this);
    }

    @Override
    public Matrix<E> inverse() {
        return getProvider().inverse(this);
    }

    @Override
    public Matrix<E> negate() {
        List<List<E>> resultRows = new ArrayList<>();
        for (int i = 0; i < rowsCount; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < colsCount; j++) {
                row.add(field.negate(this.get(i, j)));
            }
            resultRows.add(row);
        }
        return new DenseMatrix<>(resultRows, field);
    }

    @Override
    public Matrix<E> zero() {
        // Return zero matrix of same size
        List<List<E>> resultRows = new ArrayList<>();
        E zero = field.zero();
        for (int i = 0; i < rowsCount; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < colsCount; j++) {
                row.add(zero);
            }
            resultRows.add(row);
        }
        return new DenseMatrix<>(resultRows, field);
    }

    @Override
    public Matrix<E> one() {
        if (rowsCount != colsCount) {
            throw new UnsupportedOperationException("Identity only defined for square matrices");
        }
        List<List<E>> resultRows = new ArrayList<>();
        E zero = field.zero();
        E one = field.one();
        for (int i = 0; i < rowsCount; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < colsCount; j++) {
                row.add(i == j ? one : zero);
            }
            resultRows.add(row);
        }
        return new DenseMatrix<>(resultRows, field);
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return false; // Matrix multiplication is generally not commutative
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DenseMatrix[").append(rowsCount).append("x").append(colsCount).append("] {\n");
        for (List<E> row : rows) {
            sb.append("  ").append(row).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String description() {
        return "DenseMatrix (" + rowsCount + "x" + colsCount + ")";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Matrix<E> element) {
        return element != null && element.rows() == this.rows() && element.cols() == this.cols();
    }
}
