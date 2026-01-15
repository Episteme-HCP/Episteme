/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
