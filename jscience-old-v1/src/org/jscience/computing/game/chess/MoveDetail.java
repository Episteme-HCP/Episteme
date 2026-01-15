/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import java.io.Serializable;


/**
 * MoveDetail is a 'read-only' class, none of its members can be changed
 * after instanciation. This is to ensure that there is no potential problem
 * when a ChessMove is cloned, which will not clone the MoveDetail ojbect.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.chess.ChessMove
 */
class MoveDetail implements Serializable, Cloneable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 498690297597150543L;

    /** DOCUMENT ME! */
    private ChessPiece captured;

    /** DOCUMENT ME! */
    private ChessPiece promoted;

/**
     * Creates a new MoveDetail object.
     *
     * @param captured DOCUMENT ME!
     */
    MoveDetail(ChessPiece captured) {
        this.captured = captured;
        this.promoted = null;
    }

/**
     * Creates a new MoveDetail object.
     *
     * @param captured DOCUMENT ME!
     * @param promoted DOCUMENT ME!
     */
    MoveDetail(ChessPiece captured, ChessPiece promoted) {
        this.captured = captured;
        this.promoted = promoted;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ChessPiece getCaptured() {
        return captured;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ChessPiece getPromotion() {
        return promoted;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "[org.jscience.computing.game.chess.MoveDetail:]";
        s += (" captured: " + captured);
        s += (", promoted: " + promoted);
        s += " [MoveDetail end]";

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    protected Object clone() throws CloneNotSupportedException {
        MoveDetail d = (MoveDetail) super.clone();

        if (captured != null) {
            d.captured = (ChessPiece) captured.clone();
        }

        if (promoted != null) {
            d.promoted = (ChessPiece) promoted.clone();
        }

        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        MoveDetail other = null;

        try {
            other = (MoveDetail) obj;
        } catch (ClassCastException e) {
            return false;
        }

        if (captured == null) {
            if (other.captured != null) {
                return false;
            }
        } else {
            if (other.captured == null) {
                return false;
            }

            if (!other.captured.equals(captured)) {
                return false;
            }
        }

        if (promoted == null) {
            if (other.promoted != null) {
                return false;
            }
        } else {
            if (other.promoted == null) {
                return false;
            }

            if (!other.promoted.equals(promoted)) {
                return false;
            }
        }

        return true;
    }
}
