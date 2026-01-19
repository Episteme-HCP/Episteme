package org.jscience.architecture.urbanism;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models the Urban Heat Island (UHI) effect based on land cover and urban density.
 */
public final class UrbanHeatIslandModel {

    private UrbanHeatIslandModel() {}

    public enum LandCover {
        VEGETATION(0.25, 0.40), // Albedo, Emissivity
        ASPHALT(0.10, 0.90),
        CONCRETE(0.30, 0.85),
        WATER(0.08, 0.98),
        ROOF_LIGHT(0.60, 0.85),
        ROOF_DARK(0.15, 0.90);

        private final double albedo;
        LandCover(double a, double e) { this.albedo = a; }
    }

    public record UrbanZone(
        String name,
        Map<LandCover, Double> surfaceComposition, // Fraction 0-1
        double skyViewFactor, // 0-1 (1 = open, 0 = canyon)
        double anthropogenicHeatFlux // W/m2 (traffic, AC, etc.)
    ) {}

    /**
     * Calculates the surface temperature anomaly for an urban zone.
     * ΔT = (Anthropogenic + Radiation_Absorbed) * (1 - SkyViewFactor) / h
     */
    public static Real calculateUHIIntensity(UrbanZone zone, double solarIrradiance) {
        double avgAlbedo = 0;
        for (var entry : zone.surfaceComposition().entrySet()) {
            avgAlbedo += entry.getKey().albedo * entry.getValue();
        }

        // Absorbed radiation flux
        double absorbedFlux = solarIrradiance * (1 - avgAlbedo);
        
        // Simplified UHI formula (Oke, 1982 style)
        // Intensity is proportional to building density (1 - SVF) and heat sources
        double intensity = (zone.anthropogenicHeatFlux() + absorbedFlux * 0.2) * (1 - zone.skyViewFactor());
        
        // Typical rural-urban difference in Kelvin
        return Real.of(Math.max(0, intensity / 20.0));
    }

    /**
     * Suggests cooling strategies to mitigate UHI.
     */
    public static List<String> suggestMitigation(UrbanZone zone, Real currentUHI) {
        List<String> strategies = new ArrayList<>();
        double uhiValue = currentUHI.doubleValue();

        if (uhiValue > 3.0) {
            strategies.add("Critical: Increase Sky View Factor by reducing building heights or widening streets");
        }
        
        double vegFraction = zone.surfaceComposition().getOrDefault(LandCover.VEGETATION, 0.0);
        if (vegFraction < 0.3) {
            strategies.add("Install green roofs or urban parks to increase evapotranspiration");
        }

        double asphaltFraction = zone.surfaceComposition().getOrDefault(LandCover.ASPHALT, 0.0);
        if (asphaltFraction > 0.4) {
            strategies.add("Replace dark asphalt with permeable, light-colored pavers (Cool Pavements)");
        }

        return strategies;
    }

    /**
     * Estimates Sky View Factor for a symmetrical urban canyon.
     * SVF = cos(atan(H / (W/2)))
     */
    public static Real estimateSkyViewFactor(double buildingHeight, double streetWidth) {
        double svf = Math.cos(Math.atan(buildingHeight / (streetWidth / 2.0)));
        return Real.of(svf);
    }
}
