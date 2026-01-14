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
class OrmDataSet2 extends OrmDataSet {
    /** DOCUMENT ME! */
    public static OrmDataSet2[] table = {
            new OrmDataSet2(new String("ORM_SOVIET_GEODETIC_1990"), /*!< ISO18026 Label */
                new String("Soviet Geodetic System"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SOVIET_GEODETIC_1990, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_SOVIET_GEODETIC_1990, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_SUN_1992"), /*!< ISO18026 Label */
                new String("Sun"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_SUN_1992, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_SUN_1992, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_SUN_1992, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TAN_OBS_1925"), /*!< ISO18026 Label */
                new String("Tananarive Observatory"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TAN_OBS_1925, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TAN_OBS_1925_PM_PARIS"), /*!< ISO18026 Label */
                new String("Tananarive Observatory (with the Prime Meridian at Paris)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TAN_OBS_1925_PM_PARIS, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TELESTO_1988"), /*!< ISO18026 Label */
                new String("Telesto"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TELESTO_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_TELESTO_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_TELESTO_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TERN_1961"), /*!< ISO18026 Label */
                new String("Tern (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TERN_1961, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TETHYS_1991"), /*!< ISO18026 Label */
                new String("Tethys"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TETHYS_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_TETHYS_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_TETHYS_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_THALASSA_1991"), /*!< ISO18026 Label */
                new String("Thalassa"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_THALASSA_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_THALASSA_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_THALASSA_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_THEBE_2000"), /*!< ISO18026 Label */
                new String("Thebe"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_THEBE_2000, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_THEBE_2000, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_THEBE_2000, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TIM_BESSEL_1948"), /*!< ISO18026 Label */
                new String("Timbali (Bessel)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TIM_BESSEL_1948, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TIM_BESSEL_ADJ_1968"), /*!< ISO18026 Label */
                new String("Timbali (Bessel) - adjusted"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TIM_BESSEL_ADJ_1968, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TIM_EV_1948"), /*!< ISO18026 Label */
                new String("Timbali (Everest)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TIM_EV_1948, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_BRUNEI_1967, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TIM_EV_ADJ_1968"), /*!< ISO18026 Label */
                new String("Timbali (Everest) - adjusted"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TIM_EV_ADJ_1968, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_EVEREST_BRUNEI_1967, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TITAN_1982"), /*!< ISO18026 Label */
                new String("Titan"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TITAN_1982, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_TITAN_1982, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_TITAN_1982, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TITANIA_1988"), /*!< ISO18026 Label */
                new String("Titania"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TITANIA_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_TITANIA_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_TITANIA_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TOKYO_1991"), /*!< ISO18026 Label */
                new String("Tokyo"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TOKYO_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_BESSEL_1841_ETHIOPIA, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TRISTAN_1968"), /*!< ISO18026 Label */
                new String("Tristan (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TRISTAN_1968, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_TRITON_1991"), /*!< ISO18026 Label */
                new String("Triton"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_TRITON_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_TRITON_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_TRITON_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_UMBRIEL_1988"), /*!< ISO18026 Label */
                new String("Umbriel"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_UMBRIEL_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_UMBRIEL_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_UMBRIEL_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_URANUS_1988"), /*!< ISO18026 Label */
                new String("Uranus"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_URANUS_1988, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_URANUS_1988, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_URANUS_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_URANUS_INERTIAL"), /*!< ISO18026 Label */
                new String("Uranus equatorial inertial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_URANUS_INERTIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_URANUS_1988, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_URANUS_MAGNETIC_1993"), /*!< ISO18026 Label */
                new String("Uranus magnetic"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_URANUS_MAGNETIC_1993, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_URANUS_1988, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_VENUS_1991"), /*!< ISO18026 Label */
                new String("Venus"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_VENUS_1991, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_SPHERE, /*!< ORM Template Code */
                SRM_RD_Code.RD_VENUS_1991, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_VENUS_1991, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_VENUS_INERTIAL"), /*!< ISO18026 Label */
                new String("Venus equatorial inertial"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_VENUS_INERTIAL, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_3D, /*!< ORM Template Code */
                SRM_RD_Code.RD_UNDEFINED, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_VENUS_1991, /*!< Reference ORM */
                0 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_VITI_LEVU_1916"), /*!< ISO18026 Label */
                new String("Viti Levu"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_VITI_LEVU_1916, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_VOIROL_1874"), /*!< ISO18026 Label */
                new String("Voirol"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_VOIROL_1874, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_VOIROL_1874_PM_PARIS"), /*!< ISO18026 Label */
                new String("Voirol (with the Prime Meridian at Paris)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_VOIROL_1874_PM_PARIS, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_VOIROL_1960"), /*!< ISO18026 Label */
                new String("Voirol - Revised"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_VOIROL_1960, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_VOIROL_1960_PM_PARIS"), /*!< ISO18026 Label */
                new String("Voirol - Revised (with the Prime Meridian at Paris)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_VOIROL_1960_PM_PARIS, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_CLARKE_1880, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_WAKE_1952"), /*!< ISO18026 Label */
                new String("Wake (astronomic)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_WAKE_1952, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_WAKE_ENIWETOK_1960"), /*!< ISO18026 Label */
                new String("Wake-Eniwetok"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_WAKE_ENIWETOK_1960, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_HOUGH_1960, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_WGS_1972"), /*!< ISO18026 Label */
                new String("World Geodetic System"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_WGS_1972, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_WGS_1972, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_WGS_1984"), /*!< ISO18026 Label */
                new String("World Geodetic System"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_WGS_1984, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_WGS_1984, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_YACARE_1987"), /*!< ISO18026 Label */
                new String("Yacare (Uruguay)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_YACARE_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */),
            
            new OrmDataSet2(new String("ORM_ZANDERIJ_1987"), /*!< ISO18026 Label */
                new String("Zanderij (Suriname)"), /*!< ISO18026 Published name */
                SRM_ORM_Code.ORM_ZANDERIJ_1987, /*!< ORM Code enumeration */
                SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID, /*!< ORM Template Code */
                SRM_RD_Code.RD_INTERNATIONAL_1924, /*!< RD parameterization code */
                SRM_ORM_Code.ORM_WGS_1984, /*!< Reference ORM */
                1 /*!< HSR count for this ORM */)
        };

/**
     * Creates a new OrmDataSet2 object.
     *
     * @param label         DOCUMENT ME!
     * @param description   DOCUMENT ME!
     * @param orm_code      DOCUMENT ME!
     * @param orm_template  DOCUMENT ME!
     * @param rd_code       DOCUMENT ME!
     * @param reference_orm DOCUMENT ME!
     * @param hsr_count     DOCUMENT ME!
     */
    private OrmDataSet2(String label, String description,
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
        return table[code.toInt() - 242];
    }
}
