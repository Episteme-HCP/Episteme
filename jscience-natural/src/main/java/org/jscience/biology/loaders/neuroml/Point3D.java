/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

/** 3D point with diameter for morphology. */
public class Point3D {
    private final double x, y, z;
    private final double diameter;

    public Point3D(double x, double y, double z, double diameter) {
        this.x = x; this.y = y; this.z = z; this.diameter = diameter;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getDiameter() { return diameter; }
    
    public double distanceTo(Point3D other) {
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    
    @Override
    public String toString() { 
        return String.format("(%.2f, %.2f, %.2f, d=%.2f)", x, y, z, diameter); 
    }
}
