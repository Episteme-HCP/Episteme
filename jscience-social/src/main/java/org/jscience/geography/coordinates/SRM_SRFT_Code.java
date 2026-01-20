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
package org.jscience.geography.coordinates;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author David Shen
 * @brief Declaration of SRM SRF template enumeration class.
 */
public class SRM_SRFT_Code extends Enum {
    /**
     * DOCUMENT ME!
     */
    public static final int _totalEnum = 25;
    private static Vector _enumVec = new Vector();
    private static HashMap _enumMap = new HashMap();

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_UNDEFINED = 0; /// Undefined

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_CELESTIOCENTRIC = 1; /// Celestiocentric SRF. The generalization of geocentric spatial reference frames to include non-Earth objects.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_LOCAL_SPACE_RECT_3D = 2; /// Local space rectangular 3D SRF. A 3D Euclidean spatial reference frame for an abstract space.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_CELESTIODETIC = 3; /// Celestiodetic SRF. The generalization of geodetic SRFs to include non-Earth objects.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_PLANETODETIC = 4; /// Planetodetic SRF. Similar to celestiodetic SRF with reversed sign for longitude.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN = 5; /// Local tangent space Euclidean SRF. Euclidean 3D spatial CS with the zero 3^(rd)-coordinate component surface that is tangent to the oblate ellipsoid RD and with the CS natural origin at the tangent point.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL = 6; /// Local tangent space azimuthal spherical SRF. Azimuthal spherical spatial CS with the zero 3^(rd)-coordinate component surface that is tangent to the oblate ellipsoid RD and with the CS natural origin at the tangent point.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL = 7; /// Local tangent space cylindrical SRF. Cylindrical spatial CS with the zero 3^(rd)-coordinate component surface that is tangent to the oblate ellipsoid RD and with the CS natural origin at the tangent point.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_CELESTIOMAGNETIC = 8; /// Celestiomagnetic SRF. A spherical CS based SRF aligned with the magnetic dipole of a celestial object.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_EQUATORIAL_INERTIAL = 9; /// Equatorial inertial SRF. A spherical CS based SRF aligned with the equator of a planet and the direction to the Sun at the vernal equinox (at a specified epoch).

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_SOLAR_ECLIPTIC = 10; /// Solar ecliptic SRF. A spherical CS based SRF aligned with the ecliptic plane of a planet and the direction to the Sun.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_SOLAR_EQUATORIAL = 11; /// Solar equatorial SRF. A spherical CS based planet centred SRF aligned with the ecliptic plane of a planet and the rotational axis of the Sun.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_SOLAR_MAGNETIC_ECLIPTIC = 12; /// Solar magnetic ecliptic SRF. A Euclidean 3D CS based planet centred SRF aligned with the direction to the Sun and the plane formed with that direction and the magnetic dipole of the planet.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_SOLAR_MAGNETIC_DIPOLE = 13; /// Solar magnetic dipole SRF. A Euclidean 3D CS based planet centred SRF aligned with the z-axis aligned with the magnetic dipole and the xz-plane containing the Sun.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_HELIOSPHERIC_ARIES_ECLIPTIC = 14; /// Heliospheric Aries ecliptic SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_HELIOSPHERIC_EARTH_ECLIPTIC = 15; /// Heliospheric Earth ecliptic SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_HELIOSPHERIC_EARTH_EQUATORIAL = 16; /// Heliospheric Earth equatorial SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_MERCATOR = 17; /// Mercator SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_OBLIQUE_MERCATOR_SPHERICAL = 18; /// Oblique Mercator spherical SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_TRANSVERSE_MERCATOR = 19; /// Transverse Mercator SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_LAMBERT_CONFORMAL_CONIC = 20; /// Lambert conformal conic SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_POLAR_STEREOGRAPHIC = 21; /// Polar stereographic SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_EQUIDISTANT_CYLINDRICAL = 22; /// Equidistant cylindrical SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_LOCAL_SPACE_RECT_2D = 23; /// Local space rectangular 2D SRF.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_LOCAL_SPACE_AZIMUTHAL = 24; /// Local space zimuthal SRF. An azimuthal CS based SRF for 2D abstract space.

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFT_LOCAL_SPACE_POLAR = 25; /// Local space polar SRF. A polar CS based SRF for 2D abstract space.

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_UNDEFINED = new SRM_SRFT_Code(_SRFT_UNDEFINED,
            "SRFT_UNDEFINED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_CELESTIOCENTRIC = new SRM_SRFT_Code(_SRFT_CELESTIOCENTRIC,
            "SRFT_CELESTIOCENTRIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_LOCAL_SPACE_RECT_3D = new SRM_SRFT_Code(_SRFT_LOCAL_SPACE_RECT_3D,
            "SRFT_LOCAL_SPACE_RECT_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_CELESTIODETIC = new SRM_SRFT_Code(_SRFT_CELESTIODETIC,
            "SRFT_CELESTIODETIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_PLANETODETIC = new SRM_SRFT_Code(_SRFT_PLANETODETIC,
            "SRFT_PLANETODETIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN = new SRM_SRFT_Code(_SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN,
            "SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL =
            new SRM_SRFT_Code(_SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL,
                    "SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL = new SRM_SRFT_Code(_SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL,
            "SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_CELESTIOMAGNETIC = new SRM_SRFT_Code(_SRFT_CELESTIOMAGNETIC,
            "SRFT_CELESTIOMAGNETIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_EQUATORIAL_INERTIAL = new SRM_SRFT_Code(_SRFT_EQUATORIAL_INERTIAL,
            "SRFT_EQUATORIAL_INERTIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_SOLAR_ECLIPTIC = new SRM_SRFT_Code(_SRFT_SOLAR_ECLIPTIC,
            "SRFT_SOLAR_ECLIPTIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_SOLAR_EQUATORIAL = new SRM_SRFT_Code(_SRFT_SOLAR_EQUATORIAL,
            "SRFT_SOLAR_EQUATORIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_SOLAR_MAGNETIC_ECLIPTIC = new SRM_SRFT_Code(_SRFT_SOLAR_MAGNETIC_ECLIPTIC,
            "SRFT_SOLAR_MAGNETIC_ECLIPTIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_SOLAR_MAGNETIC_DIPOLE = new SRM_SRFT_Code(_SRFT_SOLAR_MAGNETIC_DIPOLE,
            "SRFT_SOLAR_MAGNETIC_DIPOLE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_HELIOSPHERIC_ARIES_ECLIPTIC = new SRM_SRFT_Code(_SRFT_HELIOSPHERIC_ARIES_ECLIPTIC,
            "SRFT_HELIOSPHERIC_ARIES_ECLIPTIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_HELIOSPHERIC_EARTH_ECLIPTIC = new SRM_SRFT_Code(_SRFT_HELIOSPHERIC_EARTH_ECLIPTIC,
            "SRFT_HELIOSPHERIC_EARTH_ECLIPTIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_HELIOSPHERIC_EARTH_EQUATORIAL = new SRM_SRFT_Code(_SRFT_HELIOSPHERIC_EARTH_EQUATORIAL,
            "SRFT_HELIOSPHERIC_EARTH_EQUATORIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_MERCATOR = new SRM_SRFT_Code(_SRFT_MERCATOR,
            "SRFT_MERCATOR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_OBLIQUE_MERCATOR_SPHERICAL = new SRM_SRFT_Code(_SRFT_OBLIQUE_MERCATOR_SPHERICAL,
            "SRFT_OBLIQUE_MERCATOR_SPHERICAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_TRANSVERSE_MERCATOR = new SRM_SRFT_Code(_SRFT_TRANSVERSE_MERCATOR,
            "SRFT_TRANSVERSE_MERCATOR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_LAMBERT_CONFORMAL_CONIC = new SRM_SRFT_Code(_SRFT_LAMBERT_CONFORMAL_CONIC,
            "SRFT_LAMBERT_CONFORMAL_CONIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_POLAR_STEREOGRAPHIC = new SRM_SRFT_Code(_SRFT_POLAR_STEREOGRAPHIC,
            "SRFT_POLAR_STEREOGRAPHIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_EQUIDISTANT_CYLINDRICAL = new SRM_SRFT_Code(_SRFT_EQUIDISTANT_CYLINDRICAL,
            "SRFT_EQUIDISTANT_CYLINDRICAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_LOCAL_SPACE_RECT_2D = new SRM_SRFT_Code(_SRFT_LOCAL_SPACE_RECT_2D,
            "SRFT_LOCAL_SPACE_RECT_2D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_LOCAL_SPACE_AZIMUTHAL = new SRM_SRFT_Code(_SRFT_LOCAL_SPACE_AZIMUTHAL,
            "SRFT_LOCAL_SPACE_AZIMUTHAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFT_Code SRFT_LOCAL_SPACE_POLAR = new SRM_SRFT_Code(_SRFT_LOCAL_SPACE_POLAR,
            "SRFT_LOCAL_SPACE_POLAR");

    private SRM_SRFT_Code(int code, String name) {
        super(code, name);
        _enumMap.put(name, this);
        _enumVec.add(code, this);
    }

    /// returns the SRM_SRFT_Code from its enumerant value
    public static SRM_SRFT_Code getEnum(int enumeration) throws SrmException {
        if ((enumeration < 1) || (enumeration > _totalEnum)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_SRFT_Code.getEnum: enumerant out of range"));
        } else {
            return (SRM_SRFT_Code) (_enumVec.elementAt(enumeration));
        }
    }

    /// returns the SRM_SRFT_Code from its string name
    public static SRM_SRFT_Code getEnum(String name) throws SrmException {
        SRM_SRFT_Code retCode = (SRM_SRFT_Code) _enumMap.get(name);

        if (retCode == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_SRFT_Code.getEnum: enum. string not found=> " +
                            name));
        }

        return retCode;
    }
}
