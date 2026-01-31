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

package org.jscience.social.arts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Analytical engine for simulating the natural aging and chemical degradation 
 * of artworks over time, as well as the corrective impacts of various 
 * conservation and restoration treatments.
 */
public final class RestorationSimulator {

    private RestorationSimulator() {}

    /**
     * Common forms of chemical and physical degradation in fine art materials.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum DegradationType {
        /** Oxidation leading to yellowing of natural resin varnishes. */
        VARNISH_YELLOWING, 
        /** Mechanical stress leading to craquelure and paint loss. */
        PAINT_CRACKING, 
        /** Loss of tension in canvas support due to humidity cycling. */
        CANVAS_SAGGING,
        /** Photochemical degradation of organic dyes and pigments. */
        PIGMENT_FADING, 
        /** Chemical reaction with atmospheric pollutants. */
        OXIDATION, 
        /** Biological growth on moist organic substrates. */
        MOLD, 
        /** Separation of layers due to adhesive failure. */
        DELAMINATION, 
        /** Small reddish-brown spots on paper caused by metal oxidation. */
        FOXING, 
        /** Irreversible structural damage from aqueous exposure. */
        WATER_DAMAGE, 
        /** Destruction of wood or canvas by larvae or termites. */
        INSECT_DAMAGE
    }

    /**
     * Represents the quantified state of damage for an artwork.
     */
    public record DegradationState(
        int ageYears,
        Map<DegradationType, Double> severity,  // Index from 0.0 to 1.0
        double overallCondition                  // Reliability index from 0.0 to 1.0
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Represents a specific technical intervention.
     */
    public record ConservationTreatment(
        String name,
        Set<DegradationType> addresses,
        double effectiveness,  // Improvement coefficient from 0.0 to 1.0
        double cost,
        int durationDays
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /** Catalog of standard conservation treatments. */
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
     * Simulates the progression of degradation based on time and environmental exposure.
     * 
     * @param currentAge starting age of the artwork in years
     * @param additionalYears time span of the simulation
     * @param environmentFactors map containing "humidity" (0-1), "temperature" (Celsius), and "light" (0-1)
     * @return predicted DegradationState
     */
    public static DegradationState simulateAging(int currentAge, int additionalYears,
            Map<String, Double> environmentFactors) {
        
        Map<DegradationType, Double> severity = new EnumMap<>(DegradationType.class);
        if (environmentFactors == null) environmentFactors = Map.of();
        
        double humidity = environmentFactors.getOrDefault("humidity", 0.5);
        double temperature = environmentFactors.getOrDefault("temperature", 20.0);
        double lightExposure = environmentFactors.getOrDefault("light", 0.5);
        
        int totalAge = Math.max(0, currentAge + additionalYears);
        
        // Semi-empirical kinetic models for degradation
        severity.put(DegradationType.VARNISH_YELLOWING,
            Math.min(1.0, totalAge * 0.002 * (1 + lightExposure)));
        
        severity.put(DegradationType.PAINT_CRACKING,
            Math.min(1.0, totalAge * 0.001 * (1 + Math.abs(temperature - 20) * 0.05)));
        
        severity.put(DegradationType.CANVAS_SAGGING,
            Math.min(1.0, totalAge * 0.0008 * (1 + humidity * 0.5)));
        
        severity.put(DegradationType.PIGMENT_FADING,
            Math.min(1.0, totalAge * 0.001 * lightExposure * 2));
        
        if (humidity > 0.65) {
            severity.put(DegradationType.MOLD, Math.min(1.0, (humidity - 0.65) * 3));
        }
        
        double avgSeverity = severity.values().stream()
            .mapToDouble(Double::doubleValue).average().orElse(0);
        
        return new DegradationState(totalAge, severity, Math.max(0, 1.0 - avgSeverity));
    }

    /**
     * Provides an prioritized list of recommended treatments constrained by a financial budget.
     * 
     * @param state current damage metrics
     * @param budget maximum available funding
     * @return ordered list of viable treatments
     */
    public static List<ConservationTreatment> recommendTreatments(DegradationState state,
            double budget) {
        
        List<ConservationTreatment> recommended = new ArrayList<>();
        if (state == null) return recommended;
        
        double remainingBudget = budget;
        
        List<Map.Entry<DegradationType, Double>> sorted = state.severity().entrySet()
            .stream()
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .toList();
        
        for (Map.Entry<DegradationType, Double> entry : sorted) {
            if (entry.getValue() < 0.3) continue;
            
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
     * Projects the outcome of applying a specific treatment to the current state.
     * 
     * @param state source degradation state
     * @param treatment treatment to apply
     * @return improved DegradationState
     */
    public static DegradationState applyTreatment(DegradationState state,
            ConservationTreatment treatment) {
        if (state == null) return null;
        if (treatment == null) return state;
        
        Map<DegradationType, Double> newSeverity = new EnumMap<>(state.severity());
        for (DegradationType type : treatment.addresses()) {
            double current = newSeverity.getOrDefault(type, 0.0);
            double treated = current * (1 - Math.max(0, Math.min(1.0, treatment.effectiveness())));
            newSeverity.put(type, treated);
        }
        
        double avgSeverity = newSeverity.values().stream()
            .mapToDouble(Double::doubleValue).average().orElse(0);
        
        return new DegradationState(state.ageYears(), newSeverity, 1.0 - avgSeverity);
    }
}

