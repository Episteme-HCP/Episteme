/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

import java.util.*;

/** Neural network with populations and projections. */
public class Network {
    private String id;
    private final List<Population> populations = new ArrayList<>();
    private final List<Projection> projections = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public void addPopulation(Population p) { if (p != null) populations.add(p); }
    public List<Population> getPopulations() { return Collections.unmodifiableList(populations); }
    
    public void addProjection(Projection p) { if (p != null) projections.add(p); }
    public List<Projection> getProjections() { return Collections.unmodifiableList(projections); }
    
    public int getTotalNeuronCount() {
        return populations.stream().mapToInt(Population::getSize).sum();
    }
}
