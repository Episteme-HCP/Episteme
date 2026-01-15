/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.GameBoardMove;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class SolitaireMove extends GameBoardMove {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 3262981995186020006L;

/**
     * Creates a new SolitaireMove object.
     *
     * @param player DOCUMENT ME!
     * @param from   DOCUMENT ME!
     * @param to     DOCUMENT ME!
     */
    public SolitaireMove(int player, SolitairePosition from,
        SolitairePosition to) {
        super(player, from, to);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "from " + getOldPosition() + " to " + getNewPosition();
    }
}
