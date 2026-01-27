/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.biology.loaders.phyloxml;

import java.util.*;

/**
 * Represents a clade (node) in a phylogenetic tree.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Clade {
    
    private String name;
    private double branchLength = Double.NaN;
    private final Map<String, Double> confidenceValues = new HashMap<>();
    private Taxonomy taxonomy;
    private SequenceInfo sequence;
    private Events events;
    private final List<Clade> children = new ArrayList<>();

    public Clade() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBranchLength() { return branchLength; }
    public void setBranchLength(double branchLength) { this.branchLength = branchLength; }

    public void addConfidence(String type, double value) {
        confidenceValues.put(type, value);
    }

    public Map<String, Double> getConfidenceValues() {
        return Collections.unmodifiableMap(confidenceValues);
    }

    public Double getBootstrap() {
        return confidenceValues.get("bootstrap");
    }

    public Taxonomy getTaxonomy() { return taxonomy; }
    public void setTaxonomy(Taxonomy taxonomy) { this.taxonomy = taxonomy; }

    public SequenceInfo getSequence() { return sequence; }
    public void setSequence(SequenceInfo sequence) { this.sequence = sequence; }

    public Events getEvents() { return events; }
    public void setEvents(Events events) { this.events = events; }

    public List<Clade> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(Clade child) {
        if (child != null) {
            children.add(child);
        }
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    /**
     * Counts all leaf descendants.
     */
    public int getLeafCount() {
        if (isLeaf()) return 1;
        int count = 0;
        for (Clade child : children) {
            count += child.getLeafCount();
        }
        return count;
    }

    /**
     * Returns the maximum depth from this node.
     */
    public int getMaxDepth() {
        if (isLeaf()) return 0;
        int maxChildDepth = 0;
        for (Clade child : children) {
            maxChildDepth = Math.max(maxChildDepth, child.getMaxDepth());
        }
        return 1 + maxChildDepth;
    }

    /**
     * Returns a display label for this clade.
     */
    public String getDisplayLabel() {
        if (name != null && !name.isEmpty()) return name;
        if (taxonomy != null && taxonomy.getScientificName() != null) {
            return taxonomy.getScientificName();
        }
        return null;
    }

    @Override
    public String toString() {
        String label = getDisplayLabel();
        return "Clade{" + (label != null ? label : "unnamed") + 
               ", children=" + children.size() + "}";
    }
}
