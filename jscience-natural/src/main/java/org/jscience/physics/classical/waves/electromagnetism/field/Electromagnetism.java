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

package org.jscience.physics.classical.waves.electromagnetism.field;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;
import org.jscience.physics.PhysicalConstants;

/**
 * Electromagnetism equations (Maxwell's equations, Coulomb's law, etc.).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Electromagnetism {

    /**
     * Coulomb's law: F = k * |q1*q2| / rÃ‚Â²
     * where k = 1/(4Ãâ‚¬ÃŽÂµÃ¢â€šâ‚¬)
     */
    public static Quantity<Force> coulombForce(
            Quantity<ElectricCharge> q1,
            Quantity<ElectricCharge> q2,
            Quantity<Length> r) {
        Real epsilon0 = PhysicalConstants.ELECTRIC_CONSTANT.getValue();
        Real k = Real.ONE.divide(Real.of(4).multiply(Real.PI).multiply(epsilon0));

        Real q1Val = q1.getValue();
        Real q2Val = q2.getValue();
        Real rVal = r.getValue();

        Real force = k.multiply(q1Val).multiply(q2Val).divide(rVal.multiply(rVal));
        return Quantities.create(force, Units.NEWTON).asType(Force.class);
    }

    /**
     * Electric field: E = F/q
     */
    public static Quantity<?> electricField(
            Quantity<Force> force,
            Quantity<ElectricCharge> charge) {
        return force.divide(charge);
    }

    /**
     * Ohm's law: V = IR
     */
    public static Quantity<ElectricPotential> voltage(
            Quantity<ElectricCurrent> current,
            Quantity<ElectricResistance> resistance) {
        return current.multiply(resistance).asType(ElectricPotential.class);
    }

    /**
     * Magnetic force on moving charge: F = qvB (simplified, perpendicular case)
     */
    public static Quantity<Force> lorentzForce(
            Quantity<ElectricCharge> charge,
            Quantity<Velocity> velocity,
            Quantity<MagneticFluxDensity> B) {
        return charge.multiply(velocity).multiply(B).asType(Force.class);
    }

    /**
     * Capacitor energy: E = (1/2)CVÃ‚Â²
     */
    public static Quantity<Energy> capacitorEnergy(
            Quantity<ElectricCapacitance> capacitance,
            Quantity<ElectricPotential> voltage) {
        Real half = Real.of(0.5);
        return capacitance.multiply(voltage).multiply(voltage).multiply(half).asType(Energy.class);
    }
}


