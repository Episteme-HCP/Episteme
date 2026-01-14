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
 * MoveTemplate just provides a skeleton implementation for the
 * <code>GameMove</code> interface, so you can focus soley on the game
 * specific properties of the GameMove. Move specific distingtions - other
 * than the player role - have to be specified by a subclass. If the only
 * distingtion for a move in a game is who plays the move, however, instances
 * of this class will in fact be sufficient for use in that game.
 *
 * @author Holger Antelmann
 *
 * @see GameMove
 * @see GamePlay
 */
public class MoveTemplate implements GameMove {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -7024459561191495737L;

    /** DOCUMENT ME! */
    protected int player;

/**
     * Creates a new MoveTemplate object.
     *
     * @param player DOCUMENT ME!
     */
    public MoveTemplate(int player) {
        this.player = player;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPlayer() {
        return player;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (super.toString() + "; player: " + player);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof MoveTemplate)) {
            return false;
        }

        if (((MoveTemplate) obj).player != player) {
            return false;
        }

        return true;
    }
}
