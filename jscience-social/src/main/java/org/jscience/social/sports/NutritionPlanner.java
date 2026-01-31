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

package org.jscience.social.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Provides mathematical methods for athlete nutrition planning and metabolic analysis.
 * Includes BMR and TDEE calculations using the Mifflin-St Jeor equation.
 */
public final class NutritionPlanner {

    private NutritionPlanner() {}

    /** Standard categorical activity levels for metabolic calculations.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum ActivityLevel {
        SEDENTARY(1.2),
        LIGHT(1.375),
        MODERATE(1.55),
        ACTIVE(1.725),
        VERY_ACTIVE(1.9),
        ELITE_ATHLETE(2.2);

        private final double factor;
        ActivityLevel(double factor) { this.factor = factor; }
        public double getFactor() { return factor; }
    }

    /** Training cycle phases for nutrient adjustment. */
    public enum TrainingPhase {
        OFF_SEASON, BASE, BUILD, COMPETITION, RECOVERY
    }

    /** Physiological data for an athlete. */
    public record AthleteProfile(
        double massKg,
        double heightCm,
        int age,
        boolean isMale,
        String sport,
        ActivityLevel activityLevel,
        TrainingPhase phase
    ) implements Serializable {}

    /** Macro-nutrient distribution targets. */
    public record MacroTargets(
        double calories,
        double proteinGrams,
        double carbsGrams,
        double fatGrams,
        double fiberGrams
    ) implements Serializable {}

    /** Guidelines for fluid intake. */
    public record HydrationPlan(
        double dailyWaterLiters,
        double preworkoutMl,
        double duringWorkoutMlPerHour,
        double postWorkoutMl,
        boolean needsElectrolytes
    ) implements Serializable {}

    /** Optimized meal schedule relative to training. */
    public record MealTiming(
        int hoursBeforeTraining,
        String preTrainingMeal,
        String duringTraining,
        String postTrainingImmediate,
        String postTrainingMeal
    ) implements Serializable {}

    /** Calculates Basal Metabolic Rate (BMR) for the athlete. */
    public static Real calculateBMR(AthleteProfile athlete) {
        double bmr = (10.0 * athlete.massKg()) + (6.25 * athlete.heightCm()) - (5.0 * athlete.age());
        bmr += athlete.isMale() ? 5.0 : -161.0;
        return Real.of(bmr);
    }

    /** 
     * Calculates Total Daily Energy Expenditure (TDEE).
     */
    public static Real calculateTDEE(AthleteProfile athlete) {
        double bmr = calculateBMR(athlete).doubleValue();
        double tdee = bmr * athlete.activityLevel().getFactor();
        tdee *= switch (athlete.phase()) {
            case BUILD -> 1.1;
            case COMPETITION -> 1.05;
            case RECOVERY -> 0.9;
            default -> 1.0;
        };
        return Real.of(tdee);
    }

    /** Generates recommended macro-nutrient targets for a specific goal. */
    public static MacroTargets generateMacroTargets(AthleteProfile athlete, String goal) {
        double tdee = calculateTDEE(athlete).doubleValue();
        double calories = "fat loss".equalsIgnoreCase(goal) ? tdee * 0.85 : ("muscle gain".equalsIgnoreCase(goal) ? tdee * 1.1 : tdee);
        
        double proteinPerKg = athlete.sport().toLowerCase().contains("strength") ? 2.2 : (athlete.sport().toLowerCase().contains("endurance") ? 1.4 : 1.8);
        double protein = athlete.massKg() * proteinPerKg;
        double fatCalories = calories * 0.25;
        double carbCalories = calories - (protein * 4.0) - fatCalories;
        
        return new MacroTargets(calories, protein, carbCalories / 4.0, fatCalories / 9.0, (calories / 1000.0) * 14.0);
    }

    /** Generates a hydration strategy based on intensity and climate. */
    public static HydrationPlan generateHydrationPlan(AthleteProfile athlete,
            double trainingHours, double ambientTemperatureC) {
        double baseWater = athlete.massKg() * 0.033;
        double trainingWater = trainingHours * 0.5 * (ambientTemperatureC > 25 ? 1.5 : 1.0);
        return new HydrationPlan(baseWater + trainingWater, 500.0, ambientTemperatureC > 25 ? 800.0 : 500.0, trainingHours * 500.0, trainingHours > 1 || ambientTemperatureC > 25);
    }

    /** Provides general supplement guidelines. */
    public static List<String> supplementRecommendations(AthleteProfile athlete) {
        List<String> res = new ArrayList<>();
        res.add("Vitamin D (1000-2000 IU)");
        res.add("Omega-3 (1-2g)");
        if (athlete.sport().toLowerCase().contains("strength")) res.add("Creatine monohydrate (5g)");
        return res;
    }
}

