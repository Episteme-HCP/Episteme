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
import org.jscience.mathematics.algebraic.SquareMatrix;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.util.IllegalDimensionException;
import org.jscience.util.MaximumIterationsExceededException;

/**
 * The AbstractDoubleSquareMatrix class provides an object for encapsulating double square matrices.
 *
 * @author Mark Hale
 * @version 2.2
 */
public abstract class AbstractDoubleSquareMatrix extends AbstractDoubleMatrix implements SquareMatrix {
    protected transient AbstractDoubleSquareMatrix[] LU;
    protected transient int[] LUpivot;

    /**
     * Constructs a matrix.
     */
    protected AbstractDoubleSquareMatrix(final int size) {
        super(size, size);
    }

    /**
     * Converts this matrix to an integer matrix.
     *
     * @return an integer square matrix
     */
    public AbstractIntegerMatrix toIntegerMatrix() {
        final int ans[][] = new int[numRows()][numColumns()];
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
        ComplexSquareMatrix cm = new ComplexSquareMatrix(numRows());
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                cm.setElement(i, j, getPrimitiveElement(i, j), 0.0);
        }
        return cm;
    }

    /**
     * Returns true if this matrix is symmetric.
     */
    public boolean isSymmetric() {
        return this.equals(this.transpose());
    }

    /**
     * Returns true if this matrix is unitary.
     */
    public boolean isUnitary() {
        return this.multiply((AbstractDoubleSquareMatrix) this.transpose()).equals(DoubleDiagonalMatrix.identity(numRows()));
    }

    /**
     * Returns the determinant.
     */
    public double det() {
        if (numElements() > 0) {
            if (numRows() == 2) {
                return getPrimitiveElement(0, 0) * getPrimitiveElement(1, 1) - getPrimitiveElement(0, 1) * getPrimitiveElement(1, 0);
            } else {
                final AbstractDoubleSquareMatrix lu[] = this.luDecompose(null);
                double det = lu[1].getPrimitiveElement(0, 0);
                for (int i = 1; i < numRows(); i++)
                    det *= lu[1].getPrimitiveElement(i, i);
                return det * LUpivot[numRows()];
            }
        } else
            throw new ArithmeticException("The determinant of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the trace.
     */
    public double trace() {
        if (numElements() > 0) {
            double result = getPrimitiveElement(0, 0);
            for (int i = 1; i < numRows(); i++)
                result += getPrimitiveElement(i, i);
            return result;
        } else
            throw new ArithmeticException("The trace of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the operator norm.
     *
     * @throws MaximumIterationsExceededException
     *          If it takes more than 50 iterations to determine an eigenvalue.
     */
    public double operatorNorm() throws MaximumIterationsExceededException {
        if (numElements() > 0) {
            return Math.sqrt(ArrayMathUtils.max(LinearMathUtils.eigenvalueSolveSymmetric((AbstractDoubleSquareMatrix) (((AbstractDoubleSquareMatrix) this.transpose()).multiply(this)))));
        } else
            throw new ArithmeticException("The operator norm of a zero dimension matrix is undefined.");
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
            array[i][0] = -getPrimitiveElement(i, 0);
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = -getPrimitiveElement(i, j);
        }
        return new DoubleSquareMatrix(array);
    }

// ADDITION

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a double square matrix
     * @throws IllegalDimensionException If the matrices are not square or different sizes.
     */
    public final AbstractDoubleMatrix add(final AbstractDoubleMatrix m) {
        if (m instanceof AbstractDoubleSquareMatrix)
            return add((AbstractDoubleSquareMatrix) m);
        else if (numRows() == m.numRows() && numColumns() == m.numColumns())
            return add(new SquareMatrixAdaptor(m));
        else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a double square matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractDoubleSquareMatrix add(final AbstractDoubleSquareMatrix m) {
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

// SUBTRACTION

    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m a double square matrix
     * @throws IllegalDimensionException If the matrices are not square or different sizes.
     */
    public final AbstractDoubleMatrix subtract(final AbstractDoubleMatrix m) {
        if (m instanceof AbstractDoubleSquareMatrix)
            return subtract((AbstractDoubleSquareMatrix) m);
        else if (numRows() == m.numRows() && numColumns() == m.numColumns())
            return subtract(new SquareMatrixAdaptor(m));
        else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    /**
     * Returns the subtraction of this matrix by another.
     *
     * @param m a double square matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractDoubleSquareMatrix subtract(final AbstractDoubleSquareMatrix m) {
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

// SCALAR MULTIPLICATION

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a double.
     * @return a double square matrix.
     */
    public AbstractDoubleMatrix scalarMultiply(final double x) {
        final double array[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = x * getPrimitiveElement(i, 0);
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = x * getPrimitiveElement(i, j);
        }
        return new DoubleSquareMatrix(array);
    }

// SCALAR DIVISON

    /**
     * Returns the division of this matrix by a scalar.
     *
     * @param x a double.
     * @return a double square matrix.
     */
    public AbstractDoubleMatrix scalarDivide(final double x) {
        final double array[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = getPrimitiveElement(i, 0) / x;
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = getPrimitiveElement(i, j) / x;
        }
        return new DoubleSquareMatrix(array);
    }

// SCALAR PRODUCT

    /**
     * Returns the scalar product of this matrix and another.
     *
     * @param m a double square matrix.
     * @throws IllegalDimensionException If the matrices are not square or different sizes.
     */
    public final double scalarProduct(final AbstractDoubleMatrix m) {
        if (m instanceof AbstractDoubleSquareMatrix)
            return scalarProduct((AbstractDoubleSquareMatrix) m);
        else if (numRows() == m.numRows() && numColumns() == m.numColumns())
            return scalarProduct(new SquareMatrixAdaptor(m));
        else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    /**
     * Returns the scalar product of this matrix and another.
     *
     * @param m a double square matrix.
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public double scalarProduct(final AbstractDoubleSquareMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double ans = 0;
            for (int i = 0; i < numRows(); i++) {
                ans += getPrimitiveElement(i, 0) * m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    ans += getPrimitiveElement(i, j) * m.getPrimitiveElement(i, j);
            }
            return ans;
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

// MATRIX MULTIPLICATION

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a double square matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractDoubleSquareMatrix multiply(final AbstractDoubleSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            final int mColumns = m.numColumns();
            final double array[][] = new double[numRows()][mColumns];
            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < mColumns; k++) {
                    array[j][k] = getPrimitiveElement(j, 0) * m.getPrimitiveElement(0, k);
                    for (int n = 1; n < numColumns(); n++)
                        array[j][k] += getPrimitiveElement(j, n) * m.getPrimitiveElement(n, k);
                }
            }
            return new DoubleSquareMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

// DIRECT SUM

    /**
     * Returns the direct sum of this matrix and another.
     */
    public AbstractDoubleSquareMatrix directSum(final AbstractDoubleSquareMatrix m) {
        final double array[][] = new double[numRows() + m.numRows()][numColumns() + m.numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                array[i][j] = getPrimitiveElement(i, j);
        }
        for (int i = 0; i < m.numRows(); i++) {
            for (int j = 0; j < m.numColumns(); j++)
                array[i + numRows()][j + numColumns()] = m.getPrimitiveElement(i, j);
        }
        return new DoubleSquareMatrix(array);
    }

// TENSOR PRODUCT

    /**
     * Returns the tensor product of this matrix and another.
     */
    public AbstractDoubleSquareMatrix tensorProduct(final AbstractDoubleSquareMatrix m) {
        final double array[][] = new double[numRows() * m.numRows()][numColumns() * m.numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                for (int k = 0; k < m.numRows(); j++) {
                    for (int l = 0; l < m.numColumns(); l++)
                        array[i * m.numRows() + k][j * m.numColumns() + l] = getPrimitiveElement(i, j) * m.getPrimitiveElement(k, l);
                }
            }
        }
        return new DoubleSquareMatrix(array);
    }

// TRANSPOSE

    /**
     * Returns the transpose of this matrix.
     *
     * @return a double square matrix
     */
    public Matrix transpose() {
        final double array[][] = new double[numColumns()][numRows()];
        for (int i = 0; i < numRows(); i++) {
            array[0][i] = getPrimitiveElement(i, 0);
            for (int j = 1; j < numColumns(); j++)
                array[j][i] = getPrimitiveElement(i, j);
        }
        return new DoubleSquareMatrix(array);
    }

// INVERSE

    /**
     * Returns the inverse of this matrix.
     *
     * @return a double square matrix.
     */
    public AbstractDoubleSquareMatrix inverse() {
        final int N = numRows();
        final double arrayL[][] = new double[N][N];
        final double arrayU[][] = new double[N][N];
        final AbstractDoubleSquareMatrix lu[] = this.luDecompose(null);
        arrayL[0][0] = 1.0 / lu[0].getPrimitiveElement(0, 0);
        arrayU[0][0] = 1.0 / lu[1].getPrimitiveElement(0, 0);
        for (int i = 1; i < N; i++) {
            arrayL[i][i] = 1.0 / lu[0].getPrimitiveElement(i, i);
            arrayU[i][i] = 1.0 / lu[1].getPrimitiveElement(i, i);
        }
        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                double tmpL = 0.0, tmpU = 0.0;
                for (int k = i; k < j; k++) {
                    tmpL -= lu[0].getPrimitiveElement(j, k) * arrayL[k][i];
                    tmpU -= arrayU[i][k] * lu[1].getPrimitiveElement(k, j);
                }
                arrayL[j][i] = tmpL / lu[0].getPrimitiveElement(j, j);
                arrayU[i][j] = tmpU / lu[1].getPrimitiveElement(j, j);
            }
        }
        // matrix multiply arrayU x arrayL
        final double inv[][] = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                for (int k = i; k < N; k++)
                    inv[i][LUpivot[j]] += arrayU[i][k] * arrayL[k][j];
            }
            for (int j = i; j < N; j++) {
                for (int k = j; k < N; k++)
                    inv[i][LUpivot[j]] += arrayU[i][k] * arrayL[k][j];
            }
        }
        return new DoubleSquareMatrix(inv);
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
     * @planetmath LUDecomposition
     */
    public AbstractDoubleSquareMatrix[] luDecompose(int pivot[]) {
        if (LU != null) {
            if (pivot != null)
                System.arraycopy(LUpivot, 0, pivot, 0, pivot.length);
            return LU;
        }
        int pivotrow;
        final int N = numRows();
        final double arrayL[][] = new double[N][N];
        final double arrayU[][] = new double[N][N];
        final double buf[] = new double[N];
        if (pivot == null)
            pivot = new int[N + 1];
        for (int i = 0; i < N; i++)
            pivot[i] = i;
        pivot[N] = 1;
        // LU decomposition to arrayU
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < j; i++) {
                double tmp = getPrimitiveElement(pivot[i], j);
                for (int k = 0; k < i; k++)
                    tmp -= arrayU[i][k] * arrayU[k][j];
                arrayU[i][j] = tmp;
            }
            double max = 0.0;
            pivotrow = j;
            for (int i = j; i < N; i++) {
                double tmp = getPrimitiveElement(pivot[i], j);
                for (int k = 0; k < j; k++)
                    tmp -= arrayU[i][k] * arrayU[k][j];
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
                pivot[N] = -pivot[N];
            }
            // divide by pivot
            double tmp = arrayU[j][j];
            for (int i = j + 1; i < N; i++)
                arrayU[i][j] /= tmp;
        }
        // move lower triangular part to arrayL
        for (int j = 0; j < N; j++) {
            arrayL[j][j] = 1.0;
            for (int i = j + 1; i < N; i++) {
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
     * Returns the Cholesky decomposition of this matrix.
     * Matrix must be symmetric and positive definite.
     *
     * @return an array with [0] containing the L-matrix and [1] containing the U-matrix.
     * @planetmath CholeskyDecomposition
     */
    public AbstractDoubleSquareMatrix[] choleskyDecompose() {
        final int N = numRows();
        final double arrayL[][] = new double[N][N];
        final double arrayU[][] = new double[N][N];
        double tmp = Math.sqrt(getPrimitiveElement(0, 0));
        arrayL[0][0] = arrayU[0][0] = tmp;
        for (int i = 1; i < N; i++)
            arrayL[i][0] = arrayU[0][i] = getPrimitiveElement(i, 0) / tmp;
        for (int j = 1; j < N; j++) {
            tmp = getPrimitiveElement(j, j);
            for (int i = 0; i < j; i++)
                tmp -= arrayL[j][i] * arrayL[j][i];
            arrayL[j][j] = arrayU[j][j] = Math.sqrt(tmp);
            for (int i = j + 1; i < N; i++) {
                tmp = getPrimitiveElement(i, j);
                for (int k = 0; k < i; k++)
                    tmp -= arrayL[j][k] * arrayU[k][i];
                arrayL[i][j] = arrayU[j][i] = tmp / arrayU[j][j];
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
        for (int i = 0; i < N; i++) {
            array[i][0] = getPrimitiveElement(i, 0);
            for (int j = 1; j < N; j++)
                array[i][j] = getPrimitiveElement(i, j);
        }
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
     * @planetmath SingularValueDecomposition
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
        for (int i = 0; i < N; i++) {
            array[i][0] = getPrimitiveElement(i, 0);
            for (int j = 1; j < N; j++)
                array[i][j] = getPrimitiveElement(i, j);
        }
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

// POLAR DECOMPOSITION

    /**
     * Returns the polar decomposition of this matrix.
     */
    public AbstractDoubleSquareMatrix[] polarDecompose() {
        final int N = numRows();
        final AbstractDoubleVector evec[] = new AbstractDoubleVector[N];
        double eval[];
        try {
            eval = LinearMathUtils.eigenSolveSymmetric(this, evec);
        } catch (MaximumIterationsExceededException e) {
            return null;
        }
        final double tmpa[][] = new double[N][N];
        final double tmpm[][] = new double[N][N];
        double abs;
        for (int i = 0; i < N; i++) {
            abs = Math.abs(eval[i]);
            tmpa[i][0] = eval[i] * evec[i].getPrimitiveElement(0) / abs;
            tmpm[i][0] = abs * evec[i].getPrimitiveElement(0);
            for (int j = 1; j < N; j++) {
                tmpa[i][j] = eval[i] * evec[i].getPrimitiveElement(j) / abs;
                tmpm[i][j] = abs * evec[i].getPrimitiveElement(j);
            }
        }
        final double arg[][] = new double[N][N];
        final double mod[][] = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                arg[i][j] = evec[0].getPrimitiveElement(i) * tmpa[0][j];
                mod[i][j] = evec[0].getPrimitiveElement(i) * tmpm[0][j];
                for (int k = 1; k < N; k++) {
                    arg[i][j] += evec[k].getPrimitiveElement(i) * tmpa[k][j];
                    mod[i][j] += evec[k].getPrimitiveElement(i) * tmpm[k][j];
                }
            }
        }
        final AbstractDoubleSquareMatrix us[] = new AbstractDoubleSquareMatrix[2];
        us[0] = new DoubleSquareMatrix(arg);
        us[1] = new DoubleSquareMatrix(mod);
        return us;
    }

    private static class SquareMatrixAdaptor extends AbstractDoubleSquareMatrix {
        private final AbstractDoubleMatrix matrix;

        private SquareMatrixAdaptor(AbstractDoubleMatrix m) {
            super(m.numRows());
            matrix = m;
        }

        public double getPrimitiveElement(final int i, final int j) {
            return matrix.getPrimitiveElement(i, j);
        }

        public void setElement(final int i, final int j, final double x) {
            matrix.setElement(i, j, x);
        }
    }
}
