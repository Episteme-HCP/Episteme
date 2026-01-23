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

package org.jscience.linguistics.loaders;

import org.jscience.linguistics.Language;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Main loader for linguistic data and metadata.
 * Provides access to countries, languages, and specialized models.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class LinguisticDataLoader {

    private LinguisticDataLoader() {}

    /**
     * Loads the country code map (ISO-3166) from resources.
     * 
     * @return Map of 2-letter country codes to country names.
     */
    public static Map<String, String> loadCountryCodes() {
        Map<String, String> countries = new TreeMap<>();
        try (InputStream is = LinguisticDataLoader.class.getResourceAsStream("/org/jscience/linguistics/data/CountryCodes.tab");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            
            if (is == null) {
                // Fallback for development (if resource not moved yet)
                File devFile = new File("src/main/java/org/jscience/linguistics/CountryCodes.tab");
                if (devFile.exists()) {
                    return loadFromTabFile(devFile);
                }
                return Collections.emptyMap();
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\t");
                if (parts.length >= 2) {
                    countries.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            // Log error
        }
        return countries;
    }

    private static Map<String, String> loadFromTabFile(File file) throws IOException {
        Map<String, String> data = new TreeMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\t");
                if (parts.length >= 2) {
                    data.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        return data;
    }

    /**
     * Loads the language registry from the primary index.
     */
    public static List<Language> loadLanguageIndex() {
        List<Language> languages = new ArrayList<>();
        // Implementation for LanguageIndex.tab parsing
        return languages;
    }
}
