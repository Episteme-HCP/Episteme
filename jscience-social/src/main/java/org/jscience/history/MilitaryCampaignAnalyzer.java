package org.jscience.history;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Analyzes military campaigns from a logistical perspective.
 */
public final class MilitaryCampaignAnalyzer {

    private MilitaryCampaignAnalyzer() {}

    public enum SupplyType {
        FOOD, WATER, AMMUNITION, FODDER, FUEL, MEDICAL
    }

    public record Army(
        String name,
        int infantry,
        int cavalry,
        int artillery,
        int supportPersonnel,
        double dailyMarchKm
    ) {}

    public record TerrainSegment(
        String name,
        double distanceKm,
        double terrainDifficulty,  // 1.0 = road, 2.0 = rough, 3.0 = mountain
        double forageAvailability, // 0-1
        boolean hasWaterSource
    ) {}

    public record SupplyRequirements(
        double foodKgPerDay,
        double waterLitersPerDay,
        double fodderKgPerDay,
        double ammunitionKg,
        double fuelLitersPerDay
    ) {}

    public record CampaignAnalysis(
        double totalDistanceKm,
        int daysRequired,
        Map<SupplyType, Double> totalSuppliesNeeded,
        double attritionRate,
        List<String> criticalPoints,
        boolean logisticallyFeasible
    ) {}

    /**
     * Calculates daily supply requirements for an army.
     */
    public static SupplyRequirements calculateDailyRequirements(Army army) {
        int totalMen = army.infantry() + army.cavalry() + army.artillery() + army.supportPersonnel();
        int animals = army.cavalry() + (army.artillery() * 6); // Horses + draft animals
        
        // Historical estimates
        double food = totalMen * 1.0; // 1 kg food per man per day
        double water = totalMen * 3.0; // 3 liters per man
        double fodder = animals * 10.0; // 10 kg per animal
        double ammo = army.infantry() * 0.1 + army.artillery() * 50; // Approximate
        double fuel = 0; // Pre-mechanized warfare
        
        return new SupplyRequirements(food, water, fodder, ammo, fuel);
    }

    /**
     * Analyzes a campaign route.
     */
    public static CampaignAnalysis analyzeCampaign(Army army, List<TerrainSegment> route) {
        SupplyRequirements daily = calculateDailyRequirements(army);
        
        double totalDistance = route.stream().mapToDouble(TerrainSegment::distanceKm).sum();
        
        // Calculate days considering terrain
        double totalDays = 0;
        for (TerrainSegment segment : route) {
            double effectiveSpeed = army.dailyMarchKm() / segment.terrainDifficulty();
            totalDays += segment.distanceKm() / effectiveSpeed;
        }
        int daysRequired = (int) Math.ceil(totalDays);
        
        // Calculate total supplies
        Map<SupplyType, Double> supplies = new EnumMap<>(SupplyType.class);
        supplies.put(SupplyType.FOOD, daily.foodKgPerDay() * daysRequired);
        supplies.put(SupplyType.WATER, daily.waterLitersPerDay() * daysRequired);
        supplies.put(SupplyType.FODDER, daily.fodderKgPerDay() * daysRequired);
        supplies.put(SupplyType.AMMUNITION, daily.ammunitionKg());
        
        // Subtract foraging
        double totalForage = route.stream()
            .mapToDouble(s -> s.forageAvailability() * s.distanceKm() / totalDistance)
            .average().orElse(0);
        supplies.put(SupplyType.FOOD, supplies.get(SupplyType.FOOD) * (1 - totalForage * 0.3));
        
        // Identify critical points
        List<String> critical = new ArrayList<>();
        for (TerrainSegment segment : route) {
            if (!segment.hasWaterSource()) {
                critical.add(segment.name() + ": No water source - need to carry supplies");
            }
            if (segment.terrainDifficulty() > 2.0) {
                critical.add(segment.name() + ": Difficult terrain - reduced march speed");
            }
            if (segment.forageAvailability() < 0.2) {
                critical.add(segment.name() + ": Poor foraging - supply lines critical");
            }
        }
        
        // Attrition estimate
        double attrition = calculateAttrition(army, route, daysRequired);
        
        // Feasibility: Can the army carry enough supplies?
        int totalStrength = army.infantry() + army.cavalry() + army.supportPersonnel();
        double carryCapacityKg = totalStrength * 20 + army.cavalry() * 100; // Men + pack animals
        double totalSupplyWeight = supplies.get(SupplyType.FOOD) + supplies.get(SupplyType.AMMUNITION);
        boolean feasible = totalSupplyWeight < carryCapacityKg || hasSupplyLines(route);
        
        return new CampaignAnalysis(totalDistance, daysRequired, supplies, attrition, 
            critical, feasible);
    }

    /**
     * Calculates expected attrition (non-combat losses).
     */
    public static double calculateAttrition(Army army, List<TerrainSegment> route, int days) {
        int totalMen = army.infantry() + army.cavalry() + army.supportPersonnel();
        
        // Base attrition rate (historical: 1-3% per month on campaign)
        double baseRate = 0.01; // 1% per month baseline
        
        // Terrain factor
        double terrainFactor = route.stream()
            .mapToDouble(s -> s.terrainDifficulty() * s.distanceKm())
            .sum() / route.stream().mapToDouble(TerrainSegment::distanceKm).sum();
        
        // Forage factor
        double forageFactor = 1 - route.stream()
            .mapToDouble(TerrainSegment::forageAvailability)
            .average().orElse(0.5);
        
        // Water factor
        long noWaterSegments = route.stream().filter(s -> !s.hasWaterSource()).count();
        double waterFactor = 1 + (noWaterSegments * 0.1);
        
        double monthlyRate = baseRate * terrainFactor * (1 + forageFactor) * waterFactor;
        double totalAttrition = monthlyRate * (days / 30.0) * totalMen;
        
        return totalAttrition / totalMen; // Return as percentage
    }

    /**
     * Estimates maximum operational range without resupply.
     */
    public static Real maxOperationalRange(Army army, double carryCapacityPercentage) {
        SupplyRequirements daily = calculateDailyRequirements(army);
        
        int totalStrength = army.infantry() + army.cavalry() + army.supportPersonnel();
        double carryCapacity = totalStrength * 20 * carryCapacityPercentage; // kg
        
        double daysOfSupplies = carryCapacity / daily.foodKgPerDay();
        double maxRange = (daysOfSupplies / 2) * army.dailyMarchKm(); // Out and back
        
        return Real.of(maxRange);
    }

    private static boolean hasSupplyLines(List<TerrainSegment> route) {
        // Simplified: assume supply lines if majority of route is road
        long roadSegments = route.stream()
            .filter(s -> s.terrainDifficulty() <= 1.2)
            .count();
        return roadSegments > route.size() / 2;
    }
}
