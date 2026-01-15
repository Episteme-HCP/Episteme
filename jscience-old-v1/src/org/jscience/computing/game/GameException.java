/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

/**
 * thrown when game related exceptions are raised that should be caught
 *
 * @author Holger Antelmann
 *
 * @see GameRuntimeException
 */
public class GameException extends Exception {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2699881463251280747L;

    /** DOCUMENT ME! */
    GamePlay game;

/**
     * Creates a new GameException object.
     */
    public GameException() {
        super();
    }

/**
     * Creates a new GameException object.
     *
     * @param text DOCUMENT ME!
     */
    public GameException(String text) {
        this(null, text);
    }

/**
     * Creates a new GameException object.
     *
     * @param game DOCUMENT ME!
     * @param text DOCUMENT ME!
     */
    public GameException(GamePlay game, String text) {
        super(text);
        this.game = game;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GamePlay getGame() {
        return game;
    }
}
