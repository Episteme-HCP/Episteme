package org.jscience.architecture;

import java.util.*;

/**
 * Database of historical construction methods by period and region.
 */
public final class HistoricalConstructionMethods {

    private HistoricalConstructionMethods() {}

    public record ConstructionMethod(
        String name,
        String region,
        int startYear,
        int endYear,
        String description,
        List<String> materials,
        List<String> characteristics
    ) {}

    public static final List<ConstructionMethod> CATALOG = List.of(
        // Roman
        new ConstructionMethod(
            "Opus Caementicium", "Roman Empire", -300, 500,
            "Roman concrete using volcanic ash (pozzolana)",
            List.of("Volcanic ash", "Lime", "Seawater", "Rubble aggregate"),
            List.of("Extremely durable", "Could set underwater", "Formed in wooden molds")
        ),
        new ConstructionMethod(
            "Opus Reticulatum", "Roman Empire", -100, 300,
            "Diamond-pattern brick facing over concrete core",
            List.of("Tufa blocks", "Concrete core"),
            List.of("Distinctive diagonal pattern", "Decorative wall facing")
        ),
        
        // Medieval
        new ConstructionMethod(
            "Colombage (Half-timber)", "Northern Europe", 1100, 1800,
            "Timber frame with infill panels",
            List.of("Oak timber", "Wattle and daub", "Brick nogging"),
            List.of("Visible timber structure", "Quick construction", "Flexible infill")
        ),
        new ConstructionMethod(
            "Opus Francigenum (Gothic)", "France, England", 1140, 1500,
            "Pointed arch masonry with flying buttresses",
            List.of("Limestone", "Lead roofing", "Stained glass"),
            List.of("Pointed arches", "Ribbed vaults", "Large windows", "Flying buttresses")
        ),
        
        // Islamic
        new ConstructionMethod(
            "Muqarnas", "Islamic World", 1000, 1700,
            "Honeycomb vaulting decoration",
            List.of("Stucco", "Wood", "Stone"),
            List.of("Geometric complexity", "Light diffusion", "Acoustic properties")
        ),
        
        // Asian
        new ConstructionMethod(
            "Dougong (Bracket sets)", "China", -500, 1900,
            "Interlocking wooden brackets without nails",
            List.of("Hardwood", "No metal fasteners"),
            List.of("Earthquake resistant", "No nails", "Modular system")
        ),
        
        // Modern
        new ConstructionMethod(
            "Reinforced Concrete (Béton armé)", "France/International", 1853, 2100,
            "Concrete with embedded steel reinforcement",
            List.of("Portland cement", "Steel rebar", "Aggregate"),
            List.of("High tensile strength", "Moldable forms", "Fire resistant")
        ),
        new ConstructionMethod(
            "Steel Frame", "USA/International", 1885, 2100,
            "Structural skeleton of steel columns and beams",
            List.of("Structural steel", "Rivets/Bolts/Welds"),
            List.of("Tall buildings possible", "Large spans", "Curtain wall enabled")
        )
    );

    /**
     * Finds construction methods available in a given year and region.
     */
    public static List<ConstructionMethod> findMethods(int year, String region) {
        return CATALOG.stream()
            .filter(m -> year >= m.startYear() && year <= m.endYear())
            .filter(m -> region == null || m.region().toLowerCase().contains(region.toLowerCase()))
            .toList();
    }

    /**
     * Identifies construction method from observed characteristics.
     */
    public static List<ConstructionMethod> identifyFromCharacteristics(List<String> observed) {
        return CATALOG.stream()
            .filter(m -> m.characteristics().stream()
                .anyMatch(c -> observed.stream()
                    .anyMatch(o -> c.toLowerCase().contains(o.toLowerCase()))))
            .toList();
    }

    /**
     * Gets typical lifespan of a construction method.
     */
    public static int typicalLifespan(ConstructionMethod method) {
        if (method.materials().contains("Concrete") || method.materials().contains("Stone")) {
            return 200;
        } else if (method.materials().stream().anyMatch(m -> m.contains("timber") || m.contains("Wood"))) {
            return 100;
        }
        return 150;
    }
}
