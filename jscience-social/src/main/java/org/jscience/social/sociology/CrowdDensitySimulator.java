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

package org.jscience.social.sociology;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;

import org.jscience.core.measure.quantity.Length;
import org.jscience.core.measure.quantity.Velocity;

/**
 * Utility class for simulating crowd dynamics and estimating flow rates in evacuation scenarios.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class CrowdDensitySimulator {

    private CrowdDensitySimulator() {}

    /**
     * Calculates the estimated flow rate of people through an exit.
     * Uses a simplified relationship: Flow = density * velocity * width.
     * Velocity is modeled as decreasing linearly with density.
     *
     * @param density crowd density in persons/m^2 as Quantity
     * @param width   width of the exit in meters as Quantity
     * @return the flow rate in persons per second as Quantity
     */
    public static Quantity<?> exitFlow(Quantity<?> density, Quantity<Length> width) {
        // Simplified speed deduction model: v = 1.2 * (1 - 0.25 * density)
        Real dVal = density.getValue();
        Real baseVelocity = Real.of(1.2);
        Real densityFactor = Real.of(0.25);
        
        Real factor = Real.ONE.subtract(densityFactor.multiply(dVal));
        Real vVal = baseVelocity.multiply(factor);

        // Ensure non-negative and non-zero minimum velocity for flow calculation purposes (capping lower bound)
        Real minVelocity = Real.of(0.1);
        if (vVal.compareTo(minVelocity) < 0) {
            vVal = minVelocity;
        }

        Quantity<Velocity> velocity = org.jscience.core.measure.Quantities.create(vVal, Units.METER_PER_SECOND);
        
        // Flow = density * velocity * width
        // [pers/m^2] * [m/s] * [m] = [pers/s]
        return density.multiply(velocity).multiply(width);
    }
}

