package org.jscience.mathematics.algebraic.matrices;

import org.jscience.JScience;
import org.jscience.mathematics.algebraic.AbstractMatrix;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.algebraic.numbers.Integer;
import org.jscience.mathematics.analysis.ComplexMapping;
import org.jscience.util.IllegalDimensionException;

import java.io.BufferedReader;
import java.io.StreamTokenizer;

/**
 * The AbstractComplexMatrix class provides an object for encapsulating matrices containing complex numbers.
 *
 * @author Mark Hale
 * @version 2.2
 */

//hashcode, norm, norm, normalize, infnorm, trace, hermitianAdjoint, operatorNorm new dimension checking

//Jama provides some more methods from which we could get some inspiration: see AbstractDoubleMatrix

public abstract class AbstractComplexMatrix extends AbstractMatrix {
    /**
     * Constructs a matrix.
     */
    protected AbstractComplexMatrix(final int rows, final int cols) {
        super(rows, cols);
    }

    /**
     * Compares two complex matrices for equality.
     *
     * @param obj a complex matrix
     */
    public boolean equals(Object obj) {
        if (obj instanceof AbstractComplexMatrix) {
            return equals((AbstractComplexMatrix) obj);
        } else {
            return false;
        }
    }

    /**
     * Compares two complex matrices for equality.
     * Two matrices are considered to be equal if the Frobenius norm of their difference is within the zero tolerance.
     *
     * @param m a complex matrix
     */
    public boolean equals(AbstractComplexMatrix m) {
        return equals(m, java.lang.Double.valueOf(JScience.getProperty("tolerance")).doubleValue());
    }

    public boolean equals(AbstractComplexMatrix m, double tol) {
        if (m != null && numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double sumSqr = 0.0;
            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++) {
                    double deltaRe = getRealElement(i, j) - m.getRealElement(i, j);
                    double deltaIm = getImagElement(i, j) - m.getImagElement(i, j);
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
     * Returns the real part of this complex matrix.
     *
     * @return a double matrix
     */
    public AbstractDoubleMatrix real() {
        final double ans[][] = new double[numRows()][numColumns()];
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
        final double ans[][] = new double[numRows()][numColumns()];
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
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public abstract Complex getPrimitiveElement(int i, int j);

    /**
     * Returns an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public Complex getElement(final int i, final int j) {
        return getPrimitiveElement(i, j);
    }

    /**
     * Returns the real part of an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public double getRealElement(final int i, final int j) {
        return getPrimitiveElement(i, j).real();
    }

    /**
     * Returns the imag part of an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public double getImagElement(final int i, final int j) {
        return getPrimitiveElement(i, j).imag();
    }

    /**
     * Returns the ith row.
     */
    public AbstractComplexVector getRow(final int i) {

        double[] elementsR;
        double[] elementsI;

        if ((i >= 0) && (i < numRows())) {
            elementsR = new double[numColumns()];
            elementsI = new double[numColumns()];
            for (int j = 0; j < numColumns(); j++) {
                elementsR[j] = getPrimitiveElement(i, j).real();
                elementsI[j] = getPrimitiveElement(i, j).imag();
            }
            return new ComplexVector(elementsR, elementsI);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Returns the ith column.
     */
    public AbstractComplexVector getColumn(final int j) {

        double[] elementsR;
        double[] elementsI;

        if ((j >= 0) && (j < numColumns())) {
            elementsR = new double[numRows()];
            elementsI = new double[numRows()];
            for (int i = 0; i < numRows(); i++) {
                elementsR[i] = getPrimitiveElement(i, j).real();
                elementsI[i] = getPrimitiveElement(i, j).imag();
            }
            return new ComplexVector(elementsR, elementsI);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Returns the ith row.
     */
    public void setRow(final int i, final AbstractComplexVector v) {

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
    public void setColumn(final int j, final AbstractComplexVector v) {

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
     * @param z a complex number
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public abstract void setElement(final int i, final int j, final Complex z);

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
        setElement(i, j, new Complex(x, y));
    }

    /**
     * Sets the value of all elements of the matrix.
     *
     * @param m a complex element
     */
    public void setAllElements(final Complex m) {
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                setElement(i, j, m);
            }
        }
    }

    public final Object getSet() {
        return ComplexMatrixAlgebra.get(numRows(), numColumns());
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
                    tmpResult += getPrimitiveElement(i, j).norm();
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
            for (int i = 0; i < numRows(); i++) {
                for (int j = 0; j < numColumns(); j++)
                    result += getRealElement(i, j) * getRealElement(i, j) + getImagElement(i, j) * getImagElement(i, j);
            }
            return Math.sqrt(result);
        } else
            throw new ArithmeticException("The frobenius norm of a zero dimension matrix is undefined.");
    }

    /**
     * Applies the abs function on all the matrix components.
     */
    public AbstractDoubleMatrix abs() {
        final double array[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                array[i][j] = getPrimitiveElement(i, j).abs();
            }
        }
        return new DoubleMatrix(array);
    }

    /**
     * Gets the mass of the matrix components.
     *
     * @return the mass.
     */
    public Complex mass() {
        Complex result;
        if ((numRows() > 0) || (numColumns() > 0)) {
            result = Complex.ZERO;
            for (int i = 0; i < numRows(); i++)
                for (int j = 0; j < numColumns(); j++)
                    result = result.add(getPrimitiveElement(i, j));
            return result;
        } else
            throw new ArithmeticException("The mass of a zero dimension matrix is undefined.");
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
        for (int i = 0; i < numRows(); i++) {
            arrayRe[i][0] = -getRealElement(i, 0);
            arrayIm[i][0] = -getImagElement(i, 0);
            for (int j = 1; j < numColumns(); j++) {
                arrayRe[i][j] = -getRealElement(i, j);
                arrayIm[i][j] = -getImagElement(i, j);
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }

// ADDITION

    /**
     * Returns the addition of this matrix and another.
     */
    public final AbelianGroup.Member add(final AbelianGroup.Member m) {
        if (m instanceof AbstractComplexMatrix)
            return add((AbstractComplexMatrix) m);
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a complex matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexMatrix add(final AbstractComplexMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double arrayRe[][] = new double[numRows()][numColumns()];
            final double arrayIm[][] = new double[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                arrayRe[i][0] = getRealElement(i, 0) + m.getRealElement(i, 0);
                arrayIm[i][0] = getImagElement(i, 0) + m.getImagElement(i, 0);
                for (int j = 1; j < numColumns(); j++) {
                    arrayRe[i][j] = getRealElement(i, j) + m.getRealElement(i, j);
                    arrayIm[i][j] = getImagElement(i, j) + m.getImagElement(i, j);
                }
            }
            return new ComplexMatrix(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this matrix by another.
     */
    public final AbelianGroup.Member subtract(final AbelianGroup.Member m) {
        if (m instanceof AbstractComplexMatrix)
            return subtract((AbstractComplexMatrix) m);
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the subtraction of this matrix by another.
     *
     * @param m a complex matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public AbstractComplexMatrix subtract(final AbstractComplexMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            final double arrayRe[][] = new double[numRows()][numColumns()];
            final double arrayIm[][] = new double[numRows()][numColumns()];
            for (int i = 0; i < numRows(); i++) {
                arrayRe[i][0] = getRealElement(i, 0) - m.getRealElement(i, 0);
                arrayIm[i][0] = getImagElement(i, 0) - m.getImagElement(i, 0);
                for (int j = 1; j < numColumns(); j++) {
                    arrayRe[i][j] = getRealElement(i, j) - m.getRealElement(i, j);
                    arrayIm[i][j] = getImagElement(i, j) - m.getImagElement(i, j);
                }
            }
            return new ComplexMatrix(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Matrices are different sizes.");
    }

// SCALAR MULTIPLICATION

    /**
     * Returns the multiplication of this matrix by a scalar.
     */
    public final Module.Member scalarMultiply(Ring.Member x) {
        if (x instanceof Complex)
            return scalarMultiply((Complex) x);
        else if (x instanceof Double)
            return scalarMultiply(((Double) x).value());
        else if (x instanceof Integer)
            return scalarMultiply(((Integer) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param z a complex number
     * @return a complex matrix
     */
    public AbstractComplexMatrix scalarMultiply(final Complex z) {
        final double real = z.real();
        final double imag = z.imag();
        final double arrayRe[][] = new double[numRows()][numColumns()];
        final double arrayIm[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            arrayRe[i][0] = real * getRealElement(i, 0) - imag * getImagElement(i, 0);
            arrayIm[i][0] = imag * getRealElement(i, 0) + real * getImagElement(i, 0);
            for (int j = 1; j < numColumns(); j++) {
                arrayRe[i][j] = real * getRealElement(i, j) - imag * getImagElement(i, j);
                arrayIm[i][j] = imag * getRealElement(i, j) + real * getImagElement(i, j);
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }

    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param x a double
     * @return a complex matrix
     */
    public AbstractComplexMatrix scalarMultiply(final double x) {
        final double arrayRe[][] = new double[numRows()][numColumns()];
        final double arrayIm[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            arrayRe[i][0] = x * getRealElement(i, 0);
            arrayIm[i][0] = x * getImagElement(i, 0);
            for (int j = 1; j < numColumns(); j++) {
                arrayRe[i][j] = x * getRealElement(i, j);
                arrayIm[i][j] = x * getImagElement(i, j);
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }

// SCALAR DIVISON

    /**
     * Returns the division of this matrix by a scalar.
     */
    public final VectorSpace.Member scalarDivide(Field.Member x) {
        if (x instanceof Complex)
            return scalarDivide((Complex) x);
        if (x instanceof Double)
            return scalarDivide(((Double) x).value());
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the division of this matrix by a scalar.
     *
     * @param z a complex number
     * @return a complex matrix
     */
    public AbstractComplexMatrix scalarDivide(final Complex z) {
        final Complex array[][] = new Complex[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = getPrimitiveElement(i, 0).divide(z);
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = getPrimitiveElement(i, j).divide(z);
        }
        return new ComplexMatrix(array);
    }

    /**
     * Returns the division of this matrix by a scalar.
     *
     * @param x a double
     * @return a complex matrix
     */
    public AbstractComplexMatrix scalarDivide(final double x) {
        final double arrayRe[][] = new double[numRows()][numColumns()];
        final double arrayIm[][] = new double[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            arrayRe[i][0] = getRealElement(i, 0) / x;
            arrayIm[i][0] = getImagElement(i, 0) / x;
            for (int j = 1; j < numColumns(); j++) {
                arrayRe[i][j] = getRealElement(i, j) / x;
                arrayIm[i][j] = getImagElement(i, j) / x;
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }

// SCALAR PRODUCT

    /**
     * Returns the scalar product of this matrix and another.
     *
     * @param m a complex matrix.
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public Complex scalarProduct(final AbstractComplexMatrix m) {
        if (numRows() == m.numRows() && numColumns() == m.numColumns()) {
            double real = 0.0, imag = 0.0;
            for (int i = 0; i < numRows(); i++) {
                real += getRealElement(i, 0) * m.getRealElement(i, 0) + getImagElement(i, 0) * m.getImagElement(i, 0);
                imag += getImagElement(i, 0) * m.getRealElement(i, 0) - getRealElement(i, 0) * m.getImagElement(i, 0);
                for (int j = 1; j < numColumns(); j++) {
                    real += getRealElement(i, j) * m.getRealElement(i, j) + getImagElement(i, j) * m.getImagElement(i, j);
                    imag += getImagElement(i, j) * m.getRealElement(i, j) - getRealElement(i, j) * m.getImagElement(i, j);
                }
            }
            return new Complex(real, imag);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
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
            Complex tmp;
            for (int i = 0; i < numRows(); i++) {
                tmp = getPrimitiveElement(i, 0).multiply(v.getPrimitiveElement(0));
                arrayRe[i] = tmp.real();
                arrayIm[i] = tmp.imag();
                for (int j = 1; j < numColumns(); j++) {
                    tmp = getPrimitiveElement(i, j).multiply(v.getPrimitiveElement(j));
                    arrayRe[i] += tmp.real();
                    arrayIm[i] += tmp.imag();
                }
            }
            return new ComplexVector(arrayRe, arrayIm);
        } else
            throw new IllegalDimensionException("Matrix and vector are incompatible.");
    }

    /**
     * Returns the multiplication of this matrix and another.
     */
    public final Ring.Member multiply(final Ring.Member m) {
        if (m instanceof AbstractComplexMatrix)
            return multiply((AbstractComplexMatrix) m);
        else
            throw new IllegalArgumentException("Matrix class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a complex matrix
     * @return an AbstractComplexMatrix or an AbstractComplexSquareMatrix as appropriate
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public AbstractComplexMatrix multiply(final AbstractComplexMatrix m) {
        if (numColumns() == m.numRows()) {
            final double arrayRe[][] = new double[numRows()][m.numColumns()];
            final double arrayIm[][] = new double[numRows()][m.numColumns()];
            Complex tmp;
            for (int j = 0; j < numRows(); j++) {
                for (int k = 0; k < m.numColumns(); k++) {
                    tmp = getPrimitiveElement(j, 0).multiply(m.getPrimitiveElement(0, k));
                    arrayRe[j][k] = tmp.real();
                    arrayIm[j][k] = tmp.imag();
                    for (int n = 1; n < numColumns(); n++) {
                        tmp = getPrimitiveElement(j, n).multiply(m.getPrimitiveElement(n, k));
                        arrayRe[j][k] += tmp.real();
                        arrayIm[j][k] += tmp.imag();
                    }
                }
            }
            if (numRows() == m.numColumns())
                return new ComplexSquareMatrix(arrayRe, arrayIm);
            else
                return new ComplexMatrix(arrayRe, arrayIm);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

// DIRECT SUM

    /**
     * Returns the direct sum of this matrix and another.
     */
    public AbstractComplexMatrix directSum(final AbstractComplexMatrix m) {
        final double arrayRe[][] = new double[numRows() + m.numRows()][numColumns() + m.numColumns()];
        final double arrayIm[][] = new double[numRows() + m.numRows()][numColumns() + m.numColumns()];
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                arrayRe[i][j] = getRealElement(i, j);
                arrayIm[i][j] = getImagElement(i, j);
            }
        }
        for (int j, i = 0; i < m.numRows(); i++) {
            for (j = 0; j < m.numColumns(); j++) {
                arrayRe[i + numRows()][j + numColumns()] = m.getRealElement(i, j);
                arrayIm[i + numRows()][j + numColumns()] = m.getImagElement(i, j);
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }

// TENSOR PRODUCT

    /**
     * Returns the tensor product of this matrix and another.
     */
    public AbstractComplexMatrix tensorProduct(final AbstractComplexMatrix m) {
        final double arrayRe[][] = new double[numRows() * m.numRows()][numColumns() * m.numColumns()];
        final double arrayIm[][] = new double[numRows() * m.numRows()][numColumns() * m.numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                for (int k = 0; k < m.numRows(); j++) {
                    for (int l = 0; l < m.numColumns(); l++) {
                        Complex tmp = getPrimitiveElement(i, j).multiply(m.getPrimitiveElement(k, l));
                        arrayRe[i * m.numRows() + k][j * m.numColumns() + l] = tmp.real();
                        arrayIm[i * m.numRows() + k][j * m.numColumns() + l] = tmp.imag();
                    }
                }
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }

// HERMITIAN ADJOINT

    /**
     * Returns the hermitian adjoint of this matrix.
     *
     * @return a complex matrix
     */
    public AbstractComplexMatrix hermitianAdjoint() {
        final double arrayRe[][] = new double[numColumns()][numRows()];
        final double arrayIm[][] = new double[numColumns()][numRows()];
        for (int i = 0; i < numRows(); i++) {
            arrayRe[0][i] = getRealElement(i, 0);
            arrayIm[0][i] = -getImagElement(i, 0);
            for (int j = 1; j < numColumns(); j++) {
                arrayRe[j][i] = getRealElement(i, j);
                arrayIm[j][i] = -getImagElement(i, j);
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }

// CONJUGATE

    /**
     * Returns the complex conjugate of this matrix.
     *
     * @return a complex matrix
     */
    public AbstractComplexMatrix conjugate() {
        final double arrayRe[][] = new double[numColumns()][numRows()];
        final double arrayIm[][] = new double[numColumns()][numRows()];
        for (int i = 0; i < numRows(); i++) {
            arrayRe[i][0] = getRealElement(i, 0);
            arrayIm[i][0] = -getImagElement(i, 0);
            for (int j = 1; j < numColumns(); j++) {
                arrayRe[i][j] = getRealElement(i, j);
                arrayIm[i][j] = -getImagElement(i, j);
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }

// TRANSPOSE

    /**
     * Returns the transpose of this matrix.
     *
     * @return a complex matrix
     */
    public Matrix transpose() {
        final double arrayRe[][] = new double[numColumns()][numRows()];
        final double arrayIm[][] = new double[numColumns()][numRows()];
        for (int i = 0; i < numRows(); i++) {
            arrayRe[0][i] = getRealElement(i, 0);
            arrayIm[0][i] = getImagElement(i, 0);
            for (int j = 1; j < numColumns(); j++) {
                arrayRe[j][i] = getRealElement(i, j);
                arrayIm[j][i] = getImagElement(i, j);
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }

    /**
     * Invert matrix elements order from the top to the bottom.
     */
    public AbstractComplexMatrix horizontalAxisSymmetry() {
        final Complex array[][] = new Complex[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                array[i][j] = getPrimitiveElement(numRows() - i, j);
            }
        }
        return new ComplexMatrix(array);
    }

    /**
     * Invert matrix elements order from the right to the left.
     */
    public AbstractComplexMatrix verticalAxisSymmetry() {
        final Complex array[][] = new Complex[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                array[i][j] = getPrimitiveElement(i, numColumns() - j);
            }
        }
        return new ComplexMatrix(array);
    }

    /**
     * Invert matrix elements order from the top to the bottom, from the right to the left.
     */
    public AbstractComplexMatrix reverse() {
        final Complex array[][] = new Complex[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                array[i][j] = getPrimitiveElement(numRows() - i, numColumns() - j);
            }
        }
        return new ComplexMatrix(array);
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
    public AbstractComplexMatrix getSubMatrix(final int k1, final int k2, final int k3, final int k4) {
        final Complex[][] ans = new Complex[Math.abs(k2 - k1) + 1][Math.abs(k4 - k3) + 1];
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
        return new ComplexMatrix(ans);
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
    public AbstractComplexMatrix setSubMatrix(final int k, final int l, final AbstractComplexMatrix m) {
        if (k < 0) {
            if (l < 0) {
                Complex[][] ans = new Complex[Math.max(numRows() - k, m.numRows())][Math.max(numColumns() - l, m.numColumns())];
                for (int i = 0; i < numRows(); i++)
                    for (int j = 0; j < numRows(); j++)
                        ans[i - k][j - l] = getPrimitiveElement(i, j);
                for (int i = 0; i < m.numRows(); i++)
                    for (int j = 0; j < m.numRows(); j++)
                        ans[i][j] = m.getPrimitiveElement(i, j);
                return new ComplexMatrix(ans);
            } else {
                Complex[][] ans = new Complex[Math.max(numRows() - k, m.numRows())][Math.max(numColumns(), m.numColumns() + l)];
                for (int i = 0; i < numRows(); i++)
                    for (int j = 0; j < numRows(); j++)
                        ans[i - k][j] = getPrimitiveElement(i, j);
                for (int i = 0; i < m.numRows(); i++)
                    for (int j = 0; j < m.numRows(); j++)
                        ans[i][j + l] = m.getPrimitiveElement(i, j);
                return new ComplexMatrix(ans);
            }
        } else {
            if (l < 0) {
                Complex[][] ans = new Complex[Math.max(numRows(), m.numRows() + k)][Math.max(numColumns() - l, m.numColumns())];
                for (int i = 0; i < numRows(); i++)
                    for (int j = 0; j < numRows(); j++)
                        ans[i][j - l] = getPrimitiveElement(i, j);
                for (int i = 0; i < m.numRows(); i++)
                    for (int j = 0; j < m.numRows(); j++)
                        ans[i + k][j] = m.getPrimitiveElement(i, j);
                return new ComplexMatrix(ans);
            } else {
                Complex[][] ans = new Complex[Math.max(numRows(), m.numRows() + k)][Math.max(numColumns(), m.numColumns() + l)];
                for (int i = 0; i < numRows(); i++)
                    for (int j = 0; j < numRows(); j++)
                        ans[i][j] = getPrimitiveElement(i, j);
                for (int i = 0; i < m.numRows(); i++)
                    for (int j = 0; j < m.numRows(); j++)
                        ans[i + k][j + l] = m.getPrimitiveElement(i, j);
                return new ComplexMatrix(ans);
            }
        }
    }

// MAP ELEMENTS

    /**
     * Applies a function on all the matrix elements.
     *
     * @param f a user-defined function
     * @return a complex matrix
     */
    public AbstractComplexMatrix mapElements(final ComplexMapping f) {
        final Complex array[][] = new Complex[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            array[i][0] = f.map(getPrimitiveElement(i, 0));
            for (int j = 1; j < numColumns(); j++)
                array[i][j] = f.map(getPrimitiveElement(i, j));
        }
        return new ComplexMatrix(array);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an Complex array.
     */
    public Complex[][] toPrimitiveArray() {
        final Complex[][] result = new Complex[numRows()][numColumns()];
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
    public Complex[] getMatrixAsRows() {
        final Complex[] result = new Complex[numRows() * numColumns()];
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
    public Complex[] getMatrixAsColumns() {
        final Complex[] result = new Complex[numRows() * numColumns()];
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
    public static AbstractComplexMatrix read(BufferedReader input) throws java.io.IOException {
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
            v.addElement(new Complex(tokenizer.sval)); // Read & store 1st row.
        } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);

        int n = v.size();  // Now we've got the number of columns!
        Complex row[] = new Complex[n];
        for (int j = 0; j < n; j++)  // extract the elements of the 1st row.
            row[j] = (Complex) v.elementAt(j);
        v.removeAllElements();
        v.addElement(row);  // Start storing rows instead of columns.
        while (tokenizer.nextToken() == StreamTokenizer.TT_WORD) {
            // While non-empty lines
            v.addElement(row = new Complex[n]);
            int j = 0;
            do {
                if (j >= n)
                    throw new java.io.IOException
                            ("Row " + v.size() + " is too long.");
                row[j++] = new Complex(tokenizer.sval);
            } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
            if (j < n)
                throw new java.io.IOException
                        ("Row " + v.size() + " is too short.");
        }
        int m = v.size();  // Now we've got the number of rows.
        Complex[][] A = new Complex[m][];
        v.copyInto(A);  // copy the rows out of the vector
        return new ComplexMatrix(A);
    }

}

