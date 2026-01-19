package org.jscience.sports;

import org.jscience.history.time.UncertainDate;
import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Tracks and analyzes world records with trend detection.
 */
public final class RecordTracker {

    private RecordTracker() {}

    public record WorldRecord(
        String discipline,
        String category,      // Men's, Women's, Mixed
        Real performance,
        String unit,
        String athlete,
        String nationality,
        UncertainDate date,
        String venue,
        boolean isHigherBetter // true for distance/score, false for time
    ) {}

    public record RecordProgression(
        String discipline,
        List<WorldRecord> history,
        Real improvementRate,  // Per year
        Real predictedNextRecord
    ) {}

    private static final List<WorldRecord> RECORD_DATABASE = new ArrayList<>();

    /**
     * Adds a new record to the database.
     */
    public static void addRecord(WorldRecord record) {
        RECORD_DATABASE.add(record);
    }

    /**
     * Gets the current world record for a discipline.
     */
    public static Optional<WorldRecord> getCurrentRecord(String discipline, String category) {
        return RECORD_DATABASE.stream()
            .filter(r -> r.discipline().equalsIgnoreCase(discipline))
            .filter(r -> r.category().equalsIgnoreCase(category))
            .max((a, b) -> {
                if (a.isHigherBetter()) {
                    return a.performance().compareTo(b.performance());
                } else {
                    return b.performance().compareTo(a.performance());
                }
            });
    }

    /**
     * Gets the progression of records over time for a discipline.
     */
    public static RecordProgression getProgression(String discipline, String category) {
        List<WorldRecord> history = RECORD_DATABASE.stream()
            .filter(r -> r.discipline().equalsIgnoreCase(discipline))
            .filter(r -> r.category().equalsIgnoreCase(category))
            .sorted((a, b) -> a.date().compareTo(b.date()))
            .toList();
        
        if (history.size() < 2) {
            return new RecordProgression(discipline, history, Real.ZERO, Real.ZERO);
        }
        
        // Calculate improvement rate using linear regression
        double[] performances = history.stream()
            .mapToDouble(r -> r.performance().doubleValue())
            .toArray();
        
        // Simple linear regression
        int n = performances.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += performances[i];
            sumXY += i * performances[i];
            sumX2 += i * i;
        }
        
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        
        // Adjust slope direction based on discipline type
        boolean higherBetter = history.get(0).isHigherBetter();
        double annualImprovement = Math.abs(slope); // Per record, approximate to annual
        
        // Predict next record
        double lastPerformance = history.get(history.size() - 1).performance().doubleValue();
        double predicted = higherBetter 
            ? lastPerformance + annualImprovement 
            : lastPerformance - annualImprovement;
        
        return new RecordProgression(
            discipline, 
            history, 
            Real.of(annualImprovement),
            Real.of(predicted)
        );
    }

    /**
     * Detects records that are "plateauing" (improvement slowing down).
     */
    public static boolean isPlateauing(String discipline, String category, int recentRecords) {
        RecordProgression prog = getProgression(discipline, category);
        if (prog.history().size() < recentRecords * 2) return false;
        
        List<WorldRecord> history = prog.history();
        int n = history.size();
        
        // Compare recent improvement rate to historical
        double recentImprovement = 0;
        double historicalImprovement = 0;
        
        for (int i = n - recentRecords; i < n - 1; i++) {
            recentImprovement += Math.abs(
                history.get(i + 1).performance().doubleValue() - 
                history.get(i).performance().doubleValue()
            );
        }
        recentImprovement /= (recentRecords - 1);
        
        for (int i = 0; i < n - recentRecords - 1; i++) {
            historicalImprovement += Math.abs(
                history.get(i + 1).performance().doubleValue() - 
                history.get(i).performance().doubleValue()
            );
        }
        historicalImprovement /= (n - recentRecords - 1);
        
        return recentImprovement < historicalImprovement * 0.5;
    }

    /**
     * Calculates the theoretical limit using asymptotic modeling.
     */
    public static Real estimateTheoreticalLimit(String discipline, String category) {
        RecordProgression prog = getProgression(discipline, category);
        
        if (prog.history().size() < 5) {
            return prog.history().isEmpty() 
                ? Real.ZERO 
                : prog.history().get(prog.history().size() - 1).performance();
        }
        
        // Simplified logistic curve fitting
        double current = prog.history().get(prog.history().size() - 1).performance().doubleValue();
        double rate = prog.improvementRate().doubleValue();
        boolean higherBetter = prog.history().get(0).isHigherBetter();
        
        // Asymptote estimate (very simplified)
        double limit = higherBetter 
            ? current * (1 + rate / current * 10) 
            : current * (1 - rate / current * 10);
        
        return Real.of(limit);
    }
}
