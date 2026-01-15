package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
abstract class CoordCheck {
    // table of reference template SRFs associated with the SRF (standard)
    /** DOCUMENT ME! */
    private static SRM_SRFT_Code[] srfStdBoundaryDefTemplate = {
            SRM_SRFT_Code.SRFT_UNDEFINED, // UNDEFINED,
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // BRITISH_NATIONAL_GRID = 1;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // DELAWARE_SPCS_1983 = 2;
            SRM_SRFT_Code.SRFT_CELESTIOCENTRIC, // GEOCENTRIC_DATUM_AUSTRALIA_1994 = 3;
            SRM_SRFT_Code.SRFT_CELESTIOCENTRIC, // GEOCENTRIC_WGS_1984 = 4;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // GEODETIC_AUSTRALIA_1984 = 5;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // GEODETIC_EUROPE_1950 = 6;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // GEODETIC_N_AMERICAN_1983 = 7;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // GEODETIC_WGS_1984 = 8;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // IRISH_GRID_1965 = 9;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // IRISH_TRANSVERSE_MERCATOR_1989 = 10;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // LAMBERT_1993 = 11;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // LAMBERT_II_WIDE = 12;
            SRM_SRFT_Code.SRFT_CELESTIOCENTRIC, // MARS_PLANETOCENTRIC_2000 = 13;
            SRM_SRFT_Code.SRFT_CELESTIODETIC, // MARS_PLANETOGRAPHIC_2000 = 14;
            SRM_SRFT_Code.SRFT_CELESTIODETIC // MARYLAND_SPCS_1983 = 15;
        };

    // table of reference template SRFs associated with the SRF sets
    // Notice they all have boundaries defined in CD SRF.
    /** DOCUMENT ME! */
    private static SRM_SRFT_Code[] srfsBoundaryDefTemplate = {
            SRM_SRFT_Code.SRFT_UNDEFINED, SRM_SRFT_Code.SRFT_CELESTIODETIC,
            SRM_SRFT_Code.SRFT_CELESTIODETIC, SRM_SRFT_Code.SRFT_CELESTIODETIC,
            SRM_SRFT_Code.SRFT_CELESTIODETIC, SRM_SRFT_Code.SRFT_CELESTIODETIC,
            SRM_SRFT_Code.SRFT_CELESTIODETIC, SRM_SRFT_Code.SRFT_CELESTIODETIC
        };

    // table of reference template SRFs associated with the SRF templates
    // Notice that only the Projection based SRFs have their boundary SRFs
    // different from their own SRFs
    /** DOCUMENT ME! */
    private static SRM_SRFT_Code[] srftBoundaryDefTemplate = {
            SRM_SRFT_Code.SRFT_UNDEFINED, SRM_SRFT_Code.SRFT_CELESTIOCENTRIC,
            SRM_SRFT_Code.SRFT_LOCAL_SPACE_RECT_3D,
            SRM_SRFT_Code.SRFT_CELESTIODETIC, SRM_SRFT_Code.SRFT_PLANETODETIC,
            SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN,
            SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL,
            SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL,
            SRM_SRFT_Code.SRFT_CELESTIOMAGNETIC,
            SRM_SRFT_Code.SRFT_EQUATORIAL_INERTIAL,
            SRM_SRFT_Code.SRFT_SOLAR_ECLIPTIC,
            SRM_SRFT_Code.SRFT_SOLAR_EQUATORIAL,
            SRM_SRFT_Code.SRFT_SOLAR_MAGNETIC_ECLIPTIC,
            SRM_SRFT_Code.SRFT_SOLAR_MAGNETIC_DIPOLE,
            SRM_SRFT_Code.SRFT_HELIOSPHERIC_ARIES_ECLIPTIC,
            SRM_SRFT_Code.SRFT_HELIOSPHERIC_EARTH_ECLIPTIC,
            SRM_SRFT_Code.SRFT_HELIOSPHERIC_EARTH_EQUATORIAL,
            SRM_SRFT_Code.SRFT_CELESTIODETIC, SRM_SRFT_Code.SRFT_CELESTIODETIC,
            SRM_SRFT_Code.SRFT_CELESTIODETIC, SRM_SRFT_Code.SRFT_CELESTIODETIC,
            SRM_SRFT_Code.SRFT_CELESTIODETIC, SRM_SRFT_Code.SRFT_CELESTIODETIC,
            SRM_SRFT_Code.SRFT_LOCAL_SPACE_RECT_2D,
            SRM_SRFT_Code.SRFT_LOCAL_SPACE_AZIMUTHAL,
            SRM_SRFT_Code.SRFT_LOCAL_SPACE_POLAR
        };

    /**
     * DOCUMENT ME!
     *
     * @param srfs_code DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static SRM_SRFT_Code getsrfsBoundaryDefTemplate(
        SRM_SRFS_Code srfs_code) {
        return srfsBoundaryDefTemplate[srfs_code.toInt()];
    }

    /**
     * DOCUMENT ME!
     *
     * @param srf DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_SRFT_Code getsrfBoundaryDefTemplate(BaseSRF srf)
        throws SrmException {
        SRM_SRFT_Code retCode = SRM_SRFT_Code.SRFT_UNDEFINED;

        // if it is a SRF Set member
        if (srf.getSRFSetCode() != SRM_SRFS_Code.SRFS_UNDEFINED) {
            retCode = srfsBoundaryDefTemplate[srf.getSRFSetCode().toInt()];
        }
        // if it is a standard (defined) SRF
        else if (srf.getSRFCode() != SRM_SRF_Code.SRF_UNDEFINED) {
            retCode = srfStdBoundaryDefTemplate[srf.getSRFCode().toInt()];
        }
        // it is a SRF template instance
        else if (srf.getSRFTemplateCode() != SRM_SRFT_Code.SRFT_UNDEFINED) {
            retCode = srftBoundaryDefTemplate[srf.getSRFTemplateCode().toInt()];
        } else {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("Invalid source SRF Code"));
        }

        return retCode;
    }

    // check for NaN
    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forNaN(double[] coord)
        throws SrmException {
        // this catches the cases where any of coord is NaN
        if (Double.isNaN(coord[0]) || Double.isNaN(coord[1]) ||
                Double.isNaN(coord[2])) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                new String("Invalid (not well formed) coordinate"));
        } else {
            return SRM_Coordinate_Valid_Region_Code.VALID;
        }
    }

    // forCelestiodetic
    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forCelestiodetic(
        OrmData ormData, double[] coord) throws SrmException {
        if (Const.isWellFormedLongitude(coord[0]) && // longitude
                Const.isWellFormedLatitude(coord[1]) && // latitude
                (coord[2] > -ormData.B)) // ellipsoidal_height
         {
            return SRM_Coordinate_Valid_Region_Code.VALID;
        } else {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                new String("Invalid (not well formed) coordinate"));
        }
    }

    // forLCC  (using Celestiodetic)
    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forLCC_cd(
        OrmData ormData, double[] coord) throws SrmException {
        return forCelestiodetic(ormData, coord);
    }

    // forMercator  (using Celestiodetic)
    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forMercator_cd(
        OrmData ormData, double[] coord) throws SrmException {
        return forCelestiodetic(ormData, coord);
    }

    // forTransverseMercator
    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     * @param params DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forTransverseMercator_cd(
        OrmData ormData, SRF_Mercator_Params params, double[] coord)
        throws SrmException {
        /*This ensures the the coordinate that we got is actually a good celestiodetic*/
        forCelestiodetic(ormData, coord);

        if ((Const.delta_lambda_min(coord[0], params.origin_longitude) <= ((4.0 * Const.RADIANS_PER_DEGREE) +
                Const.EPSILON)) &&
                (coord[1] <= ((84.5 * Const.RADIANS_PER_DEGREE) +
                Const.EPSILON)) &&
                (coord[1] >= ((-80.5 * Const.RADIANS_PER_DEGREE) +
                Const.EPSILON))) {
            return SRM_Coordinate_Valid_Region_Code.VALID;
        } else if ((Const.delta_lambda_min(coord[0], params.origin_longitude) <= ((12.0 * Const.RADIANS_PER_DEGREE) +
                1.0e-5)) &&
                (coord[1] <= ((89.99 * Const.RADIANS_PER_DEGREE) + 1.0e-5)) &&
                (coord[1] >= ((-89.99 * Const.RADIANS_PER_DEGREE) + 1.0e-5))) {
            return SRM_Coordinate_Valid_Region_Code.EXTENDED_VALID;
        }

        throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
            new String("Invalid coordinate"));
    }

    // forPolarStereographic
    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forPolarStereographic_cd(
        SRF_PS_Params params, double[] coord) throws SrmException {
        // 	geodetic_longitude=coord[0];
        // 	geodetic_latitude=coord[1];
        // 	ellipsoidal_height=coord[2];
        if (params.polar_aspect == SRM_Polar_Aspect.NORTH) {
            if (coord[1] >= (84.0 * Const.RADIANS_PER_DEGREE)) {
                return SRM_Coordinate_Valid_Region_Code.VALID;
            } else if (coord[1] <= (83.5 * Const.RADIANS_PER_DEGREE)) {
                return SRM_Coordinate_Valid_Region_Code.EXTENDED_VALID;
            } else {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            }
        } else // SRM_Polar_Aspect.SOUTH
         {
            if (coord[1] <= (-80.0 * Const.RADIANS_PER_DEGREE)) {
                return SRM_Coordinate_Valid_Region_Code.VALID;
            } else if (coord[1] <= (-79.5 * Const.RADIANS_PER_DEGREE)) {
                return SRM_Coordinate_Valid_Region_Code.EXTENDED_VALID;
            } else {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            }
        }
    }

    // forSpherical
    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forSpherical(
        double[] coord) throws SrmException {
        //   spherical_longitude=coord[0];
        //   spherical_latitude=coord[1];
        //   radius=coord[2];
        if (Const.isWellFormedLongitude(coord[0]) &&
                Const.isWellFormedLatitude(coord[1]) &&
                Const.isWellFormedRadius(coord[2])) {
            return SRM_Coordinate_Valid_Region_Code.VALID;
        } else {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                new String("Invalid coordinate"));
        }
    }

    // forAzSpher
    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forAzSpherical(
        double[] coord) throws SrmException {
        //  azimuth =coord[0];
        //  elevation_angle =coord[1];
        //  radius =coord[2];
        if (Const.isWellFormedAzimuth(coord[0]) &&
                Const.isWellFormedLatitude(coord[1]) &&
                Const.isWellFormedRadius(coord[2])) {
            return SRM_Coordinate_Valid_Region_Code.VALID;
        } else {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                new String("Invalid coordinate"));
        }
    }

    // forCylindrical
    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forCylindrical(
        double[] coord) throws SrmException {
        //   cylindrical_angle_theta=coord[0];
        //   radius_rho=coord[1];
        //   height_zeta=coord[2];
        if (Const.isWellFormedAzimuth(coord[0]) &&
                Const.isWellFormedRadius(coord[1])) {
            return SRM_Coordinate_Valid_Region_Code.VALID;
        } else {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                new String("Invalid coordinate"));
        }
    }

    //
    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     * @param params DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forALSP_cd(
        OrmData ormData, SRF_Mercator_Params params, double[] coord)
        throws SrmException {
        // this validation is based on TM validation
        return forTransverseMercator_cd(ormData, params, coord);
    }

    // forGTRS
    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forGTRS(double[] coord)
        throws SrmException {
        /*This is the version that is used in conversions where it is not on the path to
          check the validity
        */
        return SRM_Coordinate_Valid_Region_Code.DEFINED;
    }

    // forGTRS
    /**
     * DOCUMENT ME!
     *
     * @param srfSetMemberCode DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forGTRS_cd(
        int srfSetMemberCode, double[] coord) throws SrmException {
        /*This uses code inside of the GTRS library to look up the
          validity of a GCS coordinate.  it takes the cellid and the WGS84 Celestiodetic coordinates
          of a point
        */
        double GtrsCellOriginLat = 0.0;

        GtrsDataSet tmpGtrsElem = GtrsDataSet.getElem(srfSetMemberCode);

        GtrsCellOriginLat = GtrsDataSet.getCellOrigLat(tmpGtrsElem);

        if ((coord[0] >= GtrsDataSet.getCellOrigLonMinExtent(srfSetMemberCode,
                    tmpGtrsElem)) &&
                (coord[0] <= GtrsDataSet.getCellOrigLonMaxExtent(
                    srfSetMemberCode, tmpGtrsElem)) &&
                (coord[1] >= (GtrsCellOriginLat -
                (0.5 * Const.RADIANS_PER_DEGREE))) &&
                (coord[1] <= (GtrsCellOriginLat +
                (0.5 * Const.RADIANS_PER_DEGREE)))) {
            return SRM_Coordinate_Valid_Region_Code.VALID;
        } else {
            return SRM_Coordinate_Valid_Region_Code.DEFINED;
        }
    }

    // forUPS
    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forUPS_cd(
        SRF_PS_Params params, double[] coord) throws SrmException {
        /*!\note that this routine receives a transverse mercator SRF but a
          coordinate that is celestiodetic since validation for mercator is
          performed in a celestiodetic SRF*/

        // 	geodetic_longitude=coord[0];
        // 	geodetic_latitude=coord[1];
        // 	ellipsoidal_height=coord[2];
        if (params.polar_aspect == SRM_Polar_Aspect.NORTH) {
            if (coord[1] >= (84.0 * Const.RADIANS_PER_DEGREE)) {
                return SRM_Coordinate_Valid_Region_Code.VALID;
            } else if (coord[1] <= (83.5 * Const.RADIANS_PER_DEGREE)) {
                return SRM_Coordinate_Valid_Region_Code.EXTENDED_VALID;
            } else {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            }
        } else //SRM_Polar_Aspect.SOUTH
         {
            if (coord[1] <= (-80.0 * Const.RADIANS_PER_DEGREE)) {
                return SRM_Coordinate_Valid_Region_Code.VALID;
            } else if (coord[1] <= (-79.5 * Const.RADIANS_PER_DEGREE)) {
                return SRM_Coordinate_Valid_Region_Code.EXTENDED_VALID;
            } else {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            }
        }
    }

    // forUTM
    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     * @param srfSetMemberCode DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forUTM_cd(
        SRF_Mercator_Params params, int srfSetMemberCode, double[] coord)
        throws SrmException {
        /*!\note that this routine receives a transverse mercator SRF but a
          coordinate that is celestiodetic since validation for mercator is
          performed in a celestiodetic SRF*/

        // 	geodetic_longitude=coord[0];
        // 	geodetic_latitude=coord[1];
        // 	ellipsoidal_height=coord[2];
        if (srfSetMemberCode <= 60) {
            if ((Const.delta_lambda_min(coord[0], params.origin_longitude) <= (3.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] <= (84.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] >= (0.0 * Const.RADIANS_PER_DEGREE))) {
                return SRM_Coordinate_Valid_Region_Code.VALID;
            } else if ((Const.delta_lambda_min(coord[0], params.origin_longitude) <= (3.5 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] <= (84.5 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] >= (-0.5 * Const.RADIANS_PER_DEGREE))) {
                return SRM_Coordinate_Valid_Region_Code.EXTENDED_VALID;
            } else {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            }
        } else // > 60 and <= 120
         {
            if ((Const.delta_lambda_min(coord[0], params.origin_longitude) <= (3.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] <= (0.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] >= (-80.0 * Const.RADIANS_PER_DEGREE))) {
                return SRM_Coordinate_Valid_Region_Code.VALID;
            } else if ((Const.delta_lambda_min(coord[0], params.origin_longitude) <= (3.5 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] <= (0.5 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] >= (-80.5 * Const.RADIANS_PER_DEGREE))) {
                return SRM_Coordinate_Valid_Region_Code.EXTENDED_VALID;
            } else {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            }
        }
    }

    // forWISP
    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forWISP_cd(
        OrmData ormData, double[] coord) throws SrmException {
        // this validation is based on LCC, which is based on
        // the celectiodetic validation
        return forLCC_cd(ormData, coord);
    }

    // forLNTF
    /**
     * DOCUMENT ME!
     *
     * @param member DOCUMENT ME!
     * @param ormData DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forLNTF_cd(int member,
        OrmData ormData, double[] coord) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = null;

        if (member == 1) { // LAMBERT_NTF_ZONE_I

            if ((coord[0] >= (-5.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[0] <= (10.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] >= (-53.5 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] <= (57.0 * Const.RADIANS_PER_DEGREE))) {
                retValid = SRM_Coordinate_Valid_Region_Code.VALID;
            } else {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            }
        } else if (member == 2) { // LAMBERT_NTF_ZONE_II

            if ((coord[0] >= (-5.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[0] <= (10.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] >= (-50.5 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] <= (53.5 * Const.RADIANS_PER_DEGREE))) {
                retValid = SRM_Coordinate_Valid_Region_Code.VALID;
            } else {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            }
        } else if (member == 3) { // LAMBERT_NTF_ZONE_III

            if ((coord[0] >= (-5.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[0] <= (10.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] >= (-47.0 * Const.RADIANS_PER_DEGREE)) &&
                    (coord[1] <= (50.5 * Const.RADIANS_PER_DEGREE))) {
                retValid = SRM_Coordinate_Valid_Region_Code.VALID;
            } else {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            }
        } else if (member == 4) { // LAMBERT_NTF_ZONE_IV

            /*!\bug ISO18026 CD does not specify actionable extents
              for LNTF zone IV.  this routine will assume all
              coordinates are valid for LNTF zone IV until
              the standard is fixed.
            */
            return SRM_Coordinate_Valid_Region_Code.VALID;
        } else {
            throw new SrmException(SrmException._INACTIONABLE,
                new String("Error in LNTF validation - illegal member=> " +
                    member));
        }

        return retValid;
    }

    // forMTMB
    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     * @param params DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forMTMB_cd(
        OrmData ormData, SRF_Mercator_Params params, double[] coord)
        throws SrmException {
        // this validation is based on TM validation
        return forTransverseMercator_cd(ormData, params, coord);
    }

    // forMTMP
    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     * @param params DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forMTMP_cd(
        OrmData ormData, SRF_Mercator_Params params, double[] coord)
        throws SrmException {
        // this validation is based on TM validation
        return forTransverseMercator_cd(ormData, params, coord);
    }

    // for TM native
    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     * @param bl DOCUMENT ME!
     * @param x_threshold DOCUMENT ME!
     * @param y_threshold DOCUMENT ME!
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static SRM_Coordinate_Valid_Region_Code forTransverseMercator_native(
        double m, double bl, double x_threshold, double y_threshold,
        double[] coord) throws SrmException {
        if (coord[1] > y_threshold) {
            if ((coord[1] > ((m * -Math.abs(coord[0])) + bl))) {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            } else {
                return SRM_Coordinate_Valid_Region_Code.VALID;
            }
        } else if (coord[1] < -y_threshold) {
            if (coord[1] < ((-m * -Math.abs(coord[0])) - bl)) {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            } else {
                return SRM_Coordinate_Valid_Region_Code.VALID;
            }
        } else {
            if (Math.abs(coord[0]) > x_threshold) {
                throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Invalid coordinate"));
            } else {
                return SRM_Coordinate_Valid_Region_Code.VALID;
            }
        }
    }
}
