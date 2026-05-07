/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.loaders.pdbml;

import org.episteme.natural.chemistry.Atom;
import org.episteme.natural.chemistry.Element;
import org.episteme.natural.chemistry.PeriodicTable;
import org.episteme.natural.biology.Protein;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.sets.Reals;

import java.util.*;

/**
 * Bridge for converting PDBML DTOs to core Episteme structural biology objects.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PDBMLBridge {

    /**
     * Converts PDBML structure to Episteme Protein.
     *
     * @param pdbModel the parsed PDBML model
     * @return a Protein object with full structural data
     */
    public Protein toProtein(PDBMLStructure pdbModel) {
        if (pdbModel == null) return null;
        
        Protein protein = new Protein(pdbModel.getEntryId());
        protein.setTrait("title", pdbModel.getTitle());
        protein.setTrait("resolution", pdbModel.getResolution());
        protein.setTrait("experimental_method", pdbModel.getExperimentalMethod());
        protein.setTrait("deposition_date", pdbModel.getDepositionDate());
        
        // Group atoms by chain and residue
        Map<String, Map<Integer, List<AtomSite>>> chainsMap = new LinkedHashMap<>();
        
        for (AtomSite atomSite : pdbModel.getAtoms()) {
            chainsMap.computeIfAbsent(atomSite.getChainId(), k -> new LinkedHashMap<>())
                     .computeIfAbsent(atomSite.getResidueSeq(), k -> new ArrayList<>())
                     .add(atomSite);
        }
        
        // Build chains and residues
        for (String chainId : chainsMap.keySet()) {
            Protein.Chain chain = new Protein.Chain(chainId);
            Map<Integer, List<AtomSite>> residuesMap = chainsMap.get(chainId);
            
            for (Integer resSeq : residuesMap.keySet()) {
                List<AtomSite> atomSites = residuesMap.get(resSeq);
                if (atomSites.isEmpty()) continue;
                
                String resName = atomSites.get(0).getResidueName();
                Protein.Residue residue = new Protein.Residue(resName, resSeq);
                
                for (AtomSite as : atomSites) {
                    Element element = PeriodicTable.getElement(as.getAtomSymbol());
                    Atom epistemeAtom = new Atom(element, DenseVector.of(List.of(Real.of(as.getX()), Real.of(as.getY()), Real.of(as.getZ())), Reals.getInstance()));
                    epistemeAtom.getTraits().put("pdb.atom_id", as.getAtomSymbol());
                    epistemeAtom.setTrait("pdb.name", as.getAtomName());
                    epistemeAtom.setTrait("occupancy", as.getOccupancy());
                    epistemeAtom.setTrait("temp_factor", as.getTempFactor());
                    residue.addAtom(epistemeAtom);
                }
                chain.addResidue(residue);
            }
            protein.addChain(chain);
        }
        
        return protein;
    }
}


