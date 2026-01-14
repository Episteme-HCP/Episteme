/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
