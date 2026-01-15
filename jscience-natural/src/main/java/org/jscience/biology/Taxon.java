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
    
    // Genetic Markers (Simulated)
    private double coi;
    private double rna16s;
    private double cytb;
    
    // Layout properties (transient)
    public transient double x, y, angle, radius;

    public Taxon(String id, String parentId, String name, double coi, double rna16s, double cytb) {
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
    public double getCoi() { return coi; }
    public double getRna16s() { return rna16s; }
    public double getCytb() { return cytb; }
}
