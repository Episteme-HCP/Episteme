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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.jscience.mathematics.numbers.real.Real;

/**
 * An implementation of Thomas Schelling's model of spatial segregation.
 * demonstrating how individual preferences for similar neighbors (homophily) can lead
 * to macro-level segregation, even with mild preferences.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class SegregationModel {

    private SegregationModel() {}

    /** Types of agents or occupancy status in the grid. */
    public enum AgentType { TYPE_A, TYPE_B, EMPTY }

    /** Represents a snapshot of the grid at a specific simulation step. */
    public record GridState(
        int step,
        AgentType[][] grid,
        double segregationIndex,
        double happinessRate,
        int moves
    ) implements Serializable {}

    /**
     * Runs the Schelling segregation simulation.
     * 
     * @param size      Dimensions of the grid (size x size)
     * @param density   Fraction of cells initially occupied (0.0 to 1.0)
     * @param threshold Minimum fraction of similar neighbors required for an agent to be "happy"
     * @param steps     Maximum number of simulation steps
     * @return List of GridState objects representing the simulation history
     */
    public static List<GridState> simulate(int size, double density, double threshold, int steps) {
        List<GridState> history = new ArrayList<>();
        Random random = new Random(42);
        
        // Initialize grid
        AgentType[][] grid = new AgentType[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (random.nextDouble() < density) {
                    grid[i][j] = random.nextBoolean() ? AgentType.TYPE_A : AgentType.TYPE_B;
                } else {
                    grid[i][j] = AgentType.EMPTY;
                }
            }
        }
        
        history.add(createState(0, grid, 0));
        
        for (int step = 1; step <= steps; step++) {
            int moves = 0;
            
            // Identify unhappy agents and empty spots
            List<int[]> unhappy = new ArrayList<>();
            List<int[]> empty = new ArrayList<>();
            
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (grid[i][j] == AgentType.EMPTY) {
                        empty.add(new int[]{i, j});
                    } else if (!isHappy(grid, i, j, threshold)) {
                        unhappy.add(new int[]{i, j});
                    }
                }
            }
            
            // Move unhappy agents to random empty cells
            Collections.shuffle(unhappy, random);
            Collections.shuffle(empty, random);
            
            int moveCount = Math.min(unhappy.size(), empty.size());
            for (int m = 0; m < moveCount; m++) {
                int[] from = unhappy.get(m);
                int[] to = empty.get(m);
                
                grid[to[0]][to[1]] = grid[from[0]][from[1]];
                grid[from[0]][from[1]] = AgentType.EMPTY;
                moves++;
            }
            
            history.add(createState(step, grid, moves));
            
            // Stop if no agents moved (equilibrium reached)
            if (moves == 0) break;
        }
        
        return history;
    }

    /**
     * Checks if an agent at (row, col) is satisfied with their neighborhood.
     */
    private static boolean isHappy(AgentType[][] grid, int row, int col, double threshold) {
        AgentType type = grid[row][col];
        if (type == AgentType.EMPTY) return true;
        
        int same = 0, different = 0;
        int size = grid.length;
        
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) continue;
                
                int ni = row + di;
                int nj = col + dj;
                
                if (ni >= 0 && ni < size && nj >= 0 && nj < size) {
                    AgentType neighbor = grid[ni][nj];
                    if (neighbor != AgentType.EMPTY) {
                        if (neighbor == type) same++;
                        else different++;
                    }
                }
            }
        }
        
        int total = same + different;
        if (total == 0) return true; // Happy if no neighbors
        
        return (double) same / total >= threshold;
    }

    /**
     * Calculates the Segregation (Dissimilarity) Index.
     * Uses a simplified block-level dissimilarity measure.
     * 
     * @param grid the simulation grid
     * @return the segregation index score
     */
    public static Real calculateSegregationIndex(AgentType[][] grid) {
        int size = grid.length;
        int totalA = 0, totalB = 0;
        
        // Count totals
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == AgentType.TYPE_A) totalA++;
                if (grid[i][j] == AgentType.TYPE_B) totalB++;
            }
        }
        
        if (totalA == 0 || totalB == 0) return Real.ZERO;
        
        // Calculate neighborhood-level dissimilarity
        double sum = 0;
        int blockSize = Math.max(1, size / 5); // Use 5x5 blocks or smaller
        
        for (int bi = 0; bi < size; bi += blockSize) {
            for (int bj = 0; bj < size; bj += blockSize) {
                int blockA = 0, blockB = 0;
                
                for (int i = bi; i < Math.min(bi + blockSize, size); i++) {
                    for (int j = bj; j < Math.min(bj + blockSize, size); j++) {
                        if (grid[i][j] == AgentType.TYPE_A) blockA++;
                        if (grid[i][j] == AgentType.TYPE_B) blockB++;
                    }
                }
                
                sum += Math.abs((double) blockA / totalA - (double) blockB / totalB);
            }
        }
        
        return Real.of(sum / 2);
    }

    /**
     * Calculates the percentage of agents who are currently "happy".
     * 
     * @param grid      the simulation grid
     * @param threshold the happiness threshold used
     * @return happiness rate (0.0 to 1.0)
     */
    public static Real calculateHappiness(AgentType[][] grid, double threshold) {
        int happy = 0, total = 0;
        int size = grid.length;
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != AgentType.EMPTY) {
                    total++;
                    if (isHappy(grid, i, j, threshold)) happy++;
                }
            }
        }
        
        return total > 0 ? Real.of((double) happy / total) : Real.ONE;
    }

    /**
     * Generates a string visualization of the grid.
     * @param grid the grid to visualize
     * @return string representation
     */
    public static String visualize(AgentType[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (AgentType[] row : grid) {
            for (AgentType cell : row) {
                sb.append(switch (cell) {
                    case TYPE_A -> "A";
                    case TYPE_B -> "B";
                    case EMPTY -> ".";
                });
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static GridState createState(int step, AgentType[][] grid, int moves) {
        AgentType[][] copy = new AgentType[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        
        double segIndex = calculateSegregationIndex(grid).doubleValue();
        double happiness = calculateHappiness(grid, 0.3).doubleValue();
        
        return new GridState(step, copy, segIndex, happiness, moves);
    }
}
