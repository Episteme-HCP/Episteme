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
