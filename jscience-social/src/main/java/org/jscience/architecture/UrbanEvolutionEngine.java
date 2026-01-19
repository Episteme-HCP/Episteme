package org.jscience.architecture;

import java.util.*;

/**
 * Simulates urban evolution through historical periods with dynamic architectural style transitions.
 */
public final class UrbanEvolutionEngine {

    private UrbanEvolutionEngine() {}

    public enum HistoricalPeriod {
        EARLY_MEDIEVAL(500, 1000, "Haut Moyen-Âge"),
        HIGH_MEDIEVAL(1000, 1250, "Moyen-Âge Central"),
        LATE_MEDIEVAL(1250, 1400, "Bas Moyen-Âge"),
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

    public enum BuildingType {
        CATHEDRAL, CHURCH, MONASTERY, CASTLE, PALACE, TOWN_HALL,
        GUILD_HALL, MARKET_HALL, HOSPITAL, UNIVERSITY, HOUSE, WORKSHOP, WAREHOUSE
    }

    public record Building(
        String name,
        BuildingType type,
        ArchitecturalStyle style,
        int constructionYear,
        int demolitionYear,  // -1 if still standing
        double importance    // 0-1, affects city character
    ) {}

    public record CityState(
        String name,
        int year,
        int population,
        HistoricalPeriod period,
        Map<ArchitecturalStyle, Double> styleDistribution,
        List<Building> notableBuildings,
        double prosperity,  // 0-1
        double religiousInfluence,  // 0-1
        double commercialActivity  // 0-1
    ) {}

    /**
     * Simulates urban evolution over a time period.
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

    /**
     * City simulation parameters.
     */
    public record CityParameters(
        double growthRate,           // Annual population growth
        double buildingTurnover,     // Rate of replacement
        double conservatism,         // Resistance to new styles (0-1)
        boolean isBishopSeat,
        boolean hasUniversity,
        boolean isTradeHub
    ) {}

    private static CityState initializeCity(String name, int year, CityParameters params) {
        HistoricalPeriod period = HistoricalPeriod.forYear(year);
        
        Map<ArchitecturalStyle, Double> styles = new HashMap<>();
        for (ArchitecturalStyle style : ArchitecturalStyle.values()) {
            if (style.isActiveIn(year)) {
                styles.put(style, 1.0 / getActiveStylesCount(year));
            }
        }
        
        List<Building> buildings = new ArrayList<>();
        if (params.isBishopSeat()) {
            ArchitecturalStyle churchStyle = getDominantReligiousStyle(year);
            buildings.add(new Building("Cathédrale", BuildingType.CATHEDRAL, churchStyle, 
                year - 100, -1, 1.0));
        }
        
        return new CityState(
            name, year,
            5000,  // Base population
            period,
            styles,
            buildings,
            0.5,
            params.isBishopSeat() ? 0.8 : 0.5,
            params.isTradeHub() ? 0.8 : 0.5
        );
    }

    private static CityState evolveCity(CityState previous, int newYear, CityParameters params) {
        HistoricalPeriod period = HistoricalPeriod.forYear(newYear);
        int yearsDelta = newYear - previous.year();
        
        // Population growth
        int newPopulation = (int)(previous.population() * 
            Math.pow(1 + params.growthRate(), yearsDelta));
        
        // Update style distribution
        Map<ArchitecturalStyle, Double> newStyles = updateStyleDistribution(
            previous.styleDistribution(), newYear, params);
        
        // Add new notable buildings based on prosperity and period
        List<Building> buildings = new ArrayList<>(previous.notableBuildings());
        buildings = updateBuildings(buildings, newYear, period, params, newStyles);
        
        // Update prosperity based on events
        double prosperity = calculateProsperity(previous, newYear, params);
        
        return new CityState(
            previous.name(),
            newYear,
            newPopulation,
            period,
            newStyles,
            buildings,
            prosperity,
            previous.religiousInfluence() + (params.isBishopSeat() ? 0.01 : -0.005),
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
                // New buildings use current styles
                double currentShare = current.getOrDefault(style, 0.0);
                
                // Style popularity peaks in the middle of its period
                double stylePeak = (style.startYear + style.endYear) / 2.0;
                double distanceFromPeak = Math.abs(year - stylePeak) / 
                    ((style.endYear - style.startYear) / 2.0);
                double peakFactor = Math.max(0.1, 1 - distanceFromPeak);
                
                // Old buildings persist (conservatism)
                weight = currentShare * params.conservatism() + 
                        (1 - params.conservatism()) * peakFactor * 0.3;
            } else {
                // Style no longer built but old buildings remain
                weight = current.getOrDefault(style, 0.0) * 0.95; // Slow decay
            }
            
            if (weight > 0.001) {
                updated.put(style, weight);
                total += weight;
            }
        }
        
        // Normalize
        for (ArchitecturalStyle style : updated.keySet()) {
            updated.put(style, updated.get(style) / total);
        }
        
        return updated;
    }

    private static List<Building> updateBuildings(List<Building> current, int year,
            HistoricalPeriod period, CityParameters params,
            Map<ArchitecturalStyle, Double> styleDistribution) {
        
        List<Building> updated = new ArrayList<>();
        
        // Age existing buildings
        for (Building b : current) {
            if (b.demolitionYear() == -1 || b.demolitionYear() > year) {
                updated.add(b);
            }
        }
        
        // Add new buildings based on period
        Random random = new Random(year);
        ArchitecturalStyle dominantStyle = getDominantStyle(styleDistribution);
        
        if (period == HistoricalPeriod.HIGH_MEDIEVAL && random.nextDouble() < 0.3) {
            updated.add(new Building(
                "Église Saint-" + generateSaintName(random),
                BuildingType.CHURCH, dominantStyle, year, -1, 0.5
            ));
        }
        
        if (period == HistoricalPeriod.RENAISSANCE && params.hasUniversity() && random.nextDouble() < 0.2) {
            updated.add(new Building(
                "Collège de " + generateCollegeName(random),
                BuildingType.UNIVERSITY, ArchitecturalStyle.RENAISSANCE, year, -1, 0.7
            ));
        }
        
        return updated;
    }

    private static double calculateProsperity(CityState previous, int year, CityParameters params) {
        double base = previous.prosperity();
        
        // Trade hubs prosper
        if (params.isTradeHub()) base += 0.02;
        
        // Random events (plagues, wars, prosperity)
        Random random = new Random(year * 31);
        if (random.nextDouble() < 0.05) base -= 0.2; // Crisis
        if (random.nextDouble() < 0.1) base += 0.1;  // Boom
        
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
     * Generates a textual description of city evolution.
     */
    public static String describeEvolution(List<CityState> timeline) {
        StringBuilder sb = new StringBuilder();
        
        for (CityState state : timeline) {
            sb.append(String.format("\n=== %s en %d (%s) ===\n", 
                state.name(), state.year(), state.period().getFrenchName()));
            sb.append(String.format("Population: %d habitants\n", state.population()));
            sb.append("Styles architecturaux dominants:\n");
            
            state.styleDistribution().entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(3)
                .forEach(e -> sb.append(String.format("  - %s: %.1f%%\n", 
                    e.getKey().getFrenchName(), e.getValue() * 100)));
            
            if (!state.notableBuildings().isEmpty()) {
                sb.append("Bâtiments notables:\n");
                state.notableBuildings().forEach(b -> 
                    sb.append(String.format("  - %s (%s, %d)\n", 
                        b.name(), b.style().getFrenchName(), b.constructionYear())));
            }
        }
        
        return sb.toString();
    }
}
