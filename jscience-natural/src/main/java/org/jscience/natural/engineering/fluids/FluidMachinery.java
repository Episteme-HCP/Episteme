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

package org.jscience.natural.engineering.fluids;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Frequency;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.measure.quantity.MassDensity;
import org.jscience.core.measure.quantity.Power;
import org.jscience.core.measure.quantity.Pressure;
import org.jscience.core.measure.quantity.Velocity;
import org.jscience.core.measure.quantity.VolumetricFlowRate;
import org.jscience.natural.physics.PhysicalConstants;

/**
 * Fluid machinery calculations.
 * Modernized to use high-precision Real and typed Quantities.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FluidMachinery {

    /** Water density (kg/mÃ‚Â³) */
    public static final Quantity<MassDensity> RHO_WATER = Quantities.create(1000, Units.KILOGRAM.divide(Units.CUBIC_METER).asType(MassDensity.class));

    /** Use PhysicalConstants for standard gravity */
    private static final Real G = PhysicalConstants.g_n;

    private FluidMachinery() {}

    /**
     * Pump hydraulic power.
     * P_h = ÃÂ * g * Q * H
     */
    public static Quantity<Power> hydraulicPower(Quantity<MassDensity> density, Quantity<VolumetricFlowRate> flowRate, Quantity<Length> head) {
        Real rho = density.to(Units.KILOGRAM.divide(Units.CUBIC_METER).asType(MassDensity.class)).getValue();
        Real q = flowRate.to(Units.CUBIC_METER.divide(Units.SECOND).asType(VolumetricFlowRate.class)).getValue();
        Real h = head.to(Units.METER).getValue();
        
        Real power = rho.multiply(G).multiply(q).multiply(h);
        return Quantities.create(power, Units.WATT);
    }

    /**
     * Pump efficiency.
     * ÃŽÂ· = P_hydraulic / P_shaft
     */
    public static Real pumpEfficiency(Quantity<Power> hydraulicPower, Quantity<Power> shaftPower) {
        Real ph = hydraulicPower.to(Units.WATT).getValue();
        Real ps = shaftPower.to(Units.WATT).getValue();
        return ph.divide(ps);
    }

    /**
     * Affinity laws: flow rate vs speed.
     * Q2 = Q1 * (N2 / N1)
     */
    public static Quantity<VolumetricFlowRate> affinityFlowRate(Quantity<VolumetricFlowRate> Q1, Quantity<Frequency> N1, Quantity<Frequency> N2) {
        Real q1 = Q1.getValue();
        Real n1 = N1.to(Units.HERTZ).getValue();
        Real n2 = N2.to(Units.HERTZ).getValue();
        
        Real q2 = q1.multiply(n2).divide(n1);
        return Quantities.create(q2, Q1.getUnit());
    }

    /**
     * Affinity laws: head vs speed.
     * H2 = H1 * (N2 / N1)Ã‚Â²
     */
    public static Quantity<Length> affinityHead(Quantity<Length> H1, Quantity<Frequency> N1, Quantity<Frequency> N2) {
        Real h1 = H1.getValue();
        Real n1 = N1.to(Units.HERTZ).getValue();
        Real n2 = N2.to(Units.HERTZ).getValue();
        
        Real h2 = h1.multiply(n2.divide(n1).pow(2));
        return Quantities.create(h2, H1.getUnit());
    }

    /**
     * Affinity laws: power vs speed.
     * P2 = P1 * (N2 / N1)Ã‚Â³
     */
    public static Quantity<Power> affinityPower(Quantity<Power> P1, Quantity<Frequency> N1, Quantity<Frequency> N2) {
        Real p1 = P1.getValue();
        Real n1 = N1.to(Units.HERTZ).getValue();
        Real n2 = N2.to(Units.HERTZ).getValue();
        
        Real p2 = p1.multiply(n2.divide(n1).pow(3));
        return Quantities.create(p2, P1.getUnit());
    }

    /**
     * Net Positive Suction Head available.
     * NPSH_a = (P_atm / (ÃÂg)) + H_s - H_f - (P_vap / (ÃÂg))
     */
    public static Quantity<Length> npshAvailable(Quantity<Pressure> atmosphericPressure, Quantity<Length> suctionHeight,
            Quantity<Length> frictionLoss, Quantity<Pressure> vaporPressure, Quantity<MassDensity> density) {
            
        Real pAtm = atmosphericPressure.to(Units.PASCAL).getValue();
        Real rho = density.to(Units.KILOGRAM.divide(Units.CUBIC_METER).asType(MassDensity.class)).getValue();
        Real hS = suctionHeight.to(Units.METER).getValue();
        Real hF = frictionLoss.to(Units.METER).getValue();
        Real pVap = vaporPressure.to(Units.PASCAL).getValue();
        
        Real term1 = pAtm.divide(rho.multiply(G));
        Real term4 = pVap.divide(rho.multiply(G));
        
        Real npsh = term1.add(hS).subtract(hF).subtract(term4);
        return Quantities.create(npsh, Units.METER);
    }

    /**
     * Cavitation number.
     * ÃÆ’ = (P - P_vap) / (0.5 * ÃÂ * vÃ‚Â²)
     */
    public static Real cavitationNumber(Quantity<Pressure> pressure, Quantity<Pressure> vaporPressure,
            Quantity<MassDensity> density, Quantity<Velocity> velocity) {
            
        Real p = pressure.to(Units.PASCAL).getValue();
        Real pVap = vaporPressure.to(Units.PASCAL).getValue();
        Real rho = density.to(Units.KILOGRAM.divide(Units.CUBIC_METER).asType(MassDensity.class)).getValue();
        Real v = velocity.to(Units.METERS_PER_SECOND).getValue();
        
        return p.subtract(pVap).divide(Real.of(0.5).multiply(rho).multiply(v.pow(2)));
    }

    /**
     * Specific speed (dimensionless-ish).
     * Ns = N * sqrt(Q) / H^(3/4)
     * Using RPM, mÃ‚Â³/s, m often; here we keep it consistent with base units but return Real.
     */
    public static Real specificSpeed(Quantity<Frequency> speed, Quantity<VolumetricFlowRate> flowRate, Quantity<Length> head) {
        // Standard definition is N(rpm)*sqrt(Q)/H^0.75
        // Let's use strict units.
        Real nm = speed.to(Units.HERTZ).getValue().multiply(Real.of(60));
        Real q = flowRate.to(Units.CUBIC_METER.divide(Units.SECOND).asType(VolumetricFlowRate.class)).getValue();
        Real h = head.to(Units.METER).getValue();
        
        return nm.multiply(q.sqrt()).divide(h.pow(Real.of(0.75)));
    }

    /**
     * Turbine power output.
     * P = ÃŽÂ· * ÃÂ * g * Q * H
     */
    public static Quantity<Power> turbinePower(Real efficiency, Quantity<MassDensity> density, Quantity<VolumetricFlowRate> flowRate, Quantity<Length> head) {
        Real rho = density.to(Units.KILOGRAM.divide(Units.CUBIC_METER).asType(MassDensity.class)).getValue();
        Real q = flowRate.to(Units.CUBIC_METER.divide(Units.SECOND).asType(VolumetricFlowRate.class)).getValue();
        Real h = head.to(Units.METER).getValue();
        
        Real power = efficiency.multiply(rho).multiply(G).multiply(q).multiply(h);
        return Quantities.create(power, Units.WATT);
    }

    /**
     * Pelton wheel jet velocity.
     * v = Cv * sqrt(2 * g * H)
     */
    public static Quantity<Velocity> peltonJetVelocity(Quantity<Length> head, Real velocityCoefficient) {
        Real h = head.to(Units.METER).getValue();
        Real v = velocityCoefficient.multiply(Real.TWO.multiply(G).multiply(h).sqrt());
        return Quantities.create(v, Units.METERS_PER_SECOND);
    }
}




