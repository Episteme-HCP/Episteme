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
import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.AbstractMatrix;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.IllegalDimensionException;

import java.io.BufferedReader;
import java.io.StreamTokenizer;

/**
 * The AbstractIntegerMatrix class provides an object for encapsulating integer matrices.
 *
 * @author Mark Hale
 * @version 2.2
 */

//hashcode, norm, norm, normalize, infnorm, trace, hermitianAdjoint, operatorNorm new dimension checking

//Jama provides some more methods from which we could get some inspiration: see AbstractDoubleMatrix

public abstract class AbstractIntegerMatrix extends AbstractMatrix {
    /**
     * Constructs a matrix.
     */
    protected AbstractIntegerMatrix(final int rows, final int cols) {
        super(rows, cols);
    }

    /**
     * Compares two ${nativeTyp} matrices for equality.
     *
     * @param obj a int matrix
     */
    public boolean equals(Object obj) {
        if (obj instanceof AbstractIntegerMatrix) {
            return equals((AbstractIntegerMatrix) obj);
        } else {
            return false;
        }
    }

    /**
     * Compares two ${nativeTyp} matrices for equality.
     * Two matrices are considered to be equal if the Frobenius norm of their difference is within the zero tolerance.
     *
     * @param m a double matrix
     */
    public boolean equals(AbstractIntegerMatrix m) {
        return equals(m, java.lang.Double.valueOf(JScience.getProperty("tolerance")).doubleValue());
    }

    public boolean equals(AbstractIntegerMatrix m, double tol) {
        if (m != null && numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double sumSqr = 0;
            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++) {
                    double delta = getPrimitiveElement(i, j) - m.getPrimitiveElement(i, j);
                    sumSqr += delta * delta;
                }
            }
            return (sumSqr <= tol * tol);
        } else {
            return false;
        }
    }

    /**
     * Compares two ${nativeTyp} matrices for equality.
     * Two matrices are considered to be equal if their element by element difference is zero.
     *
     * @param m a int matrix
     */
    /**public boolean equals(AbstractIntegerMatrix m) {
     if (m != null && numRows() == m.numRows() && numColumns() == m.numColumns()) {
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
     } else {
     return false;
     }
     }*/

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
     * Returns a hashcode for this NON EMPTY matrix.
     */
    public int hashCode() {
        return (int) Math.exp(infNorm());
    }

    /**
     * Converts this matrix to a double matrix.
     *
     * @return a double matrix
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
        ComplexMatrix cm = new ComplexMatrix(numRows(), numColumns());
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                cm.setElement(i, j, getPrimitiveElement(i, j), 0.0);
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
    public abstract int getPrimitiveElement(int i, int j);

    /**
     * Returns an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public Integer getElement(final int i, final int j) {
        return new Integer(getPrimitiveElement(i, j));
    }

    /**
     * Returns the ith row.
     */
    public AbstractIntegerVector getRow(final int i) {

        int[] elements;

        if ((i >= 0) && (i < numRows())) {
            elements = new int[numColumns()];
            for (int j = 0; j < numColumns(); j++) {
                elements[j] = getPrimitiveElement(i, j);
            }
            return new IntegerVector(elements);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Returns the ith column.
     */
    public AbstractIntegerVector getColumn(final int j) {

        int[] elements;

        if ((j >= 0) && (j < numColumns())) {
            elements = new int[numRows()];
            for (int i = 0; i < numRows(); i++) {
                elements[i] = getPrimitiveElement(i, j);
            }
            return new IntegerVector(elements);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Returns the ith row.
     */
    public void setRow(final int i, final AbstractIntegerVector v) {

        if ((i >= 0) && (i < numRows())) {
            if (v.getDimension() == numColumns()) {
                for (int j = 0; j < numColumns(); j++)
                    setElement(i, j, v.getPrimitiveElement(j));
            } else
                throw new IllegalDimensionException("The vector has not the required dimension.");
        } else
            throw new IllegalDimensionException("Element out of bounds.");

    }

    /**
     * Returns the ith column.
     */
    public void setColumn(final int j, final AbstractIntegerVector v) {

        if ((j >= 0) && (j < numColumns())) {
            if (v.getDimension() == numRows()) {
                for (int i = 0; i < numRows(); i++) {
                    setElement(i, j, v.getPrimitiveElement(i));
                }
            } else
                throw new IllegalDimensionException("The vector has not the required dimension.");
        } else
            throw new IllegalDimensionException("Element out of bounds.");

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
    public abstract void setElement(int i, int j, int x);

    /**
     * Sets the value of all elements of the matrix.
     *
     * @param m a complex element
     */
    public void setAllElements(final int m) {
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                setElement(i, j, m);
            }
        }
    }

    public final Object getSet() {
        return IntegerMatrixAlgebra.get(numRows(), numColumns());
    }

    /**
     * Returns the l<sup><img border=0 alt="infinity" src="doc-files/infinity.gif"></sup>-norm.
     *
     * @author Taber Smith
     */
    public int infNorm() {
        if (numElements() > 0) {
            int result = 0, tmpResult;
            for (int i = 0; i < numRows(); i++) {
                tmpResult = 0;
                for (int j = 0; j < numColumns(); j++)
                    tmpResult += Math.abs(getPrimitiveElement(i, j));
                if (tmpResult > result)
                    result = tmpResult;
            }
            return result;
        } else
            throw new ArithmeticException("The inf norm of a zero dimension vector is undefined.");
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
                    result = MathUtils.hypot(result, getPrimitiveElement(i, j));
            }
            return result;
        } else
            throw new ArithmeticException("The frobenius norm of a zero dimension vector is undefined.");
    }

    /**
     * Applies the abs function on all the matrix components.
     */
    public AbstractIntegerMatrix abs() {
        final int array[][] = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                array[i][j] = Math.abs(getPrimitiveElement(i, j));
            }
        }
        return new IntegerMatrix(array);
    }

    /**
     * Gets the min of the matrix components.
     *
     * @return the min.
     */
    public int min() {
        int result;
        if ((numRows() > 0) || (numColumns() > 0)) {
            result = getPrimitiveElement(0, 0);
            for (int i = 0; i < numRows(); i++)
                for (int j = 0; j < numColumns(); j++)
                    if (getPrimitiveElement(i, j) < result) {
                        result = getPrimitiveElement(i, j);
                    }
            return result;
        } else
            throw new ArithmeticException("The minimum of a zero dimension matrix is undefined.");
    }

    /**
     * Gets the mass of the matrix components.
     *
     * @return the mass.
     */
    public int mass() {
        int result;
        if ((numRows() > 0) || (numColumns() > 0)) {
            result = 0;
            for (int i = 0; i < numRows(); i++)
                for (int j = 0; j < numColumns(); j++)
                    result += getPrimitiveElement(i, j);
            return result;
        } else
            throw new ArithmeticException("The mass of a zero dimension matrix is undefined.");
    }

    /**
     * Gets the max of the matrix components.
     *
     * @return the max.
     */
    public int max() {
        int result;
        if ((numRows() > 0) || (numColumns() > 0)) {
            result = getPrimitiveElement(0, 0);
            for (int i = 0; i < numRows(); i++)
                for (int j = 0; j < numColumns(); j++)
                    if (getPrimitiveElement(i, j) > result) {
                        result = getPrimitiveElement(i, j);
                    }
            return result;
        } else
            throw new ArithmeticException("The maximum of a zero dimension matrix is undefined.");
    }

    /**
     * Gets the mean of the matrix components.
     *
     * @return the mean.
     */
    public double mean() {
        double result;
        if ((numRows() > 0) || (numColumns() > 0)) {
            result = 0;
            for (int i = 0; i < numRows(); i++)
                for (int j = 0; j < numColumns(); j++)
                    result += getPrimitiveElement(i, j);
            return result / numElements();
        } else
            throw new ArithmeticException("The mean of a zero dimension matrix is undefined.");
    }

//============
// OPERATIONS
//============

    /**
     * Returns the negative of this matrix.
     */
    public AbelianGroup.Member negate() {
        final int array[][] = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = -getPrimitiveElement(i, 0);
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = -getPrimitiveElement(i, j);
        }
        return new IntegerMatrix(array);
    }

// ADDITION

    /**
     * Returns the addition of this matrix and another.
     */
    public final AbelianGroup.Member add(final AbelianGroup.Member m) {
        if (m instanceof AbstractIntegerMatrix)
            return add((AbstractIntegerMatrix) m);
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a int matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractIntegerMatrix add(final AbstractIntegerMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = getPrimitiveElement(i, 0) + m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = getPrimitiveElement(i, j) + m.getPrimitiveElement(i, j);
            }
            return new IntegerMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this matrix by another.
     */
    public final AbelianGroup.Member subtract(final AbelianGroup.Member m) {
        if (m instanceof AbstractIntegerMatrix)
            return subtract((AbstractIntegerMatrix) m);
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the subtraction of this matrix by another.
     *
     * @param m a int matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractIntegerMatrix subtract(final AbstractIntegerMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final int array[][] = new int[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                array[i][0] = getPrimitiveElement(i, 0) - m.getPrimitiveElement(i, 0);
                for (int j = 1; j < numColumns(); j++)
                    array[i][j] = getPrimitiveElement(i, j) - m.getPrimitiveElement(i, j);
            }
            return new IntegerMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

// SCALAR MULTIPLICATION

    /**
     * Returns the multiplication of this matrix by a scalar.
     */
    public final Module.Member scalarMultiply(Ring.Member x) {
        if (x instanceof Number) {
            return scalarMultiply(((Number) x).intValue());
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a int.
     * @return a int matrix.
     */
    public AbstractIntegerMatrix scalarMultiply(final int x) {
        final int array[][] = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = x * getPrimitiveElement(i, 0);
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = x * getPrimitiveElement(i, j);
        }
        return new IntegerMatrix(array);
    }

// SCALAR DIVISON

    /**
     * Returns the division of this matrix by a scalar.
     * Always throws an exception.
     */
    public final VectorSpace.Member scalarDivide(Field.Member x) {
        throw new UnsupportedOperationException("Not an algebra");
    }

// SCALAR PRODUCT

    /**
     * Returns the scalar product of this matrix and another.
     *
     * @param m a int matrix.
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public int scalarProduct(final AbstractIntegerMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            int ans = 0;
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
     * Returns the multiplication of a vector by this matrix.
     *
     * @param v a int vector.
     * @throws IllegalDimensionException If the matrix and vector are incompatible.
     */
    public AbstractIntegerVector multiply(final AbstractIntegerVector v) {
        if (numColumns() == v.getDimension()) {
            final int array[] = new int[numRows()];
            for (int i = 0; i < numRows(); i++) {
                array[i] = getPrimitiveElement(i, 0) * v.getPrimitiveElement(0);
                for (int j = 1; j < numColumns(); j++)
                    array[i] += getPrimitiveElement(i, j) * v.getPrimitiveElement(j);
            }
            return new IntegerVector(array);
        } else {
            throw new IllegalDimensionException("Matrix and vector are incompatible.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     */
    public final Ring.Member multiply(final Ring.Member m) {
        if (m instanceof AbstractIntegerMatrix)
            return multiply((AbstractIntegerMatrix) m);
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a int matrix
     * @return a AbstractIntegerMatrix or a AbstractIntegerSquareMatrix as appropriate
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public AbstractIntegerMatrix multiply(final AbstractIntegerMatrix m) {
        if (numColumns() == m.numRows()) {
            final int mColumns = m.numColumns();
            final int array[][] = new int[numRows()][mColumns];
            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < mColumns; k++) {
                    array[j][k] = getPrimitiveElement(j, 0) * m.getPrimitiveElement(0, k);
                    for (int n = 1; n < numColumns(); n++)
                        array[j][k] += getPrimitiveElement(j, n) * m.getPrimitiveElement(n, k);
                }
            }
            if (numRows() == mColumns)
                return new IntegerSquareMatrix(array);
            else
                return new IntegerMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

// DIRECT SUM

    /**
     * Returns the direct sum of this matrix and another.
     */
    public AbstractIntegerMatrix directSum(final AbstractIntegerMatrix m) {
        final int array[][] = new int[numRows() + m.numRows()][numColumns() + m.numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++)
                array[i][j] = getPrimitiveElement(i, j);
        }
        for (int i = 0; i < m.numRows(); i++) {
            for (int j = 0; j < m.numColumns(); j++)
                array[i + numRows()][j + numColumns()] = m.getPrimitiveElement(i, j);
        }
        return new IntegerMatrix(array);
    }

// TENSOR PRODUCT

    /**
     * Returns the tensor product of this matrix and another.
     */
    public AbstractIntegerMatrix tensorProduct(final AbstractIntegerMatrix m) {
        final int array[][] = new int[numRows() * m.numRows()][numColumns() * m.numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                for (int k = 0; k < m.numRows(); j++) {
                    for (int l = 0; l < m.numColumns(); l++)
                        array[i * m.numRows() + k][j * m.numColumns() + l] = getPrimitiveElement(i, j) * m.getPrimitiveElement(k, l);
                }
            }
        }
        return new IntegerMatrix(array);
    }

// TRANSPOSE

    /**
     * Returns the transpose of this matrix.
     *
     * @return a int matrix
     */
    public Matrix transpose() {
        final int array[][] = new int[numColumns()][numRows()];
        for (int i = 0; i < numRows(); i++) {
            array[0][i] = getPrimitiveElement(i, 0);
            for (int j = 1; j < numColumns(); j++)
                array[j][i] = getPrimitiveElement(i, j);
        }
        return new IntegerMatrix(array);
    }

    /**
     * Invert matrix elements order from the top to the bottom.
     */
    public AbstractIntegerMatrix horizontalAxisSymmetry() {
        final int array[][] = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                array[i][j] = getPrimitiveElement(numRows() - i, j);
            }
        }
        return new IntegerMatrix(array);
    }

    /**
     * Invert matrix elements order from the right to the left.
     */
    public AbstractIntegerMatrix verticalAxisSymmetry() {
        final int array[][] = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                array[i][j] = getPrimitiveElement(i, numColumns() - j);
            }
        }
        return new IntegerMatrix(array);
    }

    /**
     * Invert matrix elements order from the top to the bottom, from the right to the left.
     */
    public AbstractIntegerMatrix reverse() {
        final int array[][] = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                array[i][j] = getPrimitiveElement(numRows() - i, numColumns() - j);
            }
        }
        return new IntegerMatrix(array);
    }

    /**
     * Computes a sub matrix from the parameters index.
     * If k1<0 k1 elements are added at the beginning of the returned matrix
     * If k2>numRows() k2-numRows() elements are added at the end of the returned matrix
     * Finally, if k1>k2 the vector is returned inverted.
     * If k3<0 k3 elements are added at the beginning of the returned matrix
     * If k4>numColumns() k2-numColumns() elements are added at the end of the returned matrix
     * Finally, if k3>k4 the matrix is returned inverted.
     *
     * @param k1 the beginning rows index
     * @param k2 the finishing rows index
     * @param k3 the beginning columns index
     * @param k4 the finishing columns index
     * @return the sub matrix.
     */
    public AbstractIntegerMatrix getSubMatrix(final int k1, final int k2, final int k3, final int k4) {
        final int[][] ans = new int[Math.abs(k2 - k1) + 1][Math.abs(k4 - k3) + 1];
        if (k1 < k2) {
            if (k3 < k4) {
                for (int i = Math.max(k1, 0); i < Math.min(k2, numRows()); i++) {
                    for (int j = Math.max(k3, 0); j < Math.min(k4, numColumns()); j++) {
                        ans[i - k1][j - k3] = getPrimitiveElement(i, j);
                    }
                }
            } else {
                for (int i = Math.max(k1, 0); i < Math.min(k2, numRows()); i++) {
                    for (int j = Math.max(k4, 0); j < Math.min(k3, numColumns()); j++) {
                        ans[i - k1][k3 - j] = getPrimitiveElement(i, j);
                    }
                }
            }
        } else {
            if (k3 < k4) {
                for (int i = Math.max(k2, 0); i < Math.min(k1, numRows()); i++) {
                    for (int j = Math.max(k3, 0); j < Math.min(k4, numColumns()); j++) {
                        ans[k1 - i][j - k3] = getPrimitiveElement(i, j);
                    }
                }
            } else {
                for (int i = Math.max(k2, 0); i < Math.min(k1, numRows()); i++) {
                    for (int j = Math.max(k3, 0); j < Math.min(k4, numColumns()); j++) {
                        ans[k1 - i][k3 - j] = getPrimitiveElement(i, j);
                    }
                }
            }
        }
        return new IntegerMatrix(ans);
    }

    /**
     * Set a sub matrix.
     * If k<0 k elements are added at the beginning of the returned matrix
     * If k+v.numRows()>numRows() k+v.numRows()-numRows() elements are added at the end of the returned matrix
     * If l<0 l elements are added at the beginning of the returned matrix
     * If l+v.numColumns()>numColumns() l+v.numColumns()-numColumns() elements are added at the end of the returned matrix
     *
     * @param k Initial row index to offset the patching matrix
     * @param l Initial column index to offset the patching matrix
     * @param m the patching matrix
     */
    //todo: we are using a suboptimal algorithm here by filling the matrix with old elements
    //and patching over where needed ; a better algorithm would involve many case but would have only one assignment per element
    public AbstractIntegerMatrix setSubMatrix(final int k, final int l, final AbstractIntegerMatrix m) {
        if (k < 0) {
            if (l < 0) {
                int[][] ans = new int[Math.max(numRows() - k, m.numRows())][Math.max(numColumns() - l, m.numColumns())];
                for (int i = 0; i < numRows(); i++)
                    for (int j = 0; j < numRows(); j++)
                        ans[i - k][j - l] = getPrimitiveElement(i, j);
                for (int i = 0; i < m.numRows(); i++)
                    for (int j = 0; j < m.numRows(); j++)
                        ans[i][j] = m.getPrimitiveElement(i, j);
                return new IntegerMatrix(ans);
            } else {
                int[][] ans = new int[Math.max(numRows() - k, m.numRows())][Math.max(numColumns(), m.numColumns() + l)];
                for (int i = 0; i < numRows(); i++)
                    for (int j = 0; j < numRows(); j++)
                        ans[i - k][j] = getPrimitiveElement(i, j);
                for (int i = 0; i < m.numRows(); i++)
                    for (int j = 0; j < m.numRows(); j++)
                        ans[i][j + l] = m.getPrimitiveElement(i, j);
                return new IntegerMatrix(ans);
            }
        } else {
            if (l < 0) {
                int[][] ans = new int[Math.max(numRows(), m.numRows() + k)][Math.max(numColumns() - l, m.numColumns())];
                for (int i = 0; i < numRows(); i++)
                    for (int j = 0; j < numRows(); j++)
                        ans[i][j - l] = getPrimitiveElement(i, j);
                for (int i = 0; i < m.numRows(); i++)
                    for (int j = 0; j < m.numRows(); j++)
                        ans[i + k][j] = m.getPrimitiveElement(i, j);
                return new IntegerMatrix(ans);
            } else {
                int[][] ans = new int[Math.max(numRows(), m.numRows() + k)][Math.max(numColumns(), m.numColumns() + l)];
                for (int i = 0; i < numRows(); i++)
                    for (int j = 0; j < numRows(); j++)
                        ans[i][j] = getPrimitiveElement(i, j);
                for (int i = 0; i < m.numRows(); i++)
                    for (int j = 0; j < m.numRows(); j++)
                        ans[i + k][j + l] = m.getPrimitiveElement(i, j);
                return new IntegerMatrix(ans);
            }
        }
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
            array[i][0] = f.map(getPrimitiveElement(i, 0));
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = f.map(getPrimitiveElement(i, j));
        }
        return new DoubleMatrix(array);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an int array.
     */
    public int[][] toPrimitiveArray() {
        final int[][] result = new int[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++)
            for (int j = 0; j < numColumns(); j++)
                result[i][j] = getPrimitiveElement(i, j);
        return result;
    }

    /**
     * Make a one-dimensional row packed copy of the internal array. Useful to iterate over the elements with an org.jscience.util.ArrayIterator
     *
     * @return Matrix elements packed in a one-dimensional array by rows.
     */
    public int[] getMatrixAsRows() {
        final int[] result = new int[numRows() * numColumns()];
        for (int i = 0; i < numRows(); i++)
            for (int j = 0; j < numColumns(); j++)
                result[i * numRows() + j] = getPrimitiveElement(i, j);
        return result;
    }

    /**
     * Make a one-dimensional column packed copy of the internal array. Useful to iterate over the elements with an org.jscience.util.ArrayIterator
     *
     * @return Matrix elements packed in a one-dimensional array by columns.
     */
    public int[] getMatrixAsColumns() {
        final int[] result = new int[numRows() * numColumns()];
        for (int i = 0; i < numColumns(); i++)
            for (int j = 0; j < numRows(); j++)
                result[i * numColumns() + j] = getPrimitiveElement(i, j);
        return result;
    }

    /**
     * Read a matrix from a stream.  The format is the same the print method,
     * so printed matrices can be read back in (provided they were printed using
     * US Locale).  Elements are separated by
     * whitespace, all the elements for each row appear on a single line,
     * the last row is followed by a blank line.
     *
     * @param input the input stream.
     */
    public static AbstractIntegerMatrix read(BufferedReader input) throws java.io.IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(input);

        // Although StreamTokenizer will parse numbers, it doesn't recognize
        // scientific notation (E or D); however, Double.valueOf does.
        // The strategy here is to disable StreamTokenizer's number parsing.
        // We'll only get whitespace delimited words, EOL's and EOF's.
        // These words should all be numbers, for Double.valueOf to parse.

        tokenizer.resetSyntax();
        tokenizer.wordChars(0, 255);
        tokenizer.whitespaceChars(0, ' ');
        tokenizer.eolIsSignificant(true);
        java.util.Vector v = new java.util.Vector();

        // Ignore initial empty lines
        while (tokenizer.nextToken() == StreamTokenizer.TT_EOL) ;
        if (tokenizer.ttype == StreamTokenizer.TT_EOF)
            throw new java.io.IOException("Unexpected EOF on matrix read.");
        do {
            v.addElement(new Integer(tokenizer.sval)); // Read & store 1st row.
        } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);

        int n = v.size();  // Now we've got the number of columns!
        int row[] = new int[n];
        for (int j = 0; j < n; j++)  // extract the elements of the 1st row.
            row[j] = ((Integer) v.elementAt(j)).intValue();
        v.removeAllElements();
        v.addElement(row);  // Start storing rows instead of columns.
        while (tokenizer.nextToken() == StreamTokenizer.TT_WORD) {
            // While non-empty lines
            v.addElement(row = new int[n]);
            int j = 0;
            do {
                if (j >= n)
                    throw new java.io.IOException
                            ("Row " + v.size() + " is too long.");
                row[j++] = new Integer(tokenizer.sval).intValue();
            } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
            if (j < n)
                throw new java.io.IOException
                        ("Row " + v.size() + " is too short.");
        }
        int m = v.size();  // Now we've got the number of rows.
        int[][] A = new int[m][];
        v.copyInto(A);  // copy the rows out of the vector
        return new IntegerMatrix(A);
    }
}
