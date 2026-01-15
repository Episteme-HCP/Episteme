package org.jscience.mathematics.algebraic.matrices;

import org.jscience.JScience;

import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.analysis.PrimitiveMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * The DoubleSparseSquareMatrix class provides an object for encapsulating
 * sparse square matrices. Uses compressed row storage.
 *
 * @author Mark Hale
 * @version 1.2
 */
public class DoubleSparseSquareMatrix extends AbstractDoubleSquareMatrix
    implements Cloneable, Serializable {
    /** Matrix elements. */
    private double[] elements;

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
    public DoubleSparseSquareMatrix(final int size) {
        super(size);
        elements = new double[0];
        colPos = new int[0];
        rows = new int[numRows() + 1];
    }

/**
     * Constructs a matrix from an array.
     *
     * @param array an assigned value
     * @throws IllegalDimensionException If the array is not square.
     */
    public DoubleSparseSquareMatrix(final double[][] array) {
        super(array.length);
        rows = new int[numRows() + 1];

        int n = 0;

        for (int i = 0; i < numRows(); i++) {
            if (array[i].length != array.length) {
                throw new IllegalDimensionException("Array is not square.");
            }

            for (int j = 0; j < numColumns(); j++) {
                if (Math.abs(array[i][j]) > java.lang.Double.valueOf(
                            JScience.getProperty("tolerance")).doubleValue()) {
                    n++;
                }
            }
        }

        elements = new double[n];
        colPos = new int[n];
        n = 0;

        for (int i = 0; i < numRows(); i++) {
            rows[i] = n;

            for (int j = 0; j < numColumns(); j++) {
                if (Math.abs(array[i][j]) > java.lang.Double.valueOf(
                            JScience.getProperty("tolerance")).doubleValue()) {
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
    public DoubleSparseSquareMatrix(final DoubleSparseSquareMatrix mat) {
        this(mat.numRows());
        System.arraycopy(mat.elements, 0, elements, 0, mat.elements.length);
        System.arraycopy(mat.colPos, 0, colPos, 0, mat.colPos.length);
        System.arraycopy(mat.rows, 0, rows, 0, mat.rows.length);
    }

    /**
     * Compares two double sparse square matrices for equality.
     *
     * @param m a double matrix
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(AbstractDoubleSquareMatrix m, double tol) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            if (m instanceof DoubleSparseSquareMatrix) {
                return this.equals((DoubleSparseSquareMatrix) m);
            } else {
                double sumSqr = 0;

                for (int i = 0; i < numRows(); i++) {
                    for (int j = 0; j < numColumns(); j++) {
                        double delta = getPrimitiveElement(i, j) -
                            m.getPrimitiveElement(i, j);
                        sumSqr += (delta * delta);
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
    public boolean equals(DoubleSparseSquareMatrix m) {
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
    public boolean equals(DoubleSparseSquareMatrix m, double tol) {
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

                double delta = elements[i] - m.elements[i];
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
     * Converts this matrix to an integer matrix.
     *
     * @return an integer square matrix
     */
    public AbstractIntegerMatrix toIntegerMatrix() {
        final int[][] ans = new int[numRows()][numColumns()];

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                ans[i][j] = Math.round((float) getPrimitiveElement(i, j));
        }

        return new IntegerSquareMatrix(ans);
    }

    /**
     * Converts this matrix to a complex matrix.
     *
     * @return a complex square matrix
     */
    public AbstractComplexMatrix toComplexMatrix() {
        final double[][] arrayRe = new double[numRows()][numColumns()];

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                arrayRe[i][j] = getPrimitiveElement(i, j);
        }

        return new ComplexSquareMatrix(arrayRe,
            new double[numRows()][numColumns()]);
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
    public double getPrimitiveElement(final int i, final int j) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns())) {
            for (int k = rows[i]; k < rows[i + 1]; k++) {
                if (colPos[k] == j) {
                    return elements[k];
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
    public void setElement(final int i, final int j, final double x) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns())) {
            if (Math.abs(x) <= java.lang.Double.valueOf(JScience.getProperty(
                            "tolerance")).doubleValue()) {
                return;
            }

            for (int k = rows[i]; k < rows[i + 1]; k++) {
                if (colPos[k] == j) {
                    elements[k] = x;

                    return;
                }
            }

            final double[] oldMatrix = elements;
            final int[] oldColPos = colPos;
            elements = new double[oldMatrix.length + 1];
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
     * about using a DoubleSquareMatrix.
     *
     * @param x a double element
     */
    public void setAllElements(final double x) {
        if (Math.abs(x) <= java.lang.Double.valueOf(JScience.getProperty(
                        "tolerance")).doubleValue()) {
            elements = new double[0];
            colPos = new int[0];
            rows = new int[numRows() + 1];
        } else {
            elements = new double[numRows() * numColumns()];
        }

        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                elements[(i * numColumns()) + j] = x;
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
    public double det() {
        if (numElements() > 0) {
            final AbstractDoubleSquareMatrix[] lu = this.luDecompose(null);
            double det = lu[1].getPrimitiveElement(0, 0);

            for (int i = 1; i < numRows(); i++)
                det *= lu[1].getPrimitiveElement(i, i);

            return det * LUpivot[numRows()];
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
    public double trace() {
        if (numElements() > 0) {
            double result = getPrimitiveElement(0, 0);

            for (int i = 1; i < numRows(); i++)
                result += getPrimitiveElement(i, i);

            return result;
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
            double result = 0.0;
            double tmpResult;

            for (int j, i = 0; i < numRows(); i++) {
                tmpResult = 0.0;

                for (j = rows[i]; j < rows[i + 1]; j++)
                    tmpResult += Math.abs(elements[j]);

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
                result += (elements[i] * elements[i]);

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
    public AbstractDoubleSquareMatrix add(final AbstractDoubleSquareMatrix m) {
        if (m instanceof DoubleSparseSquareMatrix) {
            return add((DoubleSparseSquareMatrix) m);
        }

        if (m instanceof DoubleSquareMatrix) {
            return add((DoubleSquareMatrix) m);
        }

        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] array = new double[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] += m.getPrimitiveElement(i, 0);

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] += m.getPrimitiveElement(i, j);
            }

            return new DoubleSquareMatrix(array);
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
    public DoubleSquareMatrix add(final DoubleSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] array = new double[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] += m.matrix[i][0];

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] += m.matrix[i][j];
            }

            return new DoubleSquareMatrix(array);
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
    public DoubleSparseSquareMatrix add(final DoubleSparseSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            DoubleSparseSquareMatrix ans = new DoubleSparseSquareMatrix(numRows());

            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++)
                    ans.setElement(i, j,
                        getPrimitiveElement(i, j) +
                        m.getPrimitiveElement(i, j));
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
    public AbstractDoubleSquareMatrix subtract(
        final AbstractDoubleSquareMatrix m) {
        if (m instanceof DoubleSparseSquareMatrix) {
            return subtract((DoubleSparseSquareMatrix) m);
        }

        if (m instanceof DoubleSquareMatrix) {
            return subtract((DoubleSquareMatrix) m);
        }

        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] array = new double[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] -= m.getPrimitiveElement(i, 0);

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] -= m.getPrimitiveElement(i, j);
            }

            return new DoubleSquareMatrix(array);
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
    public DoubleSquareMatrix subtract(final DoubleSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] array = new double[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] -= m.matrix[i][0];

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] -= m.matrix[i][j];
            }

            return new DoubleSquareMatrix(array);
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
    public DoubleSparseSquareMatrix subtract(final DoubleSparseSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            DoubleSparseSquareMatrix ans = new DoubleSparseSquareMatrix(numRows());

            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++)
                    ans.setElement(i, j,
                        getPrimitiveElement(i, j) -
                        m.getPrimitiveElement(i, j));
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
    public AbstractDoubleMatrix scalarMultiply(final double x) {
        final DoubleSparseSquareMatrix ans = new DoubleSparseSquareMatrix(numRows());
        ans.elements = new double[elements.length];
        ans.colPos = new int[colPos.length];
        System.arraycopy(colPos, 0, ans.colPos, 0, colPos.length);
        System.arraycopy(rows, 0, ans.rows, 0, rows.length);

        for (int i = 0; i < colPos.length; i++)
            ans.elements[i] = x * elements[i];

        return ans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleMatrix scalarDivide(final double x) {
        final DoubleSparseSquareMatrix ans = new DoubleSparseSquareMatrix(numRows());
        ans.elements = new double[elements.length];
        ans.colPos = new int[colPos.length];
        System.arraycopy(colPos, 0, ans.colPos, 0, colPos.length);
        System.arraycopy(rows, 0, ans.rows, 0, rows.length);

        for (int i = 0; i < colPos.length; i++)
            ans.elements[i] = elements[i] / x;

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
    public double scalarProduct(final AbstractDoubleSquareMatrix m) {
        if (m instanceof DoubleSquareMatrix) {
            return scalarProduct((DoubleSquareMatrix) m);
        }

        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            double ans = 0.0;

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    ans += (elements[j] * m.getPrimitiveElement(i, colPos[j]));
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
    public double scalarProduct(final DoubleSquareMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            double ans = 0.0;

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    ans += (elements[j] * m.matrix[i][colPos[j]]);
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
     * @param v a double vector
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrix and vector are
     *         incompatible.
     */
    public AbstractDoubleVector multiply(final AbstractDoubleVector v) {
        if (numColumns() == v.getDimension()) {
            final double[] array = new double[numRows()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i] += (elements[j] * v.getPrimitiveElement(colPos[j]));
            }

            return new DoubleVector(array);
        } else {
            throw new IllegalDimensionException(
                "Matrix and vector are incompatible.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a double matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public AbstractDoubleSquareMatrix multiply(
        final AbstractDoubleSquareMatrix m) {
        if (m instanceof DoubleSparseSquareMatrix) {
            return multiply((DoubleSparseSquareMatrix) m);
        }

        if (m instanceof DoubleSquareMatrix) {
            return multiply((DoubleSquareMatrix) m);
        }

        if (numColumns() == m.numRows()) {
            final double[][] array = new double[numRows()][m.numColumns()];

            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < m.numColumns(); k++) {
                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        array[j][k] += (elements[n] * m.getPrimitiveElement(colPos[n],
                            k));
                }
            }

            return new DoubleSquareMatrix(array);
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
    public DoubleSquareMatrix multiply(final DoubleSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            final double[][] array = new double[numRows()][m.numColumns()];

            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < m.numColumns(); k++) {
                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        array[j][k] += (elements[n] * m.matrix[colPos[n]][k]);
                }
            }

            return new DoubleSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a double sparse matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public DoubleSparseSquareMatrix multiply(final DoubleSparseSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            DoubleSparseSquareMatrix ans = new DoubleSparseSquareMatrix(numRows());

            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < numColumns(); k++) {
                    double tmp = 0.0;

                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        tmp += (elements[n] * m.getPrimitiveElement(colPos[n], k));

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
     * @return a double sparse matrix
     */
    public Matrix transpose() {
        final DoubleSparseSquareMatrix ans = new DoubleSparseSquareMatrix(numRows());

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
    public AbstractDoubleSquareMatrix[] luDecompose(int[] pivot) {
        if (LU != null) {
            if (pivot != null) {
                System.arraycopy(LUpivot, 0, pivot, 0, pivot.length);
            }

            return LU;
        }

        final double[][] arrayL = new double[numRows()][numColumns()];
        final double[][] arrayU = new double[numRows()][numColumns()];
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
                double tmp = getPrimitiveElement(pivot[i], j);

                for (int k = 0; k < i; k++)
                    tmp -= (arrayU[i][k] * arrayU[k][j]);

                arrayU[i][j] = tmp;
            }

            double max = 0.0;
            int pivotrow = j;

            for (int i = j; i < numRows(); i++) {
                double tmp = getPrimitiveElement(pivot[i], j);

                for (int k = 0; k < j; k++)
                    tmp -= (arrayU[i][k] * arrayU[k][j]);

                arrayU[i][j] = tmp;
                // while we're here search for a pivot for arrayU[j][j]
                tmp = Math.abs(tmp);

                if (tmp > max) {
                    max = tmp;
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
                arrayU[i][j] /= arrayU[j][j];
        }

        // move lower triangular part to arrayL
        for (int j = 0; j < numColumns(); j++) {
            arrayL[j][j] = 1.0;

            for (int i = j + 1; i < numRows(); i++) {
                arrayL[i][j] = arrayU[i][j];
                arrayU[i][j] = 0.0;
            }
        }

        LU = new AbstractDoubleSquareMatrix[2];
        LU[0] = new DoubleSquareMatrix(arrayL);
        LU[1] = new DoubleSquareMatrix(arrayU);
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
    public AbstractDoubleSquareMatrix[] choleskyDecompose() {
        final double[][] arrayL = new double[numRows()][numColumns()];
        final double[][] arrayU = new double[numRows()][numColumns()];
        arrayL[0][0] = arrayU[0][0] = Math.sqrt(getPrimitiveElement(0, 0));

        for (int i = 1; i < numRows(); i++)
            arrayL[i][0] = arrayU[0][i] = getPrimitiveElement(i, 0) / arrayL[0][0];

        for (int j = 1; j < numColumns(); j++) {
            double tmp = getPrimitiveElement(j, j);

            for (int i = 0; i < j; i++)
                tmp -= (arrayL[j][i] * arrayL[j][i]);

            arrayL[j][j] = arrayU[j][j] = Math.sqrt(tmp);

            for (int i = j + 1; i < numRows(); i++) {
                tmp = getPrimitiveElement(i, j);

                for (int k = 0; k < i; k++)
                    tmp -= (arrayL[j][k] * arrayU[k][i]);

                arrayL[i][j] = arrayU[j][i] = tmp / arrayU[j][j];
            }
        }

        final AbstractDoubleSquareMatrix[] lu = new AbstractDoubleSquareMatrix[2];
        lu[0] = new DoubleSquareMatrix(arrayL);
        lu[1] = new DoubleSquareMatrix(arrayU);

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
        return new DoubleSparseSquareMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an double array.
     */
    public double[][] toPrimitiveArray() {
        final double[][] result = new double[numRows()][numColumns()];

        for (int i = 0; i < numRows(); i++) {
            for (int j = rows[i]; j < rows[i + 1]; j++)
                result[i][colPos[j]] = elements[j];
        }

        return result;
    }
}
