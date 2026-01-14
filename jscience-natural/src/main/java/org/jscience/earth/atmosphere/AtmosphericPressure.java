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

package org.jscience.earth.atmosphere;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Pressure;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AtmosphericPressure {

    // Sea level standard values
    public static final double P0 = 101325.0; // Pa
    public static final double T0 = 288.15; // K (15Ã‚Â°C)
    public static final double L = 0.0065; // Temperature lapse rate K/m
    public static final double g = 9.80665; // m/sÃ‚Â²
    public static final double M = 0.0289644; // Molar mass of air kg/mol
    public static final double R = 8.31447; // Universal gas constant J/(molÃ‚Â·K)

    private AtmosphericPressure() {
    }

    /**
     * Barometric formula for pressure at altitude.
     * P = P0 * (1 - L*h/T0)^(g*M/(R*L))
     * 
     * @param altitude Height above sea level
     * @return Pressure at altitude
     */
    public static Quantity<Pressure> pressureAtAltitude(Quantity<Length> altitude) {
        double h = altitude.to(Units.METER).getValue().doubleValue();

        if (h < 0 || h > 11000) {
            // Troposphere only (0-11km)
            // For higher altitudes, different formula needed
        }

        double exponent = g * M / (R * L);
        double pressure = P0 * Math.pow(1 - L * h / T0, exponent);

        return Quantities.create(pressure, Units.PASCAL);
    }

    /**
     * Calculates altitude from pressure (inverse barometric).
     */
    public static Quantity<Length> altitudeFromPressure(Quantity<Pressure> pressure) {
        double P = pressure.to(Units.PASCAL).getValue().doubleValue();
        double exponent = R * L / (g * M);
        double altitude = T0 / L * (1 - Math.pow(P / P0, exponent));
        return Quantities.create(altitude, Units.METER);
    }

    /**
     * Temperature at altitude (troposphere).
     * T = T0 - L * h
     */
    public static double temperatureAtAltitude(double altitudeMeters) {
        return T0 - L * altitudeMeters;
    }
}


