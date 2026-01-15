/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.wolfsheep;

import org.jscience.computing.game.wolfsheep.WolfsheepGame.Move;
import org.jscience.computing.game.wolfsheep.WolfsheepGame.Piece;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JWSListener extends MouseInputAdapter {
    /** DOCUMENT ME! */
    JWS jplay;

    /** DOCUMENT ME! */
    int origin = 0;

/**
     * Creates a new JWSListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JWSListener(JWS jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
        WolfsheepGame game = (WolfsheepGame) jplay.getAutoPlay().getGame();

        if (game.gameOver()) {
            return;
        }

        Point point = e.getPoint();

        if (jplay.isFlipped()) {
            point = new Point(jplay.jboard.getWidth() - (int) point.getX(),
                    jplay.jboard.getHeight() - (int) point.getY());
        }

        origin = jplay.jboard.pointToPosition(point);

        if (origin == 0) {
            return;
        }

        byte other = (game.firstPlayer == WolfsheepGame.WOLF)
            ? WolfsheepGame.SHEEP : WolfsheepGame.WOLF;
        byte nextType = ((game.nextPlayer() % 2) == 0) ? game.firstPlayer : other;

        if ((getPiece(origin) == null) ||
                (getPiece(origin).getType() != nextType)) {
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
            jplay.jboard.draggedPiece = null;
            jplay.jboard.repaint();

            return;
        }

        jplay.getFrame().say("trying to move to: " + dest);

        Move[] moves = (Move[]) jplay.getAutoPlay().getGame().getLegalMoves();
        Move move = null;

        for (int i = 0; i < moves.length; i++) {
            if ((moves[i].getOldPosition().asInteger() == origin) &&
                    (moves[i].getNewPosition().asInteger() == dest)) {
                move = moves[i];

                break;
            }
        }

        if (move == null) {
            jplay.getFrame().say("Move: " + move + " is not legal");
            origin = 0;
            jplay.jboard.draggedPiece = null;
            jplay.jboard.repaint();

            return;
        }

        jplay.getFrame().say("requesting move: " + move);
        jplay.getFrame().requestGUIMove(move);
        origin = 0;
        jplay.jboard.draggedPiece = null;
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

        jplay.jboard.draggedPiece = getPiece(origin);
        jplay.jboard.dragPoint = e.getPoint();
        jplay.jboard.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Piece getPiece(int position) {
        WolfsheepGame game = (WolfsheepGame) jplay.getAutoPlay().getGame();

        if (game.getWolfPosition().asInteger() == position) {
            return game.wolf;
        }

        if (game.getSheepPosition(0).asInteger() == position) {
            return game.sheep[0];
        }

        if (game.getSheepPosition(1).asInteger() == position) {
            return game.sheep[1];
        }

        if (game.getSheepPosition(2).asInteger() == position) {
            return game.sheep[2];
        }

        if (game.getSheepPosition(3).asInteger() == position) {
            return game.sheep[3];
        }

        return null;
    }
}
