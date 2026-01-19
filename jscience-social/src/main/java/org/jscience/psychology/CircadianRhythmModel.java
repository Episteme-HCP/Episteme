package org.jscience.psychology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models circadian rhythms and cognitive alertness.
 */
public final class CircadianRhythmModel {

    private CircadianRhythmModel() {}

    /**
     * Estimates cognitive alertness (0-1) based on hour of day (24h).
     * Simplified sinusoidal model with a "post-lunch dip".
     * 
     * @param hour Hour of day (0-23)
     */
    public static Real estimateAlertness(int hour) {
        double base = 0.7 + 0.2 * Math.sin((hour - 8) * Math.PI / 12);
        // Post-lunch dip around 14h
        if (hour >= 13 && hour <= 15) base -= 0.15;
        return Real.of(Math.max(0, Math.min(1, base)));
    }

    /**
     * Calculates Jet Lag recovery time in days.
     */
    public static double jetLagRecoveryDays(int timezoneShiftH, boolean eastward) {
        return eastward ? timezoneShiftH / 1.0 : timezoneShiftH / 1.5;
    }
}
