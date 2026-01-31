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

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Analytical tool for designing and verifying ventilation systems in 
 * architectural spaces. It covers both mechanical ventilation requirements 
 * (based on occupancy) and natural stack effect flow.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class VentilationCalculator {

    private VentilationCalculator() {}

    /**
     * Calculates the required Air Changes per Hour (ACH) for a room based 
     * on its occupancy load.
     * 
     * @param occupancy number of people expected in the room
     * @param volumePerPerson target fresh air volume per person (e.g., 30 m3/h)
     * @param roomVolume total volume of the room in cubic meters
     * @return the required ACH rate
     */
    public static Real requiredACH(int occupancy, double volumePerPerson, double roomVolume) {
        if (roomVolume <= 0) return Real.ZERO;
        double flowRateNeeded = occupancy * (volumePerPerson > 0 ? volumePerPerson : 30.0);
        return Real.of(flowRateNeeded / roomVolume);
    }

    /**
     * Calculates the volumetric airflow (Q) resulting from the natural 
     * stack effect (thermal buoyancy).
     * 
     * @param area opening area in square meters
     * @param height vertical distance between openings in meters
     * @param tempIn indoor temperature in Celsius
     * @param tempOut outdoor temperature in Celsius
     * @return volumetric flow rate in cubic meters per second (m3/s)
     */
    public static Real stackEffectFlow(double area, double height, double tempIn, double tempOut) {
        if (tempIn <= tempOut) return Real.ZERO;
        
        double g = 9.81;
        double cd = 0.65; // discharge coefficient
        // Convert to absolute temp for denominator
        double tempInK = tempIn + 273.15;
        
        double flow = cd * area * Math.sqrt(2 * g * height * (tempIn - tempOut) / tempInK);
        return Real.of(flow);
    }
}

