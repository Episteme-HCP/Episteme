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
 @brief Declaration of Base SRF with ellipsoidal height.
 */
package org.jscience.geography.coordinates;

import java.util.HashMap;

// import SRM_Internal.*;

/**
 * The BaseSRF_WithEllipsoidalHeight abstract class.
 *
 * @author David Shen
 * @see BaseSRF_3D, BaseSRF
 */
public abstract class BaseSRF_WithEllipsoidalHeight extends BaseSRF_3D {
    /**
     * Creates a surface coordinate object with default [ Double.NaN, Double.NaN ].
     *
     * @return a surface coordinate object
     */
    public abstract CoordSurf createSurfaceCoordinate();

    /**
     * Creates a surface coordinate object with initial values.
     *
     * @param coord_surf_comp1 in: the values of the first component of the surface coordinate
     * @param coord_surf_comp2 in: the values of the second component of the surface coordinate
     * @return a surface coordinate object
     */
    public abstract CoordSurf createSurfaceCoordinate(double coord_surf_comp1,
                                                      double coord_surf_comp2);

    /**
     * Retrieves a coordinate surface component values
     *
     * @param coord_surf in: a surface coordinate
     * @return an array of size 2 containing the component values for the surface coordinate
     * @note The input surface coordinate must have been created using this SRF.
     */
    public double[] getSurfaceCoordinateValues(CoordSurf coord_surf)
            throws SrmException {
        if (coord_surf == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getSurfaceCoordinateValues: Input null reference coordinate"));
        }

        return coord_surf.getValues();
    }

    /**
     * Returns a surface coordinate associated with a 3D coordinate.
     *
     * @param coord in: a 3D coordinate in this SRF
     * @return a surface coordinate object in this SRF
     * @note The input coordinate must have been created using this SRF.
     */
    public abstract CoordSurf getAssociatedSurfaceCoordinate(Coord3D coord)
            throws SrmException;

    /**
     * Returns a 3D coordinate representing the same location as specified
     * by a surface coordinate.
     *
     * @param coord in: a surface coordinate in this SRF
     * @return a 3D coordinate object in this SRF
     * @note The input surface coordinate must have been created using this SRF.
     */
    public abstract Coord3D getPromotedSurfaceCoordinate(CoordSurf surf_coord)
            throws SrmException;

    /**
     * Creates a Local Tangent Euclidean SRF with natural origin at a given
     * position.
     *
     * @param surf_coord     in: a surface coordinate
     * @param azimuth        in: the LTE's azimuth parameter
     * @param false_x_origin in: the LTE's false x origin
     * @param false_y_origin in: the LTE's false y origin
     * @param offset_height  in: the LTE's offset height
     * @return a local tangent euclidean object
     * @note The input surface coordinate must have been created using this SRF.
     */
    public SRF_LocalTangentSpaceEuclidean createLocalTangentSpaceEuclideanSRF(CoordSurf surf_coord, double azimuth, double false_x_origin,
                                                                              double false_y_origin, double offset_height) throws SrmException {
        if (surf_coord == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createLocalTangentEuclideanSRF: null reference input parameter"));
        }

        if (surf_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("createLocalTangentEuclideanSRF: Coordinate associated with different SRF"));
        }

        try {
            return LteSupport.createLtesSRF(this, surf_coord.getValues(),
                    azimuth, false_x_origin, false_y_origin, offset_height);
        } catch (SrmException ex) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createLocalTangentEuclideanSRF: Incompatible input parameters"));
        }
    }

    /**
     * Computes the natural SRF Set member code (region) where the Surface coordinate is
     * located in the target SRF Set.
     *
     * @param src_coord in : the source surface coordinate in an SRF
     * @param orm_dst   in : the ORM for the destination SRF Set
     * @param hsr_dst   in : the HSR for the destination SRF Set
     * @param tgt_srfs  in : the destination SRF Set Code
     * @return the SRF Set Member code for the destination SRF Set
     * @note The input coordinate must have been created using this SRF.
     */
    public int getNaturalSRFSetMemberCode(CoordSurf src_coord,
                                          SRM_ORM_Code orm_dst, SRM_HSR_Code hsr_dst, SRM_SRFS_Code tgt_srfs)
            throws SrmException {
        if ((src_coord == null) || (orm_dst == null) || (hsr_dst == null) ||
                (tgt_srfs == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getNaturalSRFSetMemberCode: null reference input parameter"));
        }

        if (src_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("getNaturalSRFSetMemberCode: Coordinate associated with different SRF"));
        }

        // fill in the third coord (altitude) parameter with 0.0.
        // The set member is independent of the altitude.
        double[] tmpCoord = {
                src_coord.getValues()[0], src_coord.getValues()[1], 0.0
        };

        return getMemberCode(tmpCoord, orm_dst, hsr_dst, tgt_srfs);
    }

    /**
     * Computes the natural SRF Set member instance that the surface coordinate is
     * located in the target SRF Set.
     *
     * @param src_coord in : the source surface coordinate in an SRF
     * @param orm_dst   in : the ORM for the destination SRF Set
     * @param hsr_dst   in : the Hsr for the destination SRF Set
     * @param tgt_srfs  in : the destination SRF Set Code
     * @return the SRF Set Member instance for the destination SRF Set
     * @note The input coordinate must have been created using this SRF.
     */
    public BaseSRF_3D getNaturalSRFSetMember(CoordSurf src_coord,
                                             SRM_ORM_Code orm_dst, SRM_HSR_Code hsr_dst, SRM_SRFS_Code tgt_srfs)
            throws SrmException {
        if ((src_coord == null) || (orm_dst == null) || (hsr_dst == null) ||
                (tgt_srfs == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getNaturalSRFSetMemberCode: null reference input parameter"));
        }

        if (src_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("getNaturalSRFSetMemberCode: Coordinate associated with different SRF"));
        }

        // fill in the third coord (altitude) parameter with 0.0.
        // The set member is independent of the altitude.
        double[] tmpCoord = {
                src_coord.getValues()[0], src_coord.getValues()[1], 0.0
        };

        int tmpMemberCode;

        tmpMemberCode = getMemberCode(tmpCoord, orm_dst, hsr_dst, tgt_srfs);

        return (BaseSRF_3D) CreateSRF.srfSetMember(tgt_srfs, tmpMemberCode,
                orm_dst, hsr_dst);
    }

    /**
     * Returns the euclidean distance between two coordinates.
     *
     * @param coord1 in: a coordinate in some SRF
     * @param coord2 in: a coordinate in some SRF
     * @return the Euclidean distance between the two CoordSurf coordinates (in meters).
     * @note The input coordinates do not need to be from the same SRF.
     * @note This method will make (and cache) internal conversions when the inputs coordinates
     * are from SRFs other than SRF_Celestiocentric.
     */
    public static double calculateEuclideanDistance(CoordSurf coord1,
                                                    CoordSurf coord2) throws SrmException {
        SRM_ORM_Code orm_ref_tgt;

        SRF_Celestiocentric tempCcSrf;
        double[] tempCcSrcCoord = new double[3];
        double[] tempCcTgtCoord = new double[3];

        double delta_x;
        double delta_y;
        double delta_z;

        /*Check for null reference*/
        if ((coord1 == null) || (coord2 == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("calculateEuclideanDistance: null reference input parameter"));
        }

        /*Test to see if the source and target SRF's are both for the same body*/
        if (OrmDataSet.getElem(coord1.getSRF().get_orm())._reference_orm != OrmDataSet.getElem(coord2.getSRF().get_orm())._reference_orm) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("calculateEuclideanDistance: coordinates" +
                            " associated with different reference ORMs"));
        }

        // instantiate cache for the interim SRFs
        if (coord1.getSRF()._internalSRFs == null) {
            coord1.getSRF()._internalSRFs = new HashMap();
        }

        tempCcSrf = (SRF_Celestiocentric) coord1.getSRF()._internalSRFs.get("Interim_Cc");

        // create a Celestiocentric SRF using the common
        if (tempCcSrf == null) {
            tempCcSrf = new SRF_Celestiocentric(coord1.getSRF().get_orm(),
                    coord1.getSRF().get_hsr());
        }

        // cache the interim Celestiocentric in the (source) SRF for
        // coordinate 1.  We use the same one for coordinate 2.
        coord1.getSRF()._internalSRFs.put("Interim_Cc", tempCcSrf);

        double[] tmp3DCoord1 = {
                coord1.getValues()[0], coord1.getValues()[1], 0.0
        };

        // converting source coordinate to celestiocentric SRF
        OpManager.instance().computeAsArray(coord1.getSRF(), tempCcSrf,
                tmp3DCoord1, tempCcSrcCoord);

        // 		System.out.println("tempCcSrcCoord=> " + tempCcSrcCoord[0] + ", "
        // 				   + tempCcSrcCoord[1] + ", "
        // 				   + tempCcSrcCoord[2]);
        double[] tmp3DCoord2 = {
                coord2.getValues()[0], coord2.getValues()[1], 0.0
        };

        // converting target coordinate to celestiocentric SRF
        OpManager.instance().computeAsArray(coord2.getSRF(), tempCcSrf,
                tmp3DCoord2, tempCcTgtCoord);

        // 		System.out.println("tempCcTgtCoord=> " + tempCcTgtCoord[0] + ", "
        // 				   + tempCcTgtCoord[1] + ", "
        // 				   + tempCcTgtCoord[2]);
        delta_x = tempCcSrcCoord[0] - tempCcTgtCoord[0];
        delta_y = tempCcSrcCoord[1] - tempCcTgtCoord[1];
        delta_z = tempCcSrcCoord[2] - tempCcTgtCoord[2];

        return Math.sqrt(Const.square(delta_x) + Const.square(delta_y) +
                Const.square(delta_z));
    }

    /**
     * Returns the geodesic distance between two surface coordinates.
     *
     * @param src_coord in: the source surface coordinate in this SRF
     * @param des_coord in: the destination surface coordinate in this SRF
     * @return the geodesic distance between the two surface coordinates (in meters)
     * @note The input surface coordinates must have been created using this SRF.
     */
    public static double calculateGeodesicDistance(CoordSurf src_coord,
                                                   CoordSurf des_coord) throws SrmException {
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
                new String("calculateGeodesicDistance is unimplemented"));
    }

    /**
     * Returns the vertical separation at a position.
     *
     * @param vos        in: the reference vertical offset surface model
     * @param surf_coord in: the surface coordinate in this SRF
     * @return the vertical separation distance (in meters)
     * @note The input surface coordinate must have been created using this SRF.
     */
    public double calculateVerticalSeparationOffset(SRM_VOS_Code vos,
                                                    CoordSurf surf_coord) throws SrmException {
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
                new String("calculateVerticalSeparationOffset is unimplemented"));
    }
}
