/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology;

import java.util.*;
import org.jscience.biology.taxonomy.Species;

/**
 * Common biological species with predefined data.
 * <p>
 * Provides factory methods for frequently used species in biology.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class CommonSpecies {

    private CommonSpecies() {
    } // Utility class

    private static Species get(String name) {
        return org.jscience.biology.taxonomy.TaxonomyLoader.getSpecies(name);
    }

    // ========== Model Organisms ==========

    public static Species escherichiaColiK12() {
        return get("Escherichia coli");
    }

    public static Species saccharomycesCerevisiae() {
        return get("Saccharomyces cerevisiae");
    }

    public static Species drosophilaMelanogaster() {
        return get("Drosophila melanogaster");
    }

    public static Species musMusculus() {
        return get("Mus musculus");
    }

    public static Species arabidopsisThaliana() {
        return get("Arabidopsis thaliana");
    }

    public static Species caenorhabditisElegans() {
        return get("Caenorhabditis elegans");
    }

    public static Species danioRerio() {
        return get("Danio rerio");
    }

    // ========== Common Animals ==========

    public static Species panTroglodytes() {
        return get("Pan troglodytes");
    }

    public static Species canisLupusFamiliaris() {
        return get("Canis lupus familiaris");
    }

    public static Species felisCatus() {
        return get("Felis catus");
    }

    /**
     * Returns list of all common model organisms.
     */
    public static List<Species> modelOrganisms() {
        return List.of(
                escherichiaColiK12(),
                saccharomycesCerevisiae(),
                drosophilaMelanogaster(),
                musMusculus(),
                arabidopsisThaliana(),
                caenorhabditisElegans(),
                danioRerio());
    }
}
