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

package org.episteme.social.history;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.social.history.time.TimeCoordinate;
import org.episteme.social.history.time.FuzzyTimePoint;
import java.util.Objects;

/**
 * Utilities for radiocarbon (C-14) dating calculations and calibration.
 * Translates between radiocarbon ages (Years BP) and calendar dates.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class CarbonDatingConverter {

    private CarbonDatingConverter() {
        // Prevent instantiation
    }

    /** Conventional Libby C-14 half-life in years (used for reporting raw dates). */
    public static final double HALF_LIFE_YEARS = 5568.0;
    
    /** True physical C-14 half-life in years (Cambridge half-life). */
    public static final double TRUE_HALF_LIFE_YEARS = 5730.0;

    /** Decay constant Î» = ln(2) / tÂ½ based on Libby half-life. */
    public static final double DECAY_CONSTANT = Math.log(2) / HALF_LIFE_YEARS;

    /**
     * Converts a radiocarbon age (BP = Before Present, where Present is 1950 CE) 
     * to a calendar date estimate using a simplified linear model.
     * 
     * @param radiocarbonYearsBP radiocarbon age in years BP
     * @return estimated calendar date with calculated uncertainty as a {@link TimeCoordinate}
     */
    public static TimeCoordinate toCalendarDate(double radiocarbonYearsBP) {
        // Simple linear conversion (standard reporting convention)
        int calendarYear = 1950 - (int) Math.round(radiocarbonYearsBP);
        
        // Return a fuzzy interval centered on the calculated year with uncertainty
        // Note: Ideally we would construct a more precise interval, but FuzzyTemporalInterval 
        // works for year-level precision.
        return FuzzyTimePoint.circa(calendarYear);
    }

    /**
     * Calculates the radiocarbon age from the remaining C-14 fraction.
     * Formula: t = -ln(N/Nâ‚€) / Î»
     * 
     * @param remainingFraction the fraction of original C-14 remaining (0.0 - 1.0)
     * @return age in radiocarbon years BP as a {@link Real} number
     * @throws NullPointerException if remainingFraction is null
     */
    public static Real calculateAge(Real remainingFraction) {
        Objects.requireNonNull(remainingFraction, "Remaining fraction cannot be null");
        double fraction = remainingFraction.doubleValue();
        if (fraction <= 0 || fraction > 1.0) {
            return Real.of(Double.NaN);
        }
        double age = -Math.log(fraction) / DECAY_CONSTANT;
        return Real.of(age);
    }

    /**
     * Calculates the remaining C-14 fraction for a given age.
     * Formula: N/Nâ‚€ = e^(-Î»t)
     * 
     * @param ageYearsBP the age in radiocarbon years BP
     * @return remaining fraction (0.0 - 1.0) as a {@link Real} number
     * @throws NullPointerException if ageYearsBP is null
     */
    public static Real remainingFraction(Real ageYearsBP) {
        Objects.requireNonNull(ageYearsBP, "Age cannot be null");
        double t = ageYearsBP.doubleValue();
        return Real.of(Math.exp(-DECAY_CONSTANT * t));
    }

    /**
     * Estimates the maximum dateable age based on a detection limit (approx. 1% remaining C-14).
     * 
     * @return maximum age in years as a {@link Real} number
     */
    public static Real maximumDateableAge() {
        return calculateAge(Real.of(0.01));
    }

    /**
     * Converts a calendar year to its radiocarbon age BP equivalent (1950 reference).
     * 
     * @param calendarYear the calendar year (CE)
     * @return radiocarbon years BP
     */
    public static double toRadiocarbonBP(int calendarYear) {
        return 1950.0 - calendarYear;
    }
}

