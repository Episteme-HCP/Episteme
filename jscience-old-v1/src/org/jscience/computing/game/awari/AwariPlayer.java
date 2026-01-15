/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.awari;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.TemplatePlayer;

import java.util.Random;


/**
 * AwariPlayer adds AI to the AwariGame
 *
 * @author Holger Antelmann
 *
 * @see AwariGame
 */
public class AwariPlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4921170950609823654L;

    /** DOCUMENT ME! */
    Random random;

/**
     * Creates a new AwariPlayer object.
     */
    public AwariPlayer() {
        this("AwariPlayer");
    }

/**
     * Creates a new AwariPlayer object.
     *
     * @param name DOCUMENT ME!
     */
    public AwariPlayer(String name) {
        super(name, SEARCH_ALPHABETA, true);
    }

    /**
     * randomizes the heuristics with the given seed. By default,
     * randomization is disabled; you can disable randomization through this
     * method by using 0 as the seed parameter. Every other number will
     * initialize the internal randomizer.
     *
     * @param seed DOCUMENT ME!
     */
    public void setRandomSeed(long seed) {
        if (seed == 0) {
            random = null;
        } else {
            if (random == null) {
                random = new Random(seed);
            } else {
                random.setSeed(seed);
            }
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
        return (game instanceof AwariGame);
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
        AwariGame g = (AwariGame) game.spawnChild(move);
        double h = g.getLead(role[0]);

        if (random != null) {
            h = h + random.nextDouble();
        }

        return h;
    }
}
