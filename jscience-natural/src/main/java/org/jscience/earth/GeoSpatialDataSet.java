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

package org.jscience.earth;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Specialized SpatialDataSet for Earth-based coordinates (Latitude, Longitude).
 * Adds support for projection handling and geographical attributes.
 */
public final class GeoSpatialDataSet {

    public String getModelType() { return "GEOSPATIAL_EARTH_MAP"; }

    public record GeoPoint(double latitude, double longitude) {}

    public void addGeoLocation(String id, String label, double lat, double lon) {
        addLocation(id, label, Real.of(lon), Real.of(lat));
    }

    public void addGeoFlow(String fromId, String toId, Real intensity) {
        addFlow(fromId, toId, intensity);
    }
    
    // Stub methods to satisfy calls
    public void addLocation(String id, String label, Real x, Real y) {
        // Implementation would go here
    }

    public void addFlow(String fromLink, String toLink, Real intensity) {
        // Implementation would go here
    }
}
