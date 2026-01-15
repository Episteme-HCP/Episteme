/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.reversi;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JReversiPanelListener extends MouseAdapter {
    /** DOCUMENT ME! */
    JReversi jplay;

/**
     * Creates a new JReversiPanelListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JReversiPanelListener(JReversi jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
        ReversiGame game = (ReversiGame) jplay.getAutoPlay().getGame();
        int column = (((int) e.getPoint().getX()) / jplay.panel.tileSize) + 1;
        int row = game.boardHeight -
            (((int) e.getPoint().getY()) / jplay.panel.tileSize);
        ReversiPosition pos = new ReversiPosition(column, row);
        ReversiMove move = new ReversiMove(jplay.getAutoPlay().getGame()
                                                .nextPlayer(), pos,
                game.getBoard());

        if (!jplay.getAutoPlay().getGame().isLegalMove(move)) {
            jplay.getFrame().say("ReversiMove: " + move + " is not legal");

            return;
        }

        jplay.getFrame().say("requesting move: " + move);
        jplay.getFrame().requestGUIMove(move);
    }
}
