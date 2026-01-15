/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.mine;

import org.jscience.computing.game.GameMove;


/**
 * MineMove implements a GameMove for the game MineSweeper.
 *
 * @author Holger Antelmann
 *
 * @see MineSweeper
 */
class MineMove implements GameMove {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 2061196597107578019L;

    /** DOCUMENT ME! */
    int column;

    /** DOCUMENT ME! */
    int row;

/**
     * Creates a new MineMove object.
     *
     * @param column DOCUMENT ME!
     * @param row    DOCUMENT ME!
     */
    public MineMove(int column, int row) {
        this.column = column;
        this.row = row;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPlayer() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumn() {
        return column;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRow() {
        return row;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof MineMove)) {
            return false;
        }

        MineMove m = (MineMove) obj;

        if (column != m.column) {
            return false;
        }

        if (row != m.row) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return column + "/" + row;
    }
}
