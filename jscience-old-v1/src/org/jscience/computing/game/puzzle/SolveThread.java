/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.GameUtils;

import org.jscience.util.Monitor;


/**
 * A game solver that tries to solve a game by search for a winning
 * position by brute force when running; can be controlled externally with a
 * given monitor.
 *
 * @author Holger Antelmann
 */
class SolveThread extends Thread {
    /** DOCUMENT ME! */
    GamePlay game;

    /** DOCUMENT ME! */
    Monitor monitor;

    /** DOCUMENT ME! */
    int number;

/**
     * Creates a new SolveThread object.
     *
     * @param game    DOCUMENT ME!
     * @param number  DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     */
    SolveThread(GamePlay game, int number, Monitor monitor) {
        super("JSolitaire SolveThread");
        this.game = game;
        this.number = number;
        this.monitor = monitor;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        GamePlay solved = GameUtils.depthFirstSearch(game, new int[] { 0 },
                number, monitor);
        monitor.setObject(solved);
        monitor.test = true;

        return;
    }
}
