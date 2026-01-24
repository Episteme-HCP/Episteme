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

package org.jscience.architecture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Analytical engine for the virtual reconstruction of historical buildings from 
 * archaeological and archival textual sources. It parses descriptive strings 
 * into structured architectural features.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class HistoricalReconstructionEngine {

    private HistoricalReconstructionEngine() {}

    /**
     * Represents a discrete architectural feature extracted from a source.
     */
    public record BuildingFeature(String type, String material, double value) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Parses a semicolon-separated string of building descriptions into a list 
     * of structured BuildingFeature records.
     * 
     * <p>Format: "Type.Material.Value; Type2.Material2.Value2"</p>
     * <p>Example: "Wall.Stone.10.5; Column.Marble.4.0"</p>
     * 
     * @param description raw descriptive string from historical source
     * @return list of extracted BuildingFeatures
     */
    public static List<BuildingFeature> reconstruct(String description) {
        List<BuildingFeature> features = new ArrayList<>();
        if (description == null || description.isEmpty()) return features;
        
        String[] parts = description.split(";");
        for (String p : parts) {
            String[] tokens = p.trim().split("\\.");
            if (tokens.length >= 3) {
                try {
                    features.add(new BuildingFeature(
                        tokens[0], 
                        tokens[1], 
                        Double.parseDouble(tokens[2])
                    ));
                } catch (NumberFormatException e) {
                    // Skip invalid numeric values
                }
            }
        }
        return features;
    }
}
