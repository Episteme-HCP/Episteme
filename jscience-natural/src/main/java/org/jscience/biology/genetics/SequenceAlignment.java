package org.jscience.biology.genetics;

/**
 * Pairwise sequence alignment algorithms.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SequenceAlignment {

    /**
     * Needleman-Wunsch algorithm for global alignment.
     * 
     * @param s1       First sequence
     * @param s2       Second sequence
     * @param match    Score for a match
     * @param mismatch Penalty for a mismatch
     * @param gap      Penalty for a gap
     * @return Alignment metrics
     */
    public static AlignmentResult alignGlobal(String s1, String s2,
            int match, int mismatch, int gap) {
        int n = s1.length();
        int m = s2.length();
        int[][] score = new int[n + 1][m + 1];

        // Initialize
        for (int i = 0; i <= n; i++)
            score[i][0] = i * gap;
        for (int j = 0; j <= m; j++)
            score[0][j] = j * gap;

        // Fill matrix
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int scoreMatch = score[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch);
                int scoreDelete = score[i - 1][j] + gap;
                int scoreInsert = score[i][j - 1] + gap;
                score[i][j] = Math.max(scoreMatch, Math.max(scoreDelete, scoreInsert));
            }
        }

        // Traceback (simplified, just basic strings)
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                int scoreMatch = score[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch);
                if (score[i][j] == scoreMatch) {
                    sb1.append(s1.charAt(i - 1));
                    sb2.append(s2.charAt(j - 1));
                    i--;
                    j--;
                    continue;
                }
            }
            if (i > 0 && score[i][j] == score[i - 1][j] + gap) {
                sb1.append(s1.charAt(i - 1));
                sb2.append('-');
                i--;
            } else {
                sb1.append('-');
                sb2.append(s2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(score[n][m], sb1.reverse().toString(), sb2.reverse().toString());
    }

    /**
     * Smith-Waterman algorithm for local alignment.
     */
    public static AlignmentResult alignLocal(String s1, String s2,
            int match, int mismatch, int gap) {
        int n = s1.length();
        int m = s2.length();
        int[][] score = new int[n + 1][m + 1];
        int maxScore = 0;
        int maxI = 0, maxJ = 0;

        // Fill matrix
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int scoreMatch = score[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch);
                int scoreDelete = score[i - 1][j] + gap;
                int scoreInsert = score[i][j - 1] + gap;
                score[i][j] = Math.max(0, Math.max(scoreMatch, Math.max(scoreDelete, scoreInsert)));

                if (score[i][j] > maxScore) {
                    maxScore = score[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        // Traceback from maxI, maxJ until 0
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int i = maxI, j = maxJ;
        while (i > 0 && j > 0 && score[i][j] > 0) {
            int scoreMatch = score[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch);
            if (score[i][j] == scoreMatch) {
                sb1.append(s1.charAt(i - 1));
                sb2.append(s2.charAt(j - 1));
                i--;
                j--;
            } else if (score[i][j] == score[i - 1][j] + gap) {
                sb1.append(s1.charAt(i - 1));
                sb2.append('-');
                i--;
            } else {
                sb1.append('-');
                sb2.append(s2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(maxScore, sb1.reverse().toString(), sb2.reverse().toString());
    }

    /**
     * Hamming distance between two sequences of equal length.
     * Number of mismatches.
     */
    public static int hammingDistance(String s1, String s2) {
        if (s1.length() != s2.length()) {
            throw new IllegalArgumentException("Sequences must be of equal length");
        }
        int distance = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    /**
     * Levenshtein distance (edit distance).
     */
    public static int levenshteinDistance(String s1, String s2) {
        // Actually Levenshtein is usually min edit distance.
        // Needleman-Wunsch is maximizing score.
        // Let's implement standard Levenshtein directly.

        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++)
            dp[i][0] = i;
        for (int j = 0; j <= m; j++)
            dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(dp[i - 1][j] + 1, // deletion
                        Math.min(dp[i][j - 1] + 1, // insertion
                                dp[i - 1][j - 1] + cost)); // substitution
            }
        }
        return dp[n][m];
    }

    // Result container
    public static class AlignmentResult {
        public final int score;
        public final String aligned1;
        public final String aligned2;

        public AlignmentResult(int score, String aligned1, String aligned2) {
            this.score = score;
            this.aligned1 = aligned1;
            this.aligned2 = aligned2;
        }

        @Override
        public String toString() {
            return String.format("Score: %d\n%s\n%s", score, aligned1, aligned2);
        }
    }
}
