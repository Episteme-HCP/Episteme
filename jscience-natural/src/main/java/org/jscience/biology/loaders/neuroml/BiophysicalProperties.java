/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

import java.util.*;

/** Biophysical (electrophysiological) properties. */
public class BiophysicalProperties {
    private String id;
    private String specificCapacitance;
    private final List<ChannelDensity> channelDensities = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getSpecificCapacitance() { return specificCapacitance; }
    public void setSpecificCapacitance(String c) { this.specificCapacitance = c; }
    
    public void addChannelDensity(ChannelDensity cd) { if (cd != null) channelDensities.add(cd); }
    public List<ChannelDensity> getChannelDensities() { return Collections.unmodifiableList(channelDensities); }
}
