/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;


/**
 * class with static methods to provide standard configurations for
 * Solitaire games. Simply use the return values for the constructor of a
 * Solitaire game. Example:<br>
 * <code>Solitaire game = new Solitaire("arrow game",
 * SolitaireSamples.getArrow());</code>
 *
 * @author Holger Antelmann
 *
 * @see Solitaire
 */
public class SolitaireSamples {
    /**
     * Creates a new SolitaireSamples object.
     */
    private SolitaireSamples() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] getSolitaire() {
        int[][] board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if (SolitairePosition.isValidPosition(column, row)) {
                    if ((column == 4) && (row == 4)) {
                        board[column][row] = 0;
                    } else {
                        board[column][row] = 1;
                    }
                } else {
                    board[column][row] = -1;
                }
            }
        }

        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numberOfPeggs DOCUMENT ME!
     * @param randomSeed DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] getRandomField(int numberOfPeggs, long randomSeed) {
        if ((numberOfPeggs < 2) || (numberOfPeggs > 32)) {
            throw new IllegalArgumentException();
        }

        int[] pos = new int[] {
                13, 14, 15, 23, 24, 25, 31, 32, 33, 34, 35, 36, 37, 41, 42, 43,
                44, 45, 46, 47, 51, 52, 53, 54, 55, 56, 57, 63, 64, 65, 73, 74,
                75
            };
        Random random = new Random(randomSeed);
        Vector<Object> all = new Vector<Object>(pos.length);

        for (int i = 0; i < pos.length; i++) {
            all.add(new Integer(pos[i]));
        }

        Collections.shuffle(all, random);

        int[][] board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if (SolitairePosition.isValidPosition(column, row)) {
                    Integer p = new Integer(SolitairePosition.calculateIntPos(
                                column, row));

                    if (all.indexOf(p) < numberOfPeggs) {
                        board[column][row] = 1;
                    } else {
                        board[column][row] = 0;
                    }
                } else {
                    board[column][row] = -1;
                }
            }
        }

        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] getCross() {
        int[][] board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if (SolitairePosition.isValidPosition(column, row)) {
                    switch (SolitairePosition.calculateIntPos(column, row)) {
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 33:
                    case 53:
                        board[column][row] = 1;

                        break;

                    default:
                        board[column][row] = 0;
                    }
                } else {
                    board[column][row] = -1;
                }
            }
        }

        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] getPlus() {
        int[][] board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if (SolitairePosition.isValidPosition(column, row)) {
                    switch (SolitairePosition.calculateIntPos(column, row)) {
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 24:
                    case 34:
                    case 54:
                    case 64:
                        board[column][row] = 1;

                        break;

                    default:
                        board[column][row] = 0;
                    }
                } else {
                    board[column][row] = -1;
                }
            }
        }

        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] getFireplace() {
        int[][] board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if (SolitairePosition.isValidPosition(column, row)) {
                    switch (SolitairePosition.calculateIntPos(column, row)) {
                    case 31:
                    case 41:
                    case 51:
                    case 32:
                    case 42:
                    case 52:
                    case 33:
                    case 43:
                    case 53:
                    case 34:
                    case 54:
                        board[column][row] = 1;

                        break;

                    default:
                        board[column][row] = 0;
                    }
                } else {
                    board[column][row] = -1;
                }
            }
        }

        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] getArrow() {
        int[][] board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if (SolitairePosition.isValidPosition(column, row)) {
                    switch (SolitairePosition.calculateIntPos(column, row)) {
                    case 41:
                    case 32:
                    case 42:
                    case 52:
                    case 23:
                    case 33:
                    case 43:
                    case 53:
                    case 63:
                    case 44:
                    case 45:
                    case 36:
                    case 46:
                    case 56:
                    case 37:
                    case 47:
                    case 57:
                        board[column][row] = 1;

                        break;

                    default:
                        board[column][row] = 0;
                    }
                } else {
                    board[column][row] = -1;
                }
            }
        }

        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] getPyramid() {
        int[][] board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if (SolitairePosition.isValidPosition(column, row)) {
                    switch (SolitairePosition.calculateIntPos(column, row)) {
                    case 42:
                    case 33:
                    case 43:
                    case 53:
                    case 24:
                    case 34:
                    case 44:
                    case 54:
                    case 64:
                    case 15:
                    case 25:
                    case 35:
                    case 45:
                    case 55:
                    case 65:
                    case 75:
                        board[column][row] = 1;

                        break;

                    default:
                        board[column][row] = 0;
                    }
                } else {
                    board[column][row] = -1;
                }
            }
        }

        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[][] getDiamond() {
        int[][] board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if (SolitairePosition.isValidPosition(column, row)) {
                    switch (SolitairePosition.calculateIntPos(column, row)) {
                    case 31:
                    case 51:
                    case 13:
                    case 73:
                    case 44:
                    case 15:
                    case 75:
                    case 37:
                    case 57:
                        board[column][row] = 0;

                        break;

                    default:
                        board[column][row] = 1;
                    }
                } else {
                    board[column][row] = -1;
                }
            }
        }

        return board;
    }
}
