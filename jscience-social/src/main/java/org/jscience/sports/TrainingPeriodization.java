package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Training periodization planning for athletes.
 */
public final class TrainingPeriodization {

    private TrainingPeriodization() {}

    public enum MesoPhase {
        ANATOMICAL_ADAPTATION, HYPERTROPHY, STRENGTH, POWER, 
        ENDURANCE_BASE, THRESHOLD, VO2MAX, COMPETITION, RECOVERY
    }

    public enum TrainingLoad {
        RECOVERY(0.4), LOW(0.6), MODERATE(0.75), HIGH(0.85), MAX(0.95);

        private final double intensity;
        TrainingLoad(double intensity) { this.intensity = intensity; }
        public double getIntensity() { return intensity; }
    }

    public record TrainingWeek(
        int weekNumber,
        MesoPhase phase,
        TrainingLoad load,
        double volumeHours,
        double intensityAvg,
        String focus,
        List<String> keyWorkouts
    ) {}

    public record Macrocycle(
        String name,
        int totalWeeks,
        List<TrainingWeek> weeks,
        String targetEvent,
        int targetEventWeek
    ) {}

    public record WorkoutSession(
        String name,
        String type,
        int durationMinutes,
        double intensity,
        String description,
        List<String> exercises
    ) {}

    /**
     * Generates a periodization plan for endurance sports.
     */
    public static Macrocycle createEndurancePlan(int weeks, int targetEventWeek) {
        List<TrainingWeek> plan = new ArrayList<>();
        
        // Base building (40% of time)
        int baseWeeks = weeks * 40 / 100;
        // Build phase (30%)
        int buildWeeks = weeks * 30 / 100;
        // Peak/competition (20%)
        int peakWeeks = weeks * 20 / 100;
        // Taper (10%)
        int taperWeeks = weeks - baseWeeks - buildWeeks - peakWeeks;
        
        int week = 1;
        
        // Base phase
        for (int i = 0; i < baseWeeks; i++) {
            TrainingLoad load = (i + 1) % 4 == 0 ? TrainingLoad.RECOVERY : TrainingLoad.MODERATE;
            plan.add(new TrainingWeek(week++, MesoPhase.ENDURANCE_BASE, load,
                8 + i * 0.5, 0.65, "Aerobic base development",
                List.of("Long slow distance", "Easy recovery runs", "Cross-training")));
        }
        
        // Build phase
        for (int i = 0; i < buildWeeks; i++) {
            TrainingLoad load = (i + 1) % 3 == 0 ? TrainingLoad.RECOVERY : TrainingLoad.HIGH;
            MesoPhase phase = i < buildWeeks / 2 ? MesoPhase.THRESHOLD : MesoPhase.VO2MAX;
            plan.add(new TrainingWeek(week++, phase, load,
                10 + i * 0.3, 0.75, "Building race fitness",
                List.of("Tempo runs", "Interval training", "Race pace work")));
        }
        
        // Peak phase
        for (int i = 0; i < peakWeeks; i++) {
            plan.add(new TrainingWeek(week++, MesoPhase.COMPETITION, TrainingLoad.MAX,
                8, 0.85, "Race sharpening",
                List.of("Race simulations", "Speed work", "Tune-up races")));
        }
        
        // Taper
        for (int i = 0; i < taperWeeks; i++) {
            double volume = 8 * (1 - (i + 1) * 0.2);
            plan.add(new TrainingWeek(week++, MesoPhase.COMPETITION, TrainingLoad.LOW,
                volume, 0.75, "Taper and sharpen",
                List.of("Short intervals", "Strides", "Easy runs")));
        }
        
        return new Macrocycle("Endurance Plan", weeks, plan, "Race", targetEventWeek);
    }

    /**
     * Generates a periodization plan for strength/power sports.
     */
    public static Macrocycle createStrengthPlan(int weeks, int targetEventWeek) {
        List<TrainingWeek> plan = new ArrayList<>();
        
        int week = 1;
        
        // Anatomical adaptation (3-4 weeks)
        for (int i = 0; i < 4; i++) {
            TrainingLoad load = i == 3 ? TrainingLoad.RECOVERY : TrainingLoad.MODERATE;
            plan.add(new TrainingWeek(week++, MesoPhase.ANATOMICAL_ADAPTATION, load,
                6, 0.60, "Prepare tissues for heavier loading",
                List.of("High rep work (12-15)", "Movement quality focus", "Core stability")));
        }
        
        // Hypertrophy (4-6 weeks)
        for (int i = 0; i < 5; i++) {
            TrainingLoad load = (i + 1) % 3 == 0 ? TrainingLoad.RECOVERY : TrainingLoad.HIGH;
            plan.add(new TrainingWeek(week++, MesoPhase.HYPERTROPHY, load,
                8, 0.70, "Build muscle mass",
                List.of("Moderate reps (8-12)", "Volume accumulation", "Accessory work")));
        }
        
        // Strength (4-6 weeks)
        for (int i = 0; i < 5; i++) {
            TrainingLoad load = (i + 1) % 3 == 0 ? TrainingLoad.RECOVERY : TrainingLoad.HIGH;
            plan.add(new TrainingWeek(week++, MesoPhase.STRENGTH, load,
                6, 0.80, "Build maximal strength",
                List.of("Heavy compound lifts (3-6 reps)", "Progressive overload", "Competition lifts")));
        }
        
        // Power/peaking
        int remaining = weeks - week + 1;
        for (int i = 0; i < remaining; i++) {
            TrainingLoad load = i == remaining - 1 ? TrainingLoad.LOW : TrainingLoad.MAX;
            plan.add(new TrainingWeek(week++, MesoPhase.POWER, load,
                4, 0.90, "Peak for competition",
                List.of("Heavy singles/doubles", "Explosive work", "Skill practice")));
        }
        
        return new Macrocycle("Strength Plan", weeks, plan, "Competition", targetEventWeek);
    }

    /**
     * Calculates training load metrics.
     */
    public static Real calculateTrainingStress(double duration, double intensity, double threshold) {
        // Training Stress Score formula
        double tss = (duration * intensity * intensity) / (threshold * 3600) * 100;
        return Real.of(tss);
    }

    /**
     * Calculates chronic training load (fitness).
     */
    public static Real calculateCTL(List<Double> dailyTSS, int days) {
        if (dailyTSS.size() < days) return Real.ZERO;
        
        // Exponentially weighted average (last 42 days typically)
        double sum = 0;
        double weight = 0;
        for (int i = 0; i < Math.min(days, dailyTSS.size()); i++) {
            double w = Math.exp(-i / (double) days);
            sum += dailyTSS.get(dailyTSS.size() - 1 - i) * w;
            weight += w;
        }
        
        return Real.of(sum / weight);
    }

    /**
     * Calculates acute training load (fatigue).
     */
    public static Real calculateATL(List<Double> dailyTSS) {
        // 7-day average
        return calculateCTL(dailyTSS, 7);
    }

    /**
     * Calculates Training Stress Balance (form).
     */
    public static Real calculateTSB(Real ctl, Real atl) {
        return ctl.subtract(atl);
    }
}
