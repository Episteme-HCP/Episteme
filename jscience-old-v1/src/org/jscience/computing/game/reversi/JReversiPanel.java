/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
