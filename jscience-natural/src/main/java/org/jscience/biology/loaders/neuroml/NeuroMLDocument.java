/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

import java.util.*;

/** NeuroML document container. */
public class NeuroMLDocument {
    private String id;
    private final List<Cell> cells = new ArrayList<>();
    private final List<IonChannel> ionChannels = new ArrayList<>();
    private final List<Network> networks = new ArrayList<>();
    private final List<Synapse> synapses = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public void addCell(Cell cell) { if (cell != null) cells.add(cell); }
    public List<Cell> getCells() { return Collections.unmodifiableList(cells); }
    
    public void addIonChannel(IonChannel ch) { if (ch != null) ionChannels.add(ch); }
    public List<IonChannel> getIonChannels() { return Collections.unmodifiableList(ionChannels); }
    
    public void addNetwork(Network n) { if (n != null) networks.add(n); }
    public List<Network> getNetworks() { return Collections.unmodifiableList(networks); }
    
    public void addSynapse(Synapse s) { if (s != null) synapses.add(s); }
    public List<Synapse> getSynapses() { return Collections.unmodifiableList(synapses); }
}
