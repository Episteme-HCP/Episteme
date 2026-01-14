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
 @brief class that handle the creation of SRFs, SRF Set Members and SRF Templates.
 */
package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
class CreateSRF {
    // createSRFTFromCode (limited cases just for the interim SRF for validation checking)
    /**
     * DOCUMENT ME!
     *
     * @param srftCode DOCUMENT ME!
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static BaseSRF fromCode(SRM_SRFT_Code srftCode, SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        BaseSRF retSrf;

        if (srftCode == SRM_SRFT_Code.SRFT_CELESTIODETIC) {
            retSrf = new SRF_Celestiodetic(orm, hsr);
        } else if (srftCode == SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) {
            retSrf = new SRF_Celestiocentric(orm, hsr);
        } else {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("Validation checking not supported"));
        }

        return retSrf;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srf_code DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static BaseSRF standardSRF(SRM_SRF_Code srf_code)
        throws SrmException {
        BaseSRF retSRF = null;

        if (srf_code == SRM_SRF_Code.SRF_BRITISH_NATIONAL_GRID) {
            retSRF = new SRF_TransverseMercator(SRM_ORM_Code.ORM_OSGB_1936,
                    SRM_HSR_Code.HSR_OSGB_1936_ENGLAND,
                    -2.0 * Const.RADIANS_PER_DEGREE, //origin_longitude
                    49.0 * Const.RADIANS_PER_DEGREE, //origin_latitude
                    0.9996012717, //central_scale
                    400000.0, //false_easting
                    -100000.0); //false_northing
        } else if (srf_code == SRM_SRF_Code.SRF_DELAWARE_SPCS_1983) {
            retSRF = new SRF_TransverseMercator(SRM_ORM_Code.ORM_N_AM_1983,
                    SRM_HSR_Code.HSR_N_AM_1983_CONTINENTAL_US,
                    
                /*-75 degrees 25 minutes*/
                (-75.0 - (25.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //origin_longitude
                    38.0 * Const.RADIANS_PER_DEGREE, //origin_latitude
                    (1.0 - (1.0 / 200000.0)), //central_scale
                    200000.0, //false_easting
                    0.0); //false_northing
        } else if (srf_code == SRM_SRF_Code.SRF_GEOCENTRIC_WGS_1984) {
            retSRF = new SRF_Celestiocentric(SRM_ORM_Code.ORM_WGS_1984,
                    SRM_HSR_Code.HSR_WGS_1984_IDENTITY);
        } else if (srf_code == SRM_SRF_Code.SRF_GEODETIC_AUSTRALIA_1984) {
            retSRF = new SRF_Celestiodetic(SRM_ORM_Code.ORM_AUSTRALIAN_GEOD_1984,
                    SRM_HSR_Code.HSR_AUSTRALIAN_GEOD_1984_7_AUSTRALIA_TASMANIA);
        } else if (srf_code == SRM_SRF_Code.SRF_GEOCENTRIC_DATUM_AUSTRALIA_1994) {
            retSRF = new SRF_Celestiocentric(SRM_ORM_Code.ORM_GDA_1994,
                    SRM_HSR_Code.HSR_GDA_1994_IDENTITY_BY_MEASUREMENT);
        } else if (srf_code == SRM_SRF_Code.SRF_GEODETIC_WGS_1984) {
            retSRF = new SRF_Celestiodetic(SRM_ORM_Code.ORM_WGS_1984,
                    SRM_HSR_Code.HSR_WGS_1984_IDENTITY);
        } else if (srf_code == SRM_SRF_Code.SRF_GEODETIC_EUROPE_1950) {
            retSRF = new SRF_Celestiodetic(SRM_ORM_Code.ORM_EUROPE_1950,
                    SRM_HSR_Code.HSR_EUROPE_1950_MEAN_SOLUTION);
        } else if (srf_code == SRM_SRF_Code.SRF_GEODETIC_N_AMERICAN_1983) {
            retSRF = new SRF_Celestiodetic(SRM_ORM_Code.ORM_N_AM_1983,
                    SRM_HSR_Code.HSR_N_AM_1983_CONTINENTAL_US);
        } else if (srf_code == SRM_SRF_Code.SRF_IRISH_GRID_1965) {
            retSRF = new SRF_TransverseMercator(SRM_ORM_Code.ORM_IRELAND_1965,
                    SRM_HSR_Code.HSR_IRELAND_1965_3_IRELAND,
                    -8.0 * Const.RADIANS_PER_DEGREE, //origin_longitude
                    (53.0 + (30.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //origin_latitude
                    1.000035, //central_scale
                    200000.0, //false_easting
                    250000.0); //false_northing
        } else if (srf_code == SRM_SRF_Code.SRF_IRISH_TRANSVERSE_MERCATOR_1989) {
            retSRF = new SRF_TransverseMercator(SRM_ORM_Code.ORM_ETRS_1989,
                    SRM_HSR_Code.HSR_ETRS_1989_IDENTITY_BY_MEASUREMENT,
                    -8.0 * Const.RADIANS_PER_DEGREE, //origin_longitude
                    (53.0 + (30.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //origin_latitude
                    0.999820, //central_scale
                    600000.0, //false_easting
                    750000.0); //false_northing
        } else if (srf_code == SRM_SRF_Code.SRF_LAMBERT_1993) {
            retSRF = new SRF_LambertConformalConic(SRM_ORM_Code.ORM_RGF_1993,
                    SRM_HSR_Code.HSR_RGF_1993_IDENTITY_BY_MEASUREMENT,
                    3.0 * Const.RADIANS_PER_DEGREE, //origin_longitude
                    (46.0 + (30.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //origin_latitude
                    44.0 * Const.RADIANS_PER_DEGREE, //north_parallel_geodetic_latitude
                    49.0 * Const.RADIANS_PER_DEGREE, //south_parallel_geodetic_latitude
                    700000.0, //false_easting
                    6600000.0); //false_northing
        } else if (srf_code == SRM_SRF_Code.SRF_LAMBERT_II_WIDE) {
            retSRF = new SRF_LambertConformalConic(SRM_ORM_Code.ORM_NTF_1896_PM_PARIS,
                    SRM_HSR_Code.HSR_NTF_1896_PM_PARIS_FRANCE,
                    0.0 * Const.RADIANS_PER_DEGREE, //origin_longitude
                    (46.0 + (48.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //origin_latitude
                    (45.0 + (53.0 / 60.0) + (56.108 / 3600.0)) * Const.RADIANS_PER_DEGREE, //north_parallel_geodetic_latitude
                    (47.0 + (41.0 / 60.0) + (45.652 / 3600.0)) * Const.RADIANS_PER_DEGREE, //south_parallel_geodetic_latitude
                    600000.0, //false_easting
                    2200000.0); //false_northing
        } else if (srf_code == SRM_SRF_Code.SRF_MARYLAND_SPCS_1983) {
            retSRF = new SRF_LambertConformalConic(SRM_ORM_Code.ORM_N_AM_1983,
                    SRM_HSR_Code.HSR_N_AM_1983_CONTINENTAL_US,
                    -77.0 * Const.RADIANS_PER_DEGREE, //origin_longitude
                    (37.0 + (50.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //origin_latitude
                    (38.0 + (18.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //north_parallel_geodetic_latitude
                    (39.0 + (27.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //south_parallel_geodetic_latitude
                    400000.0, //false_easting
                    0.0); //false_northing
        } else if (srf_code == SRM_SRF_Code.SRF_MARS_PLANETOCENTRIC_2000) {
            throw new SrmException(SrmException._NOT_IMPLEMENTED,
                new String("createStandardSRF: SRF_MARS_PLANETOCENTRIC_2000 not implemented"));
        } else if (srf_code == SRM_SRF_Code.SRF_MARS_PLANETOGRAPHIC_2000) {
            throw new SrmException(SrmException._NOT_IMPLEMENTED,
                new String("createStandardSRF: SRF_MARS_PLANETOGRAPHIC_2000 not implemented"));
        } else { // none of the above
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createStandardSRF: invalid SRF code"));
        }

        retSRF.setSrfCode(srf_code);

        return retSRF;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srf_set DOCUMENT ME!
     * @param set_member DOCUMENT ME!
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static BaseSRF srfSetMember(SRM_SRFS_Code srf_set,
        int set_member, SRM_ORM_Code orm, SRM_HSR_Code hsr)
        throws SrmException {
        BaseSRF retSRF = null;

        if (srf_set == SRM_SRFS_Code.SRFS_ALABAMA_SPCS) {
            retSRF = createAlabama_SPCS(set_member, orm, hsr);
        } else if (srf_set == SRM_SRFS_Code.SRFS_GTRS_GLOBAL_COORDINATE_SYSTEM) {
            retSRF = createGTRS_GCS(set_member, orm, hsr);
        } else if (srf_set == SRM_SRFS_Code.SRFS_LAMBERT_NTF) {
            retSRF = createLambert_NTF(set_member, orm, hsr);
        } else if (srf_set == SRM_SRFS_Code.SRFS_JAPAN_RECTANGULAR_PLANE_COORDINATE_SYSTEM) {
            retSRF = createJpRectPlane(set_member, orm, hsr);
        } else if (srf_set == SRM_SRFS_Code.SRFS_UNIVERSAL_POLAR_STEREOGRAPHIC) {
            retSRF = createUPS(set_member, orm, hsr);
        } else if (srf_set == SRM_SRFS_Code.SRFS_UNIVERSAL_TRANSVERSE_MERCATOR) {
            retSRF = createUTM(set_member, orm, hsr);
        } else if (srf_set == SRM_SRFS_Code.SRFS_WISCONSIN_SPCS) {
            retSRF = createWI_SPCS(set_member, orm, hsr);
        } else { // none of the above
            throw new SrmException(SrmException._INVALID_CODE,
                new String("srfSetMember: invalid SRFS code"));
        }

        retSRF.setSrfSetCode(srf_set);

        return retSRF;
    }

    // createAlabama_SPCS
    /**
     * DOCUMENT ME!
     *
     * @param set_member DOCUMENT ME!
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    private static BaseSRF createAlabama_SPCS(int set_member, SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        BaseSRF retSRF = null;

        if ((orm != SRM_ORM_Code.ORM_N_AM_1983) ||
                (hsr != SRM_HSR_Code.HSR_N_AM_1983_CONTINENTAL_US)) {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createSRFSetMember: invalid Alabama SPCS SRF ORM code"));
        }

        // ASPCS_WEST_ZONE
        if (set_member == 1) {
            retSRF = new SRF_TransverseMercator(orm, hsr,
                    (-87.0 - (30.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //origin_longitude
                    (50.0) * Const.RADIANS_PER_DEGREE, //origin_latitude
                    1.0 - (1.0 / 15000.0), //central_scale
                    600000.0, //false_easting
                    0.0); //false_northing
        }
        //ASPCS_EAST_ZONE
        else if (set_member == 2) {
            retSRF = new SRF_TransverseMercator(orm, hsr,
                    (-85.0 - (50.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //origin_longitude
                    (30.0 + (30.0 / 60.0)) * Const.RADIANS_PER_DEGREE, //origin_latitude
                    1.0 - (1.0 / 25000.0), //central_scale
                    200000.0, //false_easting
                    0.0); //false_northing
        } else {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createSRFSetMember: invalid Alabama SPCS SRF Set member code"));
        }

        retSRF.setSrfSetMemberCode(set_member);

        return retSRF;
    }

    // createGTRS_GCS
    /**
     * DOCUMENT ME!
     *
     * @param set_member DOCUMENT ME!
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    private static BaseSRF createGTRS_GCS(int set_member, SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        BaseSRF retSRF = null;

        if ((orm != SRM_ORM_Code.ORM_WGS_1984) ||
                (hsr != SRM_HSR_Code.HSR_WGS_1984_IDENTITY)) {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createSRFSetMember: invalid GCS ORM code (valid ORM=> WGS_1984"));
        }

        GtrsDataSet tmpGtrsElem = GtrsDataSet.getElem(set_member);

        retSRF = new SRF_LocalTangentSpaceEuclidean(orm, hsr,
                GtrsDataSet.getCellOrigLon(set_member, tmpGtrsElem), //geodetic_longitude
                GtrsDataSet.getCellOrigLat(tmpGtrsElem), //geodetic_latitude
                0.0, //azimuth
                50000.0, //x_false_origin
                50000.0, //y_false_origin
                0.0); //height_offset

        retSRF.setSrfSetMemberCode(set_member);

        return retSRF;
    }

    // createLambert_NTF
    /**
     * DOCUMENT ME!
     *
     * @param set_member DOCUMENT ME!
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    private static BaseSRF createLambert_NTF(int set_member, SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        BaseSRF retSRF = null;

        if ((orm != SRM_ORM_Code.ORM_NTF_1896) ||
                (hsr != SRM_HSR_Code.HSR_NTF_1896_FRANCE)) {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createSRFSetMember: invalid Lambert NTF SRF ORM code"));
        }

        // LNTF_ZONE_I
        if (set_member == 1) {
            retSRF = new SRF_LambertConformalConic(orm, hsr,
                    0.0 * Const.RADIANS_PER_DEGREE, // origin_longitude
                    (49.5) * Const.RADIANS_PER_DEGREE, // origin_latitude
                    (48.0 + (35.0 / 60.0) + (54.682 / 3600.0)) * Const.RADIANS_PER_DEGREE, // north_parallel_geodetic_latitude
                    (50.0 + (23.0 / 60.0) + (45.282 / 3600.0)) * Const.RADIANS_PER_DEGREE, // south_parallel_geodetic_latitude
                    600000.0, // false_easting
                    200000.0); // false_northing
        }
        // LNTF_ZONE_II
        else if (set_member == 2) {
            retSRF = new SRF_LambertConformalConic(orm, hsr,
                    0.0 * Const.RADIANS_PER_DEGREE, // origin_longitude
                    (46.8) * Const.RADIANS_PER_DEGREE, // origin_latitude
                    (45.0 + (53.0 / 60.0) + (56.108 / 3600.0)) * Const.RADIANS_PER_DEGREE, // north_parallel_geodetic_latitude
                    (47.0 + (41.0 / 60.0) + (45.652 / 3600.0)) * Const.RADIANS_PER_DEGREE, // south_parallel_geodetic_latitude
                    600000.0, // false_easting
                    200000.0); // false_northing
        }
        // LNTF_ZONE_III
        else if (set_member == 3) {
            retSRF = new SRF_LambertConformalConic(orm, hsr,
                    0.0 * Const.RADIANS_PER_DEGREE, // origin_longitude
                    (44.1) * Const.RADIANS_PER_DEGREE, // origin_latitude
                    (43.0 + (11.0 / 60.0) + (57.449 / 3600.0)) * Const.RADIANS_PER_DEGREE, // north_parallel_geodetic_latitude
                    (44.0 + (59.0 / 60.0) + (45.938 / 3600.0)) * Const.RADIANS_PER_DEGREE, // south_parallel_geodetic_latitude
                    600000.0, // false_easting
                    200000.0); // false_northing
        }
        // LNTF_ZONE_IV
        else if (set_member == 4) {
            retSRF = new SRF_LambertConformalConic(orm, hsr,
                    0.0 * Const.RADIANS_PER_DEGREE, // origin_longitude
                    (42.0 + (9.0 / 60.0) + (54.0 / 3600.0)) * Const.RADIANS_PER_DEGREE, // origin_latitude
                    (41.0 + (33.0 / 60.0) + (37.396 / 3600.0)) * Const.RADIANS_PER_DEGREE, // north_parallel_geodetic_latitude
                    (42.0 + (46.0 / 60.0) + (3.588 / 3600)) * Const.RADIANS_PER_DEGREE, // south_parallel_geodetic_latitude
                    234358.0, // false_easting
                    185861.369); // false_northing
        } else {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createSRFSetMember: invalid Lambert NTF SRF Set member code"));
        }

        retSRF.setSrfSetMemberCode(set_member);

        return retSRF;
    }

    // createJpRectPlane
    /**
     * DOCUMENT ME!
     *
     * @param set_member DOCUMENT ME!
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    private static BaseSRF createJpRectPlane(int set_member, SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        BaseSRF retSRF = null;
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
            new String("createSRFSetMember:  SRFS_JAPAN_RECTANGULAR_PLANE_COORDINATE_SYSTEM not implemented"));
    }

    // createUPS
    /**
     * DOCUMENT ME!
     *
     * @param set_member DOCUMENT ME!
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    private static BaseSRF createUPS(int set_member, SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        BaseSRF retSRF = null;

        //
        if (set_member == 1) {
            retSRF = new SRF_PolarStereographic(orm, hsr,
                    SRM_Polar_Aspect.NORTH, //polar_aspect
                    0.0 * Const.RADIANS_PER_DEGREE, //origin_longitude
                    90.0 * Const.RADIANS_PER_DEGREE, //origin_latitude
                    // 						180.0 * Const.RADIANS_PER_DEGREE, //standard_latitude
                0.994, // true_scale
                    2000000.0, //false_easting
                    2000000.0); //false_northing
        } else if (set_member == 2) {
            retSRF = new SRF_PolarStereographic(orm, hsr,
                    SRM_Polar_Aspect.SOUTH, //polar_aspect
                    180.0 * Const.RADIANS_PER_DEGREE, //origin_longitude
                    -90.0 * Const.RADIANS_PER_DEGREE, //origin_latitude
                    // 						0.0 * Const.RADIANS_PER_DEGREE, //standard_latitude
                0.994, // true_scale
                    2000000.0, //false_easting
                    2000000.0); //false_northing
        } else {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createSRFSetMember: invalid UPS Set member code"));
        }

        retSRF.setSrfSetMemberCode(set_member);

        return retSRF;
    }

    // createUTM
    /**
     * DOCUMENT ME!
     *
     * @param set_member DOCUMENT ME!
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    private static BaseSRF createUTM(int set_member, SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        BaseSRF retSRF = null;
        double origin_longitude = 0.0;
        double false_northing = 0.0;

        // UTM zone between 1 and 60
        if ((set_member >= 1) && (set_member <= 60)) {
            origin_longitude = (((double) set_member * 6.0) - 183.0) * Const.RADIANS_PER_DEGREE;
            false_northing = 0.0;
        }
        // UTM zone between 61 and 120
        else if ((set_member >= 61) && (set_member <= 120)) {
            origin_longitude = (((double) set_member * 6.0) - 543.0) * Const.RADIANS_PER_DEGREE;
            false_northing = 10000000.0;
        } else {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createSRFSetMember: invalid UTM Set member code"));
        }

        retSRF = new SRF_TransverseMercator(orm, hsr, origin_longitude, //origin_longitude
                0.0, //origin_latitude
                0.9996, //central_scale
                500000.0, //false_easting
                false_northing); //false_northing

        retSRF.setSrfSetMemberCode(set_member);

        return retSRF;
    }

    // createUPS
    /**
     * DOCUMENT ME!
     *
     * @param set_member DOCUMENT ME!
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    private static BaseSRF createWI_SPCS(int set_member, SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        BaseSRF retSRF = null;

        if ((orm != SRM_ORM_Code.ORM_N_AM_1983) ||
                (hsr != SRM_HSR_Code.HSR_N_AM_1983_CONTINENTAL_US)) {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createSRFSetMember: invalid WI SPCS SRF ORM code"));
        }

        // WSPCS_SOUTH_ZONE
        if (set_member == 1) {
            retSRF = new SRF_LambertConformalConic(orm, hsr,
                    -90.0 * Const.RADIANS_PER_DEGREE, // origin_longitude
                    (42.0) * Const.RADIANS_PER_DEGREE, // origin_latitude
                    (42.0 + (44.0 / 60.0)) * Const.RADIANS_PER_DEGREE, // north_parallel_geodetic_latitude
                    (44.0 + (4.0 / 60.0)) * Const.RADIANS_PER_DEGREE, // south_parallel_geodetic_latitude
                    600000.0, // false_easting
                    0.0); // false_northing
        }
        // WSPCS_CENTRAL_ZONE
        else if (set_member == 2) {
            retSRF = new SRF_LambertConformalConic(orm, hsr,
                    -90.0 * Const.RADIANS_PER_DEGREE, // origin_longitude
                    (43.0 + (50.0 / 60.0)) * Const.RADIANS_PER_DEGREE, // origin_latitude
                    (44.0 + (15.0 / 60.0)) * Const.RADIANS_PER_DEGREE, // north_parallel_geodetic_latitude
                    (45.0 + (30.0 / 60.0)) * Const.RADIANS_PER_DEGREE, // south_parallel_geodetic_latitude
                    600000.0, // false_easting
                    0.0); // false_northing
        }
        // WSPCS_NORTH_ZONE
        else if (set_member == 3) {
            retSRF = new SRF_LambertConformalConic(orm, hsr,
                    -90.0 * Const.RADIANS_PER_DEGREE, // origin_longitude
                    (45.0 + (10.0 / 60.0)) * Const.RADIANS_PER_DEGREE, // origin_latitude
                    (45.0 + (34.0 / 60.0)) * Const.RADIANS_PER_DEGREE, // north_parallel_geodetic_latitude
                    (46.0 + (46.0 / 60.0)) * Const.RADIANS_PER_DEGREE, // south_parallel_geodetic_latitude
                    600000.0, // false_easting
                    0.0); // false_northing
        } else {
            throw new SrmException(SrmException._INVALID_CODE,
                new String("createSRFSetMember: invalid WI SPCS SRF Set member code"));
        }

        retSRF.setSrfSetMemberCode(set_member);

        return retSRF;
    }
}
