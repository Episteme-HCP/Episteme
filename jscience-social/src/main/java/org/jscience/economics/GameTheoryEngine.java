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

package org.jscience.economics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Computes strategic outcomes and equilibria for game theoretic models.
 * Supports finding Pure Nash Equilibria and solving 2x2 mixed strategy games.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class GameTheoryEngine {

    private GameTheoryEngine() {}

    /** Data model for a 2-player game in normal form. */
    public record NormalFormGame(
        List<String> player1Strategies,
        List<String> player2Strategies,
        double[][] player1Payoffs,
        double[][] player2Payoffs
    ) implements Serializable {}

    /** Details of a strategic equilibrium point. */
    public record NashEquilibrium(
        String player1Strategy,
        String player2Strategy,
        double player1Payoff,
        double player2Payoff,
        boolean isPure
    ) implements Serializable {}

    /**
     * Identifies all pure strategy Nash equilibria in a normal form game.
     * 
     * @param game the game to analyze
     * @return a list of pure equilibria
     */
    public static List<NashEquilibrium> findPureNashEquilibria(NormalFormGame game) {
        Objects.requireNonNull(game, "Game cannot be null");
        List<NashEquilibrium> equilibria = new ArrayList<>();
        
        int m = game.player1Strategies().size();
        int n = game.player2Strategies().size();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                boolean p1BestResponse = true;
                for (int i2 = 0; i2 < m; i2++) {
                    if (game.player1Payoffs()[i2][j] > game.player1Payoffs()[i][j]) {
                        p1BestResponse = false;
                        break;
                    }
                }
                
                boolean p2BestResponse = true;
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
     * Calculates the mixed strategy probabilities for a symmetric 2x2 game.
     * 
     * @return Optional array [p, q] where p is P1's probability for first strategy, q is P2's.
     */
    public static Optional<double[]> findMixedNashEquilibrium2x2(NormalFormGame game) {
        if (game == null || game.player1Strategies().size() != 2 || game.player2Strategies().size() != 2) {
            return Optional.empty();
        }
        
        double[][] p1 = game.player1Payoffs();
        double[][] p2 = game.player2Payoffs();
        
        double num1 = p1[0][1] - p1[1][1];
        double den1 = (p1[0][0] - p1[1][0]) - (p1[0][1] - p1[1][1]);
        
        double num2 = p2[1][0] - p2[1][1];
        double den2 = (p2[0][0] - p2[0][1]) - (p2[1][0] - p2[1][1]);
        
        if (Math.abs(den1) < 1e-10 || Math.abs(den2) < 1e-10) return Optional.empty();
        
        double q = num1 / den1;
        double p = num2 / den2;
        
        if (p < 0 || p > 1 || q < 0 || q > 1) return Optional.empty();
        
        return Optional.of(new double[]{p, q});
    }

    /** Factory for the classic Prisoner's Dilemma. */
    public static NormalFormGame prisonersDilemma() {
        return new NormalFormGame(
            List.of("Cooperate", "Defect"),
            List.of("Cooperate", "Defect"),
            new double[][]{{-1, -3}, {0, -2}},
            new double[][]{{-1, 0}, {-3, -2}}
        );
    }
}
