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

package org.jscience.social.geography;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Utility for assessing flooding risks based on physical and meteorological data.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class FloodRiskModel {

    private FloodRiskModel() {}

    /**
     * Estimates flood probability (0 to 1).
     * 
     * @param elevation the terrain elevation
     * @param rainfallRate the intensity of rainfall
     * @param soilSaturation percentage of water already in soil (0-1)
     * @return risk factor as Real
     */
    public static Real estimateRisk(Quantity<Length> elevation, Quantity<Length> rainfallRate, double soilSaturation) {
        double elevM = elevation.to(Units.METER).getValue().doubleValue();
        double rainM = rainfallRate.to(Units.METER).getValue().doubleValue();
        
        // Simplified physical model: Risk increases with rain and saturation, decreases with elevation
        double risk = (rainM * 1000 * soilSaturation) / (elevM + 1.0);
        return Real.of(Math.min(1.0, risk));
    }
}

