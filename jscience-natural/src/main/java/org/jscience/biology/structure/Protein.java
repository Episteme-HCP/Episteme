package org.jscience.biology.structure;

import org.jscience.chemistry.Molecule;
import org.jscience.chemistry.Atom;
import org.jscience.chemistry.biochemistry.AminoAcid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a protein structure composed of polypeptide chains.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Protein extends Molecule {

    private final String pdbId;
    private final List<Chain> chains = new ArrayList<>();

    public Protein(String pdbId) {
        super(pdbId);
        this.pdbId = pdbId;
    }

    public String getPdbId() {
        return pdbId;
    }

    public void addChain(Chain chain) {
        chains.add(chain);
        // Also add all atoms to the parent Molecule for global operations
        for (Residue residue : chain.getResidues()) {
            for (Atom atom : residue.getAtoms()) {
                addAtom(atom);
            }
        }
    }

    public List<Chain> getChains() {
        return Collections.unmodifiableList(chains);
    }

    public Chain getChain(String chainId) {
        for (Chain c : chains) {
            if (c.getChainId().equals(chainId))
                return c;
        }
        return null;
    }

    // --- Inner Classes ---

    public static class Chain {
        private final String chainId;
        private final List<Residue> residues = new ArrayList<>();

        public Chain(String chainId) {
            this.chainId = chainId;
        }

        public String getChainId() {
            return chainId;
        }

        public void addResidue(Residue residue) {
            residues.add(residue);
        }

        public List<Residue> getResidues() {
            return Collections.unmodifiableList(residues);
        }

        /**
         * Returns the sequence as a string of one-letter codes.
         */
        public String getSequence() {
            StringBuilder sb = new StringBuilder();
            for (Residue r : residues) {
                if (r.getAminoAcidType() != null) {
                    sb.append(r.getAminoAcidType().getOneLetterCode());
                } else {
                    sb.append("?"); // Unknown or non-standard
                }
            }
            return sb.toString();
        }
    }

    public static class Residue {
        private final String name; // e.g., "GLY"
        private final int sequenceNumber;
        private final AminoAcid aminoAcidType; // Reference to type definition
        private final List<Atom> atoms = new ArrayList<>();

        public Residue(String name, int sequenceNumber) {
            this.name = name;
            this.sequenceNumber = sequenceNumber;
            // Try to resolve standard AminoAcid type
            AminoAcid type = null;
            try {
                // Name in PDB is 3-letter uppercase, e.g. "ALA"
                // AminoAcid.valueOf uses full name e.g. "Glycine"?
                // No, we have static constants but no lookup by 3-letter code efficiently
                // exposed in AminoAcid class currently
                // except maybe iterating values.
                // Or we can add a lookup to AminoAcid.
                // For now, let's try a simple helper or iteration.
                type = lookupBy3Letter(name);
            } catch (Exception e) {
                // Unknown residue
            }
            this.aminoAcidType = type;
        }

        private static AminoAcid lookupBy3Letter(String code3) {
            for (AminoAcid aa : AminoAcid.values()) {
                if (aa.getThreeLetterCode().equalsIgnoreCase(code3)) {
                    return aa;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }

        public AminoAcid getAminoAcidType() {
            return aminoAcidType;
        }

        public void addAtom(Atom atom) {
            atoms.add(atom);
        }

        public List<Atom> getAtoms() {
            return Collections.unmodifiableList(atoms);
        }
    }
}
