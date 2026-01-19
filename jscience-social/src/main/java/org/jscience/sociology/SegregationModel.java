package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Schelling's model of spatial segregation.
 */
public final class SegregationModel {

    private SegregationModel() {}

    public enum AgentType { TYPE_A, TYPE_B, EMPTY }

    public record GridState(
        int step,
        AgentType[][] grid,
        double segregationIndex,
        double happinessRate,
        int moves
    ) {}

    /**
     * Runs Schelling segregation simulation.
     * 
     * @param size Grid size (size x size)
     * @param density Fraction of cells occupied
     * @param threshold Minimum fraction of same-type neighbors required for happiness
     * @param steps Number of simulation steps
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
            
            // Find unhappy agents
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
            
            // Stop if no moves (equilibrium)
            if (moves == 0) break;
        }
        
        return history;
    }

    /**
     * Checks if an agent is happy with their neighborhood.
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
        if (total == 0) return true;
        
        return (double) same / total >= threshold;
    }

    /**
     * Calculates segregation index (dissimilarity index).
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
        int blockSize = size / 5; // 5x5 blocks
        if (blockSize < 1) blockSize = 1;
        
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
     * Calculates happiness rate.
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
     * Visualizes grid as string.
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
