package org.jscience.biology.structure;

import java.util.Stack;

/**
 * RNA Secondary Structure Prediction using Nussinov Algorithm.
 * Maximizes base pairs.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class RNAFolding {

    public static String fold(String rna) {
        int n = rna.length();
        int[][] dp = new int[n][n];

        // Fill DP table
        // l is length of subsequence
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i < n - l + 1; i++) {
                int j = i + l - 1;
                int jPair = -1;

                // Case 1: i and j pair?
                if (canPair(rna.charAt(i), rna.charAt(j))) {
                    jPair = 1 + (i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0);
                }

                // Case 2: i unpaired
                int unpaired = dp[i + 1][j];

                // Case 3: j unpaired
                int jUnpaired = dp[i][j - 1]; // Usually covered by splitting?

                // Case 4: Bifurcation
                int bifurcation = 0;
                for (int k = i + 1; k < j; k++) {
                    bifurcation = Math.max(bifurcation, dp[i][k] + dp[k + 1][j]);
                }

                dp[i][j] = Math.max(Math.max(jPair, unpaired), Math.max(jUnpaired, bifurcation));
            }
        }

        // Traceback to find structure
        return traceback(dp, rna, 0, n - 1);
    }

    private static String traceback(int[][] dp, String rna, int i, int j) {
        if (i > j)
            return "";
        if (i == j)
            return ".";

        if (dp[i][j] == dp[i + 1][j]) {
            return "." + traceback(dp, rna, i + 1, j);
        } else if (dp[i][j] == dp[i][j - 1]) {
            return traceback(dp, rna, i, j - 1) + ".";
        } else if (canPair(rna.charAt(i), rna.charAt(j)) && dp[i][j] == 1 + (i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0)) {
            return "(" + traceback(dp, rna, i + 1, j - 1) + ")";
        } else {
            for (int k = i + 1; k < j; k++) {
                if (dp[i][j] == dp[i][k] + dp[k + 1][j]) {
                    return traceback(dp, rna, i, k) + traceback(dp, rna, k + 1, j);
                }
            }
        }
        return "."; // Fallback? should not reach here if logic correct
    }

    private static boolean canPair(char a, char b) {
        /*
         * Watson-Crick pairs: A-U, G-C
         * Wobble pair: G-U
         */
        String pair = "" + a + b;
        return pair.equals("AU") || pair.equals("UA") ||
                pair.equals("GC") || pair.equals("CG") ||
                pair.equals("GU") || pair.equals("UG");
    }
}
