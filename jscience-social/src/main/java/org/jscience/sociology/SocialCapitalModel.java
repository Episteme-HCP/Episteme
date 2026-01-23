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

package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Utility class for modeling social capital and analyzing network properties.
 * Provides metrics for network density and trust based on reciprocity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class SocialCapitalModel {

    private SocialCapitalModel() {}

    /**
     * Calculates the Network Density.
     * Defined as the ratio of actual connections to the total number of possible connections.
     *
     * @param nodes    number of individuals (nodes)
     * @param edges    number of relationships (edges)
     * @param directed true if relationships are directed (A->B != B->A), false otherwise
     * @return the density value between 0.0 and 1.0
     */
    public static Real calculateDensity(int nodes, int edges, boolean directed) {
        if (nodes < 2) return Real.ZERO;
        double possible = directed ? (double) nodes * (nodes - 1) : (double) nodes * (nodes - 1) / 2.0;
        return Real.of(edges / possible);
    }

    /**
     * Calculates a Trust Index based on reciprocity in relationships.
     *
     * @param mutualLinks count of bidirectional (reciprocated) links
     * @param totalLinks  count of total links
     * @return the reciprocity index
     */
    public static Real reciprocityIndex(int mutualLinks, int totalLinks) {
        if (totalLinks == 0) return Real.ZERO;
        // For directed graphs, reciprocity is typically 2 * mutual / total
        return Real.of((double) 2 * mutualLinks / totalLinks);
    }
}
