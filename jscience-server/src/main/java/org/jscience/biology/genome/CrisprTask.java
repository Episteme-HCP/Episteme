/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.biology.genome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.discrete.TextAlgorithms;

/**
 * CRISPR-Cas9 Target Scanning Task.
 * 
 * Scans a genomic sequence for PAM sites (NGG) and evaluates guide efficiency.
 * Uses bit-parallel Shift-Or for high-speed motif matching.
 * 
 * <p>
 * References:
 * <ul>
 * <li>Hsu, P. D., et al. (2013). DNA targeting specificity of RNA-guided Cas9
 * nucleases. Nature Biotechnology, 31(9), 827-832.</li>
 * </ul>
 * </p>
 * 
 * @javadoc Complexity: O(N * M / W) where N=Genome Length, M=Pattern Length,
 *          W=Word Size. Uses Shift-Or (Bit-Parallel).
 */
public class CrisprTask implements Serializable {

    private final String sequence;
    private final String targetMotif = "GG"; // PAM for Cas9 is NGG

    public record Target(int position, String spacer, String pam, double score) implements Serializable {
    }

    public CrisprTask(String sequence) {
        this.sequence = sequence.toUpperCase().replaceAll("[^ATCG]", "");
    }

    public List<Target> scan() {
        List<Target> targets = new ArrayList<>();
        // Find all "GG" positions using bit-parallel Shift-Or
        List<Integer> ggPositions = TextAlgorithms.shiftOrSearch(sequence, targetMotif);

        for (int pos : ggPositions) {
            // PAM is NGG, so pos is the index of the first G.
            // We need 21 bp before the GG (20bp spacer + 1bp for N in NGG)
            if (pos >= 21) {
                int start = pos - 21;
                String fullMatch = sequence.substring(start, pos + 2); // 23 bp
                String spacer = fullMatch.substring(0, 20);
                String pam = fullMatch.substring(20);

                double score = evaluateEfficiency(spacer);
                targets.add(new Target(start + 1, spacer, pam, score));
            }
        }
        return targets;
    }

    private double evaluateEfficiency(String spacer) {
        // Advanced Scoring: GC content (40-60% ideal) + Position-dependent weights
        double gc = 0;
        for (char c : spacer.toCharArray())
            if (c == 'G' || c == 'C')
                gc++;
        double gcPercent = gc / 20.0;

        // Penalize deviations from 50% GC
        double score = 100.0 * (1.0 - Math.abs(0.5 - gcPercent) * 2.0);

        // Favor G at position 20 (base before PAM)
        if (spacer.charAt(19) == 'G')
            score += 10;

        return Math.max(0, Math.min(100, score));
    }
}
