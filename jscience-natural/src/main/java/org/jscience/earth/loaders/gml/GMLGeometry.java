/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

/** Base class for GML geometries. */
public abstract class GMLGeometry {
    protected String srsName;
    
    public String getSrsName() { return srsName; }
    public void setSrsName(String srs) { this.srsName = srs; }
    
    public abstract String getGeometryType();
    public abstract double[] getBoundingBox();
}
