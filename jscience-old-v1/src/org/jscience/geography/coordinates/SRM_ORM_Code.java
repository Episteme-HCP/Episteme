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
 * @brief SRM ORM code enumeration according to the SRM spec.
 */
public class SRM_ORM_Code extends Enum {
    /**
     * DOCUMENT ME!
     */
    public static final int _totalEnum = 276;
    private static Vector _enumVec = new Vector();
    private static HashMap _enumMap = new HashMap();

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_UNDEFINED = 0; /// Undefined

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ABSTRACT_2D = 1; /// 2D modelling space

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ABSTRACT_3D = 2; /// 3D modelling space

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ADINDAN_1991 = 3; /// Adindan

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ADRASTEA_2000 = 4; /// Adrastea

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_AFGOOYE_1987 = 5; /// Afgooye (Somalia)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_AIN_EL_ABD_1970 = 6; /// Ain el Abd

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_AMALTHEA_2000 = 7; /// Amalthea

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_AMERICAN_SAMOA_1962 = 8; /// American Samoa

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_AMERSFOORT_1885_1903 = 9; /// Amersfoort 1885 - Revised

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ANNA_1_1965 = 10; /// Anna 1 (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ANTIGUA_1943 = 11; /// Antigua (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ARC_1950 = 12; /// Arc

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ARC_1960 = 13; /// Arc

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ARIEL_1988 = 14; /// Ariel

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ASCENSION_1958 = 15; /// Ascension

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ATLAS_1988 = 16; /// Atlas

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_AUSTRALIAN_GEOD_1966 = 17; /// Australian Geodetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_AUSTRALIAN_GEOD_1984 = 18; /// Australian Geodetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_AYABELLE_LIGHTHOUSE_1991 = 19; /// Ayabelle Lighthouse (Djibouti)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BEACON_E_1945 = 20; /// Beacon E (Iwo-jima; astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BELGIUM_1972 = 21; /// Belgium (Observatoire d'Uccle)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BELINDA_1988 = 22; /// Belinda

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BELLEVUE_IGN_1987 = 23; /// Bellevue (IGN)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BERMUDA_1957 = 24; /// Bermuda

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BERN_1898 = 25; /// Bern

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BERN_1898_PM_BERN = 26; /// Bern (with the Prime Meridian at Bern)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BIANCA_1988 = 27; /// Bianca

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BISSAU_1991 = 28; /// Bissau

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BOGOTA_OBS_1987 = 29; /// Bogota Observatory

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BOGOTA_OBS_1987_PM_BOGOTA = 30; /// Bogota Observatory (with the Prime Meridian at Bogota)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_BUKIT_RIMPAH_1987 = 31; /// Bukit Rimpah

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CALLISTO_2000 = 32; /// Callisto

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CALYPSO_1988 = 33; /// Calypso

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CAMP_AREA_1987 = 34; /// Camp Area (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CAMPO_INCHAUSPE_1969 = 35; /// Campo Inchauspe

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CANTON_1966 = 36; /// Canton (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CAPE_1987 = 37; /// Cape

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CAPE_CANAVERAL_1991 = 38; /// Cape Canaveral

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CARTHAGE_1987 = 39; /// Carthage

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CHARON_1991 = 40; /// Charon

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CHATHAM_1971 = 41; /// Chatam (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CHUA_1987 = 42; /// Chua (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_COAMPS_1998 = 43; /// COAMPS^(TM)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CORDELIA_1988 = 44; /// Cordelia

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CORREGO_ALEGRE_1987 = 45; /// Corrego Alegre

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CRESSIDA_1988 = 46; /// Cressida

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_CYPRUS_1935 = 47; /// Cyprus

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DABOLA_1991 = 48; /// Dabola

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DECEPTION_1993 = 49; /// Deception

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DEIMOS_1988 = 50; /// Deimos

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DESDEMONA_1988 = 51; /// Desdemona

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DESPINA_1991 = 52; /// Despina

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DIONE_1982 = 53; /// Dione

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DJAKARTA_1987 = 54; /// Djakarta (also known as Batavia)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DJAKARTA_1987_PM_DJAKARTA = 55; /// Djakarta (also known as Batavia; with the Prime Meridian at Djakarta)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DOS_1968 = 56; /// DOS

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_DOS_71_4_1987 = 57; /// DOS 71/4 (St. Helena Island; astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EARTH_INERTIAL_ARIES_1950 = 58; /// Earth equatorial inertial, Aries mean of 1950

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EARTH_INERTIAL_ARIES_TRUE_OF_DATE = 59; /// Earth equatorial inertial, Aries true of date

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EARTH_INERTIAL_J2000r0 = 60; /// Earth equatorial inertial, J2000.0

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EARTH_SOLAR_ECLIPTIC = 61; /// Solar ecliptic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EARTH_SOLAR_EQUATORIAL = 62; /// Solar equatorial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EARTH_SOLAR_MAG_DIPOLE = 63; /// Solar magnetic dipole

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EARTH_SOLAR_MAGNETOSPHERIC = 64; /// Solar magnetospheric

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EASTER_1967 = 65; /// Easter

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ENCELADUS_1994 = 66; /// Enceladus

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EPIMETHEUS_1988 = 67; /// Epimetheus

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EROS_2000 = 68; /// Eros (asteroid 433)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ESTONIA_1937 = 69; /// Estonia

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ETRS_1989 = 70; /// ETRS

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EUROPA_2000 = 71; /// Europa

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EUROPE_1950 = 72; /// European

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_EUROPE_1979 = 73; /// European

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_FAHUD_1987 = 74; /// Fahud

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_FORT_THOMAS_1955 = 75; /// Fort Thomas

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GALATEA_1991 = 76; /// Galatea

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GAN_1970 = 77; /// Gan

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GANYMEDE_2000 = 78; /// Ganymede

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GANYMEDE_MAGNETIC_2000 = 79; /// Ganymede magnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GASPRA_1991 = 80; /// Gaspra (asteroid 951)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GDA_1994 = 81; /// GDA

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEODETIC_DATUM_1949 = 82; /// Geodetic Datum

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1945 = 83; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1950 = 84; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1955 = 85; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1960 = 86; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1965 = 87; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1970 = 88; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1975 = 89; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1980 = 90; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1985 = 91; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1990 = 92; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_1995 = 93; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GEOMAGNETIC_2000 = 94; /// Geomagnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GGRS_1987 = 95; /// GGRS 87)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GRACIOSA_BASE_SW_1948 = 96; /// Graciosa Base SW

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GUAM_1963 = 97; /// Guam

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GUNONG_SEGARA_1987 = 98; /// Gunung Segara

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_GUX_1_1987 = 99; /// GUX1 (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HARTEBEESTHOCK_1994 = 100; /// Hartebeesthock

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HELENE_1992 = 101; /// Helene

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HELIO_ARIES_ECLIPTIC_J2000r0 = 102; /// Heliocentric Aries ecliptic, J2000.0

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HELIO_ARIES_ECLIPTIC_TRUE_OF_DATE = 103; /// Heliocentric Aries ecliptic, true of date

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HELIO_EARTH_ECLIPTIC = 104; /// Heliocentric Earth ecliptic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HELIO_EARTH_EQUATORIAL = 105; /// Heliocentric Earth equatorial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HERAT_NORTH_1987 = 106; /// Herat North

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HERMANNSKOGEL_1871 = 107; /// Hermannskogel

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HJORSEY_1955 = 108; /// Hjorsey

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HONG_KONG_1963 = 109; /// Hong Kong

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HONG_KONG_1980 = 110; /// Hong Kong

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HU_TZU_SHAN_1991 = 111; /// Hu-Tzu-Shan

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_HUNGARIAN_1972 = 112; /// Hungarian

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_IAPETUS_1988 = 113; /// Iapetus

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_IDA_1991 = 114; /// Ida (asteroid 243)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_INDIAN_1916 = 115; /// Indian

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_INDIAN_1954 = 116; /// Indian

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_INDIAN_1956 = 117; /// Indian

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_INDIAN_1960 = 118; /// Indian

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_INDIAN_1962 = 119; /// Indian

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_INDIAN_1975 = 120; /// Indian

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_INDONESIAN_1974 = 121; /// Indonesian

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_IO_2000 = 122; /// Io

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_IRAQ_KUWAIT_BNDRY_1992 = 123; /// Iraq/Kuwait Boundary

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_IRELAND_1965 = 124; /// Ireland 1965

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ISTS_061_1968 = 125; /// ISTS 061 (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ISTS_073_1969 = 126; /// ISTS 073 (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JANUS_1988 = 127; /// Janus

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JGD_2000 = 128; /// Japanese Geodetic Datum 2000 (JGD2000)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JOHNSTON_1961 = 129; /// Johnston

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JULIET_1988 = 130; /// Juliet

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JUPITER_1988 = 131; /// Jupiter

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JUPITER_INERTIAL = 132; /// Jupiter equatorial inertial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JUPITER_MAGNETIC_1992 = 133; /// Jupiter magnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JUPITER_SOLAR_ECLIPTIC = 134; /// Jupiter solar ecliptic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JUPITER_SOLAR_EQUATORIAL = 135; /// Jupiter solar equatorial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JUPITER_SOLAR_MAG_DIPOLE = 136; /// Jupiter solar magnetic dipole

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_JUPITER_SOLAR_MAG_ECLIPTIC = 137; /// Jupiter solar magnetic ecliptic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_KANDAWALA_1987 = 138; /// Kandawala

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_KERGUELEN_1949 = 139; /// Kerguelen

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_KERTAU_1948 = 140; /// Kertau

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_KOREAN_GEODETIC_1995 = 141; /// Korean Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_KUSAIE_1951 = 142; /// Kusaie 1951 (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_LANDESVERMESSUNG_1995 = 143; /// Landesvermessung (CH1903+)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_LARISSA_1991 = 144; /// Larissa

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_LC5_1961 = 145; /// LC5 (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_LEIGON_1991 = 146; /// Leigon

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_LIBERIA_1964 = 147; /// Liberia

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_LISBON_D73 = 148; /// Lisbon D73 (Castelo di Sao Jorge)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_LKS_1994 = 149; /// LKS94

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_LUZON_1987 = 150; /// Luzon

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_M_PORALOKO_1991 = 151; /// M'Poraloko

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MAHE_1971 = 152; /// Mahe

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MARCUS_STATION_1952 = 153; /// Marcus Station (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MARS_2000 = 154; /// Mars

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MARS_INERTIAL = 155; /// Mars equatorial inertial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MARS_SPHERE_2000 = 156; /// Mars (spherical)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MASS_1999 = 157; /// MASS

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MASSAWA_1987 = 158; /// Massawa

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MERCHICH_1987 = 159; /// Merchich

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MERCURY_1988 = 160; /// Mercury

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MERCURY_INERTIAL = 161; /// Mercury equatorial inertial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_METIS_2000 = 162; /// Metis

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MIDWAY_1961 = 163; /// Midway 1961 (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MIMAS_1994 = 164; /// Mimas

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MINNA_1991 = 165; /// Minna

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MIRANDA_1988 = 166; /// Miranda

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MM5_1997 = 167; /// MM5 (AFWA)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MODTRAN_MIDLATITUDE_N_1989 = 168; /// MODTRAN

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MODTRAN_MIDLATITUDE_S_1989 = 169; /// MODTRAN

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MODTRAN_SUBARCTIC_N_1989 = 170; /// MODTRAN

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MODTRAN_SUBARCTIC_S_1989 = 171; /// MODTRAN

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MODTRAN_TROPICAL_1989 = 172; /// MODTRAN

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MONTSERRAT_1958 = 173; /// Montserrat (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MOON_1991 = 174; /// Moon

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_MULTIGEN_FLAT_EARTH_1989 = 175; /// Multigen flat Earth

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_N_AM_1927 = 176; /// North American

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_N_AM_1983 = 177; /// North American

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_N_SAHARA_1959 = 178; /// North Sahara

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_NAHRWAN_1987 = 179; /// Nahrwan

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_NAIAD_1991 = 180; /// Naiad

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_NAPARIMA_1991 = 181; /// Naparima BWI

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_NEPTUNE_1991 = 182; /// Neptune

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_NEPTUNE_INERTIAL = 183; /// Neptune equatorial inertial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_NEPTUNE_MAGNETIC_1993 = 184; /// Neptune magnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_NOGAPS_1988 = 185; /// NOGAPS

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_NTF_1896 = 186; /// NTF

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_NTF_1896_PM_PARIS = 187; /// NTF (with the Prime Meridian at Paris)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_OBERON_1988 = 188; /// Oberon

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_OBSERV_METEORO_1939 = 189; /// Observatorio Meteorologico

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_OLD_EGYPTIAN_1907 = 190; /// Old Egyptian

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_OLD_HAW_CLARKE_1987 = 191; /// Old Hawaiian (Clarke)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_OLD_HAW_INT_1987 = 192; /// Old Hawaiian (International)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_OPHELIA_1988 = 193; /// Ophelia

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_OSGB_1936 = 194; /// Ordnance Survey of Great Britain

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PALESTINE_1928 = 195; /// Palestine

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PAN_1991 = 196; /// Pan

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PANDORA_1988 = 197; /// Pandora

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PHOBOS_1988 = 198; /// Phobos

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PHOEBE_1988 = 199; /// Phoebe

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PICO_DE_LAS_NIEVES_1987 = 200; /// Pico de las Nieves

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PITCAIRN_1967 = 201; /// Pitcairn (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PLUTO_1994 = 202; /// Pluto

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PLUTO_INERTIAL = 203; /// Pluto equatorial inertial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_POINT_58_1991 = 204; /// Point 58

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_POINTE_NOIRE_1948 = 205; /// Pointe Noire

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PORTIA_1988 = 206; /// Portia

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PORTO_SANTO_1936 = 207; /// Porto Santo

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PROMETHEUS_1988 = 208; /// Prometheus

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PROTEUS_1991 = 209; /// Proteus

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PROV_S_AM_1956 = 210; /// Provisional South American

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PROV_S_CHILEAN_1963 = 211; /// Provisional South Chilean (Hito XVIII)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PUCK_1988 = 212; /// Puck

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PUERTO_RICO_1987 = 213; /// Puerto Rico

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_PULKOVO_1942 = 214; /// Pulkovo

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_QATAR_NATIONAL_1974 = 215; /// Qatar National

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_QATAR_NATIONAL_1995 = 216; /// Qatar National

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_QORNOQ_1987 = 217; /// Qornoq

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_REUNION_1947 = 218; /// Reunion

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_RGF_1993 = 219; /// Reseau Geodesique Francais

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_RHEA_1988 = 220; /// Rhea

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ROME_1940 = 221; /// Rome (also known as Monte Mario)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ROME_1940_PM_ROME = 222; /// Rome (also known as Monte Mario) (with the Prime Meridian at Rome)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ROSALIND_1988 = 223; /// Rosalind

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_RT_1990 = 224; /// RT

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_RT_1990_PM_STOCKHOLM = 225; /// RT (with the Prime Meridian at Stockholm)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_S_AM_1969 = 226; /// South American

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_S_ASIA_1987 = 227; /// South Asia

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_S_JTSK_1993 = 228; /// S-JTSK

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_S42_PULKOVO = 229; /// S-42 (Pulkovo)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SANTO_DOS_1965 = 230; /// Santo (DOS)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SAO_BRAZ_1987 = 231; /// Sao Braz

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SAPPER_HILL_1943 = 232; /// Sapper Hill

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SATURN_1988 = 233; /// Saturn

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SATURN_INERTIAL = 234; /// Saturn equatorial inertial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SATURN_MAGNETIC_1993 = 235; /// Saturn magnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SCHWARZECK_1991 = 236; /// Schwarzeck

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SELVAGEM_GRANDE_1938 = 237; /// Selvagem Grande

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SIERRA_LEONE_1960 = 238; /// Sierra Leone

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SIRGAS_2000 = 239; /// SIRGAS

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SOUTHEAST_1943 = 240; /// Southeast

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SOVIET_GEODETIC_1985 = 241; /// Soviet Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SOVIET_GEODETIC_1990 = 242; /// Soviet Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_SUN_1992 = 243; /// Sun

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TAN_OBS_1925 = 244; /// Tananarive Observatory

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TAN_OBS_1925_PM_PARIS = 245; /// Tananarive Observatory (with the Prime Meridian at Paris)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TELESTO_1988 = 246; /// Telesto

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TERN_1961 = 247; /// Tern (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TETHYS_1991 = 248; /// Tethys

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_THALASSA_1991 = 249; /// Thalassa

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_THEBE_2000 = 250; /// Thebe

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TIM_BESSEL_1948 = 251; /// Timbali (Bessel)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TIM_BESSEL_ADJ_1968 = 252; /// Timbali (Bessel) - adjusted

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TIM_EV_1948 = 253; /// Timbali (Everest)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TIM_EV_ADJ_1968 = 254; /// Timbali (Everest) - adjusted

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TITAN_1982 = 255; /// Titan

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TITANIA_1988 = 256; /// Titania

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TOKYO_1991 = 257; /// Tokyo

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TRISTAN_1968 = 258; /// Tristan (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_TRITON_1991 = 259; /// Triton

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_UMBRIEL_1988 = 260; /// Umbriel

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_URANUS_1988 = 261; /// Uranus

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_URANUS_INERTIAL = 262; /// Uranus equatorial inertial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_URANUS_MAGNETIC_1993 = 263; /// Uranus magnetic

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_VENUS_1991 = 264; /// Venus

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_VENUS_INERTIAL = 265; /// Venus equatorial inertial

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_VITI_LEVU_1916 = 266; /// Viti Levu

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_VOIROL_1874 = 267; /// Voirol

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_VOIROL_1874_PM_PARIS = 268; /// Voirol (with the Prime Meridian at Paris)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_VOIROL_1960 = 269; /// Voirol - Revised

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_VOIROL_1960_PM_PARIS = 270; /// Voirol - Revised (with the Prime Meridian at Paris)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_WAKE_1952 = 271; /// Wake (astronomic)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_WAKE_ENIWETOK_1960 = 272; /// Wake-Eniwetok

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_WGS_1972 = 273; /// World Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_WGS_1984 = 274; /// World Geodetic System

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_YACARE_1987 = 275; /// Yacare (Uruguay)

    /**
     * DOCUMENT ME!
     */
    public static final int _ORM_ZANDERIJ_1987 = 276; /// Zanderij (Suriname)

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_UNDEFINED = new SRM_ORM_Code(_ORM_UNDEFINED,
            "ORM_UNDEFINED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ABSTRACT_2D = new SRM_ORM_Code(_ORM_ABSTRACT_2D,
            "ORM_ABSTRACT_2D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ABSTRACT_3D = new SRM_ORM_Code(_ORM_ABSTRACT_3D,
            "ORM_ABSTRACT_3D");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ADINDAN_1991 = new SRM_ORM_Code(_ORM_ADINDAN_1991,
            "ORM_ADINDAN_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ADRASTEA_2000 = new SRM_ORM_Code(_ORM_ADRASTEA_2000,
            "ORM_ADRASTEA_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_AFGOOYE_1987 = new SRM_ORM_Code(_ORM_AFGOOYE_1987,
            "ORM_AFGOOYE_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_AIN_EL_ABD_1970 = new SRM_ORM_Code(_ORM_AIN_EL_ABD_1970,
            "ORM_AIN_EL_ABD_1970");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_AMALTHEA_2000 = new SRM_ORM_Code(_ORM_AMALTHEA_2000,
            "ORM_AMALTHEA_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_AMERICAN_SAMOA_1962 = new SRM_ORM_Code(_ORM_AMERICAN_SAMOA_1962,
            "ORM_AMERICAN_SAMOA_1962");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_AMERSFOORT_1885_1903 = new SRM_ORM_Code(_ORM_AMERSFOORT_1885_1903,
            "ORM_AMERSFOORT_1885_1903");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ANNA_1_1965 = new SRM_ORM_Code(_ORM_ANNA_1_1965,
            "ORM_ANNA_1_1965");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ANTIGUA_1943 = new SRM_ORM_Code(_ORM_ANTIGUA_1943,
            "ORM_ANTIGUA_1943");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ARC_1950 = new SRM_ORM_Code(_ORM_ARC_1950,
            "ORM_ARC_1950");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ARC_1960 = new SRM_ORM_Code(_ORM_ARC_1960,
            "ORM_ARC_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ARIEL_1988 = new SRM_ORM_Code(_ORM_ARIEL_1988,
            "ORM_ARIEL_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ASCENSION_1958 = new SRM_ORM_Code(_ORM_ASCENSION_1958,
            "ORM_ASCENSION_1958");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ATLAS_1988 = new SRM_ORM_Code(_ORM_ATLAS_1988,
            "ORM_ATLAS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_AUSTRALIAN_GEOD_1966 = new SRM_ORM_Code(_ORM_AUSTRALIAN_GEOD_1966,
            "ORM_AUSTRALIAN_GEOD_1966");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_AUSTRALIAN_GEOD_1984 = new SRM_ORM_Code(_ORM_AUSTRALIAN_GEOD_1984,
            "ORM_AUSTRALIAN_GEOD_1984");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_AYABELLE_LIGHTHOUSE_1991 = new SRM_ORM_Code(_ORM_AYABELLE_LIGHTHOUSE_1991,
            "ORM_AYABELLE_LIGHTHOUSE_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BEACON_E_1945 = new SRM_ORM_Code(_ORM_BEACON_E_1945,
            "ORM_BEACON_E_1945");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BELGIUM_1972 = new SRM_ORM_Code(_ORM_BELGIUM_1972,
            "ORM_BELGIUM_1972");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BELINDA_1988 = new SRM_ORM_Code(_ORM_BELINDA_1988,
            "ORM_BELINDA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BELLEVUE_IGN_1987 = new SRM_ORM_Code(_ORM_BELLEVUE_IGN_1987,
            "ORM_BELLEVUE_IGN_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BERMUDA_1957 = new SRM_ORM_Code(_ORM_BERMUDA_1957,
            "ORM_BERMUDA_1957");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BERN_1898 = new SRM_ORM_Code(_ORM_BERN_1898,
            "ORM_BERN_1898");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BERN_1898_PM_BERN = new SRM_ORM_Code(_ORM_BERN_1898_PM_BERN,
            "ORM_BERN_1898_PM_BERN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BIANCA_1988 = new SRM_ORM_Code(_ORM_BIANCA_1988,
            "ORM_BIANCA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BISSAU_1991 = new SRM_ORM_Code(_ORM_BISSAU_1991,
            "ORM_BISSAU_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BOGOTA_OBS_1987 = new SRM_ORM_Code(_ORM_BOGOTA_OBS_1987,
            "ORM_BOGOTA_OBS_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BOGOTA_OBS_1987_PM_BOGOTA = new SRM_ORM_Code(_ORM_BOGOTA_OBS_1987_PM_BOGOTA,
            "ORM_BOGOTA_OBS_1987_PM_BOGOTA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_BUKIT_RIMPAH_1987 = new SRM_ORM_Code(_ORM_BUKIT_RIMPAH_1987,
            "ORM_BUKIT_RIMPAH_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CALLISTO_2000 = new SRM_ORM_Code(_ORM_CALLISTO_2000,
            "ORM_CALLISTO_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CALYPSO_1988 = new SRM_ORM_Code(_ORM_CALYPSO_1988,
            "ORM_CALYPSO_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CAMP_AREA_1987 = new SRM_ORM_Code(_ORM_CAMP_AREA_1987,
            "ORM_CAMP_AREA_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CAMPO_INCHAUSPE_1969 = new SRM_ORM_Code(_ORM_CAMPO_INCHAUSPE_1969,
            "ORM_CAMPO_INCHAUSPE_1969");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CANTON_1966 = new SRM_ORM_Code(_ORM_CANTON_1966,
            "ORM_CANTON_1966");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CAPE_1987 = new SRM_ORM_Code(_ORM_CAPE_1987,
            "ORM_CAPE_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CAPE_CANAVERAL_1991 = new SRM_ORM_Code(_ORM_CAPE_CANAVERAL_1991,
            "ORM_CAPE_CANAVERAL_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CARTHAGE_1987 = new SRM_ORM_Code(_ORM_CARTHAGE_1987,
            "ORM_CARTHAGE_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CHARON_1991 = new SRM_ORM_Code(_ORM_CHARON_1991,
            "ORM_CHARON_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CHATHAM_1971 = new SRM_ORM_Code(_ORM_CHATHAM_1971,
            "ORM_CHATHAM_1971");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CHUA_1987 = new SRM_ORM_Code(_ORM_CHUA_1987,
            "ORM_CHUA_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_COAMPS_1998 = new SRM_ORM_Code(_ORM_COAMPS_1998,
            "ORM_COAMPS_1998");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CORDELIA_1988 = new SRM_ORM_Code(_ORM_CORDELIA_1988,
            "ORM_CORDELIA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CORREGO_ALEGRE_1987 = new SRM_ORM_Code(_ORM_CORREGO_ALEGRE_1987,
            "ORM_CORREGO_ALEGRE_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CRESSIDA_1988 = new SRM_ORM_Code(_ORM_CRESSIDA_1988,
            "ORM_CRESSIDA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_CYPRUS_1935 = new SRM_ORM_Code(_ORM_CYPRUS_1935,
            "ORM_CYPRUS_1935");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DABOLA_1991 = new SRM_ORM_Code(_ORM_DABOLA_1991,
            "ORM_DABOLA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DECEPTION_1993 = new SRM_ORM_Code(_ORM_DECEPTION_1993,
            "ORM_DECEPTION_1993");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DEIMOS_1988 = new SRM_ORM_Code(_ORM_DEIMOS_1988,
            "ORM_DEIMOS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DESDEMONA_1988 = new SRM_ORM_Code(_ORM_DESDEMONA_1988,
            "ORM_DESDEMONA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DESPINA_1991 = new SRM_ORM_Code(_ORM_DESPINA_1991,
            "ORM_DESPINA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DIONE_1982 = new SRM_ORM_Code(_ORM_DIONE_1982,
            "ORM_DIONE_1982");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DJAKARTA_1987 = new SRM_ORM_Code(_ORM_DJAKARTA_1987,
            "ORM_DJAKARTA_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DJAKARTA_1987_PM_DJAKARTA = new SRM_ORM_Code(_ORM_DJAKARTA_1987_PM_DJAKARTA,
            "ORM_DJAKARTA_1987_PM_DJAKARTA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DOS_1968 = new SRM_ORM_Code(_ORM_DOS_1968,
            "ORM_DOS_1968");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_DOS_71_4_1987 = new SRM_ORM_Code(_ORM_DOS_71_4_1987,
            "ORM_DOS_71_4_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EARTH_INERTIAL_ARIES_1950 = new SRM_ORM_Code(_ORM_EARTH_INERTIAL_ARIES_1950,
            "ORM_EARTH_INERTIAL_ARIES_1950");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EARTH_INERTIAL_ARIES_TRUE_OF_DATE = new SRM_ORM_Code(_ORM_EARTH_INERTIAL_ARIES_TRUE_OF_DATE,
            "ORM_EARTH_INERTIAL_ARIES_TRUE_OF_DATE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EARTH_INERTIAL_J2000r0 = new SRM_ORM_Code(_ORM_EARTH_INERTIAL_J2000r0,
            "ORM_EARTH_INERTIAL_J2000r0");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EARTH_SOLAR_ECLIPTIC = new SRM_ORM_Code(_ORM_EARTH_SOLAR_ECLIPTIC,
            "ORM_EARTH_SOLAR_ECLIPTIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EARTH_SOLAR_EQUATORIAL = new SRM_ORM_Code(_ORM_EARTH_SOLAR_EQUATORIAL,
            "ORM_EARTH_SOLAR_EQUATORIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EARTH_SOLAR_MAG_DIPOLE = new SRM_ORM_Code(_ORM_EARTH_SOLAR_MAG_DIPOLE,
            "ORM_EARTH_SOLAR_MAG_DIPOLE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EARTH_SOLAR_MAGNETOSPHERIC = new SRM_ORM_Code(_ORM_EARTH_SOLAR_MAGNETOSPHERIC,
            "ORM_EARTH_SOLAR_MAGNETOSPHERIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EASTER_1967 = new SRM_ORM_Code(_ORM_EASTER_1967,
            "ORM_EASTER_1967");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ENCELADUS_1994 = new SRM_ORM_Code(_ORM_ENCELADUS_1994,
            "ORM_ENCELADUS_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EPIMETHEUS_1988 = new SRM_ORM_Code(_ORM_EPIMETHEUS_1988,
            "ORM_EPIMETHEUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EROS_2000 = new SRM_ORM_Code(_ORM_EROS_2000,
            "ORM_EROS_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ESTONIA_1937 = new SRM_ORM_Code(_ORM_ESTONIA_1937,
            "ORM_ESTONIA_1937");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ETRS_1989 = new SRM_ORM_Code(_ORM_ETRS_1989,
            "ORM_ETRS_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EUROPA_2000 = new SRM_ORM_Code(_ORM_EUROPA_2000,
            "ORM_EUROPA_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EUROPE_1950 = new SRM_ORM_Code(_ORM_EUROPE_1950,
            "ORM_EUROPE_1950");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_EUROPE_1979 = new SRM_ORM_Code(_ORM_EUROPE_1979,
            "ORM_EUROPE_1979");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_FAHUD_1987 = new SRM_ORM_Code(_ORM_FAHUD_1987,
            "ORM_FAHUD_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_FORT_THOMAS_1955 = new SRM_ORM_Code(_ORM_FORT_THOMAS_1955,
            "ORM_FORT_THOMAS_1955");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GALATEA_1991 = new SRM_ORM_Code(_ORM_GALATEA_1991,
            "ORM_GALATEA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GAN_1970 = new SRM_ORM_Code(_ORM_GAN_1970,
            "ORM_GAN_1970");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GANYMEDE_2000 = new SRM_ORM_Code(_ORM_GANYMEDE_2000,
            "ORM_GANYMEDE_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GANYMEDE_MAGNETIC_2000 = new SRM_ORM_Code(_ORM_GANYMEDE_MAGNETIC_2000,
            "ORM_GANYMEDE_MAGNETIC_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GASPRA_1991 = new SRM_ORM_Code(_ORM_GASPRA_1991,
            "ORM_GASPRA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GDA_1994 = new SRM_ORM_Code(_ORM_GDA_1994,
            "ORM_GDA_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEODETIC_DATUM_1949 = new SRM_ORM_Code(_ORM_GEODETIC_DATUM_1949,
            "ORM_GEODETIC_DATUM_1949");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1945 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1945,
            "ORM_GEOMAGNETIC_1945");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1950 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1950,
            "ORM_GEOMAGNETIC_1950");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1955 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1955,
            "ORM_GEOMAGNETIC_1955");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1960 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1960,
            "ORM_GEOMAGNETIC_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1965 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1965,
            "ORM_GEOMAGNETIC_1965");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1970 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1970,
            "ORM_GEOMAGNETIC_1970");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1975 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1975,
            "ORM_GEOMAGNETIC_1975");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1980 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1980,
            "ORM_GEOMAGNETIC_1980");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1985 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1985,
            "ORM_GEOMAGNETIC_1985");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1990 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1990,
            "ORM_GEOMAGNETIC_1990");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_1995 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_1995,
            "ORM_GEOMAGNETIC_1995");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GEOMAGNETIC_2000 = new SRM_ORM_Code(_ORM_GEOMAGNETIC_2000,
            "ORM_GEOMAGNETIC_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GGRS_1987 = new SRM_ORM_Code(_ORM_GGRS_1987,
            "ORM_GGRS_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GRACIOSA_BASE_SW_1948 = new SRM_ORM_Code(_ORM_GRACIOSA_BASE_SW_1948,
            "ORM_GRACIOSA_BASE_SW_1948");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GUAM_1963 = new SRM_ORM_Code(_ORM_GUAM_1963,
            "ORM_GUAM_1963");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GUNONG_SEGARA_1987 = new SRM_ORM_Code(_ORM_GUNONG_SEGARA_1987,
            "ORM_GUNONG_SEGARA_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_GUX_1_1987 = new SRM_ORM_Code(_ORM_GUX_1_1987,
            "ORM_GUX_1_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HARTEBEESTHOCK_1994 = new SRM_ORM_Code(_ORM_HARTEBEESTHOCK_1994,
            "ORM_HARTEBEESTHOCK_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HELENE_1992 = new SRM_ORM_Code(_ORM_HELENE_1992,
            "ORM_HELENE_1992");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HELIO_ARIES_ECLIPTIC_J2000r0 = new SRM_ORM_Code(_ORM_HELIO_ARIES_ECLIPTIC_J2000r0,
            "ORM_HELIO_ARIES_ECLIPTIC_J2000r0");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HELIO_ARIES_ECLIPTIC_TRUE_OF_DATE = new SRM_ORM_Code(_ORM_HELIO_ARIES_ECLIPTIC_TRUE_OF_DATE,
            "ORM_HELIO_ARIES_ECLIPTIC_TRUE_OF_DATE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HELIO_EARTH_ECLIPTIC = new SRM_ORM_Code(_ORM_HELIO_EARTH_ECLIPTIC,
            "ORM_HELIO_EARTH_ECLIPTIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HELIO_EARTH_EQUATORIAL = new SRM_ORM_Code(_ORM_HELIO_EARTH_EQUATORIAL,
            "ORM_HELIO_EARTH_EQUATORIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HERAT_NORTH_1987 = new SRM_ORM_Code(_ORM_HERAT_NORTH_1987,
            "ORM_HERAT_NORTH_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HERMANNSKOGEL_1871 = new SRM_ORM_Code(_ORM_HERMANNSKOGEL_1871,
            "ORM_HERMANNSKOGEL_1871");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HJORSEY_1955 = new SRM_ORM_Code(_ORM_HJORSEY_1955,
            "ORM_HJORSEY_1955");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HONG_KONG_1963 = new SRM_ORM_Code(_ORM_HONG_KONG_1963,
            "ORM_HONG_KONG_1963");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HONG_KONG_1980 = new SRM_ORM_Code(_ORM_HONG_KONG_1980,
            "ORM_HONG_KONG_1980");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HU_TZU_SHAN_1991 = new SRM_ORM_Code(_ORM_HU_TZU_SHAN_1991,
            "ORM_HU_TZU_SHAN_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_HUNGARIAN_1972 = new SRM_ORM_Code(_ORM_HUNGARIAN_1972,
            "ORM_HUNGARIAN_1972");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_IAPETUS_1988 = new SRM_ORM_Code(_ORM_IAPETUS_1988,
            "ORM_IAPETUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_IDA_1991 = new SRM_ORM_Code(_ORM_IDA_1991,
            "ORM_IDA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_INDIAN_1916 = new SRM_ORM_Code(_ORM_INDIAN_1916,
            "ORM_INDIAN_1916");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_INDIAN_1954 = new SRM_ORM_Code(_ORM_INDIAN_1954,
            "ORM_INDIAN_1954");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_INDIAN_1956 = new SRM_ORM_Code(_ORM_INDIAN_1956,
            "ORM_INDIAN_1956");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_INDIAN_1960 = new SRM_ORM_Code(_ORM_INDIAN_1960,
            "ORM_INDIAN_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_INDIAN_1962 = new SRM_ORM_Code(_ORM_INDIAN_1962,
            "ORM_INDIAN_1962");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_INDIAN_1975 = new SRM_ORM_Code(_ORM_INDIAN_1975,
            "ORM_INDIAN_1975");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_INDONESIAN_1974 = new SRM_ORM_Code(_ORM_INDONESIAN_1974,
            "ORM_INDONESIAN_1974");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_IO_2000 = new SRM_ORM_Code(_ORM_IO_2000,
            "ORM_IO_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_IRAQ_KUWAIT_BNDRY_1992 = new SRM_ORM_Code(_ORM_IRAQ_KUWAIT_BNDRY_1992,
            "ORM_IRAQ_KUWAIT_BNDRY_1992");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_IRELAND_1965 = new SRM_ORM_Code(_ORM_IRELAND_1965,
            "ORM_IRELAND_1965");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ISTS_061_1968 = new SRM_ORM_Code(_ORM_ISTS_061_1968,
            "ORM_ISTS_061_1968");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ISTS_073_1969 = new SRM_ORM_Code(_ORM_ISTS_073_1969,
            "ORM_ISTS_073_1969");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JANUS_1988 = new SRM_ORM_Code(_ORM_JANUS_1988,
            "ORM_JANUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JGD_2000 = new SRM_ORM_Code(_ORM_JGD_2000,
            "ORM_JGD_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JOHNSTON_1961 = new SRM_ORM_Code(_ORM_JOHNSTON_1961,
            "ORM_JOHNSTON_1961");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JULIET_1988 = new SRM_ORM_Code(_ORM_JULIET_1988,
            "ORM_JULIET_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JUPITER_1988 = new SRM_ORM_Code(_ORM_JUPITER_1988,
            "ORM_JUPITER_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JUPITER_INERTIAL = new SRM_ORM_Code(_ORM_JUPITER_INERTIAL,
            "ORM_JUPITER_INERTIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JUPITER_MAGNETIC_1992 = new SRM_ORM_Code(_ORM_JUPITER_MAGNETIC_1992,
            "ORM_JUPITER_MAGNETIC_1992");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JUPITER_SOLAR_ECLIPTIC = new SRM_ORM_Code(_ORM_JUPITER_SOLAR_ECLIPTIC,
            "ORM_JUPITER_SOLAR_ECLIPTIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JUPITER_SOLAR_EQUATORIAL = new SRM_ORM_Code(_ORM_JUPITER_SOLAR_EQUATORIAL,
            "ORM_JUPITER_SOLAR_EQUATORIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JUPITER_SOLAR_MAG_DIPOLE = new SRM_ORM_Code(_ORM_JUPITER_SOLAR_MAG_DIPOLE,
            "ORM_JUPITER_SOLAR_MAG_DIPOLE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_JUPITER_SOLAR_MAG_ECLIPTIC = new SRM_ORM_Code(_ORM_JUPITER_SOLAR_MAG_ECLIPTIC,
            "ORM_JUPITER_SOLAR_MAG_ECLIPTIC");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_KANDAWALA_1987 = new SRM_ORM_Code(_ORM_KANDAWALA_1987,
            "ORM_KANDAWALA_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_KERGUELEN_1949 = new SRM_ORM_Code(_ORM_KERGUELEN_1949,
            "ORM_KERGUELEN_1949");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_KERTAU_1948 = new SRM_ORM_Code(_ORM_KERTAU_1948,
            "ORM_KERTAU_1948");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_KOREAN_GEODETIC_1995 = new SRM_ORM_Code(_ORM_KOREAN_GEODETIC_1995,
            "ORM_KOREAN_GEODETIC_1995");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_KUSAIE_1951 = new SRM_ORM_Code(_ORM_KUSAIE_1951,
            "ORM_KUSAIE_1951");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_LANDESVERMESSUNG_1995 = new SRM_ORM_Code(_ORM_LANDESVERMESSUNG_1995,
            "ORM_LANDESVERMESSUNG_1995");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_LARISSA_1991 = new SRM_ORM_Code(_ORM_LARISSA_1991,
            "ORM_LARISSA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_LC5_1961 = new SRM_ORM_Code(_ORM_LC5_1961,
            "ORM_LC5_1961");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_LEIGON_1991 = new SRM_ORM_Code(_ORM_LEIGON_1991,
            "ORM_LEIGON_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_LIBERIA_1964 = new SRM_ORM_Code(_ORM_LIBERIA_1964,
            "ORM_LIBERIA_1964");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_LISBON_D73 = new SRM_ORM_Code(_ORM_LISBON_D73,
            "ORM_LISBON_D73");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_LKS_1994 = new SRM_ORM_Code(_ORM_LKS_1994,
            "ORM_LKS_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_LUZON_1987 = new SRM_ORM_Code(_ORM_LUZON_1987,
            "ORM_LUZON_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_M_PORALOKO_1991 = new SRM_ORM_Code(_ORM_M_PORALOKO_1991,
            "ORM_M_PORALOKO_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MAHE_1971 = new SRM_ORM_Code(_ORM_MAHE_1971,
            "ORM_MAHE_1971");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MARCUS_STATION_1952 = new SRM_ORM_Code(_ORM_MARCUS_STATION_1952,
            "ORM_MARCUS_STATION_1952");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MARS_2000 = new SRM_ORM_Code(_ORM_MARS_2000,
            "ORM_MARS_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MARS_INERTIAL = new SRM_ORM_Code(_ORM_MARS_INERTIAL,
            "ORM_MARS_INERTIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MARS_SPHERE_2000 = new SRM_ORM_Code(_ORM_MARS_SPHERE_2000,
            "ORM_MARS_SPHERE_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MASS_1999 = new SRM_ORM_Code(_ORM_MASS_1999,
            "ORM_MASS_1999");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MASSAWA_1987 = new SRM_ORM_Code(_ORM_MASSAWA_1987,
            "ORM_MASSAWA_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MERCHICH_1987 = new SRM_ORM_Code(_ORM_MERCHICH_1987,
            "ORM_MERCHICH_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MERCURY_1988 = new SRM_ORM_Code(_ORM_MERCURY_1988,
            "ORM_MERCURY_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MERCURY_INERTIAL = new SRM_ORM_Code(_ORM_MERCURY_INERTIAL,
            "ORM_MERCURY_INERTIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_METIS_2000 = new SRM_ORM_Code(_ORM_METIS_2000,
            "ORM_METIS_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MIDWAY_1961 = new SRM_ORM_Code(_ORM_MIDWAY_1961,
            "ORM_MIDWAY_1961");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MIMAS_1994 = new SRM_ORM_Code(_ORM_MIMAS_1994,
            "ORM_MIMAS_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MINNA_1991 = new SRM_ORM_Code(_ORM_MINNA_1991,
            "ORM_MINNA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MIRANDA_1988 = new SRM_ORM_Code(_ORM_MIRANDA_1988,
            "ORM_MIRANDA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MM5_1997 = new SRM_ORM_Code(_ORM_MM5_1997,
            "ORM_MM5_1997");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MODTRAN_MIDLATITUDE_N_1989 = new SRM_ORM_Code(_ORM_MODTRAN_MIDLATITUDE_N_1989,
            "ORM_MODTRAN_MIDLATITUDE_N_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MODTRAN_MIDLATITUDE_S_1989 = new SRM_ORM_Code(_ORM_MODTRAN_MIDLATITUDE_S_1989,
            "ORM_MODTRAN_MIDLATITUDE_S_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MODTRAN_SUBARCTIC_N_1989 = new SRM_ORM_Code(_ORM_MODTRAN_SUBARCTIC_N_1989,
            "ORM_MODTRAN_SUBARCTIC_N_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MODTRAN_SUBARCTIC_S_1989 = new SRM_ORM_Code(_ORM_MODTRAN_SUBARCTIC_S_1989,
            "ORM_MODTRAN_SUBARCTIC_S_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MODTRAN_TROPICAL_1989 = new SRM_ORM_Code(_ORM_MODTRAN_TROPICAL_1989,
            "ORM_MODTRAN_TROPICAL_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MONTSERRAT_1958 = new SRM_ORM_Code(_ORM_MONTSERRAT_1958,
            "ORM_MONTSERRAT_1958");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MOON_1991 = new SRM_ORM_Code(_ORM_MOON_1991,
            "ORM_MOON_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_MULTIGEN_FLAT_EARTH_1989 = new SRM_ORM_Code(_ORM_MULTIGEN_FLAT_EARTH_1989,
            "ORM_MULTIGEN_FLAT_EARTH_1989");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_N_AM_1927 = new SRM_ORM_Code(_ORM_N_AM_1927,
            "ORM_N_AM_1927");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_N_AM_1983 = new SRM_ORM_Code(_ORM_N_AM_1983,
            "ORM_N_AM_1983");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_N_SAHARA_1959 = new SRM_ORM_Code(_ORM_N_SAHARA_1959,
            "ORM_N_SAHARA_1959");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_NAHRWAN_1987 = new SRM_ORM_Code(_ORM_NAHRWAN_1987,
            "ORM_NAHRWAN_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_NAIAD_1991 = new SRM_ORM_Code(_ORM_NAIAD_1991,
            "ORM_NAIAD_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_NAPARIMA_1991 = new SRM_ORM_Code(_ORM_NAPARIMA_1991,
            "ORM_NAPARIMA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_NEPTUNE_1991 = new SRM_ORM_Code(_ORM_NEPTUNE_1991,
            "ORM_NEPTUNE_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_NEPTUNE_INERTIAL = new SRM_ORM_Code(_ORM_NEPTUNE_INERTIAL,
            "ORM_NEPTUNE_INERTIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_NEPTUNE_MAGNETIC_1993 = new SRM_ORM_Code(_ORM_NEPTUNE_MAGNETIC_1993,
            "ORM_NEPTUNE_MAGNETIC_1993");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_NOGAPS_1988 = new SRM_ORM_Code(_ORM_NOGAPS_1988,
            "ORM_NOGAPS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_NTF_1896 = new SRM_ORM_Code(_ORM_NTF_1896,
            "ORM_NTF_1896");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_NTF_1896_PM_PARIS = new SRM_ORM_Code(_ORM_NTF_1896_PM_PARIS,
            "ORM_NTF_1896_PM_PARIS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_OBERON_1988 = new SRM_ORM_Code(_ORM_OBERON_1988,
            "ORM_OBERON_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_OBSERV_METEORO_1939 = new SRM_ORM_Code(_ORM_OBSERV_METEORO_1939,
            "ORM_OBSERV_METEORO_1939");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_OLD_EGYPTIAN_1907 = new SRM_ORM_Code(_ORM_OLD_EGYPTIAN_1907,
            "ORM_OLD_EGYPTIAN_1907");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_OLD_HAW_CLARKE_1987 = new SRM_ORM_Code(_ORM_OLD_HAW_CLARKE_1987,
            "ORM_OLD_HAW_CLARKE_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_OLD_HAW_INT_1987 = new SRM_ORM_Code(_ORM_OLD_HAW_INT_1987,
            "ORM_OLD_HAW_INT_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_OPHELIA_1988 = new SRM_ORM_Code(_ORM_OPHELIA_1988,
            "ORM_OPHELIA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_OSGB_1936 = new SRM_ORM_Code(_ORM_OSGB_1936,
            "ORM_OSGB_1936");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PALESTINE_1928 = new SRM_ORM_Code(_ORM_PALESTINE_1928,
            "ORM_PALESTINE_1928");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PAN_1991 = new SRM_ORM_Code(_ORM_PAN_1991,
            "ORM_PAN_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PANDORA_1988 = new SRM_ORM_Code(_ORM_PANDORA_1988,
            "ORM_PANDORA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PHOBOS_1988 = new SRM_ORM_Code(_ORM_PHOBOS_1988,
            "ORM_PHOBOS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PHOEBE_1988 = new SRM_ORM_Code(_ORM_PHOEBE_1988,
            "ORM_PHOEBE_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PICO_DE_LAS_NIEVES_1987 = new SRM_ORM_Code(_ORM_PICO_DE_LAS_NIEVES_1987,
            "ORM_PICO_DE_LAS_NIEVES_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PITCAIRN_1967 = new SRM_ORM_Code(_ORM_PITCAIRN_1967,
            "ORM_PITCAIRN_1967");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PLUTO_1994 = new SRM_ORM_Code(_ORM_PLUTO_1994,
            "ORM_PLUTO_1994");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PLUTO_INERTIAL = new SRM_ORM_Code(_ORM_PLUTO_INERTIAL,
            "ORM_PLUTO_INERTIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_POINT_58_1991 = new SRM_ORM_Code(_ORM_POINT_58_1991,
            "ORM_POINT_58_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_POINTE_NOIRE_1948 = new SRM_ORM_Code(_ORM_POINTE_NOIRE_1948,
            "ORM_POINTE_NOIRE_1948");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PORTIA_1988 = new SRM_ORM_Code(_ORM_PORTIA_1988,
            "ORM_PORTIA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PORTO_SANTO_1936 = new SRM_ORM_Code(_ORM_PORTO_SANTO_1936,
            "ORM_PORTO_SANTO_1936");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PROMETHEUS_1988 = new SRM_ORM_Code(_ORM_PROMETHEUS_1988,
            "ORM_PROMETHEUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PROTEUS_1991 = new SRM_ORM_Code(_ORM_PROTEUS_1991,
            "ORM_PROTEUS_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PROV_S_AM_1956 = new SRM_ORM_Code(_ORM_PROV_S_AM_1956,
            "ORM_PROV_S_AM_1956");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PROV_S_CHILEAN_1963 = new SRM_ORM_Code(_ORM_PROV_S_CHILEAN_1963,
            "ORM_PROV_S_CHILEAN_1963");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PUCK_1988 = new SRM_ORM_Code(_ORM_PUCK_1988,
            "ORM_PUCK_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PUERTO_RICO_1987 = new SRM_ORM_Code(_ORM_PUERTO_RICO_1987,
            "ORM_PUERTO_RICO_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_PULKOVO_1942 = new SRM_ORM_Code(_ORM_PULKOVO_1942,
            "ORM_PULKOVO_1942");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_QATAR_NATIONAL_1974 = new SRM_ORM_Code(_ORM_QATAR_NATIONAL_1974,
            "ORM_QATAR_NATIONAL_1974");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_QATAR_NATIONAL_1995 = new SRM_ORM_Code(_ORM_QATAR_NATIONAL_1995,
            "ORM_QATAR_NATIONAL_1995");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_QORNOQ_1987 = new SRM_ORM_Code(_ORM_QORNOQ_1987,
            "ORM_QORNOQ_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_REUNION_1947 = new SRM_ORM_Code(_ORM_REUNION_1947,
            "ORM_REUNION_1947");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_RGF_1993 = new SRM_ORM_Code(_ORM_RGF_1993,
            "ORM_RGF_1993");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_RHEA_1988 = new SRM_ORM_Code(_ORM_RHEA_1988,
            "ORM_RHEA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ROME_1940 = new SRM_ORM_Code(_ORM_ROME_1940,
            "ORM_ROME_1940");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ROME_1940_PM_ROME = new SRM_ORM_Code(_ORM_ROME_1940_PM_ROME,
            "ORM_ROME_1940_PM_ROME");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ROSALIND_1988 = new SRM_ORM_Code(_ORM_ROSALIND_1988,
            "ORM_ROSALIND_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_RT_1990 = new SRM_ORM_Code(_ORM_RT_1990,
            "ORM_RT_1990");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_RT_1990_PM_STOCKHOLM = new SRM_ORM_Code(_ORM_RT_1990_PM_STOCKHOLM,
            "ORM_RT_1990_PM_STOCKHOLM");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_S_AM_1969 = new SRM_ORM_Code(_ORM_S_AM_1969,
            "ORM_S_AM_1969");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_S_ASIA_1987 = new SRM_ORM_Code(_ORM_S_ASIA_1987,
            "ORM_S_ASIA_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_S_JTSK_1993 = new SRM_ORM_Code(_ORM_S_JTSK_1993,
            "ORM_S_JTSK_1993");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_S42_PULKOVO = new SRM_ORM_Code(_ORM_S42_PULKOVO,
            "ORM_S42_PULKOVO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SANTO_DOS_1965 = new SRM_ORM_Code(_ORM_SANTO_DOS_1965,
            "ORM_SANTO_DOS_1965");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SAO_BRAZ_1987 = new SRM_ORM_Code(_ORM_SAO_BRAZ_1987,
            "ORM_SAO_BRAZ_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SAPPER_HILL_1943 = new SRM_ORM_Code(_ORM_SAPPER_HILL_1943,
            "ORM_SAPPER_HILL_1943");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SATURN_1988 = new SRM_ORM_Code(_ORM_SATURN_1988,
            "ORM_SATURN_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SATURN_INERTIAL = new SRM_ORM_Code(_ORM_SATURN_INERTIAL,
            "ORM_SATURN_INERTIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SATURN_MAGNETIC_1993 = new SRM_ORM_Code(_ORM_SATURN_MAGNETIC_1993,
            "ORM_SATURN_MAGNETIC_1993");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SCHWARZECK_1991 = new SRM_ORM_Code(_ORM_SCHWARZECK_1991,
            "ORM_SCHWARZECK_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SELVAGEM_GRANDE_1938 = new SRM_ORM_Code(_ORM_SELVAGEM_GRANDE_1938,
            "ORM_SELVAGEM_GRANDE_1938");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SIERRA_LEONE_1960 = new SRM_ORM_Code(_ORM_SIERRA_LEONE_1960,
            "ORM_SIERRA_LEONE_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SIRGAS_2000 = new SRM_ORM_Code(_ORM_SIRGAS_2000,
            "ORM_SIRGAS_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SOUTHEAST_1943 = new SRM_ORM_Code(_ORM_SOUTHEAST_1943,
            "ORM_SOUTHEAST_1943");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SOVIET_GEODETIC_1985 = new SRM_ORM_Code(_ORM_SOVIET_GEODETIC_1985,
            "ORM_SOVIET_GEODETIC_1985");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SOVIET_GEODETIC_1990 = new SRM_ORM_Code(_ORM_SOVIET_GEODETIC_1990,
            "ORM_SOVIET_GEODETIC_1990");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_SUN_1992 = new SRM_ORM_Code(_ORM_SUN_1992,
            "ORM_SUN_1992");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TAN_OBS_1925 = new SRM_ORM_Code(_ORM_TAN_OBS_1925,
            "ORM_TAN_OBS_1925");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TAN_OBS_1925_PM_PARIS = new SRM_ORM_Code(_ORM_TAN_OBS_1925_PM_PARIS,
            "ORM_TAN_OBS_1925_PM_PARIS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TELESTO_1988 = new SRM_ORM_Code(_ORM_TELESTO_1988,
            "ORM_TELESTO_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TERN_1961 = new SRM_ORM_Code(_ORM_TERN_1961,
            "ORM_TERN_1961");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TETHYS_1991 = new SRM_ORM_Code(_ORM_TETHYS_1991,
            "ORM_TETHYS_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_THALASSA_1991 = new SRM_ORM_Code(_ORM_THALASSA_1991,
            "ORM_THALASSA_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_THEBE_2000 = new SRM_ORM_Code(_ORM_THEBE_2000,
            "ORM_THEBE_2000");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TIM_BESSEL_1948 = new SRM_ORM_Code(_ORM_TIM_BESSEL_1948,
            "ORM_TIM_BESSEL_1948");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TIM_BESSEL_ADJ_1968 = new SRM_ORM_Code(_ORM_TIM_BESSEL_ADJ_1968,
            "ORM_TIM_BESSEL_ADJ_1968");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TIM_EV_1948 = new SRM_ORM_Code(_ORM_TIM_EV_1948,
            "ORM_TIM_EV_1948");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TIM_EV_ADJ_1968 = new SRM_ORM_Code(_ORM_TIM_EV_ADJ_1968,
            "ORM_TIM_EV_ADJ_1968");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TITAN_1982 = new SRM_ORM_Code(_ORM_TITAN_1982,
            "ORM_TITAN_1982");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TITANIA_1988 = new SRM_ORM_Code(_ORM_TITANIA_1988,
            "ORM_TITANIA_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TOKYO_1991 = new SRM_ORM_Code(_ORM_TOKYO_1991,
            "ORM_TOKYO_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TRISTAN_1968 = new SRM_ORM_Code(_ORM_TRISTAN_1968,
            "ORM_TRISTAN_1968");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_TRITON_1991 = new SRM_ORM_Code(_ORM_TRITON_1991,
            "ORM_TRITON_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_UMBRIEL_1988 = new SRM_ORM_Code(_ORM_UMBRIEL_1988,
            "ORM_UMBRIEL_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_URANUS_1988 = new SRM_ORM_Code(_ORM_URANUS_1988,
            "ORM_URANUS_1988");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_URANUS_INERTIAL = new SRM_ORM_Code(_ORM_URANUS_INERTIAL,
            "ORM_URANUS_INERTIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_URANUS_MAGNETIC_1993 = new SRM_ORM_Code(_ORM_URANUS_MAGNETIC_1993,
            "ORM_URANUS_MAGNETIC_1993");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_VENUS_1991 = new SRM_ORM_Code(_ORM_VENUS_1991,
            "ORM_VENUS_1991");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_VENUS_INERTIAL = new SRM_ORM_Code(_ORM_VENUS_INERTIAL,
            "ORM_VENUS_INERTIAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_VITI_LEVU_1916 = new SRM_ORM_Code(_ORM_VITI_LEVU_1916,
            "ORM_VITI_LEVU_1916");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_VOIROL_1874 = new SRM_ORM_Code(_ORM_VOIROL_1874,
            "ORM_VOIROL_1874");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_VOIROL_1874_PM_PARIS = new SRM_ORM_Code(_ORM_VOIROL_1874_PM_PARIS,
            "ORM_VOIROL_1874_PM_PARIS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_VOIROL_1960 = new SRM_ORM_Code(_ORM_VOIROL_1960,
            "ORM_VOIROL_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_VOIROL_1960_PM_PARIS = new SRM_ORM_Code(_ORM_VOIROL_1960_PM_PARIS,
            "ORM_VOIROL_1960_PM_PARIS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_WAKE_1952 = new SRM_ORM_Code(_ORM_WAKE_1952,
            "ORM_WAKE_1952");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_WAKE_ENIWETOK_1960 = new SRM_ORM_Code(_ORM_WAKE_ENIWETOK_1960,
            "ORM_WAKE_ENIWETOK_1960");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_WGS_1972 = new SRM_ORM_Code(_ORM_WGS_1972,
            "ORM_WGS_1972");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_WGS_1984 = new SRM_ORM_Code(_ORM_WGS_1984,
            "ORM_WGS_1984");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_YACARE_1987 = new SRM_ORM_Code(_ORM_YACARE_1987,
            "ORM_YACARE_1987");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_ORM_Code ORM_ZANDERIJ_1987 = new SRM_ORM_Code(_ORM_ZANDERIJ_1987,
            "ORM_ZANDERIJ_1987");

    private SRM_ORM_Code(int code, String name) {
        super(code, name);
        _enumMap.put(name, this);
        _enumVec.add(code, this);
    }

    /// returns the SRM_ORM_Code from its enumerant value
    public static SRM_ORM_Code getEnum(int enumeration) throws SrmException {
        if ((enumeration < 1) || (enumeration > _totalEnum)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_ORM_Code.getEnum: enumerant out of range"));
        } else {
            return (SRM_ORM_Code) (_enumVec.elementAt(enumeration));
        }
    }

    /// returns the SRM_ORM_Code from its label string
    public static SRM_ORM_Code getEnum(String name) throws SrmException {
        SRM_ORM_Code retCode = (SRM_ORM_Code) _enumMap.get(name);

        if (retCode == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_ORM_Code.getEnum: enum. string not found=> " +
                            name));
        }

        return retCode;
    }
}
