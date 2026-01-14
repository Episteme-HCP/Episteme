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

package org.jscience.computing.game.fourwins;

import java.awt.*;

import javax.swing.*;


/**
 * JFourWinsPanel provides a Java Swing container to be used with JFourWins
 *
 * @author Holger Antelmann
 *
 * @see JFourWins
 * @see org.jscience.computing.game.JGameFrame
 */
class JFourWinsPanel extends JPanel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -6859735994515694678L;

    /** DOCUMENT ME! */
    JFourWins jplay;

    /** DOCUMENT ME! */
    int tileSize = 35;

    /** DOCUMENT ME! */
    int tileInset = 2;

    /** DOCUMENT ME! */
    Color colorBackground = Color.blue;

    /** DOCUMENT ME! */
    Color colorX = Color.yellow;

    /** DOCUMENT ME! */
    Color colorO = Color.red;

    /** DOCUMENT ME! */
    Color colorNone = Color.lightGray;

/**
     * Creates a new JFourWinsPanel object.
     *
     * @param jplay DOCUMENT ME!
     */
    JFourWinsPanel(JFourWins jplay) {
        this.jplay = jplay;
        setTileSize(tileSize, tileInset);
        addComponentListener(new FourWinsSizer(this));
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

        FourWinsGame game = (FourWinsGame) jplay.getAutoPlay().getGame();
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

        FourWinsGame game = (FourWinsGame) jplay.getAutoPlay().getGame();

        for (int c = 0; c < game.boardWidth; c++) {
            for (int r = 0; r < game.boardHeight; r++) {
                switch (game.getValueAt(c, r)) {
                case 0:
                    g.setColor(colorX);

                    break;

                case 1:
                    g.setColor(colorO);

                    break;

                default:
                    g.setColor(colorNone);

                    break;
                }

                g.fillOval((c * tileSize) + tileInset,
                    (tileSize * (game.boardHeight - 1)) - (r * tileSize) +
                    tileInset, tileSize - (tileInset * 2),
                    tileSize - (tileInset * 2));
            }
        }

        if (game.getWinner() != null) {
            int column = 0;
            int row = 0;
            int player = 0;
loop: 
            for (column = 0; column < game.boardWidth; column++) {
                for (row = 0; row < game.boardHeight; row++) {
                    if (game.board[column][row] != -1) {
                        player = game.checkPositionWin(column, row);

                        if (player != -1) {
                            break loop;
                        }
                    }
                }
            }

            int line;

            // up
            outlineWin(game, g, column, row);

            for (line = 1; (line <= 4) && ((row + line) < game.boardHeight);
                    line++) {
                if (game.board[column][row + line] != player) {
                    break;
                }
            }

            if (line >= 4) {
                outlineWin(game, g, column, row + 1);
                outlineWin(game, g, column, row + 2);
                outlineWin(game, g, column, row + 3);

                return;
            }

            // up right
            for (line = 1;
                    (line <= 4) && ((row + line) < game.boardHeight) &&
                    ((column + line) < game.boardWidth); line++) {
                if (game.board[column + line][row + line] != player) {
                    break;
                }
            }

            if (line >= 4) {
                outlineWin(game, g, column + 1, row + 1);
                outlineWin(game, g, column + 2, row + 2);
                outlineWin(game, g, column + 3, row + 3);

                return;
            }

            // right
            for (line = 1; (line <= 4) && ((column + line) < game.boardWidth);
                    line++) {
                if (game.board[column + line][row] != player) {
                    break;
                }
            }

            if (line >= 4) {
                outlineWin(game, g, column + 1, row);
                outlineWin(game, g, column + 2, row);
                outlineWin(game, g, column + 3, row);

                return;
            }

            // down right
            for (line = 1;
                    (line <= 4) && ((row - line) >= 0) &&
                    ((column + line) < game.boardWidth); line++) {
                if (game.board[column + line][row - line] != player) {
                    break;
                }
            }

            if (line >= 4) {
                outlineWin(game, g, column + 1, row - 1);
                outlineWin(game, g, column + 2, row - 2);
                outlineWin(game, g, column + 3, row - 3);

                return;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param r DOCUMENT ME!
     */
    void outlineWin(FourWinsGame game, Graphics g, int c, int r) {
        g.setColor(Color.black);
        g.drawOval((c * tileSize) + tileInset + tileInset,
            (tileSize * (game.boardHeight - 1)) - (r * tileSize) + tileInset +
            tileInset, tileSize - (tileInset * 4), tileSize - (tileInset * 4));
    }
}
