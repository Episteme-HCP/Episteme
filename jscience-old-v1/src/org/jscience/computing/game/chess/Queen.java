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
class Queen extends ChessPiece {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 4077514712426979176L;

    /**
     * Creates a new Queen object.
     *
     * @param color DOCUMENT ME!
     * @param board DOCUMENT ME!
     * @param position DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Queen(int color, ChessBoard board, BoardPosition position)
        throws IllegalArgumentException {
        super(color, board, position);

        //bishop = new Bishop(color, board, position);
        //rook   = new Rook  (color, board, position);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<BoardPosition> possibleTargets() {
        //bishop.setBoard(getBoard());
        //bishop.setPosition(getPosition());
        //rook.setBoard(getBoard());
        //rook.setPosition(getPosition());
        Vector<BoardPosition> targets = new Vector<BoardPosition>();
        //targets.addAll( bishop.possibleTargets() );
        //targets.addAll( rook.possibleTargets() );
        targets.addAll((new Rook(getColor(), getBoard(), getPosition())).possibleTargets());
        targets.addAll((new Bishop(getColor(), getBoard(), getPosition())).possibleTargets());

        return targets;
    }

    //Bishop bishop;
    //Rook   rook;
}
