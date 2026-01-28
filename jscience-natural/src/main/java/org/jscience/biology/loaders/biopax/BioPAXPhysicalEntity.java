/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

import java.util.*;

/** 
 * BioPAX Physical Entity DTO.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BioPAXPhysicalEntity {
    private String rdfId;
    private String type;
    private String displayName;
    private String chemicalFormula;
    private String smiles;
    private String inchi;
    private Double stoichiometry = 1.0;
    private String sequence;
    private final List<BioPAXXref> xrefs = new ArrayList<>();

    public String getRdfId() { return rdfId; }
    public void setRdfId(String id) { this.rdfId = id; }
    
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String n) { this.displayName = n; }

    public String getChemicalFormula() { return chemicalFormula; }
    public void setChemicalFormula(String f) { this.chemicalFormula = f; }

    public String getSmiles() { return smiles; }
    public void setSmiles(String s) { this.smiles = s; }

    public String getInchi() { return inchi; }
    public void setInchi(String i) { this.inchi = i; }

    public Double getStoichiometry() { return stoichiometry; }
    public void setStoichiometry(Double s) { this.stoichiometry = s; }

    public String getSequence() { return sequence; }
    public void setSequence(String s) { this.sequence = s; }

    public void addXref(BioPAXXref xref) { if (xref != null) xrefs.add(xref); }
    public List<BioPAXXref> getXrefs() { return Collections.unmodifiableList(xrefs); }
    
    @Override
    public String toString() { return (type != null ? type : "Entity") + "{" + rdfId + "}"; }
}
