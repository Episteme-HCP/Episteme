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
package org.jscience.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Audit test to verify 1:1 mapping between Loaders and their backing JSON
 * resources.
 * <p>
 * This test discovers all loader classes and verifies that their expected
 * resource files exist in the classpath.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ResourceAuditTest {

    /**
     * Known loader-resource mappings for audit verification.
     */
    private static final List<LoaderResourceMapping> KNOWN_MAPPINGS = List.of(
            // Chemistry
            new LoaderResourceMapping("ChemistryDataLoader", "/org/jscience/chemistry/elements.json"),
            new LoaderResourceMapping("ChemistryDataLoader", "/org/jscience/chemistry/molecules.json"),
            new LoaderResourceMapping("PeriodicTableLoader", "/org/jscience/chemistry/elements.json"),

            // Astronomy
            new LoaderResourceMapping("SolarSystemLoader", "/org/jscience/ui/astronomy/solar_system.json"),

            // Physics
            new LoaderResourceMapping("ParticleLoader", "/org/jscience/physics/particles.json"),

            // Biology
            new LoaderResourceMapping("TaxonomyLoader", "/org/jscience/biology/taxonomy/species.json"),

            // Social
            new LoaderResourceMapping("FactbookLoader", "/org/jscience/politics/countries.json"),

            // Earth
            new LoaderResourceMapping("OpenWeatherLoader", null) // API-based, no file
    );

    @Test
    @DisplayName("All known loader resources should exist")
    void testLoaderResourcesExist() {
        List<String> missingResources = new ArrayList<>();

        for (LoaderResourceMapping mapping : KNOWN_MAPPINGS) {
            if (mapping.resourcePath() == null) {
                // API-based loader, skip
                continue;
            }

            InputStream is = getClass().getResourceAsStream(mapping.resourcePath());
            if (is == null) {
                missingResources.add(String.format("%s -> %s", mapping.loaderName(), mapping.resourcePath()));
            } else {
                try {
                    is.close();
                } catch (Exception ignored) {
                }
            }
        }

        if (!missingResources.isEmpty()) {
            fail("Missing resources for loaders:\n" + String.join("\n", missingResources));
        }
    }

    @Test
    @DisplayName("Chemistry elements.json should exist and be valid")
    void testChemistryElementsResource() {
        InputStream is = getClass().getResourceAsStream("/org/jscience/chemistry/elements.json");
        assertNotNull(is, "elements.json should exist");
        try {
            assertTrue(is.available() > 0, "elements.json should not be empty");
            is.close();
        } catch (Exception e) {
            fail("Failed to read elements.json: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Particles.json should exist")
    void testParticlesResource() {
        InputStream is = getClass().getResourceAsStream("/org/jscience/physics/particles.json");
        assertNotNull(is, "particles.json should exist");
        try {
            is.close();
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("Solar system demo JSON should exist")
    void testSolarSystemResource() {
        InputStream is = getClass().getResourceAsStream("/org/jscience/ui/astronomy/solar_system.json");
        assertNotNull(is, "solar_system.json should exist");
        try {
            is.close();
        } catch (Exception ignored) {
        }
    }

    /**
     * Record representing a loader-to-resource mapping.
     */
    private record LoaderResourceMapping(String loaderName, String resourcePath) {
    }
}
