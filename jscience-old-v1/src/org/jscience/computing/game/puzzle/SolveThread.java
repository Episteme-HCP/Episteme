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
