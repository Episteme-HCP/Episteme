/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import org.jscience.computing.game.GameBoardPosition;

import org.jscience.mathematics.MathUtils;


/**
 * BoardPosition represents a position on a chess board. This class is also
 * used by other game packages and therefore public.
 *
 * @author Holger Antelmann
 */
public class BoardPosition extends GameBoardPosition {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -5672381258284361546L;

/**
     * Creates a new BoardPosition object.
     *
     * @param file DOCUMENT ME!
     * @param rank DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public BoardPosition(char file, int rank) throws IllegalArgumentException {
        super(calculateIntPos(file, rank));
    }

/**
     * Creates a new BoardPosition object.
     *
     * @param pos DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public BoardPosition(int pos) throws IllegalArgumentException {
        super(pos);

        if (!validPosition(pos)) {
            String text = "integer value representing a ChessBoardPosition not valid int the constructor";
            text = text + " (given integer: " + pos + ")";
            throw (new IllegalArgumentException(text));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTileColor() {
        if ((MathUtils.sumOfDigits(asInteger()) % 2) == 0) {
            return ChessBoard.BLACK;
        } else {
            return ChessBoard.WHITE;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = String.valueOf(getColumn()) + getRow();

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public char getFile() {
        return getColumn();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRank() {
        return getRow();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected char getColumn() {
        return ((char) (('a' + ((int) (asInteger() / 10))) - 1));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int getRow() {
        return ((asInteger() % 10));
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @param rank DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean validPosition(char file, int rank) {
        if ((rank < 1) || (rank > 8)) {
            return false;
        }

        if ((file >= 'a') && (file <= 'h')) {
            return true;
        }

        if ((file >= 'A') && (file <= 'H')) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean validPosition(int pos) {
        if ((pos < 11) || (pos > 88)) {
            return false;
        }

        int tmp = pos % 10;

        if ((tmp < 1) || (tmp > 8)) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileAddition DOCUMENT ME!
     * @param rankAddition DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BoardPosition relativePosition(int fileAddition, int rankAddition) {
        int newPos = asInteger() + (10 * fileAddition) + rankAddition;

        try {
            BoardPosition p = new BoardPosition(newPos);

            return p;
        } catch (IllegalArgumentException e) {
            return null;
        }
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
    protected static int calculateIntPos(char file, int rank)
        throws IllegalArgumentException {
        if (validPosition(file, rank)) {
            int pos = rank;

            if (file <= 'H') {
                pos = pos + (10 * (file - 'A' + 1));
            } else {
                pos = pos + (10 * (file - 'a' + 1));
            }

            return (pos);
        } else {
            String text = "values for a (Chess)BoardPosition not valid (given column: " +
                file + ", row: " + rank + ")";
            throw (new IllegalArgumentException(text));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
