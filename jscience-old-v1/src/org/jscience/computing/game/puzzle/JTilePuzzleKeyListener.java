/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * not used, yet
 *
 * @author Holger Antelmann
 */
class JTilePuzzleKeyListener implements KeyListener {
    /** DOCUMENT ME! */
    JTilePuzzle jplay;

/**
     * Creates a new JTilePuzzleKeyListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JTilePuzzleKeyListener(JTilePuzzle jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void keyPressed(KeyEvent e) {
        // nothing
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void keyReleased(KeyEvent e) {
        // nothing
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void keyTyped(KeyEvent e) {
        TilePuzzleMove move;

        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            jplay.getFrame().say("moving left");
            move = new TilePuzzleMove(TilePuzzleMove.LEFT);
            jplay.frame.requestGUIMove(move);

            break;

        case KeyEvent.VK_RIGHT:
            jplay.getFrame().say("moving right");
            move = new TilePuzzleMove(TilePuzzleMove.RIGHT);
            jplay.frame.requestGUIMove(move);

            break;

        case KeyEvent.VK_UP:
            jplay.getFrame().say("moving up");
            move = new TilePuzzleMove(TilePuzzleMove.UP);
            jplay.frame.requestGUIMove(move);

            break;

        case KeyEvent.VK_DOWN:
            jplay.getFrame().say("moving down");
            move = new TilePuzzleMove(TilePuzzleMove.DOWN);
            jplay.frame.requestGUIMove(move);

            break;

        default:

            //nothing
        }

        jplay.frame.repaint();
    }
}
