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
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.ComplexMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * The DoubleSparseSquareMatrix class provides an object for encapsulating
 * sparse square matrices. Uses compressed row storage.
 *
 * @author Mark Hale
 * @version 1.2
 */
public class ComplexSparseSquareMatrix extends AbstractComplexSquareMatrix
    implements Cloneable, Serializable {
    /** Matrix elements. */
    private Complex[] elements;

    /**
     * Sparse indexing data. Contains the column positions of each
     * element, e.g. <code>colPos[n]</code> is the column position of the
     * <code>n</code>th element.
     */
    private int[] colPos;

    /**
     * Sparse indexing data. Contains the indices of the start of each
     * row, e.g. <code>rows[i]</code> is the index where the <code>i</code>th
     * row starts.
     */
    private int[] rows;

/**
     * Constructs an empty matrix.
     *
     * @param size the number of rows/columns
     */
    public ComplexSparseSquareMatrix(final int size) {
        super(size);
        elements = new Complex[0];
        colPos = new int[0];
        rows = new int[numRows() + 1];
    }

/**
     * Constructs a matrix from an array.
     *
     * @param array an assigned value
     * @throws IllegalDimensionException If the array is not square.
     */
    public ComplexSparseSquareMatrix(final Complex[][] array) {
        super(array.length);
        rows = new int[numRows() + 1];

        int n = 0;

        for (int i = 0; i < numRows(); i++) {
            if (array[i].length != array.length) {
                throw new IllegalDimensionException("Array is not square.");
            }

            for (int j = 0; j < numColumns(); j++) {
                if ((Math.abs(array[i][j].real()) > java.lang.Double.valueOf(
                            JScience.getProperty("tolerance")).doubleValue()) ||
                        (Math.abs(array[i][j].imag()) > java.lang.Double.valueOf(
                            JScience.getProperty("tolerance")).doubleValue())) {
                    n++;
                }
            }
        }

        elements = new Complex[n];
        colPos = new int[n];
        n = 0;

        for (int i = 0; i < numRows(); i++) {
            rows[i] = n;

            for (int j = 0; j < numColumns(); j++) {
                if ((Math.abs(array[i][j].real()) > java.lang.Double.valueOf(
                            JScience.getProperty("tolerance")).doubleValue()) ||
                        (Math.abs(array[i][j].imag()) > java.lang.Double.valueOf(
                            JScience.getProperty("tolerance")).doubleValue())) {
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
    public ComplexSparseSquareMatrix(final ComplexSparseSquareMatrix mat) {
        this(mat.numRows());
        System.arraycopy(mat.elements, 0, elements, 0, mat.elements.length);
        System.arraycopy(mat.colPos, 0, colPos, 0, mat.colPos.length);
        System.arraycopy(mat.rows, 0, rows, 0, mat.rows.length);
    }

    /**
     * Compares two Complex sparse square matrices for equality.
     *
     * @param m a Complex matrix
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(AbstractComplexSquareMatrix m, double tol) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            if (m instanceof ComplexSparseSquareMatrix) {
                return this.equals((ComplexSparseSquareMatrix) m);
            } else {
                double sumSqr = 0;

                for (int i = 0; i < numRows(); i++) {
                    for (int j = 0; j < numColumns(); j++) {
                        double deltaRe = getPrimitiveElement(i, j).real() -
                            m.getPrimitiveElement(i, j).real();
                        double deltaIm = getPrimitiveElement(i, j).imag() -
                            m.getPrimitiveElement(i, j).imag();
                        sumSqr += ((deltaRe * deltaRe) + (deltaIm * deltaIm));
                    }
                }

                return (sumSqr <= (tol * tol));
            }
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(ComplexSparseSquareMatrix m) {
        return equals(m,
            java.lang.Double.valueOf(JScience.getProperty("tolerance"))
                            .doubleValue());
    }

    //should we use the complex norm here ???
    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(ComplexSparseSquareMatrix m, double tol) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            if (colPos.length != m.colPos.length) {
                return false;
            }

            for (int i = 1; i < rows.length; i++) {
                if (rows[i] != m.rows[i]) {
                    return false;
                }
            }

            double sumSqr = 0.0;

            for (int i = 1; i < colPos.length; i++) {
                if (colPos[i] != m.colPos[i]) {
                    return false;
                }

                double delta = Math.abs(elements[i].real() -
                        m.elements[i].real()) +
                    Math.abs(elements[i].imag() - m.elements[i].imag());
                sumSqr += (delta * delta);
            }

            return (sumSqr <= (tol * tol));
        } else {
            return false;
        }
    }

    /**
     * Returns a string representing this matrix.
     *
     * @return DOCUMENT ME!
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
            for (int k = rows[i]; k < rows[i + 1]; k++) {
                if (colPos[k] == j) {
                    return elements[k];
                }
            }

            return Complex.ZERO;
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        }
    }

    /**
     * Sets the value of an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @param x a number
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         element.
     */
    public void setElement(final int i, final int j, final Complex x) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns())) {
            if ((Math.abs(x.real()) <= java.lang.Double.valueOf(
                        JScience.getProperty("tolerance")).doubleValue()) &&
                    (Math.abs(x.imag()) <= java.lang.Double.valueOf(
                        JScience.getProperty("tolerance")).doubleValue())) {
                return;
            }

            for (int k = rows[i]; k < rows[i + 1]; k++) {
                if (colPos[k] == j) {
                    elements[k] = x;

                    return;
                }
            }

            final Complex[] oldMatrix = elements;
            final int[] oldColPos = colPos;
            elements = new Complex[oldMatrix.length + 1];
            colPos = new int[oldColPos.length + 1];
            System.arraycopy(oldMatrix, 0, elements, 0, rows[i]);
            System.arraycopy(oldColPos, 0, colPos, 0, rows[i]);

            int k;

            for (k = rows[i]; (k < rows[i + 1]) && (oldColPos[k] < j); k++) {
                elements[k] = oldMatrix[k];
                colPos[k] = oldColPos[k];
            }

            elements[k] = x;
            colPos[k] = j;
            System.arraycopy(oldMatrix, k, elements, k + 1, oldMatrix.length -
                k);
            System.arraycopy(oldColPos, k, colPos, k + 1, oldColPos.length - k);

            for (k = i + 1; k < rows.length; k++)
                rows[k]++;
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        }
    }

    /**
     * Sets the value of all elements of the matrix. You should think
     * about using a ComplexSquareMatrix.
     *
     * @param m a complex element
     */
    public void setAllElements(final Complex m) {
        if ((Math.abs(m.real()) <= java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")).doubleValue()) &&
                (Math.abs(m.imag()) <= java.lang.Double.valueOf(
                    JScience.getProperty("tolerance")).doubleValue())) {
            elements = new Complex[0];
            colPos = new int[0];
            rows = new int[numRows() + 1];
        } else {
            elements = new Complex[numRows() * numColumns()];
        }

        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                elements[(i * numColumns()) + j] = m;
                colPos[(i * numColumns()) + j] = j;
            }

            rows[i + 1] = i * numColumns();
        }
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
            final AbstractComplexSquareMatrix[] lu = this.luDecompose(null);
            Complex det = lu[1].getPrimitiveElement(0, 0);

            for (int i = 1; i < numRows(); i++)
                det = det.multiply(lu[1].getPrimitiveElement(i, i));

            return det.multiply(LUpivot[numRows()]);
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
            Complex result = getPrimitiveElement(0, 0);

            for (int i = 1; i < numRows(); i++)
                result = result.add(getPrimitiveElement(i, i));

            return result;
        } else {
            throw new ArithmeticException(
                "The trace norm of a zero dimension matrix is undefined.");
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
            double result = 0.0;
            double tmpResult;

            for (int j, i = 0; i < numRows(); i++) {
                tmpResult = 0.0;

                for (j = rows[i]; j < rows[i + 1]; j++)
                    tmpResult += Math.sqrt(((elements[j].real() * elements[j].real()) +
                        (elements[j].imag() * elements[j].imag())));

                if (tmpResult > result) {
                    result = tmpResult;
                }
            }

            return result;
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
            double result = 0.0;

            for (int i = 0; i < colPos.length; i++)
                result += ((elements[i].real() * elements[i].real()) +
                (elements[i].imag() * elements[i].imag()));

            return Math.sqrt(result);
        } else {
            throw new ArithmeticException(
                "The frobenius norm of a zero dimension matrix is undefined.");
        }
    }

    //============
    // OPERATIONS
    //============

    // ADDITION
    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a Complex matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexSquareMatrix add(final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexSparseSquareMatrix) {
            return add((ComplexSparseSquareMatrix) m);
        }

        if (m instanceof ComplexSquareMatrix) {
            return add((ComplexSquareMatrix) m);
        }

        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final Complex[][] array = new Complex[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] = array[i][0].add(m.getPrimitiveElement(i, 0));

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = array[i][j].add(m.getPrimitiveElement(i, j));
            }

            return new ComplexSquareMatrix(array);
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
            final Complex[][] array = new Complex[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] = array[i][0].add(new Complex(m.matrixRe[i][0],
                            m.matrixIm[i][0]));

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = array[i][j].add(new Complex(
                                m.matrixRe[i][j], m.matrixIm[i][j]));
            }

            return new ComplexSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a Complex sparse matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexSparseSquareMatrix add(final ComplexSparseSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            ComplexSparseSquareMatrix ans = new ComplexSparseSquareMatrix(numRows());

            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++)
                    ans.setElement(i, j,
                        getPrimitiveElement(i, j)
                            .add(m.getPrimitiveElement(i, j)));
            }

            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m a Complex matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexSquareMatrix subtract(
        final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexSparseSquareMatrix) {
            return subtract((ComplexSparseSquareMatrix) m);
        }

        if (m instanceof ComplexSquareMatrix) {
            return subtract((ComplexSquareMatrix) m);
        }

        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final Complex[][] array = new Complex[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] = array[i][0].subtract(m.getPrimitiveElement(i, 0));

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = array[i][j].subtract(m.getPrimitiveElement(
                                i, j));
            }

            return new ComplexSquareMatrix(array);
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
            final Complex[][] array = new Complex[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] = array[i][0].subtract(new Complex(
                            m.matrixRe[i][0], m.matrixIm[i][0]));

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = array[i][j].subtract(new Complex(
                                m.matrixRe[i][j], m.matrixIm[i][j]));
            }

            return new ComplexSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a Complex sparse matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexSparseSquareMatrix subtract(final ComplexSparseSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            ComplexSparseSquareMatrix ans = new ComplexSparseSquareMatrix(numRows());

            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++)
                    ans.setElement(i, j,
                        getPrimitiveElement(i, j)
                            .subtract(m.getPrimitiveElement(i, j)));
            }

            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // SCALAR MULTIPLICATION
    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a Complex
     *
     * @return a Complex sparse matrix
     */
    public AbstractComplexMatrix scalarMultiply(final Complex x) {
        final ComplexSparseSquareMatrix ans = new ComplexSparseSquareMatrix(numRows());
        ans.elements = new Complex[elements.length];
        ans.colPos = new int[colPos.length];
        System.arraycopy(colPos, 0, ans.colPos, 0, colPos.length);
        System.arraycopy(rows, 0, ans.rows, 0, rows.length);

        for (int i = 0; i < colPos.length; i++)
            ans.elements[i] = x.multiply(elements[i]);

        return ans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractComplexMatrix scalarDivide(final Complex x) {
        final ComplexSparseSquareMatrix ans = new ComplexSparseSquareMatrix(numRows());
        ans.elements = new Complex[elements.length];
        ans.colPos = new int[colPos.length];
        System.arraycopy(colPos, 0, ans.colPos, 0, colPos.length);
        System.arraycopy(rows, 0, ans.rows, 0, rows.length);

        for (int i = 0; i < colPos.length; i++)
            ans.elements[i] = elements[i].divide(x);

        return ans;
    }

    // SCALAR PRODUCT
    /**
     * Returns the scalar product of this matrix and another.
     *
     * @param m a Complex matrix.
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public Complex scalarProduct(final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexSquareMatrix) {
            return scalarProduct((ComplexSquareMatrix) m);
        }

        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            Complex ans = Complex.ZERO;

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    ans = ans.add(elements[j].multiply(m.getPrimitiveElement(
                                    i, colPos[j])));
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
    public Complex scalarProduct(final ComplexSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            Complex ans = Complex.ZERO;

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    ans = ans.add(elements[j].multiply(
                                new Complex(m.matrixRe[i][colPos[j]],
                                    m.matrixIm[i][colPos[j]])));
            }

            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // MATRIX MULTIPLICATION
    /**
     * Returns the multiplication of a vector by this matrix.
     *
     * @param v a Complex vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrix and vector are
     *         incompatible.
     */
    public AbstractComplexVector multiply(final AbstractComplexVector v) {
        if (numColumns() == v.getDimension()) {
            final Complex[] array = new Complex[numRows()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i] = array[i].add(elements[j].multiply(
                                v.getPrimitiveElement(colPos[j])));
            }

            return new ComplexVector(array);
        } else {
            throw new IllegalDimensionException(
                "Matrix and vector are incompatible.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a Complex matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public AbstractComplexSquareMatrix multiply(
        final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexSparseSquareMatrix) {
            return multiply((ComplexSparseSquareMatrix) m);
        }

        if (m instanceof ComplexSquareMatrix) {
            return multiply((ComplexSquareMatrix) m);
        }

        if (numColumns() == m.numRows()) {
            final Complex[][] array = new Complex[numRows()][m.numColumns()];

            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < m.numColumns(); k++) {
                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        array[j][k] = array[j][k].add(elements[n].multiply(
                                    m.getPrimitiveElement(colPos[n], k)));
                }
            }

            return new ComplexSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComplexSquareMatrix multiply(final ComplexSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            final Complex[][] array = new Complex[numRows()][m.numColumns()];

            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < m.numColumns(); k++) {
                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        array[j][k] = array[j][k].add(elements[n].multiply(
                                    new Complex(m.matrixRe[colPos[n]][k],
                                        m.matrixIm[colPos[n]][k])));
                }
            }

            return new ComplexSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a Complex sparse matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public ComplexSparseSquareMatrix multiply(final ComplexSparseSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            ComplexSparseSquareMatrix ans = new ComplexSparseSquareMatrix(numRows());

            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < numColumns(); k++) {
                    Complex tmp = Complex.ZERO;

                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        tmp = tmp.add(elements[n].multiply(
                                    m.getPrimitiveElement(colPos[n], k)));

                    ans.setElement(j, k, tmp);
                }
            }

            return ans;
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    // TRANSPOSE
    /**
     * Returns the transpose of this matrix.
     *
     * @return a Complex sparse matrix
     */
    public Matrix transpose() {
        final ComplexSparseSquareMatrix ans = new ComplexSparseSquareMatrix(numRows());

        for (int i = 0; i < numRows(); i++) {
            ans.setElement(0, i, getPrimitiveElement(i, 0));

            for (int j = 1; j < numColumns(); j++)
                ans.setElement(j, i, getPrimitiveElement(i, j));
        }

        return ans;
    }

    // LU DECOMPOSITION
    /**
     * Returns the LU decomposition of this matrix.
     *
     * @param pivot an empty array of length <code>numRows()+1</code> to hold
     *        the pivot information (null if not interested)
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

        final Complex[][] arrayL = new Complex[numRows()][numColumns()];
        final Complex[][] arrayU = new Complex[numRows()][numColumns()];
        final double[] buf = new double[numRows()];

        if (pivot == null) {
            pivot = new int[numRows() + 1];
        }

        for (int i = 0; i < numRows(); i++)
            pivot[i] = i;

        pivot[numRows()] = 1;

        // LU decomposition to arrayU
        for (int j = 0; j < numColumns(); j++) {
            for (int i = 0; i < j; i++) {
                Complex tmp = getPrimitiveElement(pivot[i], j);

                for (int k = 0; k < i; k++)
                    tmp = tmp.subtract(arrayU[i][k].multiply(arrayU[k][j]));

                arrayU[i][j] = tmp;
            }

            double max = 0.0;
            int pivotrow = j;

            for (int i = j; i < numRows(); i++) {
                Complex tmp = getPrimitiveElement(pivot[i], j);

                for (int k = 0; k < j; k++)
                    tmp = tmp.subtract(arrayU[i][k].multiply(arrayU[k][j]));

                arrayU[i][j] = tmp;

                // while we're here search for a pivot for arrayU[j][j]
                double temp = (tmp.real() * tmp.real()) +
                    (tmp.imag() * tmp.imag());

                if (temp > max) {
                    max = temp;
                    pivotrow = i;
                }
            }

            // swap row j with pivotrow
            if (pivotrow != j) {
                System.arraycopy(arrayU[j], 0, buf, 0, j + 1);
                System.arraycopy(arrayU[pivotrow], 0, arrayU[j], 0, j + 1);
                System.arraycopy(buf, 0, arrayU[pivotrow], 0, j + 1);

                int k = pivot[j];
                pivot[j] = pivot[pivotrow];
                pivot[pivotrow] = k;
                // update parity
                pivot[numRows()] = -pivot[numRows()];
            }

            // divide by pivot
            for (int i = j + 1; i < numRows(); i++)
                arrayU[i][j] = arrayU[i][j].divide(arrayU[j][j]);
        }

        // move lower triangular part to arrayL
        for (int j = 0; j < numColumns(); j++) {
            arrayL[j][j] = Complex.ONE; //is this correct ?

            for (int i = j + 1; i < numRows(); i++) {
                arrayL[i][j] = arrayU[i][j];
                arrayU[i][j] = Complex.ZERO;
            }
        }

        LU = new AbstractComplexSquareMatrix[2];
        LU[0] = new ComplexSquareMatrix(arrayL);
        LU[1] = new ComplexSquareMatrix(arrayU);
        LUpivot = new int[pivot.length];
        System.arraycopy(pivot, 0, LUpivot, 0, pivot.length);

        return LU;
    }

    // CHOLESKY DECOMPOSITION
    /**
     * Returns the Cholesky decomposition of this matrix. Matrix must
     * be symmetric and positive definite.
     *
     * @return an array with [0] containing the L-matrix and [1] containing the
     *         U-matrix.
     */

    //unchecked
    public AbstractComplexSquareMatrix[] choleskyDecompose() {
        final Complex[][] arrayL = new Complex[numRows()][numColumns()];
        final Complex[][] arrayU = new Complex[numRows()][numColumns()];
        arrayL[0][0] = arrayU[0][0] = getPrimitiveElement(0, 0).sqrt(); //is this correct ?

        for (int i = 1; i < numRows(); i++)
            arrayL[i][0] = arrayU[0][i] = getPrimitiveElement(i, 0)
                                              .divide(arrayL[0][0]);

        for (int j = 1; j < numColumns(); j++) {
            Complex tmp = getPrimitiveElement(j, j);

            for (int i = 0; i < j; i++)
                tmp = tmp.subtract(arrayL[j][i].multiply(arrayL[j][i]));

            arrayL[j][j] = arrayU[j][j] = tmp.sqrt(); //is this correct ?

            for (int i = j + 1; i < numRows(); i++) {
                tmp = getPrimitiveElement(i, j);

                for (int k = 0; k < i; k++)
                    tmp = tmp.subtract(arrayL[j][k].multiply(arrayU[k][i]));

                arrayL[i][j] = arrayU[j][i] = tmp.divide(arrayU[j][j]);
            }
        }

        final AbstractComplexSquareMatrix[] lu = new AbstractComplexSquareMatrix[2];
        lu[0] = new ComplexSquareMatrix(arrayL);
        lu[1] = new ComplexSquareMatrix(arrayU);

        return lu;
    }

    // MAP ELEMENTS
    /**
     * Applies a function on all the matrix elements.
     *
     * @param f a user-defined function
     *
     * @return a double sparse matrix
     */
    public AbstractComplexMatrix mapElements(final ComplexMapping f) {
        final Complex[][] ans = new Complex[numRows()][numColumns()];
        Complex val = f.map(Complex.ZERO);

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                ans[i][j] = val;
            }
        }

        for (int i = 0; i < numRows(); i++) {
            for (int j = rows[i]; j < rows[i + 1]; j++)
                ans[i][colPos[j]] = f.map(elements[j]);
        }

        return new ComplexMatrix(ans);
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new ComplexSparseSquareMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an Complex array.
     */
    public Complex[][] toPrimitiveArray() {
        final Complex[][] result = new Complex[numRows()][numColumns()];

        for (int i = 0; i < numRows(); i++)
            for (int j = rows[i]; j < rows[i + 1]; j++)
                result[i][colPos[j]] = elements[j];

        return result;
    }
}
