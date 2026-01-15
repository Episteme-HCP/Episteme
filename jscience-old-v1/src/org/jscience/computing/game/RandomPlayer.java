/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import java.util.Random;


/**
 * RandomPlayer provides a reference implementation of the Player
 * interface. The RandomPlayer is a Player implementation that can play any
 * game without further domain knowledge by providing either a purely random
 * heuristic or heuristics that take advantage of the knowledge whether a
 * GameMove can win or loose a given GamePlay. If the tree search options for
 * its super class are set to a meaningful option and the getCheckForWin() is
 * set to true, the RandomPlayer can actually play reasonably intelligent
 * based on a given level when selecting moves (that is the RandomPlayer then
 * ensures that if a win/loss situation can be seen within the given level,
 * the prefered move by this RandomPlaye is the one that either wins or at
 * least doesn't loose the game within the number of moves specified by the
 * level).
 *
 * @author Holger Antelmann
 *
 * @see GamePlay
 * @see GameMove
 * @see TemplatePlayer
 * @see Player
 */
public class RandomPlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1688213522164461957L;

    /** DOCUMENT ME! */
    Random random = null;

    /** DOCUMENT ME! */
    boolean checkForWin = false;

    /** DOCUMENT ME! */
    final int HEURISTIC_CONSTANT = 0;

/**
     * calls RandomPlayer(System.currentTimeMillis())
     */
    public RandomPlayer() {
        this(System.currentTimeMillis());
    }

/**
     * calls RandomPlayer("unnamed RandomPlayer")
     *
     * @param randomSeed DOCUMENT ME!
     */
    public RandomPlayer(long randomSeed) {
        this("unnamed RandomPlayer");
    }

/**
     * creates a RandomPlayer with no tree search option, no win checking and
     * takes System.currentTimeMillis() as random seed.
     *
     * @param name DOCUMENT ME!
     */
    public RandomPlayer(String name) {
        this(name, System.currentTimeMillis());
    }

/**
     * calls RandomPlayer(name, randomSeed, false, 0, false)
     *
     * @param name       DOCUMENT ME!
     * @param randomSeed DOCUMENT ME!
     */
    public RandomPlayer(String name, long randomSeed) {
        this(name, randomSeed, false, 0, false);
    }

/**
     * If checkForWin is set to true, heuristic() does preserve the knowledge
     * about whether it wins or loses a game with the given move, in which
     * case appropriate values (other than random ones) are returned. If the
     * searchOption is then set to one of the tree search options (see
     * TemplatePlayer), the  RandomPlayer can in fact play reasonably
     * intelligent as to be able to seek a move that wins or does not loose
     * within the number of moves given by the level.
     *
     * @see TemplatePlayer
     */
    public RandomPlayer(String name, long randomSeed, boolean checkForWin,
        int searchOption, boolean enableTracking) {
        super(name, searchOption, enableTracking);

        if (randomSeed != 0) {
            random = new Random(randomSeed);
        }

        this.checkForWin = checkForWin;
    }

    /**
     * always returns true, since the RandomPlayer can play any game
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        return true;
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
     * if enabled, the RandomPlayer's heuristic checks whether a given
     * move can win the game and returns values accordingly
     *
     * @param enable DOCUMENT ME!
     */
    public void setCheckForWin(boolean enable) {
        checkForWin = enable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getCheckForWin() {
        return checkForWin;
    }

    /**
     * Preserves the knowlege of whether the move wins or looses the
     * game only if getCheckForWin() returns true; return values are then
     * randomized unless the random seed is set to 0.
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        double result = HEURISTIC_CONSTANT;

        if (checkForWin) {
            result = GameUtils.checkForWin(game.spawnChild(move), role) * 2;
        }

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
        s += "; RandomPlayer properties: checking for win is ";

        if (checkForWin) {
            s += "enabled";
        } else {
            s += "disabled";
        }

        return s;
    }
}
