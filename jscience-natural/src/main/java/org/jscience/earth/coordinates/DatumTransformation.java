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

import org.jscience.mathematics.numbers.real.Real;

/**
 * Provides utilities for datum transformations (e.g., Molodensky or 7-parameter Helios/Bursa-Wolf).
 * Essential for converting archival/historical spatial data to modern reference systems.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class DatumTransformation {

    private DatumTransformation() {
        // Utility class
    }

    /**
     * Parameters for a Helmert 7-parameter transformation.
     */
    public record HelmertParameters(
            double dx, double dy, double dz, // translation (meters)
            double rx, double ry, double rz, // rotation (arc-seconds)
            double s // scale (ppm)
    ) {}

    /**
     * Standard transformation from WGS 84 to ED 50 (European Datum 1950)
     * Approximate parameters for western Europe.
     */
    public static final HelmertParameters WGS84_TO_ED50 = 
        new HelmertParameters(-87, -98, -121, 0, 0, 0, 0);

    /**
     * Standard transformation from WGS 84 to NAD 27 (North American Datum 1927)
     * Approximate parameters for CONUS.
     */
    public static final HelmertParameters WGS84_TO_NAD27 = 
        new HelmertParameters(-8, 160, 176, 0, 0, 0, 0);

    /**
     * Performs a 7-parameter Helmert transformation on ECEF coordinates.
     */
    public static ECEFCoordinate transform(ECEFCoordinate ecef, HelmertParameters p) {
        double X = ecef.getX().doubleValue();
        double Y = ecef.getY().doubleValue();
        double Z = ecef.getZ().doubleValue();

        // Convert rotations from arc-seconds to radians
        double rx = Math.toRadians(p.rx / 3600.0);
        double ry = Math.toRadians(p.ry / 3600.0);
        double rz = Math.toRadians(p.rz / 3600.0);
        double s = 1.0 + p.s * 1e-6;

        double Xt = p.dx + s * (X + rz * Y - ry * Z);
        double Yt = p.dy + s * (-rz * X + Y + rx * Z);
        double Zt = p.dz + s * (ry * X - rx * Y + Z);

        return new ECEFCoordinate(Real.of(Xt), Real.of(Yt), Real.of(Zt));
    }
}
