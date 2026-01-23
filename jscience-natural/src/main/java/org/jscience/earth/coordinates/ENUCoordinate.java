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
 * East-North-Up (ENU) topocentric coordinate system.
 * Represents coordinates relative to a local tangent plane at a reference point.
 * Useful for local navigation, radar, and site-specific scientific measurements.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class ENUCoordinate implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Real east;
    private final Real north;
    private final Real up;
    private final GeodeticCoordinate referencePoint;

    public ENUCoordinate(Real east, Real north, Real up, GeodeticCoordinate referencePoint) {
        this.east = east;
        this.north = north;
        this.up = up;
        this.referencePoint = referencePoint;
    }

    public Quantity<Length> getEast() { return Quantities.create(east.doubleValue(), Units.METER); }
    public Quantity<Length> getNorth() { return Quantities.create(north.doubleValue(), Units.METER); }
    public Quantity<Length> getUp() { return Quantities.create(up.doubleValue(), Units.METER); }
    public GeodeticCoordinate getReferencePoint() { return referencePoint; }

    /**
     * Converts ECEF to ENU relative to a reference point.
     */
    public static ENUCoordinate fromECEF(ECEFCoordinate point, GeodeticCoordinate reference) {
        ECEFCoordinate refECEF = reference.toECEF();
        
        double dx = point.getXReal().doubleValue() - refECEF.getXReal().doubleValue();
        double dy = point.getYReal().doubleValue() - refECEF.getYReal().doubleValue();
        double dz = point.getZReal().doubleValue() - refECEF.getZReal().doubleValue();

        double lat = reference.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double lon = reference.getLongitude().to(Units.RADIAN).getValue().doubleValue();

        double sinLat = Math.sin(lat);
        double cosLat = Math.cos(lat);
        double sinLon = Math.sin(lon);
        double cosLon = Math.cos(lon);

        double e = -sinLon * dx + cosLon * dy;
        double n = -sinLat * cosLon * dx - sinLat * sinLon * dy + cosLat * dz;
        double u = cosLat * cosLon * dx + cosLat * sinLon * dy + sinLat * dz;

        return new ENUCoordinate(Real.of(e), Real.of(n), Real.of(u), reference);
    }

    /**
     * Converts this ENU coordinate back to ECEF.
     */
    public ECEFCoordinate toECEF() {
        ECEFCoordinate refECEF = referencePoint.toECEF();
        
        double lat = referencePoint.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double lon = referencePoint.getLongitude().to(Units.RADIAN).getValue().doubleValue();

        double sinLat = Math.sin(lat);
        double cosLat = Math.cos(lat);
        double sinLon = Math.sin(lon);
        double cosLon = Math.cos(lon);

        double e = east.doubleValue();
        double n = north.doubleValue();
        double u = up.doubleValue();

        double dx = -sinLon * e - sinLat * cosLon * n + cosLat * cosLon * u;
        double dy = cosLon * e - sinLat * sinLon * n + cosLat * sinLon * u;
        double dz = cosLat * n + sinLat * u;

        return new ECEFCoordinate(
            Real.of(refECEF.getXReal().doubleValue() + dx),
            Real.of(refECEF.getYReal().doubleValue() + dy),
            Real.of(refECEF.getZReal().doubleValue() + dz),
            referencePoint.getEllipsoid()
        );
    }

    @Override
    public String toString() {
        return String.format("ENU[E=%.2fm, N=%.2fm, U=%.2fm]", east.doubleValue(), north.doubleValue(), up.doubleValue());
    }
}
