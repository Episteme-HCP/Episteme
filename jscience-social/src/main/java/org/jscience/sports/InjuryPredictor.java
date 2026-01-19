package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Injury risk prediction based on training load and physiology.
 */
public final class InjuryPredictor {

    private InjuryPredictor() {}

    public enum InjuryType {
        MUSCLE_STRAIN, LIGAMENT_SPRAIN, STRESS_FRACTURE, 
        TENDINITIS, CONCUSSION, OVERUSE
    }

    public record TrainingLoad(
        double duration,        // hours
        double intensity,       // 0-100 RPE
        double frequency,       // sessions per week
        String type             // endurance, strength, speed, etc.
    ) {}

    public record AthleteProfile(
        int age,
        double bodyMass,        // kg
        double previousInjuries,
        double sleepQuality,    // 0-100
        double nutritionScore,  // 0-100
        double muscleBalance,   // 0-100 (100 = perfect symmetry)
        String sport
    ) {}

    public record InjuryRisk(
        InjuryType type,
        double probability,     // 0-1
        String primaryFactor,
        List<String> recommendations
    ) {}

    /**
     * Calculates Acute:Chronic Workload Ratio (ACWR).
     * ACWR = Acute Load (7 days) / Chronic Load (28 days avg)
     */
    public static Real calculateACWR(List<Double> dailyLoads) {
        if (dailyLoads.size() < 28) {
            return Real.of(1.0); // Insufficient data
        }
        
        int size = dailyLoads.size();
        double acute = 0, chronic = 0;
        
        for (int i = 0; i < 7; i++) {
            acute += dailyLoads.get(size - 1 - i);
        }
        for (int i = 0; i < 28; i++) {
            chronic += dailyLoads.get(size - 1 - i);
        }
        chronic /= 4; // Weekly average
        
        if (chronic == 0) return Real.of(1.0);
        return Real.of(acute / chronic);
    }

    /**
     * Predicts injury risk based on ACWR and athlete profile.
     */
    public static List<InjuryRisk> predictRisks(Real acwr, AthleteProfile athlete, 
            TrainingLoad currentLoad) {
        
        List<InjuryRisk> risks = new ArrayList<>();
        double acwrValue = acwr.doubleValue();
        
        // ACWR sweet spot is 0.8-1.3, danger zones are <0.8 or >1.5
        double acwrRisk = 0;
        if (acwrValue < 0.8) {
            acwrRisk = 0.3; // Undertraining - loss of protective adaptation
        } else if (acwrValue > 1.5) {
            acwrRisk = 0.5 + (acwrValue - 1.5) * 0.3; // Spike in load
        } else if (acwrValue > 1.3) {
            acwrRisk = 0.2;
        }
        
        // Age factor
        double ageFactor = athlete.age() > 30 ? 1.0 + (athlete.age() - 30) * 0.02 : 1.0;
        
        // Previous injury factor
        double historyFactor = 1.0 + athlete.previousInjuries() * 0.15;
        
        // Recovery factors
        double recoveryFactor = (200 - athlete.sleepQuality() - athlete.nutritionScore()) / 200.0;
        
        // Calculate specific risks
        // Muscle strain
        double strainRisk = acwrRisk * ageFactor * historyFactor * 
            (currentLoad.intensity() / 100.0) * (1 + recoveryFactor);
        strainRisk = Math.min(0.8, strainRisk);
        
        if (strainRisk > 0.2) {
            risks.add(new InjuryRisk(
                InjuryType.MUSCLE_STRAIN, strainRisk,
                acwrValue > 1.3 ? "High ACWR" : "Poor recovery",
                generateRecommendations(InjuryType.MUSCLE_STRAIN, acwrValue, athlete)
            ));
        }
        
        // Overuse injuries
        double overuseRisk = (currentLoad.frequency() / 7.0) * historyFactor * 
            (currentLoad.duration() / 20.0);
        overuseRisk = Math.min(0.7, overuseRisk);
        
        if (overuseRisk > 0.15) {
            risks.add(new InjuryRisk(
                InjuryType.OVERUSE, overuseRisk,
                "High training frequency",
                generateRecommendations(InjuryType.OVERUSE, acwrValue, athlete)
            ));
        }
        
        // Muscle imbalance injuries
        if (athlete.muscleBalance() < 85) {
            double imbalanceRisk = (85 - athlete.muscleBalance()) / 100.0 * historyFactor;
            risks.add(new InjuryRisk(
                InjuryType.LIGAMENT_SPRAIN, imbalanceRisk,
                "Muscle asymmetry detected",
                List.of("Corrective exercises", "Unilateral training", "Physical therapy assessment")
            ));
        }
        
        risks.sort((a, b) -> Double.compare(b.probability(), a.probability()));
        return risks;
    }

    /**
     * Calculates safe load progression.
     */
    public static Real safeLoadIncrease(double currentWeeklyLoad) {
        // 10% rule: don't increase more than 10% per week
        return Real.of(currentWeeklyLoad * 1.10);
    }

    private static List<String> generateRecommendations(InjuryType type, 
            double acwr, AthleteProfile athlete) {
        List<String> recs = new ArrayList<>();
        
        if (acwr > 1.3) {
            recs.add("Reduce training load by " + (int)((acwr - 1.0) * 20) + "%");
        }
        if (athlete.sleepQuality() < 70) {
            recs.add("Improve sleep quality - target 8+ hours");
        }
        if (athlete.nutritionScore() < 70) {
            recs.add("Review nutrition - ensure adequate protein intake");
        }
        
        recs.add("Include active recovery sessions");
        recs.add("Monitor soreness and fatigue levels");
        
        return recs;
    }
}
