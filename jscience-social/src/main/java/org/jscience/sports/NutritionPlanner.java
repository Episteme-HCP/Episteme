package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Athlete nutrition planning and analysis.
 */
public final class NutritionPlanner {

    private NutritionPlanner() {}

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

    public enum TrainingPhase {
        OFF_SEASON, BASE, BUILD, COMPETITION, RECOVERY
    }

    public record AthleteProfile(
        double massKg,
        double heightCm,
        int age,
        boolean isMale,
        String sport,
        ActivityLevel activityLevel,
        TrainingPhase phase
    ) {}

    public record MacroTargets(
        double calories,
        double proteinGrams,
        double carbsGrams,
        double fatGrams,
        double fiberGrams
    ) {}

    public record HydrationPlan(
        double dailyWaterLiters,
        double preworkoutMl,
        double duringWorkoutMlPerHour,
        double postWorkoutMl,
        boolean needsElectrolytes
    ) {}

    public record MealTiming(
        int hoursBeforeTraining,
        String preTrainingMeal,
        String duringTraining,
        String postTrainingImmediate,
        String postTrainingMeal
    ) {}

    /**
     * Calculates Basal Metabolic Rate using Mifflin-St Jeor equation.
     */
    public static Real calculateBMR(AthleteProfile athlete) {
        double bmr;
        if (athlete.isMale()) {
            bmr = 10 * athlete.massKg() + 6.25 * athlete.heightCm() - 5 * athlete.age() + 5;
        } else {
            bmr = 10 * athlete.massKg() + 6.25 * athlete.heightCm() - 5 * athlete.age() - 161;
        }
        return Real.of(bmr);
    }

    /**
     * Calculates Total Daily Energy Expenditure.
     */
    public static Real calculateTDEE(AthleteProfile athlete) {
        double bmr = calculateBMR(athlete).doubleValue();
        double tdee = bmr * athlete.activityLevel().getFactor();
        
        // Adjust for training phase
        tdee *= switch (athlete.phase()) {
            case BUILD -> 1.1;
            case COMPETITION -> 1.05;
            case RECOVERY -> 0.9;
            default -> 1.0;
        };
        
        return Real.of(tdee);
    }

    /**
     * Generates macro nutrient targets.
     */
    public static MacroTargets generateMacroTargets(AthleteProfile athlete, String goal) {
        double tdee = calculateTDEE(athlete).doubleValue();
        double mass = athlete.massKg();
        
        // Adjust calories for goal
        double calories = switch (goal.toLowerCase()) {
            case "fat loss" -> tdee * 0.85;
            case "muscle gain" -> tdee * 1.1;
            case "maintenance" -> tdee;
            default -> tdee;
        };
        
        // Protein needs by sport type
        double proteinPerKg = switch (athlete.sport().toLowerCase()) {
            case "bodybuilding", "powerlifting" -> 2.2;
            case "endurance", "marathon", "cycling" -> 1.4;
            case "team sports", "football", "soccer" -> 1.8;
            default -> 1.6;
        };
        double protein = mass * proteinPerKg;
        
        // Fat needs (20-35% of calories for athletes)
        double fatCalories = calories * 0.25;
        double fat = fatCalories / 9;
        
        // Remaining calories from carbs
        double proteinCalories = protein * 4;
        double carbCalories = calories - proteinCalories - fatCalories;
        double carbs = carbCalories / 4;
        
        // Fiber
        double fiber = calories / 1000 * 14;
        
        return new MacroTargets(calories, protein, carbs, fat, fiber);
    }

    /**
     * Creates hydration plan.
     */
    public static HydrationPlan generateHydrationPlan(AthleteProfile athlete,
            double trainingHours, double ambientTemperatureC) {
        
        // Base water needs
        double baseWater = athlete.massKg() * 0.033; // 33ml per kg
        
        // Training adjustment
        double trainingWater = trainingHours * 0.5; // 500ml per hour
        
        // Temperature adjustment
        if (ambientTemperatureC > 25) {
            trainingWater *= 1.5;
        }
        
        double totalWater = baseWater + trainingWater;
        
        double preworkout = 500;
        double during = ambientTemperatureC > 25 ? 800 : 500;
        double postworkout = (int)(trainingHours * 500);
        
        boolean needsElectrolytes = trainingHours > 1 || ambientTemperatureC > 25;
        
        return new HydrationPlan(totalWater, preworkout, during, postworkout, needsElectrolytes);
    }

    /**
     * Creates meal timing recommendations.
     */
    public static MealTiming generateMealTiming(String trainingType, int trainingDurationMins) {
        int hoursBefore = trainingType.toLowerCase().contains("strength") ? 2 : 3;
        
        String preMeal = trainingType.toLowerCase().contains("endurance") 
            ? "High carb, moderate protein (pasta, rice with chicken)"
            : "Balanced meal (protein + complex carbs)";
        
        String during = trainingDurationMins > 60
            ? "Carb drink/gel, 30-60g carbs per hour"
            : "Water only";
        
        String postImmediate = "Protein shake + simple carbs within 30 minutes";
        
        String postMeal = "Complete meal within 2 hours: lean protein + carbs + vegetables";
        
        return new MealTiming(hoursBefore, preMeal, during, postImmediate, postMeal);
    }

    /**
     * Calculates supplement recommendations.
     */
    public static List<String> supplementRecommendations(AthleteProfile athlete) {
        List<String> supplements = new ArrayList<>();
        
        // Universal recommendations
        supplements.add("Vitamin D: 1000-2000 IU daily (if limited sun exposure)");
        supplements.add("Omega-3: 1-2g EPA+DHA daily");
        
        // Sport-specific
        if (athlete.sport().toLowerCase().contains("strength") ||
            athlete.sport().toLowerCase().contains("power")) {
            supplements.add("Creatine monohydrate: 3-5g daily");
        }
        
        if (athlete.phase() == TrainingPhase.BUILD || athlete.phase() == TrainingPhase.COMPETITION) {
            supplements.add("Caffeine: 3-6mg/kg before performance (if tolerated)");
        }
        
        // Consider beta-alanine for high-intensity
        if (athlete.activityLevel() == ActivityLevel.ELITE_ATHLETE) {
            supplements.add("Beta-alanine: 3-6g daily for high-intensity performance");
        }
        
        return supplements;
    }
}
