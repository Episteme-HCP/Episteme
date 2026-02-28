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

package org.episteme.social.architecture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Historical simulation engine that models the architectural and demographic 
 * evolution of a city over centuries. It tracks transitions between 
 * architectural styles, building construction cycles, and urban prosperity.
 */
public final class UrbanEvolutionEngine {

    private UrbanEvolutionEngine() {}

    /**
     * Defined historical periods for urban categorization.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum HistoricalPeriod {
        EARLY_MEDIEVAL(500, 1000, "Haut Moyen-Ã‚ge"),
        HIGH_MEDIEVAL(1000, 1250, "Moyen-Ã‚ge Central"),
        LATE_MEDIEVAL(1250, 1400, "Bas Moyen-Ã‚ge"),
        RENAISSANCE(1400, 1600, "Renaissance"),
        BAROQUE(1600, 1750, "Baroque"),
        INDUSTRIAL(1750, 1900, "Industriel");

        private final int startYear;
        private final int endYear;
        private final String frenchName;

        HistoricalPeriod(int start, int end, String frenchName) {
            this.startYear = start;
            this.endYear = end;
            this.frenchName = frenchName;
        }

        public int getStartYear() { return startYear; }
        public int getEndYear() { return endYear; }
        public String getFrenchName() { return frenchName; }

        public static HistoricalPeriod forYear(int year) {
            for (HistoricalPeriod p : values()) {
                if (year >= p.startYear && year < p.endYear) return p;
            }
            return year < 500 ? EARLY_MEDIEVAL : INDUSTRIAL;
        }
    }

    /**
     * Major architectural styles and their defining characteristics.
     */
    public enum ArchitecturalStyle {
        CAROLINGIAN(500, 1000, "Carolingien", List.of("Stone basilicas", "Westwork", "Round arches")),
        ROMANESQUE(1000, 1200, "Roman", List.of("Thick walls", "Semi-circular arches", "Barrel vaults", "Towers")),
        GOTHIC(1150, 1500, "Gothique", List.of("Pointed arches", "Ribbed vaults", "Flying buttresses", "Large windows")),
        FLAMBOYANT_GOTHIC(1350, 1550, "Gothique Flamboyant", List.of("Flame-like tracery", "Complex vaults", "Ornamentation")),
        RENAISSANCE(1420, 1600, "Renaissance", List.of("Classical orders", "Symmetry", "Domes", "Rustication")),
        VERNACULAR_TIMBER(500, 1800, "Colombage", List.of("Half-timber", "Wattle and daub", "Thatched roofs"));

        private final int startYear;
        private final int endYear;
        private final String frenchName;
        private final List<String> characteristics;

        ArchitecturalStyle(int start, int end, String frenchName, List<String> characteristics) {
            this.startYear = start;
            this.endYear = end;
            this.frenchName = frenchName;
            this.characteristics = characteristics;
        }

        public boolean isActiveIn(int year) {
            return year >= startYear && year <= endYear;
        }

        public List<String> getCharacteristics() { return characteristics; }
        public String getFrenchName() { return frenchName; }
    }

    /**
     * Functional classes for building simulation.
     */
    public enum BuildingType {
        CATHEDRAL, CHURCH, MONASTERY, CASTLE, PALACE, TOWN_HALL,
        GUILD_HALL, MARKET_HALL, HOSPITAL, UNIVERSITY, HOUSE, WORKSHOP, WAREHOUSE
    }

    /**
     * Represents a single notable building within the city.
     */
    public record Building(
        String name,
        BuildingType type,
        ArchitecturalStyle style,
        int constructionYear,
        int demolitionYear,  // -1 if still standing
        double importance    // 0 to 1
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Snapshots the complete state of a city at a specific moment in history.
     */
    public record CityState(
        String name,
        int year,
        int population,
        HistoricalPeriod period,
        Map<ArchitecturalStyle, Double> styleDistribution,
        List<Building> notableBuildings,
        double prosperity,  // 0 to 1
        double religiousInfluence,  // 0 to 1
        double commercialActivity  // 0 to 1
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Controls the simulation dynamics.
     */
    public record CityParameters(
        double growthRate,           // annual population growth
        double buildingTurnover,     // rate of replacement
        double conservatism,         // resistance to new styles (0-1)
        boolean isBishopSeat,
        boolean hasUniversity,
        boolean isTradeHub
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Simulates the evolution of a city over a defined time span.
     * 
     * @param cityName name of the city
     * @param startYear beginning of simulation
     * @param endYear end of simulation
     * @param intervalYears time step in years
     * @param params simulation parameters
     * @return timeline of CityState snapshots
     */
    public static List<CityState> simulateEvolution(String cityName, int startYear, int endYear, 
            int intervalYears, CityParameters params) {
        
        List<CityState> timeline = new ArrayList<>();
        CityState current = initializeCity(cityName, startYear, params);
        timeline.add(current);
        
        for (int year = startYear + intervalYears; year <= endYear; year += intervalYears) {
            current = evolveCity(current, year, params);
            timeline.add(current);
        }
        return timeline;
    }

    private static CityState initializeCity(String name, int year, CityParameters params) {
        HistoricalPeriod period = HistoricalPeriod.forYear(year);
        Map<ArchitecturalStyle, Double> styles = new HashMap<>();
        int activeCount = getActiveStylesCount(year);
        for (ArchitecturalStyle style : ArchitecturalStyle.values()) {
            if (style.isActiveIn(year)) {
                styles.put(style, 1.0 / activeCount);
            }
        }
        
        List<Building> buildings = new ArrayList<>();
        if (params.isBishopSeat()) {
            ArchitecturalStyle churchStyle = getDominantReligiousStyle(year);
            buildings.add(new Building("CathÃ©drale", BuildingType.CATHEDRAL, churchStyle, 
                year - 100, -1, 1.0));
        }
        
        return new CityState(
            name, year, 5000, period, styles, buildings, 
            0.5, params.isBishopSeat() ? 0.8 : 0.5, params.isTradeHub() ? 0.8 : 0.5
        );
    }

    private static CityState evolveCity(CityState previous, int newYear, CityParameters params) {
        HistoricalPeriod period = HistoricalPeriod.forYear(newYear);
        int yearsDelta = newYear - previous.year();
        int newPopulation = (int)(previous.population() * Math.pow(1 + params.growthRate(), yearsDelta));
        Map<ArchitecturalStyle, Double> newStyles = updateStyleDistribution(previous.styleDistribution(), newYear, params);
        List<Building> buildings = updateBuildings(previous.notableBuildings(), newYear, period, params, newStyles);
        double prosperity = calculateProsperity(previous, newYear, params);
        
        return new CityState(
            previous.name(), newYear, newPopulation, period, newStyles, buildings, 
            prosperity, previous.religiousInfluence() + (params.isBishopSeat() ? 0.01 : -0.005), 
            previous.commercialActivity() + (params.isTradeHub() ? 0.01 : 0)
        );
    }

    private static Map<ArchitecturalStyle, Double> updateStyleDistribution(
            Map<ArchitecturalStyle, Double> current, int year, CityParameters params) {
        
        Map<ArchitecturalStyle, Double> updated = new HashMap<>();
        double total = 0;
        for (ArchitecturalStyle style : ArchitecturalStyle.values()) {
            double weight = 0;
            if (style.isActiveIn(year)) {
                double currentShare = current.getOrDefault(style, 0.0);
                double stylePeak = (style.startYear + style.endYear) / 2.0;
                double span = (style.endYear - style.startYear) / 2.0;
                double distanceFromPeak = span > 0 ? Math.abs(year - stylePeak) / span : 0;
                double peakFactor = Math.max(0.1, 1 - distanceFromPeak);
                weight = currentShare * params.conservatism() + (1 - params.conservatism()) * peakFactor * 0.3;
            } else {
                weight = current.getOrDefault(style, 0.0) * 0.95;
            }
            if (weight > 0.001) {
                updated.put(style, weight);
                total += weight;
            }
        }
        for (ArchitecturalStyle style : updated.keySet()) {
            updated.put(style, updated.get(style) / total);
        }
        return updated;
    }

    private static List<Building> updateBuildings(List<Building> current, int year,
            HistoricalPeriod period, CityParameters params,
            Map<ArchitecturalStyle, Double> styleDistribution) {
        
        List<Building> updated = new ArrayList<>();
        for (Building b : current) {
            if (b.demolitionYear() == -1 || b.demolitionYear() > year) {
                updated.add(b);
            }
        }
        
        Random random = new Random((long) year * 73);
        ArchitecturalStyle dominantStyle = getDominantStyle(styleDistribution);
        
        if (period == HistoricalPeriod.HIGH_MEDIEVAL && random.nextDouble() < 0.3) {
            updated.add(new Building("Ã‰glise Saint-" + generateSaintName(random), BuildingType.CHURCH, dominantStyle, year, -1, 0.5));
        }
        if (period == HistoricalPeriod.RENAISSANCE && params.hasUniversity() && random.nextDouble() < 0.2) {
            updated.add(new Building("CollÃ¨ge de " + generateCollegeName(random), BuildingType.UNIVERSITY, ArchitecturalStyle.RENAISSANCE, year, -1, 0.7));
        }
        return updated;
    }

    private static double calculateProsperity(CityState previous, int year, CityParameters params) {
        double base = previous.prosperity();
        if (params.isTradeHub()) base += 0.02;
        Random random = new Random((long) year * 31);
        if (random.nextDouble() < 0.05) base -= 0.2; 
        if (random.nextDouble() < 0.1) base += 0.1;
        return Math.max(0.1, Math.min(1.0, base));
    }

    private static ArchitecturalStyle getDominantStyle(Map<ArchitecturalStyle, Double> distribution) {
        return distribution.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(ArchitecturalStyle.ROMANESQUE);
    }

    private static ArchitecturalStyle getDominantReligiousStyle(int year) {
        if (year < 1000) return ArchitecturalStyle.CAROLINGIAN;
        if (year < 1150) return ArchitecturalStyle.ROMANESQUE;
        if (year < 1400) return ArchitecturalStyle.GOTHIC;
        if (year < 1550) return ArchitecturalStyle.FLAMBOYANT_GOTHIC;
        return ArchitecturalStyle.RENAISSANCE;
    }

    private static int getActiveStylesCount(int year) {
        int count = 0;
        for (ArchitecturalStyle style : ArchitecturalStyle.values()) {
            if (style.isActiveIn(year)) count++;
        }
        return Math.max(1, count);
    }

    private static String generateSaintName(Random random) {
        String[] saints = {"Pierre", "Paul", "Martin", "Nicolas", "Jacques", "Jean", "Marie", "Anne"};
        return saints[random.nextInt(saints.length)];
    }

    private static String generateCollegeName(Random random) {
        String[] names = {"Sorbonne", "Navarre", "Montaigu", "Cluny", "Beauvais"};
        return names[random.nextInt(names.length)];
    }

    /**
     * Generates a structural summary of city evolution over the simulated timeline.
     * 
     * @param timeline the simulated evolution history
     * @return a multi-line human-readable summary
     */
    public static String describeEvolution(List<CityState> timeline) {
        StringBuilder sb = new StringBuilder();
        for (CityState state : timeline) {
            sb.append(String.format("\n=== %s en %d (%s) ===\n", state.name(), state.year(), state.period().getFrenchName()));
            sb.append(String.format("Population: %d habitants\n", state.population()));
            sb.append("Styles architecturaux dominants:\n");
            state.styleDistribution().entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(3)
                .forEach(e -> sb.append(String.format("  - %s: %.1f%%\n", e.getKey().getFrenchName(), e.getValue() * 100)));
            if (!state.notableBuildings().isEmpty()) {
                sb.append("BÃ¢timents notables:\n");
                state.notableBuildings().forEach(b -> sb.append(String.format("  - %s (%s, %d)\n", b.name(), b.style().getFrenchName(), b.constructionYear())));
            }
        }
        return sb.toString();
    }
}

