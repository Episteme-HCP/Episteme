package org.jscience.arts;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Analyzes artworks against historical technical standards.
 */
public final class ArtCritiqueEngine {

    private ArtCritiqueEngine() {}

    public enum TechniqueCategory {
        PIGMENTS, PERSPECTIVE, COMPOSITION, BRUSHWORK, MEDIUM, SUBJECT_MATTER
    }

    public record TechnicalStandard(
        String name,
        TechniqueCategory category,
        int startYear,
        int endYear,
        String description
    ) {}

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
            "Use of φ (1.618) proportions"),
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
     */
    public static List<CritiqueResult> evaluate(Artwork artwork, List<String> observedTechniques) {
        List<CritiqueResult> results = new ArrayList<>();
        
        Instant date = artwork.getProductionDate();
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

    public enum CritiqueVerdict {
        PERIOD_APPROPRIATE, ANACHRONISTIC, UNUSUAL_FOR_PERIOD, UNKNOWN
    }

    public record CritiqueResult(String technique, CritiqueVerdict verdict, String explanation) {}

    /**
     * Suggests expected techniques for a given period.
     */
    public static List<TechnicalStandard> getExpectedTechniques(int year) {
        return STANDARDS.stream()
            .filter(s -> year >= s.startYear() && year <= s.endYear())
            .toList();
    }
}
