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

package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class LsrConv extends Conversions {
    /** DOCUMENT ME! */
    protected double[][] _transformation_matrix;

/**
     * Creates a new LsrConv object.
     *
     * @param src  DOCUMENT ME!
     * @param dest DOCUMENT ME!
     */
    protected LsrConv(SRM_SRFT_Code src, SRM_SRFT_Code[] dest) {
        super(src, dest);
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected abstract void setTransformMatrix(BaseSRF srcSrf, BaseSRF destSrf)
        throws SrmException;

    /*
     *----------------------------------------------------------------------------
     *
     * ToSphere routines
     *
     *----------------------------------------------------------------------------
     */
    protected void toLsr(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coord, double[] dest_generic_coord)
        throws SrmException {
        if (_transformation_matrix == null) {
            setTransformMatrix(srcSrf, destSrf);
        }

        // although the transformation matrix is 4x4, but we really need to
        // use the upper left 3x3 for this operation.
        Const.applyMatrix3x3(source_generic_coord, _transformation_matrix,
            dest_generic_coord);
    }
}
