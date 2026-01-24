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

package org.jscience.architecture.urbanism;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Analytical model for the Urban Heat Island (UHI) effect. It estimates the 
 * temperature differential between urban and rural areas based on land cover, 
 * geometry (sky view factor), and anthropogenic heat sources.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class UrbanHeatIslandModel {

    private UrbanHeatIslandModel() {}

    /**
     * Categories of urban land cover with their associated thermal properties.
     */
    public enum LandCover {
        /** Natural vegetation, high evapotranspiration and moderate albedo. */
        VEGETATION(0.25, 0.40), 
        /** Dark road surfaces, very low albedo and high thermal storage. */
        ASPHALT(0.10, 0.90),
        /** Structural concrete, moderate albedo and high thermal mass. */
        CONCRETE(0.30, 0.85),
        /** Water bodies, low albedo and high thermal capacity. */
        WATER(0.08, 0.98),
        /** Cool roofs or light-colored roofing materials. */
        ROOF_LIGHT(0.60, 0.85),
        /** Traditional dark roofing materials. */
        ROOF_DARK(0.15, 0.90);

        private final double albedo;
        private final double emissivity;

        LandCover(double albedo, double emissivity) { 
            this.albedo = albedo; 
            this.emissivity = emissivity;
        }

        public double getAlbedo() { return albedo; }
        public double getEmissivity() { return emissivity; }
    }

    /**
     * Represents a discrete urban zone with specific surface and geometric characteristics.
     */
    public record UrbanZone(
        String name,
        Map<LandCover, Double> surfaceComposition, // Fraction 0.0 to 1.0
        double skyViewFactor, // 0.0 to 1.0 (1.0 = open sky, 0.0 = deep canyon)
        double anthropogenicHeatFlux // W/m2 (from traffic, appliances, AC systems)
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Calculates the estimated Urban Heat Island (UHI) intensity as a temperature 
     * anomaly relative to the surrounding rural environment.
     * Formula: ΔT ≈ (Q_ant + Q_solar_abs) * (1 - SVF) / h
     * 
     * @param zone the urban zone to analyze
     * @param solarIrradiance current incident solar radiation in W/m2
     * @return the temperature difference in Kelvins (K)
     */
    public static Real calculateUHIIntensity(UrbanZone zone, double solarIrradiance) {
        if (zone == null || zone.surfaceComposition() == null) return Real.ZERO;
        
        double avgAlbedo = 0;
        double totalFraction = 0;
        for (var entry : zone.surfaceComposition().entrySet()) {
            avgAlbedo += entry.getKey().getAlbedo() * entry.getValue();
            totalFraction += entry.getValue();
        }
        
        if (totalFraction > 0) avgAlbedo /= totalFraction;

        // Absorbed radiation flux
        double absorbedFlux = solarIrradiance * (1 - avgAlbedo);
        
        // Simplified semi-empirical model for UHI intensity
        // Magnitude is driven by anthropogenic heat and poorly dissipated solar gain
        double intensity = (zone.anthropogenicHeatFlux() + absorbedFlux * 0.2) * (1 - Math.max(0, Math.min(1.0, zone.skyViewFactor())));
        
        return Real.of(Math.max(0, intensity / 20.0));
    }

    /**
     * Generates a list of targeted architectural and urban planning mitigation 
     * strategies based on the zone's specific vulnerabilities.
     * 
     * @param zone the zone under evaluation
     * @param currentUHI the calculated heat island intensity
     * @return list of recommended interventions
     */
    public static List<String> suggestMitigation(UrbanZone zone, Real currentUHI) {
        List<String> strategies = new ArrayList<>();
        if (zone == null || currentUHI == null) return strategies;
        
        double uhiValue = currentUHI.doubleValue();

        if (uhiValue > 3.0) {
            strategies.add("Critical: Increase Sky View Factor by reducing building heights or widening streets to improve long-wave radiation loss.");
        }
        
        double vegFraction = zone.surfaceComposition().getOrDefault(LandCover.VEGETATION, 0.0);
        if (vegFraction < 0.3) {
            strategies.add("Install green roofs or urban forests to increase latent heat cooling through evapotranspiration.");
        }

        double asphaltFraction = zone.surfaceComposition().getOrDefault(LandCover.ASPHALT, 0.0);
        if (asphaltFraction > 0.4) {
            strategies.add("Replace dark asphalt with 'Cool Pavements' (permeable, high-albedo materials) to reduce short-wave absorption.");
        }

        return strategies;
    }

    /**
     * Estimates the Sky View Factor (SVF) for a symmetrical urban canyon geometry.
     * Formula: SVF = cos(atan(H / (W/2)))
     * 
     * @param buildingHeight average height of buildings (H)
     * @param streetWidth average width of the street (W)
     * @return SVF coefficient (0.0 to 1.0)
     */
    public static Real estimateSkyViewFactor(double buildingHeight, double streetWidth) {
        if (streetWidth <= 0) return Real.ZERO;
        double svf = Math.cos(Math.atan(buildingHeight / (streetWidth / 2.0)));
        return Real.of(Math.max(0, Math.min(1.0, svf)));
    }
}
