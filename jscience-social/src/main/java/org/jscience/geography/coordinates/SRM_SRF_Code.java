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
 * @brief Declaration of SRM SRF set enumeration class.
 */
public class SRM_SRF_Code extends Enum {
    /**
     * DOCUMENT ME!
     */
    public static final int _totalEnum = 15;
    private static Vector _enumVec = new Vector();
    private static HashMap _enumMap = new HashMap();

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_UNDEFINED = 0; /// Undefined

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_BRITISH_NATIONAL_GRID = 1; /// British National Grid

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_DELAWARE_SPCS_1983 = 2; /// Delaware (US) State Plane Coordinate System

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_GEOCENTRIC_DATUM_AUSTRALIA_1994 = 3; /// The Geocentric Datum of Australia (GDA).

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_GEOCENTRIC_WGS_1984 = 4; /// Geocentric WGS 1984

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_GEODETIC_AUSTRALIA_1984 = 5; /// Geodetic Australia 1984

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_GEODETIC_EUROPE_1950 = 6; /// Geodetic Europe 1950

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_GEODETIC_N_AMERICAN_1983 = 7; /// Geodetic North American 1983

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_GEODETIC_WGS_1984 = 8; /// Geodetic WGS  1984

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_IRISH_GRID_1965 = 9; /// Irish Grid

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_IRISH_TRANSVERSE_MERCATOR_1989 = 10; /// Irish Transverse Mercator

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_LAMBERT_1993 = 11; /// Lambert-93

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_LAMBERT_II_WIDE = 12; /// Lambert II &eacute;tendu (Lambert II wide)

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_MARS_PLANETOCENTRIC_2000 = 13; /// Mars planetocentric

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_MARS_PLANETOGRAPHIC_2000 = 14; /// Mars planetographic

    /**
     * DOCUMENT ME!
     */
    public static final int _SRF_MARYLAND_SPCS_1983 = 15; /// Maryland (US) State Plane Coordinate System

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_UNDEFINED = new SRM_SRF_Code(_SRF_UNDEFINED,
            "SRF_UNDEFINED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_BRITISH_NATIONAL_GRID = new SRM_SRF_Code(_SRF_BRITISH_NATIONAL_GRID,
            "SRF_BRITISH_NATIONAL_GRID");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_DELAWARE_SPCS_1983 = new SRM_SRF_Code(_SRF_DELAWARE_SPCS_1983,
            "SRF_DELAWARE_SPCS_1983");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_GEOCENTRIC_DATUM_AUSTRALIA_1994 = new SRM_SRF_Code(_SRF_GEOCENTRIC_DATUM_AUSTRALIA_1994,
            "SRF_GEOCENTRIC_DATUM_AUSTRALIA_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_GEOCENTRIC_WGS_1984 = new SRM_SRF_Code(_SRF_GEOCENTRIC_WGS_1984,
            "SRF_GEOCENTRIC_WGS_1984");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_GEODETIC_AUSTRALIA_1984 = new SRM_SRF_Code(_SRF_GEODETIC_AUSTRALIA_1984,
            "SRF_GEODETIC_AUSTRALIA_1984");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_GEODETIC_EUROPE_1950 = new SRM_SRF_Code(_SRF_GEODETIC_EUROPE_1950,
            "SRF_GEODETIC_EUROPE_1950");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_GEODETIC_N_AMERICAN_1983 = new SRM_SRF_Code(_SRF_GEODETIC_N_AMERICAN_1983,
            "SRF_GEODETIC_N_AMERICAN_1983");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_GEODETIC_WGS_1984 = new SRM_SRF_Code(_SRF_GEODETIC_WGS_1984,
            "SRF_GEODETIC_WGS_1984");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_IRISH_GRID_1965 = new SRM_SRF_Code(_SRF_IRISH_GRID_1965,
            "SRF_IRISH_GRID_1965");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_IRISH_TRANSVERSE_MERCATOR_1989 = new SRM_SRF_Code(_SRF_IRISH_TRANSVERSE_MERCATOR_1989,
            "SRF_IRISH_TRANSVERSE_MERCATOR_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_LAMBERT_1993 = new SRM_SRF_Code(_SRF_LAMBERT_1993,
            "SRF_LAMBERT_1993");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_LAMBERT_II_WIDE = new SRM_SRF_Code(_SRF_LAMBERT_II_WIDE,
            "SRF_LAMBERT_II_WIDE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_MARS_PLANETOCENTRIC_2000 = new SRM_SRF_Code(_SRF_MARS_PLANETOCENTRIC_2000,
            "SRF_MARS_PLANETOCENTRIC_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_MARS_PLANETOGRAPHIC_2000 = new SRM_SRF_Code(_SRF_MARS_PLANETOGRAPHIC_2000,
            "SRF_MARS_PLANETOGRAPHIC_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRF_Code SRF_MARYLAND_SPCS_1983 = new SRM_SRF_Code(_SRF_MARYLAND_SPCS_1983,
            "SRF_MARYLAND_SPCS_1983");

    private SRM_SRF_Code(int code, String name) {
        super(code, name);
        _enumMap.put(name, this);
        _enumVec.add(code, this);
    }

    /// returns the SRM_SRF_Code from its enumerant value
    public static SRM_SRF_Code getEnum(int enumeration) throws SrmException {
        if ((enumeration < 1) || (enumeration > _totalEnum)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_SRF_Code.getEnum: enumerant out of range"));
        } else {
            return (SRM_SRF_Code) (_enumVec.elementAt(enumeration));
        }
    }

    /// returns the SRM_SRF_Code from its string name
    public static SRM_SRF_Code getEnum(String name) throws SrmException {
        SRM_SRF_Code retCode = (SRM_SRF_Code) _enumMap.get(name);

        if (retCode == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_SRF_Code.getEnum: enum. string not found=> " +
                            name));
        }

        return retCode;
    }
}
