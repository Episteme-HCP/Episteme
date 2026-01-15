/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import java.io.Serializable;


/**
 * GameBoardPosition implements a board position that can be used for
 * various board game implementations. It is also used in the GameBoardMove.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.GameBoardMove
 */
public class GameBoardPosition implements Serializable, Cloneable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1175207030136507332L;

    /** DOCUMENT ME! */
    private int position;

/**
     * Creates a new GameBoardPosition object.
     *
     * @param position DOCUMENT ME!
     */
    public GameBoardPosition(int position) {
        this.position = position;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int asInteger() {
        return position;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ("GameBoardPosition: " + position);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        //if (obj.getClass() != getClass()) return false;
        return (((GameBoardPosition) obj).position == position);

        /*try {
            return (((GameBoardPosition)obj).asInteger() == position);
        } catch (ClassCastException e) {
            return false;
        }*/
    }

    /*
    protected Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
    */
}
