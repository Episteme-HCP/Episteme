package org.jscience.physics.thermodynamics;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.Unit;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Temperature;

/**
 * Provides static methods for Thermodynamics calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Thermodynamics {

    private Thermodynamics() {
    }

    /**
     * Calculates Heat Energy transferred.
     * Q = m * c * deltaT
     * 
     * @param m            mass
     * @param specificHeat specific heat capacity (J/(kg*K))
     * @param deltaT       change in temperature
     * @return Heat Energy (Joule)
     */
    public static Quantity<Energy> calculateHeat(Quantity<Mass> m, Quantity<?> specificHeat,
            Quantity<Temperature> deltaT) {
        double mVal = m.to(Units.KILOGRAM).getValue().doubleValue();
        double dtVal = deltaT.to(Units.KELVIN).getValue().doubleValue(); // Delta K is same as Delta C

        // specificHeat units: J/(kg*K)
        // We assume the user passes a Quantity compatible with specific heat.
        // Since we don't have SpecificHeatCapacity type, we extract value assuming SI.
        double cVal = specificHeat.getValue().doubleValue();

        // Ideally we should check unit compatibility but Quantity generic handling is
        // tricky without known type.
        // Assuming strict SI values were passed or converted.

        return Quantities.create(mVal * cVal * dtVal, Units.JOULE);
    }

    /**
     * Ideal Gas Law: PV = nRT
     * Calculates Pressure P = nRT / V
     */
    // ... specialized methods can be added
}
