/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import org.jscience.util.Settings;

import javax.swing.*;


/**
 * This class contains two set of sample chess graphics shipped with the
 * antelmann.jar file.
 *
 * @author Holger Antelmann
 */
public class SampleChessGraphics implements ChessGraphics {
    /** DOCUMENT ME! */
    int setNumber;

    /** DOCUMENT ME! */
    ImageIcon[] whiteKingIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] whiteQueenIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] whiteRookIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] whiteKnightIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] whiteBishopIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] whitePawnIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] blackKingIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] blackQueenIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] blackRookIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] blackKnightIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] blackBishopIcon = new ImageIcon[2];

    /** DOCUMENT ME! */
    ImageIcon[] blackPawnIcon = new ImageIcon[2];

/**
     * currently, this class only supports 1 sets; hence setNumber can be
     * either 1 or 2 - everthing else throws IllegalArgumentException
     */
    public SampleChessGraphics() {
        this.setNumber = 0;
        initGraphics();
    }

    /**
     * currently, this class only supports 2 sets; hence setNumber can
     * be either 1 or 2 - everthing else throws IllegalArgumentException
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //public SampleChessGraphics (int setNumber) {
    //    if ((setNumber != 1) && (setNumber != 2))
    //        throw new IllegalArgumentException("only 1 or 2 is allowed as a parameter");
    //    this.setNumber = setNumber - 1;
    //    initGraphics();
    //}
    public ImageIcon getKingIcon(int color) {
        switch (color) {
        case WHITE:
            return whiteKingIcon[setNumber];

        case BLACK:
            return blackKingIcon[setNumber];

        default:
            throw new IllegalArgumentException("given color (" + color +
                ") is not a valid");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ImageIcon getQueenIcon(int color) {
        switch (color) {
        case WHITE:
            return whiteQueenIcon[setNumber];

        case BLACK:
            return blackQueenIcon[setNumber];

        default:
            throw new IllegalArgumentException("given color (" + color +
                ") is not a valid");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ImageIcon getRookIcon(int color) {
        switch (color) {
        case WHITE:
            return whiteRookIcon[setNumber];

        case BLACK:
            return blackRookIcon[setNumber];

        default:
            throw new IllegalArgumentException("given color (" + color +
                ") is not a valid");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ImageIcon getKnightIcon(int color) {
        switch (color) {
        case WHITE:
            return whiteKnightIcon[setNumber];

        case BLACK:
            return blackKnightIcon[setNumber];

        default:
            throw new IllegalArgumentException("given color (" + color +
                ") is not a valid");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ImageIcon getBishopIcon(int color) {
        switch (color) {
        case WHITE:
            return whiteBishopIcon[setNumber];

        case BLACK:
            return blackBishopIcon[setNumber];

        default:
            throw new IllegalArgumentException("given color (" + color +
                ") is not a valid");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ImageIcon getPawnIcon(int color) {
        switch (color) {
        case WHITE:
            return whitePawnIcon[setNumber];

        case BLACK:
            return blackPawnIcon[setNumber];

        default:
            throw new IllegalArgumentException("given color (" + color +
                ") is not a valid");
        }
    }

    /**
     * DOCUMENT ME!
     */
    void initGraphics() {
        whiteKingIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/wk.gif"), "white King");
        whiteQueenIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/wq.gif"), "white Queen");
        whiteRookIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/wr.gif"), "white Rook");
        whiteKnightIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/wn.gif"), "white Knight");
        whiteBishopIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/wb.gif"), "white Bishop");
        whitePawnIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/wp.gif"), "white Pawn");
        blackKingIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/bk.gif"), "black King");
        blackQueenIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/bq.gif"), "black Queen");
        blackRookIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/br.gif"), "black Rook");
        blackKnightIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/bn.gif"), "black Knight");
        blackBishopIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/bb.gif"), "black Bishop");
        blackPawnIcon[0] = new ImageIcon(Settings.getResource(
                    "org/jscience/swing/chess/bp.gif"), "black Pawn");
    }
}
