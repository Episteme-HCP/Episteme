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

package org.jscience.mathematics.discrete.graph.matching;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Hungarian Algorithm (Kuhn-Munkres) for optimal bipartite matching.
 * <p>
 * Finds minimum-cost perfect matching in weighted bipartite graph.
 * Used for: assignment problems, task allocation, resource optimization.
 * O(n³) complexity.
 * </p>
 *
 * <p>
 * References:
 * <ul>
 * <li>Kuhn, H. W. (1955). The Hungarian method for the assignment problem.
 * Naval Research Logistics Quarterly, 2(1‐2), 83-97.</li>
 * <li>Munkres, J. (1957). Algorithms for the assignment and transportation
 * problems. Journal of the Society for Industrial and Applied Mathematics,
 * 5(1), 32-38.</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HungarianAlgorithm {

    /**
     * Solves assignment problem: minimize total cost.
     * 
     * @param costMatrix nÃƒâ€”n cost matrix (workers Ãƒâ€” tasks)
     * @return assignment array: assignment[worker] = task
     */
    public static int[] solve(Real[][] costMatrix) {
        int n = costMatrix.length;

        // Convert to double for efficiency
        double[][] cost = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = costMatrix[i][j].doubleValue();
            }
        }

        // Step 1: Subtract row minimums
        for (int i = 0; i < n; i++) {
            double rowMin = Double.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (cost[i][j] < rowMin)
                    rowMin = cost[i][j];
            }
            for (int j = 0; j < n; j++) {
                cost[i][j] -= rowMin;
            }
        }

        // Step 2: Subtract column minimums
        for (int j = 0; j < n; j++) {
            double colMin = Double.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (cost[i][j] < colMin)
                    colMin = cost[i][j];
            }
            for (int i = 0; i < n; i++) {
                cost[i][j] -= colMin;
            }
        }

        // Step 3: Cover zeros with minimum lines
        int[] assignment = new int[n];
        boolean[] rowCovered = new boolean[n];
        boolean[] colCovered = new boolean[n];
        int[][] starMatrix = new int[n][n]; // 1 = starred zero, 2 = primed zero

        // Initial zero coverage
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cost[i][j] == 0 && !rowCovered[i] && !colCovered[j]) {
                    starMatrix[i][j] = 1;
                    rowCovered[i] = true;
                    colCovered[j] = true;
                }
            }
        }

        // Reset covers
        for (int i = 0; i < n; i++) {
            rowCovered[i] = false;
            colCovered[i] = false;
        }

        // Cover columns with starred zeros
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (starMatrix[i][j] == 1) {
                    colCovered[j] = true;
                }
            }
        }

        int coveredCount = countCovered(colCovered);

        // Main loop
        while (coveredCount < n) {
            // Find uncovered zero
            int[] zero = findUncoveredZero(cost, rowCovered, colCovered);

            if (zero == null) {
                // No uncovered zero - adjust matrix
                double minUncovered = findMinUncovered(cost, rowCovered, colCovered);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (rowCovered[i])
                            cost[i][j] += minUncovered;
                        if (!colCovered[j])
                            cost[i][j] -= minUncovered;
                    }
                }
                continue;
            }

            int row = zero[0], col = zero[1];
            starMatrix[row][col] = 2; // Prime it

            // Find starred zero in same row
            int starCol = -1;
            for (int j = 0; j < n; j++) {
                if (starMatrix[row][j] == 1) {
                    starCol = j;
                    break;
                }
            }

            if (starCol == -1) {
                // No starred zero in row - augment path
                augmentPath(starMatrix, row, col);

                // Reset covers
                for (int i = 0; i < n; i++) {
                    rowCovered[i] = false;
                    colCovered[i] = false;
                }

                // Cover columns with starred zeros
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (starMatrix[i][j] == 1) {
                            colCovered[j] = true;
                        }
                    }
                }

                coveredCount = countCovered(colCovered);
            } else {
                // Cover row, uncover column
                rowCovered[row] = true;
                colCovered[starCol] = false;
            }
        }

        // Extract assignment
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (starMatrix[i][j] == 1) {
                    assignment[i] = j;
                    break;
                }
            }
        }

        return assignment;
    }

    private static int[] findUncoveredZero(double[][] cost, boolean[] rowCovered, boolean[] colCovered) {
        for (int i = 0; i < cost.length; i++) {
            if (rowCovered[i])
                continue;
            for (int j = 0; j < cost[i].length; j++) {
                if (!colCovered[j] && cost[i][j] == 0) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    private static double findMinUncovered(double[][] cost, boolean[] rowCovered, boolean[] colCovered) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < cost.length; i++) {
            if (rowCovered[i])
                continue;
            for (int j = 0; j < cost[i].length; j++) {
                if (!colCovered[j] && cost[i][j] < min) {
                    min = cost[i][j];
                }
            }
        }
        return min;
    }

    private static void augmentPath(int[][] starMatrix, int row, int col) {
        int n = starMatrix.length;
        java.util.List<int[]> path = new java.util.ArrayList<>();
        path.add(new int[] { row, col });

        while (true) {
            // Find starred zero in column
            int starRow = -1;
            for (int i = 0; i < n; i++) {
                if (starMatrix[i][col] == 1) {
                    starRow = i;
                    break;
                }
            }

            if (starRow == -1)
                break;

            path.add(new int[] { starRow, col });

            // Find primed zero in row
            int primeCol = -1;
            for (int j = 0; j < n; j++) {
                if (starMatrix[starRow][j] == 2) {
                    primeCol = j;
                    break;
                }
            }

            if (primeCol == -1)
                break;

            path.add(new int[] { starRow, primeCol });
            col = primeCol;
        }

        // Augment: star all primed, unstar all starred in path
        for (int[] p : path) {
            if (starMatrix[p[0]][p[1]] == 1) {
                starMatrix[p[0]][p[1]] = 0;
            } else {
                starMatrix[p[0]][p[1]] = 1;
            }
        }

        // Erase primes
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (starMatrix[i][j] == 2) {
                    starMatrix[i][j] = 0;
                }
            }
        }
    }

    private static int countCovered(boolean[] covered) {
        int count = 0;
        for (boolean b : covered) {
            if (b)
                count++;
        }
        return count;
    }

    /**
     * Computes total cost of assignment.
     */
    public static Real totalCost(Real[][] costMatrix, int[] assignment) {
        Real total = Real.ZERO;
        for (int i = 0; i < assignment.length; i++) {
            total = total.add(costMatrix[i][assignment[i]]);
        }
        return total;
    }
}
