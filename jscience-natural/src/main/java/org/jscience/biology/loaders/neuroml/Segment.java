/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

/** A segment (compartment) of a neuron. */
public class Segment {
    private String id;
    private String name;
    private String parentId;
    private Point3D proximal;
    private Point3D distal;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    
    public Point3D getProximal() { return proximal; }
    public void setProximal(Point3D p) { this.proximal = p; }
    
    public Point3D getDistal() { return distal; }
    public void setDistal(Point3D p) { this.distal = p; }
    
    /** Calculate segment length in micrometers. */
    public double getLength() {
        if (proximal == null || distal == null) return 0;
        return proximal.distanceTo(distal);
    }
    
    @Override
    public String toString() { return "Segment{id='" + id + "', name='" + name + "'}"; }
}
