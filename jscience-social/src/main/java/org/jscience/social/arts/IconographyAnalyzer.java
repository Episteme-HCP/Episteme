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

package org.jscience.social.arts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analytical tool for identifying and interpreting iconographic symbols 
 * in artworks. It supports art historical research by mapping visual elements 
 * to their symbolic meanings, historical periods, and thematic categories.
 */
public final class IconographyAnalyzer {

    private IconographyAnalyzer() {}

    /**
     * Thematic categories for iconographic symbols.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum SymbolCategory {
        RELIGIOUS, MYTHOLOGICAL, HERALDIC, ALLEGORICAL, NATURAL, POLITICAL
    }

    /**
     * Represents a single iconographic symbol and its historical context.
     */
    public record IconographicSymbol(
        String name,
        SymbolCategory category,
        String meaning,
        int startYear,
        int endYear
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

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
     * Checks a list of detected visual elements against the database to 
     * identify known symbols.
     * 
     * @param detectedElements names of visual elements found in the artwork
     * @return list of identified iconographic symbols
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
     * Calculates the frequency of symbols within a collection of artworks.
     * 
     * @param artworkSymbolLists a list where each entry is a list of detected 
     *                           element names for one artwork
     * @return a frequency map of identified symbols
     */
    public static Map<IconographicSymbol, Integer> calculateFrequency(
            List<List<String>> artworkSymbolLists) {
        Map<IconographicSymbol, Integer> frequency = new HashMap<>();
        for (List<String> symbols : artworkSymbolLists) {
            for (IconographicSymbol identified : identifySymbols(symbols)) {
                frequency.merge(identified, 1, (a, b) -> Integer.sum(a, b));
            }
        }
        return frequency;
    }

    /**
     * Suggests a probable historical period based on the identified symbols.
     * 
     * @param symbols list of identified symbols
     * @return a string describing the suggested period
     */
    public static String suggestPeriod(List<IconographicSymbol> symbols) {
        if (symbols.isEmpty()) return "Unknown";
        int avgStart = (int) symbols.stream().mapToInt(IconographicSymbol::startYear).average().orElse(0);
        if (avgStart < 0) return "Classical Antiquity";
        if (avgStart < 500) return "Late Antiquity / Early Christian";
        if (avgStart < 1400) return "Medieval";
        if (avgStart < 1400) return "Renaissance";
        if (avgStart < 1800) return "Baroque / Early Modern";
        return "Modern";
    }

    /**
     * Retrieves all symbols belonging to a specific category.
     * 
     * @param category the category to filter by
     * @return list of symbols in that category
     */
    public static List<IconographicSymbol> getSymbolsByCategory(SymbolCategory category) {
        return SYMBOL_DATABASE.stream()
            .filter(s -> s.category() == category)
            .toList();
    }
}

