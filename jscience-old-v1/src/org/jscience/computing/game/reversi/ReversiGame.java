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

package org.jscience.computing.game.reversi;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameBoardMove;
import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GameRuntimeException;

import java.util.Vector;


/**
 * also known as Othello game
 *
 * @author Holger Antelmann
 */
public class ReversiGame extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 9120341763056326196L;

    /**
     * DOCUMENT ME!
     */
    int[][] board;

    /**
     * DOCUMENT ME!
     */
    public final int boardWidth = 8;

    /**
     * DOCUMENT ME!
     */
    public final int boardHeight = 8;

    /**
     * Creates a new ReversiGame object.
     */
    public ReversiGame() {
        this("Reversi game");
    }

    /**
     * Creates a new ReversiGame object.
     *
     * @param name DOCUMENT ME!
     */
    public ReversiGame(String name) {
        super(name, 2);
        board = new int[boardWidth][boardHeight];

        for (int column = 0; column < boardWidth; column++) {
            for (int row = 0; row < boardHeight; row++) {
                if (((column == 3) && (row == 3)) ||
                        ((column == 4) && (row == 4))) {
                    board[column][row] = 0;
                } else if (((column == 4) && (row == 3)) ||
                        ((column == 3) && (row == 4))) {
                    board[column][row] = 1;
                } else {
                    board[column][row] = -1;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        if (!gameOver()) {
            return null;
        }

        if (getCount(0) < getCount(1)) {
            return (new int[] { 1 });
        }

        if (getCount(0) > getCount(1)) {
            return (new int[] { 0 });
        }

        return (new int[] { 0, 1 }); //draw
    }

    /**
     * DOCUMENT ME!
     *
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCount(int player) {
        int count = 0;

        for (int column = 0; column < boardWidth; column++) {
            for (int row = 0; row < boardHeight; row++) {
                if (board[column][row] == player) {
                    count++;
                }
            }
        }

        return (count);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        if (getLegalMoves(0).length > 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GameMove[] listLegalMoves() {
        Vector<ReversiMove> v = new Vector<ReversiMove>();
        GameBoardMove move = null;
        ReversiPosition pos = null;
        int player;

        try {
            player = (getLastPlayer() == 0) ? 1 : 0;
        } catch (GameRuntimeException e) {
            player = 0;
        }

        for (int row = 0; row < boardHeight; row++) {
            for (int column = 0; column < boardWidth; column++) {
                if ((board[column][row] == -1) &&
                        (possibleMove(column + 1, row + 1, player))) {
                    v.add(new ReversiMove(player,
                            new ReversiPosition(column + 1, row + 1), board));
                }
            }
        }

        if (v.size() > 0) {
            return (GameBoardMove[]) v.toArray(new GameBoardMove[v.size()]);
        }

        player = (player == 0) ? 1 : 0;

        for (int row = 0; row < boardHeight; row++) {
            for (int column = 0; column < boardWidth; column++) {
                if ((board[column][row] == -1) &&
                        (possibleMove(column + 1, row + 1, player))) {
                    v.add(new ReversiMove(player,
                            new ReversiPosition(column + 1, row + 1), board));
                }
            }
        }

        return v.toArray(new ReversiMove[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        ReversiPosition pos = (ReversiPosition) ((ReversiMove) move).getPosition();
        int player = move.getPlayer();
        int c;
        int r;
        //up
        c = pos.getColumn();
        r = pos.getRow() - 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c, r - 1) &&
                    (board[c - 1][r - 1] != -1)) {
                r--;

                if (board[c - 1][r - 1] == player) {
                    while (r != pos.getRow()) {
                        r++;
                        board[c - 1][r - 1] = player;
                    }

                    break;
                }
            }
        }

        //down
        c = pos.getColumn();
        r = pos.getRow() + 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c, r + 1) &&
                    (board[c - 1][r - 1] != -1)) {
                r++;

                if (board[c - 1][r - 1] == player) {
                    while (r != pos.getRow()) {
                        r--;
                        board[c - 1][r - 1] = player;
                    }

                    break;
                }
            }
        }

        //left
        c = pos.getColumn() - 1;
        r = pos.getRow();

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c - 1, r) &&
                    (board[c - 1][r - 1] != -1)) {
                c--;

                if (board[c - 1][r - 1] == player) {
                    while (c != pos.getColumn()) {
                        c++;
                        board[c - 1][r - 1] = player;
                    }

                    break;
                }
            }
        }

        //right
        c = pos.getColumn() + 1;
        r = pos.getRow();

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c + 1, r) &&
                    (board[c - 1][r - 1] != -1)) {
                c++;

                if (board[c - 1][r - 1] == player) {
                    while (c != pos.getColumn()) {
                        c--;
                        board[c - 1][r - 1] = player;
                    }

                    break;
                }
            }
        }

        //up left
        c = pos.getColumn() - 1;
        r = pos.getRow() - 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c - 1, r - 1) &&
                    (board[c - 1][r - 1] != -1)) {
                c--;
                r--;

                if (board[c - 1][r - 1] == player) {
                    while ((c != pos.getColumn()) && (r != pos.getRow())) {
                        c++;
                        r++;
                        board[c - 1][r - 1] = player;
                    }

                    break;
                }
            }
        }

        //down right
        c = pos.getColumn() + 1;
        r = pos.getRow() + 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c + 1, r + 1) &&
                    (board[c - 1][r - 1] != -1)) {
                c++;
                r++;

                if (board[c - 1][r - 1] == player) {
                    while ((c != pos.getColumn()) && (r != pos.getRow())) {
                        c--;
                        r--;
                        board[c - 1][r - 1] = player;
                    }

                    break;
                }
            }
        }

        //down left
        c = pos.getColumn() - 1;
        r = pos.getRow() + 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c - 1, r + 1) &&
                    (board[c - 1][r - 1] != -1)) {
                c--;
                r++;

                if (board[c - 1][r - 1] == player) {
                    while ((c != pos.getColumn()) && (r != pos.getRow())) {
                        c++;
                        r--;
                        board[c - 1][r - 1] = player;
                    }

                    break;
                }
            }
        }

        //up right
        c = pos.getColumn() + 1;
        r = pos.getRow() - 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c + 1, r - 1) &&
                    (board[c - 1][r - 1] != -1)) {
                c++;
                r--;

                if (board[c - 1][r - 1] == player) {
                    while ((c != pos.getColumn()) && (r != pos.getRow())) {
                        c--;
                        r++;
                        board[c - 1][r - 1] = player;
                    }

                    break;
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
    protected boolean popMove() {
        int[][] b = ((ReversiMove) getLastMove()).getBoard();

        if (b == null) {
            return false;
        }

        // checking correctness of the board - probably not necessary
        if (b.length != boardWidth) {
            return false;
        }

        for (int i = 0; i < boardWidth; i++) {
            if (b[i].length != boardWidth) {
                return false;
            }

            for (int j = 0; j < boardHeight; j++) {
                if ((b[i][j] < -1) || (b[i][j] > 1)) {
                    return false;
                }
            }
        }

        // end correctness checking
        for (int column = 0; column < boardWidth; column++) {
            for (int row = 0; row < boardHeight; row++) {
                board[column][row] = b[column][row];
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean possibleMove(int column, int row, int player) {
        int c;
        int r;
        //up
        c = column;
        r = row - 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c, r - 1)) {
                r--;

                if (board[c - 1][r - 1] == -1) {
                    break;
                }

                if (board[c - 1][r - 1] == player) {
                    return true;
                }
            }
        }

        //down
        c = column;
        r = row + 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c, r + 1)) {
                r++;

                if (board[c - 1][r - 1] == -1) {
                    break;
                }

                if (board[c - 1][r - 1] == player) {
                    return true;
                }
            }
        }

        //left
        c = column - 1;
        r = row;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c - 1, r)) {
                c--;

                if (board[c - 1][r - 1] == -1) {
                    break;
                }

                if (board[c - 1][r - 1] == player) {
                    return true;
                }
            }
        }

        //right
        c = column + 1;
        r = row;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c + 1, r)) {
                c++;

                if (board[c - 1][r - 1] == -1) {
                    break;
                }

                if (board[c - 1][r - 1] == player) {
                    return true;
                }
            }
        }

        //up left
        c = column - 1;
        r = row - 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c - 1, r - 1)) {
                c--;
                r--;

                if (board[c - 1][r - 1] == -1) {
                    break;
                }

                if (board[c - 1][r - 1] == player) {
                    return true;
                }
            }
        }

        //down right
        c = column + 1;
        r = row + 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c + 1, r + 1)) {
                c++;
                r++;

                if (board[c - 1][r - 1] == -1) {
                    break;
                }

                if (board[c - 1][r - 1] == player) {
                    return true;
                }
            }
        }

        //up right
        c = column - 1;
        r = row + 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c - 1, r + 1)) {
                c--;
                r++;

                if (board[c - 1][r - 1] == -1) {
                    break;
                }

                if (board[c - 1][r - 1] == player) {
                    return true;
                }
            }
        }

        //down left
        c = column + 1;
        r = row - 1;

        if (ReversiPosition.isValidPosition(c, r) &&
                (board[c - 1][r - 1] == ((player == 0) ? 1 : 0))) {
            while (ReversiPosition.isValidPosition(c + 1, r - 1)) {
                c++;
                r--;

                if (board[c - 1][r - 1] == -1) {
                    break;
                }

                if (board[c - 1][r - 1] == player) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        ReversiGame newGame = (ReversiGame) super.clone();
        newGame.board = new int[boardWidth][boardHeight];

        for (int column = 0; column < boardWidth; column++) {
            for (int row = 0; row < boardHeight; row++) {
                newGame.board[column][row] = board[column][row];
            }
        }

        return newGame;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "\n";

        for (int row = boardHeight - 1; row >= 0; row--) {
            for (int column = 0; column < boardWidth; column++) {
                s += " ";

                switch (board[column][row]) {
                case -1:
                    s += "_";

                    break;

                case 0:
                    s += "X";

                    break;

                case 1:
                    s += "O";

                    break;
                }
            }

            s += "\n";
        }

        s += "\n";

        return s;
    }

    /**
     * experimental
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int h = 0;
        h = h + (board[4][4] * 7);
        h = h + (board[4][5] * 11);
        h = h + (board[5][4] * 13);
        h = h + (board[5][5] * 17);
        h = h + (board[6][4] * 23);
        h = h + (board[6][5] * 31);

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
        if (!(obj instanceof ReversiGame)) {
            return false;
        }

        ReversiGame o = (ReversiGame) obj;

        for (int column = 0; column < boardWidth; column++) {
            for (int row = 0; row < boardHeight; row++) {
                if (o.board[column][row] != board[column][row]) {
                    return false;
                }
            }
        }

        return true;
    }
}
