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
