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

package org.jscience.bibliography.loaders;

import org.jscience.io.cache.ResourceCache;
import org.jscience.io.AbstractResourceReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

/**
 * Connector to CrossRef API for DOI resolution and citation metadata.
 * <p>
 * <b>What it does</b>: Resolves DOIs to get publication metadata including
 * title, authors, journal, year, and citation information.
 * </p>
 *
 * <p>
 * <b>Data Source</b>: CrossRef REST API (https://api.crossref.org)
 * </p>
 * <p>
 * <b>License</b>: Public API, free for non-commercial use.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CrossRef extends AbstractResourceReader<CitationInfo> {

    @Override
    protected CitationInfo loadFromSource(String resourceId) throws Exception {
        // resourceId should be DOI
        return resolve(resourceId);
    }

    @Override
    public String getName() {
        return "loader.crossref.name";
    }

    @Override
    public String getCategory() {
        return "category.bibliography";
    }

    @Override
    public String getDescription() {
        return "loader.crossref.desc";
    }

    @Override
    public String getResourcePath() {
        return "https://api.crossref.org/works/";
    }

    @Override
    public Class<CitationInfo> getResourceType() {
        return CitationInfo.class;
    }

    private static final String API_BASE = org.jscience.io.Configuration.get(
            "api.crossref.base", "https://api.crossref.org/works/");

    /**
     * Resolves a DOI to get citation metadata.
     * 
     * @param doi DOI string (e.g., "10.1038/nature12373")
     * @return CitationInfo with metadata, or null if not found
     */
    public static CitationInfo resolve(String doi) {
        String cacheKey = "crossref_" + doi;
        Optional<String> cached = ResourceCache.global().get(cacheKey);

        String json;
        if (cached.isPresent()) {
            json = cached.get();
        } else {
            try {
                json = fetchDoi(doi);
                ResourceCache.global().put(cacheKey, json);
            } catch (Exception e) {
                System.err.println("WARNING: Failed to resolve DOI '" + doi + "': " + e.getMessage());
                return null;
            }
        }

        return parseCitationJson(json, doi);
    }

    /**
     * Searches for publications by title.
     * 
     * @param title publication title or keywords
     * @return JSON search results
     */
    public static String searchByTitle(String title) {
        try {
            String urlStr = API_BASE.replace("/works/", "/works?query.title=") +
                    java.net.URLEncoder.encode(title, "UTF-8") + "&rows=10";
            return fetchUrl(urlStr);
        } catch (Exception e) {
            throw new RuntimeException("CrossRef search failed", e);
        }
    }

    private static String fetchDoi(String doi) {
        String urlStr = API_BASE + doi;
        return fetchUrl(urlStr);
    }

    private static String fetchUrl(String urlStr) {
        try {
            URL url = URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "JScience/2.0 (mailto:silvere.martin@gmail.com)");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            conn.disconnect();
            return output.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch from CrossRef", e);
        }
    }

    private static CitationInfo parseCitationJson(String json, String doi) {
        CitationInfo.Builder builder = new CitationInfo.Builder().doi(doi);

        // Extract title
        String title = extractJsonArray(json, "title");
        if (title != null)
            builder.title(title);

        // Extract container-title (journal)
        String journal = extractJsonArray(json, "container-title");
        if (journal != null)
            builder.journal(journal);

        // Extract year from published-print or published-online
        String year = extractYear(json);
        if (year != null)
            builder.year(Integer.parseInt(year));

        // Extract authors
        String authors = extractAuthors(json);
        if (authors != null)
            builder.authors(authors);

        // Extract volume
        String volume = extractJsonValue(json, "volume");
        if (volume != null)
            builder.volume(volume);

        // Extract pages
        String pages = extractJsonValue(json, "page");
        if (pages != null)
            builder.pages(pages);

        return builder.build();
    }

    private static String extractJsonArray(String json, String key) {
        int idx = json.indexOf("\"" + key + "\":[");
        if (idx == -1)
            return null;
        int start = json.indexOf("[", idx) + 1;
        int end = json.indexOf("]", start);
        if (start <= 0 || end <= start)
            return null;
        String content = json.substring(start, end).trim();
        if (content.startsWith("\"")) {
            content = content.substring(1);
            int endQuote = content.indexOf("\"");
            if (endQuote > 0)
                content = content.substring(0, endQuote);
        }
        return content;
    }

    private static String extractJsonValue(String json, String key) {
        int idx = json.indexOf("\"" + key + "\":");
        if (idx == -1)
            return null;
        int start = idx + key.length() + 3;
        if (json.charAt(start) == '"') {
            int end = json.indexOf("\"", start + 1);
            return json.substring(start + 1, end);
        } else {
            int end = start;
            while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.')) {
                end++;
            }
            return json.substring(start, end);
        }
    }

    private static String extractYear(String json) {
        // Try published-print first, then published-online
        int idx = json.indexOf("\"published\":");
        if (idx == -1)
            idx = json.indexOf("\"published-print\":");
        if (idx == -1)
            idx = json.indexOf("\"published-online\":");
        if (idx == -1)
            return null;

        // Find date-parts array
        int dpIdx = json.indexOf("\"date-parts\":", idx);
        if (dpIdx == -1 || dpIdx > idx + 200)
            return null;
        int numStart = json.indexOf("[[", dpIdx);
        if (numStart == -1)
            return null;
        numStart += 2;
        int numEnd = numStart;
        while (numEnd < json.length() && Character.isDigit(json.charAt(numEnd))) {
            numEnd++;
        }
        return json.substring(numStart, numEnd);
    }

    private static String extractAuthors(String json) {
        int idx = json.indexOf("\"author\":[");
        if (idx == -1)
            return null;

        StringBuilder authors = new StringBuilder();
        int pos = idx;
        int count = 0;

        while (count < 5) { // Limit to 5 authors
            int famIdx = json.indexOf("\"family\":\"", pos);
            if (famIdx == -1 || famIdx > idx + 2000)
                break;

            int famEnd = json.indexOf("\"", famIdx + 10);
            String family = json.substring(famIdx + 10, famEnd);

            int givIdx = json.indexOf("\"given\":\"", famIdx);
            String given = "";
            if (givIdx != -1 && givIdx < famIdx + 100) {
                int givEnd = json.indexOf("\"", givIdx + 9);
                given = json.substring(givIdx + 9, givEnd);
            }

            if (authors.length() > 0)
                authors.append(", ");
            authors.append(family);
            if (!given.isEmpty()) {
                authors.append(", ").append(given.charAt(0)).append(".");
            }

            pos = famEnd;
            count++;
        }

        if (count >= 5)
            authors.append(" et al.");
        return authors.toString();
    }

    public CrossRef() {
    }
}
