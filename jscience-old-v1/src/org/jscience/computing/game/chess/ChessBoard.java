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

import org.jscience.computing.game.GameRuntimeException;

import java.io.Serializable;

import java.util.Vector;


/**
 * ChessBoard represents the game status and is contained in a ChessGame,
 * which embedds this ChessBoard to provide all functions needed to play the
 * game.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.chess.ChessGame
 */
class ChessBoard implements Serializable //, Cloneable
 {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 126884074370505822L;

    // constants
    /**
     * DOCUMENT ME!
     */
    public final static int WHITE = 0;

    /**
     * DOCUMENT ME!
     */
    public final static int BLACK = 1;

    // member variables
    /**
     * DOCUMENT ME!
     */
    ChessGame game;

    /**
     * DOCUMENT ME!
     */
    ChessPiece[][] board;

    //transient Vector attackedPositions;
    /**
     * Creates a new ChessBoard object.
     *
     * @param game DOCUMENT ME!
     */
    public ChessBoard(ChessGame game) {
        this.game = game;
        board = new ChessPiece[8][8];

        for (int row = 0; row <= 7; row++) {
            switch (row) {
            case 0:
                board[0][row] = new Rook(ChessBoard.WHITE, this,
                        new BoardPosition('a', row + 1));
                board[1][row] = new Knight(ChessBoard.WHITE, this,
                        new BoardPosition('b', row + 1));
                board[2][row] = new Bishop(ChessBoard.WHITE, this,
                        new BoardPosition('c', row + 1));
                board[3][row] = new Queen(ChessBoard.WHITE, this,
                        new BoardPosition('d', row + 1));
                board[4][row] = new King(ChessBoard.WHITE, this,
                        new BoardPosition('e', row + 1));
                board[5][row] = new Bishop(ChessBoard.WHITE, this,
                        new BoardPosition('f', row + 1));
                board[6][row] = new Knight(ChessBoard.WHITE, this,
                        new BoardPosition('g', row + 1));
                board[7][row] = new Rook(ChessBoard.WHITE, this,
                        new BoardPosition('h', row + 1));

                break;

            case 7:
                board[0][row] = new Rook(ChessBoard.BLACK, this,
                        new BoardPosition('a', row + 1));
                board[1][row] = new Knight(ChessBoard.BLACK, this,
                        new BoardPosition('b', row + 1));
                board[2][row] = new Bishop(ChessBoard.BLACK, this,
                        new BoardPosition('c', row + 1));
                board[3][row] = new Queen(ChessBoard.BLACK, this,
                        new BoardPosition('d', row + 1));
                board[4][row] = new King(ChessBoard.BLACK, this,
                        new BoardPosition('e', row + 1));
                board[5][row] = new Bishop(ChessBoard.BLACK, this,
                        new BoardPosition('f', row + 1));
                board[6][row] = new Knight(ChessBoard.BLACK, this,
                        new BoardPosition('g', row + 1));
                board[7][row] = new Rook(ChessBoard.BLACK, this,
                        new BoardPosition('h', row + 1));

                break;

            case 1:

                for (int column = 0; column <= 7; column++) {
                    board[column][row] = new Pawn(ChessBoard.WHITE, this,
                            new BoardPosition((char) (column + 'a'), row + 1));
                }

                break;

            case 6:

                for (int column = 0; column <= 7; column++) {
                    board[column][row] = new Pawn(ChessBoard.BLACK, this,
                            new BoardPosition((char) (column + 'a'), row + 1));
                }

                break;

            default:

                //for (int column=0; column<=7; column++) {
                //    board[column][row] = null;
                //}
                //break;
            }
        }
    }

/**
     * This constructor is internally used to clone the ChessGame Object.
     */
    ChessBoard(ChessGame game, ChessPiece[] pieces) {
        try {
            this.game = game;
            board = new ChessPiece[8][8];

            /*for (int row=0; row <= 7; row++) {
                for (int column=0; column<=7; column++) {
                    board[column][row] = null;
                }
            }*/
            ChessPiece p;

            for (int i = 0; i < pieces.length; i++) {
                if (pieces[i].getPosition() != null) {
                    p = (ChessPiece) pieces[i].clone();
                    p.placeOnBoard(this, p.getPosition());
                }
            }
        } catch (CloneNotSupportedException e) {
            throw new Error();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTileColor(BoardPosition pos) {
        return pos.getTileColor();
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ChessPiece getPieceAt(BoardPosition position)
        throws IllegalArgumentException {
        return (board[position.getColumn() - 'a'][position.getRow() - 1]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @param rank DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ChessPiece getPieceAt(char file, int rank)
        throws IllegalArgumentException {
        if (BoardPosition.validPosition(file, rank)) {
            if (file < 'X') {
                return (board[file - 'A'][rank - 1]);
            } else {
                return (board[file - 'a'][rank - 1]);
            }
        } else {
            String s = "Position (file: " + file + ", rank: " + rank +
                ") not valid";
            throw new IllegalArgumentException(s);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ChessPiece getPieceAt(int position) {
        if (BoardPosition.validPosition(position)) {
            return (board[((int) (position / 10)) - 1][(position % 10) - 1]);
        } else {
            String s = "Position (" + position + ") not valid";
            throw new IllegalArgumentException(s);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param piece DOCUMENT ME!
     * @param position DOCUMENT ME!
     */
    void setPieceAt(ChessPiece piece, BoardPosition position) {
        board[position.getColumn() - 'a'][position.getRow() - 1] = piece;
    }

    /**
     * DOCUMENT ME!
     *
     * @param piece DOCUMENT ME!
     * @param position DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    void setPieceAt(ChessPiece piece, int position)
        throws IllegalArgumentException {
        if (BoardPosition.validPosition(position)) {
            board[((int) (position / 10)) - 1][(position % 10) - 1] = piece;
        } else {
            throw (new IllegalArgumentException(
                "board coordinates in getPieceAt() are not valid"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param piece DOCUMENT ME!
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private void setPieceAt(ChessPiece piece, char column, int row)
        throws IllegalArgumentException {
        if (BoardPosition.validPosition(column, row)) {
            board[column - 'a'][row - 1] = piece;
        } else {
            throw (new IllegalArgumentException(
                "board coordinates in getPieceAt() are not valid"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean positionAttackedBy(BoardPosition position, int color) {
        ChessPiece[] pieces = getPieces();

        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getColor() == color) {
                if (pieces[i].isPawn()) {
                    // Pawn needs special treatment since possibleTargets()
                    // doesn't reflect whether the Pawn attacks a position
                    // if there is no piece at the given position or
                    // if the piece is right in front of the Pawn.
                    char c;
                    int r;

                    switch (color) {
                    case ChessBoard.WHITE:
                        c = (char) (pieces[i].getPosition().getColumn() + 1);
                        r = pieces[i].getPosition().getRow() + 1;

                        try {
                            if ((BoardPosition.calculateIntPos(c, r) == position.asInteger())) {
                                return true;
                            }
                        } catch (IllegalArgumentException e) {
                        }

                        c = (char) (pieces[i].getPosition().getColumn() - 1);
                        r = pieces[i].getPosition().getRow() + 1;

                        try {
                            if ((BoardPosition.calculateIntPos(c, r) == position.asInteger())) {
                                return true;
                            }
                        } catch (IllegalArgumentException e) {
                        }

                        break;

                    case ChessBoard.BLACK:
                        c = (char) (pieces[i].getPosition().getColumn() + 1);
                        r = pieces[i].getPosition().getRow() - 1;

                        try {
                            if ((BoardPosition.calculateIntPos(c, r) == position.asInteger())) {
                                return true;
                            }
                        } catch (IllegalArgumentException e) {
                        }

                        c = (char) (pieces[i].getPosition().getColumn() - 1);
                        r = pieces[i].getPosition().getRow() - 1;

                        try {
                            if ((BoardPosition.calculateIntPos(c, r) == position.asInteger())) {
                                return true;
                            }
                        } catch (IllegalArgumentException e) {
                        }

                        break;
                    }
                } else {
                    if (pieces[i].possibleTarget(position)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /*
    void initAttackBoard () {
    }
    */
    public King findKing(int color) {
        ChessPiece p;

        for (int row = 1; row <= 8; row++) {
            for (char column = 'a'; column <= 'h'; column++) {
                p = getPieceAt(column, row);

                if ((p != null) && (p.getColor() == color) && (p.isKing())) {
                    return ((King) p);
                }
            }
        }

        String s = "cannot find the King (" + color + ") on board";
        throw new GameRuntimeException(game, s);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChessPiece[] getPieces() {
        Vector<ChessPiece> pieces = new Vector<ChessPiece>();

        for (int row = 1; row <= 8; row++) {
            for (char column = 'a'; column <= 'h'; column++) {
                if (getPieceAt(column, row) != null) {
                    pieces.add(getPieceAt(column, row));
                }
            }
        }

        return pieces.toArray(new ChessPiece[pieces.size()]);
    }

    /**
     * does some basic checking on the validity of the board; should be
     * used after a board was set up manually
     *
     * @return DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public boolean isValid() {
        ChessPiece[] pieces = getPieces();
        int wk = 0;
        int bk = 0;

        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].isKing()) {
                switch (pieces[i].getColor()) {
                case 0:
                    wk++;

                    break;

                case 1:
                    bk++;

                    break;

                default:
                    throw new Error();
                }
            }
        }

        if (!((wk == 1) && (bk == 1))) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ChessGame getGame() {
        return game;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        //doesn't support colors
        ChessPiece p = null;
        String s = "\nChess Board:\n\n";

        for (int row = 8; row >= 1; row--) {
            s += (" " + row + " ");

            for (char column = 'a'; column <= 'h'; column++) {
                p = getPieceAt(column, row);

                if (p == null) {
                    s += "-- ";
                } else {
                    switch (p.getColor()) {
                    case WHITE:
                        s += '>';

                        break;

                    case BLACK:
                        s += '<';

                        break;

                    default:
                    }

                    s += (p.getPieceChar() + " ");
                }
            }

            s += "\n";
        }

        s += "\n";
        s += "   A  B  C  D  E  F  G  H  \n";

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
        throw new CloneNotSupportedException();
    }

    /**
     * returns some number based on the pieces placed on their
     * positions; no guarantees on whether this provides a good distribution.
     * Here, a lot of testing will be required.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int hash = 0;
        int count = 0;

        for (int row = 1; row <= 8; row++) {
            for (char column = 'a'; column <= 'h'; column++) {
                count++;

                ChessPiece p = getPieceAt(column, row);

                if (p != null) {
                    switch (p.getPieceChar()) {
                    case 'P':
                        hash = hash + (count * 7);

                        break;

                    case 'R':
                        hash = hash + (count * 11);

                        break;

                    case 'N':
                        hash = hash + (count * 13);

                        break;

                    case 'B':
                        hash = hash + (count * 17);

                        break;

                    case 'Q':
                        hash = hash + (count * 23);

                        break;

                    case 'K':
                        hash = hash + (count * 29);

                        break;
                    }
                }
            }
        }

        return hash;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        ChessBoard b = (ChessBoard) obj;

        for (int row = 1; row <= 8; row++) {
            for (char column = 'a'; column <= 'h'; column++) {
                if (getPieceAt(column, row) == null) {
                    if (b.getPieceAt(column, row) != null) {
                        return false;
                    }
                } else {
                    if (b.getPieceAt(column, row) == null) {
                        return false;
                    }

                    if (!getPieceAt(column, row)
                                 .equals(b.getPieceAt(column, row))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
