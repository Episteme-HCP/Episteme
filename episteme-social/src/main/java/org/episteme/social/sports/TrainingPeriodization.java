/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sports;

import java.io.Serializable;
import java.util.List;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Time;
import org.episteme.core.measure.quantity.Dimensionless;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;

/**
 * Handles training periodization logic, including cycle generation and load monitoring.
 * <p>
 * Provides implementations for Chronic Training Load (CTL) and Acute Training Load (ATL) 
 * based on the impulse-response models developed by Eric Banister and popularized 
 * by Dr. Andrew Coggan (TSS/NP/IF metrics).
 * </p>
 * <p>
 * References:
 * <ul>
 *   <li>Banister, E. W. (1991). Modeling Elite Athletic Performance. In Physiological Testing of the High-Performance Athlete.</li>
 *   <li>Coggan, A. R. (2003). Training and racing with a power meter.</li>
 * </ul>
 * </p>
 */
public final class TrainingPeriodization {

    private TrainingPeriodization() {}

    /** Phases of a training period (Macro/Meso cycles). */
    public enum MesoPhase {
        ANATOMICAL_ADAPTATION, HYPERTROPHY, STRENGTH, POWER, 
        ENDURANCE_BASE, THRESHOLD, VO2MAX, COMPETITION, RECOVERY
    }

    /** Descriptive training intensity levels based on functional threshold ratios. */
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
        Quantity<Time> volume,
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
        Quantity<Time> duration,
        double intensity,
        String description,
        List<String> exercises
    ) implements Serializable {}

    /** 
     * Calculates the Chronic Training Load (CTL), representing long-term fitness.
     * CTL is an exponentially weighted moving average (EWMA) of daily TSS, 
     * typically using a 42-day time constant.
     * 
     * @param dailyTSS List of daily Training Stress Scores (TSS)
     * @param timeConstant The time constant (tau), usually 42 days.
     * @return The CTL value (dimensionless).
     */
    public static Quantity<Dimensionless> calculateCTL(List<Quantity<Dimensionless>> dailyTSS, int timeConstant) {
        if (dailyTSS == null || dailyTSS.isEmpty()) return Quantities.create(0.0, Units.ONE);
        double sum = 0, weight = 0;
        int n = Math.min(timeConstant * 2, dailyTSS.size()); // Sample window
        for (int i = 0; i < n; i++) {
            double w = Math.exp(-i / (double) timeConstant);
            sum += dailyTSS.get(dailyTSS.size() - 1 - i).getValue().doubleValue() * w;
            weight += w;
        }
        return Quantities.create(sum / weight, Units.ONE);
    }

    /** 
     * Calculates Acute Training Load (ATL), representing short-term fatigue.
     * ATL is an EWMA of daily TSS using a short time constant (typically 7 days).
     * 
     * @param dailyTSS List of daily Training Stress Scores (TSS)
     * @return The ATL value (dimensionless).
     */
    public static Quantity<Dimensionless> calculateATL(List<Quantity<Dimensionless>> dailyTSS) {
        return calculateCTL(dailyTSS, 7);
    }

    /**
     * Calculates Training Stress Balance (TSB), representing freshness.
     * Formula: TSB = CTL - ATL.
     * 
     * @param ctl Chronic Training Load (Fitness)
     * @param atl Acute Training Load (Fatigue)
     * @return Training Stress Balance (Freshness)
     */
    public static Quantity<Dimensionless> calculateTSB(Quantity<Dimensionless> ctl, Quantity<Dimensionless> atl) {
        return (Quantity<Dimensionless>) ctl.subtract(atl);
    }
}

