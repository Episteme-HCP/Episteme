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

import org.jscience.computing.game.MoveTemplate;


/**
 * AwariMove implements a game move for AwariGame
 *
 * @author Holger Antelmann
 *
 * @see AwariGame
 */
class AwariMove extends MoveTemplate {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 3229016848843074677L;

    /** DOCUMENT ME! */
    int position;

    /** DOCUMENT ME! */
    AwariGame game;

/**
     * Creates a new AwariMove object.
     *
     * @param player   DOCUMENT ME!
     * @param position DOCUMENT ME!
     */
    public AwariMove(int player, int position) {
        super(player);
        this.position = position;
    }

/**
     * for allowing undo moves
     *
     * @param player   DOCUMENT ME!
     * @param position DOCUMENT ME!
     * @param game     DOCUMENT ME!
     */
    AwariMove(int player, int position, AwariGame game) {
        this(player, position);

        try {
            this.game = (AwariGame) game.clone();
        } catch (CloneNotSupportedException e) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getPosition() {
        return position;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (((AwariMove) obj).position == position) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * denotes positions as columns from left to right
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        int row = position;

        switch (row) {
        case 13:
            row = 1;

            break;

        case 12:
            row = 2;

            break;

        case 11:
            row = 3;

            break;

        case 10:
            row = 4;

            break;

        case 9:
            row = 5;

            break;

        case 8:
            row = 6;

            break;
        }

        return ("role " + getPlayer() + " column " + row);
    }
}
