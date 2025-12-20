/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
 * @author Gemini AI
 * @since 2.0
 */
public class OrbitalMechanics {

    /** Gravitational constant (m³/(kg·s²)) */
    public static final double G = 6.67430e-11;

    /** Standard gravitational parameter for Earth (m³/s²) */
    public static final double MU_EARTH = 3.986004418e14;

    /** Standard gravitational parameter for Sun (m³/s²) */
    public static final double MU_SUN = 1.32712440018e20;

    private OrbitalMechanics() {
    }

    /**
     * Kepler's Third Law: Orbital period from semi-major axis.
     * T = 2π√(a³/μ)
     * 
     * @param semiMajorAxis orbital semi-major axis in meters
     * @param mu            gravitational parameter (m³/s²)
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
     * v = √[μ(2/r - 1/a)]
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
     * v = √(μ/r)
     */
    public static Quantity<Velocity> circularVelocity(double radius, double mu) {
        double v = Math.sqrt(mu / radius);
        return Quantities.create(v, Units.METER_PER_SECOND);
    }

    /**
     * Escape velocity from distance r.
     * v_esc = √(2μ/r)
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
     * ε = -μ/(2a) = v²/2 - μ/r
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
     * r_H ≈ a(m/3M)^(1/3)
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
}
