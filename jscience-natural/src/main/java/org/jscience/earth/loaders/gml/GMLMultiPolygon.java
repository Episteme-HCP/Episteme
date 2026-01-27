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

package org.jscience.earth.loaders.gml;

import java.util.*;

/** Multi-polygon geometry.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GMLMultiPolygon extends GMLGeometry {
    private final List<GMLPolygon> polygons = new ArrayList<>();

    public void addPolygon(GMLPolygon p) { if (p != null) polygons.add(p); }
    public List<GMLPolygon> getPolygons() { return Collections.unmodifiableList(polygons); }
    
    public double getTotalArea() {
        return polygons.stream().mapToDouble(GMLPolygon::getArea).sum();
    }
    
    @Override
    public String getGeometryType() { return "MultiPolygon"; }
    
    @Override
    public double[] getBoundingBox() {
        if (polygons.isEmpty()) return null;
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (GMLPolygon p : polygons) {
            double[] bbox = p.getBoundingBox();
            if (bbox != null) {
                minX = Math.min(minX, bbox[0]);
                minY = Math.min(minY, bbox[1]);
                maxX = Math.max(maxX, bbox[2]);
                maxY = Math.max(maxY, bbox[3]);
            }
        }
        return new double[]{minX, minY, maxX, maxY};
    }
}
