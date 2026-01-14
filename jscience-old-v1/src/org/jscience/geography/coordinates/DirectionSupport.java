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

// SRM SDK Release 4.0.0 - July 29, 2004
// - SRM spec. 4.0
/*
 *                             NOTICE
 *
 * This software is provided openly and freely for use in representing and
 * interchanging environmental data & databases.
 *
 * This software was developed for use by the United States Government with
 * unlimited rights.  The software was developed under contract
 * N61339-03-C-0068 by Science Applications International Corporation.
 * The software is unclassified and is deemed as Distribution A, approved
 * for Public Release.
 *
 * Use by others is permitted only upon the ACCEPTANCE OF THE TERMS AND
 * CONDITIONS, AS STIPULATED UNDER THE FOLLOWING PROVISIONS:
 *
 *    1. Recipient may make unlimited copies of this software and give
 *       copies to other persons or entities as long as the copies contain
 *       this NOTICE, and as long as the same copyright notices that
 *       appear on, or in, this software remain.
 *
 *    2. Trademarks. All trademarks belong to their respective trademark
 *       holders.  Third-Party applications/software/information are
 *       copyrighted by their respective owners.
 *
 *    3. Recipient agrees to forfeit all intellectual property and
 *       ownership rights for any version created from the modification
 *       or adaptation of this software, including versions created from
 *       the translation and/or reverse engineering of the software design.
 *
 *    4. Transfer.  Recipient may not sell, rent, lease, or sublicense
 *       this software.  Recipient may, however enable another person
 *       or entity the rights to use this software, provided that this
 *       AGREEMENT and NOTICE is furnished along with the software and
 *       /or software system utilizing this software.
 *
 *       All revisions, modifications, created by the Recipient, to this
 *       software and/or related technical data shall be forwarded by the
 *       Recipient to the Government at the following address:
 *
 *         NAVAIR Orlando TSD PJM
 *         Attention SEDRIS II TPOC
 *         12350 Research Parkway
 *         Orlando, FL 32826-3275
 *
 *         or via electronic mail to:  se-mgmt@sedris.org
 *
 *    5. No Warranty. This software is being delivered to you AS IS
 *       and there is no warranty, EXPRESS or IMPLIED, as to its use
 *       or performance.
 *
 *       The RECIPIENT ASSUMES ALL RISKS, KNOWN AND UNKNOWN, OF USING
 *       THIS SOFTWARE.  The DEVELOPER EXPRESSLY DISCLAIMS, and the
 *       RECIPIENT WAIVES, ANY and ALL PERFORMANCE OR RESULTS YOU MAY
 *       OBTAIN BY USING THIS SOFTWARE OR DOCUMENTATION.  THERE IS
 *       NO WARRANTY, EXPRESS OR, IMPLIED, AS TO NON-INFRINGEMENT OF
 *       THIRD PARTY RIGHTS, MERCHANTABILITY, OR FITNESS FOR ANY
 *       PARTICULAR PURPOSE.  IN NO EVENT WILL THE DEVELOPER, THE
 *       UNITED STATES GOVERNMENT OR ANYONE ELSE ASSOCIATED WITH THE
 *       DEVELOPMENT OF THIS SOFTWARE BE HELD LIABLE FOR ANY CONSEQUENTIAL,
 *       INCIDENTAL OR SPECIAL DAMAGES, INCLUDING ANY LOST PROFITS
 *       OR LOST SAVINGS WHATSOEVER.
 */

/*
 * COPYRIGHT 2003, SCIENCE APPLICATIONS INTERNATIONAL CORPORATION.
 *                 ALL RIGHTS RESERVED.
 *
 */

// SRM_OTHERS_GOES_HERE
/**
 @author David Shen
 @brief Declaration of coordinate base classes.
 */
package org.jscience.geography.coordinates;

/**
 * The Coord abstract class is the base class for all coordinate types.
 * Coordinates can be allocated on the stack, by using "new" and "delete", or
 * by using the SRF class methods. If you use an SRF class method to create a
 * coordinate, you should call the corresponding "freeCoordinate" method to
 * free it.
 *
 * @author David Shen
 *
 * @see Coord2D, Coord3D
 */
class DirectionSupport {
    /**
     * DOCUMENT ME!
     *
     * @param srft_code DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static SRM_SRFT_Code GetDirectionTreatmentFrameid(
        SRM_SRFT_Code srft_code) {
        SRM_SRFT_Code retCode;

        if (srft_code == SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) {
            retCode = SRM_SRFT_Code.SRFT_CELESTIOCENTRIC;
        } else if ((srft_code == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN) ||
                (srft_code == SRM_SRFT_Code.SRFT_CELESTIODETIC) ||
                (srft_code == SRM_SRFT_Code.SRFT_PLANETODETIC) ||
                (srft_code == SRM_SRFT_Code.SRFT_MERCATOR) ||
                (srft_code == SRM_SRFT_Code.SRFT_OBLIQUE_MERCATOR_SPHERICAL) ||
                (srft_code == SRM_SRFT_Code.SRFT_TRANSVERSE_MERCATOR) ||
                (srft_code == SRM_SRFT_Code.SRFT_LAMBERT_CONFORMAL_CONIC) ||
                (srft_code == SRM_SRFT_Code.SRFT_POLAR_STEREOGRAPHIC) ||
                (srft_code == SRM_SRFT_Code.SRFT_EQUIDISTANT_CYLINDRICAL)) {
            retCode = SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN;
        } else {
            retCode = SRM_SRFT_Code.SRFT_UNDEFINED;
        }

        return retCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcSrf DOCUMENT ME!
     * @param tgtSrf DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int GetDirectionCase(BaseSRF srcSrf, BaseSRF tgtSrf) {
        int retCase = 0;
        SRM_SRFT_Code frame_id_src = GetDirectionTreatmentFrameid(srcSrf.getSRFTemplateCode());
        SRM_SRFT_Code frame_id_tgt = GetDirectionTreatmentFrameid(tgtSrf.getSRFTemplateCode());

        if (frame_id_tgt == SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) {
            if (frame_id_src == SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) {
                retCase = 1;
            } else if (frame_id_src == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN) {
                retCase = 2;
            }
        } else if (frame_id_tgt == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN) {
            if (frame_id_src == SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) {
                retCase = 3;
            } else if (frame_id_src == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN) {
                retCase = 4;
            }
        }

        return retCase;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcSrf DOCUMENT ME!
     * @param ref_loc_src DOCUMENT ME!
     * @param dir_values_src DOCUMENT ME!
     * @param tgtSrf DOCUMENT ME!
     * @param ref_loc_tgt DOCUMENT ME!
     * @param dir_values_tgt DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void changeDirectionVector(BaseSRF_3D srcSrf,
        Coord3D ref_loc_src, double[] dir_values_src, BaseSRF_3D tgtSrf,
        Coord3D ref_loc_tgt, double[] dir_values_tgt) throws SrmException {
        double[] temp_vector = new double[3];
        double[] temp_vector2 = new double[3];

        // compute the _Tst constant re-using the DShiftConv object
        DShiftConv dShift = new DShiftConv();
        dShift.setDShiftConst(srcSrf.get_orm(), srcSrf.get_hsr(),
            tgtSrf.get_orm(), tgtSrf.get_hsr());

        try {
            switch (GetDirectionCase(srcSrf, tgtSrf)) {
            case 1: /*The celestiocentric to celestiocentric case*/ {
                //                  System.out.println("in Case 1");

                /*The reference location changes and the rotation portion
                of the datum shift affects the vector but the translation portion doesn't.
                see ISO18026 equations 10.39 and 10.40*/
                Const.multMatrixSubsetVector(dShift._Tst, dir_values_src,
                    dir_values_tgt, 3);

                Const.normalizeDirection(dir_values_tgt);

                break;
            }

            case 2: /*lte to celestiocentric case*/ {
                // 		    System.out.println("in Case 2");
                double[][] F = new double[4][4];

                /* Generate F matrix based on the src srf and ref_coord
                */
                if (srcSrf instanceof SRF_LocalTangentSpaceEuclidean) {
                    SRF_LTSE_Params lteParams = ((SRF_LocalTangentSpaceEuclidean) srcSrf).getSRFParameters();
                    LteSupport.LTP_Rotation_Matrix_F(F,
                        lteParams.geodetic_longitude,
                        lteParams.geodetic_latitude, lteParams.azimuth);
                } else { // any other SRF
                    LteSupport.LTP_Rotation_Matrix_F(F,
                        ref_loc_src.getValues()[0], //lon
                        ref_loc_src.getValues()[1], //lat
                        0.0); //azimuth
                }

                Const.multMatrixSubsetVector(F, dir_values_src, temp_vector, 3);

                Const.multMatrixSubsetVector(dShift._Tst, temp_vector,
                    dir_values_tgt, 3);

                Const.normalizeDirection(dir_values_tgt);

                break;
            }

            case 3: /*the celestiocentric to lte case*/ {
                // 		    System.out.println("in Case 3");
                double[][] F = new double[4][4];
                double[][] F_inv = new double[4][4];

                /* Generate F_inv matrix based on target SRF and ref_coord
                */
                if (tgtSrf instanceof SRF_LocalTangentSpaceEuclidean) {
                    SRF_LTSE_Params lteParams = ((SRF_LocalTangentSpaceEuclidean) tgtSrf).getSRFParameters();
                    LteSupport.LTP_Rotation_Matrix_F(F,
                        lteParams.geodetic_longitude,
                        lteParams.geodetic_latitude, lteParams.azimuth);
                } else {
                    LteSupport.LTP_Rotation_Matrix_F(F,
                        ref_loc_tgt.getValues()[0], //lon
                        ref_loc_tgt.getValues()[1], //lat
                        0.0); //azimuth
                }

                Const.invert(F, F_inv);

                Const.multMatrixSubsetVector(dShift._Tst, dir_values_src,
                    temp_vector, 3);

                Const.multMatrixSubsetVector(F_inv, temp_vector,
                    dir_values_tgt, 3);

                Const.normalizeDirection(dir_values_tgt);

                break;
            }

            case 4: /*The lte case to lte case*/ {
                // 		    System.out.println("in Case 4");
                double[][] src_F = new double[4][4];
                double[][] tgt_F = new double[4][4];
                double[][] tgt_F_inv = new double[4][4];

                /* Generate F matrix based on the src srf and ref_coord
                */
                if (srcSrf instanceof SRF_LocalTangentSpaceEuclidean) {
                    SRF_LTSE_Params lteParams = ((SRF_LocalTangentSpaceEuclidean) srcSrf).getSRFParameters();
                    LteSupport.LTP_Rotation_Matrix_F(src_F,
                        lteParams.geodetic_longitude,
                        lteParams.geodetic_latitude, lteParams.azimuth);
                } else { // any other SRF
                    LteSupport.LTP_Rotation_Matrix_F(src_F,
                        ref_loc_src.getValues()[0], //lon
                        ref_loc_src.getValues()[1], //lat
                        0.0); //azimuth
                }

                /* Generate F_inv matrix based on target SRF and ref_coord
                */
                if (tgtSrf instanceof SRF_LocalTangentSpaceEuclidean) {
                    SRF_LTSE_Params lteParams = ((SRF_LocalTangentSpaceEuclidean) tgtSrf).getSRFParameters();
                    LteSupport.LTP_Rotation_Matrix_F(tgt_F,
                        lteParams.geodetic_longitude,
                        lteParams.geodetic_latitude, lteParams.azimuth);
                } else {
                    LteSupport.LTP_Rotation_Matrix_F(tgt_F,
                        ref_loc_tgt.getValues()[0], //lon
                        ref_loc_tgt.getValues()[1], //lat
                        0.0); //azimuth
                }

                Const.invert(tgt_F, tgt_F_inv);

                Const.multMatrixSubsetVector(src_F, dir_values_src,
                    temp_vector, 3);

                Const.multMatrixSubsetVector(dShift._Tst, temp_vector,
                    temp_vector2, 3);

                Const.multMatrixSubsetVector(tgt_F_inv, temp_vector2,
                    dir_values_tgt, 3);

                Const.normalizeDirection(dir_values_tgt);

                break;
            }

            default:
                throw new SrmException(SrmException._OPERATION_UNSUPPORTED,
                    new String("changeDirectionSRF: case not supported"));
            }

            if (dir_values_tgt[0] == 1.0) {
                throw new SrmException(SrmException._OPERATION_UNSUPPORTED,
                    new String("changeDirectionSRF: case not supported"));
            }
        } catch (SrmException ex) {
            throw new SrmException(SrmException._INACTIONABLE,
                new String("changeDirectionSRF: inactionable error"));
        }
    }
}
