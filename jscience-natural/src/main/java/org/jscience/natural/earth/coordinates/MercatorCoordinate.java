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

import java.io.Serializable;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Mercator projection coordinate.
 * Used for navigation and web maps (pseudo-Mercator).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class MercatorCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final Real x;
    private final Real y;
    private final boolean webMercator; // EPSG:3857 vs EPSG:3395

    public MercatorCoordinate(Real x, Real y, boolean webMercator) {
        this.x = x;
        this.y = y;
        this.webMercator = webMercator;
    }

    public Quantity<Length> getX() { return Quantities.create(x.doubleValue(), Units.METER); }
    public Quantity<Length> getY() { return Quantities.create(y.doubleValue(), Units.METER); }
    public boolean isWebMercator() { return webMercator; }

    @Override
    public String getCoordinateSystem() { return webMercator ? "EPSG:3857" : "EPSG:3395"; }

    @Override
    public ReferenceEllipsoid getEllipsoid() { return ReferenceEllipsoid.WGS84; }

    @Override
    public GeodeticCoordinate toGeodetic() {
        double a = ReferenceEllipsoid.WGS84.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double lon = x.doubleValue() / a;
        double lat;
        if (webMercator) {
            lat = 2 * Math.atan(Math.exp(y.doubleValue() / a)) - Math.PI / 2;
        } else {
            // Simplified inverse for ellipsoidal (iterative would be more accurate)
            lat = 2 * Math.atan(Math.exp(y.doubleValue() / a)) - Math.PI / 2;
        }
        return new GeodeticCoordinate(Math.toDegrees(lat), Math.toDegrees(lon), 0.0);
    }

    @Override
    public ECEFCoordinate toECEF() { return toGeodetic().toECEF(); }

    /**
     * Converts Geodetic to Mercator.
     */
    public static MercatorCoordinate fromGeodetic(GeodeticCoordinate geodetic, boolean web) {
        double lat = geodetic.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double lon = geodetic.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        double a = geodetic.getEllipsoid().getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        
        double x = a * lon;
        double y;
        
        if (web) {
            // Spherical Mercator (EPSG:3857)
            y = a * Math.log(Math.tan(Math.PI/4 + lat/2));
        } else {
            // Ellipsoidal Mercator (EPSG:3395)
            double e = Math.sqrt(geodetic.getEllipsoid().getEccentricitySquared().doubleValue());
            y = a * Math.log(Math.tan(Math.PI/4 + lat/2) * Math.pow((1 - e*Math.sin(lat)) / (1 + e*Math.sin(lat)), e/2));
        }

        return new MercatorCoordinate(Real.of(x), Real.of(y), web);
    }

    @Override
    public int dimension() {
        return 0;
    }

    @Override
    public int ambientDimension() {
        return 3;
    }

    @Override
    public String description() {
        return "Mercator Coordinate: " + toString();
    }

    @Override
    public String toString() {
        return String.format("%sMercator[X=%.2f, Y=%.2f]", webMercator ? "Web" : "", x.doubleValue(), y.doubleValue());
    }
}

