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

/** Geographic feature with geometry and properties.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GMLFeature {
    private String id;
    private String typeName;
    private GMLGeometry geometry;
    private final Map<String, String> properties = new LinkedHashMap<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTypeName() { return typeName; }
    public void setTypeName(String t) { this.typeName = t; }
    
    public GMLGeometry getGeometry() { return geometry; }
    public void setGeometry(GMLGeometry g) { this.geometry = g; }
    
    public void setProperty(String name, String value) { properties.put(name, value); }
    public String getProperty(String name) { return properties.get(name); }
    public Map<String, String> getProperties() { return Collections.unmodifiableMap(properties); }
    
    @Override
    public String toString() {
        return "Feature{id=" + id + ", type=" + typeName + ", geom=" + 
               (geometry != null ? geometry.getGeometryType() : "none") + "}";
    }
}
