/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.biology.structure;

import org.jscience.chemistry.biochemistry.AminoAcid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Protein Sequence (Chain of Amino Acids).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
