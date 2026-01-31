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

package org.jscience.social.history;

import org.jscience.core.mathematics.numbers.real.Real;
import java.io.Serializable;
import java.util.*;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Analyzes historical military campaigns from a logistical and strategic perspective.
 * Estimates supply requirements, movement speeds, and attrition rates across different terrains.
 */
public final class MilitaryCampaignAnalyzer {

    private MilitaryCampaignAnalyzer() {
        // Prevent instantiation
    }

    /**
     * Categories of military supplies required for operations.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum SupplyType {
        FOOD, WATER, AMMUNITION, FODDER, FUEL, MEDICAL
    }

    /**
     * Composition and capabilities of a military force.
     * 
     * @param name             army identifier
     * @param infantry         number of foot soldiers
     * @param cavalry          number of mounted soldiers
     * @param artillery        number of artillery pieces
     * @param supportPersonnel number of camp followers and logistics staff
     * @param dailyMarchKm     average march speed in km per day
     */
    @Persistent
    public record Army(
        @Attribute String name,
        @Attribute int infantry,
        @Attribute int cavalry,
        @Attribute int artillery,
        @Attribute int supportPersonnel,
        @Attribute double dailyMarchKm
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public Army {
            Objects.requireNonNull(name, "Army name cannot be null");
            if (infantry < 0 || cavalry < 0 || artillery < 0 || supportPersonnel < 0) {
                throw new IllegalArgumentException("Army personnel counts cannot be negative");
            }
        }
    }

    /**
     * Physical characteristics of a geographical segment of a campaign route.
     * 
     * @param name               segment name
     * @param distanceKm         length in kilometers
     * @param terrainDifficulty  weight (1.0 = road, 2.0 = rough, 3.0 = mountain)
     * @param forageAvailability availability of resources on land (0.0 to 1.0)
     * @param hasWaterSource     true if natural water is present along the path
     */
    @Persistent
    public record TerrainSegment(
        @Attribute String name,
        @Attribute double distanceKm,
        @Attribute double terrainDifficulty,
        @Attribute double forageAvailability,
        @Attribute boolean hasWaterSource
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public TerrainSegment {
            Objects.requireNonNull(name, "Segment name cannot be null");
        }
    }

    /**
     * Quantified daily needs for a specific army.
     * 
     * @param foodKgPerDay       total food weight needed daily
     * @param waterLitersPerDay  total water volume needed daily
     * @param fodderKgPerDay     total animal feed weight needed daily
     * @param ammunitionKg       total ammunition weight for operation
     * @param fuelLitersPerDay   total fuel volume for cooking/machines
     */
    @Persistent
    public record SupplyRequirements(
        @Attribute double foodKgPerDay,
        @Attribute double waterLitersPerDay,
        @Attribute double fodderKgPerDay,
        @Attribute double ammunitionKg,
        @Attribute double fuelLitersPerDay
    ) implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    /**
     * Final report of campaign feasibility and requirements.
     * 
     * @param totalDistanceKm     total path length
     * @param daysRequired        estimated duration in days
     * @param totalSuppliesNeeded sum of requirements per category
     * @param attritionRate       estimated percentage of force lost (0.0 to 1.0)
     * @param criticalPoints      list of identified logistical hazards
     * @param logisticallyFeasible true if the army can sustain itself along the route
     */
    @Persistent
    public record CampaignAnalysis(
        @Attribute double totalDistanceKm,
        @Attribute int daysRequired,
        @Attribute Map<SupplyType, Double> totalSuppliesNeeded,
        @Attribute double attritionRate,
        @Attribute List<String> criticalPoints,
        @Attribute boolean logisticallyFeasible
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public CampaignAnalysis {
            totalSuppliesNeeded = totalSuppliesNeeded != null ? Map.copyOf(totalSuppliesNeeded) : Map.of();
            criticalPoints = criticalPoints != null ? List.copyOf(criticalPoints) : List.of();
        }
    }

    /**
     * Calculates the daily supply requirements based on force composition.
     * 
     * @param army the military force
     * @return daily requirements as {@link SupplyRequirements}
     * @throws NullPointerException if army is null
     */
    public static SupplyRequirements calculateDailyRequirements(Army army) {
        Objects.requireNonNull(army, "Army cannot be null");
        int totalMen = army.infantry() + army.cavalry() + army.artillery() + army.supportPersonnel();
        int animals = army.cavalry() + (army.artillery() * 6); // Approximation for horse-drawn artillery
        
        // Baseline historical logistical constants
        double food = totalMen * 1.0; // 1kg/man/day
        double water = totalMen * 3.0; // 3L/man/day
        double fodder = animals * 10.0; // 10kg/animal/day
        double ammo = army.infantry() * 0.1 + army.artillery() * 50.0;
        double fuel = 0.0; // Negligible for pre-mechanized infantry
        
        return new SupplyRequirements(food, water, fodder, ammo, fuel);
    }

    /**
     * Conducts a detailed logistical analysis of a planned route for a specific army.
     * 
     * @param army  the military force
     * @param route list of terrain segments to traverse
     * @return findings of the analysis as {@link CampaignAnalysis}
     * @throws NullPointerException if army or route is null
     */
    public static CampaignAnalysis analyzeCampaign(Army army, List<TerrainSegment> route) {
        Objects.requireNonNull(army, "Army cannot be null");
        Objects.requireNonNull(route, "Route cannot be null");
        
        SupplyRequirements daily = calculateDailyRequirements(army);
        double totalDistance = route.stream().mapToDouble(TerrainSegment::distanceKm).sum();
        
        double totalDays = 0;
        for (TerrainSegment segment : route) {
            double effectiveSpeed = army.dailyMarchKm() / Math.max(1.0, segment.terrainDifficulty());
            if (effectiveSpeed > 0) {
                totalDays += segment.distanceKm() / effectiveSpeed;
            }
        }
        int daysRequired = (int) Math.ceil(totalDays);
        
        Map<SupplyType, Double> supplies = new EnumMap<>(SupplyType.class);
        supplies.put(SupplyType.FOOD, daily.foodKgPerDay() * daysRequired);
        supplies.put(SupplyType.WATER, daily.waterLitersPerDay() * daysRequired);
        supplies.put(SupplyType.FODDER, daily.fodderKgPerDay() * daysRequired);
        supplies.put(SupplyType.AMMUNITION, daily.ammunitionKg());
        
        // Adjust for foraging capability
        double avgForage = route.stream()
            .mapToDouble(TerrainSegment::forageAvailability)
            .average().orElse(0.0);
        supplies.put(SupplyType.FOOD, supplies.getOrDefault(SupplyType.FOOD, 0.0) * (1.0 - avgForage * 0.3));
        
        List<String> critical = new ArrayList<>();
        for (TerrainSegment segment : route) {
            if (!segment.hasWaterSource()) {
                critical.add(segment.name() + ": No natural water source");
            }
            if (segment.terrainDifficulty() > 2.0) {
                critical.add(segment.name() + ": Extreme terrain slows movement");
            }
            if (segment.forageAvailability() < 0.2) {
                critical.add(segment.name() + ": Barren land - supply lines mandatory");
            }
        }
        
        double attrition = calculateAttrition(army, route, daysRequired);
        
        int totalStrength = army.infantry() + army.cavalry() + army.supportPersonnel();
        double carryCapacityKg = totalStrength * 20.0 + army.cavalry() * 80.0;
        double totalSupplyWeight = supplies.getOrDefault(SupplyType.FOOD, 0.0) + supplies.getOrDefault(SupplyType.AMMUNITION, 0.0);
        boolean feasible = totalSupplyWeight < carryCapacityKg || hasSupplyLines(route);
        
        return new CampaignAnalysis(totalDistance, daysRequired, supplies, attrition, 
            critical, feasible);
    }

    /**
     * Estimates non-combat losses due to disease, desertion, and exhaustion.
     * 
     * @param army  the military force
     * @param route route traversed
     * @param days  duration of the campaign in days
     * @return percentage of force lost to attrition (0.0 to 1.0)
     */
    public static double calculateAttrition(Army army, List<TerrainSegment> route, int days) {
        Objects.requireNonNull(army, "Army cannot be null");
        Objects.requireNonNull(route, "Route cannot be null");
        
        int totalMen = army.infantry() + army.cavalry() + army.supportPersonnel();
        if (totalMen == 0) return 0.0;
        
        double baseRate = 0.01; // 1% monthly baseline
        
        double terrainSum = route.stream().mapToDouble(s -> s.terrainDifficulty() * s.distanceKm()).sum();
        double totalDist = Math.max(1.0, route.stream().mapToDouble(TerrainSegment::distanceKm).sum());
        double terrainFactor = terrainSum / totalDist;
        
        double forageFactor = 1.0 - route.stream()
            .mapToDouble(TerrainSegment::forageAvailability)
            .average().orElse(0.5);
            
        long noWaterCount = route.stream().filter(s -> !s.hasWaterSource()).count();
        double waterFactor = 1.0 + (noWaterCount * 0.1);
        
        double monthlyRate = baseRate * terrainFactor * (1.0 + forageFactor) * waterFactor;
        double totalLosses = monthlyRate * (days / 30.0) * totalMen;
        
        return Math.min(1.0, totalLosses / totalMen);
    }

    /**
     * Calculates the maximum distance an army can project without external resupply.
     * 
     * @param army military force
     * @param carryCapacityPercentage efficiency of transport (0.0 to 1.0)
     * @return maximum range in kilometers as a {@link Real} number
     * @throws NullPointerException if army is null
     */
    public static Real maxOperationalRange(Army army, double carryCapacityPercentage) {
        Objects.requireNonNull(army, "Army cannot be null");
        SupplyRequirements daily = calculateDailyRequirements(army);
        
        int totalStrength = army.infantry() + army.cavalry() + army.supportPersonnel();
        double carryCapacity = totalStrength * 20.0 * Math.min(1.0, carryCapacityPercentage);
        
        if (daily.foodKgPerDay() <= 0) return Real.ZERO;
        
        double daysOfSupplies = carryCapacity / daily.foodKgPerDay();
        double maxRange = (daysOfSupplies / 2.0) * army.dailyMarchKm(); // Outward and return journey
        
        return Real.of(maxRange);
    }

    private static boolean hasSupplyLines(List<TerrainSegment> route) {
        if (route.isEmpty()) return false;
        long roadSegments = route.stream()
            .filter(s -> s.terrainDifficulty() <= 1.2)
            .count();
        return roadSegments > (route.size() / 2);
    }
}

