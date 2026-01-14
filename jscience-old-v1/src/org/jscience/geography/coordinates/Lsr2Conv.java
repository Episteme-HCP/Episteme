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
class Lsr2Conv extends LsrConv {
/**
     * Creates a new Lsr2Conv object.
     */
    protected Lsr2Conv() {
        // setting the source and destinations of this conversion object
        super(SRM_SRFT_Code.SRFT_LOCAL_SPACE_RECT_2D,
            new SRM_SRFT_Code[] {
                SRM_SRFT_Code.SRFT_LOCAL_SPACE_RECT_2D,
                SRM_SRFT_Code.SRFT_UNDEFINED
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new Lsr2Conv();
    }

    /*
     *----------------------------------------------------------------------------
     *
     * Conversion dispatcher
     *
     *----------------------------------------------------------------------------
     */
    protected SRM_Coordinate_Valid_Region_Code convert(
        SRM_SRFT_Code destSrfType, BaseSRF srcSrf, BaseSRF destSrf,
        double[] src, double[] dest) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        if (destSrfType == SRM_SRFT_Code.SRFT_LOCAL_SPACE_RECT_2D) {
            // No restrictions
            toLsr(srcSrf, destSrf, src, dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_UNDEFINED) {
            // just pass the coordinate through.  This is the last conversion of chain
            dest[0] = src[0];
            dest[1] = src[1];
        }

        return retValid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lsr_2d_SRF_params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static double[][] compute_F_u_v(
        SRF_LSR_2D_Params lsr_2d_SRF_params) throws SrmException {
        /* These values are column vectors representing the
         * axes primary secondary and tertiary
         */
        double[] e1 = { 1, 0, 0 };
        double[] e2 = { 0, 1, 0 };

        double[] s = new double[3];
        double[] t = new double[3]; /*Where s is forward and u is the other one*/

        double[][] F = new double[4][4];

        if (lsr_2d_SRF_params.forward_direction == SRM_Axis_Direction.POS_PRIMARY_AXIS) {
            Const.copyArray(e1, s);
            Const.copyArray(e2, t);
        } else if (lsr_2d_SRF_params.forward_direction == SRM_Axis_Direction.POS_SECONDARY_AXIS) {
            Const.copyArray(e2, s);
            Const.copyArray(e1, t);
        } else if (lsr_2d_SRF_params.forward_direction == SRM_Axis_Direction.NEG_PRIMARY_AXIS) {
            Const.copyArray(e1, s);
            Const.ConstTimesVect(s, -1); /*Explicitly multiplied by -1 for negative primary axis*/
            Const.copyArray(e2, t);
            Const.ConstTimesVect(t, -1);
        } else if (lsr_2d_SRF_params.forward_direction == SRM_Axis_Direction.NEG_SECONDARY_AXIS) {
            Const.copyArray(e2, s);
            Const.ConstTimesVect(s, -1);

            Const.copyArray(e1, t);
            Const.ConstTimesVect(t, -1);
        } else {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("Invalid LSR 2D parameters"));
        }

        /*We use t&s s and as column vectors and we initialize the U matrix*/
        F[0][0] = s[0];
        F[1][0] = t[0];
        F[2][0] = 0.0;
        F[3][0] = 0.0;

        F[0][1] = s[1];
        F[1][1] = t[1];
        F[2][1] = 0.0;
        F[3][1] = 0.0;

        F[0][2] = s[2];
        F[1][2] = t[2];
        F[2][2] = 0.0;
        F[3][2] = 0.0;

        F[0][3] = 0.0;
        F[1][3] = 0.0;
        F[2][3] = 0.0;
        F[3][3] = 1.0;

        return F;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected void setTransformMatrix(BaseSRF srcSrf, BaseSRF destSrf)
        throws SrmException {
        double[][] F_src;
        double[][] F_tgt;
        double[][] F_inv_tgt = new double[4][4];
        SRF_LSR_2D_Params lsr2d_srf_src = ((SRF_LocalSpaceRectangular2D) srcSrf).getSRFParameters();
        SRF_LSR_2D_Params lsr2d_srf_tgt = ((SRF_LocalSpaceRectangular2D) destSrf).getSRFParameters();

        _transformation_matrix = new double[4][4];

        F_src = compute_F_u_v(lsr2d_srf_src);

        F_tgt = compute_F_u_v(lsr2d_srf_tgt);

        F_tgt[2][2] = 1.0;
        Const.transpose(F_tgt, F_inv_tgt, 4);

        Const.matrixMultiply4x4(F_src, F_inv_tgt, _transformation_matrix);
    }
}
