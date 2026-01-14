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

package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.TemplatePlayer;

import java.util.Random;


/**
 * provides AI for a TilePuzzle game
 *
 * @author Holger Antelmann
 */
public class TilePuzzlePlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 9095991555143586238L;

    /** DOCUMENT ME! */
    Random random;

/**
     * Creates a new TilePuzzlePlayer object.
     */
    public TilePuzzlePlayer() {
        this("unnamed TilePuzzlePlayer");
    }

/**
     * Creates a new TilePuzzlePlayer object.
     *
     * @param name DOCUMENT ME!
     */
    public TilePuzzlePlayer(String name) {
        this(name, SEARCH_ALPHABETA, false);
    }

/**
     * Creates a new TilePuzzlePlayer object.
     *
     * @param name            DOCUMENT ME!
     * @param searchOption    DOCUMENT ME!
     * @param trackingEnabled DOCUMENT ME!
     */
    public TilePuzzlePlayer(String name, int searchOption,
        boolean trackingEnabled) {
        this(name, searchOption, trackingEnabled, System.currentTimeMillis());
    }

/**
     * Creates a new TilePuzzlePlayer object.
     *
     * @param name            DOCUMENT ME!
     * @param searchOption    DOCUMENT ME!
     * @param trackingEnabled DOCUMENT ME!
     * @param randomSeed      DOCUMENT ME!
     */
    public TilePuzzlePlayer(String name, int searchOption,
        boolean trackingEnabled, long randomSeed) {
        super(name, searchOption, trackingEnabled);

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
        return (game instanceof TilePuzzle);
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
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        TilePuzzle g = (TilePuzzle) game.spawnChild(move);
        double dist = 0 - manhattanDistance(g);

        // a touch of randomization
        if (random != null) {
            dist = dist - random.nextDouble();
        }

        return dist;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double nielsson(TilePuzzle game) {
        double dist = 0 - ((3 * outOfPlace(game)) + manhattanDistance(game));

        return dist;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int manhattanDistance(TilePuzzle game) {
        Object[][] puzzle = game.getPuzzleMatrix();
        Object[][] solution = game.getSolutionMatrix();
        int distance = 0;

        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                if (puzzle[column][row] != null) {
                    Object tile = puzzle[column][row];
loop: 
                    for (int c = 0; c < puzzle.length; c++) {
                        for (int r = 0; r < puzzle[c].length; r++) {
                            if (solution[c][r] != null) {
                                if (tile.equals(solution[c][r])) {
                                    distance = distance + Math.abs(column - c) +
                                        Math.abs(row - r);

                                    break loop;
                                }
                            }
                        }
                    }
                }
            }
        }

        return distance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int outOfPlace(TilePuzzle game) {
        Object[][] puzzle = game.getPuzzleMatrix();
        Object[][] solution = game.getSolutionMatrix();
        int oop = 0;

        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                if (puzzle[column][row] != null) {
                    if (solution[column][row] != null) {
                        if (puzzle[column][row].equals(solution[column][row])) {
                            continue;
                        }
                    }

                    oop++;
                }
            }
        }

        return oop;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = super.toString();
        s += "; TilePuzzlePlayer properties: randomization ";

        if (random == null) {
            s += "disabled";
        } else {
            s += "enabled";
        }

        return s;
    }
}
