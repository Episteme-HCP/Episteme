/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

import java.util.*;

/** Geographic feature with geometry and properties. */
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
