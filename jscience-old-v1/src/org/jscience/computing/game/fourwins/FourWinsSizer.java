/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.fourwins;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class FourWinsSizer extends ComponentAdapter {
    /** DOCUMENT ME! */
    JFourWinsPanel panel;

/**
     * Creates a new FourWinsSizer object.
     *
     * @param panel DOCUMENT ME!
     */
    public FourWinsSizer(JFourWinsPanel panel) {
        this.panel = panel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentResized(ComponentEvent e) {
        FourWinsGame game = (FourWinsGame) panel.jplay.getAutoPlay().getGame();
        int height = e.getComponent().getSize().height / game.boardHeight;
        int width = e.getComponent().getSize().width / game.boardWidth;
        int min = (height < width) ? height : width;
        panel.setTileSize(min, panel.tileInset);
    }
}
