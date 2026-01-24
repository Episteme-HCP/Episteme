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

package org.jscience.bibliography.loaders;

import org.jscience.io.cache.ResourceCache;
import org.jscience.io.AbstractResourceReader;
import org.jscience.util.SimpleJson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Modernized connector to CrossRef API for DOI resolution and citation metadata.
 * <p>
 * Uses {@link org.jscience.util.SimpleJson} for robust metadata extraction.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @version 2.0 (Modernized)
 */
public class CrossRefReader extends AbstractResourceReader<CitationInfo> {

    private static final String API_BASE = "https://api.crossref.org/works/";

    @Override
    protected CitationInfo loadFromSource(String doi) throws Exception {
        return resolve(doi);
    }

    public static CitationInfo resolve(String doi) {
        String cacheKey = "crossref_" + doi;
        Optional<String> cached = ResourceCache.global().get(cacheKey);

        String json;
        if (cached.isPresent()) {
            json = cached.get();
        } else {
            try {
                json = fetchUrl(API_BASE + doi);
                ResourceCache.global().put(cacheKey, json);
            } catch (Exception e) {
                return null;
            }
        }

        return parseCitationJson(json, doi);
    }

    private static String fetchUrl(String urlStr) throws Exception {
        URL url = URI.create(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "JScience/2.0 (mailto:admin@jscience.org)");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error: " + conn.getResponseCode());
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) output.append(line);
            return output.toString();
        } finally {
            conn.disconnect();
        }
    }

    @SuppressWarnings("unchecked")
    private static CitationInfo parseCitationJson(String json, String doi) {
        try {
            Map<String, Object> root = (Map<String, Object>) SimpleJson.parse(json);
            Map<String, Object> message = (Map<String, Object>) root.get("message");
            if (message == null) return null;

            CitationInfo.Builder builder = new CitationInfo.Builder().doi(doi);

            // Title
            List<Object> titles = (List<Object>) message.get("title");
            if (titles != null && !titles.isEmpty()) builder.title(titles.get(0).toString());

            // Journal
            List<Object> journals = (List<Object>) message.get("container-title");
            if (journals != null && !journals.isEmpty()) builder.journal(journals.get(0).toString());

            // Year
            Map<String, Object> published = (Map<String, Object>) message.get("published-print");
            if (published == null) published = (Map<String, Object>) message.get("published-online");
            if (published != null) {
                List<Object> parts = (List<Object>) published.get("date-parts");
                if (parts != null && !parts.isEmpty()) {
                    List<Object> yearPart = (List<Object>) parts.get(0);
                    if (!yearPart.isEmpty()) builder.year(((Number) yearPart.get(0)).intValue());
                }
            }

            // Authors
            List<Map<String, Object>> authors = (List<Map<String, Object>>) message.get("author");
            if (authors != null) {
                StringBuilder authorStr = new StringBuilder();
                for (int i = 0; i < Math.min(authors.size(), 5); i++) {
                    Map<String, Object> a = authors.get(i);
                    if (authorStr.length() > 0) authorStr.append(", ");
                    authorStr.append(a.get("family"));
                    Object given = a.get("given");
                    if (given != null) authorStr.append(", ").append(given.toString().charAt(0)).append(".");
                }
                if (authors.size() > 5) authorStr.append(" et al.");
                builder.authors(authorStr.toString());
            }

            builder.volume((String) message.get("volume"));
            builder.pages((String) message.get("page"));

            return builder.build();
        } catch (Exception e) {
            return null;
        }
    }

    @Override public String getName() { return "CrossRef Connector"; }
    @Override public String getCategory() { return "Bibliography"; }
    @Override public String getDescription() { return "Resolves DOIs to get publication metadata."; }
    @Override public Class<CitationInfo> getResourceType() { return CitationInfo.class; }
    @Override public String getResourcePath() { return API_BASE; }
    @Override public String[] getSupportedVersions() { return new String[]{"v1"}; }
}
