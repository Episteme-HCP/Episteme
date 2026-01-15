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
/**
 */
package org.jscience.geography.coordinates;

/**
 * SRF_LocalTangentSpaceEuclidean class declaration.
 *
 * @author David Shen
 *
 * @see BaseSRF_WithTangentPlaneSurface
 */
public class SRF_LocalTangentSpaceEuclidean
    extends BaseSRF_WithTangentPlaneSurface {
    /// SRF Parameter data using SRF_LTSE_Params
    /** DOCUMENT ME! */
    private SRF_LTSE_Params _params = new SRF_LTSE_Params();

    /// Constructor by SRF parameter object
    /**
     * Creates a new SRF_LocalTangentSpaceEuclidean object.
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    public SRF_LocalTangentSpaceEuclidean(SRM_ORM_Code orm, SRM_HSR_Code hsr,
        SRF_LTSE_Params params) throws SrmException {
        _mySrftCode = SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN;
        _orm = orm;
        _hsr = hsr;
        _params.geodetic_longitude = params.geodetic_longitude;
        _params.geodetic_latitude = params.geodetic_latitude;
        _params.azimuth = params.azimuth;
        _params.x_false_origin = params.x_false_origin;
        _params.y_false_origin = params.y_false_origin;
        _params.height_offset = params.height_offset;

        SrfCheck.forLocalTangentSpaceEuclidean(_orm, _hsr, _params);
    }

    /// Constructor by individual SRF parameters
    /**
     * Creates a new SRF_LocalTangentSpaceEuclidean object.
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param geodetic_longitude DOCUMENT ME!
     * @param geodetic_latitude DOCUMENT ME!
     * @param azimuth DOCUMENT ME!
     * @param x_false_origin DOCUMENT ME!
     * @param y_false_origin DOCUMENT ME!
     * @param height_offset DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    public SRF_LocalTangentSpaceEuclidean(SRM_ORM_Code orm, SRM_HSR_Code hsr,
        double geodetic_longitude, double geodetic_latitude, double azimuth,
        double x_false_origin, double y_false_origin, double height_offset)
        throws SrmException {
        _mySrftCode = SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN;
        _orm = orm;
        _hsr = hsr;
        _params.geodetic_longitude = geodetic_longitude;
        _params.geodetic_latitude = geodetic_latitude;
        _params.azimuth = azimuth;
        _params.x_false_origin = x_false_origin;
        _params.y_false_origin = y_false_origin;
        _params.height_offset = height_offset;

        SrfCheck.forLocalTangentSpaceEuclidean(_orm, _hsr, _params);
    }

    /// Create specific 3D coordinate for SRF_LocalTangentSpaceEuclidean with [ Double.NaN, Double.NaN, Double.NaN ]
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord3D createCoordinate3D() {
        return new Coord3D_LocalTangentSpaceEuclidean((SRF_LocalTangentSpaceEuclidean) this,
            Double.NaN, Double.NaN, Double.NaN);
    }

    /// Create specific 3D coordinate for SRF_LocalTangentSpaceEuclidean with input values
    /**
     * DOCUMENT ME!
     *
     * @param coord_comp1 DOCUMENT ME!
     * @param coord_comp2 DOCUMENT ME!
     * @param coord_comp3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord3D createCoordinate3D(double coord_comp1, double coord_comp2,
        double coord_comp3) {
        return new Coord3D_LocalTangentSpaceEuclidean((SRF_LocalTangentSpaceEuclidean) this,
            coord_comp1, coord_comp2, coord_comp3);
    }

    /// Create specific Surface coordinate for SRF_LocalTangentSpaceEuclidean with [ Double.NaN, Double.NaN ]
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CoordSurf createSurfaceCoordinate() {
        return new CoordSurf_LocalTangentSpaceEuclidean((SRF_LocalTangentSpaceEuclidean) this,
            Double.NaN, Double.NaN);
    }

    /// Create specific Surface coordinate for SRF_LocalTangentSpaceEuclidean with input values
    /**
     * DOCUMENT ME!
     *
     * @param coord_comp1 DOCUMENT ME!
     * @param coord_comp2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CoordSurf createSurfaceCoordinate(double coord_comp1,
        double coord_comp2) {
        return new CoordSurf_LocalTangentSpaceEuclidean((SRF_LocalTangentSpaceEuclidean) this,
            coord_comp1, coord_comp2);
    }

    /// Returns the SRF parameter object
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SRF_LTSE_Params getSRFParameters() {
        SRF_LTSE_Params retParams = new SRF_LTSE_Params();

        retParams.geodetic_longitude = _params.geodetic_longitude;
        retParams.geodetic_latitude = _params.geodetic_latitude;
        retParams.azimuth = _params.azimuth;
        retParams.x_false_origin = _params.x_false_origin;
        retParams.y_false_origin = _params.y_false_origin;
        retParams.height_offset = _params.height_offset;

        return retParams;
    }

    /// Returns the geodetic_longitude SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_geodetic_longitude() {
        return _params.geodetic_longitude;
    }

    /// Returns the geodetic_latitude SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_geodetic_latitude() {
        return _params.geodetic_latitude;
    }

    /// Returns the azimuth SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_azimuth() {
        return _params.azimuth;
    }

    /// Returns the x_false_origin SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_x_false_origin() {
        return _params.x_false_origin;
    }

    /// Returns the y_false_origin SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_y_false_origin() {
        return _params.y_false_origin;
    }

    /// Returns the height_offset SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_height_offset() {
        return _params.height_offset;
    }

    /// Returns a surface coordinate associated with a 3D coordinate for SRF_LocalTangentSpaceEuclidean
    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    public CoordSurf getAssociatedSurfaceCoordinate(Coord3D coord)
        throws SrmException {
        if (coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                new String("getAssociatedSurfaceCoordinate: Coordinate associated with different SRF"));
        }

        return new CoordSurf_LocalTangentSpaceEuclidean((SRF_LocalTangentSpaceEuclidean) this,
            coord.getValues()[0], coord.getValues()[1]);
    }

    /// Returns a 3D coordinate representing the same location as
    /**
     * DOCUMENT ME!
     *
     * @param surf_coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    public Coord3D getPromotedSurfaceCoordinate(CoordSurf surf_coord)
        throws SrmException {
        if (surf_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                new String("getPromotedSurfaceCoordinate: Coordinate associated with different SRF"));
        }

        return new Coord3D_LocalTangentSpaceEuclidean((SRF_LocalTangentSpaceEuclidean) this,
            surf_coord.getValues()[0], surf_coord.getValues()[1], 0.0);
    }

    /* Returns TRUE if the SRF parameters are equal
    */
    public boolean isEqual(BaseSRF srf) {
        return ((srf != null) &&
        (srf instanceof SRF_LocalTangentSpaceEuclidean) &&
        (this._orm == srf.get_orm()) && (this._hsr == srf.get_hsr()) &&
        this._params.isEqual(((SRF_LocalTangentSpaceEuclidean) srf).getSRFParameters()));
    }

    /// Returns a String with the parameter values
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String retString = new String();

        retString = retString + "orm: " + _orm + "\n";
        retString = retString + "hsr: " + _hsr + "\n";
        retString = retString + "geodetic_longitude: " +
            _params.geodetic_longitude + "\n";
        retString = retString + "geodetic_latitude: " +
            _params.geodetic_latitude + "\n";
        retString = retString + "azimuth: " + _params.azimuth + "\n";
        retString = retString + "x_false_origin: " + _params.x_false_origin +
            "\n";
        retString = retString + "y_false_origin: " + _params.y_false_origin +
            "\n";
        retString = retString + "height_offset: " + _params.height_offset;

        return retString;
    }
}
