package org.jscience.physics.classical.thermodynamics;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Pressure;
import org.jscience.measure.quantity.Volume;
import org.jscience.measure.quantity.Energy;

/**
 * Interface representing the thermodynamic state of a system.
 * <p>
 * Defines standard state variables with type-safe Quantity types:
 * <ul>
 * <li>$T$: Temperature (K)</li>
 * <li>$P$: Pressure (Pa)</li>
 * <li>$V$: Volume (m³)</li>
 * <li>$S$: Entropy (J/K)</li>
 * </ul>
 * Also provides an Equation of State (EOS) mechanism.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public interface ThermodynamicState {

    /**
     * Returns the temperature of this state.
     * 
     * @return temperature in standard units (Kelvin)
     */
    Quantity<Temperature> getTemperature();

    /**
     * Returns the pressure of this state.
     * 
     * @return pressure in standard units (Pascal)
     */
    Quantity<Pressure> getPressure();

    /**
     * Returns the volume of this state.
     * 
     * @return volume in standard units (cubic meters)
     */
    Quantity<Volume> getVolume();

    /**
     * Returns the entropy of this state.
     * 
     * @return entropy (J/K) - uses Energy type as placeholder for entropy
     */
    Quantity<Energy> getEntropy();

    /**
     * Internal energy ($U$).
     * 
     * @return internal energy in standard units (Joules)
     */
    Quantity<Energy> getInternalEnergy();

    /**
     * Enthalpy ($H = U + PV$).
     * 
     * @return enthalpy in standard units (Joules)
     */
    Quantity<Energy> getEnthalpy();
}
