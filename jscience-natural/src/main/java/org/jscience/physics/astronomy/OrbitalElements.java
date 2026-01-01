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

package org.jscience.physics.astronomy;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Reals;
import java.util.Arrays;

/**
 * Represents Keplerian Orbital Elements.
 * <p>
 * Defines an orbit using 6 parameters:
 * <ul>
 * <li>a: Semi-major axis (m)</li>
 * <li>e: Eccentricity (dimensionless)</li>
 * <li>i: Inclination (rad)</li>
 * <li>ÃŽÂ© (Omega): Longitude of ascending node (rad)</li>
 * <li>Ãâ€° (omega): Argument of periapsis (rad)</li>
 * <li>M: Mean anomaly at epoch (rad)</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OrbitalElements {

    public final Real a;
    public final Real e;
    public final Real i;
    public final Real Omega;
    public final Real omega;
    public final Real M;
    public final Real mu; // Standard gravitational parameter of parent

    public OrbitalElements(Real a, Real e, Real i, Real Omega, Real omega, Real M, Real mu) {
        this.a = a;
        this.e = e;
        this.i = i;
        this.Omega = Omega;
        this.omega = omega;
        this.M = M;
        this.mu = mu;
    }

    /**
     * Converts orbital elements to state vectors (position and velocity).
     * 
     * @return Object[] {Vector<Real> r, Vector<Real> v}
     */
    public Object[] toStateVector() {
        double aVal = a.doubleValue();
        double eVal = e.doubleValue();
        double iVal = i.doubleValue();
        double OVal = Omega.doubleValue();
        double wVal = omega.doubleValue();
        double MVal = M.doubleValue();
        double muVal = mu.doubleValue();

        // Solve Kepler equation M = E - e sin E for E (Eccentric anomaly)
        double E = MVal;
        double delta = 1.0;
        int iter = 0;
        while (Math.abs(delta) > 1e-10 && iter < 100) {
            delta = (E - eVal * Math.sin(E) - MVal) / (1 - eVal * Math.cos(E));
            E = E - delta;
            iter++;
        }

        // True anomaly (nu)
        double nu = 2 * Math.atan2(Math.sqrt(1 + eVal) * Math.sin(E / 2), Math.sqrt(1 - eVal) * Math.cos(E / 2));

        // Distance
        double r = aVal * (1 - eVal * Math.cos(E));

        // Position / Velocity in orbital frame (PQW)
        double X = r * Math.cos(nu);
        double Y = r * Math.sin(nu);

        double p = aVal * (1 - eVal * eVal);
        double h = Math.sqrt(muVal * p); // angular momentum

        double V_X = (X * h * eVal * Math.sin(nu)) / (r * p); // Derived from h = r x v?
        // Simplified:
        // V_X = -sqrt(mu/p) * sin(nu)
        // V_Y = sqrt(mu/p) * (e + cos(nu))

        double term = Math.sqrt(muVal / p);
        V_X = -term * Math.sin(nu);
        double V_Y = term * (eVal + Math.cos(nu));

        // Rotate to inertial frame
        double cO = Math.cos(OVal);
        double sO = Math.sin(OVal);
        double co = Math.cos(wVal);
        double so = Math.sin(wVal);
        double ci = Math.cos(iVal);
        double si = Math.sin(iVal);

        // Rotation matrix elements
        // P vector (X axis of orbit)
        double Px = cO * co - sO * so * ci;
        double Py = sO * co + cO * so * ci;
        double Pz = so * si;

        // Q vector (Y axis of orbit)
        double Qx = -cO * so - sO * co * ci;
        double Qy = -sO * so + cO * co * ci;
        double Qz = co * si;

        double rx = X * Px + Y * Qx;
        double ry = X * Py + Y * Qy;
        double rz = X * Pz + Y * Qz;

        double vx = V_X * Px + V_Y * Qx;
        double vy = V_X * Py + V_Y * Qy;
        double vz = V_X * Pz + V_Y * Qz;

        Vector<Real> pos = DenseVector.of(Arrays.asList(Real.of(rx), Real.of(ry), Real.of(rz)), Reals.getInstance());
        Vector<Real> vel = DenseVector.of(Arrays.asList(Real.of(vx), Real.of(vy), Real.of(vz)), Reals.getInstance());

        return new Object[] { pos, vel };
    }
}


