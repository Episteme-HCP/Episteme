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

package org.jscience.computing.game.chess;

import java.awt.*;

import javax.swing.*;


/**
 * implements a container to display a Chess board for use with JChess
 *
 * @author Holger Antelmann
 */
class JChessBoard extends JPanel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -8491923156796303350L;

    /** DOCUMENT ME! */
    ChessGraphics icons;

    /** DOCUMENT ME! */
    JChess jplay;

    /** DOCUMENT ME! */
    int draggedPiece = 0;

    /** DOCUMENT ME! */
    Point dragPoint = null;

    /** DOCUMENT ME! */
    int tileSize;

    /** DOCUMENT ME! */
    int defaultTileSize;

    /** DOCUMENT ME! */
    private boolean showCoordinates = false;

    /** DOCUMENT ME! */
    Color tileDark = Color.gray;

    /** DOCUMENT ME! */
    Color tileLight = Color.lightGray;

/**
     * Creates a new JChessBoard object.
     *
     * @param jplay DOCUMENT ME!
     */
    JChessBoard(JChess jplay) {
        this.jplay = jplay;
        setStandardGraphics();
        defaultTileSize = icons.getKingIcon(0).getImage()
                               .getWidth(icons.getKingIcon(0).getImageObserver());
        setTileSize(defaultTileSize);
        addComponentListener(new ChessSizer(this));
        setBackground(Color.white);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tileSize DOCUMENT ME!
     */
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;

        if (showCoordinates) {
            setSize(tileSize * 9, tileSize * 9);
        } else {
            setSize(tileSize * 8, tileSize * 8);
        }

        setPreferredSize(getSize());
    }

    /**
     * DOCUMENT ME!
     *
     * @param on DOCUMENT ME!
     */
    public void setShowCoorinates(boolean on) {
        showCoordinates = on;
        setTileSize(tileSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getShowCoordinates() {
        return showCoordinates;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ChessBoard board;

        if (jplay.setupGame == null) {
            board = ((ChessGame) jplay.getAutoPlay().getGame()).getBoard();
        } else {
            board = jplay.setupGame.getBoard();
        }

        for (int c = 0; c < 8; c++) {
loop: 
            for (int r = 0; r < 8; r++) {
                // draw the tile color as checkered background
                if (((c + r) % 2) == ChessBoard.WHITE) {
                    g.setColor(tileDark);
                } else {
                    g.setColor(tileLight);
                }

                if (jplay.isFlipped()) {
                    g.fillRect(((7 - c) * tileSize) +
                        (showCoordinates ? tileSize : 0), r * tileSize,
                        tileSize, tileSize);
                } else {
                    g.fillRect((c * tileSize) +
                        (showCoordinates ? tileSize : 0), (7 - r) * tileSize,
                        tileSize, tileSize);
                }

                int pos = ((c + 1) * 10) + r + 1;

                if (pos == draggedPiece) {
                    continue loop;
                }

                ChessPiece piece = board.getPieceAt(pos);

                if (piece == null) {
                    continue loop;
                }

                if (jplay.isFlipped()) {
                    drawPiece(g,
                        ((7 - c) * tileSize) +
                        (showCoordinates ? tileSize : 0), r * tileSize, piece);
                } else {
                    drawPiece(g,
                        (c * tileSize) + (showCoordinates ? tileSize : 0),
                        (7 - r) * tileSize, piece);
                }
            }
        }

        if (draggedPiece != 0) {
            int offset = tileSize / 2;
            dragPoint.translate(-offset, -offset);
            drawPiece(g, (int) dragPoint.getX(), (int) dragPoint.getY(),
                board.getPieceAt(draggedPiece));
        }

        if (showCoordinates) {
            for (int i = 1; i <= 8; i++) {
                String r = String.valueOf((jplay.isFlipped()) ? i : (9 - i));
                String c = String.valueOf(new Character(
                            (char) ('A' - 1 +
                            ((jplay.isFlipped()) ? (9 - i) : i))));
                g.drawString(r, (tileSize / 2),
                    (tileSize * (i - 1)) + (tileSize / 2));
                g.drawString(c, (tileSize * i) + (tileSize / 2),
                    (tileSize * 8) + (tileSize / 2));
            }
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
    void drawPiece(Graphics g, int x, int y, ChessPiece piece) {
        switch (piece.getPieceChar()) {
        case 'P':
            g.drawImage(icons.getPawnIcon(piece.getColor()).getImage(), x, y,
                tileSize, tileSize,
                icons.getPawnIcon(piece.getColor()).getImageObserver());

            return;

        case 'R':
            g.drawImage(icons.getRookIcon(piece.getColor()).getImage(), x, y,
                tileSize, tileSize,
                icons.getRookIcon(piece.getColor()).getImageObserver());

            return;

        case 'N':
            g.drawImage(icons.getKnightIcon(piece.getColor()).getImage(), x, y,
                tileSize, tileSize,
                icons.getKnightIcon(piece.getColor()).getImageObserver());

            return;

        case 'B':
            g.drawImage(icons.getBishopIcon(piece.getColor()).getImage(), x, y,
                tileSize, tileSize,
                icons.getBishopIcon(piece.getColor()).getImageObserver());

            return;

        case 'Q':
            g.drawImage(icons.getQueenIcon(piece.getColor()).getImage(), x, y,
                tileSize, tileSize,
                icons.getQueenIcon(piece.getColor()).getImageObserver());

            return;

        case 'K':
            g.drawImage(icons.getKingIcon(piece.getColor()).getImage(), x, y,
                tileSize, tileSize,
                icons.getKingIcon(piece.getColor()).getImageObserver());

            return;

        default:
            throw new Error();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void setStandardGraphics() {
        setGraphics(new SampleChessGraphics());
    }

    /**
     * DOCUMENT ME!
     *
     * @param icons DOCUMENT ME!
     */
    public synchronized void setGraphics(ChessGraphics icons) {
        this.icons = icons;
        defaultTileSize = icons.getKingIcon(0).getImage()
                               .getWidth(icons.getKingIcon(0).getImageObserver());
        setTileSize(defaultTileSize);
    }

    /**
     * pointToPosition translates the point coordinates to an integer
     * representing a BoardPosition
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int pointToPosition(Point p) {
        Point point = p;

        if (jplay.isFlipped()) {
            point = new Point(jplay.jboard.getWidth() - (int) point.getX(),
                    jplay.jboard.getHeight() - (int) point.getY());

            if (jplay.jboard.getShowCoordinates()) {
                point.translate(jplay.jboard.tileSize, -jplay.jboard.tileSize);
            }
        }

        int column = ((int) point.getX() / tileSize) + 1;

        if (showCoordinates) {
            column--;
        }

        int row = ((int) point.getY() / tileSize);
        int pos = (column * 10) + (8 - row);

        if (BoardPosition.validPosition(pos)) {
            return pos;
        } else {
            return 0;
        }
    }
}
