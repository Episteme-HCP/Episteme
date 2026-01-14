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

package org.jscience.computing.game.checkers;

import java.io.Serializable;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class CheckersPiece implements Serializable, Cloneable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1734088943870275274L;

    /** DOCUMENT ME! */
    boolean king = false;

    /** DOCUMENT ME! */
    int player;

/**
     * Creates a new CheckersPiece object.
     *
     * @param player DOCUMENT ME!
     */
    public CheckersPiece(int player) {
        this.player = player;

        if ((player < 0) || (player > 1)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param isKing DOCUMENT ME!
     */
    protected void setKing(boolean isKing) {
        king = isKing;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int getPlayer() {
        return player;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isKing() {
        return king;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "CheckersPiece (role" + player;
        s += ((king) ? ", is King)" : ")");

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        CheckersPiece o = (CheckersPiece) obj;

        if (player != o.player) {
            return false;
        }

        if (king != o.king) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
