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

import org.jscience.computing.game.*;


/**
 * EightQueens implements the problem of placing 8 queens on a chess board
 * so that they don't attack each other. The Solution is found here after
 * searching through 25944 positions without any specific AI (giving an
 * indication about the complexity of the puzzle).
 *
 * @author Holger Antelmann
 */
public class EightQueens extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -3617418950927816050L;

    /**
     * DOCUMENT ME!
     */
    byte[][] board;

    /**
     * DOCUMENT ME!
     */
    int queens = 0;

    /**
     * Creates a new EightQueens object.
     */
    public EightQueens() {
        super("EightQueens Puzzle", 1);
        board = new byte[8][8]; // initialized with 0;
    }

    /**
     * solves the puzzle and fires up a JGameFrame that lets you
     * examine the solution
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        new JGameFrame(new JDefaultGame(searchSolution())).setVisible(true);

        //searchSolution();
    }

    /**
     * tries to solve the puzzle with GameUtils functions
     *
     * @return DOCUMENT ME!
     */
    public static GamePlay searchSolution() {
        EightQueens game = new EightQueens();
        org.jscience.util.Monitor monitor = new org.jscience.util.Monitor();
        GamePlay solution = GameUtils.depthFirstSearch(game, new int[] { 0 },
                10, monitor);

        if (solution == null) {
            System.out.println("no solution found");
        } else {
            System.out.println(solution);
        }

        System.out.println("game moves examined: " + monitor.getNumber());

        return solution;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        java.util.Vector<Move> v = new java.util.Vector<Move>();

        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                if (board[c][r] != 0) {
                    continue;
                }

                if (check(c, r)) {
                    v.add(new Move(c, r));
                }
            }
        }

        return v.toArray(new Move[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean check(int c, int r) {
        int dc;
        int dr;
        dr = 0;

        while ((r - dr) >= 0)

            if (board[c][r - dr++] != 0) {
                return false;
            }

        dr = 0;

        while ((r + dr) < 8)

            if (board[c][r + dr++] != 0) {
                return false;
            }

        dc = 0;

        while ((c - dc) >= 0)

            if (board[c - dc++][r] != 0) {
                return false;
            }

        dr = 0;

        while ((c + dr) < 8)

            if (board[c + dr++][r] != 0) {
                return false;
            }

        dc = 0;
        dr = 0;

        while (((c + dc) < 8) && ((r + dr) < 8))

            if (board[c + dc++][r + dr++] != 0) {
                return false;
            }

        dc = 0;
        dr = 0;

        while (((c + dc) < 8) && ((r - dr) >= 0))

            if (board[c + dc++][r - dr++] != 0) {
                return false;
            }

        dc = 0;
        dr = 0;

        while (((c - dc) >= 0) && ((r + dr) < 8))

            if (board[c - dc++][r + dr++] != 0) {
                return false;
            }

        dc = 0;
        dr = 0;

        while (((c - dc) >= 0) && ((r - dr) >= 0))

            if (board[c - dc++][r - dr++] != 0) {
                return false;
            }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        Move m = (Move) move;
        board[m.column][m.row] = 1;
        queens++;

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        Move m = (Move) getLastMove();
        board[m.column][m.row] = 0;
        queens--;

        return true;
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
        if (queens >= 8) {
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
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "EightQueens game status:\n";

        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                s += " ";

                switch (board[c][r]) {
                case 1:
                    s += "Q";

                    break;

                default:
                    s += "_";

                    break;
                }
            }

            s += "\n";
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        EightQueens ng = (EightQueens) super.clone();
        ng.board = new byte[8][8];

        for (int c = 0; c < 8; c++)
            for (int r = 0; r < 8; r++)
                ng.board[c][r] = board[c][r];

        return ng;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    static class Move extends MoveTemplate {
        /**
         * DOCUMENT ME!
         */
        static final long serialVersionUID = -2070585793199513820L;

        /**
         * DOCUMENT ME!
         */
        int column;

        /**
         * DOCUMENT ME!
         */
        int row;

        /**
         * DOCUMENT ME!
         */
        double heuristic;

        /**
         * Creates a new Move object.
         *
         * @param column DOCUMENT ME!
         * @param row DOCUMENT ME!
         */
        public Move(int column, int row) {
            super(0);
            this.column = column;
            this.row = row;
        }

        /**
         * DOCUMENT ME!
         *
         * @param obj DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (!(obj instanceof Move)) {
                return false;
            }

            Move m = (Move) obj;

            if (m.player != player) {
                return false;
            }

            if (m.column != column) {
                return false;
            }

            if (m.row != row) {
                return false;
            }

            return true;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            String s = "Queen to ";
            s += (column + "/" + row);

            return s;
        }
    }
}
