package org.jscience.biology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Deoxyribonucleic acid (DNA) strand.
 *
 * @author Silvere Martin-Michiellot
 * @version 5.0
 */
public class DNA implements Serializable, Cloneable {

    private final List<Base> bases;

    public DNA(List<Base> bases) {
        if (bases.contains(Base.URACIL)) {
            throw new IllegalArgumentException("DNA cannot contain Uracil.");
        }
        this.bases = new ArrayList<>(bases);
    }

    public DNA(Base... bases) {
        this(Arrays.asList(bases));
    }

    /**
     * Parses a DNA string (e.g. "ATCG").
     * 
     * @param sequence
     */
    public DNA(String sequence) {
        this.bases = new ArrayList<>();
        for (char c : sequence.toUpperCase().toCharArray()) {
            switch (c) {
                case 'A':
                    bases.add(Base.ADENINE);
                    break;
                case 'T':
                    bases.add(Base.THYMINE);
                    break;
                case 'C':
                    bases.add(Base.CYTOSINE);
                    break;
                case 'G':
                    bases.add(Base.GUANINE);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid DNA base: " + c);
            }
        }
    }

    public List<Base> getBases() {
        return Collections.unmodifiableList(bases);
    }

    public int getLength() {
        return bases.size();
    }

    /**
     * Returns the complementary DNA strand.
     */
    public DNA getComplementary() {
        List<Base> compl = new ArrayList<>(bases.size());
        for (Base b : bases) {
            compl.add(b.getComplementary(false));
        }
        return new DNA(compl);
    }

    /**
     * Transcribes this DNA to mRNA.
     * (Complementary, but T -> U).
     */
    public RNA transcribe() {
        List<Base> rnaBases = new ArrayList<>(bases.size());
        for (Base b : bases) {
            rnaBases.add(b.getComplementary(true));
        }
        return new RNA(rnaBases);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Base b : bases) {
            sb.append(b.name().charAt(0));
        }
        return sb.toString();
    }
}
