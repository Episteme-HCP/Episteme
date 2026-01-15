package org.jscience.economics.money;

/**
 * A class representing a list of common currencies as of circa 2000.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//since java 1.4 there is a Currency class in java.util
//you should use the java class, not this class
//one could also use com.dautelle.money.Currency
// Currencies are identified by their ISO 4217 currency codes. See the ISO 4217 maintenance agency for more information, including a table of currency codes.
//see http://www.bsi-global.com/iso4217currency
public class Currencies extends Object {
    //added to the list for convenience although the symbol used has nothing to do with the standard
    /** DOCUMENT ME! */
    public final static Currency US_DOLLAR = new Currency("US Dollar", "$"); //US dollar

    /** DOCUMENT ME! */
    public final static Currency EURO = new Currency("Euro", "�");

    /** DOCUMENT ME! */
    public final static Currency YEN = new Currency("Yen", "�");

    /** DOCUMENT ME! */
    public final static Currency POUND_STERLING = new Currency("Pound Sterling",
            "�");

    //perhaps we should store all these values into a table
    /** DOCUMENT ME! */
    public final static Currency ADP = new Currency("Andorran Peseta", "ADP");

    /** DOCUMENT ME! */
    public final static Currency AED = new Currency("UAE Dirham", "AED");

    /** DOCUMENT ME! */
    public final static Currency AFA = new Currency("Afghani", "AFA");

    /** DOCUMENT ME! */
    public final static Currency ALL = new Currency("Lek", "ALL");

    /** DOCUMENT ME! */
    public final static Currency AMD = new Currency("Armenian Dram", "AMD");

    /** DOCUMENT ME! */
    public final static Currency ANG = new Currency("Netherlands Antillian Guilder",
            "ANG");

    /** DOCUMENT ME! */
    public final static Currency AOA = new Currency("Kwanza", "AOA");

    /** DOCUMENT ME! */
    public final static Currency ARS = new Currency("Argentine Peso", "ARS");

    /** DOCUMENT ME! */
    public final static Currency ATS = new Currency("Schilling", "ATS"); //         ATS used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-28

    /** DOCUMENT ME! */
    public final static Currency AUD = new Currency("Australian Dollar", "AUD");

    /** DOCUMENT ME! */
    public final static Currency AWG = new Currency("Aruban Guilder", "AWG");

    /** DOCUMENT ME! */
    public final static Currency AZM = new Currency("Azerbaijanian Manat", "AZM");

    /** DOCUMENT ME! */
    public final static Currency BAM = new Currency("Convertible Marks", "BAM");

    /** DOCUMENT ME! */
    public final static Currency BBD = new Currency("Barbados Dollar", "BBD");

    /** DOCUMENT ME! */
    public final static Currency BDT = new Currency("Taka", "BDT");

    /** DOCUMENT ME! */
    public final static Currency BEF = new Currency("Belgian Franc", "BEF"); //         BEF used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-28

    /** DOCUMENT ME! */
    public final static Currency BGL = new Currency("Lev", "BGL");

    /** DOCUMENT ME! */
    public final static Currency BGN = new Currency("Bulgarian Lev", "BGN");

    /** DOCUMENT ME! */
    public final static Currency BHD = new Currency("Bahraini Dinar", "BHD");

    /** DOCUMENT ME! */
    public final static Currency BIF = new Currency("Burundi Franc", "BIF");

    /** DOCUMENT ME! */
    public final static Currency BMD = new Currency("Bermudian Dollar", "BMD");

    /** DOCUMENT ME! */
    public final static Currency BND = new Currency("Brunei Dollar", "BND");

    /** DOCUMENT ME! */
    public final static Currency BOB = new Currency("Boliviano", "BOB");

    /** DOCUMENT ME! */
    public final static Currency BOV = new Currency("Mvdol", "BOV"); //Funds code

    /** DOCUMENT ME! */
    public final static Currency BRL = new Currency("Brazilian Real", "BRL");

    /** DOCUMENT ME! */
    public final static Currency BSD = new Currency("Bahamian Dollar", "BSD");

    /** DOCUMENT ME! */
    public final static Currency BTN = new Currency("Ngultrum", "BTN");

    /** DOCUMENT ME! */
    public final static Currency BWP = new Currency("Pula", "BWP");

    //public final static Currency BYB = new Currency("", "BYB");
    /** DOCUMENT ME! */
    public final static Currency BYR = new Currency("Belarussian Ruble", "BYR");

    /** DOCUMENT ME! */
    public final static Currency BZD = new Currency("Belize Dollar", "BZD");

    /** DOCUMENT ME! */
    public final static Currency CAD = new Currency("Canadian Dollar", "CAD");

    /** DOCUMENT ME! */
    public final static Currency CDF = new Currency("Franc Congolais", "CDF");

    /** DOCUMENT ME! */
    public final static Currency CHF = new Currency("Swiss Franc", "CHF");

    /** DOCUMENT ME! */
    public final static Currency CLF = new Currency("Unidades de fomento", "CLF"); //Funds code

    /** DOCUMENT ME! */
    public final static Currency CLP = new Currency("Chilean Peso", "CLP");

    /** DOCUMENT ME! */
    public final static Currency CNY = new Currency("Yuan Renminbi", "CNY");

    /** DOCUMENT ME! */
    public final static Currency COP = new Currency("Colombian Peso", "COP");

    /** DOCUMENT ME! */
    public final static Currency CRC = new Currency("Costa Rican Colon", "CRC");

    /** DOCUMENT ME! */
    public final static Currency CUP = new Currency("Cuban Peso", "CUP");

    /** DOCUMENT ME! */
    public final static Currency CVE = new Currency("Cape Verde Escudo", "CVE");

    /** DOCUMENT ME! */
    public final static Currency CYP = new Currency("Cyprus Pound", "CYP");

    /** DOCUMENT ME! */
    public final static Currency CZK = new Currency("Czech Koruna", "CZK");

    /** DOCUMENT ME! */
    public final static Currency DEM = new Currency("Deutsche Mark", "DEM"); //DEM used for scriptural until 2001-12-31 and for cash until end of legal tender 2001-12-31. Businesses will, however, accept national currency unis at least until 2002-02-28, according to the joint statement of professional associations of 1998-10-22.

    /** DOCUMENT ME! */
    public final static Currency DJF = new Currency("Djibouti Franc", "DJF");

    /** DOCUMENT ME! */
    public final static Currency DKK = new Currency("Danish Krone", "DKK");

    /** DOCUMENT ME! */
    public final static Currency DOP = new Currency("Dominican Peso", "DOP");

    /** DOCUMENT ME! */
    public final static Currency DZD = new Currency("Algerian Dinar", "DZD");

    /** DOCUMENT ME! */
    public final static Currency EEK = new Currency("Kroon", "EEK");

    /** DOCUMENT ME! */
    public final static Currency EGP = new Currency("Egyptian Pound", "EGP");

    /** DOCUMENT ME! */
    public final static Currency ERN = new Currency("Nakfa", "ERN");

    /** DOCUMENT ME! */
    public final static Currency ESP = new Currency("Spanish Peseta", "ESP"); //         ESP used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-28

    /** DOCUMENT ME! */
    public final static Currency ETB = new Currency("Ethiopian Birr", "ETB");

    /** DOCUMENT ME! */
    public final static Currency EUR = new Currency("euro", "EUR"); //EUR used for scriptural from 1999-01-01 (2001-01-01 for Greece) and for cash as from 2002-01-01

    /** DOCUMENT ME! */
    public final static Currency FIM = new Currency("Markka", "FIM"); //FIM used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-28

    /** DOCUMENT ME! */
    public final static Currency FJD = new Currency("Fiji Dollar", "FJD");

    /** DOCUMENT ME! */
    public final static Currency FKP = new Currency("Falkland Islands Pound",
            "FKP");

    /** DOCUMENT ME! */
    public final static Currency FRF = new Currency("French Franc", "FRF"); //FRF used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-17

    /** DOCUMENT ME! */
    public final static Currency GBP = new Currency("Pound Sterling", "GBP");

    /** DOCUMENT ME! */
    public final static Currency GEL = new Currency("Lari", "GEL");

    /** DOCUMENT ME! */
    public final static Currency GHC = new Currency("Cedi", "GHC");

    /** DOCUMENT ME! */
    public final static Currency GIP = new Currency("Gibraltar Pound", "GIP");

    /** DOCUMENT ME! */
    public final static Currency GMD = new Currency("Dalasi", "GMD");

    /** DOCUMENT ME! */
    public final static Currency GNF = new Currency("Guinea Franc", "GNF");

    /** DOCUMENT ME! */
    public final static Currency GRD = new Currency("Drachma", "GRD"); //used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-28

    /** DOCUMENT ME! */
    public final static Currency GTQ = new Currency("Quetzal", "GTQ");

    /** DOCUMENT ME! */
    public final static Currency GWP = new Currency("Guinea-Bissau Peso", "GWP");

    /** DOCUMENT ME! */
    public final static Currency GYD = new Currency("Guyana Dollar", "GYD");

    /** DOCUMENT ME! */
    public final static Currency HKD = new Currency("Hong Kong Dollar", "HKD");

    /** DOCUMENT ME! */
    public final static Currency HNL = new Currency("Lempira", "HNL");

    /** DOCUMENT ME! */
    public final static Currency HRK = new Currency("Croatian Kuna", "HRK");

    /** DOCUMENT ME! */
    public final static Currency HTG = new Currency("Gourde", "HTG");

    /** DOCUMENT ME! */
    public final static Currency HUF = new Currency("Forint", "HUF");

    /** DOCUMENT ME! */
    public final static Currency IDR = new Currency("Rupiah", "IDR");

    /** DOCUMENT ME! */
    public final static Currency IEP = new Currency("Irish Pound", "IEP"); //          IEP used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-09

    /** DOCUMENT ME! */
    public final static Currency ILS = new Currency("New Israeli Sheqel", "ILS"); //Currency name was effective 4th September 1985.

    /** DOCUMENT ME! */
    public final static Currency INR = new Currency("Indian Rupee", "INR");

    /** DOCUMENT ME! */
    public final static Currency IQD = new Currency("Iraqi Dinar", "IQD");

    /** DOCUMENT ME! */
    public final static Currency IRR = new Currency("Iranian Rial", "IRR");

    /** DOCUMENT ME! */
    public final static Currency ISK = new Currency("Iceland Krona", "ISK");

    /** DOCUMENT ME! */
    public final static Currency ITL = new Currency("Italian Lira", "ITL"); //         ITL used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-28

    /** DOCUMENT ME! */
    public final static Currency JMD = new Currency("Jamaican Dollar", "JMD");

    /** DOCUMENT ME! */
    public final static Currency JOD = new Currency("Jordanian Dinar", "JOD");

    /** DOCUMENT ME! */
    public final static Currency JPY = new Currency("Yen", "JPY");

    /** DOCUMENT ME! */
    public final static Currency KES = new Currency("Kenyan Shilling", "KES");

    /** DOCUMENT ME! */
    public final static Currency KGS = new Currency("Som", "KGS");

    /** DOCUMENT ME! */
    public final static Currency KHR = new Currency("Riel", "KHR");

    /** DOCUMENT ME! */
    public final static Currency KMF = new Currency("Comoro Franc", "KMF");

    /** DOCUMENT ME! */
    public final static Currency KPW = new Currency("North Korean Won", "KPW");

    /** DOCUMENT ME! */
    public final static Currency KRW = new Currency("Won", "KRW");

    /** DOCUMENT ME! */
    public final static Currency KWD = new Currency("Kuwaiti Dinar", "KWD");

    /** DOCUMENT ME! */
    public final static Currency KYD = new Currency("Cayman Islands Dollar",
            "KYD");

    /** DOCUMENT ME! */
    public final static Currency KZT = new Currency("Tenge", "KZT");

    /** DOCUMENT ME! */
    public final static Currency LAK = new Currency("Kip", "LAK");

    /** DOCUMENT ME! */
    public final static Currency LBP = new Currency("Lebanese Pound", "LBP");

    /** DOCUMENT ME! */
    public final static Currency LKR = new Currency("Sri Lanka Rupee", "LKR");

    /** DOCUMENT ME! */
    public final static Currency LRD = new Currency("Liberian Dollar", "LRD");

    /** DOCUMENT ME! */
    public final static Currency LSL = new Currency("Loti", "LSL");

    /** DOCUMENT ME! */
    public final static Currency LTL = new Currency("Lithuanian Litus", "LTL");

    /** DOCUMENT ME! */
    public final static Currency LUF = new Currency("Luxembourg Franc", "LUF"); //         LUF used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-28

    /** DOCUMENT ME! */
    public final static Currency LVL = new Currency("Latvian Lats", "LVL");

    /** DOCUMENT ME! */
    public final static Currency LYD = new Currency("Libyan Dinar", "LYD");

    /** DOCUMENT ME! */
    public final static Currency MAD = new Currency("Moroccan Dirham", "MAD");

    /** DOCUMENT ME! */
    public final static Currency MDL = new Currency("Moldovan Leu", "MDL");

    /** DOCUMENT ME! */
    public final static Currency MGF = new Currency("Malagasy Franc", "MGF");

    /** DOCUMENT ME! */
    public final static Currency MKD = new Currency("Denar", "MKD");

    /** DOCUMENT ME! */
    public final static Currency MMK = new Currency("Kyat", "MMK");

    /** DOCUMENT ME! */
    public final static Currency MNT = new Currency("Tugrik", "MNT");

    /** DOCUMENT ME! */
    public final static Currency MOP = new Currency("Pataca", "MOP");

    /** DOCUMENT ME! */
    public final static Currency MRO = new Currency("Ouguiya", "MRO");

    /** DOCUMENT ME! */
    public final static Currency MTL = new Currency("Maltese Lira", "MTL");

    /** DOCUMENT ME! */
    public final static Currency MUR = new Currency("Mauritius Rupee", "MUR");

    /** DOCUMENT ME! */
    public final static Currency MVR = new Currency("Rufiyaa", "MVR");

    /** DOCUMENT ME! */
    public final static Currency MWK = new Currency("Kwacha", "MWK");

    /** DOCUMENT ME! */
    public final static Currency MXN = new Currency("Mexican Peso", "MXN");

    /** DOCUMENT ME! */
    public final static Currency MXV = new Currency("Mexican Unidad de Inversion (UDI)",
            "MXV"); //Funds code

    /** DOCUMENT ME! */
    public final static Currency MYR = new Currency("Malaysian Ringgit", "MYR");

    /** DOCUMENT ME! */
    public final static Currency MZM = new Currency("Metical", "MZM");

    /** DOCUMENT ME! */
    public final static Currency NAD = new Currency("Namibia Dollar", "NAD");

    /** DOCUMENT ME! */
    public final static Currency NGN = new Currency("Naira", "NGN");

    /** DOCUMENT ME! */
    public final static Currency NIO = new Currency("Cordoba Oro", "NIO");

    /** DOCUMENT ME! */
    public final static Currency NLG = new Currency("Netherlands Guilder", "NLG"); //NLG used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-01-28

    /** DOCUMENT ME! */
    public final static Currency NOK = new Currency("Norwegian Krone", "NOK");

    /** DOCUMENT ME! */
    public final static Currency NPR = new Currency("Nepalese Rupee", "NPR");

    /** DOCUMENT ME! */
    public final static Currency NZD = new Currency("New Zealand Dollar", "NZD");

    /** DOCUMENT ME! */
    public final static Currency OMR = new Currency("Rial Omani", "OMR");

    /** DOCUMENT ME! */
    public final static Currency PAB = new Currency("Balboa", "PAB");

    /** DOCUMENT ME! */
    public final static Currency PEN = new Currency("Nuevo Sol", "PEN");

    /** DOCUMENT ME! */
    public final static Currency PGK = new Currency("Kina", "PGK");

    /** DOCUMENT ME! */
    public final static Currency PHP = new Currency("Philippine Peso", "PHP");

    /** DOCUMENT ME! */
    public final static Currency PKR = new Currency("Pakistan Rupee", "PKR");

    /** DOCUMENT ME! */
    public final static Currency PLN = new Currency("Zloty", "PLN");

    /** DOCUMENT ME! */
    public final static Currency PTE = new Currency("Portuguese Escudo", "PTE"); //         PTE used for scriptural until 2001-12-31 and for cash until end of legal tender 2002-02-28

    /** DOCUMENT ME! */
    public final static Currency PYG = new Currency("Guarani", "PYG");

    /** DOCUMENT ME! */
    public final static Currency QAR = new Currency("Qatari Rial", "QAR");

    /** DOCUMENT ME! */
    public final static Currency ROL = new Currency("Leu", "ROL");

    /** DOCUMENT ME! */
    public final static Currency RUB = new Currency("Russian Ruble", "RUB");

    /** DOCUMENT ME! */
    public final static Currency RUR = new Currency("Russian Ruble", "RUR");

    /** DOCUMENT ME! */
    public final static Currency RWF = new Currency("Rwanda Franc", "RWF");

    /** DOCUMENT ME! */
    public final static Currency SAR = new Currency("Saudi Riyal", "SAR");

    /** DOCUMENT ME! */
    public final static Currency SBD = new Currency("Solomon Islands Dollar",
            "SBD");

    /** DOCUMENT ME! */
    public final static Currency SCR = new Currency("Seychelles Rupee", "SCR");

    /** DOCUMENT ME! */
    public final static Currency SDD = new Currency("Sudanese Dinar", "SDD");

    /** DOCUMENT ME! */
    public final static Currency SEK = new Currency("Swedish Krona", "SEK");

    /** DOCUMENT ME! */
    public final static Currency SGD = new Currency("Singapore Dollar", "SGD");

    /** DOCUMENT ME! */
    public final static Currency SHP = new Currency("Saint Helena Pound", "SHP");

    /** DOCUMENT ME! */
    public final static Currency SIT = new Currency("Tolar", "SIT");

    /** DOCUMENT ME! */
    public final static Currency SKK = new Currency("Slovak Koruna", "SKK");

    /** DOCUMENT ME! */
    public final static Currency SLL = new Currency("Leone", "SLL");

    /** DOCUMENT ME! */
    public final static Currency SOS = new Currency("Somali Shilling", "SOS");

    /** DOCUMENT ME! */
    public final static Currency SRG = new Currency("Suriname Guilder", "SRG");

    /** DOCUMENT ME! */
    public final static Currency STD = new Currency("Dobra", "STD");

    /** DOCUMENT ME! */
    public final static Currency SVC = new Currency("El Salvador Colon", "SVC");

    /** DOCUMENT ME! */
    public final static Currency SYP = new Currency("Syrian Pound", "SYP");

    /** DOCUMENT ME! */
    public final static Currency SZL = new Currency("Lilangeni", "SZL");

    /** DOCUMENT ME! */
    public final static Currency THB = new Currency("Baht", "THB");

    /** DOCUMENT ME! */
    public final static Currency TJS = new Currency("Somoni", "TJS");

    /** DOCUMENT ME! */
    public final static Currency TMM = new Currency("Manat", "TMM");

    /** DOCUMENT ME! */
    public final static Currency TND = new Currency("Tunisian Dinar", "TND");

    /** DOCUMENT ME! */
    public final static Currency TOP = new Currency("Pa�anga", "TOP");

    /** DOCUMENT ME! */
    public final static Currency TPE = new Currency("Timor Escudo", "TPE");

    /** DOCUMENT ME! */
    public final static Currency TRL = new Currency("Turkish Lira", "TRL");

    /** DOCUMENT ME! */
    public final static Currency TTD = new Currency("Trinidad and Tobago Dollar",
            "TTD");

    /** DOCUMENT ME! */
    public final static Currency TWD = new Currency("New Taiwan Dollar", "TWD");

    /** DOCUMENT ME! */
    public final static Currency TZS = new Currency("Tanzanian Shilling", "TZS");

    /** DOCUMENT ME! */
    public final static Currency UAH = new Currency("Hryvnia", "UAH");

    /** DOCUMENT ME! */
    public final static Currency UGX = new Currency("Uganda Shilling", "UGX");

    /** DOCUMENT ME! */
    public final static Currency USD = new Currency("US Dollar", "USD");

    /** DOCUMENT ME! */
    public final static Currency USN = new Currency("US Dollar (Same day)",
            "USN"); //   Funds code

    /** DOCUMENT ME! */
    public final static Currency USS = new Currency("US Dollar (Next day)",
            "USS"); //     Funds code

    /** DOCUMENT ME! */
    public final static Currency UYU = new Currency("Peso Uruguayo", "UYU");

    /** DOCUMENT ME! */
    public final static Currency UZS = new Currency("Uzbekistan Sum", "UZS");

    /** DOCUMENT ME! */
    public final static Currency VEB = new Currency("Bolivar", "VEB");

    /** DOCUMENT ME! */
    public final static Currency VND = new Currency("Dong", "VND");

    /** DOCUMENT ME! */
    public final static Currency VUV = new Currency("Vatu", "VUV");

    /** DOCUMENT ME! */
    public final static Currency WST = new Currency("Tala", "WST");

    /** DOCUMENT ME! */
    public final static Currency XAF = new Currency("CFA Franc BEAC", "XAF"); //CFA Franc BEAC; Responsible authority: Banque des �tats de l'Afrique Centrale.

    /** DOCUMENT ME! */
    public final static Currency XAG = new Currency("Silver", "XAG");

    /** DOCUMENT ME! */
    public final static Currency XAU = new Currency("Gold", "XAU");

    /** DOCUMENT ME! */
    public final static Currency XBA = new Currency("Bond Markets Units European Composite Unit (EURCO)",
            "XBA");

    /** DOCUMENT ME! */
    public final static Currency XBB = new Currency("Bond Markets Units European Monetary Unit (E.M.U.-6)",
            "XBB"); //E.M.U.-6 is sometimes known as the European Currency Unit. This should not be confused with the settlement unit of the European                                                                                                                                                                                                                          Monetary Co-operation Fund (E.M.C.F.) which has the same name.

    /** DOCUMENT ME! */
    public final static Currency XBC = new Currency("Bond Markets Units European Unit of Account 9 (E.U.A.-9)",
            "XBC");

    /** DOCUMENT ME! */
    public final static Currency XBD = new Currency("Bond Markets Units European Unit of Account 17 (E.U.A.-17)",
            "XBD");

    /** DOCUMENT ME! */
    public final static Currency XCD = new Currency("East Caribbean Dollar",
            "XCD");

    /** DOCUMENT ME! */
    public final static Currency XDR = new Currency("SDR", "XDR"); //**   This entry is not derived from ISO 3166, but is included here in alphabetic sequence for convenience

    /** DOCUMENT ME! */
    public final static Currency XFO = new Currency("Gold-Franc", "XFO");

    /** DOCUMENT ME! */
    public final static Currency XFU = new Currency("UIC-Franc", "XFU");

    /** DOCUMENT ME! */
    public final static Currency XOF = new Currency("Gold-Franc", "XOF");

    /** DOCUMENT ME! */
    public final static Currency XPD = new Currency("Palladium", "XPD");

    /** DOCUMENT ME! */
    public final static Currency XPF = new Currency("CFP Franc", "XPF");

    /** DOCUMENT ME! */
    public final static Currency XPT = new Currency("Platinum", "XPT");

    /** DOCUMENT ME! */
    public final static Currency XTS = new Currency("Codes specifically reserved for testing purposes",
            "XTS");

    /** DOCUMENT ME! */
    public final static Currency XXX = new Currency("The codes assigned for transactions where no currency is involved are:",
            "XXX");

    /** DOCUMENT ME! */
    public final static Currency YER = new Currency("Yemeni Rial", "YER");

    /** DOCUMENT ME! */
    public final static Currency YUM = new Currency("Yugoslavian Dinar", "YUM");

    /** DOCUMENT ME! */
    public final static Currency ZAR = new Currency("Rand", "ZAR");

    /** DOCUMENT ME! */
    public final static Currency ZMK = new Currency("Kwacha", "ZMK");

    /** DOCUMENT ME! */
    public final static Currency ZWD = new Currency("Zimbabwe Dollar", "ZWD");
}
