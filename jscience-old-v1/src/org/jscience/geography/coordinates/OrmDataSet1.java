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

package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class OrmDataSet1 extends OrmDataSet {
    /** DOCUMENT ME! */
    public static OrmDataSet1[] table = {
            new OrmDataSet1( // Dummy entry to force the first ORM to start at element "1" of array
                new String("Null ORM"), /*!< ISO18026 Label */
                new String("Empty ORM data entry"), /*!< ISO18026 Description */
                SRM_ORM_Code.ORM_UNDEFINED, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_UNDEFINED, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_UNDEFINED, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ABSTRACT_2D"), /*!< ISO18026 Label */
                new String("2D modelling space"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ABSTRACT_2D, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_2D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_ABSTRACT_2D, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ABSTRACT_3D"), /*!< ISO18026 Label */
                new String("3D modelling space"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ABSTRACT_3D, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_PLANE, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_ABSTRACT_3D, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ADINDAN_1991"), /*!< ISO18026 Label */
                new String("Adindan"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ADINDAN_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ADRASTEA_2000"), /*!< ISO18026 Label */
                new String("Adrastea"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ADRASTEA_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_ADRASTEA_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_ADRASTEA_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_AFGOOYE_1987"), /*!< ISO18026 Label */
                new String("Afgooye (Somalia)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_AFGOOYE_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_KRASSOVSKY_1940, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_AIN_EL_ABD_1970"), /*!< ISO18026 Label */
                new String("Ain el Abd"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_AIN_EL_ABD_1970, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_AMALTHEA_2000"), /*!< ISO18026 Label */
                new String("Amalthea"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_AMALTHEA_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_AMALTHEA_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_AMALTHEA_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_AMERICAN_SAMOA_1962"), /*!< ISO18026 Label */
                new String("American Samoa"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_AMERICAN_SAMOA_1962, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1866, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_AMERSFOORT_1885_1903"), /*!< ISO18026 Label */
                new String("Amersfoort 1885 - Revised"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_AMERSFOORT_1885_1903, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ANNA_1_1965"), /*!< ISO18026 Label */
                new String("Anna 1 (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ANNA_1_1965, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_AUSTRALIAN_NATIONAL_1966, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ANTIGUA_1943"), /*!< ISO18026 Label */
                new String("Antigua (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ANTIGUA_1943, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ARC_1950"), /*!< ISO18026 Label */
                new String("Arc"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ARC_1950, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ARC_1960"), /*!< ISO18026 Label */
                new String("Arc"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ARC_1960, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ARIEL_1988"), /*!< ISO18026 Label */
                new String("Ariel"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ARIEL_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_ARIEL_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_ARIEL_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ASCENSION_1958"), /*!< ISO18026 Label */
                new String("Ascension"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ASCENSION_1958, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ATLAS_1988"), /*!< ISO18026 Label */
                new String("Atlas"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ATLAS_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_ATLAS_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_ATLAS_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_AUSTRALIAN_GEOD_1966"), /*!< ISO18026 Label */
                new String("Australian Geodetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_AUSTRALIAN_GEOD_1966, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_AUSTRALIAN_NATIONAL_1966, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_AUSTRALIAN_GEOD_1984"), /*!< ISO18026 Label */
                new String("Australian Geodetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_AUSTRALIAN_GEOD_1984, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_AUSTRALIAN_NATIONAL_1966, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_AYABELLE_LIGHTHOUSE_1991"), /*!< ISO18026 Label */
                new String("Ayabelle Lighthouse (Djibouti)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_AYABELLE_LIGHTHOUSE_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BEACON_E_1945"), /*!< ISO18026 Label */
                new String("Beacon E (Iwo-jima; astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BEACON_E_1945, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BELGIUM_1972"), /*!< ISO18026 Label */
                new String("Belgium (Observatoire d'Uccle)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BELGIUM_1972, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BELINDA_1988"), /*!< ISO18026 Label */
                new String("Belinda"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BELINDA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_BELINDA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_BELINDA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BELLEVUE_IGN_1987"), /*!< ISO18026 Label */
                new String("Bellevue (IGN)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BELLEVUE_IGN_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BERMUDA_1957"), /*!< ISO18026 Label */
                new String("Bermuda"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BERMUDA_1957, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1866, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BERN_1898"), /*!< ISO18026 Label */
                new String("Bern"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BERN_1898, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BERN_1898_PM_BERN"), /*!< ISO18026 Label */
                new String("Bern (with the Prime Meridian at Bern)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BERN_1898_PM_BERN, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BIANCA_1988"), /*!< ISO18026 Label */
                new String("Bianca"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BIANCA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_BIANCA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_BIANCA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BISSAU_1991"), /*!< ISO18026 Label */
                new String("Bissau"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BISSAU_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BOGOTA_OBS_1987"), /*!< ISO18026 Label */
                new String("Bogota Observatory"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BOGOTA_OBS_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BOGOTA_OBS_1987_PM_BOGOTA"), /*!< ISO18026 Label */
                new String("Bogota Observatory (with the Prime Meridian at Bogota)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BOGOTA_OBS_1987_PM_BOGOTA, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_BUKIT_RIMPAH_1987"), /*!< ISO18026 Label */
                new String("Bukit Rimpah"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_BUKIT_RIMPAH_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CALLISTO_2000"), /*!< ISO18026 Label */
                new String("Callisto"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CALLISTO_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_CALLISTO_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_CALLISTO_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CALYPSO_1988"), /*!< ISO18026 Label */
                new String("Calypso"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CALYPSO_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CALYPSO_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_CALYPSO_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CAMP_AREA_1987"), /*!< ISO18026 Label */
                new String("Camp Area (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CAMP_AREA_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CAMPO_INCHAUSPE_1969"), /*!< ISO18026 Label */
                new String("Campo Inchauspe"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CAMPO_INCHAUSPE_1969, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CANTON_1966"), /*!< ISO18026 Label */
                new String("Canton (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CANTON_1966, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CAPE_1987"), /*!< ISO18026 Label */
                new String("Cape"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CAPE_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CAPE_CANAVERAL_1991"), /*!< ISO18026 Label */
                new String("Cape Canaveral"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CAPE_CANAVERAL_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1866, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CARTHAGE_1987"), /*!< ISO18026 Label */
                new String("Carthage"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CARTHAGE_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CHARON_1991"), /*!< ISO18026 Label */
                new String("Charon"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CHARON_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_CHARON_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_CHARON_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CHATHAM_1971"), /*!< ISO18026 Label */
                new String("Chatam (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CHATHAM_1971, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CHUA_1987"), /*!< ISO18026 Label */
                new String("Chua (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CHUA_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_COAMPS_1998"), /*!< ISO18026 Label */
                new String("COAMPS^(TM)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_COAMPS_1998, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_COAMPS_1998, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CORDELIA_1988"), /*!< ISO18026 Label */
                new String("Cordelia"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CORDELIA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_CORDELIA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_CORDELIA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CORREGO_ALEGRE_1987"), /*!< ISO18026 Label */
                new String("Corrego Alegre"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CORREGO_ALEGRE_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CRESSIDA_1988"), /*!< ISO18026 Label */
                new String("Cressida"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CRESSIDA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_CRESSIDA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_CRESSIDA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_CYPRUS_1935"), /*!< ISO18026 Label */
                new String("Cyprus"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_CYPRUS_1935, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1858, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DABOLA_1991"), /*!< ISO18026 Label */
                new String("Dabola"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DABOLA_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DECEPTION_1993"), /*!< ISO18026 Label */
                new String("Deception"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DECEPTION_1993, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DEIMOS_1988"), /*!< ISO18026 Label */
                new String("Deimos"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DEIMOS_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_DEIMOS_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_DEIMOS_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DESDEMONA_1988"), /*!< ISO18026 Label */
                new String("Desdemona"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DESDEMONA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_DESDEMONA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_DESDEMONA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DESPINA_1991"), /*!< ISO18026 Label */
                new String("Despina"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DESPINA_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_DESPINA_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_DESPINA_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DIONE_1982"), /*!< ISO18026 Label */
                new String("Dione"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DIONE_1982, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_DIONE_1982, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_DIONE_1982, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DJAKARTA_1987"), /*!< ISO18026 Label */
                new String("Djakarta (also known as Batavia)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DJAKARTA_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DJAKARTA_1987_PM_DJAKARTA"), /*!< ISO18026 Label */
                new String("Djakarta (also known as Batavia; with the Prime Meridian at Djakarta)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DJAKARTA_1987_PM_DJAKARTA, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DOS_1968"), /*!< ISO18026 Label */
                new String("DOS"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DOS_1968, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_DOS_71_4_1987"), /*!< ISO18026 Label */
                new String("DOS 71/4 (St. Helena Island; astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_DOS_71_4_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EARTH_INERTIAL_ARIES_1950"), /*!< ISO18026 Label */
                new String("Earth equatorial inertial, Aries mean of 1950"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EARTH_INERTIAL_ARIES_1950, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EARTH_INERTIAL_ARIES_TRUE_OF_DATE"), /*!< ISO18026 Label */
                new String("Earth equatorial inertial, Aries true of date"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EARTH_INERTIAL_ARIES_TRUE_OF_DATE, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EARTH_INERTIAL_J2000r0"), /*!< ISO18026 Label */
                new String("Earth equatorial inertial, J2000.0"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EARTH_INERTIAL_J2000r0, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EARTH_SOLAR_ECLIPTIC"), /*!< ISO18026 Label */
                new String("Solar ecliptic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EARTH_SOLAR_ECLIPTIC, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EARTH_SOLAR_EQUATORIAL"), /*!< ISO18026 Label */
                new String("Solar equatorial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EARTH_SOLAR_EQUATORIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EARTH_SOLAR_MAG_DIPOLE"), /*!< ISO18026 Label */
                new String("Solar magnetic dipole"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EARTH_SOLAR_MAG_DIPOLE, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EARTH_SOLAR_MAGNETOSPHERIC"), /*!< ISO18026 Label */
                new String("Solar magnetospheric"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EARTH_SOLAR_MAGNETOSPHERIC, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EASTER_1967"), /*!< ISO18026 Label */
                new String("Easter"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EASTER_1967, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ENCELADUS_1994"), /*!< ISO18026 Label */
                new String("Enceladus"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ENCELADUS_1994, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_ENCELADUS_1994, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_ENCELADUS_1994, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EPIMETHEUS_1988"), /*!< ISO18026 Label */
                new String("Epimetheus"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EPIMETHEUS_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EPIMETHEUS_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_EPIMETHEUS_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EROS_2000"), /*!< ISO18026 Label */
                new String("Eros (asteroid 433)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EROS_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_EROS_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_EROS_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ESTONIA_1937"), /*!< ISO18026 Label */
                new String("Estonia"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ESTONIA_1937, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ETRS_1989"), /*!< ISO18026 Label */
                new String("ETRS"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ETRS_1989, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EUROPA_2000"), /*!< ISO18026 Label */
                new String("Europa"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EUROPA_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_EUROPA_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_EUROPA_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EUROPE_1950"), /*!< ISO18026 Label */
                new String("European"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EUROPE_1950, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_EUROPE_1979"), /*!< ISO18026 Label */
                new String("European"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_EUROPE_1979, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_FAHUD_1987"), /*!< ISO18026 Label */
                new String("Fahud"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_FAHUD_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_FORT_THOMAS_1955"), /*!< ISO18026 Label */
                new String("Fort Thomas"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_FORT_THOMAS_1955, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GALATEA_1991"), /*!< ISO18026 Label */
                new String("Galatea"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GALATEA_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_GALATEA_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_GALATEA_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GAN_1970"), /*!< ISO18026 Label */
                new String("Gan"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GAN_1970, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GANYMEDE_2000"), /*!< ISO18026 Label */
                new String("Ganymede"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GANYMEDE_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_GANYMEDE_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_GANYMEDE_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GANYMEDE_MAGNETIC_2000"), /*!< ISO18026 Label */
                new String("Ganymede magnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GANYMEDE_MAGNETIC_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_GANYMEDE_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GASPRA_1991"), /*!< ISO18026 Label */
                new String("Gaspra (asteroid 951)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GASPRA_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GASPRA_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_GASPRA_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GDA_1994"), /*!< ISO18026 Label */
                new String("GDA"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GDA_1994, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEODETIC_DATUM_1949"), /*!< ISO18026 Label */
                new String("Geodetic Datum"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEODETIC_DATUM_1949, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1945"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1945, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1950"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1950, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1955"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1955, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1960"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1960, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1965"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1965, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1970"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1970, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1975"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1975, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1980"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1980, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1985"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1985, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1990"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1990, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_1995"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_1995, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GEOMAGNETIC_2000"), /*!< ISO18026 Label */
                new String("Geomagnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GEOMAGNETIC_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GGRS_1987"), /*!< ISO18026 Label */
                new String("GGRS 87)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GGRS_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GRACIOSA_BASE_SW_1948"), /*!< ISO18026 Label */
                new String("Graciosa Base SW"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GRACIOSA_BASE_SW_1948, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GUAM_1963"), /*!< ISO18026 Label */
                new String("Guam"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GUAM_1963, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1866, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GUNONG_SEGARA_1987"), /*!< ISO18026 Label */
                new String("Gunung Segara"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GUNONG_SEGARA_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_GUX_1_1987"), /*!< ISO18026 Label */
                new String("GUX1 (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_GUX_1_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HARTEBEESTHOCK_1994"), /*!< ISO18026 Label */
                new String("Hartebeesthock"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HARTEBEESTHOCK_1994, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HELENE_1992"), /*!< ISO18026 Label */
                new String("Helene"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HELENE_1992, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_HELENE_1992, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_HELENE_1992, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HELIO_ARIES_ECLIPTIC_J2000r0"), /*!< ISO18026 Label */
                new String("Heliocentric Aries ecliptic, J2000.0"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HELIO_ARIES_ECLIPTIC_J2000r0, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_SUN_1992, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HELIO_ARIES_ECLIPTIC_TRUE_OF_DATE"), /*!< ISO18026 Label */
                new String("Heliocentric Aries ecliptic, true of date"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HELIO_ARIES_ECLIPTIC_TRUE_OF_DATE, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_SUN_1992, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HELIO_EARTH_ECLIPTIC"), /*!< ISO18026 Label */
                new String("Heliocentric Earth ecliptic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HELIO_EARTH_ECLIPTIC, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_SUN_1992, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HELIO_EARTH_EQUATORIAL"), /*!< ISO18026 Label */
                new String("Heliocentric Earth equatorial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HELIO_EARTH_EQUATORIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_SUN_1992, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HERAT_NORTH_1987"), /*!< ISO18026 Label */
                new String("Herat North"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HERAT_NORTH_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HERMANNSKOGEL_1871"), /*!< ISO18026 Label */
                new String("Hermannskogel"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HERMANNSKOGEL_1871, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HJORSEY_1955"), /*!< ISO18026 Label */
                new String("Hjorsey"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HJORSEY_1955, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HONG_KONG_1963"), /*!< ISO18026 Label */
                new String("Hong Kong"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HONG_KONG_1963, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HONG_KONG_1980"), /*!< ISO18026 Label */
                new String("Hong Kong"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HONG_KONG_1980, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HU_TZU_SHAN_1991"), /*!< ISO18026 Label */
                new String("Hu-Tzu-Shan"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HU_TZU_SHAN_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_HUNGARIAN_1972"), /*!< ISO18026 Label */
                new String("Hungarian"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_HUNGARIAN_1972, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1967, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_IAPETUS_1988"), /*!< ISO18026 Label */
                new String("Iapetus"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_IAPETUS_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_IAPETUS_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_IAPETUS_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_IDA_1991"), /*!< ISO18026 Label */
                new String("Ida (asteroid 243)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_IDA_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_IDA_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_IDA_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_INDIAN_1916"), /*!< ISO18026 Label */
                new String("Indian"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_INDIAN_1916, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_ADJ_1937, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_INDIAN_1954"), /*!< ISO18026 Label */
                new String("Indian"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_INDIAN_1954, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_ADJ_1937, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_INDIAN_1956"), /*!< ISO18026 Label */
                new String("Indian"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_INDIAN_1956, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_1956, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_INDIAN_1960"), /*!< ISO18026 Label */
                new String("Indian"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_INDIAN_1960, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_ADJ_1937, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_INDIAN_1962"), /*!< ISO18026 Label */
                new String("Indian"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_INDIAN_1962, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_REVISED_1962, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_INDIAN_1975"), /*!< ISO18026 Label */
                new String("Indian"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_INDIAN_1975, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_ADJ_1937, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_INDONESIAN_1974"), /*!< ISO18026 Label */
                new String("Indonesian"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_INDONESIAN_1974, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INDONESIAN_1974, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_IO_2000"), /*!< ISO18026 Label */
                new String("Io"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_IO_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_IO_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_IO_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_IRAQ_KUWAIT_BNDRY_1992"), /*!< ISO18026 Label */
                new String("Iraq/Kuwait Boundary"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_IRAQ_KUWAIT_BNDRY_1992, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_IRELAND_1965"), /*!< ISO18026 Label */
                new String("Ireland 1965"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_IRELAND_1965, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_MODIFIED_AIRY_1849, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ISTS_061_1968"), /*!< ISO18026 Label */
                new String("ISTS 061 (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ISTS_061_1968, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ISTS_073_1969"), /*!< ISO18026 Label */
                new String("ISTS 073 (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ISTS_073_1969, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JANUS_1988"), /*!< ISO18026 Label */
                new String("Janus"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JANUS_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_JANUS_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_JANUS_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JGD_2000"), /*!< ISO18026 Label */
                new String("Japanese Geodetic Datum 2000 (JGD2000)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JGD_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JOHNSTON_1961"), /*!< ISO18026 Label */
                new String("Johnston"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JOHNSTON_1961, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JULIET_1988"), /*!< ISO18026 Label */
                new String("Juliet"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JULIET_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_JULIET_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_JULIET_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JUPITER_1988"), /*!< ISO18026 Label */
                new String("Jupiter"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JUPITER_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_JUPITER_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_JUPITER_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JUPITER_INERTIAL"), /*!< ISO18026 Label */
                new String("Jupiter equatorial inertial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JUPITER_INERTIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_JUPITER_1988, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JUPITER_MAGNETIC_1992"), /*!< ISO18026 Label */
                new String("Jupiter magnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JUPITER_MAGNETIC_1992, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_JUPITER_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JUPITER_SOLAR_ECLIPTIC"), /*!< ISO18026 Label */
                new String("Jupiter solar ecliptic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JUPITER_SOLAR_ECLIPTIC, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_JUPITER_1988, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JUPITER_SOLAR_EQUATORIAL"), /*!< ISO18026 Label */
                new String("Jupiter solar equatorial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JUPITER_SOLAR_EQUATORIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_JUPITER_1988, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JUPITER_SOLAR_MAG_DIPOLE"), /*!< ISO18026 Label */
                new String("Jupiter solar magnetic dipole"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JUPITER_SOLAR_MAG_DIPOLE, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_JUPITER_1988, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_JUPITER_SOLAR_MAG_ECLIPTIC"), /*!< ISO18026 Label */
                new String("Jupiter solar magnetic ecliptic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_JUPITER_SOLAR_MAG_ECLIPTIC, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_JUPITER_1988, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_KANDAWALA_1987"), /*!< ISO18026 Label */
                new String("Kandawala"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_KANDAWALA_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_ADJ_1937, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_KERGUELEN_1949"), /*!< ISO18026 Label */
                new String("Kerguelen"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_KERGUELEN_1949, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_KERTAU_1948"), /*!< ISO18026 Label */
                new String("Kertau"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_KERTAU_1948, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_1948, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_KOREAN_GEODETIC_1995"), /*!< ISO18026 Label */
                new String("Korean Geodetic System"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_KOREAN_GEODETIC_1995, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_WGS_1984, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_KUSAIE_1951"), /*!< ISO18026 Label */
                new String("Kusaie 1951 (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_KUSAIE_1951, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_LANDESVERMESSUNG_1995"), /*!< ISO18026 Label */
                new String("Landesvermessung (CH1903+)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_LANDESVERMESSUNG_1995, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_LARISSA_1991"), /*!< ISO18026 Label */
                new String("Larissa"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_LARISSA_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_LARISSA_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_LARISSA_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_LC5_1961"), /*!< ISO18026 Label */
                new String("LC5 (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_LC5_1961, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1866, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_LEIGON_1991"), /*!< ISO18026 Label */
                new String("Leigon"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_LEIGON_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_LIBERIA_1964"), /*!< ISO18026 Label */
                new String("Liberia"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_LIBERIA_1964, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_LISBON_D73"), /*!< ISO18026 Label */
                new String("Lisbon D73 (Castelo di Sao Jorge)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_LISBON_D73, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_LKS_1994"), /*!< ISO18026 Label */
                new String("LKS94"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_LKS_1994, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_LUZON_1987"), /*!< ISO18026 Label */
                new String("Luzon"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_LUZON_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1866, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_M_PORALOKO_1991"), /*!< ISO18026 Label */
                new String("M'Poraloko"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_M_PORALOKO_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MAHE_1971"), /*!< ISO18026 Label */
                new String("Mahe"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MAHE_1971, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MARCUS_STATION_1952"), /*!< ISO18026 Label */
                new String("Marcus Station (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MARCUS_STATION_1952, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MARS_2000"), /*!< ISO18026 Label */
                new String("Mars"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MARS_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_MARS_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_MARS_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MARS_INERTIAL"), /*!< ISO18026 Label */
                new String("Mars equatorial inertial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MARS_INERTIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_MARS_2000, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MARS_SPHERE_2000"), /*!< ISO18026 Label */
                new String("Mars (spherical)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MARS_SPHERE_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MARS_SPHERE_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_MARS_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MASS_1999"), /*!< ISO18026 Label */
                new String("MASS"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MASS_1999, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MASS_1999, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MASSAWA_1987"), /*!< ISO18026 Label */
                new String("Massawa"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MASSAWA_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MERCHICH_1987"), /*!< ISO18026 Label */
                new String("Merchich"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MERCHICH_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MERCURY_1988"), /*!< ISO18026 Label */
                new String("Mercury"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MERCURY_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MERCURY_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_MERCURY_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MERCURY_INERTIAL"), /*!< ISO18026 Label */
                new String("Mercury equatorial inertial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MERCURY_INERTIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_MERCURY_1988, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_METIS_2000"), /*!< ISO18026 Label */
                new String("Metis"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_METIS_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_METIS_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_METIS_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MIDWAY_1961"), /*!< ISO18026 Label */
                new String("Midway 1961 (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MIDWAY_1961, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MIMAS_1994"), /*!< ISO18026 Label */
                new String("Mimas"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MIMAS_1994, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MIMAS_1994, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_MIMAS_1994, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MINNA_1991"), /*!< ISO18026 Label */
                new String("Minna"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MINNA_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MIRANDA_1988"), /*!< ISO18026 Label */
                new String("Miranda"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MIRANDA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MIRANDA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_MIRANDA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MM5_1997"), /*!< ISO18026 Label */
                new String("MM5 (AFWA)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MM5_1997, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MM5_1997, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MODTRAN_MIDLATITUDE_N_1989"), /*!< ISO18026 Label */
                new String("MODTRAN"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MODTRAN_MIDLATITUDE_N_1989, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MODTRAN_MIDLATITUDE_1989, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MODTRAN_MIDLATITUDE_S_1989"), /*!< ISO18026 Label */
                new String("MODTRAN"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MODTRAN_MIDLATITUDE_S_1989, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MODTRAN_MIDLATITUDE_1989, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MODTRAN_SUBARCTIC_N_1989"), /*!< ISO18026 Label */
                new String("MODTRAN"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MODTRAN_SUBARCTIC_N_1989, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MODTRAN_SUBARCTIC_1989, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MODTRAN_SUBARCTIC_S_1989"), /*!< ISO18026 Label */
                new String("MODTRAN"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MODTRAN_SUBARCTIC_S_1989, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MODTRAN_SUBARCTIC_1989, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MODTRAN_TROPICAL_1989"), /*!< ISO18026 Label */
                new String("MODTRAN"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MODTRAN_TROPICAL_1989, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MODTRAN_TROPICAL_1989, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MONTSERRAT_1958"), /*!< ISO18026 Label */
                new String("Montserrat (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MONTSERRAT_1958, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MOON_1991"), /*!< ISO18026 Label */
                new String("Moon"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MOON_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MOON_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_MOON_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_MULTIGEN_FLAT_EARTH_1989"), /*!< ISO18026 Label */
                new String("Multigen flat Earth"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_MULTIGEN_FLAT_EARTH_1989, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_MULTIGEN_FLAT_EARTH_1989, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_N_AM_1927"), /*!< ISO18026 Label */
                new String("North American"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_N_AM_1927, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1866, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_N_AM_1983"), /*!< ISO18026 Label */
                new String("North American"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_N_AM_1983, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_N_SAHARA_1959"), /*!< ISO18026 Label */
                new String("North Sahara"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_N_SAHARA_1959, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_NAHRWAN_1987"), /*!< ISO18026 Label */
                new String("Nahrwan"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_NAHRWAN_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_NAIAD_1991"), /*!< ISO18026 Label */
                new String("Naiad"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_NAIAD_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_NAIAD_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_NAIAD_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_NAPARIMA_1991"), /*!< ISO18026 Label */
                new String("Naparima BWI"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_NAPARIMA_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_NEPTUNE_1991"), /*!< ISO18026 Label */
                new String("Neptune"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_NEPTUNE_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_NEPTUNE_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_NEPTUNE_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_NEPTUNE_INERTIAL"), /*!< ISO18026 Label */
                new String("Neptune equatorial inertial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_NEPTUNE_INERTIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_NEPTUNE_1991, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_NEPTUNE_MAGNETIC_1993"), /*!< ISO18026 Label */
                new String("Neptune magnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_NEPTUNE_MAGNETIC_1993, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_NEPTUNE_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_NOGAPS_1988"), /*!< ISO18026 Label */
                new String("NOGAPS"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_NOGAPS_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_NOGAPS_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_NTF_1896"), /*!< ISO18026 Label */
                new String("NTF"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_NTF_1896, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880_IGN, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_NTF_1896_PM_PARIS"), /*!< ISO18026 Label */
                new String("NTF (with the Prime Meridian at Paris)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_NTF_1896_PM_PARIS, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880_IGN, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_OBERON_1988"), /*!< ISO18026 Label */
                new String("Oberon"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_OBERON_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_OBERON_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_OBERON_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_OBSERV_METEORO_1939"), /*!< ISO18026 Label */
                new String("Observatorio Meteorologico"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_OBSERV_METEORO_1939, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_OLD_EGYPTIAN_1907"), /*!< ISO18026 Label */
                new String("Old Egyptian"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_OLD_EGYPTIAN_1907, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_HELMERT_1906, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_OLD_HAW_CLARKE_1987"), /*!< ISO18026 Label */
                new String("Old Hawaiian (Clarke)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_OLD_HAW_CLARKE_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1866, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_OLD_HAW_INT_1987"), /*!< ISO18026 Label */
                new String("Old Hawaiian (International)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_OLD_HAW_INT_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_OPHELIA_1988"), /*!< ISO18026 Label */
                new String("Ophelia"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_OPHELIA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_OPHELIA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_OPHELIA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_OSGB_1936"), /*!< ISO18026 Label */
                new String("Ordnance Survey of Great Britain"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_OSGB_1936, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_AIRY_1830, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PALESTINE_1928"), /*!< ISO18026 Label */
                new String("Palestine"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PALESTINE_1928, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880_PALESTINE, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PAN_1991"), /*!< ISO18026 Label */
                new String("Pan"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PAN_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_PAN_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PAN_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PANDORA_1988"), /*!< ISO18026 Label */
                new String("Pandora"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PANDORA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_PANDORA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PANDORA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PHOBOS_1988"), /*!< ISO18026 Label */
                new String("Phobos"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PHOBOS_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_PHOBOS_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PHOBOS_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PHOEBE_1988"), /*!< ISO18026 Label */
                new String("Phoebe"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PHOEBE_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_PHOEBE_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PHOEBE_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PICO_DE_LAS_NIEVES_1987"), /*!< ISO18026 Label */
                new String("Pico de las Nieves"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PICO_DE_LAS_NIEVES_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PITCAIRN_1967"), /*!< ISO18026 Label */
                new String("Pitcairn (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PITCAIRN_1967, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PLUTO_1994"), /*!< ISO18026 Label */
                new String("Pluto"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PLUTO_1994, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_PLUTO_1994, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PLUTO_1994, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PLUTO_INERTIAL"), /*!< ISO18026 Label */
                new String("Pluto equatorial inertial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PLUTO_INERTIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PLUTO_1994, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_POINT_58_1991"), /*!< ISO18026 Label */
                new String("Point 58"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_POINT_58_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_POINTE_NOIRE_1948"), /*!< ISO18026 Label */
                new String("Pointe Noire"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_POINTE_NOIRE_1948, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PORTIA_1988"), /*!< ISO18026 Label */
                new String("Portia"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PORTIA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_PORTIA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PORTIA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PORTO_SANTO_1936"), /*!< ISO18026 Label */
                new String("Porto Santo"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PORTO_SANTO_1936, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PROMETHEUS_1988"), /*!< ISO18026 Label */
                new String("Prometheus"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PROMETHEUS_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_PROMETHEUS_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PROMETHEUS_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PROTEUS_1991"), /*!< ISO18026 Label */
                new String("Proteus"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PROTEUS_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_PROTEUS_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PROTEUS_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PROV_S_AM_1956"), /*!< ISO18026 Label */
                new String("Provisional South American"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PROV_S_AM_1956, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PROV_S_CHILEAN_1963"), /*!< ISO18026 Label */
                new String("Provisional South Chilean (Hito XVIII)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PROV_S_CHILEAN_1963, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PUCK_1988"), /*!< ISO18026 Label */
                new String("Puck"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PUCK_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_PUCK_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_PUCK_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PUERTO_RICO_1987"), /*!< ISO18026 Label */
                new String("Puerto Rico"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PUERTO_RICO_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1866, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_PULKOVO_1942"), /*!< ISO18026 Label */
                new String("Pulkovo"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_PULKOVO_1942, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_KRASSOVSKY_1940, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_QATAR_NATIONAL_1974"), /*!< ISO18026 Label */
                new String("Qatar National"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_QATAR_NATIONAL_1974, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_QATAR_NATIONAL_1995"), /*!< ISO18026 Label */
                new String("Qatar National"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_QATAR_NATIONAL_1995, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_QORNOQ_1987"), /*!< ISO18026 Label */
                new String("Qornoq"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_QORNOQ_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_REUNION_1947"), /*!< ISO18026 Label */
                new String("Reunion"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_REUNION_1947, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_RGF_1993"), /*!< ISO18026 Label */
                new String("Reseau Geodesique Francais"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_RGF_1993, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_RHEA_1988"), /*!< ISO18026 Label */
                new String("Rhea"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_RHEA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_RHEA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_RHEA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ROME_1940"), /*!< ISO18026 Label */
                new String("Rome (also known as Monte Mario)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ROME_1940, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ROME_1940_PM_ROME"), /*!< ISO18026 Label */
                new String("Rome (also known as Monte Mario) (with the Prime Meridian at Rome)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ROME_1940_PM_ROME, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_ROSALIND_1988"), /*!< ISO18026 Label */
                new String("Rosalind"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ROSALIND_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_ROSALIND_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_ROSALIND_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_RT_1990"), /*!< ISO18026 Label */
                new String("RT"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_RT_1990, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_RT_1990_PM_STOCKHOLM"), /*!< ISO18026 Label */
                new String("RT (with the Prime Meridian at Stockholm)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_RT_1990_PM_STOCKHOLM, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_S_AM_1969"), /*!< ISO18026 Label */
                new String("South American"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_S_AM_1969, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_SOUTH_AMERICAN_1969, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_S_ASIA_1987"), /*!< ISO18026 Label */
                new String("South Asia"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_S_ASIA_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_MODIFIED_FISCHER_1960, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_S_JTSK_1993"), /*!< ISO18026 Label */
                new String("S-JTSK"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_S_JTSK_1993, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_S42_PULKOVO"), /*!< ISO18026 Label */
                new String("S-42 (Pulkovo)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_S42_PULKOVO, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_KRASSOVSKY_1940, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SANTO_DOS_1965"), /*!< ISO18026 Label */
                new String("Santo (DOS)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SANTO_DOS_1965, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SAO_BRAZ_1987"), /*!< ISO18026 Label */
                new String("Sao Braz"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SAO_BRAZ_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SAPPER_HILL_1943"), /*!< ISO18026 Label */
                new String("Sapper Hill"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SAPPER_HILL_1943, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SATURN_1988"), /*!< ISO18026 Label */
                new String("Saturn"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SATURN_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_SATURN_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_SATURN_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SATURN_INERTIAL"), /*!< ISO18026 Label */
                new String("Saturn equatorial inertial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SATURN_INERTIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_SATURN_1988, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SATURN_MAGNETIC_1993"), /*!< ISO18026 Label */
                new String("Saturn magnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SATURN_MAGNETIC_1993, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_SATURN_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SCHWARZECK_1991"), /*!< ISO18026 Label */
                new String("Schwarzeck"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SCHWARZECK_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_NAMIBIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SELVAGEM_GRANDE_1938"), /*!< ISO18026 Label */
                new String("Selvagem Grande"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SELVAGEM_GRANDE_1938, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SIERRA_LEONE_1960"), /*!< ISO18026 Label */
                new String("Sierra Leone"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SIERRA_LEONE_1960, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SIRGAS_2000"), /*!< ISO18026 Label */
                new String("SIRGAS"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SIRGAS_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_GRS_1980, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SOUTHEAST_1943"), /*!< ISO18026 Label */
                new String("Southeast"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SOUTHEAST_1943, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet1(new String("ORM_SOVIET_GEODETIC_1985"), /*!< ISO18026 Label */
                new String("Soviet Geodetic System"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SOVIET_GEODETIC_1985, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_SOVIET_GEODETIC_1985, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */)
        };

/**
     * Creates a new OrmDataSet1 object.
     *
     * @param label         DOCUMENT ME!
     * @param description   DOCUMENT ME!
     * @param orm_code      DOCUMENT ME!
     * @param orm_template  DOCUMENT ME!
     * @param rd_code       DOCUMENT ME!
     * @param reference_orm DOCUMENT ME!
     * @param hsr_count     DOCUMENT ME!
     */
    private OrmDataSet1(String label, String description,
        SRM_ORM_Code orm_code, SRM_ORMT_Code orm_template, SRM_RD_Code rd_code,
        SRM_ORM_Code reference_orm, int hsr_count) {
        super(label, description, orm_code, orm_template, rd_code,
            reference_orm, hsr_count);
    }

    /**
     * DOCUMENT ME!
     *
     * @param code DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static OrmDataSet getElem(SRM_ORM_Code code) {
        return table[code.toInt()];
    }
}
