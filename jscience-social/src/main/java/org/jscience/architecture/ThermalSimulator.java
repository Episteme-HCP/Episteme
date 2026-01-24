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

import java.io.Serializable;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Area;

/**
 * Analytical engine for thermal simulation and building energy analysis.
 * It provides models for calculating U-values (thermal transmittance), 
 * annual heating demand, and thermal mass for building assemblies.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class ThermalSimulator {

    private ThermalSimulator() {}

    /**
     * Intrinsic thermal properties of a construction material.
     */
    public record ThermalMaterial(
        String name,
        double thermalConductivity, // W/(m·K)
        double density,             // kg/m³
        double specificHeat         // J/(kg·K)
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    // Common materials database
    public static final ThermalMaterial CONCRETE = new ThermalMaterial("Concrete", 1.4, 2300, 880);
    public static final ThermalMaterial BRICK = new ThermalMaterial("Brick", 0.84, 1920, 790);
    public static final ThermalMaterial GLASS = new ThermalMaterial("Glass", 1.0, 2500, 840);
    public static final ThermalMaterial WOOD = new ThermalMaterial("Wood (softwood)", 0.13, 500, 1600);
    public static final ThermalMaterial INSULATION_MINERAL = new ThermalMaterial("Mineral Wool", 0.035, 30, 1030);
    public static final ThermalMaterial INSULATION_EPS = new ThermalMaterial("EPS Foam", 0.038, 20, 1450);
    public static final ThermalMaterial AIR_GAP = new ThermalMaterial("Air Gap", 0.025, 1.2, 1005);
    public static final ThermalMaterial PLASTERBOARD = new ThermalMaterial("Plasterboard", 0.25, 900, 1000);

    /**
     * A single layer within a wall or roof assembly.
     */
    public record WallLayer(ThermalMaterial material, double thicknessMeters) implements Serializable {
        private static final long serialVersionUID = 2L;

        /**
         * Calculates the thermal resistance (R-value) for this specific layer.
         * R = thickness / conductivity
         */
        public double thermalResistance() {
            if (material.thermalConductivity() == 0) return 0;
            return thicknessMeters / material.thermalConductivity();
        }
    }

    /**
     * Calculates the U-value (thermal transmittance) of a complete wall assembly.
     * U = 1 / (Rsi + sum(R_layers) + Rse)
     * 
     * @param layers list of wall layers from interior to exterior
     * @param internalSurfaceResistance Rsi (standard value ~0.13)
     * @param externalSurfaceResistance Rse (standard value ~0.04)
     * @return the U-value in W/(m²·K)
     */
    public static Real calculateUValue(List<WallLayer> layers, 
            double internalSurfaceResistance, double externalSurfaceResistance) {
        
        double totalR = internalSurfaceResistance + externalSurfaceResistance;
        for (WallLayer layer : layers) {
            totalR += layer.thermalResistance();
        }
        
        if (totalR == 0) return Real.ZERO;
        return Real.of(1.0 / totalR);
    }

    /**
     * Estimates the annual heating energy demand using the simplified degree-day method.
     * 
     * @param uValue the thermal transmittance of the building envelope
     * @param area total surface area of the envelope
     * @param heatingDegreeDays localized heating degree days (HDD)
     * @return annual heating demand in kilowatt-hours (kWh)
     */
    public static Real annualHeatingDemand(Real uValue, Quantity<Area> area, double heatingDegreeDays) {
        double u = uValue.doubleValue();
        double a = area.to(Units.SQUARE_METER).getValue().doubleValue();
        double demand = u * a * heatingDegreeDays * 24 / 1000;
        return Real.of(demand);
    }

    /**
     * Calculates the thermal mass (heat storage capacity) of a building element.
     * 
     * @param layer the wall layer representing the thermal mass
     * @param area of the element
     * @return heat capacity in Joules per Kelvin (J/K)
     */
    public static Real thermalMass(WallLayer layer, Quantity<Area> area) {
        double a = area.to(Units.SQUARE_METER).getValue().doubleValue();
        double volume = a * layer.thicknessMeters();
        double mass = volume * layer.material().density();
        double capacity = mass * layer.material().specificHeat();
        return Real.of(capacity);
    }

    /**
     * Verifies if a building element meets local regulatory minimum standards.
     * 
     * @param uValue calculated U-value
     * @param maxU maximum allowed U-value by code
     * @return true if the element is compliant
     */
    public static boolean meetsMinimumStandard(Real uValue, double maxU) {
        return uValue.doubleValue() <= maxU;
    }

    /**
     * Suggests the required thickness of a new insulation layer to reach 
     * a target U-value for an existing wall.
     * 
     * @param existingLayers current wall configuration
     * @param insulation material to be added as insulation
     * @param targetU the desired U-value
     * @param rsi internal surface resistance
     * @param rse external surface resistance
     * @return required insulation thickness in meters
     */
    public static Real requiredInsulationThickness(List<WallLayer> existingLayers,
            ThermalMaterial insulation, double targetU, 
            double rsi, double rse) {
        
        if (targetU <= 0) return Real.ZERO;
        double existingR = rsi + rse;
        for (WallLayer layer : existingLayers) {
            existingR += layer.thermalResistance();
        }
        
        double requiredR = 1.0 / targetU;
        double additionalR = requiredR - existingR;
        
        if (additionalR <= 0) return Real.ZERO;
        
        double thickness = additionalR * insulation.thermalConductivity();
        return Real.of(thickness);
    }
}
