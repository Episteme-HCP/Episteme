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

package org.jscience.computing.game.muehle;

import org.jscience.computing.game.GameBoardMove;


/**
 * Note that the parameter syntax of the constructors is reversed for
 * MuehleMove as opposed to its superclasses. I.e. the (int)player comes after
 * the positions - so that an initial 'set piece' move that also captures a
 * piece can be properly distinguished from a MuehleMove that moves a piece
 * without taking a piece.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.muehle.MuehlePosition
 */
class MuehleMove extends GameBoardMove {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -8705572247385851540L;

/**
     * placing a piece on the board
     *
     * @param pos    DOCUMENT ME!
     * @param player DOCUMENT ME!
     */
    public MuehleMove(MuehlePosition pos, int player) {
        super(player, pos);

        if (pos == null) {
            String s = "MuehlePosition passed into MuehleMove (muehle) is null - not allowed";
            throw (new IllegalArgumentException(s));
        }
    }

/**
     * moving a piece on the board
     *
     * @param oldPos DOCUMENT ME!
     * @param newPos DOCUMENT ME!
     * @param player DOCUMENT ME!
     */
    public MuehleMove(MuehlePosition oldPos, MuehlePosition newPos, int player) {
        super(player, oldPos, newPos);

        if ((oldPos == null) || (newPos == null)) {
            String s = "MuehlePosition passed into MuehleMove (muehle) is null - not allowed";
            throw (new IllegalArgumentException(s));
        }
    }

/**
     * placing a piece on the board while closing a muehle and thus taking
     * another piece
     *
     * @param pos        DOCUMENT ME!
     * @param player     DOCUMENT ME!
     * @param pieceTaken DOCUMENT ME!
     */
    public MuehleMove(MuehlePosition pos, int player, MuehlePosition pieceTaken) {
        super(player, null, pos, pieceTaken);

        if ((pos == null) || (pieceTaken == null)) {
            String s = "an argument passed into MuehleMove (muehle) is null - not allowed";
            throw (new IllegalArgumentException(s));
        }
    }

/**
     * moving a piece on the board while closing a muehle and thus taking
     * another piece
     *
     * @param oldPos     DOCUMENT ME!
     * @param newPos     DOCUMENT ME!
     * @param player     DOCUMENT ME!
     * @param pieceTaken DOCUMENT ME!
     */
    public MuehleMove(MuehlePosition oldPos, MuehlePosition newPos, int player,
        MuehlePosition pieceTaken) {
        super(player, oldPos, newPos, pieceTaken);

        if ((oldPos == null) || (newPos == null) || (pieceTaken == null)) {
            String s = "an argument passed into MuehleMove (muehle) is null - not allowed";
            throw (new IllegalArgumentException(s));
        }
    }

    /**
     * overwritten to ensure that only MuehleMove instances are
     * considered equal
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof MuehleMove)) {
            return false;
        } else {
            return super.equals(obj);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "";
        s += ((getPlayer() == 0) ? "X " : "O ");

        if (getOldPosition() == null) {
            s += "put at ";
        } else {
            s += ("move from " + getOldPosition().asInteger());
            s += " to ";
        }

        s += getNewPosition().asInteger();

        if (getOption() != null) {
            s += (", take from " + ((MuehlePosition) getOption()).asInteger());
        }

        return s;
    }
}
