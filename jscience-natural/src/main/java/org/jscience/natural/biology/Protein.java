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

package org.jscience.natural.biology;

import org.jscience.natural.chemistry.Molecule;
import org.jscience.natural.chemistry.Atom;
import org.jscience.natural.chemistry.biochemistry.AminoAcid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Relation;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents a protein structure composed of polypeptide chains.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Protein extends Molecule {
    private static final long serialVersionUID = 2L;

    @Attribute
    private final String pdbId;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Chain> chains = new ArrayList<>();

    public Protein(String pdbId) {
        super(pdbId);
        this.pdbId = pdbId;
        setName(pdbId);
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

    /**
     * Sets the sequence for this protein. 
     * Creates a single default chain if the sequence is provided.
     * @param sequence the protein sequence (one-letter codes)
     */
    public void setSequence(String sequence) {
        if (sequence == null) return;
        Chain chain = new Chain("A");
        for (int i = 0; i < sequence.length(); i++) {
            char code = sequence.charAt(i);
            // We don't have atomic detail from a simple sequence, but we can create the residues
            Residue residue = new Residue(AminoAcid.fromCode(code).getThreeLetterCode(), i + 1);
            chain.addResidue(residue);
        }
        addChain(chain);
    }

    public Chain getChain(String chainId) {
        for (Chain c : chains) {
            if (c.getChainId().equals(chainId))
                return c;
        }
        return null;
    }

    // --- Inner Classes ---

    @Persistent
    public static class Chain {
        @Attribute
        private final String chainId;

        @Relation(type = Relation.Type.ONE_TO_MANY)
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

    @Persistent
    public static class Residue {
        @Attribute
        private final String name; // e.g., "GLY"
        @Attribute
        private final int sequenceNumber;
        @Attribute
        private final AminoAcid aminoAcidType; // Reference to type definition
        @Relation(type = Relation.Type.ONE_TO_MANY)
        private final List<Atom> atoms = new ArrayList<>();

        public Residue(String name, int sequenceNumber) {
            this.name = name;
            this.sequenceNumber = sequenceNumber;
            // Try to resolve standard AminoAcid type
            AminoAcid type = null;
            try {
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


