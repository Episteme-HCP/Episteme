package org.jscience.biology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Ribonucleic acid (RNA) strand.
 *
 * @author Silvere Martin-Michiellot
 * @version 5.0
 */
public class RNA implements Serializable, Cloneable {

    private final List<Base> bases;

    public RNA(List<Base> bases) {
        if (bases.contains(Base.THYMINE)) {
            throw new IllegalArgumentException("RNA cannot contain Thymine.");
        }
        this.bases = new ArrayList<>(bases);
    }

    public RNA(Base... bases) {
        this(Arrays.asList(bases));
    }

    public RNA(String sequence) {
        this.bases = new ArrayList<>();
        for (char c : sequence.toUpperCase().toCharArray()) {
            switch (c) {
                case 'A':
                    bases.add(Base.ADENINE);
                    break;
                case 'U':
                    bases.add(Base.URACIL);
                    break;
                case 'C':
                    bases.add(Base.CYTOSINE);
                    break;
                case 'G':
                    bases.add(Base.GUANINE);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid RNA base: " + c);
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
     * Returns the complementary RNA strand.
     */
    public RNA getComplementary() {
        List<Base> compl = new ArrayList<>(bases.size());
        for (Base b : bases) {
            compl.add(b.getComplementary(true));
        }
        return new RNA(compl);
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
