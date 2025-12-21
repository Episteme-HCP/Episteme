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
package org.jscience.devices.instruments;

import org.jscience.devices.Sensor;

/**
 * Interface for multimeter devices.
 * <p>
 * A multimeter is a measuring instrument that combines multiple electrical
 * measurement functions (voltage, current, resistance, etc.).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Multimeter extends Sensor {

    /**
     * Measurement functions.
     */
    enum Function {
        DC_VOLTAGE,
        AC_VOLTAGE,
        DC_CURRENT,
        AC_CURRENT,
        RESISTANCE,
        CAPACITANCE,
        FREQUENCY,
        CONTINUITY,
        DIODE_TEST,
        TEMPERATURE
    }

    /**
     * Gets the current measurement function.
     */
    Function getFunction();

    /**
     * Sets the measurement function.
     */
    void setFunction(Function function);

    /**
     * Gets the current range for the selected function.
     */
    double getRange();

    /**
     * Sets the measurement range.
     */
    void setRange(double range);

    /**
     * Enables auto-range mode.
     */
    void setAutoRange(boolean enabled);

    /**
     * Checks if auto-range is enabled.
     */
    boolean isAutoRange();

    /**
     * Reads the current measurement value.
     */
    double read();

    /**
     * Gets the measurement accuracy/resolution.
     */
    double getAccuracy();

    /**
     * Measures DC voltage.
     */
    double measureDCVoltage();

    /**
     * Measures AC voltage.
     */
    double measureACVoltage();

    /**
     * Measures resistance.
     */
    double measureResistance();

    /**
     * Measures capacitance in Farads.
     */
    double measureCapacitance();

    /**
     * Performs continuity test.
     *
     * @return true if circuit is continuous
     */
    boolean testContinuity();
}
