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

package org.jscience.physics.loaders;

import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;

import java.time.Instant;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Loads ephemerides from JPL Horizons system (Text format).
 * <p>
 * Expected format: CSV-like or fixed width inside $$SOE and $$EOE markers.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HorizonsEphemerisReader extends AbstractResourceReader<List<HorizonsEphemerisReader.EphemerisPoint>> {


    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getDescription() {
        return "JPL Horizons Ephemeris Reader.";
    }

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<HorizonsEphemerisReader.EphemerisPoint>> getResourceType() {
        return (Class<List<HorizonsEphemerisReader.EphemerisPoint>>) (Class<?>) List.class;
    }

    @Override
    protected List<EphemerisPoint> loadFromSource(String id) throws Exception {
        if (id.startsWith("http")) {
            try (InputStream is = new java.net.URI(id).toURL().openStream()) {
                return loadEphemeris(is);
            }
        }
        try (InputStream is = getClass().getResourceAsStream(id)) {
            if (is == null)
                throw new java.io.IOException("Horizons resource not found: " + id);
            return loadEphemeris(is);
        }
    }

    @Override
    protected MiniCatalog<List<EphemerisPoint>> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<List<EphemerisPoint>> getAll() {
                return List.of(List.of());
            }

            @Override
            public Optional<List<EphemerisPoint>> findByName(String name) {
                return Optional.of(List.of());
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    public static class EphemerisPoint {
        public final Instant time;
        public final DenseVector<Real> position;
        public final DenseVector<Real> velocity;

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
                        String[] parts = line.split(",");
                        if (parts.length >= 8) {
                            double x = Double.parseDouble(parts[2].trim());
                            double y = Double.parseDouble(parts[3].trim());
                            double z = Double.parseDouble(parts[4].trim());

                            double vx = Double.parseDouble(parts[5].trim());
                            double vy = Double.parseDouble(parts[6].trim());
                            double vz = Double.parseDouble(parts[7].trim());

                            DenseVector<Real> pos = DenseVector.of(
                                    java.util.Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance());
                            DenseVector<Real> vel = DenseVector.of(
                                    java.util.Arrays.asList(Real.of(vx), Real.of(vy), Real.of(vz)),
                                    Reals.getInstance());

                            double jd = Double.parseDouble(parts[0].trim());
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

    @Override public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("reader.horizonsephemeris.name"); }
}

