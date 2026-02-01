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
 * Universal Polar Stereographic (UPS) coordinate.
 * Used for North (>84Â°N) and South (<80Â°S) polar regions where UTM is not suitable.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class UPSCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final char hemisphere; // 'N' or 'S'
    private final Real easting;
    private final Real northing;
    private final ReferenceEllipsoid ellipsoid;

    public UPSCoordinate(char hemisphere, Real easting, Real northing, ReferenceEllipsoid ellipsoid) {
        this.hemisphere = hemisphere;
        this.easting = easting;
        this.northing = northing;
        this.ellipsoid = ellipsoid;
    }

    public char getHemisphere() { return hemisphere; }
    public Quantity<Length> getEasting() { return Quantities.create(easting.doubleValue(), Units.METER); }
    public Quantity<Length> getNorthing() { return Quantities.create(northing.doubleValue(), Units.METER); }
    
    @Override
    public ReferenceEllipsoid getEllipsoid() { return ellipsoid; }

    @Override
    public String getCoordinateSystem() { return "UPS-" + hemisphere; }

    @Override
    public GeodeticCoordinate toGeodetic() {
        // Inverse UPS formula (simplified)
        double x = easting.doubleValue() - 2000000.0;
        double y = northing.doubleValue() - 2000000.0;
        double lon = Math.atan2(x, (hemisphere == 'N') ? -y : y);
        // More complex latitude calculation would be needed for full precision
        return new GeodeticCoordinate(
            Quantities.create((hemisphere == 'N') ? 90.0 : -90.0, Units.DEGREE_ANGLE),
            Quantities.create(Math.toDegrees(lon), Units.DEGREE_ANGLE),
            Quantities.create(0, Units.METER), ellipsoid);
    }

    @Override
    public ECEFCoordinate toECEF() { return toGeodetic().toECEF(); }

    /**
     * Converts Geodetic to UPS.
     */
    public static UPSCoordinate fromGeodetic(GeodeticCoordinate geodetic) {
        double lat = geodetic.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double lon = geodetic.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        double a = geodetic.getEllipsoid().getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double e2 = geodetic.getEllipsoid().getEccentricitySquared().doubleValue();
        double e = Math.sqrt(e2);
        
        char hemisphere = (lat >= 0) ? 'N' : 'S';
        double k0 = 0.994; // UPS scale factor
        
        double absLat = Math.abs(lat);
        double t = Math.tan(Math.PI/4 - absLat/2) / Math.pow((1 - e*Math.sin(absLat)) / (1 + e*Math.sin(absLat)), e/2);
        
        double cm = (hemisphere == 'N') ? 1.0 : -1.0;
        double rho = 2 * a * k0 * t / Math.sqrt(Math.pow(1+e, 1+e) * Math.pow(1-e, 1-e));
        
        // Simplified UPS formula for demonstration
        double x = 2000000.0 + rho * Math.sin(lon);
        double y = 2000000.0 + cm * rho * Math.cos(lon);

        return new UPSCoordinate(hemisphere, Real.of(x), Real.of(y), geodetic.getEllipsoid());
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
        return "UPS Coordinate: " + toString();
    }

    @Override
    public String toString() {
        return String.format("UPS-%c[E=%.2f, N=%.2f]", hemisphere, easting.doubleValue(), northing.doubleValue());
    }
}

