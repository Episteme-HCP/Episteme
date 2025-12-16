package org.jscience.physics.classical.thermodynamics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface representing the thermodynamic state of a system.
 * <p>
 * Defines standard state variables:
 * <ul>
 * <li>$T$: Temperature (Kelvin)</li>
 * <li>$P$: Pressure (Pascal)</li>
 * <li>$V$: Volume ($m^3$)</li>
 * <li>$S$: Entropy (Joule/Kelvin)</li>
 * </ul>
 * Also provides an Equation of State (EOS) mechanism.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public interface ThermodynamicState {

    Real getTemperature();

    Real getPressure();

    Real getVolume();

    Real getEntropy();

    /**
     * Internal energy ($U$).
     */
    Real getInternalEnergy();

    /**
     * Enthalpy ($H = U + PV$).
     */
    default Real getEnthalpy() {
        return getInternalEnergy().add(getPressure().multiply(getVolume()));
    }
}
