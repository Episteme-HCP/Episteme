package org.jscience.biology.genetics;

/**
 * Pairwise Sequence Alignment using Needleman-Wunsch algorithm.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SequenceAligner {

    private final int matchScore;
    private final int mismatchScore;
    private final int gapScore;

    public SequenceAligner(int matchScore, int mismatchScore, int gapScore) {
        this.matchScore = matchScore;
        this.mismatchScore = mismatchScore;
        this.gapScore = gapScore;
    }

    public static SequenceAligner defaultDNA() {
        return new SequenceAligner(1, -1, -2);
    }

    public static SequenceAligner defaultProtein() {
        return new SequenceAligner(5, -3, -4); // BLOSUM62 approximation
    }

    public AlignmentResult align(String seq1, String seq2) {
        int n = seq1.length();
        int m = seq2.length();
        int[][] score = new int[n + 1][m + 1];

        // Initialization
        for (int i = 0; i <= n; i++)
            score[i][0] = i * gapScore;
        for (int j = 0; j <= m; j++)
            score[0][j] = j * gapScore;

        // Fill matrix
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int match = score[i - 1][j - 1]
                        + (seq1.charAt(i - 1) == seq2.charAt(j - 1) ? matchScore : mismatchScore);
                int delete = score[i - 1][j] + gapScore;
                int insert = score[i][j - 1] + gapScore;
                score[i][j] = Math.max(match, Math.max(delete, insert));
            }
        }

        // Traceback
        StringBuilder align1 = new StringBuilder();
        StringBuilder align2 = new StringBuilder();
        int i = n;
        int j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && score[i][j] == score[i - 1][j - 1]
                    + (seq1.charAt(i - 1) == seq2.charAt(j - 1) ? matchScore : mismatchScore)) {
                align1.append(seq1.charAt(i - 1));
                align2.append(seq2.charAt(j - 1));
                i--;
                j--;
            } else if (i > 0 && score[i][j] == score[i - 1][j] + gapScore) {
                align1.append(seq1.charAt(i - 1));
                align2.append('-');
                i--;
            } else {
                align1.append('-');
                align2.append(seq2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(align1.reverse().toString(), align2.reverse().toString(), score[n][m]);
    }

    public static class AlignmentResult {
        public final String alignedSeq1;
        public final String alignedSeq2;
        public final int score;

        public AlignmentResult(String s1, String s2, int score) {
            this.alignedSeq1 = s1;
            this.alignedSeq2 = s2;
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format("Score: %d\n%s\n%s", score, alignedSeq1, alignedSeq2);
        }
    }
}
