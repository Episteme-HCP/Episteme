/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
