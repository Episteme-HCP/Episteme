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

package org.jscience.history;

import org.jscience.history.time.UncertainDate;
import org.jscience.mathematics.numbers.real.Real;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Models historical population dynamics, growth rates, and carrying capacity estimates.
 * Provides statistical models for demographic analysis across different historical eras.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class HistoricalDemography {

    private HistoricalDemography() {
        // Prevent instantiation
    }

    /**
     * Data record representing population status at a point in time.
     * 
     * @param date         point in time (possibly fuzzy)
     * @param population   estimated population count
     * @param growthRate   annual growth rate (as a decimal, e.g., 0.01 for 1%)
     * @param region       geographical region identifier
     * @param reliability  data reliability score (0.0 to 1.0)
     */
    @Persistent
    public record DemographicSnapshot(
        @Relation(type = Relation.Type.ONE_TO_ONE)
        UncertainDate date,
        @Attribute
        long population,
        @Attribute
        double growthRate,
        @Attribute
        String region,
        @Attribute
        double reliability
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public DemographicSnapshot {
            Objects.requireNonNull(date, "Date cannot be null");
            Objects.requireNonNull(region, "Region cannot be null");
        }
    }

    /**
     * Projects population based on Malthusian growth model.
     * Formula: P(t) = P0 * e^(rt)
     * 
     * @param p0    initial population
     * @param r     annual growth rate (decimal, e.g., 0.01 for 1%)
     * @param years number of years to project
     * @return projected population as a {@link Real} number
     */
    public static Real projectPopulation(long p0, double r, int years) {
        double p = p0 * Math.exp(r * years);
        return Real.of(p);
    }

    /**
     * Calculates the doubling time of a historical population.
     * Formula: T = ln(2) / r
     * 
     * @param growthRate annual growth rate
     * @return doubling time in years as a {@link Real} number
     */
    public static Real doublingTime(double growthRate) {
        if (growthRate <= 0) return Real.ZERO;
        return Real.of(Math.log(2) / growthRate);
    }

    /**
     * Estimates Carrying Capacity (K) based on land area and historical technology level.
     * 
     * @param landAreaKm2   land area in square kilometers
     * @param technologyEra the historical era (e.g., "paleolithic", "neolithic", "medieval")
     * @return estimated carrying capacity as a {@link Real} number
     * @throws NullPointerException if technologyEra is null
     */
    public static Real estimateCarryingCapacity(double landAreaKm2, String technologyEra) {
        Objects.requireNonNull(technologyEra, "Technology era cannot be null");
        double densityPerKm2 = switch (technologyEra.toLowerCase().trim()) {
            case "paleolithic" -> 0.1;
            case "neolithic" -> 5.0;
            case "bronze_age" -> 20.0;
            case "medieval" -> 40.0;
            case "industrial" -> 200.0;
            case "modern" -> 1000.0;
            default -> 10.0;
        };
        return Real.of(landAreaKm2 * densityPerKm2);
    }

    /**
     * Calculates the dependency ratio.
     * Formula: (young + old) / working age population.
     * 
     * @param age0to14   population aged 0-14
     * @param age15to64  population aged 15-64
     * @param age65plus  population aged 65 or older
     * @return dependency ratio as a {@link Real} number
     */
    public static Real dependencyRatio(int age0to14, int age15to64, int age65plus) {
        if (age15to64 <= 0) return Real.ZERO;
        return Real.of((double)(age0to14 + age65plus) / age15to64);
    }

    /**
     * Returns a predefined list of global population snapshots throughout history.
     * 
     * @return unmodifiable list of global demographic snapshots
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
