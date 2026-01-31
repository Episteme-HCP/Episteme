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

package org.jscience.social.law;

import java.time.LocalDate;

/**
 * Provides utility methods for calculating legal terms, deadlines, and statutes of limitations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class LegalTermCalculator {

    private LegalTermCalculator() {
        // Utility class
    }

    /**
     * Calculates the expiry date for a legal action based on a start date and a duration in years.
     * 
     * @param startDate the date the limitation period begins
     * @param years the number of years in the limitation period
     * @return the expiry date, or null if startDate is null
     */
    public static LocalDate calculateStatuteOfLimitations(LocalDate startDate, int years) {
        if (startDate == null) {
            return null;
        }
        return startDate.plusYears(years);
    }

    /**
     * Checks if a legal action taken on a specific date is time-barred according to an expiry date.
     * 
     * @param actionDate the date the action was taken
     * @param expiryDate the expiry date of the limitation period
     * @return true if the action is after the expiry date, false otherwise
     */
    public static boolean isTimeBarred(LocalDate actionDate, LocalDate expiryDate) {
        if (actionDate == null || expiryDate == null) {
            return false;
        }
        return actionDate.isAfter(expiryDate);
    }
}

