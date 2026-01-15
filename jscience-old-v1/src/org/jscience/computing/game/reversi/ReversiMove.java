/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
