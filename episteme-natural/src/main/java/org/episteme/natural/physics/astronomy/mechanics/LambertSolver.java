/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics.astronomy.mechanics;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Time;
import org.episteme.core.measure.Units;

/**
 * Solver for Lambert's problem: finding the orbit between two points in space for a given duration.
 * 
 * Boundary value problem for the differential equation of the Two-Body Problem.
 */
public final class LambertSolver {

    private LambertSolver() {}

    /**
     * Solves Lambert's problem using a robust iterative method.
     * 
     * @param r1 Position vector at t1 (km)
     * @param r2 Position vector at t2 (km)
     * @param dt Time of flight (seconds)
     * @param mu Standard gravitational parameter (km^3/s^2)
     * @param retrograde Whether the orbit is retrograde
     * @return Initial velocity vector (km/s)
     */
    public static Real[] solve(Real[] r1, Real[] r2, Quantity<Time> dt, double mu, boolean retrograde) {
        double tOfFlight = dt.to(Units.SECOND).getValue().doubleValue();
        
        double magR1 = Math.sqrt(r1[0].pow(2).add(r1[1].pow(2)).add(r1[2].pow(2)).doubleValue());
        double magR2 = Math.sqrt(r2[0].pow(2).add(r2[1].pow(2)).add(r2[2].pow(2)).doubleValue());

        double cosDeltaNu = (r1[0].multiply(r2[0]).add(r1[1].multiply(r2[1])).add(r1[2].multiply(r2[2])).doubleValue()) / (magR1 * magR2);
        
        if (retrograde) {
            // Retrograde handling
        }

        double deltaNu = Math.acos(Math.max(-1.0, Math.min(1.0, cosDeltaNu)));
        if (retrograde) deltaNu = 2 * Math.PI - deltaNu;

        double A = Math.sin(deltaNu) * Math.sqrt(magR1 * magR2 / (1.0 - cosDeltaNu));

        // Iterative solution for z (Stumpff variables)
        double z = 0.0;
        double zUpper = 4 * Math.PI * Math.PI;
        double zLower = -4 * Math.PI;
        
        for (int i = 0; i < 100; i++) {
            double c2 = stumpffC2(z);
            double c3 = stumpffC3(z);
            
            double y = magR1 + magR2 + A * (z * c3 - 1.0) / Math.sqrt(c2);
            double chi = Math.sqrt(y / c2);
            double t = (Math.pow(chi, 3) * c3 + A * Math.sqrt(y)) / Math.sqrt(mu);
            
            if (Math.abs(t - tOfFlight) < 1.0e-6) break;
            
            if (t <= tOfFlight) {
                zLower = z;
            } else {
                zUpper = z;
            }
            z = (zUpper + zLower) / 2.0;
        }

        double y = magR1 + magR2 + A * (z * stumpffC3(z) - 1.0) / Math.sqrt(stumpffC2(z));
        double f = 1.0 - y / magR1;
        double g = A * Math.sqrt(y / mu);
        
        Real[] v1 = new Real[3];
        v1[0] = r2[0].subtract(r1[0].multiply(Real.of(f))).divide(Real.of(g));
        v1[1] = r2[1].subtract(r1[1].multiply(Real.of(f))).divide(Real.of(g));
        v1[2] = r2[2].subtract(r1[2].multiply(Real.of(f))).divide(Real.of(g));
        
        return v1;
    }

    private static double stumpffC2(double z) {
        if (z > 0) return (1.0 - Math.cos(Math.sqrt(z))) / z;
        if (z < 0) return (Math.cosh(Math.sqrt(-z)) - 1.0) / (-z);
        return 0.5;
    }

    private static double stumpffC3(double z) {
        if (z > 0) return (Math.sqrt(z) - Math.sin(Math.sqrt(z))) / Math.pow(z, 1.5);
        if (z < 0) return (Math.sinh(Math.sqrt(-z)) - Math.sqrt(-z)) / Math.pow(-z, 1.5);
        return 1.0 / 6.0;
    }
}

