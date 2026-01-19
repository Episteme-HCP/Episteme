package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Optimizes material selection for sustainability (CO2 footprint, recycled content).
 */
public final class SustainableMaterialOptimizer {

    private SustainableMaterialOptimizer() {}

    public record MaterialData(
        String name,
        double embodiedCarbonKgCO2PerKg, // kg CO2 / kg
        double densityKgM3,
        double recycledContentRatio,
        double costPerKg
    ) {}

    /**
     * Calculates total carbon footprint of a material volume.
     */
    public static Real calculateCarbonFootprint(MaterialData data, double volumeM3) {
        double mass = volumeM3 * data.densityKgM3();
        return Real.of(mass * data.embodiedCarbonKgCO2PerKg());
    }

    /**
     * Finds the best material for a given target volume based on a weight factor between carbon and cost.
     * 
     * @param materials List of candidates
     * @param volumeM3 Required volume
     * @param carbonWeight Weight for carbon footprint (0-1)
     */
    public static MaterialData findOptimalMaterial(List<MaterialData> materials, double volumeM3, double carbonWeight) {
        return materials.stream().min(Comparator.comparingDouble(m -> {
            double carbon = calculateCarbonFootprint(m, volumeM3).doubleValue();
            double cost = volumeM3 * m.densityKgM3() * m.costPerKg();
            return carbon * carbonWeight + cost * (1.0 - carbonWeight);
        })).orElse(null);
    }
}
