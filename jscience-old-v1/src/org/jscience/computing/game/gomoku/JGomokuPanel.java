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

package org.jscience.computing.game.gomoku;

import java.awt.*;

import javax.swing.*;


/**
 * JGomokuPanel provides a Java Swing container to be used with JGomoku
 *
 * @author Holger Antelmann
 *
 * @see JGomoku
 */
class JGomokuPanel extends JPanel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4230160414557255311L;

    /** DOCUMENT ME! */
    JGomoku jplay;

    /** DOCUMENT ME! */
    int tileSize = 20;

    /** DOCUMENT ME! */
    int tileInset = 1;

    /** DOCUMENT ME! */
    Color colorBackground = Color.gray;

    /** DOCUMENT ME! */
    Color colorX = Color.black;

    /** DOCUMENT ME! */
    Color colorO = Color.white;

    /** DOCUMENT ME! */
    Color colorNone = Color.lightGray;

/**
     * Creates a new JGomokuPanel object.
     *
     * @param jplay DOCUMENT ME!
     */
    JGomokuPanel(JGomoku jplay) {
        this.jplay = jplay;
        setTileSize(tileSize, tileInset);
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

        GomokuGame game = (GomokuGame) jplay.getAutoPlay().getGame();
        setSize(tileSize * game.width, tileSize * game.height);
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

        GomokuGame game = (GomokuGame) jplay.getAutoPlay().getGame();

        for (int c = 0; c < game.width; c++) {
            g.drawLine(((c * tileSize) + (tileSize / 2)), 0,
                ((c * tileSize) + (tileSize / 2)), (tileSize * game.getWidth()));
        }

        for (int r = 0; r < game.width; r++) {
            g.drawLine(0, ((r * tileSize) + (tileSize / 2)),
                (tileSize * game.getHeight()), ((r * tileSize) +
                (tileSize / 2)));
        }

        for (int c = 0; c < game.width; c++) {
            for (int r = 0; r < game.height; r++) {
                switch (game.getValueAt(c, r)) {
                case 0:
                    g.setColor(colorX);

                    break;

                case 1:
                    g.setColor(colorO);

                    break;

                default:
                    g.setColor(colorNone);

                    continue;
                }

                if (g.getColor() != colorNone) {
                    g.fillOval((c * tileSize) + tileInset,
                        (r * tileSize) + tileInset, tileSize - (tileInset * 2),
                        tileSize - (tileInset * 2));
                }
            }
        }
    }
}
