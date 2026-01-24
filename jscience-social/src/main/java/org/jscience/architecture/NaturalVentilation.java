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

package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models passive natural ventilation and airflow in buildings. It provides 
 * calculations for the stack effect (thermal buoyancy) and wind-driven 
 * ventilation, supporting sustainable building design.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class NaturalVentilation {

    private NaturalVentilation() {}

    /**
     * Calculates the Stack Effect airflow (Q) caused by thermal buoyancy.
     * Formula: Q = Cd * A * sqrt(2 * g * H * (Ti - To) / Ti)
     * 
     * @param cd discharge coefficient (typical values around 0.65)
     * @param area opening area in square meters
     * @param height vertical distance between the neutral pressure level or openings in meters
     * @param tempInside average indoor temperature in Kelvin
     * @param tempOutside outdoor temperature in Kelvin
     * @return volumetric flow rate in cubic meters per second (m3/s)
     */
    public static Real stackEffectFlow(double cd, double area, double height, 
            double tempInside, double tempOutside) {
        if (tempInside <= tempOutside || tempInside <= 0) return Real.ZERO;
        
        double g = 9.81;
        double flow = cd * area * Math.sqrt(2 * g * height * (tempInside - tempOutside) / tempInside);
        return Real.of(flow);
    }

    /**
     * Calculates Wind-Driven ventilation flow (Q) based on pressure differences.
     * Formula: Q = Cv * A * v
     * 
     * @param cv effectiveness factor (0.5-0.6 for perpendicular wind, 0.25-0.35 for diagonal)
     * @param area area of the opening in square meters
     * @param windSpeed external wind speed in meters per second (m/s)
     * @return estimated volumetric flow rate (m3/s)
     */
    public static Real windDrivenFlow(double cv, double area, double windSpeed) {
        return Real.of(cv * area * windSpeed);
    }

    /**
     * Estimates the Air Changes per Hour (ACH) for a building volume given a total flow rate.
     * Formula: ACH = (Q * 3600) / Volume
     * 
     * @param totalFlowRate total volumetric flow rate (m3/s)
     * @param buildingVolume interior volume of the building in cubic meters
     * @return the number of air changes per hour
     */
    public static Real calculateACH(double totalFlowRate, double buildingVolume) {
        if (buildingVolume <= 0) return Real.ZERO;
        return Real.of((totalFlowRate * 3600) / buildingVolume);
    }

    /**
     * Determines if Cross Ventilation is structurally possible based on 
     * building depth and window-to-wall ratios.
     * 
     * @param buildingDepth distance between windward and leeward facades
     * @param windowRatio ratio of openable window area to facade area
     * @return true if Cross Ventilation is likely feasible
     */
    public static boolean isCrossVentilationPossible(double buildingDepth, double windowRatio) {
        // Rule of thumb: cross ventilation is effective up to approx 5x room height or ~12m depth
        return buildingDepth < 12.0 && windowRatio > 0.05;
    }
}
