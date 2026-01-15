/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.checkers;

import java.awt.*;

import javax.swing.*;


/**
 * implements a container to display a Checkers board for use with
 * JCheckers.
 *
 * @author Holger Antelmann
 */
class JCheckersBoard extends JPanel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -949734855297990007L;

    /** DOCUMENT ME! */
    JCheckers jplay;

    /** DOCUMENT ME! */
    int draggedPiece = 0;

    /** DOCUMENT ME! */
    Point dragPoint = null;

    /** DOCUMENT ME! */
    int tileSize = 33;

    /** DOCUMENT ME! */
    int tileInset = 5;

    /** DOCUMENT ME! */
    Color tileDark = Color.gray;

    /** DOCUMENT ME! */
    Color tileLight = Color.lightGray;

    /** DOCUMENT ME! */
    Color color0 = Color.red;

    /** DOCUMENT ME! */
    Color color1 = Color.white;

/**
     * Creates a new JCheckersBoard object.
     *
     * @param jplay DOCUMENT ME!
     */
    JCheckersBoard(JCheckers jplay) {
        this.jplay = jplay;
        setTileSize(tileSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tileSize DOCUMENT ME!
     */
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
        setSize(tileSize * 8, tileSize * 8);
        setPreferredSize(getSize());
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        CheckersGame game = (CheckersGame) jplay.getAutoPlay().getGame();

        for (int c = 0; c < 8; c++) {
loop: 
            for (int r = 0; r < 8; r++) {
                // draw the tile color as checkered background
                if (((c + r) % 2) == 0) {
                    g.setColor(tileLight);
                } else {
                    g.setColor(tileDark);
                }

                if (jplay.isFlipped()) {
                    g.fillRect((7 - c) * tileSize, r * tileSize, tileSize,
                        tileSize);
                } else {
                    g.fillRect(c * tileSize, (7 - r) * tileSize, tileSize,
                        tileSize);
                }

                int pos = ((c + 1) * 10) + r + 1;

                if (pos == draggedPiece) {
                    continue loop;
                }

                CheckersPiece piece = game.getBoard().getPieceAt(pos);

                if (piece == null) {
                    continue loop;
                }

                if (jplay.isFlipped()) {
                    drawPiece(g, ((7 - c) * tileSize) + tileInset,
                        (r * tileSize) + tileInset, piece);
                } else {
                    drawPiece(g, (c * tileSize) + tileInset,
                        ((7 - r) * tileSize) + tileInset, piece);
                }
            }
        }

        if (draggedPiece != 0) {
            int offset = (tileSize - (tileInset * 2)) / 2;
            dragPoint.translate(-offset, -offset);
            drawPiece(g, (int) dragPoint.getX(), (int) dragPoint.getY(),
                game.getBoard().getPieceAt(draggedPiece));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param piece DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    void drawPiece(Graphics g, int x, int y, CheckersPiece piece) {
        switch (piece.getPlayer()) {
        case 0:
            g.setColor(color0);

            break;

        case 1:
            g.setColor(color1);

            break;

        default:
            throw new Error();
        }

        g.fillOval(x, y, tileSize - (tileInset * 2), tileSize -
            (tileInset * 2));

        if (piece.isKing()) {
            g.setColor(Color.black);
            g.drawOval(x + tileInset, y + tileInset,
                tileSize - (tileInset * 4), tileSize - (tileInset * 4));
        }
    }

    /**
     * pointToPosition() translates the point coordinates to an integer
     * representing a board position on the checkers board based on this
     * board's tile size. A 0 is returned if the given position is not a valid
     * position
     *
     * @see CheckersBoard#validPosition(int)
     */
    public int pointToPosition(Point p) {
        int column = ((int) p.getX() / tileSize) + 1;
        int row = ((int) p.getY() / tileSize);
        int pos = (column * 10) + (8 - row);

        if (CheckersBoard.validPosition(pos)) {
            return pos;
        } else {
            return 0;
        }
    }
}
