/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.muehle;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.GameUtils;
import org.jscience.computing.game.TemplatePlayer;

import java.util.Random;


/**
 * adds AI to the MuehleGame and provides a suitable heuristic to evaluate
 * the game
 *
 * @author Holger Antelmann
 *
 * @see MuehleGame
 */
public class MuehlePlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 5358984832175362858L;

    /** DOCUMENT ME! */
    Random random;

/**
     * Creates a new MuehlePlayer object.
     */
    public MuehlePlayer() {
        this("unnamed MuehlePlayer");
    }

/**
     * Creates a new MuehlePlayer object.
     *
     * @param name DOCUMENT ME!
     */
    public MuehlePlayer(String name) {
        this(name, SEARCH_ALPHABETA, false);
    }

/**
     * Creates a new MuehlePlayer object.
     *
     * @param name            DOCUMENT ME!
     * @param searchOption    DOCUMENT ME!
     * @param trackingEnabled DOCUMENT ME!
     */
    public MuehlePlayer(String name, int searchOption, boolean trackingEnabled) {
        this(name, searchOption, trackingEnabled, System.currentTimeMillis());
    }

/**
     * Creates a new MuehlePlayer object.
     *
     * @param name            DOCUMENT ME!
     * @param searchOption    DOCUMENT ME!
     * @param trackingEnabled DOCUMENT ME!
     * @param randomSeed      DOCUMENT ME!
     */
    public MuehlePlayer(String name, int searchOption, boolean trackingEnabled,
        long randomSeed) {
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
        return (game instanceof MuehleGame);
    }

    /**
     * A move is pruned if capture moves are possible while this is not
     * one of them (with the exception of when the opponent may only have 3
     * pieces left and may jump).
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean pruneMove(GamePlay game, GameMove move, int[] role) {
        if (((MuehleMove) move).getOption() != null) {
            return false;
        }

        if (((MuehleGame) game).piecesLeft((role[0] == 0) ? 1 : 0) == 4) {
            return false;
        }

        MuehleMove[] moves = (MuehleMove[]) game.getLegalMoves();

        for (int i = 0; i < moves.length; i++) {
            if (moves[i].getOption() != null) {
                return true;
            }
        }

        return false;
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
        MuehleGame g = (MuehleGame) game.spawnChild(move);
        double result = GameUtils.checkForWin(g, role);

        if (result == 0) {
            // calculating the difference of the pieces left for each player
            result = g.piecesLeft(role[0]) -
                g.piecesLeft((role[0] == 0) ? 1 : 0);

            // weighting the current result with a factor of 1000
            result = result * 1000;

            // considering the number of legal moves available for the
            // player when in 'moving mode' (as opposed to 'placing mode')
            if (g.getMoveHistory().length > 17) {
                result = (result + g.getLegalMoves(role[0]).length) -
                    g.getLegalMoves((role[0] == 0) ? 1 : 0).length;
            }

            // todo: maybe looking at potential muehle positions?
            // adding just a little bit of randomization
            if (random != null) {
                result = result + random.nextFloat();
            }
        } else {
            result = result * 10000;

            // ensuring that a victory in the next move is considered
            // better than a victory in a couple of moves or an immediate
            // loss is worse than a later loss
            result = result +
                ((result < 0) ? game.getMoveHistory().length
                              : (-game.getMoveHistory().length));
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
        s += "; MuehlePlayer properties: randomization ";

        if (random == null) {
            s += "disabled";
        } else {
            s += "enabled";
        }

        return s;
    }
}
