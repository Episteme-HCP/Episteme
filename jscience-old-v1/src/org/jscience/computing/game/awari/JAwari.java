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
