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

package org.jscience.medicine;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;

/**
 * Immutable record representing a snapshot of vital signs.
 * Uses the JScience measure system for physical accuracy.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public record VitalSigns(
        Quantity<Frequency> heartRate,
        Quantity<Pressure> systolic,
        Quantity<Pressure> diastolic,
        Quantity<Dimensionless> spO2,
        Quantity<Frequency> respirationRate,
        Quantity<Temperature> temperature) {

    public VitalSigns(int hr, int sys, int dia, int spo2, int rr, double temp) {
        this(
            Quantities.create(hr, Units.HERTZ.divide(60).asType(Frequency.class)), // BPM to Hz conversion approximate if just storing
            Quantities.create(sys, Units.MILLIMETRE_OF_MERCURY),
            Quantities.create(dia, Units.MILLIMETRE_OF_MERCURY),
            Quantities.create(spo2 / 100.0, Units.ONE),
            Quantities.create(rr, Units.HERTZ.divide(60).asType(Frequency.class)),
            Quantities.create(temp, Units.FAHRENHEIT)); // Monitor usually uses Fahrenheit in US or Celsius. The sim uses 98.6 so Fahrenheit.
    }

    /**
     * Returns blood pressure as a formatted string "systolic/diastolic".
     */
    public String bloodPressureString() {
        return String.format("%d/%d %s", 
            Math.round(systolic.to(Units.MILLIMETRE_OF_MERCURY).getValue().doubleValue()),
            Math.round(diastolic.to(Units.MILLIMETRE_OF_MERCURY).getValue().doubleValue()),
            Units.MILLIMETRE_OF_MERCURY.getSymbol());
    }

    /**
     * Returns temperature formatted to one decimal place.
     */
    public String temperatureString() {
        return String.format("%.1f %s", 
            temperature.to(Units.CELSIUS).getValue().doubleValue(),
            Units.CELSIUS.getSymbol());
    }

    @Override
    public String toString() {
        return String.format("Heart Rate: %s, BP: %s, SpO2: %s, Temp: %s",
                heartRate, bloodPressureString(), spO2, temperatureString());
    }
}


