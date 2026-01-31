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

package org.jscience.social.economics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Specialized utility for solving matrix-form games and identifying strategic equilibria.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class GameTheorySolver {

    private GameTheorySolver() {}

    /** Data structure for 2-player payoff entries. */
    public record Payoff(Real playerA, Real playerB) implements Serializable {}

    /**
     * Finds index pairs (row, col) representing pure Nash Equilibria.
     */
    public static List<int[]> findPureNashEquilibria(Payoff[][] matrix) {
        if (matrix == null || matrix.length == 0) return List.of();
        List<int[]> equilibria = new ArrayList<>();
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isBestResponseA(matrix, i, j) && isBestResponseB(matrix, i, j)) {
                    equilibria.add(new int[]{i, j});
                }
            }
        }
        return equilibria;
    }

    private static boolean isBestResponseA(Payoff[][] matrix, int row, int col) {
        Real current = matrix[row][col].playerA();
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][col].playerA().compareTo(current) > 0) return false;
        }
        return true;
    }

    private static boolean isBestResponseB(Payoff[][] matrix, int row, int col) {
        Real current = matrix[row][col].playerB();
        for (int j = 0; j < matrix[0].length; j++) {
            if (matrix[row][j].playerB().compareTo(current) > 0) return false;
        }
        return true;
    }

    /** 
     * Calculates the Tragedy of the Commons payoff for a given number of users.
     */
    public static org.jscience.social.economics.money.Money calculateCommonsPayoff(int users, org.jscience.social.economics.money.Money resourceValue, org.jscience.social.economics.money.Money baseCost) {
        return resourceValue.subtract(baseCost.multiply(Real.of(users)));
    }
}

