/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import javax.swing.*;


/**
 * This interface allows other programs to customize the appearence
 * of the graphics used by the class JChess to visualize the chess board.
 * All methods expect one of constants of either WHITE or BLACK as
 * parameters; otherwise, an exception may be thrown.
 * The following conventions should be used to implement this interface
 * in a usable way:
 * <ul><li>make all icons square shaped (width = height) to avoid streching</li>
 * <li>the icons should have a transparent background (like GIF images), so that the tile background color can be seen through</li>
 * <li>when deciding on a size for your icons, keep in mind your screen resolution and the size of a chess board (8x8)</li>
 * <li>it is always the white king's icon that will determine standard display size for all icons</li>
 * <li></li>the default height/width used by SampleChessGraphics is 32 pixels</ul>
 * Here is some sample code that demonstrates how to potentially customize
 * only some graphics by simply overwriting some methods of the ChessGraphics
 * implementing class SampleChessGraphics:
 * <pre>
 * java.net.URL blackKingImageURL = ...;
 * java.net.URL whitePawnURL = ...;
 * java.net.URL blackPawnURL = ...;
 * JChess jchess = new JChess();
 * jchess.setGraphics(new SampleChessGraphics(1) {
 *     public ImageIcon getKingIcon(int color) {
 *         // customizing only the black king
 *         if (color == ChessGraphics.WHITE)
 *             return super.getKingIcon(ChessGraphics.WHITE);
 *         else return new ImageIcon(blackKingImageURL);
 *     }
 * <p/>
 *     public ImageIcon getPawnIcon(int color) {
 *         switch (color) {
 *             case ChessGraphics.WHITE: return new ImageIcon(whitePawnURL);
 *             case ChessGraphics.BLACK: return new ImageIcon(blackPawnURL);
 *             default: throw new IllegalArgumentException();
 *         }
 *     }
 * });
 * new JGameFrame(jchess).setVisible(true);
 * </pre>
 *
 * @author Holger Antelmann
 * @see SampleChessGraphics
 * @see JChess#setGraphics(ChessGraphics)
 */
public interface ChessGraphics {
    /**
     * DOCUMENT ME!
     */
    static final int WHITE = 0;

    /**
     * DOCUMENT ME!
     */
    static final int BLACK = 1;

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ImageIcon getKingIcon(int color);

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ImageIcon getQueenIcon(int color);

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ImageIcon getRookIcon(int color);

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ImageIcon getKnightIcon(int color);

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ImageIcon getBishopIcon(int color);

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ImageIcon getPawnIcon(int color);
}
