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
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Analytical engine designed to evaluate artworks against historical technical standards.
 * It can detect anachronisms and verify if techniques used in an artwork were 
 * available during its purported production period.
 */
public final class ArtCritiqueEngine {

    private ArtCritiqueEngine() {}

    /**
     * Categories of technical analysis for artworks.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum TechniqueCategory {
        PIGMENTS, PERSPECTIVE, COMPOSITION, BRUSHWORK, MEDIUM, SUBJECT_MATTER
    }

    /**
     * Represents a technical standard from a specific historical period.
     */
    public record TechnicalStandard(
        String name,
        TechniqueCategory category,
        int startYear,
        int endYear,
        String description
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    private static final List<TechnicalStandard> STANDARDS = List.of(
        new TechnicalStandard("Natural Pigments", TechniqueCategory.PIGMENTS, -3000, 1850, 
            "Use of earth pigments, lapis lazuli, vermilion"),
        new TechnicalStandard("Synthetic Pigments", TechniqueCategory.PIGMENTS, 1850, 2100,
            "Industrial pigments like cadmium, cobalt blue"),
        new TechnicalStandard("Inverse Perspective", TechniqueCategory.PERSPECTIVE, 500, 1300,
            "Byzantine/Medieval divergent perspective"),
        new TechnicalStandard("Linear Perspective", TechniqueCategory.PERSPECTIVE, 1400, 2100,
            "Brunelleschi's vanishing point system"),
        new TechnicalStandard("Golden Ratio Composition", TechniqueCategory.COMPOSITION, -500, 2100,
            "Use of Ï† (1.618) proportions"),
        new TechnicalStandard("Chiaroscuro", TechniqueCategory.BRUSHWORK, 1450, 1700,
            "Strong light-dark contrast (Caravaggio style)"),
        new TechnicalStandard("Impasto", TechniqueCategory.BRUSHWORK, 1600, 2100,
            "Thick, textured paint application"),
        new TechnicalStandard("Tempera Medium", TechniqueCategory.MEDIUM, -1500, 1500,
            "Egg-based paint binder"),
        new TechnicalStandard("Oil Medium", TechniqueCategory.MEDIUM, 1400, 2100,
            "Linseed/walnut oil binder")
    );

    /**
     * Evaluates an artwork against period-appropriate standards.
     * 
     * @param artwork the artwork to examine
     * @param observedTechniques list of techniques identified during physical examination
     * @return a list of critique results identifying consistency or anachronisms
     */
    public static List<CritiqueResult> evaluate(Artwork artwork, List<String> observedTechniques) {
        List<CritiqueResult> results = new ArrayList<>();
        
        Instant date = artwork.getProductionDate().toInstant();
        int year = date != null ? date.atZone(ZoneOffset.UTC).getYear() : 1500;

        List<TechnicalStandard> periodStandards = STANDARDS.stream()
            .filter(s -> year >= s.startYear() && year <= s.endYear())
            .toList();

        for (String technique : observedTechniques) {
            boolean matches = periodStandards.stream()
                .anyMatch(s -> s.name().toLowerCase().contains(technique.toLowerCase()));
            
            if (matches) {
                results.add(new CritiqueResult(technique, CritiqueVerdict.PERIOD_APPROPRIATE,
                    "Technique consistent with " + year + " standards"));
            } else {
                // Check if anachronistic
                Optional<TechnicalStandard> future = STANDARDS.stream()
                    .filter(s -> s.name().toLowerCase().contains(technique.toLowerCase()))
                    .filter(s -> s.startYear() > year)
                    .findFirst();
                
                if (future.isPresent()) {
                    results.add(new CritiqueResult(technique, CritiqueVerdict.ANACHRONISTIC,
                        "Technique not available until " + future.get().startYear()));
                } else {
                    results.add(new CritiqueResult(technique, CritiqueVerdict.UNKNOWN,
                        "Technique not found in database"));
                }
            }
        }
        
        return results;
    }

    /**
     * Verdicts for technical evaluation.
     */
    public enum CritiqueVerdict {
        PERIOD_APPROPRIATE, ANACHRONISTIC, UNUSUAL_FOR_PERIOD, UNKNOWN
    }

    /**
     * The result of a technical examination of a specific technique.
     */
    public record CritiqueResult(String technique, CritiqueVerdict verdict, String explanation) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Suggests expected techniques for a given period.
     * 
     * @param year the year to query
     * @return list of technical standards applicable to that year
     */
    public static List<TechnicalStandard> getExpectedTechniques(int year) {
        return STANDARDS.stream()
            .filter(s -> year >= s.startYear() && year <= s.endYear())
            .toList();
    }
}

