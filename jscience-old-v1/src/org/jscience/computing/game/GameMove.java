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
