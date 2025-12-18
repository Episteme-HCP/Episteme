package org.jscience.biology.evolution;

import java.util.List;

/**
 * Basic phylogenetics utilities.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Phylogenetics {

    /**
     * Calculates distance matrix from a list of aligned sequences.
     * Using simple Hamming distance (p-distance).
     * 
     * @param sequences List of aligned DNA strings (same length)
     * @return Symmetric distance matrix
     */
    public static double[][] calculateDistanceMatrix(List<String> sequences) {
        int n = sequences.size();
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = pDistance(sequences.get(i), sequences.get(j));
                matrix[i][j] = dist;
                matrix[j][i] = dist;
            }
        }
        return matrix;
    }

    /**
     * p-distance: proportion of nucleotide sites that are different.
     * d = n_d / n
     */
    public static double pDistance(String s1, String s2) {
        if (s1.length() != s2.length())
            return 0; // Should throw or handle align
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
        return len == 0 ? 0 : (double) diff / len;
    }

    /**
     * Jukes-Cantor distance correction.
     * d_JC = -3/4 * ln(1 - 4/3 * p)
     */
    public static double jukesCantorDistance(double pDistance) {
        if (pDistance >= 0.75)
            return Double.POSITIVE_INFINITY; // Saturation
        return -0.75 * Math.log(1 - (4.0 / 3.0) * pDistance);
    }
}
