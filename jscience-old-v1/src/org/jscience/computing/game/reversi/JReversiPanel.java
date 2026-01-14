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

import java.awt.*;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JReversiPanel extends JPanel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -4609157992020046069L;

    /** DOCUMENT ME! */
    JReversi jplay;

    /** DOCUMENT ME! */
    int tileSize = 33;

    /** DOCUMENT ME! */
    int tileInset = 1;

    /** DOCUMENT ME! */
    Color colorBackground = Color.blue;

    /** DOCUMENT ME! */
    Color colorX = Color.yellow;

    /** DOCUMENT ME! */
    Color colorO = Color.red;

    /** DOCUMENT ME! */
    Color colorNone = Color.lightGray;

    /** DOCUMENT ME! */
    Color colorLegal = Color.black;

/**
     * Creates a new JReversiPanel object.
     *
     * @param jplay DOCUMENT ME!
     */
    JReversiPanel(JReversi jplay) {
        this.jplay = jplay;
        setTileSize(tileSize, tileInset);
        addComponentListener(new ReversiSizer(this));
    }

    /**
     * DOCUMENT ME!
     *
     * @param tileSize DOCUMENT ME!
     * @param tileInset DOCUMENT ME!
     */
    public void setTileSize(int tileSize, int tileInset) {
        this.tileSize = tileSize;
        this.tileInset = tileInset;

        ReversiGame game = (ReversiGame) jplay.getAutoPlay().getGame();
        setSize(tileSize * game.boardWidth, tileSize * game.boardHeight);
        setPreferredSize(getSize());
        setBackground(colorBackground);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ReversiGame game = (ReversiGame) jplay.getAutoPlay().getGame();

        for (int c = 0; c < game.boardWidth; c++) {
            for (int r = 0; r < game.boardHeight; r++) {
                switch (game.getBoard()[c][r]) {
                case 0:
                    g.setColor(colorX);

                    break;

                case 1:
                    g.setColor(colorO);

                    break;

                default:

                    if (jplay.showLegalMoves.isSelected() &&
                            game.isLegalMove(
                                new ReversiMove(game.nextPlayer(),
                                    new ReversiPosition(c + 1, r + 1)))) {
                        g.setColor(colorLegal);

                        break;
                    } else {
                        g.setColor(colorNone);

                        break;
                    }
                }

                g.fillOval((c * tileSize) + tileInset,
                    (tileSize * (game.boardHeight - 1)) - (r * tileSize) +
                    tileInset, tileSize - (tileInset * 2),
                    tileSize - (tileInset * 2));
            }
        }
    }
}
