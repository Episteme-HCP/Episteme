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

import org.jscience.computing.game.GameBoardMove;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class ReversiMove extends GameBoardMove {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 3280592908714193286L;

/**
     * Creates a new ReversiMove object.
     *
     * @param player   DOCUMENT ME!
     * @param position DOCUMENT ME!
     */
    public ReversiMove(int player, ReversiPosition position) {
        super(player, position);
    }

/**
     * Creates a new ReversiMove object.
     *
     * @param player   DOCUMENT ME!
     * @param position DOCUMENT ME!
     * @param board    DOCUMENT ME!
     */
    ReversiMove(int player, ReversiPosition position, int[][] board) {
        this(player, position);

        int[][] bcopy = new int[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                bcopy[i][j] = board[i][j];
            }
        }

        moveOption = bcopy;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = (getPlayer() == 0) ? "X" : "O";
        s += (": " + getPosition().asInteger());

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[][] getBoard() {
        return ((int[][]) moveOption);
    }

    /**
     * overwritten to ignore moveOption (the int[][] board in this
     * case) and positionFrom
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof ReversiMove)) {
            return false;
        }

        ReversiMove m = (ReversiMove) obj;

        if (playerRole != m.playerRole) {
            return false;
        }

        if (positionTo == null) {
            if (m.positionTo != null) {
                return false;
            }
        } else {
            if (!m.positionTo.equals(positionTo)) {
                return false;
            }
        }

        return true;
    }
}
