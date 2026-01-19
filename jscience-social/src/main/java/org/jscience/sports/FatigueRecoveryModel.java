package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Optimizes tapering and recovery phases for athletes.
 */
public final class FatigueRecoveryModel {

    private FatigueRecoveryModel() {}

    /**
     * Banister's Performance Model (Impulse-Response).
     * P(t) = P0 + (Fitness * e^-t/tau1) - (Fatigue * e^-t/tau2)
     */
    public static Real predictPerformance(double p0, double fitness, double fatigue, 
                                          int restDays, double tau1, double tau2) {
        double perf = p0 + (fitness * Math.exp(-restDays / tau1)) - 
                           (fatigue * Math.exp(-restDays / tau2));
        return Real.of(perf);
    }

    /**
     * Finds the optimal tapering duration (days) to maximize performance.
     */
    public static int findOptimalTaper(double fitness, double fatigue, double tau1, double tau2) {
        int bestDay = 0;
        double maxPerf = -Double.MAX_VALUE;
        for (int d = 0; d < 30; d++) {
            double p = predictPerformance(0, fitness, fatigue, d, tau1, tau2).doubleValue();
            if (p > maxPerf) {
                maxPerf = p;
                bestDay = d;
            }
        }
        return bestDay;
    }
}
