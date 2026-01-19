package org.jscience.geography;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Calculates accessibility indices and isochrones.
 */
public final class AccessibilityIndex {

    private AccessibilityIndex() {}

    public record Location(
        String id,
        String name,
        double latitude,
        double longitude,
        String type  // hospital, school, grocery, transit, etc.
    ) {}

    public record AccessibilityResult(
        double latitude,
        double longitude,
        Map<String, Double> travelTimesToNearest,  // minutes by type
        double compositeScore,
        String accessibilityLevel
    ) {}

    public record Isochrone(
        double centerLat,
        double centerLon,
        int travelTimeMinutes,
        String travelMode,
        List<double[]> boundaryPoints
    ) {}

    /**
     * Calculates accessibility score for a location.
     */
    public static AccessibilityResult calculateAccessibility(double lat, double lon,
            List<Location> facilities, Map<String, Double> weights) {
        
        Map<String, Double> nearestTimes = new HashMap<>();
        Map<String, Location> nearestLocations = new HashMap<>();
        
        // Find nearest facility of each type
        for (Location facility : facilities) {
            double time = estimateTravelTime(lat, lon, facility.latitude(), facility.longitude());
            
            String type = facility.type();
            if (!nearestTimes.containsKey(type) || time < nearestTimes.get(type)) {
                nearestTimes.put(type, time);
                nearestLocations.put(type, facility);
            }
        }
        
        // Calculate composite score (weighted inverse of travel times)
        double compositeScore = 0;
        double totalWeight = 0;
        
        for (Map.Entry<String, Double> entry : nearestTimes.entrySet()) {
            double weight = weights.getOrDefault(entry.getKey(), 1.0);
            // Inverse time, capped at 60 minutes
            double timeScore = Math.max(0, 1 - entry.getValue() / 60);
            compositeScore += weight * timeScore;
            totalWeight += weight;
        }
        
        compositeScore = totalWeight > 0 ? compositeScore / totalWeight : 0;
        
        // Determine level
        String level;
        if (compositeScore > 0.8) level = "Excellent";
        else if (compositeScore > 0.6) level = "Good";
        else if (compositeScore > 0.4) level = "Moderate";
        else if (compositeScore > 0.2) level = "Poor";
        else level = "Very Poor";
        
        return new AccessibilityResult(lat, lon, nearestTimes, compositeScore, level);
    }

    /**
     * Generates accessibility grid for an area.
     */
    public static List<AccessibilityResult> generateAccessibilityGrid(
            double minLat, double maxLat, double minLon, double maxLon,
            int gridSize, List<Location> facilities, Map<String, Double> weights) {
        
        List<AccessibilityResult> grid = new ArrayList<>();
        
        double latStep = (maxLat - minLat) / gridSize;
        double lonStep = (maxLon - minLon) / gridSize;
        
        for (int i = 0; i <= gridSize; i++) {
            for (int j = 0; j <= gridSize; j++) {
                double lat = minLat + i * latStep;
                double lon = minLon + j * lonStep;
                
                grid.add(calculateAccessibility(lat, lon, facilities, weights));
            }
        }
        
        return grid;
    }

    /**
     * Generates isochrone polygon (areas reachable within time limit).
     */
    public static Isochrone generateIsochrone(double centerLat, double centerLon,
            int travelTimeMinutes, String travelMode) {
        
        double speedKmh = switch (travelMode.toLowerCase()) {
            case "walking" -> 5;
            case "cycling" -> 15;
            case "driving" -> 40;  // Urban average
            case "transit" -> 25;
            default -> 30;
        };
        
        double radiusKm = speedKmh * travelTimeMinutes / 60.0;
        double radiusDeg = radiusKm / 111.0;  // Approximate conversion
        
        // Generate circle approximation
        List<double[]> boundary = new ArrayList<>();
        int points = 36;
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double lat = centerLat + radiusDeg * Math.cos(angle);
            double lon = centerLon + radiusDeg * Math.sin(angle) / Math.cos(Math.toRadians(centerLat));
            boundary.add(new double[]{lat, lon});
        }
        
        return new Isochrone(centerLat, centerLon, travelTimeMinutes, travelMode, boundary);
    }

    /**
     * Calculates transit desert score (lack of public transit access).
     */
    public static Real calculateTransitDesertScore(double lat, double lon,
            List<Location> transitStops, int populationDensity) {
        
        // Find nearest transit stop
        double nearestDistance = Double.MAX_VALUE;
        for (Location stop : transitStops) {
            if (stop.type().contains("transit") || stop.type().contains("bus") || 
                stop.type().contains("rail")) {
                double dist = haversineDistance(lat, lon, stop.latitude(), stop.longitude());
                nearestDistance = Math.min(nearestDistance, dist);
            }
        }
        
        // High population + far from transit = transit desert
        double distanceFactor = nearestDistance > 1.0 ? 1.0 : nearestDistance;
        double densityFactor = populationDensity > 3000 ? 1.0 : populationDensity / 3000.0;
        
        double score = distanceFactor * densityFactor;
        return Real.of(score);
    }

    /**
     * Standard accessibility weights.
     */
    public static Map<String, Double> standardWeights() {
        return Map.of(
            "hospital", 1.5,
            "grocery", 1.2,
            "school", 1.0,
            "transit", 1.3,
            "pharmacy", 1.0,
            "park", 0.8,
            "bank", 0.6
        );
    }

    /**
     * Walker's Paradise (Walk Score methodology simplified).
     */
    public static Real calculateWalkScore(double lat, double lon, List<Location> amenities) {
        double score = 0;
        
        Map<String, int[]> categoryDistances = Map.of(
            "grocery", new int[]{5, 400, 800, 1600},      // Full points at 400m, none at 1600m
            "restaurant", new int[]{5, 300, 600, 1200},
            "shopping", new int[]{3, 400, 800, 1600},
            "cafe", new int[]{3, 300, 600, 1200},
            "parks", new int[]{3, 400, 800, 1600}
        );
        
        for (Map.Entry<String, int[]> entry : categoryDistances.entrySet()) {
            String category = entry.getKey();
            int[] params = entry.getValue();
            int maxPoints = params[0];
            int fullDist = params[1];
            int partialDist = params[3];
            
            // Find nearest of this category
            double nearest = Double.MAX_VALUE;
            for (Location loc : amenities) {
                if (loc.type().toLowerCase().contains(category)) {
                    double dist = haversineDistance(lat, lon, loc.latitude(), loc.longitude()) * 1000;
                    nearest = Math.min(nearest, dist);
                }
            }
            
            // Score based on distance
            if (nearest <= fullDist) {
                score += maxPoints;
            } else if (nearest < partialDist) {
                score += maxPoints * (1 - (nearest - fullDist) / (partialDist - fullDist));
            }
        }
        
        // Normalize to 0-100
        double normalized = Math.min(100, score * 5);
        return Real.of(normalized);
    }

    private static double estimateTravelTime(double lat1, double lon1, double lat2, double lon2) {
        double distKm = haversineDistance(lat1, lon1, lat2, lon2);
        // Assume average urban speed of 30 km/h
        return distKm / 30 * 60;  // minutes
    }

    private static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
}
