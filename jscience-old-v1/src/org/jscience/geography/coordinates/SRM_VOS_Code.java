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
 * @brief Declaration of SRM Vertical Offset Surface enumeration class
 */
public class SRM_VOS_Code extends Enum {
    /**
     * DOCUMENT ME!
     */
    public static final int _totalEnum = 9;
    private static Vector _enumVec = new Vector();
    private static HashMap _enumMap = new HashMap();

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_UNDEFINED = 0; /// Undefined

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_EGM96_GEOID = 1; /// WGS 84 EGM96 geoid

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_IGLD_1955 = 2; /// International Great Lakes Datum (IGLD) 1955

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_IGLD_1985 = 3; /// International Great Lakes Datum (IGLD) 1985

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_MSL = 4; /// Mean sea level (MSL)

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_NAVD_1988 = 5; /// North American Vertical Datum (NAVD) 1988

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_NGVD_1929 = 6; /// National Geodetic Vertical Datum (NGVD) 1929

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_OSGM_2002 = 7; /// Ordnance Survey Geoid Model (OSGM) 2002

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_WGS84_ELLIPSOID = 8; /// WGS 84 ellipsoid

    /**
     * DOCUMENT ME!
     */
    public static final int _VOS_WGS84_GEOID = 9; /// WGS 84 geoid

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_UNDEFINED = new SRM_VOS_Code(_VOS_UNDEFINED,
            "UNDEFINED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_EGM96_GEOID = new SRM_VOS_Code(_VOS_EGM96_GEOID,
            "EGM96_GEOID");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_IGLD_1955 = new SRM_VOS_Code(_VOS_IGLD_1955,
            "IGLD_1955");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_IGLD_1985 = new SRM_VOS_Code(_VOS_IGLD_1985,
            "IGLD_1985");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_MSL = new SRM_VOS_Code(_VOS_MSL, "MSL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_NAVD_1988 = new SRM_VOS_Code(_VOS_NAVD_1988,
            "NAVD_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_NGVD_1929 = new SRM_VOS_Code(_VOS_NGVD_1929,
            "NGVD_1929");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_OSGM_2002 = new SRM_VOS_Code(_VOS_OSGM_2002,
            "OSGM_2002");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_WGS84_ELLIPSOID = new SRM_VOS_Code(_VOS_WGS84_ELLIPSOID,
            "WGS84_ELLIPSOID");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_VOS_Code VOS_WGS84_GEOID = new SRM_VOS_Code(_VOS_WGS84_GEOID,
            "WGS84_GEOID");

    private SRM_VOS_Code(int code, String name) {
        super(code, name);
        _enumMap.put(name, this);
        _enumVec.add(code, this);
    }

    /// returns the SRM_VOS_Code from its enumerant value
    public static SRM_VOS_Code getEnum(int enumeration) throws SrmException {
        if ((enumeration < 1) || (enumeration > _totalEnum)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_VOS_Code.getEnum: enumerant out of range"));
        } else {
            return (SRM_VOS_Code) (_enumVec.elementAt(enumeration));
        }
    }

    /// returns the SRM_VOS_Code from its string name
    public static SRM_VOS_Code getEnum(String name) throws SrmException {
        SRM_VOS_Code retCode = (SRM_VOS_Code) _enumMap.get(name);

        if (retCode == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_VOS_Code.getEnum: enum. string not found=> " +
                            name));
        }

        return retCode;
    }
}
