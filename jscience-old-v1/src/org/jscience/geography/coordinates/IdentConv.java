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


// This class is create for the cases when the source and the target
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
class IdentConv extends Conversions {
/**
     * Creates a new IdentConv object.
     */
    public IdentConv() {
        // this is a dummy data and is not used anywhere in the application
        super(SRM_SRFT_Code.SRFT_UNDEFINED,
            new SRM_SRFT_Code[] { SRM_SRFT_Code.SRFT_UNDEFINED });
    }

    // This method will never be called
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new IdentConv();
    }

    /**
     * DOCUMENT ME!
     *
     * @param SrfType DOCUMENT ME!
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     * @param src DOCUMENT ME!
     * @param dest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SRM_Coordinate_Valid_Region_Code convert(SRM_SRFT_Code SrfType,
        BaseSRF srcSrf, BaseSRF destSrf, double[] src, double[] dest) {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        System.arraycopy(src, 0, dest, 0, src.length);

        return retValid;
    }
}
