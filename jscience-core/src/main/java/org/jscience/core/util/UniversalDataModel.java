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

package org.jscience.core.util;

import java.util.Map;
import java.util.HashMap;

import java.util.Collections;
import org.jscience.core.measure.Quantity;

/**
 * Common interface for all universal data models in JScience.
 * Provides a unified way to access metadata and structure,
 * facilitating integration with external interfaces (MCP, Python, Web).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface UniversalDataModel {

    /**
     * Returns a map of metadata for this model (e.g., creation date, source).
     */
    default Map<String, Object> getMetadata() {
        return new HashMap<>();
    }

    /**
     * Returns a unique identifier for the type of data model.
     * Examples: "SPATIAL_GEOMETRY", "ECONOMIC_PORTFOLIO", "ARCHITECTURAL_PLAN".
     */
    String getModelType();

    /**
     * Returns the primary physical values associated with this model.
     * This allows generic tools to extract and convert measurements.
     */
    default Map<String, Quantity<?>> getQuantities() {
        return Collections.emptyMap();
    }

    /**
     * Validates the data model for consistency.
     */
    default boolean validate() {
        return true;
    }
}


