/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
