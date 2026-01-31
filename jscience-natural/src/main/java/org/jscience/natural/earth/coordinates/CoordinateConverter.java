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

package org.jscience.natural.earth.coordinates;

/**
 * High-level utility for seamless coordinate conversions.
 * Supports Geodetic, ECEF, UTM, MGRS, and Topocentric systems.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class CoordinateConverter {

    private CoordinateConverter() {
        // Utility class
    }

    /**
     * Converts a geodetic point to Albers Equal-Area Conic.
     */
    public static AlbersEqualAreaCoordinate toAlbers(GeodeticCoordinate geodetic, AlbersEqualAreaCoordinate.AlbersParameters params) {
        return AlbersEqualAreaCoordinate.fromGeodetic(geodetic, params);
    }

    /**
     * Parameters for the Lambert Azimuthal Equal-Area projection centered on Europe.
     */
    public static final LambertAzimuthalEqualAreaCoordinate.LAEAParameters EUROPE_LAEA = 
        new LambertAzimuthalEqualAreaCoordinate.LAEAParameters(52.0, 10.0, 4321000.0, 3210000.0);

    /**
     * Parameters for the Bonne projection as used for the French "Carte de l'Ã‰tat-Major".
     */
    public static final BonneCoordinate.BonneParameters FRANCE_STATE_MAJOR_BONNE = 
        new BonneCoordinate.BonneParameters(45.0, 0.0, 0.0, 0.0);

    /**
     * Parameters for the Cassini-Soldner projection used by the UK Ordnance Survey
     * (e.g., for the Great Britain National Grid).
     */
    public static final CassiniSoldnerCoordinate.CassiniParameters UK_ORDNANCE_SURVEY_CASSINI =
        new CassiniSoldnerCoordinate.CassiniParameters(49.0, -2.0, 400000.0, -100000.0);

    /**
     * Converts a geodetic point to Cassini-Soldner.
     */
    public static CassiniSoldnerCoordinate toCassini(GeodeticCoordinate geodetic, CassiniSoldnerCoordinate.CassiniParameters params) {
        return CassiniSoldnerCoordinate.fromGeodetic(geodetic, params);
    }

    /**
     * Converts a geodetic point to Polyconic.
     */
    public static PolyconicCoordinate toPolyconic(GeodeticCoordinate geodetic, PolyconicCoordinate.PolyconicParameters params) {
        return PolyconicCoordinate.fromGeodetic(geodetic, params);
    }

    /**
     * Converts a geodetic point to Lambert Azimuthal Equal-Area.
     */
    public static LambertAzimuthalEqualAreaCoordinate toLAEA(GeodeticCoordinate geodetic, LambertAzimuthalEqualAreaCoordinate.LAEAParameters params) {
        return LambertAzimuthalEqualAreaCoordinate.fromGeodetic(geodetic, params);
    }

    /**
     * Converts a geodetic point to Bonne.
     */
    public static BonneCoordinate toBonne(GeodeticCoordinate geodetic, BonneCoordinate.BonneParameters params) {
        return BonneCoordinate.fromGeodetic(geodetic, params);
    }

    /**
     * Converts a geodetic point to Lambert Conformal Conic.
     */
    public static LambertConformalConicCoordinate toLambert(GeodeticCoordinate geodetic, LambertConformalConicCoordinate.LCCParams params) {
        return LambertConformalConicCoordinate.fromGeodetic(geodetic, params);
    }

    /**
     * Converts a geodetic point to Mercator.
     */
    public static MercatorCoordinate toMercator(GeodeticCoordinate geodetic, boolean webMercator) {
        return MercatorCoordinate.fromGeodetic(geodetic, webMercator);
    }

    /**
     * Converts a geodetic point to UPS (Universal Polar Stereographic).
     */
    public static UPSCoordinate toUPS(GeodeticCoordinate geodetic) {
        return UPSCoordinate.fromGeodetic(geodetic);
    }

    /**
     * Converts a geodetic point to UTM.
     */
    public static UTMCoordinate toUTM(GeodeticCoordinate geodetic) {
        return UTMCoordinate.fromGeodetic(geodetic);
    }

    /**
     * Converts a geodetic point to MGRS.
     */
    public static MGRSCoordinate toMGRS(GeodeticCoordinate geodetic) {
        return MGRSCoordinate.fromUTM(UTMCoordinate.fromGeodetic(geodetic));
    }

    /**
     * Converts a geodetic point to ECEF XYZ.
     */
    public static ECEFCoordinate toECEF(GeodeticCoordinate geodetic) {
        return geodetic.toECEF();
    }

    /**
     * Converts a target geodetic point to local ENU relative to an observer.
     */
    public static ENUCoordinate toLocalENU(GeodeticCoordinate target, GeodeticCoordinate observer) {
        return ENUCoordinate.fromECEF(target.toECEF(), observer);
    }

    /**
     * Converts a target geodetic point to local AER (Azimuth, Elevation, Range) relative to an observer.
     */
    public static AERCoordinate toLocalAER(GeodeticCoordinate target, GeodeticCoordinate observer) {
        return AERCoordinate.fromENU(toLocalENU(target, observer));
    }

    /**
     * Converts ECEF to Geodetic.
     */
    public static GeodeticCoordinate fromECEF(ECEFCoordinate ecef) {
        return ecef.toGeodetic();
    }

    /**
     * Converts UTM back to Geodetic.
     */
    public static GeodeticCoordinate fromUTM(UTMCoordinate utm) {
        return utm.toGeodetic();
    }
}

