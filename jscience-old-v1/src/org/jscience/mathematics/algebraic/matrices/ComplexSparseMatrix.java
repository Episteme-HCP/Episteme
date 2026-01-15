package org.jscience.mathematics.algebraic.matrices;

import org.jscience.JScience;

import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.ComplexMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * The ComplexSparseMatrix class provides an object for encapsulating
 * sparse matrices. Uses compressed row storage.
 *
 * @author Mark Hale
 * @version 1.2
 */
public class ComplexSparseMatrix extends AbstractComplexMatrix
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
     * @param rowCount the number of rows
     * @param colCount the number of columns
     */
    public ComplexSparseMatrix(final int rowCount, final int colCount) {
        super(rowCount, colCount);
        elements = new Complex[0];
        colPos = new int[0];
        rows = new int[numRows() + 1];
    }

/**
     * Constructs a matrix from an array.
     *
     * @param array an assigned value
     */
    public ComplexSparseMatrix(final Complex[][] array) {
        super(array.length, array[0].length);
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
    public ComplexSparseMatrix(final ComplexSparseMatrix mat) {
        this(mat.numRows(), mat.numColumns());
        System.arraycopy(mat.elements, 0, elements, 0, mat.elements.length);
        System.arraycopy(mat.colPos, 0, colPos, 0, mat.colPos.length);
        System.arraycopy(mat.rows, 0, rows, 0, mat.rows.length);
    }

    /**
     * Compares two Complex sparse matrices for equality.
     *
     * @param m a Complex matrix
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(AbstractComplexMatrix m, double tol) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            if (m instanceof ComplexSparseMatrix) {
                return this.equals((ComplexSparseMatrix) m);
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
    public boolean equals(ComplexSparseMatrix m) {
        return equals(m,
            java.lang.Double.valueOf(JScience.getProperty("tolerance"))
                            .doubleValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(ComplexSparseMatrix m, double tol) {
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

                double deltaRe = elements[i].real() - m.elements[i].real();
                double deltaIm = elements[i].imag() - m.elements[i].imag();
                sumSqr += ((deltaRe * deltaRe) + (deltaIm * deltaIm));
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
     * Returns the real part of this complex matrix.
     *
     * @return a double matrix
     */
    public AbstractDoubleMatrix real() {
        final double[][] ans = new double[numRows()][numColumns()];

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                ans[i][j] = getPrimitiveElement(i, j).real();
        }

        return new DoubleMatrix(ans);
    }

    /**
     * Returns the imaginary part of this complex matrix.
     *
     * @return a double matrix
     */
    public AbstractDoubleMatrix imag() {
        final double[][] ans = new double[numRows()][numColumns()];

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                ans[i][j] = getPrimitiveElement(i, j).imag();
        }

        return new DoubleMatrix(ans);
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
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRealElement(final int i, final int j) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns())) {
            for (int k = rows[i]; k < rows[i + 1]; k++) {
                if (colPos[k] == j) {
                    return elements[k].real();
                }
            }

            return 0.0;
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
            for (int k = rows[i]; k < rows[i + 1]; k++) {
                if (colPos[k] == j) {
                    return elements[k].imag();
                }
            }

            return 0.0;
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

            for (int i = 0; i < numRows(); i++) {
                tmpResult = 0.0;

                for (int j = rows[i]; j < rows[i + 1]; j++)
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
     * @param m a double matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexMatrix add(final AbstractComplexMatrix m) {
        if (m instanceof ComplexSparseMatrix) {
            return add((ComplexSparseMatrix) m);
        }

        if (m instanceof ComplexMatrix) {
            return add((ComplexMatrix) m);
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

            return new ComplexMatrix(array);
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
    public ComplexMatrix add(final ComplexMatrix m) {
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

            return new ComplexMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a double sparse matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexSparseMatrix add(final ComplexSparseMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            ComplexSparseMatrix ans = new ComplexSparseMatrix(numRows(),
                    numColumns());

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
     * @param m a double matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexMatrix subtract(final AbstractComplexMatrix m) {
        if (m instanceof ComplexSparseMatrix) {
            return subtract((ComplexSparseMatrix) m);
        }

        if (m instanceof ComplexMatrix) {
            return subtract((ComplexMatrix) m);
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

            return new ComplexMatrix(array);
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
    public ComplexMatrix subtract(final ComplexMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final Complex[][] array = new Complex[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] = array[i][0].subtract(new Complex(
                            m.matrixRe[i][0], m.matrixRe[i][0]));

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = array[i][j].subtract(new Complex(
                                m.matrixRe[i][j], m.matrixIm[i][j]));
            }

            return new ComplexMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a double sparse matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public ComplexSparseMatrix subtract(final ComplexSparseMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            ComplexSparseMatrix ans = new ComplexSparseMatrix(numRows(),
                    numColumns());

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
     * @param x a double
     *
     * @return a double sparse matrix
     */
    public AbstractComplexMatrix scalarMultiply(final Complex x) {
        final ComplexSparseMatrix ans = new ComplexSparseMatrix(numRows(),
                numColumns());
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
        final ComplexSparseMatrix ans = new ComplexSparseMatrix(numRows(),
                numColumns());
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
     * @param m a double matrix.
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public Complex scalarProduct(final AbstractComplexMatrix m) {
        if (m instanceof ComplexMatrix) {
            return scalarProduct((ComplexMatrix) m);
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
    public Complex scalarProduct(final ComplexMatrix m) {
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
    public AbstractComplexMatrix multiply(final AbstractComplexMatrix m) {
        if (m instanceof ComplexSparseMatrix) {
            return multiply((ComplexSparseMatrix) m);
        }

        if (m instanceof ComplexMatrix) {
            return multiply((ComplexMatrix) m);
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

            if (numRows() == m.numColumns()) {
                return new ComplexSquareMatrix(array);
            } else {
                return new ComplexMatrix(array);
            }
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
    public AbstractComplexMatrix multiply(final ComplexMatrix m) {
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

            if (numRows() == m.numColumns()) {
                return new ComplexSquareMatrix(array);
            } else {
                return new ComplexMatrix(array);
            }
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
    public AbstractComplexMatrix multiply(final ComplexSparseMatrix m) {
        if (numColumns() == m.numRows()) {
            AbstractComplexMatrix ans;

            if (numRows() == m.numColumns()) {
                ans = new ComplexSparseSquareMatrix(numRows());
            } else {
                ans = new ComplexSparseMatrix(numRows(), m.numColumns());
            }

            for (int j = 0; j < ans.numRows(); j++) {
                for (int k = 0; k < ans.numColumns(); k++) {
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
        final ComplexSparseMatrix ans = new ComplexSparseMatrix(numColumns(),
                numRows());

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
     *
     * @return a Complex sparse matrix
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
        return new ComplexSparseMatrix(this);
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
