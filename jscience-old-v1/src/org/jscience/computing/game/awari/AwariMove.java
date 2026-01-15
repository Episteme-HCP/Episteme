/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
