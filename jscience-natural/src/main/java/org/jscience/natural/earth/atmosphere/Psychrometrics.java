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

package org.jscience.natural.earth.atmosphere;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Temperature;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.Quantities;

/**
 * Calculators for humid air properties (Psychrometrics).
 */
public final class Psychrometrics {

    private Psychrometrics() {}

    /**
     * Calculates Saturation Vapor Pressure (hPa) using Magnus-Tetens formula.
     */
    public static Real saturationVaporPressure(Quantity<Temperature> temperature) {
        double t = temperature.to(Units.CELSIUS).getValue().doubleValue();
        double es = 6.112 * Math.exp((17.67 * t) / (t + 243.5));
        return Real.of(es);
    }

    /**
     * Calculates Dew Point temperature.
     */
    public static Quantity<Temperature> dewPoint(Quantity<Temperature> temperature, Real relativeHumidity) {
        double t = temperature.to(Units.CELSIUS).getValue().doubleValue();
        double rh = relativeHumidity.doubleValue();
        double gamma = (17.67 * t) / (t + 243.5) + Math.log(rh / 100.0);
        double dp = (243.5 * gamma) / (17.67 - gamma);
        return Quantities.create(dp, Units.CELSIUS);
    }

    /**
     * Calculates Relative Humidity.
     */
    public static Real relativeHumidity(Quantity<Temperature> temperature, Quantity<Temperature> dewPoint) {
        Real es = saturationVaporPressure(temperature);
        Real e = saturationVaporPressure(dewPoint);
        return e.divide(es).multiply(Real.of(100.0));
    }

    /**
     * Calculates Enthalpy of moist air (kJ/kg).
     * h = cpa*t + w*(hwe + cpw*t)
     */
    public static Real enthalpy(Quantity<Temperature> temperature, Real humidityRatio) {
        double t = temperature.to(Units.CELSIUS).getValue().doubleValue();
        double w = humidityRatio.doubleValue();
        double h = 1.006 * t + w * (2501 + 1.86 * t);
        return Real.of(h);
    }
}

