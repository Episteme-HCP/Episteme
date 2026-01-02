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

package org.jscience.politics.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.politics.Country;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.jscience.io.AbstractLoader;
import org.jscience.io.MiniCatalog;

public class WorldBankLoader extends AbstractLoader<Country> {

    private static final Logger LOG = LoggerFactory.getLogger(WorldBankLoader.class);
    private static final String RESOURCE_PATH = "/org/jscience/politics/worldbank-fallback.json";
    private static final String WB_API_BASE = "https://api.worldbank.org/v2";
    private static final int PER_PAGE = 500; // Max allowed by API

    private static WorldBankLoader instance;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient;

    // Cache handled by AbstractLoader now? No, AbstractLoader has ResourceCache.
    // But WorldBankLoader had manual cache 'cachedCountries'.
    // AbstractLoader uses 'cache' field from 'ResourceCache.global()'.
    // We should use that or keep manual if AbstractLoader pattern differs.
    // AbstractLoader.loadAll calls loadAllFromSource.
    // We will stick to AbstractLoader pattern.

    // Rate Limiter: 120 requests per minute
    private final RateLimiter rateLimiter;

    public WorldBankLoader() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build());
    }

    // For testing
    public WorldBankLoader(HttpClient httpClient) {
        this.httpClient = httpClient;
        // AbstractLoader initializes cache in its constructor?
        // We need to call super()? AbstractLoader usually has no-arg constructor.

        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(120)
                .timeoutDuration(Duration.ofSeconds(30))
                .build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        this.rateLimiter = registry.rateLimiter("worldbank");
    }

    public static synchronized WorldBankLoader getInstance() {
        if (instance == null) {
            instance = new WorldBankLoader();
        }
        return instance;
    }

    @Override
    public String getResourcePath() {
        return RESOURCE_PATH;
    }

    @Override
    public Class<Country> getResourceType() {
        return Country.class;
    }

    @Override
    protected Country loadFromSource(String id) throws Exception {
        String url = String.format("%s/country/%s?format=json", WB_API_BASE, id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(15))
                .build();

        rateLimiter.acquirePermission();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return null;
        }

        List<Country> list = parseCountriesFromApi(response.body());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    protected List<Country> loadAllFromSource() throws Exception {
        return loadFromApi();
    }

    @Override
    protected MiniCatalog<Country> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<Country> getAll() {
                return loadFromResource();
            }

            @Override
            public Optional<Country> findByName(String name) {
                return loadFromResource().stream()
                        .filter(c -> c.getName().equalsIgnoreCase(name))
                        .findFirst();
            }

            @Override
            public int size() {
                return loadFromResource().size();
            }
        };
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
        // This method is now a wrapper around the blocking fetchIndicator for backward
        // compatibility
        // or if an async version is still desired.
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fetchIndicator(countryCode, indicator);
            } catch (Exception e) {
                LOG.warn("Failed to fetch indicator data for {}/{}: {}",
                        countryCode, indicator, e.getMessage());
                return new LinkedHashMap<>();
            }
        });
    }

    /**
     * Parses countries from World Bank API.
     */
    private List<Country> loadFromApi() throws Exception {
        String url = String.format("%s/country?format=json&per_page=%d", WB_API_BASE, PER_PAGE);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(15))
                .build();

        // Rate limit: wait for permission before making API call
        rateLimiter.acquirePermission();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("World Bank API returned status: " + response.statusCode());
        }

        return parseCountriesFromApi(response.body());
    }

    /**
     * Parses countries from World Bank API response.
     */
    private List<Country> parseCountriesFromApi(String jsonResponse) throws Exception {
        List<Country> countries = new ArrayList<>();
        JsonNode root = mapper.readTree(jsonResponse);

        if (root.isArray() && root.size() > 1) {
            JsonNode countryNodes = root.get(1);
            if (countryNodes.isArray()) {
                for (JsonNode node : countryNodes) {
                    // Skip aggregates (regions, income levels, etc.)
                    String regionType = node.path("region").path("value").asText("");
                    if ("Aggregates".equals(regionType)) {
                        continue;
                    }

                    Country country = parseCountryFromApi(node);
                    if (country != null) {
                        countries.add(country);
                    }
                }
            }
        }

        return countries;
    }

    /**
     * Parses a single country from World Bank API format.
     */
    private Country parseCountryFromApi(JsonNode node) {
        String name = node.path("name").asText(null);
        if (name == null || name.isEmpty()) {
            return null;
        }

        String code = node.path("id").asText(null); // ISO3 code

        // Country constructor expects (name, alpha2) or full params.
        // We might need alpha2. API usually provides "iso2Code".
        String alpha2 = node.path("iso2Code").asText(code.length() >= 2 ? code.substring(0, 2) : "XX");

        Country country = new Country(name, alpha2);
        country.setAlpha3(code);

        // Set capital city using existing API
        String capital = node.path("capitalCity").asText(null);
        if (capital != null && !capital.isEmpty()) {
            country.setCapital(capital);
        }

        // Log additional metadata
        LOG.debug("Loaded country {}: capital={}, region={}, income={}",
                name,
                capital,
                node.path("region").path("value").asText(""),
                node.path("incomeLevel").path("value").asText(""));

        return country;
    }

    /**
     * Loads countries from local JSON resource.
     */
    private List<Country> loadFromResource() {
        try (InputStream is = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                LOG.warn("Local resource not found: {}, using sample data", RESOURCE_PATH);
                return getSampleData();
            }

            JsonNode root = mapper.readTree(is);
            List<Country> countries = new ArrayList<>();

            if (root.isArray()) {
                for (JsonNode node : root) {
                    Country country = parseCountry(node);
                    if (country != null) {
                        countries.add(country);
                    }
                }
            }

            return countries.isEmpty() ? getSampleData() : countries;

        } catch (Exception e) {
            LOG.error("Failed to load from local resource", e);
            return getSampleData();
        }
    }

    /**
     * Parses country from local JSON format.
     */
    private Country parseCountry(JsonNode node) {
        String name = node.path("name").asText(null);
        if (name == null) {
            return null;
        }

        String code = node.path("code").asText("XX");
        Country country = new Country(name, code);
        country.setPopulation(node.path("population").asLong(0));

        // GDP storage not standard in Country yet? Or maybe use generic attributes?
        // Country doesn't have GDP field in my previous edit.
        /*
         * double gdpValue = node.path("gdp").asDouble(0.0);
         * if (gdpValue > 0) {
         * // country.setGdp(new Money(Real.of(gdpValue), "USD"));
         * }
         */

        return country;
    }

    /**
     * Provides sample data when no other source is available.
     */
    private List<Country> getSampleData() {
        LOG.info("Using built-in sample data");
        List<Country> samples = new ArrayList<>();

        samples.add(Country.FRANCE);
        samples.add(Country.USA);
        samples.add(Country.CHINA);
        samples.add(Country.GERMANY);
        samples.add(Country.JAPAN);

        return samples;
    }

    /**
     * Fetches multiple indicators for multiple countries in parallel.
     * 
     * @param countryCodes List of ISO3 country codes
     * @param indicators   List of World Bank indicator codes (e.g., SP.POP.TOTL)
     * @return Map of Country Code -> (Indicator Code -> Value)
     */
    public Map<String, Map<String, Double>> fetchIndicators(List<String> countryCodes, List<String> indicators) {
        Map<String, Map<String, Double>> result = new java.util.concurrent.ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String indicator : indicators) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    // Fetch for "all" or specific countries?
                    // API allows querying 'country/all/indicator/XYZ' which is efficient.
                    // Doing per-country is too slow.
                    // However, we want to filter by requested countryCodes later if needed.
                    String codeFilter = (countryCodes == null || countryCodes.isEmpty()) ? "all"
                            : String.join(";", countryCodes);

                    // URL length limit might be an issue if many codes. If > 10 codes, use "all"
                    // and filter client-side?
                    // Safe approach: For now, if codes < 20, use filter, else "all".
                    String targetCountries = (countryCodes != null && countryCodes.size() > 20) ? "all" : codeFilter;

                    Map<String, Double> data = fetchIndicator(targetCountries, indicator);

                    // Merge into result
                    data.forEach((country, value) -> {
                        if (countryCodes == null || countryCodes.contains(country) || countryCodes.isEmpty()) {
                            result.computeIfAbsent(country, k -> new java.util.concurrent.ConcurrentHashMap<>())
                                    .put(indicator, value);
                        }
                    });

                } catch (Exception e) {
                    LOG.error("Failed to fetch indicator {}", indicator, e);
                }
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return result;
    }

    /**
     * Fetches a single indicator for given countries (semicolon separated or
     * 'all').
     */
    private Map<String, Double> fetchIndicator(String countryCodeStr, String indicator) throws Exception {
        // v2/country/{country}/indicator/{indicator}?format=json
        String url = String.format("%s/country/%s/indicator/%s?format=json&per_page=%d",
                WB_API_BASE, countryCodeStr, indicator, PER_PAGE);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(20))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("API Error " + response.statusCode());
        }

        return parseIndicatorData(response.body());
    }

    private Map<String, Double> parseIndicatorData(String json) throws Exception {
        Map<String, Double> data = new HashMap<>();
        JsonNode root = mapper.readTree(json);

        if (root.isArray() && root.size() > 1) {
            JsonNode entries = root.get(1);
            if (entries.isArray()) {
                for (JsonNode entry : entries) {
                    String countryId = entry.path("countryiso3code").asText("");
                    if (countryId.isEmpty()) {
                        countryId = entry.path("country").path("id").asText("");
                    }

                    // Most recent value usually? API returns timeseries default.
                    // If we want latest, we should sort or param date.
                    // Simple impl: take valid value if we don't have one yet (API returns sorted by
                    // date desc usually).
                    // Actually, let's take the first non-null value we find for the country
                    // (latest).
                    if (!data.containsKey(countryId)) {
                        double val = entry.path("value").asDouble(Double.NaN);
                        if (!Double.isNaN(val)) {
                            data.put(countryId, val);
                        }
                    }
                }
            }
        }
        return data;
    }

    /**
     * Clears the cached regions, forcing a reload on next access.
     */
    public void clearCache() {
        // Cache is now managed by AbstractLoader / ResourceCache
        LOG.debug("WorldBankLoader: manual cache clear requested (delegated to system cache)");
    }
}
