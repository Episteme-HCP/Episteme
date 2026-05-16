/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.earth.coordinates;

/**
 * Registry of historically significant geodetic parameters and presets.
 * Designed for historians and researchers working with archival maps and data.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class HistoricalGeodeticModels {

    private HistoricalGeodeticModels() {
        // Utility registry
    }

    /**
     * Parameters for the Cassini-Soldner projection used by the UK Ordnance Survey
     * during the 19th and early 20th century.
     */
    public static final CassiniSoldnerCoordinate.CassiniParameters UK_OS_CASSINI = 
        new CassiniSoldnerCoordinate.CassiniParameters(52.2, -2.5, 0.0, 0.0);

    /**
     * Parameters for the Albers Equal-Area Conic projection as specified by
     * the USGS for the contiguous United States.
     */
    public static final AlbersEqualAreaCoordinate.AlbersParameters USGS_USA_ALBERS = 
        new AlbersEqualAreaCoordinate.AlbersParameters(29.5, 45.5, 23.0, -96.0, 0.0, 0.0);

    /**
     * Parameters for the Polyconic projection widely used for early USGS topographic maps.
     */
    public static final PolyconicCoordinate.PolyconicParameters USGS_USA_POLYCONIC = 
        new PolyconicCoordinate.PolyconicParameters(30.0, -96.0, 0.0, 0.0);

    /**
     * Bessel 1841 Ellipsoid - widely used in Europe (e.g. Germany, Switzerland, Netherlands).
     */
    public static ReferenceEllipsoid getBessel1841() {
        return ReferenceEllipsoid.get("Bessel 1841");
    }

    /**
     * Clarke 1866 Ellipsoid - base for NAD27 in North America.
     */
    public static ReferenceEllipsoid getClarke1866() {
        return ReferenceEllipsoid.get("Clarke 1866");
    }

     /**
     * Everest 1830 Ellipsoid - used in India and Southeast Asia.
     */
    public static ReferenceEllipsoid getEverest1830() {
        return ReferenceEllipsoid.get("Everest 1830 (1937 Adjustment)");
    }

    /**
     * Airy 1830 Ellipsoid - used for OSGB36 in the UK.
     */
    public static ReferenceEllipsoid getAiry1830() {
        return ReferenceEllipsoid.get("Airy 1830");
    }
}

