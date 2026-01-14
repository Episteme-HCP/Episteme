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

package org.jscience.chemistry.electrochemistry;

import org.jscience.util.SimpleJson;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Standard Electrode Potentials (EÃ‚Â°) at 25Ã‚Â°C.
 * Relative to Standard Hydrogen Electrode (SHE).
 * Loads data from potentials.json.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StandardPotentials {

    private static final Map<String, Double> POTENTIALS = new HashMap<>();

    static {
        loadPotentials();
    }

    @SuppressWarnings("unchecked")
    private static void loadPotentials() {
        try (InputStream is = StandardPotentials.class.getResourceAsStream("potentials.json")) {
            if (is == null) {
                System.err.println("StandardPotentials: Resource not found: potentials.json");
                return;
            }

            String jsonText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Object result = SimpleJson.parse(jsonText);

            if (result instanceof Map) {
                Map<String, Object> root = (Map<String, Object>) result;
                List<Object> list = (List<Object>) root.get("potentials");

                if (list != null) {
                    for (Object item : list) {
                        Map<String, Object> entry = (Map<String, Object>) item;
                        String reaction = (String) entry.get("reaction");
                        Double e0 = ((Number) entry.get("E0")).doubleValue();
                        POTENTIALS.put(reaction, e0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the standard potential for a given half-reaction representation.
     * 
     * @param halfReaction Key string (e.g., "Cu2+ + 2e- -> Cu")
     * @return EÃ‚Â° in Volts, or null if not found
     */
    public static Double getPotential(String halfReaction) {
        return POTENTIALS.get(halfReaction);
    }

    /**
     * Lists all registered half-reactions.
     */
    public static java.util.Set<String> getAvailableReactions() {
        return POTENTIALS.keySet();
    }
}


