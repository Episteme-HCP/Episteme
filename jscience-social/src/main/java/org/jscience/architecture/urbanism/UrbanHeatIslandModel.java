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
import java.util.HashMap;

import org.jscience.util.UniversalDataModel;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Length;

/**
 * Analytical model for the Urban Heat Island (UHI) effect. It estimates the 
 * temperature differential between urban and rural areas based on land cover, 
 * geometry (sky view factor), and anthropogenic heat sources.
 */
public final class UrbanHeatIslandModel implements UniversalDataModel {

    private final String name;
    private final List<UrbanZone> zones = new ArrayList<>();
    private Quantity<?> ambientSolarIrradiance; // W/m2

    public UrbanHeatIslandModel(String name) {
        this.name = name;
    }

    public void addZone(UrbanZone zone) { zones.add(zone); }
    public void setAmbientSolarIrradiance(Quantity<?> irradiance) { this.ambientSolarIrradiance = irradiance; }

    /**
     * Categories of urban land cover with their associated thermal properties.
     */
    public enum LandCover {
        VEGETATION(0.25, 0.40), 
        ASPHALT(0.10, 0.90),
        CONCRETE(0.30, 0.85),
        WATER(0.08, 0.98),
        ROOF_LIGHT(0.60, 0.85),
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
        Map<LandCover, Double> surfaceComposition,
        double skyViewFactor,
        Quantity<?> anthropogenicHeatFlux // W/m2
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Calculates the estimated Urban Heat Island (UHI) intensity as a temperature 
     * anomaly relative to the surrounding rural environment.
     */
    public Quantity<Temperature> calculateUHIIntensity(UrbanZone zone) {
        if (zone == null || ambientSolarIrradiance == null) return null;
        
        double avgAlbedo = 0;
        double totalFraction = 0;
        for (var entry : zone.surfaceComposition().entrySet()) {
            avgAlbedo += entry.getKey().getAlbedo() * (entry.getValue() != null ? entry.getValue() : 0.0);
            totalFraction += (entry.getValue() != null ? entry.getValue() : 0.0);
        }
        
        if (totalFraction > 0) avgAlbedo /= totalFraction;

        double solar = ambientSolarIrradiance.getValue().doubleValue();
        double absorbedFlux = solar * (1 - avgAlbedo);
        double anthro = zone.anthropogenicHeatFlux().getValue().doubleValue();
        
        double intensity = (anthro + absorbedFlux * 0.2) * (1 - Math.max(0, Math.min(1.0, zone.skyViewFactor())));
        
        return Quantities.create(Math.max(0, intensity / 20.0), Units.KELVIN);
    }

    public static Quantity<?> estimateSkyViewFactor(Quantity<Length> buildingHeight, Quantity<Length> streetWidth) {
        double H = buildingHeight.to(Units.METER).getValue().doubleValue();
        double W = streetWidth.to(Units.METER).getValue().doubleValue();
        if (W <= 0) return Quantities.create(0.0, Units.ONE);
        double svf = Math.cos(Math.atan(H / (W / 2.0)));
        return Quantities.create(svf, Units.ONE);
    }

    @Override
    public String getModelType() {
        return "URBAN_HEAT_ISLAND";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("name", name);
        meta.put("urban_zones_count", zones.size());
        return meta;
    }

    @Override
    public Map<String, Quantity<?>> getQuantities() {
        Map<String, Quantity<?>> q = new HashMap<>();
        if (!zones.isEmpty()) {
            UrbanZone primary = zones.get(0);
            Quantity<Temperature> intensity = calculateUHIIntensity(primary);
            if (intensity != null) q.put("primary_zone_uhi_intensity", intensity);
            q.put("primary_zone_heat_flux", primary.anthropogenicHeatFlux());
        }
        return q;
    }
}
