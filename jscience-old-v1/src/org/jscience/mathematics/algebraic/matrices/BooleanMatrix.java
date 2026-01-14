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

import org.jscience.mathematics.algebraic.AbstractMatrix;
import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.algebraic.numbers.Boolean;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.IllegalDimensionException;

import java.awt.*;
import java.io.Serializable;

/**
 * Fixed sized (non resizable) n*m boolean matrix.
 * A boolean matrix has a number of columns and rows, which are assigned upon instance construction - The matrix's size is then <tt>columns()*rows()</tt>.
 * booleans are accessed via <tt>(column,row)</tt> coordinates.
 * <p/>
 * Individual booleans can be examined, set, or cleared.
 * Rectangular parts (boxes) can quickly be extracted, copied and replaced.
 * Quick iteration over boxes is provided by optimized internal iterators (<tt>forEach()</tt> methods).
 * One <code>BooleanMatrix</code> may be used to modify the contents of another
 * <code>BooleanMatrix</code> through logical AND, OR, XOR and other similar operations.
 * <p/>
 * Legal coordinates range from <tt>[0,0]</tt> to <tt>[columns()-1,rows()-1]</tt>.
 * Any attempt to access a boolean at a coordinate <tt>column&lt;0 || column&gt;=columns() || row&lt;0 || row&gt;=rows()</tt> will throw an <tt>IndexOutOfBoundsException</tt>.
 * Operations involving two boolean matrices (like AND, OR, XOR, etc.) will throw an <tt>IllegalArgumentException</tt> if both boolean matrices do not have the same number of columns and rows.
 * <p/>
 * If you need extremely quick access to individual booleans: Although getting and setting individual booleans with methods <tt>get(...)</tt> and <tt>put(...)</tt> is quick, it is even quicker (<b>but not safe</b>) to use <tt>getQuick(...)</tt> and <tt>putQuick(...)</tt>.
 * <p/>
 * <b>Note</b> that this implementation is not synchronized.
 *
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 09/24/99
 * @see BooleanVector
 * @see java.util.BitSet
 */
//Boolean class is defined as a BooleanLogic Member, which means that it has nothing to do with a BooleanRing Member
//we cannot therefore return values for scalarMultiply(RingMember r) or scalarDivide(RingMember r)
public class BooleanMatrix extends AbstractMatrix implements Cloneable, Serializable {

    /*
    * The booleans of this matrix.
    * booleans are stored in row major, i.e.
    * bitIndex==row*columns + column
    * columnOf(bitIndex)==bitIndex%columns
    * rowOf(bitIndex)==bitIndex/columns
    */
    private long bits[];

    /**
     * Constructs a boolean matrix with a given number of columns and rows. All booleans are initially <tt>false</tt>.
     *
     * @param columns the number of columns the matrix shall have.
     * @param rows    the number of rows the matrix shall have.
     * @throws IllegalArgumentException if <tt>columns &lt; 0 || rows &lt; 0</tt>.
     */
    public BooleanMatrix(int rows, int columns) {
        super(rows, columns);
        this.bits = QuickBooleanVector.makeBooleanVector(columns * rows, 1);
    }

    /**
     * Constructs a vector by wrapping an array.
     *
     * @param array an assigned value
     */
    public BooleanMatrix(final boolean array[][]) {
        this(array.length, array[0].length);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                setElement(i, j, array[i][j]);
            }
        }
    }

    /**
     * Copy constructor.
     *
     * @param mat an assigned value
     */
    public BooleanMatrix(final BooleanMatrix mat) {
        this(mat.numRows(), mat.numColumns());
        System.arraycopy(mat.bits, 0, bits, 0, mat.bits.length);
    }

    /**
     * Converts this matrix to a integer matrix.
     *
     * @return a double matrix
     */
    public IntegerMatrix toIntegerMatrix() {
        final int[][] ans = new int[numRows()][numColumns()];

        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                if (getPrimitiveElement(i, j) == true) {
                    ans[i][j] = 1;
                } else {
                    ans[i][j] = 0;
                }
            }
        }

        return new IntegerMatrix(ans);
    }

    /**
     * Converts this matrix to a double matrix.
     *
     * @return a double matrix
     */
    public DoubleMatrix toDoubleMatrix() {
        final double[][] ans = new double[numRows()][numColumns()];

        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                if (getPrimitiveElement(i, j) == true) {
                    ans[i][j] = 1;
                } else {
                    ans[i][j] = 0;
                }
            }
        }

        return new DoubleMatrix(ans);
    }

    /**
     * Converts this matrix to a complex matrix.
     *
     * @return a complex matrix
     */
    public ComplexMatrix toComplexMatrix() {
        final double[][] arrayRe = new double[numRows()][numColumns()];
        final double[][] arrayIm = new double[numRows()][numColumns()];

        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                if (getPrimitiveElement(i, j) == true) {
                    arrayRe[i][j] = 1;
                } else {
                    arrayRe[i][j] = 0;
                }
            }
        }

        return new ComplexMatrix(arrayRe, arrayIm);
    }

    /**
     * Returns the number of booleans currently in the <tt>true</tt> state.
     * Optimized for speed. Particularly quick if the receiver is either sparse or dense.
     */
    public int trueBooleans() {
        return toBooleanVector().trueBooleans();
    }

    /**
     * Returns from the receiver the value of the boolean at the specified coordinate (this is the fastest way of getting an element for this kind of matrix).
     * The value is <tt>true</tt> if this boolean is currently set; otherwise, returns <tt>false</tt>.
     *
     * @param column the index of the column-coordinate.
     * @param row    the index of the row-coordinate.
     * @return the value of the boolean at the specified coordinate.
     * @throws IndexOutOfBoundsException if <tt>column&lt;0 || column&gt;=columns() || row&lt;0 || row&gt;=rows()</tt>
     */
    public boolean getPrimitiveElement(final int row, final int column) {
        if (column < 0 || column >= numColumns() || row < 0 || row >= numRows())
            throw new IllegalDimensionException("column:" + column + ", row:" + row);
        return QuickBooleanVector.get(bits, row * numColumns() + column);
    }

    /**
     * Returns an element of this matrix.
     *
     * @param column the index of the column-coordinate.
     * @param row    the index of the row-coordinate.
     * @throws IllegalDimensionException If attempting to access an invalid element.
     */
    public Boolean getElement(final int row, final int column) {
        return new Boolean(getPrimitiveElement(row, column));
    }

    /**
     * Returns the ith row.
     */
    public AbstractBooleanVector getRow(final int i) {

        boolean[] elements;

        if ((i >= 0) && (i < numRows())) {
            elements = new boolean[numColumns()];
            for (int j = 0; j < numColumns(); j++) {
                elements[j] = QuickBooleanVector.get(bits, i * numColumns() + j);
            }
            return new BooleanVector(elements);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Returns the ith column.
     */
    public AbstractBooleanVector getColumn(final int j) {

        boolean[] elements;

        if ((j >= 0) && (j < numColumns())) {
            elements = new boolean[numRows()];
            for (int i = 0; i < numRows(); i++) {
                elements[i] = QuickBooleanVector.get(bits, i * numColumns() + j);
            }
            return new BooleanVector(elements);
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Sets the ith row.
     */
    public void setRow(final int i, final AbstractBooleanVector v) {

        if ((i >= 0) && (i < numRows()) && (v.getDimension() == numColumns())) {
            for (int j = 0; j < numColumns(); j++) {
                QuickBooleanVector.put(bits, i * numColumns() + j, v.getPrimitiveElement(j));
            }
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Sets the jth column.
     */
    public void setColumn(final int j, final AbstractBooleanVector v) {

        if ((j >= 0) && (j < numColumns()) && (v.getDimension() == numRows())) {
            for (int i = 0; i < numRows(); i++) {
                QuickBooleanVector.put(bits, i * numColumns() + j, v.getPrimitiveElement(i));
            }
        } else
            throw new IllegalDimensionException("Requested element out of bounds.");

    }

    /**
     * Checks whether the receiver contains the given box.
     */
    protected void containsBox(int column, int row, int width, int height) {
        if (column < 0 || column + width > numColumns() || row < 0 || row + height > numRows())
            throw new IllegalDimensionException("column:" + column + ", row:" + row + " ,width:" + width + ", height:" + height);
    }

    /**
     * Constructs and returns a new matrix with <tt>width</tt> columns and <tt>height</tt> rows which is a copy of the contents of the given box.
     * The box ranges from <tt>[column,row]</tt> to <tt>[column+width-1,row+height-1]</tt>, all inclusive.
     *
     * @param column the index of the column-coordinate.
     * @param row    the index of the row-coordinate.
     * @param width  the width of the box.
     * @param height the height of the box.
     * @throws IndexOutOfBoundsException if <tt>column&lt;0 || column+width&gt;columns() || row&lt;0 || row+height&gt;rows()</tt>
     */
    public BooleanMatrix part(int column, int row, int width, int height) {
        if (column < 0 || column + width > numColumns() || row < 0 || row + height > numRows())
            throw new IllegalDimensionException("column:" + column + ", row:" + row + " ,width:" + width + ", height:" + height);
        if (width <= 0 || height <= 0) return new BooleanMatrix(0, 0);

        BooleanMatrix subMatrix = new BooleanMatrix(width, height);
        subMatrix.replaceBoxWith(0, 0, width, height, this, column, row);
        return subMatrix;
    }

    /**
     * Sets the boolean at the specified coordinate to the state specified by <tt>value</tt>.
     *
     * @param column the index of the column-coordinate.
     * @param row    the index of the row-coordinate.
     * @param value  the value of the boolean to be copied into the specified coordinate.
     * @throws IndexOutOfBoundsException if <tt>column&lt;0 || column&gt;=columns() || row&lt;0 || row&gt;=rows()</tt>
     */
    public void setElement(int row, int column, boolean value) {
        if (column < 0 || column >= numColumns() || row < 0 || row >= numRows())
            throw new IllegalDimensionException("column:" + column + ", row:" + row);
        QuickBooleanVector.put(bits, row * numColumns() + column, value);
    }

    /**
     * Sets the boolean at the specified coordinate to the state specified by <tt>value</tt>.
     *
     * @param column the index of the column-coordinate.
     * @param row    the index of the row-coordinate.
     * @param value  the value of the boolean to be copied into the specified coordinate.
     * @throws IndexOutOfBoundsException if <tt>column&lt;0 || column&gt;=columns() || row&lt;0 || row&gt;=rows()</tt>
     */
    public void setElement(int row, int column, Boolean value) {
        setElement(row, column, value.value());
    }

    /**
     * Sets the value of all elements of the matrix.
     *
     * @param b a boolean element
     */
    public void setAllElements(final boolean b) {
        for (int j, i = 0; i < numRows(); i++) {
            for (j = 0; j < numColumns(); j++) {
                //todo we can optimize this
                QuickBooleanVector.put(bits, i * numColumns() + j, b);
            }
        }
    }

    /**
     * Replaces a box of the receiver with the contents of another matrix's box.
     * The source box ranges from <tt>[sourceColumn,sourceRow]</tt> to <tt>[sourceColumn+width-1,sourceRow+height-1]</tt>, all inclusive.
     * The destination box ranges from <tt>[column,row]</tt> to <tt>[column+width-1,row+height-1]</tt>, all inclusive.
     * Does nothing if <tt>width &lt;= 0 || height &lt;= 0</tt>.
     * If <tt>source==this</tt> and the source and destination box intersect in an ambiguous way, then replaces as if using an intermediate auxiliary copy of the receiver.
     *
     * @param column       the index of the column-coordinate.
     * @param row          the index of the row-coordinate.
     * @param width        the width of the box.
     * @param height       the height of the box.
     * @param source       the source matrix to copy from(may be identical to the receiver).
     * @param sourceColumn the index of the source column-coordinate.
     * @param sourceRow    the index of the source row-coordinate.
     * @throws IndexOutOfBoundsException if <tt>column&lt;0 || column+width&gt;columns() || row&lt;0 || row+height&gt;rows()</tt>
     * @throws IndexOutOfBoundsException if <tt>sourceColumn&lt;0 || sourceColumn+width&gt;source.columns() || sourceRow&lt;0 || sourceRow+height&gt;source.rows()</tt>
     */
    public void replaceBoxWith(int column, int row, int width, int height, BooleanMatrix source, int sourceColumn, int sourceRow) {
        this.containsBox(column, row, width, height);
        source.containsBox(sourceColumn, sourceRow, width, height);
        if (width <= 0 || height <= 0) return;

        if (source == this) {
            Rectangle destRect = new Rectangle(column, row, width, height);
            Rectangle sourceRect = new Rectangle(sourceColumn, sourceRow, width, height);
            if (destRect.intersects(sourceRect)) { // dangerous intersection
                source = (BooleanMatrix) source.clone();
            }
        }

        BooleanVector sourceVector = source.toBooleanVector();
        BooleanVector destVector = this.toBooleanVector();
        int sourceColumns = source.numColumns();
        for (; --height >= 0; row++, sourceRow++) {
            int offset = row * numColumns() + column;
            int sourceOffset = sourceRow * sourceColumns + sourceColumn;
            destVector.replaceFromToWith(offset, offset + width - 1, sourceVector, sourceOffset);
        }
    }

    /**
     * Sets the booleans in the given box to the state specified by <tt>value</tt>.
     * The box ranges from <tt>[column,row]</tt> to <tt>[column+width-1,row+height-1]</tt>, all inclusive.
     * (Does nothing if <tt>width &lt;= 0 || height &lt;= 0</tt>).
     *
     * @param column the index of the column-coordinate.
     * @param row    the index of the row-coordinate.
     * @param width  the width of the box.
     * @param height the height of the box.
     * @param value  the value of the boolean to be copied into the booleans of the specified box.
     * @throws IndexOutOfBoundsException if <tt>column&lt;0 || column+width&gt;columns() || row&lt;0 || row+height&gt;rows()</tt>
     */
    public void replaceBoxWith(int column, int row, int width, int height, boolean value) {
        containsBox(column, row, width, height);
        if (width <= 0 || height <= 0) return;

        BooleanVector destVector = this.toBooleanVector();
        for (; --height >= 0; row++) {
            int offset = row * numColumns() + column;
            destVector.replaceFromToWith(offset, offset + width - 1, value);
        }
    }

    /**
     * Converts the receiver to a booleanvector.
     * In many cases this method only makes sense on one-dimensional matrices.
     * <b>WARNING:</b> The returned booleanvector and the receiver share the <b>same</b> backing booleans.
     * Modifying either of them will affect the other.
     * If this behaviour is not what you want, you should first use <tt>copy()</tt> to make sure both objects use separate internal storage.
     */
    public BooleanVector toBooleanVector() {
        return new BooleanVector(bits, numElements());
    }

    /**
     * Returns a hash code value for the NON EMPTY receiver.
     */
    public int hashCode() {
        return toBooleanVector().hashCode();
    }

    /**
     * Compares this object against the specified object.
     * The result is <code>true</code> if and only if the argument is
     * not <code>null</code> and is a <code>BooleanMatrix</code> object
     * that has the same number of columns and rows as the receiver and
     * that has exactly the same booleans set to <code>true</code> as the receiver.
     *
     * @param obj the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BooleanMatrix))
            return false;
        if (this == obj)
            return true;

        BooleanMatrix other = (BooleanMatrix) obj;
        if (numColumns() != other.numColumns() || numRows() != other.numRows()) return false;

        return toBooleanVector().equals(other.toBooleanVector());
    }

    /**
     * Returns a (very crude) string representation of the receiver.
     */
    public String toString() {
        return toBooleanVector().toString();
    }

    /**
     * Clears all booleans of the receiver.
     */
    public BooleanMatrix clear() {
        BooleanMatrix result = new BooleanMatrix(numRows(), numColumns());
        result.bits = toBooleanVector().clear().elements();
        return result;
    }

    /**
     * Performs a logical <b>AND</b> of the receiver with another boolean matrix.
     * The receiver is modified so that a boolean in it has the
     * value <code>true</code> if and only if it already had the
     * value <code>true</code> and the corresponding boolean in the other boolean matrix
     * argument has the value <code>true</code>.
     *
     * @param other a boolean matrix.
     * @throws IllegalArgumentException if <tt>columns() != other.columns() || rows() != other.rows()</tt>.
     */
    public BooleanMatrix and(BooleanMatrix other) {
        if (numColumns() == other.numColumns() && numRows() == other.numRows()) {
            BooleanMatrix result = new BooleanMatrix(numRows(), numColumns());
            result.bits = toBooleanVector().and(other.toBooleanVector()).elements();
            return result;
        } else
            throw new IllegalDimensionException("Incompatible dimensions: (rows, columns)=(" + numRows() + "," + numColumns() + "), (other.rows, other.columns)=(" + other.numRows() + "," + other.numColumns() + ")");
    }

    /**
     * Clears all of the booleans in receiver whose corresponding
     * boolean is set in the other boolean matrix.
     * In other words, determines the difference (A\B) between two boolean matrices.
     *
     * @param other a boolean matrix with which to mask the receiver.
     * @throws IllegalArgumentException if <tt>columns() != other.columns() || rows() != other.rows()</tt>.
     */
    public BooleanMatrix andNot(BooleanMatrix other) {
        if (numColumns() == other.numColumns() && numRows() == other.numRows()) {
            BooleanMatrix result = new BooleanMatrix(numRows(), numColumns());
            result.bits = toBooleanVector().andNot(other.toBooleanVector()).elements();
            return result;
        } else
            throw new IllegalDimensionException("Incompatible dimensions: (rows, columns)=(" + numRows() + "," + numColumns() + "), (other.rows, other.columns)=(" + other.numRows() + "," + other.numColumns() + ")");
    }

    /**
     * Performs a logical <b>NOT</b> on the booleans of the receiver.
     */
    public BooleanMatrix not() {
        BooleanMatrix result = new BooleanMatrix(numRows(), numColumns());
        result.bits = toBooleanVector().not().elements();
        return result;
    }

    /**
     * Performs a logical <b>OR</b> of the receiver with another boolean matrix.
     * The receiver is modified so that a boolean in it has the
     * value <code>true</code> if and only if it either already had the
     * value <code>true</code> or the corresponding boolean in the other boolean matrix
     * argument has the value <code>true</code>.
     *
     * @param other a boolean matrix.
     * @throws IllegalArgumentException if <tt>columns() != other.columns() || rows() != other.rows()</tt>.
     */
    public BooleanMatrix or(BooleanMatrix other) {
        if (numColumns() == other.numColumns() && numRows() == other.numRows()) {
            BooleanMatrix result = new BooleanMatrix(numRows(), numColumns());
            result.bits = toBooleanVector().or(other.toBooleanVector()).elements();
            return result;
        } else
            throw new IllegalDimensionException("Incompatible dimensions: (rows, columns)=(" + numRows() + "," + numColumns() + "), (other.rows, other.columns)=(" + other.numRows() + "," + other.numColumns() + ")");
    }

    /**
     * Performs a logical <b>XOR</b> of the receiver with another boolean matrix.
     * The receiver is modified so that a boolean in it has the
     * value <code>true</code> if and only if one of the following statements holds:
     * <ul>
     * <li>The boolean initially has the value <code>true</code>, and the
     * corresponding boolean in the argument has the value <code>false</code>.
     * <li>The boolean initially has the value <code>false</code>, and the
     * corresponding boolean in the argument has the value <code>true</code>.
     * </ul>
     *
     * @param other a boolean matrix.
     * @throws IllegalArgumentException if <tt>columns() != other.columns() || rows() != other.rows()</tt>.
     */
    public BooleanMatrix xor(BooleanMatrix other) {
        if (numColumns() == other.numColumns() && numRows() == other.numRows()) {
            BooleanMatrix result = new BooleanMatrix(numRows(), numColumns());
            result.bits = toBooleanVector().xor(other.toBooleanVector()).elements();
            return result;
        } else
            throw new IllegalDimensionException("Incompatible dimensions: (rows, columns)=(" + numRows() + "," + numColumns() + "), (other.rows, other.columns)=(" + other.numRows() + "," + other.numColumns() + ")");
    }

    //============
    // OPERATIONS
    //============

    /**
     * Returns the negative (not) of this matrix.
     */
    public AbelianGroup.Member negate() {
        return not();
    }

    // ADDITION

    /**
     * Returns the addition (or) of this matrix and another.
     */
    public AbelianGroup.Member add(final AbelianGroup.Member m) {
        if (m instanceof BooleanMatrix) {
            return add((BooleanMatrix) m);
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition (or) of this matrix and another.
     *
     * @param m an boolean matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public BooleanMatrix add(final BooleanMatrix m) {
        return or(m);
    }

    // SUBTRACTION

    /**
     * Returns the subtraction (or not) of this matrix by another.
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member m) {
        if (m instanceof BooleanMatrix) {
            return subtract((BooleanMatrix) m);
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction (or not) of this matrix by another.
     *
     * @param m a boolean matrix
     * @throws IllegalDimensionException If the matrices are different sizes.
     */
    public BooleanMatrix subtract(final BooleanMatrix m) {
        if (numColumns() == m.numColumns() && numRows() == m.numRows()) {
            BooleanMatrix result = new BooleanMatrix(numRows(), numColumns());
            result.bits = toBooleanVector().or(m.toBooleanVector().not()).elements();
            return result;
        } else
            throw new IllegalDimensionException("Incompatible dimensions: (rows, columns)=(" + numRows() + "," + numColumns() + "), (other.rows, other.columns)=(" + m.numRows() + "," + m.numColumns() + ")");
    }

    // SCALAR MULTIPLICATION

    /**
     * Returns the multiplication (and) of this matrix by a scalar.
     */
    public Module.Member scalarMultiply(Ring.Member x) {
        //if (x instanceof Boolean) {
        //    return scalarMultiply(((Boolean) x).value());
        //} else {
        throw new IllegalArgumentException("Member class not recognised by this method.");
        //}
    }

    /**
     * Returns the multiplication (and) of this matrix by a scalar.
     *
     * @param x an boolean
     * @return an boolean matrix
     */
    public BooleanMatrix scalarMultiply(final boolean x) {
        BooleanMatrix result = new BooleanMatrix(numRows(), numColumns());
        result.bits = ((BooleanVector) toBooleanVector().scalarMultiply(x)).elements();
        return result;
    }

    // SCALAR DIVISON

    /**
     * Returns the division of this matrix by a scalar.
     */
    public VectorSpace.Member scalarDivide(Field.Member x) {
        // if (x instanceof Boolean) {
        //     return scalarDivide(((Boolean) x).value());
        // } else {
        throw new IllegalArgumentException("Member class not recognised by this method.");
        //}
    }

    // MATRIX MULTIPLICATION

    /**
     * Returns the multiplication of a vector by this matrix.
     *
     * @param v an integer vector
     * @throws IllegalDimensionException If the matrix and vector are incompatible.
     */
    public BooleanVector multiply(final BooleanVector v) {
        if (numColumns() == v.getDimension()) {
            final boolean[] array = new boolean[numRows()];

            for (int j, i = 0; i < numRows(); i++) {
                array[i] = getPrimitiveElement(i, 0) && v.getPrimitiveElement(0);

                for (j = 1; j < numColumns(); j++)
                    array[i] |= (getPrimitiveElement(i, j) && v.getPrimitiveElement(j));
            }

            return new BooleanVector(array);
        } else {
            throw new IllegalDimensionException("Matrix and vector are incompatible.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     */
    public Ring.Member multiply(final Ring.Member m) {
        if (m instanceof BooleanMatrix) {
            return multiply((BooleanMatrix) m);
        } else {
            throw new IllegalArgumentException("Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this matrix and another.
     *
     * @param m an integer matrix
     * @return an BooleanMatrix or a IntegerSquareMatrix as appropriate
     * @throws IllegalDimensionException If the matrices are incompatible.
     */
    public BooleanMatrix multiply(final BooleanMatrix m) {
        if (numColumns() == m.numRows()) {
            int n;
            int k;
            final boolean[][] array = new boolean[numRows()][m.numColumns()];

            for (int j = 0; j < numRows(); j++) {
                for (k = 0; k < m.numColumns(); k++) {
                    array[j][k] = getPrimitiveElement(j, 0) && m.getPrimitiveElement(0, k);

                    for (n = 1; n < numColumns(); n++)
                        array[j][k] |= (getPrimitiveElement(j, n) && m.getPrimitiveElement(n, k));
                }
            }

            //if (numRows() == m.numColumns()) {
            //    return new BooleanSquareMatrix(array);
            //} else {
            return new BooleanMatrix(array);
            //}
        } else {
            throw new IllegalDimensionException("Incompatible matrices.");
        }
    }

    // TRANSPOSE

    /**
     * Returns the transpose of this matrix.
     *
     * @return an integer matrix
     */
    public Matrix transpose() {
        final boolean[][] array = new boolean[numColumns()][numRows()];

        for (int j, i = 0; i < numRows(); i++) {
            array[0][i] = getPrimitiveElement(i, 0);

            for (j = 1; j < numColumns(); j++)
                array[j][i] = getPrimitiveElement(i, j);
        }

        return new BooleanMatrix(array);
    }

// MAP ELEMENTS

    /**
     * Applies a function on all the matrix elements. We assume that the mapping is from int to int and that input and output values are only 0 or 1 meaning respectively false, true
     *
     * @param f a user-defined function
     * @return a complex matrix
     */
    public BooleanMatrix mapElements(final PrimitiveMapping f) {
        final boolean array[][] = new boolean[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                if (f.map(new Boolean(getPrimitiveElement(i, j)).intValue()) == 1) {
                    array[i][j] = true;
                } else {
                    array[i][j] = false;
                }
            }
        }
        return new BooleanMatrix(array);
    }

    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new BooleanMatrix(this);
    }

    /**
     * Projects the matrix to an array.
     *
     * @return an double array.
     */
    public boolean[][] toPrimitiveArray() {
        final boolean[][] result = new boolean[numRows()][numColumns()];
        for (int i = 0; i < numRows(); i++)
            for (int j = 0; j < numColumns(); j++)
                result[i][j] = getPrimitiveElement(i, j);
        return result;
    }

}
