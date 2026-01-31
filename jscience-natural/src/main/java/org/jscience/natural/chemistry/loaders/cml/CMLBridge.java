/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.chemistry.loaders.cml;

import org.jscience.natural.chemistry.Atom;
import org.jscience.natural.chemistry.Bond;
import org.jscience.natural.chemistry.Element;
import org.jscience.natural.chemistry.Molecule;
import org.jscience.natural.chemistry.PeriodicTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;

/**
 * Bridge for converting CML (Chemical Markup Language) DTOs to core JScience chemistry objects.
 * <p>
 * CML is the XML standard for representing molecular data in chemistry. This bridge
 * converts parsed CML structures to the core JScience chemistry domain model.
 * </p>
 * 
 * <h2>Architecture</h2>
 * <pre>
 * CML XML â†’ CMLReader â†’ CML DTOs â†’ CMLBridge â†’ Core JScience Objects
 *                                              â”œâ”€â”€ Molecule
 *                                              â”œâ”€â”€ Atom
 *                                              â”œâ”€â”€ Bond
 *                                              â””â”€â”€ Element
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
        
        // CML coordinates are typically in Angstroms, convert to meters if needed
        // but core Atom position is a Vector<Real>.
        double x = cmlAtom.getX3() != null ? cmlAtom.getX3() : 0.0;
        double y = cmlAtom.getY3() != null ? cmlAtom.getY3() : 0.0;
        double z = cmlAtom.getZ3() != null ? cmlAtom.getZ3() : 0.0;
        
        List<Real> coords = new ArrayList<>();
        coords.add(Real.of(x));
        coords.add(Real.of(y));
        coords.add(Real.of(z));
        
        org.jscience.core.mathematics.linearalgebra.Vector<Real> position = 
            DenseVector.of(coords, Reals.getInstance());
        
        Atom atom = new Atom(element, position);
        atom.setTrait("cml.id", cmlAtom.getId());
        
        // Set formal charge
        if (cmlAtom.getFormalCharge() != null) {
            atom.setFormalCharge(Quantities.create(
                cmlAtom.getFormalCharge(), 
                Units.COULOMB
            ));
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
        
        org.jscience.natural.chemistry.BondType type = parseBondOrder(cmlBond.getOrder());
        return new Bond(atom1, atom2, type);
    }

    /**
     * Parses CML bond order string to BondType.
     */
    private org.jscience.natural.chemistry.BondType parseBondOrder(String order) {
        if (order == null) return org.jscience.natural.chemistry.BondType.SINGLE;
        return switch (order.toUpperCase()) {
            case "S", "1", "SINGLE" -> org.jscience.natural.chemistry.BondType.SINGLE;
            case "D", "2", "DOUBLE" -> org.jscience.natural.chemistry.BondType.DOUBLE;
            case "T", "3", "TRIPLE" -> org.jscience.natural.chemistry.BondType.TRIPLE;
            case "A", "AROMATIC" -> org.jscience.natural.chemistry.BondType.AROMATIC;
            default -> org.jscience.natural.chemistry.BondType.SINGLE;
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


