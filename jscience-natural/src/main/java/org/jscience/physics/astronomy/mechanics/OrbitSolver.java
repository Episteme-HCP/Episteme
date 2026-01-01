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

package org.jscience.physics.astronomy.mechanics;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Mass;

/**
 * Calculations for Keplerian Orbits.
 * <p>
 * Based on: J. Kepler, "Astronomia Nova" (1609) and "Harmonices Mundi" (1619).
 * </p>
 * <p>
 * Implements orbital mechanics calculations using the six classical
 * Keplerian orbital elements and transformations between perifocal
 * and inertial reference frames.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OrbitSolver {

    public static final double G = 6.67430e-11; // m^3 kg^-1 s^-2

    /**
     * Calculates position and velocity from Keplerian parameters and True Anomaly.
     * 
     * @param params      orbital elements
     * @param trueAnomaly angle from periapsis
     * @param centralMass mass of the central body
     * @return State vectors (Position and Velocity)
     */
    @SuppressWarnings("unchecked")
    public static OrbitalState solve(KeplerParams params, Quantity<Angle> trueAnomaly, Quantity<Mass> centralMass) {

        double a = params.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double e = params.getEccentricity();
        double i = params.getInclination().to((Unit<Angle>) (Unit<?>) Units.RADIAN).getValue().doubleValue();
        double omega = params.getArgumentPeriapsis().to((Unit<Angle>) (Unit<?>) Units.RADIAN).getValue().doubleValue();
        double Omega = params.getLongitudeAscendingNode().to((Unit<Angle>) (Unit<?>) Units.RADIAN).getValue()
                .doubleValue();
        double nu = trueAnomaly.to((Unit<Angle>) (Unit<?>) Units.RADIAN).getValue().doubleValue();
        double M = centralMass.to(Units.KILOGRAM).getValue().doubleValue();

        // 1. Distance r
        // r = a * (1 - e^2) / (1 + e * cos(nu))
        double p = a * (1.0 - e * e); // Semilatus rectum
        double r = p / (1.0 + e * Math.cos(nu));

        // 2. Position in perifocal frame
        double x_pqw = r * Math.cos(nu);
        double y_pqw = r * Math.sin(nu);
        // double z_pqw = 0;

        // 3. Velocity in perifocal frame
        // mu = G * M
        double mu = G * M;
        double v_factor = Math.sqrt(mu / p);

        double vx_pqw = -v_factor * Math.sin(nu);
        double vy_pqw = v_factor * (e + Math.cos(nu));
        // double vz_pqw = 0;

        // 4. Rotate to ECI (Inertial Frame)
        // 3-1-3 Rotation ? No, commonly Omega (z), i (x), omega (z)
        // R = Rz(-Omega) * Rx(-i) * Rz(-omega) ... wait, we transform FROM perifocal TO
        // ECI.
        // x_eci = ...

        double cosO = Math.cos(Omega);
        double sinO = Math.sin(Omega);
        double cosi = Math.cos(i);
        double sini = Math.sin(i);
        double coso = Math.cos(omega);
        double sino = Math.sin(omega);

        // Transformation Matrix elements
        // P vector (x axis of perifocal)
        double Px = cosO * coso - sinO * sino * cosi;
        double Py = sinO * coso + cosO * sino * cosi;
        double Pz = sino * sini;

        // Q vector (y axis of perifocal)
        double Qx = -cosO * sino - sinO * coso * cosi;
        double Qy = -sinO * sino + cosO * coso * cosi;
        double Qz = coso * sini;

        // r_vec = x_pqw * P + y_pqw * Q
        double rx = x_pqw * Px + y_pqw * Qx;
        double ry = x_pqw * Py + y_pqw * Qy;
        double rz = x_pqw * Pz + y_pqw * Qz;

        // v_vec = vx_pqw * P + vy_pqw * Q
        double vx = vx_pqw * Px + vy_pqw * Qx;
        double vy = vx_pqw * Py + vy_pqw * Qy;
        double vz = vx_pqw * Pz + vy_pqw * Qz;

        return new OrbitalState(
                Quantities.create(rx, Units.METER),
                Quantities.create(ry, Units.METER),
                Quantities.create(rz, Units.METER),
                Quantities.create(vx, Units.METERS_PER_SECOND),
                Quantities.create(vy, Units.METERS_PER_SECOND),
                Quantities.create(vz, Units.METERS_PER_SECOND));
    }
}


