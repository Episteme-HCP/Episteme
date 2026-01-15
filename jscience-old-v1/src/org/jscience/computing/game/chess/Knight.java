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
class Knight extends ChessPiece {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -2291606173030135074L;

    /**
     * Creates a new Knight object.
     *
     * @param color DOCUMENT ME!
     * @param board DOCUMENT ME!
     * @param position DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Knight(int color, ChessBoard board, BoardPosition position)
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
        BoardPosition p;
        char c;
        int r;
        c = (char) (getPosition().getColumn() + 1);
        r = getPosition().getRow() + 2;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() + 2);
        r = getPosition().getRow() + 1;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() - 1);
        r = getPosition().getRow() - 2;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() - 2);
        r = getPosition().getRow() - 1;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() - 1);
        r = getPosition().getRow() + 2;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() + 1);
        r = getPosition().getRow() - 2;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() - 2);
        r = getPosition().getRow() + 1;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        c = (char) (getPosition().getColumn() + 2);
        r = getPosition().getRow() - 1;

        if (validTarget(c, r)) {
            targets.add(new BoardPosition(c, r));
        }

        return targets;
    }
}
