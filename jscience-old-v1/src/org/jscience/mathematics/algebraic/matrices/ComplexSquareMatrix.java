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
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.ComplexMapping;
import org.jscience.util.IllegalDimensionException;
import org.jscience.util.MaximumIterationsExceededException;

import java.io.Serializable;

/**
 * The ComplexSquareMatrix class provides an object for encapsulating square matrices containing complex numbers.
 *
 * @author Mark Hale
 * @version 2.2
 */
public class ComplexSquareMatrix extends AbstractComplexSquareMatrix implements Cloneable, Serializable {
    /**
     * Arrays containing the elements of the matrix.
     */
    protected final double matrixRe[][], matrixIm[][];

    /**
     * Constructs a matrix by wrapping two arrays.
     *
     * @param arrayRe an array of real values
     * @param arrayIm an array of imaginary values
     * @throws IllegalDimensionException If the array is not square.
     */
    public ComplexSquareMatrix(final double arrayRe[][], final double arrayIm[][]) {
        super(arrayRe.length);
        if (!ArrayMathUtils.isSquare(arrayRe))
            throw new IllegalDimensionException("Array is not square.");
        if (!ArrayMathUtils.isSquare(arrayIm))
            throw new IllegalDimensionException("Array is not square.");
        matrixRe = arrayRe;
        matrixIm = arrayIm;
    }

    /**
     * Constructs an empty matrix.
     *
     * @param size the number of rows/columns
     */
    public ComplexSquareMatrix(final int size) {
        this(new double[size][size], new double[size][size]);
    }

    /**
     * Constructs a matrix from an array.
     *
     * @param array an assigned value
     * @throws IllegalDimensionException If the array is not square.
     */
    public ComplexSquareMatrix(final Complex array[][]) {
        this(array.length);
        for (int i = 0; i < numRows(); i++) {
            if (array[i].length != array.length)
                throw new IllegalDimensionException("Array is not square.");
            for (int j = 0; j < numColumns(); j++) {
                matrixRe[i][j] = array[i][j].real();
                matrixIm[i][j] = array[i][j].imag();
            }
        }
    }

    /**
     * Copy constructor.
     *
     * @param mat an assigned value
     */
    public ComplexSquareMatrix(final ComplexSquareMatrix mat) {
        this(mat.numRows());
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                matrixRe[i][j] = mat.matrixRe[i][j];
                matrixIm[i][j] = mat.matrixIm[i][j];
            }
        }
    }

    /**
     * Constructs a matrix from an array of vectors.
     * The vectors form columns in the matrix.
     *
     * @param array an assigned value.
     * @throws IllegalDimensionException If the array is not square.
     */
    public ComplexSquareMatrix(final AbstractComplexVector array[]) {
        this(array.length);
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                matrixRe[i][j] = array[j].getPrimitiveElement(i).real();
                matrixIm[i][j] = array[j].getPrimitiveElement(i).imag();
            }
        }
    }

    /**
     * Compares two complex matrices for equality.
     *
     * @param m a complex matrix
     */
    public boolean equals(AbstractComplexMatrix m, double tol) {
        if (m != null && numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double sumSqr = 0.0;
            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++) {
                    double deltaRe = matrixRe[i][j] - m.getRealElement(i, j);
                    double deltaIm = matrixIm[i][j] - m.getImagElement(i, j);
                    sumSqr += deltaRe * deltaRe + deltaIm * deltaIm;
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
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                buf.append(Complex.toString(matrixRe[i][j], matrixIm[i][j]));
                buf.append(' ');
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    /**
     * Returns the real part of this complex matrix.
     *
     * @return a double square matrix
     */
    public AbstractDoubleMatrix real() {
        return new DoubleSquareMatrix(matrixRe);
    }

    /**
     * Returns the imaginary part of this complex matrix.
     *
     * @return a double square matrix
     */
    public AbstractDoubleMatrix imag() {
        return new DoubleSquareMatrix(matrixIm);
    }

    /**
     * Returns an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public Complex getPrimitiveElement(final int i, final int j) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns())
            return new Complex(matrixRe[i][j], matrixIm[i][j]);
        else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    public double getRealElement(final int i, final int j) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns())
            return matrixRe[i][j];
        else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    public double getImagElement(final int i, final int j) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns())
            return matrixIm[i][j];
        else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    /**
     * Sets the value of an element of the matrix.
     * Should only be used to initialise this matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @param z a complex number
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public void setElement(final int i, final int j, final Complex z) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns()) {
            matrixRe[i][j] = z.real();
            matrixIm[i][j] = z.imag();
        } else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    /**
     * Sets the value of an element of the matrix.
     * Should only be used to initialise this matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @param x the real part of a complex number
     * @param y the imaginary part of a complex number
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public void setElement(final int i, final int j, final double x, final double y) {
        if (i >= 0 && i < numRows() && j >= 0 && j < numColumns()) {
            matrixRe[i][j] = x;
            matrixIm[i][j] = y;
        } else
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
    }

    /**
     * Sets the value of all elements of the matrix.
     *
     * @param m a complex element
     */
    public void setAllElements(final Complex m) {
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                matrixRe[i][j] = m.real();
                matrixIm[i][j] = m.imag();
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
            double result = 0.0, tmpResult;
            for (int i = 0; i < numRows(); i++) {
                tmpResult = 0.0;
                for (int j = 0; j < numColumns(); j++)
                    tmpResult += Math.sqrt((matrixRe[i][j] * matrixRe[i][j] + matrixIm[i][j] * matrixIm[i][j]));
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
     * @author Taber Smith
     */
    public double frobeniusNorm() {
        if (numElements() > 0) {
            double result = 0.0;
            for (int j, i = 0; i < numRows(); i++)
                for (j = 0; j < numColumns(); j++)
                    result += matrixRe[i][j] * matrixRe[i][j] + matrixIm[i][j] * matrixIm[i][j];
            return Math.sqrt(result);
        } else
            throw new ArithmeticException("The frobenius norm of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the determinant.
     */
    public Complex det() {
        if (numElements() > 0) {
            if (numRows() == 2) {
                return new Complex(matrixRe[0][0] * matrixRe[1][1] - matrixIm[0][0] * matrixIm[1][1] - matrixRe[0][1] * matrixRe[1][0] + matrixIm[0][1] * matrixIm[1][0],
                        matrixRe[0][0] * matrixIm[1][1] + matrixIm[0][0] * matrixRe[1][1] - matrixRe[0][1] * matrixIm[1][0] - matrixIm[0][1] * matrixRe[1][0]);
            } else {
                final ComplexSquareMatrix lu[] = (ComplexSquareMatrix[]) this.luDecompose(null);
                double tmp;
                double detRe = lu[1].matrixRe[0][0];
                double detIm = lu[1].matrixIm[0][0];
                for (int i = 1; i < numRows(); i++) {
                    tmp = detRe * lu[1].matrixRe[i][i] - detIm * lu[1].matrixIm[i][i];
                    detIm = detRe * lu[1].matrixIm[i][i] + detIm * lu[1].matrixRe[i][i];
                    detRe = tmp;
                }
                return new Complex(detRe * LUpivot[numRows()], detIm * LUpivot[numRows()]);
            }
        } else
            throw new ArithmeticException("The determinant of a zero dimension matrix is undefined.");
    }

    /**
     * Returns the trace.
     */
    public Complex trace() {
        if (numElements() > 0) {
            double trRe = matrixRe[0][0];
            double trIm = matrixIm[0][0];
            for (int i = 1; i < numRows(); i++) {
                trRe += matrixRe[i][i];
                trIm += matrixIm[i][i];
            }
            return new Complex(trRe, trIm);
        } else
            throw new ArithmeticException("The trace of a zero dimension matrix is undefined.");
    }

//============
// OPERATIONS
//============

    /**
     * Returns the negative of this matrix.
     */
    public AbelianGroup.Member negate() {
        final double arrayRe[][] = new double[numRows()][numColumns()];
        final double arrayIm[][] = new double[numRows()][numColumns()];
        for (int j, i = 0; i < numRows(); i++) {
            arrayRe[i][0] = -matrixRe[i][0];
            arrayIm[i][0] = -matrixIm[i][0];
            for (j = 1; j < numColumns(); j++) {
                arrayRe[i][j] = -matrixRe[i][j];
                arrayIm[i][j] = -matrixIm[i][j];
            }
        }
        return new ComplexSquareMatrix(arrayRe, arrayIm);
    }

// ADDITION

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a complex matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexSquareMatrix add(final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexSquareMatrix)
            return add((ComplexSquareMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double arrayRe[][] = new double[numRows()][numColumns()];
            final double arrayIm[][] = new double[numRows()][numColumns()];
            for (int j, i = 0; i < numRows(); i++) {
                arrayRe[i][0] = matrixRe[i][0] + m.getPrimitiveElement(i, 0).real();
                arrayIm[i][0] = matrixIm[i][0] + m.getPrimitiveElement(i, 0).imag();
                for (j = 1; j < numColumns(); j++) {
                    arrayRe[i][j] = matrixRe[i][j] + m.getPrimitiveElement(i, j).real();
                    arrayIm[i][j] = matrixIm[i][j] + m.getPrimitiveElement(i, j).imag();
                }
            }
            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    public ComplexSquareMatrix add(final ComplexSquareMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double arrayRe[][] = new double[numRows()][numColumns()];
            final double arrayIm[][] = new double[numRows()][numColumns()];
            for (int j, i = 0; i < numRows(); i++) {
                arrayRe[i][0] = matrixRe[i][0] + m.matrixRe[i][0];
                arrayIm[i][0] = matrixIm[i][0] + m.matrixIm[i][0];
                for (j = 1; j < numColumns(); j++) {
                    arrayRe[i][j] = matrixRe[i][j] + m.matrixRe[i][j];
                    arrayIm[i][j] = matrixIm[i][j] + m.matrixIm[i][j];
                }
            }
            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this matrix by another.
     *
     * @param m a complex matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexSquareMatrix subtract(final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexSquareMatrix)
            return subtract((ComplexSquareMatrix) m);

        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double arrayRe[][] = new double[numRows()][numColumns()];
            final double arrayIm[][] = new double[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                arrayRe[i][0] = matrixRe[i][0] - m.getPrimitiveElement(i, 0).real();
                arrayIm[i][0] = matrixIm[i][0] - m.getPrimitiveElement(i, 0).imag();
                for (int j = 1; j < numColumns(); j++) {
                    arrayRe[i][j] = matrixRe[i][j] - m.getPrimitiveElement(i, j).real();
                    arrayIm[i][j] = matrixIm[i][j] - m.getPrimitiveElement(i, j).imag();
                }
            }
            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

    public ComplexSquareMatrix subtract(final ComplexSquareMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double arrayRe[][] = new double[numRows()][numColumns()];
            final double arrayIm[][] = new double[numRows()][numColumns()];
            for (int j, i = 0; i < numRows(); i++) {
                arrayRe[i][0] = matrixRe[i][0] - m.matrixRe[i][0];
                arrayIm[i][0] = matrixIm[i][0] - m.matrixIm[i][0];
                for (j = 1; j < numColumns(); j++) {
                    arrayRe[i][j] = matrixRe[i][j] - m.matrixRe[i][j];
                    arrayIm[i][j] = matrixIm[i][j] - m.matrixIm[i][j];
                }
            }
            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// SCALAR MULTIPLICATION

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param z a complex number
     * @return a complex square matrix
     */
    public AbstractComplexMatrix scalarMultiply(final Complex z) {
        final double real = z.real();
        final double imag = z.imag();
        final double arrayRe[][] = new double[numRows()][numColumns()];
        final double arrayIm[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            arrayRe[i][0] = matrixRe[i][0] * real - matrixIm[i][0] * imag;
            arrayIm[i][0] = matrixRe[i][0] * imag + matrixIm[i][0] * real;
            for (int j = 1; j < numColumns(); j++) {
                arrayRe[i][j] = matrixRe[i][j] * real - matrixIm[i][j] * imag;
                arrayIm[i][j] = matrixRe[i][j] * imag + matrixIm[i][j] * real;
            }
        }
        return new ComplexSquareMatrix(arrayRe, arrayIm);
    }

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a double
     * @return a complex square matrix
     */
    public AbstractComplexMatrix scalarMultiply(final double x) {
        final double arrayRe[][] = new double[numRows()][numColumns()];
        final double arrayIm[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            arrayRe[i][0] = x * matrixRe[i][0];
            arrayIm[i][0] = x * matrixIm[i][0];
            for (int j = 1; j < numColumns(); j++) {
                arrayRe[i][j] = x * matrixRe[i][j];
                arrayIm[i][j] = x * matrixIm[i][j];
            }
        }
        return new ComplexSquareMatrix(arrayRe, arrayIm);
    }

// MATRIX MULTIPLICATION

    /**
     * Returns the multiplication of a vector by this matrix.
     *
     * @param v a complex vector
     * @throws IllegalDimensionException If the matrix and vector are incompatible.
     */
    public AbstractComplexVector multiply(final AbstractComplexVector v) {
        if (numColumns() == v.getDimension()) {
            final double arrayRe[] = new double[numRows()];
            final double arrayIm[] = new double[numRows()];
            Complex comp;
            for (int j, i = 0; i < numRows(); i++) {
                comp = v.getPrimitiveElement(0);
                arrayRe[i] = (matrixRe[i][0] * comp.real() - matrixIm[i][0] * comp.imag());
                arrayIm[i] = (matrixIm[i][0] * comp.real() + matrixRe[i][0] * comp.imag());
                for (j = 1; j < numColumns(); j++) {
                    comp = v.getPrimitiveElement(j);
                    arrayRe[i] += (matrixRe[i][j] * comp.real() - matrixIm[i][j] * comp.imag());
                    arrayIm[i] += (matrixIm[i][j] * comp.real() + matrixRe[i][j] * comp.imag());
                }
            }
            return new ComplexVector(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Matrix and vector are incompatible.");
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a complex square matrix
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public AbstractComplexSquareMatrix multiply(final AbstractComplexSquareMatrix m) {
        if (m instanceof ComplexSquareMatrix)
            return multiply((ComplexSquareMatrix) m);

        if (numColumns() == m.numRows()) {
            final double arrayRe[][] = new double[numRows()][numColumns()];
            final double arrayIm[][] = new double[numRows()][numColumns()];
            Complex elem;
            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < numColumns(); k++) {
                    elem = m.getPrimitiveElement(0, k);
                    arrayRe[j][k] = (matrixRe[j][0] * elem.real() - matrixIm[j][0] * elem.imag());
                    arrayIm[j][k] = (matrixIm[j][0] * elem.real() + matrixRe[j][0] * elem.imag());
                    for (int n = 1; n < numColumns(); n++) {
                        elem = m.getPrimitiveElement(n, k);
                        arrayRe[j][k] += (matrixRe[j][n] * elem.real() - matrixIm[j][n] * elem.imag());
                        arrayIm[j][k] += (matrixIm[j][n] * elem.real() + matrixRe[j][n] * elem.imag());
                    }
                }
            }
            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

    public ComplexSquareMatrix multiply(final ComplexSquareMatrix m) {
        if (numColumns() == m.numRows()) {
            final double arrayRe[][] = new double[numRows()][numColumns()];
            final double arrayIm[][] = new double[numRows()][numColumns()];
            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < numColumns(); k++) {
                    arrayRe[j][k] = (matrixRe[j][0] * m.matrixRe[0][k] - matrixIm[j][0] * m.matrixIm[0][k]);
                    arrayIm[j][k] = (matrixIm[j][0] * m.matrixRe[0][k] + matrixRe[j][0] * m.matrixIm[0][k]);
                    for (int n = 1; n < numColumns(); n++) {
                        arrayRe[j][k] += (matrixRe[j][n] * m.matrixRe[n][k] - matrixIm[j][n] * m.matrixIm[n][k]);
                        arrayIm[j][k] += (matrixIm[j][n] * m.matrixRe[n][k] + matrixRe[j][n] * m.matrixIm[n][k]);
                    }
                }
            }
            return new ComplexSquareMatrix(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Incompatible matrices.");
    }

// DIRECT SUM

    /**
     * Returns the direct sum of this matrix and another.
     */
    public AbstractComplexSquareMatrix directSum(final AbstractComplexSquareMatrix m) {
        final double arrayRe[][] = new double[numRows() + m.numRows()][numColumns() + m.numColumns()];
        final double arrayIm[][] = new double[numRows() + m.numRows()][numColumns() + m.numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                arrayRe[i][j] = matrixRe[i][j];
                arrayIm[i][j] = matrixIm[i][j];
            }
        }
        for (int i = 0; i < m.numRows(); i++) {
            for (int j = 0; j < m.numColumns(); j++) {
                Complex elem = m.getPrimitiveElement(i, j);
                arrayRe[i + numRows()][j + numColumns()] = elem.real();
                arrayIm[i + numRows()][j + numColumns()] = elem.imag();
            }
        }
        return new ComplexSquareMatrix(arrayRe, arrayIm);
    }

// TENSOR PRODUCT

    /**
     * Returns the tensor product of this matrix and another.
     */
    public AbstractComplexSquareMatrix tensorProduct(final AbstractComplexSquareMatrix m) {
        final double arrayRe[][] = new double[numRows() * m.numRows()][numColumns() * m.numColumns()];
        final double arrayIm[][] = new double[numRows() * m.numRows()][numColumns() * m.numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                for (int k = 0; k < m.numRows(); j++) {
                    for (int l = 0; l < m.numColumns(); l++) {
                        Complex elem = m.getPrimitiveElement(k, l);
                        arrayRe[i * m.numRows() + k][j * m.numColumns() + l] = (matrixRe[i][j] * elem.real() - matrixIm[i][j] * elem.imag());
                        arrayIm[i * m.numRows() + k][j * m.numColumns() + l] = (matrixIm[i][j] * elem.real() + matrixRe[i][j] * elem.imag());
                    }
                }
            }
        }
        return new ComplexSquareMatrix(arrayRe, arrayIm);
    }

// HERMITIAN ADJOINT

    /**
     * Returns the hermitian adjoint of this matrix.
     *
     * @return a complex square matrix
     */
    public AbstractComplexMatrix hermitianAdjoint() {
        final double arrayRe[][] = new double[numColumns()][numRows()];
        final double arrayIm[][] = new double[numColumns()][numRows()];
        for (int j, i = 0; i < numRows(); i++) {
            arrayRe[0][i] = matrixRe[i][0];
            arrayIm[0][i] = -matrixIm[i][0];
            for (j = 1; j < numColumns(); j++) {
                arrayRe[j][i] = matrixRe[i][j];
                arrayIm[j][i] = -matrixIm[i][j];
            }
        }
        return new ComplexSquareMatrix(arrayRe, arrayIm);
    }

// CONJUGATE

    /**
     * Returns the complex conjugate of this matrix.
     *
     * @return a complex square matrix
     */
    public AbstractComplexMatrix conjugate() {
        final double arrayIm[][] = new double[numRows()][numColumns()];
        for (int j, i = 0; i < numRows(); i++) {
            arrayIm[i][0] = -matrixIm[i][0];
            for (j = 1; j < numColumns(); j++)
                arrayIm[i][j] = -matrixIm[i][j];
        }
        return new ComplexSquareMatrix(matrixRe, arrayIm);
    }

// TRANSPOSE

    /**
     * Returns the transpose of this matrix.
     *
     * @return a complex square matrix
     */
    public Matrix transpose() {
        final double arrayRe[][] = new double[numColumns()][numRows()];
        final double arrayIm[][] = new double[numColumns()][numRows()];
        for (int j, i = 0; i < numRows(); i++) {
            arrayRe[0][i] = matrixRe[i][0];
            arrayIm[0][i] = matrixIm[i][0];
            for (j = 1; j < numColumns(); j++) {
                arrayRe[j][i] = matrixRe[i][j];
                arrayIm[j][i] = matrixIm[i][j];
            }
        }
        return new ComplexSquareMatrix(arrayRe, arrayIm);
    }

// INVERSE

    /**
     * Returns the inverse of this matrix.
     *
     * @return a complex square matrix
     */
    public AbstractComplexSquareMatrix inverse() {
        final int N = numRows();
        final double arrayLRe[][] = new double[N][N];
        final double arrayLIm[][] = new double[N][N];
        final double arrayURe[][] = new double[N][N];
        final double arrayUIm[][] = new double[N][N];
        final ComplexSquareMatrix lu[] = (ComplexSquareMatrix[]) this.luDecompose(null);
        double denom;
        denom = lu[0].matrixRe[0][0] * lu[0].matrixRe[0][0] + lu[0].matrixIm[0][0] * lu[0].matrixIm[0][0];
        arrayLRe[0][0] = lu[0].matrixRe[0][0] / denom;
        arrayLIm[0][0] = -lu[0].matrixIm[0][0] / denom;
        denom = lu[1].matrixRe[0][0] * lu[1].matrixRe[0][0] + lu[1].matrixIm[0][0] * lu[1].matrixIm[0][0];
        arrayURe[0][0] = lu[1].matrixRe[0][0] / denom;
        arrayUIm[0][0] = -lu[1].matrixIm[0][0] / denom;
        for (int i = 1; i < N; i++) {
            denom = lu[0].matrixRe[i][i] * lu[0].matrixRe[i][i] + lu[0].matrixIm[i][i] * lu[0].matrixIm[i][i];
            arrayLRe[i][i] = lu[0].matrixRe[i][i] / denom;
            arrayLIm[i][i] = -lu[0].matrixIm[i][i] / denom;
            denom = lu[1].matrixRe[i][i] * lu[1].matrixRe[i][i] + lu[1].matrixIm[i][i] * lu[1].matrixIm[i][i];
            arrayURe[i][i] = lu[1].matrixRe[i][i] / denom;
            arrayUIm[i][i] = -lu[1].matrixIm[i][i] / denom;
        }
        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                double tmpLRe = 0.0, tmpLIm = 0.0;
                double tmpURe = 0.0, tmpUIm = 0.0;
                for (int k = i; k < j; k++) {
                    tmpLRe -= (lu[0].matrixRe[j][k] * arrayLRe[k][i] - lu[0].matrixIm[j][k] * arrayLIm[k][i]);
                    tmpLIm -= (lu[0].matrixIm[j][k] * arrayLRe[k][i] + lu[0].matrixRe[j][k] * arrayLIm[k][i]);
                    tmpURe -= (arrayURe[i][k] * lu[1].matrixRe[k][j] - arrayUIm[i][k] * lu[1].matrixIm[k][j]);
                    tmpUIm -= (arrayUIm[i][k] * lu[1].matrixRe[k][j] + arrayURe[i][k] * lu[1].matrixIm[k][j]);
                }
                denom = lu[0].matrixRe[j][j] * lu[0].matrixRe[j][j] + lu[0].matrixIm[j][j] * lu[0].matrixIm[j][j];
                arrayLRe[j][i] = (tmpLRe * lu[0].matrixRe[j][j] + tmpLIm * lu[0].matrixIm[j][j]) / denom;
                arrayLIm[j][i] = (tmpLIm * lu[0].matrixRe[j][j] - tmpLRe * lu[0].matrixIm[j][j]) / denom;
                denom = lu[1].matrixRe[j][j] * lu[1].matrixRe[j][j] + lu[1].matrixIm[j][j] * lu[1].matrixIm[j][j];
                arrayURe[i][j] = (tmpURe * lu[1].matrixRe[j][j] + tmpUIm * lu[1].matrixIm[j][j]) / denom;
                arrayUIm[i][j] = (tmpUIm * lu[1].matrixRe[j][j] - tmpURe * lu[1].matrixIm[j][j]) / denom;
            }
        }
        // matrix multiply arrayU x arrayL
        final double invRe[][] = new double[N][N];
        final double invIm[][] = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                for (int k = i; k < N; k++) {
                    invRe[i][LUpivot[j]] += (arrayURe[i][k] * arrayLRe[k][j] - arrayUIm[i][k] * arrayLIm[k][j]);
                    invIm[i][LUpivot[j]] += (arrayUIm[i][k] * arrayLRe[k][j] + arrayURe[i][k] * arrayLIm[k][j]);
                }
            }
            for (int j = i; j < N; j++) {
                for (int k = j; k < N; k++) {
                    invRe[i][LUpivot[j]] += (arrayURe[i][k] * arrayLRe[k][j] - arrayUIm[i][k] * arrayLIm[k][j]);
                    invIm[i][LUpivot[j]] += (arrayUIm[i][k] * arrayLRe[k][j] + arrayURe[i][k] * arrayLIm[k][j]);
                }
            }
        }
        return new ComplexSquareMatrix(invRe, invIm);
    }

// LU DECOMPOSITION

    /**
     * Returns the LU decomposition of this matrix.
     *
     * @return an array with [0] containing the L-matrix and [1] containing the U-matrix.
     */
    public final AbstractComplexSquareMatrix[] luDecompose(int pivot[]) {
        if (LU != null) {
            if (pivot != null)
                System.arraycopy(LUpivot, 0, pivot, 0, pivot.length);
            return LU;
        }
        final int N = numRows();
        final double arrayLRe[][] = new double[N][N];
        final double arrayLIm[][] = new double[N][N];
        final double arrayURe[][] = new double[N][N];
        final double arrayUIm[][] = new double[N][N];
        final double buf[] = new double[N];
        if (pivot == null)
            pivot = new int[N + 1];
        for (int i = 0; i < N; i++)
            pivot[i] = i;
        pivot[N] = 1;
        // LU decomposition to arrayU
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < j; i++) {
                double tmpRe = matrixRe[pivot[i]][j];
                double tmpIm = matrixIm[pivot[i]][j];
                for (int k = 0; k < i; k++) {
                    tmpRe -= (arrayURe[i][k] * arrayURe[k][j] - arrayUIm[i][k] * arrayUIm[k][j]);
                    tmpIm -= (arrayUIm[i][k] * arrayURe[k][j] + arrayURe[i][k] * arrayUIm[k][j]);
                }
                arrayURe[i][j] = tmpRe;
                arrayUIm[i][j] = tmpIm;
            }
            double max = 0.0;
            int pivotrow = j;
            for (int i = j; i < N; i++) {
                double tmpRe = matrixRe[pivot[i]][j];
                double tmpIm = matrixIm[pivot[i]][j];
                for (int k = 0; k < j; k++) {
                    tmpRe -= (arrayURe[i][k] * arrayURe[k][j] - arrayUIm[i][k] * arrayUIm[k][j]);
                    tmpIm -= (arrayUIm[i][k] * arrayURe[k][j] + arrayURe[i][k] * arrayUIm[k][j]);
                }
                arrayURe[i][j] = tmpRe;
                arrayUIm[i][j] = tmpIm;
                // while we're here search for a pivot for arrayU[j][j]
                double tmp = tmpRe * tmpRe + tmpIm * tmpIm;
                if (tmp > max) {
                    max = tmp;
                    pivotrow = i;
                }
            }
            // swap row j with pivotrow
            if (pivotrow != j) {
                System.arraycopy(arrayURe[j], 0, buf, 0, j + 1);
                System.arraycopy(arrayURe[pivotrow], 0, arrayURe[j], 0, j + 1);
                System.arraycopy(buf, 0, arrayURe[pivotrow], 0, j + 1);
                System.arraycopy(arrayUIm[j], 0, buf, 0, j + 1);
                System.arraycopy(arrayUIm[pivotrow], 0, arrayUIm[j], 0, j + 1);
                System.arraycopy(buf, 0, arrayUIm[pivotrow], 0, j + 1);
                int k = pivot[j];
                pivot[j] = pivot[pivotrow];
                pivot[pivotrow] = k;
                // update parity
                pivot[N] = -pivot[N];
            }
            // divide by pivot
            double tmpRe = arrayURe[j][j];
            double tmpIm = arrayUIm[j][j];
            if (Math.abs(tmpRe) < Math.abs(tmpIm)) {
                double a = tmpRe / tmpIm;
                double denom = tmpRe * a + tmpIm;
                for (int i = j + 1; i < N; i++) {
                    double tmp = (arrayURe[i][j] * a + arrayUIm[i][j]) / denom;
                    arrayUIm[i][j] = (arrayUIm[i][j] * a - arrayURe[i][j]) / denom;
                    arrayURe[i][j] = tmp;
                }
            } else {
                double a = tmpIm / tmpRe;
                double denom = tmpRe + tmpIm * a;
                for (int i = j + 1; i < N; i++) {
                    double tmp = (arrayURe[i][j] + arrayUIm[i][j] * a) / denom;
                    arrayUIm[i][j] = (arrayUIm[i][j] - arrayURe[i][j] * a) / denom;
                    arrayURe[i][j] = tmp;
                }
            }
        }
        // move lower triangular part to arrayL
        for (int j = 0; j < N; j++) {
            arrayLRe[j][j] = 1.0;
            for (int i = j + 1; i < N; i++) {
                arrayLRe[i][j] = arrayURe[i][j];
                arrayLIm[i][j] = arrayUIm[i][j];
                arrayURe[i][j] = 0.0;
                arrayUIm[i][j] = 0.0;
            }
        }
        LU = new ComplexSquareMatrix[2];
        LU[0] = new ComplexSquareMatrix(arrayLRe, arrayLIm);
        LU[1] = new ComplexSquareMatrix(arrayURe, arrayUIm);
        LUpivot = new int[pivot.length];
        System.arraycopy(pivot, 0, LUpivot, 0, pivot.length);
        return LU;
    }

// POLAR DECOMPOSITION

    /**
     * Returns the polar decomposition of this matrix.
     */
    public AbstractComplexSquareMatrix[] polarDecompose() {
        final int N = numRows();
        final AbstractComplexVector evec[] = new AbstractComplexVector[N];
        double eval[];
        try {
            eval = LinearMathUtils.eigenSolveHermitian(this, evec);
        } catch (MaximumIterationsExceededException e) {
            return null;
        }
        final double tmpaRe[][] = new double[N][N];
        final double tmpaIm[][] = new double[N][N];
        final double tmpmRe[][] = new double[N][N];
        final double tmpmIm[][] = new double[N][N];
        double abs;
        Complex comp;
        for (int i = 0; i < N; i++) {
            abs = Math.abs(eval[i]);
            comp = evec[i].getPrimitiveElement(0).conjugate();
            tmpaRe[i][0] = eval[i] * comp.real() / abs;
            tmpaIm[i][0] = eval[i] * comp.imag() / abs;
            tmpmRe[i][0] = abs * comp.real();
            tmpmIm[i][0] = abs * comp.imag();
            for (int j = 1; j < N; j++) {
                comp = evec[i].getPrimitiveElement(j).conjugate();
                tmpaRe[i][j] = eval[i] * comp.real() / abs;
                tmpaIm[i][j] = eval[i] * comp.imag() / abs;
                tmpmRe[i][j] = abs * comp.real();
                tmpmIm[i][j] = abs * comp.imag();
            }
        }
        final double argRe[][] = new double[N][N];
        final double argIm[][] = new double[N][N];
        final double modRe[][] = new double[N][N];
        final double modIm[][] = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                comp = evec[0].getPrimitiveElement(i);
                argRe[i][j] = (tmpaRe[0][j] * comp.real() - tmpaIm[0][j] * comp.imag());
                argIm[i][j] = (tmpaIm[0][j] * comp.real() + tmpaRe[0][j] * comp.imag());
                modRe[i][j] = (tmpmRe[0][j] * comp.real() - tmpmIm[0][j] * comp.imag());
                modIm[i][j] = (tmpmIm[0][j] * comp.real() + tmpmRe[0][j] * comp.imag());
                for (int k = 1; k < N; k++) {
                    comp = evec[k].getPrimitiveElement(i);
                    argRe[i][j] += (tmpaRe[k][j] * comp.real() - tmpaIm[k][j] * comp.imag());
                    argIm[i][j] += (tmpaIm[k][j] * comp.real() + tmpaRe[k][j] * comp.imag());
                    modRe[i][j] += (tmpmRe[k][j] * comp.real() - tmpmIm[k][j] * comp.imag());
                    modIm[i][j] += (tmpmIm[k][j] * comp.real() + tmpmRe[k][j] * comp.imag());
                }
            }
        }
        final ComplexSquareMatrix us[] = new ComplexSquareMatrix[2];
        us[0] = new ComplexSquareMatrix(argRe, argIm);
        us[1] = new ComplexSquareMatrix(modRe, modIm);
        return us;
    }

// MAP ELEMENTS

    /**
     * Applies a function on all the matrix elements.
     *
     * @param f a user-defined function
     * @return a complex square matrix
     */
    public AbstractComplexMatrix mapElements(final ComplexMapping f) {
        final Complex array[][] = new Complex[numRows()][numColumns()];
        for (int j, i = 0; i < numRows(); i++) {
            array[i][0] = f.map(new Complex(matrixRe[i][0], matrixIm[i][0]));
            for (j = 1; j < numColumns(); j++)
                array[i][j] = f.map(new Complex(matrixRe[i][j], matrixIm[i][j]));
        }
        return new ComplexSquareMatrix(array);
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new ComplexSquareMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an double array.
     */
    public Complex[][] toPrimitiveArray() {
        final Complex[][] result = new Complex[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++)
            for (int j = 0; j < numColumns(); j++)
                result[i][j] = new Complex(matrixRe[i][j], matrixIm[i][j]);
        return result;
    }

}
