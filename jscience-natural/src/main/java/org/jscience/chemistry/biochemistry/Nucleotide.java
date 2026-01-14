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

package org.jscience.chemistry.biochemistry;

/**
 * Nucleotide bases for DNA/RNA.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public enum Nucleotide {

    // DNA bases
    ADENINE("A", "Adenine", "purine", 135.13, true, true),
    GUANINE("G", "Guanine", "purine", 151.13, true, true),
    CYTOSINE("C", "Cytosine", "pyrimidine", 111.10, true, true),
    THYMINE("T", "Thymine", "pyrimidine", 126.11, true, false),

    // RNA only
    URACIL("U", "Uracil", "pyrimidine", 112.09, false, true);

    private final String symbol;
    private final String name;
    private final String type;
    private final double baseMass; // g/mol (base only)
    private final boolean inDNA;
    private final boolean inRNA;

    Nucleotide(String symbol, String name, String type, double mass,
            boolean inDNA, boolean inRNA) {
        this.symbol = symbol;
        this.name = name;
        this.type = type;
        this.baseMass = mass;
        this.inDNA = inDNA;
        this.inRNA = inRNA;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getBaseMass() {
        return baseMass;
    }

    public boolean isInDNA() {
        return inDNA;
    }

    public boolean isInRNA() {
        return inRNA;
    }

    public boolean isPurine() {
        return type.equals("purine");
    }

    public boolean isPyrimidine() {
        return type.equals("pyrimidine");
    }

    /**
     * Returns the Watson-Crick complement.
     */
    public Nucleotide complement() {
        switch (this) {
            case ADENINE:
                return THYMINE;
            case THYMINE:
                return ADENINE;
            case GUANINE:
                return CYTOSINE;
            case CYTOSINE:
                return GUANINE;
            case URACIL:
                return ADENINE;
            default:
                return null;
        }
    }

    /**
     * Returns RNA transcript base.
     */
    public Nucleotide toRNA() {
        return this == THYMINE ? URACIL : this;
    }

    /**
     * Looks up nucleotide by symbol.
     */
    public static Nucleotide fromSymbol(char symbol) {
        String s = String.valueOf(symbol).toUpperCase();
        for (Nucleotide n : values()) {
            if (n.symbol.equals(s))
                return n;
        }
        return null;
    }

    /**
     * Calculates GC content of a DNA sequence.
     */
    public static double gcContent(String sequence) {
        int gc = 0;
        int total = 0;
        for (char c : sequence.toUpperCase().toCharArray()) {
            if (c == 'G' || c == 'C')
                gc++;
            if (c == 'A' || c == 'T' || c == 'G' || c == 'C')
                total++;
        }
        return total > 0 ? (double) gc / total : 0;
    }

    /**
     * Estimates melting temperature of short DNA (< 14 bp).
     * Tm = 2(A+T) + 4(G+C) Ã‚Â°C
     */
    public static double meltingTemperatureSimple(String sequence) {
        int at = 0, gc = 0;
        for (char c : sequence.toUpperCase().toCharArray()) {
            if (c == 'A' || c == 'T')
                at++;
            else if (c == 'G' || c == 'C')
                gc++;
        }
        return 2 * at + 4 * gc;
    }

    /**
     * Estimates melting temperature (>14 bp, salt-adjusted).
     * Tm = 81.5 + 16.6*log10([Na+]) + 0.41*(%GC) - 675/length
     */
    public static double meltingTemperature(String sequence, double saltMolar) {
        double gc = gcContent(sequence) * 100;
        int len = sequence.length();
        return 81.5 + 16.6 * Math.log10(saltMolar) + 0.41 * gc - 675.0 / len;
    }

    /**
     * Generates reverse complement of a DNA sequence.
     */
    public static String reverseComplement(String sequence) {
        StringBuilder sb = new StringBuilder();
        for (int i = sequence.length() - 1; i >= 0; i--) {
            Nucleotide n = fromSymbol(sequence.charAt(i));
            if (n != null) {
                sb.append(n.complement().getSymbol());
            }
        }
        return sb.toString();
    }

    /**
     * Transcribes DNA to RNA.
     */
    public static String transcribe(String dnaSequence) {
        StringBuilder sb = new StringBuilder();
        for (char c : dnaSequence.toCharArray()) {
            Nucleotide n = fromSymbol(c);
            if (n != null) {
                sb.append(n.toRNA().getSymbol());
            }
        }
        return sb.toString();
    }
}


