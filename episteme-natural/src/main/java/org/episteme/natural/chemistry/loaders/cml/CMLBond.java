/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.chemistry.loaders.cml;

/**
 * DTO for CML Bond.
 */
public class CMLBond {
    private String id;
    private String[] atomRefs;
    private String order;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String[] getAtomRefs() { return atomRefs; }
    public void setAtomRefs(String[] atomRefs) { this.atomRefs = atomRefs; }
    public String getOrder() { return order; }
    public void setOrder(String order) { this.order = order; }
}

