package org.jscience.physics.relativity;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.quantity.Velocity;
import org.jscience.physics.PhysicalConstants;

/**
 * Provides static methods for Relativistic Mechanics calculations.
 * Supports helper methods for Lorentz factor, Relativistic Energy, etc.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class RelativisticMechanics {

    private RelativisticMechanics() {
    }

    /**
     * Calculates the Lorentz factor (gamma) given a velocity v.
     * gamma = 1 / sqrt(1 - v^2/c^2)
     * 
     * @param v velocity
     * @return Lorentz factor (dimensionless)
     */
    public static double lorentzFactor(Quantity<Velocity> v) {
        double vVal = v.to(Units.METER_PER_SECOND).getValue().doubleValue();
        double c = PhysicalConstants.SPEED_OF_LIGHT.to(Units.METER_PER_SECOND).getValue().doubleValue();

        if (vVal >= c) {
            throw new IllegalArgumentException("Velocity cannot equal or exceed speed of light (c)");
        }

        return 1.0 / Math.sqrt(1.0 - (vVal * vVal) / (c * c));
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
        double gamma = lorentzFactor(v);
        return restMass.multiply(gamma);
    }

    /**
     * Calculates Total Relativistic Energy.
     * E = m * c^2 (where m is relativistic mass)
     * Or E = gamma * m0 * c^2
     * 
     * @param restMass rest mass
     * @param v        velocity
     * @return Total Energy
     */
    public static Quantity<Energy> totalEnergy(Quantity<Mass> restMass, Quantity<Velocity> v) {
        Quantity<Mass> mRel = relativisticMass(restMass, v);
        double c = PhysicalConstants.SPEED_OF_LIGHT.to(Units.METER_PER_SECOND).getValue().doubleValue();

        // E = m * c^2
        // kg * (m/s)^2 = J
        double energyVal = mRel.to(Units.KILOGRAM).getValue().doubleValue() * c * c;
        return Quantities.create(energyVal, Units.JOULE);
    }

    /**
     * Calculates Kinetic Energy.
     * KE = (gamma - 1) * m0 * c^2
     * 
     * @param restMass rest mass
     * @param v        velocity
     * @return Kinetic Energy
     */
    public static Quantity<Energy> kineticEnergy(Quantity<Mass> restMass, Quantity<Velocity> v) {
        double gamma = lorentzFactor(v);
        double c = PhysicalConstants.SPEED_OF_LIGHT.to(Units.METER_PER_SECOND).getValue().doubleValue();
        double m0 = restMass.to(Units.KILOGRAM).getValue().doubleValue();

        double keVal = (gamma - 1.0) * m0 * c * c;
        return Quantities.create(keVal, Units.JOULE);
    }
}
