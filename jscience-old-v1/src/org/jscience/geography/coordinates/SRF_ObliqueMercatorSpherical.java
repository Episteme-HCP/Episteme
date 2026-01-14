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
/**
 */
package org.jscience.geography.coordinates;

/**
 * SRF_ObliqueMercatorSpherical class declaration.
 *
 * @author David Shen
 *
 * @see BaseSRF_MapProjection
 */
public class SRF_ObliqueMercatorSpherical extends BaseSRF_MapProjection {
    /// SRF Parameter data using SRF_OM_Params
    /** DOCUMENT ME! */
    private SRF_OM_Params _params = new SRF_OM_Params();

    /// Constructor by SRF parameter object
    /**
     * Creates a new SRF_ObliqueMercatorSpherical object.
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    public SRF_ObliqueMercatorSpherical(SRM_ORM_Code orm, SRM_HSR_Code hsr,
        SRF_OM_Params params) throws SrmException {
        _mySrftCode = SRM_SRFT_Code.SRFT_OBLIQUE_MERCATOR_SPHERICAL;
        _orm = orm;
        _hsr = hsr;
        _params.longitude1 = params.longitude1;
        _params.latitude1 = params.latitude1;
        _params.longitude2 = params.longitude2;
        _params.latitude2 = params.latitude2;
        _params.central_scale = params.central_scale;
        _params.false_easting = params.false_easting;
        _params.false_northing = params.false_northing;

        SrfCheck.forObliqueMercatorSpherical(_orm, _hsr, _params);
    }

    /// Constructor by individual SRF parameters
    /**
     * Creates a new SRF_ObliqueMercatorSpherical object.
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param longitude1 DOCUMENT ME!
     * @param latitude1 DOCUMENT ME!
     * @param longitude2 DOCUMENT ME!
     * @param latitude2 DOCUMENT ME!
     * @param central_scale DOCUMENT ME!
     * @param false_easting DOCUMENT ME!
     * @param false_northing DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    public SRF_ObliqueMercatorSpherical(SRM_ORM_Code orm, SRM_HSR_Code hsr,
        double longitude1, double latitude1, double longitude2,
        double latitude2, double central_scale, double false_easting,
        double false_northing) throws SrmException {
        _mySrftCode = SRM_SRFT_Code.SRFT_OBLIQUE_MERCATOR_SPHERICAL;
        _orm = orm;
        _hsr = hsr;
        _params.longitude1 = longitude1;
        _params.latitude1 = latitude1;
        _params.longitude2 = longitude2;
        _params.latitude2 = latitude2;
        _params.central_scale = central_scale;
        _params.false_easting = false_easting;
        _params.false_northing = false_northing;

        SrfCheck.forObliqueMercatorSpherical(_orm, _hsr, _params);
    }

    /// Create specific 3D coordinate for SRF_ObliqueMercatorSpherical with [ Double.NaN, Double.NaN, Double.NaN ]
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord3D createCoordinate3D() {
        return new Coord3D_ObliqueMercatorSpherical((SRF_ObliqueMercatorSpherical) this,
            Double.NaN, Double.NaN, Double.NaN);
    }

    /// Create specific 3D coordinate for SRF_ObliqueMercatorSpherical with input values
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
        return new Coord3D_ObliqueMercatorSpherical((SRF_ObliqueMercatorSpherical) this,
            coord_comp1, coord_comp2, coord_comp3);
    }

    /// Create specific Surface coordinate for SRF_ObliqueMercatorSpherical with [ Double.NaN, Double.NaN ]
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CoordSurf createSurfaceCoordinate() {
        return new CoordSurf_ObliqueMercatorSpherical((SRF_ObliqueMercatorSpherical) this,
            Double.NaN, Double.NaN);
    }

    /// Create specific Surface coordinate for SRF_ObliqueMercatorSpherical with input values
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
        return new CoordSurf_ObliqueMercatorSpherical((SRF_ObliqueMercatorSpherical) this,
            coord_comp1, coord_comp2);
    }

    /// Returns the SRF parameter object
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SRF_OM_Params getSRFParameters() {
        SRF_OM_Params retParams = new SRF_OM_Params();

        retParams.longitude1 = _params.longitude1;
        retParams.latitude1 = _params.latitude1;
        retParams.longitude2 = _params.longitude2;
        retParams.latitude2 = _params.latitude2;
        retParams.central_scale = _params.central_scale;
        retParams.false_easting = _params.false_easting;
        retParams.false_northing = _params.false_northing;

        return retParams;
    }

    /// Returns the longitude1 SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_longitude1() {
        return _params.longitude1;
    }

    /// Returns the latitude1 SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_latitude1() {
        return _params.latitude1;
    }

    /// Returns the longitude2 SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_longitude2() {
        return _params.longitude2;
    }

    /// Returns the latitude2 SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_latitude2() {
        return _params.latitude2;
    }

    /// Returns the central_scale SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_central_scale() {
        return _params.central_scale;
    }

    /// Returns the false_easting SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_false_easting() {
        return _params.false_easting;
    }

    /// Returns the false_northing SRF parameter value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_false_northing() {
        return _params.false_northing;
    }

    /// Returns a surface coordinate associated with a 3D coordinate for SRF_ObliqueMercatorSpherical
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

        return new CoordSurf_ObliqueMercatorSpherical((SRF_ObliqueMercatorSpherical) this,
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

        return new Coord3D_ObliqueMercatorSpherical((SRF_ObliqueMercatorSpherical) this,
            surf_coord.getValues()[0], surf_coord.getValues()[1], 0.0);
    }

    /* Returns TRUE if the SRF parameters are equal
    */
    public boolean isEqual(BaseSRF srf) {
        return ((srf != null) && (srf instanceof SRF_ObliqueMercatorSpherical) &&
        (this._orm == srf.get_orm()) && (this._hsr == srf.get_hsr()) &&
        this._params.isEqual(((SRF_ObliqueMercatorSpherical) srf).getSRFParameters()));
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
        retString = retString + "longitude1: " + _params.longitude1 + "\n";
        retString = retString + "latitude1: " + _params.latitude1 + "\n";
        retString = retString + "longitude2: " + _params.longitude2 + "\n";
        retString = retString + "latitude2: " + _params.latitude2 + "\n";
        retString = retString + "central_scale: " + _params.central_scale +
            "\n";
        retString = retString + "false_easting: " + _params.false_easting +
            "\n";
        retString = retString + "false_northing: " + _params.false_northing;

        return retString;
    }
}
