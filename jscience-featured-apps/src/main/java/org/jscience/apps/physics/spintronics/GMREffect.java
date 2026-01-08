/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Advanced Magnetoresistance models for Spintronics.
 * <p>
 * This class implements standard models for Giant Magnetoresistance (GMR) and
 * Tunnel Magnetoresistance (TMR), which earned Albert Fert and Peter Grünberg
 * the
 * Nobel Prize in Physics (2007).
 * </p>
 * 
 * <h3>Models Implemented</h3>
 * <ul>
 * <li><b>Valet-Fert (CPP-GMR)</b>: Calculates resistance for Current
 * Perpendicular to Plane geometry
 * considering spin diffusion lengths.</li>
 * <li><b>Julliere (TMR)</b>: Simple model for magnetic tunnel junctions based
 * on spin polarization.</li>
 * </ul>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Valet, T. & Fert, A.</b> (1993). "Theory of the perpendicular
 * magnetoresistance in magnetic multilayers".
 * <i>Physical Review B</i>, 48(10), 7099.
 * <a href="https://doi.org/10.1103/PhysRevB.48.7099">DOI:
 * 10.1103/PhysRevB.48.7099</a></li>
 * <li><b>Julliere, M.</b> (1975). "Tunneling between ferromagnetic films".
 * <i>Physics Letters A</i>, 54(3), 225-226.
 * <a href="https://doi.org/10.1016/0375-9601(75)90174-7">DOI:
 * 10.1016/0375-9601(75)90174-7</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class GMREffect {

    /**
     * Calculates TMR Ratio using Julliere's Formula.
     * $$ TMR = \frac{2 P_1 P_2}{1 - P_1 P_2} $$
     * 
     * @param p1 Spin polarization of electrode 1 (0 to 1)
     * @param p2 Spin polarization of electrode 2 (0 to 1)
     * @return TMR ratio (dimensionless)
     */
    public static Real calculateJulliereTMR(Real p1, Real p2) {
        Real pProduct = p1.multiply(p2);
        return Real.TWO.multiply(pProduct).divide(Real.ONE.subtract(pProduct));
    }

    /**
     * Calculates resistance using the Valet-Fert model in the CPP (Current
     * Perpendicular to Plane) geometry.
     * This implementation assumes the "long spin diffusion length" limit or
     * simplifies the interface resistances
     * for real-time simulation purposes.
     * <p>
     * $$ R(\theta) = R_P + (R_{AP} - R_P) \frac{1 - \cos(\theta)}{2} $$
     * </p>
     * 
     * @param valve The spin valve structure
     * @param cpp   True for CPP geometry, False for CIP (Current In Plane) -
     *              Currently only CPP logic is fully detailed.
     * @return Resistance-Area product (RA) in $\Omega \cdot m^2$ (or scaled units)
     */
    public static Real valetFertResistance(SpinValve valve) {
        // Reduced to the series resistance of spin channels in the CPP limit
        Real rp = calculateSeries(valve, true);
        Real rap = calculateSeries(valve, false);

        Real angle = valve.getFreeLayer().getAngleWith(valve.getPinnedLayer());
        // R(θ) = R_P + (R_AP - R_P) * sin²(θ/2)
        // Note: sin²(θ/2) = (1 - cos(θ))/2
        Real sinSqHalfTheta = angle.divide(Real.TWO).sin().pow(2);

        return rp.add(rap.subtract(rp).multiply(sinSqHalfTheta));
    }

    private static Real calculateSeries(SpinValve valve, boolean parallel) {
        FerromagneticLayer f1 = valve.getPinnedLayer();
        FerromagneticLayer f2 = valve.getFreeLayer();

        // Material parameters
        Real rho1 = f1.getMaterial().getResistivity();
        Real beta1 = f1.getMaterial().getSpinPolarization();
        Real t1 = f1.getThickness();

        Real rho2 = f2.getMaterial().getResistivity();
        Real beta2 = f2.getMaterial().getSpinPolarization();
        Real t2 = f2.getThickness();

        Real rs = valve.getSpacerMaterial().getResistivity().multiply(valve.getSpacerThickness());
        if (valve.isSafEnabled()) {
            // If SAF is enabled, we should technically add the resistance of the SAF
            // layers.
            // For MVP, we assume the pinned layer returned by getPinnedLayer() captures the
            // relevant interface physics.
            // A full SAF model would add more series resistors.
        }

        // Two-Current Model resistor addition
        if (parallel) {
            // Channel UP (Majority-Majority)
            // R_up = rho1*(1-beta1)*t1 + rs + rho2*(1-beta2)*t2
            Real rUp = rho1.multiply(Real.ONE.subtract(beta1)).multiply(t1)
                    .add(rs)
                    .add(rho2.multiply(Real.ONE.subtract(beta2)).multiply(t2));

            // Channel DOWN (Minority-Minority)
            // R_down = rho1*(1+beta1)*t1 + rs + rho2*(1+beta2)*t2
            Real rDown = rho1.multiply(Real.ONE.add(beta1)).multiply(t1)
                    .add(rs)
                    .add(rho2.multiply(Real.ONE.add(beta2)).multiply(t2));

            // Parallel combination: R = (R_up * R_down) / (R_up + R_down)
            return rUp.multiply(rDown).divide(rUp.add(rDown));
        } else {
            // Anti-parallel: channels are mixed
            // Channel 1: Majority in FM1 -> Minority in FM2
            Real r1 = rho1.multiply(Real.ONE.subtract(beta1)).multiply(t1)
                    .add(rs)
                    .add(rho2.multiply(Real.ONE.add(beta2)).multiply(t2));

            // Channel 2: Minority in FM1 -> Majority in FM2
            Real r2 = rho1.multiply(Real.ONE.add(beta1)).multiply(t1)
                    .add(rs)
                    .add(rho2.multiply(Real.ONE.subtract(beta2)).multiply(t2));

            return r1.multiply(r2).divide(r1.add(r2));
        }
    }
}
