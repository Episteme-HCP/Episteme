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

package org.jscience.computing.game.mine;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;


/**
 * An implementation of the game Mine Sweeper
 *
 * @author Holger Antelmann
 *
 * @since 9/14/2002
 */
public class MineSweeper extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 1403256706847543014L;

    /**
     * DOCUMENT ME!
     */
    static final int MAX_WIDTH = 100;

    /**
     * DOCUMENT ME!
     */
    static final int MAX_HEIGHT = 100;

    /**
     * DOCUMENT ME!
     */
    static final int MINE = 9;

    /**
     * DOCUMENT ME!
     */
    int[][] field;

    /**
     * DOCUMENT ME!
     */
    boolean[][] opened;

    /**
     * DOCUMENT ME!
     */
    int numberOfMines;

    /**
     * DOCUMENT ME!
     */
    boolean busted;

    /**
     * DOCUMENT ME!
     */
    int hash;

/**
     * calls <code>this(30, 16, 99, System.currentTimeMillis())</code>
     */
    public MineSweeper() {
        this(30, 16, 99, System.currentTimeMillis());

        //this(10, 10, 20, System.currentTimeMillis());
    }

    /**
     * Creates a new MineSweeper object.
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param numberOfMines DOCUMENT ME!
     * @param randomSeed DOCUMENT ME!
     */
    public MineSweeper(int width, int height, int numberOfMines, long randomSeed) {
        this(width, height, numberOfMines, new Random(randomSeed));
    }

    /**
     * Creates a new MineSweeper object.
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param numberOfMines DOCUMENT ME!
     * @param random DOCUMENT ME!
     */
    public MineSweeper(int width, int height, int numberOfMines, Random random) {
        super("MineSweeper", 1);

        if ((width > MAX_WIDTH) || (height > MAX_HEIGHT)) {
            throw new IllegalArgumentException("field size is too large");
        }

        if ((width < 2) || (height < 2)) {
            throw new IllegalArgumentException("field size is too small");
        }

        if (numberOfMines < 1) {
            throw new IllegalArgumentException("too few mines");
        }

        if (numberOfMines >= ((width * height) - 1)) {
            throw new IllegalArgumentException("too many mines for given field");
        }

        this.numberOfMines = numberOfMines;
        //initialize mines
        field = new int[width][height];

        Vector<Integer> m = new Vector<Integer>(width * height);

        for (int i = 0; i < (width * height); i++) {
            m.add(new Integer(i));
        }

        Collections.shuffle(m, random);

        for (int i = 0; i < numberOfMines; i++) {
            int n = ((Integer) m.get(i)).intValue();
            hash += (n * i);
            field[n % width][n / width] = MINE;
        }

        for (int c = 0; c < field.length; c++) {
            for (int r = 0; r < field[0].length; r++) {
                if (field[c][r] != MINE) {
                    field[c][r] = calculateFieldValue(c, r);
                }
            }
        }

        opened = new boolean[width][height];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        MineSweeper ng = (MineSweeper) super.clone();
        ng.field = new int[field.length][field[0].length];

        for (int c = 0; c < field.length; c++) {
            for (int r = 0; r < field[0].length; r++) {
                ng.field[c][r] = field[c][r];
            }
        }

        ng.opened = new boolean[opened.length][opened[0].length];

        for (int c = 0; c < opened.length; c++) {
            for (int r = 0; r < opened[0].length; r++) {
                ng.opened[c][r] = opened[c][r];
            }
        }

        return ng;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof MineSweeper)) {
            return false;
        }

        MineSweeper g = (MineSweeper) obj;

        if (numberOfMines != g.numberOfMines) {
            return false;
        }

        if (busted != g.busted) {
            return false;
        }

        for (int c = 0; c < opened.length; c++) {
            for (int r = 0; r < opened[0].length; r++) {
                if (g.opened[c][r] != opened[c][r]) {
                    return false;
                }
            }
        }

        for (int c = 0; c < field.length; c++) {
            for (int r = 0; r < field[0].length; r++) {
                if (g.field[c][r] != field[c][r]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return hash;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        if (getWinner() != null) {
            return null;
        }

        Vector<MineMove> moves = new Vector<MineMove>();

        for (int column = 0; column < opened.length; column++) {
            for (int row = 0; row < opened[column].length; row++) {
                if (!opened[column][row]) {
                    moves.add(new MineMove(column, row));
                }
            }
        }

        return moves.toArray(new GameMove[moves.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        MineMove m = (MineMove) move;

        if (opened[m.column][m.row]) {
            return false;
        }

        opened[m.column][m.row] = true;

        if (field[m.column][m.row] == 0) {
            openNeighbor(m.column, m.row);
        }

        if (field[m.column][m.row] == MINE) {
            busted = true;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     */
    private void openNeighbor(int column, int row) {
        for (int c = column - 1; c <= (column + 1); c++) {
            for (int r = row - 1; r <= (row + 1); r++) {
                if ((c < 0) || (r < 0) || (c >= field.length) ||
                        (r >= field[c].length)) {
                    continue;
                }

                if ((c == column) && (r == row)) {
                    continue;
                }

                if (opened[c][r] && (field[c][r] == 0)) {
                    openNeighbor(c, r);
                }

                opened[c][r] = true;
            }
        }
    }

    /**
     * always returns false
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        if (busted) {
            return new int[] {  }; // no winner
        }

        for (int c = 0; c < opened.length; c++) {
            for (int r = 0; r < opened[c].length; r++) {
                if (field[c][r] == MINE) {
                    continue;
                }

                if (!opened[c][r]) {
                    return null;
                }
            }
        }

        return new int[] { 0 };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return field.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return field[0].length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOpen(int column, int row) {
        return (opened[column][row]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int calculateFieldValue(int column, int row) {
        if (field[column][row] == MINE) {
            return MINE;
        }

        int count = 0;

        for (int c = column - 1; c <= (column + 1); c++) {
            for (int r = row - 1; r <= (row + 1); r++) {
                if ((c == column) && (r == row)) {
                    continue;
                }

                if ((c < 0) || (r < 0) || (c >= field.length) ||
                        (r >= field[c].length)) {
                    continue;
                }

                if (field[c][r] == MINE) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "";

        for (int r = 0; r < field[0].length; r++) {
            for (int c = 0; c < field.length; c++) {
                if (opened[c][r]) {
                    s += ((field[c][r] == MINE) ? "*"
                                                : String.valueOf(field[c][r]));
                } else {
                    if (busted) {
                        s += ((field[c][r] == MINE) ? "*" : "?");
                    } else {
                        s += "?";
                    }
                }
            }

            s += "\n";
        }

        return s;
    }
}
