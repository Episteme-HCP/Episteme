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

import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;

/**
 * The DoubleMatrix class provides an object for encapsulating double matrices.
 *
 * @author Mark Hale
 * @version 2.2
 */
public class DoubleMatrix extends AbstractDoubleMatrix implements Cloneable, Serializable {
    /**
     * Array containing the elements of the matrix.
     */
    protected final double matrix[][];

    /**
     * Constructs a matrix by wrapping an array.
     *
     * @param array an assigned value
     */
    public DoubleMatrix(final double array[][]) {
        super(array.length, array[0].length);
        matrix = array;
    }

    /**
     * Constructs an empty matrix.
     */
    public DoubleMatrix(final int rows, final int cols) {
        this(new double[rows][cols]);
    }

    /**
     * Constructs a matrix from an array of vectors (columns).
     *
     * @param array an assigned value
     */
    public DoubleMatrix(final AbstractDoubleVector array[]) {
        this(array[0].getDimension(), array.length);
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                matrix[i][j] = array[j].getPrimitiveElement(i);
        }
    }

    /**
     * Copy constructor.
     *
     * @param mat an assigned value
     */
    public DoubleMatrix(final DoubleMatrix mat) {
        this(mat.numRows(), mat.numColumns());
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                matrix[i][j] = mat.matrix[i][j];
            }
        }
    }

    /**
     * Compares two ${nativeTyp} matrices for equality.
     *
     * @param m a double matrix
     */
    public boolean equals(AbstractDoubleMatrix m, double tol) {
        if (m != null && numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double sumSqr = 0;
            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++) {
                    double delta = matrix[i][j] - m.getPrimitiveElement(i, j);
                    sumSqr += delta * delta;
                }
            }
            return (sumSqr <= tol * tol);
        } else {
            return false;
        }
    }

    /**
     * Returns a string representing this matrix.
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(5 * numRows() * numColumns());
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                buf.append(matrix[i][j]);
                buf.append(' ');
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    /**
     * Converts this matrix to an integer matrix.
     *
     * @return an integer matrix
     */
    public AbstractIntegerMatrix toIntegerMatrix() {
        final int ans[][] = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                ans[i][j] = Math.round((float) matrix[i][j]);
        }
        return new IntegerMatrix(ans);
    }

    /**
     * Converts this matrix to a complex matrix.
     *
     * @return a complex matrix
     */
    public AbstractComplexMatrix toComplexMatrix() {
        ComplexMatrix cm = new ComplexMatrix(numRows(), numColumns());
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                cm.setElement(i, j, matrix[i][j], 0.0);
        }
        return cm;
    }

    /**
     * Returns an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public double getPrimitiveElement(final int i, final int j) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns())
            return matrix[i][j];
        else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    /**
     * Returns the ith row.
     */
    public AbstractDoubleVector getRow(final int i) {

        double[] elements;

        if ((i >= 0) && (i < numRows())) {
            elements = new double[numColumns()];
            for (int j = 0; j < numColumns(); j++) {
                elements[j] = matrix[i][j];
            }
            return new DoubleVector(elements);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Returns the ith column.
     */
    public AbstractDoubleVector getColumn(final int j) {

        double[] elements;

        if ((j >= 0) && (j < numColumns())) {
            elements = new double[numRows()];
            for (int i = 0; i < numRows(); i++) {
                elements[i] = matrix[i][j];
            }
            return new DoubleVector(elements);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Sets the value of an element of the matrix.
     * Should only be used to initialise this matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @param x a number
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public void setElement(final int i, final int j, final double x) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns())
            matrix[i][j] = x;
        else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    /**
     * Sets the value of all elements of the matrix.
     *
     * @param m a complex element
     */
    public void setAllElements(final double m) {
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                matrix[i][j] = m;
            }
        }
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
     *
     * @author Taber Smith
     */
    public double infNorm() {
        if (numElements() > 0) {
            double result = 0, tmpResult;
            for (int i = 0; i < numRows(); i++) {
                tmpResult = 0;
                for (int j = 0; j < numColumns(); j++)
                    tmpResult += Math.abs(matrix[i][j]);
                if (tmpResult > result)
                    result = tmpResult;
            }
            return result;
        } else
            throw new ArithmeticException("The inf norm of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the Frobenius or Hilbert-Schmidt (l<sup>2</sup>) norm.
     *
     * @planetmath FrobeniusMatrixNorm
     */
    public double frobeniusNorm() {
        if (numElements() > 0) {
            double result = 0.0;
            for (int j, i = 0; i < numRows(); i++) {
                for (j = 0; j < numColumns(); j++)
                    result = MathUtils.hypot(result, matrix[i][j]);
            }
            return result;
        } else
            throw new ArithmeticException("The frobenius of a zero dimension matrix is undefined.");
    }

//============
// OPERATIONS
//============

    /**
     * Returns the negative of this matrix.
     */
    public AbelianGroup.Member negate() {
        final double array[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = -matrix[i][0];
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = -matrix[i][j];
        }
        return new DoubleMatrix(array);
    }

// ADDITION

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a double matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractDoubleMatrix add(final AbstractDoubleMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double array[][] = new double[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = matrix[i][0] + m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = matrix[i][j] + m.getPrimitiveElement(i, j);
            }
            return new DoubleMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this matrix by another.
     *
     * @param m a double matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractDoubleMatrix subtract(final AbstractDoubleMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double array[][] = new double[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = matrix[i][0] - m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = matrix[i][j] - m.getPrimitiveElement(i, j);
            }
            return new DoubleMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

// SCALAR MULTIPLICATION

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a double.
     * @return a double matrix.
     */
    public AbstractDoubleMatrix scalarMultiply(final double x) {
        final double array[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = x * matrix[i][0];
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = x * matrix[i][j];
        }
        return new DoubleMatrix(array);
    }

// SCALAR DIVISON

    /**
     * Returns the division of this matrix by a scalar.
     *
     * @param x a double.
     * @return a double matrix.
     */
    public AbstractDoubleMatrix scalarDivide(final double x) {
        final double array[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = matrix[i][0] / x;
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = matrix[i][j] / x;
        }
        return new DoubleMatrix(array);
    }

// SCALAR PRODUCT

    /**
     * Returns the scalar product of this matrix and another.
     *
     * @param m a double matrix.
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public double scalarProduct(final AbstractDoubleMatrix m) {
        if (m instanceof DoubleMatrix)
            return scalarProduct((DoubleMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double ans = 0;
            for (int i = 0; i < numRows(); i++) {
                ans += matrix[i][0] * m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    ans += matrix[i][j] * m.getPrimitiveElement(i, j);
            }
            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    public double scalarProduct(final DoubleMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double ans = 0;
            for (int i = 0; i < numRows(); i++) {
                ans += matrix[i][0] * m.matrix[i][0];
                for (int j = 1; j < numColumns(); j++)
                    ans += matrix[i][j] * m.matrix[i][j];
            }
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// MATRIX MULTIPLICATION

    /**
     * Returns the multiplication of a vector by this matrix.
     *
     * @param v a double vector.
     * @throws IllegalDimensionException If the matrix and vector are incompatible.
     */
    public AbstractDoubleVector multiply(final AbstractDoubleVector v) {
        if (numColumns() == v.getDimension()) {
            final double array[] = new double[numRows()];
            for (int i = 0; i < numRows(); i++) {
                array[i] = matrix[i][0] * v.getPrimitiveElement(0);
                for (int j = 1; j < numColumns(); j++)
                    array[i] += matrix[i][j] * v.getPrimitiveElement(j);
            }
            return new DoubleVector(array);
        } else {
            throw new IllegalDimensionException("Matrix and vector are incompatible.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a double matrix
     * @return a AbstractDoubleMatrix or a AbstractDoubleSquareMatrix as appropriate
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public AbstractDoubleMatrix multiply(final AbstractDoubleMatrix m) {
        if (m instanceof DoubleMatrix)
            return multiply((DoubleMatrix) m);

        if (numColumns() == m.numRows()) {
            final int mColumns = m.numColumns();
            final double array[][] = new double[numRows()][mColumns];
            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < mColumns; k++) {
                    array[j][k] = matrix[j][0] * m.getPrimitiveElement(0, k);
                    for (int n = 1; n < numColumns(); n++)
                        array[j][k] += matrix[j][n] * m.getPrimitiveElement(n, k);
                }
            }
            if (numRows() == mColumns)
                return new DoubleSquareMatrix(array);
            else
                return new DoubleMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    public AbstractDoubleMatrix multiply(final DoubleMatrix m) {
        if (numColumns() == m.numRows()) {
            final double array[][] = new double[numRows()][m.numColumns()];
            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < m.numColumns(); k++) {
                    array[j][k] = matrix[j][0] * m.matrix[0][k];
                    for (int n = 1; n < numColumns(); n++)
                        array[j][k] += matrix[j][n] * m.matrix[n][k];
                }
            }
            if (numRows() == m.numColumns())
                return new DoubleSquareMatrix(array);
            else
                return new DoubleMatrix(array);
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

// DIRECT SUM

    /**
     * Returns the direct sum of this matrix and another.
     */
    public AbstractDoubleMatrix directSum(final AbstractDoubleMatrix m) {
        final double array[][] = new double[numRows() + m.numRows()][numColumns() + m.numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                array[i][j] = matrix[i][j];
        }
        for (int i = 0; i < m.numRows(); i++) {
            for (int j = 0; j < m.numColumns(); j++)
                array[i + numRows()][j + numColumns()] = m.getPrimitiveElement(i, j);
        }
        return new DoubleMatrix(array);
    }

// TENSOR PRODUCT

    /**
     * Returns the tensor product of this matrix and another.
     */
    public AbstractDoubleMatrix tensorProduct(final AbstractDoubleMatrix m) {
        final double array[][] = new double[numRows() * m.numRows()][numColumns() * m.numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                for (int k = 0; k < m.numRows(); j++) {
                    for (int l = 0; l < m.numColumns(); l++)
                        array[i * m.numRows() + k][j * m.numColumns() + l] = matrix[i][j] * m.getPrimitiveElement(k, l);
                }
            }
        }
        return new DoubleMatrix(array);
    }

// TRANSPOSE

    /**
     * Returns the transpose of this matrix.
     *
     * @return a double matrix
     */
    public Matrix transpose() {
        final double array[][] = new double[numColumns()][numRows()];
        for (int i = 0; i < numRows(); i++) {
            array[0][i] = matrix[i][0];
            for (int j = 1; j < numColumns(); j++)
                array[j][i] = matrix[i][j];
        }
        return new DoubleMatrix(array);
    }

// MAP ELEMENTS

    /**
     * Applies a function on all the matrix elements.
     *
     * @param f a user-defined function
     * @return a double matrix
     */
    public AbstractDoubleMatrix mapElements(final PrimitiveMapping f) {
        final double array[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = f.map(matrix[i][0]);
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = f.map(matrix[i][j]);
        }
        return new DoubleMatrix(array);
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new DoubleMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an double array.
     */
    public double[][] toPrimitiveArray() {
        final double[][] result = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++)
            for (int j = 0; j < numColumns(); j++)
                result[i][j] = matrix[i][j];
        return result;
    }

}
