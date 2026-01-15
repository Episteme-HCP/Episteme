/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.wolfsheep;

import org.jscience.computing.game.chess.BoardPosition;
import org.jscience.computing.game.wolfsheep.WolfsheepGame.Piece;

import org.jscience.util.Settings;

import java.awt.*;

import javax.swing.*;


/**
 * implements a container to display a WolfsheepGame board for use with JWS
 *
 * @author Holger Antelmann
 */
class JWSBoard extends JPanel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 337704566672924353L;

    /** DOCUMENT ME! */
    JWS jplay;

    /** DOCUMENT ME! */
    Piece draggedPiece = null;

    /** DOCUMENT ME! */
    Point dragPoint = null;

    /** DOCUMENT ME! */
    int tileSize;

    /** DOCUMENT ME! */
    int defaultTileSize = 44;

    /** DOCUMENT ME! */
    Color tileDark = Color.gray;

    /** DOCUMENT ME! */
    Color tileLight = Color.lightGray;

    /** DOCUMENT ME! */
    ImageIcon wolfIcon = new ImageIcon(Settings.getResource(
                "org/jscience/computing/game/wolfsheep/wolf.gif"), "wolf");

    /** DOCUMENT ME! */
    ImageIcon sheepIcon = new ImageIcon(Settings.getResource(
                "org/jscience/computing/game/wolfsheep/sheep.gif"), "sheep");

/**
     * Creates a new JWSBoard object.
     *
     * @param jplay DOCUMENT ME!
     */
    JWSBoard(JWS jplay) {
        this.jplay = jplay;
        setTileSize(defaultTileSize);
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
     *
     * @throws Error DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        WolfsheepGame game = (WolfsheepGame) jplay.getAutoPlay().getGame();

        for (int c = 0; c < 8; c++) {
loop: 
            for (int r = 0; r < 8; r++) {
                // draw the tile color as checkered background
                if (((c + r) % 2) == 0) {
                    g.setColor(tileLight);
                } else {
                    g.setColor(tileDark);
                }

                g.fillRect(c * tileSize, (7 - r) * tileSize, tileSize, tileSize);
            }
        }

        if (draggedPiece != null) {
            if (!draggedPiece.equals(game.wolf)) {
                drawPiece(g, game.wolf);
            }

            if (!draggedPiece.equals(game.sheep[0])) {
                drawPiece(g, game.sheep[0]);
            }

            if (!draggedPiece.equals(game.sheep[1])) {
                drawPiece(g, game.sheep[1]);
            }

            if (!draggedPiece.equals(game.sheep[2])) {
                drawPiece(g, game.sheep[2]);
            }

            if (!draggedPiece.equals(game.sheep[3])) {
                drawPiece(g, game.sheep[3]);
            }

            // drawing dragged piece
            switch (draggedPiece.getType()) {
            case WolfsheepGame.WOLF:
                g.drawImage(wolfIcon.getImage(),
                    (int) dragPoint.getX() - (tileSize / 2),
                    (int) dragPoint.getY() - (tileSize / 2), tileSize,
                    tileSize, wolfIcon.getImageObserver());

                break;

            case WolfsheepGame.SHEEP:
                g.drawImage(sheepIcon.getImage(),
                    (int) dragPoint.getX() - (tileSize / 2),
                    (int) dragPoint.getY() - (tileSize / 2), tileSize,
                    tileSize, sheepIcon.getImageObserver());

                break;

            default:
                throw new Error();
            }
        } else {
            drawPiece(g, game.wolf);
            drawPiece(g, game.sheep[0]);
            drawPiece(g, game.sheep[1]);
            drawPiece(g, game.sheep[2]);
            drawPiece(g, game.sheep[3]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param piece DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    void drawPiece(Graphics g, Piece piece) {
        int x = ((piece.getPosition().getFile() - 'a') * tileSize);
        int y = ((8 - piece.getPosition().getRank()) * tileSize);

        if (jplay.isFlipped()) {
            x = getWidth() - x - tileSize;
            y = getWidth() - y - tileSize;
        }

        switch (piece.getType()) {
        case WolfsheepGame.WOLF:
            g.drawImage(wolfIcon.getImage(), x, y, tileSize, tileSize,
                wolfIcon.getImageObserver());

            break;

        case WolfsheepGame.SHEEP:
            g.drawImage(sheepIcon.getImage(), x, y, tileSize, tileSize,
                sheepIcon.getImageObserver());

            break;

        default:
            throw new Error();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int pointToPosition(Point p) {
        int column = ((int) p.getX() / tileSize) + 1;
        int row = 8 - ((int) p.getY() / tileSize);
        int pos = (column * 10) + row;

        if (BoardPosition.validPosition(pos)) {
            return pos;
        } else {
            return 0;
        }
    }
}
