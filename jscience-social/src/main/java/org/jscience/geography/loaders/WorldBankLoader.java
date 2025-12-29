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

package org.jscience.geography.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.economics.Money;
import org.jscience.geography.Region;
import org.jscience.mathematics.numbers.real.Real;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads World Bank demographic and economic data.
 * Simulates loading country data from a JSON source (e.g., World Bank API).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WorldBankLoader {

    private static final String RESOURCE_PATH = "/data/world_bank_countries.json";
    private static WorldBankLoader instance;
    private final ObjectMapper mapper;
    private List<Region> cachedRegions;

    private WorldBankLoader() {
        this.mapper = new ObjectMapper();
    }

    public static synchronized WorldBankLoader getInstance() {
        if (instance == null) {
            instance = new WorldBankLoader();
        }
        return instance;
    }

    /**
     * Loads all countries from the World Bank data source.
     *
     * @return List of Region objects representing countries
     */
    public List<Region> loadAll() {
        if (cachedRegions != null) {
            return cachedRegions;
        }

        cachedRegions = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                // Return sample data if resource not found
                return getSampleData();
            }

            JsonNode root = mapper.readTree(is);
            if (root.isArray()) {
                for (JsonNode node : root) {
                    Region region = parseCountry(node);
                    if (region != null) {
                        cachedRegions.add(region);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getSampleData();
        }

        return cachedRegions;
    }

    private Region parseCountry(JsonNode node) {
        String name = node.path("name").asText(null);
        if (name == null)
            return null;

        Region region = new Region(name, Region.Type.COUNTRY);
        region.setCode(node.path("code").asText(null));
        region.setPopulation(node.path("population").asLong(0));

        double gdpValue = node.path("gdp").asDouble(0.0);
        if (gdpValue > 0) {
            region.setGdp(new Money(Real.of(gdpValue), "USD"));
        }

        return region;
    }

    private List<Region> getSampleData() {
        List<Region> samples = new ArrayList<>();

        Region france = new Region("France", Region.Type.COUNTRY);
        france.setCode("FRA");
        france.setPopulation(67_000_000L);
        france.setGdp(new Money(Real.of(2.8e12), "USD"));
        samples.add(france);

        Region usa = new Region("United States", Region.Type.COUNTRY);
        usa.setCode("USA");
        usa.setPopulation(330_000_000L);
        usa.setGdp(new Money(Real.of(2.5e13), "USD"));
        samples.add(usa);

        Region china = new Region("China", Region.Type.COUNTRY);
        china.setCode("CHN");
        china.setPopulation(1_400_000_000L);
        china.setGdp(new Money(Real.of(1.8e13), "USD"));
        samples.add(china);

        return samples;
    }
}
