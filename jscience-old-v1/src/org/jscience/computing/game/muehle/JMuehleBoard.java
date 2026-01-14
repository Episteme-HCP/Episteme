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

package org.jscience.computing.game.muehle;

import java.awt.*;

import javax.swing.*;


/**
 * implements a container to display a Muehle board for use with JMuehle
 *
 * @author Holger Antelmann
 */
class JMuehleBoard extends JPanel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 2827344717093415263L;

    /** DOCUMENT ME! */
    JMuehle jplay;

    /** DOCUMENT ME! */
    MuehlePosition draggedPiece = null;

    /** DOCUMENT ME! */
    MuehleMove captureMove = null;

    /** DOCUMENT ME! */
    Point dragPoint = null;

    /** DOCUMENT ME! */
    int tileSize = 35;

    /** DOCUMENT ME! */
    int tileInset = 5;

    /** DOCUMENT ME! */
    Color background = Color.lightGray;

    /** DOCUMENT ME! */
    Color line = Color.black;

    /** DOCUMENT ME! */
    Color color0 = Color.red;

    /** DOCUMENT ME! */
    Color color1 = Color.white;

    /** DOCUMENT ME! */
    Rectangle[] positionTile;

/**
     * Creates a new JMuehleBoard object.
     *
     * @param jplay DOCUMENT ME!
     */
    JMuehleBoard(JMuehle jplay) {
        this.jplay = jplay;
        setBackground(background);
        setTileSize(tileSize, tileInset);
        addComponentListener(new MuehleSizer(this));
    }

    /**
     * DOCUMENT ME!
     *
     * @param tileSize DOCUMENT ME!
     */
    public void setTileSize(int tileSize) {
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

        // muehle board is split into 7x7
        setSize(tileSize * 7, tileSize * 7);
        setPreferredSize(getSize());
        positionTile = new Rectangle[MuehlePosition.NUMBER_OF_FIELDS];

        for (int i = 0; i < MuehlePosition.NUMBER_OF_FIELDS; i++) {
            Point p = getOffset(i);
            positionTile[i] = new Rectangle(tileSize * (int) p.getX(),
                    tileSize * (int) p.getY(), tileSize, tileSize);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        MuehleGame game = (MuehleGame) jplay.getAutoPlay().getGame();

        // draw lines
        int offset = tileSize / 2;

        // outer ring
        g.drawLine(offset, offset, (tileSize * 6) + offset, offset);
        g.drawLine(offset, offset, offset, (tileSize * 6) + offset);
        g.drawLine((tileSize * 6) + offset, (tileSize * 6) + offset,
            (tileSize * 6) + offset, offset);
        g.drawLine(offset, (tileSize * 6) + offset, (tileSize * 6) + offset,
            (tileSize * 6) + offset);

        // middle ring
        g.drawLine(tileSize + offset, tileSize + offset,
            (tileSize * 5) + offset, tileSize + offset);
        g.drawLine((tileSize * 5) + offset, tileSize + offset,
            (tileSize * 5) + offset, (tileSize * 5) + offset);
        g.drawLine((tileSize * 5) + offset, (tileSize * 5) + offset,
            tileSize + offset, (tileSize * 5) + offset);
        g.drawLine(tileSize + offset, (tileSize * 5) + offset,
            tileSize + offset, tileSize + offset);

        // inner ring
        g.drawLine((tileSize * 2) + offset, (tileSize * 2) + offset,
            (tileSize * 2) + offset, (tileSize * 4) + offset);
        g.drawLine((tileSize * 2) + offset, (tileSize * 2) + offset,
            (tileSize * 4) + offset, (tileSize * 2) + offset);
        g.drawLine((tileSize * 4) + offset, (tileSize * 4) + offset,
            (tileSize * 2) + offset, (tileSize * 4) + offset);
        g.drawLine((tileSize * 4) + offset, (tileSize * 4) + offset,
            (tileSize * 4) + offset, (tileSize * 2) + offset);

        // connection lines
        g.drawLine(offset, (tileSize * 3) + offset, (tileSize * 2) + offset,
            (tileSize * 3) + offset);
        g.drawLine((tileSize * 3) + offset, offset, (tileSize * 3) + offset,
            (tileSize * 2) + offset);
        g.drawLine((tileSize * 4) + offset, (tileSize * 3) + offset,
            (tileSize * 6) + offset, (tileSize * 3) + offset);
        g.drawLine((tileSize * 3) + offset, (tileSize * 4) + offset,
            (tileSize * 3) + offset, (tileSize * 6) + offset);

        // draw pieces
        for (int i = 0; i < MuehlePosition.NUMBER_OF_FIELDS; i++) {
            MuehlePosition pos = new MuehlePosition(i);

            if (game.getValueAt(pos) == MuehleGame.EMPTY) {
                continue;
            }

            if ((draggedPiece != null) && draggedPiece.equals(pos)) {
                continue;
            }

            drawPiece(g, positionToPoint(pos), game.getValueAt(pos));
        }

        // draw capture piece if applicable
        if (captureMove != null) {
            drawPiece(g,
                positionToPoint((MuehlePosition) captureMove.getNewPosition()),
                captureMove.getPlayer());
        } else {
            // draw drag piece if applicable
            if (draggedPiece != null) {
                Point p = dragPoint;
                p.translate(-tileSize / 2, -tileSize / 2);
                drawPiece(g, p, game.nextPlayer());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param point DOCUMENT ME!
     * @param player DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    void drawPiece(Graphics g, Point point, int player) {
        switch (player) {
        case 0:
            g.setColor(color0);

            break;

        case 1:
            g.setColor(color1);

            break;

        default:
            throw new Error();
        }

        g.fillOval((int) point.getX(), (int) point.getY(),
            tileSize - (tileInset * 2), tileSize - (tileInset * 2));
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    MuehlePosition pointToPosition(Point p) {
        for (int i = 0; i < MuehlePosition.NUMBER_OF_FIELDS; i++) {
            if (positionTile[i].contains(p)) {
                return new MuehlePosition(i);
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Point positionToPoint(MuehlePosition pos) {
        Point p = getOffset(pos.asInteger());

        return new Point(((int) p.getX() * tileSize) + tileInset,
            ((int) p.getY() * tileSize) + tileInset);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    Point getOffset(int pos) {
        int offsetX;
        int offsetY;

        switch (pos) {
        case 0:
            offsetX = 0;
            offsetY = 0;

            break;

        case 1:
            offsetX = 3;
            offsetY = 0;

            break;

        case 2:
            offsetX = 6;
            offsetY = 0;

            break;

        case 3:
            offsetX = 6;
            offsetY = 3;

            break;

        case 4:
            offsetX = 6;
            offsetY = 6;

            break;

        case 5:
            offsetX = 3;
            offsetY = 6;

            break;

        case 6:
            offsetX = 0;
            offsetY = 6;

            break;

        case 7:
            offsetX = 0;
            offsetY = 3;

            break;

        case 8:
            offsetX = 1;
            offsetY = 1;

            break;

        case 9:
            offsetX = 3;
            offsetY = 1;

            break;

        case 10:
            offsetX = 5;
            offsetY = 1;

            break;

        case 11:
            offsetX = 5;
            offsetY = 3;

            break;

        case 12:
            offsetX = 5;
            offsetY = 5;

            break;

        case 13:
            offsetX = 3;
            offsetY = 5;

            break;

        case 14:
            offsetX = 1;
            offsetY = 5;

            break;

        case 15:
            offsetX = 1;
            offsetY = 3;

            break;

        case 16:
            offsetX = 2;
            offsetY = 2;

            break;

        case 17:
            offsetX = 3;
            offsetY = 2;

            break;

        case 18:
            offsetX = 4;
            offsetY = 2;

            break;

        case 19:
            offsetX = 4;
            offsetY = 3;

            break;

        case 20:
            offsetX = 4;
            offsetY = 4;

            break;

        case 21:
            offsetX = 3;
            offsetY = 4;

            break;

        case 22:
            offsetX = 2;
            offsetY = 4;

            break;

        case 23:
            offsetX = 2;
            offsetY = 3;

            break;

        default:
            throw new Error();
        }

        return new Point(offsetX, offsetY);
    }
}
