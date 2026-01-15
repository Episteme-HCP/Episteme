/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.util.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;


/**
 * class with static methods to provide standard configurations for
 * TilePuzzle games. Simply use the return values for the constructor of a
 * TilePuzzle game. Example:<br>
 * <code>TilePuzzle game = new TilePuzzle("Number Puzzle 3x3",
 * TilePuzzle.getNumberPuzzle(3));</code>
 *
 * @author Holger Antelmann
 *
 * @see TilePuzzle
 */
public final class TilePuzzleSamples {
/**
     * Creates a new TilePuzzleSamples object.
     */
    private TilePuzzleSamples() {
    }

    /**
     * provides the standard 3x3 AI class number puzzle
     *
     * @return DOCUMENT ME!
     */
    public static Integer[][] getAIClassPuzzle() {
        Integer[][] puzzle = new Integer[3][3];
        puzzle[0][0] = new Integer(1);
        puzzle[0][1] = new Integer(2);
        puzzle[0][2] = new Integer(3);
        puzzle[1][0] = new Integer(8);
        puzzle[1][1] = null;
        puzzle[1][2] = new Integer(4);
        puzzle[2][0] = new Integer(7);
        puzzle[2][1] = new Integer(6);
        puzzle[2][2] = new Integer(5);

        return puzzle;
    }

    /**
     * returns a number puzzle of variable size (size x size)
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Integer[][] getNumberPuzzle(int size) {
        Integer[][] defaultPuzzle = new Integer[size][size];

        for (int column = 0; column < size; column++) {
            for (int row = 0; row < size; row++) {
                if ((row + column) == ((size - 1) * 2)) {
                    continue;
                }

                defaultPuzzle[column][row] = new Integer((column * size) + row +
                        1);
            }
        }

        return defaultPuzzle;
    }

    /**
     * Returns a customized image puzzle that is derived from the given
     * image; each tile will be an ImageIcon fraction of the original image;
     * the number of tiles is determined by splitNumber (puzzle size is
     * splitNumber x splitNumber).
     *
     * @param image DOCUMENT ME!
     * @param splitNumber DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ImageIcon[][] getImagePuzzle(ImageIcon image, int splitNumber) {
        // creating a temporary top level container to have a context for
        // drawing the buffered image
        JFrame tframe = new JFrame();
        int width = image.getIconWidth();
        int height = image.getIconHeight();
        tframe.getContentPane().add(new JLabel(image));
        tframe.pack();

        //  creating the canvas for extracting the sub-images
        BufferedImage canvas = (BufferedImage) tframe.createImage(width, height);
        canvas.getGraphics().drawImage(image.getImage(), 0, 0, null);

        ImageIcon[][] imagePuzzle = new ImageIcon[splitNumber][splitNumber];

        for (int column = 0; column < splitNumber; column++) {
            for (int row = 0; row < splitNumber; row++) {
                if ((row + column) == ((splitNumber - 1) * 2)) {
                    continue;
                }

                Image tile = canvas.getSubimage(width / splitNumber * column,
                        height / splitNumber * row, width / splitNumber,
                        height / splitNumber);
                imagePuzzle[row][column] = new ImageIcon(tile);
            }
        }

        return imagePuzzle;
    }

    /**
     * Returns a customized image puzzle where the original image is
     * scaled to fit the given height and width
     *
     * @param image DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param splitNumber DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ImageIcon[][] getImagePuzzle(ImageIcon image, int width,
        int height, int splitNumber) {
        JFrame tframe = new JFrame();
        JPanel panel = new JPanel();
        panel.setSize(width, height);
        tframe.getContentPane().add(panel);
        tframe.pack();

        BufferedImage canvas = (BufferedImage) tframe.createImage(width, height);
        canvas.getGraphics()
              .drawImage(image.getImage(), 0, 0, width, height, null);

        ImageIcon scaled = new ImageIcon(canvas);

        return getImagePuzzle(scaled, splitNumber);
    }

    /**
     * calls getImagePuzzle() with a sample US flag
     *
     * @param splitNumber DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ImageIcon[][] getUSFlagPuzzle(int splitNumber) {
        ImageIcon icon = new ImageIcon(Settings.getResource(
                    "org/jscience/computing/game/puzzle/flag.jpg"));

        return getImagePuzzle(icon, splitNumber);
    }

    /**
     * calls getImagePuzzle() with a San Francisco skyline picture
     *
     * @param splitNumber DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ImageIcon[][] getSFPuzzle(int splitNumber) {
        ImageIcon icon = new ImageIcon(Settings.getResource(
                    "org/jscience/computing/game/puzzle/sfskyline.jpg"));

        return getImagePuzzle(icon, splitNumber);
    }
}
