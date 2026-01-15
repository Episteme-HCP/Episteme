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

/**
 * @author David Shen
 * @brief Declaration of coordinate type enumeration class.
 */
class Coord_ClassType extends Enum {
    /**
     * DOCUMENT ME!
     */
    public static final int _AZ = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int _CC = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int _CD = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_CD = 4;

    /**
     * DOCUMENT ME!
     */
    public static final int _PD = 5;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_PD = 6;

    /**
     * DOCUMENT ME!
     */
    public static final int _CM = 7;

    /**
     * DOCUMENT ME!
     */
    public static final int _EC = 8;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_EC = 9;

    /**
     * DOCUMENT ME!
     */
    public static final int _EI = 10;

    /**
     * DOCUMENT ME!
     */
    public static final int _HAEC = 11;

    /**
     * DOCUMENT ME!
     */
    public static final int _HEEC = 12;

    /**
     * DOCUMENT ME!
     */
    public static final int _HEEQ = 13;

    /**
     * DOCUMENT ME!
     */
    public static final int _LCC = 14;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_LCC = 15;

    /**
     * DOCUMENT ME!
     */
    public static final int _LSR_3D = 16;

    /**
     * DOCUMENT ME!
     */
    public static final int _LSR_2D = 17;

    /**
     * DOCUMENT ME!
     */
    public static final int _LTSAS = 18;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_LTSAS = 19;

    /**
     * DOCUMENT ME!
     */
    public static final int _LTSC = 20;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_LTSC = 21;

    /**
     * DOCUMENT ME!
     */
    public static final int _LTSE = 22;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_LTSE = 23;

    /**
     * DOCUMENT ME!
     */
    public static final int _MERCATOR = 24;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_Mercator = 25;

    /**
     * DOCUMENT ME!
     */
    public static final int _OMS = 26;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_OMS = 27;

    /**
     * DOCUMENT ME!
     */
    public static final int _POLAR = 28;

    /**
     * DOCUMENT ME!
     */
    public static final int _PS = 29;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_PS = 30;

    /**
     * DOCUMENT ME!
     */
    public static final int _SEC = 31;

    /**
     * DOCUMENT ME!
     */
    public static final int _SEQ = 32;

    /**
     * DOCUMENT ME!
     */
    public static final int _SMD = 33;

    /**
     * DOCUMENT ME!
     */
    public static final int _SME = 34;

    /**
     * DOCUMENT ME!
     */
    public static final int _TM = 35;

    /**
     * DOCUMENT ME!
     */
    public static final int _SURF_TM = 36;

    /**
     * DOCUMENT ME!
     */
    public static final int _totalEnum = 34;

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType AZ = new Coord_ClassType(_AZ, "AZ");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType CC = new Coord_ClassType(_CC, "CC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType CD = new Coord_ClassType(_CD, "CD");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_CD = new Coord_ClassType(_SURF_CD,
            "SURF_CD");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType PD = new Coord_ClassType(_PD, "PD");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_PD = new Coord_ClassType(_SURF_PD,
            "SURF_PD");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType CM = new Coord_ClassType(_CM, "CM");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType EC = new Coord_ClassType(_EC, "EC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_EC = new Coord_ClassType(_SURF_EC,
            "SURF_EC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType EI = new Coord_ClassType(_EI, "EI");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType HAEC = new Coord_ClassType(_HAEC, "HAEC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType HEEC = new Coord_ClassType(_HEEC, "HEEC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType HEEQ = new Coord_ClassType(_HEEQ, "HEEQ");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType LCC = new Coord_ClassType(_LCC, "LCC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_LCC = new Coord_ClassType(_SURF_LCC,
            "SURF_LCC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType LSR_2D = new Coord_ClassType(_LSR_2D,
            "LSR_2D");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType LSR_3D = new Coord_ClassType(_LSR_3D,
            "LSR_3D");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType LTSAS = new Coord_ClassType(_LTSAS,
            "LTSAS");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_LTSAS = new Coord_ClassType(_SURF_LTSAS,
            "SURF_LTSAS");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType LTSC = new Coord_ClassType(_LTSC, "LTSC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_LTSC = new Coord_ClassType(_SURF_LTSC,
            "SURF_LTSC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType LTSE = new Coord_ClassType(_LTSE, "LTSE");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_LTSE = new Coord_ClassType(_SURF_LTSE,
            "SURF_LTSE");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType MERCATOR = new Coord_ClassType(_MERCATOR,
            "MERCATOR");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_Mercator = new Coord_ClassType(_SURF_Mercator,
            "SURF_Mercator");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType OMS = new Coord_ClassType(_OMS, "OMS");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_OMS = new Coord_ClassType(_SURF_OMS,
            "SURF_OMS");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType POLAR = new Coord_ClassType(_POLAR,
            "POLAR");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType PS = new Coord_ClassType(_PS, "PS");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_PS = new Coord_ClassType(_SURF_PS,
            "SURF_PS");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SEC = new Coord_ClassType(_SEC, "SEC");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SEQ = new Coord_ClassType(_SEQ, "SEQ");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SMD = new Coord_ClassType(_SMD, "SMD");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SME = new Coord_ClassType(_SME, "SME");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType TM = new Coord_ClassType(_TM, "TM");

    /**
     * DOCUMENT ME!
     */
    public static final Coord_ClassType SURF_TM = new Coord_ClassType(_SURF_TM,
            "SURF_TM");

    private Coord_ClassType(int code, String name) {
        super(code, name);
    }
}
