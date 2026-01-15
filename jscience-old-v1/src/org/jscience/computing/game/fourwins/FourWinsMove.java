/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.fourwins;

import org.jscience.computing.game.MoveTemplate;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class FourWinsMove extends MoveTemplate {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -3738931563012339301L;

    /** DOCUMENT ME! */
    private int column;

    /** DOCUMENT ME! */
    private double heuristic;

/**
     * Creates a new FourWinsMove object.
     *
     * @param player DOCUMENT ME!
     * @param column DOCUMENT ME!
     */
    FourWinsMove(int player, int column) {
        super(player);
        this.column = column;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumn() {
        return column;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "";

        switch (getPlayer()) {
        case 0:
            s += "X: ";

            break;

        case 1:
            s += "O: ";

            break;

        default:
            s += "none";
        }

        s += (column + 1);

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
        if (!(obj instanceof FourWinsMove)) {
            return false;
        }

        FourWinsMove o = (FourWinsMove) obj;

        if (player != o.player) {
            return false;
        }

        if (column != o.column) {
            return false;
        }

        return true;
    }
}
