/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

/**
 * A specialized GameMove class suitable for board games to implement moves
 * of pieces from and to positions on a board.
 *
 * @author Holger Antelmann
 */
public class GameBoardMove implements GameMove, Cloneable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 3916390724999500091L;

    /** DOCUMENT ME! */
    protected Object moveOption;

    /** DOCUMENT ME! */
    protected int playerRole;

    /** DOCUMENT ME! */
    protected GameBoardPosition positionTo;

    /** DOCUMENT ME! */
    protected GameBoardPosition positionFrom;

/**
     * Creates a new GameBoardMove object.
     *
     * @param playerRole DOCUMENT ME!
     */
    private GameBoardMove(int playerRole) {
        this.playerRole = playerRole;
    }

/**
     * Creates a new GameBoardMove object.
     *
     * @param playerRole DOCUMENT ME!
     * @param option     DOCUMENT ME!
     */
    private GameBoardMove(int playerRole, Object option) {
        this.playerRole = playerRole;
        this.moveOption = option;
    }

/**
     * Creates a new GameBoardMove object.
     *
     * @param playerRole DOCUMENT ME!
     * @param position   DOCUMENT ME!
     */
    public GameBoardMove(int playerRole, GameBoardPosition position) {
        this.playerRole = playerRole;
        positionTo = position;
    }

/**
     * Creates a new GameBoardMove object.
     *
     * @param playerRole DOCUMENT ME!
     * @param position   DOCUMENT ME!
     * @param option     DOCUMENT ME!
     */
    public GameBoardMove(int playerRole, GameBoardPosition position,
        Object option) {
        this.playerRole = playerRole;
        this.moveOption = option;
        positionTo = position;
    }

/**
     * Creates a new GameBoardMove object.
     *
     * @param playerRole  DOCUMENT ME!
     * @param oldPosition DOCUMENT ME!
     * @param newPosition DOCUMENT ME!
     */
    public GameBoardMove(int playerRole, GameBoardPosition oldPosition,
        GameBoardPosition newPosition) {
        this.playerRole = playerRole;
        positionFrom = oldPosition;
        positionTo = newPosition;
    }

/**
     * Creates a new GameBoardMove object.
     *
     * @param playerRole  DOCUMENT ME!
     * @param oldPosition DOCUMENT ME!
     * @param newPosition DOCUMENT ME!
     * @param option      DOCUMENT ME!
     */
    public GameBoardMove(int playerRole, GameBoardPosition oldPosition,
        GameBoardPosition newPosition, Object option) {
        this.playerRole = playerRole;
        this.moveOption = option;
        positionFrom = oldPosition;
        positionTo = newPosition;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPlayer() {
        return playerRole;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getOption() {
        return moveOption;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GameBoardPosition getPosition() {
        if ((positionFrom == null)) {
            return positionTo;
        } else {
            String s = "getPosition() is ambivalent since there";
            s += " is more than one Position in this instance";
            throw (new UnsupportedOperationException(s));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GameBoardPosition getNewPosition() {
        return positionTo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GameBoardPosition getOldPosition() {
        return positionFrom;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "GameBoardMove Player Role: " + getPlayer();

        if (positionFrom != null) {
            s += (" from: " + positionFrom);
        }

        if (positionTo != null) {
            s += (" to: " + positionTo);
        }

        if (getOption() != null) {
            s += getOption().toString();
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof GameBoardMove)) {
            return false;
        }

        GameBoardMove m = (GameBoardMove) obj;

        if (playerRole != m.playerRole) {
            return false;
        }

        if (moveOption == null) {
            if (m.moveOption != null) {
                return false;
            }
        } else {
            if (m.moveOption == null) {
                return false;
            }

            if (!m.moveOption.equals(moveOption)) {
                return false;
            }
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

        if (positionFrom == null) {
            if (m.positionFrom != null) {
                return false;
            }
        } else {
            if (!m.positionFrom.equals(positionFrom)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Due to protected access of Object.clone() and the Object type of
     * moveOption, this clone() function does not clone the moveOption, but
     * only references the same object is referenced from the original
     * GameMove. Thus, one should be aware that GameMove.clone() only does a
     * shallow copy! The embedded GameBoardPositions are only shallow-copied
     * because they cannot be changed after instanciation, i.e. it should not
     * harm things; if derived classes are used, though, that may chance of
     * course. Note: as a resolution, an overwriting clone() function that
     * knows the type of the moveOption could cast the Object type of the
     * moveOption to something where the clone() function of that object is
     * accessible for a deep copy; this overwriting method could also take
     * care of cloning the GameBoardPositions if needed (in case subclasses
     * are used there, too).
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
