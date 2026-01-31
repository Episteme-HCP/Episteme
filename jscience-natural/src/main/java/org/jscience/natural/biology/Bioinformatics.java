/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.natural.biology;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Basic bioinformatics algorithms for sequence analysis.
 */
public final class Bioinformatics {

    private Bioinformatics() {}

    /**
     * Calculates the local alignment score between two sequences using the Smith-Waterman algorithm.
     * 
     * @param seq1 First sequence (e.g., DNA string).
     * @param seq2 Second sequence.
     * @param match Score for a match.
     * @param mismatch Penalty for a mismatch.
     * @param gap Penalty for a gap.
     * @return The maximum alignment score.
     */
    public static Real smithWaterman(String seq1, String seq2, Real match, Real mismatch, Real gap) {
        int n = seq1.length();
        int m = seq2.length();
        Real[][] score = new Real[n + 1][m + 1];
        Real maxScore = Real.ZERO;

        for (int i = 0; i <= n; i++) score[i][0] = Real.ZERO;
        for (int j = 0; j <= m; j++) score[0][j] = Real.ZERO;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                Real diagonal = score[i - 1][j - 1].add(seq1.charAt(i - 1) == seq2.charAt(j - 1) ? match : mismatch);
                Real up = score[i - 1][j].add(gap);
                Real left = score[i][j - 1].add(gap);
                
                Real best = diagonal.compareTo(up) > 0 ? diagonal : up;
                best = best.compareTo(left) > 0 ? best : left;
                
                score[i][j] = best.compareTo(Real.of(0.0)) > 0 ? best : Real.of(0.0);
                maxScore = maxScore.compareTo(score[i][j]) > 0 ? maxScore : score[i][j];
            }
        }
        return maxScore;
    }
}

