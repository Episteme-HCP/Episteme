package org.jscience.linguistics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models the evolution and divergence of languages (Glottochronology).
 */
public final class LanguageEvolution {

    private LanguageEvolution() {}

    /**
     * Estimates time since divergence using Swadesh list retention.
     * t = log(c) / (2 * log(r))
     * 
     * @param c Fraction of shared cognates (0-1)
     * @param r Retention rate (typically 0.81-0.86 per millennium)
     * @return Time in millennia
     */
    public static Real divergenceTimeMillennia(double c, double r) {
        if (c <= 0 || r <= 0 || r >= 1) return Real.ZERO;
        return Real.of(Math.log(c) / (2 * Math.log(r)));
    }

    /**
     * Calculates shared cognates from word lists.
     */
    public static double calculateCognateFraction(List<String> list1, List<String> list2) {
        int shared = 0;
        int minSize = Math.min(list1.size(), list2.size());
        if (minSize == 0) return 0;

        for (int i = 0; i < minSize; i++) {
            if (isCognate(list1.get(i), list2.get(i))) {
                shared++;
            }
        }
        return (double) shared / minSize;
    }

    /**
     * Simplified cognate check using Levenshtein distance.
     */
    private static boolean isCognate(String word1, String word2) {
        int dist = editDistance(word1.toLowerCase(), word2.toLowerCase());
        double similarity = 1 - (double) dist / Math.max(word1.length(), word2.length());
        return similarity > 0.6; // Threshold for cognate status
    }

    private static int editDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[s1.length()][s2.length()];
    }

    /**
     * Projects future semantic drift probability.
     * S(t) = exp(-kt)
     */
    public static Real semanticRetention(double k_drift, int years) {
        return Real.of(Math.exp(-k_drift * years / 1000.0));
    }
}
