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
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Handles training periodization logic, including cycle generation and load monitoring.
 * Provides implementations for Chronic Training Load (CTL) and Acute Training Load (ATL).
 */
public final class TrainingPeriodization {

    private TrainingPeriodization() {}

    /** Phases of a training period (Macro/Meso cycles).
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum MesoPhase {
        ANATOMICAL_ADAPTATION, HYPERTROPHY, STRENGTH, POWER, 
        ENDURANCE_BASE, THRESHOLD, VO2MAX, COMPETITION, RECOVERY
    }

    /** Descriptive training intensity levels. */
    public enum TrainingLoad {
        RECOVERY(0.4), LOW(0.6), MODERATE(0.75), HIGH(0.85), MAX(0.95);
        private final double intensity;
        TrainingLoad(double intensity) { this.intensity = intensity; }
        public double getIntensity() { return intensity; }
    }

    /** Represents a specific week's training goals and metrics. */
    public record TrainingWeek(
        int weekNumber,
        MesoPhase phase,
        TrainingLoad load,
        double volumeHours,
        double intensityAvg,
        String focus,
        List<String> keyWorkouts
    ) implements Serializable {}

    /** Top-level training cycle (typically 3-12 months). */
    public record Macrocycle(
        String name,
        int totalWeeks,
        List<TrainingWeek> weeks,
        String targetEvent,
        int targetEventWeek
    ) implements Serializable {}

    /** Standardized workout configuration. */
    public record WorkoutSession(
        String name,
        String type,
        int durationMinutes,
        double intensity,
        String description,
        List<String> exercises
    ) implements Serializable {}

    /** 
     * Calculates the Chronic Training Load (CTL), representing long-term fitness.
     * Calculated as an exponentially weighted average.
     */
    public static Real calculateCTL(List<Double> dailyTSS, int days) {
        if (dailyTSS == null || dailyTSS.isEmpty()) return Real.ZERO;
        double sum = 0, weight = 0;
        int n = Math.min(days, dailyTSS.size());
        for (int i = 0; i < n; i++) {
            double w = Math.exp(-i / (double) days);
            sum += dailyTSS.get(dailyTSS.size() - 1 - i) * w;
            weight += w;
        }
        return Real.of(sum / weight);
    }

    /** Calculates Acute Training Load (ATL), representing short-term fatigue (7-day window). */
    public static Real calculateATL(List<Double> dailyTSS) {
        return calculateCTL(dailyTSS, 7);
    }
}

