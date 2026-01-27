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

package org.jscience.geography;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.UniversalDataModel;
import java.util.*;

/**
 * Universal data model for spatial visualization.
 * Tracks locations (with values) and flows between them.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpatialDataSet implements UniversalDataModel {

    @Override
    public String getModelType() { return "SPATIAL_GEOMETRY"; }

    public record Location(String id, String label, Real x, Real y, Map<String, Real> values) {}
    public record Flow(String fromId, String toId, Real intensity) {}

    private final List<Location> locations = new ArrayList<>();
    private final List<Flow> flows = new ArrayList<>();
    private Real minX, maxX, minY, maxY;

    public void addLocation(String id, String label, Real x, Real y) {
        locations.add(new Location(id, label, x, y, new HashMap<>()));
        updateBounds(x, y);
    }

    public void addValue(String locId, String key, Real value) {
        locations.stream().filter(l -> l.id().equals(locId)).findFirst()
                .ifPresent(l -> l.values().put(key, value));
    }

    public void addFlow(String from, String to, Real intensity) {
        flows.add(new Flow(from, to, intensity));
    }

    private void updateBounds(Real x, Real y) {
        if (minX == null || x.compareTo(minX) < 0) minX = x;
        if (maxX == null || x.compareTo(maxX) > 0) maxX = x;
        if (minY == null || y.compareTo(minY) < 0) minY = y;
        if (maxY == null || y.compareTo(maxY) > 0) maxY = y;
    }

    public List<Location> getLocations() { return Collections.unmodifiableList(locations); }
    public List<Flow> getFlows() { return Collections.unmodifiableList(flows); }
    public Real getMinX() { return minX; }
    public Real getMaxX() { return maxX; }
    public Real getMinY() { return minY; }
    public Real getMaxY() { return maxY; }
}
