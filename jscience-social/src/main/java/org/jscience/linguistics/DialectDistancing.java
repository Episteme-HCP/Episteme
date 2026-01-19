package org.jscience.linguistics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Measures linguistic distance between dialects.
 */
public final class DialectDistancing {

    private DialectDistancing() {}

    /**
     * Calculates Levenshtein Distance between two words.
     */
    public static int levenshteinDistance(String s1, String s2) {
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
     * Calculates average distance between two sets of equivalent words (isoglosses).
     */
    public static Real dialectDistance(List<String> d1, List<String> d2) {
        if (d1.size() != d2.size() || d1.isEmpty()) return Real.ZERO;
        double sum = 0;
        for (int i = 0; i < d1.size(); i++) {
            sum += levenshteinDistance(d1.get(i), d2.get(i));
        }
        return Real.of(sum / d1.size());
    }
}
