package org.jscience.history;

import org.jscience.history.time.UncertainDate;
import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models historical population dynamics and growth rates.
 */
public final class HistoricalDemography {

    private HistoricalDemography() {}

    public record DemographicSnapshot(
        UncertainDate date,
        long population,
        double growthRate, // annual %
        String region,
        double reliability // 0-1
    ) {}

    /**
     * Projects population based on Malthusian growth model.
     * P(t) = P0 * e^(rt)
     */
    public static Real projectPopulation(long p0, double r, int years) {
        double p = p0 * Math.exp(r * years);
        return Real.of(p);
    }

    /**
     * Calculates Doubling Time of a historical population.
     * T = ln(2) / r
     */
    public static Real doublingTime(double growthRate) {
        if (growthRate <= 0) return Real.ZERO;
        return Real.of(Math.log(2) / growthRate);
    }

    /**
     * Estimates Carrying Capacity (K) based on land and technology level.
     */
    public static Real estimateCarryingCapacity(double landAreaKm2, String technologyEra) {
        double densityPerKm2 = switch (technologyEra.toLowerCase()) {
            case "paleolithic" -> 0.1;
            case "neolithic" -> 5;
            case "bronze_age" -> 20;
            case "medieval" -> 40;
            case "industrial" -> 200;
            case "modern" -> 1000;
            default -> 10;
        };
        return Real.of(landAreaKm2 * densityPerKm2);
    }

    /**
     * Calculates dependency ratio (young+old / working age).
     */
    public static Real dependencyRatio(int age0to14, int age15to64, int age65plus) {
        if (age15to64 == 0) return Real.ZERO;
        return Real.of((double)(age0to14 + age65plus) / age15to64);
    }

    /**
     * Predefined global population estimates.
     */
    public static List<DemographicSnapshot> globalHistory() {
        return List.of(
            new DemographicSnapshot(UncertainDate.circa(-10000), 5_000_000L, 0.0001, "Global", 0.1),
            new DemographicSnapshot(UncertainDate.circa(-1000), 50_000_000L, 0.001, "Global", 0.3),
            new DemographicSnapshot(UncertainDate.circa(1), 200_000_000L, 0.001, "Global", 0.5),
            new DemographicSnapshot(UncertainDate.certain(1500, 1, 1), 450_000_000L, 0.002, "Global", 0.7),
            new DemographicSnapshot(UncertainDate.certain(1800, 1, 1), 1_000_000_000L, 0.005, "Global", 0.9)
        );
    }
}
