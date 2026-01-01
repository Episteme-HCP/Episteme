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
import org.jscience.io.InputLoader;
import org.jscience.mathematics.numbers.real.Real;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Production loader for World Bank demographic and economic data.
 * 
 * Integrates with World Bank API v2 to fetch:
 * - Country metadata
 * - Economic indicators (GDP, GNI, etc.)
 * - Demographic data (population, life expectancy, etc.)
 * - Time series data for historical analysis
 * 
 * @see <a href=
 *      "https://datahelpdesk.worldbank.org/knowledgebase/articles/889392-about-the-indicators-api-documentation">World
 *      Bank API</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WorldBankLoader implements InputLoader<List<Region>> {

    private static final Logger LOG = LoggerFactory.getLogger(WorldBankLoader.class);
    private static final String RESOURCE_PATH = "/data/worldbank_countries.json";
    private static final String WB_API_BASE = "https://api.worldbank.org/v2";
    private static final int PER_PAGE = 500; // Max allowed by API

    private static WorldBankLoader instance;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient;
    private List<Region> cachedRegions;

    public WorldBankLoader() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public static synchronized WorldBankLoader getInstance() {
        if (instance == null) {
            instance = new WorldBankLoader();
        }
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
     * 
     * Common indicators:
     * - NY.GDP.MKTP.CD: GDP (current US$)
     * - SP.POP.TOTL: Population, total
     * - SP.DYN.LE00.IN: Life expectancy at birth
     * - EN.ATM.CO2E.PC: CO2 emissions (metric tons per capita)
     * 
     * @param countryCode ISO 3-letter country code (e.g., "USA", "FRA")
     * @param indicator   World Bank indicator code
     * @return Map of year -> value
     */
    public CompletableFuture<Map<String, Double>> fetchIndicatorData(String countryCode, String indicator) {
        String url = String.format("%s/country/%s/indicator/%s?format=json&per_page=%d",
                WB_API_BASE, countryCode, indicator, PER_PAGE);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        return parseIndicatorResponse(response.body());
                    } catch (Exception e) {
                        LOG.warn("Failed to parse indicator data for {}/{}: {}",
                                countryCode, indicator, e.getMessage());
                        return new LinkedHashMap<>();
                    }
                });
    }

    /**
     * Parses World Bank API response for indicator data.
     */
    private Map<String, Double> parseIndicatorResponse(String jsonResponse) throws Exception {
        Map<String, Double> result = new LinkedHashMap<>();
        JsonNode root = mapper.readTree(jsonResponse);

        if (root.isArray() && root.size() > 1) {
            JsonNode data = root.get(1);
            if (data.isArray()) {
                for (JsonNode entry : data) {
                    String date = entry.path("date").asText();
                    double value = entry.path("value").asDouble(Double.NaN);
                    if (!Double.isNaN(value)) {
                        result.put(date, value);
                    }
                }
            }
        }

        LOG.debug("Parsed {} data points from World Bank API", result.size());
        return result;
    }

    /**
     * Loads all countries from World Bank API or fallback to local resource.
     *
     * @return List of Region objects representing countries
     */
    public List<Region> loadAll() {
        if (cachedRegions != null) {
            return cachedRegions;
        }

        // Try to load from World Bank API first
        try {
            cachedRegions = loadFromApi();
            if (!cachedRegions.isEmpty()) {
                LOG.info("Loaded {} countries from World Bank API", cachedRegions.size());
                return cachedRegions;
            }
        } catch (Exception e) {
            LOG.warn("Failed to load from World Bank API, trying local resource: {}", e.getMessage());
        }

        // Fallback to local resource
        cachedRegions = loadFromResource();
        LOG.info("Loaded {} countries from local resource", cachedRegions.size());
        return cachedRegions;
    }

    /**
     * Loads countries from World Bank API.
     */
    private List<Region> loadFromApi() throws Exception {
        String url = String.format("%s/country?format=json&per_page=%d", WB_API_BASE, PER_PAGE);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(15))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("World Bank API returned status: " + response.statusCode());
        }

        return parseCountriesFromApi(response.body());
    }

    /**
     * Parses countries from World Bank API response.
     */
    private List<Region> parseCountriesFromApi(String jsonResponse) throws Exception {
        List<Region> regions = new ArrayList<>();
        JsonNode root = mapper.readTree(jsonResponse);

        if (root.isArray() && root.size() > 1) {
            JsonNode countries = root.get(1);
            if (countries.isArray()) {
                for (JsonNode node : countries) {
                    // Skip aggregates (regions, income levels, etc.)
                    String regionType = node.path("region").path("value").asText("");
                    if ("Aggregates".equals(regionType)) {
                        continue;
                    }

                    Region region = parseCountryFromApi(node);
                    if (region != null) {
                        regions.add(region);
                    }
                }
            }
        }

        return regions;
    }

    /**
     * Parses a single country from World Bank API format.
     */
    private Region parseCountryFromApi(JsonNode node) {
        String name = node.path("name").asText(null);
        if (name == null || name.isEmpty()) {
            return null;
        }

        Region region = new Region(name, Region.Type.COUNTRY);
        region.setCode(node.path("id").asText(null)); // ISO3 code

        // Set capital city using existing API
        String capital = node.path("capitalCity").asText(null);
        if (capital != null && !capital.isEmpty()) {
            region.setCapital(capital);
        }

        // Log additional metadata (no storage API available on Region)
        LOG.debug("Loaded country {}: capital={}, region={}, income={}",
                name,
                capital,
                node.path("region").path("value").asText(""),
                node.path("incomeLevel").path("value").asText(""));

        return region;
    }

    /**
     * Loads countries from local JSON resource.
     */
    private List<Region> loadFromResource() {
        try (InputStream is = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                LOG.warn("Local resource not found: {}, using sample data", RESOURCE_PATH);
                return getSampleData();
            }

            JsonNode root = mapper.readTree(is);
            List<Region> regions = new ArrayList<>();

            if (root.isArray()) {
                for (JsonNode node : root) {
                    Region region = parseCountry(node);
                    if (region != null) {
                        regions.add(region);
                    }
                }
            }

            return regions.isEmpty() ? getSampleData() : regions;

        } catch (Exception e) {
            LOG.error("Failed to load from local resource", e);
            return getSampleData();
        }
    }

    /**
     * Parses country from local JSON format.
     */
    private Region parseCountry(JsonNode node) {
        String name = node.path("name").asText(null);
        if (name == null) {
            return null;
        }

        Region region = new Region(name, Region.Type.COUNTRY);
        region.setCode(node.path("code").asText(null));
        region.setPopulation(node.path("population").asLong(0));

        double gdpValue = node.path("gdp").asDouble(0.0);
        if (gdpValue > 0) {
            region.setGdp(new Money(Real.of(gdpValue), "USD"));
        }

        return region;
    }

    /**
     * Provides sample data when no other source is available.
     */
    private List<Region> getSampleData() {
        LOG.info("Using built-in sample data");
        List<Region> samples = new ArrayList<>();

        Region france = new Region("France", Region.Type.COUNTRY);
        france.setCode("FRA");
        france.setPopulation(67_000_000L);
        france.setGdp(new Money(Real.of(2.8e12), "USD"));
        france.setCapital("Paris");
        samples.add(france);

        Region usa = new Region("United States", Region.Type.COUNTRY);
        usa.setCode("USA");
        usa.setPopulation(330_000_000L);
        usa.setGdp(new Money(Real.of(25.0e12), "USD"));
        usa.setCapital("Washington, D.C.");
        samples.add(usa);

        Region china = new Region("China", Region.Type.COUNTRY);
        china.setCode("CHN");
        china.setPopulation(1_400_000_000L);
        china.setGdp(new Money(Real.of(18.0e12), "USD"));
        china.setCapital("Beijing");
        samples.add(china);

        Region germany = new Region("Germany", Region.Type.COUNTRY);
        germany.setCode("DEU");
        germany.setPopulation(83_000_000L);
        germany.setGdp(new Money(Real.of(4.0e12), "USD"));
        germany.setCapital("Berlin");
        samples.add(germany);

        Region japan = new Region("Japan", Region.Type.COUNTRY);
        japan.setCode("JPN");
        japan.setPopulation(125_000_000L);
        japan.setGdp(new Money(Real.of(5.0e12), "USD"));
        japan.setCapital("Tokyo");
        samples.add(japan);

        return samples;
    }

    /**
     * Clears the cached regions, forcing a reload on next access.
     */
    public void clearCache() {
        cachedRegions = null;
        LOG.debug("WorldBankLoader cache cleared");
    }
}
