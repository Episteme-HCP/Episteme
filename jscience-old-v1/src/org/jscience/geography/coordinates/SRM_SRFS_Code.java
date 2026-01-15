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
 * @brief Declaration of SRF Set enumeration class.
 */
public class SRM_SRFS_Code extends Enum {
    /**
     * DOCUMENT ME!
     */
    public static final int _totalEnum = 7;
    private static Vector _enumVec = new Vector();
    private static HashMap _enumMap = new HashMap();

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFS_UNDEFINED = 0; /// Undefined

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFS_ALABAMA_SPCS = 1; /// Alabama (US) State Plane Coordinate System

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFS_GTRS_GLOBAL_COORDINATE_SYSTEM = 2; /// GTRS global coordinate system (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFS_JAPAN_RECTANGULAR_PLANE_COORDINATE_SYSTEM = 3; /// Japan Rectangular Plane Coordinate System

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFS_LAMBERT_NTF = 4; /// Lambert NTF

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFS_UNIVERSAL_POLAR_STEREOGRAPHIC = 5; /// Universal Polar Stereographic (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFS_UNIVERSAL_TRANSVERSE_MERCATOR = 6; /// Universal Transverse Mercator (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _SRFS_WISCONSIN_SPCS = 7; /// Wisconsin (US) State Plane Coordinate System

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFS_Code SRFS_UNDEFINED = new SRM_SRFS_Code(_SRFS_UNDEFINED,
            "SRFS_UNDEFINED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFS_Code SRFS_ALABAMA_SPCS = new SRM_SRFS_Code(_SRFS_ALABAMA_SPCS,
            "SRFS_ALABAMA_SPCS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFS_Code SRFS_GTRS_GLOBAL_COORDINATE_SYSTEM = new SRM_SRFS_Code(_SRFS_GTRS_GLOBAL_COORDINATE_SYSTEM,
            "SRFS_GTRS_GLOBAL_COORDINATE_SYSTEM");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFS_Code SRFS_JAPAN_RECTANGULAR_PLANE_COORDINATE_SYSTEM =
            new SRM_SRFS_Code(_SRFS_JAPAN_RECTANGULAR_PLANE_COORDINATE_SYSTEM,
                    "SRFS_JAPAN_RECTANGULAR_PLANE_COORDINATE_SYSTEM");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFS_Code SRFS_LAMBERT_NTF = new SRM_SRFS_Code(_SRFS_LAMBERT_NTF,
            "SRFS_LAMBERT_NTF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFS_Code SRFS_UNIVERSAL_POLAR_STEREOGRAPHIC = new SRM_SRFS_Code(_SRFS_UNIVERSAL_POLAR_STEREOGRAPHIC,
            "SRFS_UNIVERSAL_POLAR_STEREOGRAPHIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFS_Code SRFS_UNIVERSAL_TRANSVERSE_MERCATOR = new SRM_SRFS_Code(_SRFS_UNIVERSAL_TRANSVERSE_MERCATOR,
            "SRFS_UNIVERSAL_TRANSVERSE_MERCATOR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SRFS_Code SRFS_WISCONSIN_SPCS = new SRM_SRFS_Code(_SRFS_WISCONSIN_SPCS,
            "SRFS_WISCONSIN_SPCS");

    private SRM_SRFS_Code(int code, String name) {
        super(code, name);
        _enumMap.put(name, this);
        _enumVec.add(code, this);
    }

    /// returns the SRM_SRFS_Code from its enumerant value
    public static SRM_SRFS_Code getEnum(int enumeration) throws SrmException {
        if ((enumeration < 1) || (enumeration > _totalEnum)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_SRFS_Code.getEnum: enumerant out of range"));
        } else {
            return (SRM_SRFS_Code) (_enumVec.elementAt(enumeration));
        }
    }

    /// returns the SRM_SRFS_Code from its string name
    public static SRM_SRFS_Code getEnum(String name) throws SrmException {
        SRM_SRFS_Code retCode = (SRM_SRFS_Code) _enumMap.get(name);

        if (retCode == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_SRFS_Code.getEnum: enum. string not found=> " +
                            name));
        }

        return retCode;
    }
}
