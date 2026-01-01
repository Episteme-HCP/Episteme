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

package org.jscience.physics.classical.thermodynamics;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Pressure;
import org.jscience.measure.quantity.Volume;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.quantity.Entropy;

/**
 * Interface representing the thermodynamic state of a system.
 * <p>
 * Defines standard state variables with type-safe Quantity types:
 * <ul>
 * <li>$T$: Temperature (K)</li>
 * <li>$P$: Pressure (Pa)</li>
 * <li>$V$: Volume (mÃ‚Â³)</li>
 * <li>$S$: Entropy (J/K)</li>
 * </ul>
 * Also provides an Equation of State (EOS) mechanism.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
     * @return entropy (J/K)
     */
    Quantity<Entropy> getEntropy();

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


