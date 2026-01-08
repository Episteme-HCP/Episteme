/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Advanced Magnetoresistance models including Valet-Fert and Julliere.
 */
public class GMREffect {

    /**
     * TMR Ratio using Julliere's Formula.
     * TMR = 2*P1*P2 / (1 - P1*P2)
     */
    public static Real calculateJulliereTMR(Real p1, Real p2) {
        Real pProduct = p1.multiply(p2);
        return Real.TWO.multiply(pProduct).divide(Real.ONE.subtract(pProduct));
    }

    /**
     * CPP-GMR using simplified Valet-Fert (long or short limit).
     * Area Resistance (AR) calculation.
     */
    public static Real valetFertResistance(SpinValve valve) {
        // Reduced to the series resistance of spin channels in the CPP limit
        Real rp = calculateSeries(valve, true);
        Real rap = calculateSeries(valve, false);

        Real angle = valve.getFreeLayer().getAngleWith(valve.getPinnedLayer());
        // R(θ) = R_P + (R_AP - R_P) * sin²(θ/2)
        Real sinSqHalfTheta = angle.divide(Real.TWO).sin().pow(2);
        return rp.add(rap.subtract(rp).multiply(sinSqHalfTheta));
    }

    private static Real calculateSeries(SpinValve valve, boolean parallel) {
        FerromagneticLayer f1 = valve.getPinnedLayer();
        FerromagneticLayer f2 = valve.getFreeLayer();

        Real rho1 = f1.getMaterial().getResistivity();
        Real beta1 = f1.getMaterial().getSpinPolarization();
        Real t1 = f1.getThickness();

        Real rho2 = f2.getMaterial().getResistivity();
        Real beta2 = f2.getMaterial().getSpinPolarization();
        Real t2 = f2.getThickness();

        Real rs = valve.getSpacerMaterial().getResistivity().multiply(valve.getSpacerThickness());

        if (parallel) {
            // Channel UP
            Real rUp = rho1.multiply(Real.ONE.subtract(beta1)).multiply(t1)
                    .add(rs)
                    .add(rho2.multiply(Real.ONE.subtract(beta2)).multiply(t2));
            // Channel DOWN
            Real rDown = rho1.multiply(Real.ONE.add(beta1)).multiply(t1)
                    .add(rs)
                    .add(rho2.multiply(Real.ONE.add(beta2)).multiply(t2));
            return rUp.multiply(rDown).divide(rUp.add(rDown));
        } else {
            // Anti-parallel: channels are mixed
            Real r1 = rho1.multiply(Real.ONE.subtract(beta1)).multiply(t1)
                    .add(rs)
                    .add(rho2.multiply(Real.ONE.add(beta2)).multiply(t2));
            Real r2 = rho1.multiply(Real.ONE.add(beta1)).multiply(t1)
                    .add(rs)
                    .add(rho2.multiply(Real.ONE.subtract(beta2)).multiply(t2));
            return r1.multiply(r2).divide(r1.add(r2));
        }
    }
}
