/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.muehle;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JMuehleListener extends MouseInputAdapter {
    /** DOCUMENT ME! */
    JMuehle jplay;

    /** DOCUMENT ME! */
    MuehlePosition origin = null;

/**
     * Creates a new JMuehleListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JMuehleListener(JMuehle jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
        MuehlePosition place = jplay.jboard.pointToPosition(e.getPoint());
        MuehleGame game = (MuehleGame) jplay.getAutoPlay().getGame();

        if (jplay.jboard.captureMove == null) {
            if (place == null) {
                return;
            }

            if (game.getValueAt(place) != MuehleGame.EMPTY) {
                return;
            }

            if (game.getRemainingInHand(game.nextPlayer()) <= 0) {
                return;
            }

            MuehleMove move = new MuehleMove(place, game.nextPlayer());

            if (game.isLegalMove(move)) {
                jplay.getFrame().requestGUIMove(move);
            } else {
                MuehleMove[] moves = (MuehleMove[]) jplay.getAutoPlay().getGame()
                                                         .getLegalMoves();

                for (int i = 0; i < moves.length; i++) {
                    if (((MuehleMove) moves[i]).getOption() != null) {
                        jplay.jboard.captureMove = move;
                        jplay.getFrame().say("now select piece to be captured");
                        jplay.jboard.repaint();

                        break;
                    }
                }
            }
        } else {
            MuehleMove cm = jplay.jboard.captureMove;
            jplay.jboard.captureMove = null;
            jplay.jboard.repaint();

            if (place == null) {
                return;
            }

            if (jplay.jboard.draggedPiece == null) {
                cm = new MuehleMove((MuehlePosition) cm.getNewPosition(),
                        cm.getPlayer(), place);
            } else {
                cm = new MuehleMove((MuehlePosition) cm.getOldPosition(),
                        (MuehlePosition) cm.getNewPosition(), cm.getPlayer(),
                        place);
                jplay.jboard.draggedPiece = null;
            }

            jplay.getFrame().requestGUIMove(cm);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
        origin = jplay.jboard.pointToPosition(e.getPoint());

        if (origin == null) {
            return;
        }

        MuehleGame game = (MuehleGame) jplay.getAutoPlay().getGame();

        if (game.getRemainingInHand(game.nextPlayer()) > 0) {
            origin = null;

            return;
        }

        if ((game.getValueAt(origin) != game.nextPlayer())) {
            origin = null;

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
        if (origin == null) {
            return;
        }

        if (jplay.jboard.captureMove != null) {
            return;
        }

        MuehleGame game = (MuehleGame) jplay.getAutoPlay().getGame();

        if (game.getRemainingInHand(game.nextPlayer()) > 0) {
            return;
        }

        MuehlePosition dest = jplay.jboard.pointToPosition(e.getPoint());

        if (dest == null) {
            jplay.getFrame().say("not a valid position");
            origin = null;
            jplay.jboard.draggedPiece = null;
            jplay.jboard.repaint();

            return;
        }

        jplay.getFrame().say("trying to move to: " + dest);

        MuehleMove[] moves = (MuehleMove[]) jplay.getAutoPlay().getGame()
                                                 .getLegalMoves();
        MuehleMove move = null;

        for (int i = 0; i < moves.length; i++) {
            if ((moves[i].getOldPosition().equals(origin)) &&
                    (moves[i].getNewPosition().equals(dest))) {
                move = moves[i];

                break;
            }
        }

        if (move == null) {
            jplay.getFrame().say("MuehleMove is not legal");
            origin = null;
            jplay.jboard.draggedPiece = null;
            jplay.jboard.repaint();

            return;
        }

        if (move.getOption() == null) {
            jplay.getFrame().requestGUIMove(move);
            jplay.jboard.draggedPiece = null;
        } else {
            jplay.jboard.captureMove = move;
        }

        origin = null;
        jplay.jboard.repaint();
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

        jplay.jboard.draggedPiece = origin;
        jplay.jboard.dragPoint = e.getPoint();
        jplay.jboard.repaint();
    }
}
