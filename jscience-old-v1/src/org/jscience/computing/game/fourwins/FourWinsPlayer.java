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

package org.jscience.computing.game.fourwins;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.GameUtils;
import org.jscience.computing.game.TemplatePlayer;

import java.util.Random;


/**
 * adds AI to the FourWinsGame
 *
 * @author Holger Antelmann
 *
 * @see FourWinsGame
 */
public class FourWinsPlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4468286686431444991L;

    /** DOCUMENT ME! */
    Random random;

/**
     * Creates a new FourWinsPlayer object.
     */
    public FourWinsPlayer() {
        this("unnamed FourWinsPlayer");
    }

/**
     * Creates a new FourWinsPlayer object.
     *
     * @param name DOCUMENT ME!
     */
    public FourWinsPlayer(String name) {
        this(name, SEARCH_ALPHABETA, false);
    }

/**
     * Creates a new FourWinsPlayer object.
     *
     * @param name            DOCUMENT ME!
     * @param searchOption    DOCUMENT ME!
     * @param trackingEnabled DOCUMENT ME!
     */
    public FourWinsPlayer(String name, int searchOption, boolean trackingEnabled) {
        this(name, searchOption, trackingEnabled, System.currentTimeMillis());
    }

/**
     * Creates a new FourWinsPlayer object.
     *
     * @param name            DOCUMENT ME!
     * @param searchOption    DOCUMENT ME!
     * @param trackingEnabled DOCUMENT ME!
     * @param randomSeed      DOCUMENT ME!
     */
    public FourWinsPlayer(String name, int searchOption,
        boolean trackingEnabled, long randomSeed) {
        super(name, searchOption, trackingEnabled);

        if (randomSeed != 0) {
            random = new Random(randomSeed);
        }

        setOrderMoves(true);
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
     */
    public void disableRandom() {
        random = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        return (game instanceof FourWinsGame);
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param role DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param milliseconds DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GameMove selectMove(GamePlay game, int[] role, int level,
        long milliseconds) {
        if (game.getMoveHistory().length == 0) {
            // book move
            return new FourWinsMove(0, (((FourWinsGame) game).boardWidth / 2));
        } else {
            return super.selectMove(game, role, level, milliseconds);
        }
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
        game = game.spawnChild(move);

        double result = GameUtils.checkForWin(game, role);

        if (result == 0) {
            FourWinsGame g = (FourWinsGame) game;

            for (int column = 0; column < g.boardWidth; column++) {
                for (int row = 0; row < g.boardHeight; row++) {
                    result = result + ratePosition(g, column, row, role[0]);
                }
            }
        } else {
            result = result * 10000;
            result = result +
                ((result < 0) ? game.getMoveHistory().length
                              : (-game.getMoveHistory().length));
        }

        // ad just a little randomization, in case the above wasn't decisive enough
        if (random != null) {
            result = result + random.nextFloat();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double ratePosition(FourWinsGame game, int column, int row,
        int player) {
        if (game.getValueAt(column, row) == -1) {
            return 0;
        }

        int antiValue = (game.getValueAt(column, row) == 0) ? 1 : 0;
        int count = 0;
        int line;
        int delta;
        int lcount;

        //checking right
        line = 1;
        delta = 1;
        lcount = 0;

        while ((game.WINNING_LINE_LENGTH > delta) &&
                ((column + delta) < game.boardWidth)) {
            if (antiValue == game.getValueAt(column + delta, row)) {
                break;
            }

            if (game.getValueAt(column + delta, row) != -1) {
                lcount++;
            }

            line++;
            delta++;
        }

        if (line == game.WINNING_LINE_LENGTH) {
            count = count + lcount;
        }

        //checking up
        line = 1;
        delta = 1;
        lcount = 0;

        while ((game.WINNING_LINE_LENGTH > delta) &&
                ((row + delta) < game.boardHeight)) {
            if (antiValue == game.getValueAt(column, row + delta)) {
                break;
            }

            if (game.getValueAt(column, row + delta) != -1) {
                lcount++;
            }

            line++;
            delta++;
        }

        if (line == game.WINNING_LINE_LENGTH) {
            count = count + lcount;
        }

        //checking diagonal up
        line = 1;
        delta = 1;
        lcount = 0;

        while ((game.WINNING_LINE_LENGTH > delta) &&
                ((column + delta) < game.boardWidth) &&
                ((row + delta) < game.boardHeight)) {
            if (antiValue == game.getValueAt(column + delta, row + delta)) {
                break;
            }

            if (game.getValueAt(column + delta, row + delta) != -1) {
                lcount++;
            }

            line++;
            delta++;
        }

        if (line == game.WINNING_LINE_LENGTH) {
            count = count + lcount;
        }

        //checking diagonal down
        line = 1;
        delta = 1;
        lcount = 0;

        while ((game.WINNING_LINE_LENGTH > delta) &&
                ((column + delta) < game.boardWidth) && ((row - delta) >= 0)) {
            if (antiValue != game.getValueAt(column + delta, row - delta)) {
                break;
            }

            if (game.getValueAt(column + delta, row - delta) != -1) {
                lcount++;
            }

            line++;
            delta++;
        }

        if (line == game.WINNING_LINE_LENGTH) {
            count = count + lcount;
        }

        if (player != game.getValueAt(column, row)) {
            count = 0 - count;
        }

        return (count);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = super.toString();
        s += "; FourWinsPlayer properties: randomization ";

        if (random == null) {
            s += "disabled";
        } else {
            s += "enabled";
        }

        return s;
    }
}
