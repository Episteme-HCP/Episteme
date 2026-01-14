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

import org.jscience.mathematics.algebraic.DiagonalMatrix;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.TridiagonalMatrix;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.ComplexMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * The ComplexDiagonalMatrix class provides an object for encapsulating
 * diagonal matrices containing complex numbers. Uses compressed diagonal
 * storage.
 *
 * @author Mark Hale
 * @version 2.2
 */
public class ComplexDiagonalMatrix extends AbstractComplexSquareMatrix
    implements Cloneable, Serializable, DiagonalMatrix {
    /** Arrays containing the elements of the matrix. */
    protected final double[] diagRe;

    /** Arrays containing the elements of the matrix. */
    protected final double[] diagIm;

/**
     * Constructs a matrix by wrapping two arrays containing the diagonal elements.
     *
     * @param arrayRe an array of real values
     * @param arrayIm an array of imaginary values
     */
    public ComplexDiagonalMatrix(final double[] arrayRe, final double[] arrayIm) {
        super(arrayRe.length);
        diagRe = arrayRe;
        diagIm = arrayIm;
    }

/**
     * Constructs an empty matrix.
     *
     * @param size the number of rows/columns
     */
    public ComplexDiagonalMatrix(final int size) {
        this(new double[size], new double[size]);
    }

/**
     * Constructs a matrix from an array.
     *
     * @param array an assigned value
     * @throws IllegalDimensionException If the array is not square.
     */
    public ComplexDiagonalMatrix(final Complex[][] array) {
        this(array.length);

        for (int i = 0; i < numRows(); i++) {
            if (array[i].length != array.length) {
                throw new IllegalDimensionException("Array is not square.");
            }

            diagRe[i] = array[i][i].real();
            diagIm[i] = array[i][i].imag();
        }
    }

/**
     * Constructs a matrix from an array containing the diagonal elements.
     *
     * @param array an assigned value
     */
    public ComplexDiagonalMatrix(final Complex[] array) {
        this(array.length);
        diagRe[0] = array[0].real();
        diagIm[0] = array[0].imag();

        for (int i = 1; i < array.length; i++) {
            diagRe[i] = array[i].real();
            diagIm[i] = array[i].imag();
        }
    }

/**
     * Copy constructor.
     *
     * @param mat an assigned value
     */
    public ComplexDiagonalMatrix(final ComplexDiagonalMatrix mat) {
        this(mat.numRows());
        System.arraycopy(mat.diagRe, 0, diagRe, 0, mat.diagRe.length);
        System.arraycopy(mat.diagIm, 0, diagIm, 0, mat.diagIm.length);
    }

    /**
     * Creates an identity matrix.
     *
     * @param size the number of rows/columns
     *
     * @return DOCUMENT ME!
     */
    public static ComplexDiagonalMatrix identity(final int size) {
        final double[] arrayRe = new double[size];
        final double[] arrayIm = new double[size];

        for (int i = 0; i < size; i++)
            arrayRe[i] = 1.0;

        return new ComplexDiagonalMatrix(arrayRe, arrayIm);
    }

    /**
     * Compares two complex diagonal matrices for equality.
     *
     * @param m a complex diagonal matrix
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(AbstractComplexMatrix m, double tol) {
        if (m instanceof DiagonalMatrix) {
            if ((numRows() != m.numRows()) || (numColumns() != m.numColumns())) {
                return false;
            }

            double sumSqr = 0;
            double deltaRe = diagRe[0] - m.getRealElement(0, 0);
            double deltaIm = diagIm[0] - m.getImagElement(0, 0);
            sumSqr += ((deltaRe * deltaRe) + (deltaIm * deltaIm));

            for (int i = 1; i < numRows(); i++) {
                deltaRe = diagRe[i] - m.getRealElement(i, i);
                deltaIm = diagIm[i] - m.getImagElement(i, i);
                sumSqr += ((deltaRe * deltaRe) + (deltaIm * deltaIm));
            }

            return (sumSqr <= (tol * tol));
        } else {
            return super.equals(m);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK1() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK2() {
        return 0;
    }

    /**
     * Returns the real part of this complex matrix.
     *
     * @return a double diagonal matrix
     */
    public AbstractDoubleMatrix real() {
        return new DoubleDiagonalMatrix(diagRe);
    }

    /**
     * Returns the imaginary part of this complex matrix.
     *
     * @return a double diagonal matrix
     */
    public AbstractDoubleMatrix imag() {
        return new DoubleDiagonalMatrix(diagIm);
    }

    /**
     * Returns an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         element.
     */
    public Complex getPrimitiveElement(final int i, final int j) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns())) {
            if (i == j) {
                return new Complex(diagRe[i], diagIm[i]);
            } else {
                return Complex.ZERO;
            }
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRealElement(final int i, final int j) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns())) {
            if (i == j) {
                return diagRe[i];
            } else {
                return 0.0;
            }
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getImagElement(final int i, final int j) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns())) {
            if (i == j) {
                return diagIm[i];
            } else {
                return 0.0;
            }
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        }
    }

    /**
     * Sets the value of an element of the matrix. Should only be used
     * to initialise this matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @param z a complex number
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         element.
     */
    public void setElement(final int i, final int j, final Complex z) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns()) &&
                (i == j)) {
            diagRe[i] = z.real();
            diagIm[i] = z.imag();
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        }
    }

    /**
     * Sets the value of an element of the matrix. Should only be used
     * to initialise this matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @param x the real part of a complex number
     * @param y the imaginary part of a complex number
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         element.
     */
    public void setElement(final int i, final int j, final double x,
        final double y) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns()) &&
                (i == j)) {
            diagRe[i] = x;
            diagIm[i] = y;
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        }
    }

    /**
     * Sets the value of all elements of the matrix. This method will
     * throw an error unless the paramter is Complex.ZERO. This is because you
     * need a SquareMatrix to set all the contents of the matrix to a value.
     * You should think about using a ComplexSquareMatrix.
     *
     * @param r a Complex element
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setAllElements(final Complex r) {
        if (r.equals(Complex.ZERO)) {
            for (int i = 0; i < numRows(); i++) {
                diagRe[i] = 0;
                diagIm[i] = 0;
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set all elements of a diagonal matrix unless to zero.");
        }
    }

    /**
     * Returns true if this matrix is symmetric.
     *
     * @return DOCUMENT ME!
     */
    public boolean isSymmetric() {
        return true;
    }

    /**
     * Returns the determinant.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public Complex det() {
        if (numElements() > 0) {
            double detRe = diagRe[0];
            double detIm = diagIm[0];

            for (int i = 1; i < numRows(); i++) {
                double tmp = (detRe * diagRe[i]) - (detIm * diagIm[i]);
                detIm = (detIm * diagRe[i]) + (detRe * diagIm[i]);
                detRe = tmp;
            }

            return new Complex(detRe, detIm);
        } else {
            throw new ArithmeticException(
                "The determinant of a zero dimension matrix is undefined.");
        }
    }

    /**
     * Returns the trace.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public Complex trace() {
        if (numElements() > 0) {
            double trRe = diagRe[0];
            double trIm = diagIm[0];

            for (int i = 1; i < numRows(); i++) {
                trRe += diagRe[i];
                trIm += diagIm[i];
            }

            return new Complex(trRe, trIm);
        } else {
            throw new ArithmeticException(
                "The trace of a zero dimension matrix is undefined.");
        }
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity"
     * src="doc-files/infinity.gif"></sup>-norm.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public double infNorm() {
        if (numElements() > 0) {
            double result = (diagRe[0] * diagRe[0]) + (diagIm[0] * diagIm[0]);
            double tmpResult;

            for (int i = 1; i < numRows(); i++) {
                tmpResult = (diagRe[i] * diagRe[i]) + (diagIm[i] * diagIm[i]);

                if (tmpResult > result) {
                    result = tmpResult;
                }
            }

            return Math.sqrt(result);
        } else {
            throw new ArithmeticException(
                "The inf norm of a zero dimension matrix is undefined.");
        }
    }

    /**
     * Returns the Frobenius (l<sup>2</sup>) norm.
     *
     * @return DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    public double frobeniusNorm() {
        if (numElements() > 0) {
            double result = (diagRe[0] * diagRe[0]) + (diagIm[0] * diagIm[0]);

            for (int i = 1; i < numRows(); i++)
                result += ((diagRe[i] * diagRe[i]) + (diagIm[i] * diagIm[i]));

            return Math.sqrt(result);
        } else {
            throw new ArithmeticException(
                "The frobenius norm of a zero dimension matrix is undefined.");
        }
    }

    /**
     * Returns the operator norm.
     *
     * @return DOCUMENT ME!
     */
    public double operatorNorm() {
        return infNorm();
    }

    //============
    // OPERATIONS
    //============

    // ADDITION
    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a complex matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexSquareMatrix add(final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexDiagonalMatrix) {
            return add((ComplexDiagonalMatrix) m);
        }

        if (m instanceof ComplexTridiagonalMatrix) {
            return add((ComplexTridiagonalMatrix) m);
        }

        if (m instanceof TridiagonalMatrix) {
            return addTridiagonal(m);
        }

        if (m instanceof ComplexSquareMatrix) {
            return add((ComplexSquareMatrix) m);
        }

        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] arrayRe = new double[numRows()][numColumns()];
            final double[][] arrayIm = new double[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                Complex elem = m.getPrimitiveElement(i, 0);
                arrayRe[i][0] = elem.real();
                arrayIm[i][0] = elem.imag();

                for (int j = 1; j < numColumns(); j++) {
                    elem = m.getPrimitiveElement(i, j);
                    arrayRe[i][j] = elem.real();
                    arrayIm[i][j] = elem.imag();
                }
            }

            for (int i = 0; i < numRows(); i++) {
                arrayRe[i][i] += diagRe[i];
                arrayIm[i][i] += diagIm[i];
            }

            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComplexSquareMatrix add(final ComplexSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] arrayRe = new double[numRows()][numColumns()];
            final double[][] arrayIm = new double[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                System.arraycopy(m.matrixRe[i], 0, arrayRe[i], 0, numColumns());
                System.arraycopy(m.matrixIm[i], 0, arrayIm[i], 0, numColumns());
            }

            for (int i = 0; i < numRows(); i++) {
                arrayRe[i][i] += diagRe[i];
                arrayIm[i][i] += diagIm[i];
            }

            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a complex tridiagonal matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexTridiagonalMatrix add(final ComplexTridiagonalMatrix m) {
        if (numRows() == m.numRows()) {
            final ComplexTridiagonalMatrix ans = new ComplexTridiagonalMatrix(numRows());
            System.arraycopy(m.ldiagRe, 0, ans.ldiagRe, 0, m.ldiagRe.length);
            System.arraycopy(m.ldiagIm, 0, ans.ldiagIm, 0, m.ldiagIm.length);
            System.arraycopy(m.udiagRe, 0, ans.udiagRe, 0, m.udiagRe.length);
            System.arraycopy(m.udiagIm, 0, ans.udiagIm, 0, m.udiagIm.length);
            ans.diagRe[0] = diagRe[0] + m.diagRe[0];
            ans.diagIm[0] = diagIm[0] + m.diagIm[0];

            for (int i = 1; i < numRows(); i++) {
                ans.diagRe[i] = diagRe[i] + m.diagRe[i];
                ans.diagIm[i] = diagIm[i] + m.diagIm[i];
            }

            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ComplexTridiagonalMatrix addTridiagonal(
        final AbstractComplexSquareMatrix m) {
        int mRow = numRows();

        if (mRow == m.numRows()) {
            final ComplexTridiagonalMatrix ans = new ComplexTridiagonalMatrix(mRow);
            Complex elem = m.getPrimitiveElement(0, 0);
            ans.diagRe[0] = diagRe[0] + elem.real();
            ans.diagIm[0] = diagIm[0] + elem.imag();
            elem = m.getPrimitiveElement(0, 1);
            ans.udiagRe[0] = elem.real();
            ans.udiagIm[0] = elem.imag();
            mRow--;

            for (int i = 1; i < mRow; i++) {
                elem = m.getPrimitiveElement(i, i - 1);
                ans.ldiagRe[i] = elem.real();
                ans.ldiagIm[i] = elem.imag();
                elem = m.getPrimitiveElement(i, i);
                ans.diagRe[i] = diagRe[i] + elem.real();
                ans.diagIm[i] = diagIm[i] + elem.imag();
                elem = m.getPrimitiveElement(i, i + 1);
                ans.udiagRe[i] = elem.real();
                ans.udiagIm[i] = elem.imag();
            }

            elem = m.getPrimitiveElement(mRow, mRow - 1);
            ans.ldiagRe[mRow] = elem.real();
            ans.ldiagIm[mRow] = elem.imag();
            elem = m.getPrimitiveElement(mRow, mRow);
            ans.diagRe[mRow] = diagRe[mRow] + elem.real();
            ans.diagIm[mRow] = diagIm[mRow] + elem.imag();

            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a complex diagonal matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexDiagonalMatrix add(final ComplexDiagonalMatrix m) {
        if (numRows() == m.numRows()) {
            final double[] arrayRe = new double[numRows()];
            final double[] arrayIm = new double[numRows()];
            arrayRe[0] = diagRe[0] + m.diagRe[0];
            arrayIm[0] = diagIm[0] + m.diagIm[0];

            for (int i = 1; i < numRows(); i++) {
                arrayRe[i] = diagRe[i] + m.diagRe[i];
                arrayIm[i] = diagIm[i] + m.diagIm[i];
            }

            return new ComplexDiagonalMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this matrix by another.
     *
     * @param m a complex matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexSquareMatrix subtract(
        final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexDiagonalMatrix) {
            return subtract((ComplexDiagonalMatrix) m);
        }

        if (m instanceof ComplexTridiagonalMatrix) {
            return subtract((ComplexTridiagonalMatrix) m);
        }

        if (m instanceof TridiagonalMatrix) {
            return subtractTridiagonal(m);
        }

        if (m instanceof ComplexSquareMatrix) {
            return subtract((ComplexSquareMatrix) m);
        }

        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] arrayRe = new double[numRows()][numColumns()];
            final double[][] arrayIm = new double[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                Complex elem = m.getPrimitiveElement(i, 0);
                arrayRe[i][0] = -elem.real();
                arrayIm[i][0] = -elem.imag();

                for (int j = 1; j < numColumns(); j++) {
                    elem = m.getPrimitiveElement(i, j);
                    arrayRe[i][j] = -elem.real();
                    arrayIm[i][j] = -elem.imag();
                }
            }

            for (int i = 0; i < numRows(); i++) {
                arrayRe[i][i] += diagRe[i];
                arrayIm[i][i] += diagIm[i];
            }

            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComplexSquareMatrix subtract(final ComplexSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] arrayRe = new double[numRows()][numColumns()];
            final double[][] arrayIm = new double[numRows()][numColumns()];

            for (int j, i = 0; i < numRows(); i++) {
                arrayRe[i][0] = -m.matrixRe[i][0];
                arrayIm[i][0] = -m.matrixIm[i][0];

                for (j = 1; j < numColumns(); j++) {
                    arrayRe[i][j] = -m.matrixRe[i][j];
                    arrayIm[i][j] = -m.matrixIm[i][j];
                }
            }

            for (int i = 0; i < numRows(); i++) {
                arrayRe[i][i] += diagRe[i];
                arrayIm[i][i] += diagIm[i];
            }

            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComplexTridiagonalMatrix subtract(final ComplexTridiagonalMatrix m) {
        int mRow = numRows();

        if (mRow == m.numRows()) {
            final ComplexTridiagonalMatrix ans = new ComplexTridiagonalMatrix(mRow);
            ans.diagRe[0] = diagRe[0] - m.diagRe[0];
            ans.diagIm[0] = diagIm[0] - m.diagIm[0];
            ans.udiagRe[0] = -m.udiagRe[0];
            ans.udiagIm[0] = -m.udiagIm[0];
            mRow--;

            for (int i = 1; i < mRow; i++) {
                ans.ldiagRe[i] = -m.ldiagRe[i];
                ans.ldiagIm[i] = -m.ldiagIm[i];
                ans.diagRe[i] = diagRe[i] - m.diagRe[i];
                ans.diagIm[i] = diagIm[i] - m.diagIm[i];
                ans.udiagRe[i] = -m.udiagRe[i];
                ans.udiagIm[i] = -m.udiagIm[i];
            }

            ans.ldiagRe[mRow] = -m.ldiagRe[mRow];
            ans.ldiagIm[mRow] = -m.ldiagIm[mRow];
            ans.diagRe[mRow] = diagRe[mRow] - m.diagRe[mRow];
            ans.diagIm[mRow] = diagIm[mRow] - m.diagIm[mRow];

            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the subtraction of this matrix by another.
     *
     * @param m a complex tridiagonal matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    private ComplexTridiagonalMatrix subtractTridiagonal(
        final AbstractComplexSquareMatrix m) {
        int mRow = numRows();

        if (mRow == m.numRows()) {
            final ComplexTridiagonalMatrix ans = new ComplexTridiagonalMatrix(mRow);
            Complex elem = m.getPrimitiveElement(0, 0);
            ans.diagRe[0] = diagRe[0] - elem.real();
            ans.diagIm[0] = diagIm[0] - elem.imag();
            elem = m.getPrimitiveElement(0, 1);
            ans.udiagRe[0] = -elem.real();
            ans.udiagIm[0] = -elem.imag();
            mRow--;

            for (int i = 1; i < mRow; i++) {
                elem = m.getPrimitiveElement(i, i - 1);
                ans.ldiagRe[i] = -elem.real();
                ans.ldiagIm[i] = -elem.imag();
                elem = m.getPrimitiveElement(i, i);
                ans.diagRe[i] = diagRe[i] - elem.real();
                ans.diagIm[i] = diagIm[i] - elem.imag();
                elem = m.getPrimitiveElement(i, i + 1);
                ans.udiagRe[i] = -elem.real();
                ans.udiagIm[i] = -elem.imag();
            }

            elem = m.getPrimitiveElement(mRow, mRow - 1);
            ans.ldiagRe[mRow] = -elem.real();
            ans.ldiagIm[mRow] = -elem.imag();
            elem = m.getPrimitiveElement(mRow, mRow);
            ans.diagRe[mRow] = diagRe[mRow] - elem.real();
            ans.diagIm[mRow] = diagIm[mRow] - elem.imag();

            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the subtraction of this matrix by another.
     *
     * @param m a complex diagonal matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexDiagonalMatrix subtract(final ComplexDiagonalMatrix m) {
        if (numRows() == m.numRows()) {
            final double[] arrayRe = new double[numRows()];
            final double[] arrayIm = new double[numRows()];
            arrayRe[0] = diagRe[0] - m.diagRe[0];
            arrayIm[0] = diagIm[0] - m.diagIm[0];

            for (int i = 1; i < numRows(); i++) {
                arrayRe[i] = diagRe[i] - m.diagRe[i];
                arrayIm[i] = diagIm[i] - m.diagIm[i];
            }

            return new ComplexDiagonalMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // SCALAR MULTIPLY
    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param z a complex number
     *
     * @return a complex diagonal matrix
     */
    public AbstractComplexMatrix scalarMultiply(final Complex z) {
        final double real = z.real();
        final double imag = z.imag();
        final double[] arrayRe = new double[numRows()];
        final double[] arrayIm = new double[numRows()];
        arrayRe[0] = (real * diagRe[0]) - (imag * diagIm[0]);
        arrayIm[0] = (imag * diagRe[0]) + (real * diagIm[0]);

        for (int i = 1; i < numRows(); i++) {
            arrayRe[i] = (real * diagRe[i]) - (imag * diagIm[i]);
            arrayIm[i] = (imag * diagRe[i]) + (real * diagIm[i]);
        }

        return new ComplexDiagonalMatrix(arrayRe, arrayIm);
    }

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a double
     *
     * @return a complex diagonal matrix
     */
    public AbstractComplexMatrix scalarMultiply(final double x) {
        final double[] arrayRe = new double[numRows()];
        final double[] arrayIm = new double[numRows()];
        arrayRe[0] = x * diagRe[0];
        arrayIm[0] = x * diagIm[0];

        for (int i = 1; i < numRows(); i++) {
            arrayRe[i] = x * diagRe[i];
            arrayIm[i] = x * diagIm[i];
        }

        return new ComplexDiagonalMatrix(arrayRe, arrayIm);
    }

    // MATRIX MULTIPLICATION
    /**
     * Returns the multiplication of a vector by this matrix.
     *
     * @param v a complex vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrix and vector are
     *         incompatible.
     */
    public AbstractComplexVector multiply(final AbstractComplexVector v) {
        if (numColumns() == v.getDimension()) {
            final double[] arrayRe = new double[numRows()];
            final double[] arrayIm = new double[numRows()];
            Complex comp = v.getPrimitiveElement(0);
            arrayRe[0] = ((diagRe[0] * comp.real()) -
                (diagIm[0] * comp.imag()));
            arrayIm[0] = ((diagIm[0] * comp.real()) +
                (diagRe[0] * comp.imag()));

            for (int i = 1; i < numRows(); i++) {
                comp = v.getPrimitiveElement(i);
                arrayRe[i] = ((diagRe[i] * comp.real()) -
                    (diagIm[i] * comp.imag()));
                arrayIm[i] = ((diagIm[i] * comp.real()) +
                    (diagRe[i] * comp.imag()));
            }

            return new ComplexVector(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException(
                "Matrix and vector are incompatible.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a complex matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexSquareMatrix multiply(
        final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexDiagonalMatrix) {
            return multiply((ComplexDiagonalMatrix) m);
        }

        if (m instanceof ComplexTridiagonalMatrix) {
            return multiply((ComplexTridiagonalMatrix) m);
        }

        if (m instanceof TridiagonalMatrix) {
            return multiplyTridiagonal(m);
        }

        if (m instanceof ComplexSquareMatrix) {
            return multiply((ComplexSquareMatrix) m);
        }

        if (numColumns() == m.numRows()) {
            final double[][] arrayRe = new double[numRows()][m.numColumns()];
            final double[][] arrayIm = new double[numRows()][m.numColumns()];
            Complex elem;

            for (int i = 0; i < numRows(); i++) {
                elem = m.getPrimitiveElement(i, 0);
                arrayRe[i][0] = ((diagRe[i] * elem.real()) -
                    (diagIm[i] * elem.imag()));
                arrayIm[i][0] = ((diagIm[i] * elem.real()) +
                    (diagRe[i] * elem.imag()));

                for (int j = 1; j < m.numColumns(); j++) {
                    elem = m.getPrimitiveElement(i, j);
                    arrayRe[i][j] = ((diagRe[i] * elem.real()) -
                        (diagIm[i] * elem.imag()));
                    arrayIm[i][j] = ((diagIm[i] * elem.real()) +
                        (diagRe[i] * elem.imag()));
                }
            }

            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a complex square matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexSquareMatrix multiply(final ComplexSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            final double[][] arrayRe = new double[numRows()][numColumns()];
            final double[][] arrayIm = new double[numRows()][numColumns()];

            for (int j, i = 0; i < numRows(); i++) {
                arrayRe[i][0] = ((diagRe[i] * m.matrixRe[i][0]) -
                    (diagIm[i] * m.matrixIm[i][0]));
                arrayIm[i][0] = ((diagIm[i] * m.matrixRe[i][0]) +
                    (diagRe[i] * m.matrixIm[i][0]));

                for (j = 1; j < numColumns(); j++) {
                    arrayRe[i][j] = ((diagRe[i] * m.matrixRe[i][j]) -
                        (diagIm[i] * m.matrixIm[i][j]));
                    arrayIm[i][j] = ((diagIm[i] * m.matrixRe[i][j]) +
                        (diagRe[i] * m.matrixIm[i][j]));
                }
            }

            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a complex tridiagonal matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexTridiagonalMatrix multiply(final ComplexTridiagonalMatrix m) {
        int mRow = numRows();

        if (numColumns() == m.numRows()) {
            final ComplexTridiagonalMatrix ans = new ComplexTridiagonalMatrix(mRow);
            ans.diagRe[0] = ((diagRe[0] * m.diagRe[0]) -
                (diagIm[0] * m.diagIm[0]));
            ans.diagIm[0] = ((diagIm[0] * m.diagRe[0]) +
                (diagRe[0] * m.diagIm[0]));
            ans.udiagRe[0] = ((diagRe[0] * m.udiagRe[0]) -
                (diagIm[0] * m.udiagIm[0]));
            ans.udiagIm[0] = ((diagIm[0] * m.udiagRe[0]) +
                (diagRe[0] * m.udiagIm[0]));
            mRow--;

            for (int i = 1; i < mRow; i++) {
                ans.ldiagRe[i] = ((diagRe[i] * m.ldiagRe[i]) -
                    (diagIm[i] * m.ldiagIm[i]));
                ans.ldiagIm[i] = ((diagIm[i] * m.ldiagRe[i]) +
                    (diagRe[i] * m.ldiagIm[i]));
                ans.diagRe[i] = ((diagRe[i] * m.diagRe[i]) -
                    (diagIm[i] * m.diagIm[i]));
                ans.diagIm[i] = ((diagIm[i] * m.diagRe[i]) +
                    (diagRe[i] * m.diagIm[i]));
                ans.udiagRe[i] = ((diagRe[i] * m.udiagRe[i]) -
                    (diagIm[i] * m.udiagIm[i]));
                ans.udiagIm[i] = ((diagIm[i] * m.udiagRe[i]) +
                    (diagRe[i] * m.udiagIm[i]));
            }

            ans.ldiagRe[mRow] = ((diagRe[mRow] * m.ldiagRe[mRow]) -
                (diagIm[mRow] * m.ldiagIm[mRow]));
            ans.ldiagIm[mRow] = ((diagIm[mRow] * m.ldiagRe[mRow]) +
                (diagRe[mRow] * m.ldiagIm[mRow]));
            ans.diagRe[mRow] = ((diagRe[mRow] * m.diagRe[mRow]) -
                (diagIm[mRow] * m.diagIm[mRow]));
            ans.diagIm[mRow] = ((diagIm[mRow] * m.diagRe[mRow]) +
                (diagRe[mRow] * m.diagIm[mRow]));

            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ComplexTridiagonalMatrix multiplyTridiagonal(
        final AbstractComplexSquareMatrix m) {
        int mRow = numRows();

        if (numColumns() == m.numRows()) {
            final ComplexTridiagonalMatrix ans = new ComplexTridiagonalMatrix(mRow);
            Complex elem = m.getPrimitiveElement(0, 0);
            ans.diagRe[0] = ((diagRe[0] * elem.real()) -
                (diagIm[0] * elem.imag()));
            ans.diagIm[0] = ((diagIm[0] * elem.real()) +
                (diagRe[0] * elem.imag()));
            elem = m.getPrimitiveElement(0, 1);
            ans.udiagRe[0] = ((diagRe[0] * elem.real()) -
                (diagIm[0] * elem.imag()));
            ans.udiagIm[0] = ((diagIm[0] * elem.real()) +
                (diagRe[0] * elem.imag()));
            mRow--;

            for (int i = 1; i < mRow; i++) {
                elem = m.getPrimitiveElement(i, i - 1);
                ans.ldiagRe[i] = ((diagRe[i] * elem.real()) -
                    (diagIm[i] * elem.imag()));
                ans.ldiagIm[i] = ((diagIm[i] * elem.real()) +
                    (diagRe[i] * elem.imag()));
                elem = m.getPrimitiveElement(i, i);
                ans.diagRe[i] = ((diagRe[i] * elem.real()) -
                    (diagIm[i] * elem.imag()));
                ans.diagIm[i] = ((diagIm[i] * elem.real()) +
                    (diagRe[i] * elem.imag()));
                elem = m.getPrimitiveElement(i, i + 1);
                ans.udiagRe[i] = ((diagRe[i] * elem.real()) -
                    (diagIm[i] * elem.imag()));
                ans.udiagIm[i] = ((diagIm[i] * elem.real()) +
                    (diagRe[i] * elem.imag()));
            }

            elem = m.getPrimitiveElement(mRow, mRow - 1);
            ans.ldiagRe[mRow] = ((diagRe[mRow] * elem.real()) -
                (diagIm[mRow] * elem.imag()));
            ans.ldiagIm[mRow] = ((diagIm[mRow] * elem.real()) +
                (diagRe[mRow] * elem.imag()));
            elem = m.getPrimitiveElement(mRow, mRow);
            ans.diagRe[mRow] = ((diagRe[mRow] * elem.real()) -
                (diagIm[mRow] * elem.imag()));
            ans.diagIm[mRow] = ((diagIm[mRow] * elem.real()) +
                (diagRe[mRow] * elem.imag()));

            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a complex diagonal matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexDiagonalMatrix multiply(final ComplexDiagonalMatrix m) {
        if (numColumns() == m.numRows()) {
            final double[] arrayRe = new double[numRows()];
            final double[] arrayIm = new double[numRows()];
            arrayRe[0] = ((diagRe[0] * m.diagRe[0]) -
                (diagIm[0] * m.diagIm[0]));
            arrayIm[0] = ((diagIm[0] * m.diagRe[0]) +
                (diagRe[0] * m.diagIm[0]));

            for (int i = 1; i < numRows(); i++) {
                arrayRe[i] = ((diagRe[i] * m.diagRe[i]) -
                    (diagIm[i] * m.diagIm[i]));
                arrayIm[i] = ((diagIm[i] * m.diagRe[i]) +
                    (diagRe[i] * m.diagIm[i]));
            }

            return new ComplexDiagonalMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // INVERSE
    /**
     * Returns the inverse of this matrix.
     *
     * @return a complex diagonal matrix
     */
    public AbstractComplexSquareMatrix inverse() {
        final double[] arrayRe = new double[numRows()];
        final double[] arrayIm = new double[numRows()];
        double denom = (diagRe[0] * diagRe[0]) + (diagIm[0] * diagIm[0]);
        arrayRe[0] = diagRe[0] / denom;
        arrayIm[0] = -diagIm[0] / denom;

        for (int i = 1; i < numRows(); i++) {
            denom = (diagRe[i] * diagRe[i]) + (diagIm[i] * diagIm[i]);
            arrayRe[i] = diagRe[i] / denom;
            arrayIm[i] = -diagIm[i] / denom;
        }

        return new ComplexDiagonalMatrix(arrayRe, arrayIm);
    }

    // HERMITIAN ADJOINT
    /**
     * Returns the hermitian adjoint of this matrix.
     *
     * @return a complex diagonal matrix
     */
    public AbstractComplexMatrix hermitianAdjoint() {
        return conjugate();
    }

    // CONJUGATE
    /**
     * Returns the complex conjugate of this matrix.
     *
     * @return a complex diagonal matrix
     */
    public AbstractComplexMatrix conjugate() {
        final double[] arrayIm = new double[numRows()];
        arrayIm[0] = -diagIm[0];

        for (int i = 1; i < numRows(); i++)
            arrayIm[i] = -diagIm[i];

        return new ComplexDiagonalMatrix(diagRe, arrayIm);
    }

    // TRANSPOSE
    /**
     * Returns the transpose of this matrix.
     *
     * @return a complex diagonal matrix
     */
    public Matrix transpose() {
        return this;
    }

    // LU DECOMPOSITION
    /**
     * Returns the LU decomposition of this matrix.
     *
     * @param pivot DOCUMENT ME!
     *
     * @return an array with [0] containing the L-matrix and [1] containing the
     *         U-matrix.
     */
    public AbstractComplexSquareMatrix[] luDecompose(int[] pivot) {
        if (LU != null) {
            if (pivot != null) {
                System.arraycopy(LUpivot, 0, pivot, 0, pivot.length);
            }

            return LU;
        }

        if (pivot == null) {
            pivot = new int[numRows() + 1];
        }

        for (int i = 0; i < numRows(); i++)
            pivot[i] = i;

        pivot[numRows()] = 1;
        LU = new AbstractComplexSquareMatrix[2];
        LU[0] = identity(numRows());
        LU[1] = this;
        LUpivot = new int[pivot.length];
        System.arraycopy(pivot, 0, LUpivot, 0, pivot.length);

        return LU;
    }

    // MAP ELEMENTS
    /**
     * Applies a function on all the matrix elements.
     *
     * @param f a user-defined function
     *
     * @return a Complex matrix
     */
    public AbstractComplexMatrix mapElements(final ComplexMapping f) {
        final Complex[][] ans = new Complex[numRows()][numColumns()];
        Complex val = f.map(Complex.ZERO);

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                ans[i][j] = val;
            }
        }

        for (int i = 0; i < numRows(); i++)
            ans[i][i] = f.map(new Complex(diagRe[i], diagIm[i]));

        return new ComplexMatrix(ans);
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new ComplexDiagonalMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an Complex array.
     */
    public Complex[][] toPrimitiveArray() {
        final Complex[][] ans = new Complex[numRows()][numColumns()];

        for (int i = 0; i < numRows(); i++)
            ans[i][i] = new Complex(diagRe[i], diagIm[i]);

        return ans;
    }
}
