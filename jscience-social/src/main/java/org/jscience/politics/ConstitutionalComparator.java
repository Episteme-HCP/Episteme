package org.jscience.politics;


/**
 * Compares constitutional structures of different nations.
 */
public final class ConstitutionalComparator {

    private ConstitutionalComparator() {}

    public record ConstitutionInfo(String country, boolean isFederal, boolean isPresidential, int termLength) {}

    /**
     * Calculates structural similarity between two constitutions.
     */
    public static double structuralSimilarity(ConstitutionInfo c1, ConstitutionInfo c2) {
        double score = 0;
        if (c1.isFederal() == c2.isFederal()) score += 0.33;
        if (c1.isPresidential() == c2.isPresidential()) score += 0.33;
        if (c1.termLength() == c2.termLength()) score += 0.34;
        return score;
    }
}
