/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.geography.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.economics.Money;
import org.jscience.geography.Region;
import org.jscience.io.InputLoader;
import org.jscience.mathematics.numbers.real.Real;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads World Bank demographic and economic data.
 * Simulates loading country data from a JSON source (e.g., World Bank API).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WorldBankLoader implements InputLoader<List<Region>> {

    private static final String RESOURCE_PATH = "/data/worldbank_countries.json";
    private static WorldBankLoader instance;
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Region> cachedRegions;

    public WorldBankLoader() {
    }

    public static synchronized WorldBankLoader getInstance() {
        if (instance == null)
            instance = new WorldBankLoader();
        return instance;
    }

    @Override
    public List<Region> load(String resourceId) throws Exception {
        return loadAll();
    }

    @Override
    public String getResourcePath() {
        return RESOURCE_PATH;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Region>> getResourceType() {
        return (Class<List<Region>>) (Class<?>) List.class;
    }

    /**
     * Fetches indicator data for a country from World Bank API.
     */
    public Map<String, Map<String, Double>> fetchIndicatorData(String countryCode, String indicator) {
        Map<String, Map<String, Double>> result = new LinkedHashMap<>();
        // Stub implementation - would connect to World Bank API
        return result;
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
