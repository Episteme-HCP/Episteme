package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Area;
import org.jscience.measure.Units;

/**
 * Thermal simulation for building energy analysis.
 */
public final class ThermalSimulator {

    private ThermalSimulator() {}

    /**
     * Material thermal properties.
     */
    public record ThermalMaterial(
        String name,
        double thermalConductivity, // W/(m·K)
        double density,             // kg/m³
        double specificHeat         // J/(kg·K)
    ) {}

    public static final ThermalMaterial CONCRETE = new ThermalMaterial("Concrete", 1.4, 2300, 880);
    public static final ThermalMaterial BRICK = new ThermalMaterial("Brick", 0.84, 1920, 790);
    public static final ThermalMaterial GLASS = new ThermalMaterial("Glass", 1.0, 2500, 840);
    public static final ThermalMaterial WOOD = new ThermalMaterial("Wood (softwood)", 0.13, 500, 1600);
    public static final ThermalMaterial INSULATION_MINERAL = new ThermalMaterial("Mineral Wool", 0.035, 30, 1030);
    public static final ThermalMaterial INSULATION_EPS = new ThermalMaterial("EPS Foam", 0.038, 20, 1450);
    public static final ThermalMaterial AIR_GAP = new ThermalMaterial("Air Gap", 0.025, 1.2, 1005);
    public static final ThermalMaterial PLASTERBOARD = new ThermalMaterial("Plasterboard", 0.25, 900, 1000);

    /**
     * A wall layer with material and thickness.
     */
    public record WallLayer(ThermalMaterial material, double thicknessMeters) {
        public double thermalResistance() {
            return thicknessMeters / material.thermalConductivity();
        }
    }

    /**
     * Calculates the U-value (thermal transmittance) of a wall assembly.
     * U = 1 / (Rsi + ΣR + Rse)
     * 
     * @param layers The wall layers from inside to outside.
     * @param internalSurfaceResistance Rsi (typically 0.13 for walls).
     * @param externalSurfaceResistance Rse (typically 0.04 for walls).
     * @return U-value in W/(m²·K).
     */
    public static Real calculateUValue(java.util.List<WallLayer> layers, 
            double internalSurfaceResistance, double externalSurfaceResistance) {
        
        double totalR = internalSurfaceResistance + externalSurfaceResistance;
        for (WallLayer layer : layers) {
            totalR += layer.thermalResistance();
        }
        
        return Real.of(1.0 / totalR);
    }

    /**
     * Calculates annual heating energy demand (simplified degree-day method).
     * Q = U × A × DD × 24 / 1000 (kWh)
     */
    public static Real annualHeatingDemand(Real uValue, Quantity<Area> area, double heatingDegreeDays) {
        double u = uValue.doubleValue();
        double a = area.to(Units.SQUARE_METER).getValue().doubleValue();
        double demand = u * a * heatingDegreeDays * 24 / 1000;
        return Real.of(demand); // kWh
    }

    /**
     * Calculates thermal mass (heat storage capacity) of a building element.
     */
    public static Real thermalMass(WallLayer layer, Quantity<Area> area) {
        double a = area.to(Units.SQUARE_METER).getValue().doubleValue();
        double volume = a * layer.thicknessMeters();
        double mass = volume * layer.material().density();
        double capacity = mass * layer.material().specificHeat();
        return Real.of(capacity); // Joules/K
    }

    /**
     * Checks if wall meets building code minimum (e.g., U ≤ 0.3 W/m²K).
     */
    public static boolean meetsMinimumStandard(Real uValue, double maxU) {
        return uValue.doubleValue() <= maxU;
    }

    /**
     * Suggests insulation thickness needed to achieve target U-value.
     */
    public static Real requiredInsulationThickness(java.util.List<WallLayer> existingLayers,
            ThermalMaterial insulation, double targetU, 
            double rsi, double rse) {
        
        double existingR = rsi + rse;
        for (WallLayer layer : existingLayers) {
            existingR += layer.thermalResistance();
        }
        
        double requiredR = 1.0 / targetU;
        double additionalR = requiredR - existingR;
        
        if (additionalR <= 0) return Real.of(0); // Already meets target
        
        double thickness = additionalR * insulation.thermalConductivity();
        return Real.of(thickness);
    }
}
