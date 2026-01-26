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

package org.jscience.earth.coordinates;

import java.io.Serializable;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Universal Transverse Mercator (UTM) coordinate.
 * UTM divides the Earth into 60 zones, each 6° wide in longitude.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class UTMCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final int zoneNumber;
    private final char zoneLetter;
    private final Real easting; // Meters
    private final Real northing; // Meters
    private final ReferenceEllipsoid ellipsoid;

    public UTMCoordinate(int zoneNumber, char zoneLetter, double easting, double northing) {
        this(zoneNumber, zoneLetter, Real.of(easting), Real.of(northing), ReferenceEllipsoid.WGS84);
    }

    public UTMCoordinate(int zoneNumber, char zoneLetter, Real easting, Real northing, ReferenceEllipsoid ellipsoid) {
        this.zoneNumber = zoneNumber;
        this.zoneLetter = zoneLetter;
        this.easting = easting;
        this.northing = northing;
        this.ellipsoid = ellipsoid;
    }

    public int getZoneNumber() { return zoneNumber; }
    public char getZoneLetter() { return zoneLetter; }
    public Quantity<Length> getEasting() { return Quantities.create(easting.doubleValue(), Units.METER); }
    public Quantity<Length> getNorthing() { return Quantities.create(northing.doubleValue(), Units.METER); }
    
    @Override
    public ReferenceEllipsoid getEllipsoid() { return ellipsoid; }

    @Override
    public String getCoordinateSystem() { return "UTM Zone " + zoneNumber + zoneLetter; }

    @Override
    public ECEFCoordinate toECEF() { return toGeodetic().toECEF(); }

    /**
     * Converts this UTM coordinate to Geodetic (Lat/Lon).
     * Implementation based on USGS Bulletin 1532.
     */
    @Override
    public GeodeticCoordinate toGeodetic() {
        double x = easting.doubleValue() - 500000.0; // remove 500km false easting
        double y = northing.doubleValue();
        if (zoneLetter < 'N') {
            y -= 10000000.0; // remove 10,000km false northing for southern hemisphere
        }

        double a = ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double k0 = 0.9996; // UTM scale factor
        
        double e2 = ellipsoid.getEccentricitySquared().doubleValue();
        double e4 = e2 * e2;
        double e6 = e4 * e2;
        double e1 = (1.0 - Math.sqrt(1.0 - e2)) / (1.0 + Math.sqrt(1.0 - e2));

        double m = y / k0;
        double mu = m / (a * (1.0 - e2/4.0 - 3.0*e4/64.0 - 5.0*e6/256.0));

        double phi1 = mu + (3.0*e1/2.0 - 27.0*Math.pow(e1, 3)/32.0) * Math.sin(2.0*mu)
                      + (21.0*e1*e1/16.0 - 55.0*Math.pow(e1, 4)/32.0) * Math.sin(4.0*mu)
                      + (151.0*Math.pow(e1, 3)/96.0) * Math.sin(6.0*mu)
                      + (1097.0*Math.pow(e1, 4)/512.0) * Math.sin(8.0*mu);

        double C1 = (e2 / (1.0 - e2)) * Math.pow(Math.cos(phi1), 2);
        double T1 = Math.pow(Math.tan(phi1), 2);
        double N1 = a / Math.sqrt(1.0 - e2 * Math.pow(Math.sin(phi1), 2));
        double R1 = a * (1.0 - e2) / Math.pow(1.0 - e2 * Math.pow(Math.sin(phi1), 2), 1.5);
        double D = x / (N1 * k0);

        double lat = phi1 - (N1 * Math.tan(phi1) / R1) * (D*D/2.0 - (5.0 + 3.0*T1 + 10.0*C1 - 4.0*C1*C1 - 9.0*(e2/(1.0-e2))) * Math.pow(D, 4) / 24.0
                     + (61.0 + 90.0*T1 + 298.0*C1 + 45.0*T1*T1 - 252.0*(e2/(1.0-e2)) - 3.0*C1*C1) * Math.pow(D, 6) / 720.0);

        double lon0 = Math.toRadians((zoneNumber - 1) * 6 - 180 + 3);
        double lon = lon0 + (D - (1.0 + 2.0*T1 + C1) * Math.pow(D, 3) / 6.0
                     + (5.0 - 2.0*C1 + 28.0*T1 - 3.0*C1*C1 + 8.0*(e2/(1.0-e2)) + 24.0*T1*T1) * Math.pow(D, 5) / 120.0) / Math.cos(phi1);

        return new GeodeticCoordinate(
            Quantities.create(Math.toDegrees(lat), Units.DEGREE_ANGLE),
            Quantities.create(Math.toDegrees(lon), Units.DEGREE_ANGLE),
            Quantities.create(0, Units.METER), // UTM usually surface only
            ellipsoid
        );
    }

    /**
     * Static factory to convert Geodetic to UTM.
     */
    public static UTMCoordinate fromGeodetic(GeodeticCoordinate geodetic) {
        double lat = geodetic.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double lon = geodetic.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        ReferenceEllipsoid ellipsoid = geodetic.getEllipsoid();

        int zoneNumber = (int) ((geodetic.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue() + 180) / 6) + 1;
        char zoneLetter = getUtmLetter(geodetic.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue());

        double lon0 = Math.toRadians((zoneNumber - 1) * 6 - 180 + 3);
        double a = ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double e2 = ellipsoid.getEccentricitySquared().doubleValue();
        double k0 = 0.9996;

        double e4 = e2 * e2;
        double e6 = e4 * e2;
        double ep2 = e2 / (1 - e2);

        double N = a / Math.sqrt(1 - e2 * Math.sin(lat) * Math.sin(lat));
        double T = Math.tan(lat) * Math.tan(lat);
        double C = ep2 * Math.cos(lat) * Math.cos(lat);
        double A = (lon - lon0) * Math.cos(lat);

        double M = a * ((1 - e2/4 - 3*e4/64 - 5*e6/256) * lat 
                   - (3*e2/8 + 3*e4/32 + 45*e6/1024) * Math.sin(2*lat)
                   + (15*e4/256 + 45*e6/1024) * Math.sin(4*lat)
                   - (35*e6/3072) * Math.sin(6*lat));

        double easting = k0 * N * (A + (1 - T + C) * Math.pow(A, 3) / 6 + (5 - 18*T + T*T + 72*C - 58*ep2) * Math.pow(A, 5) / 120) + 500000.0;
        double northing = k0 * (M + N * Math.tan(lat) * (A*A/2 + (5 - T + 9*C + 4*C*C) * Math.pow(A, 4) / 24 + (61 - 58*T + T*T + 600*C - 330*ep2) * Math.pow(A, 6) / 720));

        if (lat < 0) {
            northing += 10000000.0;
        }

        return new UTMCoordinate(zoneNumber, zoneLetter, Real.of(easting), Real.of(northing), ellipsoid);
    }

    private static char getUtmLetter(double latitude) {
        if (latitude >= 84 || latitude < -80) return 'Z'; // Outside UTM bounds
        char[] letters = "CDEFGHJKLMNPQRSTUVWXX".toCharArray();
        int index = (int) ((latitude + 80) / 8);
        return letters[Math.min(index, letters.length - 1)];
    }

    @Override
    public String toString() {
        return String.format("%d%c %.2f E %.2f N", zoneNumber, zoneLetter, easting.doubleValue(), northing.doubleValue());
    }
}
