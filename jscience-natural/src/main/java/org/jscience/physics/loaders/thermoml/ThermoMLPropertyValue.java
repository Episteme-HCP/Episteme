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

package org.jscience.physics.loaders.thermoml;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a thermodynamic property value from ThermoML data.
 * <p>
 * Contains the property name, numerical value, uncertainty, and associated
 * measurement conditions (temperature, pressure, composition, etc.).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThermoMLPropertyValue {

    private String propertyName;
    private Real value;
    private Real uncertainty;
    private String unitString;
    private Real temperature;
    private Real pressure;
    private Real moleFraction;
    private int compoundIndex;

    /**
     * Creates a new property value.
     */
    public ThermoMLPropertyValue() {
    }

    /**
     * Creates a property value with name and value.
     */
    public ThermoMLPropertyValue(String propertyName, Real value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Real getValue() {
        return value;
    }

    public void setValue(Real value) {
        this.value = value;
    }

    public Real getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(Real uncertainty) {
        this.uncertainty = uncertainty;
    }

    public String getUnitString() {
        return unitString;
    }

    public void setUnitString(String unitString) {
        this.unitString = unitString;
    }

    public Real getTemperature() {
        return temperature;
    }

    public void setTemperature(Real temperature) {
        this.temperature = temperature;
    }

    public Real getPressure() {
        return pressure;
    }

    public void setPressure(Real pressure) {
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

    /**
     * Returns the value as a double for use in calculations.
     */
    public double getDoubleValue() {
        return value != null ? value.doubleValue() : Double.NaN;
    }

    /**
     * Returns the relative uncertainty as a fraction.
     */
    public Real getRelativeUncertainty() {
        if (value == null || uncertainty == null || value.isZero()) {
            return null;
        }
        return uncertainty.divide(value.abs());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ThermoMLPropertyValue{");
        sb.append("property='").append(propertyName).append('\'');
        sb.append(", value=").append(value);
        if (uncertainty != null) {
            sb.append(" ± ").append(uncertainty);
        }
        if (unitString != null) {
            sb.append(' ').append(unitString);
        }
        if (temperature != null) {
            sb.append(", T=").append(temperature).append(" K");
        }
        if (pressure != null) {
            sb.append(", P=").append(pressure).append(" Pa");
        }
        sb.append('}');
        return sb.toString();
    }
}
