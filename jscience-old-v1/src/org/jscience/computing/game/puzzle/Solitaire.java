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

package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GameRuntimeException;

import java.util.Vector;


/**
 * A one-player game where the goal is to remove all 'peggs' on the board
 * by jumping over them one by one.
 *
 * @author Holger Antelmann
 */
public class Solitaire extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 5625847923326671763L;

    /**
     * DOCUMENT ME!
     */
    private int[][] board;

/**
     * uses SolitaireSamples.getSolitiare() to initialize the game
     *
     * @see SolitaireSamples#getSolitaire()
     */
    public Solitaire() {
        this("standard Solitaire", SolitaireSamples.getSolitaire());
    }

/**
     * Instead of building the board array yourself, you can
     * take advantage of the static methods of the class
     * SolitaireSamples, which provides convenient standard
     * configurations; if you build a configuration yourself,
     * the convention for int[][] board is as follows.
     * <li>array must be of size [8][8]</li>
     * <li>0 values in the array are wasted; for the game the values
     * considered are only the array values 1 to 7
     * to make it more 'human-readable'</li>
     * <li>every position outside the Solitaire playing field
     * (here including the 0 value border arrays) must be other than
     * 0 or 1</li>
     * <li>every Solitaire game position is either set to 0 or 1,
     * where 0 is an empty tile and 1 is a peg</li>
     * The String name is just used as an identification
     *
     * @see SolitaireSamples
     */
    public Solitaire(String name, int[][] board) {
        super(name, 1);

        // checking validity of the given board
        if ((board.length != 8) || (board[0].length != 8)) {
            throw new IllegalArgumentException("not a valid Solitaire board");
        }

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                if (!SolitairePosition.isValidPosition(column, row)) {
                    if ((board[column][row] == 0) || (board[column][row] == 1)) {
                        throw new IllegalArgumentException(
                            "not a valid Solitaire board");
                    }
                } else if ((board[column][row] != 0) &&
                        (board[column][row] != 1)) {
                    throw new IllegalArgumentException(
                        "not a valid Solitaire board");
                }
            }
        }

        // initializing this.board
        this.board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                this.board[column][row] = board[column][row];
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        if (isSolved()) {
            return new int[] { 0 };
        }

        if (getLegalMoves().length == 0) {
            return new int[] { -1 };
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param playerRole DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getResult(int playerRole) {
        if (!gameOver()) {
            throw new GameRuntimeException(this, "game not over yet");
        }

        return (1 - pegsLeft());
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
    protected GameMove[] listLegalMoves() {
        Vector<SolitaireMove> v = new Vector<SolitaireMove>();

        for (int column = 1; column < 8; column++) {
            for (int row = 1; row < 8; row++) {
                if (board[column][row] == 1) {
                    try {
                        if ((board[column + 1][row] == 1) &&
                                (board[column + 2][row] == 0)) {
                            v.add(new SolitaireMove(0,
                                    new SolitairePosition(column, row),
                                    new SolitairePosition(column + 2, row)));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try {
                        if ((board[column - 1][row] == 1) &&
                                (board[column - 2][row] == 0)) {
                            v.add(new SolitaireMove(0,
                                    new SolitairePosition(column, row),
                                    new SolitairePosition(column - 2, row)));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try {
                        if ((board[column][row + 1] == 1) &&
                                (board[column][row + 2] == 0)) {
                            v.add(new SolitaireMove(0,
                                    new SolitairePosition(column, row),
                                    new SolitairePosition(column, row + 2)));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try {
                        if ((board[column][row - 1] == 1) &&
                                (board[column][row - 2] == 0)) {
                            v.add(new SolitaireMove(0,
                                    new SolitairePosition(column, row),
                                    new SolitairePosition(column, row - 2)));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        return v.toArray(new SolitaireMove[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        int c1 = ((SolitairePosition) ((SolitaireMove) move).getOldPosition()).getColumn();
        int r1 = ((SolitairePosition) ((SolitaireMove) move).getOldPosition()).getRow();
        int c2 = ((SolitairePosition) ((SolitaireMove) move).getNewPosition()).getColumn();
        int r2 = ((SolitairePosition) ((SolitaireMove) move).getNewPosition()).getRow();
        board[c1][r1] = 0;
        board[c2][r2] = 1;

        int middlec = c1 + ((c2 - c1) / 2);
        int middler = r1 + ((r2 - r1) / 2);
        board[middlec][middler] = 0;

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        SolitaireMove move = (SolitaireMove) getLastMove();
        int c1 = ((SolitairePosition) move.getOldPosition()).getColumn();
        int r1 = ((SolitairePosition) move.getOldPosition()).getRow();
        int c2 = ((SolitairePosition) move.getNewPosition()).getColumn();
        int r2 = ((SolitairePosition) move.getNewPosition()).getRow();
        board[c1][r1] = 1;
        board[c2][r2] = 0;

        int middlec = c1 + ((c2 - c1) / 2);
        int middler = r1 + ((r2 - r1) / 2);
        board[middlec][middler] = 1;

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSolved() {
        int pegs = 0;

        for (int column = 1; column < 8; column++) {
            for (int row = 1; row < 8; row++) {
                if (board[column][row] == 1) {
                    pegs++;
                }

                if (pegs > 1) {
                    return false;
                }
            }
        }

        if (pegs == 1) {
            return true;
        }

        throw new Error();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int pegsLeft() {
        int pegs = 0;

        for (int column = 1; column < 8; column++) {
            for (int row = 1; row < 8; row++) {
                if (board[column][row] == 1) {
                    pegs++;
                }
            }
        }

        return pegs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getValueAt(SolitairePosition pos) {
        return getValueAt(pos.getColumn(), pos.getRow());
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public int getValueAt(int column, int row)
        throws ArrayIndexOutOfBoundsException {
        //if (!SolitairePosition.isValidPosition(row, column)) {
        //    String s = "given values do not constitute a valid SolitairePosition";
        //    s += "; values given: column: " + column + ", row: " + row;
        //    throw new GameRuntimeException(this, s);
        //}
        return board[column][row];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        Solitaire newsol = (Solitaire) super.clone();
        newsol.board = new int[8][8];

        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                newsol.board[column][row] = board[column][row];
            }
        }

        return newsol;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "\n";

        for (int row = 1; row < 8; row++) {
            for (int column = 1; column < 8; column++) {
                switch (board[column][row]) {
                case -1:
                    s += "  ";

                    break;

                case 0:
                    s += " _";

                    break;

                case 1:
                    s += " *";

                    break;

                default:

                    String ex = "the game matrix wasn't correctly maintained";
                    throw new Error(ex);
                }
            }

            s += "\n";
        }

        return s;
    }

    /**
     * still experimental at this point
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int h = 0;
        h = h + (board[3][3] * 3);
        h = h + (board[3][4] * 7);
        h = h + (board[3][5] * 11);
        h = h + (board[4][3] * 17);
        h = h + (board[4][4] * 23);
        h = h + (board[4][5] * 29);
        h = h + (board[5][3] * 37);
        h = h + (board[5][4] * 43);
        h = h + (board[5][5] * 53);

        return h;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Solitaire)) {
            return false;
        }

        Solitaire o = (Solitaire) obj;

        for (int column = 1; column < 8; column++) {
            for (int row = 1; row < 8; row++) {
                if (board[column][row] != o.board[column][row]) {
                    return false;
                }
            }
        }

        return true;
    }
}
