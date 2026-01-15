/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
