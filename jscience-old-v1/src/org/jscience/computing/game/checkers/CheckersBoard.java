/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.checkers;

import org.jscience.mathematics.MathUtils;

import java.io.Serializable;


/**
 * CheckersBoard implements the checkers board which maintains the pieces
 * at their positions. The positions are represented as integers from 11
 * (lower left) to 88 (upper right), where the first digit denotes the column
 * and the second digit denotes the row.
 *
 * @author Holger Antelmann
 */
class CheckersBoard implements Serializable, Cloneable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1014707758098222115L;

    /**
     * individual elements of the array could still be chanced even
     * though it's final; that's why it's protected
     */
    protected static final int[] POSITIONS = new int[] {
            11, 13, 15, 17, 22, 24, 26, 28, 31, 33, 35, 37, 42, 44, 46, 48, 51,
            53, 55, 57, 62, 64, 66, 68, 71, 73, 75, 77, 82, 84, 86, 88
        };

    /** DOCUMENT ME! */
    CheckersPiece[][] board;

/**
     * Creates a new CheckersBoard object.
     */
    public CheckersBoard() {
        board = new CheckersPiece[8][8];
        board[0][0] = new CheckersPiece(0);
        board[2][0] = new CheckersPiece(0);
        board[4][0] = new CheckersPiece(0);
        board[6][0] = new CheckersPiece(0);
        board[1][1] = new CheckersPiece(0);
        board[3][1] = new CheckersPiece(0);
        board[5][1] = new CheckersPiece(0);
        board[7][1] = new CheckersPiece(0);
        board[0][2] = new CheckersPiece(0);
        board[2][2] = new CheckersPiece(0);
        board[4][2] = new CheckersPiece(0);
        board[6][2] = new CheckersPiece(0);
        board[1][5] = new CheckersPiece(1);
        board[3][5] = new CheckersPiece(1);
        board[5][5] = new CheckersPiece(1);
        board[7][5] = new CheckersPiece(1);
        board[0][6] = new CheckersPiece(1);
        board[2][6] = new CheckersPiece(1);
        board[4][6] = new CheckersPiece(1);
        board[6][6] = new CheckersPiece(1);
        board[1][7] = new CheckersPiece(1);
        board[3][7] = new CheckersPiece(1);
        board[5][7] = new CheckersPiece(1);
        board[7][7] = new CheckersPiece(1);
    }

    /**
     * may throw ArrayIndexOutOfBoundsException if position is not
     * valid
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CheckersPiece getPieceAt(int pos) {
        //if (!validPosition(pos)) {
        //    throw new IllegalArgumentException("given position (" + pos + ") not a valid position");
        //}
        return board[(pos / 10) - 1][(pos % 10) - 1];
    }

    /**
     * DOCUMENT ME!
     *
     * @param piece DOCUMENT ME!
     * @param pos DOCUMENT ME!
     */
    protected void setPieceAt(CheckersPiece piece, int pos) {
        //if (!validPosition(pos)) {
        //    throw new IllegalArgumentException ("given position not valid: " + pos);
        //}
        board[(pos / 10) - 1][(pos % 10) - 1] = piece;
    }

    /**
     * includes kings
     *
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPieceCount(int player) {
        int count = 0;

        for (int i = 0; i < POSITIONS.length; i++) {
            CheckersPiece p = getPieceAt(POSITIONS[i]);

            if ((p != null) && (p.getPlayer() == player)) {
                count++;
            }
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKingCount(int player) {
        int count = 0;

        for (int i = 0; i < POSITIONS.length; i++) {
            CheckersPiece p = getPieceAt(POSITIONS[i]);

            if ((p != null) && p.isKing() && (p.getPlayer() == player)) {
                count++;
            }
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public String toString() {
        String s = "\n";

        for (int r = 7; r >= 0; r--) {
            s += ((r + 1) + " ");

            for (int c = 0; c < 8; c++) {
                CheckersPiece p = board[c][r];

                if (p == null) {
                    s += "_ ";
                } else {
                    switch (p.getPlayer()) {
                    case 0:
                        s += ((p.isKing()) ? "X " : "x ");

                        break;

                    case 1:
                        s += ((p.isKing()) ? "O " : "o ");

                        break;

                    default:
                        throw new Error();
                    }
                }
            }

            s += "\n";
        }

        s += "\n  1 2 3 4 5 6 7 8";

        return s;
    }

    /**
     * valid positions are only those that can be occupied by a piece;
     * see the description of this class for how the position is defined
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean validPosition(int pos) {
        // needs to be an element of array POSITIONS,
        // but this is probably faster to calculate
        if ((pos < 11) || (pos > 88)) {
            return false;
        }

        int tmp = pos % 10;

        if ((tmp < 1) || (tmp > 8)) {
            return false;
        }

        if ((MathUtils.sumOfDigits(pos) % 2) != 0) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    protected Object clone() throws CloneNotSupportedException {
        CheckersBoard nb = (CheckersBoard) super.clone();
        nb.board = new CheckersPiece[8][8];

        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                if (board[c][r] != null) {
                    nb.board[c][r] = (CheckersPiece) board[c][r].clone();
                }
            }
        }

        return nb;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof CheckersBoard)) {
            return false;
        }

        CheckersBoard o = (CheckersBoard) obj;

        for (int i = 0; i < POSITIONS.length; i++) {
            CheckersPiece p = getPieceAt(POSITIONS[i]);
            CheckersPiece po = o.getPieceAt(POSITIONS[i]);

            if (p == null) {
                if (po != null) {
                    return false;
                }
            } else {
                if (po == null) {
                    return false;
                }

                if (!po.equals(p)) {
                    return false;
                }
            }
        }

        return true;
    }
}
