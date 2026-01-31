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

package org.jscience.natural.physics.relativity;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Mass;
import org.jscience.core.measure.quantity.Energy;
import org.jscience.core.measure.quantity.Velocity;
import org.jscience.natural.physics.PhysicalConstants;

/**
 * Provides static methods for Relativistic Mechanics calculations.
 * <p>
 * Supports helper methods for Lorentz factor, Relativistic Energy, etc.
 * Uses Real arithmetic for all calculations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RelativisticMechanics {

    private RelativisticMechanics() {
    }

    /**
     * Calculates the Lorentz factor (gamma) given a velocity v.
     * gamma = 1 / sqrt(1 - vÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â²/cÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â²)
     *
     * @param v velocity
     * @return Lorentz factor as Real (dimensionless)
     */
    public static Real lorentzFactor(Quantity<Velocity> v) {
        Real vVal = v.to(Units.METER_PER_SECOND).getValue();
        Real c = PhysicalConstants.SPEED_OF_LIGHT.to(Units.METER_PER_SECOND).getValue();

        if (vVal.compareTo(c) >= 0) {
            throw new IllegalArgumentException("Velocity cannot equal or exceed speed of light (c)");
        }

        Real betaSq = vVal.multiply(vVal).divide(c.multiply(c));
        return Real.ONE.divide(Real.ONE.subtract(betaSq).sqrt());
    }

    /**
     * Calculates Relativistic Mass.
     * m = gamma * rest_mass
     *
     * @param restMass rest mass
     * @param v        velocity
     * @return relativistic mass
     */
    public static Quantity<Mass> relativisticMass(Quantity<Mass> restMass, Quantity<Velocity> v) {
        Real gamma = lorentzFactor(v);
        return restMass.multiply(gamma);
    }

    /**
     * Calculates Total Relativistic Energy.
     * E = gamma * mÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ * cÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â²
     *
     * @param restMass rest mass
     * @param v        velocity
     * @return Total Energy
     */
    public static Quantity<Energy> totalEnergy(Quantity<Mass> restMass, Quantity<Velocity> v) {
        Real gamma = lorentzFactor(v);
        Real m0 = restMass.to(Units.KILOGRAM).getValue();
        Real c = PhysicalConstants.SPEED_OF_LIGHT.to(Units.METER_PER_SECOND).getValue();
        Real cSquared = c.multiply(c);

        // E = gamma * m0 * cÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â²
        Real energyVal = gamma.multiply(m0).multiply(cSquared);
        return Quantities.create(energyVal, Units.JOULE);
    }

    /**
     * Calculates Kinetic Energy.
     * KE = (gamma - 1) * mÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ * cÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â²
     *
     * @param restMass rest mass
     * @param v        velocity
     * @return Kinetic Energy
     */
    public static Quantity<Energy> kineticEnergy(Quantity<Mass> restMass, Quantity<Velocity> v) {
        Real gamma = lorentzFactor(v);
        Real m0 = restMass.to(Units.KILOGRAM).getValue();
        Real c = PhysicalConstants.SPEED_OF_LIGHT.to(Units.METER_PER_SECOND).getValue();
        Real cSquared = c.multiply(c);

        Real keVal = gamma.subtract(Real.ONE).multiply(m0).multiply(cSquared);
        return Quantities.create(keVal, Units.JOULE);
    }

    /**
     * Rest energy: EÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ = mÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬cÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â²
     *
     * @param restMass rest mass
     * @return rest energy
     */
    public static Quantity<Energy> restEnergy(Quantity<Mass> restMass) {
        Real m0 = restMass.to(Units.KILOGRAM).getValue();
        Real c = PhysicalConstants.SPEED_OF_LIGHT.to(Units.METER_PER_SECOND).getValue();
        Real cSquared = c.multiply(c);
        return Quantities.create(m0.multiply(cSquared), Units.JOULE);
    }

    /**
     * Relativistic momentum: p = gamma * mÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ * v
     *
     * @param restMass rest mass
     * @param v        velocity
     * @return momentum (kgÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â·m/s)
     */
    public static Real relativisticMomentum(Quantity<Mass> restMass, Quantity<Velocity> v) {
        Real gamma = lorentzFactor(v);
        Real m0 = restMass.to(Units.KILOGRAM).getValue();
        Real vVal = v.to(Units.METER_PER_SECOND).getValue();
        return gamma.multiply(m0).multiply(vVal);
    }
}




