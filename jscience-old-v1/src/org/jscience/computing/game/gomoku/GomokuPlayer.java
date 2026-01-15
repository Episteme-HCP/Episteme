/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.gomoku;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.GameUtils;
import org.jscience.computing.game.TemplatePlayer;

import java.util.Random;


/**
 * adds AI to the GomokuGame
 *
 * @author Holger Antelmann
 *
 * @see GomokuGame
 */
public class GomokuPlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2723027527829523990L;

    /** DOCUMENT ME! */
    Random random;

/**
     * Creates a new GomokuPlayer object.
     */
    public GomokuPlayer() {
        this("default GomokuPlayer");
    }

/**
     * Creates a new GomokuPlayer object.
     *
     * @param name DOCUMENT ME!
     */
    public GomokuPlayer(String name) {
        super(name, SEARCH_ALPHABETA, true);
        random = new Random();
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        return (game instanceof GomokuGame);
    }

    /**
     * ignore all moves that are either at the edge or have no neighbor
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean pruneMove(GamePlay game, GameMove move, int[] role) {
        int column = ((GomokuGame.Move) move).getColumn();
        int row = ((GomokuGame.Move) move).getRow();
        GomokuGame g = (GomokuGame) game;

        try {
            if (g.getValueAt(column + 1, row) != GomokuGame.EMPTY) {
                return false;
            }

            if (g.getValueAt(column - 1, row) != GomokuGame.EMPTY) {
                return false;
            }

            if (g.getValueAt(column, row + 1) != GomokuGame.EMPTY) {
                return false;
            }

            if (g.getValueAt(column, row - 1) != GomokuGame.EMPTY) {
                return false;
            }

            if (g.getValueAt(column + 1, row + 1) != GomokuGame.EMPTY) {
                return false;
            }

            if (g.getValueAt(column + 1, row - 1) != GomokuGame.EMPTY) {
                return false;
            }

            if (g.getValueAt(column - 1, row + 1) != GomokuGame.EMPTY) {
                return false;
            }

            if (g.getValueAt(column - 1, row - 1) != GomokuGame.EMPTY) {
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        return true;
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
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        GomokuGame g = (GomokuGame) game.spawnChild(move);
        double result = GameUtils.checkForWin(game.spawnChild(move), role) * 2;

        if (result != 0) {
            return (result * 100000);
        }

        for (int c = 0; c < g.getWidth(); c++) {
            for (int r = 0; r < g.getHeight(); r++) {
                result = result + ratePosition(g, c, r, role[0]);
            }
        }

        if (random != null) {
            result = result + random.nextDouble();
        }

        return result;
    }

    /**
     * not done quite right, yet
     *
     * @param game DOCUMENT ME!
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double ratePosition(GomokuGame game, int column, int row,
        int player) {
        if (game.getValueAt(column, row) == game.EMPTY) {
            return 0;
        }

        int antiValue = (game.getValueAt(column, row) == 0) ? 1 : 0;
        int count = 0;
        int line;
        int delta;

        //checking right
        line = 1;
        delta = 1;

        while ((game.WINNING_LINE_LENGTH > delta) &&
                ((column + delta) < game.getWidth())) {
            if (antiValue == game.getValueAt(column + delta, row)) {
                break;
            }

            if (game.getValueAt(column + delta, row) == player) {
                line++;
            }

            delta++;
        }

        if (delta == game.WINNING_LINE_LENGTH) {
            count = count + (line * 100);
        }

        //checking up
        line = 1;
        delta = 1;

        while ((game.WINNING_LINE_LENGTH > delta) &&
                ((row + delta) < game.getHeight())) {
            if (antiValue == game.getValueAt(column, row + delta)) {
                break;
            }

            if (game.getValueAt(column, row + delta) == player) {
                line++;
            }

            delta++;
        }

        if (delta == game.WINNING_LINE_LENGTH) {
            count = count + (line * 100);
        }

        //checking diagonal up
        line = 1;
        delta = 1;

        while ((game.WINNING_LINE_LENGTH > delta) &&
                ((column + delta) < game.getWidth()) &&
                ((row + delta) < game.getHeight())) {
            if (antiValue == game.getValueAt(column + delta, row + delta)) {
                break;
            }

            if (game.getValueAt(column + delta, row + delta) == player) {
                line++;
            }

            delta++;
        }

        if (line == game.WINNING_LINE_LENGTH) {
            count = count + (line * 100);
        }

        //checking diagonal down
        line = 1;
        delta = 1;

        while ((game.WINNING_LINE_LENGTH > delta) &&
                ((column + delta) < game.getWidth()) && ((row - delta) >= 0)) {
            if (antiValue != game.getValueAt(column + delta, row - delta)) {
                break;
            }

            if (game.getValueAt(column + delta, row - delta) == player) {
                line++;
            }

            delta++;
        }

        if (delta == game.WINNING_LINE_LENGTH) {
            count = count + (line * 100);
        }

        if (player != game.getValueAt(column, row)) {
            count = 0 - count;
        }

        return (count);
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
}
