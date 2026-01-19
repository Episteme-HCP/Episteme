package org.jscience.economics;

import java.util.*;

/**
 * Game theory engine for strategic analysis.
 */
public final class GameTheoryEngine {

    private GameTheoryEngine() {}

    /**
     * Represents a game in normal form.
     */
    public record NormalFormGame(
        List<String> player1Strategies,
        List<String> player2Strategies,
        double[][] player1Payoffs,
        double[][] player2Payoffs
    ) {}

    /**
     * Represents a Nash equilibrium.
     */
    public record NashEquilibrium(
        String player1Strategy,
        String player2Strategy,
        double player1Payoff,
        double player2Payoff,
        boolean isPure
    ) {}

    /**
     * Finds all pure strategy Nash equilibria.
     */
    public static List<NashEquilibrium> findPureNashEquilibria(NormalFormGame game) {
        List<NashEquilibrium> equilibria = new ArrayList<>();
        
        int m = game.player1Strategies().size();
        int n = game.player2Strategies().size();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Check if (i, j) is a Nash equilibrium
                boolean p1BestResponse = true;
                boolean p2BestResponse = true;
                
                // Check if i is best response to j
                for (int i2 = 0; i2 < m; i2++) {
                    if (game.player1Payoffs()[i2][j] > game.player1Payoffs()[i][j]) {
                        p1BestResponse = false;
                        break;
                    }
                }
                
                // Check if j is best response to i
                for (int j2 = 0; j2 < n; j2++) {
                    if (game.player2Payoffs()[i][j2] > game.player2Payoffs()[i][j]) {
                        p2BestResponse = false;
                        break;
                    }
                }
                
                if (p1BestResponse && p2BestResponse) {
                    equilibria.add(new NashEquilibrium(
                        game.player1Strategies().get(i),
                        game.player2Strategies().get(j),
                        game.player1Payoffs()[i][j],
                        game.player2Payoffs()[i][j],
                        true
                    ));
                }
            }
        }
        
        return equilibria;
    }

    /**
     * Solves a 2x2 game for mixed strategy Nash equilibrium.
     */
    public static Optional<double[]> findMixedNashEquilibrium2x2(NormalFormGame game) {
        if (game.player1Strategies().size() != 2 || game.player2Strategies().size() != 2) {
            return Optional.empty();
        }
        
        double[][] p1 = game.player1Payoffs();
        double[][] p2 = game.player2Payoffs();
        
        // Player 2 mixed strategy (probability of playing strategy 0)
        // Makes player 1 indifferent
        double num1 = p1[0][1] - p1[1][1];
        double den1 = (p1[0][0] - p1[1][0]) - (p1[0][1] - p1[1][1]);
        
        // Player 1 mixed strategy (probability of playing strategy 0)
        // Makes player 2 indifferent
        double num2 = p2[1][0] - p2[1][1];
        double den2 = (p2[0][0] - p2[0][1]) - (p2[1][0] - p2[1][1]);
        
        if (Math.abs(den1) < 1e-10 || Math.abs(den2) < 1e-10) {
            return Optional.empty();
        }
        
        double q = num1 / den1; // P2's probability of strategy 0
        double p = num2 / den2; // P1's probability of strategy 0
        
        if (p < 0 || p > 1 || q < 0 || q > 1) {
            return Optional.empty();
        }
        
        return Optional.of(new double[]{p, q});
    }

    /**
     * Minimax solution for zero-sum games.
     */
    public static double[] solveMinimax(double[][] payoffMatrix) {
        int m = payoffMatrix.length;
        int n = payoffMatrix[0].length;
        
        // Find minimax for row player
        double maxMinRow = Double.NEGATIVE_INFINITY;
        int bestRow = 0;
        
        for (int i = 0; i < m; i++) {
            double minInRow = Double.POSITIVE_INFINITY;
            for (int j = 0; j < n; j++) {
                minInRow = Math.min(minInRow, payoffMatrix[i][j]);
            }
            if (minInRow > maxMinRow) {
                maxMinRow = minInRow;
                bestRow = i;
            }
        }
        
        // Find minimax for column player
        double minMaxCol = Double.POSITIVE_INFINITY;
        int bestCol = 0;
        
        for (int j = 0; j < n; j++) {
            double maxInCol = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < m; i++) {
                maxInCol = Math.max(maxInCol, payoffMatrix[i][j]);
            }
            if (maxInCol < minMaxCol) {
                minMaxCol = maxInCol;
                bestCol = j;
            }
        }
        
        return new double[]{bestRow, bestCol, maxMinRow, minMaxCol};
    }

    /**
     * Creates the Prisoner's Dilemma game.
     */
    public static NormalFormGame prisonersDilemma() {
        return new NormalFormGame(
            List.of("Cooperate", "Defect"),
            List.of("Cooperate", "Defect"),
            new double[][]{{-1, -3}, {0, -2}},
            new double[][]{{-1, 0}, {-3, -2}}
        );
    }

    /**
     * Creates the Chicken game.
     */
    public static NormalFormGame chickenGame() {
        return new NormalFormGame(
            List.of("Swerve", "Straight"),
            List.of("Swerve", "Straight"),
            new double[][]{{0, -1}, {1, -10}},
            new double[][]{{0, 1}, {-1, -10}}
        );
    }

    /**
     * Creates Battle of the Sexes game.
     */
    public static NormalFormGame battleOfSexes() {
        return new NormalFormGame(
            List.of("Opera", "Football"),
            List.of("Opera", "Football"),
            new double[][]{{3, 0}, {0, 2}},
            new double[][]{{2, 0}, {0, 3}}
        );
    }
}
