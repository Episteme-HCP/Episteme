/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.gomoku;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.MoveTemplate;

import java.util.Vector;


/**
 * an implementation of the game Go-moku
 *
 * @author Holger Antelmann
 */
public class GomokuGame extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -2678202549971261236L;

    /**
     * DOCUMENT ME!
     */
    public static final int EMPTY = -1;

    /**
     * DOCUMENT ME!
     */
    static final int WINNING_LINE_LENGTH = 5;

    /**
     * DOCUMENT ME!
     */
    int[][] board;

    /**
     * DOCUMENT ME!
     */
    final int width;

    /**
     * DOCUMENT ME!
     */
    final int height;

    /**
     * Creates a new GomokuGame object.
     */
    public GomokuGame() {
        this("Go-moku", 15, 15);
    }

    /**
     * Creates a new GomokuGame object.
     *
     * @param name DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public GomokuGame(String name, int width, int height) {
        super(name, 2);
        this.width = width;
        this.height = height;
        board = new int[width][height];

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                board[c][r] = EMPTY;
            }
        }
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

        Vector<Move> moves = new Vector<Move>();
        int next = nextPlayer();

        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                if (board[c][r] == EMPTY) {
                    moves.add(new Move(next, c, r));
                }
            }
        }

        return moves.toArray(new Move[moves.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        board[((Move) move).column][((Move) move).row] = move.getPlayer();

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        Move move = (Move) getLastMove();
        board[move.column][move.row] = EMPTY;

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        return (numberOfMoves() % 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        int win = -1;

        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                if (board[column][row] != -1) {
                    win = checkPositionWin(column, row);

                    if (win != -1) {
                        return (new int[] { win });
                    }
                }
            }
        }

        if (numberOfMoves() == (height * width)) {
            return new int[] {  };
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int checkPositionWin(int column, int row) {
        int line;
        int delta;
        //checking right
        line = 1;
        delta = 1;

        while ((WINNING_LINE_LENGTH > delta) && ((column + delta) < width)) {
            if (board[column][row] != board[column + delta][row]) {
                break;
            }

            line++;
            delta++;
        }

        if (line == WINNING_LINE_LENGTH) {
            return (board[column][row]);
        }

        //checking up
        line = 1;
        delta = 1;

        while ((WINNING_LINE_LENGTH > delta) && ((row + delta) < height)) {
            if (board[column][row] != board[column][row + delta]) {
                break;
            }

            line++;
            delta++;
        }

        if (line == WINNING_LINE_LENGTH) {
            return (board[column][row]);
        }

        //checking diagonal up
        line = 1;
        delta = 1;

        while ((WINNING_LINE_LENGTH > delta) && ((column + delta) < width) &&
                ((row + delta) < height)) {
            if (board[column][row] != board[column + delta][row + delta]) {
                break;
            }

            line++;
            delta++;
        }

        if (line == WINNING_LINE_LENGTH) {
            return (board[column][row]);
        }

        //checking diagonal down
        line = 1;
        delta = 1;

        while ((WINNING_LINE_LENGTH > delta) && ((column + delta) < width) &&
                ((row - delta) >= 0)) {
            if (board[column][row] != board[column + delta][row - delta]) {
                break;
            }

            line++;
            delta++;
        }

        if (line == WINNING_LINE_LENGTH) {
            return (board[column][row]);
        }

        return (-1);
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
        return board[column][row];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "Go-moku gameboard:\n";

        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                switch (board[column][row]) {
                case EMPTY:
                    s += " _";

                    break;

                case 0:
                    s += " X";

                    break;

                case 1:
                    s += " O";

                    break;

                default:
                    throw new Error();
                }
            }

            s += "\n";
        }

        s += "\n";

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        try {
            GomokuGame ngame = (GomokuGame) super.clone();
            ngame.board = new int[width][height];

            for (int c = 0; c < width; c++) {
                for (int r = 0; r < height; r++) {
                    ngame.board[c][r] = board[c][r];
                }
            }

            return ngame;
        } catch (CloneNotSupportedException e) {
            throw new Error();
        }
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
        static final long serialVersionUID = 6800875087778931804L;

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
         * @param player DOCUMENT ME!
         * @param column DOCUMENT ME!
         * @param row DOCUMENT ME!
         */
        public Move(int player, int column, int row) {
            super(player);
            this.column = column;
            this.row = row;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getRow() {
            return row;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getColumn() {
            return column;
        }

        /**
         * DOCUMENT ME!
         *
         * @param obj DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean equals(Object obj) {
            if (!(obj instanceof Move)) {
                return false;
            }

            Move m = (Move) obj;

            if (m.player != player) {
                return false;
            }

            if (m.row != row) {
                return false;
            }

            if (m.column != column) {
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
            return ("player " + player + " to " + (column + 1) + "/" +
            (row + 1));
        }
    }
}
