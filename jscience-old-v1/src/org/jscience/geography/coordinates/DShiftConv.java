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
class DShiftConv extends Conversions {
    /** DOCUMENT ME! */
    protected double[][] _Tst = {
            { 1.0, 0.0, 0.0, 0.0 },
            { 0.0, 1.0, 0.0, 0.0 },
            { 0.0, 0.0, 1.0, 0.0 },
            { 0.0, 0.0, 0.0, 1.0 }
        };

    /** DOCUMENT ME! */
    private boolean constAreSet = false;

/**
     * Creates a new DShiftConv object.
     */
    protected DShiftConv() {
        super(SRM_SRFT_Code.SRFT_UNDEFINED,
            new SRM_SRFT_Code[] { SRM_SRFT_Code.SRFT_UNDEFINED });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new DShiftConv();
    }

    /**
     * DOCUMENT ME!
     *
     * @param srfType DOCUMENT ME!
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     * @param src DOCUMENT ME!
     * @param dest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected SRM_Coordinate_Valid_Region_Code convert(SRM_SRFT_Code srfType,
        BaseSRF srcSrf, BaseSRF destSrf, double[] src, double[] dest)
        throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        // we will just apply the 4x4 matrix and return it as the "dest" Coordinate
        if (!constAreSet) {
            setDShiftConst(srcSrf.get_orm(), srcSrf.get_hsr(),
                destSrf.get_orm(), destSrf.get_hsr());
        }

        double[] temp1 = { src[0], src[1], src[2], 1.0 };
        double[] temp2 = new double[4];

        Const.multMatrixSubsetVector(_Tst, temp1, temp2, 4);

        dest[0] = temp2[0];
        dest[1] = temp2[1];
        dest[2] = temp2[2];

        return retValid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcOrm DOCUMENT ME!
     * @param scrHsr DOCUMENT ME!
     * @param tgtOrm DOCUMENT ME!
     * @param tgtHsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected void setDShiftConst(SRM_ORM_Code srcOrm, SRM_HSR_Code scrHsr,
        SRM_ORM_Code tgtOrm, SRM_HSR_Code tgtHsr) throws SrmException {
        HsrDataSet s = null;
        HsrDataSet t = null;

        s = HsrDataSet.getElem(srcOrm, scrHsr);

        t = HsrDataSet.getElem(tgtOrm, tgtHsr);

        double[][] T_SR = new double[4][4];
        double[][] T_RT = new double[4][4];

        // kludge for now until the db is fixed
        if (Double.isNaN(s._delta_x)) {
            s._delta_x = 0.0;
            s._delta_y = 0.0;
            s._delta_z = 0.0;
            s._omega_1 = 0.0;
            s._omega_2 = 0.0;
            s._omega_3 = 0.0;
            s._delta_scale = 0.0;
        }

        if (Double.isNaN(t._delta_x)) {
            t._delta_x = 0.0;
            t._delta_y = 0.0;
            t._delta_z = 0.0;
            t._omega_1 = 0.0;
            t._omega_2 = 0.0;
            t._omega_3 = 0.0;
            t._delta_scale = 0.0;
        }

        // set the _Tsr constant to the result of this tranformation
        Const.WGS84_Transformation_Matrix(T_SR, s._delta_x, s._delta_y,
            s._delta_z, s._omega_1, /* omega1 rot around x_axis*/
            s._omega_2, /* omega2 rot around y_axis*/
            s._omega_3, /* omega3 rot around z_axis*/
            s._delta_scale);

        Const.WGS84_InverseTransformation_Matrix(T_RT, t._delta_x, t._delta_y,
            t._delta_z, t._omega_1, /* omega1 rot around x_axis*/
            t._omega_2, /* omega2 rot around y_axis*/
            t._omega_3, /* omega3 rot around z_axis*/
            t._delta_scale);

        Const.matrixMultiply4x4(T_RT, T_SR, _Tst);

        // 	Const.printMatrix( T_SR, 4 );
        // 	Const.printMatrix( T_RT, 4 );
        // 	Const.printMatrix( _Tst, 4 );
        constAreSet = true;
    }
}
