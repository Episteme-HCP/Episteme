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

package org.jscience.natural.physics.loaders;

import org.jscience.natural.physics.astronomy.Constellation;
import org.jscience.natural.physics.astronomy.StarCatalog;
import org.jscience.natural.physics.astronomy.Star;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Loads Constellation definitions from JSON resources.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ConstellationLoader {

    private final StarCatalog starCatalog;

    public ConstellationLoader(StarCatalog starCatalog) {
        this.starCatalog = starCatalog;
    }

    /**
     * Loads constellations from the default resource path.
     * @return List of loaded constellations
     */
    public List<Constellation> loadConstellations() {
        return loadConstellations("/org/jscience/physics/astronomy/constellations.json");
    }

    /**
     * Loads constellations from the specified resource path.
     * @param resourcePath path to JSON file
     * @return List of loaded constellations
     */
    public List<Constellation> loadConstellations(String resourcePath) {
        List<Constellation> constellations = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("Could not find constellation resource: " + resourcePath);
                return constellations;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            if (root.isArray()) {
                for (JsonNode node : root) {
                    Constellation c = parseConstellation(node);
                    if (c != null) {
                        constellations.add(c);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading constellations: " + e.getMessage());
        }
        return constellations;
    }

    private Constellation parseConstellation(JsonNode node) {
        String name = node.get("name").asText();
        String colorHex = node.has("color") ? node.get("color").asText() : "#FFFFFF";
        Color color = Color.decode(colorHex);

        Constellation constellation = new Constellation(name, color);

        if (node.has("lines") && node.get("lines").isArray()) {
            for (JsonNode line : node.get("lines")) {
                if (line.isArray() && line.size() >= 2) {
                    String startName = line.get(0).asText();
                    String endName = line.get(1).asText();

                    Optional<Star> startStar = starCatalog.findByName(startName);
                    Optional<Star> endStar = starCatalog.findByName(endName);

                    if (startStar.isPresent() && endStar.isPresent()) {
                        constellation.addConnection(startStar.get(), endStar.get());
                    } else {
                        System.err.println("Warning: Could not find stars for connection: " 
                                + startName + " - " + endName + " in constellation " + name);
                    }
                }
            }
        }
        return constellation;
    }
}
