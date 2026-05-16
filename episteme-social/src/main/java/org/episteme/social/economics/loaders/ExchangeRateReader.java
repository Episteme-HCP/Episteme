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

package org.episteme.social.economics.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.episteme.core.io.AbstractResourceReader;

import org.episteme.core.ui.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Modern currency exchange rate reader using free public APIs.
 * 
 * <p>Supports multiple data sources:
 * <ul>
 *   <li><b>ECB (European Central Bank)</b> - Free, no API key required, updates daily</li>
 *   <li><b>Frankfurter</b> - Free, based on ECB data, no API key required</li>
 *   <li><b>ExchangeRate-API</b> - Free tier with 1500 requests/month</li>
 * </ul>
 * 
 * <p>This replaces the deprecated {@code OandaRateLoader}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ExchangeRateReader extends AbstractResourceReader<org.episteme.core.mathematics.numbers.real.Real> {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateReader.class);
    
    // Free APIs for exchange rates
    private static final String FRANKFURTER_API = "https://api.frankfurter.app/latest";

    private static final String EXCHANGE_RATE_API = "https://api.exchangerate-api.com/v4/latest";

    public enum Source {
        FRANKFURTER,  // Based on ECB, free, no key required
        ECB,          // European Central Bank, free, no key required
        EXCHANGE_RATE_API  // Free tier: 1500 requests/month
    }

    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final Source defaultSource;

    public ExchangeRateReader() {
        this(Source.FRANKFURTER); // Default to Frankfurter (most reliable free option)
    }

    public ExchangeRateReader(Source source) {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(15))
                .build();
        this.mapper = new ObjectMapper();
        this.defaultSource = source;
    }

    @Override
    public String getName() {
        return I18N.getInstance().get("reader.exchangerate.name", "Exchange Rate Reader");
    }

    @Override
    public String getCategory() {
        return I18N.getInstance().get("category.economics", "Economics");
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().get("reader.exchangerate.desc", "Currency exchange rate reader.");
    }

    @Override
    public String getLongDescription() {
        return I18N.getInstance().get("reader.exchangerate.longdesc", 
            "Fetches real-time currency exchange rates from free APIs including " +
            "the European Central Bank and Frankfurter. No API key required for basic usage.");
    }

    @Override
    public String getResourcePath() {
        return FRANKFURTER_API;
    }

    @Override
    public Class<org.episteme.core.mathematics.numbers.real.Real> getResourceType() {
        return org.episteme.core.mathematics.numbers.real.Real.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"Frankfurter v1", "ECB", "ExchangeRate-API v4"};
    }

    /**
     * Loads an exchange rate for a currency pair.
     * @param currencyPairId Format: "FROM/TO" (e.g., "USD/EUR" or "EUR/JPY")
     * @return The exchange rate (how many target units per 1 source unit)
     */
    @Override
    protected org.episteme.core.mathematics.numbers.real.Real loadFromSource(String currencyPairId) throws Exception {
        String[] parts = currencyPairId.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                "Invalid currency pair format. Expected: FROM/TO (e.g., USD/EUR)");
        }
        
        String from = parts[0].toUpperCase().trim();
        String to = parts[1].toUpperCase().trim();

        return switch (defaultSource) {
            case FRANKFURTER -> fetchFromFrankfurter(from, to);
            case ECB -> fetchFromECB(from, to);
            case EXCHANGE_RATE_API -> fetchFromExchangeRateApi(from, to);
        };
    }

    /**
     * Fetches exchange rate from Frankfurter API (based on ECB data).
     * Free, reliable, no API key required.
     */
    private org.episteme.core.mathematics.numbers.real.Real fetchFromFrankfurter(String from, String to) throws Exception {
        String url = String.format("%s?from=%s&to=%s", FRANKFURTER_API, from, to);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Episteme/5.0")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Frankfurter API returned HTTP " + response.statusCode());
        }

        JsonNode root = mapper.readTree(response.body());
        JsonNode rates = root.get("rates");
        
        if (rates == null || !rates.has(to)) {
            throw new Exception("Rate not found for " + from + "/" + to);
        }

        double rate = rates.get(to).asDouble();
        LOG.debug("Fetched rate {}/{}: {}", from, to, rate);
        return org.episteme.core.mathematics.numbers.real.Real.of(rate);
    }

    /**
     * Fetches exchange rate from ExchangeRate-API (free tier).
     */
    private org.episteme.core.mathematics.numbers.real.Real fetchFromExchangeRateApi(String from, String to) throws Exception {
        String url = EXCHANGE_RATE_API + "/" + from;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Episteme/5.0")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("ExchangeRate-API returned HTTP " + response.statusCode());
        }

        JsonNode root = mapper.readTree(response.body());
        JsonNode rates = root.get("rates");
        
        if (rates == null || !rates.has(to)) {
            throw new Exception("Rate not found for " + from + "/" + to);
        }

        return org.episteme.core.mathematics.numbers.real.Real.of(rates.get(to).asDouble());
    }

    /**
     * Fetches exchange rate from ECB (simplified, returns EUR-based rates).
     * Note: ECB only provides EUR-based rates, so non-EUR pairs require calculation.
     */
    private org.episteme.core.mathematics.numbers.real.Real fetchFromECB(String from, String to) throws Exception {
        // For simplicity, use Frankfurter which wraps ECB data in a simpler format
        return fetchFromFrankfurter(from, to);
    }

    /**
     * Gets all available rates for a base currency.
     * 
     * @param baseCurrency The base currency code (e.g., "USD", "EUR")
     * @return Map of currency codes to exchange rates
     */
    public Map<String, org.episteme.core.mathematics.numbers.real.Real> getAllRates(String baseCurrency) throws Exception {
        String url = String.format("%s?from=%s", FRANKFURTER_API, baseCurrency.toUpperCase());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Episteme/5.0")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("API returned HTTP " + response.statusCode());
        }

        JsonNode root = mapper.readTree(response.body());
        JsonNode rates = root.get("rates");
        
        Map<String, org.episteme.core.mathematics.numbers.real.Real> result = new HashMap<>();
        if (rates != null) {
            Iterator<String> fields = rates.fieldNames();
            while (fields.hasNext()) {
                String currency = fields.next();
                result.put(currency, org.episteme.core.mathematics.numbers.real.Real.of(rates.get(currency).asDouble()));
            }
        }

        LOG.debug("Fetched {} rates for base currency {}", result.size(), baseCurrency);
        return result;
    }

    /**
     * Converts an amount between currencies.
     * 
     * @param amount The amount to convert
     * @param from Source currency code
     * @param to Target currency code
     * @return The converted amount
     */
    public org.episteme.core.mathematics.numbers.real.Real convert(org.episteme.core.mathematics.numbers.real.Real amount, String from, String to) throws Exception {
        org.episteme.core.mathematics.numbers.real.Real rate = loadFromSource(from + "/" + to);
        return amount.multiply(rate);
    }
}

