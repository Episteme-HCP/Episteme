/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JSolitairePanelListener extends MouseInputAdapter {
    /** DOCUMENT ME! */
    JSolitaire jplay;

    /** DOCUMENT ME! */
    SolitairePosition origin = null;

/**
     * Creates a new JSolitairePanelListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JSolitairePanelListener(JSolitaire jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
        origin = jplay.panel.pointToPosition(e.getPoint());

        if (origin == null) {
            jplay.getFrame().say("not a valid position");

            return;
        }

        Solitaire game = (Solitaire) jplay.getAutoPlay().getGame();

        if (game.getValueAt(origin) != 1) {
            origin = null;
            jplay.getFrame().say("position is empty");

            return;
        }

        jplay.getFrame().say("trying to move from: " + origin);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseReleased(MouseEvent e) {
        if (origin == null) {
            return;
        }

        SolitairePosition dest = jplay.panel.pointToPosition(e.getPoint());

        if (dest == null) {
            jplay.getFrame().say("not a valid position");
            origin = null;
            jplay.panel.draggedPeg = null;
            jplay.panel.repaint();

            return;
        }

        jplay.getFrame().say("trying to move to: " + dest);

        SolitaireMove move = new SolitaireMove(jplay.getAutoPlay().getGame()
                                                    .nextPlayer(), origin, dest);

        if (!jplay.getAutoPlay().getGame().isLegalMove(move)) {
            jplay.getFrame().say("Move: " + move + " is not legal");
            origin = null;
            jplay.panel.draggedPeg = null;
            jplay.panel.repaint();

            return;
        }

        jplay.getFrame().say("requesting move: " + move);
        jplay.getFrame().requestGUIMove(move);
        origin = null;
        jplay.panel.draggedPeg = null;
        jplay.panel.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseDragged(MouseEvent e) {
        if (origin == null) {
            return;
        }

        jplay.panel.draggedPeg = origin;
        jplay.panel.dragPoint = e.getPoint();
        jplay.panel.repaint();
    }
}
