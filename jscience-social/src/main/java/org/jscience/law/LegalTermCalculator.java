package org.jscience.law;

import java.time.LocalDate;

/**
 * Calculators for legal terms and statutes of limitations.
 */
public final class LegalTermCalculator {

    private LegalTermCalculator() {}

    /**
     * Calculates the expiry date for a legal action.
     * 
     * @param startDate The date the limitation period begins.
     * @param years The number of years in the limitation period.
     * @return The expiry date.
     */
    public static LocalDate calculateStatuteOfLimitations(LocalDate startDate, int years) {
        if (startDate == null) return null;
        return startDate.plusYears(years);
    }

    /**
     * Checks if a legal action is time-barred.
     * 
     * @param actionDate Date the action was taken.
     * @param expiryDate Expiry date of the limitation period.
     * @return True if barred.
     */
    public static boolean isTimeBarred(LocalDate actionDate, LocalDate expiryDate) {
        if (actionDate == null || expiryDate == null) return false;
        return actionDate.isAfter(expiryDate);
    }
}
