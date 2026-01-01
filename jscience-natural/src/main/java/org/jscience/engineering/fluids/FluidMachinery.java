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

package org.jscience.engineering.fluids;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;

/**
 * Fluid machinery calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FluidMachinery {

    /** Water density (kg/mÃ‚Â³) */
    public static final Real RHO_WATER = Real.of(1000);

    /** Use PhysicalConstants for standard gravity */
    private static final Real G = PhysicalConstants.g_n;

    /**
     * Pump hydraulic power.
     * P_h = ÃÂ * g * Q * H
     */
    public static Real hydraulicPower(Real density, Real flowRate, Real head) {
        return density.multiply(G).multiply(flowRate).multiply(head);
    }

    /**
     * Pump efficiency.
     * ÃŽÂ· = P_hydraulic / P_shaft
     */
    public static Real pumpEfficiency(Real hydraulicPower, Real shaftPower) {
        return hydraulicPower.divide(shaftPower);
    }

    /**
     * Affinity laws: flow rate vs speed.
     */
    public static Real affinityFlowRate(Real Q1, Real N1, Real N2) {
        return Q1.multiply(N2).divide(N1);
    }

    /**
     * Affinity laws: head vs speed.
     */
    public static Real affinityHead(Real H1, Real N1, Real N2) {
        return H1.multiply(N2.divide(N1).pow(2));
    }

    /**
     * Affinity laws: power vs speed.
     */
    public static Real affinityPower(Real P1, Real N1, Real N2) {
        return P1.multiply(N2.divide(N1).pow(3));
    }

    /**
     * Net Positive Suction Head available.
     */
    public static Real npshAvailable(Real atmosphericPressure, Real suctionHeight,
            Real frictionLoss, Real vaporPressure, Real density) {
        return atmosphericPressure.divide(density.multiply(G)).add(suctionHeight)
                .subtract(frictionLoss).subtract(vaporPressure.divide(density.multiply(G)));
    }

    /**
     * Cavitation number.
     */
    public static Real cavitationNumber(Real pressure, Real vaporPressure,
            Real density, Real velocity) {
        return pressure.subtract(vaporPressure).divide(
                Real.of(0.5).multiply(density).multiply(velocity.pow(2)));
    }

    /**
     * Specific speed (dimensionless).
     */
    public static Real specificSpeed(Real rpm, Real flowRate, Real head) {
        return rpm.multiply(flowRate.sqrt()).divide(head.pow(Real.of(0.75)));
    }

    /**
     * Turbine power output.
     */
    public static Real turbinePower(Real efficiency, Real density, Real flowRate, Real head) {
        return efficiency.multiply(density).multiply(G).multiply(flowRate).multiply(head);
    }

    /**
     * Pelton wheel jet velocity.
     */
    public static Real peltonJetVelocity(Real head, Real velocityCoefficient) {
        return velocityCoefficient.multiply(Real.TWO.multiply(G).multiply(head).sqrt());
    }

    /**
     * Optimal bucket speed for Pelton wheel.
     */
    public static Real peltonOptimalBucketSpeed(Real jetVelocity) {
        return jetVelocity.divide(Real.TWO);
    }

    /**
     * Francis turbine runner speed.
     */
    public static Real runnerSpeed(Real diameter, Real rpm) {
        return Real.PI.multiply(diameter).multiply(rpm).divide(Real.of(60));
    }

    /**
     * Compressor isentropic work.
     */
    public static Real isentropicWork(Real gamma, Real R, Real T1, Real P1, Real P2) {
        Real exponent = gamma.subtract(Real.ONE).divide(gamma);
        return gamma.divide(gamma.subtract(Real.ONE)).multiply(R).multiply(T1)
                .multiply(P2.divide(P1).pow(exponent).subtract(Real.ONE));
    }

    /**
     * Fan laws (same as affinity laws).
     */
    public static Real fanFlowRate(Real Q1, Real D1, Real D2, Real N1, Real N2) {
        return Q1.multiply(D2.divide(D1).pow(3)).multiply(N2.divide(N1));
    }
}


