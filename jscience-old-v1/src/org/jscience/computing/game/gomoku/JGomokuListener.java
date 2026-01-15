/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.gomoku;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JGomokuListener extends MouseAdapter {
    /** DOCUMENT ME! */
    JGomoku jplay;

/**
     * Creates a new JGomokuListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JGomokuListener(JGomoku jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
        int column = ((int) e.getPoint().getX()) / jplay.panel.tileSize;
        int row = ((int) e.getPoint().getY()) / jplay.panel.tileSize;
        GomokuGame.Move move = new GomokuGame.Move(jplay.getAutoPlay().getGame()
                                                        .nextPlayer(), column,
                row);

        if (!jplay.getAutoPlay().getGame().isLegalMove(move)) {
            jplay.getFrame().say("FourWinsMove: " + move + " is not legal");

            return;
        }

        jplay.getFrame().say("requesting move: " + move);
        jplay.getFrame().requestGUIMove(move);
    }
}
