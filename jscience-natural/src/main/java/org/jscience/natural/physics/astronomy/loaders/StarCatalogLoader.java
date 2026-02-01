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

package org.jscience.natural.physics.astronomy.loaders;

import org.jscience.core.io.AbstractResourceReader;
import org.jscience.core.mathematics.linearalgebra.DenseVector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.numbers.real.Reals;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.natural.physics.astronomy.Star;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads StarCatalog data.
 * Extends AbstractResourceReader to support caching and fallback.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StarCatalogLoader extends AbstractResourceReader<Star> {

    private static final String DEFAULT_CATALOG_PATH = "/org/jscience/physics/astronomy/stars.json";

    @Override
    protected Star loadFromSource(String id) throws Exception {
        // In a real implementation, this could query a web API like SIMBAD or VizieR
        // For now, we load from the local JSON catalog as the "source"
        return loadAll().stream()
                .filter(s -> id.equals(s.getName()) || id.equals(s.getCatalogId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    protected List<Star> loadAllFromSource() throws Exception {
        List<Star> stars = new ArrayList<>();
        InputStream is = getClass().getResourceAsStream(DEFAULT_CATALOG_PATH);
        if (is == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(is);

        if (root.isArray()) {
            for (JsonNode node : root) {
                String name = node.get("name").asText();
                String spectralType = node.has("spectralType") ? node.get("spectralType").asText() : "Unknown";
                String catalogId = node.has("catalogId") ? node.get("catalogId").asText() : null;
                
                double mass = node.get("mass").asDouble();
                double temp = node.get("temperature").asDouble();
                double lum = node.get("luminosity").asDouble();
                double radius = node.get("radius").asDouble();
                double dist = node.has("distance") ? node.get("distance").asDouble() : 0.0;

                Star star = new Star(name, catalogId, 
                    Quantities.create(mass, Units.KILOGRAM),
                    Quantities.create(radius, Units.METER),
                    DenseVector.of(List.of(Real.ZERO, Real.ZERO, Real.ZERO), Reals.getInstance()),
                    DenseVector.of(List.of(Real.ZERO, Real.ZERO, Real.ZERO), Reals.getInstance()));
                
                star.setSpectralType(spectralType);
                star.setLuminosity(Quantities.create(lum, Units.WATT));
                star.setTemperature(Quantities.create(temp, Units.KELVIN));
                star.setDistanceLightYears(dist);
                
                stars.add(star);
            }
        }
        return stars;
    }
}
