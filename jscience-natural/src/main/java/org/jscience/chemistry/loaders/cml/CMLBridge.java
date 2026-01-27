/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.chemistry.loaders.cml;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.Molecule;
import org.jscience.chemistry.PeriodicTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bridge for converting CML (Chemical Markup Language) DTOs to core JScience chemistry objects.
 * <p>
 * CML is the XML standard for representing molecular data in chemistry. This bridge
 * converts parsed CML structures to the core JScience chemistry domain model.
 * </p>
 * 
 * <h2>Architecture</h2>
 * <pre>
 * CML XML → CMLReader → CML DTOs → CMLBridge → Core JScience Objects
 *                                              ├── Molecule
 *                                              ├── Atom
 *                                              ├── Bond
 *                                              └── Element
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see CMLReader
 * @see Molecule
 * @see Atom
 */
public class CMLBridge {

    /**
     * Converts a CML molecule representation to a JScience Molecule.
     *
     * @param cmlMolecule the CML molecule DTO
     * @return a fully populated Molecule object
     */
    public Molecule toMolecule(CMLMolecule cmlMolecule) {
        if (cmlMolecule == null) {
            return null;
        }
        
        String name = cmlMolecule.getTitle();
        if (name == null || name.isEmpty()) {
            name = cmlMolecule.getId();
        }
        
        Molecule molecule = new Molecule(name);
        molecule.setTrait("cml.id", cmlMolecule.getId());
        
        // Build atom map for bond resolution
        Map<String, Atom> atomMap = new HashMap<>();
        
        // Convert atoms
        if (cmlMolecule.getAtoms() != null) {
            for (CMLAtom cmlAtom : cmlMolecule.getAtoms()) {
                Atom atom = convertAtom(cmlAtom);
                if (atom != null) {
                    molecule.addAtom(atom);
                    atomMap.put(cmlAtom.getId(), atom);
                }
            }
        }
        
        // Convert bonds
        if (cmlMolecule.getBonds() != null) {
            for (CMLBond cmlBond : cmlMolecule.getBonds()) {
                Bond bond = convertBond(cmlBond, atomMap);
                if (bond != null) {
                    molecule.addBond(bond);
                }
            }
        }
        
        // Transfer molecular properties
        if (cmlMolecule.getFormula() != null) {
            molecule.setTrait("formula", cmlMolecule.getFormula());
        }
        if (cmlMolecule.getInchi() != null) {
            molecule.setTrait("inchi", cmlMolecule.getInchi());
        }
        if (cmlMolecule.getSmiles() != null) {
            molecule.setTrait("smiles", cmlMolecule.getSmiles());
        }
        
        return molecule;
    }

    /**
     * Converts a CML atom to a JScience Atom.
     */
    public Atom convertAtom(CMLAtom cmlAtom) {
        if (cmlAtom == null) {
            return null;
        }
        
        String elementSymbol = cmlAtom.getElementType();
        Element element = PeriodicTable.getElement(elementSymbol);
        
        Atom atom = new Atom(element);
        atom.setTrait("cml.id", cmlAtom.getId());
        
        // Set 3D coordinates if available
        if (cmlAtom.getX3() != null) {
            atom.setX(cmlAtom.getX3());
            atom.setY(cmlAtom.getY3());
            atom.setZ(cmlAtom.getZ3());
        }
        
        // Set formal charge
        if (cmlAtom.getFormalCharge() != null) {
            atom.setFormalCharge(cmlAtom.getFormalCharge());
        }
        
        // Set isotope if specified
        if (cmlAtom.getIsotope() != null) {
            atom.setTrait("isotope", cmlAtom.getIsotope());
        }
        
        return atom;
    }

    /**
     * Converts a CML bond to a JScience Bond.
     */
    public Bond convertBond(CMLBond cmlBond, Map<String, Atom> atomMap) {
        if (cmlBond == null || cmlBond.getAtomRefs() == null || cmlBond.getAtomRefs().length < 2) {
            return null;
        }
        
        String[] refs = cmlBond.getAtomRefs();
        Atom atom1 = atomMap.get(refs[0]);
        Atom atom2 = atomMap.get(refs[1]);
        
        if (atom1 == null || atom2 == null) {
            return null;
        }
        
        int order = parseBondOrder(cmlBond.getOrder());
        return new Bond(atom1, atom2, order);
    }

    /**
     * Parses CML bond order string to integer.
     */
    private int parseBondOrder(String order) {
        if (order == null) return 1;
        return switch (order.toUpperCase()) {
            case "S", "1", "SINGLE" -> 1;
            case "D", "2", "DOUBLE" -> 2;
            case "T", "3", "TRIPLE" -> 3;
            case "A", "AROMATIC" -> 4; // Aromatic represented as 4
            default -> 1;
        };
    }

    /**
     * Converts multiple CML molecules to JScience Molecules.
     */
    public List<Molecule> toMolecules(List<CMLMolecule> cmlMolecules) {
        List<Molecule> molecules = new ArrayList<>();
        if (cmlMolecules != null) {
            for (CMLMolecule cml : cmlMolecules) {
                Molecule m = toMolecule(cml);
                if (m != null) {
                    molecules.add(m);
                }
            }
        }
        return molecules;
    }
}
