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

package org.jscience.computing.game.checkers;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JCheckersListener extends MouseInputAdapter {
    /** DOCUMENT ME! */
    JCheckers jplay;

    /** DOCUMENT ME! */
    int origin = 0;

/**
     * Creates a new JCheckersListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JCheckersListener(JCheckers jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
        CheckersGame game = (CheckersGame) jplay.getAutoPlay().getGame();
        Point point = e.getPoint();

        if (jplay.isFlipped()) {
            point = new Point(jplay.jboard.getWidth() - (int) point.getX(),
                    jplay.jboard.getHeight() - (int) point.getY());
        }

        origin = jplay.jboard.pointToPosition(point);

        if (origin == 0) {
            return;
        }

        if ((game.getBoard().getPieceAt(origin) == null) ||
                (game.getBoard().getPieceAt(origin).getPlayer() != game.nextPlayer())) {
            origin = 0;

            return;
        }

        jplay.getFrame().say("trying to move piece at: " + origin);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseReleased(MouseEvent e) {
        if (origin == 0) {
            return;
        }

        Point point = e.getPoint();

        if (jplay.isFlipped()) {
            point = new Point(jplay.jboard.getWidth() - (int) point.getX(),
                    jplay.jboard.getHeight() - (int) point.getY());
        }

        int dest = jplay.jboard.pointToPosition(point);

        if (dest == 0) {
            jplay.getFrame().say("not a valid position");
            origin = 0;
            jplay.jboard.draggedPiece = 0;
            jplay.jboard.repaint();

            return;
        }

        jplay.getFrame().say("trying to move to: " + dest);

        CheckersMove[] moves = (CheckersMove[]) jplay.getAutoPlay().getGame()
                                                     .getLegalMoves();
        CheckersMove move = null;

        for (int i = 0; i < moves.length; i++) {
            if ((moves[i].getFrom() == origin) && (moves[i].getTo() == dest)) {
                move = moves[i];

                break;
            }
        }

        if (move == null) {
            jplay.getFrame().say("Move: " + move + " is not legal");
            origin = 0;
            jplay.jboard.draggedPiece = 0;
            jplay.jboard.repaint();

            return;
        }

        jplay.getFrame().say("requesting move: " + move);
        jplay.getFrame().requestGUIMove(move);
        origin = 0;
        jplay.jboard.draggedPiece = 0;
        jplay.jboard.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseDragged(MouseEvent e) {
        if (origin == 0) {
            return;
        }

        jplay.jboard.draggedPiece = origin;
        jplay.jboard.dragPoint = e.getPoint();
        jplay.jboard.repaint();
    }
}
