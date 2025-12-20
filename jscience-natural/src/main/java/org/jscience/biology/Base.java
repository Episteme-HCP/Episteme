package org.jscience.biology;

/**
 * Represents the nucleobases found in DNA and RNA.
 *
 * @author Silvere Martin-Michiellot
 * @version 5.0
 */
public enum Base {
    ADENINE,
    CYTOSINE,
    GUANINE,
    THYMINE,
    URACIL;

    /**
     * Returns the complementary base.
     * DNA: A-T, C-G
     * RNA: A-U, C-G
     * 
     * @param isRNA if true, returns complement for RNA (A -> U), else for DNA (A ->
     *              T).
     * @return the complementary base.
     */
    public Base getComplementary(boolean isRNA) {
        switch (this) {
            case ADENINE:
                return isRNA ? URACIL : THYMINE;
            case CYTOSINE:
                return GUANINE;
            case GUANINE:
                return CYTOSINE;
            case THYMINE:
                return ADENINE; // Only in DNA
            case URACIL:
                return ADENINE; // Only in RNA
            default:
                return null;
        }
    }
}
