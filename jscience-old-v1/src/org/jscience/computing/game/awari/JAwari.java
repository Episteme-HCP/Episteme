/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.awari;

import org.jscience.computing.game.JDefaultGame;

import org.jscience.io.ExtensionFileFilter;


/**
 * JAwari can play the AwariGame in the JGameFrame GUI. It pretty much adds
 * nothing to the JDefaultGame, except it provides proper AwariPlayers by
 * default.
 *
 * @author Holger Antelmann
 *
 * @see AwariGame
 * @see org.jscience.computing.game.JGameFrame
 */
public class JAwari extends JDefaultGame {
/**
     * Creates a new JAwari object.
     */
    public JAwari() {
        super(new AwariGame(),
            new AwariPlayer[] {
                new AwariPlayer("player0"), new AwariPlayer("player1")
            }, 9, new ExtensionFileFilter("awi", "Awari games (*.awi)"));
        ((AwariPlayer) getAutoPlay().getPlayer(0)).setRandomSeed(System.currentTimeMillis());
        ((AwariPlayer) getAutoPlay().getPlayer(1)).setRandomSeed(System.currentTimeMillis());
    }
}
