/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.loaders;

import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;

import java.time.Instant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads ephemerides from JPL Horizons system (Text format).
 * <p>
 * Expected format: CSV-like or fixed width inside $$SOE and $$EOE markers.
 * Example Record:
 * 2460676.500000000, A.D. 2025-Jan-01 00:00:00.0000, 1.4709829123E+08,
 * 1.4709829123E+08, ...
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HorizonsEphemerisLoader
        implements org.jscience.io.InputLoader<List<HorizonsEphemerisLoader.EphemerisPoint>> {

    @Override
    public List<EphemerisPoint> load(String resourceId) throws Exception {
        if (resourceId.startsWith("http")) {
            try (InputStream is = new java.net.URI(resourceId).toURL().openStream()) {
                return loadEphemeris(is);
            }
        }
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is == null)
                throw new java.io.IOException("Horizons resource not found: " + resourceId);
            return loadEphemeris(is);
        }
    }

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<EphemerisPoint>> getResourceType() {
        return (Class) List.class;
    }

    public static class EphemerisPoint {
        public final Instant time;
        public final DenseVector<Real> position; // (x, y, z) in km or AU
        public final DenseVector<Real> velocity; // (vx, vy, vz) in km/s or AU/d

        public EphemerisPoint(Instant time, DenseVector<Real> pos, DenseVector<Real> vel) {
            this.time = time;
            this.position = pos;
            this.velocity = vel;
        }
    }

    /**
     * Parses a JPL Horizons text stream.
     * Looks for $$SOE (Start of Ephemeris) and $$EOE (End of Ephemeris).
     */
    public static List<EphemerisPoint> loadEphemeris(InputStream is) {
        List<EphemerisPoint> points = new ArrayList<>();
        boolean inData = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("$$SOE")) {
                    inData = true;
                    continue;
                }
                if (line.trim().equals("$$EOE")) {
                    inData = false;
                    break;
                }

                if (inData) {
                    try {
                        // Horizons format varies widely based on request settings.
                        // Assuming vectors: X, Y, Z, VX, VY, VZ
                        // CSV format usually.
                        String[] parts = line.split(",");
                        if (parts.length >= 8) {
                            // Part 0: JDTDB
                            // Part 1: Date String "A.D. 2025-Jan-01 00:00:00.0000"

                            // Parse positions (X,Y,Z)
                            double x = Double.parseDouble(parts[2].trim());
                            double y = Double.parseDouble(parts[3].trim());
                            double z = Double.parseDouble(parts[4].trim());

                            // Parse velocities (VX,VY,VZ)
                            double vx = Double.parseDouble(parts[5].trim());
                            double vy = Double.parseDouble(parts[6].trim());
                            double vz = Double.parseDouble(parts[7].trim());

                            // Create vectors
                            DenseVector<Real> pos = DenseVector.of(
                                    java.util.Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance());
                            DenseVector<Real> vel = DenseVector.of(
                                    java.util.Arrays.asList(Real.of(vx), Real.of(vy), Real.of(vz)),
                                    Reals.getInstance());

                            // Parse time from JDTDB (Julian Day Terrestrial Barycentric Dynamical Time)
                            double jd = Double.parseDouble(parts[0].trim());
                            // Convert JD to Unix epoch: JD 2440587.5 = Unix epoch (1970-01-01 00:00:00 UTC)
                            double daysSinceUnixEpoch = jd - 2440587.5;
                            long millisSinceEpoch = (long) (daysSinceUnixEpoch * 86400000.0);
                            Instant tp = Instant.ofEpochMilli(millisSinceEpoch);

                            points.add(new EphemerisPoint(tp, pos, vel));
                        }
                    } catch (Exception e) {
                        // Skip malformed lines
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }
}
