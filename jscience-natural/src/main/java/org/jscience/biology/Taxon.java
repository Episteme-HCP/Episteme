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

package org.jscience.biology;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a taxonomic group in a phylogenetic tree.
 */
public class Taxon {
    private String id;
    private String parentId;
    private String name;
    private List<Taxon> children = new ArrayList<>();
    
    private org.jscience.mathematics.numbers.real.Real coi;
    private org.jscience.mathematics.numbers.real.Real rna16s;
    private org.jscience.mathematics.numbers.real.Real cytb;
    
    // Layout properties (transient)
    public transient double x, y, angle, radius;

    public Taxon(String id, String parentId, String name, org.jscience.mathematics.numbers.real.Real coi, org.jscience.mathematics.numbers.real.Real rna16s, org.jscience.mathematics.numbers.real.Real cytb) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.coi = coi;
        this.rna16s = rna16s;
        this.cytb = cytb;
    }

    public void addChild(Taxon t) {
        children.add(t);
    }
    
    public List<Taxon> getChildren() { return children; }
    public String getName() { return name; }
    public String getId() { return id; }
    public String getParentId() { return parentId; }
    public org.jscience.mathematics.numbers.real.Real getCoi() { return coi; }
    public org.jscience.mathematics.numbers.real.Real getRna16s() { return rna16s; }
    public org.jscience.mathematics.numbers.real.Real getCytb() { return cytb; }
}
