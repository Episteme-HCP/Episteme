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

package org.jscience.computing.game.reversi;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.TemplatePlayer;

import java.util.Random;


/**
 * adds AI to the ReversiGame
 *
 * @author Holger Antelmann
 *
 * @see ReversiGame
 */
public class ReversiPlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1914776443694471905L;

    /** DOCUMENT ME! */
    Random random;

    /** DOCUMENT ME! */
    public int borderWeight = 5;

    /** DOCUMENT ME! */
    public int cornerWeight = 12;

/**
     * Creates a new ReversiPlayer object.
     */
    public ReversiPlayer() {
        this("unnamed ReversiPlayer");
    }

/**
     * Creates a new ReversiPlayer object.
     *
     * @param name DOCUMENT ME!
     */
    public ReversiPlayer(String name) {
        this(name, SEARCH_ALPHABETA, false);
    }

/**
     * Creates a new ReversiPlayer object.
     *
     * @param name          DOCUMENT ME!
     * @param searchOption  DOCUMENT ME!
     * @param enableLogging DOCUMENT ME!
     */
    public ReversiPlayer(String name, int searchOption, boolean enableLogging) {
        this(name, searchOption, enableLogging, System.currentTimeMillis());
    }

/**
     * Creates a new ReversiPlayer object.
     *
     * @param name          DOCUMENT ME!
     * @param searchOption  DOCUMENT ME!
     * @param enableLogging DOCUMENT ME!
     * @param randomSeed    DOCUMENT ME!
     */
    public ReversiPlayer(String name, int searchOption, boolean enableLogging,
        long randomSeed) {
        super(name, searchOption, enableLogging);

        if (randomSeed != 0) {
            random = new Random(randomSeed);
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
        return (game instanceof ReversiGame);
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
     * this heuristic is basically counting pieces, but as long as the
     * game is not over, it weights the pieces differently according to their
     * potential for a final win. Following this strategy, corner tiles (as
     * they're not alterable anymore) are rated the highest, and border pieces
     * are also rated higher than others.
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        ReversiGame g = (ReversiGame) game.spawnChild(move);
        double result = 0;

        if (g.gameOver()) {
            result = g.getCount(role[0]) - g.getCount((role[0] == 0) ? 1 : 0);
            result = result * 1000;
        } else {
            int[] pieces = new int[3];
            int[] border = new int[3];
            int[] corner = new int[3];

            for (int column = 0; column < g.boardWidth; column++) {
                for (int row = 0; row < g.boardHeight; row++) {
                    pieces[g.getBoard()[column][row] + 1]++;

                    if ((column == 0) || (row == 0) ||
                            (column == (g.boardWidth - 1)) ||
                            (row == (g.boardWidth - 1))) {
                        border[g.getBoard()[column][row] + 1]++;

                        if (((column == 0) && (row == 0)) ||
                                ((column == (g.boardWidth - 1)) &&
                                (row == (g.boardWidth - 1)))) {
                            corner[g.getBoard()[column][row] + 1]++;
                        }
                    }
                }
            }

            result = (pieces[1] + (border[1] * borderWeight) +
                (corner[1] * cornerWeight)) - pieces[2] -
                (border[2] * borderWeight) - (corner[2] * cornerWeight);

            if (role[0] == 1) {
                result = 0 - result;
            }
        }

        // add a bit of randomization in case the above is not decisive enough
        if (random != null) {
            result = result + random.nextFloat();
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
        s += "; ReversiPlayer properties: randomization ";

        if (random == null) {
            s += "disabled";
        } else {
            s += "enabled";
        }

        return s;
    }
}
