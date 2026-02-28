/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.natural.biology.genetics;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Pairwise Sequence Alignment using Needleman-Wunsch algorithm with high-precision scores.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SequenceAligner {

    private final Real matchScore;
    private final Real mismatchScore;
    private final Real gapScore;

    public SequenceAligner(Real matchScore, Real mismatchScore, Real gapScore) {
        this.matchScore = matchScore;
        this.mismatchScore = mismatchScore;
        this.gapScore = gapScore;
    }

    public static SequenceAligner defaultDNA() {
        return new SequenceAligner(Real.of(1.0), Real.of(-1.0), Real.of(-2.0));
    }

    public static SequenceAligner defaultProtein() {
        return new SequenceAligner(Real.of(5.0), Real.of(-3.0), Real.of(-4.0));
    }

    public AlignmentResult align(String seq1, String seq2) {
        int n = seq1.length();
        int m = seq2.length();
        Real[][] score = new Real[n + 1][m + 1];

        // Initialization
        for (int i = 0; i <= n; i++)
            score[i][0] = Real.of(i).multiply(gapScore);
        for (int j = 0; j <= m; j++)
            score[0][j] = Real.of(j).multiply(gapScore);

        // Fill matrix
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                Real match = score[i - 1][j - 1].add(seq1.charAt(i - 1) == seq2.charAt(j - 1) ? matchScore : mismatchScore);
                Real delete = score[i - 1][j].add(gapScore);
                Real insert = score[i][j - 1].add(gapScore);
                
                Real best = match.compareTo(delete) > 0 ? match : delete;
                score[i][j] = best.compareTo(insert) > 0 ? best : insert;
            }
        }

        // Traceback
        StringBuilder align1 = new StringBuilder();
        StringBuilder align2 = new StringBuilder();
        int i = n;
        int j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && score[i][j].compareTo(score[i - 1][j - 1].add(
                    seq1.charAt(i - 1) == seq2.charAt(j - 1) ? matchScore : mismatchScore)) == 0) {
                align1.append(seq1.charAt(i - 1));
                align2.append(seq2.charAt(j - 1));
                i--; j--;
            } else if (i > 0 && score[i][j].compareTo(score[i - 1][j].add(gapScore)) == 0) {
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

    /**
     * Smith-Waterman local alignment algorithm.
     * Finds the best local alignment (highest-scoring substring match).
     */
    public AlignmentResult localAlign(String seq1, String seq2) {
        int n = seq1.length();
        int m = seq2.length();
        Real[][] score = new Real[n + 1][m + 1];
        
        // Initialize with zeros (local alignment allows starting anywhere)
        for (int i = 0; i <= n; i++) score[i][0] = Real.ZERO;
        for (int j = 0; j <= m; j++) score[0][j] = Real.ZERO;

        int maxI = 0, maxJ = 0;
        Real maxScore = Real.ZERO;

        // Fill matrix
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                Real match = score[i - 1][j - 1].add(
                    seq1.charAt(i - 1) == seq2.charAt(j - 1) ? matchScore : mismatchScore);
                Real delete = score[i - 1][j].add(gapScore);
                Real insert = score[i][j - 1].add(gapScore);
                
                Real best = match.compareTo(delete) > 0 ? match : delete;
                best = best.compareTo(insert) > 0 ? best : insert;
                score[i][j] = best.compareTo(Real.ZERO) > 0 ? best : Real.ZERO;
                
                if (score[i][j].compareTo(maxScore) > 0) {
                    maxScore = score[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        // Traceback from max score position
        StringBuilder align1 = new StringBuilder();
        StringBuilder align2 = new StringBuilder();
        int i = maxI, j = maxJ;

        while (i > 0 && j > 0 && score[i][j].compareTo(Real.ZERO) > 0) {
            if (score[i][j].compareTo(score[i - 1][j - 1].add(
                    seq1.charAt(i - 1) == seq2.charAt(j - 1) ? matchScore : mismatchScore)) == 0) {
                align1.append(seq1.charAt(i - 1));
                align2.append(seq2.charAt(j - 1));
                i--; j--;
            } else if (score[i][j].compareTo(score[i - 1][j].add(gapScore)) == 0) {
                align1.append(seq1.charAt(i - 1));
                align2.append('-');
                i--;
            } else {
                align1.append('-');
                align2.append(seq2.charAt(j - 1));
                j--;
            }
        }

        return new AlignmentResult(align1.reverse().toString(), align2.reverse().toString(), maxScore);
    }

    public record AlignmentResult(String alignedSeq1, String alignedSeq2, Real score) {
        @Override
        public String toString() {
            return String.format("Score: %s\n%s\n%s", score, alignedSeq1, alignedSeq2);
        }
    }
}

