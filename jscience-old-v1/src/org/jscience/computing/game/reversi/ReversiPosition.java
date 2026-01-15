/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.reversi;

import org.jscience.computing.game.GameBoardPosition;


/**
 * represents a board position in the ReversiGame
 *
 * @author Holger Antelmann
 *
 * @see ReversiGame
 */
class ReversiPosition extends GameBoardPosition {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -8086896213371599811L;

/**
     * Creates a new ReversiPosition object.
     *
     * @param column DOCUMENT ME!
     * @param row    DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ReversiPosition(int column, int row) throws IllegalArgumentException {
        this(calculateIntPos(column, row), true);
    }

/**
     * Creates a new ReversiPosition object.
     *
     * @param pos DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ReversiPosition(int pos) throws IllegalArgumentException {
        super(pos);

        if (!isValidPosition(pos)) {
            String text = "integer value representing a org.jscience.computing.game.reversi.ReversiPosition not valid int the constructor";
            text = text + " (given integer: " + pos + ")";
            throw (new IllegalArgumentException(text));
        }
    }

/**
     * Creates a new ReversiPosition object.
     *
     * @param pos   DOCUMENT ME!
     * @param valid DOCUMENT ME!
     */
    ReversiPosition(int pos, boolean valid) {
        super(pos);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (getColumn() + "/" + getRow());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getColumn() {
        return ((int) (asInteger() / 10));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getRow() {
        return ((asInteger() % 10));
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static boolean isValidPosition(int column, int row) {
        if ((row < 1) || (row > 8) || (column < 1) || (column > 8)) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static boolean isValidPosition(int pos) {
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
     * @param columnAddition DOCUMENT ME!
     * @param rowAddition DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ReversiPosition relativePosition(int columnAddition, int rowAddition) {
        ReversiPosition temp;
        int newPos = asInteger() + (10 * columnAddition) + rowAddition;

        try {
            temp = new ReversiPosition(newPos);
        } catch (IllegalArgumentException e) {
            temp = null;
        }

        return (temp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    protected static int calculateIntPos(int column, int row)
        throws IllegalArgumentException {
        if (isValidPosition(column, row)) {
            return ((10 * column) + row);
        } else {
            String text = "values for a org.jscience.computing.game.reversi.ReversiPosition not valid (given column: " +
                column + ", row: " + row + ")";
            throw (new IllegalArgumentException(text));
        }
    }
}
