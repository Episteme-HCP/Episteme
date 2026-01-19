package org.jscience.arts;

import java.util.*;

/**
 * Simulates aging and restoration of artworks.
 */
public final class RestorationSimulator {

    private RestorationSimulator() {}

    public enum DegradationType {
        VARNISH_YELLOWING, PAINT_CRACKING, CANVAS_SAGGING,
        PIGMENT_FADING, OXIDATION, MOLD, DELAMINATION, 
        FOXING, WATER_DAMAGE, INSECT_DAMAGE
    }

    public record DegradationState(
        int ageYears,
        Map<DegradationType, Double> severity,  // 0-1 for each type
        double overallCondition                  // 0-1
    ) {}

    public record ConservationTreatment(
        String name,
        Set<DegradationType> addresses,
        double effectiveness,  // 0-1
        double cost,
        int durationDays
    ) {}

    public static final List<ConservationTreatment> TREATMENTS = List.of(
        new ConservationTreatment("Varnish removal and replacement",
            Set.of(DegradationType.VARNISH_YELLOWING), 0.95, 5000, 14),
        new ConservationTreatment("Crack consolidation",
            Set.of(DegradationType.PAINT_CRACKING, DegradationType.DELAMINATION), 0.8, 8000, 30),
        new ConservationTreatment("Canvas relining",
            Set.of(DegradationType.CANVAS_SAGGING, DegradationType.DELAMINATION), 0.9, 15000, 60),
        new ConservationTreatment("Inpainting",
            Set.of(DegradationType.PAINT_CRACKING, DegradationType.WATER_DAMAGE), 0.75, 10000, 45),
        new ConservationTreatment("Fumigation",
            Set.of(DegradationType.MOLD, DegradationType.INSECT_DAMAGE), 0.95, 2000, 7),
        new ConservationTreatment("Deacidification",
            Set.of(DegradationType.FOXING, DegradationType.OXIDATION), 0.7, 3000, 10)
    );

    /**
     * Simulates degradation over time.
     */
    public static DegradationState simulateAging(int currentAge, int additionalYears,
            Map<String, Double> environmentFactors) {
        
        Map<DegradationType, Double> severity = new EnumMap<>(DegradationType.class);
        
        double humidity = environmentFactors.getOrDefault("humidity", 0.5);
        double temperature = environmentFactors.getOrDefault("temperature", 20.0);
        double lightExposure = environmentFactors.getOrDefault("light", 0.5);
        
        int totalAge = currentAge + additionalYears;
        
        // Varnish yellowing - accelerated by light
        severity.put(DegradationType.VARNISH_YELLOWING,
            Math.min(1.0, totalAge * 0.002 * (1 + lightExposure)));
        
        // Cracking - accelerated by temperature fluctuations
        severity.put(DegradationType.PAINT_CRACKING,
            Math.min(1.0, totalAge * 0.001 * (1 + Math.abs(temperature - 20) * 0.05)));
        
        // Canvas sagging
        severity.put(DegradationType.CANVAS_SAGGING,
            Math.min(1.0, totalAge * 0.0008 * (1 + humidity * 0.5)));
        
        // Pigment fading - highly light dependent
        severity.put(DegradationType.PIGMENT_FADING,
            Math.min(1.0, totalAge * 0.001 * lightExposure * 2));
        
        // Mold risk
        if (humidity > 0.65) {
            severity.put(DegradationType.MOLD, Math.min(1.0, (humidity - 0.65) * 3));
        }
        
        // Calculate overall condition
        double avgSeverity = severity.values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0);
        double overall = Math.max(0, 1.0 - avgSeverity);
        
        return new DegradationState(totalAge, severity, overall);
    }

    /**
     * Recommends conservation treatments based on current state.
     */
    public static List<ConservationTreatment> recommendTreatments(DegradationState state,
            double budget) {
        
        List<ConservationTreatment> recommended = new ArrayList<>();
        double remainingBudget = budget;
        
        // Priority order by severity
        List<Map.Entry<DegradationType, Double>> sorted = state.severity().entrySet()
            .stream()
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .toList();
        
        for (Map.Entry<DegradationType, Double> entry : sorted) {
            if (entry.getValue() < 0.3) continue; // Minor issue
            
            for (ConservationTreatment treatment : TREATMENTS) {
                if (treatment.addresses().contains(entry.getKey()) &&
                    treatment.cost() <= remainingBudget &&
                    !recommended.contains(treatment)) {
                    
                    recommended.add(treatment);
                    remainingBudget -= treatment.cost();
                    break;
                }
            }
        }
        
        return recommended;
    }

    /**
     * Simulates the effect of conservation treatment.
     */
    public static DegradationState applyTreatment(DegradationState state,
            ConservationTreatment treatment) {
        
        Map<DegradationType, Double> newSeverity = new EnumMap<>(state.severity());
        
        for (DegradationType type : treatment.addresses()) {
            double current = newSeverity.getOrDefault(type, 0.0);
            double treated = current * (1 - treatment.effectiveness());
            newSeverity.put(type, treated);
        }
        
        double avgSeverity = newSeverity.values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0);
        
        return new DegradationState(state.ageYears(), newSeverity, 1.0 - avgSeverity);
    }

    /**
     * Predicts future condition without intervention.
     */
    public static List<DegradationState> projectCondition(DegradationState current,
            int yearsAhead, int intervalYears, Map<String, Double> environment) {
        
        List<DegradationState> projection = new ArrayList<>();
        projection.add(current);
        
        for (int y = intervalYears; y <= yearsAhead; y += intervalYears) {
            DegradationState future = simulateAging(current.ageYears(), y, environment);
            projection.add(future);
        }
        
        return projection;
    }
}
