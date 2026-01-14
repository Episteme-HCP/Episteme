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

import java.util.List;
import java.util.Vector;


/**
 * 
DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class King extends ChessPiece {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 5703820004972198319L;

    /**
     * Creates a new King object.
     *
     * @param color DOCUMENT ME!
     * @param board DOCUMENT ME!
     * @param position DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public King(int color, ChessBoard board, BoardPosition position)
        throws IllegalArgumentException {
        super(color, board, position);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<BoardPosition> possibleTargets() {
        BoardPosition p;
        Vector<BoardPosition> targets = new Vector<BoardPosition>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!((x == 0) && (y == 0))) {
                    p = getPosition().relativePosition(x, y);

                    if (validTarget(p)) {
                        targets.add(p);
                    }
                }
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
        List<ChessMove> moves = super.possibleMoves();
        moves.addAll(castleMoves());

        return moves;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private List<ChessMove> castleMoves() {
        Vector<ChessMove> moves = new Vector<ChessMove>();

        switch (getColor()) {
        case ChessBoard.WHITE:

            if ((getPosition().asInteger() == 51) &&
                    (board.getPieceAt('b', 1) == null) &&
                    (board.getPieceAt('c', 1) == null) &&
                    (board.getPieceAt('d', 1) == null) &&
                    (board.getPieceAt('a', 1) != null) &&
                    (board.getPieceAt('a', 1).getColor() == ChessBoard.WHITE) &&
                    (board.getPieceAt('a', 1).isRook()) &&
                    (board.getGame().queenSideCastleEnabled(ChessBoard.WHITE)) &&
                    (!board.positionAttackedBy(new BoardPosition(31),
                        getOppositeColor())) &&
                    (!board.positionAttackedBy(new BoardPosition(41),
                        getOppositeColor())) &&
                    (!board.positionAttackedBy(new BoardPosition(51),
                        getOppositeColor()))) {
                moves.add(new ChessMove(this, new BoardPosition(31)));
            }

            if ((getPosition().asInteger() == 51) &&
                    (board.getPieceAt('g', 1) == null) &&
                    (board.getPieceAt('f', 1) == null) &&
                    (board.getPieceAt('h', 1) != null) &&
                    (board.getPieceAt('h', 1).getColor() == ChessBoard.WHITE) &&
                    (board.getPieceAt('h', 1).isRook()) &&
                    (board.getGame().kingSideCastleEnabled(ChessBoard.WHITE)) &&
                    (!board.positionAttackedBy(new BoardPosition(51),
                        getOppositeColor())) &&
                    (!board.positionAttackedBy(new BoardPosition(61),
                        getOppositeColor())) &&
                    (!board.positionAttackedBy(new BoardPosition(71),
                        getOppositeColor()))) {
                moves.add(new ChessMove(this, new BoardPosition(71)));
            }

            break;

        case ChessBoard.BLACK:

            if ((getPosition().asInteger() == 58) &&
                    (board.getPieceAt('b', 8) == null) &&
                    (board.getPieceAt('c', 8) == null) &&
                    (board.getPieceAt('d', 8) == null) &&
                    (board.getPieceAt('a', 8) != null) &&
                    (board.getPieceAt('a', 8).getColor() == ChessBoard.BLACK) &&
                    (board.getPieceAt('a', 8).isRook()) &&
                    (board.getGame().queenSideCastleEnabled(ChessBoard.BLACK)) &&
                    (!board.positionAttackedBy(new BoardPosition(38),
                        getOppositeColor())) &&
                    (!board.positionAttackedBy(new BoardPosition(48),
                        getOppositeColor())) &&
                    (!board.positionAttackedBy(new BoardPosition(58),
                        getOppositeColor()))) {
                moves.add(new ChessMove(this, new BoardPosition(38)));
            }

            if ((getPosition().asInteger() == 58) &&
                    (board.getPieceAt('g', 8) == null) &&
                    (board.getPieceAt('f', 8) == null) &&
                    (board.getPieceAt('h', 8) != null) &&
                    (board.getPieceAt('h', 8).getColor() == ChessBoard.BLACK) &&
                    (board.getPieceAt('h', 8).isRook()) &&
                    (board.getGame().kingSideCastleEnabled(ChessBoard.BLACK)) &&
                    (!board.positionAttackedBy(new BoardPosition(58),
                        getOppositeColor())) &&
                    (!board.positionAttackedBy(new BoardPosition(68),
                        getOppositeColor())) &&
                    (!board.positionAttackedBy(new BoardPosition(78),
                        getOppositeColor()))) {
                moves.add(new ChessMove(this, new BoardPosition(78)));
            }

            break;

        default: // never reached

        }

        return moves;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean moveTo(BoardPosition pos) {
        if ((Math.abs(getPosition().getColumn() - pos.getColumn()) == 2) &&
                (pos.getColumn() != 'e')) {
            // it's a castle move that's not an undo move
            Rook rook;

            try {
                switch (pos.getColumn()) {
                case 'c':
                case 'C':
                    rook = (Rook) getBoard().getPieceAt('a', pos.getRow());

                    if (!rook.moveTo(new BoardPosition('d', pos.getRow()))) {
                        return false;
                    }

                    break;

                case 'g':
                case 'G':
                    rook = (Rook) getBoard().getPieceAt('h', pos.getRow());

                    if (!rook.moveTo(new BoardPosition('f', pos.getRow()))) {
                        return false;
                    }

                    break;

                default:
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return (super.moveTo(pos));
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean undoMove(ChessMove move) {
        if (move.isCastling()) {
            Rook rook;

            try {
                switch (getPosition().getColumn()) {
                case 'C':
                case 'c':
                    rook = (Rook) getBoard()
                                      .getPieceAt('d', getPosition().getRow());

                    if (!rook.moveTo(
                                new BoardPosition('a', getPosition().getRow()))) {
                        return false;
                    }

                    break;

                case 'G':
                case 'g':
                    rook = (Rook) getBoard()
                                      .getPieceAt('f', getPosition().getRow());

                    if (!rook.moveTo(
                                new BoardPosition('h', getPosition().getRow()))) {
                        return false;
                    }

                    break;

                default:
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return super.undoMove(move);
    }
}
