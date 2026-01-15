package org.jscience.mathematics.algebraic.matrices;

import org.jscience.JScience;

import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.analysis.PrimitiveMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * The DoubleSparseMatrix class provides an object for encapsulating sparse
 * matrices. Uses compressed row storage.
 *
 * @author Mark Hale
 * @version 1.2
 */
public class DoubleSparseMatrix extends AbstractDoubleMatrix
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
     * @param rowCount the number of rows
     * @param colCount the number of columns
     */
    public DoubleSparseMatrix(final int rowCount, final int colCount) {
        super(rowCount, colCount);
        elements = new double[0];
        colPos = new int[0];
        rows = new int[numRows() + 1];
    }

/**
     * Constructs a matrix from an array.
     *
     * @param array an assigned value
     */
    public DoubleSparseMatrix(final double[][] array) {
        super(array.length, array[0].length);
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
    public DoubleSparseMatrix(final DoubleSparseMatrix mat) {
        this(mat.numRows(), mat.numColumns());
        System.arraycopy(mat.elements, 0, elements, 0, mat.elements.length);
        System.arraycopy(mat.colPos, 0, colPos, 0, mat.colPos.length);
        System.arraycopy(mat.rows, 0, rows, 0, mat.rows.length);
    }

    /**
     * Compares two double sparse matrices for equality.
     *
     * @param m a double matrix
     * @param tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(AbstractDoubleMatrix m, double tol) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            if (m instanceof DoubleSparseMatrix) {
                return this.equals((DoubleSparseMatrix) m);
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
    public boolean equals(DoubleSparseMatrix m) {
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
    public boolean equals(DoubleSparseMatrix m, double tol) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            if (colPos.length != m.colPos.length) {
                return false;
            }

            for (int i = 0; i < rows.length; i++) {
                if (rows[i] != m.rows[i]) {
                    return false;
                }
            }

            double sumSqr = 0.0;

            for (int i = 0; i < colPos.length; i++) {
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
     * @return an integer matrix
     */
    public AbstractIntegerMatrix toIntegerMatrix() {
        final int[][] ans = new int[numRows()][numColumns()];

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                ans[i][j] = Math.round((float) getPrimitiveElement(i, j));
        }

        return new IntegerMatrix(ans);
    }

    /**
     * Converts this matrix to a complex matrix.
     *
     * @return a complex matrix
     */
    public AbstractComplexMatrix toComplexMatrix() {
        final double[][] arrayRe = new double[numRows()][numColumns()];

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
    public AbstractDoubleMatrix add(final AbstractDoubleMatrix m) {
        if (m instanceof DoubleSparseMatrix) {
            return add((DoubleSparseMatrix) m);
        }

        if (m instanceof DoubleMatrix) {
            return add((DoubleMatrix) m);
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

            return new DoubleMatrix(array);
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
    public DoubleMatrix add(final DoubleMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] array = new double[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] += m.matrix[i][0];

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] += m.matrix[i][j];
            }

            return new DoubleMatrix(array);
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
    public DoubleSparseMatrix add(final DoubleSparseMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            DoubleSparseMatrix ans = new DoubleSparseMatrix(numRows(),
                    numColumns());

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
    public AbstractDoubleMatrix subtract(final AbstractDoubleMatrix m) {
        if (m instanceof DoubleSparseMatrix) {
            return subtract((DoubleSparseMatrix) m);
        }

        if (m instanceof DoubleMatrix) {
            return subtract((DoubleMatrix) m);
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

            return new DoubleMatrix(array);
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
    public DoubleMatrix subtract(final DoubleMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final double[][] array = new double[numRows()][numColumns()];

            for (int i = 0; i < numRows(); i++) {
                for (int j = rows[i]; j < rows[i + 1]; j++)
                    array[i][colPos[j]] = elements[j];

                array[i][0] -= m.matrix[i][0];

                for (int j = 1; j < numColumns(); j++)
                    array[i][j] -= m.matrix[i][j];
            }

            return new DoubleMatrix(array);
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
    public DoubleSparseMatrix subtract(final DoubleSparseMatrix m) {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            DoubleSparseMatrix ans = new DoubleSparseMatrix(numRows(),
                    numColumns());

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
        final DoubleSparseMatrix ans = new DoubleSparseMatrix(numRows(),
                numColumns());
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
        final DoubleSparseMatrix ans = new DoubleSparseMatrix(numRows(),
                numColumns());
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
    public double scalarProduct(final AbstractDoubleMatrix m) {
        if (m instanceof DoubleMatrix) {
            return scalarProduct((DoubleMatrix) m);
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
    public double scalarProduct(final DoubleMatrix m) {
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
    public AbstractDoubleMatrix multiply(final AbstractDoubleMatrix m) {
        if (m instanceof DoubleSparseMatrix) {
            return multiply((DoubleSparseMatrix) m);
        }

        if (m instanceof DoubleMatrix) {
            return multiply((DoubleMatrix) m);
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

            if (numRows() == m.numColumns()) {
                return new DoubleSquareMatrix(array);
            } else {
                return new DoubleMatrix(array);
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
    public AbstractDoubleMatrix multiply(final DoubleMatrix m) {
        if (numColumns() == m.numRows()) {
            final double[][] array = new double[numRows()][m.numColumns()];

            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < m.numColumns(); k++) {
                    for (int n = rows[j]; n < rows[j + 1]; n++)
                        array[j][k] += (elements[n] * m.matrix[colPos[n]][k]);
                }
            }

            if (numRows() == m.numColumns()) {
                return new DoubleSquareMatrix(array);
            } else {
                return new DoubleMatrix(array);
            }
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
    public AbstractDoubleMatrix multiply(final DoubleSparseMatrix m) {
        if (numColumns() == m.numRows()) {
            AbstractDoubleMatrix ans;

            if (numRows() == m.numColumns()) {
                ans = new DoubleSparseSquareMatrix(numRows());
            } else {
                ans = new DoubleSparseMatrix(numRows(), m.numColumns());
            }

            for (int j = 0; j < ans.numRows(); j++) {
                for (int k = 0; k < ans.numColumns(); k++) {
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
        final DoubleSparseMatrix ans = new DoubleSparseMatrix(numColumns(),
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
        return new DoubleSparseMatrix(this);
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
