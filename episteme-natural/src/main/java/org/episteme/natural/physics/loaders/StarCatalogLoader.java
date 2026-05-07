/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics.loaders;

import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.natural.physics.astronomy.Star;

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

    private static final String DEFAULT_CATALOG_PATH = "/org/episteme/physics/astronomy/stars.json";

    @Override
    public String getName() {
        return "Star Catalog Loader";
    }

    @Override
    public String getDescription() {
        return "Loads star catalogs (e.g. Hipparcos, Tycho)";
    }

    @Override
    public String getLongDescription() {
        return "Parses standard astronomical star catalog formats to populate a stellar database.";
    }

    @Override
    public String getCategory() {
        return "Astronomy/Stars";
    }
    
    @Override
    public Class<Star> getResourceType() {
        return Star.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] { "Hipparcos Main", "Tycho-2" };
    }

    @Override
    public String getResourcePath() {
        return DEFAULT_CATALOG_PATH;
    }

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
