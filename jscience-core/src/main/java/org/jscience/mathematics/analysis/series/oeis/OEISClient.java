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

package org.jscience.mathematics.analysis.series.oeis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jscience.mathematics.analysis.series.IntegerSequence;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.numbers.integers.Natural;

/**
 * Client for the Online Encyclopedia of Integer Sequences (OEIS).
 * <p>
 * Allows searching and fetching sequences from
 * <a href="https://oeis.org">oeis.org</a>.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OEISClient {

    private static final String SEARCH_URL = "https://oeis.org/search?q=id:%s&fmt=text";

    /**
     * Fetches a sequence by its OEIS ID (e.g., "A000045").
     * 
     * @param oeisId the OEIS identifier
     * @return the sequence data, or null if not found
     */
    public static IntegerSequence fetch(String oeisId) {
        if (!oeisId.matches("A\\d{6}")) {
            throw new IllegalArgumentException("Invalid OEIS ID format. Expected Axxxxxx (e.g., A000045)");
        }

        try {
            java.net.URI uri = java.net.URI.create(String.format(SEARCH_URL, oeisId));
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "JScience/1.0");

            if (conn.getResponseCode() != 200) {
                System.err.println("OEIS API Error: " + conn.getResponseCode());
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            List<Integer> terms = new ArrayList<>();
            String name = "Unknown Sequence";

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("%N " + oeisId)) {
                    name = line.substring(line.indexOf(oeisId) + 8).trim();
                }
                if (line.startsWith("%S " + oeisId) || line.startsWith("%T " + oeisId)
                        || line.startsWith("%U " + oeisId)) {
                    // Sequence data lines
                    String data = line.substring(line.indexOf(oeisId) + 8).trim();
                    String[] parts = data.split(",");
                    for (String part : parts) {
                        if (!part.trim().isEmpty()) {
                            try {
                                terms.add(Integer.of(Long.parseLong(part.trim())));
                            } catch (NumberFormatException e) {
                                // Ignore non-integer parts
                            }
                        }
                    }
                }
            }
            reader.close();

            if (terms.isEmpty()) {
                return null;
            }

            return new CachedOEISSequence(oeisId, name, terms);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Internal implementation of a cached OEIS sequence.
     */
    private static class CachedOEISSequence implements IntegerSequence {
        private final String id;
        private final String name;
        private final List<Integer> terms;

        public CachedOEISSequence(String id, String name, List<Integer> terms) {
            this.id = id;
            this.name = name;
            this.terms = terms;
        }

        public Integer get(Natural n) {
            int index = n.intValue();
            if (index >= terms.size()) {
                throw new IndexOutOfBoundsException(
                        "Term " + index + " not available in cached data (size: " + terms.size() + ")");
            }
            return terms.get(index);
        }

        public String getOEISId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDomain() {
            return "ℕ (0 to " + (terms.size() - 1) + ")";
        }
    }
}