package org.jscience.psychology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models human memory retention and forgetting.
 */
public final class MemoryModels {

    private MemoryModels() {}

    /**
     * Ebbinghaus Forgetting Curve: R = exp(-t/S)
     * 
     * @param t Time since learning (days)
     * @param s Stability of memory (strength)
     * @return Retention probability (0-1)
     */
    public static Real ebbinghausRetention(double t, double s) {
        return Real.of(Math.exp(-t / s));
    }

    /**
     * Spaced Repetition: Calculates next interval.
     * I(n) = I(n-1) * EF
     * 
     * @param prevInterval Previous interval in days
     * @param easeFactor Ease factor (e.g., 2.5)
     * @param quality Grade of recall (0-5)
     */
    public static double sm2NextInterval(int iteration, double prevInterval, double easeFactor, int quality) {
        if (iteration == 1) return 1;
        if (iteration == 2) return 6;
        
        if (quality < 3) return 1; // Reset
        return prevInterval * easeFactor;
    }

    /**
     * Adjusts Ease Factor (EF) based on recall quality.
     * NewEF = EF + (0.1 - (5-q)*(0.08 + (5-q)*0.02))
     */
    public static double sm2NewEaseFactor(double oldEf, int quality) {
        double newEf = oldEf + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
        return Math.max(1.3, newEf);
    }

    /**
     * Models the Serial Position Effect (Primacy and Recency).
     * Probability of recall is higher at start and end of a list.
     */
    public static double recallProbability(int position, int totalItems) {
        double x = (double) position / totalItems;
        // U-shaped curve approximation
        return 0.8 * (Math.pow(x - 0.5, 2) * 4) + 0.2;
    }
}
