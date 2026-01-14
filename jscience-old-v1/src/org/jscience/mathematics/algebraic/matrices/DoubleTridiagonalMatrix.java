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

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.TridiagonalMatrix;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.IllegalDimensionException;
import org.jscience.util.MaximumIterationsExceededException;

import java.io.Serializable;

/**
 * The DoubleTridiagonalMatrix class provides an object for encapsulating double tridiagonal matrices.
 *
 * @author Mark Hale
 * @version 2.2
 */
public class DoubleTridiagonalMatrix extends AbstractDoubleSquareMatrix implements Cloneable, Serializable, TridiagonalMatrix {
    /**
     * Tridiagonal data.
     */
    protected final double ldiag[];
    protected final double diag[];
    protected final double udiag[];

    /**
     * Constructs an empty matrix.
     *
     * @param size the number of rows/columns
     */
    public DoubleTridiagonalMatrix(final int size) {
        super(size);
        ldiag = new double[size];
        diag = new double[size];
        udiag = new double[size];
    }

    /**
     * Constructs a matrix from an array.
     * Any non-tridiagonal elements in the array are ignored.
     *
     * @param array an assigned value
     * @throws IllegalDimensionException If the array is not square.
     */
    public DoubleTridiagonalMatrix(final double array[][]) {
        this(array.length);
        if (!ArrayMathUtils.isSquare(array))
            throw new IllegalDimensionException("Array is not square.");
        diag[0] = array[0][0];
        udiag[0] = array[0][1];
        int i = 1;
        for (; i < array.length - 1; i++) {
            ldiag[i] = array[i][i - 1];
            diag[i] = array[i][i];
            udiag[i] = array[i][i + 1];
        }
        ldiag[i] = array[i][i - 1];
        diag[i] = array[i][i];
    }

    /**
     * Copy constructor.
     *
     * @param mat an assigned value
     */
    public DoubleTridiagonalMatrix(final DoubleTridiagonalMatrix mat) {
        this(mat.numRows());
        System.arraycopy(mat.ldiag, 0, ldiag, 0, mat.ldiag.length);
        System.arraycopy(mat.diag, 0, diag, 0, mat.diag.length);
        System.arraycopy(mat.udiag, 0, udiag, 0, mat.udiag.length);
    }

    /**
     * Compares two ${nativeTyp} matrices for equality.
     *
     * @param m a double matrix
     */
    public boolean equals(AbstractDoubleMatrix m, double tol) {
        if (m instanceof TridiagonalMatrix) {
            if (numRows() != m.numRows() || numColumns() != m.numColumns())
                return false;
            double sumSqr = 0;
            double ldelta;
            double delta = diag[0] - m.getPrimitiveElement(0, 0);
            double udelta = udiag[0] - m.getPrimitiveElement(0, 1);
            sumSqr += delta * delta + udelta * udelta;
            int i = 1;
            for (; i < numRows() - 1; i++) {
                ldelta = ldiag[i] - m.getPrimitiveElement(i, i - 1);
                delta = diag[i] - m.getPrimitiveElement(i, i);
                udelta = udiag[i] - m.getPrimitiveElement(i, i + 1);
                sumSqr += ldelta * ldelta + delta * delta + udelta * udelta;
            }
            ldelta = ldiag[i] - m.getPrimitiveElement(i, i - 1);
            delta = diag[i] - m.getPrimitiveElement(i, i);
            sumSqr += ldelta * ldelta + delta * delta;
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
                buf.append(getPrimitiveElement(i, j));
                buf.append(' ');
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    //we don't check if the upper or lower diagonal elements are actually empty and thertefore always return 1
    public int getK1() {
        return 1;
    }

    //we don't check if the upper or lower diagonal elements are actually empty and thertefore always return 1
    public int getK2() {
        return 1;
    }

    /**
     * Converts this matrix to an integer matrix.
     *
     * @return an integer matrix
     */
    public AbstractIntegerMatrix toIntegerMatrix() {
        final IntegerTridiagonalMatrix m = new IntegerTridiagonalMatrix(numRows());
        m.diag[0] = Math.round((float) diag[0]);
        m.udiag[0] = Math.round((float) udiag[0]);
        int i = 1;
        for (; i < numRows() - 1; i++) {
            m.ldiag[i] = Math.round((float) ldiag[i]);
            m.diag[i] = Math.round((float) diag[i]);
            m.udiag[i] = Math.round((float) udiag[i]);
        }
        m.ldiag[i] = Math.round((float) ldiag[i]);
        m.diag[i] = Math.round((float) diag[i]);
        return m;
    }

    /**
     * Converts this matrix to a complex matrix.
     *
     * @return a complex matrix
     */
    public AbstractComplexMatrix toComplexMatrix() {
        final ComplexTridiagonalMatrix m = new ComplexTridiagonalMatrix(numRows());
        m.diagRe[0] = diag[0];
        m.udiagRe[0] = udiag[0];
        int i = 1;
        for (; i < numRows() - 1; i++) {
            m.ldiagRe[i] = ldiag[i];
            m.diagRe[i] = diag[i];
            m.udiagRe[i] = udiag[i];
        }
        m.ldiagRe[i] = ldiag[i];
        m.diagRe[i] = diag[i];
        return m;
    }

    /**
     * Returns an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public double getPrimitiveElement(final int i, final int j) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns()) {
            if (j == i - 1)
                return ldiag[i];
            else if (j == i)
                return diag[i];
            else if (j == i + 1)
                return udiag[i];
            else
                return 0;
        } else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
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
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns()) {
            if (j == i - 1)
                ldiag[i] = x;
            else if (j == i)
                diag[i] = x;
            else if (j == i + 1)
                udiag[i] = x;
            else
                throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        } else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    /**
     * Sets the value of all elements of the matrix. This method will throw an error unless the paramter is 0.
     * This is because you need a SquareMatrix to set all the contents of the matrix to a value.
     * You should think about using a DoubleSquareMatrix.
     *
     * @param r a double element
     */
    public void setAllElements(final double r) {
        if (r == 0) {
            for (int i = 0; i < numRows(); i++) {
                ldiag[i] = 0;
                diag[i] = 0;
                udiag[i] = 0;
            }
        } else
            throw new IllegalArgumentException("You can't set all elements of a tridiagonal matrix unless to zero.");
    }

    /**
     * Returns true if this matrix is symmetric.
     */
    public boolean isSymmetric() {
        if (numElements() > 1) {
            if (ldiag[1] != udiag[0])
                return false;
            for (int i = 1; i < numRows() - 1; i++) {
                if (ldiag[i + 1] != udiag[i])
                    return false;
            }
            return true;
        } else
            return true;
    }

    /**
     * Returns the trace.
     */
    public double trace() {
        if (numElements() > 0) {
            double tr = diag[0];
            for (int i = 1; i < numRows(); i++)
                tr += diag[i];
            return tr;
        } else
            throw new ArithmeticException("The trace of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
     *
     * @author Taber Smith
     */
    public double infNorm() {
        if (numElements() > 0) {
            double result = Math.abs(diag[0]) + Math.abs(udiag[0]);
            double tmpResult;
            int i = 1;
            for (; i < numRows() - 1; i++) {
                tmpResult = Math.abs(ldiag[i]) + Math.abs(diag[i]) + Math.abs(udiag[i]);
                if (tmpResult > result)
                    result = tmpResult;
            }
            tmpResult = Math.abs(ldiag[i]) + Math.abs(diag[i]);
            if (tmpResult > result)
                result = tmpResult;
            return result;
        } else
            throw new ArithmeticException("The inf norm of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the Frobenius (l<sup>2</sup>) norm.
     *
     * @author Taber Smith
     */
    public double frobeniusNorm() {
        if (numElements() > 0) {
            double result = diag[0] * diag[0] + udiag[0] * udiag[0];
            int i = 1;
            for (; i < numRows() - 1; i++)
                result += ldiag[i] * ldiag[i] + diag[i] * diag[i] + udiag[i] * udiag[i];
            result += ldiag[i] * ldiag[i] + diag[i] * diag[i];
            return Math.sqrt(result);
        } else
            throw new ArithmeticException("The frobenius norm of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the operator norm.
     *
     * @throws MaximumIterationsExceededException
     *          If it takes more than 50 iterations to determine an eigenvalue.
     */
    public double operatorNorm() throws MaximumIterationsExceededException {
        if (numElements() > 0) {
            return Math.sqrt(ArrayMathUtils.max(LinearMathUtils.eigenvalueSolveSymmetric((((DoubleTridiagonalMatrix) this.transpose()).multiply(this)))));
        } else
            throw new ArithmeticException("The operator norm of a zero dimension matrix is undefined.");
    }

//============
// OPERATIONS
//============

// ADDITION

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a double matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractDoubleSquareMatrix add(final AbstractDoubleSquareMatrix m) {
        if (m instanceof DoubleTridiagonalMatrix)
            return add((DoubleTridiagonalMatrix) m);
        if (m instanceof TridiagonalMatrix)
            return addTridiagonal(m);
        if (m instanceof DoubleSquareMatrix)
            return add((DoubleSquareMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double array[][] = new double[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = getPrimitiveElement(i, 0) + m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = getPrimitiveElement(i, j) + m.getPrimitiveElement(i, j);
            }
            return new DoubleSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    public DoubleSquareMatrix add(final DoubleSquareMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double array[][] = new double[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++)
                System.arraycopy(m.matrix[i], 0, array[i], 0, numRows());
            array[0][0] += diag[0];
            array[0][1] += udiag[0];
            int n = numRows() - 1;
            for (int i = 1; i < n; i++) {
                array[i][i - 1] += ldiag[i];
                array[i][i] += diag[i];
                array[i][i + 1] += udiag[i];
            }
            array[n][n - 1] += ldiag[n];
            array[n][n] += diag[n];
            return new DoubleSquareMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a double tridiagonal matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public DoubleTridiagonalMatrix add(final DoubleTridiagonalMatrix m) {
        int mRow = numRows();
        if (mRow == m.numRows()) {
            final DoubleTridiagonalMatrix ans = new DoubleTridiagonalMatrix(mRow);
            ans.diag[0] = diag[0] + m.diag[0];
            ans.udiag[0] = udiag[0] + m.udiag[0];
            mRow--;
            for (int i = 1; i < mRow; i++) {
                ans.ldiag[i] = ldiag[i] + m.ldiag[i];
                ans.diag[i] = diag[i] + m.diag[i];
                ans.udiag[i] = udiag[i] + m.udiag[i];
            }
            ans.ldiag[mRow] = ldiag[mRow] + m.ldiag[mRow];
            ans.diag[mRow] = diag[mRow] + m.diag[mRow];
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    private DoubleTridiagonalMatrix addTridiagonal(final AbstractDoubleSquareMatrix m) {
        int mRow = numRows();
        if (mRow == m.numRows()) {
            final DoubleTridiagonalMatrix ans = new DoubleTridiagonalMatrix(mRow);
            ans.diag[0] = diag[0] + m.getPrimitiveElement(0, 0);
            ans.udiag[0] = udiag[0] + m.getPrimitiveElement(0, 1);
            mRow--;
            for (int i = 1; i < mRow; i++) {
                ans.ldiag[i] = ldiag[i] + m.getPrimitiveElement(i, i - 1);
                ans.diag[i] = diag[i] + m.getPrimitiveElement(i, i);
                ans.udiag[i] = udiag[i] + m.getPrimitiveElement(i, i + 1);
            }
            ans.ldiag[mRow] = ldiag[mRow] + m.getPrimitiveElement(mRow, mRow - 1);
            ans.diag[mRow] = diag[mRow] + m.getPrimitiveElement(mRow, mRow);
            return ans;
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
    public AbstractDoubleSquareMatrix subtract(final AbstractDoubleSquareMatrix m) {
        if (m instanceof DoubleTridiagonalMatrix)
            return subtract((DoubleTridiagonalMatrix) m);
        if (m instanceof TridiagonalMatrix)
            return subtractTridiagonal(m);
        if (m instanceof DoubleSquareMatrix)
            return subtract((DoubleSquareMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double array[][] = new double[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = getPrimitiveElement(i, 0) - m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = getPrimitiveElement(i, j) - m.getPrimitiveElement(i, j);
            }
            return new DoubleSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    public DoubleSquareMatrix subtract(final DoubleSquareMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double array[][] = new double[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = getPrimitiveElement(i, 0) - m.matrix[i][0];
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = getPrimitiveElement(i, j) - m.matrix[i][j];
            }
            return new DoubleSquareMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m a double tridiagonal matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public DoubleTridiagonalMatrix subtract(final DoubleTridiagonalMatrix m) {
        int mRow = numRows();
        if (mRow == m.numRows()) {
            final DoubleTridiagonalMatrix ans = new DoubleTridiagonalMatrix(mRow);
            ans.diag[0] = diag[0] - m.diag[0];
            ans.udiag[0] = udiag[0] - m.udiag[0];
            mRow--;
            for (int i = 1; i < mRow; i++) {
                ans.ldiag[i] = ldiag[i] - m.ldiag[i];
                ans.diag[i] = diag[i] - m.diag[i];
                ans.udiag[i] = udiag[i] - m.udiag[i];
            }
            ans.ldiag[mRow] = ldiag[mRow] - m.ldiag[mRow];
            ans.diag[mRow] = diag[mRow] - m.diag[mRow];
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    private DoubleTridiagonalMatrix subtractTridiagonal(final AbstractDoubleSquareMatrix m) {
        int mRow = numRows();
        if (mRow == m.numRows()) {
            final DoubleTridiagonalMatrix ans = new DoubleTridiagonalMatrix(mRow);
            ans.diag[0] = diag[0] - m.getPrimitiveElement(0, 0);
            ans.udiag[0] = udiag[0] - m.getPrimitiveElement(0, 1);
            mRow--;
            for (int i = 1; i < mRow; i++) {
                ans.ldiag[i] = ldiag[i] - m.getPrimitiveElement(i, i - 1);
                ans.diag[i] = diag[i] - m.getPrimitiveElement(i, i);
                ans.udiag[i] = udiag[i] - m.getPrimitiveElement(i, i + 1);
            }
            ans.ldiag[mRow] = ldiag[mRow] - m.getPrimitiveElement(mRow, mRow - 1);
            ans.diag[mRow] = diag[mRow] - m.getPrimitiveElement(mRow, mRow);
            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

// SCALAR MULTIPLICATION

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a double.
     * @return a double square matrix.
     */
    public AbstractDoubleMatrix scalarMultiply(final double x) {
        final DoubleTridiagonalMatrix ans = new DoubleTridiagonalMatrix(numRows());
        final int lastRow = numRows() - 1;
        ans.diag[0] = x * diag[0];
        ans.udiag[0] = x * udiag[0];
        for (int i = 1; i < lastRow; i++) {
            ans.ldiag[i] = x * ldiag[i];
            ans.diag[i] = x * diag[i];
            ans.udiag[i] = x * udiag[i];
        }
        ans.ldiag[lastRow] = x * ldiag[lastRow];
        ans.diag[lastRow] = x * diag[lastRow];
        return ans;
    }

// SCALAR DIVISON

    /**
     * Returns the division of this matrix by a scalar.
     *
     * @param x a double.
     * @return a double square matrix.
     */
    public AbstractDoubleMatrix scalarDivide(final double x) {
        final DoubleTridiagonalMatrix ans = new DoubleTridiagonalMatrix(numRows());
        final int lastRow = numRows() - 1;
        ans.diag[0] = diag[0] / x;
        ans.udiag[0] = udiag[0] / x;
        for (int i = 1; i < lastRow; i++) {
            ans.ldiag[i] = ldiag[i] / x;
            ans.diag[i] = diag[i] / x;
            ans.udiag[i] = udiag[i] / x;
        }
        ans.ldiag[lastRow] = ldiag[lastRow] / x;
        ans.diag[lastRow] = diag[lastRow] / x;
        return ans;
    }

// SCALAR PRODUCT

    /**
     * Returns the scalar product of this matrix and another.
     *
     * @param m a double matrix.
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public double scalarProduct(final AbstractDoubleSquareMatrix m) {
        if (m instanceof DoubleTridiagonalMatrix)
            return scalarProduct((DoubleTridiagonalMatrix) m);
        if (m instanceof DoubleSquareMatrix)
            return scalarProduct((DoubleSquareMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double ans = diag[0] * m.getPrimitiveElement(0, 0) + udiag[0] * m.getPrimitiveElement(0, 1);
            final int lastRow = numRows() - 1;
            for (int i = 1; i < lastRow; i++)
                ans += ldiag[i] * m.getPrimitiveElement(i, i - 1) + diag[i] * m.getPrimitiveElement(i, i) + udiag[i] * m.getPrimitiveElement(i, i + 1);
            ans += ldiag[lastRow] * m.getPrimitiveElement(lastRow, lastRow - 1) + diag[lastRow] * m.getPrimitiveElement(lastRow, lastRow);
            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    public double scalarProduct(final DoubleSquareMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double ans = diag[0] * m.matrix[0][0] + udiag[0] * m.matrix[0][1];
            final int lastRow = numRows() - 1;
            for (int i = 1; i < lastRow; i++)
                ans += ldiag[i] * m.matrix[i][i - 1] + diag[i] * m.matrix[i][i] + udiag[i] * m.matrix[i][i + 1];
            ans += ldiag[lastRow] * m.matrix[lastRow][lastRow - 1] + diag[lastRow] * m.matrix[lastRow][lastRow];
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    public double scalarProduct(final DoubleTridiagonalMatrix m) {
        if (numRows() == m.numRows()) {
            double ans = diag[0] * m.diag[0] + udiag[0] * m.udiag[0];
            final int lastRow = numRows() - 1;
            for (int i = 1; i < lastRow; i++)
                ans += ldiag[i] * m.ldiag[i] + diag[i] * m.diag[i] + udiag[i] * m.udiag[i];
            ans += ldiag[lastRow] * m.ldiag[lastRow] + diag[lastRow] * m.diag[lastRow];
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
            final int lastRow = numRows() - 1;
            array[0] = diag[0] * v.getPrimitiveElement(0) + udiag[0] * v.getPrimitiveElement(1);
            for (int i = 1; i < lastRow; i++)
                array[i] = ldiag[i] * v.getPrimitiveElement(i - 1) + diag[i] * v.getPrimitiveElement(i) + udiag[i] * v.getPrimitiveElement(i + 1);
            array[lastRow] = ldiag[lastRow] * v.getPrimitiveElement(lastRow - 1) + diag[lastRow] * v.getPrimitiveElement(lastRow);
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
    public AbstractDoubleSquareMatrix multiply(final AbstractDoubleSquareMatrix m) {
        if (m instanceof DoubleTridiagonalMatrix)
            return multiply((DoubleTridiagonalMatrix) m);
        if (m instanceof TridiagonalMatrix)
            return multiplyTridiagonal(m);
        if (m instanceof DoubleSquareMatrix)
            return multiply((DoubleSquareMatrix) m);

        if (numColumns() == m.numRows()) {
            final int mColumns = m.numColumns();
            final double array[][] = new double[numRows()][mColumns];
            final int lastRow = numRows() - 1;
            for (int j = 0; j < numRows(); j++) {
                array[0][j] = diag[0] * m.getPrimitiveElement(j, 0) + udiag[0] * m.getPrimitiveElement(j, 1);
                for (int i = 1; i < lastRow; i++)
                    array[i][j] = ldiag[i] * m.getPrimitiveElement(j, i - 1) + diag[i] * m.getPrimitiveElement(j, i) + udiag[i] * m.getPrimitiveElement(j, i + 1);
                array[lastRow][j] = ldiag[lastRow] * m.getPrimitiveElement(j, lastRow - 1) + diag[lastRow] * m.getPrimitiveElement(j, lastRow);
            }
            return new DoubleSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    public DoubleSquareMatrix multiply(final DoubleSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            final double array[][] = new double[numRows()][m.numColumns()];
            final int lastRow = numRows() - 1;
            for (int j = 0; j < numRows(); j++) {
                array[0][j] = diag[0] * m.matrix[j][0] + udiag[0] * m.matrix[j][1];
                for (int i = 1; i < lastRow; i++)
                    array[i][j] = ldiag[i] * m.matrix[j][i - 1] + diag[i] * m.matrix[j][i] + udiag[i] * m.matrix[j][i + 1];
                array[lastRow][j] = ldiag[lastRow] * m.matrix[j][lastRow - 1] + diag[lastRow] * m.matrix[j][lastRow];
            }
            return new DoubleSquareMatrix(array);
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

    public DoubleSquareMatrix multiply(final DoubleTridiagonalMatrix m) {
        int mRow = numRows();
        if (numColumns() == m.numRows()) {
            final double array[][] = new double[mRow][mRow];
            array[0][0] = diag[0] * m.diag[0] + udiag[0] * m.ldiag[1];
            array[0][1] = diag[0] * m.udiag[0] + udiag[0] * m.diag[1];
            array[0][2] = udiag[0] * m.udiag[1];
            if (mRow > 3) {
                array[1][0] = ldiag[1] * m.diag[0] + diag[1] * m.ldiag[1];
                array[1][1] = ldiag[1] * m.udiag[0] + diag[1] * m.diag[1] + udiag[1] * m.ldiag[2];
                array[1][2] = diag[1] * m.udiag[1] + udiag[1] * m.diag[2];
                array[1][3] = udiag[1] * m.udiag[2];
            }
            if (mRow == 3) {
                array[1][0] = ldiag[1] * m.diag[0] + diag[1] * m.ldiag[1];
                array[1][1] = ldiag[1] * m.udiag[0] + diag[1] * m.diag[1] + udiag[1] * m.ldiag[2];
                array[1][2] = diag[1] * m.udiag[1] + udiag[1] * m.diag[2];
            } else if (mRow > 4) {
                for (int i = 2; i < mRow - 2; i++) {
                    array[i][i - 2] = ldiag[i] * m.ldiag[i - 1];
                    array[i][i - 1] = ldiag[i] * m.diag[i - 1] + diag[i] * m.ldiag[i];
                    array[i][i] = ldiag[i] * m.udiag[i - 1] + diag[i] * m.diag[i] + udiag[i] * m.ldiag[i + 1];
                    array[i][i + 1] = diag[i] * m.udiag[i] + udiag[i] * m.diag[i + 1];
                    array[i][i + 2] = udiag[i] * m.udiag[i + 1];
                }
            }
            if (mRow > 3) {
                array[mRow - 2][mRow - 4] = ldiag[mRow - 2] * m.ldiag[mRow - 3];
                array[mRow - 2][mRow - 3] = ldiag[mRow - 2] * m.diag[mRow - 3] + diag[mRow - 2] * m.ldiag[mRow - 2];
                array[mRow - 2][mRow - 2] = ldiag[mRow - 2] * m.udiag[mRow - 3] + diag[mRow - 2] * m.diag[mRow - 2] + udiag[mRow - 2] * m.ldiag[mRow - 1];
                array[mRow - 2][mRow - 1] = diag[mRow - 2] * m.udiag[mRow - 2] + udiag[mRow - 2] * m.diag[mRow - 1];
            }
            mRow--;
            array[mRow][mRow - 2] = ldiag[mRow] * m.ldiag[mRow - 1];
            array[mRow][mRow - 1] = ldiag[mRow] * m.diag[mRow - 1] + diag[mRow] * m.ldiag[mRow];
            array[mRow][mRow] = ldiag[mRow] * m.udiag[mRow - 1] + diag[mRow] * m.diag[mRow];
            return new DoubleSquareMatrix(array);
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

    private DoubleSquareMatrix multiplyTridiagonal(final AbstractDoubleSquareMatrix m) {
        int mRow = numRows();
        if (numColumns() == m.numRows()) {
            final double array[][] = new double[mRow][mRow];
            array[0][0] = diag[0] * m.getPrimitiveElement(0, 0) + udiag[0] * m.getPrimitiveElement(1, 0);
            array[0][1] = diag[0] * m.getPrimitiveElement(0, 1) + udiag[0] * m.getPrimitiveElement(1, 1);
            array[0][2] = udiag[0] * m.getPrimitiveElement(1, 2);
            if (mRow > 3) {
                array[1][0] = ldiag[1] * m.getPrimitiveElement(0, 0) + diag[1] * m.getPrimitiveElement(1, 0);
                array[1][1] = ldiag[1] * m.getPrimitiveElement(0, 1) + diag[1] * m.getPrimitiveElement(1, 1) + udiag[1] * m.getPrimitiveElement(2, 1);
                array[1][2] = diag[1] * m.getPrimitiveElement(1, 2) + udiag[1] * m.getPrimitiveElement(2, 2);
                array[1][3] = udiag[1] * m.getPrimitiveElement(2, 3);
            }
            if (mRow == 3) {
                array[1][0] = ldiag[1] * m.getPrimitiveElement(0, 0) + diag[1] * m.getPrimitiveElement(1, 0);
                array[1][1] = ldiag[1] * m.getPrimitiveElement(0, 1) + diag[1] * m.getPrimitiveElement(1, 1) + udiag[1] * m.getPrimitiveElement(2, 1);
                array[1][2] = diag[1] * m.getPrimitiveElement(1, 2) + udiag[1] * m.getPrimitiveElement(2, 2);
            } else if (mRow > 4) {
                for (int i = 2; i < mRow - 2; i++) {
                    array[i][i - 2] = ldiag[i] * m.getPrimitiveElement(i - 1, i - 2);
                    array[i][i - 1] = ldiag[i] * m.getPrimitiveElement(i - 1, i - 1) + diag[i] * m.getPrimitiveElement(i, i - 1);
                    array[i][i] = ldiag[i] * m.getPrimitiveElement(i - 1, i) + diag[i] * m.getPrimitiveElement(i, i) + udiag[i] * m.getPrimitiveElement(i + 1, i);
                    array[i][i + 1] = diag[i] * m.getPrimitiveElement(i, i + 1) + udiag[i] * m.getPrimitiveElement(i + 1, i + 1);
                    array[i][i + 2] = udiag[i] * m.getPrimitiveElement(i + 1, i + 2);
                }
            }
            if (mRow > 3) {
                array[mRow - 2][mRow - 4] = ldiag[mRow - 2] * m.getPrimitiveElement(mRow - 3, mRow - 4);
                array[mRow - 2][mRow - 3] = ldiag[mRow - 2] * m.getPrimitiveElement(mRow - 3, mRow - 3) + diag[mRow - 2] * m.getPrimitiveElement(mRow - 2, mRow - 3);
                array[mRow - 2][mRow - 2] = ldiag[mRow - 2] * m.getPrimitiveElement(mRow - 3, mRow - 2) + diag[mRow - 2] * m.getPrimitiveElement(mRow - 2, mRow - 2) + udiag[mRow - 2] * m.getPrimitiveElement(mRow - 1, mRow - 2);
                array[mRow - 2][mRow - 1] = diag[mRow - 2] * m.getPrimitiveElement(mRow - 2, mRow - 1) + udiag[mRow - 2] * m.getPrimitiveElement(mRow - 1, mRow - 1);
            }
            mRow--;
            array[mRow][mRow - 2] = ldiag[mRow] * m.getPrimitiveElement(mRow - 1, mRow - 2);
            array[mRow][mRow - 1] = ldiag[mRow] * m.getPrimitiveElement(mRow - 1, mRow - 1) + diag[mRow] * m.getPrimitiveElement(mRow, mRow - 1);
            array[mRow][mRow] = ldiag[mRow] * m.getPrimitiveElement(mRow - 1, mRow) + diag[mRow] * m.getPrimitiveElement(mRow, mRow);
            return new DoubleSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

// TRANSPOSE

    /**
     * Returns the transpose of this matrix.
     *
     * @return a double matrix
     */
    public Matrix transpose() {
        final DoubleTridiagonalMatrix ans = new DoubleTridiagonalMatrix(numRows());
        System.arraycopy(ldiag, 1, ans.udiag, 0, ldiag.length - 1);
        System.arraycopy(diag, 0, ans.diag, 0, diag.length);
        System.arraycopy(udiag, 0, ans.ldiag, 1, udiag.length - 1);
        return ans;
    }

// CHOLESKY DECOMPOSITION

    /**
     * Returns the Cholesky decomposition of this matrix.
     * Matrix must be symmetric and positive definite.
     *
     * @return an array with [0] containing the L-matrix and [1] containing the U-matrix.
     */
    public AbstractDoubleSquareMatrix[] choleskyDecompose() {
        final int N = numRows();
        final double arrayL[][] = new double[N][N];
        final double arrayU[][] = new double[N][N];
        double tmp = Math.sqrt(diag[0]);
        arrayL[0][0] = arrayU[0][0] = tmp;
        arrayL[1][0] = arrayU[0][1] = ldiag[1] / tmp;
        for (int j = 1; j < N; j++) {
            tmp = diag[j];
            for (int i = 0; i < j; i++)
                tmp -= arrayL[j][i] * arrayL[j][i];
            arrayL[j][j] = arrayU[j][j] = Math.sqrt(tmp);
            if (j + 1 < N) {
                tmp = ldiag[j + 1];
                for (int k = 0; k < j + 1; k++)
                    tmp -= arrayL[j][k] * arrayU[k][j + 1];
                arrayL[j + 1][j] = arrayU[j][j + 1] = tmp / arrayU[j][j];
                for (int i = j + 2; i < N; i++) {
                    tmp = 0.0;
                    for (int k = 0; k < i; k++)
                        tmp -= arrayL[j][k] * arrayU[k][i];
                    arrayL[i][j] = arrayU[j][i] = tmp / arrayU[j][j];
                }
            }
        }
        final AbstractDoubleSquareMatrix lu[] = new AbstractDoubleSquareMatrix[2];
        lu[0] = new DoubleSquareMatrix(arrayL);
        lu[1] = new DoubleSquareMatrix(arrayU);
        return lu;
    }

// QR DECOMPOSITION

    /**
     * Returns the QR decomposition of this matrix.
     * Based on the code from <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a> (public domain).
     *
     * @return an array with [0] containing the Q-matrix and [1] containing the R-matrix.
     * @planetmath QRDecomposition
     */
    public AbstractDoubleSquareMatrix[] qrDecompose() {
        final int N = numRows();
        final double array[][] = new double[N][N];
        final double arrayQ[][] = new double[N][N];
        final double arrayR[][] = new double[N][N];
        // copy matrix
        array[0][0] = diag[0];
        array[0][1] = udiag[0];
        for (int i = 1; i < N - 1; i++) {
            array[i][i - 1] = ldiag[i];
            array[i][i] = diag[i];
            array[i][i + 1] = udiag[i];
        }
        array[N - 1][N - 2] = ldiag[N - 1];
        array[N - 1][N - 1] = diag[N - 1];
        for (int k = 0; k < N; k++) {
            // compute l2-norm of kth column
            double norm = array[k][k];
            for (int i = k + 1; i < N; i++)
                norm = MathUtils.hypot(norm, array[i][k]);
            if (norm != 0.0) {
                // form kth Householder vector
                if (array[k][k] < 0.0)
                    norm = -norm;
                for (int i = k; i < N; i++)
                    array[i][k] /= norm;
                array[k][k] += 1.0;
                // apply transformation to remaining columns
                for (int j = k + 1; j < N; j++) {
                    double s = array[k][k] * array[k][j];
                    for (int i = k + 1; i < N; i++)
                        s += array[i][k] * array[i][j];
                    s /= array[k][k];
                    for (int i = k; i < N; i++)
                        array[i][j] -= s * array[i][k];
                }
            }
            arrayR[k][k] = -norm;
        }
        for (int k = N - 1; k >= 0; k--) {
            arrayQ[k][k] = 1.0;
            for (int j = k; j < N; j++) {
                if (array[k][k] != 0.0) {
                    double s = array[k][k] * arrayQ[k][j];
                    for (int i = k + 1; i < N; i++)
                        s += array[i][k] * arrayQ[i][j];
                    s /= array[k][k];
                    for (int i = k; i < N; i++)
                        arrayQ[i][j] -= s * array[i][k];
                }
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++)
                arrayR[i][j] = array[i][j];
        }
        final AbstractDoubleSquareMatrix qr[] = new AbstractDoubleSquareMatrix[2];
        qr[0] = new DoubleSquareMatrix(arrayQ);
        qr[1] = new DoubleSquareMatrix(arrayR);
        return qr;
    }

// SINGULAR VALUE DECOMPOSITION

    /**
     * Returns the singular value decomposition of this matrix.
     * Based on the code from <a href="http://math.nist.gov/javanumerics/jama/">JAMA</a> (public domain).
     *
     * @return an array with [0] containing the U-matrix, [1] containing the S-matrix and [2] containing the V-matrix.
     */
    public AbstractDoubleSquareMatrix[] singularValueDecompose() {
        final int N = numRows();
        final int Nm1 = N - 1;
        final double array[][] = new double[N][N];
        final double arrayU[][] = new double[N][N];
        final double arrayS[] = new double[N];
        final double arrayV[][] = new double[N][N];
        final double e[] = new double[N];
        final double work[] = new double[N];
        // copy matrix
        array[0][0] = diag[0];
        array[0][1] = udiag[0];
        for (int i = 1; i < Nm1; i++) {
            array[i][i - 1] = ldiag[i];
            array[i][i] = diag[i];
            array[i][i + 1] = udiag[i];
        }
        array[Nm1][Nm1 - 1] = ldiag[Nm1];
        array[Nm1][Nm1] = diag[Nm1];
        // reduce matrix to bidiagonal form
        for (int k = 0; k < Nm1; k++) {
            // compute the transformation for the kth column
            // compute l2-norm of kth column
            arrayS[k] = array[k][k];
            for (int i = k + 1; i < N; i++)
                arrayS[k] = MathUtils.hypot(arrayS[k], array[i][k]);
            if (arrayS[k] != 0.0) {
                if (array[k][k] < 0.0)
                    arrayS[k] = -arrayS[k];
                for (int i = k; i < N; i++)
                    array[i][k] /= arrayS[k];
                array[k][k] += 1.0;
            }
            arrayS[k] = -arrayS[k];
            for (int j = k + 1; j < N; j++) {
                if (arrayS[k] != 0.0) {
                    // apply the transformation
                    double t = array[k][k] * array[k][j];
                    for (int i = k + 1; i < N; i++)
                        t += array[i][k] * array[i][j];
                    t /= array[k][k];
                    for (int i = k; i < N; i++)
                        array[i][j] -= t * array[i][k];
                }
                e[j] = array[k][j];
            }
            for (int i = k; i < N; i++)
                arrayU[i][k] = array[i][k];
            if (k < N - 2) {
                // compute the kth row transformation
                // compute l2-norm of kth column
                e[k] = e[k + 1];
                for (int i = k + 2; i < N; i++)
                    e[k] = MathUtils.hypot(e[k], e[i]);
                if (e[k] != 0.0) {
                    if (e[k + 1] < 0.0)
                        e[k] = -e[k];
                    for (int i = k + 1; i < N; i++)
                        e[i] /= e[k];
                    e[k + 1] += 1.0;
                }
                e[k] = -e[k];
                if (e[k] != 0.0) {
                    // apply the transformation
                    for (int i = k + 1; i < N; i++) {
                        work[i] = 0.0;
                        for (int j = k + 1; j < N; j++)
                            work[i] += e[j] * array[i][j];
                    }
                    for (int j = k + 1; j < N; j++) {
                        double t = e[j] / e[k + 1];
                        for (int i = k + 1; i < N; i++)
                            array[i][j] -= t * work[i];
                    }
                }
                for (int i = k + 1; i < N; i++)
                    arrayV[i][k] = e[i];
            }
        }
        // setup the final bidiagonal matrix of order p
        int p = N;
        arrayS[Nm1] = array[Nm1][Nm1];
        e[N - 2] = array[N - 2][Nm1];
        e[Nm1] = 0.0;
        arrayU[Nm1][Nm1] = 1.0;
        for (int k = N - 2; k >= 0; k--) {
            if (arrayS[k] != 0.0) {
                for (int j = k + 1; j < N; j++) {
                    double t = arrayU[k][k] * arrayU[k][j];
                    for (int i = k + 1; i < N; i++)
                        t += arrayU[i][k] * arrayU[i][j];
                    t /= arrayU[k][k];
                    for (int i = k; i < N; i++)
                        arrayU[i][j] -= t * arrayU[i][k];
                }
                for (int i = k; i < N; i++)
                    arrayU[i][k] = -arrayU[i][k];
                arrayU[k][k] += 1.0;
                for (int i = 0; i < k - 1; i++)
                    arrayU[i][k] = 0.0;
            } else {
                for (int i = 0; i < N; i++)
                    arrayU[i][k] = 0.0;
                arrayU[k][k] = 1.0;
            }
        }
        for (int k = Nm1; k >= 0; k--) {
            if (k < N - 2 && e[k] != 0.0) {
                for (int j = k + 1; j < N; j++) {
                    double t = arrayV[k + 1][k] * arrayV[k + 1][j];
                    for (int i = k + 2; i < N; i++)
                        t += arrayV[i][k] * arrayV[i][j];
                    t /= arrayV[k + 1][k];
                    for (int i = k + 1; i < N; i++)
                        arrayV[i][j] -= t * arrayV[i][k];
                }
            }
            for (int i = 0; i < N; i++)
                arrayV[i][k] = 0.0;
            arrayV[k][k] = 1.0;
        }
        final double eps = Math.pow(2.0, -52.0);
        int iter = 0;
        while (p > 0) {
            int k, action;
            // action = 1 if arrayS[p] and e[k-1] are negligible and k<p
            // action = 2 if arrayS[k] is negligible and k<p
            // action = 3 if e[k-1] is negligible, k<p, and arrayS[k], ..., arrayS[p] are not negligible (QR step)
            // action = 4 if e[p-1] is negligible (convergence)
            for (k = p - 2; k >= -1; k--) {
                if (k == -1)
                    break;
                if (Math.abs(e[k]) <= eps * (Math.abs(arrayS[k]) + Math.abs(arrayS[k + 1]))) {
                    e[k] = 0.0;
                    break;
                }
            }
            if (k == p - 2) {
                action = 4;
            } else {
                int ks;
                for (ks = p - 1; ks >= k; ks--) {
                    if (ks == k)
                        break;
                    double t = (ks != p ? Math.abs(e[ks]) : 0.0) + (ks != k + 1 ? Math.abs(e[ks - 1]) : 0.0);
                    if (Math.abs(arrayS[ks]) <= eps * t) {
                        arrayS[ks] = 0.0;
                        break;
                    }
                }
                if (ks == k) {
                    action = 3;
                } else if (ks == p - 1) {
                    action = 1;
                } else {
                    action = 2;
                    k = ks;
                }
            }
            k++;
            switch (action) {
                // deflate negligible arrayS[p]
                case 1: {
                    double f = e[p - 2];
                    e[p - 2] = 0.0;
                    for (int j = p - 2; j >= k; j--) {
                        double t = MathUtils.hypot(arrayS[j], f);
                        final double cs = arrayS[j] / t;
                        final double sn = f / t;
                        arrayS[j] = t;
                        if (j != k) {
                            f = -sn * e[j - 1];
                            e[j - 1] *= cs;
                        }
                        for (int i = 0; i < N; i++) {
                            t = cs * arrayV[i][j] + sn * arrayV[i][p - 1];
                            arrayV[i][p - 1] = -sn * arrayV[i][j] + cs * arrayV[i][p - 1];
                            arrayV[i][j] = t;
                        }
                    }
                }
                break;
                // split at negligible arrayS[k]
                case 2: {
                    double f = e[k - 1];
                    e[k - 1] = 0.0;
                    for (int j = k; j < p; j++) {
                        double t = MathUtils.hypot(arrayS[j], f);
                        final double cs = arrayS[j] / t;
                        final double sn = f / t;
                        arrayS[j] = t;
                        f = -sn * e[j];
                        e[j] *= cs;
                        for (int i = 0; i < N; i++) {
                            t = cs * arrayU[i][j] + sn * arrayU[i][k - 1];
                            arrayU[i][k - 1] = -sn * arrayU[i][j] + cs * arrayU[i][k - 1];
                            arrayU[i][j] = t;
                        }
                    }
                }
                break;
                // perform one QR step
                case 3: {
                    // calculate the shift
                    final double scale = Math.max(Math.max(Math.max(Math.max(Math.abs(arrayS[p - 1]), Math.abs(arrayS[p - 2])), Math.abs(e[p - 2])),
                            Math.abs(arrayS[k])), Math.abs(e[k]));
                    double sp = arrayS[p - 1] / scale;
                    double spm1 = arrayS[p - 2] / scale;
                    double epm1 = e[p - 2] / scale;
                    double sk = arrayS[k] / scale;
                    double ek = e[k] / scale;
                    double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
                    double c = (sp * epm1) * (sp * epm1);
                    double shift = 0.0;
                    if (b != 0.0 || c != 0.0) {
                        shift = Math.sqrt(b * b + c);
                        if (b < 0.0)
                            shift = -shift;
                        shift = c / (b + shift);
                    }
                    double f = (sk + sp) * (sk - sp) + shift;
                    double g = sk * ek;
                    // chase zeros
                    for (int j = k; j < p - 1; j++) {
                        double t = MathUtils.hypot(f, g);
                        double cs = f / t;
                        double sn = g / t;
                        if (j != k)
                            e[j - 1] = t;
                        f = cs * arrayS[j] + sn * e[j];
                        e[j] = cs * e[j] - sn * arrayS[j];
                        g = sn * arrayS[j + 1];
                        arrayS[j + 1] *= cs;
                        for (int i = 0; i < N; i++) {
                            t = cs * arrayV[i][j] + sn * arrayV[i][j + 1];
                            arrayV[i][j + 1] = -sn * arrayV[i][j] + cs * arrayV[i][j + 1];
                            arrayV[i][j] = t;
                        }
                        t = MathUtils.hypot(f, g);
                        cs = f / t;
                        sn = g / t;
                        arrayS[j] = t;
                        f = cs * e[j] + sn * arrayS[j + 1];
                        arrayS[j + 1] = -sn * e[j] + cs * arrayS[j + 1];
                        g = sn * e[j + 1];
                        e[j + 1] *= cs;
                        if (j < Nm1) {
                            for (int i = 0; i < N; i++) {
                                t = cs * arrayU[i][j] + sn * arrayU[i][j + 1];
                                arrayU[i][j + 1] = -sn * arrayU[i][j] + cs * arrayU[i][j + 1];
                                arrayU[i][j] = t;
                            }
                        }
                    }
                    e[p - 2] = f;
                    iter++;
                }
                break;
                // convergence
                case 4: {
                    // make the singular values positive
                    if (arrayS[k] <= 0.0) {
                        arrayS[k] = -arrayS[k];
                        for (int i = 0; i < p; i++)
                            arrayV[i][k] = -arrayV[i][k];
                    }
                    // order the singular values
                    while (k < p - 1) {
                        if (arrayS[k] >= arrayS[k + 1])
                            break;
                        double tmp = arrayS[k];
                        arrayS[k] = arrayS[k + 1];
                        arrayS[k + 1] = tmp;
                        if (k < Nm1) {
                            for (int i = 0; i < N; i++) {
                                tmp = arrayU[i][k + 1];
                                arrayU[i][k + 1] = arrayU[i][k];
                                arrayU[i][k] = tmp;
                                tmp = arrayV[i][k + 1];
                                arrayV[i][k + 1] = arrayV[i][k];
                                arrayV[i][k] = tmp;
                            }
                        }
                        k++;
                    }
                    iter = 0;
                    p--;
                }
                break;
            }
        }
        final AbstractDoubleSquareMatrix svd[] = new AbstractDoubleSquareMatrix[3];
        svd[0] = new DoubleSquareMatrix(arrayU);
        svd[1] = new DoubleDiagonalMatrix(arrayS);
        svd[2] = new DoubleSquareMatrix(arrayV);
        return svd;
    }

// MAP ELEMENTS

    /**
     * Applies a function on all the matrix elements.
     *
     * @param f a user-defined function
     * @return a double matrix
     */
    public AbstractDoubleMatrix mapElements(final PrimitiveMapping f) {
        final double[][] array = new double[numRows()][numColumns()];
        double val = f.map(0);
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                array[i][j] = val;
            }
        }
        array[0][0] = f.map(diag[0]);
        array[0][1] = f.map(udiag[0]);
        int n = numRows() - 1;
        for (int i = 1; i < n; i++) {
            array[i][i - 1] = f.map(ldiag[i]);
            array[i][i] = f.map(diag[i]);
            array[i][i + 1] = f.map(udiag[i]);
        }
        array[n][n - 1] = f.map(ldiag[n]);
        array[n][n] = f.map(diag[n]);
        return new DoubleMatrix(array);
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new DoubleTridiagonalMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an double array.
     */
    public double[][] toPrimitiveArray() {
        final double[][] result = new double[numRows()][numColumns()];
        result[0][0] = diag[0];
        result[0][1] = udiag[0];
        int n = numRows() - 1;
        for (int i = 1; i < n; i++) {
            result[i][i - 1] = ldiag[i];
            result[i][i] = diag[i];
            result[i][i + 1] = udiag[i];
        }
        result[n][n - 1] = ldiag[n];
        result[n][n] = diag[n];
        return result;
    }

}
