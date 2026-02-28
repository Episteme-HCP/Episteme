/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.earth.geophysics;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;

/**
 * Represents Mars' atmosphere.
 * Provides approximations for Martian atmospheric properties.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MarsAtmosphere extends Atmosphere {

    // Mars Constants
    public static final Real P0_MARS = Real.of(610); // Pa (average)
    public static final Real T0_MARS = Real.of(210); // K (approx mean)
    public static final Real G_MARS = Real.of(3.711); // m/s^2
    public static final Real R_CO2 = Real.of(188.92); // J/(kg*K)
    public static final Real LAPSE_RATE_MARS = Real.of(0.0025); // K/m (approx)

    public MarsAtmosphere() {
        super(Quantities.create(11, Units.KILOMETER), "Carbon Dioxide 95%, Nitrogen 2.8%, Argon 2%");
        // Scale height is approx 11km
    }

    /**
     * Calculates pressure at a given altitude on Mars.
     * Uses simple exponential model: P = P0 * exp(-h/H) where H ~ 11.1 km
     * Or barometric formula if lapse rate is considered.
     * 
     * @param altitude altitude (m)
     * @return Pressure (Pa)
     */
    public Real getPressure(Real altitude) {
        if (altitude.compareTo(Real.ZERO) < 0) return P0_MARS;
        
        // Scale height H = RT/g
        // H ~ 188.92 * 210 / 3.711 ~ 10690 m ~ 10.7 km
        Real scaleHeight = R_CO2.multiply(T0_MARS).divide(G_MARS);
        
        // P = P0 * exp(-h/H)
        Real exponent = altitude.divide(scaleHeight).negate();
        return P0_MARS.multiply(exponent.exp());
    }

    /**
     * Calculates temperature at a given altitude on Mars.
     * @param altitude altitude (m)
     * @return Temperature (K)
     */
    public Real getTemperature(Real altitude) {
         if (altitude.compareTo(Real.ZERO) < 0) return T0_MARS;
         
         // Simple linear lapse rate model up to a certain height
         Real temp = T0_MARS.subtract(LAPSE_RATE_MARS.multiply(altitude));
         // Floor at some temperature? Mars gets very cold high up, but let's keep it simple.
         return temp;
    }
}
