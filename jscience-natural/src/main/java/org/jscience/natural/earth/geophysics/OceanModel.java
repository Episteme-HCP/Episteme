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

package org.jscience.natural.earth.geophysics;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Physics of Sea Water.
 * Provides calculations for density, pressure, and sound speed in the ocean.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OceanModel {

    /** Standard density of seawater (kg/m^3) at surface */
    public static final Real RHO_SEAWATER = Real.of(1025.0);
    
    /** Gravity (m/s^2) */
    private static final Real G = Real.of(9.80665);

    /**
     * Hydrostatic pressure at depth.
     * P = P_atm + rho * g * h
     * 
     * @param depth depth meters (positive downwards)
     * @return Pressure (Pa)
     */
    public static Real pressure(Real depth) {
        if (depth.compareTo(Real.ZERO) < 0) depth = Real.ZERO;
        return Atmosphere.P0.add(RHO_SEAWATER.multiply(G).multiply(depth));
    }
    
    /**
     * Speed of sound in seawater (Chen-Millero-Li approximation, simplified).
     * C ~ 1449.2 + 4.6T - 0.055T^2 + ... + 1.39(S - 35) + 0.016z
     * 
     * @param tempCelsius Temperature in Celsius
     * @param salinityPPT Salinity in parts per thousand (e.g. 35)
     * @param depthMeters Depth in meters
     * @return Speed of sound (m/s)
     */
    public static Real speedOfSound(Real tempCelsius, Real salinityPPT, Real depthMeters) {
        // Simplified formula
        // C = 1449.2 + 4.6T - 0.055T^2 + 0.00029T^3 + (1.34 - 0.01T)(S - 35) + 0.016z
        
        Real T = tempCelsius;
        Real S = salinityPPT;
        Real z = depthMeters;
        
        Real term1 = Real.of(1449.2);
        Real term2 = Real.of(4.6).multiply(T);
        Real term3 = Real.of(0.055).multiply(T.pow(2));
        Real term4 = Real.of(0.00029).multiply(T.pow(3));
        
        Real term5a = Real.of(1.34).subtract(Real.of(0.01).multiply(T));
        Real term5b = S.subtract(Real.of(35.0));
        Real term5 = term5a.multiply(term5b);
        
        Real term6 = Real.of(0.016).multiply(z);
        
        return term1.add(term2).subtract(term3).add(term4).add(term5).add(term6);
    }
}
