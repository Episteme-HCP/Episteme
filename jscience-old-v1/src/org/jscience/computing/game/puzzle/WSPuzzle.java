/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.*;


/**
 * WSPuzzle implements the 'Wolf & Sheep Puzzle'. This puzzle is inspired by<br>
 * http://www.fitzweb.com/brainteasers/wolves.shtml<br>
 * The solution can be easily found by searching through 6379 game positions.
 *
 * @author Holger Antelmann
 */
public class WSPuzzle extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 6076623982285771157L;

    /**
     * DOCUMENT ME!
     */
    public static final byte WOLF = 4;

    /**
     * DOCUMENT ME!
     */
    public static final byte SHEEP = 5;

    /**
     * DOCUMENT ME!
     */
    byte[][] board;

    /**
     * DOCUMENT ME!
     */
    int wolfs = 0;

    /**
     * DOCUMENT ME!
     */
    int sheeps = 0;

    /**
     * Creates a new WSPuzzle object.
     */
    public WSPuzzle() {
        super("Wolf&Sheep Puzzle", 1);
        board = new byte[5][5]; // initialized with 0;
    }

    /**
     * solves the puzzle and then fires up a GUI to let you examine the
     * solution
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
        WSPuzzle game = new WSPuzzle();
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
     * lists all legal moves. To minimize the possibilities, fist place
     * a wolf, then a sheep, then a wolf, etc. until 5 wolfs and 3 sheeps are
     * on the board (the 7th and the 8th move is placing a wolf).
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        java.util.Vector<Move> v = new java.util.Vector<Move>();

        if ((sheeps < wolfs) && (sheeps < 3)) {
            // try placing a sheep
            for (int c = 0; c < 5; c++) {
                for (int r = 0; r < 5; r++) {
                    if (board[c][r] != 0) {
                        continue;
                    }

                    if (check(c, r, WOLF)) {
                        v.add(new Move(c, r, SHEEP));
                    }
                }
            }
        } else {
            // try placing a wolf
            for (int c = 0; c < 5; c++) {
                for (int r = 0; r < 5; r++) {
                    if (board[c][r] != 0) {
                        continue;
                    }

                    if (check(c, r, SHEEP)) {
                        v.add(new Move(c, r, WOLF));
                    }
                }
            }
        }

        return (Move[]) v.toArray(new Move[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param r DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean check(int c, int r, byte type) {
        int dc;
        int dr;
        dr = 0;

        while ((r - dr) >= 0)

            if (board[c][r - dr++] == type) {
                return false;
            }

        dr = 0;

        while ((r + dr) < 5)

            if (board[c][r + dr++] == type) {
                return false;
            }

        dc = 0;

        while ((c - dc) >= 0)

            if (board[c - dc++][r] == type) {
                return false;
            }

        dr = 0;

        while ((c + dr) < 5)

            if (board[c + dr++][r] == type) {
                return false;
            }

        dc = 0;
        dr = 0;

        while (((c + dc) < 5) && ((r + dr) < 5))

            if (board[c + dc++][r + dr++] == type) {
                return false;
            }

        dc = 0;
        dr = 0;

        while (((c + dc) < 5) && ((r - dr) >= 0))

            if (board[c + dc++][r - dr++] == type) {
                return false;
            }

        dc = 0;
        dr = 0;

        while (((c - dc) >= 0) && ((r + dr) < 5))

            if (board[c - dc++][r + dr++] == type) {
                return false;
            }

        dc = 0;
        dr = 0;

        while (((c - dc) >= 0) && ((r - dr) >= 0))

            if (board[c - dc++][r - dr++] == type) {
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
        board[m.column][m.row] = m.type;

        if (m.type == WOLF) {
            wolfs++;
        } else {
            sheeps++;
        }

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

        if (m.type == WOLF) {
            wolfs--;
        } else {
            sheeps--;
        }

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
        if ((wolfs == 5) && (sheeps == 3)) {
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
        String s = "org.jscience.computing.game.puzzle.WSPuzzle game status:\n";

        for (int c = 0; c < 5; c++) {
            for (int r = 0; r < 5; r++) {
                s += " ";

                switch (board[c][r]) {
                case WOLF:
                    s += "W";

                    break;

                case SHEEP:
                    s += "S";

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
        WSPuzzle ng = (WSPuzzle) super.clone();
        ng.board = new byte[5][5];

        for (int c = 0; c < 5; c++)
            for (int r = 0; r < 5; r++)
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
        static final long serialVersionUID = 4963264104455837504L;

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
        byte type;

        /**
         * Creates a new Move object.
         *
         * @param column DOCUMENT ME!
         * @param row DOCUMENT ME!
         * @param type DOCUMENT ME!
         */
        public Move(int column, int row, byte type) {
            super(0);
            this.column = column;
            this.row = row;
            this.type = type;
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

            if (m.column != column) {
                return false;
            }

            if (m.row != row) {
                return false;
            }

            if (m.type != type) {
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
            String s = (type == WOLF) ? "Wolf to " : "Sheep to ";
            s += (column + "/" + row);

            return s;
        }
    }
}
