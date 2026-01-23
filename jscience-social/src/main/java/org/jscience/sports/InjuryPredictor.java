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

package org.jscience.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Provides injury risk prediction and training load analysis for athletes.
 * Includes implementations for Acute:Chronic Workload Ratio (ACWR).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class InjuryPredictor {

    private InjuryPredictor() {}

    /** Categories of common athletic injuries. */
    public enum InjuryType {
        MUSCLE_STRAIN, LIGAMENT_SPRAIN, STRESS_FRACTURE, 
        TENDINITIS, CONCUSSION, OVERUSE
    }

    /** Detailed data regarding an individual training session. */
    public record TrainingLoad(
        double duration,
        double intensity,
        double frequency,
        String type
    ) implements Serializable {}

    /** Physiological and historical profile of an athlete. */
    public record AthleteProfile(
        int age,
        double bodyMass,
        double previousInjuries,
        double sleepQuality,
        double nutritionScore,
        double muscleBalance,
        String sport
    ) implements Serializable {}

    /** Result of an injury risk assessment. */
    public record InjuryRisk(
        InjuryType type,
        double probability,
        String primaryFactor,
        List<String> recommendations
    ) implements Serializable {}

    /**
     * Calculates the Acute:Chronic Workload Ratio (ACWR) to monitor training spikes.
     * 
     * @param dailyLoads a historical list of daily training intensities
     * @return the ACWR as a Real number
     */
    public static Real calculateACWR(List<Double> dailyLoads) {
        if (dailyLoads == null || dailyLoads.size() < 28) {
            return Real.of(1.0);
        }
        
        int size = dailyLoads.size();
        double acute = 0, chronic = 0;
        
        for (int i = 0; i < 7; i++) {
            acute += dailyLoads.get(size - 1 - i);
        }
        for (int i = 0; i < 28; i++) {
            chronic += dailyLoads.get(size - 1 - i);
        }
        chronic /= 4.0;
        
        return (chronic == 0) ? Real.of(1.0) : Real.of(acute / chronic);
    }

    /**
     * Predicts potential injury risks based on workload ratios and athlete health profiles.
     * 
     * @param acwr        calculated workload ratio
     * @param athlete     athlete profile
     * @param currentLoad the load of the current period
     * @return a list of prioritized injury risks
     */
    public static List<InjuryRisk> predictRisks(Real acwr, AthleteProfile athlete, 
            TrainingLoad currentLoad) {
        
        List<InjuryRisk> risks = new ArrayList<>();
        double acwrValue = acwr.doubleValue();
        
        double acwrRisk = (acwrValue < 0.8) ? 0.3 : (acwrValue > 1.5) ? 0.5 + (acwrValue - 1.5) * 0.3 : (acwrValue > 1.3) ? 0.2 : 0;
        double ageFactor = athlete.age() > 30 ? 1.0 + (athlete.age() - 30) * 0.02 : 1.0;
        double historyFactor = 1.0 + athlete.previousInjuries() * 0.15;
        double recoveryFactor = (200.0 - athlete.sleepQuality() - athlete.nutritionScore()) / 200.0;
        
        double strainRisk = Math.min(0.8, acwrRisk * ageFactor * historyFactor * (currentLoad.intensity() / 100.0) * (1.0 + recoveryFactor));
        if (strainRisk > 0.2) {
            risks.add(new InjuryRisk(InjuryType.MUSCLE_STRAIN, strainRisk, (acwrValue > 1.3 ? "High ACWR" : "Poor recovery"), generateRecommendations(InjuryType.MUSCLE_STRAIN, acwrValue, athlete)));
        }
        
        double overuseRisk = Math.min(0.7, (currentLoad.frequency() / 7.0) * historyFactor * (currentLoad.duration() / 20.0));
        if (overuseRisk > 0.15) {
            risks.add(new InjuryRisk(InjuryType.OVERUSE, overuseRisk, "High training frequency", generateRecommendations(InjuryType.OVERUSE, acwrValue, athlete)));
        }
        
        if (athlete.muscleBalance() < 85) {
            double imbalanceRisk = (85.0 - athlete.muscleBalance()) / 100.0 * historyFactor;
            risks.add(new InjuryRisk(InjuryType.LIGAMENT_SPRAIN, imbalanceRisk, "Muscle asymmetry detected", List.of("Corrective exercises", "Unilateral training")));
        }
        
        risks.sort((a, b) -> Double.compare(b.probability(), a.probability()));
        return risks;
    }

    /** Calculates a safe training load progression limit. */
    public static Real safeLoadIncrease(double currentWeeklyLoad) {
        return Real.of(currentWeeklyLoad * 1.10);
    }

    private static List<String> generateRecommendations(InjuryType type, double acwr, AthleteProfile athlete) {
        List<String> recs = new ArrayList<>();
        if (acwr > 1.3) recs.add("Reduce training load");
        if (athlete.sleepQuality() < 70) recs.add("Improve sleep quality");
        if (athlete.nutritionScore() < 70) recs.add("Ensure adequate protein intake");
        recs.add("Active recovery sessions");
        return recs;
    }
}
