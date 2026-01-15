/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
