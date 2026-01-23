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

package org.jscience.politics;

/**
 * Defines various constants related to political systems, government forms, 
 * ideological spectrum, and international country codes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class PoliticsConstants {

    private PoliticsConstants() {}

    /** State of unclassified or unknown political entity. */
    public final static int UNKNOWN = 0;

    // Economic Development Status
    
    /** Economically underdeveloped nation. */
    public final static int UNDERDEVELOPED = 1;

    /** Nation in the process of industrializing and developing. */
    public final static int DEVELOPING = 2;

    /** Economically and industrially advanced nation. */
    public final static int DEVELOPED = 3;

    // Governmental Archetypes (Forms of Government)
    
    /** Stateless social order based on voluntary cooperation. */
    public final static int ANARCHISM = 1;

    /** System where supreme power is concentrated in the hands of one person. */
    public final static int AUTOCRACY = 2;

    /** System of government by the whole population, typically through elected representatives. */
    public final static int DEMOCRACY = 3;

    /** Rule by a small group of people, often distinguished by wealth or family. */
    public final static int OLIGARCHY = 4;

    /** State in which supreme power is held by the people and their elected representatives. */
    public final static int REPUBLIC = 5;

    // Ideological Wing Categorization
    
    /** Political position characterizing revolutionary social equality. */
    public final static int EXTREME_LEFT_WING = 1;

    /** Position supporting social reform and egalitarianism. */
    public final static int LEFT_WING = 2;

    /** Moderate position balancing social reform with tradition. */
    public final static int CENTER = 4;

    /** Position supporting traditional values and hierarchy. */
    public final static int RIGHT_WING = 8;

    /** Position characterizing reactionary or ultranationalist tradition. */
    public final static int EXTREME_RIGHT_WING = 16;

    // ISO Alpha-2 Country Codes (Partial mapping for internal lookup)
    
    public final static String AC = "Ascension Island";
    public final static String AD = "Andorra";
    public final static String AE = "United Arab Emirates";
    public final static String AF = "Afghanistan";
    public final static String AG = "Antigua and Barbuda";
    public final static String AI = "Anguilla";
    public final static String AL = "Albania";
    public final static String AM = "Armenia";
    public final static String AN = "Netherlands Antilles";
    public final static String AO = "Angola";
    public final static String AQ = "Antarctica";
    public final static String AR = "Argentina";
    public final static String AS = "American Samoa";
    public final static String AT = "Austria";
    public final static String AU = "Australia";
    public final static String AW = "Aruba";
    public final static String AZ = "Azerbaijan";
    public final static String BA = "Bosnia and Herzegovina";
    public final static String BB = "Barbados";
    public final static String BD = "Bangladesh";
    public final static String BE = "Belgium";
    public final static String BF = "Burkina Faso";
    public final static String BG = "Bulgaria";
    public final static String BH = "Bahrain";
    public final static String BI = "Burundi";
    public final static String BJ = "Benin";
    public final static String BM = "Bermuda";
    public final static String BN = "Brunei Darussalam";
    public final static String BO = "Bolivia";
    public final static String BR = "Brazil";
    public final static String BS = "Bahamas";
    public final static String BT = "Bhutan";
    public final static String BV = "Bouvet Island";
    public final static String BW = "Botswana";
    public final static String BY = "Belarus";
    public final static String BZ = "Belize";
    public final static String CA = "Canada";
    public final static String CC = "Cocos (Keeling) Islands";
    public final static String CD = "Congo, Democratic Republic of the";
    public final static String CF = "Central African Republic";
    public final static String CG = "Congo, Republic of";
    public final static String CH = "Switzerland";
    public final static String CI = "Cote d'Ivoire";
    public final static String CK = "Cook Islands";
    public final static String CL = "Chile";
    public final static String CM = "Cameroon";
    public final static String CN = "China";
    public final static String CO = "Colombia";
    public final static String CR = "Costa Rica";
    public final static String CU = "Cuba";
    public final static String CV = "Cap Verde";
    public final static String CX = "Christmas Island";
    public final static String CY = "Cyprus";
    public final static String CZ = "Czech Republic";
    public final static String DE = "Germany";
    public final static String DJ = "Djibouti";
    public final static String DK = "Denmark";
    public final static String DM = "Dominica";
    public final static String DO = "Dominican Republic";
    public final static String DZ = "Algeria";
    public final static String EC = "Ecuador";
    public final static String EE = "Estonia";
    public final static String EG = "Egypt";
    public final static String EH = "Western Sahara";
    public final static String ER = "Eritrea";
    public final static String ES = "Spain";
    public final static String ET = "Ethiopia";
    public final static String FI = "Finland";
    public final static String FJ = "Fiji";
    public final static String FK = "Falkland Islands (Malvina)";
    public final static String FM = "Micronesia, Federal State of";
    public final static String FO = "Faroe Islands";
    public final static String FR = "France";
    public final static String GA = "Gabon";
    public final static String GD = "Grenada";
    public final static String GE = "Georgia";
    public final static String GF = "French Guiana";
    public final static String GG = "Guernsey";
    public final static String GH = "Ghana";
    public final static String GI = "Gibraltar";
    public final static String GL = "Greenland";
    public final static String GM = "Gambia";
    public final static String GN = "Guinea";
    public final static String GP = "Guadeloupe";
    public final static String GQ = "Equatorial Guinea";
    public final static String GR = "Greece";
    public final static String GS = "South Georgia and the South Sandwich Islands";
    public final static String GT = "Guatemala";
    public final static String GU = "Guam";
    public final static String GW = "Guinea-Bissau";
    public final static String GY = "Guyana";
    public final static String HK = "Hong Kong";
    public final static String HM = "Heard and McDonald Islands";
    public final static String HN = "Honduras";
    public final static String HR = "Croatia/Hrvatska";
    public final static String HT = "Haiti";
    public final static String HU = "Hungary";
    public final static String ID = "Indonesia";
    public final static String IE = "Ireland";
    public final static String IL = "Israel";
    public final static String IM = "Isle of Man";
    public final static String IN = "India";
    public final static String IO = "British Indian Ocean Territory";
    public final static String IQ = "Iraq";
    public final static String IR = "Iran (Islamic Republic of)";
    public final static String IS = "Iceland";
    public final static String IT = "Italy";
    public final static String JE = "Jersey";
    public final static String JM = "Jamaica";
    public final static String JO = "Jordan";
    public final static String JP = "Japan";
    public final static String KE = "Kenya";
    public final static String KG = "Kyrgyzstan";
    public final static String KH = "Cambodia";
    public final static String KI = "Kiribati";
    public final static String KM = "Comoros";
    public final static String KN = "Saint Kitts and Nevis";
    public final static String KP = "Korea, Democratic People's Republic";
    public final static String KR = "Korea, Republic of";
    public final static String KW = "Kuwait";
    public final static String KY = "Cayman Islands";
    public final static String KZ = "Kazakhstan";
    public final static String LA = "Lao People's Democratic Republic";
    public final static String LB = "Lebanon";
    public final static String LC = "Saint Lucia";
    public final static String LI = "Liechtenstein";
    public final static String LK = "Sri Lanka";
    public final static String LR = "Liberia";
    public final static String LS = "Lesotho";
    public final static String LT = "Lithuania";
    public final static String LU = "Luxembourg";
    public final static String LV = "Latvia";
    public final static String LY = "Libyan Arab Jamahiriya";
    public final static String MA = "Morocco";
    public final static String MC = "Monaco";
    public final static String MD = "Moldova, Republic of";
    public final static String MG = "Madagascar";
    public final static String MH = "Marshall Islands";
    public final static String MK = "Macedonia, Former Yugoslav Republic";
    public final static String ML = "Mali";
    public final static String MM = "Myanmar";
    public final static String MN = "Mongolia";
    public final static String MO = "Macau";
    public final static String MP = "Northern Mariana Islands";
    public final static String MQ = "Martinique";
    public final static String MR = "Mauritania";
    public final static String MS = "Montserrat";
    public final static String MT = "Malta";
    public final static String MU = "Mauritius";
    public final static String MV = "Maldives";
    public final static String MW = "Malawi";
    public final static String MX = "Mexico";
    public final static String MY = "Malaysia";
    public final static String MZ = "Mozambique";
    public final static String NA = "Namibia";
    public final static String NC = "New Caledonia";
    public final static String NE = "Niger";
    public final static String NF = "Norfolk Island";
    public final static String NG = "Nigeria";
    public final static String NI = "Nicaragua";
    public final static String NL = "Netherlands";
    public final static String NO = "Norway";
    public final static String NP = "Nepal";
    public final static String NR = "Nauru";
    public final static String NU = "Niue";
    public final static String NZ = "New Zealand";
    public final static String OM = "Oman";
    public final static String PA = "Panama";
    public final static String PE = "Peru";
    public final static String PF = "French Polynesia";
    public final static String PG = "Papua New Guinea";
    public final static String PH = "Philippines";
    public final static String PK = "Pakistan";
    public final static String PL = "Poland";
    public final static String PM = "St. Pierre and Miquelon";
    public final static String PN = "Pitcairn Island";
    public final static String PR = "Puerto Rico";
    public final static String PS = "Palestinian Territories";
    public final static String PT = "Portugal";
    public final static String PW = "Palau";
    public final static String PY = "Paraguay";
    public final static String QA = "Qatar";
    public final static String RE = "Reunion Island";
    public final static String RO = "Romania";
    public final static String RU = "Russian Federation";
    public final static String RW = "Rwanda";
    public final static String SA = "Saudi Arabia";
    public final static String SB = "Solomon Islands";
    public final static String SC = "Seychelles";
    public final static String SD = "Sudan";
    public final static String SE = "Sweden";
    public final static String SG = "Singapore";
    public final static String SH = "St. Helena";
    public final static String SI = "Slovenia";
    public final static String SJ = "Svalbard and Jan Mayen Islands";
    public final static String SK = "Slovak Republic";
    public final static String SL = "Sierra Leone";
    public final static String SM = "San Marino";
    public final static String SN = "Senegal";
    public final static String SO = "Somalia";
    public final static String SR = "Suriname";
    public final static String ST = "Sao Tome and Principe";
    public final static String SV = "El Salvador";
    public final static String SY = "Syrian Arab Republic";
    public final static String SZ = "Swaziland";
    public final static String TC = "Turks and Caicos Islands";
    public final static String TD = "Chad";
    public final static String TF = "French Southern Territories";
    public final static String TG = "Togo";
    public final static String TH = "Thailand";
    public final static String TJ = "Tajikistan";
    public final static String TK = "Tokelau";
    public final static String TM = "Turkmenistan";
    public final static String TN = "Tunisia";
    public final static String TO = "Tonga";
    public final static String TP = "East Timor";
    public final static String TR = "Turkey";
    public final static String TT = "Trinidad and Tobago";
    public final static String TV = "Tuvalu";
    public final static String TW = "Taiwan";
    public final static String TZ = "Tanzania";
    public final static String UA = "Ukraine";
    public final static String UG = "Uganda";
    public final static String UK = "United Kingdom";
    public final static String UM = "US Minor Outlying Islands";
    public final static String US = "United States";
    public final static String UY = "Uruguay";
    public final static String UZ = "Uzbekistan";
    public final static String VA = "Holy See (City Vatican State)";
    public final static String VC = "Saint Vincent and the Grenadines";
    public final static String VE = "Venezuela";
    public final static String VG = "Virgin Islands (British)";
    public final static String VI = "Virgin Islands (USA)";
}
