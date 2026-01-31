/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.biology.loaders.phyloxml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a parsed PhyloXML document.
 */
public class PhyloXMLDocument {
    private final List<PhyloXMLPhylogeny> phylogenies = new ArrayList<>();

    public void addPhylogeny(PhyloXMLPhylogeny phylogeny) {
        if (phylogeny != null) {
            phylogenies.add(phylogeny);
        }
    }

    public List<PhyloXMLPhylogeny> getPhylogenies() {
        return Collections.unmodifiableList(phylogenies);
    }
    
    public PhyloXMLPhylogeny getPhylogeny() {
        return phylogenies.isEmpty() ? null : phylogenies.get(0);
    }
}

