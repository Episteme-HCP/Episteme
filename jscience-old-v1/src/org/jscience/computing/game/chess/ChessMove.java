/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import org.jscience.computing.game.GameBoardMove;
import org.jscience.computing.game.GameRuntimeException;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class ChessMove extends GameBoardMove {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 8163226662776124033L;

    // constats
    /** DOCUMENT ME! */
    public static final char NO_VALID_ROW = 'n';

    // member variables
    /** DOCUMENT ME! */
    private String annotation;

    /** DOCUMENT ME! */
    private boolean check;

    /** DOCUMENT ME! */
    private char pieceChar;

    /** DOCUMENT ME! */
    private int moveNumber;

/**
     * Creates a new ChessMove object.
     *
     * @param piece DOCUMENT ME!
     * @param to    DOCUMENT ME!
     */
    ChessMove(ChessPiece piece, BoardPosition to) {
        super(piece.getColor(), piece.getPosition(), to,
            determineDetails(piece, to));
        init(piece, to);
    }

/**
     * Creates a new ChessMove object.
     *
     * @param piece      DOCUMENT ME!
     * @param to         DOCUMENT ME!
     * @param promotedTo DOCUMENT ME!
     */
    ChessMove(ChessPiece piece, BoardPosition to, ChessPiece promotedTo) {
        super(piece.getColor(), piece.getPosition(), to,
            determineDetails(piece, to, promotedTo));
        init(piece, to);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChessPiece getCaptured() {
        if (getOption() != null) {
            return (((MoveDetail) getOption()).getCaptured());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param piece DOCUMENT ME!
     * @param to DOCUMENT ME!
     */
    private void init(ChessPiece piece, BoardPosition to) {
        moveNumber = piece.getBoard().getGame().numberOfMoves();

        if (piece.getBoard().getGame().blackMovesFirst) {
            moveNumber++;
        }

        pieceChar = piece.getPieceChar();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChessPiece getPromotion() {
        if (getOption() != null) {
            return (((MoveDetail) getOption()).getPromotion());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean check() {
        return check;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public char getPieceChar() {
        return pieceChar;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCastling() {
        return ((pieceChar == 'K') &&
        (Math.abs(((BoardPosition) getOldPosition()).getColumn() -
            ((BoardPosition) getNewPosition()).getColumn()) == 2));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnpassant() {
        return ((pieceChar == 'P') &&
        (((BoardPosition) getOldPosition()).getRow() == ((BoardPosition) getNewPosition()).getRow()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param check DOCUMENT ME!
     */
    void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * DOCUMENT ME!
     *
     * @param piece DOCUMENT ME!
     * @param to DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static MoveDetail determineDetails(ChessPiece piece,
        BoardPosition to) {
        ChessPiece p = piece.getBoard().getPieceAt(to);

        if (p == null) {
            return null;
        }

        return (new MoveDetail(p));
    }

    /**
     * DOCUMENT ME!
     *
     * @param piece DOCUMENT ME!
     * @param to DOCUMENT ME!
     * @param promoted DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static MoveDetail determineDetails(ChessPiece piece,
        BoardPosition to, ChessPiece promoted) {
        return (new MoveDetail(piece.getBoard().getPieceAt(to), promoted));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        ChessMove m = (ChessMove) super.clone();

        if (getOption() != null) {
            try {
                m.moveOption = ((MoveDetail) getOption()).clone();
            } catch (ClassCastException e) {
                String s = "caught ClassCastException in org.jscience.computing.game.chess.ChessMove.clone()\n";
                s += "The moveOption in this ChessMove instance is not a MoveDetail object, but a";
                s += getOption().getClass().getName();
                e.printStackTrace();
                throw (new GameRuntimeException(null, s));
            }
        }

        return m;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "";
        s += (((moveNumber + 2) / 2) + ".");
        s += ((((moveNumber + 2) % 2) == 0) ? " " : ". ");

        //switch (getPlayer()) {
        //    case ChessBoard.WHITE:
        //        s += "white: ";
        //        break;
        //    case ChessBoard.BLACK:
        //        s += "black: ";
        //        break;
        //}
        if ((pieceChar == 'K') &&
                ((((BoardPosition) getOldPosition()).getColumn() -
                ((BoardPosition) getNewPosition()).getColumn()) == 2)) {
            s += "O-O-O";
        } else if ((pieceChar == 'K') &&
                ((((BoardPosition) getOldPosition()).getColumn() -
                ((BoardPosition) getNewPosition()).getColumn()) == -2)) {
            s += "O-O";
        } else {
            if (pieceChar != 'P') {
                s += pieceChar;
            }

            s += (new BoardPosition(getOldPosition().asInteger()));

            if (getCaptured() == null) {
                s += "-";
            } else {
                s += "x";
            }

            if (isEnpassant()) {
                s += ((BoardPosition) getNewPosition()).getColumn();

                switch (getPlayer()) {
                case ChessBoard.WHITE:
                    s += (((BoardPosition) getNewPosition()).getRow() + 1);

                    break;

                case ChessBoard.BLACK:
                    s += (((BoardPosition) getNewPosition()).getRow() - 1);

                    break;
                }
            } else {
                s += getNewPosition();
            }
        }

        if (getPromotion() != null) {
            s += getPromotion().getPieceChar();
        }

        if (check) {
            s += "+";
        }

        return s;
    }
}
