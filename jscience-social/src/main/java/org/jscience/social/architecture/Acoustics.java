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

package org.jscience.social.architecture;

import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Area;
import org.jscience.core.measure.quantity.Time;
import org.jscience.core.measure.quantity.Volume;

/**
 * Analytical tool for architectural acoustics calculations. Implementation of 
 * classical acoustic formulas, including Sabine's reverberation equation, supporting 
 * room acoustics design and sound quality evaluation.
 *
 * <p>Reference: Sabine, W. C. (1922). Collected Papers on Acoustics.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class Acoustics {

    private Acoustics() {}

    /**
     * Calculates the reverberation time (RT60) using Sabine's formula.
     * Formula: RT60 = 0.161 * V / A (metric units)
     * 
     * @param volume the total air volume of the room (Volume)
     * @param totalAbsorption the total absorption in Sabins, which represents 
     *      the sum of (area * absorptionCoefficient) for all surfaces (Area)
     * @return the estimated time in seconds for the sound level to decay by 60 dB
     * @throws IllegalArgumentException if totalAbsorption is zero or negative
     */
    public static Quantity<Time> calculateSabineRT60(Quantity<Volume> volume, Quantity<Area> totalAbsorption) {
        double v = volume.to(Units.CUBIC_METER).getValue().doubleValue();
        double a = totalAbsorption.to(Units.SQUARE_METER).getValue().doubleValue();
        
        if (a <= 0) {
            return Quantities.create(0, Units.SECOND);
        }
        
        double rt60 = 0.161 * v / a;
        return Quantities.create(rt60, Units.SECOND);
    }

    /**
     * Calculates the effective absorption of a specific architectural surface.
     * Formula: A = S * alpha
     * 
     * @param area the physical surface area (Area)
     * @param absorptionCoefficient the sound absorption coefficient (0.0 to 1.0)
     * @return the effective absorption area in Sabins (m2)
     */
    public static Quantity<Area> calculateAbsorption(Quantity<Area> area, double absorptionCoefficient) {
        return area.multiply(Math.max(0, Math.min(1.0, absorptionCoefficient))).asType(Area.class);
    }
}

