package org.jscience.arts;

import java.util.*;

/**
 * Analyzes iconographic symbols in artworks for art history research.
 */
public final class IconographyAnalyzer {

    private IconographyAnalyzer() {}

    public enum SymbolCategory {
        RELIGIOUS, MYTHOLOGICAL, HERALDIC, ALLEGORICAL, NATURAL, POLITICAL
    }

    public record IconographicSymbol(
        String name,
        SymbolCategory category,
        String meaning,
        int startYear,
        int endYear
    ) {}

    private static final List<IconographicSymbol> SYMBOL_DATABASE = List.of(
        // Religious
        new IconographicSymbol("Aureole", SymbolCategory.RELIGIOUS, "Holiness/Divinity", -500, 2100),
        new IconographicSymbol("Halo", SymbolCategory.RELIGIOUS, "Sainthood", 300, 2100),
        new IconographicSymbol("Keys", SymbolCategory.RELIGIOUS, "St. Peter / Papal authority", 400, 2100),
        new IconographicSymbol("Lamb", SymbolCategory.RELIGIOUS, "Christ / Sacrifice", 100, 2100),
        new IconographicSymbol("Dove", SymbolCategory.RELIGIOUS, "Holy Spirit / Peace", -500, 2100),
        new IconographicSymbol("Fish", SymbolCategory.RELIGIOUS, "Early Christianity", 100, 400),
        new IconographicSymbol("Chi-Rho", SymbolCategory.RELIGIOUS, "Christ monogram", 300, 1200),
        
        // Mythological
        new IconographicSymbol("Caduceus", SymbolCategory.MYTHOLOGICAL, "Hermes / Commerce", -800, 2100),
        new IconographicSymbol("Thunderbolt", SymbolCategory.MYTHOLOGICAL, "Zeus/Jupiter", -1000, 500),
        new IconographicSymbol("Owl", SymbolCategory.MYTHOLOGICAL, "Athena / Wisdom", -800, 2100),
        new IconographicSymbol("Trident", SymbolCategory.MYTHOLOGICAL, "Poseidon/Neptune", -800, 500),
        
        // Allegorical
        new IconographicSymbol("Skull", SymbolCategory.ALLEGORICAL, "Vanitas / Mortality", 1400, 2100),
        new IconographicSymbol("Hourglass", SymbolCategory.ALLEGORICAL, "Time / Transience", 1400, 1800),
        new IconographicSymbol("Scales", SymbolCategory.ALLEGORICAL, "Justice", -1500, 2100),
        new IconographicSymbol("Mirror", SymbolCategory.ALLEGORICAL, "Vanity / Truth", 1400, 1800),
        new IconographicSymbol("Cornucopia", SymbolCategory.ALLEGORICAL, "Abundance", -500, 2100),
        
        // Heraldic
        new IconographicSymbol("Fleur-de-lis", SymbolCategory.HERALDIC, "French monarchy", 1100, 1800),
        new IconographicSymbol("Lion rampant", SymbolCategory.HERALDIC, "Courage / Royalty", 1100, 2100),
        new IconographicSymbol("Eagle", SymbolCategory.HERALDIC, "Empire / Power", -800, 2100),
        new IconographicSymbol("Rose", SymbolCategory.HERALDIC, "England (Tudor)", 1485, 1603)
    );

    /**
     * Identifies symbols present in a list of detected elements.
     */
    public static List<IconographicSymbol> identifySymbols(List<String> detectedElements) {
        List<IconographicSymbol> found = new ArrayList<>();
        for (String element : detectedElements) {
            for (IconographicSymbol symbol : SYMBOL_DATABASE) {
                if (symbol.name().toLowerCase().contains(element.toLowerCase()) ||
                    element.toLowerCase().contains(symbol.name().toLowerCase())) {
                    found.add(symbol);
                }
            }
        }
        return found;
    }

    /**
     * Calculates symbol frequency in a collection.
     */
    public static Map<IconographicSymbol, Integer> calculateFrequency(
            List<List<String>> artworkSymbolLists) {
        Map<IconographicSymbol, Integer> frequency = new HashMap<>();
        for (List<String> symbols : artworkSymbolLists) {
            for (IconographicSymbol identified : identifySymbols(symbols)) {
                frequency.merge(identified, 1, Integer::sum);
            }
        }
        return frequency;
    }

    /**
     * Suggests period based on identified symbols.
     */
    public static String suggestPeriod(List<IconographicSymbol> symbols) {
        if (symbols.isEmpty()) return "Unknown";
        
        int avgStart = (int) symbols.stream().mapToInt(IconographicSymbol::startYear).average().orElse(0);
        if (avgStart < 0) return "Classical Antiquity";
        if (avgStart < 500) return "Late Antiquity / Early Christian";
        if (avgStart < 1400) return "Medieval";
        if (avgStart < 1600) return "Renaissance";
        if (avgStart < 1800) return "Baroque / Early Modern";
        return "Modern";
    }

    /**
     * Finds symbols by category.
     */
    public static List<IconographicSymbol> getSymbolsByCategory(SymbolCategory category) {
        return SYMBOL_DATABASE.stream()
            .filter(s -> s.category() == category)
            .toList();
    }
}
