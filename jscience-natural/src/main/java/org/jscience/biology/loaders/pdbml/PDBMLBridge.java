/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.pdbml;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.Molecule;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.biology.macromolecules.Protein;
import org.jscience.biology.macromolecules.AminoAcid;
import org.jscience.biology.macromolecules.ProteinChain;
import org.jscience.biology.macromolecules.SecondaryStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bridge for converting PDBML DTOs to core JScience structural biology objects.
 * <p>
 * PDBML is the XML format for macromolecular structures from the Protein Data Bank.
 * This bridge converts parsed PDBML data to core JScience protein and chemistry structures.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * PDBML → PDBMLReader → Structure DTOs → PDBMLBridge → Core Objects
 *                                                      ├── Protein
 *                                                      ├── ProteinChain
 *                                                      ├── AminoAcid
 *                                                      ├── Molecule (ligands)
 *                                                      └── Atom
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PDBMLBridge {

    private static final Map<String, AminoAcid.Type> AMINO_ACID_MAP = new HashMap<>();
    
    static {
        AMINO_ACID_MAP.put("ALA", AminoAcid.Type.ALANINE);
        AMINO_ACID_MAP.put("ARG", AminoAcid.Type.ARGININE);
        AMINO_ACID_MAP.put("ASN", AminoAcid.Type.ASPARAGINE);
        AMINO_ACID_MAP.put("ASP", AminoAcid.Type.ASPARTIC_ACID);
        AMINO_ACID_MAP.put("CYS", AminoAcid.Type.CYSTEINE);
        AMINO_ACID_MAP.put("GLU", AminoAcid.Type.GLUTAMIC_ACID);
        AMINO_ACID_MAP.put("GLN", AminoAcid.Type.GLUTAMINE);
        AMINO_ACID_MAP.put("GLY", AminoAcid.Type.GLYCINE);
        AMINO_ACID_MAP.put("HIS", AminoAcid.Type.HISTIDINE);
        AMINO_ACID_MAP.put("ILE", AminoAcid.Type.ISOLEUCINE);
        AMINO_ACID_MAP.put("LEU", AminoAcid.Type.LEUCINE);
        AMINO_ACID_MAP.put("LYS", AminoAcid.Type.LYSINE);
        AMINO_ACID_MAP.put("MET", AminoAcid.Type.METHIONINE);
        AMINO_ACID_MAP.put("PHE", AminoAcid.Type.PHENYLALANINE);
        AMINO_ACID_MAP.put("PRO", AminoAcid.Type.PROLINE);
        AMINO_ACID_MAP.put("SER", AminoAcid.Type.SERINE);
        AMINO_ACID_MAP.put("THR", AminoAcid.Type.THREONINE);
        AMINO_ACID_MAP.put("TRP", AminoAcid.Type.TRYPTOPHAN);
        AMINO_ACID_MAP.put("TYR", AminoAcid.Type.TYROSINE);
        AMINO_ACID_MAP.put("VAL", AminoAcid.Type.VALINE);
    }

    /**
     * Converts PDBML structure to JScience Protein.
     *
     * @param pdbModel the parsed PDBML model
     * @return a Protein object with full structural data
     */
    public Protein toProtein(PDBMLStructure pdbModel) {
        if (pdbModel == null) {
            return null;
        }
        
        Protein protein = new Protein(pdbModel.getPdbId());
        protein.setTrait("pdb.id", pdbModel.getPdbId());
        protein.setTrait("title", pdbModel.getTitle());
        protein.setTrait("resolution", pdbModel.getResolution());
        protein.setTrait("experiment.type", pdbModel.getExperimentType());
        protein.setTrait("deposition.date", pdbModel.getDepositionDate());
        
        // Convert chains
        if (pdbModel.getChains() != null) {
            for (PDBMLChain pdbChain : pdbModel.getChains()) {
                ProteinChain chain = convertChain(pdbChain);
                if (chain != null) {
                    protein.addChain(chain);
                }
            }
        }
        
        // Convert ligands as Molecules
        if (pdbModel.getLigands() != null) {
            for (PDBMLLigand ligand : pdbModel.getLigands()) {
                Molecule mol = convertLigand(ligand);
                if (mol != null) {
                    protein.addLigand(mol);
                }
            }
        }
        
        return protein;
    }

    /**
     * Converts PDBML chain to JScience ProteinChain.
     */
    public ProteinChain convertChain(PDBMLChain pdbChain) {
        if (pdbChain == null) {
            return null;
        }
        
        ProteinChain chain = new ProteinChain(pdbChain.getChainId());
        
        // Convert residues to amino acids
        if (pdbChain.getResidues() != null) {
            for (PDBMLResidue residue : pdbChain.getResidues()) {
                AminoAcid aa = convertResidue(residue);
                if (aa != null) {
                    chain.addResidue(aa);
                }
            }
        }
        
        // Convert secondary structure elements
        if (pdbChain.getHelices() != null) {
            for (PDBMLHelix helix : pdbChain.getHelices()) {
                chain.addSecondaryStructure(SecondaryStructure.ALPHA_HELIX, 
                    helix.getStartResId(), helix.getEndResId());
            }
        }
        
        if (pdbChain.getSheets() != null) {
            for (PDBMLSheet sheet : pdbChain.getSheets()) {
                chain.addSecondaryStructure(SecondaryStructure.BETA_SHEET,
                    sheet.getStartResId(), sheet.getEndResId());
            }
        }
        
        return chain;
    }

    /**
     * Converts PDBML residue to JScience AminoAcid.
     */
    public AminoAcid convertResidue(PDBMLResidue residue) {
        if (residue == null) {
            return null;
        }
        
        AminoAcid.Type type = AMINO_ACID_MAP.get(residue.getResName());
        if (type == null) {
            type = AminoAcid.Type.UNKNOWN;
        }
        
        AminoAcid aa = new AminoAcid(type, residue.getResSeq());
        
        // Add atomic coordinates
        if (residue.getAtoms() != null) {
            for (PDBMLAtom pdbAtom : residue.getAtoms()) {
                Atom atom = convertAtom(pdbAtom);
                if (atom != null) {
                    aa.addAtom(atom);
                }
            }
        }
        
        return aa;
    }

    /**
     * Converts PDBML atom to JScience Atom.
     */
    public Atom convertAtom(PDBMLAtom pdbAtom) {
        if (pdbAtom == null) {
            return null;
        }
        
        Element element = PeriodicTable.getElement(pdbAtom.getElement());
        Atom atom = new Atom(element);
        
        atom.setX(pdbAtom.getX());
        atom.setY(pdbAtom.getY());
        atom.setZ(pdbAtom.getZ());
        atom.setTrait("pdb.serial", pdbAtom.getSerial());
        atom.setTrait("pdb.name", pdbAtom.getName());
        atom.setTrait("occupancy", pdbAtom.getOccupancy());
        atom.setTrait("bfactor", pdbAtom.getBFactor());
        
        return atom;
    }

    /**
     * Converts PDBML ligand to JScience Molecule.
     */
    public Molecule convertLigand(PDBMLLigand ligand) {
        if (ligand == null) {
            return null;
        }
        
        Molecule mol = new Molecule(ligand.getName());
        mol.setTrait("pdb.het.id", ligand.getHetId());
        mol.setTrait("formula", ligand.getFormula());
        
        // Add ligand atoms
        if (ligand.getAtoms() != null) {
            for (PDBMLAtom pdbAtom : ligand.getAtoms()) {
                Atom atom = convertAtom(pdbAtom);
                if (atom != null) {
                    mol.addAtom(atom);
                }
            }
        }
        
        return mol;
    }
}
