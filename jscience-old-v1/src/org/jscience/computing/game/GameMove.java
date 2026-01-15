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
 * GameMove provides the necessary methods that allow a <code>GamePlay</code>
 * object to handle a game move properly. The only method defined is a method
 * to retrieve the game role associated with the move, as every move will have
 * to be played by a particular player. GameMove represents a move that can be
 * applied to a GamePlay object. GameMove extends Serializable to ensure that
 * a game and its moves can be used over e.g. an Internet connection or can be
 * written to a file. It is advised to also implement a useful
 * <code>toString()</code> method, so that the moves will be displayed
 * properly.
 *
 * @author Holger Antelmann
 * @see GamePlay
 * @see MoveTemplate
 */
public interface GameMove extends Serializable {
    /**
     * returns the game player role that plays this move
     *
     * @see org.jscience.computing.game.GamePlay#numberOfPlayers()
     */
    public int getPlayer();
}
