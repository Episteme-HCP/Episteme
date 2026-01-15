/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import java.util.List;
import java.util.Vector;


/**
 * 
DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class Bishop extends ChessPiece {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 1598765013291317949L;

    /**
     * Creates a new Bishop object.
     *
     * @param color DOCUMENT ME!
     * @param board DOCUMENT ME!
     * @param position DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Bishop(int color, ChessBoard board, BoardPosition position)
        throws IllegalArgumentException {
        super(color, board, position);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<BoardPosition> possibleTargets() {
        Vector<BoardPosition> targets = new Vector<BoardPosition>();
        char c;
        int r;
        BoardPosition p;

        for (int i = 1;; i++) {
            c = (char) (getPosition().getColumn() + i);
            r = getPosition().getRow() + i;

            if (!validTarget(c, r)) {
                break;
            }

            p = new BoardPosition(c, r);
            targets.add(p);

            if (board.getPieceAt(p) != null) {
                break;
            }
        }

        for (int i = 1;; i++) {
            c = (char) (getPosition().getColumn() + i);
            r = getPosition().getRow() - i;

            if (!validTarget(c, r)) {
                break;
            }

            p = new BoardPosition(c, r);
            targets.add(p);

            if (board.getPieceAt(p) != null) {
                break;
            }
        }

        for (int i = 1;; i++) {
            c = (char) (getPosition().getColumn() - i);
            r = getPosition().getRow() + i;

            if (!validTarget(c, r)) {
                break;
            }

            p = new BoardPosition(c, r);
            targets.add(p);

            if (board.getPieceAt(p) != null) {
                break;
            }
        }

        for (int i = 1;; i++) {
            c = (char) (getPosition().getColumn() - i);
            r = getPosition().getRow() - i;

            if (!validTarget(c, r)) {
                break;
            }

            p = new BoardPosition(c, r);
            targets.add(p);

            if (board.getPieceAt(p) != null) {
                break;
            }
        }

        return targets;
    }
}
