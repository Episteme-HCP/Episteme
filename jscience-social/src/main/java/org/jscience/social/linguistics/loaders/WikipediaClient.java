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

package org.jscience.social.linguistics.loaders;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Modern client for interfacing with online linguistic resources like Wikipedia.
 * Useful for fetching large-scale corpora for analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class WikipediaClient {

    private static final String API_ENDPOINT = "https://en.wikipedia.org/w/api.php";

    private WikipediaClient() {}

    /**
     * Fetches the plain text content of a Wikipedia article by title.
     *
     * @param title The title of the article.
     * @return The plain text content or an empty string if not found.
     * @throws IOException If a network error occurs.
     */
    public static String fetchArticleText(String title) throws IOException {
        String urlString = String.format("%s?action=query&prop=extracts&explaintext=1&titles=%s&format=json",
                API_ENDPOINT, URLEncoder.encode(title, StandardCharsets.UTF_8.name()));
        
        URL url = URI.create(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "JScience-Linguistics/1.0 (" + System.getProperty("os.name") + ")");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String json = reader.lines().collect(Collectors.joining("\n"));
            
            // Note: In a production environment, use a JSON library like Jackson.
            // This is a simple heuristic to extract the text from the Wikipedia JSON response.
            int extractStart = json.indexOf("\"extract\":\"");
            if (extractStart == -1) return "";
            
            extractStart += 11;
            int extractEnd = json.lastIndexOf("\"}");
            if (extractEnd <= extractStart) return "";
            
            String text = json.substring(extractStart, extractEnd);
            // Basic unescape of common JSON-encoded characters
            return text.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
        }
    }
}

