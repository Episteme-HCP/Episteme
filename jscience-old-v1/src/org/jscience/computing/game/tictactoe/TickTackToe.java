/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.tictactoe;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.MoveTemplate;

import java.util.Vector;


/**
 * This class implements the game TickTackToe. The board positions are
 * represented as follows:<br><pre>0 1 23 4 56 7 8</pre>
 *
 * @author Holger Antelmann
 */
public class TickTackToe extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -5020228607900916039L;

    /** internal board representation */
    int[] board;

    /**
     * Creates a new TickTackToe object.
     */
    public TickTackToe() {
        this("TickTackToe");
    }

    /**
     * Creates a new TickTackToe object.
     *
     * @param name DOCUMENT ME!
     */
    public TickTackToe(String name) {
        // tell AbstractGame that this is a 2-player game
        super("TickTackToe", 2);
        // initiate the game board
        // -1 represents an empty slot
        // 0 corresponds to the first player role (i.e. 0)
        // 1 corresponds to the second player role
        board = new int[9];

        for (int i = 0; i < board.length; i++) {
            board[i] = -1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        // simple: each of the two players always take one turn
        return (numberOfMoves() % 2);
    }

    /**
     * getWinner() is checking for a triple (three-in-a-row). If found,
     * it returns an array with the player role that has the triple. If the
     * board is full (after 9 moves) and no triple is found, an empty array is
     * returned to represent a draw (i.e. no winner). Otherwise, null is
     * returned (when the game is still in progress).
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        int r;
        r = checkTriple(new int[] { 0, 1, 2 });

        if (r != -1) {
            return new int[] { r };
        }

        r = checkTriple(new int[] { 3, 4, 5 });

        if (r != -1) {
            return new int[] { r };
        }

        r = checkTriple(new int[] { 6, 7, 8 });

        if (r != -1) {
            return new int[] { r };
        }

        r = checkTriple(new int[] { 0, 3, 6 });

        if (r != -1) {
            return new int[] { r };
        }

        r = checkTriple(new int[] { 1, 4, 7 });

        if (r != -1) {
            return new int[] { r };
        }

        r = checkTriple(new int[] { 2, 5, 8 });

        if (r != -1) {
            return new int[] { r };
        }

        r = checkTriple(new int[] { 0, 4, 8 });

        if (r != -1) {
            return new int[] { r };
        }

        r = checkTriple(new int[] { 2, 4, 6 });

        if (r != -1) {
            return new int[] { r };
        }

        if (numberOfMoves() > 8) {
            return new int[] {  };
        }

        return null;
    }

    /**
     * called by getWinner()
     *
     * @param triple DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int checkTriple(int[] triple) {
        if (board[triple[0]] == -1) {
            return -1;
        }

        if (board[triple[0]] != board[triple[1]]) {
            return -1;
        }

        if (board[triple[0]] != board[triple[2]]) {
            return -1;
        }

        return board[triple[0]];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        // simply find every spot that is still empty
        if (getWinner() != null) {
            return null;
        }

        int p = nextPlayer();
        Vector<Move> v = new Vector<Move>(9);

        for (int i = 0; i < board.length; i++) {
            if (board[i] == -1) {
                v.add(new Move(p, i));
            }
        }

        return v.toArray(new Move[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        board[((Move) move).getInt()] = move.getPlayer();

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        board[((Move) getLastMove()).getInt()] = -1;

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        // straight forward w/o fancy for/while loops;
        // this domain is simple enough
        String s = "\n";
        s += (" " + getChar(0) + " " + getChar(1) + " " + getChar(2) + "\n");
        s += (" " + getChar(3) + " " + getChar(4) + " " + getChar(5) + "\n");
        s += (" " + getChar(6) + " " + getChar(7) + " " + getChar(8) + "\n");
        s += "\n";

        return s;
    }

    /**
     * used in toString()
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    String getChar(int i) {
        switch (board[i]) {
        case -1:
            return "_";

        case 0:
            return "X";

        case 1:
            return "O";
        }

        throw new Error();
    }

    /**
     * making sure that the internal non-primitive board representation
     * is properly cloned to allow the deep-copy required for spawnChild()
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        TickTackToe ngame = (TickTackToe) super.clone();
        ngame.board = new int[board.length];

        for (int i = 0; i < board.length; i++) {
            ngame.board[i] = board[i];
        }

        return ngame;
    }

    /**
     * It's just some number derived from a portion of the board; not
     * guaranteed to be unique for every game, but guaranteed to always be the
     * same for the same board. (trading off performance vs. effectiveness -
     * not really an issue for TickTackToe, but just to make a point ..)
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int h = (board[2] * 2) + (board[3] * 4) + (board[4] * 8) +
            (board[5] * 16) + (board[6] * 32);

        return h;
    }

    /**
     * required to allow recognizing the same game status even after
     * serialization (to support network games, GameBooks, etc.). A call to
     * super.equals() is not needed here, as we only care about the board
     * status and not the move history or anything else where the AbstractGame
     * may differ.
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof TickTackToe)) {
            return false;
        }

        TickTackToe o = (TickTackToe) obj;

        for (int i = 0; i < board.length; i++) {
            if (board[i] != o.board[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Nested class that implements a GameMove for TickTackToe.
     *
     * @see TickTackToe
     */
    static class Move extends MoveTemplate {
        /**
         * DOCUMENT ME!
         */
        static final long serialVersionUID = 5576041588043089925L;

        /**
         * DOCUMENT ME!
         */
        int position;

/**
         * be sure not to mess up the order: first player, then position!
         */
        public Move(int player, int position) {
            super(player);
            this.position = position;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getInt() {
            return position;
        }

        /**
         * provide a String that is useful for display
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            String s = null;

            switch (player) {
            case 0:
                s = "X: ";

                break;

            case 1:
                s = "O: ";

                break;
            }

            s += position;

            return s;
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

            if (((Move) obj).position != position) {
                return false;
            }

            return super.equals(obj);
        }
    }
}
