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
 */
public class HorizonsEphemerisLoader {

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

                            // Time Parsing (Simplified for demo)
                            // Ideally parse JDTDB or Date String
                            // For now just storing dummy time if parsing fails or complex
                            Instant tp = Instant.now(); // TODO: Parse from JDTDB

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
