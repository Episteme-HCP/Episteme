/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

/** Point geometry. */
public class GMLPoint extends GMLGeometry {
    private double x, y, z;

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    
    public double getZ() { return z; }
    public void setZ(double z) { this.z = z; }
    
    @Override
    public String getGeometryType() { return "Point"; }
    
    @Override
    public double[] getBoundingBox() {
        return new double[]{x, y, x, y};
    }
    
    @Override
    public String toString() {
        return String.format("Point(%.6f, %.6f)", x, y);
    }
}
