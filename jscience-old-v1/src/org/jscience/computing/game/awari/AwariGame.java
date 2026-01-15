/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.awari;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GameRuntimeException;

import java.util.Vector;


/**
 * AwariGame implements an ancient African game (for all I know). The board
 * positions are configured as follows:<pre>   13 12 11 10  9  8
 *  0                    7    1  2  3  4  5  6</pre>
 *
 * @author Holger Antelmann
 */
public class AwariGame extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 8345661640705546731L;

    /**
     * DOCUMENT ME!
     */
    int[] board = new int[] { 0, 3, 3, 3, 3, 3, 3, 0, 3, 3, 3, 3, 3, 3 };

    /**
     * DOCUMENT ME!
     */
    int reverseNext = 0;

    /**
     * Creates a new AwariGame object.
     */
    public AwariGame() {
        super("Awari", 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        Vector<GameMove> moves = new Vector<GameMove>(6);

        if (nextPlayer() == 0) {
            for (int i = 1; i < 7; i++) {
                if (board[i] > 0) {
                    moves.add(new AwariMove(0, i, this));
                }
            }
        } else {
            for (int i = 8; i < 14; i++) {
                if (board[i] > 0) {
                    moves.add(new AwariMove(1, i, this));
                }
            }
        }

        return (AwariMove[]) moves.toArray(new AwariMove[moves.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        int p = ((AwariMove) move).getPosition();
        int n = board[p];
        board[p] = 0;

        // distribute the pegs
        for (int i = 1; i < (n + 1); i++) {
            board[(p + i) % 14]++;
        }

        int last = (p + n) % 14;

        switch (last) {
        // if last peg hits home base, player plays again
        case 0:

            if (move.getPlayer() == 1) {
                reverseNext++;
            }

            return true;

        case 7:

            if (move.getPlayer() == 0) {
                reverseNext++;
            }

            return true;
        }

        // if the last position was empty and the opposite peg is
        // not empty, collect all into home base
        if (board[last] == 1) {
            int base = (move.getPlayer() == 0) ? 7 : 0;

            switch (last) {
            case 1:

                if (board[13] > 0) {
                    board[base] += (board[13] + 1);
                    board[1] = 0;
                    board[13] = 0;
                }

                break;

            case 2:

                if (board[12] > 0) {
                    board[base] += (board[12] + 1);
                    board[2] = 0;
                    board[12] = 0;
                }

                break;

            case 3:

                if (board[11] > 0) {
                    board[base] += (board[11] + 1);
                    board[3] = 0;
                    board[11] = 0;
                }

                break;

            case 4:

                if (board[10] > 0) {
                    board[base] += (board[10] + 1);
                    board[4] = 0;
                    board[10] = 0;
                }

                break;

            case 5:

                if (board[9] > 0) {
                    board[base] += (board[9] + 1);
                    board[5] = 0;
                    board[9] = 0;
                }

                break;

            case 6:

                if (board[8] > 0) {
                    board[base] += (board[8] + 1);
                    board[6] = 0;
                    board[8] = 0;
                }

                break;

            case 8:

                if (board[6] > 0) {
                    board[base] += (board[6] + 1);
                    board[8] = 0;
                    board[6] = 0;
                }

                break;

            case 9:

                if (board[5] > 0) {
                    board[base] += (board[5] + 1);
                    board[9] = 0;
                    board[5] = 0;
                }

                break;

            case 10:

                if (board[4] > 0) {
                    board[base] += (board[4] + 1);
                    board[10] = 0;
                    board[4] = 0;
                }

                break;

            case 11:

                if (board[3] > 0) {
                    board[base] += (board[3] + 1);
                    board[11] = 0;
                    board[3] = 0;
                }

                break;

            case 12:

                if (board[2] > 0) {
                    board[base] += (board[2] + 1);
                    board[12] = 0;
                    board[2] = 0;
                }

                break;

            case 13:

                if (board[1] > 0) {
                    board[base] += (board[1] + 1);
                    board[13] = 0;
                    board[1] = 0;
                }

                break;

            default: // nothing

            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        AwariGame mg = ((AwariMove) getLastMove()).game;

        if (mg == null) {
            return false;
        }

        for (int i = 0; i < board.length; i++)
            board[i] = mg.board[i];

        reverseNext = mg.reverseNext;

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        return ((numberOfMoves() + reverseNext) % 2);
    }

    /**
     * if the game is even, all players win
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        if (!gameOver()) {
            return null;
        }

        if (board[0] < board[7]) {
            return new int[] { 0 };
        }

        if (board[0] > board[7]) {
            return new int[] { 1 };
        }

        return new int[] { 0, 1 };
    }

    /**
     * returns the difference in number of pegs once the game is over
     *
     * @param playerRole DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws GameRuntimeException DOCUMENT ME!
     */
    public double getResult(int playerRole) throws GameRuntimeException {
        if (!gameOver()) {
            throw new GameRuntimeException(this, "game not over, yet");
        }

        return (getBaseCount(playerRole) -
        getBaseCount((playerRole == 0) ? 1 : 0));
    }

    /**
     * returns the lead as in difference in number of pegs during the
     * game
     *
     * @param playerRole DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLead(int playerRole) {
        return (getBaseCount(playerRole) -
        getBaseCount((playerRole == 0) ? 1 : 0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBaseCount(int player) {
        switch (player) {
        case 0:
            return board[7];

        case 1:
            return board[0];

        default:
            throw new IllegalArgumentException("not allowed: " + player);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "Awari game board:\n   ";

        for (int i = 13; i > 7; i--) {
            s += (((board[i] > 9) ? " " : "  ") + board[i]);
        }

        s += ("\n" + ((board[0] > 9) ? " " : "  ") + board[0]);
        s += (((board[7] > 9) ? "                   " : "                    ") +
        board[7]);
        s += "\n   ";

        for (int i = 1; i < 7; i++) {
            s += (((board[i] > 9) ? " " : "  ") + board[i]);
        }

        s += "\nnext player: ";
        s += (((nextPlayer() == 0) ? "lower" : "upper") + " row");

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
        AwariGame ng = (AwariGame) super.clone();
        ng.board = new int[14];

        for (int i = 0; i < board.length; i++)
            ng.board[i] = board[i];

        return ng;
    }
}
