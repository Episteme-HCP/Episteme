/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.chemistry.loaders.cml;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for CML Molecule.
 */
public class CMLMolecule {
    private String id;
    private String title;
    private String formula;
    private String inchi;
    private String smiles;
    private final List<CMLAtom> atoms = new ArrayList<>();
    private final List<CMLBond> bonds = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getFormula() { return formula; }
    public void setFormula(String formula) { this.formula = formula; }
    public String getInchi() { return inchi; }
    public void setInchi(String inchi) { this.inchi = inchi; }
    public String getSmiles() { return smiles; }
    public void setSmiles(String smiles) { this.smiles = smiles; }
    
    public List<CMLAtom> getAtoms() { return atoms; }
    public void addAtom(CMLAtom atom) { atoms.add(atom); }
    
    public List<CMLBond> getBonds() { return bonds; }
    public void addBond(CMLBond bond) { bonds.add(bond); }
}
