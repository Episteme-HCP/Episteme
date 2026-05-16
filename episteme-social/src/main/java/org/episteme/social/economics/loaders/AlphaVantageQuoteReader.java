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
import org.episteme.social.economics.money.Money;
import org.episteme.social.economics.money.Quote;
import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.io.Configuration;
import org.episteme.core.ui.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.episteme.social.history.time.TimePoint;
import org.episteme.core.mathematics.numbers.real.Real;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Modern stock quote reader using Alpha Vantage API.
 * 
 * <p>Alpha Vantage provides free access to real-time and historical stock data.
 * A free API key is required (get one at https://www.alphavantage.co/support/#api-key).
 * 
 * <p>This replaces the deprecated {@code YahooQuoteLoader} and {@code YahooSymbolLoader}.
 * 
 * <p>Configure your API key via:
 * <ul>
 *   <li>Configuration property: {@code api.alphavantage.key}</li>
 *   <li>Environment variable: {@code ALPHAVANTAGE_API_KEY}</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AlphaVantageQuoteReader extends AbstractResourceReader<Quote> {

    private static final Logger LOG = LoggerFactory.getLogger(AlphaVantageQuoteReader.class);
    private static final String API_BASE = "https://www.alphavantage.co/query";
    
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final String apiKey;

    public AlphaVantageQuoteReader() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(15))
                .build();
        this.mapper = new ObjectMapper();
        
        // Try configuration first, then environment variable
        String key = Configuration.get("api.alphavantage.key", "");
        if (key.isEmpty()) {
            key = System.getenv("ALPHAVANTAGE_API_KEY");
        }
        this.apiKey = key != null ? key : "";
    }

    @Override
    public String getName() {
        return I18N.getInstance().get("reader.alphavantage.name", "Alpha Vantage Quote Reader");
    }

    @Override
    public String getCategory() {
        return I18N.getInstance().get("category.economics", "Economics");
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().get("reader.alphavantage.desc", "Stock quote reader using Alpha Vantage API.");
    }

    @Override
    public String getLongDescription() {
        return I18N.getInstance().get("reader.alphavantage.longdesc", 
            "Fetches real-time and historical stock quotes from Alpha Vantage. " +
            "Requires a free API key from alphavantage.co.");
    }

    @Override
    public String getResourcePath() {
        return API_BASE;
    }

    @Override
    public Class<Quote> getResourceType() {
        return Quote.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"v1"};
    }

    @Override
    protected Quote loadFromSource(String symbol) throws Exception {
        if (apiKey.isEmpty()) {
            LOG.warn("Alpha Vantage API key not configured. Set 'api.alphavantage.key' or ALPHAVANTAGE_API_KEY env var.");
            throw new IllegalStateException("Alpha Vantage API key not configured");
        }

        String url = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
                API_BASE, symbol, apiKey);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Episteme/5.0")
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Alpha Vantage API returned HTTP " + response.statusCode());
        }

        return parseQuote(symbol, response.body());
    }

    private Quote parseQuote(String symbol, String json) throws Exception {
        JsonNode root = mapper.readTree(json);
        
        // Check for error message
        if (root.has("Error Message")) {
            throw new Exception("API Error: " + root.get("Error Message").asText());
        }
        
        // Check for rate limit
        if (root.has("Note")) {
            LOG.warn("API rate limit reached: {}", root.get("Note").asText());
            throw new Exception("API rate limit reached. Try again later.");
        }

        JsonNode globalQuote = root.get("Global Quote");
        if (globalQuote == null || globalQuote.isEmpty()) {
            throw new Exception("No quote data found for symbol: " + symbol);
        }

        String sym = globalQuote.path("01. symbol").asText(symbol);
        Real price = Real.of(globalQuote.path("05. price").asText("0.0"));
        Real open = Real.of(globalQuote.path("02. open").asText("0.0"));
        Real high = Real.of(globalQuote.path("03. high").asText("0.0"));
        Real low = Real.of(globalQuote.path("04. low").asText("0.0"));
        long volume = globalQuote.path("06. volume").asLong(0);
        Real change = Real.of(globalQuote.path("09. change").asText("0.0"));
        String changePercent = globalQuote.path("10. change percent").asText("0%");

        Quote quote = new Quote(sym, sym, "Alpha Vantage");
        quote.update(volume, Money.usd(price), TimePoint.now());
        quote.setOpenPrice(Money.usd(open));
        quote.setHighPrice(Money.usd(high));
        quote.setLowPrice(Money.usd(low));
        quote.setChange(Money.usd(change));
        
        LOG.debug("Loaded quote for {}: ${} ({})", sym, price, changePercent);
        
        return quote;
    }

    /**
     * Searches for stock symbols matching the given keywords.
     * 
     * @param keywords Search keywords (e.g., "Apple" or "Microsoft")
     * @return JSON string with matching symbols
     * @throws Exception if search fails
     */
    public String searchSymbols(String keywords) throws Exception {
        if (apiKey.isEmpty()) {
            throw new IllegalStateException("Alpha Vantage API key not configured");
        }

        String url = String.format("%s?function=SYMBOL_SEARCH&keywords=%s&apikey=%s",
                API_BASE, keywords.replace(" ", "%20"), apiKey);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Episteme/5.0")
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Alpha Vantage API returned HTTP " + response.statusCode());
        }

        return response.body();
    }
}

