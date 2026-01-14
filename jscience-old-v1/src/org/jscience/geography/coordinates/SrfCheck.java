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
class SrfCheck {
    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean isValidOrmHsr(SRM_ORM_Code orm, SRM_HSR_Code hsr) {
        boolean isValid = true;

        // Undefined HSR is valid for all ORMs
        if (hsr != SRM_HSR_Code.HSR_UNDEFINED) {
            try {
                HsrDataSet.getElem(orm, hsr);
            } catch (SrmException ex) {
                isValid = false;
            }
        } else {
            isValid = false;
        }

        return isValid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forCelestiocentric(SRM_ORM_Code orm, SRM_HSR_Code hsr)
        throws SrmException {
        if ((OrmDataSet.getElem(orm)._orm_template == SRM_ORMT_Code.ORMT_BI_AXIS_ORIGIN_2D) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_Celestiocentric: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forLocalSpaceRectangular2D(SRM_ORM_Code orm,
        SRM_HSR_Code hsr, SRF_LSR_2D_Params params) throws SrmException {
        if ((orm != SRM_ORM_Code.ORM_ABSTRACT_2D) || !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_LocalSpaceRectangular2D: Invalid ORM/HSR pair");
        }

        Lsr2Conv.compute_F_u_v(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forLocalSpaceRectangular3D(SRM_ORM_Code orm,
        SRM_HSR_Code hsr, SRF_LSR_3D_Params params) throws SrmException {
        if ((orm != SRM_ORM_Code.ORM_ABSTRACT_3D) || !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_LocalSpaceRectangular3D: Invalid ORM/HSR pair");
        }

        Lsr3Conv.compute_F_u_v_w(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forCelestiodetic(SRM_ORM_Code orm, SRM_HSR_Code hsr)
        throws SrmException {
        if (((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) &&
                (OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID)) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_Celestiodetic: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forPlanetodetic(SRM_ORM_Code orm, SRM_HSR_Code hsr)
        throws SrmException {
        if (((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) &&
                (OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID)) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_Planetodetic: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forLocalTangentSpaceEuclidean(SRM_ORM_Code orm,
        SRM_HSR_Code hsr, SRF_LTSE_Params params) throws SrmException {
        if (((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) &&
                (OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID)) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_LocalTangentSpaceEuclidean: Invalid ORM/HSR pair");
        }

        if (!Const.isWellFormedLongitude(params.geodetic_longitude) ||
                !Const.isWellFormedLatitude(params.geodetic_latitude) ||
                !Const.isWellFormedAzimuth(params.azimuth)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("SRF_LocalTangentSpaceEuclidean: Invalid SRF parameters"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forTransverseMercator(SRM_ORM_Code orm,
        SRM_HSR_Code hsr, SRF_Mercator_Params params) throws SrmException {
        if (((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) &&
                (OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID)) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_TransverseMercator: Invalid ORM/HSR pair");
        }

        if (!Const.isWellFormedLongitude(params.origin_longitude) ||
                !Const.isWellFormedLatitude(params.origin_latitude)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("SRF_TransverseMercator: Invalid SRF parameters"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forMercator(SRM_ORM_Code orm, SRM_HSR_Code hsr,
        SRF_Mercator_Params params) throws SrmException {
        if (((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) &&
                (OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID)) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_Mercator: Invalid ORM/HSR pair");
        }

        if (!Const.isWellFormedLongitude(params.origin_longitude) ||
                !Const.isWellFormedLatitude(params.origin_latitude)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("SRF_Mercator: Invalid SRF parameters"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forLambertConformalConic(SRM_ORM_Code orm,
        SRM_HSR_Code hsr, SRF_LCC_Params params) throws SrmException {
        if (((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) &&
                (OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID)) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_LambertConformalConic: Invalid ORM/HSR pair");
        }

        if (!Const.isWellFormedLongitude(params.origin_longitude) ||
                !Const.isWellFormedLatitude(params.origin_latitude) ||
                !Const.isWellFormedLatitude(params.latitude1) ||
                !Const.isWellFormedLatitude(params.latitude2)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("SRF_LambertConformalConic: Invalid SRF parameters"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forLocalTangentSpaceCylindrical(SRM_ORM_Code orm,
        SRM_HSR_Code hsr, SRF_LT_Params params) throws SrmException {
        if (((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) &&
                (OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID)) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_LocalTangentSpaceCylindrical: Invalid ORM/HSR pair");
        }

        if (!Const.isWellFormedLongitude(params.geodetic_longitude) ||
                !Const.isWellFormedLatitude(params.geodetic_latitude) ||
                !Const.isWellFormedAzimuth(params.azimuth)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("SRF_LocalTangentSpaceCylindrical: Invalid SRF parameters"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forLocalTangentSpaceAzimuthalSpherical(
        SRM_ORM_Code orm, SRM_HSR_Code hsr, SRF_LT_Params params)
        throws SrmException {
        if (((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) &&
                (OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID)) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_LocalTangentSpaceAzimuthalSpherical: Invalid ORM/HSR pair");
        }

        if (!Const.isWellFormedLongitude(params.geodetic_longitude) ||
                !Const.isWellFormedLatitude(params.geodetic_latitude) ||
                !Const.isWellFormedAzimuth(params.azimuth)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("SRF_LocalTangentSpaceAzimuthalSpherical: Invalid SRF parameters"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forCelestiomagnetic(SRM_ORM_Code orm, SRM_HSR_Code hsr)
        throws SrmException {
        if (((orm != SRM_ORM_Code.ORM_GANYMEDE_MAGNETIC_2000) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1945) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1950) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1955) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1960) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1965) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1970) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1975) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1980) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1985) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1990) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_1995) &&
                (orm != SRM_ORM_Code.ORM_GEOMAGNETIC_2000) &&
                (orm != SRM_ORM_Code.ORM_JUPITER_MAGNETIC_1992) &&
                (orm != SRM_ORM_Code.ORM_NEPTUNE_MAGNETIC_1993) &&
                (orm != SRM_ORM_Code.ORM_SATURN_MAGNETIC_1993) &&
                (orm != SRM_ORM_Code.ORM_URANUS_MAGNETIC_1993)) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_Celestiomagnetic: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forEquatorialInertial(SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        if (((orm != SRM_ORM_Code.ORM_EARTH_INERTIAL_ARIES_1950) &&
                (orm != SRM_ORM_Code.ORM_EARTH_INERTIAL_ARIES_TRUE_OF_DATE) &&
                (orm != SRM_ORM_Code.ORM_EARTH_INERTIAL_J2000r0) &&
                (orm != SRM_ORM_Code.ORM_JUPITER_INERTIAL) &&
                (orm != SRM_ORM_Code.ORM_MARS_INERTIAL) &&
                (orm != SRM_ORM_Code.ORM_MERCURY_INERTIAL) &&
                (orm != SRM_ORM_Code.ORM_NEPTUNE_INERTIAL) &&
                (orm != SRM_ORM_Code.ORM_PLUTO_INERTIAL) &&
                (orm != SRM_ORM_Code.ORM_SATURN_INERTIAL) &&
                (orm != SRM_ORM_Code.ORM_URANUS_INERTIAL) &&
                (orm != SRM_ORM_Code.ORM_VENUS_INERTIAL)) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_EquatorialInertial: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forSolarEcliptic(SRM_ORM_Code orm, SRM_HSR_Code hsr)
        throws SrmException {
        if (((orm != SRM_ORM_Code.ORM_EARTH_SOLAR_ECLIPTIC) &&
                (orm != SRM_ORM_Code.ORM_JUPITER_SOLAR_ECLIPTIC)) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_SolarEcliptic: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forSolarEquatorial(SRM_ORM_Code orm, SRM_HSR_Code hsr)
        throws SrmException {
        if (((orm != SRM_ORM_Code.ORM_EARTH_SOLAR_EQUATORIAL) &&
                (orm != SRM_ORM_Code.ORM_JUPITER_SOLAR_EQUATORIAL)) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_SolarEquatorial: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forSolarMagneticDipole(SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        if (((orm != SRM_ORM_Code.ORM_EARTH_SOLAR_MAG_DIPOLE) &&
                (orm != SRM_ORM_Code.ORM_JUPITER_SOLAR_MAG_DIPOLE)) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_SolarMagneticDipole: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forSolarMagneticEcliptic(SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        if ((orm != SRM_ORM_Code.ORM_JUPITER_SOLAR_MAG_ECLIPTIC) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_SolarMagneticEcliptic: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forHeliosphericAriesEcliptic(SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        if (((orm != SRM_ORM_Code.ORM_HELIO_ARIES_ECLIPTIC_J2000r0) &&
                (orm != SRM_ORM_Code.ORM_HELIO_ARIES_ECLIPTIC_TRUE_OF_DATE)) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_HeliosphericAriesEcliptic: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forHeliosphericEarthEcliptic(SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        if ((orm != SRM_ORM_Code.ORM_HELIO_EARTH_ECLIPTIC) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_HeliosphericEarthEcliptic: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forHeliosphericEarthEquatorial(SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        if ((orm != SRM_ORM_Code.ORM_HELIO_EARTH_EQUATORIAL) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_HeliosphericEarthEquatorial: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forLocalSpaceAzimuthal(SRM_ORM_Code orm,
        SRM_HSR_Code hsr) throws SrmException {
        if ((orm != SRM_ORM_Code.ORM_ABSTRACT_2D) || !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_LocalSpaceAzimuthal: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forLocalSpacePolar(SRM_ORM_Code orm, SRM_HSR_Code hsr)
        throws SrmException {
        if ((orm != SRM_ORM_Code.ORM_ABSTRACT_2D) || !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_LocalSpacePolar: Invalid ORM/HSR pair");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forObliqueMercatorSpherical(SRM_ORM_Code orm,
        SRM_HSR_Code hsr, SRF_OM_Params params) throws SrmException {
        if ((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_ObliqueMercatorSpherical: Invalid ORM/HSR pair");
        }

        if (!Const.isWellFormedLongitude(params.longitude1) ||
                !Const.isWellFormedLatitude(params.latitude1) ||
                !Const.isWellFormedLongitude(params.longitude2) ||
                !Const.isWellFormedLatitude(params.latitude2)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("SRF_ObliqueMercator: Invalid SRF parameters"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forPolarStereographic(SRM_ORM_Code orm,
        SRM_HSR_Code hsr, SRF_PS_Params params) throws SrmException {
        if (((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) &&
                (OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID)) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_PolarStereographic: Invalid ORM/HSR pair");
        }

        if (!Const.isWellFormedLongitude(params.origin_longitude) ||
                !Const.isWellFormedLatitude(params.true_scale_latitude)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("SRF_PolarStereographic: Invalid SRF parameters"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     * @param params DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static void forEquidistantCylindrical(SRM_ORM_Code orm,
        SRM_HSR_Code hsr, SRF_EC_Params params) throws SrmException {
        if ((OrmDataSet.getElem(orm)._orm_template != SRM_ORMT_Code.ORMT_SPHERE) ||
                (orm == SRM_ORM_Code.ORM_ABSTRACT_3D) ||
                !isValidOrmHsr(orm, hsr)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                "SRF_EquidistantCylindrical: Invalid ORM/HSR pair");
        }

        if (!Const.isWellFormedLongitude(params.origin_longitude) ||
                !Const.isWellFormedLatitude(params.standard_latitude)) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                new String("SRF_EquidistantCylindrical: Invalid SRF parameters"));
        }
    }
}
