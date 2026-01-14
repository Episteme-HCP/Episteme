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

package org.jscience.computing.game.checkers;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.TemplatePlayer;

import java.util.Random;


/**
 * AI for the CheckersGame
 *
 * @author Holger Antelmann
 */
public class CheckersPlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -920589714492673296L;

    /** DOCUMENT ME! */
    Random random;

/**
     * Creates a new CheckersPlayer object.
     */
    public CheckersPlayer() {
        this("default CheckersPlayer");
    }

/**
     * Creates a new CheckersPlayer object.
     *
     * @param name DOCUMENT ME!
     */
    public CheckersPlayer(String name) {
        super(name, SEARCH_ALPHABETA, false);
    }

    /**
     * if seed = 0, randomization is disabled
     *
     * @param seed DOCUMENT ME!
     */
    public void setRandomSeed(long seed) {
        if (random != null) {
            random.setSeed(seed);

            if (seed == 0) {
                random = null;
            }
        } else if (seed != 0) {
            random = new Random(seed);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        return (game instanceof CheckersGame);
    }

    /**
     * seems effective for the opening, but not sufficiently
     * discriminating in the end game
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        CheckersGame g = (CheckersGame) game.spawnChild(move);

        //if (g.getLegalMoves().length == 1) g.makeMove(g.getLegalMoves()[0]);
        double result = 0;
        int other = (role[0] == 0) ? 1 : 0;
        CheckersPiece piece;

        for (int i = 0; i < CheckersBoard.POSITIONS.length; i++) {
            piece = g.getBoard().getPieceAt(CheckersBoard.POSITIONS[i]);

            if (piece != null) {
                if (piece.getPlayer() == role[0]) {
                    result = result + 10;

                    if (piece.isKing()) {
                        result = result + 1000;
                    }
                } else {
                    result = result - 10;

                    if (piece.isKing()) {
                        result = result - 1000;
                    }
                }

                if (!piece.isKing()) {
                    if (role[0] == 0) {
                        result = result + (CheckersBoard.POSITIONS[i] % 10);
                    } else {
                        result = result +
                            (8 - (CheckersBoard.POSITIONS[i] % 10));
                    }
                }
            }
        }

        if (random != null) {
            result = result + random.nextDouble();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    double heuristic1(GamePlay game, GameMove move, int[] role) {
        CheckersGame g = (CheckersGame) game.spawnChild(move);
        int other = (role[0] == 0) ? 1 : 0;
        double result = 10 * (g.getBoard().getKingCount(role[0]) -
            g.getBoard().getKingCount(other));
        result = (result + g.getBoard().getPieceCount(role[0])) -
            g.getBoard().getPieceCount(other);

        if (random != null) {
            result = result + random.nextDouble();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = super.toString();
        s += "; CheckersPlayer properties: randomization ";

        if (random == null) {
            s += "disabled";
        } else {
            s += "enabled";
        }

        return s;
    }
}
