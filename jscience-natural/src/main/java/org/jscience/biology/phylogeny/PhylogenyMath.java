package org.jscience.biology.phylogeny;

import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Basic phylogenetics utilities and mathematical models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class PhylogenyMath {

    /**
     * Calculates distance matrix from a list of aligned sequences.
     * Using simple Hamming distance (p-distance).
     * 
     * @param sequences List of aligned DNA strings (same length)
     * @return Symmetric distance matrix
     */
    public static Real[][] calculateDistanceMatrix(List<String> sequences) {
        int n = sequences.size();
        Real[][] matrix = new Real[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Real dist = pDistance(sequences.get(i), sequences.get(j));
                matrix[i][j] = dist;
                matrix[j][i] = dist;
            }
        }
        // Fill diagonal with ZERO
        for (int i = 0; i < n; i++) {
            matrix[i][i] = Real.ZERO;
        }
        return matrix;
    }

    /**
     * p-distance: proportion of nucleotide sites that are different.
     * d = n_d / n
     */
    public static Real pDistance(String s1, String s2) {
        if (s1.length() != s2.length())
            return Real.ZERO; // Should throw or handle align
        int diff = 0;
        int len = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != '-' && s2.charAt(i) != '-') { // Ignore gap-gap comparison?
                len++;
                if (s1.charAt(i) != s2.charAt(i)) {
                    diff++;
                }
            }
        }
        if (len == 0)
            return Real.ZERO;
        return Real.of((double) diff).divide(Real.of((double) len));
    }

    /**
     * Jukes-Cantor distance correction.
     * d_JC = -3/4 * ln(1 - 4/3 * p)
     */
    public static Real jukesCantorDistance(Real pDistance) {
        if (pDistance.compareTo(Real.of(0.75)) >= 0)
            return Real.POSITIVE_INFINITY; // Saturation

        // -0.75 * ln(1 - 1.333 * p)
        Real term = Real.ONE.subtract(Real.of(4.0 / 3.0).multiply(pDistance));
        return Real.of(-0.75).multiply(term.log());
    }
}
