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

package org.jscience.social.economics.models;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.List;

/**
 * Solves for Nash Equilibria in non-cooperative games.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NashEquilibriumSolver {

    private NashEquilibriumSolver() {}

    /**
     * Finds a pure strategy Nash Equilibrium in a 2-player normal-form game.
     * 
     * @param payoffMatrixA Payoffs for Player A (row player).
     * @param payoffMatrixB Payoffs for Player B (column player).
     * @return Array of indices {row, col} representing the strategy profile, or null if none found.
     */
    public static int[] findPureStrategyNE(double[][] payoffMatrixA, double[][] payoffMatrixB) {
        int rows = payoffMatrixA.length;
        int cols = payoffMatrixA[0].length;
        
        // Brute force check: A cell (r, c) is a NE if:
        // A[r][c] >= A[i][c] for all i (Best response for A)
        // B[r][c] >= B[r][j] for all j (Best response for B)
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                boolean bestForA = true;
                for (int i = 0; i < rows; i++) {
                    if (payoffMatrixA[i][c] > payoffMatrixA[r][c]) {
                        bestForA = false;
                        break;
                    }
                }
                
                if (!bestForA) continue;
                
                boolean bestForB = true;
                for (int j = 0; j < cols; j++) {
                    if (payoffMatrixB[r][j] > payoffMatrixB[r][c]) {
                        bestForB = false;
                        break;
                    }
                }
                
                if (bestForB) {
                    return new int[]{r, c};
                }
            }
        }
        
        return null;
    }
}
