/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;


/**
 * 
DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class Pawn extends ChessPiece {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 7240746166684654619L;

    /**
     * Creates a new Pawn object.
     *
     * @param color DOCUMENT ME!
     * @param board DOCUMENT ME!
     * @param position DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Pawn(int color, ChessBoard board, BoardPosition position)
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
        BoardPosition p = null;
        boolean more = false;

        // normal move forward
        switch (getColor()) {
        case ChessBoard.WHITE:
            p = getPosition().relativePosition(0, 1);

            break;

        case ChessBoard.BLACK:
            p = getPosition().relativePosition(0, -1);

            break;

        default:
            p = null; //never reached
        }

        if ((p != null) && (getBoard().getPieceAt(p) == null)) {
            targets.add(p);
            more = true;
        }

        // additional 2-step move in case the pawn hasn't been moved before
        p = null;

        if (more) {
            switch (getColor()) {
            case ChessBoard.WHITE:

                if (getPosition().getRow() == 2) {
                    p = getPosition().relativePosition(0, 2);
                }

                break;

            case ChessBoard.BLACK:

                if (getPosition().getRow() == 7) {
                    p = getPosition().relativePosition(0, -2);
                }

                break;

            default: // never reached

            }

            if ((p != null) && (getBoard().getPieceAt(p) == null)) {
                targets.add(p);
            }
        }

        // capture move to the right
        p = null;

        switch (getColor()) {
        case ChessBoard.WHITE:
            p = getPosition().relativePosition(1, 1);

            break;

        case ChessBoard.BLACK:
            p = getPosition().relativePosition(1, -1);

            break;

        default:
            p = null; //never reached
        }

        if ((validTarget(p)) && (getBoard().getPieceAt(p) != null)) {
            targets.add(p);
        }

        // capture move to the left
        p = null;

        switch (getColor()) {
        case ChessBoard.WHITE:
            p = getPosition().relativePosition(-1, 1);

            break;

        case ChessBoard.BLACK:
            p = getPosition().relativePosition(-1, -1);

            break;

        default:
            p = null; //never reached
        }

        if ((validTarget(p)) && (getBoard().getPieceAt(p) != null)) {
            targets.add(p);
        }

        // checking for En Passant ChessMove; the En Passant move is represented as a move within the same row
        char enpassantColumn = board.getGame().enpassantEnableColumn();
        ChessPiece piece = null;

        if ((getPosition().getColumn() - 1) == enpassantColumn) {
            if ((getColor() == ChessBoard.WHITE) &&
                    (getPosition().getRow() == 5) &&
                    (getBoard().getPieceAt(getPosition().relativePosition(-1, 1)) == null)) {
                targets.add(getPosition().relativePosition(-1, 0));
            }

            if ((getColor() == ChessBoard.BLACK) &&
                    (getPosition().getRow() == 4) &&
                    (getBoard()
                             .getPieceAt(getPosition().relativePosition(-1, -1)) == null)) {
                targets.add(getPosition().relativePosition(-1, 0));
            }
        }

        if ((getPosition().getColumn() + 1) == enpassantColumn) {
            if ((getColor() == ChessBoard.WHITE) &&
                    (getPosition().getRow() == 5) &&
                    (getBoard().getPieceAt(getPosition().relativePosition(1, 1)) == null)) {
                targets.add(getPosition().relativePosition(1, 0));
            }

            if ((getColor() == ChessBoard.BLACK) &&
                    (getPosition().getRow() == 4) &&
                    (getBoard().getPieceAt(getPosition().relativePosition(1, -1)) == null)) {
                targets.add(getPosition().relativePosition(1, 0));
            }
        }

        return targets;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    List<ChessMove> possibleMoves() {
        Vector<ChessMove> moves = new Vector<ChessMove>();
        Iterator e = possibleTargets().iterator();

        while (e.hasNext()) {
            BoardPosition p = (BoardPosition) e.next();

            /* //this portion is already taken care of by the normal move addition below which handles enpassant just fine
            if (getPosition().getRow() == p.getRow()) {
                // en passant
                switch (getColor()) {
                    case ChessBoard.WHITE:
                        moves.add( new ChessMove(this, p.relativePosition(0,1)) );
                        break;
                    case ChessBoard.BLACK:
                        moves.add( new ChessMove(this, p.relativePosition(0,-1)) );
                        break;
                    default: // nothing
                }
            }
            */
            if (((getColor() == ChessBoard.WHITE) && (p.getRank() == 8)) ||
                    ((getColor() == ChessBoard.BLACK) && (p.getRank() == 1))) {
                // promotions
                moves.add(new ChessMove(this, p,
                        new Queen(getColor(), getBoard(), p)));
                moves.add(new ChessMove(this, p,
                        new Knight(getColor(), getBoard(), p)));
                moves.add(new ChessMove(this, p,
                        new Rook(getColor(), getBoard(), p)));
                moves.add(new ChessMove(this, p,
                        new Bishop(getColor(), getBoard(), p)));
            } else {
                // normal move - capture or no capture
                moves.add(new ChessMove(this, p));
            }
        }

        return moves;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     * @param piece DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean promotionMoveTo(BoardPosition pos, ChessPiece piece) {
        if (moveTo(pos)) {
            ChessBoard b = getBoard();
            removeFromBoard();
            piece.placeOnBoard(b, pos);

            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean moveTo(BoardPosition pos) {
        try {
            if (getPosition().getRank() == pos.getRank()) {
                // en passant move
                Pawn p = (Pawn) getBoard().getPieceAt(pos);
                p.removeFromBoard();

                switch (getColor()) {
                case ChessBoard.WHITE:
                    getBoard().setPieceAt(null, getPosition());
                    getBoard().setPieceAt(this, pos.relativePosition(0, 1));
                    setPosition(pos.relativePosition(0, 1));

                    break;

                case ChessBoard.BLACK:
                    getBoard().setPieceAt(null, getPosition());
                    getBoard().setPieceAt(this, pos.relativePosition(0, -1));
                    setPosition(pos.relativePosition(0, -1));

                    break;

                default:
                    return false;
                }
            } else {
                if (!super.moveTo(pos)) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean undoMove(ChessMove move) {
        if (((BoardPosition) move.getOldPosition()).getRow() == ((BoardPosition) move.getNewPosition()).getRow()) {
            // enPassant move handling
            try {
                if (getBoard().getPieceAt((BoardPosition) move.getOldPosition()) != null) {
                    return false;
                }

                moveTo((BoardPosition) move.getOldPosition());

                ChessPiece captured = move.getCaptured();

                if (captured != null) {
                    captured.placeOnBoard(getBoard(),
                        ((BoardPosition) move.getNewPosition()));
                } else {
                    return false; // it's enpassant; there has to be a captured piece
                }

                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            // default case
            if (super.undoMove(move)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
