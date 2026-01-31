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

package org.jscience.social.architecture;

import java.io.Serializable;
import java.util.List;

/**
 * Historical database of architectural construction methods categorized by 
 * period, region, and materials. It allows for identifying construction 
 * techniques used in historical buildings based on observed physical characteristics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class HistoricalConstructionMethods {

    private HistoricalConstructionMethods() {}

    /**
     * Represents a specific construction technique and its historical context.
     */
    public record ConstructionMethod(
        String name,
        String region,
        int startYear,
        int endYear,
        String description,
        List<String> materials,
        List<String> characteristics
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Catalog of globally significant construction methods.
     */
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
            List.of("Distinctive diamond pattern", "Decorative wall facing")
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
            "Reinforced Concrete (BÃ©ton armÃ©)", "France/International", 1853, 2100,
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
     * Finds construction methods that were active during a specific year and 
     * in a specified geographical region.
     * 
     * @param year the year to query
     * @param region the region (e.g., "France") or null for global
     * @return list of applicable construction methods
     */
    public static List<ConstructionMethod> findMethods(int year, String region) {
        return CATALOG.stream()
            .filter(m -> year >= m.startYear() && year <= m.endYear())
            .filter(m -> region == null || m.region().toLowerCase().contains(region.toLowerCase()))
            .toList();
    }

    /**
     * Identifies potential construction methods based on a list of observed 
     * physical characteristics.
     * 
     * @param observed list of seen characteristics (e.g., "pointed arch")
     * @return list of matching construction methods
     */
    public static List<ConstructionMethod> identifyFromCharacteristics(List<String> observed) {
        return CATALOG.stream()
            .filter(m -> m.characteristics().stream()
                .anyMatch(c -> observed.stream()
                    .anyMatch(o -> c.toLowerCase().contains(o.toLowerCase()))))
            .toList();
    }

    /**
     * Estimates the typical design lifespan of a building constructed using 
     * a specific method, based on data about the main materials used.
     * 
     * @param method the construction method to evaluate
     * @return estimated lifespan in years
     */
    public static int typicalLifespan(ConstructionMethod method) {
        if (method.materials().stream().anyMatch(m -> m.contains("Stone") || m.contains("Concrete"))) {
            return 200;
        } else if (method.materials().stream().anyMatch(m -> m.contains("timber") || m.contains("Wood"))) {
            return 100;
        }
        return 150;
    }
}

