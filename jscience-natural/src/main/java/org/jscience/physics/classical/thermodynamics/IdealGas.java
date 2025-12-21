/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Pressure;
import org.jscience.measure.quantity.Volume;
import org.jscience.measure.quantity.Energy;

/**
 * Represents an ideal gas following the law PV = nRT.
 * <p>
 * Uses type-safe Quantity classes and Real arithmetic for all physical
 * properties.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class IdealGas implements ThermodynamicState {

    /** Gas constant R = 8.314462618 J/(mol·K) */
    private static final Real R = Real.of(8.314462618);
    /** Degrees of freedom for monatomic gas */
    private static final Real DEGREES_OF_FREEDOM = Real.of(3);
    /** Half factor */
    private static final Real HALF = Real.ONE.divide(Real.TWO);

    private final Real amountOfSubstance; // n (moles)

    private Quantity<Temperature> temperature;
    private Quantity<Pressure> pressure;
    private Quantity<Volume> volume;

    /**
     * Creates an ideal gas with specified moles, temperature, and volume.
     * Pressure is calculated using PV = nRT.
     *
     * @param moles       amount of substance in moles
     * @param temperature initial temperature
     * @param volume      initial volume
     */
    public IdealGas(Real moles, Quantity<Temperature> temperature, Quantity<Volume> volume) {
        this.amountOfSubstance = moles;
        this.temperature = temperature;
        this.volume = volume;
        calculatePressure();
    }

    /**
     * Creates an ideal gas using Real values directly.
     */
    public IdealGas(Real moles, Real temperatureK, Real volumeM3) {
        this(moles,
                Quantities.create(temperatureK, Units.KELVIN),
                Quantities.create(volumeM3, Units.CUBIC_METER));
    }

    private void calculatePressure() {
        // P = nRT / V
        Real T = temperature.getValue(Units.KELVIN);
        Real V = volume.getValue(Units.CUBIC_METER);
        Real P = amountOfSubstance.multiply(R).multiply(T).divide(V);
        this.pressure = Quantities.create(P, Units.PASCAL);
    }

    private void calculateVolume() {
        // V = nRT / P
        Real T = temperature.getValue(Units.KELVIN);
        Real P = pressure.getValue(Units.PASCAL);
        Real V = amountOfSubstance.multiply(R).multiply(T).divide(P);
        this.volume = Quantities.create(V, Units.CUBIC_METER);
    }

    // --- State Accessors ---

    @Override
    public Quantity<Temperature> getTemperature() {
        return temperature;
    }

    @Override
    public Quantity<Pressure> getPressure() {
        return pressure;
    }

    @Override
    public Quantity<Volume> getVolume() {
        return volume;
    }

    @Override
    public Quantity<Energy> getInternalEnergy() {
        // U = (f/2) nRT
        Real T = temperature.getValue(Units.KELVIN);
        Real U = DEGREES_OF_FREEDOM.multiply(HALF).multiply(amountOfSubstance).multiply(R).multiply(T);
        return Quantities.create(U, Units.JOULE);
    }

    @Override
    public Quantity<org.jscience.measure.quantity.Entropy> getEntropy() {
        // S = nR ln(V) (simplified relative entropy)
        Real V = volume.getValue(Units.CUBIC_METER);
        Real S = amountOfSubstance.multiply(R).multiply(V.log());
        @SuppressWarnings("unchecked")
        org.jscience.measure.Unit<org.jscience.measure.quantity.Entropy> entropyUnit = (org.jscience.measure.Unit<org.jscience.measure.quantity.Entropy>) Units.JOULE
                .divide(Units.KELVIN);
        return Quantities.create(S, entropyUnit);
    }

    @Override
    public Quantity<Energy> getEnthalpy() {
        // H = U + PV
        Real U = getInternalEnergy().getValue(Units.JOULE);
        Real P = pressure.getValue(Units.PASCAL);
        Real V = volume.getValue(Units.CUBIC_METER);
        Real H = U.add(P.multiply(V));
        return Quantities.create(H, Units.JOULE);
    }

    // --- Mutators (Iso-process simulation) ---

    /**
     * Isochoric heating/cooling (Volume constant).
     *
     * @param newT new temperature
     */
    public void setTemperatureIsochoric(Quantity<Temperature> newT) {
        this.temperature = newT;
        calculatePressure();
    }

    /**
     * Isobaric expansion/compression (Pressure constant).
     *
     * @param newT new temperature
     */
    public void setTemperatureIsobaric(Quantity<Temperature> newT) {
        this.temperature = newT;
        calculateVolume();
    }

    /**
     * Sets temperature (isochoric) using Real.
     */
    public void setTemperatureIsochoric(Real newT) {
        setTemperatureIsochoric(Quantities.create(newT, Units.KELVIN));
    }

    /**
     * Sets temperature (isobaric) using Real.
     */
    public void setTemperatureIsobaric(Real newT) {
        setTemperatureIsobaric(Quantities.create(newT, Units.KELVIN));
    }

    /**
     * Returns amount of substance in moles.
     */
    public Real getAmountOfSubstance() {
        return amountOfSubstance;
    }
}
