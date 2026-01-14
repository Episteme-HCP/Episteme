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

package org.jscience.computing.game;

/**
 * An Exception that is thrown if a Player cannot play a given game
 * implementation
 *
 * @author Holger Antelmann
 */
public class CannotPlayGameException extends GameRuntimeException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -7825468847574732784L;

    /** DOCUMENT ME! */
    Player player;

    /** DOCUMENT ME! */
    GamePlay game;

/**
     * Creates a new CannotPlayGameException object.
     *
     * @param player     DOCUMENT ME!
     * @param game       DOCUMENT ME!
     * @param customText DOCUMENT ME!
     */
    public CannotPlayGameException(Player player, GamePlay game,
        String customText) {
        super(game,
            "The player " + player.getPlayerName() + " (" + player.getClass() +
            ")" + " cannot play the game " + game.getClass().getName() +
            "; more info: " + customText);
        this.player = player;
        this.game = game;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GamePlay getGame() {
        return game;
    }
}
