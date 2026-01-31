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

import org.jscience.core.io.AbstractResourceReader;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Modernized loader for linguistic data and metadata.
 * Implements {@link AbstractResourceReader} and provides caching/backup mechanisms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LinguisticDataReader extends AbstractResourceReader<Map<String, String>> {

    private static final String EXTERNAL_ISO_URL = "https://raw.githubusercontent.com/datasets/country-list/master/data.json";
    private static final String LOCAL_BACKUP_PATH = "linguistics/cache/country_catalog.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public LinguisticDataReader() {}

    @Override
    protected Map<String, String> loadFromSource(String id) throws Exception {
        if ("country_codes".equals(id)) {
            return loadCountryCodes();
        }
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, String> loadFromInputStream(InputStream is, String id) throws Exception {
        return objectMapper.readValue(is, new TypeReference<Map<String, String>>() {});
    }

    /**
     * Loads country codes from external source, with local JSON backup.
     */
    public Map<String, String> loadCountryCodes() {
        try {
            // Try fetching from external "known and efficient" resource
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(EXTERNAL_ISO_URL))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                List<Map<String, String>> data = objectMapper.readValue(response.body(), new TypeReference<>() {});
                Map<String, String> result = new TreeMap<>();
                for (Map<String, String> entry : data) {
                    result.put(entry.get("Code"), entry.get("Name"));
                }
                // Backup locally
                saveToBackup(result);
                return result;
            }
        } catch (Exception e) {
            // Fallback to local backup
            return loadFromBackup();
        }
        return Collections.emptyMap();
    }

    private void saveToBackup(Map<String, String> data) {
        try {
            Path backupPath = Paths.get(LOCAL_BACKUP_PATH);
            Files.createDirectories(backupPath.getParent());
            objectMapper.writeValue(backupPath.toFile(), data);
        } catch (IOException e) {
            // Log warning
        }
    }

    private Map<String, String> loadFromBackup() {
        try {
            File backup = new File(LOCAL_BACKUP_PATH);
            if (backup.exists()) {
                return objectMapper.readValue(backup, new TypeReference<Map<String, String>>() {});
            }
        } catch (IOException e) {
            // Log error
        }
        return Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Map<String, String>> getResourceType() {
        return (Class<Map<String, String>>) (Class<?>) Map.class;
    }

    @Override
    public String getName() {
        return "Linguistic Data Reader";
    }

    @Override
    public String getDescription() {
        return "Reads country codes and language metadata with external synchronization.";
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }

    @Override
    public String getCategory() {
        return "Linguistics";
    }

    @Override
    public String getResourcePath() {
        return "linguistics/data";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[]{"ISO-3166-1", "JSON Catalog 1.0"};
    }
}

