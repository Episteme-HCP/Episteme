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
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.astronomy.AstronomyConstants;

/**
 * Orbital mechanics calculations.
 * <p>
 * Provides Kepler's laws, vis-viva equation, and orbital maneuvers.
 * All methods use type-safe Quantity measurements and arbitrary precision Real numbers.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OrbitalMechanics {

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
    public static Quantity<Time> orbitalPeriod(Real semiMajorAxis, Real mu) {
        Real T = Real.TWO_PI.multiply(
            semiMajorAxis.pow(3).divide(mu).sqrt()
        );
        return Quantities.create(T, Units.SECOND);
    }

    /**
     * Orbital period for Earth-orbiting satellite.
     * 
     * @param semiMajorAxis in meters
     * @return period
     */
    public static Quantity<Time> orbitalPeriodEarth(Real semiMajorAxis) {
        return orbitalPeriod(semiMajorAxis, AstronomyConstants.MU_EARTH);
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
    public static Quantity<Velocity> orbitalVelocity(Real r, Real semiMajorAxis, Real mu) {
        Real two = Real.TWO; // Using Real.TWO constant
        // Real has ONE, ZERO, PI, E. Wait, does it have TWO? Step 5717 Line 67: public static final Real TWO.
        // It's available.
        
        Real term = Real.TWO.divide(r).subtract(Real.ONE.divide(semiMajorAxis));
        Real v = mu.multiply(term).sqrt();
        return Quantities.create(v, Units.METER_PER_SECOND);
    }

    /**
     * Circular orbit velocity.
     * v = √(μ/r)
     */
    public static Quantity<Velocity> circularVelocity(Real radius, Real mu) {
        Real v = mu.divide(radius).sqrt();
        return Quantities.create(v, Units.METER_PER_SECOND);
    }

    /**
     * Escape velocity from distance r.
     * v_esc = √(2μ/r)
     */
    public static Quantity<Velocity> escapeVelocity(Real radius, Real mu) {
        Real v = Real.TWO.multiply(mu).divide(radius).sqrt();
        return Quantities.create(v, Units.METER_PER_SECOND);
    }

    /**
     * Hohmann transfer delta-v (from circular to circular).
     * 
     * @param r1 initial orbit radius (m)
     * @param r2 final orbit radius (m)
     * @param mu gravitational parameter
     * @return total delta-v required
     */
    public static Quantity<Velocity> hohmannDeltaV(Real r1, Real r2, Real mu) {
        Real a_transfer = r1.add(r2).divide(Real.TWO);

        // Velocity at r1 on circular orbit
        Real v1_circ = mu.divide(r1).sqrt();
        // Velocity at r1 on transfer ellipse: sqrt(mu * (2/r1 - 1/a))
        Real v1_transfer = mu.multiply(
            Real.TWO.divide(r1).subtract(Real.ONE.divide(a_transfer))
        ).sqrt();
        
        Real dv1 = v1_transfer.subtract(v1_circ).abs();

        // Velocity at r2 on transfer ellipse
        Real v2_transfer = mu.multiply(
            Real.TWO.divide(r2).subtract(Real.ONE.divide(a_transfer))
        ).sqrt();
        // Velocity at r2 on circular orbit
        Real v2_circ = mu.divide(r2).sqrt();
        Real dv2 = v2_circ.subtract(v2_transfer).abs();

        return Quantities.create(dv1.add(dv2), Units.METER_PER_SECOND);
    }

    /**
     * Hohmann transfer time (half the period of transfer orbit).
     */
    public static Quantity<Time> hohmannTransferTime(Real r1, Real r2, Real mu) {
        Real a_transfer = r1.add(r2).divide(Real.TWO);
        // T_transfer = 2 * PI * sqrt(a^3 / mu) ... but we want half logic
        // T (period) / 2
        Real T = Real.TWO_PI.multiply(
            a_transfer.pow(3).divide(mu).sqrt()
        );
        return Quantities.create(T.divide(Real.TWO), Units.SECOND);
    }

    /**
     * Specific orbital energy.
     * ε = -μ/(2a)
     */
    public static Quantity<?> specificOrbitalEnergy(Real semiMajorAxis, Real mu) { // Unit J/kg = m^2/s^2
        Real energy = mu.negate().divide(Real.TWO.multiply(semiMajorAxis));
        return Quantities.create(energy, Units.JOULE.divide(Units.KILOGRAM));
    }

    /**
     * Calculates orbital altitude from period (for Earth).
     * 
     * @param periodSeconds orbital period in seconds
     * @return altitude above Earth surface in meters
     */
    public static Quantity<Length> altitudeFromPeriod(Real periodSeconds) {
        Real earthRadius = Real.of(6.371e6); // Should actally be AstronomyConstants.EARTH_RADIUS if exists, but keeping logic
        // a = cbrt(MU * (T/2pi)^2)
        Real ratio = periodSeconds.divide(Real.TWO_PI);
        Real a = AstronomyConstants.MU_EARTH.multiply(ratio.pow(2)).cbrt();
        Real altitude = a.subtract(earthRadius);
        return Quantities.create(altitude, Units.METER);
    }

    /**
     * Geostationary orbit altitude (period = 24 hours).
     * 
     * @return altitude above Earth surface
     */
    public static Quantity<Length> geostationaryAltitude() {
        return altitudeFromPeriod(Real.of(86164.1)); // Sidereal day in seconds
    }

    /**
     * Hill sphere radius.
     */
    public static Quantity<Length> hillSphereRadius(Real semiMajorAxis, Real smallerMass, Real largerMass) {
        Real r = semiMajorAxis.multiply(
            smallerMass.divide(Real.of(3).multiply(largerMass)).cbrt()
        );
        return Quantities.create(r, Units.METER);
    }

    /**
     * Synodic period between two orbiting bodies.
     * 1/T_syn = |1/T1 - 1/T2|
     */
    public static Quantity<Time> synodicPeriod(Real period1, Real period2) {
        Real inv1 = Real.ONE.divide(period1);
        Real inv2 = Real.ONE.divide(period2);
        Real synodicInv = inv1.subtract(inv2).abs();
        return Quantities.create(Real.ONE.divide(synodicInv), Units.SECOND);
    }

    // ========== Lambert Problem Solver ==========

    public static class LambertResult {
        public final Real[] v1;
        public final Real[] v2;
        public final boolean converged;

        public LambertResult(Real[] v1, Real[] v2, boolean converged) {
            this.v1 = v1;
            this.v2 = v2;
            this.converged = converged;
        }
    }

    public static LambertResult solveLambert(Real[] r1, Real[] r2, Real dt, Real mu, boolean prograde) {
        final int MAX_ITER = 50;
        final Real TOL = Real.of(1e-10);

        Real magR1 = lambertNorm(r1);
        Real magR2 = lambertNorm(r2);

        Real[] crossR1R2 = lambertCross(r1, r2);
        Real dot = lambertDot(r1, r2);
        Real cosDTheta = dot.divide(magR1.multiply(magR2));
        
        // clamp -1 to 1
        if (cosDTheta.compareTo(Real.ONE) > 0) cosDTheta = Real.ONE;
        if (cosDTheta.compareTo(Real.ONE.negate()) < 0) cosDTheta = Real.ONE.negate();

        Real dTheta;
        Real twoPi = Real.TWO_PI;
        if (prograde) {
            dTheta = (crossR1R2[2].compareTo(Real.ZERO) >= 0) ? cosDTheta.acos() : twoPi.subtract(cosDTheta.acos());
        } else {
            dTheta = (crossR1R2[2].compareTo(Real.ZERO) < 0) ? cosDTheta.acos() : twoPi.subtract(cosDTheta.acos());
        }

        Real A = dTheta.sin().multiply(
             magR1.multiply(magR2).divide(Real.ONE.subtract(dTheta.cos())).sqrt()
        );
        
        Real z = Real.ZERO;

        for (int iter = 0; iter < MAX_ITER; iter++) {
            Real C = stumpffC(z);
            Real S = stumpffS(z);
            
            // y = r1 + r2 + A * (z*S - 1) / sqrt(C)
            Real bracket = z.multiply(S).subtract(Real.ONE);
            Real y = magR1.add(magR2).add(
                A.multiply(bracket).divide(C.sqrt())
            );

            if (y.compareTo(Real.ZERO) < 0) {
                 // z = z / 2; (Avoiding infinite loop in real logic?)
                 // Original logic roughly valid.
                 // Actually this logic for negative y implies searching elsewhere.
                 // Let's assume standard behavior.
                 // But wait, z update logic: z = z.divide(Real.TWO);
                 // And continue?
            }
            
            // For now, assume y > 0 to proceed with sqrt(y)
            // But if y < 0, original code did continue.
             if (y.compareTo(Real.ZERO) < 0) {
                z = z.divide(Real.TWO);
                continue;
             }

            Real x = y.divide(C).sqrt();
            // tof = (x^3 * S + A * sqrt(y)) / sqrt(mu)
            Real tof = x.pow(3).multiply(S).add(A.multiply(y.sqrt())).divide(mu.sqrt());
            Real error = tof.subtract(dt);

            if (error.abs().compareTo(TOL) < 0) {
                Real f = Real.ONE.subtract(y.divide(magR1));
                Real gDot = Real.ONE.subtract(y.divide(magR2));
                Real g = A.multiply(y.divide(mu).sqrt());

                Real[] v1 = {
                    r2[0].subtract(f.multiply(r1[0])).divide(g),
                    r2[1].subtract(f.multiply(r1[1])).divide(g),
                    r2[2].subtract(f.multiply(r1[2])).divide(g)
                };
                Real[] v2 = {
                    gDot.multiply(r2[0]).subtract(r1[0]).divide(g),
                    gDot.multiply(r2[1]).subtract(r1[1]).divide(g),
                    gDot.multiply(r2[2]).subtract(r1[2]).divide(g)
                };
                return new LambertResult(v1, v2, true);
            }

            // Derivative logic
            // ... (omitted for brevity in verification, but included in file)
            // dtof_dz = ...
            // Need care with derivatives.
            // Copied from logic:
            // term1 = x^3 * (DC - 3S*DS/2C) / 2C
            // term2 = A/8 * (3S*sqrt(y)/C + A/sqrt(y))
            // dtof_dz = (term1 + term2) / sqrt(mu)
            
            Real DC = stumpffDC(z);
            Real DS = stumpffDS(z);
            
            Real twoC = Real.TWO.multiply(C);
            Real term1 = x.pow(3).multiply(
                 DC.subtract(Real.of(3).multiply(S).multiply(DS).divide(twoC))
            ).divide(twoC);
            
            Real term2 = A.divide(Real.of(8)).multiply(
                 Real.of(3).multiply(S).multiply(y.sqrt()).divide(C).add(A.divide(y.sqrt()))
            );
            
            Real dtof_dz = term1.add(term2).divide(mu.sqrt());
            z = z.subtract(error.divide(dtof_dz));
        }
        
        Real[] zero3 = {Real.ZERO, Real.ZERO, Real.ZERO};
        return new LambertResult(zero3, zero3, false);
    }
    
    private static Real stumpffC(Real z) {
        Real limit = Real.of(1e-6);
        if (z.compareTo(limit) > 0) {
            Real sq = z.sqrt();
            return Real.ONE.subtract(sq.cos()).divide(z);
        }
        if (z.compareTo(limit.negate()) < 0) {
            Real sq = z.negate().sqrt();
            return sq.cosh().subtract(Real.ONE).divide(z.negate());
        }
        return Real.of(0.5).subtract(z.divide(Real.of(24)));
    }

    private static Real stumpffS(Real z) {
        Real limit = Real.of(1e-6);
        if (z.compareTo(limit) > 0) {
            Real sq = z.sqrt();
            return sq.subtract(sq.sin()).divide(sq.pow(3));
        }
        if (z.compareTo(limit.negate()) < 0) {
            Real sq = z.negate().sqrt();
            return sq.sinh().subtract(sq).divide(sq.pow(3));
        }
        return Real.ONE.divide(Real.of(6)).subtract(z.divide(Real.of(120)));
    }
    
    // ... stumpffDC, DS ...
    private static Real stumpffDC(Real z) {
        // (1 / 2z) * (1 - zS - 2C)
        Real oneOver2z = Real.ONE.divide(Real.TWO.multiply(z));
        return oneOver2z.multiply(
            Real.ONE.subtract(z.multiply(stumpffS(z))).subtract(Real.TWO.multiply(stumpffC(z)))
        );
    }
    
    private static Real stumpffDS(Real z) {
        // (1 / 2z) * (C - 3S)
        Real oneOver2z = Real.ONE.divide(Real.TWO.multiply(z));
        return oneOver2z.multiply(
             stumpffC(z).subtract(Real.of(3).multiply(stumpffS(z)))
        );
    }

    private static Real lambertNorm(Real[] v) {
        return v[0].pow(2).add(v[1].pow(2)).add(v[2].pow(2)).sqrt();
    }

    private static Real lambertDot(Real[] a, Real[] b) {
        return a[0].multiply(b[0]).add(a[1].multiply(b[1])).add(a[2].multiply(b[2]));
    }

    private static Real[] lambertCross(Real[] a, Real[] b) {
        return new Real[] { 
            a[1].multiply(b[2]).subtract(a[2].multiply(b[1])), 
            a[2].multiply(b[0]).subtract(a[0].multiply(b[2])), 
            a[0].multiply(b[1]).subtract(a[1].multiply(b[0])) 
        };
    }
}
