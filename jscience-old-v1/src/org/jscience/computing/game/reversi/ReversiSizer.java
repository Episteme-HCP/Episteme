/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.reversi;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class ReversiSizer extends ComponentAdapter {
    /** DOCUMENT ME! */
    JReversiPanel panel;

/**
     * Creates a new ReversiSizer object.
     *
     * @param panel DOCUMENT ME!
     */
    public ReversiSizer(JReversiPanel panel) {
        this.panel = panel;
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
        panel.setTileSize(min / 8, panel.tileInset);
    }
}
