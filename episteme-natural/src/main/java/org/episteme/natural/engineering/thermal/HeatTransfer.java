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

package org.episteme.natural.engineering.thermal;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Area;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.measure.quantity.Power;
import org.episteme.core.measure.quantity.Temperature;
import org.episteme.natural.physics.PhysicalConstants;

/**
 * Fundamental heat transfer calculations.
 * Modernized to use high-precision Real and typed Quantities.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HeatTransfer {

    private HeatTransfer() {
    }

    /**
     * Fourier's Law for heat conduction.
     * Q = k * A * ÃŽâ€T / d
     * 
     * @param thermalConductivity   k (W/(mÃ‚Â·K))
     * @param area                  Cross-sectional area
     * @param temperatureDifference ÃŽâ€T
     * @param thickness             d
     * @return Heat transfer rate
     */
    public static Quantity<Power> conduction(Real thermalConductivity, Quantity<Area> area,
            Quantity<Temperature> temperatureDifference, Quantity<Length> thickness) {
        
        Real A = area.to(Units.SQUARE_METER).getValue();
        Real dT = temperatureDifference.to(Units.KELVIN).getValue();
        Real d = thickness.to(Units.METER).getValue();
        
        Real Q = thermalConductivity.multiply(A).multiply(dT).divide(d);
        return Quantities.create(Q, Units.WATT);
    }

    /**
     * Newton's Law of Cooling for convection.
     * Q = h * A * ÃŽâ€T
     * 
     * @param convectionCoefficient h (W/(mÃ‚Â²Ã‚Â·K))
     * @param area                  Surface area
     * @param temperatureDifference ÃŽâ€T
     * @return Heat transfer rate
     */
    public static Quantity<Power> convection(Real convectionCoefficient, Quantity<Area> area,
            Quantity<Temperature> temperatureDifference) {
            
        Real A = area.to(Units.SQUARE_METER).getValue();
        Real dT = temperatureDifference.to(Units.KELVIN).getValue();
        
        Real Q = convectionCoefficient.multiply(A).multiply(dT);
        return Quantities.create(Q, Units.WATT);
    }

    /**
     * Stefan-Boltzmann Law for radiation.
     * Q = ÃŽÂµ * ÃÆ’ * A * (TÃ¢ÂÂ´ - T_surrÃ¢ÂÂ´)
     * 
     * @param emissivity      ÃŽÂµ (0-1)
     * @param area            Surface area
     * @param surfaceTemp     T
     * @param surroundingTemp T_surr
     * @return Heat transfer rate
     */
    public static Quantity<Power> radiation(Real emissivity, Quantity<Area> area,
            Quantity<Temperature> surfaceTemp, Quantity<Temperature> surroundingTemp) {
            
        Real A = area.to(Units.SQUARE_METER).getValue();
        Real T1 = surfaceTemp.to(Units.KELVIN).getValue();
        Real T2 = surroundingTemp.to(Units.KELVIN).getValue();
        Real sigma = PhysicalConstants.stefan_boltzmann; 
        
        Real Q = emissivity.multiply(sigma).multiply(A).multiply(T1.pow(4).subtract(T2.pow(4)));
        return Quantities.create(Q, Units.WATT);
    }

    /**
     * Thermal resistance for conduction.
     * R = d / (k * A)
     * 
     * @return Thermal resistance (K/W)
     */
    public static Real thermalResistance(Quantity<Length> thickness, Real thermalConductivity, Quantity<Area> area) {
        Real d = thickness.to(Units.METER).getValue();
        Real A = area.to(Units.SQUARE_METER).getValue();
        
        return d.divide(thermalConductivity.multiply(A));
    }
}


