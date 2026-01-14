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
package org.jscience.geography.coordinates;

import java.util.Vector;

/**
 * @author David Shen
 * @brief Declaration of RD enumeration class.
 */
public class SRM_RD_Code extends Enum {
    /**
     * DOCUMENT ME!
     */
    public static final int _totalEnum = 146;
    private static Vector _enumVec = new Vector();

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_UNDEFINED = 0; /// Undefined

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_ORIGIN_2D = 1; /// Origin in 2D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_X_UNIT_POINT_2D = 2; /// x-axis unit point in 2D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_Y_UNIT_POINT_2D = 3; /// y-axis unit point in 2D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_ORIGIN_3D = 4; /// Origin in 3D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_X_UNIT_POINT_3D = 5; /// x-axis unit point in 3D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_Y_UNIT_POINT_3D = 6; /// y-axis unit point in 3D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_Z_UNIT_POINT_3D = 7; /// z-axis unit point in 3D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_X_AXIS_2D = 8; /// x-axis in 2D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_Y_AXIS_2D = 9; /// y-axis in 2D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_X_AXIS_3D = 10; /// x-axis in 3D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_Y_AXIS_3D = 11; /// y-axis in 3D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_Z_AXIS_3D = 12; /// z-axis in 3D

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_XY_PLANE_3D = 13; /// xy-plane

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_XZ_PLANE_3D = 14; /// xz-plane

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_YZ_PLANE_3D = 15; /// yz-plane

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_ADRASTEA_2000 = 16; /// Adrastea (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_AIRY_1830 = 17; /// Airy

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_AMALTHEA_2000 = 18; /// Amalthea (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_ANANKE_1988 = 19; /// Ananke (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_APL_4r5_1968 = 20; /// APL 4.5

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_ARIEL_1988 = 21; /// Ariel (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_ATLAS_1988 = 22; /// Atlas (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_AUSTRALIAN_NATIONAL_1966 = 23; /// Australian National

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_AVERAGE_TERRESTRIAL_1977 = 24; /// Average Terrestrial System

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_BELINDA_1988 = 25; /// Belinda (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_BESSEL_1841_ETHIOPIA = 26; /// Bessel (Ethiopia, Indonesia, Japan, and Korea)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_BESSEL_1841_NAMIBIA = 27; /// Bessel (Namibia)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_BIANCA_1988 = 28; /// Bianca (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CALLISTO_2000 = 29; /// Callisto (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CALYPSO_1988 = 30; /// Calypso (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CARME_1988 = 31; /// Carme (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CHARON_1991 = 32; /// Charon (satellite of Pluto)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CLARKE_1858 = 33; /// Clarke

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CLARKE_1858_MODIFIED = 34; /// Clarke - Modified

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CLARKE_1866 = 35; /// Clarke

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CLARKE_1880 = 36; /// Clarke

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CLARKE_1880_CAPE = 37; /// Clarke - Cape

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CLARKE_1880_FIJI = 38; /// Clarke - Fiji

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CLARKE_1880_IGN = 39; /// Clarke - IGN

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CLARKE_1880_PALESTINE = 40; /// Clarke - Palestine

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CLARKE_1880_SYRIA = 41; /// Clarke - Syria

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_COAMPS_1998 = 42; /// COAMPS^(TM)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CORDELIA_1988 = 43; /// Cordelia (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_CRESSIDA_1988 = 44; /// Cressida (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_DANISH_1876 = 45; /// Danish - Andrae

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_DEIMOS_1988 = 46; /// Deimos (satellite of Mars)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_DELAMBRE_1810 = 47; /// Delambre

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_DESDEMONA_1988 = 48; /// Desdemona (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_DESPINA_1991 = 49; /// Despina (satellite of Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_DIONE_1982 = 50; /// Dione (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_ELARA_1988 = 51; /// Elara (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_ENCELADUS_1994 = 52; /// Enceladus (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_EPIMETHEUS_1988 = 53; /// Epimetheus (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_EROS_2000 = 54; /// Eros (asteroid 433, a minor planet)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_EUROPA_2000 = 55; /// Europa (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_EVEREST_ADJ_1937 = 56; /// Everest 1830 - Adjusted

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_EVEREST_1948 = 57; /// Everest

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_EVEREST_1956 = 58; /// Everest

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_EVEREST_REVISED_1962 = 59; /// Everest 1830 - Revised definition

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_EVEREST_1969 = 60; /// Everest

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_EVEREST_BRUNEI_1967 = 61; /// Everest 1830 - 1967 definition (Brunei and East Malaysia - Sabah and Sarawak)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_FISCHER_1960 = 62; /// Fischer - Mercury

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_FISCHER_1968 = 63; /// Fischer

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_GALATEA_1991 = 64; /// Galatea (satellite of Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_GANYMEDE_2000 = 65; /// Ganymede (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_GASPRA_1991 = 66; /// Gaspra (asteroid 951, a minor planet)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_GRS_1967 = 67; /// GRS

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_GRS_1980 = 68; /// GRS

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_HELENE_1992 = 69; /// Helene (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_HELMERT_1906 = 70; /// Helmert

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_HIMALIA_1988 = 71; /// Himalia (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_HOUGH_1960 = 72; /// Hough

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_HYPERION_2000 = 73; /// Hyperion (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_IAG_1975 = 74; /// IAG Best Estimate

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_IAPETUS_1988 = 75; /// Iapetus (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_IDA_1991 = 76; /// Ida (asteroid 293, a minor planet)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_INDONESIAN_1974 = 77; /// Indonesian

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_INTERNATIONAL_1924 = 78; /// International

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_IO_2000 = 79; /// Io (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_JANUS_1988 = 80; /// Janus (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_JULIET_1988 = 81; /// Juliet (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_JUPITER_1988 = 82; /// Jupiter

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_KLEOPATRA_2000 = 83; /// Kleopatra (asteroid 216, a minor planet)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_KRASSOVSKY_1940 = 84; /// Krassovsky

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_KRAYENHOFF_1827 = 85; /// Krayenhoff

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_LARISSA_1991 = 86; /// Larissa (satellite of Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_LEDA_1988 = 87; /// Leda (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_LYSITHEA_1988 = 88; /// Lysithea (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MARS_2000 = 89; /// Mars

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MARS_SPHERE_2000 = 90; /// Mars

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MASS_1999 = 91; /// MASS

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MERCURY_1988 = 92; /// Mercury

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_METIS_2000 = 93; /// Metis (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MIMAS_1994 = 94; /// Mimas (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MIRANDA_1988 = 95; /// Miranda (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MM5_1997 = 96; /// MM5 (AFWA)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MODIFIED_AIRY_1849 = 97; /// Modified Airy

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MODIFIED_FISCHER_1960 = 98; /// Modified Fischer

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MODTRAN_MIDLATITUDE_1989 = 99; /// MODTRAN (midlatitude regions)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MODTRAN_SUBARCTIC_1989 = 100; /// MODTRAN (subarctic regions)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MODTRAN_TROPICAL_1989 = 101; /// MODTRAN (tropical regions)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MOON_1991 = 102; /// Moon (satellite of Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_MULTIGEN_FLAT_EARTH_1989 = 103; /// Multigen Flat Earth

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_NAIAD_1991 = 104; /// Naiad (satellite of Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_NEPTUNE_1991 = 105; /// Neptune

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_NEREID_1991 = 106; /// Nereid (satellite of Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_NOGAPS_1988 = 107; /// NOGAPS

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_OBERON_1988 = 108; /// Oberon (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_OPHELIA_1988 = 109; /// Ophelia (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PAN_1991 = 110; /// Pan (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PANDORA_1988 = 111; /// Pandora (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PASIPHAE_1988 = 112; /// Pasiphae (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PHOBOS_1988 = 113; /// Phobos (satellite of Mars)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PHOEBE_1988 = 114; /// Phoebe (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PLESSIS_MODIFIED_1817 = 115; /// Plessis - Modified

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PLUTO_1994 = 116; /// Pluto

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PORTIA_1988 = 117; /// Portia (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PROMETHEUS_1988 = 118; /// Prometheus (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PROTEUS_1991 = 119; /// Proteus (satellite of Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_PUCK_1988 = 120; /// Puck (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_RHEA_1988 = 121; /// Rhea (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_ROSALIND_1988 = 122; /// Rosalind (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_SATURN_1988 = 123; /// Saturn

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_SINOPE_1988 = 124; /// Sinope (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_SOUTH_AMERICAN_1969 = 125; /// South American

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_SOVIET_GEODETIC_1985 = 126; /// Soviet Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_SOVIET_GEODETIC_1990 = 127; /// Soviet Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_STRUVE_1860 = 128; /// Struve

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_SUN_1992 = 129; /// Sun

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_TELESTO_1988 = 130; /// Telesto (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_TETHYS_1991 = 131; /// Tethys (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_THALASSA_1991 = 132; /// Thalassa (satellite of Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_THEBE_2000 = 133; /// Thebe (satellite of Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_TITAN_1982 = 134; /// Titan (satellite of Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_TITANIA_1988 = 135; /// Titania (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_TRITON_1991 = 136; /// Triton (satellite of Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_UMBRIEL_1988 = 137; /// Umbriel (satellite of Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_URANUS_1988 = 138; /// Uranus

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_VENUS_1991 = 139; /// Venus

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_WALBECK_AMS_1963 = 140; /// Walbeck 1819 - AMS

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_WALBECK_PLANHEFT_1942 = 141; /// Walbeck 1819 - Planheft

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_WAR_OFFICE_1924 = 142; /// War Office - McCaw

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_WGS_1960 = 143; /// World Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_WGS_1966 = 144; /// World Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_WGS_1984 = 145; /// World Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _RD_WGS_1972 = 146; /// World Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_UNDEFINED = new SRM_RD_Code(_RD_UNDEFINED,
            "UNDEFINED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_ORIGIN_2D = new SRM_RD_Code(_RD_ORIGIN_2D,
            "ORIGIN_2D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_X_UNIT_POINT_2D = new SRM_RD_Code(_RD_X_UNIT_POINT_2D,
            "X_UNIT_POINT_2D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_Y_UNIT_POINT_2D = new SRM_RD_Code(_RD_Y_UNIT_POINT_2D,
            "Y_UNIT_POINT_2D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_ORIGIN_3D = new SRM_RD_Code(_RD_ORIGIN_3D,
            "ORIGIN_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_X_UNIT_POINT_3D = new SRM_RD_Code(_RD_X_UNIT_POINT_3D,
            "X_UNIT_POINT_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_Y_UNIT_POINT_3D = new SRM_RD_Code(_RD_Y_UNIT_POINT_3D,
            "Y_UNIT_POINT_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_Z_UNIT_POINT_3D = new SRM_RD_Code(_RD_Z_UNIT_POINT_3D,
            "Z_UNIT_POINT_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_X_AXIS_2D = new SRM_RD_Code(_RD_X_AXIS_2D,
            "X_AXIS_2D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_Y_AXIS_2D = new SRM_RD_Code(_RD_Y_AXIS_2D,
            "Y_AXIS_2D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_X_AXIS_3D = new SRM_RD_Code(_RD_X_AXIS_3D,
            "X_AXIS_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_Y_AXIS_3D = new SRM_RD_Code(_RD_Y_AXIS_3D,
            "Y_AXIS_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_Z_AXIS_3D = new SRM_RD_Code(_RD_Z_AXIS_3D,
            "Z_AXIS_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_XY_PLANE_3D = new SRM_RD_Code(_RD_XY_PLANE_3D,
            "XY_PLANE_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_XZ_PLANE_3D = new SRM_RD_Code(_RD_XZ_PLANE_3D,
            "XZ_PLANE_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_YZ_PLANE_3D = new SRM_RD_Code(_RD_YZ_PLANE_3D,
            "YZ_PLANE_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_ADRASTEA_2000 = new SRM_RD_Code(_RD_ADRASTEA_2000,
            "ADRASTEA_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_AIRY_1830 = new SRM_RD_Code(_RD_AIRY_1830,
            "AIRY_1830");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_AMALTHEA_2000 = new SRM_RD_Code(_RD_AMALTHEA_2000,
            "AMALTHEA_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_ANANKE_1988 = new SRM_RD_Code(_RD_ANANKE_1988,
            "ANANKE_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_APL_4r5_1968 = new SRM_RD_Code(_RD_APL_4r5_1968,
            "APL_4r5_1968");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_ARIEL_1988 = new SRM_RD_Code(_RD_ARIEL_1988,
            "ARIEL_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_ATLAS_1988 = new SRM_RD_Code(_RD_ATLAS_1988,
            "ATLAS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_AUSTRALIAN_NATIONAL_1966 = new SRM_RD_Code(_RD_AUSTRALIAN_NATIONAL_1966,
            "AUSTRALIAN_NATIONAL_1966");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_AVERAGE_TERRESTRIAL_1977 = new SRM_RD_Code(_RD_AVERAGE_TERRESTRIAL_1977,
            "AVERAGE_TERRESTRIAL_1977");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_BELINDA_1988 = new SRM_RD_Code(_RD_BELINDA_1988,
            "BELINDA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_BESSEL_1841_ETHIOPIA = new SRM_RD_Code(_RD_BESSEL_1841_ETHIOPIA,
            "BESSEL_1841_ETHIOPIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_BESSEL_1841_NAMIBIA = new SRM_RD_Code(_RD_BESSEL_1841_NAMIBIA,
            "BESSEL_1841_NAMIBIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_BIANCA_1988 = new SRM_RD_Code(_RD_BIANCA_1988,
            "BIANCA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CALLISTO_2000 = new SRM_RD_Code(_RD_CALLISTO_2000,
            "CALLISTO_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CALYPSO_1988 = new SRM_RD_Code(_RD_CALYPSO_1988,
            "CALYPSO_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CARME_1988 = new SRM_RD_Code(_RD_CARME_1988,
            "CARME_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CHARON_1991 = new SRM_RD_Code(_RD_CHARON_1991,
            "CHARON_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CLARKE_1858 = new SRM_RD_Code(_RD_CLARKE_1858,
            "CLARKE_1858");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CLARKE_1858_MODIFIED = new SRM_RD_Code(_RD_CLARKE_1858_MODIFIED,
            "CLARKE_1858_MODIFIED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CLARKE_1866 = new SRM_RD_Code(_RD_CLARKE_1866,
            "CLARKE_1866");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CLARKE_1880 = new SRM_RD_Code(_RD_CLARKE_1880,
            "CLARKE_1880");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CLARKE_1880_CAPE = new SRM_RD_Code(_RD_CLARKE_1880_CAPE,
            "CLARKE_1880_CAPE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CLARKE_1880_FIJI = new SRM_RD_Code(_RD_CLARKE_1880_FIJI,
            "CLARKE_1880_FIJI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CLARKE_1880_IGN = new SRM_RD_Code(_RD_CLARKE_1880_IGN,
            "CLARKE_1880_IGN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CLARKE_1880_PALESTINE = new SRM_RD_Code(_RD_CLARKE_1880_PALESTINE,
            "CLARKE_1880_PALESTINE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CLARKE_1880_SYRIA = new SRM_RD_Code(_RD_CLARKE_1880_SYRIA,
            "CLARKE_1880_SYRIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_COAMPS_1998 = new SRM_RD_Code(_RD_COAMPS_1998,
            "COAMPS_1998");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CORDELIA_1988 = new SRM_RD_Code(_RD_CORDELIA_1988,
            "CORDELIA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_CRESSIDA_1988 = new SRM_RD_Code(_RD_CRESSIDA_1988,
            "CRESSIDA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_DANISH_1876 = new SRM_RD_Code(_RD_DANISH_1876,
            "DANISH_1876");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_DEIMOS_1988 = new SRM_RD_Code(_RD_DEIMOS_1988,
            "DEIMOS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_DELAMBRE_1810 = new SRM_RD_Code(_RD_DELAMBRE_1810,
            "DELAMBRE_1810");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_DESDEMONA_1988 = new SRM_RD_Code(_RD_DESDEMONA_1988,
            "DESDEMONA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_DESPINA_1991 = new SRM_RD_Code(_RD_DESPINA_1991,
            "DESPINA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_DIONE_1982 = new SRM_RD_Code(_RD_DIONE_1982,
            "DIONE_1982");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_ELARA_1988 = new SRM_RD_Code(_RD_ELARA_1988,
            "ELARA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_ENCELADUS_1994 = new SRM_RD_Code(_RD_ENCELADUS_1994,
            "ENCELADUS_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_EPIMETHEUS_1988 = new SRM_RD_Code(_RD_EPIMETHEUS_1988,
            "EPIMETHEUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_EROS_2000 = new SRM_RD_Code(_RD_EROS_2000,
            "EROS_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_EUROPA_2000 = new SRM_RD_Code(_RD_EUROPA_2000,
            "EUROPA_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_EVEREST_ADJ_1937 = new SRM_RD_Code(_RD_EVEREST_ADJ_1937,
            "EVEREST_ADJ_1937");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_EVEREST_1948 = new SRM_RD_Code(_RD_EVEREST_1948,
            "EVEREST_1948");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_EVEREST_1956 = new SRM_RD_Code(_RD_EVEREST_1956,
            "EVEREST_1956");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_EVEREST_REVISED_1962 = new SRM_RD_Code(_RD_EVEREST_REVISED_1962,
            "EVEREST_REVISED_1962");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_EVEREST_1969 = new SRM_RD_Code(_RD_EVEREST_1969,
            "EVEREST_1969");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_EVEREST_BRUNEI_1967 = new SRM_RD_Code(_RD_EVEREST_BRUNEI_1967,
            "EVEREST_BRUNEI_1967");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_FISCHER_1960 = new SRM_RD_Code(_RD_FISCHER_1960,
            "FISCHER_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_FISCHER_1968 = new SRM_RD_Code(_RD_FISCHER_1968,
            "FISCHER_1968");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_GALATEA_1991 = new SRM_RD_Code(_RD_GALATEA_1991,
            "GALATEA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_GANYMEDE_2000 = new SRM_RD_Code(_RD_GANYMEDE_2000,
            "GANYMEDE_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_GASPRA_1991 = new SRM_RD_Code(_RD_GASPRA_1991,
            "GASPRA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_GRS_1967 = new SRM_RD_Code(_RD_GRS_1967,
            "GRS_1967");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_GRS_1980 = new SRM_RD_Code(_RD_GRS_1980,
            "GRS_1980");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_HELENE_1992 = new SRM_RD_Code(_RD_HELENE_1992,
            "HELENE_1992");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_HELMERT_1906 = new SRM_RD_Code(_RD_HELMERT_1906,
            "HELMERT_1906");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_HIMALIA_1988 = new SRM_RD_Code(_RD_HIMALIA_1988,
            "HIMALIA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_HOUGH_1960 = new SRM_RD_Code(_RD_HOUGH_1960,
            "HOUGH_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_HYPERION_2000 = new SRM_RD_Code(_RD_HYPERION_2000,
            "HYPERION_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_IAG_1975 = new SRM_RD_Code(_RD_IAG_1975,
            "IAG_1975");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_IAPETUS_1988 = new SRM_RD_Code(_RD_IAPETUS_1988,
            "IAPETUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_IDA_1991 = new SRM_RD_Code(_RD_IDA_1991,
            "IDA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_INDONESIAN_1974 = new SRM_RD_Code(_RD_INDONESIAN_1974,
            "INDONESIAN_1974");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_INTERNATIONAL_1924 = new SRM_RD_Code(_RD_INTERNATIONAL_1924,
            "INTERNATIONAL_1924");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_IO_2000 = new SRM_RD_Code(_RD_IO_2000,
            "IO_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_JANUS_1988 = new SRM_RD_Code(_RD_JANUS_1988,
            "JANUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_JULIET_1988 = new SRM_RD_Code(_RD_JULIET_1988,
            "JULIET_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_JUPITER_1988 = new SRM_RD_Code(_RD_JUPITER_1988,
            "JUPITER_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_KLEOPATRA_2000 = new SRM_RD_Code(_RD_KLEOPATRA_2000,
            "KLEOPATRA_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_KRASSOVSKY_1940 = new SRM_RD_Code(_RD_KRASSOVSKY_1940,
            "KRASSOVSKY_1940");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_KRAYENHOFF_1827 = new SRM_RD_Code(_RD_KRAYENHOFF_1827,
            "KRAYENHOFF_1827");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_LARISSA_1991 = new SRM_RD_Code(_RD_LARISSA_1991,
            "LARISSA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_LEDA_1988 = new SRM_RD_Code(_RD_LEDA_1988,
            "LEDA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_LYSITHEA_1988 = new SRM_RD_Code(_RD_LYSITHEA_1988,
            "LYSITHEA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MARS_2000 = new SRM_RD_Code(_RD_MARS_2000,
            "MARS_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MARS_SPHERE_2000 = new SRM_RD_Code(_RD_MARS_SPHERE_2000,
            "MARS_SPHERE_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MASS_1999 = new SRM_RD_Code(_RD_MASS_1999,
            "MASS_1999");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MERCURY_1988 = new SRM_RD_Code(_RD_MERCURY_1988,
            "MERCURY_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_METIS_2000 = new SRM_RD_Code(_RD_METIS_2000,
            "METIS_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MIMAS_1994 = new SRM_RD_Code(_RD_MIMAS_1994,
            "MIMAS_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MIRANDA_1988 = new SRM_RD_Code(_RD_MIRANDA_1988,
            "MIRANDA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MM5_1997 = new SRM_RD_Code(_RD_MM5_1997,
            "MM5_1997");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MODIFIED_AIRY_1849 = new SRM_RD_Code(_RD_MODIFIED_AIRY_1849,
            "MODIFIED_AIRY_1849");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MODIFIED_FISCHER_1960 = new SRM_RD_Code(_RD_MODIFIED_FISCHER_1960,
            "MODIFIED_FISCHER_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MODTRAN_MIDLATITUDE_1989 = new SRM_RD_Code(_RD_MODTRAN_MIDLATITUDE_1989,
            "MODTRAN_MIDLATITUDE_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MODTRAN_SUBARCTIC_1989 = new SRM_RD_Code(_RD_MODTRAN_SUBARCTIC_1989,
            "MODTRAN_SUBARCTIC_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MODTRAN_TROPICAL_1989 = new SRM_RD_Code(_RD_MODTRAN_TROPICAL_1989,
            "MODTRAN_TROPICAL_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MOON_1991 = new SRM_RD_Code(_RD_MOON_1991,
            "MOON_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_MULTIGEN_FLAT_EARTH_1989 = new SRM_RD_Code(_RD_MULTIGEN_FLAT_EARTH_1989,
            "MULTIGEN_FLAT_EARTH_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_NAIAD_1991 = new SRM_RD_Code(_RD_NAIAD_1991,
            "NAIAD_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_NEPTUNE_1991 = new SRM_RD_Code(_RD_NEPTUNE_1991,
            "NEPTUNE_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_NEREID_1991 = new SRM_RD_Code(_RD_NEREID_1991,
            "NEREID_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_NOGAPS_1988 = new SRM_RD_Code(_RD_NOGAPS_1988,
            "NOGAPS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_OBERON_1988 = new SRM_RD_Code(_RD_OBERON_1988,
            "OBERON_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_OPHELIA_1988 = new SRM_RD_Code(_RD_OPHELIA_1988,
            "OPHELIA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PAN_1991 = new SRM_RD_Code(_RD_PAN_1991,
            "PAN_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PANDORA_1988 = new SRM_RD_Code(_RD_PANDORA_1988,
            "PANDORA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PASIPHAE_1988 = new SRM_RD_Code(_RD_PASIPHAE_1988,
            "PASIPHAE_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PHOBOS_1988 = new SRM_RD_Code(_RD_PHOBOS_1988,
            "PHOBOS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PHOEBE_1988 = new SRM_RD_Code(_RD_PHOEBE_1988,
            "PHOEBE_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PLESSIS_MODIFIED_1817 = new SRM_RD_Code(_RD_PLESSIS_MODIFIED_1817,
            "PLESSIS_MODIFIED_1817");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PLUTO_1994 = new SRM_RD_Code(_RD_PLUTO_1994,
            "PLUTO_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PORTIA_1988 = new SRM_RD_Code(_RD_PORTIA_1988,
            "PORTIA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PROMETHEUS_1988 = new SRM_RD_Code(_RD_PROMETHEUS_1988,
            "PROMETHEUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PROTEUS_1991 = new SRM_RD_Code(_RD_PROTEUS_1991,
            "PROTEUS_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_PUCK_1988 = new SRM_RD_Code(_RD_PUCK_1988,
            "PUCK_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_RHEA_1988 = new SRM_RD_Code(_RD_RHEA_1988,
            "RHEA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_ROSALIND_1988 = new SRM_RD_Code(_RD_ROSALIND_1988,
            "ROSALIND_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_SATURN_1988 = new SRM_RD_Code(_RD_SATURN_1988,
            "SATURN_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_SINOPE_1988 = new SRM_RD_Code(_RD_SINOPE_1988,
            "SINOPE_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_SOUTH_AMERICAN_1969 = new SRM_RD_Code(_RD_SOUTH_AMERICAN_1969,
            "SOUTH_AMERICAN_1969");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_SOVIET_GEODETIC_1985 = new SRM_RD_Code(_RD_SOVIET_GEODETIC_1985,
            "SOVIET_GEODETIC_1985");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_SOVIET_GEODETIC_1990 = new SRM_RD_Code(_RD_SOVIET_GEODETIC_1990,
            "SOVIET_GEODETIC_1990");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_STRUVE_1860 = new SRM_RD_Code(_RD_STRUVE_1860,
            "STRUVE_1860");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_SUN_1992 = new SRM_RD_Code(_RD_SUN_1992,
            "SUN_1992");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_TELESTO_1988 = new SRM_RD_Code(_RD_TELESTO_1988,
            "TELESTO_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_TETHYS_1991 = new SRM_RD_Code(_RD_TETHYS_1991,
            "TETHYS_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_THALASSA_1991 = new SRM_RD_Code(_RD_THALASSA_1991,
            "THALASSA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_THEBE_2000 = new SRM_RD_Code(_RD_THEBE_2000,
            "THEBE_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_TITAN_1982 = new SRM_RD_Code(_RD_TITAN_1982,
            "TITAN_1982");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_TITANIA_1988 = new SRM_RD_Code(_RD_TITANIA_1988,
            "TITANIA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_TRITON_1991 = new SRM_RD_Code(_RD_TRITON_1991,
            "TRITON_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_UMBRIEL_1988 = new SRM_RD_Code(_RD_UMBRIEL_1988,
            "UMBRIEL_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_URANUS_1988 = new SRM_RD_Code(_RD_URANUS_1988,
            "URANUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_VENUS_1991 = new SRM_RD_Code(_RD_VENUS_1991,
            "VENUS_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_WALBECK_AMS_1963 = new SRM_RD_Code(_RD_WALBECK_AMS_1963,
            "WALBECK_AMS_1963");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_WALBECK_PLANHEFT_1942 = new SRM_RD_Code(_RD_WALBECK_PLANHEFT_1942,
            "WALBECK_PLANHEFT_1942");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_WAR_OFFICE_1924 = new SRM_RD_Code(_RD_WAR_OFFICE_1924,
            "WAR_OFFICE_1924");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_WGS_1960 = new SRM_RD_Code(_RD_WGS_1960,
            "WGS_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_WGS_1966 = new SRM_RD_Code(_RD_WGS_1966,
            "WGS_1966");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_WGS_1984 = new SRM_RD_Code(_RD_WGS_1984,
            "WGS_1984");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_RD_Code RD_WGS_1972 = new SRM_RD_Code(_RD_WGS_1972,
            "WGS_1972");

    private SRM_RD_Code(int code, String name) {
        super(code, name);
        _enumVec.add(code, this);
    }

    /// returns the SRM_RD_Code from its enumerant value
    public static SRM_RD_Code getEnum(int enumeration) throws SrmException {
        if ((enumeration < 1) || (enumeration > _totalEnum)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_RD_Code.getEnum: enumerant out of range"));
        } else {
            return (SRM_RD_Code) (_enumVec.elementAt(enumeration));
        }
    }
}
