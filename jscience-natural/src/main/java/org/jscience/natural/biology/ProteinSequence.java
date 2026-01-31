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

import org.jscience.natural.chemistry.biochemistry.AminoAcid;
import org.jscience.core.mathematics.numbers.real.Real;

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

    /**
     * Returns the molecular weight in g/mol.
     * Subtracts water for peptide bond formation.
     */
    public Real getMolecularWeight() {
        if (sequence.isEmpty())
            return Real.ZERO;
        
        Real total = Real.ZERO;
        for (AminoAcid aa : sequence) {
            total = total.add(aa.getMolarMass());
        }
        
        // Subtract (n-1) * 18.01528 for water loss in peptide bonds
        int bonds = Math.max(0, sequence.size() - 1);
        total = total.subtract(Real.of(18.01528).multiply(Real.of(bonds)));
        
        return total;
    }
}


