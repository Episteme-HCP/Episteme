/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

/**
 * An Exception that is thrown if a Player cannot play a given game
 * implementation
 *
 * @author Holger Antelmann
 */
public class CannotPlayGameException extends GameRuntimeException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -7825468847574732784L;

    /** DOCUMENT ME! */
    Player player;

    /** DOCUMENT ME! */
    GamePlay game;

/**
     * Creates a new CannotPlayGameException object.
     *
     * @param player     DOCUMENT ME!
     * @param game       DOCUMENT ME!
     * @param customText DOCUMENT ME!
     */
    public CannotPlayGameException(Player player, GamePlay game,
        String customText) {
        super(game,
            "The player " + player.getPlayerName() + " (" + player.getClass() +
            ")" + " cannot play the game " + game.getClass().getName() +
            "; more info: " + customText);
        this.player = player;
        this.game = game;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Player getPlayer() {
        return player;
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
