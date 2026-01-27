/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.util.identity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.jscience.util.Commented;
import org.jscience.util.Named;

/**
 * Base class for all identification schemes.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public abstract class Identification implements Identified<Identification>, Named, Commented, Serializable {
    
    private final String value;
    private final Map<String, Object> traits = new HashMap<>();

    protected Identification(String value) {
        this.value = value;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public String getName() {
        String name = (String) getTrait("name");
        return name != null ? name : toString();
    }

    public void setName(String name) {
        setTrait("name", name);
    }

    /**
     * Returns the identification scheme (e.g., "UUID", "ISBN", "DOI").
     * @return the scheme name
     */
    public abstract String getScheme();

    @Override
    public Identification getId() {
        return this;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identification that = (Identification) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
