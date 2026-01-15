package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.DiagonalMatrix;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.TridiagonalMatrix;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.IllegalDimensionException;
import org.jscience.util.MaximumIterationsExceededException;

import java.io.Serializable;

/**
 * The IntegerDiagonalMatrix class provides an object for encapsulating integer diagonal matrices.
 *
 * @author Mark Hale
 * @version 2.2
 */
public class IntegerDiagonalMatrix extends AbstractIntegerSquareMatrix implements Cloneable, Serializable, DiagonalMatrix {
    /**
     * Diagonal data.
     */
    protected final int diag[];

    /**
     * Constructs an empty matrix.
     *
     * @param size the number of rows/columns
     */
    public IntegerDiagonalMatrix(final int size) {
        this(new int[size]);
    }

    /**
     * Constructs a matrix from an array.
     * Any non-diagonal elements in the array are ignored.
     *
     * @param array an assigned value
     * @throws IllegalDimensionException If the array is not square.
     */
    public IntegerDiagonalMatrix(final int array[][]) {
        this(array.length);
        for (int i = 0; i < array.length; i++) {
            if (array[i].length != array.length)
                throw new IllegalDimensionException("Array is not square.");
            diag[i] = array[i][i];
        }
    }

    /**
     * Constructs a matrix by wrapping an array containing the diagonal elements.
     *
     * @param array an assigned value
     */
    public IntegerDiagonalMatrix(final int array[]) {
        super(array.length);
        diag = array;
    }

    /**
     * Copy constructor.
     *
     * @param mat an assigned value
     */
    public IntegerDiagonalMatrix(final IntegerDiagonalMatrix mat) {
        this(mat.numRows());
        System.arraycopy(mat.diag, 0, diag, 0, mat.diag.length);
    }

    /**
     * Creates an identity matrix.
     *
     * @param size the number of rows/columns
     */
    public static IntegerDiagonalMatrix identity(final int size) {
        int array[] = new int[size];
        for (int i = 0; i < size; i++)
            array[i] = 1;
        return new IntegerDiagonalMatrix(array);
    }

    /**
     * Compares two ${nativeTyp} matrices for equality.
     *
     * @param m a int matrix
     */
    public boolean equals(AbstractIntegerMatrix m, double tol) {
        if (m instanceof DiagonalMatrix) {
            if (numRows() != m.numRows() || numColumns() != m.numColumns())
                return false;
            int sumSqr = 0;
            int delta = diag[0] - m.getPrimitiveElement(0, 0);
            sumSqr += delta * delta;
            for (int i = 1; i < numRows(); i++) {
                delta = diag[i] - m.getPrimitiveElement(i, i);
                sumSqr += delta * delta;
            }
            return (sumSqr <= tol * tol);
        } else {
            return false;
        }
    }

    public int getK1() {
        return 0;
    }

    public int getK2() {
        return 0;
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

    /**
     * Converts this matrix to a double matrix.
     *
     * @return a double matrix
     */
    public AbstractDoubleMatrix toDoubleMatrix() {
        final double array[] = new double[numRows()];
        for (int i = 0; i < numRows(); i++)
            array[i] = diag[i];
        return new DoubleDiagonalMatrix(array);
    }

    /**
     * Converts this matrix to a complex matrix.
     *
     * @return a complex matrix
     */
    public AbstractComplexMatrix toComplexMatrix() {
        final double array[] = new double[numRows()];
        for (int i = 0; i < numRows(); i++)
            array[i] = diag[i];
        return new ComplexDiagonalMatrix(array, new double[numRows()]);
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
            if (i == j)
                return diag[i];
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
    public void setElement(final int i, final int j, final int x) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns()) {
            if (i == j)
                diag[i] = x;
            else
                throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        } else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    /**
     * Sets the value of all elements of the matrix. This method will throw an error unless the paramter is 0.
     * This is because you need a SquareMatrix to set all the contents of the matrix to a value.
     * You should think about using a IntegerSquareMatrix.
     *
     * @param r a Complex element
     */
    public void setAllElements(final int r) {
        if (r == 0) {
            for (int i = 0; i < numRows(); i++) {
                diag[i] = 0;
            }
        } else
            throw new IllegalArgumentException("You can't set all elements of a diagonal matrix unless to zero.");
    }

    /**
     * Returns true if this matrix is symmetric.
     */
    public boolean isSymmetric() {
        return true;
    }

    /**
     * Returns the determinant.
     */
    public int det() {
        if (numElements() > 0) {
            int det = diag[0];
            for (int i = 1; i < numRows(); i++)
                det *= diag[i];
            return det;
        } else
            throw new ArithmeticException("The determinant of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the trace.
     */
    public int trace() {
        if (numElements() > 0) {
            int tr = diag[0];
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
    public int infNorm() {
        if (numElements() > 0) {
            int result = Math.abs(diag[0]);
            int tmpResult;
            for (int i = 1; i < numRows(); i++) {
                tmpResult = Math.abs(diag[i]);
                if (tmpResult > result)
                    result = tmpResult;
            }
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
            double result = diag[0];
            for (int i = 1; i < numRows(); i++)
                result = MathUtils.hypot(result, diag[i]);
            return result;
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
        return infNorm();
    }

//============
// OPERATIONS
//============

// ADDITION

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a int matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractIntegerSquareMatrix add(final AbstractIntegerSquareMatrix m) {
        if (m instanceof IntegerDiagonalMatrix)
            return add((IntegerDiagonalMatrix) m);
        if (m instanceof DiagonalMatrix)
            return addDiagonal(m);
        if (m instanceof IntegerTridiagonalMatrix)
            return add((IntegerTridiagonalMatrix) m);
        if (m instanceof TridiagonalMatrix)
            return addTridiagonal(m);
        if (m instanceof IntegerSquareMatrix)
            return add((IntegerSquareMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = m.getPrimitiveElement(i, j);
            }
            for (int i = 0; i < numRows(); i++)
                array[i][i] += diag[i];
            return new IntegerSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    public IntegerSquareMatrix add(final IntegerSquareMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++)
                System.arraycopy(m.matrix[i], 0, array[i], 0, numRows());
            for (int i = 0; i < numRows(); i++)
                array[i][i] += diag[i];
            return new IntegerSquareMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a int tridiagonal matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public IntegerTridiagonalMatrix add(final IntegerTridiagonalMatrix m) {
        if (numRows() == m.numRows()) {
            final IntegerTridiagonalMatrix ans = new IntegerTridiagonalMatrix(numRows());
            System.arraycopy(m.ldiag, 0, ans.ldiag, 0, m.ldiag.length);
            System.arraycopy(m.udiag, 0, ans.udiag, 0, m.udiag.length);
            ans.diag[0] = diag[0] + m.diag[0];
            for (int i = 1; i < numRows(); i++)
                ans.diag[i] = diag[i] + m.diag[i];
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    private IntegerTridiagonalMatrix addTridiagonal(final AbstractIntegerSquareMatrix m) {
        int mRow = numRows();
        if (mRow == m.numRows()) {
            final IntegerTridiagonalMatrix ans = new IntegerTridiagonalMatrix(mRow);
            ans.diag[0] = diag[0] + m.getPrimitiveElement(0, 0);
            ans.udiag[0] = m.getPrimitiveElement(0, 1);
            mRow--;
            for (int i = 1; i < mRow; i++) {
                ans.ldiag[i] = m.getPrimitiveElement(i, i - 1);
                ans.diag[i] = diag[i] + m.getPrimitiveElement(i, i);
                ans.udiag[i] = m.getPrimitiveElement(i, i + 1);
            }
            ans.ldiag[mRow] = m.getPrimitiveElement(mRow, mRow - 1);
            ans.diag[mRow] = diag[mRow] + m.getPrimitiveElement(mRow, mRow);
            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a int diagonal matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public IntegerDiagonalMatrix add(final IntegerDiagonalMatrix m) {
        if (numRows() == m.numRows()) {
            final int array[] = new int[numRows()];
            array[0] = diag[0] + m.diag[0];
            for (int i = 1; i < numRows(); i++)
                array[i] = diag[i] + m.diag[i];
            return new IntegerDiagonalMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    private IntegerDiagonalMatrix addDiagonal(final AbstractIntegerSquareMatrix m) {
        if (numRows() == m.numRows()) {
            final int array[] = new int[numRows()];
            array[0] = diag[0] + m.getPrimitiveElement(0, 0);
            for (int i = 1; i < numRows(); i++)
                array[i] = diag[i] + m.getPrimitiveElement(i, i);
            return new IntegerDiagonalMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this matrix by another.
     *
     * @param m a int matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractIntegerSquareMatrix subtract(final AbstractIntegerSquareMatrix m) {
        if (m instanceof IntegerDiagonalMatrix)
            return subtract((IntegerDiagonalMatrix) m);
        if (m instanceof DiagonalMatrix)
            return subtractDiagonal(m);
        if (m instanceof IntegerTridiagonalMatrix)
            return subtract((IntegerTridiagonalMatrix) m);
        if (m instanceof TridiagonalMatrix)
            return subtractTridiagonal(m);
        if (m instanceof IntegerSquareMatrix)
            return subtract((IntegerSquareMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = -m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = -m.getPrimitiveElement(i, j);
            }
            for (int i = 0; i < numRows(); i++)
                array[i][i] += diag[i];
            return new IntegerSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    public IntegerSquareMatrix subtract(final IntegerSquareMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = -m.matrix[i][0];
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = -m.matrix[i][j];
            }
            for (int i = 0; i < numRows(); i++)
                array[i][i] += diag[i];
            return new IntegerSquareMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m a int tridiagonal matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public IntegerTridiagonalMatrix subtract(final IntegerTridiagonalMatrix m) {
        int mRow = numRows();
        if (mRow == m.numRows()) {
            final IntegerTridiagonalMatrix ans = new IntegerTridiagonalMatrix(mRow);
            ans.diag[0] = diag[0] - m.diag[0];
            ans.udiag[0] = -m.udiag[0];
            mRow--;
            for (int i = 1; i < mRow; i++) {
                ans.ldiag[i] = -m.ldiag[i];
                ans.diag[i] = diag[i] - m.diag[i];
                ans.udiag[i] = -m.udiag[i];
            }
            ans.ldiag[mRow] = -m.ldiag[mRow];
            ans.diag[mRow] = diag[mRow] - m.diag[mRow];
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    private IntegerTridiagonalMatrix subtractTridiagonal(final AbstractIntegerSquareMatrix m) {
        int mRow = numRows();
        if (mRow == m.numRows()) {
            final IntegerTridiagonalMatrix ans = new IntegerTridiagonalMatrix(mRow);
            ans.diag[0] = diag[0] - m.getPrimitiveElement(0, 0);
            ans.udiag[0] = -m.getPrimitiveElement(0, 1);
            mRow--;
            for (int i = 1; i < mRow; i++) {
                ans.ldiag[i] = -m.getPrimitiveElement(i, i - 1);
                ans.diag[i] = diag[i] - m.getPrimitiveElement(i, i);
                ans.udiag[i] = -m.getPrimitiveElement(i, i + 1);
            }
            ans.ldiag[mRow] = -m.getPrimitiveElement(mRow, mRow - 1);
            ans.diag[mRow] = diag[mRow] - m.getPrimitiveElement(mRow, mRow);
            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m a int diagonal matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public IntegerDiagonalMatrix subtract(final IntegerDiagonalMatrix m) {
        if (numRows() == m.numRows()) {
            final int array[] = new int[numRows()];
            array[0] = diag[0] - m.diag[0];
            for (int i = 1; i < numRows(); i++)
                array[i] = diag[i] - m.diag[i];
            return new IntegerDiagonalMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    private IntegerDiagonalMatrix subtractDiagonal(final AbstractIntegerSquareMatrix m) {
        if (numRows() == m.numRows()) {
            final int array[] = new int[numRows()];
            array[0] = diag[0] - m.getPrimitiveElement(0, 0);
            for (int i = 1; i < numRows(); i++)
                array[i] = diag[i] - m.getPrimitiveElement(i, i);
            return new IntegerDiagonalMatrix(array);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// SCALAR MULTIPLICATION

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a int.
     * @return a int diagonal matrix.
     */
    public AbstractIntegerMatrix scalarMultiply(final int x) {
        final int array[] = new int[numRows()];
        array[0] = x * diag[0];
        for (int i = 1; i < numRows(); i++)
            array[i] = x * diag[i];
        return new IntegerDiagonalMatrix(array);
    }

// SCALAR DIVISON

// SCALAR PRODUCT

    /**
     * Returns the scalar product of this matrix and another.
     *
     * @param m a int matrix.
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public int scalarProduct(final AbstractIntegerSquareMatrix m) {
        if (m instanceof IntegerDiagonalMatrix)
            return scalarProduct((IntegerDiagonalMatrix) m);
        if (m instanceof IntegerTridiagonalMatrix)
            return scalarProduct((IntegerTridiagonalMatrix) m);
        if (m instanceof IntegerSquareMatrix)
            return scalarProduct((IntegerSquareMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            int ans = diag[0] * m.getPrimitiveElement(0, 0);
            for (int i = 1; i < numRows(); i++)
                ans += diag[i] * m.getPrimitiveElement(i, i);
            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    public int scalarProduct(final IntegerSquareMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            int ans = diag[0] * m.matrix[0][0];
            for (int i = 1; i < numRows(); i++)
                ans += diag[i] * m.matrix[i][i];
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    public int scalarProduct(final IntegerTridiagonalMatrix m) {
        if (numRows() == m.numRows()) {
            int ans = diag[0] * m.diag[0];
            for (int i = 1; i < numRows(); i++)
                ans += diag[i] * m.diag[i];
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    public int scalarProduct(final IntegerDiagonalMatrix m) {
        if (numRows() == m.numRows()) {
            int ans = diag[0] * m.diag[0];
            for (int i = 1; i < numRows(); i++)
                ans += diag[i] * m.diag[i];
            return ans;
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// MATRIX MULTIPLICATION

    /**
     * Returns the multiplication of a vector by this matrix.
     *
     * @param v a int vector.
     * @throws IllegalDimensionException If the matrix and vector are incompatible.
     */
    public AbstractIntegerVector multiply(final AbstractIntegerVector v) {
        if (numColumns() == v.getDimension()) {
            final int array[] = new int[numRows()];
            array[0] = diag[0] * v.getPrimitiveElement(0);
            for (int i = 1; i < numRows(); i++)
                array[i] = diag[i] * v.getPrimitiveElement(i);
            return new IntegerVector(array);
        } else {
            throw new IllegalDimensionException("Matrix and vector are incompatible.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a int matrix
     * @return a AbstractIntegerMatrix or a AbstractIntegerSquareMatrix as appropriate
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public AbstractIntegerSquareMatrix multiply(final AbstractIntegerSquareMatrix m) {
        if (m instanceof IntegerDiagonalMatrix)
            return multiply((IntegerDiagonalMatrix) m);
        if (m instanceof DiagonalMatrix)
            return multiplyDiagonal(m);
        if (m instanceof IntegerTridiagonalMatrix)
            return multiply((IntegerTridiagonalMatrix) m);
        if (m instanceof TridiagonalMatrix)
            return multiplyTridiagonal(m);
        if (m instanceof IntegerSquareMatrix)
            return multiply((IntegerSquareMatrix) m);

        if (numColumns() == m.numRows()) {
            final int mColumns = m.numColumns();
            final int array[][] = new int[numRows()][mColumns];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = diag[0] * m.getPrimitiveElement(i, 0);
                for (int j = 1; j < mColumns; j++)
                    array[i][j] = diag[i] * m.getPrimitiveElement(i, j);
            }
            return new IntegerSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    public IntegerSquareMatrix multiply(final IntegerSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            final int array[][] = new int[numRows()][m.numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = diag[0] * m.matrix[i][0];
                for (int j = 1; j < m.numColumns(); j++)
                    array[i][j] = diag[i] * m.matrix[i][j];
            }
            return new IntegerSquareMatrix(array);
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

    public IntegerTridiagonalMatrix multiply(final IntegerTridiagonalMatrix m) {
        int mRow = numRows();
        if (numColumns() == m.numRows()) {
            final IntegerTridiagonalMatrix ans = new IntegerTridiagonalMatrix(mRow);
            ans.diag[0] = diag[0] * m.diag[0];
            ans.udiag[0] = diag[0] * m.udiag[0];
            mRow--;
            for (int i = 1; i < mRow; i++) {
                ans.ldiag[i] = diag[i] * m.ldiag[i];
                ans.diag[i] = diag[i] * m.diag[i];
                ans.udiag[i] = diag[i] * m.udiag[i];
            }
            ans.ldiag[mRow] = diag[mRow] * m.ldiag[mRow];
            ans.diag[mRow] = diag[mRow] * m.diag[mRow];
            return ans;
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

    private IntegerTridiagonalMatrix multiplyTridiagonal(final AbstractIntegerSquareMatrix m) {
        int mRow = numRows();
        if (numColumns() == m.numRows()) {
            final IntegerTridiagonalMatrix ans = new IntegerTridiagonalMatrix(mRow);
            ans.diag[0] = diag[0] * m.getPrimitiveElement(0, 0);
            ans.udiag[0] = diag[0] * m.getPrimitiveElement(0, 1);
            mRow--;
            for (int i = 1; i < mRow; i++) {
                ans.ldiag[i] = diag[i] * m.getPrimitiveElement(i, i - 1);
                ans.diag[i] = diag[i] * m.getPrimitiveElement(i, i);
                ans.udiag[i] = diag[i] * m.getPrimitiveElement(i, i + 1);
            }
            ans.ldiag[mRow] = diag[mRow] * m.getPrimitiveElement(mRow, mRow - 1);
            ans.diag[mRow] = diag[mRow] * m.getPrimitiveElement(mRow, mRow);
            return ans;
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    public IntegerDiagonalMatrix multiply(final IntegerDiagonalMatrix m) {
        if (numColumns() == m.numRows()) {
            final int array[] = new int[numRows()];
            array[0] = diag[0] * m.diag[0];
            for (int i = 1; i < numRows(); i++) {
                array[i] = diag[i] * m.diag[i];
            }
            return new IntegerDiagonalMatrix(array);
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

    private IntegerDiagonalMatrix multiplyDiagonal(final AbstractIntegerSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            final int array[] = new int[numRows()];
            array[0] = diag[0] * m.getPrimitiveElement(0, 0);
            for (int i = 1; i < numRows(); i++) {
                array[i] = diag[i] * m.getPrimitiveElement(i, i);
            }
            return new IntegerDiagonalMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

// TRANSPOSE

    /**
     * Returns the transpose of this matrix.
     *
     * @return a int matrix
     */
    public Matrix transpose() {
        return this;
    }

// INVERSE

    /**
     * Returns the inverse of this matrix.
     *
     * @return a double diagonal matrix
     */
    public AbstractDoubleSquareMatrix inverse() {
        final double array[] = new double[numRows()];
        array[0] = 1.0 / diag[0];
        for (int i = 1; i < numRows(); i++)
            array[i] = 1.0 / diag[i];
        return new DoubleDiagonalMatrix(array);
    }

// LU DECOMPOSITION

    /**
     * Returns the LU decomposition of this matrix.
     *
     * @param pivot an empty array of length <code>numRows()+1</code>
     *              to hold the pivot information (null if not interested).
     *              The last array element will contain the parity.
     * @return an array with [0] containing the L-matrix
     *         and [1] containing the U-matrix.
     */
    public AbstractDoubleSquareMatrix[] luDecompose(int pivot[]) {
        if (LU != null) {
            if (pivot != null)
                System.arraycopy(LUpivot, 0, pivot, 0, pivot.length);
            return LU;
        }
        if (pivot == null)
            pivot = new int[numRows() + 1];
        for (int i = 0; i < numRows(); i++)
            pivot[i] = i;
        pivot[numRows()] = 1;
        LU = new AbstractDoubleSquareMatrix[2];
        LU[0] = DoubleDiagonalMatrix.identity(numRows());
        LU[1] = (AbstractDoubleSquareMatrix) this.toDoubleMatrix();
        LUpivot = new int[pivot.length];
        System.arraycopy(pivot, 0, LUpivot, 0, pivot.length);
        return LU;
    }

// CHOLESKY DECOMPOSITION

    /**
     * Returns the Cholesky decomposition of this matrix.
     * Matrix must be symmetric and positive definite.
     *
     * @return an array with [0] containing the L-matrix and [1] containing the U-matrix.
     */
    public AbstractDoubleSquareMatrix[] choleskyDecompose() {
        final AbstractDoubleSquareMatrix lu[] = new AbstractDoubleSquareMatrix[2];
        final double array[] = new double[numRows()];
        array[0] = Math.sqrt(diag[0]);
        for (int i = 1; i < numRows(); i++)
            array[i] = Math.sqrt(diag[i]);
        lu[0] = new DoubleDiagonalMatrix(array);
        lu[1] = lu[0];
        return lu;
    }

// QR DECOMPOSITION

    /**
     * Returns the QR decomposition of this matrix.
     *
     * @return an array with [0] containing the Q-matrix and [1] containing the R-matrix.
     * @planetmath QRDecomposition
     */
    public AbstractDoubleSquareMatrix[] qrDecompose() {
        final AbstractDoubleSquareMatrix qr[] = new AbstractDoubleSquareMatrix[2];
        qr[0] = DoubleDiagonalMatrix.identity(numRows());
        qr[1] = (AbstractDoubleSquareMatrix) this.toDoubleMatrix();
        return qr;
    }

// SINGULAR VALUE DECOMPOSITION

    /**
     * Returns the singular value decomposition of this matrix.
     *
     * @return an array with [0] containing the U-matrix, [1] containing the S-matrix and [2] containing the V-matrix.
     */
    public AbstractDoubleSquareMatrix[] singularValueDecompose() {
        final int N = numRows();
        final int Nm1 = N - 1;
        final double arrayU[] = new double[N];
        final double arrayS[] = new double[N];
        final double arrayV[] = new double[N];
        for (int i = 0; i < Nm1; i++) {
            arrayU[i] = -1.0;
            arrayS[i] = Math.abs(diag[i]);
            arrayV[i] = diag[i] < 0.0 ? 1.0 : -1.0;
        }
        arrayU[Nm1] = 1.0;
        arrayS[Nm1] = Math.abs(diag[Nm1]);
        arrayV[Nm1] = diag[Nm1] < 0.0 ? -1.0 : 1.0;
        final AbstractDoubleSquareMatrix svd[] = new AbstractDoubleSquareMatrix[3];
        svd[0] = new DoubleDiagonalMatrix(arrayU);
        svd[1] = new DoubleDiagonalMatrix(arrayS);
        svd[2] = new DoubleDiagonalMatrix(arrayV);
        return svd;
    }

    /**
     * Applies a function on all the matrix elements.
     *
     * @param f a user-defined function
     * @return a double matrix
     */
    public AbstractDoubleMatrix mapElements(final PrimitiveMapping f) {
        final double[][] ans = new double[numRows()][numColumns()];
        double val = f.map(0);
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                ans[i][j] = val;
            }
        }
        for (int i = 0; i < numRows(); i++)
            ans[i][i] = f.map(diag[i]);
        return new DoubleMatrix(ans);
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new IntegerDiagonalMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an int array.
     */
    public int[][] toPrimitiveArray() {
        final int[][] result = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++)
            result[i][i] = diag[i];
        return result;
    }

}
