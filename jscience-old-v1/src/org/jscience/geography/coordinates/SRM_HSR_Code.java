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
 * @brief Declaration of HSR enumeration class.
 */
public class SRM_HSR_Code extends Enum {
    private static HashMap _enumMap = new HashMap();

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_UNDEFINED = 0; /// Undefined

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ABSTRACT_2D_IDENTITY = 1; /// Universal

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ABSTRACT_3D_IDENTITY = 1; /// Universal

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ADINDAN_1991_BURKINA_FASO = 1; /// Burkina Faso

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ADINDAN_1991_CAMEROON = 2; /// Cameroon

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ADINDAN_1991_ETHIOPIA = 3; /// Ethiopia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ADINDAN_1991_MALI = 4; /// Mali

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ADINDAN_1991_MEAN_SOLUTION = 5; /// Mean Solution (Ethiopia and Sudan)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ADINDAN_1991_SENEGAL = 6; /// Senegal

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ADINDAN_1991_SUDAN = 7; /// Sudan

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ADRASTEA_2000_IDENTITY = 1; /// Global (Adrastea)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AFGOOYE_1987_SOMALIA = 1; /// Somalia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AIN_EL_ABD_1970_BAHRAIN_ISLAND = 1; /// Bahrain Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AIN_EL_ABD_1970_SAUDI_ARABIA = 2; /// Saudi Arabia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AMALTHEA_2000_IDENTITY = 1; /// Global (Amalthea)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AMERICAN_SAMOA_1962_AMERICAN_SAMOA_ISLANDS = 1; /// American Samoa Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AMERSFOORT_1885_1903_NETHERLANDS = 1; /// Netherlands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ANNA_1_1965_COCOS_ISLANDS = 1; /// Cocos Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ANTIGUA_1943_ANTIGUA_LEEWARD_ISLANDS = 1; /// Antigua and Leeward Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_BOTSWANA = 1; /// Botswana

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_BURUNDI = 2; /// Burundi

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_LESOTHO = 3; /// Lesotho

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_MALAWI = 4; /// Malawi

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_MEAN_SOLUTION = 5; /// Mean Solution (Botswana, Lesotho, Malawi, Swaziland, Zaire, Zambia and Zimbabwe)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_SWAZILAND = 6; /// Swaziland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_ZAIRE = 7; /// Zaire

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_ZAMBIA = 8; /// Zambia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_3_ZIMBABWE = 9; /// Zimbabwe

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1950_7_ZIMBABWE = 10; /// Zimbabwe

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1960_3_KENYA = 1; /// Kenya

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1960_7_KENYA = 2; /// Kenya

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1960_MEAN_SOLUTION = 3; /// Mean Solution (Kenya and Tanzania)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARC_1960_TANZANIA = 4; /// Tanzania

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ARIEL_1988_IDENTITY = 1; /// Global (Ariel)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ASCENSION_1958_ASCENSION_ISLAND = 1; /// Ascension Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ATLAS_1988_IDENTITY = 1; /// Global (Atlas)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AUSTRALIAN_GEOD_1966_AUSTRALIA_TASMANIA = 1; /// Australia and Tasmania

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AUSTRALIAN_GEOD_1984_3_AUSTRALIA_TASMANIA = 1; /// Australia and Tasmania

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AUSTRALIAN_GEOD_1984_7_AUSTRALIA_TASMANIA = 2; /// Australia and Tasmania

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_AYABELLE_LGTHS_1991_DJIBOUTI = 1; /// Djibouti

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BEACON_E_1945_IWO_JIMA_ISLAND = 1; /// Iwo Jima Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BELGIUM_1972_BELGIUM = 1; /// Belgium

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BELINDA_1988_IDENTITY = 1; /// Global (Belinda)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BELLEVUE_IGN_1987_EFATE_ERROMANGO_ISLANDS = 1; /// Efate and Erromango Islands (Vanuatu)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BERMUDA_1957_BERMUDA = 1; /// Bermuda

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BERN_1898_SWITZERLAND = 1; /// Switzerland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BERN_1898_PM_BERN_SWITZERLAND = 1; /// Switzerland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BIANCA_1988_IDENTITY = 1; /// Global (Bianca)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BISSAU_1991_GUINEA_BISSAU = 1; /// Guinea-Bissau

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BOG_OBS_1987_COLUMBIA = 1; /// Colombia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BOG_OBS_1987_PM_BOG_COLUMBIA = 1; /// Colombia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_BUKIT_RIMPAH_1987_BANGKA_BELITUNG_ISLANDS = 1; /// Bangka and Belitung Islands (Indonesia)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CALLISTO_2000_IDENTITY = 1; /// Global (Callisto)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CALYPSO_1988_IDENTITY = 1; /// Global (Calypso)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CAMP_AREA_1987_MCMURDO_CAMP = 1; /// McMurdo Camp Area (Antarctica)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CAMPO_INCHAUSPE_1969_ARGENTINA = 1; /// Argentina

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CANTON_1966_PHOENIX_ISLANDS = 1; /// Phoenix Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CAPE_1987_SOUTH_AFRICA = 1; /// South Africa

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CAPE_CANAVERAL_1991_MEAN_SOLUTION = 1; /// Mean Solution (Bahamas and Florida)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CARTHAGE_1987_TUNISIA = 1; /// Tunisia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CHARON_1991_IDENTITY = 1; /// Global (Charon)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CHATHAM_1971_CHATHAM_ISLANDS = 1; /// Chatham Islands (New Zealand)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CHUA_1987_PARAGUAY = 1; /// Paraguay

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_COAMPS_1998_IDENTITY_BY_DEFAULT = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CORDELIA_1988_IDENTITY = 1; /// Global (Cordelia)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CORREGO_ALEGRE_1987_BRAZIL = 1; /// Brazil

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CRESSIDA_1988_IDENTITY = 1; /// Global (Cressida)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_CYPRUS_1935_CYPRUS = 1; /// Cyprus

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DABOLA_1991_GUINEA = 1; /// Guinea

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DECEPTION_1993_DECEPTION_ISLAND = 1; /// Deception Island (Antarctica)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DEIMOS_1988_IDENTITY = 1; /// Global (Deimos)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DESDEMONA_1988_IDENTITY = 1; /// Global (Desdemona)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DESPINA_1991_IDENTITY = 1; /// Global (Despina)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DIONE_1982_IDENTITY = 1; /// Global (Dione)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DJK_1987_SUMATRA = 1; /// Sumatra (Indonesia)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DJK_1987_PM_DJK_SUMATRA = 1; /// Sumatra (Indonesia)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DOS_1968_GIZO_ISLAND = 1; /// Gizo Island (New Georgia Islands)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_DOS_71_4_1987_ST_HELENA_ISLAND = 1; /// St. Helena Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EASTER_1967_EASTER_ISLAND = 1; /// Easter Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ENCELADUS_1994_IDENTITY = 1; /// Global (Enceladus)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EPIMETHEUS_1988_IDENTITY = 1; /// Global (Epimetheus)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EROS_2000_IDENTITY = 1; /// Global (Eros)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ESTONIA_1937_ESTONIA = 1; /// Estonia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ETRS_1989_IDENTITY_BY_MEASUREMENT = 1; /// Europe

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPA_2000_IDENTITY = 1; /// Global (Europa)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_ALGERIA = 1; /// Algeria

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_BALEARIC_ISLANDS = 2; /// Balearic Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_CHANNEL_ISLANDS = 3; /// Channel Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_3_CYPRUS = 4; /// Cyprus

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_7_CYPRUS = 5; /// Cyprus

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_DENMARK = 6; /// Denmark

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_EGYPT = 7; /// Egypt

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_ENGLAND_SCOTLAND = 8; /// England, Scotland, Channel Islands and Shetland Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_GIBRALTAR = 9; /// Gibraltar

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_GREECE = 10; /// Greece

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_IRAN = 11; /// Iran

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_IRAQ = 12; /// Iraq

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_IRELAND = 13; /// Ireland, Northern Ireland, Wales, England, Scotland, Channel Islands, and Shetland Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_LEBANON = 14; /// Lebanon

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_MALTA = 15; /// Malta

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_MEAN_SOLUTION = 16; /// Mean Solution (Austria, Belgium, Denmark, Finland, France, FRG, Gibraltar, Greece, Italy, Luxembourg, Netherlands, Norway, Portugal, Spain, Sweden and Switzerland)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_NORWAY = 17; /// Finland and Norway

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_OMAN = 18; /// Oman

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_PORTUGAL = 19; /// Portugal

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_PORTUGAL_SPAIN = 20; /// Portugal and Spain

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_SARDINIA = 21; /// Sardinia (Italy)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_SICILY = 22; /// Sicily (Italy)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_SPAIN_EXCEPT_NORTHWEST = 23; /// Spain (except Northwest)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_SPAIN_NW = 24; /// Spain (Northwest)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_TUNISIA = 25; /// Tunisia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_TURKEY = 26; /// Turkey

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_W_EUROPE_MEAN_SOLUTION = 27; /// Western Europe Mean Solution (Austria, Denmark, France, FRG, Netherlands and Switzerland)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1950_YUGOSLAVIA_NORTH = 28; /// Former Yugoslavia North

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1979_MEAN_SOLUTION = 1; /// Mean Solution (Austria, Finland, Netherlands, Norway, Spain, Sweden and Switzerland)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_EUROPE_1979_PORTUGAL = 2; /// Portugal

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_FAHUD_1987_3_OMAN = 1; /// Oman

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_FAHUD_1987_7_OMAN = 2; /// Oman

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_FORT_THOMAS_1955_ST_KITTS_NEVIS_LEEWARD_ISLANDS =
            1; /// St. Kitts, Nevis and Leeward Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GALATEA_1991_IDENTITY = 1; /// Global (Galatea)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GAN_1970_MALDIVES = 1; /// Republic of Maldives

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GANYMEDE_2000_IDENTITY = 1; /// Global (Ganymede)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GANYMEDE_MAGNETIC_2000_GALILEO = 1; /// Global (Ganymede)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GASPRA_1991_IDENTITY = 1; /// Global (Gaspra)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GDA_1994_IDENTITY_BY_MEASUREMENT = 1; /// Australia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEODETIC_DATUM_1949_3_NEW_ZEALAND = 1; /// New Zealand

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEODETIC_DATUM_1949_7_NEW_ZEALAND = 2; /// New Zealand

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1945_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1950_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1955_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1960_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1965_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1970_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1975_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1980_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1985_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1990_DGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_1995_IGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GEOMAGNETIC_2000_IGRF = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GGRS_1987_GREECE = 1; /// Greece

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GRACIOSA_BASE_SW_1948_CENTRAL_AZORES = 1; /// Central Azores (Faial, Graciosa, Pico, Sao Jorge and Terceira Islands)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GUAM_1963_GUAM = 1; /// Guam

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GUNONG_SEGARA_1987_KALIMANTAN_ISLAND = 1; /// Kalimantan Island (Indonesia)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_GUX_1_1987_GUADALCANAL_ISLAND = 1; /// Guadalcanal Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HARTEBEESTHOCK_1994_SOUTH_AFRICA = 1; /// South Africa

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HELENE_1992_IDENTITY = 1; /// Global (Helene)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HERAT_NORTH_1987_AFGHANISTAN = 1; /// Afghanistan

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HERMANNSKOGEL_1871_AUSTRIA = 1; /// Austria

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HERMANNSKOGEL_1871_3_YUGOSLAVIA = 2; /// Yugoslavia (prior to 1990), Slovenia, Croatia, Bosnia and Herzegovina, and Serbia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HERMANNSKOGEL_1871_7_YUGOSLAVIA = 3; /// Yugoslavia (prior to 1990), Slovenia, Croatia, Bosnia and Herzegovina, and Serbia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HJORSEY_1955_ICELAND = 1; /// Iceland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HONG_KONG_1963_HONG_KONG = 1; /// Hong Kong

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HONG_KONG_1980_HONG_KONG = 1; /// Hong Kong

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HU_TZU_SHAN_1991_TAIWAN = 1; /// Taiwan

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_HUNGARIAN_1972_HUNGARY = 1; /// Hungary

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_IAPETUS_1988_IDENTITY = 1; /// Global (Iapetus)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_IDA_1991_IDENTITY = 1; /// Global (Ida)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDIAN_1916_3_BANGLADESH = 1; /// Bangladesh

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDIAN_1916_7_BANGLADESH = 2; /// Bangladesh

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDIAN_1954_THAILAND = 1; /// Thailand

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDIAN_1956_INDIA_NEPAL = 1; /// India and Nepal

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDIAN_1960_CON_SON_ISLAND = 1; /// Con Son Island (Vietnam)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDIAN_1960_VIETNAM_16_N = 2; /// Vietnam (near 16�N)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDIAN_1962_PAKISTAN = 1; /// Pakistan

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDIAN_1975_1991_THAILAND = 1; /// Thailand

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDIAN_1975_1997_THAILAND = 2; /// Thailand

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_INDONESIAN_1974_INDONESIA = 1; /// Indonesia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_IO_2000_IDENTITY = 1; /// Global (Io)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_IRAQ_KUWAIT_BNDRY_1992_IRAQ_KUWAIT = 1; /// Iraq and Kuwait

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_IRELAND_1965_3_IRELAND = 1; /// Ireland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_IRELAND_1965_7_IRELAND = 2; /// Ireland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ISTS_061_1968_SOUTH_GEORGIA_ISLAND = 1; /// South Georgia Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ISTS_073_1969_DIEGO_GARCIA = 1; /// Diego Garcia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_JANUS_1988_IDENTITY = 1; /// Global (Janus)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_JGD_2000_IDENTITY_BY_MEASUREMENT = 1; /// Japan

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_JOHNSTON_1961_JOHNSTON_ISLAND = 1; /// Johnston Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_JULIET_1988_IDENTITY = 1; /// Global (Juliet)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_JUPITER_1988_IDENTITY = 1; /// Global (Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_JUPITER_MAGNETIC_1992_VOYAGER = 1; /// Global (Jupiter)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_KANDAWALA_1987_3_SRI_LANKA = 1; /// Sri Lanka

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_KANDAWALA_1987_7_SRI_LANKA = 2; /// Sri Lanka

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_KERGUELEN_1949_KERGUELEN_ISLAND = 1; /// Kerguelen Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_KERTAU_1948_3_W_MALAYSIA_SINGAPORE = 1; /// West Malaysia and Singapore

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_KERTAU_1948_7_W_MALAYSIA_SINGAPORE = 2; /// West Malaysia and Singapore

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_KOREAN_GEODETIC_1995_SOUTH_KOREA = 1; /// South Korea

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_KUSAIE_1951_CAROLINE_ISLANDS = 1; /// Caroline Islands (Federated States of Micronesia)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LANDESVERMESSUNG_1995_SWITZERLAND = 1; /// Switzerland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LARISSA_1991_IDENTITY = 1; /// Global (Larissa)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LC5_1961_CAYMAN_BRAC_ISLAND = 1; /// Cayman Brac Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LEIGON_1991_3_GHANA = 1; /// Ghana

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LEIGON_1991_7_GHANA = 2; /// Ghana

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LIBERIA_1964_LIBERIA = 1; /// Liberia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LISBON_D73_PORTUGAL = 1; /// Portugal

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LKS_1994_LITHUANIA = 1; /// Lithuania

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LUZON_1987_MINDANAO_ISLAND = 1; /// Mindanao Island (Philippines)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_LUZON_1987_PHILIPPINES_EXCLUDING_MINDANAO_ISLAND =
            2; /// Philippines (excluding Mindanao Island)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_M_PORALOKO_1991_GABON = 1; /// Gabon

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MAHE_1971_MAHE_ISLAND = 1; /// Mahe Island (Seychelles)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MARCUS_STATION_1952_MARCUS_ISLANDS = 1; /// Marcus Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MARS_2000_IDENTITY = 1; /// Global (Mars)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MARS_SPHERE_2000_GLOBAL = 1; /// Global (Mars)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MASS_1999_IDENTITY_BY_DEFAULT = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MASSAWA_1987_ERITREA_ETHIOPIA = 1; /// Eritrea and Ethiopia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MERCHICH_1987_MOROCCO = 1; /// Morocco

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MERCURY_1988_IDENTITY = 1; /// Global (Mercury)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_METIS_2000_IDENTITY = 1; /// Global (Metis)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MIDWAY_1961_MIDWAY_ISLANDS = 1; /// Midway Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MIMAS_1994_IDENTITY = 1; /// Global (Mimas)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MINNA_1991_CAMEROON = 1; /// Cameroon

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MINNA_1991_NIGERIA = 2; /// Nigeria

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MIRANDA_1988_IDENTITY = 1; /// Global (Miranda)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MM5_1997_IDENTITY_BY_DEFAULT = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MODTRAN_MLAT_N_1989_IDENTITY_BY_DEFAULT = 1; /// Northern midlatitude regions (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MODTRAN_MLAT_S_1989_IDENTITY_BY_DEFAULT = 1; /// Southern midlatitude regions (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MODTRAN_SARC_N_1989_IDENTITY_BY_DEFAULT = 1; /// Northern subarctic regions (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MODTRAN_SARC_S_1989_IDENTITY_BY_DEFAULT = 1; /// Southern subarctic regions (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MODTRAN_TROPICAL_1989_IDENTITY_BY_DEFAULT = 1; /// Tropical regions (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MONTSERRAT_1958_MONTSERRAT_LEEWARD_ISLANDS = 1; /// Montserrat and Leeward Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MOON_1991_IDENTITY = 1; /// Global (Moon)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_MG_FLAT_EARTH_1989_IDENTITY_BY_DEFAULT = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_ALASKA_EXCLUDING_ALEUTIAN_ISLANDS = 1; /// Alaska (excluding Aleutian Islands)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_ALBERTA_BRITISH_COLUMBIA = 2; /// Canada (Alberta and British Columbia)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_BAHAMAS_EXCLUDING_SAN_SALVADOR_ISLAND =
            3; /// Bahamas (excluding San Salvador Island)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_CANADA = 4; /// Canada

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_CANAL_ZONE = 5; /// Canal Zone

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_CARIBBEAN = 6; /// Caribbean (Antigua Island, Barbados, Barbuda, Caicos Islands, Cuba, Dominican Republic, Grand Cayman, Jamaica and Turks Islands)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_CENTRAL_AMERICA = 7; /// Central America (Belize, Costa Rica, El Salvador, Guatemala, Honduras and Nicaragua)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_CONTINENTAL_US = 8; /// Continental United States Mean Solution

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_CUBA = 9; /// Cuba

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_EAST_ALEUTIAN_ISLANDS = 10; /// Aleutian Islands (east of 180�W)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_EASTERN_CANADA = 11; /// Eastern Canada (New Brunswick, Newfoundland, Nova Scotia and Quebec)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_EASTERN_US = 12; /// Eastern United States (Alabama, Connecticut, Delaware, District of Columbia, Florida, Georgia, Illinois, Indiana, Kentucky, Louisiana, Maine, Maryland, Massachusetts, Michigan, Minnesota, Mississippi, Missouri, New Hampshire, New Jersey, New York, North Carolina, Ohio, Pennsylvania, Rhode Island, South Carolina, Tennessee, Vermont, Virginia, West Virginia and Wisconsin)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_HAYES_PENINSULA = 13; /// Hayes Peninsula (Greenland)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_MANITOBA_ONTARIO = 14; /// Canada (Manitoba and Ontario)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_MEXICO = 15; /// Mexico

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_NORTHWEST_TERRITORIES_SASKATCHEWAN = 16; /// Canada (Northwest Territories and Saskatchewan)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_SAN_SALVADOR_ISLAND = 17; /// San Salvador Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_WEST_ALEUTIAN_ISLANDS = 18; /// Aleutian Islands (west of 180�W)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_WESTERN_US = 19; /// Western United States (Arizona, Arkansas, California, Colorado, Idaho, Iowa, Kansas, Montana, Nebraska, Nevada, New Mexico, North Dakota, Oklahoma, Oregon, South Dakota, Texas, Utah, Washington and Wyoming)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1927_YUKON = 20; /// Canada (Yukon)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1983_ALASKA_EXCLUDING_ALEUTIAN_ISLANDS = 1; /// Alaska (excluding Aleutian Islands)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1983_ALEUTIAN_ISLANDS = 2; /// Aleutian Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1983_CANADA = 3; /// Canada

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1983_CONTINENTAL_US = 4; /// Continental United States

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1983_HAWAII = 5; /// Hawaii

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_AM_1983_MEXICO_CENTRAL_AMERICA = 6; /// Mexico and Central America

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_N_SAHARA_1959_ALGERIA = 1; /// Algeria

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NAHRWAN_1987_MASIRAH_ISLAND = 1; /// Masirah Island (Oman)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NAHRWAN_1987_SAUDI_ARABIA = 2; /// Saudi Arabia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NAHRWAN_1987_UNITED_ARAB_EMIRATES = 3; /// United Arab Emirates

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NAIAD_1991_IDENTITY = 1; /// Global (Naiad)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NAPARIMA_1991_TRINIDAD_TOBAGO = 1; /// Trinidad and Tobago (British West Indies)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NEPTUNE_1991_IDENTITY = 1; /// Global (Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NEPTUNE_MAGNETIC_1993_VOYAGER = 1; /// Global (Neptune)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NOGAPS_1988_IDENTITY_BY_DEFAULT = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NTF_1896_FRANCE = 1; /// France

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_NTF_1896_PM_PARIS_FRANCE = 1; /// France

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OBERON_1988_IDENTITY = 1; /// Global (Oberon)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OBSERV_METEORO_1939_CORVO_FLORES_ISLANDS = 1; /// Corvo Flores Islands (Azores)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_EGYPTIAN_1907_EGYPT = 1; /// Egypt

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_CLARKE_1987_HAWAII = 1; /// Hawaii (US)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_CLARKE_1987_KAUAI = 2; /// Kauai (US)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_CLARKE_1987_MAUI = 3; /// Maui (US)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_CLARKE_1987_MEAN_SOLUTION = 4; /// Mean Solution (Hawaii (US))

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_CLARKE_1987_OAHU = 5; /// Oahu (US)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_INT_1987_HAWAII = 1; /// Hawaii (US)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_INT_1987_KAUAI = 2; /// Kauai (US)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_INT_1987_MAUI = 3; /// Maui (US)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_INT_1987_MEAN_SOLUTION = 4; /// Mean Solution (Hawaii (US))

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OLD_HAW_INT_1987_OAHU = 5; /// Oahu (US)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OPHELIA_1988_IDENTITY = 1; /// Global (Ophelia)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OSGB_1936_ENGLAND = 1; /// England

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OSGB_1936_ENGLAND_ISLE_OF_MAN_WALES = 2; /// England, Isle of Man, and Wales

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OSGB_1936_7_GREAT_BRITAIN = 3; /// Great Britain

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OSGB_1936_3_MEAN_SOLUTION = 4; /// Mean Solution (England, Isle of Man, Scotland, Shetland, and Wales)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OSGB_1936_SCOTLAND_SHETLAND_ISLANDS = 5; /// Scotland and Shetland Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_OSGB_1936_WALES = 6; /// Wales

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PALESTINE_1928_ISRAEL_JORDAN = 1; /// Israel and Jordan

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PAN_1991_IDENTITY = 1; /// Global (Pan)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PANDORA_1988_IDENTITY = 1; /// Global (Pandora)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PHOBOS_1988_IDENTITY = 1; /// Global (Phobos)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PHOEBE_1988_IDENTITY = 1; /// Global (Phoebe)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PICO_NIEVES_1987_CANARY_ISLANDS = 1; /// Canary Islands (Spain)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PITCAIRN_1967_PITCAIRN_ISLAND = 1; /// Pitcairn Island

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PLUTO_1994_IDENTITY = 1; /// Global (Pluto)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_POINT_58_1991_MEAN_SOLUTION = 1; /// Mean Solution (Burkina Faso and Niger)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_POINTE_NOIRE_1948_CONGO = 1; /// Congo

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PORTIA_1988_IDENTITY = 1; /// Global (Portia)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PORTO_SANTO_1936_PORTO_SANTO_MADEIRA_ISLANDS = 1; /// Porto Santo and Madeira Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROMETHEUS_1988_IDENTITY = 1; /// Global (Prometheus)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROTEUS_1991_IDENTITY = 1; /// Global (Proteus)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_BOLIVIA = 1; /// Bolivia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_COLOMBIA = 2; /// Colombia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_ECUADOR = 3; /// Ecuador

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_GUYANA = 4; /// Guyana

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_MEAN_SOLUTION = 5; /// Mean Solution (Bolivia, Chile, Colombia, Ecuador, Guyana, Peru and Venezuela)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_N_CHILE_19_S = 6; /// Northern Chile (near 19�S)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_PERU = 7; /// Peru

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_S_CHILE_43_S = 8; /// Southern Chile (near 43�S)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_3_VENEZUELA = 9; /// Venezuela

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_AM_1956_7_VENEZUELA = 10; /// Venezuela

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PROV_S_CHILEAN_1963_SOUTH_CHILE = 1; /// South Chile (near 53�S)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PUCK_1988_IDENTITY = 1; /// Global (Puck)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PUERTO_RICO_1987_PUERTO_RICO_VIRGIN_ISLANDS = 1; /// Puerto Rico and Virgin Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PULKOVO_1942_ESTONIA = 1; /// Estonia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PULKOVO_1942_GERMANY = 2; /// FRG

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_PULKOVO_1942_RUSSIA = 3; /// Russia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_QATAR_NATIONAL_1974_3_QATAR = 1; /// Qatar

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_QATAR_NATIONAL_1974_7_QATAR = 2; /// Qatar

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_QATAR_NATIONAL_1995_QATAR = 1; /// Qatar

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_QORNOQ_1987_SOUTH_GREENLAND = 1; /// South Greenland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_REUNION_1947_MASCARENE_ISLANDS = 1; /// Mascarene Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_RGF_1993_IDENTITY_BY_MEASUREMENT = 1; /// France

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_RHEA_1988_IDENTITY = 1; /// Global (Rhea)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ROME_1940_7_ITALY = 1; /// Italy mainland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ROME_1940_SARDINIA = 2; /// Sardinia (Italy)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ROME_1940_7_SARDINIA = 3; /// Sardinia (Italy)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ROME_1940_SICILY = 4; /// Sicily (Italy)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ROME_1940_PM_ROME_7_ITALY = 1; /// Italy mainland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ROME_1940_PM_ROME_SARDINIA = 2; /// Sardinia (Italy)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ROSALIND_1988_IDENTITY = 1; /// Global (Rosalind)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_RT_1990_SWEDEN = 1; /// Sweden

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_RT_1990_PM_STOCKHOLM_SWEDEN = 1; /// Sweden

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_ARGENTINA = 1; /// Argentina

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_BALTRA_GALAPAGOS_ISLANDS = 2; /// Baltra and Galapagos Islands (Ecuador)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_BOLIVIA = 3; /// Bolivia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_BRAZIL = 4; /// Brazil

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_CHILE = 5; /// Chile

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_COLOMBIA = 6; /// Colombia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_ECUADOR_EXCLUDING_GALAPAGOS_ISLANDS = 7; /// Ecuador (excluding Galapagos Islands)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_GUYANA = 8; /// Guyana

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_MEAN_SOLUTION = 9; /// Mean Solution (Argentina, Bolivia, Brazil, Chile, Colombia, Ecuador, Guyana, Paraguay, Peru, Trinidad and Tobago, and Venezuela)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_PARAGUAY = 10; /// Paraguay

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_PERU = 11; /// Peru

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_TRINIDAD_TOBAGO = 12; /// Trinidad and Tobago (British West Indies)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_AM_1969_VENEZUELA = 13; /// Venezuela

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_ASIA_1987_SINGAPORE = 1; /// Singapore

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_JTSK_1993_CZECH_REP = 1; /// Czech Republic

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_JTSK_1993_CZECH_REP_SLOVAKIA = 2; /// Czech Republic and Slovakia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S_JTSK_1993_SLOVAKIA = 3; /// Slovakia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_AFGHANISTAN = 1; /// Afghanistan

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_ALBANIA = 2; /// Albania

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_CZECH_REPUBLIC_SLOVAKIA = 3; /// Czech Republic and Slovakia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_HUNGARY = 4; /// Hungary

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_KAZAKHSTAN = 5; /// Kazakhstan

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_LATVIA = 6; /// Latvia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_3_POLAND = 7; /// Poland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_7_POLAND = 8; /// Poland

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_ROMANIA = 9; /// Romania

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_S42_PULKOVO_G_ROMANIA = 10; /// Romania

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SANTO_DOS_1965_ESPIRITO_SANTO_ISLAND = 1; /// Espirito Santo Island (Vanuatu)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SAO_BRAZ_1987_SAO_MIGUEL_SANTA_MARIA_ISLANDS = 1; /// Sao Miguel and Santa Maria Islands (Azores)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SAPPER_HILL_1943_3_E_FALKLAND_ISLANDS = 1; /// East Falkland Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SAPPER_HILL_1943_7_E_FALKLAND_ISLANDS_ADJ_2009 = 2; /// East Falkland Islands (adjusted 2000)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SATURN_1988_IDENTITY = 1; /// Global (Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SATURN_MAGNETIC_1993_VOYAGER = 1; /// Global (Saturn)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SCHWARZECK_1991_NAMIBIA = 1; /// Namibia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SELVAGEM_GRANDE_1938_SALVAGE_ISLANDS = 1; /// Salvage Islands (Ilhas Selvagens; Savage Islands)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SIERRA_LEONE_1960_SIERRA_LEONE = 1; /// Sierra Leone

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SIRGAS_2000_IDENTITY_BY_MEASUREMENT = 1; /// South America

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SOUTHEAST_1943_SEYCHELLES_ISLANDS = 1; /// Seychelles Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SOVIET_GEODETIC_1985_RUSSIA = 1; /// Russia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SOVIET_GEODETIC_1990_RUSSIA = 1; /// Russia

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_SUN_1992_IDENTITY = 1; /// Global (Sun)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TAN_OBS_1925_3_MADAGASCAR = 1; /// Madagascar

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TAN_OBS_1925_7_MADAGASCAR = 2; /// Madagascar

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TAN_OBS_1925_PM_PARIS_3_MADAGASCAR = 1; /// Madagascar

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TAN_OBS_1925_PM_PARIS_7_MADAGASCAR = 2; /// Madagascar

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TELESTO_1988_IDENTITY = 1; /// Global (Telesto)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TERN_1961_TERN_ISLAND = 1; /// Tern Island (French Frigate Shoals, Hawaiian Islands)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TETHYS_1991_IDENTITY = 1; /// Global (Tethys)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_THALASSA_1991_IDENTITY = 1; /// Global (Thalassa)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_THEBE_2000_IDENTITY = 1; /// Global (Thebe)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TIM_BESSEL_1948_7_BRUNEI_E_MALAYSIA = 1; /// Brunei and East Malaysia (Sabah and Sarawak)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TIM_BESSEL_ADJ_1968_7_BRUNEI_E_MALAYSIA = 1; /// Brunei and East Malaysia (Sabah and Sarawak)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TIM_EV_1948_3_BRUNEI_E_MALAYSIA = 1; /// Brunei and East Malaysia (Sabah and Sarawak)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TIM_EV_1948_7_BRUNEI_E_MALAYSIA = 2; /// Brunei and East Malaysia (Sabah and Sarawak)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TIM_EV_ADJ_1968_7_BRUNEI_E_MALAYSIA = 1; /// Brunei and East Malaysia (Sabah and Sarawak)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TITAN_1982_IDENTITY = 1; /// Global (Titan)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TITANIA_1988_IDENTITY = 1; /// Global (Titania)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TOKYO_1991_JAPAN = 1; /// Japan

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TOKYO_1991_MEAN_SOLUTION = 2; /// Mean Solution (Japan, Korea, and Okinawa)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TOKYO_1991_OKINAWA = 3; /// Okinawa (Japan)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TOKYO_1991_1991_SOUTH_KOREA = 4; /// South Korea

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TOKYO_1991_1997_SOUTH_KOREA = 5; /// South Korea

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TRISTAN_1968_TRISTAN_DA_CUHNA = 1; /// Tristan da Cunha

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_TRITON_1991_IDENTITY = 1; /// Global (Triton)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_UMBRIEL_1988_IDENTITY = 1; /// Global (Umbriel)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_URANUS_1988_IDENTITY = 1; /// Global (Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_URANUS_MAGNETIC_1993_VOYAGER = 1; /// Global (Uranus)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_VENUS_1991_IDENTITY = 1; /// Global (Venus)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_VITI_LEVU_1916_VITI_LEVU_ISLANDS = 1; /// Viti Levu Island (Fiji Islands)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_VOIROL_1874_ALGERIA = 1; /// Algeria

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_VOIROL_1874_PM_PARIS_ALGERIA = 1; /// Algeria

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_VOIROL_1960_ALGERIA = 1; /// Algeria

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_VOIROL_1960_PM_PARIS_ALGERIA = 1; /// Algeria

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_WAKE_1952_WAKE_ATOLL = 1; /// Wake Atoll

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_WAKE_ENIWETOK_1960_MARSHALL_ISLANDS = 1; /// Marshall Islands

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_WGS_1972_GLOBAL = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_WGS_1984_IDENTITY = 1; /// Global (Earth)

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_YACARE_1987_URUGUAY = 1; /// Uruguay

    /**
     * DOCUMENT ME!
     */
    public static final int _HSR_ZANDERIJ_1987_SURINAME = 1; /// Suriname

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_UNDEFINED = new SRM_HSR_Code(_HSR_UNDEFINED,
            "HSR_UNDEFINED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ABSTRACT_2D_IDENTITY = new SRM_HSR_Code(_HSR_ABSTRACT_2D_IDENTITY,
            "HSR_ABSTRACT_2D_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ABSTRACT_3D_IDENTITY = new SRM_HSR_Code(_HSR_ABSTRACT_3D_IDENTITY,
            "HSR_ABSTRACT_3D_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ADINDAN_1991_BURKINA_FASO = new SRM_HSR_Code(_HSR_ADINDAN_1991_BURKINA_FASO,
            "HSR_ADINDAN_1991_BURKINA_FASO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ADINDAN_1991_CAMEROON = new SRM_HSR_Code(_HSR_ADINDAN_1991_CAMEROON,
            "HSR_ADINDAN_1991_CAMEROON");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ADINDAN_1991_ETHIOPIA = new SRM_HSR_Code(_HSR_ADINDAN_1991_ETHIOPIA,
            "HSR_ADINDAN_1991_ETHIOPIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ADINDAN_1991_MALI = new SRM_HSR_Code(_HSR_ADINDAN_1991_MALI,
            "HSR_ADINDAN_1991_MALI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ADINDAN_1991_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_ADINDAN_1991_MEAN_SOLUTION,
            "HSR_ADINDAN_1991_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ADINDAN_1991_SENEGAL = new SRM_HSR_Code(_HSR_ADINDAN_1991_SENEGAL,
            "HSR_ADINDAN_1991_SENEGAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ADINDAN_1991_SUDAN = new SRM_HSR_Code(_HSR_ADINDAN_1991_SUDAN,
            "HSR_ADINDAN_1991_SUDAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ADRASTEA_2000_IDENTITY = new SRM_HSR_Code(_HSR_ADRASTEA_2000_IDENTITY,
            "HSR_ADRASTEA_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AFGOOYE_1987_SOMALIA = new SRM_HSR_Code(_HSR_AFGOOYE_1987_SOMALIA,
            "HSR_AFGOOYE_1987_SOMALIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AIN_EL_ABD_1970_BAHRAIN_ISLAND = new SRM_HSR_Code(_HSR_AIN_EL_ABD_1970_BAHRAIN_ISLAND,
            "HSR_AIN_EL_ABD_1970_BAHRAIN_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AIN_EL_ABD_1970_SAUDI_ARABIA = new SRM_HSR_Code(_HSR_AIN_EL_ABD_1970_SAUDI_ARABIA,
            "HSR_AIN_EL_ABD_1970_SAUDI_ARABIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AMALTHEA_2000_IDENTITY = new SRM_HSR_Code(_HSR_AMALTHEA_2000_IDENTITY,
            "HSR_AMALTHEA_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AMERICAN_SAMOA_1962_AMERICAN_SAMOA_ISLANDS =
            new SRM_HSR_Code(_HSR_AMERICAN_SAMOA_1962_AMERICAN_SAMOA_ISLANDS,
                    "HSR_AMERICAN_SAMOA_1962_AMERICAN_SAMOA_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AMERSFOORT_1885_1903_NETHERLANDS = new SRM_HSR_Code(_HSR_AMERSFOORT_1885_1903_NETHERLANDS,
            "HSR_AMERSFOORT_1885_1903_NETHERLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ANNA_1_1965_COCOS_ISLANDS = new SRM_HSR_Code(_HSR_ANNA_1_1965_COCOS_ISLANDS,
            "HSR_ANNA_1_1965_COCOS_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ANTIGUA_1943_ANTIGUA_LEEWARD_ISLANDS = new SRM_HSR_Code(_HSR_ANTIGUA_1943_ANTIGUA_LEEWARD_ISLANDS,
            "HSR_ANTIGUA_1943_ANTIGUA_LEEWARD_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_BOTSWANA = new SRM_HSR_Code(_HSR_ARC_1950_BOTSWANA,
            "HSR_ARC_1950_BOTSWANA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_BURUNDI = new SRM_HSR_Code(_HSR_ARC_1950_BURUNDI,
            "HSR_ARC_1950_BURUNDI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_LESOTHO = new SRM_HSR_Code(_HSR_ARC_1950_LESOTHO,
            "HSR_ARC_1950_LESOTHO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_MALAWI = new SRM_HSR_Code(_HSR_ARC_1950_MALAWI,
            "HSR_ARC_1950_MALAWI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_ARC_1950_MEAN_SOLUTION,
            "HSR_ARC_1950_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_SWAZILAND = new SRM_HSR_Code(_HSR_ARC_1950_SWAZILAND,
            "HSR_ARC_1950_SWAZILAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_ZAIRE = new SRM_HSR_Code(_HSR_ARC_1950_ZAIRE,
            "HSR_ARC_1950_ZAIRE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_ZAMBIA = new SRM_HSR_Code(_HSR_ARC_1950_ZAMBIA,
            "HSR_ARC_1950_ZAMBIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_3_ZIMBABWE = new SRM_HSR_Code(_HSR_ARC_1950_3_ZIMBABWE,
            "HSR_ARC_1950_3_ZIMBABWE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1950_7_ZIMBABWE = new SRM_HSR_Code(_HSR_ARC_1950_7_ZIMBABWE,
            "HSR_ARC_1950_7_ZIMBABWE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1960_3_KENYA = new SRM_HSR_Code(_HSR_ARC_1960_3_KENYA,
            "HSR_ARC_1960_3_KENYA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1960_7_KENYA = new SRM_HSR_Code(_HSR_ARC_1960_7_KENYA,
            "HSR_ARC_1960_7_KENYA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1960_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_ARC_1960_MEAN_SOLUTION,
            "HSR_ARC_1960_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARC_1960_TANZANIA = new SRM_HSR_Code(_HSR_ARC_1960_TANZANIA,
            "HSR_ARC_1960_TANZANIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ARIEL_1988_IDENTITY = new SRM_HSR_Code(_HSR_ARIEL_1988_IDENTITY,
            "HSR_ARIEL_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ASCENSION_1958_ASCENSION_ISLAND = new SRM_HSR_Code(_HSR_ASCENSION_1958_ASCENSION_ISLAND,
            "HSR_ASCENSION_1958_ASCENSION_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ATLAS_1988_IDENTITY = new SRM_HSR_Code(_HSR_ATLAS_1988_IDENTITY,
            "HSR_ATLAS_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AUSTRALIAN_GEOD_1966_AUSTRALIA_TASMANIA =
            new SRM_HSR_Code(_HSR_AUSTRALIAN_GEOD_1966_AUSTRALIA_TASMANIA,
                    "HSR_AUSTRALIAN_GEOD_1966_AUSTRALIA_TASMANIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AUSTRALIAN_GEOD_1984_3_AUSTRALIA_TASMANIA =
            new SRM_HSR_Code(_HSR_AUSTRALIAN_GEOD_1984_3_AUSTRALIA_TASMANIA,
                    "HSR_AUSTRALIAN_GEOD_1984_3_AUSTRALIA_TASMANIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AUSTRALIAN_GEOD_1984_7_AUSTRALIA_TASMANIA =
            new SRM_HSR_Code(_HSR_AUSTRALIAN_GEOD_1984_7_AUSTRALIA_TASMANIA,
                    "HSR_AUSTRALIAN_GEOD_1984_7_AUSTRALIA_TASMANIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_AYABELLE_LGTHS_1991_DJIBOUTI = new SRM_HSR_Code(_HSR_AYABELLE_LGTHS_1991_DJIBOUTI,
            "HSR_AYABELLE_LGTHS_1991_DJIBOUTI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BEACON_E_1945_IWO_JIMA_ISLAND = new SRM_HSR_Code(_HSR_BEACON_E_1945_IWO_JIMA_ISLAND,
            "HSR_BEACON_E_1945_IWO_JIMA_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BELGIUM_1972_BELGIUM = new SRM_HSR_Code(_HSR_BELGIUM_1972_BELGIUM,
            "HSR_BELGIUM_1972_BELGIUM");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BELINDA_1988_IDENTITY = new SRM_HSR_Code(_HSR_BELINDA_1988_IDENTITY,
            "HSR_BELINDA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BELLEVUE_IGN_1987_EFATE_ERROMANGO_ISLANDS =
            new SRM_HSR_Code(_HSR_BELLEVUE_IGN_1987_EFATE_ERROMANGO_ISLANDS,
                    "HSR_BELLEVUE_IGN_1987_EFATE_ERROMANGO_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BERMUDA_1957_BERMUDA = new SRM_HSR_Code(_HSR_BERMUDA_1957_BERMUDA,
            "HSR_BERMUDA_1957_BERMUDA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BERN_1898_SWITZERLAND = new SRM_HSR_Code(_HSR_BERN_1898_SWITZERLAND,
            "HSR_BERN_1898_SWITZERLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BERN_1898_PM_BERN_SWITZERLAND = new SRM_HSR_Code(_HSR_BERN_1898_PM_BERN_SWITZERLAND,
            "HSR_BERN_1898_PM_BERN_SWITZERLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BIANCA_1988_IDENTITY = new SRM_HSR_Code(_HSR_BIANCA_1988_IDENTITY,
            "HSR_BIANCA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BISSAU_1991_GUINEA_BISSAU = new SRM_HSR_Code(_HSR_BISSAU_1991_GUINEA_BISSAU,
            "HSR_BISSAU_1991_GUINEA_BISSAU");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BOG_OBS_1987_COLUMBIA = new SRM_HSR_Code(_HSR_BOG_OBS_1987_COLUMBIA,
            "HSR_BOG_OBS_1987_COLUMBIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BOG_OBS_1987_PM_BOG_COLUMBIA = new SRM_HSR_Code(_HSR_BOG_OBS_1987_PM_BOG_COLUMBIA,
            "HSR_BOG_OBS_1987_PM_BOG_COLUMBIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_BUKIT_RIMPAH_1987_BANGKA_BELITUNG_ISLANDS =
            new SRM_HSR_Code(_HSR_BUKIT_RIMPAH_1987_BANGKA_BELITUNG_ISLANDS,
                    "HSR_BUKIT_RIMPAH_1987_BANGKA_BELITUNG_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CALLISTO_2000_IDENTITY = new SRM_HSR_Code(_HSR_CALLISTO_2000_IDENTITY,
            "HSR_CALLISTO_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CALYPSO_1988_IDENTITY = new SRM_HSR_Code(_HSR_CALYPSO_1988_IDENTITY,
            "HSR_CALYPSO_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CAMP_AREA_1987_MCMURDO_CAMP = new SRM_HSR_Code(_HSR_CAMP_AREA_1987_MCMURDO_CAMP,
            "HSR_CAMP_AREA_1987_MCMURDO_CAMP");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CAMPO_INCHAUSPE_1969_ARGENTINA = new SRM_HSR_Code(_HSR_CAMPO_INCHAUSPE_1969_ARGENTINA,
            "HSR_CAMPO_INCHAUSPE_1969_ARGENTINA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CANTON_1966_PHOENIX_ISLANDS = new SRM_HSR_Code(_HSR_CANTON_1966_PHOENIX_ISLANDS,
            "HSR_CANTON_1966_PHOENIX_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CAPE_1987_SOUTH_AFRICA = new SRM_HSR_Code(_HSR_CAPE_1987_SOUTH_AFRICA,
            "HSR_CAPE_1987_SOUTH_AFRICA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CAPE_CANAVERAL_1991_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_CAPE_CANAVERAL_1991_MEAN_SOLUTION,
            "HSR_CAPE_CANAVERAL_1991_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CARTHAGE_1987_TUNISIA = new SRM_HSR_Code(_HSR_CARTHAGE_1987_TUNISIA,
            "HSR_CARTHAGE_1987_TUNISIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CHARON_1991_IDENTITY = new SRM_HSR_Code(_HSR_CHARON_1991_IDENTITY,
            "HSR_CHARON_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CHATHAM_1971_CHATHAM_ISLANDS = new SRM_HSR_Code(_HSR_CHATHAM_1971_CHATHAM_ISLANDS,
            "HSR_CHATHAM_1971_CHATHAM_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CHUA_1987_PARAGUAY = new SRM_HSR_Code(_HSR_CHUA_1987_PARAGUAY,
            "HSR_CHUA_1987_PARAGUAY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_COAMPS_1998_IDENTITY_BY_DEFAULT = new SRM_HSR_Code(_HSR_COAMPS_1998_IDENTITY_BY_DEFAULT,
            "HSR_COAMPS_1998_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CORDELIA_1988_IDENTITY = new SRM_HSR_Code(_HSR_CORDELIA_1988_IDENTITY,
            "HSR_CORDELIA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CORREGO_ALEGRE_1987_BRAZIL = new SRM_HSR_Code(_HSR_CORREGO_ALEGRE_1987_BRAZIL,
            "HSR_CORREGO_ALEGRE_1987_BRAZIL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CRESSIDA_1988_IDENTITY = new SRM_HSR_Code(_HSR_CRESSIDA_1988_IDENTITY,
            "HSR_CRESSIDA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_CYPRUS_1935_CYPRUS = new SRM_HSR_Code(_HSR_CYPRUS_1935_CYPRUS,
            "HSR_CYPRUS_1935_CYPRUS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DABOLA_1991_GUINEA = new SRM_HSR_Code(_HSR_DABOLA_1991_GUINEA,
            "HSR_DABOLA_1991_GUINEA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DECEPTION_1993_DECEPTION_ISLAND = new SRM_HSR_Code(_HSR_DECEPTION_1993_DECEPTION_ISLAND,
            "HSR_DECEPTION_1993_DECEPTION_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DEIMOS_1988_IDENTITY = new SRM_HSR_Code(_HSR_DEIMOS_1988_IDENTITY,
            "HSR_DEIMOS_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DESDEMONA_1988_IDENTITY = new SRM_HSR_Code(_HSR_DESDEMONA_1988_IDENTITY,
            "HSR_DESDEMONA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DESPINA_1991_IDENTITY = new SRM_HSR_Code(_HSR_DESPINA_1991_IDENTITY,
            "HSR_DESPINA_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DIONE_1982_IDENTITY = new SRM_HSR_Code(_HSR_DIONE_1982_IDENTITY,
            "HSR_DIONE_1982_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DJK_1987_SUMATRA = new SRM_HSR_Code(_HSR_DJK_1987_SUMATRA,
            "HSR_DJK_1987_SUMATRA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DJK_1987_PM_DJK_SUMATRA = new SRM_HSR_Code(_HSR_DJK_1987_PM_DJK_SUMATRA,
            "HSR_DJK_1987_PM_DJK_SUMATRA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DOS_1968_GIZO_ISLAND = new SRM_HSR_Code(_HSR_DOS_1968_GIZO_ISLAND,
            "HSR_DOS_1968_GIZO_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_DOS_71_4_1987_ST_HELENA_ISLAND = new SRM_HSR_Code(_HSR_DOS_71_4_1987_ST_HELENA_ISLAND,
            "HSR_DOS_71_4_1987_ST_HELENA_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EASTER_1967_EASTER_ISLAND = new SRM_HSR_Code(_HSR_EASTER_1967_EASTER_ISLAND,
            "HSR_EASTER_1967_EASTER_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ENCELADUS_1994_IDENTITY = new SRM_HSR_Code(_HSR_ENCELADUS_1994_IDENTITY,
            "HSR_ENCELADUS_1994_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EPIMETHEUS_1988_IDENTITY = new SRM_HSR_Code(_HSR_EPIMETHEUS_1988_IDENTITY,
            "HSR_EPIMETHEUS_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EROS_2000_IDENTITY = new SRM_HSR_Code(_HSR_EROS_2000_IDENTITY,
            "HSR_EROS_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ESTONIA_1937_ESTONIA = new SRM_HSR_Code(_HSR_ESTONIA_1937_ESTONIA,
            "HSR_ESTONIA_1937_ESTONIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ETRS_1989_IDENTITY_BY_MEASUREMENT = new SRM_HSR_Code(_HSR_ETRS_1989_IDENTITY_BY_MEASUREMENT,
            "HSR_ETRS_1989_IDENTITY_BY_MEASUREMENT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPA_2000_IDENTITY = new SRM_HSR_Code(_HSR_EUROPA_2000_IDENTITY,
            "HSR_EUROPA_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_ALGERIA = new SRM_HSR_Code(_HSR_EUROPE_1950_ALGERIA,
            "HSR_EUROPE_1950_ALGERIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_BALEARIC_ISLANDS = new SRM_HSR_Code(_HSR_EUROPE_1950_BALEARIC_ISLANDS,
            "HSR_EUROPE_1950_BALEARIC_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_CHANNEL_ISLANDS = new SRM_HSR_Code(_HSR_EUROPE_1950_CHANNEL_ISLANDS,
            "HSR_EUROPE_1950_CHANNEL_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_3_CYPRUS = new SRM_HSR_Code(_HSR_EUROPE_1950_3_CYPRUS,
            "HSR_EUROPE_1950_3_CYPRUS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_7_CYPRUS = new SRM_HSR_Code(_HSR_EUROPE_1950_7_CYPRUS,
            "HSR_EUROPE_1950_7_CYPRUS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_DENMARK = new SRM_HSR_Code(_HSR_EUROPE_1950_DENMARK,
            "HSR_EUROPE_1950_DENMARK");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_EGYPT = new SRM_HSR_Code(_HSR_EUROPE_1950_EGYPT,
            "HSR_EUROPE_1950_EGYPT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_ENGLAND_SCOTLAND = new SRM_HSR_Code(_HSR_EUROPE_1950_ENGLAND_SCOTLAND,
            "HSR_EUROPE_1950_ENGLAND_SCOTLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_GIBRALTAR = new SRM_HSR_Code(_HSR_EUROPE_1950_GIBRALTAR,
            "HSR_EUROPE_1950_GIBRALTAR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_GREECE = new SRM_HSR_Code(_HSR_EUROPE_1950_GREECE,
            "HSR_EUROPE_1950_GREECE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_IRAN = new SRM_HSR_Code(_HSR_EUROPE_1950_IRAN,
            "HSR_EUROPE_1950_IRAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_IRAQ = new SRM_HSR_Code(_HSR_EUROPE_1950_IRAQ,
            "HSR_EUROPE_1950_IRAQ");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_IRELAND = new SRM_HSR_Code(_HSR_EUROPE_1950_IRELAND,
            "HSR_EUROPE_1950_IRELAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_LEBANON = new SRM_HSR_Code(_HSR_EUROPE_1950_LEBANON,
            "HSR_EUROPE_1950_LEBANON");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_MALTA = new SRM_HSR_Code(_HSR_EUROPE_1950_MALTA,
            "HSR_EUROPE_1950_MALTA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_EUROPE_1950_MEAN_SOLUTION,
            "HSR_EUROPE_1950_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_NORWAY = new SRM_HSR_Code(_HSR_EUROPE_1950_NORWAY,
            "HSR_EUROPE_1950_NORWAY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_OMAN = new SRM_HSR_Code(_HSR_EUROPE_1950_OMAN,
            "HSR_EUROPE_1950_OMAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_PORTUGAL = new SRM_HSR_Code(_HSR_EUROPE_1950_PORTUGAL,
            "HSR_EUROPE_1950_PORTUGAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_PORTUGAL_SPAIN = new SRM_HSR_Code(_HSR_EUROPE_1950_PORTUGAL_SPAIN,
            "HSR_EUROPE_1950_PORTUGAL_SPAIN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_SARDINIA = new SRM_HSR_Code(_HSR_EUROPE_1950_SARDINIA,
            "HSR_EUROPE_1950_SARDINIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_SICILY = new SRM_HSR_Code(_HSR_EUROPE_1950_SICILY,
            "HSR_EUROPE_1950_SICILY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_SPAIN_EXCEPT_NORTHWEST = new SRM_HSR_Code(_HSR_EUROPE_1950_SPAIN_EXCEPT_NORTHWEST,
            "HSR_EUROPE_1950_SPAIN_EXCEPT_NORTHWEST");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_SPAIN_NW = new SRM_HSR_Code(_HSR_EUROPE_1950_SPAIN_NW,
            "HSR_EUROPE_1950_SPAIN_NW");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_TUNISIA = new SRM_HSR_Code(_HSR_EUROPE_1950_TUNISIA,
            "HSR_EUROPE_1950_TUNISIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_TURKEY = new SRM_HSR_Code(_HSR_EUROPE_1950_TURKEY,
            "HSR_EUROPE_1950_TURKEY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_W_EUROPE_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_EUROPE_1950_W_EUROPE_MEAN_SOLUTION,
            "HSR_EUROPE_1950_W_EUROPE_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1950_YUGOSLAVIA_NORTH = new SRM_HSR_Code(_HSR_EUROPE_1950_YUGOSLAVIA_NORTH,
            "HSR_EUROPE_1950_YUGOSLAVIA_NORTH");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1979_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_EUROPE_1979_MEAN_SOLUTION,
            "HSR_EUROPE_1979_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_EUROPE_1979_PORTUGAL = new SRM_HSR_Code(_HSR_EUROPE_1979_PORTUGAL,
            "HSR_EUROPE_1979_PORTUGAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_FAHUD_1987_3_OMAN = new SRM_HSR_Code(_HSR_FAHUD_1987_3_OMAN,
            "HSR_FAHUD_1987_3_OMAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_FAHUD_1987_7_OMAN = new SRM_HSR_Code(_HSR_FAHUD_1987_7_OMAN,
            "HSR_FAHUD_1987_7_OMAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_FORT_THOMAS_1955_ST_KITTS_NEVIS_LEEWARD_ISLANDS =
            new SRM_HSR_Code(_HSR_FORT_THOMAS_1955_ST_KITTS_NEVIS_LEEWARD_ISLANDS,
                    "HSR_FORT_THOMAS_1955_ST_KITTS_NEVIS_LEEWARD_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GALATEA_1991_IDENTITY = new SRM_HSR_Code(_HSR_GALATEA_1991_IDENTITY,
            "HSR_GALATEA_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GAN_1970_MALDIVES = new SRM_HSR_Code(_HSR_GAN_1970_MALDIVES,
            "HSR_GAN_1970_MALDIVES");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GANYMEDE_2000_IDENTITY = new SRM_HSR_Code(_HSR_GANYMEDE_2000_IDENTITY,
            "HSR_GANYMEDE_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GANYMEDE_MAGNETIC_2000_GALILEO = new SRM_HSR_Code(_HSR_GANYMEDE_MAGNETIC_2000_GALILEO,
            "HSR_GANYMEDE_MAGNETIC_2000_GALILEO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GASPRA_1991_IDENTITY = new SRM_HSR_Code(_HSR_GASPRA_1991_IDENTITY,
            "HSR_GASPRA_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GDA_1994_IDENTITY_BY_MEASUREMENT = new SRM_HSR_Code(_HSR_GDA_1994_IDENTITY_BY_MEASUREMENT,
            "HSR_GDA_1994_IDENTITY_BY_MEASUREMENT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEODETIC_DATUM_1949_3_NEW_ZEALAND = new SRM_HSR_Code(_HSR_GEODETIC_DATUM_1949_3_NEW_ZEALAND,
            "HSR_GEODETIC_DATUM_1949_3_NEW_ZEALAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEODETIC_DATUM_1949_7_NEW_ZEALAND = new SRM_HSR_Code(_HSR_GEODETIC_DATUM_1949_7_NEW_ZEALAND,
            "HSR_GEODETIC_DATUM_1949_7_NEW_ZEALAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1945_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1945_DGRF,
            "HSR_GEOMAGNETIC_1945_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1950_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1950_DGRF,
            "HSR_GEOMAGNETIC_1950_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1955_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1955_DGRF,
            "HSR_GEOMAGNETIC_1955_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1960_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1960_DGRF,
            "HSR_GEOMAGNETIC_1960_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1965_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1965_DGRF,
            "HSR_GEOMAGNETIC_1965_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1970_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1970_DGRF,
            "HSR_GEOMAGNETIC_1970_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1975_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1975_DGRF,
            "HSR_GEOMAGNETIC_1975_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1980_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1980_DGRF,
            "HSR_GEOMAGNETIC_1980_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1985_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1985_DGRF,
            "HSR_GEOMAGNETIC_1985_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1990_DGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1990_DGRF,
            "HSR_GEOMAGNETIC_1990_DGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_1995_IGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_1995_IGRF,
            "HSR_GEOMAGNETIC_1995_IGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GEOMAGNETIC_2000_IGRF = new SRM_HSR_Code(_HSR_GEOMAGNETIC_2000_IGRF,
            "HSR_GEOMAGNETIC_2000_IGRF");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GGRS_1987_GREECE = new SRM_HSR_Code(_HSR_GGRS_1987_GREECE,
            "HSR_GGRS_1987_GREECE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GRACIOSA_BASE_SW_1948_CENTRAL_AZORES = new SRM_HSR_Code(_HSR_GRACIOSA_BASE_SW_1948_CENTRAL_AZORES,
            "HSR_GRACIOSA_BASE_SW_1948_CENTRAL_AZORES");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GUAM_1963_GUAM = new SRM_HSR_Code(_HSR_GUAM_1963_GUAM,
            "HSR_GUAM_1963_GUAM");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GUNONG_SEGARA_1987_KALIMANTAN_ISLAND = new SRM_HSR_Code(_HSR_GUNONG_SEGARA_1987_KALIMANTAN_ISLAND,
            "HSR_GUNONG_SEGARA_1987_KALIMANTAN_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_GUX_1_1987_GUADALCANAL_ISLAND = new SRM_HSR_Code(_HSR_GUX_1_1987_GUADALCANAL_ISLAND,
            "HSR_GUX_1_1987_GUADALCANAL_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HARTEBEESTHOCK_1994_SOUTH_AFRICA = new SRM_HSR_Code(_HSR_HARTEBEESTHOCK_1994_SOUTH_AFRICA,
            "HSR_HARTEBEESTHOCK_1994_SOUTH_AFRICA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HELENE_1992_IDENTITY = new SRM_HSR_Code(_HSR_HELENE_1992_IDENTITY,
            "HSR_HELENE_1992_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HERAT_NORTH_1987_AFGHANISTAN = new SRM_HSR_Code(_HSR_HERAT_NORTH_1987_AFGHANISTAN,
            "HSR_HERAT_NORTH_1987_AFGHANISTAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HERMANNSKOGEL_1871_AUSTRIA = new SRM_HSR_Code(_HSR_HERMANNSKOGEL_1871_AUSTRIA,
            "HSR_HERMANNSKOGEL_1871_AUSTRIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HERMANNSKOGEL_1871_3_YUGOSLAVIA = new SRM_HSR_Code(_HSR_HERMANNSKOGEL_1871_3_YUGOSLAVIA,
            "HSR_HERMANNSKOGEL_1871_3_YUGOSLAVIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HERMANNSKOGEL_1871_7_YUGOSLAVIA = new SRM_HSR_Code(_HSR_HERMANNSKOGEL_1871_7_YUGOSLAVIA,
            "HSR_HERMANNSKOGEL_1871_7_YUGOSLAVIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HJORSEY_1955_ICELAND = new SRM_HSR_Code(_HSR_HJORSEY_1955_ICELAND,
            "HSR_HJORSEY_1955_ICELAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HONG_KONG_1963_HONG_KONG = new SRM_HSR_Code(_HSR_HONG_KONG_1963_HONG_KONG,
            "HSR_HONG_KONG_1963_HONG_KONG");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HONG_KONG_1980_HONG_KONG = new SRM_HSR_Code(_HSR_HONG_KONG_1980_HONG_KONG,
            "HSR_HONG_KONG_1980_HONG_KONG");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HU_TZU_SHAN_1991_TAIWAN = new SRM_HSR_Code(_HSR_HU_TZU_SHAN_1991_TAIWAN,
            "HSR_HU_TZU_SHAN_1991_TAIWAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_HUNGARIAN_1972_HUNGARY = new SRM_HSR_Code(_HSR_HUNGARIAN_1972_HUNGARY,
            "HSR_HUNGARIAN_1972_HUNGARY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_IAPETUS_1988_IDENTITY = new SRM_HSR_Code(_HSR_IAPETUS_1988_IDENTITY,
            "HSR_IAPETUS_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_IDA_1991_IDENTITY = new SRM_HSR_Code(_HSR_IDA_1991_IDENTITY,
            "HSR_IDA_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDIAN_1916_3_BANGLADESH = new SRM_HSR_Code(_HSR_INDIAN_1916_3_BANGLADESH,
            "HSR_INDIAN_1916_3_BANGLADESH");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDIAN_1916_7_BANGLADESH = new SRM_HSR_Code(_HSR_INDIAN_1916_7_BANGLADESH,
            "HSR_INDIAN_1916_7_BANGLADESH");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDIAN_1954_THAILAND = new SRM_HSR_Code(_HSR_INDIAN_1954_THAILAND,
            "HSR_INDIAN_1954_THAILAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDIAN_1956_INDIA_NEPAL = new SRM_HSR_Code(_HSR_INDIAN_1956_INDIA_NEPAL,
            "HSR_INDIAN_1956_INDIA_NEPAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDIAN_1960_CON_SON_ISLAND = new SRM_HSR_Code(_HSR_INDIAN_1960_CON_SON_ISLAND,
            "HSR_INDIAN_1960_CON_SON_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDIAN_1960_VIETNAM_16_N = new SRM_HSR_Code(_HSR_INDIAN_1960_VIETNAM_16_N,
            "HSR_INDIAN_1960_VIETNAM_16_N");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDIAN_1962_PAKISTAN = new SRM_HSR_Code(_HSR_INDIAN_1962_PAKISTAN,
            "HSR_INDIAN_1962_PAKISTAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDIAN_1975_1991_THAILAND = new SRM_HSR_Code(_HSR_INDIAN_1975_1991_THAILAND,
            "HSR_INDIAN_1975_1991_THAILAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDIAN_1975_1997_THAILAND = new SRM_HSR_Code(_HSR_INDIAN_1975_1997_THAILAND,
            "HSR_INDIAN_1975_1997_THAILAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_INDONESIAN_1974_INDONESIA = new SRM_HSR_Code(_HSR_INDONESIAN_1974_INDONESIA,
            "HSR_INDONESIAN_1974_INDONESIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_IO_2000_IDENTITY = new SRM_HSR_Code(_HSR_IO_2000_IDENTITY,
            "HSR_IO_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_IRAQ_KUWAIT_BNDRY_1992_IRAQ_KUWAIT = new SRM_HSR_Code(_HSR_IRAQ_KUWAIT_BNDRY_1992_IRAQ_KUWAIT,
            "HSR_IRAQ_KUWAIT_BNDRY_1992_IRAQ_KUWAIT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_IRELAND_1965_3_IRELAND = new SRM_HSR_Code(_HSR_IRELAND_1965_3_IRELAND,
            "HSR_IRELAND_1965_3_IRELAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_IRELAND_1965_7_IRELAND = new SRM_HSR_Code(_HSR_IRELAND_1965_7_IRELAND,
            "HSR_IRELAND_1965_7_IRELAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ISTS_061_1968_SOUTH_GEORGIA_ISLAND = new SRM_HSR_Code(_HSR_ISTS_061_1968_SOUTH_GEORGIA_ISLAND,
            "HSR_ISTS_061_1968_SOUTH_GEORGIA_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ISTS_073_1969_DIEGO_GARCIA = new SRM_HSR_Code(_HSR_ISTS_073_1969_DIEGO_GARCIA,
            "HSR_ISTS_073_1969_DIEGO_GARCIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_JANUS_1988_IDENTITY = new SRM_HSR_Code(_HSR_JANUS_1988_IDENTITY,
            "HSR_JANUS_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_JGD_2000_IDENTITY_BY_MEASUREMENT = new SRM_HSR_Code(_HSR_JGD_2000_IDENTITY_BY_MEASUREMENT,
            "HSR_JGD_2000_IDENTITY_BY_MEASUREMENT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_JOHNSTON_1961_JOHNSTON_ISLAND = new SRM_HSR_Code(_HSR_JOHNSTON_1961_JOHNSTON_ISLAND,
            "HSR_JOHNSTON_1961_JOHNSTON_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_JULIET_1988_IDENTITY = new SRM_HSR_Code(_HSR_JULIET_1988_IDENTITY,
            "HSR_JULIET_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_JUPITER_1988_IDENTITY = new SRM_HSR_Code(_HSR_JUPITER_1988_IDENTITY,
            "HSR_JUPITER_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_JUPITER_MAGNETIC_1992_VOYAGER = new SRM_HSR_Code(_HSR_JUPITER_MAGNETIC_1992_VOYAGER,
            "HSR_JUPITER_MAGNETIC_1992_VOYAGER");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_KANDAWALA_1987_3_SRI_LANKA = new SRM_HSR_Code(_HSR_KANDAWALA_1987_3_SRI_LANKA,
            "HSR_KANDAWALA_1987_3_SRI_LANKA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_KANDAWALA_1987_7_SRI_LANKA = new SRM_HSR_Code(_HSR_KANDAWALA_1987_7_SRI_LANKA,
            "HSR_KANDAWALA_1987_7_SRI_LANKA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_KERGUELEN_1949_KERGUELEN_ISLAND = new SRM_HSR_Code(_HSR_KERGUELEN_1949_KERGUELEN_ISLAND,
            "HSR_KERGUELEN_1949_KERGUELEN_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_KERTAU_1948_3_W_MALAYSIA_SINGAPORE = new SRM_HSR_Code(_HSR_KERTAU_1948_3_W_MALAYSIA_SINGAPORE,
            "HSR_KERTAU_1948_3_W_MALAYSIA_SINGAPORE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_KERTAU_1948_7_W_MALAYSIA_SINGAPORE = new SRM_HSR_Code(_HSR_KERTAU_1948_7_W_MALAYSIA_SINGAPORE,
            "HSR_KERTAU_1948_7_W_MALAYSIA_SINGAPORE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_KOREAN_GEODETIC_1995_SOUTH_KOREA = new SRM_HSR_Code(_HSR_KOREAN_GEODETIC_1995_SOUTH_KOREA,
            "HSR_KOREAN_GEODETIC_1995_SOUTH_KOREA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_KUSAIE_1951_CAROLINE_ISLANDS = new SRM_HSR_Code(_HSR_KUSAIE_1951_CAROLINE_ISLANDS,
            "HSR_KUSAIE_1951_CAROLINE_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LANDESVERMESSUNG_1995_SWITZERLAND = new SRM_HSR_Code(_HSR_LANDESVERMESSUNG_1995_SWITZERLAND,
            "HSR_LANDESVERMESSUNG_1995_SWITZERLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LARISSA_1991_IDENTITY = new SRM_HSR_Code(_HSR_LARISSA_1991_IDENTITY,
            "HSR_LARISSA_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LC5_1961_CAYMAN_BRAC_ISLAND = new SRM_HSR_Code(_HSR_LC5_1961_CAYMAN_BRAC_ISLAND,
            "HSR_LC5_1961_CAYMAN_BRAC_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LEIGON_1991_3_GHANA = new SRM_HSR_Code(_HSR_LEIGON_1991_3_GHANA,
            "HSR_LEIGON_1991_3_GHANA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LEIGON_1991_7_GHANA = new SRM_HSR_Code(_HSR_LEIGON_1991_7_GHANA,
            "HSR_LEIGON_1991_7_GHANA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LIBERIA_1964_LIBERIA = new SRM_HSR_Code(_HSR_LIBERIA_1964_LIBERIA,
            "HSR_LIBERIA_1964_LIBERIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LISBON_D73_PORTUGAL = new SRM_HSR_Code(_HSR_LISBON_D73_PORTUGAL,
            "HSR_LISBON_D73_PORTUGAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LKS_1994_LITHUANIA = new SRM_HSR_Code(_HSR_LKS_1994_LITHUANIA,
            "HSR_LKS_1994_LITHUANIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LUZON_1987_MINDANAO_ISLAND = new SRM_HSR_Code(_HSR_LUZON_1987_MINDANAO_ISLAND,
            "HSR_LUZON_1987_MINDANAO_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_LUZON_1987_PHILIPPINES_EXCLUDING_MINDANAO_ISLAND =
            new SRM_HSR_Code(_HSR_LUZON_1987_PHILIPPINES_EXCLUDING_MINDANAO_ISLAND,
                    "HSR_LUZON_1987_PHILIPPINES_EXCLUDING_MINDANAO_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_M_PORALOKO_1991_GABON = new SRM_HSR_Code(_HSR_M_PORALOKO_1991_GABON,
            "HSR_M_PORALOKO_1991_GABON");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MAHE_1971_MAHE_ISLAND = new SRM_HSR_Code(_HSR_MAHE_1971_MAHE_ISLAND,
            "HSR_MAHE_1971_MAHE_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MARCUS_STATION_1952_MARCUS_ISLANDS = new SRM_HSR_Code(_HSR_MARCUS_STATION_1952_MARCUS_ISLANDS,
            "HSR_MARCUS_STATION_1952_MARCUS_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MARS_2000_IDENTITY = new SRM_HSR_Code(_HSR_MARS_2000_IDENTITY,
            "HSR_MARS_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MARS_SPHERE_2000_GLOBAL = new SRM_HSR_Code(_HSR_MARS_SPHERE_2000_GLOBAL,
            "HSR_MARS_SPHERE_2000_GLOBAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MASS_1999_IDENTITY_BY_DEFAULT = new SRM_HSR_Code(_HSR_MASS_1999_IDENTITY_BY_DEFAULT,
            "HSR_MASS_1999_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MASSAWA_1987_ERITREA_ETHIOPIA = new SRM_HSR_Code(_HSR_MASSAWA_1987_ERITREA_ETHIOPIA,
            "HSR_MASSAWA_1987_ERITREA_ETHIOPIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MERCHICH_1987_MOROCCO = new SRM_HSR_Code(_HSR_MERCHICH_1987_MOROCCO,
            "HSR_MERCHICH_1987_MOROCCO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MERCURY_1988_IDENTITY = new SRM_HSR_Code(_HSR_MERCURY_1988_IDENTITY,
            "HSR_MERCURY_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_METIS_2000_IDENTITY = new SRM_HSR_Code(_HSR_METIS_2000_IDENTITY,
            "HSR_METIS_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MIDWAY_1961_MIDWAY_ISLANDS = new SRM_HSR_Code(_HSR_MIDWAY_1961_MIDWAY_ISLANDS,
            "HSR_MIDWAY_1961_MIDWAY_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MIMAS_1994_IDENTITY = new SRM_HSR_Code(_HSR_MIMAS_1994_IDENTITY,
            "HSR_MIMAS_1994_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MINNA_1991_CAMEROON = new SRM_HSR_Code(_HSR_MINNA_1991_CAMEROON,
            "HSR_MINNA_1991_CAMEROON");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MINNA_1991_NIGERIA = new SRM_HSR_Code(_HSR_MINNA_1991_NIGERIA,
            "HSR_MINNA_1991_NIGERIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MIRANDA_1988_IDENTITY = new SRM_HSR_Code(_HSR_MIRANDA_1988_IDENTITY,
            "HSR_MIRANDA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MM5_1997_IDENTITY_BY_DEFAULT = new SRM_HSR_Code(_HSR_MM5_1997_IDENTITY_BY_DEFAULT,
            "HSR_MM5_1997_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MODTRAN_MLAT_N_1989_IDENTITY_BY_DEFAULT =
            new SRM_HSR_Code(_HSR_MODTRAN_MLAT_N_1989_IDENTITY_BY_DEFAULT,
                    "HSR_MODTRAN_MLAT_N_1989_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MODTRAN_MLAT_S_1989_IDENTITY_BY_DEFAULT =
            new SRM_HSR_Code(_HSR_MODTRAN_MLAT_S_1989_IDENTITY_BY_DEFAULT,
                    "HSR_MODTRAN_MLAT_S_1989_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MODTRAN_SARC_N_1989_IDENTITY_BY_DEFAULT =
            new SRM_HSR_Code(_HSR_MODTRAN_SARC_N_1989_IDENTITY_BY_DEFAULT,
                    "HSR_MODTRAN_SARC_N_1989_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MODTRAN_SARC_S_1989_IDENTITY_BY_DEFAULT =
            new SRM_HSR_Code(_HSR_MODTRAN_SARC_S_1989_IDENTITY_BY_DEFAULT,
                    "HSR_MODTRAN_SARC_S_1989_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MODTRAN_TROPICAL_1989_IDENTITY_BY_DEFAULT =
            new SRM_HSR_Code(_HSR_MODTRAN_TROPICAL_1989_IDENTITY_BY_DEFAULT,
                    "HSR_MODTRAN_TROPICAL_1989_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MONTSERRAT_1958_MONTSERRAT_LEEWARD_ISLANDS =
            new SRM_HSR_Code(_HSR_MONTSERRAT_1958_MONTSERRAT_LEEWARD_ISLANDS,
                    "HSR_MONTSERRAT_1958_MONTSERRAT_LEEWARD_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MOON_1991_IDENTITY = new SRM_HSR_Code(_HSR_MOON_1991_IDENTITY,
            "HSR_MOON_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_MG_FLAT_EARTH_1989_IDENTITY_BY_DEFAULT = new SRM_HSR_Code(_HSR_MG_FLAT_EARTH_1989_IDENTITY_BY_DEFAULT,
            "HSR_MG_FLAT_EARTH_1989_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_ALASKA_EXCLUDING_ALEUTIAN_ISLANDS =
            new SRM_HSR_Code(_HSR_N_AM_1927_ALASKA_EXCLUDING_ALEUTIAN_ISLANDS,
                    "HSR_N_AM_1927_ALASKA_EXCLUDING_ALEUTIAN_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_ALBERTA_BRITISH_COLUMBIA = new SRM_HSR_Code(_HSR_N_AM_1927_ALBERTA_BRITISH_COLUMBIA,
            "HSR_N_AM_1927_ALBERTA_BRITISH_COLUMBIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_BAHAMAS_EXCLUDING_SAN_SALVADOR_ISLAND =
            new SRM_HSR_Code(_HSR_N_AM_1927_BAHAMAS_EXCLUDING_SAN_SALVADOR_ISLAND,
                    "HSR_N_AM_1927_BAHAMAS_EXCLUDING_SAN_SALVADOR_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_CANADA = new SRM_HSR_Code(_HSR_N_AM_1927_CANADA,
            "HSR_N_AM_1927_CANADA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_CANAL_ZONE = new SRM_HSR_Code(_HSR_N_AM_1927_CANAL_ZONE,
            "HSR_N_AM_1927_CANAL_ZONE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_CARIBBEAN = new SRM_HSR_Code(_HSR_N_AM_1927_CARIBBEAN,
            "HSR_N_AM_1927_CARIBBEAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_CENTRAL_AMERICA = new SRM_HSR_Code(_HSR_N_AM_1927_CENTRAL_AMERICA,
            "HSR_N_AM_1927_CENTRAL_AMERICA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_CONTINENTAL_US = new SRM_HSR_Code(_HSR_N_AM_1927_CONTINENTAL_US,
            "HSR_N_AM_1927_CONTINENTAL_US");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_CUBA = new SRM_HSR_Code(_HSR_N_AM_1927_CUBA,
            "HSR_N_AM_1927_CUBA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_EAST_ALEUTIAN_ISLANDS = new SRM_HSR_Code(_HSR_N_AM_1927_EAST_ALEUTIAN_ISLANDS,
            "HSR_N_AM_1927_EAST_ALEUTIAN_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_EASTERN_CANADA = new SRM_HSR_Code(_HSR_N_AM_1927_EASTERN_CANADA,
            "HSR_N_AM_1927_EASTERN_CANADA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_EASTERN_US = new SRM_HSR_Code(_HSR_N_AM_1927_EASTERN_US,
            "HSR_N_AM_1927_EASTERN_US");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_HAYES_PENINSULA = new SRM_HSR_Code(_HSR_N_AM_1927_HAYES_PENINSULA,
            "HSR_N_AM_1927_HAYES_PENINSULA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_MANITOBA_ONTARIO = new SRM_HSR_Code(_HSR_N_AM_1927_MANITOBA_ONTARIO,
            "HSR_N_AM_1927_MANITOBA_ONTARIO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_MEXICO = new SRM_HSR_Code(_HSR_N_AM_1927_MEXICO,
            "HSR_N_AM_1927_MEXICO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_NORTHWEST_TERRITORIES_SASKATCHEWAN =
            new SRM_HSR_Code(_HSR_N_AM_1927_NORTHWEST_TERRITORIES_SASKATCHEWAN,
                    "HSR_N_AM_1927_NORTHWEST_TERRITORIES_SASKATCHEWAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_SAN_SALVADOR_ISLAND = new SRM_HSR_Code(_HSR_N_AM_1927_SAN_SALVADOR_ISLAND,
            "HSR_N_AM_1927_SAN_SALVADOR_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_WEST_ALEUTIAN_ISLANDS = new SRM_HSR_Code(_HSR_N_AM_1927_WEST_ALEUTIAN_ISLANDS,
            "HSR_N_AM_1927_WEST_ALEUTIAN_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_WESTERN_US = new SRM_HSR_Code(_HSR_N_AM_1927_WESTERN_US,
            "HSR_N_AM_1927_WESTERN_US");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1927_YUKON = new SRM_HSR_Code(_HSR_N_AM_1927_YUKON,
            "HSR_N_AM_1927_YUKON");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1983_ALASKA_EXCLUDING_ALEUTIAN_ISLANDS =
            new SRM_HSR_Code(_HSR_N_AM_1983_ALASKA_EXCLUDING_ALEUTIAN_ISLANDS,
                    "HSR_N_AM_1983_ALASKA_EXCLUDING_ALEUTIAN_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1983_ALEUTIAN_ISLANDS = new SRM_HSR_Code(_HSR_N_AM_1983_ALEUTIAN_ISLANDS,
            "HSR_N_AM_1983_ALEUTIAN_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1983_CANADA = new SRM_HSR_Code(_HSR_N_AM_1983_CANADA,
            "HSR_N_AM_1983_CANADA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1983_CONTINENTAL_US = new SRM_HSR_Code(_HSR_N_AM_1983_CONTINENTAL_US,
            "HSR_N_AM_1983_CONTINENTAL_US");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1983_HAWAII = new SRM_HSR_Code(_HSR_N_AM_1983_HAWAII,
            "HSR_N_AM_1983_HAWAII");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_AM_1983_MEXICO_CENTRAL_AMERICA = new SRM_HSR_Code(_HSR_N_AM_1983_MEXICO_CENTRAL_AMERICA,
            "HSR_N_AM_1983_MEXICO_CENTRAL_AMERICA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_N_SAHARA_1959_ALGERIA = new SRM_HSR_Code(_HSR_N_SAHARA_1959_ALGERIA,
            "HSR_N_SAHARA_1959_ALGERIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NAHRWAN_1987_MASIRAH_ISLAND = new SRM_HSR_Code(_HSR_NAHRWAN_1987_MASIRAH_ISLAND,
            "HSR_NAHRWAN_1987_MASIRAH_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NAHRWAN_1987_SAUDI_ARABIA = new SRM_HSR_Code(_HSR_NAHRWAN_1987_SAUDI_ARABIA,
            "HSR_NAHRWAN_1987_SAUDI_ARABIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NAHRWAN_1987_UNITED_ARAB_EMIRATES = new SRM_HSR_Code(_HSR_NAHRWAN_1987_UNITED_ARAB_EMIRATES,
            "HSR_NAHRWAN_1987_UNITED_ARAB_EMIRATES");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NAIAD_1991_IDENTITY = new SRM_HSR_Code(_HSR_NAIAD_1991_IDENTITY,
            "HSR_NAIAD_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NAPARIMA_1991_TRINIDAD_TOBAGO = new SRM_HSR_Code(_HSR_NAPARIMA_1991_TRINIDAD_TOBAGO,
            "HSR_NAPARIMA_1991_TRINIDAD_TOBAGO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NEPTUNE_1991_IDENTITY = new SRM_HSR_Code(_HSR_NEPTUNE_1991_IDENTITY,
            "HSR_NEPTUNE_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NEPTUNE_MAGNETIC_1993_VOYAGER = new SRM_HSR_Code(_HSR_NEPTUNE_MAGNETIC_1993_VOYAGER,
            "HSR_NEPTUNE_MAGNETIC_1993_VOYAGER");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NOGAPS_1988_IDENTITY_BY_DEFAULT = new SRM_HSR_Code(_HSR_NOGAPS_1988_IDENTITY_BY_DEFAULT,
            "HSR_NOGAPS_1988_IDENTITY_BY_DEFAULT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NTF_1896_FRANCE = new SRM_HSR_Code(_HSR_NTF_1896_FRANCE,
            "HSR_NTF_1896_FRANCE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_NTF_1896_PM_PARIS_FRANCE = new SRM_HSR_Code(_HSR_NTF_1896_PM_PARIS_FRANCE,
            "HSR_NTF_1896_PM_PARIS_FRANCE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OBERON_1988_IDENTITY = new SRM_HSR_Code(_HSR_OBERON_1988_IDENTITY,
            "HSR_OBERON_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OBSERV_METEORO_1939_CORVO_FLORES_ISLANDS =
            new SRM_HSR_Code(_HSR_OBSERV_METEORO_1939_CORVO_FLORES_ISLANDS,
                    "HSR_OBSERV_METEORO_1939_CORVO_FLORES_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_EGYPTIAN_1907_EGYPT = new SRM_HSR_Code(_HSR_OLD_EGYPTIAN_1907_EGYPT,
            "HSR_OLD_EGYPTIAN_1907_EGYPT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_CLARKE_1987_HAWAII = new SRM_HSR_Code(_HSR_OLD_HAW_CLARKE_1987_HAWAII,
            "HSR_OLD_HAW_CLARKE_1987_HAWAII");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_CLARKE_1987_KAUAI = new SRM_HSR_Code(_HSR_OLD_HAW_CLARKE_1987_KAUAI,
            "HSR_OLD_HAW_CLARKE_1987_KAUAI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_CLARKE_1987_MAUI = new SRM_HSR_Code(_HSR_OLD_HAW_CLARKE_1987_MAUI,
            "HSR_OLD_HAW_CLARKE_1987_MAUI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_CLARKE_1987_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_OLD_HAW_CLARKE_1987_MEAN_SOLUTION,
            "HSR_OLD_HAW_CLARKE_1987_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_CLARKE_1987_OAHU = new SRM_HSR_Code(_HSR_OLD_HAW_CLARKE_1987_OAHU,
            "HSR_OLD_HAW_CLARKE_1987_OAHU");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_INT_1987_HAWAII = new SRM_HSR_Code(_HSR_OLD_HAW_INT_1987_HAWAII,
            "HSR_OLD_HAW_INT_1987_HAWAII");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_INT_1987_KAUAI = new SRM_HSR_Code(_HSR_OLD_HAW_INT_1987_KAUAI,
            "HSR_OLD_HAW_INT_1987_KAUAI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_INT_1987_MAUI = new SRM_HSR_Code(_HSR_OLD_HAW_INT_1987_MAUI,
            "HSR_OLD_HAW_INT_1987_MAUI");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_INT_1987_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_OLD_HAW_INT_1987_MEAN_SOLUTION,
            "HSR_OLD_HAW_INT_1987_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OLD_HAW_INT_1987_OAHU = new SRM_HSR_Code(_HSR_OLD_HAW_INT_1987_OAHU,
            "HSR_OLD_HAW_INT_1987_OAHU");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OPHELIA_1988_IDENTITY = new SRM_HSR_Code(_HSR_OPHELIA_1988_IDENTITY,
            "HSR_OPHELIA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OSGB_1936_ENGLAND = new SRM_HSR_Code(_HSR_OSGB_1936_ENGLAND,
            "HSR_OSGB_1936_ENGLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OSGB_1936_ENGLAND_ISLE_OF_MAN_WALES = new SRM_HSR_Code(_HSR_OSGB_1936_ENGLAND_ISLE_OF_MAN_WALES,
            "HSR_OSGB_1936_ENGLAND_ISLE_OF_MAN_WALES");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OSGB_1936_7_GREAT_BRITAIN = new SRM_HSR_Code(_HSR_OSGB_1936_7_GREAT_BRITAIN,
            "HSR_OSGB_1936_7_GREAT_BRITAIN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OSGB_1936_3_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_OSGB_1936_3_MEAN_SOLUTION,
            "HSR_OSGB_1936_3_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OSGB_1936_SCOTLAND_SHETLAND_ISLANDS = new SRM_HSR_Code(_HSR_OSGB_1936_SCOTLAND_SHETLAND_ISLANDS,
            "HSR_OSGB_1936_SCOTLAND_SHETLAND_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_OSGB_1936_WALES = new SRM_HSR_Code(_HSR_OSGB_1936_WALES,
            "HSR_OSGB_1936_WALES");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PALESTINE_1928_ISRAEL_JORDAN = new SRM_HSR_Code(_HSR_PALESTINE_1928_ISRAEL_JORDAN,
            "HSR_PALESTINE_1928_ISRAEL_JORDAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PAN_1991_IDENTITY = new SRM_HSR_Code(_HSR_PAN_1991_IDENTITY,
            "HSR_PAN_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PANDORA_1988_IDENTITY = new SRM_HSR_Code(_HSR_PANDORA_1988_IDENTITY,
            "HSR_PANDORA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PHOBOS_1988_IDENTITY = new SRM_HSR_Code(_HSR_PHOBOS_1988_IDENTITY,
            "HSR_PHOBOS_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PHOEBE_1988_IDENTITY = new SRM_HSR_Code(_HSR_PHOEBE_1988_IDENTITY,
            "HSR_PHOEBE_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PICO_NIEVES_1987_CANARY_ISLANDS = new SRM_HSR_Code(_HSR_PICO_NIEVES_1987_CANARY_ISLANDS,
            "HSR_PICO_NIEVES_1987_CANARY_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PITCAIRN_1967_PITCAIRN_ISLAND = new SRM_HSR_Code(_HSR_PITCAIRN_1967_PITCAIRN_ISLAND,
            "HSR_PITCAIRN_1967_PITCAIRN_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PLUTO_1994_IDENTITY = new SRM_HSR_Code(_HSR_PLUTO_1994_IDENTITY,
            "HSR_PLUTO_1994_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_POINT_58_1991_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_POINT_58_1991_MEAN_SOLUTION,
            "HSR_POINT_58_1991_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_POINTE_NOIRE_1948_CONGO = new SRM_HSR_Code(_HSR_POINTE_NOIRE_1948_CONGO,
            "HSR_POINTE_NOIRE_1948_CONGO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PORTIA_1988_IDENTITY = new SRM_HSR_Code(_HSR_PORTIA_1988_IDENTITY,
            "HSR_PORTIA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PORTO_SANTO_1936_PORTO_SANTO_MADEIRA_ISLANDS =
            new SRM_HSR_Code(_HSR_PORTO_SANTO_1936_PORTO_SANTO_MADEIRA_ISLANDS,
                    "HSR_PORTO_SANTO_1936_PORTO_SANTO_MADEIRA_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROMETHEUS_1988_IDENTITY = new SRM_HSR_Code(_HSR_PROMETHEUS_1988_IDENTITY,
            "HSR_PROMETHEUS_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROTEUS_1991_IDENTITY = new SRM_HSR_Code(_HSR_PROTEUS_1991_IDENTITY,
            "HSR_PROTEUS_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_BOLIVIA = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_BOLIVIA,
            "HSR_PROV_S_AM_1956_BOLIVIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_COLOMBIA = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_COLOMBIA,
            "HSR_PROV_S_AM_1956_COLOMBIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_ECUADOR = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_ECUADOR,
            "HSR_PROV_S_AM_1956_ECUADOR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_GUYANA = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_GUYANA,
            "HSR_PROV_S_AM_1956_GUYANA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_MEAN_SOLUTION,
            "HSR_PROV_S_AM_1956_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_N_CHILE_19_S = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_N_CHILE_19_S,
            "HSR_PROV_S_AM_1956_N_CHILE_19_S");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_PERU = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_PERU,
            "HSR_PROV_S_AM_1956_PERU");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_S_CHILE_43_S = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_S_CHILE_43_S,
            "HSR_PROV_S_AM_1956_S_CHILE_43_S");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_3_VENEZUELA = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_3_VENEZUELA,
            "HSR_PROV_S_AM_1956_3_VENEZUELA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_AM_1956_7_VENEZUELA = new SRM_HSR_Code(_HSR_PROV_S_AM_1956_7_VENEZUELA,
            "HSR_PROV_S_AM_1956_7_VENEZUELA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PROV_S_CHILEAN_1963_SOUTH_CHILE = new SRM_HSR_Code(_HSR_PROV_S_CHILEAN_1963_SOUTH_CHILE,
            "HSR_PROV_S_CHILEAN_1963_SOUTH_CHILE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PUCK_1988_IDENTITY = new SRM_HSR_Code(_HSR_PUCK_1988_IDENTITY,
            "HSR_PUCK_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PUERTO_RICO_1987_PUERTO_RICO_VIRGIN_ISLANDS =
            new SRM_HSR_Code(_HSR_PUERTO_RICO_1987_PUERTO_RICO_VIRGIN_ISLANDS,
                    "HSR_PUERTO_RICO_1987_PUERTO_RICO_VIRGIN_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PULKOVO_1942_ESTONIA = new SRM_HSR_Code(_HSR_PULKOVO_1942_ESTONIA,
            "HSR_PULKOVO_1942_ESTONIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PULKOVO_1942_GERMANY = new SRM_HSR_Code(_HSR_PULKOVO_1942_GERMANY,
            "HSR_PULKOVO_1942_GERMANY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_PULKOVO_1942_RUSSIA = new SRM_HSR_Code(_HSR_PULKOVO_1942_RUSSIA,
            "HSR_PULKOVO_1942_RUSSIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_QATAR_NATIONAL_1974_3_QATAR = new SRM_HSR_Code(_HSR_QATAR_NATIONAL_1974_3_QATAR,
            "HSR_QATAR_NATIONAL_1974_3_QATAR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_QATAR_NATIONAL_1974_7_QATAR = new SRM_HSR_Code(_HSR_QATAR_NATIONAL_1974_7_QATAR,
            "HSR_QATAR_NATIONAL_1974_7_QATAR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_QATAR_NATIONAL_1995_QATAR = new SRM_HSR_Code(_HSR_QATAR_NATIONAL_1995_QATAR,
            "HSR_QATAR_NATIONAL_1995_QATAR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_QORNOQ_1987_SOUTH_GREENLAND = new SRM_HSR_Code(_HSR_QORNOQ_1987_SOUTH_GREENLAND,
            "HSR_QORNOQ_1987_SOUTH_GREENLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_REUNION_1947_MASCARENE_ISLANDS = new SRM_HSR_Code(_HSR_REUNION_1947_MASCARENE_ISLANDS,
            "HSR_REUNION_1947_MASCARENE_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_RGF_1993_IDENTITY_BY_MEASUREMENT = new SRM_HSR_Code(_HSR_RGF_1993_IDENTITY_BY_MEASUREMENT,
            "HSR_RGF_1993_IDENTITY_BY_MEASUREMENT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_RHEA_1988_IDENTITY = new SRM_HSR_Code(_HSR_RHEA_1988_IDENTITY,
            "HSR_RHEA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ROME_1940_7_ITALY = new SRM_HSR_Code(_HSR_ROME_1940_7_ITALY,
            "HSR_ROME_1940_7_ITALY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ROME_1940_SARDINIA = new SRM_HSR_Code(_HSR_ROME_1940_SARDINIA,
            "HSR_ROME_1940_SARDINIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ROME_1940_7_SARDINIA = new SRM_HSR_Code(_HSR_ROME_1940_7_SARDINIA,
            "HSR_ROME_1940_7_SARDINIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ROME_1940_SICILY = new SRM_HSR_Code(_HSR_ROME_1940_SICILY,
            "HSR_ROME_1940_SICILY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ROME_1940_PM_ROME_7_ITALY = new SRM_HSR_Code(_HSR_ROME_1940_PM_ROME_7_ITALY,
            "HSR_ROME_1940_PM_ROME_7_ITALY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ROME_1940_PM_ROME_SARDINIA = new SRM_HSR_Code(_HSR_ROME_1940_PM_ROME_SARDINIA,
            "HSR_ROME_1940_PM_ROME_SARDINIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ROSALIND_1988_IDENTITY = new SRM_HSR_Code(_HSR_ROSALIND_1988_IDENTITY,
            "HSR_ROSALIND_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_RT_1990_SWEDEN = new SRM_HSR_Code(_HSR_RT_1990_SWEDEN,
            "HSR_RT_1990_SWEDEN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_RT_1990_PM_STOCKHOLM_SWEDEN = new SRM_HSR_Code(_HSR_RT_1990_PM_STOCKHOLM_SWEDEN,
            "HSR_RT_1990_PM_STOCKHOLM_SWEDEN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_ARGENTINA = new SRM_HSR_Code(_HSR_S_AM_1969_ARGENTINA,
            "HSR_S_AM_1969_ARGENTINA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_BALTRA_GALAPAGOS_ISLANDS = new SRM_HSR_Code(_HSR_S_AM_1969_BALTRA_GALAPAGOS_ISLANDS,
            "HSR_S_AM_1969_BALTRA_GALAPAGOS_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_BOLIVIA = new SRM_HSR_Code(_HSR_S_AM_1969_BOLIVIA,
            "HSR_S_AM_1969_BOLIVIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_BRAZIL = new SRM_HSR_Code(_HSR_S_AM_1969_BRAZIL,
            "HSR_S_AM_1969_BRAZIL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_CHILE = new SRM_HSR_Code(_HSR_S_AM_1969_CHILE,
            "HSR_S_AM_1969_CHILE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_COLOMBIA = new SRM_HSR_Code(_HSR_S_AM_1969_COLOMBIA,
            "HSR_S_AM_1969_COLOMBIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_ECUADOR_EXCLUDING_GALAPAGOS_ISLANDS =
            new SRM_HSR_Code(_HSR_S_AM_1969_ECUADOR_EXCLUDING_GALAPAGOS_ISLANDS,
                    "HSR_S_AM_1969_ECUADOR_EXCLUDING_GALAPAGOS_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_GUYANA = new SRM_HSR_Code(_HSR_S_AM_1969_GUYANA,
            "HSR_S_AM_1969_GUYANA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_S_AM_1969_MEAN_SOLUTION,
            "HSR_S_AM_1969_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_PARAGUAY = new SRM_HSR_Code(_HSR_S_AM_1969_PARAGUAY,
            "HSR_S_AM_1969_PARAGUAY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_PERU = new SRM_HSR_Code(_HSR_S_AM_1969_PERU,
            "HSR_S_AM_1969_PERU");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_TRINIDAD_TOBAGO = new SRM_HSR_Code(_HSR_S_AM_1969_TRINIDAD_TOBAGO,
            "HSR_S_AM_1969_TRINIDAD_TOBAGO");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_AM_1969_VENEZUELA = new SRM_HSR_Code(_HSR_S_AM_1969_VENEZUELA,
            "HSR_S_AM_1969_VENEZUELA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_ASIA_1987_SINGAPORE = new SRM_HSR_Code(_HSR_S_ASIA_1987_SINGAPORE,
            "HSR_S_ASIA_1987_SINGAPORE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_JTSK_1993_CZECH_REP = new SRM_HSR_Code(_HSR_S_JTSK_1993_CZECH_REP,
            "HSR_S_JTSK_1993_CZECH_REP");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_JTSK_1993_CZECH_REP_SLOVAKIA = new SRM_HSR_Code(_HSR_S_JTSK_1993_CZECH_REP_SLOVAKIA,
            "HSR_S_JTSK_1993_CZECH_REP_SLOVAKIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S_JTSK_1993_SLOVAKIA = new SRM_HSR_Code(_HSR_S_JTSK_1993_SLOVAKIA,
            "HSR_S_JTSK_1993_SLOVAKIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_AFGHANISTAN = new SRM_HSR_Code(_HSR_S42_PULKOVO_AFGHANISTAN,
            "HSR_S42_PULKOVO_AFGHANISTAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_ALBANIA = new SRM_HSR_Code(_HSR_S42_PULKOVO_ALBANIA,
            "HSR_S42_PULKOVO_ALBANIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_CZECH_REPUBLIC_SLOVAKIA = new SRM_HSR_Code(_HSR_S42_PULKOVO_CZECH_REPUBLIC_SLOVAKIA,
            "HSR_S42_PULKOVO_CZECH_REPUBLIC_SLOVAKIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_HUNGARY = new SRM_HSR_Code(_HSR_S42_PULKOVO_HUNGARY,
            "HSR_S42_PULKOVO_HUNGARY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_KAZAKHSTAN = new SRM_HSR_Code(_HSR_S42_PULKOVO_KAZAKHSTAN,
            "HSR_S42_PULKOVO_KAZAKHSTAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_LATVIA = new SRM_HSR_Code(_HSR_S42_PULKOVO_LATVIA,
            "HSR_S42_PULKOVO_LATVIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_3_POLAND = new SRM_HSR_Code(_HSR_S42_PULKOVO_3_POLAND,
            "HSR_S42_PULKOVO_3_POLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_7_POLAND = new SRM_HSR_Code(_HSR_S42_PULKOVO_7_POLAND,
            "HSR_S42_PULKOVO_7_POLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_ROMANIA = new SRM_HSR_Code(_HSR_S42_PULKOVO_ROMANIA,
            "HSR_S42_PULKOVO_ROMANIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_S42_PULKOVO_G_ROMANIA = new SRM_HSR_Code(_HSR_S42_PULKOVO_G_ROMANIA,
            "HSR_S42_PULKOVO_G_ROMANIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SANTO_DOS_1965_ESPIRITO_SANTO_ISLAND = new SRM_HSR_Code(_HSR_SANTO_DOS_1965_ESPIRITO_SANTO_ISLAND,
            "HSR_SANTO_DOS_1965_ESPIRITO_SANTO_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SAO_BRAZ_1987_SAO_MIGUEL_SANTA_MARIA_ISLANDS =
            new SRM_HSR_Code(_HSR_SAO_BRAZ_1987_SAO_MIGUEL_SANTA_MARIA_ISLANDS,
                    "HSR_SAO_BRAZ_1987_SAO_MIGUEL_SANTA_MARIA_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SAPPER_HILL_1943_3_E_FALKLAND_ISLANDS = new SRM_HSR_Code(_HSR_SAPPER_HILL_1943_3_E_FALKLAND_ISLANDS,
            "HSR_SAPPER_HILL_1943_3_E_FALKLAND_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SAPPER_HILL_1943_7_E_FALKLAND_ISLANDS_ADJ_2009 =
            new SRM_HSR_Code(_HSR_SAPPER_HILL_1943_7_E_FALKLAND_ISLANDS_ADJ_2009,
                    "HSR_SAPPER_HILL_1943_7_E_FALKLAND_ISLANDS_ADJ_2009");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SATURN_1988_IDENTITY = new SRM_HSR_Code(_HSR_SATURN_1988_IDENTITY,
            "HSR_SATURN_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SATURN_MAGNETIC_1993_VOYAGER = new SRM_HSR_Code(_HSR_SATURN_MAGNETIC_1993_VOYAGER,
            "HSR_SATURN_MAGNETIC_1993_VOYAGER");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SCHWARZECK_1991_NAMIBIA = new SRM_HSR_Code(_HSR_SCHWARZECK_1991_NAMIBIA,
            "HSR_SCHWARZECK_1991_NAMIBIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SELVAGEM_GRANDE_1938_SALVAGE_ISLANDS = new SRM_HSR_Code(_HSR_SELVAGEM_GRANDE_1938_SALVAGE_ISLANDS,
            "HSR_SELVAGEM_GRANDE_1938_SALVAGE_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SIERRA_LEONE_1960_SIERRA_LEONE = new SRM_HSR_Code(_HSR_SIERRA_LEONE_1960_SIERRA_LEONE,
            "HSR_SIERRA_LEONE_1960_SIERRA_LEONE");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SIRGAS_2000_IDENTITY_BY_MEASUREMENT = new SRM_HSR_Code(_HSR_SIRGAS_2000_IDENTITY_BY_MEASUREMENT,
            "HSR_SIRGAS_2000_IDENTITY_BY_MEASUREMENT");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SOUTHEAST_1943_SEYCHELLES_ISLANDS = new SRM_HSR_Code(_HSR_SOUTHEAST_1943_SEYCHELLES_ISLANDS,
            "HSR_SOUTHEAST_1943_SEYCHELLES_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SOVIET_GEODETIC_1985_RUSSIA = new SRM_HSR_Code(_HSR_SOVIET_GEODETIC_1985_RUSSIA,
            "HSR_SOVIET_GEODETIC_1985_RUSSIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SOVIET_GEODETIC_1990_RUSSIA = new SRM_HSR_Code(_HSR_SOVIET_GEODETIC_1990_RUSSIA,
            "HSR_SOVIET_GEODETIC_1990_RUSSIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_SUN_1992_IDENTITY = new SRM_HSR_Code(_HSR_SUN_1992_IDENTITY,
            "HSR_SUN_1992_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TAN_OBS_1925_3_MADAGASCAR = new SRM_HSR_Code(_HSR_TAN_OBS_1925_3_MADAGASCAR,
            "HSR_TAN_OBS_1925_3_MADAGASCAR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TAN_OBS_1925_7_MADAGASCAR = new SRM_HSR_Code(_HSR_TAN_OBS_1925_7_MADAGASCAR,
            "HSR_TAN_OBS_1925_7_MADAGASCAR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TAN_OBS_1925_PM_PARIS_3_MADAGASCAR = new SRM_HSR_Code(_HSR_TAN_OBS_1925_PM_PARIS_3_MADAGASCAR,
            "HSR_TAN_OBS_1925_PM_PARIS_3_MADAGASCAR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TAN_OBS_1925_PM_PARIS_7_MADAGASCAR = new SRM_HSR_Code(_HSR_TAN_OBS_1925_PM_PARIS_7_MADAGASCAR,
            "HSR_TAN_OBS_1925_PM_PARIS_7_MADAGASCAR");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TELESTO_1988_IDENTITY = new SRM_HSR_Code(_HSR_TELESTO_1988_IDENTITY,
            "HSR_TELESTO_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TERN_1961_TERN_ISLAND = new SRM_HSR_Code(_HSR_TERN_1961_TERN_ISLAND,
            "HSR_TERN_1961_TERN_ISLAND");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TETHYS_1991_IDENTITY = new SRM_HSR_Code(_HSR_TETHYS_1991_IDENTITY,
            "HSR_TETHYS_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_THALASSA_1991_IDENTITY = new SRM_HSR_Code(_HSR_THALASSA_1991_IDENTITY,
            "HSR_THALASSA_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_THEBE_2000_IDENTITY = new SRM_HSR_Code(_HSR_THEBE_2000_IDENTITY,
            "HSR_THEBE_2000_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TIM_BESSEL_1948_7_BRUNEI_E_MALAYSIA = new SRM_HSR_Code(_HSR_TIM_BESSEL_1948_7_BRUNEI_E_MALAYSIA,
            "HSR_TIM_BESSEL_1948_7_BRUNEI_E_MALAYSIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TIM_BESSEL_ADJ_1968_7_BRUNEI_E_MALAYSIA =
            new SRM_HSR_Code(_HSR_TIM_BESSEL_ADJ_1968_7_BRUNEI_E_MALAYSIA,
                    "HSR_TIM_BESSEL_ADJ_1968_7_BRUNEI_E_MALAYSIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TIM_EV_1948_3_BRUNEI_E_MALAYSIA = new SRM_HSR_Code(_HSR_TIM_EV_1948_3_BRUNEI_E_MALAYSIA,
            "HSR_TIM_EV_1948_3_BRUNEI_E_MALAYSIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TIM_EV_1948_7_BRUNEI_E_MALAYSIA = new SRM_HSR_Code(_HSR_TIM_EV_1948_7_BRUNEI_E_MALAYSIA,
            "HSR_TIM_EV_1948_7_BRUNEI_E_MALAYSIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TIM_EV_ADJ_1968_7_BRUNEI_E_MALAYSIA = new SRM_HSR_Code(_HSR_TIM_EV_ADJ_1968_7_BRUNEI_E_MALAYSIA,
            "HSR_TIM_EV_ADJ_1968_7_BRUNEI_E_MALAYSIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TITAN_1982_IDENTITY = new SRM_HSR_Code(_HSR_TITAN_1982_IDENTITY,
            "HSR_TITAN_1982_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TITANIA_1988_IDENTITY = new SRM_HSR_Code(_HSR_TITANIA_1988_IDENTITY,
            "HSR_TITANIA_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TOKYO_1991_JAPAN = new SRM_HSR_Code(_HSR_TOKYO_1991_JAPAN,
            "HSR_TOKYO_1991_JAPAN");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TOKYO_1991_MEAN_SOLUTION = new SRM_HSR_Code(_HSR_TOKYO_1991_MEAN_SOLUTION,
            "HSR_TOKYO_1991_MEAN_SOLUTION");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TOKYO_1991_OKINAWA = new SRM_HSR_Code(_HSR_TOKYO_1991_OKINAWA,
            "HSR_TOKYO_1991_OKINAWA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TOKYO_1991_1991_SOUTH_KOREA = new SRM_HSR_Code(_HSR_TOKYO_1991_1991_SOUTH_KOREA,
            "HSR_TOKYO_1991_1991_SOUTH_KOREA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TOKYO_1991_1997_SOUTH_KOREA = new SRM_HSR_Code(_HSR_TOKYO_1991_1997_SOUTH_KOREA,
            "HSR_TOKYO_1991_1997_SOUTH_KOREA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TRISTAN_1968_TRISTAN_DA_CUHNA = new SRM_HSR_Code(_HSR_TRISTAN_1968_TRISTAN_DA_CUHNA,
            "HSR_TRISTAN_1968_TRISTAN_DA_CUHNA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_TRITON_1991_IDENTITY = new SRM_HSR_Code(_HSR_TRITON_1991_IDENTITY,
            "HSR_TRITON_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_UMBRIEL_1988_IDENTITY = new SRM_HSR_Code(_HSR_UMBRIEL_1988_IDENTITY,
            "HSR_UMBRIEL_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_URANUS_1988_IDENTITY = new SRM_HSR_Code(_HSR_URANUS_1988_IDENTITY,
            "HSR_URANUS_1988_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_URANUS_MAGNETIC_1993_VOYAGER = new SRM_HSR_Code(_HSR_URANUS_MAGNETIC_1993_VOYAGER,
            "HSR_URANUS_MAGNETIC_1993_VOYAGER");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_VENUS_1991_IDENTITY = new SRM_HSR_Code(_HSR_VENUS_1991_IDENTITY,
            "HSR_VENUS_1991_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_VITI_LEVU_1916_VITI_LEVU_ISLANDS = new SRM_HSR_Code(_HSR_VITI_LEVU_1916_VITI_LEVU_ISLANDS,
            "HSR_VITI_LEVU_1916_VITI_LEVU_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_VOIROL_1874_ALGERIA = new SRM_HSR_Code(_HSR_VOIROL_1874_ALGERIA,
            "HSR_VOIROL_1874_ALGERIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_VOIROL_1874_PM_PARIS_ALGERIA = new SRM_HSR_Code(_HSR_VOIROL_1874_PM_PARIS_ALGERIA,
            "HSR_VOIROL_1874_PM_PARIS_ALGERIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_VOIROL_1960_ALGERIA = new SRM_HSR_Code(_HSR_VOIROL_1960_ALGERIA,
            "HSR_VOIROL_1960_ALGERIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_VOIROL_1960_PM_PARIS_ALGERIA = new SRM_HSR_Code(_HSR_VOIROL_1960_PM_PARIS_ALGERIA,
            "HSR_VOIROL_1960_PM_PARIS_ALGERIA");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_WAKE_1952_WAKE_ATOLL = new SRM_HSR_Code(_HSR_WAKE_1952_WAKE_ATOLL,
            "HSR_WAKE_1952_WAKE_ATOLL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_WAKE_ENIWETOK_1960_MARSHALL_ISLANDS = new SRM_HSR_Code(_HSR_WAKE_ENIWETOK_1960_MARSHALL_ISLANDS,
            "HSR_WAKE_ENIWETOK_1960_MARSHALL_ISLANDS");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_WGS_1972_GLOBAL = new SRM_HSR_Code(_HSR_WGS_1972_GLOBAL,
            "HSR_WGS_1972_GLOBAL");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_WGS_1984_IDENTITY = new SRM_HSR_Code(_HSR_WGS_1984_IDENTITY,
            "HSR_WGS_1984_IDENTITY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_YACARE_1987_URUGUAY = new SRM_HSR_Code(_HSR_YACARE_1987_URUGUAY,
            "HSR_YACARE_1987_URUGUAY");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_HSR_Code HSR_ZANDERIJ_1987_SURINAME = new SRM_HSR_Code(_HSR_ZANDERIJ_1987_SURINAME,
            "HSR_ZANDERIJ_1987_SURINAME");

    private SRM_HSR_Code(int code, String name) {
        super(code, name);
        _enumMap.put(name, this);
    }

    /// returns the SRM_HSR_Code from its string name
    public static SRM_HSR_Code getEnum(String name) throws SrmException {
        SRM_HSR_Code retCode = (SRM_HSR_Code) _enumMap.get(name);

        if (retCode == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_HSR_Code.getEnum: enum. string not found=> " +
                            name));
        }

        return retCode;
    }
}
