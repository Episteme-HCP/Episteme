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

package org.jscience.biology.loaders;

/**
 * Data transfer object for biological sequence information from GenBank.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SequenceInfo {

    private final String accession;
    private final String description;
    private final String sequence;
    private final SequenceType type;

    public enum SequenceType {
        DNA, RNA, PROTEIN
    }

    private SequenceInfo(String accession, String description, String sequence, SequenceType type) {
        this.accession = accession;
        this.description = description;
        this.sequence = sequence;
        this.type = type;
    }

    public String getAccession() {
        return accession;
    }

    public String getDescription() {
        return description;
    }

    public String getSequence() {
        return sequence;
    }

    public SequenceType getType() {
        return type;
    }

    public int getLength() {
        return sequence.length();
    }

    /**
     * Calculates GC content (for DNA/RNA sequences).
     * 
     * @return GC content as fraction (0.0-1.0)
     */
    public double getGCContent() {
        if (type == SequenceType.PROTEIN)
            return Double.NaN;

        long gc = sequence.chars()
                .filter(c -> c == 'G' || c == 'C' || c == 'g' || c == 'c')
                .count();
        return (double) gc / sequence.length();
    }

    /**
     * Returns complementary strand (for DNA only).
     */
    public String getComplement() {
        if (type != SequenceType.DNA)
            return null;

        StringBuilder sb = new StringBuilder(sequence.length());
        for (char c : sequence.toCharArray()) {
            sb.append(switch (c) {
                case 'A', 'a' -> 'T';
                case 'T', 't' -> 'A';
                case 'G', 'g' -> 'C';
                case 'C', 'c' -> 'G';
                default -> c;
            });
        }
        return sb.toString();
    }

    /**
     * Returns reverse complement (for DNA only).
     */
    public String getReverseComplement() {
        String comp = getComplement();
        return comp == null ? null : new StringBuilder(comp).reverse().toString();
    }

    /**
     * Parses FASTA format string into SequenceInfo.
     */
    public static SequenceInfo fromFasta(String fasta) {
        String[] lines = fasta.split("\n");
        if (lines.length == 0 || !lines[0].startsWith(">")) {
            throw new IllegalArgumentException("Invalid FASTA format");
        }

        String header = lines[0].substring(1).trim();
        String[] headerParts = header.split("\\s+", 2);
        String accession = headerParts[0];
        String description = headerParts.length > 1 ? headerParts[1] : "";

        StringBuilder seqBuilder = new StringBuilder();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (!line.isEmpty() && !line.startsWith(">")) {
                seqBuilder.append(line);
            }
        }
        String sequence = seqBuilder.toString();

        // Detect sequence type
        SequenceType type = detectType(sequence);

        return new SequenceInfo(accession, description, sequence, type);
    }

    private static SequenceType detectType(String sequence) {
        String upper = sequence.toUpperCase();

        // Check for protein-specific amino acids
        if (upper.matches(".*[EFIJLOPQZX].*")) {
            return SequenceType.PROTEIN;
        }
        // Check for RNA (contains U but no T)
        if (upper.contains("U") && !upper.contains("T")) {
            return SequenceType.RNA;
        }
        // Default to DNA
        return SequenceType.DNA;
    }

    @Override
    public String toString() {
        return String.format("SequenceInfo{accession='%s', type=%s, length=%d, GC=%.1f%%}",
                accession, type, getLength(), getGCContent() * 100);
    }
}


