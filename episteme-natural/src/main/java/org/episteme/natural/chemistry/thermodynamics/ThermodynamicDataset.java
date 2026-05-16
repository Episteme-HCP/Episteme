/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.chemistry.thermodynamics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.episteme.natural.chemistry.Molecule;

/**
 * Represents a dataset of thermodynamic properties.
 */
public class ThermodynamicDataset {
    private String title;
    private final List<ThermodynamicProperty> properties = new ArrayList<>();
    private final List<Molecule> compounds = new ArrayList<>();
    private final Map<String, Object> traits = new HashMap<>();

    public ThermodynamicDataset(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }

    public List<ThermodynamicProperty> getProperties() { return properties; }
    public void addProperty(ThermodynamicProperty p) { properties.add(p); }

    public List<Molecule> getCompounds() { return compounds; }
    public void addCompound(Molecule m) { compounds.add(m); }

    public void setTrait(String key, Object value) {
        traits.put(key, value);
    }
    
    public Object getTrait(String key) {
        return traits.get(key);
    }
}


