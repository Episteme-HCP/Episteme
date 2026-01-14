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

package org.jscience.physics;

import java.util.Map;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Provider interface for NIST physical constants.
 * <p>
 * Intended to allow fetching the latest CODATA values from NIST archives.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface NISTConstantsProvider {

    /**
     * Fetches the latest constants.
     * 
     * @return map of constant name to value (Real)
     * @throws Exception if unreachable
     */
    Map<String, Real> fetchConstants() throws Exception;

    /**
     * Default stub implementation.
     */
    /**
     * Default implementation that attempts to query NIST, falls back to local
     * knowledge.
     */
    class CODATA2018 implements NISTConstantsProvider {
        private static final String NIST_URL;

        static {
            java.util.Properties props = new java.util.Properties();
            try (java.io.InputStream is = NISTConstantsProvider.class.getResourceAsStream("/jscience.properties")) {
                if (is != null)
                    props.load(is);
            } catch (Exception e) {
                /* ignore */ }
            NIST_URL = props.getProperty("nist.constants.url",
                    "https://physics.nist.gov/cuu/Constants/Table/allascii.txt");
        }

        @Override
        public Map<String, Real> fetchConstants() {
            try {
                java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .uri(java.net.URI.create(NIST_URL))
                        .GET()
                        .build();

                java.net.http.HttpResponse<String> response = client.send(request,
                        java.net.http.HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    Map<String, Real> constants = new java.util.HashMap<>();
                    String body = response.body();
                    // Parse NIST ASCII format
                    // Format roughly: "Quantity Value Uncertainty Unit"
                    // This is fragile parsing, demonstrating intent.

                    java.util.Scanner scanner = new java.util.Scanner(body);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.length() > 60 && !line.startsWith("Quantity")) {
                            // Heuristic parsing
                            String name = line.substring(0, 60).trim();
                            String remainder = line.substring(60).trim();
                            // Value is next token
                            String[] parts = remainder.split("\\s+");
                            if (parts.length > 0) {
                                try {
                                    String valStr = parts[0].replace(" ", "").replace("...", "");
                                    double val = Double.parseDouble(valStr);
                                    constants.put(name, Real.of(val));
                                } catch (NumberFormatException e) {
                                    // ignore header/footer text
                                }
                            }
                        }
                    }
                    scanner.close();
                    if (!constants.isEmpty())
                        return constants;
                }
            } catch (Exception e) {
                System.err.println("NIST Fetch failed: " + e.getMessage());
            }

            // Fallback
            System.out.println("Using fallback local NIST constants (2018).");
            Map<String, Real> c = new java.util.HashMap<>();
            c.put("speed of light in vacuum", Real.of(299792458));
            c.put("Planck constant", Real.of(6.62607015e-34));
            return c;
        }
    }
}


