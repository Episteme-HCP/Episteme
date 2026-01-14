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
class LtasConv extends SphereConv {
/**
     * Creates a new LtasConv object.
     */
    protected LtasConv() {
        super(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL,
            new SRM_SRFT_Code[] {
                SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN,
                SRM_SRFT_Code.SRFT_UNDEFINED
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new LtasConv();
    }

    // Function dispatcher keyed on the destination SRF
    /**
     * DOCUMENT ME!
     *
     * @param destSrfType DOCUMENT ME!
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     * @param src DOCUMENT ME!
     * @param dest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected SRM_Coordinate_Valid_Region_Code convert(
        SRM_SRFT_Code destSrfType, BaseSRF srcSrf, BaseSRF destSrf,
        double[] src, double[] dest) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        if (destSrfType == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN) {
            // perform pre validation
            retValid = CoordCheck.forAzSpherical(src);
            toLte(srcSrf, destSrf, src, dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_UNDEFINED) {
            // just pass the coordinate through.  This is a datum shift case
            // or this is the last convesion in the chain
            dest[0] = src[0];
            dest[1] = src[1];
            dest[2] = src[2];
        }

        return retValid;
    }

    /*
     *----------------------------------------------------------------------------
     *
     * FUNCTION: toLTE
     *
     *----------------------------------------------------------------------------
     */
    protected void toLte(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coord, double[] dest_generic_coord)
        throws SrmException {
        // the source coordinate is interpreted as
        // source_generic_coord[0] => alpha
        // source_generic_coord[1] => theta
        // source_generic_coord[2] => rho
        double ctheta = Math.cos(source_generic_coord[1]);

        // output x
        dest_generic_coord[0] = source_generic_coord[2] * ctheta * Math.cos(source_generic_coord[0]);

        // output y
        dest_generic_coord[1] = source_generic_coord[2] * ctheta * Math.sin(source_generic_coord[0]);

        // output z
        dest_generic_coord[2] = source_generic_coord[2] * Math.sin(source_generic_coord[1]);
    }
}
