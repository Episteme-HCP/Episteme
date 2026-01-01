/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.biology;

import java.util.*;
import org.jscience.biology.taxonomy.Species;

/**
 * Common biological species with predefined data.
 * <p>
 * Provides factory methods for frequently used species in biology.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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


