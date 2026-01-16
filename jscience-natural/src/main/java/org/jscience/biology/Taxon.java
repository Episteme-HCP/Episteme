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
