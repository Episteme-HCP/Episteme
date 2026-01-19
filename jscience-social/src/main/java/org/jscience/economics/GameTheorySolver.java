package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Solves for Nash Equilibrium and other game theory concepts.
 */
public final class GameTheorySolver {

    private GameTheorySolver() {}

    public record Payoff(double playerA, double playerB) {}

    /**
     * Finds Pure Strategy Nash Equilibria in a 2x2 matrix game.
     */
    public static List<int[]> findPureNashEquilibria(Payoff[][] matrix) {
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
        double currentPayoff = matrix[row][col].playerA();
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][col].playerA() > currentPayoff) return false;
        }
        return true;
    }

    private static boolean isBestResponseB(Payoff[][] matrix, int row, int col) {
        double currentPayoff = matrix[row][col].playerB();
        for (int j = 0; j < matrix[0].length; j++) {
            if (matrix[row][j].playerB() > currentPayoff) return false;
        }
        return true;
    }

    /**
     * Calculates Mixed Strategy Nash Equilibrium for a 2x2 zero-sum game.
     * Returns probabilities for Player A.
     */
    public static Real[] solveMixed2x2(double[][] payoffsA) {
        // Solving for p such that p*a + (1-p)*c = p*b + (1-p)*d
        double a = payoffsA[0][0];
        double b = payoffsA[0][1];
        double c = payoffsA[1][0];
        double d = payoffsA[1][1];

        double p = (d - c) / (a - b - c + d);
        return new Real[]{Real.of(p), Real.of(1 - p)};
    }

    /**
     * Models the Tragedy of the Commons.
     * Utility = (Value * Users) / Users - Cost(Users)
     */
    public static Real calculateCommonsPayoff(int users, double resourceValue, double baseCost) {
        // Simplified: value increases with users but cost increases quadratically
        double payoff = resourceValue - baseCost * users;
        return Real.of(payoff);
    }

    /**
     * Classic Prisoner's Dilemma matrix.
     */
    public static Payoff[][] prisonersDilemma() {
        return new Payoff[][] {
            { new Payoff(-1, -1), new Payoff(-3, 0) }, // [Cooperate, Cooperate], [Cooperate, Defect]
            { new Payoff(0, -3),  new Payoff(-2, -2) }  // [Defect, Cooperate], [Defect, Defect]
        };
    }
}
