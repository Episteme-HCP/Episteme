package org.jscience.linguistics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Phonetic and textual distance metrics for linguistic analysis.
 */
public final class LinguisticMetrics {

    private LinguisticMetrics() {}

    /**
     * Calculates the Levenshtein distance between two strings.
     * Often used in philology to measure language divergence.
     * 
     * @param s1 First string.
     * @param s2 Second string.
     * @return The edit distance as a Real number.
     */
    public static Real levenshteinDistance(String s1, String s2) {
        if (s1 == null || s2 == null) return Real.of(Double.NaN);
        
        int n = s1.length();
        int m = s2.length();
        int[][] d = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) d[i][0] = i;
        for (int j = 0; j <= m; j++) d[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + cost);
            }
        }
        return Real.of(d[n][m]);
    }
}
