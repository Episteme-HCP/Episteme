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

package org.jscience.mathematics.algebraic.matrices;

import org.jscience.JScience;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;

/**
 * The IntegerSparseMatrix class provides an object for encapsulating sparse matrices.
 * Uses compressed row storage.
 *
 * @author Mark Hale
 * @version 1.2
 */

public class IntegerSparseMatrix extends AbstractIntegerMatrix implements Cloneable, Serializable {
    /**
     * Matrix elements.
     */
    private int elements[];
    /**
     * Sparse indexing data.
     * Contains the column positions of each element,
     * e.g. <code>colPos[n]</code> is the column position
     * of the <code>n</code>th element.
     */
    private int colPos[];
    /**
     * Sparse indexing data.
     * Contains the indices of the start of each row,
     * e.g. <code>rows[i]</code> is the index
     * where the <code>i</code>th row starts.
     */
    private int rows[];

    /**
     * Constructs an empty matrix.
     *
     * @param rowCount the number of rows
     * @param colCount the number of columns
     */
    public IntegerSparseMatrix(final int rowCount, final int colCount) {
        super(rowCount, colCount);
        elements = new int[0];
        colPos = new int[0];
        rows = new int[numRows() + 1];
    }

    /**
     * Constructs a matrix from an array.
     *
     * @param array an assigned value
     */
    public IntegerSparseMatrix(final int array[][]) {
        super(array.length, array[0].length);
        rows = new int[numRows() + 1];
        int n = 0;
        for (int i = 0; i < numRows(); i++) {
            if (array[i].length != array.length)
                throw new IllegalDimensionException("Array is not square.");
            for (int j = 0; j < numColumns(); j++) {
                if (array[i][j] != 0)
                    n++;
            }
        }
        elements = new int[n];
        colPos = new int[n];
        n = 0;
        for (int i = 0; i < numRows(); i++) {
            rows[i] = n;
            for (int j = 0; j < numColumns(); j++) {
                if (array[i][j] != 0) {
                    elements[n] = array[i][j];
                    colPos[n] = j;
                    n++;
                }
            }
        }
        rows[numRows()] = n;
    }

    /**
     * Copy constructor.
     *
     * @param mat an assigned value
     */
    public IntegerSparseMatrix(final IntegerSparseMatrix mat) {
        this(mat.numRows(), mat.numColumns());
        System.arraycopy(mat.elements, 0, elements, 0, mat.elements.length);
        System.arraycopy(mat.colPos, 0, colPos, 0, mat.colPos.length);
        System.arraycopy(mat.rows, 0, rows, 0, mat.rows.length);
    }

    /**
     * Compares two integer sparse matrices for equality.
     *
     * @param m a integer matrix
     */
    /**public boolean equals(AbstractIntegerMatrix m) {
     if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
     if (m instanceof IntegerSparseMatrix) {
     return this.equals((IntegerSparseMatrix) m);
     } else {
     int i = 0;
     while (i < numRows()) {
     int j = 0;
     while (j < numColumns()) {
     if (getPrimitiveElement(i, j)!=m.getPrimitiveElement(i, j)) {
     return false;
     }
     j++;
     }
     i++;
     }
     return true;
     }
     } else
     return false;
     }*/

    /**
     * Compares two double sparse matrices for equality.
     *
     * @param m a integer matrix
     */
    public boolean equals(AbstractIntegerMatrix m, double tol) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            if (m instanceof IntegerSparseMatrix) {
                return this.equals((IntegerSparseMatrix) m);
            } else {
                double sumSqr = 0;
                for (int i = 0; i < numRows(); i++) {
                    for (int j = 0; j < numColumns(); j++) {
                        double delta = getPrimitiveElement(i, j) - m.getPrimitiveElement(i, j);
                        sumSqr += delta * delta;
                    }
                }
                return (sumSqr <= tol * tol);
            }
        } else
            return false;
    }

    public boolean equals(IntegerSparseMatrix m) {
        return equals(m, java.lang.Double.valueOf(JScience.getProperty("tolerance")).doubleValue());
    }

    public boolean equals(IntegerSparseMatrix m, double tol) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            if (colPos.length != m.colPos.length)
                return false;
            for (int i = 0; i < rows.length; i++) {
                if (rows[i] != m.rows[i])
                    return false;
            }
            double sumSqr = 0.0;
            for (int i = 0; i < colPos.length; i++) {
                if (colPos[i] != m.colPos[i])
                    return false;
                double delta = elements[i] - m.elements[i];
                sumSqr += delta * delta;
            }
            return (sumSqr <= tol * tol);
        } else
            return false;
    }

    /**
     * Returns a string representing this matrix.
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(numRows() * numColumns());
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                buf.append(getPrimitiveElement(i, j));
                buf.append(' ');
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    /**
     * Converts this matrix to an double matrix.
     *
     * @return an double matrix
     */
    public AbstractDoubleMatrix toDoubleMatrix() {
        final double ans[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                ans[i][j] = getPrimitiveElement(i, j);
        }
        return new DoubleMatrix(ans);
    }

    /**
     * Converts this matrix to a complex matrix.
     *
     * @return a complex matrix
     */
    public AbstractComplexMatrix toComplexMatrix() {
        final double arrayRe[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                arrayRe[i][j] = getPrimitiveElement(i, j);
        }
        return new ComplexMatrix(arrayRe, new double[numRows()][numColumns()]);
    }

    /**
     * Returns an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public int getPrimitiveElement(final int i, final int j) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns()) {
            for (int k = rows[i]; k < rows[i + 1]; k++) {
                if (colPos[k] == j)
                    return elements[k];
            }
            return 0;
        } else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    /**
     * Sets the value of an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @param x a number
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public void setElement(final int i, final int j, final int x) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns()) {
            if (x == 0)
                return;
            for (int k = rows[i]; k < rows[i + 1]; k++) {
                if (colPos[k] == j) {
                    elements[k] = x;
                    return;
                }
            }
            final int oldMatrix[] = elements;
            final int oldColPos[] = colPos;
            elements = new int[oldMatrix.length + 1];
            colPos = new int[oldColPos.length + 1];
            System.arraycopy(oldMatrix, 0, elements, 0, rows[i]);
            System.arraycopy(oldColPos, 0, colPos, 0, rows[i]);
            int k;
            for (k = rows[i]; k < rows[i + 1] && oldColPos[k] < j; k++) {
                elements[k] = oldMatrix[k];
                colPos[k] = oldColPos[k];
            }
            elements[k] = x;
            colPos[k] = j;
            System.arraycopy(oldMatrix, k, elements, k + 1, oldMatrix.length - k);
            System.arraycopy(oldColPos, k, colPos, k + 1, oldColPos.length - k);
            for (k = i + 1; k < rows.length; k++)
                rows[k]++;
        } else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    /**
     * Sets the value of all elements of the matrix. You should think about using a IntegerSquareMatrix.
     *
     * @param x a int element
     */
    public void setAllElements(final int x) {
        if (x == 0) {
            elements = new int[0];
            colPos = new int[0];
            rows = new int[numRows() + 1];
        } else {
            elements = new int[numRows() * numColumns()];
        }
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                elements[i * numColumns() + j] = x;
                colPos[i * numColumns() + j] = j;
            }
            rows[i + 1] = i * numColumns();
        }
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
     */
    public int infNorm() {
        if (numElements() > 0) {
            int result = 0, tmpResult;
            for (int i = 0; i < numRows(); i++) {
                tmpResult = 0;
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    tmpResult += Math.abs(elements[j]);
                if (tmpResult > result)
                    result = tmpResult;
            }
            return result;
        } else
            throw new ArithmeticException("The inf norm of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the Frobenius (l<sup>2</sup>) norm.
     */
    public double frobeniusNorm() {
        if (numElements() > 0) {
            double result = 0.0;
            for (int i = 0; i < colPos.length; i++)
                result += elements[i] * elements[i];
            return Math.sqrt(result);
        } else
            throw new ArithmeticException("The frobenius norm of a zero dimension matrix is undefined.");
    }

//============
// OPERATIONS
//============

// ADDITION

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a integer matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractIntegerMatrix add(final AbstractIntegerMatrix m) {
        if (m instanceof IntegerSparseMatrix)
            return add((IntegerSparseMatrix) m);
        if (m instanceof IntegerMatrix)
            return add((IntegerMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];
                array[i][0] += m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] += m.getPrimitiveElement(i, j);
            }
            return new IntegerMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    public IntegerMatrix add(final IntegerMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];
                array[i][0] += m.matrix[i][0];
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] += m.matrix[i][j];
            }
            return new IntegerMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a integer sparse matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public IntegerSparseMatrix add(final IntegerSparseMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            IntegerSparseMatrix ans = new IntegerSparseMatrix(numRows(), numColumns());
            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++)
                    ans.setElement(i, j, getPrimitiveElement(i, j) + m.getPrimitiveElement(i, j));
            }
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m a integer matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractIntegerMatrix subtract(final AbstractIntegerMatrix m) {
        if (m instanceof IntegerSparseMatrix)
            return subtract((IntegerSparseMatrix) m);
        if (m instanceof IntegerMatrix)
            return subtract((IntegerMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];
                array[i][0] -= m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] -= m.getPrimitiveElement(i, j);
            }
            return new IntegerMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    public IntegerMatrix subtract(final IntegerMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];
                array[i][0] -= m.matrix[i][0];
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] -= m.matrix[i][j];
            }
            return new IntegerMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a integer sparse matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public IntegerSparseMatrix subtract(final IntegerSparseMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            IntegerSparseMatrix ans = new IntegerSparseMatrix(numRows(), numColumns());
            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++)
                    ans.setElement(i, j, getPrimitiveElement(i, j) - m.getPrimitiveElement(i, j));
            }
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// SCALAR MULTIPLICATION

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a integer
     * @return a integer sparse matrix
     */
    public AbstractIntegerMatrix scalarMultiply(final int x) {
        final IntegerSparseMatrix ans = new IntegerSparseMatrix(numRows(), numColumns());
        ans.elements = new int[elements.length];
        ans.colPos = new int[colPos.length];
        System.arraycopy(colPos, 0, ans.colPos, 0, colPos.length);
        System.arraycopy(rows, 0, ans.rows, 0, rows.length);
        for (int i = 0; i < colPos.length; i++)
            ans.elements[i] = x * elements[i];
        return ans;
    }

    // SCALAR PRODUCT

    /**
     * Returns the scalar product of this matrix and another.
     *
     * @param m a integer matrix.
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public int scalarProduct(final AbstractIntegerMatrix m) {
        if (m instanceof IntegerMatrix)
            return scalarProduct((IntegerMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            int ans = 0;
            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    ans += elements[j] * m.getPrimitiveElement(i, colPos[j]);
            }
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    public int scalarProduct(final IntegerMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            int ans = 0;
            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    ans += elements[j] * m.matrix[i][colPos[j]];
            }
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// MATRIX MULTIPLICATION

    /**
     * Returns the multiplication of a vector by this matrix.
     *
     * @param v a integer vector
     * @throws IllegalDimensionException If the matrix and vector are incompatible.
     */
    public AbstractIntegerVector multiply(final AbstractIntegerVector v) {
        if (numColumns() == v.getDimension()) {
            final int array[] = new int[numRows()];
            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i] += elements[j] * v.getPrimitiveElement(colPos[j]);
            }
            return new IntegerVector(array);
        } else
            throw new IllegalDimensionException("Matrix and vector are incompatible.");
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a integer matrix
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public AbstractIntegerMatrix multiply(final AbstractIntegerMatrix m) {
        if (m instanceof IntegerSparseMatrix)
            return multiply((IntegerSparseMatrix) m);
        if (m instanceof IntegerMatrix)
            return multiply((IntegerMatrix) m);

        if (numColumns() == m.numRows()) {
            final int array[][] = new int[numRows()][m.numColumns()];
            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < m.numColumns(); k++) {
                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        array[j][k] += elements[n] * m.getPrimitiveElement(colPos[n], k);
                }
            }
            if (numRows() == m.numColumns())
                return new IntegerSquareMatrix(array);
            else
                return new IntegerMatrix(array);
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

    public AbstractIntegerMatrix multiply(final IntegerMatrix m) {
        if (numColumns() == m.numRows()) {
            final int array[][] = new int[numRows()][m.numColumns()];
            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < m.numColumns(); k++) {
                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        array[j][k] += elements[n] * m.matrix[colPos[n]][k];
                }
            }
            if (numRows() == m.numColumns())
                return new IntegerSquareMatrix(array);
            else
                return new IntegerMatrix(array);
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a integer sparse matrix
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public AbstractIntegerMatrix multiply(final IntegerSparseMatrix m) {
        if (numColumns() == m.numRows()) {
            AbstractIntegerMatrix ans;
            if (numRows() == m.numColumns())
                ans = new IntegerSparseSquareMatrix(numRows());
            else
                ans = new IntegerSparseMatrix(numRows(), m.numColumns());
            for (int j = 0; j < ans.numRows(); j++) {
                for (int k = 0; k < ans.numColumns(); k++) {
                    int tmp = 0;
                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        tmp += elements[n] * m.getPrimitiveElement(colPos[n], k);
                    ans.setElement(j, k, tmp);
                }
            }
            return ans;
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

// TRANSPOSE

    /**
     * Returns the transpose of this matrix.
     *
     * @return a integer sparse matrix
     */
    public Matrix transpose() {
        final IntegerSparseMatrix ans = new IntegerSparseMatrix(numColumns(), numRows());
        for (int i = 0; i < numRows(); i++) {
            ans.setElement(0, i, getPrimitiveElement(i, 0));
            for (int j = 1; j < numColumns(); j++)
                ans.setElement(j, i, getPrimitiveElement(i, j));
        }
        return ans;
    }

// MAP ELEMENTS

    /**
     * Applies a function on all the matrix elements.
     *
     * @param f a user-defined function
     * @return a double sparse matrix
     */
    public AbstractDoubleMatrix mapElements(final PrimitiveMapping f) {
        final double[][] ans = new double[numRows()][numColumns()];
        double val = f.map(0);
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                ans[i][j] = val;
            }
        }
        for (int i = 0; i < numRows(); i++) {
            for (int j = rows[i]; j < rows[i + 1]; j++)
                ans[i][colPos[j]] = f.map(elements[j]);
        }
        return new DoubleMatrix(ans);
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new IntegerSparseMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an int array.
     */
    public int[][] toPrimitiveArray() {
        final int[][] result = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = rows[i]; j < rows[i + 1]; j++)
                result[i][colPos[j]] = elements[j];
        }
        return result;
    }

}

