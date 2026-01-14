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

package org.jscience.computing.game.chess;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JChessListener extends MouseInputAdapter {
    /** DOCUMENT ME! */
    JChess jplay;

    /** DOCUMENT ME! */
    int origin = 0;

/**
     * Creates a new JChessListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JChessListener(JChess jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
        origin = jplay.jboard.pointToPosition(e.getPoint());

        if (origin == 0) {
            return;
        }

        if (jplay.setupGame == null) {
            ChessGame game = (ChessGame) jplay.getAutoPlay().getGame();

            if ((game.getBoard().getPieceAt(origin) == null) ||
                    (game.getBoard().getPieceAt(origin).getColor() != game.nextPlayer())) {
                clean();

                return;
            }
        } else {
            if (jplay.setupGame.getBoard().getPieceAt(origin) == null) {
                clean();

                return;
            }
        }

        jplay.getFrame()
             .say("trying to move piece at: " + new BoardPosition(origin));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
        BoardPosition pos = new BoardPosition(jplay.jboard.pointToPosition(
                    e.getPoint()));

        if (pos == null) {
            return;
        }

        if (jplay.setupGame == null) {
            return;
        }

        ChessBoard board = jplay.setupGame.getBoard();
        ChessPiece piece = jplay.pieceSelector.getSelection();
        ChessPiece prev = board.getPieceAt(pos);

        if (prev != null) {
            prev.removeFromBoard();
        }

        if (piece != null) {
            piece.setBoard(board);
            piece.placeOnBoard(board, pos);
        }

        jplay.jboard.repaint();
    }

    /**
     * DOCUMENT ME!
     */
    void clean() {
        origin = 0;
        jplay.jboard.draggedPiece = 0;
        jplay.jboard.repaint();
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

        int dest = jplay.jboard.pointToPosition(e.getPoint());

        if (dest == 0) {
            jplay.getFrame().say("not a valid position");
            clean();

            return;
        }

        if (dest == origin) {
            jplay.getFrame().say("same position; nothing to do");
            clean();

            return;
        }

        if (jplay.setupGame == null) {
            // trying the normal move
            jplay.getFrame().say("trying to move to: " +
                new BoardPosition(dest));

            ChessMove[] moves = (ChessMove[]) jplay.getAutoPlay().getGame()
                                                   .getLegalMoves();
            ChessMove move = null;

            for (int i = 0; i < moves.length; i++) {
                if ((moves[i].getOldPosition().asInteger() == origin) &&
                        (moves[i].getNewPosition().asInteger() == dest)) {
                    move = moves[i];

                    break;
                }
            }

            if (move == null) {
                jplay.getFrame().say("ChessMove not legal");
                clean();

                return;
            }

            jplay.getFrame().say("requesting move: " + move);
            jplay.getFrame().requestGUIMove(move);
        } else {
            // in setup mode, move the piece in any case and replace it with whatever is there
            ChessBoard board = jplay.setupGame.getBoard();

            if (dest == origin) {
                clean();

                return;
            }

            ChessPiece prev = board.getPieceAt(dest);

            if (prev != null) {
                prev.removeFromBoard();
            }

            ChessPiece p = board.getPieceAt(origin);

            if (p != null) {
                board.setPieceAt(null, origin);
                board.setPieceAt(p, dest);
                p.setPosition(new BoardPosition(dest));
            }
        }

        clean();
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
