/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.MoveTemplate;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class TilePuzzleMove extends MoveTemplate {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -3188510788619077146L;

    // values are picked so that the sum of two moves
    // that negate each other is zero
    // (this feature is used in the TilePuzzlePlayer heuristic)
    /** DOCUMENT ME! */
    public static final int UP = 1;

    /** DOCUMENT ME! */
    public static final int DOWN = -1;

    /** DOCUMENT ME! */
    public static final int LEFT = 2;

    /** DOCUMENT ME! */
    public static final int RIGHT = -2;

    /** DOCUMENT ME! */
    private int type;

/**
     * Creates a new TilePuzzleMove object.
     *
     * @param type DOCUMENT ME!
     */
    public TilePuzzleMove(int type) {
        super(0);
        this.type = type;

        switch (type) {
        case UP:
        case DOWN:
        case LEFT:
        case RIGHT:
            break;

        default:
            throw new IllegalArgumentException("type not supported");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (TilePuzzleMove.class != obj.getClass()) {
            return false;
        }

        if (getType() != ((TilePuzzleMove) obj).getType()) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public String toString() {
        switch (type) {
        case UP:
            return "up";

        case DOWN:
            return "down";

        case LEFT:
            return "left";

        case RIGHT:
            return "right";

        default:
            throw new Error();
        }
    }
}
