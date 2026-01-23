/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

/** Population of neurons of the same cell type. */
public class Population {
    private String id;
    private String component;
    private int size;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getComponent() { return component; }
    public void setComponent(String c) { this.component = c; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}
