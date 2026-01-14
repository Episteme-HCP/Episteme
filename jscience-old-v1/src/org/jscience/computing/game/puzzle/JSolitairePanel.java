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

import java.awt.*;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class JSolitairePanel extends JPanel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 6747176970179437030L;

    /** DOCUMENT ME! */
    JSolitaire jplay;

    /** used by JSolitairePanelListener */
    SolitairePosition draggedPeg = null;

    /** used by JSolitairePanelListener */
    Point dragPoint = null;

    /** DOCUMENT ME! */
    int tileSize = 30;

    /** DOCUMENT ME! */
    int tileInset = 3;

    /** DOCUMENT ME! */
    Color colorBackground = Color.blue;

    /** DOCUMENT ME! */
    Color colorPeg = Color.yellow;

    /** DOCUMENT ME! */
    Color colorNone = Color.gray;

/**
     * Creates a new JSolitairePanel object.
     *
     * @param jplay DOCUMENT ME!
     */
    JSolitairePanel(JSolitaire jplay) {
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
        setSize(tileSize * 7, tileSize * 7); // solitaire size is 7 always
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

        Solitaire game = (Solitaire) jplay.getAutoPlay().getGame();

        for (int c = 1; c < 8; c++) {
loop: 
            for (int r = 1; r < 8; r++) {
                switch (game.getValueAt(c, r)) {
                case 0:
                    g.setColor(colorNone);

                    break;

                case 1:
                    g.setColor(colorPeg);

                    if ((draggedPeg != null) &&
                            (draggedPeg.equals(new SolitairePosition(c, r)))) {
                        g.setColor(colorNone);
                    }

                    break;

                default:

                    continue loop;
                }

                g.fillOval(((c - 1) * tileSize) + tileInset,
                    ((r - 1) * tileSize) + tileInset,
                    tileSize - (tileInset * 2), tileSize - (tileInset * 2));
            }
        }

        if (draggedPeg != null) {
            g.setColor(colorPeg);
            g.fillOval((int) dragPoint.getX() - (tileSize / 2),
                (int) dragPoint.getY() - (tileSize / 2),
                tileSize - (tileInset * 2), tileSize - (tileInset * 2));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SolitairePosition pointToPosition(Point p) {
        int column = ((int) p.getX() / tileSize) + 1;
        int row = ((int) p.getY() / tileSize) + 1;
        SolitairePosition position = null;

        try {
            return new SolitairePosition(column, row);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
