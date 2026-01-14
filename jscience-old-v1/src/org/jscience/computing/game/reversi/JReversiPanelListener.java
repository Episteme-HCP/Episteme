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

package org.jscience.computing.game.reversi;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JReversiPanelListener extends MouseAdapter {
    /** DOCUMENT ME! */
    JReversi jplay;

/**
     * Creates a new JReversiPanelListener object.
     *
     * @param jplay DOCUMENT ME!
     */
    JReversiPanelListener(JReversi jplay) {
        this.jplay = jplay;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
        ReversiGame game = (ReversiGame) jplay.getAutoPlay().getGame();
        int column = (((int) e.getPoint().getX()) / jplay.panel.tileSize) + 1;
        int row = game.boardHeight -
            (((int) e.getPoint().getY()) / jplay.panel.tileSize);
        ReversiPosition pos = new ReversiPosition(column, row);
        ReversiMove move = new ReversiMove(jplay.getAutoPlay().getGame()
                                                .nextPlayer(), pos,
                game.getBoard());

        if (!jplay.getAutoPlay().getGame().isLegalMove(move)) {
            jplay.getFrame().say("ReversiMove: " + move + " is not legal");

            return;
        }

        jplay.getFrame().say("requesting move: " + move);
        jplay.getFrame().requestGUIMove(move);
    }
}
