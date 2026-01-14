/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
