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

package org.episteme.social.architecture;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Analytical tool for optimizing construction material selection based on 
 * sustainability metrics, including embodied carbon footprint and 
 * recycled content ratios.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class SustainableMaterialOptimizer {

    private SustainableMaterialOptimizer() {}

    /**
     * Environmental and economic data for a specific construction material.
     */
    public record MaterialData(
        String name,
        double embodiedCarbonKgCO2PerKg, // kg CO2 per kg of material
        double densityKgM3,            // kg per cubic meter
        double recycledContentRatio,   // 0.0 to 1.0
        double costPerKg               // Currency units per kg
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Calculates the total embodied carbon footprint (in kg CO2) for a 
     * given volume of material.
     * 
     * @param data environmental properties of the material
     * @param volumeM3 the volume being used in cubic meters
     * @return the total carbon footprint as a Real value
     */
    public static Real calculateCarbonFootprint(MaterialData data, double volumeM3) {
        double mass = volumeM3 * data.densityKgM3();
        return Real.of(mass * data.embodiedCarbonKgCO2PerKg());
    }

    /**
     * Selects the optimal material from a list of candidates by balancing 
     * environmental impact (carbon) against project budget (cost).
     * 
     * @param materials list of candidate material data
     * @param volumeM3 required volume of material
     * @param carbonWeight relative importance of carbon reduction (0.0 to 1.0)
     * @return the material data that minimizes the weighted multi-objective score
     */
    public static MaterialData findOptimalMaterial(List<MaterialData> materials, double volumeM3, double carbonWeight) {
        return materials.stream().min(Comparator.comparingDouble(m -> {
            double carbon = calculateCarbonFootprint(m, volumeM3).doubleValue();
            double cost = volumeM3 * m.densityKgM3() * m.costPerKg();
            return carbon * carbonWeight + cost * (1.0 - carbonWeight);
        })).orElse(null);
    }
}

