package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.algebraic.AbstractMatrix;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.analysis.NumberMapping;

import org.jscience.util.IllegalDimensionException;

import java.io.Serializable;


/**
 * The RingMatrix class provides an object for encapsulating matrices over
 * an arbitrary ring.
 *
 * @author Mark Hale
 * @version 1.0
 */

//although it would be nice if RingMatrix was the superclass of all other matrixes,
//we decided that it wouldn't be so as it adds complexity to the design and possible performance penality
//yet it would be quite simple to change with current implementation as children class override all the methods of RingMatrix.

//possible methods we could add:
//hashCode
public class RingMatrix extends AbstractMatrix implements Cloneable,
    Serializable {
    /** Array containing the elements of the matrix. */
    private Ring.Member[][] matrix;

/**
     * Constructs an empty matrix.
     *
     * @param numRows the number of rows
     * @param numCols the number of columns
     */
    public RingMatrix(final int numRows, final int numCols) {
        super(numRows, numCols);
        matrix = new Ring.Member[numRows][numCols];
    }

/**
     * Constructs a matrix by wrapping an array.
     *
     * @param array an assigned value
     */
    public RingMatrix(final Ring.Member[][] array) {
        super(array.length, array[0].length);
        matrix = array;
    }

/**
     * Copy constructor.
     *
     * @param mat an assigned value
     */
    public RingMatrix(final RingMatrix mat) {
        this(mat.numRows(), mat.numColumns());

        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                setElement(i, j, getPrimitiveElement(i, j));
            }
        }
    }

    /**
     * Compares two matrices for equality.
     *
     * @param m a matrix
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object m) {
        if ((m != null) && (m instanceof RingMatrix) &&
                (numRows() == ((RingMatrix) m).numRows()) &&
                (numColumns() == ((RingMatrix) m).numColumns())) {
            final RingMatrix rm = (RingMatrix) m;

            for (int j, i = 0; i < numRows(); i++) {
                for (j = 0; j < numColumns(); j++) {
                    if (!matrix[i][j].equals(rm.getPrimitiveElement(i, j))) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a hashcode for this NON EMPTY matrix.
     *
     * @return DOCUMENT ME!
     */

    //public int hashCode() {
    //  return (int) Math.exp(infNorm());
    //}
    /**
     * Returns a string representing this matrix.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(5 * numRows() * numColumns());

        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                buf.append(matrix[i][j].toString());
                buf.append(' ');
            }

            buf.append('\n');
        }

        return buf.toString();
    }

    /**
     * Returns a projection of this matrix.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member[][] getElements() {
        return matrix;
    }

    /**
     * Returns a flat projection of this matrix, given "row first".
     * Useful to iterate over all the matrix elements.
     *
     * @return DOCUMENT ME!
     */
    public Object toArray() {
        Ring.Member[] result;

        result = new Ring.Member[numRows() * numColumns()];

        for (int i = 0; i < numColumns(); i++) {
            System.arraycopy(matrix[i], 0, result, numRows() * i, numRows());
        }

        return result;
    }

    /**
     * Returns an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     *
     * @return DOCUMENT ME!
     */
    public Number getElement(final int i, final int j) {
        return (Number) getPrimitiveElement(i, j);
    }

    /**
     * Returns an element of the matrix (fastest method).
     *
     * @param i row index of the element
     * @param j column index of the element
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         element.
     */
    public Ring.Member getPrimitiveElement(final int i, final int j) {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns())) {
            return matrix[i][j];
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        }
    }

    /**
     * Returns the ith row.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public RingVector getRow(final int i) {
        if ((i >= 0) && (i < numRows())) {
            Ring.Member[] array = new Ring.Member[numColumns()];

            for (int j = 0; j < numColumns(); j++) {
                array[i] = matrix[i][j];
            }

            return new RingVector(array);
        } else {
            throw new IllegalDimensionException(
                "Requested element out of bounds.");
        }
    }

    /**
     * Returns the ith column.
     *
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public RingVector getColumn(final int j) {
        if ((j >= 0) && (j < numColumns())) {
            Ring.Member[] array = new Ring.Member[numRows()];

            for (int i = 0; i < numRows(); i++) {
                array[i] = matrix[i][j];
            }

            return new RingVector(array);
        } else {
            throw new IllegalDimensionException(
                "Requested element out of bounds.");
        }
    }

    /**
     * Sets the ith row.
     *
     * @param i DOCUMENT ME!
     * @param v DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public void setRow(final int i, final RingVector v) {
        if ((i >= 0) && (i < numRows()) && (v.getDimension() == numColumns())) {
            for (int j = 0; j < numColumns(); j++) {
                matrix[i][j] = v.vector[j];
            }
        } else {
            throw new IllegalDimensionException(
                "Requested element out of bounds.");
        }
    }

    /**
     * Sets the ith column.
     *
     * @param j DOCUMENT ME!
     * @param v DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public void setColumn(final int j, final RingVector v) {
        if ((j >= 0) && (j < numColumns()) && (v.getDimension() == numRows())) {
            for (int i = 0; i < numRows(); i++) {
                matrix[i][j] = v.vector[i];
            }
        } else {
            throw new IllegalDimensionException(
                "Requested element out of bounds.");
        }
    }

    /**
     * Sets the value of an element of the matrix.
     *
     * @param i row index of the element
     * @param j column index of the element
     * @param r a ring element
     *
     * @throws IllegalDimensionException If attempting to access an invalid
     *         element.
     */
    public void setElement(final int i, final int j, final Ring.Member r)
        throws IllegalDimensionException {
        if ((i >= 0) && (i < numRows()) && (j >= 0) && (j < numColumns())) {
            matrix[i][j] = r;
        } else {
            throw new IllegalDimensionException(getInvalidElementMsg(i, j));
        }
    }

    /**
     * Sets the value of all elements of the matrix.
     *
     * @param m a ring element
     */
    public void setAllElements(final Ring.Member m) {
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                matrix[i][j] = m;
            }
        }
    }

    //============
    // OPERATIONS
    //============
    /**
     * Returns the negative of this matrix.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member negate() {
        final Ring.Member[][] array = new Ring.Member[numRows()][numColumns()];

        for (int j, i = 0; i < numRows(); i++) {
            array[i][0] = (Ring.Member) matrix[i][0].negate();

            for (j = 1; j < numColumns(); j++)
                array[i][j] = (Ring.Member) matrix[i][j].negate();
        }

        return new RingMatrix(array);
    }

    // ADDITION
    /**
     * Returns the addition of this matrix and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(final AbelianGroup.Member m) {
        if (m instanceof RingMatrix) {
            return add((RingMatrix) m);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this matrix and another.
     *
     * @param m a matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public RingMatrix add(final RingMatrix m) throws IllegalDimensionException {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final Ring.Member[][] array = new Ring.Member[numRows()][numColumns()];

            for (int j, i = 0; i < numRows(); i++) {
                array[i][0] = (Ring.Member) matrix[i][0].add(m.getPrimitiveElement(
                            i, 0));

                for (j = 1; j < numColumns(); j++)
                    array[i][j] = (Ring.Member) matrix[i][j].add(m.getPrimitiveElement(
                                i, j));
            }

            return new RingMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // SUBTRACTION
    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member m) {
        if (m instanceof RingMatrix) {
            return subtract((RingMatrix) m);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this matrix and another.
     *
     * @param m a matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public RingMatrix subtract(final RingMatrix m)
        throws IllegalDimensionException {
        if ((numRows() == m.numRows()) && (numColumns() == m.numColumns())) {
            final Ring.Member[][] array = new Ring.Member[numRows()][numColumns()];

            for (int j, i = 0; i < numRows(); i++) {
                array[i][0] = (Ring.Member) matrix[i][0].subtract(m.getPrimitiveElement(
                            i, 0));

                for (j = 1; j < numColumns(); j++)
                    array[i][j] = (Ring.Member) matrix[i][j].subtract(m.getPrimitiveElement(
                                i, j));
            }

            return new RingMatrix(array);
        } else {
            throw new IllegalDimensionException("Matrices are different sizes.");
        }
    }

    // SCALAR MULTIPLICATION
    /**
     * Returns the multiplication of this matrix by a scalar.
     *
     * @param r a ring element.
     *
     * @return DOCUMENT ME!
     */
    public Module.Member scalarMultiply(final Ring.Member r) {
        final Ring.Member[][] array = new Ring.Member[numRows()][numColumns()];

        for (int j, i = 0; i < numRows(); i++) {
            array[i][0] = r.multiply(matrix[i][0]);

            for (j = 1; j < numColumns(); j++)
                array[i][j] = r.multiply(matrix[i][j]);
        }

        return new RingMatrix(array);
    }

    // SCALAR DIVISON
    /**
     * Returns the division of this matrix by a scalar.
     *
     * @param x a field element.
     *
     * @return DOCUMENT ME!
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        final Ring.Member[][] array = new Ring.Member[numRows()][numColumns()];

        for (int j, i = 0; i < numRows(); i++) {
            array[i][0] = ((Field.Member) matrix[i][0]).divide(x);

            for (j = 1; j < numColumns(); j++)
                array[i][j] = ((Field.Member) matrix[i][j]).divide(x);
        }

        return new RingMatrix(array);
    }

    // MATRIX MULTIPLICATION
    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Ring.Member multiply(final Ring.Member m) {
        if (m instanceof RingMatrix) {
            return multiply((RingMatrix) m);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m a matrix
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public RingMatrix multiply(final RingMatrix m)
        throws IllegalDimensionException {
        if (numColumns() == m.numRows()) {
            int n;
            int k;
            final Ring.Member[][] array = new Ring.Member[numRows()][m.numColumns()];

            for (int j = 0; j < numRows(); j++) {
                for (k = 0; k < m.numColumns(); k++) {
                    AbelianGroup.Member g = matrix[j][0].multiply(m.getPrimitiveElement(
                                0, k));

                    for (n = 1; n < numColumns(); n++)
                        g = g.add(matrix[j][n].multiply(m.getPrimitiveElement(
                                        n, k)));

                    array[j][k] = (Ring.Member) g;
                }
            }

            return new RingMatrix(array);
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    // DIRECT SUM
    /**
     * Returns the direct sum of this matrix and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RingMatrix directSum(final RingMatrix m) {
        final Ring.Member[][] array = new Ring.Member[numRows() + m.numRows()][numColumns() +
            m.numColumns()];

        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++)
                array[i][j] = matrix[i][j];
        }

        for (int j, i = 0; i < m.numRows(); i++) {
            for (j = 0; j < m.numColumns(); j++)
                array[i + numRows()][j + numColumns()] = m.getPrimitiveElement(i,
                        j);
        }

        return new RingMatrix(array);
    }

    // TENSOR PRODUCT
    /**
     * Returns the tensor product of this matrix and another.
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RingMatrix tensorProduct(final RingMatrix m) {
        final Ring.Member[][] array = new Ring.Member[numRows() * m.numRows()][numColumns() * m.numColumns()];

        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                for (int k = 0; k < m.numRows(); j++) {
                    for (int l = 0; l < m.numColumns(); l++)
                        array[(i * m.numRows()) + k][(j * m.numColumns()) + l] = matrix[i][j].multiply(m.getPrimitiveElement(
                                    k, l));
                }
            }
        }

        return new RingMatrix(array);
    }

    // TRANSPOSE
    /**
     * Returns the transpose of this matrix.
     *
     * @return a matrix
     */
    public Matrix transpose() {
        final Ring.Member[][] array = new Ring.Member[numColumns()][numRows()];

        for (int j, i = 0; i < numRows(); i++) {
            array[0][i] = matrix[i][0];

            for (j = 1; j < numColumns(); j++)
                array[j][i] = matrix[i][j];
        }

        return new RingMatrix(array);
    }

    /**
     * Applies a function on all the matrix elements.
     *
     * @param f a user-defined function
     *
     * @return a Ring matrix
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public RingMatrix mapElements(final NumberMapping f) {
        final Ring.Member[][] array = new Ring.Member[numRows()][numColumns()];

        if ((numElements() > 0) && (matrix[0][0] instanceof Number)) {
            for (int j, i = 0; i < numRows(); i++) {
                array[i][0] = (Ring.Member) f.map((Number) matrix[i][0]);

                for (j = 1; j < numColumns(); j++)
                    array[i][j] = (Ring.Member) f.map((Number) matrix[i][j]);
            }

            return new RingMatrix(array);
        } else {
            throw new IllegalArgumentException(
                "Vector elements don't implement subclass Number.");
        }
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new RingMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an int array.
     */
    public Ring.Member[][] toPrimitiveArray() {
        final Ring.Member[][] array = new Ring.Member[numRows()][numColumns()];

        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++)
                array[i][j] = matrix[i][j];
        }

        return array;
    }
}
