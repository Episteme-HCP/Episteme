package org.jscience.mathematics.algebraic;

import org.jscience.mathematics.algebraic.algebras.Algebra;
import org.jscience.util.IllegalDimensionException;

import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * The Matrix superclass provides an abstract encapsulation for traditional 2D matrices. Concrete implementations of this class should implement additional interfaces. See subclasses.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 */
//see http://skal.planet-d.net/demo/matrixfaq.htm
public abstract class AbstractMatrix extends AbstractHypermatrix implements Matrix, Algebra.Member, Serializable {
    /**
     * The number of rows.
     */
    private final int numRows;  //super.getDimensions()[0]

    /**
     * The number of columns.
     */
    private final int numColumns;  //super.getDimensions()[1]

    /**
     * Constructs a matrix.
     */
    public AbstractMatrix(int rows, int cols) {
        super(new int[]{rows, cols});
        if ((rows > 0) && (cols > 0)) {
            numRows = rows;
            numColumns = cols;
        } else
            throw new IllegalDimensionException("Matrix dimensions must be strictly positive integers.");
    }

    /**
     * Returns the number of rows.
     */
    public final int numRows() {
        return numRows;
    }

    /**
     * Returns the number of columns.
     */
    public final int numColumns() {
        return numColumns;
    }

    /**
     * Returns the element at position i,j.
     */
    //public abstract Number getElement(int i, int j) throws IllegalDimensionException;

    /**
     * Returns the element at position given by the array of int.
     */
    public final Number getElement(int[] position) throws IllegalDimensionException {

        if (position.length == 2) {
            if ((position[0] >= 0) && (position[0] < numRows()) && (position[1] >= 0) && (position[0] < numColumns())) {
                return getElement(position[0], position[1]);
            } else
                throw new IllegalDimensionException("Requested element out of bounds.");
        } else
            throw new IllegalArgumentException("Array of positions hasn't matching size with dimensions.");

    }

    //Object is a Number[][]
    public Object toArray() {

        return toArray(this);

    }

    /**
     * Converts a matrix to an array.
     */
    //Java doesn't allow static here, too bad
    public Number[][] toArray(Matrix v) {

        Number[][] array = new Number[v.numRows()][v.numColumns()];

        for (int j, i = 0; i < array.length; i++) {
            for (j = 0; j < array[0].length; j++)
                array[i][j] = v.getElement(i, j);
        }

        return array;

    }

    /**
     * Returns the ith row.
     */
    /**
     public Vector getRow(int i) {

     Number[] elements;

     if ((i>=0) && (i<numRows())) {
     elements = new Number[numColumns()];
     for (int j=0; j<numColumns(); j++) {
     elements[j] = getElement(i,j);
     }
     return new Vector(elements);
     } else
     throw new IllegalDimensionException("Requested element out of bounds.");

     }
     */

    /**
     * Returns the ith column.
     */
    /**
     public Vector getColumn(int j) {

     Number[] elements;

     if ((j>=0) && (j<numColumns())) {
     elements = new Number[numRows()];
     for (int i=0; j<numRows(); j++) {
     elements[j] = getElement(i,j);
     }
     return new Vector(elements);
     } else
     throw new IllegalDimensionException("Requested element out of bounds.");

     }
     */

    /**
     * Returns the transpose of this matrix.
     */
    //public abstract Matrix transpose();

    /**
     public Matrix transpose() {

     Matrix result;

     result = new Matrix(numColumns(), numRows());
     for (int i=0; i<numRows(); i++) {
     for (int j=0; j<numColumns(); j++) {
     result.setElement(j, i, getElement(i, j));
     }
     }
     return result;
     }
     */

    /**
     * Returns an "invalid element" error message.
     *
     * @param i row index of the element
     * @param j column index of the element
     */
    protected static String getInvalidElementMsg(int i, int j) {
        return "(" + i + ',' + j + ") is an invalid element for this matrix.";
    }

    /**
     * Print the matrix to stdout.   Line the elements up in columns
     * with a Fortran-like 'Fw.d' style format.
     *
     * @param w Column width.
     * @param d Number of digits after the decimal.
     */
    public void print(int w, int d) {
        print(new PrintWriter(System.out, true), w, d);
    }

    /**
     * Print the matrix to the output stream.   Line the elements up in
     * columns with a Fortran-like 'Fw.d' style format.
     *
     * @param output Output stream.
     * @param w      Column width.
     * @param d      Number of digits after the decimal.
     */
    public void print(PrintWriter output, int w, int d) {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        format.setMinimumIntegerDigits(1);
        format.setMaximumFractionDigits(d);
        format.setMinimumFractionDigits(d);
        format.setGroupingUsed(false);
        print(output, format, w + 2);
    }

    /**
     * Print the matrix to stdout.  Line the elements up in columns.
     * Use the format object, and right justify within columns of width
     * characters.
     * Note that is the matrix is to be read back in, you probably will want
     * to use a NumberFormat that is set to US Locale.
     *
     * @param format A  Formatting object for individual elements.
     * @param width  Field width for each column.
     * @see java.text.DecimalFormat#setDecimalFormatSymbols
     */
    public void print(NumberFormat format, int width) {
        print(new PrintWriter(System.out, true), format, width);
    }

    // DecimalFormat is a little disappointing coming from Fortran or C's printf.
    // Since it doesn't pad on the left, the elements will come out different
    // widths.  Consequently, we'll pass the desired column width in as an
    // argument and do the extra padding ourselves.

    /**
     * Print the matrix to the output stream.  Line the elements up in columns.
     * Use the format object, and right justify within columns of width
     * characters.
     * Note that is the matrix is to be read back in, you probably will want
     * to use a NumberFormat that is set to US Locale.
     *
     * @param output the output stream.
     * @param format A formatting object to format the matrix elements
     * @param width  Column width.
     * @see java.text.DecimalFormat#setDecimalFormatSymbols
     */
    public void print(PrintWriter output, NumberFormat format, int width) {
        output.println();  // start on new line.
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numColumns(); j++) {
                String s = format.format(getElement(i, j)); // format the number
                int padding = Math.max(1, width - s.length()); // At _least_ 1 space
                for (int k = 0; k < padding; k++)
                    output.print(' ');
                output.print(s);
            }
            output.println();
        }
        output.println();   // end with blank line.
    }
}
