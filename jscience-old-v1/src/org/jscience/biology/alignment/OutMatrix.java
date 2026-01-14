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

package org.jscience.biology.alignment;

/**
 * Implements an interface to the OUT matrix of a block. This class is used by the
 * {@linkplain CrochemoreLandauZivUkelson} and subclasses to enconde the OUT matrix
 * from the input border and DIST matrix of an {@linkplain AlignmentBlock}.
 * <p/>
 * <P>The OUT matrix defined as <CODE>OUT[i,j] = I[i] + DIST[i,j]</CODE> where I is the
 * input border array and DIST is the DIST matrix.</P>
 * <p/>
 * <P>The output border of a block is computed from the OUT matrix by taking the maximum
 * value of each column. Note that this class <B>does not compute the OUT matrix</B>, it
 * just stores the necessary information to retrieve a value at any position of the
 * matrix.</P>
 * <p/>
 * <P>It implements the Matrix interface so that the SMAWK algorithm can be used to
 * compute its column maxima.</P>
 * <p/>
 * <P>For more information on how this class is used, please refer to the specification
 * of the <CODE>CrochemoreLandauZivUkelson</CODE> and its subclasses.
 *
 * @author Sergio A. de Carvalho Jr.
 * @see CrochemoreLandauZivUkelson
 * @see CrochemoreLandauZivUkelsonGlobalAlignment
 * @see CrochemoreLandauZivUkelsonLocalAlignment
 * @see AlignmentBlock
 * @see Smawk
 */
public class OutMatrix implements Matrix {
    /**
     * The length of the longest sequence (number of characters) being aligned. It needs
     * to be set only once per alignment.
     */
    protected int max_length;

    /**
     * The maximum absolute score that the current scoring scheme can return. It needs
     * to be set only once per alignment.
     */
    protected int max_score;

    /**
     * The DIST matrix of a block.
     */
    protected int[][] dist;

    /**
     * The input border of a block.
     */
    protected int[] input_border;

    /**
     * The dimension of the OUT matrix.
     */
    protected int dim;

    /**
     * The number of columns of the block.
     */
    protected int lc;

    /**
     * Initialised this OUT matrix interface. This method needs to be executed only once
     * per alignment.
     *
     * @param max_length the length of the longest sequence (number of characters) being
     *                   aligned
     * @param max_score  the maximum absolute score that the current scoring scheme can
     *                   return
     */
    public void init(int max_length, int max_score) {
        this.max_length = max_length;
        this.max_score = max_score;
    }

    /**
     * Sets this interface's data to represent an OUT matrix for a block. This method
     * is typically executed once for each block being aligned.
     *
     * @param dist         the DIST matrix
     * @param input_border the input border
     * @param dim          the dimension of the OUT matrix
     * @param lc           the number of columns of the block
     */
    public void setData(int[][] dist, int[] input_border, int dim, int lc) {
        this.dist = dist;
        this.input_border = input_border;
        this.dim = dim;
        this.lc = lc;
    }

    /**
     * Returns the value at a given position of the matrix. In general it returns the
     * value of <CODE>DIST[col][row] + input_border[row]</CODE>. However, special cases
     * occur for its upper right and lower left triangular parts.
     *
     * @param row row index
     * @param col column index
     * @return the value at row <CODE>row</CODE>, column <CODE>col</CODE> of this OUT
     *         matrix
     */
    public int valueAt(int row, int col) {
        // The DIST matrix is indexed by [column][row]
        if (col < lc) {
            if (row < (dim - (lc - col))) {
                return dist[col][row] + input_border[row];
            } else {
                // lower left triangle entries
                return -(max_length + row + 1) * max_score;
            }
        } else if (col == lc) {
            return dist[col][row] + input_border[row];
        } else {
            if (row < (col - lc)) {
                // upper right triangle entries
                return Integer.MIN_VALUE + row;
            } else {
                return dist[col][row - (col - lc)] + input_border[row];
            }
        }
    }

    /**
     * Returns the number of rows of this OUT matrix.
     *
     * @return the number of rows of this OUT matrix
     */
    public int numRows() {
        return dim;
    }

    /**
     * Returns the number of columns of this OUT matrix.
     *
     * @return the number of columns of this OUT matrix
     */
    public int numColumns() {
        return dim;
    }
}
