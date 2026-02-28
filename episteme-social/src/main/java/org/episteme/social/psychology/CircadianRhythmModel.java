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

package org.episteme.social.psychology;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Models circadian rhythms and their impact on cognitive alertness and performance.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class CircadianRhythmModel {

    private CircadianRhythmModel() {}

    /**
     * Estimates cognitive alertness (0.0 to 1.0) based on the hour of the day.
     * Uses a simplified sinusoidal model with a characteristic partial drop in the afternoon 
     * (the "post-lunch dip").
     * 
     * @param hour Hour of day (0-23 in 24h format)
     * @return Estimated alertness level, where 1.0 is peak alertness
     */
    public static Real estimateAlertness(int hour) {
        // Base cycle peaking around late morning/early evening
        // Shifted sine wave approximation
        double base = 0.7 + 0.2 * Math.sin((hour - 8) * Math.PI / 12);
        
        // Simulating Post-lunch dip around 13h-15h
        if (hour >= 13 && hour <= 15) {
            base -= 0.15;
        }
        
        return Real.of(Math.max(0.0, Math.min(1.0, base)));
    }

    /**
     * Calculates estimated Jet Lag recovery time in days.
     * Based on general physiological recovery rates, which are typically faster 
     * for westward travel (lengthening day) than eastward travel (shortening day).
     *
     * @param timezoneShiftH Number of timezones crossed (hours)
     * @param eastward       True if traveling East, False if West
     * @return Estimated days to full recovery
     */
    public static double jetLagRecoveryDays(int timezoneShiftH, boolean eastward) {
        // Rule of thumb: 1 day per timezone East, 1.5 timezones per day West
        // Meaning West is faster (hours / 1.5) vs East (hours / 1.0)
        return eastward ? (double) timezoneShiftH : (double) timezoneShiftH / 1.5;
    }
}

