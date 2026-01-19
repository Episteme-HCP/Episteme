package org.jscience.earth.geology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models volcanic hazard zones and risk assessment.
 */
public final class VolcanoHazardModel {

    private VolcanoHazardModel() {}

    public enum HazardType {
        LAVA_FLOW, PYROCLASTIC_FLOW, LAHAR, ASH_FALL, 
        BALLISTIC_PROJECTILES, VOLCANIC_GAS, TSUNAMI
    }

    public enum VolcanoType {
        SHIELD(2, 1000),           // Low explosivity, high volume
        STRATOVOLCANO(6, 100),     // High explosivity
        CALDERA(8, 1000),          // Extreme events
        CINDER_CONE(1, 1);         // Small eruptions

        private final int typicalVEI;
        private final double typicalVolumeKm3;

        VolcanoType(int vei, double volume) {
            this.typicalVEI = vei;
            this.typicalVolumeKm3 = volume;
        }

        public int getTypicalVEI() { return typicalVEI; }
        public double getTypicalVolumeKm3() { return typicalVolumeKm3; }
    }

    public record Volcano(
        String name,
        double latitude,
        double longitude,
        double summitElevation,
        VolcanoType type,
        int lastEruptionYear,
        List<Integer> historicalVEIs
    ) {}

    public record HazardZone(
        HazardType type,
        List<double[]> boundaryPolygon,
        double probability,       // Annual probability
        double maxIntensity,
        String description
    ) {}

    /**
     * Generates hazard zones for a volcano.
     */
    public static List<HazardZone> generateHazardZones(Volcano volcano, int vei) {
        List<HazardZone> zones = new ArrayList<>();
        double lat = volcano.latitude();
        double lon = volcano.longitude();
        
        // Pyroclastic flow zone (high danger, close range)
        if (vei >= 3) {
            double radius = 5 + vei * 2; // km
            zones.add(new HazardZone(
                HazardType.PYROCLASTIC_FLOW,
                generateCircle(lat, lon, radius),
                0.8,
                1.0,
                "Extreme danger zone - immediate evacuation required"
            ));
        }
        
        // Lahar zone (follows valleys, extended range)
        if (volcano.summitElevation() > 2000) {
            double range = 20 + vei * 5;
            zones.add(new HazardZone(
                HazardType.LAHAR,
                generateEllipse(lat, lon, range, range * 0.3, -45), // Downslope
                0.6,
                0.8,
                "Lahar/mudflow hazard - avoid river valleys"
            ));
        }
        
        // Ash fall zone (wind-dependent, large area)
        double ashRadius = 50 + vei * 30;
        zones.add(new HazardZone(
            HazardType.ASH_FALL,
            generateEllipse(lat, lon + 0.5, ashRadius, ashRadius * 0.3, 90), // Prevailing wind
            0.9,
            0.5,
            "Ash fall zone - respiratory protection recommended"
        ));
        
        // Lava flow (shield volcanoes mainly)
        if (volcano.type() == VolcanoType.SHIELD) {
            zones.add(new HazardZone(
                HazardType.LAVA_FLOW,
                generateCircle(lat, lon, 15),
                0.7,
                0.6,
                "Lava flow hazard - slow-moving but destructive"
            ));
        }
        
        // Ballistic zone (summit area)
        if (vei >= 2) {
            zones.add(new HazardZone(
                HazardType.BALLISTIC_PROJECTILES,
                generateCircle(lat, lon, 3 + vei),
                0.5,
                0.9,
                "Ballistic projectile zone - extreme danger"
            ));
        }
        
        return zones;
    }

    /**
     * Estimates Volcanic Explosivity Index from eruption parameters.
     */
    public static int estimateVEI(double ejectaVolumeKm3, double columnHeightKm) {
        // VEI is logarithmic scale
        if (ejectaVolumeKm3 >= 1000) return 8;
        if (ejectaVolumeKm3 >= 100) return 7;
        if (ejectaVolumeKm3 >= 10) return 6;
        if (ejectaVolumeKm3 >= 1) return 5;
        if (ejectaVolumeKm3 >= 0.1) return 4;
        if (ejectaVolumeKm3 >= 0.01) return 3;
        if (columnHeightKm >= 1) return 2;
        if (columnHeightKm >= 0.1) return 1;
        return 0;
    }

    /**
     * Calculates eruption recurrence probability.
     */
    public static Real recurrenceProbability(List<Integer> historicalYears, int currentYear, 
            int forecastYears) {
        
        if (historicalYears.size() < 2) return Real.of(0.1); // Unknown
        
        // Calculate average recurrence interval
        List<Integer> sorted = new ArrayList<>(historicalYears);
        Collections.sort(sorted);
        
        double totalInterval = 0;
        for (int i = 1; i < sorted.size(); i++) {
            totalInterval += sorted.get(i) - sorted.get(i - 1);
        }
        double avgInterval = totalInterval / (sorted.size() - 1);
        
        // Time since last eruption
        int yearsSinceLast = currentYear - sorted.get(sorted.size() - 1);
        
        // Poisson probability
        double lambda = forecastYears / avgInterval;
        double prob = 1 - Math.exp(-lambda);
        
        // Increase if overdue
        if (yearsSinceLast > avgInterval) {
            prob = Math.min(0.95, prob * 1.5);
        }
        
        return Real.of(prob);
    }

    /**
     * Calculates population at risk within hazard zones.
     */
    public static long estimatePopulationAtRisk(List<HazardZone> zones,
            Map<double[], Long> populationGrid) {
        
        // Simplified: sum population within hazard zones
        long total = 0;
        // Would need proper spatial analysis in real implementation
        return total;
    }

    /**
     * Generates ash fall depth estimate at distance.
     */
    public static Real ashFallDepth(int vei, double distanceKm, double windSpeed) {
        // Exponential decay with distance
        double peakDepth = Math.pow(10, vei - 2); // cm near source
        double decay = Math.exp(-distanceKm / (10 + windSpeed));
        double depth = peakDepth * decay;
        return Real.of(depth);
    }

    private static List<double[]> generateCircle(double centerLat, double centerLon, 
            double radiusKm) {
        List<double[]> points = new ArrayList<>();
        double degPerKm = 1.0 / 111.0; // Approximate
        
        for (int i = 0; i < 36; i++) {
            double angle = i * 10 * Math.PI / 180;
            points.add(new double[] {
                centerLat + radiusKm * degPerKm * Math.cos(angle),
                centerLon + radiusKm * degPerKm * Math.sin(angle)
            });
        }
        return points;
    }

    private static List<double[]> generateEllipse(double centerLat, double centerLon,
            double majorKm, double minorKm, double rotationDeg) {
        List<double[]> points = new ArrayList<>();
        double degPerKm = 1.0 / 111.0;
        double rot = rotationDeg * Math.PI / 180;
        
        for (int i = 0; i < 36; i++) {
            double angle = i * 10 * Math.PI / 180;
            double x = majorKm * Math.cos(angle);
            double y = minorKm * Math.sin(angle);
            
            // Rotate
            double xr = x * Math.cos(rot) - y * Math.sin(rot);
            double yr = x * Math.sin(rot) + y * Math.cos(rot);
            
            points.add(new double[] {
                centerLat + yr * degPerKm,
                centerLon + xr * degPerKm
            });
        }
        return points;
    }
}
