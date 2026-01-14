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

package org.jscience.physics.astronomy.mechanics;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.quantity.Velocity;

/**
 * Orbital mechanics calculations.
 * <p>
 * Provides Kepler's laws, vis-viva equation, and orbital maneuvers.
 * All methods use type-safe Quantity measurements.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OrbitalMechanics {

    /** Gravitational constant (mÃ‚Â³/(kgÃ‚Â·sÃ‚Â²)) */
    public static final double G = 6.67430e-11;

    /** Standard gravitational parameter for Earth (mÃ‚Â³/sÃ‚Â²) */
    public static final double MU_EARTH = 3.986004418e14;

    /** Standard gravitational parameter for Sun (mÃ‚Â³/sÃ‚Â²) */
    public static final double MU_SUN = 1.32712440018e20;

    private OrbitalMechanics() {
    }

    /**
     * Kepler's Third Law: Orbital period from semi-major axis.
     * T = 2Ãâ‚¬Ã¢Ë†Å¡(aÃ‚Â³/ÃŽÂ¼)
     * 
     * @param semiMajorAxis orbital semi-major axis in meters
     * @param mu            gravitational parameter (mÃ‚Â³/sÃ‚Â²)
     * @return orbital period
     */
    public static Quantity<Time> orbitalPeriod(double semiMajorAxis, double mu) {
        double T = 2 * Math.PI * Math.sqrt(Math.pow(semiMajorAxis, 3) / mu);
        return Quantities.create(T, Units.SECOND);
    }

    /**
     * Orbital period for Earth-orbiting satellite.
     * 
     * @param semiMajorAxis in meters
     * @return period
     */
    public static Quantity<Time> orbitalPeriodEarth(double semiMajorAxis) {
        return orbitalPeriod(semiMajorAxis, MU_EARTH);
    }

    /**
     * Vis-viva equation: Orbital velocity at distance r.
     * v = Ã¢Ë†Å¡[ÃŽÂ¼(2/r - 1/a)]
     * 
     * @param r             current distance from central body (m)
     * @param semiMajorAxis semi-major axis (m)
     * @param mu            gravitational parameter
     * @return orbital velocity
     */
    public static Quantity<Velocity> orbitalVelocity(double r, double semiMajorAxis, double mu) {
        double v = Math.sqrt(mu * (2 / r - 1 / semiMajorAxis));
        return Quantities.create(v, Units.METER_PER_SECOND);
    }

    /**
     * Circular orbit velocity.
     * v = Ã¢Ë†Å¡(ÃŽÂ¼/r)
     */
    public static Quantity<Velocity> circularVelocity(double radius, double mu) {
        double v = Math.sqrt(mu / radius);
        return Quantities.create(v, Units.METER_PER_SECOND);
    }

    /**
     * Escape velocity from distance r.
     * v_esc = Ã¢Ë†Å¡(2ÃŽÂ¼/r)
     */
    public static Quantity<Velocity> escapeVelocity(double radius, double mu) {
        double v = Math.sqrt(2 * mu / radius);
        return Quantities.create(v, Units.METER_PER_SECOND);
    }

    /**
     * Hohmann transfer delta-v (from circular to circular).
     * 
     * @param r1 initial orbit radius (m)
     * @param r2 final orbit radius (m)
     * @param mu gravitational parameter
     * @return total delta-v required (m/s)
     */
    public static double hohmannDeltaV(double r1, double r2, double mu) {
        double a_transfer = (r1 + r2) / 2;

        // Velocity at r1 on circular orbit
        double v1_circ = Math.sqrt(mu / r1);
        // Velocity at r1 on transfer ellipse
        double v1_transfer = Math.sqrt(mu * (2 / r1 - 1 / a_transfer));
        double dv1 = Math.abs(v1_transfer - v1_circ);

        // Velocity at r2 on transfer ellipse
        double v2_transfer = Math.sqrt(mu * (2 / r2 - 1 / a_transfer));
        // Velocity at r2 on circular orbit
        double v2_circ = Math.sqrt(mu / r2);
        double dv2 = Math.abs(v2_circ - v2_transfer);

        return dv1 + dv2;
    }

    /**
     * Hohmann transfer time (half the period of transfer orbit).
     */
    public static Quantity<Time> hohmannTransferTime(double r1, double r2, double mu) {
        double a_transfer = (r1 + r2) / 2;
        double T_transfer = 2 * Math.PI * Math.sqrt(Math.pow(a_transfer, 3) / mu);
        return Quantities.create(T_transfer / 2, Units.SECOND);
    }

    /**
     * Specific orbital energy.
     * ÃŽÂµ = -ÃŽÂ¼/(2a) = vÃ‚Â²/2 - ÃŽÂ¼/r
     */
    public static double specificOrbitalEnergy(double semiMajorAxis, double mu) {
        return -mu / (2 * semiMajorAxis);
    }

    /**
     * Calculates orbital altitude from period (for Earth).
     * 
     * @param periodSeconds orbital period in seconds
     * @return altitude above Earth surface in meters
     */
    public static Quantity<Length> altitudeFromPeriod(double periodSeconds) {
        double earthRadius = 6.371e6; // m
        double a = Math.cbrt(MU_EARTH * Math.pow(periodSeconds / (2 * Math.PI), 2));
        double altitude = a - earthRadius;
        return Quantities.create(altitude, Units.METER);
    }

    /**
     * Geostationary orbit altitude (period = 24 hours).
     * 
     * @return altitude above Earth surface
     */
    public static Quantity<Length> geostationaryAltitude() {
        return altitudeFromPeriod(86164.1); // Sidereal day in seconds
    }

    /**
     * Hill sphere radius (sphere of gravitational influence).
     * r_H Ã¢â€°Ë† a(m/3M)^(1/3)
     * 
     * @param semiMajorAxis orbit of smaller body around larger
     * @param smallerMass   mass of smaller body (kg)
     * @param largerMass    mass of larger body (kg)
     * @return Hill sphere radius
     */
    public static Quantity<Length> hillSphereRadius(double semiMajorAxis, double smallerMass, double largerMass) {
        double r = semiMajorAxis * Math.cbrt(smallerMass / (3 * largerMass));
        return Quantities.create(r, Units.METER);
    }

    /**
     * Synodic period between two orbiting bodies.
     * 1/T_syn = |1/T1 - 1/T2|
     */
    public static Quantity<Time> synodicPeriod(double period1, double period2) {
        double synodic = Math.abs(1.0 / period1 - 1.0 / period2);
        return Quantities.create(1.0 / synodic, Units.SECOND);
    }

    // ========== Lambert Problem Solver ==========

    /**
     * Result of Lambert's problem solution.
     */
    public static class LambertResult {
        public final double[] v1;
        public final double[] v2;
        public final boolean converged;

        public LambertResult(double[] v1, double[] v2, boolean converged) {
            this.v1 = v1;
            this.v2 = v2;
            this.converged = converged;
        }
    }

    /**
     * Solves Lambert's problem using Universal Variables method.
     *
     * @param r1       Position vector 1 [x, y, z] in meters
     * @param r2       Position vector 2 [x, y, z] in meters
     * @param dt       Time of flight in seconds
     * @param mu       Gravitational parameter (mÃ‚Â³/sÃ‚Â²)
     * @param prograde true for prograde (short way)
     * @return LambertResult containing v1, v2 and convergence status
     */
    public static LambertResult solveLambert(double[] r1, double[] r2, double dt, double mu, boolean prograde) {
        final int MAX_ITER = 50;
        final double TOL = 1e-10;

        double magR1 = lambertNorm(r1);
        double magR2 = lambertNorm(r2);

        double[] crossR1R2 = lambertCross(r1, r2);
        double cosDTheta = lambertDot(r1, r2) / (magR1 * magR2);
        cosDTheta = Math.max(-1, Math.min(1, cosDTheta));

        double dTheta;
        if (prograde) {
            dTheta = (crossR1R2[2] >= 0) ? Math.acos(cosDTheta) : 2 * Math.PI - Math.acos(cosDTheta);
        } else {
            dTheta = (crossR1R2[2] < 0) ? Math.acos(cosDTheta) : 2 * Math.PI - Math.acos(cosDTheta);
        }

        double A = Math.sin(dTheta) * Math.sqrt(magR1 * magR2 / (1 - Math.cos(dTheta)));
        double z = 0.0;

        for (int iter = 0; iter < MAX_ITER; iter++) {
            double C = stumpffC(z);
            double S = stumpffS(z);
            double y = magR1 + magR2 + A * (z * S - 1) / Math.sqrt(C);

            if (y < 0) {
                z = z / 2;
                continue;
            }

            double x = Math.sqrt(y / C);
            double tof = (x * x * x * S + A * Math.sqrt(y)) / Math.sqrt(mu);
            double error = tof - dt;

            if (Math.abs(error) < TOL) {
                double f = 1 - y / magR1;
                double gDot = 1 - y / magR2;
                double g = A * Math.sqrt(y / mu);

                double[] v1 = { (r2[0] - f * r1[0]) / g, (r2[1] - f * r1[1]) / g, (r2[2] - f * r1[2]) / g };
                double[] v2 = { (gDot * r2[0] - r1[0]) / g, (gDot * r2[1] - r1[1]) / g, (gDot * r2[2] - r1[2]) / g };
                return new LambertResult(v1, v2, true);
            }

            double dtof_dz = (x * x * x * (stumpffDC(z) - 3 * S * stumpffDS(z) / (2 * C)) / (2 * C)
                    + (A / 8) * (3 * S * Math.sqrt(y) / C + A / Math.sqrt(y))) / Math.sqrt(mu);
            z = z - error / dtof_dz;
        }
        return new LambertResult(new double[3], new double[3], false);
    }

    private static double stumpffC(double z) {
        if (z > 1e-6)
            return (1 - Math.cos(Math.sqrt(z))) / z;
        if (z < -1e-6)
            return (Math.cosh(Math.sqrt(-z)) - 1) / (-z);
        return 0.5 - z / 24;
    }

    private static double stumpffS(double z) {
        if (z > 1e-6) {
            double sq = Math.sqrt(z);
            return (sq - Math.sin(sq)) / (sq * sq * sq);
        }
        if (z < -1e-6) {
            double sq = Math.sqrt(-z);
            return (Math.sinh(sq) - sq) / (sq * sq * sq);
        }
        return 1.0 / 6 - z / 120;
    }

    private static double stumpffDC(double z) {
        return (1 / (2 * z)) * (1 - z * stumpffS(z) - 2 * stumpffC(z));
    }

    private static double stumpffDS(double z) {
        return (1 / (2 * z)) * (stumpffC(z) - 3 * stumpffS(z));
    }

    private static double lambertNorm(double[] v) {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

    private static double lambertDot(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    private static double[] lambertCross(double[] a, double[] b) {
        return new double[] { a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0] };
    }
}


