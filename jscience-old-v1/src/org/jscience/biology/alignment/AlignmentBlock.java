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
 * This class is used by the {@linkplain CrochemoreLandauZivUkelson} algorithm to store
 * the information of an alignment block. All fields are public (but final) in order to
 * simplify the access to the data.
 * <p/>
 * <P>For more information on how this class is used, please refer to the specification
 * of the <CODE>CrochemoreLandauZivUkelson</CODE> class and it subclasses.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 * @see CrochemoreLandauZivUkelson
 */
public class AlignmentBlock {
    /**
     * A pointer to the factor of the first sequence being aligned.
     */
    public final Factor factor1;

    /**
     * A pointer to the factor of the second sequence being aligned.
     */
    public final Factor factor2;

    /**
     * The DIST column of this block.
     */
    public final int[] dist_column;

    /**
     * An array of pointers to prefix blocks of this block.
     */
    public final AlignmentBlock[] ancestor;

    /**
     * This block's output border.
     */
    public final int[] output_border;

    /**
     * An array of indexes to the source of the highest scoring path for each entry in
     * the output border.
     */
    public final int[] source_path;

    /**
     * An array of directions that must be followed to reach the source of the highest
     * scoring path for each entry in the output border.
     *
     * @see CrochemoreLandauZivUkelson#STOP_DIRECTION
     * @see CrochemoreLandauZivUkelson#LEFT_DIRECTION
     * @see CrochemoreLandauZivUkelson#DIAGONAL_DIRECTION
     * @see CrochemoreLandauZivUkelson#TOP_DIRECTION
     */
    public final byte[] direction;

    /**
     * Creates a new root block. A root block does not have <CODE>source_path</CODE> and
     * <CODE>ancestor</CODE> arrays. Moreover, its <CODE>dist_column</CODE> and
     * <CODE>output_border</CODE> arrays are set to zero, and the <CODE>direction</CODE>
     * array is set to contain an <CODE>STOP_DIRECTION</CODE>.
     *
     * @param factor1 factor of the first sequence being aligned
     * @param factor2 factor of the second sequence being aligned
     */
    public AlignmentBlock(Factor factor1, Factor factor2) {
        this.factor1 = factor1;
        this.factor2 = factor2;

        dist_column = output_border = new int[]{0};
        direction = new byte[]{0}; // STOP_DIRECTION
        source_path = null;
        ancestor = null;
    }

    /**
     * Creates a new alignment block, with all arrays created with the specified size.
     *
     * @param factor1 factor of the first sequence being aligned
     * @param factor2 factor of the second sequence being aligned
     * @param size    size of the arrays to be created
     */
    public AlignmentBlock(Factor factor1, Factor factor2, int size) {
        this.factor1 = factor1;
        this.factor2 = factor2;

        dist_column = new int[size];
        output_border = new int[size];
        direction = new byte[size];
        source_path = new int[size];
        ancestor = new AlignmentBlock[size];
    }
}
