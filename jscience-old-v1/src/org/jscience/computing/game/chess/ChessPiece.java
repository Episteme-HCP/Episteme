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

import java.io.Serializable;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;


/**
 * ChessPiece provides all common functions for a chess piece which then is
 * specialized by the actual 6 different game pieces.
 *
 * @author Holger Antelmann
 */
abstract class ChessPiece implements Serializable, Cloneable {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 8379173264790703516L;

    /**
     * DOCUMENT ME!
     */
    int color;

    /**
     * DOCUMENT ME!
     */
    ChessBoard board;

    /**
     * DOCUMENT ME!
     */
    BoardPosition position;

    /**
     * Creates a new ChessPiece object.
     *
     * @param color DOCUMENT ME!
     * @param board DOCUMENT ME!
     * @param position DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    protected ChessPiece(int color, ChessBoard board, BoardPosition position)
        throws IllegalArgumentException {
        this.board = board;
        this.position = position;

        switch (color) {
        case ChessBoard.WHITE:
        case ChessBoard.BLACK:
            this.color = color;

            break;

        default:

            String s = "Problem encountered in org.jscience.computing.game.chess.ChessPiece constructor:";
            s += " piece color not valid - cannot instanciate Piece.";
            throw (new IllegalArgumentException(s));
        }
    }

    /**
     * possibleTargets() returns a list of BoardPositions to where the
     * Piece could move. The list includes moves that may be illegal due to
     * placing the King into check.
     *
     * @return a List containing objects of type ChessMove
     */
    abstract public List possibleTargets();

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean possibleTarget(BoardPosition pos) {
        if (possibleTargets().contains(pos)) {
            return true;
        }

        return false;
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
            moves.add(new ChessMove(this, p));
        }

        return moves;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<ChessMove> legalMoves() {
        Vector<ChessMove> moves = new Vector<ChessMove>();
        Iterator e = possibleMoves().iterator();

        // save the reference to the board, so that if this is
        // a promotion move, the reference doesn't get lost when
        // the pawn is 'taken off the board' to make the move
        ChessBoard b = board;

        while (e.hasNext()) {
            ChessMove move = (ChessMove) e.next();
            b.getGame().pushMove(move);
            b.getGame().setTempMove(move);

            if (!b.findKing(color).underAttack()) {
                moves.add(move);
            }

            // checking whether this move is a check move
            // maybe to be avoided for performance reasons
            if (b.findKing((color == ChessBoard.WHITE) ? ChessBoard.BLACK
                                                           : ChessBoard.WHITE)
                     .underAttack()) {
                move.setCheck(true);
            }

            b.getGame().popMove();
            b.getGame().setTempMove(null);

            /* old version
            move.setValid(true);
            if (!((ChessGameBoard)board.getGame().spawnChild(move)).getBoard().findKing(color).underAttack()) moves.add(move);
            */
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
        if (pos != null) {
            ChessPiece targetPiece = board.getPieceAt(pos);

            if (targetPiece != null) {
                targetPiece.removeFromBoard();
            }

            board.setPieceAt(null, position); // take the piece from its old location
            board.setPieceAt(this, pos); // put the piece to its new location
            position = pos; // set the new position for the piece

            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean undoMove(ChessMove move) {
        try {
            if (board.getPieceAt((BoardPosition) move.getOldPosition()) != null) {
                return false;
            }

            moveTo((BoardPosition) move.getOldPosition());

            ChessPiece captured = move.getCaptured();

            if (captured != null) {
                captured.placeOnBoard(board,
                    (BoardPosition) move.getNewPosition());
            }

            if (move.getPromotion() != null) {
                ChessBoard b = getBoard();
                removeFromBoard();

                Pawn p = new Pawn(getColor(), b,
                        (BoardPosition) move.getOldPosition());
                placeOnBoard(b, (BoardPosition) move.getOldPosition());
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean underAttack() {
        return (board.positionAttackedBy(getPosition(), getOppositeColor()));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOppositeColor() {
        switch (color) {
        case ChessBoard.WHITE:
            return ChessBoard.BLACK;

        case ChessBoard.BLACK:
            return ChessBoard.WHITE;

        default:
            return -1; // never reached
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BoardPosition getPosition() {
        return position;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     */
    void setPosition(BoardPosition position) {
        this.position = position;
    }

    /**
     * DOCUMENT ME!
     */
    void removeFromBoard() {
        board.setPieceAt(null, getPosition());
        //board.getPieces().remove(this);
        board = null;
        position = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param board DOCUMENT ME!
     * @param position DOCUMENT ME!
     */
    void placeOnBoard(ChessBoard board, BoardPosition position) {
        this.board = board;
        this.position = position;
        //this.board.getPieces().add(this);
        this.board.setPieceAt(this, position);
    }

    /**
     * DOCUMENT ME!
     *
     * @param board DOCUMENT ME!
     */
    protected void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean validTarget(char column, int row) {
        if (!BoardPosition.validPosition(column, row)) {
            return false;
        }

        ChessPiece p = board.getPieceAt(column, row);

        if (p == null) {
            return true;
        }

        if (p.getColor() != color) {
            return true;
        }

        return false;
    }

    /**
     * validTarget() only checks for the very basic; it doesn't check
     * for  pieces in between being non-occupied etc.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean validTarget(BoardPosition p) {
        if (p == null) {
            return false;
        }

        ChessPiece piece = board.getPieceAt(p);

        if (piece == null) {
            return true;
        }

        if (piece.getColor() != color) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean isBishop() {
        return (this instanceof Bishop);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean isKing() {
        return (this instanceof King);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean isKnight() {
        return (this instanceof Knight);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean isPawn() {
        return (this instanceof Pawn);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean isQueen() {
        return (this instanceof Queen);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean isRook() {
        return (this instanceof Rook);
    }

    /*
    * getPieceChar() is redundant to the isKing(), isQueen(), .. functions.
    * It can provide for more convenient implementations through
    * switch statements or game annotations, though.
    */
    public final char getPieceChar() {
        if (this instanceof Bishop) {
            return 'B';
        }

        if (this instanceof King) {
            return 'K';
        }

        if (this instanceof Knight) {
            return 'N';
        }

        if (this instanceof Pawn) {
            return 'P';
        }

        if (this instanceof Queen) {
            return 'Q';
        }

        if (this instanceof Rook) {
            return 'R';
        }

        throw new Error();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "";

        switch (color) {
        case ChessBoard.WHITE:
            s += "white ";

            break;

        case ChessBoard.BLACK:
            s += "black ";

            break;

        default:
            throw new Error();
        }

        switch (getPieceChar()) {
        case 'K':
            s += "King";

            break;

        case 'Q':
            s += "Queen";

            break;

        case 'R':
            s += "Rook";

            break;

        case 'N':
            s += "Knight";

            break;

        case 'B':
            s += "Bishop";

            break;

        case 'P':
            s += "Pawn";

            break;

        default:
            throw new Error();
        }

        if (position == null) {
            s += " off the board";
        } else {
            s += (" at position: " + position.getColumn() + position.getRow());
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    protected Object clone() throws CloneNotSupportedException {
        ChessPiece newPiece = (ChessPiece) super.clone();

        if (position != null) {
            newPiece.position = (BoardPosition) position.clone();
        }

        return newPiece;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        if (color != ((ChessPiece) obj).color) {
            return false;
        }

        // don't really care about position and board right now
        // particularly while comparing move details calling this funciton
        //if (board != ((ChessPiece)obj).board) return false;
        //if (position != ((ChessPiece)obj).position) return false;
        return true;
    }
}
