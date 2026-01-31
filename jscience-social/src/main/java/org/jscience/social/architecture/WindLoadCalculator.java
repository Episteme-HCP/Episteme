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
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Length;

/**
 * Analytical tool for calculating wind loads on architectural structures in 
 * accordance with Eurocode 1 (EN 1991-1-4). It accounts for terrain roughness, 
 * orography, turbulence, and structural coefficients.
 */
public final class WindLoadCalculator {

    private WindLoadCalculator() {}

    /**
     * Categories of terrain roughness for wind velocity profiles.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum TerrainCategory {
        /** Open sea or coastal area exposed to the open sea. */
        SEA_COAST(0.003, 1.0),
        /** Lakes or flat area with negligible vegetation and without obstacles. */
        OPEN_COUNTRY(0.01, 1.0),
        /** Area with low vegetation such as grass and isolated obstacles. */
        SUBURBAN(0.05, 2.0),
        /** Area with regular cover of vegetation or buildings. */
        URBAN(0.3, 5.0),
        /** Area in which at least 15% of the surface is covered with buildings (h > 15m). */
        CITY_CENTER(1.0, 10.0);

        private final double z0; // Roughness length (m)
        private final double zMin; // Minimum height (m)

        TerrainCategory(double z0, double zMin) {
            this.z0 = z0;
            this.zMin = zMin;
        }

        public double getRoughnessLength() { return z0; }
        public double getMinimumHeight() { return zMin; }
    }

    /**
     * Calculates the basic wind velocity (Vb) based on regional fundamental velocity 
     * and local modification factors.
     * Formula: Vb = Cdir * Cseason * Vb_0
     * 
     * @param vb0 fundamental basic wind velocity (m/s)
     * @param directionalFactor directional modification factor (Cdir)
     * @param seasonFactor seasonal modification factor (Cseason)
     * @return basic wind velocity as a Real
     */
    public static Real basicWindVelocity(double vb0, double directionalFactor, double seasonFactor) {
        return Real.of(directionalFactor * seasonFactor * vb0);
    }

    /**
     * Calculates the mean wind velocity (Vm) at a specific height (z) above ground.
     * Formula: Vm(z) = Cr(z) * Co(z) * Vb
     * 
     * @param basicVelocity (Vb) the basic wind velocity
     * @param height (z) the height above ground
     * @param terrain the terrain roughness category
     * @param orographyFactor orography factor (Co) (usually 1.0 for flat terrain)
     * @return mean wind velocity (m/s)
     */
    public static Real meanWindVelocity(Real basicVelocity, Quantity<Length> height, 
            TerrainCategory terrain, double orographyFactor) {
        
        double z = height.to(Units.METER).getValue().doubleValue();
        double zEff = Math.max(z, terrain.getMinimumHeight());
        double z0 = terrain.getRoughnessLength();
        
        // Roughness factor Cr(z) = Kr * ln(z/z0)
        double kr = 0.19 * Math.pow(z0 / 0.05, 0.07);
        double cr = kr * Math.log(zEff / z0);
        
        double vm = cr * orographyFactor * basicVelocity.doubleValue();
        return Real.of(vm);
    }

    /**
     * Calculates the peak velocity pressure (Qp) at a given height, accounting 
     * for mean velocity and turbulence.
     * Formula: Qp(z) = [1 + 7*Iv(z)] * 0.5 * rho * Vm^2(z)
     * 
     * @param meanVelocity (Vm) the mean wind velocity at height z
     * @param height (z) the height above ground
     * @param terrain the terrain category
     * @param airDensity the density of air (typical 1.25 kg/m3)
     * @return peak pressure in Pascals (N/m2)
     */
    public static Real peakVelocityPressure(Real meanVelocity, Quantity<Length> height,
            TerrainCategory terrain, double airDensity) {
        
        double z = height.to(Units.METER).getValue().doubleValue();
        double zEff = Math.max(z, terrain.getMinimumHeight());
        double z0 = terrain.getRoughnessLength();
        
        // Turbulence intensity Iv(z)
        double kl = 1.0; 
        double iv = kl / (Math.log(zEff / z0));
        
        double vm = meanVelocity.doubleValue();
        double qp = (1 + 7 * iv) * 0.5 * airDensity * vm * vm;
        
        return Real.of(qp);
    }

    /**
     * Calculates the total wind force (Fw) acting on a surface area.
     * Formula: Fw = Cs * Cd * Cf * Qp * Aref
     * 
     * @param peakPressure (Qp) the calculated peak pressure at the reference height
     * @param referenceArea (Aref) the total wind-exposed area in square meters
     * @param structuralFactor (Cs * Cd) structural dynamic factor (typically 1.0)
     * @param forceCoefficient (Cf) external force coefficient or pressure coefficient
     * @return total wind force in Newtons (N)
     */
    public static Real windForce(Real peakPressure, double referenceArea,
            double structuralFactor, double forceCoefficient) {
        
        double qp = peakPressure.doubleValue();
        double force = structuralFactor * forceCoefficient * qp * referenceArea;
        return Real.of(force);
    }

    /**
     * Retrieves standard external force coefficients (Cf) for various building shapes.
     * 
     * @param shape the geometric shape (e.g., "rectangular", "circular")
     * @param aspectRatio ratio of height to width
     * @return the shape's force coefficient
     */
    public static double getForceCoefficient(String shape, double aspectRatio) {
        return switch (shape.toLowerCase()) {
            case "rectangular" -> 1.0 + 0.02 * Math.min(aspectRatio, 10);
            case "circular" -> 0.8; 
            case "square" -> 2.0;
            case "triangular" -> 1.2;
            default -> 1.0;
        };
    }

    /**
     * Determines the legislative reference height for wind pressure calculations 
     * based on building proportions.
     * 
     * @param buildingHeight total height of the building
     * @param buildingWidth total width of the building
     * @param windward whether the point is on the windward side
     * @return reference height in meters
     */
    public static Real referenceHeight(Quantity<Length> buildingHeight, 
            Quantity<Length> buildingWidth, boolean windward) {
        
        double h = buildingHeight.to(Units.METER).getValue().doubleValue();
        double b = buildingWidth.to(Units.METER).getValue().doubleValue();
        
        if (h <= b) {
            return Real.of(h);
        } else if (h <= 2 * b) {
            return windward ? Real.of(h) : Real.of(b);
        } else {
            return Real.of(h); 
        }
    }
}

