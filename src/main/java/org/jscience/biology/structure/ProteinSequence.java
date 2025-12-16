package org.jscience.biology.structure;

import org.jscience.chemistry.biochemistry.AminoAcid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Protein Sequence (Chain of Amino Acids).
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public class ProteinSequence {

    private final List<AminoAcid> sequence;

    public ProteinSequence(String oneLetterSequence) {
        this.sequence = new ArrayList<>();
        for (char c : oneLetterSequence.toUpperCase().toCharArray()) {
            AminoAcid aa = AminoAcid.fromCode(c);
            if (aa != null) {
                this.sequence.add(aa);
            } else {
                // Handling unknown/ambiguous
                throw new IllegalArgumentException("Unknown Amino Acid character: " + c);
            }
        }
    }

    public List<AminoAcid> getSequence() {
        return Collections.unmodifiableList(sequence);
    }

    public double getMolecularWeight() {
        if (sequence.isEmpty())
            return 0.0;
        return sequence.stream()
                .mapToDouble(AminoAcid::getMolarMass)
                .sum() - (sequence.size() - 1) * 18.01528; // Subtract water weight for peptide bonds
    }
}
