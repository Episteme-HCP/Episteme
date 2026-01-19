package org.jscience.history;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.history.time.UncertainDate;
import java.time.LocalDate;

/**
 * Carbon-14 dating calibration and conversion utilities.
 */
public final class CarbonDatingConverter {

    private CarbonDatingConverter() {}

    /** C-14 half-life in years (Libby half-life, conventionally used). */
    public static final double HALF_LIFE_YEARS = 5568.0;
    
    /** True C-14 half-life (Cambridge half-life). */
    public static final double TRUE_HALF_LIFE_YEARS = 5730.0;

    /** Decay constant λ = ln(2) / t½ */
    public static final double DECAY_CONSTANT = Math.log(2) / HALF_LIFE_YEARS;

    /**
     * Converts a radiocarbon age (BP = Before Present, where Present = 1950 CE) 
     * to a calendar date estimate.
     * 
     * Note: This is a simplified linear conversion. Real calibration requires
     * IntCal curves for accurate results.
     * 
     * @param radiocarbonYearsBP Radiocarbon age in years BP.
     * @return Estimated calendar date.
     */
    public static UncertainDate toCalendarDate(double radiocarbonYearsBP) {
        // Simple linear conversion (real calibration is non-linear)
        int calendarYear = 1950 - (int) Math.round(radiocarbonYearsBP);
        
        // Typical uncertainty is ±40-100 years for recent, more for older samples
        double uncertainty = Math.max(40, radiocarbonYearsBP * 0.02);
        
        LocalDate central = calendarYear > 0 
            ? LocalDate.of(calendarYear, 1, 1)
            : LocalDate.of(1, 1, 1).minusYears(Math.abs(calendarYear));
            
        LocalDate earliest = central.minusYears((long) uncertainty);
        LocalDate latest = central.plusYears((long) uncertainty);
        
        return UncertainDate.between(earliest, latest);
    }

    /**
     * Calculates the radiocarbon age from the remaining C-14 fraction.
     * t = -ln(N/N₀) / λ
     * 
     * @param remainingFraction The fraction of original C-14 remaining (0-1).
     * @return Age in radiocarbon years BP.
     */
    public static Real calculateAge(Real remainingFraction) {
        if (remainingFraction.compareTo(Real.ZERO) <= 0 || 
            remainingFraction.compareTo(Real.of(1.0)) > 0) {
            return Real.of(Double.NaN);
        }
        double fraction = remainingFraction.doubleValue();
        double age = -Math.log(fraction) / DECAY_CONSTANT;
        return Real.of(age);
    }

    /**
     * Calculates the remaining C-14 fraction for a given age.
     * N/N₀ = e^(-λt)
     */
    public static Real remainingFraction(Real ageYearsBP) {
        double t = ageYearsBP.doubleValue();
        return Real.of(Math.exp(-DECAY_CONSTANT * t));
    }

    /**
     * Estimates the maximum datable age (limit of detection, typically ~50,000 years).
     */
    public static Real maximumDateableAge() {
        // When ~1% of original C-14 remains
        return calculateAge(Real.of(0.01));
    }

    /**
     * Converts calendar year to radiocarbon years BP (simplified inverse).
     */
    public static double toRadiocarbonBP(int calendarYear) {
        return 1950.0 - calendarYear;
    }
}
