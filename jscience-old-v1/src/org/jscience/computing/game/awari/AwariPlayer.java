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
