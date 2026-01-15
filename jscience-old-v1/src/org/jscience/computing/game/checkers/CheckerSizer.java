/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.checkers;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class CheckerSizer extends ComponentAdapter {
    /** DOCUMENT ME! */
    JCheckers jplay;

/**
     * Creates a new CheckerSizer object.
     *
     * @param jplay DOCUMENT ME!
     */
    public CheckerSizer(JCheckers jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentResized(ComponentEvent e) {
        int height = e.getComponent().getSize().height;
        int width = e.getComponent().getSize().width;
        int min = (height < width) ? height : width;
        jplay.jboard.setTileSize(min / 8);
    }
}
