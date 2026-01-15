/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class ChessSizer extends ComponentAdapter {
    /** DOCUMENT ME! */
    JChessBoard jboard;

/**
     * Creates a new ChessSizer object.
     *
     * @param jboard DOCUMENT ME!
     */
    public ChessSizer(JChessBoard jboard) {
        this.jboard = jboard;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentResized(ComponentEvent e) {
        //if (e.getComponent().getPreferredSize() == e.getComponent().getSize()) return;
        int height = e.getComponent().getSize().height;
        int width = e.getComponent().getSize().width;
        int min = (height < width) ? height : width;

        if (jboard.getShowCoordinates()) {
            jboard.setTileSize(min / 9);
        } else {
            jboard.setTileSize(min / 8);
        }
    }
}
