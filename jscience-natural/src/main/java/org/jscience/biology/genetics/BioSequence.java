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

package org.jscience.biology.genetics;

import org.jscience.chemistry.biochemistry.Nucleotide;

/**
 * Biological sequence (DNA, RNA, or Protein).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BioSequence {

    public enum Type {
        DNA, RNA, PROTEIN
    }

    private final String sequence;
    private final Type type;

    public BioSequence(String sequence, Type type) {
        this.sequence = sequence.toUpperCase();
        this.type = type;
        validate();
    }

    private void validate() {
        // Basic validation
        if (type == Type.DNA) {
            for (char c : sequence.toCharArray()) {
                if ("ACGTN".indexOf(c) == -1)
                    throw new IllegalArgumentException("Invalid DNA character: " + c);
            }
        } else if (type == Type.RNA) {
            for (char c : sequence.toCharArray()) {
                if ("ACGUN".indexOf(c) == -1)
                    throw new IllegalArgumentException("Invalid RNA character: " + c);
            }
        }
    }

    /**
     * Returns the length of the sequence.
     */
    public int length() {
        return sequence.length();
    }

    /**
     * Returns the sequence string.
     */
    public String getSequence() {
        return sequence;
    }

    public Type getType() {
        return type;
    }

    /**
     * Returns the complement sequence (DNA only).
     * 5'-ATGC-3' -> 5'-TACG-3' (Note: usually reverse complement is more useful)
     */
    public BioSequence complement() {
        if (type != Type.DNA)
            throw new UnsupportedOperationException("Complement only supported for DNA");

        StringBuilder sb = new StringBuilder();
        for (char c : sequence.toCharArray()) {
            sb.append(Nucleotide.fromSymbol(c).complement().getSymbol());
        }
        return new BioSequence(sb.toString(), Type.DNA);
    }

    /**
     * Returns the reverse complement sequence.
     * 5'-ATGC-3' -> 5'-GCAT-3'
     */
    public BioSequence reverseComplement() {
        if (type != Type.DNA)
            throw new UnsupportedOperationException("Reverse complement only supported for DNA");
        return new BioSequence(Nucleotide.reverseComplement(sequence), Type.DNA);
    }

    /**
     * Transcribes DNA to RNA.
     * T -> U
     */
    public BioSequence transcribe() {
        if (type != Type.DNA)
            throw new UnsupportedOperationException("Only DNA can be transcribed");
        return new BioSequence(sequence.replace('T', 'U'), Type.RNA);
    }

    /**
     * Reverse transcribes RNA to DNA.
     * U -> T
     */
    public BioSequence reverseTranscribe() {
        if (type != Type.RNA)
            throw new UnsupportedOperationException("Only RNA can be reverse transcribed");
        return new BioSequence(sequence.replace('U', 'T'), Type.DNA);
    }

    /**
     * Translates RNA/DNA to Protein.
     * Starts from reading frame 0.
     */
    public BioSequence translate() {
        if (type == Type.PROTEIN)
            return this;

        String seq = (type == Type.DNA) ? sequence.replace('T', 'U') : sequence;
        StringBuilder protein = new StringBuilder();
        CodonTable table = CodonTable.STANDARD;

        for (int i = 0; i < seq.length() - 2; i += 3) {
            String codon = seq.substring(i, i + 3);
            protein.append(table.translateToChar(codon));
        }

        return new BioSequence(protein.toString(), Type.PROTEIN);
    }

    /**
     * Calculates GC content.
     */
    public double gcContent() {
        if (type == Type.PROTEIN)
            return 0;
        return Nucleotide.gcContent(sequence);
    }

    @Override
    public String toString() {
        return type + ": " + sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BioSequence))
            return false;
        BioSequence that = (BioSequence) o;
        return sequence.equals(that.sequence) && type == that.type;
    }

    @Override
    public int hashCode() {
        return sequence.hashCode() + type.hashCode();
    }
}
