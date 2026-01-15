/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.GameBoardPosition;


/**
 * the class SolitairePosition represents a game position in the game of
 * Solitaire. The representation is an integer based on the following little
 * ASCII graphic:<br><pre>11 21 31 41 51 61 7112 22 32 42 52 62 72
 * 13 23 33 43 53 63 7314 24 34 44 54 64 7415 25 35 45 55 65 75
 * 16 26 36 46 56 66 7617 27 37 47 57 67 77</pre>and the following
 * positions are off the board:
 * <pre> 11 12 21 22 16 17 26 27 61 62 71 72 66 67 76 77 </pre>
 *
 * @author Holger Antelmann
 */
class SolitairePosition extends GameBoardPosition {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -5798544670693239732L;

/**
     * Creates a new SolitairePosition object.
     *
     * @param position DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public SolitairePosition(int position) throws IllegalArgumentException {
        super(position);

        if (!isValidPosition(position)) {
            String s = "parameters not valid to represent a SolitairePosition";
            s += (" (given position: " + position + ")");
            throw (new IllegalArgumentException(s));
        }
    }

/**
     * Creates a new SolitairePosition object.
     *
     * @param column DOCUMENT ME!
     * @param row    DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public SolitairePosition(int column, int row)
        throws IllegalArgumentException {
        super((column * 10) + row);

        if (!isValidPosition(column, row)) {
            String s = "parameters not valid to represent a SolitairePosition";
            s += (" (given column: " + column + " given row: " + row + ")");
            throw (new IllegalArgumentException(s));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (String.valueOf(getColumn() + "" + getRow()));
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
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static boolean isValidPosition(int pos) {
        if ((pos < 13) || (pos > 75)) {
            return false;
        }

        return isValidPosition(pos / 10, pos % 10);
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
        if ((row < 1) || (row > 7) || (column < 1) || (column > 7)) {
            return false;
        }

        if ((row < 3) && ((column < 3) || (column > 5))) {
            return false;
        }

        if ((row > 5) && ((column < 3) || (column > 5))) {
            return false;
        }

        return true;
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
    public static int calculateIntPos(int column, int row)
        throws IllegalArgumentException {
        if (isValidPosition(column, row)) {
            return ((10 * column) + row);
        } else {
            String s = "parameters not valid to represent a SolitairePosition";
            s += (" (given column: " + column + " given row: " + row + ")");
            throw (new IllegalArgumentException(s));
        }
    }
}
