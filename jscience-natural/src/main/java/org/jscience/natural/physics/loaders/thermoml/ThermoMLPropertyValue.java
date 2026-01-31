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

package org.jscience.natural.physics.loaders.thermoml;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;

import java.io.Serializable;

/**
 * Represents a thermodynamic property value from ThermoML data.
 * <p>
 * This class bridges the raw ThermoML XML data with the JScience Units and Quantities
 * ontology, providing type-safe physical magnitudes and uncertainties.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThermoMLPropertyValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private String propertyName;
    private Quantity<?> magnitude;
    private Quantity<?> uncertainty;
    
    // Conditions
    private Quantity<?> temperature;
    private Quantity<?> pressure;
    private Real moleFraction;
    
    private int compoundIndex;

    public ThermoMLPropertyValue() {
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Quantity<?> getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Quantity<?> magnitude) {
        this.magnitude = magnitude;
    }

    public Quantity<?> getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(Quantity<?> uncertainty) {
        this.uncertainty = uncertainty;
    }

    public Quantity<?> getTemperature() {
        return temperature;
    }

    public void setTemperature(Quantity<?> temperature) {
        this.temperature = temperature;
    }

    public Quantity<?> getPressure() {
        return pressure;
    }

    public void setPressure(Quantity<?> pressure) {
        this.pressure = pressure;
    }

    public Real getMoleFraction() {
        return moleFraction;
    }

    public void setMoleFraction(Real moleFraction) {
        this.moleFraction = moleFraction;
    }

    public int getCompoundIndex() {
        return compoundIndex;
    }

    public void setCompoundIndex(int compoundIndex) {
        this.compoundIndex = compoundIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(propertyName).append(": ").append(magnitude);
        if (uncertainty != null) {
            sb.append(" Â± ").append(uncertainty.getValue());
        }
        if (temperature != null || pressure != null) {
            sb.append(" [");
            if (temperature != null) sb.append("T=").append(temperature);
            if (pressure != null) {
                if (temperature != null) sb.append(", ");
                sb.append("P=").append(pressure);
            }
            sb.append("]");
        }
        return sb.toString();
    }
}

