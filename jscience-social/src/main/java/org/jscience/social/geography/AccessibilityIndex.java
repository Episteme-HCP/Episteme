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

package org.jscience.social.geography;

import org.jscience.natural.earth.Place;
import org.jscience.natural.earth.coordinates.GeodeticCoordinate;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Length;

import java.time.Duration;
import java.util.*;

/**
 * Utility for calculating geographical accessibility indices and isochrones.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class AccessibilityIndex {

    private AccessibilityIndex() {}

    public record AccessibilityResult(
        GeodeticCoordinate location,
        Map<String, Duration> travelTimesToNearest, // Minutes by facility type
        double compositeScore, // 0.0 to 1.0
        String accessibilityLevel
    ) {}

    public record Isochrone(
        GeodeticCoordinate center,
        Duration travelTime,
        String travelMode,
        List<GeodeticCoordinate> boundaryPoints
    ) {}

    /**
     * Calculates accessibility score for a location based on distance to nearest facilities.
     * 
     * @param location the calculation point
     * @param facilities available facilities to evaluate
     * @param weights importance of each facility type
     * @return calculated accessibility result
     */
    public static AccessibilityResult calculateAccessibility(GeodeticCoordinate location,
                                                          List<Place> facilities,
                                                          Map<String, Double> weights) {
        
        Map<String, Duration> nearestTimes = new HashMap<>();
        
        for (Place facility : facilities) {
            Quantity<Length> dist = location.distanceTo(facility.getCenter());
            if (dist == null) continue;

            double timeMinutes = dist.to(Units.KILOMETER).getValue().doubleValue() / 30.0 * 60.0; // Assume 30km/h avg
            Duration duration = Duration.ofMinutes((long)timeMinutes);
            
            String type = facility.getType().name();
            if (!nearestTimes.containsKey(type) || duration.compareTo(nearestTimes.get(type)) < 0) {
                nearestTimes.put(type, duration);
            }
        }
        
        double compositeScore = calculateCompositeScore(nearestTimes, weights);
        String level = getAccessibilityLevel(compositeScore);
        
        return new AccessibilityResult(location, nearestTimes, compositeScore, level);
    }

    private static double calculateCompositeScore(Map<String, Duration> nearestTimes, Map<String, Double> weights) {
        double score = 0;
        double totalWeight = 0;
        
        for (Map.Entry<String, Duration> entry : nearestTimes.entrySet()) {
            double weight = weights.getOrDefault(entry.getKey(), 1.0);
            double minutes = entry.getValue().toMinutes();
            double timeScore = Math.max(0, 1 - minutes / 60.0); // Decay over 1 hour
            score += weight * timeScore;
            totalWeight += weight;
        }
        
        return totalWeight > 0 ? score / totalWeight : 0;
    }

    private static String getAccessibilityLevel(double score) {
        if (score > 0.8) return "Excellent";
        if (score > 0.6) return "Good";
        if (score > 0.4) return "Moderate";
        if (score > 0.2) return "Poor";
        return "Very Poor";
    }

    /**
     * Generates an approximate isochrone (reachable area) boundary.
     */
    public static Isochrone generateIsochrone(GeodeticCoordinate center, Duration limit, String mode) {
        double speedKmh = switch (mode.toLowerCase()) {
            case "walking" -> 5;
            case "cycling" -> 15;
            case "driving" -> 40;
            default -> 30;
        };
        
        double distanceKm = speedKmh * limit.toMinutes() / 60.0;
        double radiusDeg = distanceKm / 111.0;
        
        List<GeodeticCoordinate> boundary = new ArrayList<>();
        int points = 36;
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double lat = center.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue() + radiusDeg * Math.cos(angle);
            double lon = center.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue() + radiusDeg * Math.sin(angle) / Math.cos(Math.toRadians(center.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue()));
            boundary.add(new GeodeticCoordinate(lat, lon));
        }
        
        return new Isochrone(center, limit, mode, boundary);
    }
}

