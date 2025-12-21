/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.biology.biochemistry;

import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.type.*;
import org.jscience.io.MiniCatalog;

/**
 * Catalog of vitamins with nutritional information.
 * <p>
 * Loads vitamin data from JSON including RDA values, food sources, and
 * functions.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VitaminCatalog implements MiniCatalog<VitaminCatalog.Vitamin> {

    private static final VitaminCatalog INSTANCE = new VitaminCatalog();
    private final List<Vitamin> vitamins = new ArrayList<>();

    public static VitaminCatalog getInstance() {
        return INSTANCE;
    }

    public VitaminCatalog() {
        loadFromJSON();
    }

    @SuppressWarnings("unchecked")
    private void loadFromJSON() {
        try {
            InputStream is = getClass().getResourceAsStream("/org/jscience/biology/vitamins.json");
            if (is == null) {
                System.err.println("Could not load vitamins.json");
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> data = mapper.readValue(is,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            for (Map<String, Object> entry : data) {
                vitamins.add(new Vitamin(
                        (String) entry.get("id"),
                        (String) entry.get("name"),
                        (List<String>) entry.get("aliases"),
                        (String) entry.get("solubility"),
                        ((Number) entry.get("rdaAdult")).doubleValue(),
                        (String) entry.get("rdaUnit"),
                        (List<String>) entry.get("sources"),
                        (List<String>) entry.get("functions")));
            }
        } catch (IOException e) {
            System.err.println("Error loading vitamins: " + e.getMessage());
        }
    }

    @Override
    public List<Vitamin> getAll() {
        return Collections.unmodifiableList(vitamins);
    }

    @Override
    public Optional<Vitamin> findByName(String name) {
        return vitamins.stream()
                .filter(v -> v.name.equalsIgnoreCase(name) ||
                        v.id.equalsIgnoreCase(name) ||
                        v.aliases.stream().anyMatch(a -> a.equalsIgnoreCase(name)))
                .findFirst();
    }

    @Override
    public int size() {
        return vitamins.size();
    }

    /**
     * Gets fat-soluble vitamins (A, D, E, K).
     */
    public List<Vitamin> getFatSoluble() {
        return vitamins.stream()
                .filter(v -> "fat".equals(v.solubility))
                .toList();
    }

    /**
     * Gets water-soluble vitamins (C, B-complex).
     */
    public List<Vitamin> getWaterSoluble() {
        return vitamins.stream()
                .filter(v -> "water".equals(v.solubility))
                .toList();
    }

    /**
     * Represents a vitamin with nutritional data.
     */
    public static class Vitamin {
        public final String id;
        public final String name;
        public final List<String> aliases;
        public final String solubility;
        public final double rdaAdult;
        public final String rdaUnit;
        public final List<String> sources;
        public final List<String> functions;

        public Vitamin(String id, String name, List<String> aliases, String solubility,
                double rdaAdult, String rdaUnit, List<String> sources, List<String> functions) {
            this.id = id;
            this.name = name;
            this.aliases = aliases != null ? aliases : Collections.emptyList();
            this.solubility = solubility;
            this.rdaAdult = rdaAdult;
            this.rdaUnit = rdaUnit;
            this.sources = sources != null ? sources : Collections.emptyList();
            this.functions = functions != null ? functions : Collections.emptyList();
        }

        public boolean isFatSoluble() {
            return "fat".equals(solubility);
        }

        public boolean isWaterSoluble() {
            return "water".equals(solubility);
        }

        @Override
        public String toString() {
            return String.format("%s (%s) - RDA: %.1f %s, %s-soluble",
                    name, id, rdaAdult, rdaUnit, solubility);
        }
    }
}