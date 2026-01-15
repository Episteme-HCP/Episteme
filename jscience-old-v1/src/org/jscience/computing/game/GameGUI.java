/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import java.awt.*;


/**
 * The GameGUI interface is implemented by objects that use the JGamePlay
 * interface to play a game.
 *
 * @author Holger Antelmann
 * @see JGamePlay
 */
public interface GameGUI {
    /**
     * returns the root container itself
     *
     * @return DOCUMENT ME!
     */
    Frame getFrame();

    /**
     * returns the embedded JGamePlay object
     *
     * @return DOCUMENT ME!
     */
    JGamePlay getJGamePlay();

    /**
     * to update the GUI
     */
    public void repaint();

    /**
     * requestGUIMove() is called when a JGamePlay container registered
     * a move to be made throug the GUI; this move is then passed to the game
     * playing frame through this method - allowing the main frame to decide
     * what is to be done
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean requestGUIMove(GameMove move);

    /**
     * requestGUIRedoMove() is when a GUI component requested to undo a
     * move
     *
     * @return DOCUMENT ME!
     */
    boolean requestGUIUndoMove();

    /**
     * requestGUIRedoMove() is when a GUI component requested to redo a
     * move
     *
     * @return DOCUMENT ME!
     */
    boolean requestGUIRedoMove();

    /**
     * allows to send a message to either the console or some place
     * within the GUI to be displayed at the GUI's discretion
     *
     * @param message DOCUMENT ME!
     */
    void say(String message);
}
