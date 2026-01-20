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

/**
 * @author David Shen
 * @brief Declaration of SSM enumeration class.
 */
public class SRM_SSM_Code extends Enum {
    private static HashMap _enumMap = new HashMap();

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UNDEFINED = 0; /// Undefined

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_ALABAMA_SPCS_WEST_ZONE = 1; /// West zone

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_ALABAMA_SPCS_EAST_ZONE = 2; /// East zone

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_I = 1; /// System number I

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_II = 2; /// System number II

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_III = 3; /// System number III

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_IV = 4; /// System number IV

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_V = 5; /// System number V

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_VI = 6; /// System number VI

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_VII = 7; /// System number VII

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_VIII = 8; /// System number VIII

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_IX = 9; /// System number IX

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_X = 10; /// System number X

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_XI = 11; /// System number XI

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_XII = 12; /// System number XII

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_XIII = 13; /// System number XIII

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_XIV = 14; /// System number XIV

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_XV = 15; /// System number XV

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_XVI = 16; /// System number XVI

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_XVII = 17; /// System number XVII

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_XVIII = 18; /// System number XVIII

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_JAPAN_PLANE_SY_XIX = 19; /// System number XIX

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_LAMBERT_NTF_ZONE_I = 1; /// Zone I

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_LAMBERT_NTF_ZONE_II = 2; /// Zone II

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_LAMBERT_NTF_ZONE_III = 3; /// Zone III

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_LAMBERT_NTF_ZONE_IV = 4; /// Zone IV

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UPS_NORTHERN_POLE = 1; /// UPS, northern pole

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UPS_SOUTHERN_POLE = 2; /// UPS, southern pole

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_1_NORTHERN_HEMISPHERE = 1; /// UTM Zone 1, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_2_NORTHERN_HEMISPHERE = 2; /// UTM Zone 2, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_3_NORTHERN_HEMISPHERE = 3; /// UTM Zone 3, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_4_NORTHERN_HEMISPHERE = 4; /// UTM Zone 4, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_5_NORTHERN_HEMISPHERE = 5; /// UTM Zone 5, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_6_NORTHERN_HEMISPHERE = 6; /// UTM Zone 6, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_7_NORTHERN_HEMISPHERE = 7; /// UTM Zone 7, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_8_NORTHERN_HEMISPHERE = 8; /// UTM Zone 8, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_9_NORTHERN_HEMISPHERE = 9; /// UTM Zone 9, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_10_NORTHERN_HEMISPHERE = 10; /// UTM Zone 10, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_11_NORTHERN_HEMISPHERE = 11; /// UTM Zone 11, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_12_NORTHERN_HEMISPHERE = 12; /// UTM Zone 12, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_13_NORTHERN_HEMISPHERE = 13; /// UTM Zone 13, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_14_NORTHERN_HEMISPHERE = 14; /// UTM Zone 14, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_15_NORTHERN_HEMISPHERE = 15; /// UTM Zone 15, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_16_NORTHERN_HEMISPHERE = 16; /// UTM Zone 16, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_17_NORTHERN_HEMISPHERE = 17; /// UTM Zone 17, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_18_NORTHERN_HEMISPHERE = 18; /// UTM Zone 18, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_19_NORTHERN_HEMISPHERE = 19; /// UTM Zone 19, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_20_NORTHERN_HEMISPHERE = 20; /// UTM Zone 20, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_21_NORTHERN_HEMISPHERE = 21; /// UTM Zone 21, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_22_NORTHERN_HEMISPHERE = 22; /// UTM Zone 22, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_23_NORTHERN_HEMISPHERE = 23; /// UTM Zone 23, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_24_NORTHERN_HEMISPHERE = 24; /// UTM Zone 24, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_25_NORTHERN_HEMISPHERE = 25; /// UTM Zone 25, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_26_NORTHERN_HEMISPHERE = 26; /// UTM Zone 26, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_27_NORTHERN_HEMISPHERE = 27; /// UTM Zone 27, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_28_NORTHERN_HEMISPHERE = 28; /// UTM Zone 28, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_29_NORTHERN_HEMISPHERE = 29; /// UTM Zone 29, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_30_NORTHERN_HEMISPHERE = 30; /// UTM Zone 30, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_31_NORTHERN_HEMISPHERE = 31; /// UTM Zone 31, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_32_NORTHERN_HEMISPHERE = 32; /// UTM Zone 32, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_33_NORTHERN_HEMISPHERE = 33; /// UTM Zone 33, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_34_NORTHERN_HEMISPHERE = 34; /// UTM Zone 34, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_35_NORTHERN_HEMISPHERE = 35; /// UTM Zone 35, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_36_NORTHERN_HEMISPHERE = 36; /// UTM Zone 36, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_37_NORTHERN_HEMISPHERE = 37; /// UTM Zone 37, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_38_NORTHERN_HEMISPHERE = 38; /// UTM Zone 38, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_39_NORTHERN_HEMISPHERE = 39; /// UTM Zone 39, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_40_NORTHERN_HEMISPHERE = 40; /// UTM Zone 40, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_41_NORTHERN_HEMISPHERE = 41; /// UTM Zone 41, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_42_NORTHERN_HEMISPHERE = 42; /// UTM Zone 42, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_43_NORTHERN_HEMISPHERE = 43; /// UTM Zone 43, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_44_NORTHERN_HEMISPHERE = 44; /// UTM Zone 44, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_45_NORTHERN_HEMISPHERE = 45; /// UTM Zone 45, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_46_NORTHERN_HEMISPHERE = 46; /// UTM Zone 46, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_47_NORTHERN_HEMISPHERE = 47; /// UTM Zone 47, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_48_NORTHERN_HEMISPHERE = 48; /// UTM Zone 48, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_49_NORTHERN_HEMISPHERE = 49; /// UTM Zone 49, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_50_NORTHERN_HEMISPHERE = 50; /// UTM Zone 50, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_51_NORTHERN_HEMISPHERE = 51; /// UTM Zone 51, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_52_NORTHERN_HEMISPHERE = 52; /// UTM Zone 52, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_53_NORTHERN_HEMISPHERE = 53; /// UTM Zone 53, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_54_NORTHERN_HEMISPHERE = 54; /// UTM Zone 54, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_55_NORTHERN_HEMISPHERE = 55; /// UTM Zone 55, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_56_NORTHERN_HEMISPHERE = 56; /// UTM Zone 56, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_57_NORTHERN_HEMISPHERE = 57; /// UTM Zone 57, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_58_NORTHERN_HEMISPHERE = 58; /// UTM Zone 58, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_59_NORTHERN_HEMISPHERE = 59; /// UTM Zone 59, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_60_NORTHERN_HEMISPHERE = 60; /// UTM Zone 60, northern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_1_SOUTHERN_HEMISPHERE = 61; /// UTM Zone 1, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_2_SOUTHERN_HEMISPHERE = 62; /// UTM Zone 2, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_3_SOUTHERN_HEMISPHERE = 63; /// UTM Zone 3, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_4_SOUTHERN_HEMISPHERE = 64; /// UTM Zone 4, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_5_SOUTHERN_HEMISPHERE = 65; /// UTM Zone 5, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_6_SOUTHERN_HEMISPHERE = 66; /// UTM Zone 6, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_7_SOUTHERN_HEMISPHERE = 67; /// UTM Zone 7, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_8_SOUTHERN_HEMISPHERE = 68; /// UTM Zone 8, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_9_SOUTHERN_HEMISPHERE = 69; /// UTM Zone 9, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_10_SOUTHERN_HEMISPHERE = 70; /// UTM Zone 10, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_11_SOUTHERN_HEMISPHERE = 71; /// UTM Zone 11, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_12_SOUTHERN_HEMISPHERE = 72; /// UTM Zone 12, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_13_SOUTHERN_HEMISPHERE = 73; /// UTM Zone 13, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_14_SOUTHERN_HEMISPHERE = 74; /// UTM Zone 14, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_15_SOUTHERN_HEMISPHERE = 75; /// UTM Zone 15, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_16_SOUTHERN_HEMISPHERE = 76; /// UTM Zone 16, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_17_SOUTHERN_HEMISPHERE = 77; /// UTM Zone 17, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_18_SOUTHERN_HEMISPHERE = 78; /// UTM Zone 18, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_19_SOUTHERN_HEMISPHERE = 79; /// UTM Zone 19, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_20_SOUTHERN_HEMISPHERE = 80; /// UTM Zone 20, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_21_SOUTHERN_HEMISPHERE = 81; /// UTM Zone 21, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_22_SOUTHERN_HEMISPHERE = 82; /// UTM Zone 22, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_23_SOUTHERN_HEMISPHERE = 83; /// UTM Zone 23, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_24_SOUTHERN_HEMISPHERE = 84; /// UTM Zone 24, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_25_SOUTHERN_HEMISPHERE = 85; /// UTM Zone 25, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_26_SOUTHERN_HEMISPHERE = 86; /// UTM Zone 26, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_27_SOUTHERN_HEMISPHERE = 87; /// UTM Zone 27, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_28_SOUTHERN_HEMISPHERE = 88; /// UTM Zone 28, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_29_SOUTHERN_HEMISPHERE = 89; /// UTM Zone 29, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_30_SOUTHERN_HEMISPHERE = 90; /// UTM Zone 30, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_31_SOUTHERN_HEMISPHERE = 91; /// UTM Zone 31, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_32_SOUTHERN_HEMISPHERE = 92; /// UTM Zone 32, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_33_SOUTHERN_HEMISPHERE = 93; /// UTM Zone 33, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_34_SOUTHERN_HEMISPHERE = 94; /// UTM Zone 34, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_35_SOUTHERN_HEMISPHERE = 95; /// UTM Zone 35, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_36_SOUTHERN_HEMISPHERE = 96; /// UTM Zone 36, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_37_SOUTHERN_HEMISPHERE = 97; /// UTM Zone 37, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_38_SOUTHERN_HEMISPHERE = 98; /// UTM Zone 38, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_39_SOUTHERN_HEMISPHERE = 99; /// UTM Zone 39, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_40_SOUTHERN_HEMISPHERE = 100; /// UTM Zone 40, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_41_SOUTHERN_HEMISPHERE = 101; /// UTM Zone 41, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_42_SOUTHERN_HEMISPHERE = 102; /// UTM Zone 42, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_43_SOUTHERN_HEMISPHERE = 103; /// UTM Zone 43, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_44_SOUTHERN_HEMISPHERE = 104; /// UTM Zone 44, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_45_SOUTHERN_HEMISPHERE = 105; /// UTM Zone 45, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_46_SOUTHERN_HEMISPHERE = 106; /// UTM Zone 46, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_47_SOUTHERN_HEMISPHERE = 107; /// UTM Zone 47, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_48_SOUTHERN_HEMISPHERE = 108; /// UTM Zone 48, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_49_SOUTHERN_HEMISPHERE = 109; /// UTM Zone 49, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_50_SOUTHERN_HEMISPHERE = 110; /// UTM Zone 50, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_51_SOUTHERN_HEMISPHERE = 111; /// UTM Zone 51, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_52_SOUTHERN_HEMISPHERE = 112; /// UTM Zone 52, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_53_SOUTHERN_HEMISPHERE = 113; /// UTM Zone 53, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_54_SOUTHERN_HEMISPHERE = 114; /// UTM Zone 54, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_55_SOUTHERN_HEMISPHERE = 115; /// UTM Zone 55, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_56_SOUTHERN_HEMISPHERE = 116; /// UTM Zone 56, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_57_SOUTHERN_HEMISPHERE = 117; /// UTM Zone 57, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_58_SOUTHERN_HEMISPHERE = 118; /// UTM Zone 58, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_59_SOUTHERN_HEMISPHERE = 119; /// UTM Zone 59, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_UTM_ZONE_60_SOUTHERN_HEMISPHERE = 120; /// UTM Zone 60, southern hemisphere

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_WISCONSIN_SPCS_SOUTH_ZONE = 1; /// South zone

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_WISCONSIN_SPCS_CENTRAL_ZONE = 2; /// Central zone

    /**
     * DOCUMENT ME!
     */
    public static final int _SSM_WISCONSIN_SPCS_NORTH_ZONE = 3; /// North zone

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UNDEFINED = new SRM_SSM_Code(_SSM_UNDEFINED,
            "SSM_UNDEFINED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_ALABAMA_SPCS_WEST_ZONE = new SRM_SSM_Code(_SSM_ALABAMA_SPCS_WEST_ZONE,
            "SSM_ALABAMA_SPCS_WEST_ZONE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_ALABAMA_SPCS_EAST_ZONE = new SRM_SSM_Code(_SSM_ALABAMA_SPCS_EAST_ZONE,
            "SSM_ALABAMA_SPCS_EAST_ZONE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_I = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_I,
            "SSM_JAPAN_PLANE_SY_I");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_II = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_II,
            "SSM_JAPAN_PLANE_SY_II");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_III = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_III,
            "SSM_JAPAN_PLANE_SY_III");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_IV = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_IV,
            "SSM_JAPAN_PLANE_SY_IV");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_V = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_V,
            "SSM_JAPAN_PLANE_SY_V");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_VI = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_VI,
            "SSM_JAPAN_PLANE_SY_VI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_VII = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_VII,
            "SSM_JAPAN_PLANE_SY_VII");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_VIII = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_VIII,
            "SSM_JAPAN_PLANE_SY_VIII");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_IX = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_IX,
            "SSM_JAPAN_PLANE_SY_IX");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_X = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_X,
            "SSM_JAPAN_PLANE_SY_X");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_XI = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_XI,
            "SSM_JAPAN_PLANE_SY_XI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_XII = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_XII,
            "SSM_JAPAN_PLANE_SY_XII");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_XIII = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_XIII,
            "SSM_JAPAN_PLANE_SY_XIII");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_XIV = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_XIV,
            "SSM_JAPAN_PLANE_SY_XIV");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_XV = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_XV,
            "SSM_JAPAN_PLANE_SY_XV");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_XVI = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_XVI,
            "SSM_JAPAN_PLANE_SY_XVI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_XVII = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_XVII,
            "SSM_JAPAN_PLANE_SY_XVII");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_XVIII = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_XVIII,
            "SSM_JAPAN_PLANE_SY_XVIII");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_JAPAN_PLANE_SY_XIX = new SRM_SSM_Code(_SSM_JAPAN_PLANE_SY_XIX,
            "SSM_JAPAN_PLANE_SY_XIX");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_LAMBERT_NTF_ZONE_I = new SRM_SSM_Code(_SSM_LAMBERT_NTF_ZONE_I,
            "SSM_LAMBERT_NTF_ZONE_I");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_LAMBERT_NTF_ZONE_II = new SRM_SSM_Code(_SSM_LAMBERT_NTF_ZONE_II,
            "SSM_LAMBERT_NTF_ZONE_II");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_LAMBERT_NTF_ZONE_III = new SRM_SSM_Code(_SSM_LAMBERT_NTF_ZONE_III,
            "SSM_LAMBERT_NTF_ZONE_III");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_LAMBERT_NTF_ZONE_IV = new SRM_SSM_Code(_SSM_LAMBERT_NTF_ZONE_IV,
            "SSM_LAMBERT_NTF_ZONE_IV");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UPS_NORTHERN_POLE = new SRM_SSM_Code(_SSM_UPS_NORTHERN_POLE,
            "SSM_UPS_NORTHERN_POLE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UPS_SOUTHERN_POLE = new SRM_SSM_Code(_SSM_UPS_SOUTHERN_POLE,
            "SSM_UPS_SOUTHERN_POLE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_1_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_1_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_1_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_2_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_2_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_2_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_3_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_3_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_3_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_4_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_4_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_4_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_5_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_5_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_5_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_6_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_6_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_6_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_7_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_7_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_7_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_8_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_8_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_8_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_9_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_9_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_9_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_10_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_10_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_10_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_11_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_11_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_11_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_12_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_12_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_12_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_13_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_13_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_13_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_14_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_14_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_14_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_15_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_15_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_15_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_16_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_16_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_16_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_17_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_17_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_17_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_18_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_18_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_18_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_19_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_19_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_19_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_20_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_20_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_20_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_21_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_21_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_21_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_22_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_22_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_22_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_23_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_23_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_23_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_24_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_24_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_24_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_25_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_25_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_25_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_26_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_26_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_26_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_27_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_27_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_27_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_28_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_28_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_28_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_29_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_29_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_29_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_30_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_30_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_30_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_31_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_31_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_31_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_32_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_32_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_32_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_33_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_33_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_33_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_34_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_34_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_34_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_35_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_35_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_35_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_36_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_36_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_36_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_37_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_37_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_37_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_38_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_38_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_38_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_39_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_39_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_39_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_40_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_40_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_40_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_41_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_41_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_41_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_42_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_42_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_42_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_43_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_43_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_43_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_44_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_44_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_44_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_45_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_45_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_45_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_46_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_46_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_46_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_47_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_47_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_47_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_48_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_48_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_48_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_49_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_49_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_49_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_50_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_50_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_50_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_51_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_51_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_51_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_52_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_52_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_52_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_53_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_53_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_53_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_54_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_54_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_54_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_55_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_55_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_55_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_56_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_56_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_56_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_57_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_57_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_57_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_58_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_58_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_58_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_59_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_59_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_59_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_60_NORTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_60_NORTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_60_NORTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_1_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_1_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_1_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_2_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_2_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_2_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_3_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_3_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_3_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_4_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_4_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_4_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_5_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_5_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_5_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_6_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_6_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_6_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_7_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_7_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_7_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_8_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_8_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_8_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_9_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_9_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_9_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_10_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_10_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_10_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_11_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_11_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_11_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_12_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_12_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_12_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_13_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_13_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_13_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_14_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_14_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_14_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_15_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_15_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_15_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_16_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_16_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_16_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_17_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_17_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_17_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_18_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_18_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_18_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_19_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_19_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_19_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_20_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_20_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_20_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_21_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_21_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_21_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_22_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_22_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_22_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_23_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_23_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_23_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_24_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_24_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_24_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_25_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_25_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_25_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_26_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_26_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_26_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_27_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_27_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_27_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_28_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_28_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_28_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_29_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_29_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_29_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_30_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_30_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_30_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_31_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_31_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_31_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_32_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_32_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_32_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_33_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_33_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_33_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_34_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_34_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_34_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_35_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_35_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_35_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_36_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_36_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_36_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_37_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_37_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_37_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_38_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_38_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_38_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_39_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_39_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_39_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_40_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_40_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_40_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_41_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_41_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_41_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_42_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_42_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_42_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_43_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_43_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_43_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_44_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_44_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_44_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_45_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_45_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_45_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_46_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_46_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_46_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_47_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_47_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_47_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_48_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_48_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_48_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_49_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_49_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_49_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_50_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_50_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_50_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_51_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_51_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_51_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_52_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_52_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_52_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_53_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_53_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_53_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_54_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_54_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_54_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_55_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_55_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_55_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_56_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_56_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_56_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_57_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_57_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_57_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_58_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_58_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_58_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_59_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_59_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_59_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_UTM_ZONE_60_SOUTHERN_HEMISPHERE = new SRM_SSM_Code(_SSM_UTM_ZONE_60_SOUTHERN_HEMISPHERE,
            "SSM_UTM_ZONE_60_SOUTHERN_HEMISPHERE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_WISCONSIN_SPCS_SOUTH_ZONE = new SRM_SSM_Code(_SSM_WISCONSIN_SPCS_SOUTH_ZONE,
            "SSM_WISCONSIN_SPCS_SOUTH_ZONE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_WISCONSIN_SPCS_CENTRAL_ZONE = new SRM_SSM_Code(_SSM_WISCONSIN_SPCS_CENTRAL_ZONE,
            "SSM_WISCONSIN_SPCS_CENTRAL_ZONE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_SSM_Code SSM_WISCONSIN_SPCS_NORTH_ZONE = new SRM_SSM_Code(_SSM_WISCONSIN_SPCS_NORTH_ZONE,
            "SSM_WISCONSIN_SPCS_NORTH_ZONE");

    private SRM_SSM_Code(int code, String name) {
        super(code, name);
        _enumMap.put(name, this);
    }

    /// returns the SRM_SSM_Code from its string name
    public static SRM_SSM_Code getEnum(String name) throws SrmException {
        SRM_SSM_Code retCode = (SRM_SSM_Code) _enumMap.get(name);

        if (retCode == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_SSM_Code.getEnum: enum. string not found=> " +
                            name));
        }

        return retCode;
    }
}
