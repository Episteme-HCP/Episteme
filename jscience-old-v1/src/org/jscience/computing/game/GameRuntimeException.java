/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

/**
 * The GameRuntimeException provides access to the GamePlay object that is
 * associated to the exception, so that Exception handing code can take
 * advantage of it.
 *
 * @author Holger Antelmann
 *
 * @see GameException
 */
public class GameRuntimeException extends RuntimeException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -5620229047407791305L;

    /** DOCUMENT ME! */
    GamePlay game;

/**
     * Creates a new GameRuntimeException object.
     */
    public GameRuntimeException() {
        super();
    }

/**
     * Creates a new GameRuntimeException object.
     *
     * @param text DOCUMENT ME!
     */
    public GameRuntimeException(String text) {
        super(text);
    }

/**
     * Creates a new GameRuntimeException object.
     *
     * @param text  DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public GameRuntimeException(String text, Throwable cause) {
        super(text, cause);
    }

/**
     * Creates a new GameRuntimeException object.
     *
     * @param game      DOCUMENT ME!
     * @param errorText DOCUMENT ME!
     */
    public GameRuntimeException(GamePlay game, String errorText) {
        super(errorText);
        this.game = game;
    }

/**
     * Creates a new GameRuntimeException object.
     *
     * @param game      DOCUMENT ME!
     * @param errorText DOCUMENT ME!
     * @param cause     DOCUMENT ME!
     */
    public GameRuntimeException(GamePlay game, String errorText, Throwable cause) {
        super(errorText, cause);
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
