/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.fourwins;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JFourWinsPanelListener extends MouseAdapter {
    /** DOCUMENT ME! */
    JFourWins jplay;

/**
     * Creates a new JFourWinsPanelListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JFourWinsPanelListener(JFourWins jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
        int column = ((int) e.getPoint().getX()) / jplay.panel.tileSize;
        FourWinsMove move = new FourWinsMove(jplay.getAutoPlay().getGame()
                                                  .nextPlayer(), column);

        if (!jplay.getAutoPlay().getGame().isLegalMove(move)) {
            jplay.getFrame().say("FourWinsMove: " + move + " is not legal");

            return;
        }

        jplay.getFrame().say("requesting move: " + move);
        jplay.getFrame().requestGUIMove(move);

        //jplay.panel.repaint();
    }
}
