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

/**
 * Represents a phylogenetic tree.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Phylogeny {
    
    private String name;
    private String description;
    private boolean rooted;
    private String branchLengthUnit;
    private Clade rootClade;

    public Phylogeny() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isRooted() { return rooted; }
    public void setRooted(boolean rooted) { this.rooted = rooted; }

    public String getBranchLengthUnit() { return branchLengthUnit; }
    public void setBranchLengthUnit(String unit) { this.branchLengthUnit = unit; }

    public Clade getRootClade() { return rootClade; }
    public void setRootClade(Clade rootClade) { this.rootClade = rootClade; }

    /**
     * Returns the total number of terminal (leaf) nodes.
     */
    public int getLeafCount() {
        return rootClade != null ? rootClade.getLeafCount() : 0;
    }

    /**
     * Returns the maximum depth of the tree.
     */
    public int getMaxDepth() {
        return rootClade != null ? rootClade.getMaxDepth() : 0;
    }

    @Override
    public String toString() {
        return "Phylogeny{name='" + name + "', rooted=" + rooted + ", leaves=" + getLeafCount() + "}";
    }
}
